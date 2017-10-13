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
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDataLoader {

    private static final Logger LOG = Logger.getLogger(SQLDataLoader.class);
    public static final String SQL_LINE_COMMENT_PREFIX = "--";

    private String fileLoc;
    private String seperatorChar;
    private String statement;

    public SQLDataLoader(String statement) {
        this.fileLoc = null;
        this.seperatorChar = null;
        this.statement = statement;
    }

    public SQLDataLoader(String fileLoc, String seperatorChar) {
        this.fileLoc = fileLoc;
        this.seperatorChar = seperatorChar;
        this.statement = null;
    }

    public void runSql() throws Exception {
        String[] sqlStatements = null;
        if (statement == null) {
            String sqlStatementsContent = getContentsAsString(fileLoc);
            // separator char must be the last non-whitespace char on the line
            // to avoid splitting in the middle of data that might contain the separator char
            sqlStatements = sqlStatementsContent.split("(?m)" + getSeperatorChar() + "\\s*$");
        } else {
            sqlStatements = new String[]{statement};
        }
        final String[] finalSqlStatements = sqlStatements;
        new TransactionTemplate(TestHarnessServiceLocator.getJtaTransactionManager()).execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                return new JdbcTemplate(TestHarnessServiceLocator.getDataSource()).execute(new ConnectionCallback() {
                    public Object doInConnection(Connection connection) throws SQLException {
                        Statement statement = connection.createStatement();
                        LOG.info("################################");
                        LOG.info(fileLoc != null ? "#" + fileLoc : "#");
                        LOG.info("#");
                        for (String sqlStatement : finalSqlStatements) {
                            if (StringUtils.isNotBlank(sqlStatement)) {
                                LOG.info("# Executing sql statement ->" + sqlStatement + "<-");
                                statement.execute(sqlStatement);
                            }
                        }
                        LOG.info("#");
                        LOG.info("#");
                        LOG.info("################################");
                        statement.close();
                        return null;
                    }
                });
            }
        });
    }

    private String getContentsAsString(String fileLoc) throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        String data = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resourceLoader.getResource(fileLoc).getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                // discard comments...commented single line statements
                // will result in errors when executed because there are no
                // results
                if (!line.trim().startsWith(SQL_LINE_COMMENT_PREFIX)) {
                    data += line + "\r\n ";
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    LOG.error(e);
                }
            }

        }
        return data;
    }

    public String getSeperatorChar() {
        if (this.seperatorChar == null) {
            return ";";
        }
        return seperatorChar;
    }

}
