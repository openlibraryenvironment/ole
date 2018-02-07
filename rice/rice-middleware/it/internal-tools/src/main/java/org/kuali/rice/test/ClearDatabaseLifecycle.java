/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.test;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lifecycle class to clean up the database for use in testing.
 * This lifecycle will not be run (even if it is listed in the lifecycles list)
 * if the 'use.use.clearDatabaseLifecycle' configuration property is defined, and is
 * not 'true'.  If the property is omitted the lifecycle runs as normal.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 *
 */
public class ClearDatabaseLifecycle extends BaseLifecycle {

    protected static final Logger LOG = Logger.getLogger(ClearDatabaseLifecycle.class);

    private List<String> tablesToClear = new ArrayList<String>();
    private List<String> tablesNotToClear = new ArrayList<String>();

    public ClearDatabaseLifecycle() {
        addStandardTables();
    }

    public ClearDatabaseLifecycle(List<String> tablesToClear, List<String> tablesNotToClear) {
        this.tablesToClear = tablesToClear;
        this.tablesNotToClear = tablesNotToClear;
        addStandardTables();
    }

    protected void addStandardTables() {
        tablesNotToClear.add("BIN.*");
        tablesNotToClear.add(".*_S");
    }

    public static final String TEST_TABLE_NAME = "EN_UNITTEST_T";

    public void start() throws Exception {
        String useClearDatabaseLifecycle = ConfigContext.getCurrentContextConfig().getProperty("use.clearDatabaseLifecycle");

        if (useClearDatabaseLifecycle != null && !Boolean.valueOf(useClearDatabaseLifecycle)) {
            LOG.debug("Skipping ClearDatabaseLifecycle due to property: use.clearDatabaseLifecycle=" + useClearDatabaseLifecycle);
            return;
        }

        final DataSource dataSource = TestHarnessServiceLocator.getDataSource();
        clearTables(TestHarnessServiceLocator.getJtaTransactionManager(), dataSource);
        super.start();
    }

    protected Boolean isTestTableInSchema(final Connection connection) throws SQLException {
        Assert.assertNotNull("Connection could not be located.", connection);
        ResultSet resultSet = null;
        try {
            resultSet = connection.getMetaData().getTables(null, connection.getMetaData().getUserName().toUpperCase(), TEST_TABLE_NAME, null);
            return new Boolean(resultSet.next());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    protected void verifyTestEnvironment(final DataSource dataSource) {
        new JdbcTemplate(dataSource).execute(new ConnectionCallback<Object>() {
            public Object doInConnection(final Connection connection) throws SQLException {
                String dbUrl = connection.getMetaData().getURL();
                Assert.assertTrue("No table named '" + TEST_TABLE_NAME + "' was found in the configured database.  " +
                        dbUrl + "  You are attempting to run tests against a non-test database!!!", isTestTableInSchema(connection));
                return null;
            }
        });
    }

    protected void clearTables(final PlatformTransactionManager transactionManager, final DataSource dataSource) {
        Assert.assertNotNull("DataSource could not be located.", dataSource);
        try {
            StopWatch s = new StopWatch();
            s.start();
            new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
                public Object doInTransaction(final TransactionStatus status) {
                    verifyTestEnvironment(dataSource);
                    return new JdbcTemplate(dataSource).execute(new StatementCallback() {
                        public Object doInStatement(Statement statement) throws SQLException {
                            String schemaName = statement.getConnection().getMetaData().getUserName().toUpperCase();
                            LOG.info("Clearing tables for schema " + schemaName);
                            if (StringUtils.isBlank(schemaName)) {
                                Assert.fail("Empty schema name given");
                            }
                            final List<String> reEnableConstraints = new ArrayList<String>();
                            DatabaseMetaData metaData = statement.getConnection().getMetaData();
                            Map<String, List<String[]>> exportedKeys = indexExportedKeys(metaData, schemaName);
                            final ResultSet resultSet = metaData.getTables(null, schemaName, null, new String[] { "TABLE" });
                            final StringBuilder logStatements = new StringBuilder();
                            while (resultSet.next()) {
                                String tableName = resultSet.getString("TABLE_NAME");
                                if (shouldTableBeCleared(tableName)) {
                                    if (!isUsingDerby(metaData) && isUsingOracle(metaData)) {
                                    	List<String[]> exportedKeyNames = exportedKeys.get(tableName);
                                    	if (exportedKeyNames != null) {
                                    		for (String[] exportedKeyName : exportedKeyNames) {
                                    			final String fkName = exportedKeyName[0];
                                    			final String fkTableName = exportedKeyName[1];
                                    			final String disableConstraint = "ALTER TABLE " + fkTableName + " DISABLE CONSTRAINT " + fkName;
                                    			logStatements.append("Disabling constraints using statement ->" + disableConstraint + "<-\n");
                                    			statement.addBatch(disableConstraint);
                                    			reEnableConstraints.add("ALTER TABLE " + fkTableName + " ENABLE CONSTRAINT " + fkName);
                                    		}
                                    	}
                                    } else if (isUsingMySQL(metaData)) {
                                    	statement.addBatch("SET FOREIGN_KEY_CHECKS = 0");
                                    }
                                    String deleteStatement = "DELETE FROM " + tableName;
                                    logStatements.append("Clearing contents using statement ->" + deleteStatement + "<-\n");
                                    statement.addBatch(deleteStatement);
                                }
                            }
                            for (final String constraint : reEnableConstraints) {
                                logStatements.append("Enabling constraints using statement ->" + constraint + "<-\n");
                                statement.addBatch(constraint);
                            }
                            if (isUsingMySQL(metaData)) {
                            	statement.addBatch("SET FOREIGN_KEY_CHECKS = 1");
                            }
                            LOG.info(logStatements);
                            
                            int[] results = statement.executeBatch();
                            for (int index = 0; index < results.length; index++) {
                            	if (results[index] == Statement.EXECUTE_FAILED) {
                    				Assert.fail("Execution of database clear statement failed.");
                            	}
                            	
                            }
                            resultSet.close();
                            LOG.info("Tables successfully cleared for schema " + schemaName);
                            return null;
                        }
                    });
                }
            });
            s.stop();
            LOG.info("Time to clear tables: " + DurationFormatUtils.formatDurationHMS(s.getTime()));
        } catch (Exception e) {
            LOG.error(e);
            throw new RuntimeException(e);
        }
    }
    
