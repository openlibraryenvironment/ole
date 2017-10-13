/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.devtools.pdle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.krad.devtools.pdle.PostDataLoadEncryptionDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;

public class PostDataLoadEncryptionDaoJdbc extends PlatformAwareDaoBaseJdbc implements PostDataLoadEncryptionDao {
    private static final Logger LOG = Logger.getLogger(PostDataLoadEncryptionDaoJdbc.class);

    protected static final String AND_SEPARATOR = " AND ";
    protected static final String COMMA_SEPARATOR = ", ";
    protected static final String BACKUP_TABLE_EXTENSION = "_bak";
    protected static final String BACKUP_TABLE_ENCRYPT_IND = "ENCRYPT_IND";
    
    void executeSql(String sql) {
    	LOG.info("Executing sql: " + sql);
    	getJdbcTemplate().execute(sql);
    }

    public void createBackupTable(String tableName) {
    	executeSql(getDbPlatform().getCreateTableFromTableSql(tableName + "_bak", tableName));
    }

    public void truncateTable(String tableName) {
    	executeSql(getDbPlatform().getTruncateTableSql(tableName));
    }

    public void restoreTableFromBackup(String tableName) {
    	truncateTable(tableName);
    	executeSql(getDbPlatform().getInsertDataFromTableSql(tableName, tableName + "_bak"));
    }

    public void dropBackupTable(String tableName) {
    	executeSql(getDbPlatform().getDropTableSql(tableName + "_bak"));
    }
    
    public boolean doesBackupTableExist(String tableName){
        try{
            Object tableNameObj = getJdbcTemplate().queryForObject(
                "SELECT count(*) FROM " + tableName + BACKUP_TABLE_EXTENSION + " WHERE 0=1", Integer.class);
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    public void addEncryptionIndicatorToBackupTable(String tableName){
        executeSql(new StringBuffer().append("ALTER TABLE ")
                .append(tableName + BACKUP_TABLE_EXTENSION)
                .append(" ADD ")
                .append(BACKUP_TABLE_ENCRYPT_IND)
                .append(" VARCHAR2(1)").toString());
    }

    public void dropEncryptionIndicatorFromBackupTable(String tableName){
        executeSql(new StringBuffer().append("ALTER TABLE ")
                .append(tableName + BACKUP_TABLE_EXTENSION)
                .append(" DROP COLUMN ")
                .append(BACKUP_TABLE_ENCRYPT_IND).toString());
    }
    
    public List<Map<String, String>> retrieveUnencryptedColumnValuesFromBackupTable(String tableName, final List<String> columnNames, int numberOfRowsToCommitAfter) {
        return getJdbcTemplate().query(
                getSelectBackupTableColumnsSql(tableName, columnNames, numberOfRowsToCommitAfter),
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, String> columnValuesMap = new HashMap<String, String>();
                        for(String columnName: columnNames){
                            columnValuesMap.put(columnName, rs.getString(columnName));
                        }
                        return columnValuesMap;
                    }
                });
    }
    
    public void updateColumnValuesInBackupTable(
            String tableName, Map<String, List<String>> columnNameOldNewValuesMap) {
        if(columnNameOldNewValuesMap==null || columnNameOldNewValuesMap.size()<1)
            return;
        executeSql(getUpdateBackupTableColumnsSql(tableName, columnNameOldNewValuesMap));
    }

    public String getUpdateBackupTableColumnsSql(
            String tableName, Map<String, List<String>> columnNameOldNewValuesMap){
        tableName = tableName + BACKUP_TABLE_EXTENSION;
        StringBuffer columnsNamesNewValuesBuf = new StringBuffer();
        List<String> columnOldNewValueList;
        String columnNewValue;
        for(String columnName: columnNameOldNewValuesMap.keySet()){
            columnOldNewValueList = columnNameOldNewValuesMap.get(columnName);
            columnNewValue = (columnOldNewValueList!=null&&columnOldNewValueList.size()>1)?columnOldNewValueList.get(ENCRYPTED_VALUE_INDEX):"";
            columnsNamesNewValuesBuf.append(columnName)
                                .append("='")
                                .append(columnNewValue)
                                .append("'")
                                .append(COMMA_SEPARATOR);
        }
        columnsNamesNewValuesBuf.append(BACKUP_TABLE_ENCRYPT_IND)
                            .append("='Y'");

        StringBuffer columnsNamesOldValuesBuf = new StringBuffer();
        String columnOldValue; 
        for(String columnName: columnNameOldNewValuesMap.keySet()){
            columnOldNewValueList = columnNameOldNewValuesMap.get(columnName);
            columnOldValue = (columnOldNewValueList!=null&&columnOldNewValueList.size()>0)?columnOldNewValueList.get(UNENCRYPTED_VALUE_INDEX):"";
            if(StringUtils.isEmpty(columnOldValue) || columnOldValue.equalsIgnoreCase("null")){
                columnsNamesOldValuesBuf.append(columnName)
                .append(" IS NULL ")
                .append(AND_SEPARATOR);
            } else{
                columnsNamesOldValuesBuf.append(columnName)
                                    .append("='")
                                    .append(columnOldValue)
                                    .append("'")
                                    .append(AND_SEPARATOR);
            }
        }
        columnsNamesOldValuesBuf.append(BACKUP_TABLE_ENCRYPT_IND)
                            .append(" IS NULL ");
        
        return new StringBuffer("UPDATE ")
                                .append(tableName)
                                .append(" SET ")
                                .append(columnsNamesNewValuesBuf)
                                .append(" WHERE ")
                                .append(columnsNamesOldValuesBuf)
                                .toString();
    }

    protected String getSelectBackupTableColumnsSql(String tableName, List<String> columnNames, int numberOfRowsToCommitAfter){
        tableName = tableName + BACKUP_TABLE_EXTENSION;
        StringBuffer columnsNamesBuf = new StringBuffer();
        for(String columnName: columnNames){
            columnsNamesBuf.append(columnName).append(COMMA_SEPARATOR);
        }
        String selectColumns = StringUtils.stripEnd(columnsNamesBuf.toString(), COMMA_SEPARATOR); 
        return new StringBuffer("SELECT ")
                    .append(selectColumns)
                    .append(" FROM ")
                    .append(tableName)
                    .append(" WHERE ")
                    .append(BACKUP_TABLE_ENCRYPT_IND)
                    .append(" IS NULL AND ROWNUM<=")
                    .append(numberOfRowsToCommitAfter).toString();
    }
    
    public boolean performEncryption(
            final String tableName, 
            final List<Map<String, List<String>>> rowsToEncryptColumnNameOldNewValuesMap) throws Exception {

        Boolean success = (Boolean) getJdbcTemplate().execute(new ConnectionCallback() {
                public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
                    try{
                        conn.setAutoCommit(false);
                        Statement statement = conn.createStatement();
                        int counter = 0;
                        for (Map<String, List<String>> columnNameOldNewValuesMap: rowsToEncryptColumnNameOldNewValuesMap) {
                            statement.addBatch(getUpdateBackupTableColumnsSql(tableName, columnNameOldNewValuesMap));
                            counter++;
                        }
                        statement.executeBatch();
                        conn.commit();
                        LOG.info(new StringBuffer("Encrypted ").append(" attributes of table ").append(tableName));
                    }
                    catch (Exception e) {
                        LOG.error(new StringBuffer("Caught exception, while encrypting ").append(" attributes of table ").append(tableName), e);
                        conn.rollback();
                        return false;
                    }
                    return true;
                }
            });
        return success;
    }   
}
