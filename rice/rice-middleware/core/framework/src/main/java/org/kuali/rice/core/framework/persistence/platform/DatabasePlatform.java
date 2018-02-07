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
package org.kuali.rice.core.framework.persistence.platform;

import javax.persistence.EntityManager;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;

/**
 * Interface that abstracts database dependent sql from core
 *
 * TODO Had to move this down into embedded source because of the OJB dependencies.  This probably will
 * go away once we get rid of the embedded plugin.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DatabasePlatform {
    public String getCurTimeFunction();
    public String getStrToDateFunction();
    public String getDateFormatString(String dateFormatString);
    
    /**
     * Returns the name of a function for shifting a string to uppercase on 
     * the relevant platform.
     * @return the name of a function as a String
     */ 
    String getUpperCaseFunction();
    
    /**
     * Supplies a parameterized sequence incrementation query
     * @param sequenceName name of the sequence to be incremented
     * @return parameterized sequence incrementation query
     */
    Long getNextValSQL(String sequenceName, PersistenceBroker persistenceBroker);
    Long getNextValSQL(String sequenceName, EntityManager entityManager);
    
    /**
     * Generates the query used to select route header rows for update
     * @param documentId id of the routeHeader to select for update
     * @param wait whether to block until lock is released
     * @return the query used to select route header rows for update
     */
    
    String getLockRouteHeaderQuerySQL(String documentId, boolean wait);
    /**
     * Supplies the sql for a given date string that will satisfy a where clause
     * @param date in YYYY/MM/DD format
     * @param time in hh:mm:ss format
     * @return the sql for a given date string that will satisfy a where clause
     * @see SqlUtil#establishDateString(String, String, String, StringBuffer, DatabasePlatform)
     * @see SqlUtil#formatDate(String)
     * TODO: refactor to use a parsed Date object or milliseconds instead of date String
     */
    String getDateSQL(String date, String time);

    /**
     * Returns the suffix to append to a SQL query in order to perform
     * a "select for update" lock on the table
     * 
     * @param waitMillis the milliseconds to wait, -1 forever, 0 if no wait
     * @return the suffix to append to a SQL query in order to perform a "select for update" lock on the table
     */
    String getSelectForUpdateSuffix(long waitMillis);
    
    /**
     * @param tableToCreate the String name for the table to be created
     * @param fromTable the String name of the original table
     * @return the SQL string for creating the specified table from the second 
     * specified table 
     */
    String getCreateTableFromTableSql(String tableToCreate, String fromTable);
    
    /**
     * @param tableName the name of the table to be truncated
     * @return a String of SQL for truncating a table 
     * @see <a href="http://en.wikipedia.org/wiki/Truncate_(SQL)">Truncate (SQL)</a>
     */
    String getTruncateTableSql(String tableName);

    /**
     * @param receivingTable the name of the table receiving inserted data
     * @param fromTable the name of the originating table
     * @return an "INSERT INTO" SQL command 
     */
    String getInsertDataFromTableSql(String restoreTableName, String fromTableName);
    
    /**
     * @param tableName the table to drop
     * @return an SQL command for dropping the specified table
     */
    String getDropTableSql(String tableName);
    
    /**
     * Returns a SQL expression that acts like nvl(exprToTest, exprToReplaceIfTestExprNull) on oracle.  That is,
     * an expression that will return exprToTest does not evaluate to null, and will return exprToReplaceIfTestExprNull
     * if exprToTest does evaluate to null.  NOTE: this method does not provide any protection against SQL injection
     * attacks, nor does it validate any of the parameters.
     * 
     * @param exprToTest a SQL expression that will either evaluate to null or non-null
     * @param exprToReplaceIfTestExprNull the value to return if 
     * @return a SQL expression that acts like nvl on oracle or ifnull() on MySQL
     */
    String getIsNullFunction(String exprToTest, String exprToReplaceIfTestExprNull);

    /**
     * Escapes any special DB-specific characters from an input String, to help prevent SQL injection attacks.
     * TODO: This method should be replaced by the "prepared statement" functionality in the future.
     * 
     * @param sqlString The String to escape.
     * @return The String from sqlString, but with all of its DB-specific special characters escaped.
     */
    String escapeString(String sqlString);
    
    // Methods Imported from KualiDBPlatform
    
    /*
     * MySQL impl of this method copied to org.kuali.rice.core.database.platform.MySQLDatabasePlatform
     * No other impl exists in this legacy package (Derby impl returned null)
     */
    public void applyLimit(Integer limit, Criteria criteria);
}
