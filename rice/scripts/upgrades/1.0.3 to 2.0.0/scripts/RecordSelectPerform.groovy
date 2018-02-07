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
import java.sql.Connection
import java.text.MessageFormat
import java.sql.ResultSet

/**
 * A DbCommand that selects and then emits sql that modifies table rows
 */
abstract class RecordSelectPerform extends DbCommand {
    String table

    def RecordSelectPerform(String table) {
        this.table = table
    }

    def abstract String generateSelectSql()

    def void performSql(sql) {
        println sql
    }

    def static Map getRow(ResultSet rs) {
        def row = [:]
        for (i in 1..rs.metaData.columnCount) {
            row[rs.metaData.getColumnName(i)] = rs.getObject(i)
        }
        row
    }

    def perform(Connection c, List<String> args) {
        def sql = generateSelectSql()
        println "Executing: " + sql
        ResultSet rs = c.createStatement().executeQuery(sql)
        println()
        println "Generating transformation SQL:"
        println()
        while (rs.next()) {
            def row = getRow(rs)
            def actions = handleRow(row)
            if (actions != null && !actions.empty) {
                actions.each { performSql(it) }
            }
        }
        println()
        println "Done."
    }
}