    protected Map<String, List<String[]>> indexExportedKeys(DatabaseMetaData metaData, String schemaName) throws SQLException {
    	Map<String, List<String[]>> exportedKeys = new HashMap<String, List<String[]>>();
        if (!isUsingDerby(metaData) && isUsingOracle(metaData)) {
        	ResultSet keyResultSet = metaData.getExportedKeys(null, schemaName, null);
        	while (keyResultSet.next()) {
        		String tableName = keyResultSet.getString("PKTABLE_NAME");
        		if (shouldTableBeCleared(tableName)) {
        			List<String[]> exportedKeyNames = exportedKeys.get(tableName);
        			if (exportedKeyNames == null) {
        				exportedKeyNames = new ArrayList<String[]>();
        				exportedKeys.put(tableName, exportedKeyNames);
        			}
        			final String fkName = keyResultSet.getString("FK_NAME");
        			final String fkTableName = keyResultSet.getString("FKTABLE_NAME");
        			exportedKeyNames.add(new String[] { fkName, fkTableName });
        		}
        	}
        	keyResultSet.close();
        }
        return exportedKeys;        
    }

    private boolean shouldTableBeCleared(String tableName) {
        if (getTablesNotToClear() != null && !getTablesNotToClear().isEmpty()) {
            for (String tableNotToClear : getTablesNotToClear()) {
                if (tableName.toUpperCase().matches(tableNotToClear.toUpperCase())) {
                    return false;
                }
            }
        }
        if (getTablesToClear() != null && !getTablesToClear().isEmpty()) {
            for (String tableToClear : getTablesToClear()) {
                if (tableName.toUpperCase().matches(tableToClear.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
        
        return true;
    }

    private boolean isUsingDerby(DatabaseMetaData metaData) throws SQLException {
        return metaData.getDriverName().toLowerCase().contains("derby");
    }

    private boolean isUsingOracle(DatabaseMetaData metaData) throws SQLException {
        return metaData.getDriverName().toLowerCase().contains("oracle");
    }

    private boolean isUsingMySQL(DatabaseMetaData metaData) throws SQLException {
        return metaData.getDriverName().toLowerCase().contains("mysql");
    }

    
    public List<String> getTablesToClear() {
        return this.tablesToClear;
    }

    public List<String> getTablesNotToClear() {
        return this.tablesNotToClear;
    }
}
