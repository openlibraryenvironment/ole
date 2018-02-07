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
 * A DbCommand that selects duplicate rows based on specified columns, and emits
 * sql to "uniquify" the row
 */
abstract class RecordUniquifier extends RecordSelectTransform {
    def static SELECT_DUPLICATES = """
        select * from {1} where ({0}) in (
            select {0} from {1} group by {0} having count(*) > 1
        )
        """

    def columns
    def transform
    Map<String, Collection<String>> exclude = [:]

    def RecordUniquifier(String table, String pk_col, Closure transform, List<String> columns, Map<String, Collection<String>> exclude = [:]) {
        super(table, pk_col, transform)
        this.columns = columns
        this.exclude = exclude
    }

    def addExclusion(select, exclude) {
        select + " where " + exclude
    }

    def String generateSelectSql() {
        def select = new MessageFormat(SELECT_DUPLICATES).format([ this.columns.join(","), this.table ] as Object[])
        def clauses = []
        exclude.each  { k, v ->
            if (!v.empty) {
                clauses << "${k} not in (${v.join(',')})"
            }
        }
        if (!clauses.empty) {
            println "Excluding: " + exclude
            select = addExclusion(select, clauses.join(" and "))
        }
        select
    }

    def help() {
        return "Uniquifies " + this.table + " records that are duplicated on the following fields: " + this.columns
    }
}