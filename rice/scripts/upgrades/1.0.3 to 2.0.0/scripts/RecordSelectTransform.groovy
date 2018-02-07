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
abstract class RecordSelectTransform extends RecordSelectPerform {
    String pk_col
    Closure transform

    def RecordSelectTransform(String table, String pk_col, Closure transform) {
        super(table)
        this.pk_col = pk_col
        this.transform = transform
    }

    def String generateUpdateSql(Map row, Map updated) {
        "update ${this.table} set ${updated.collect { k, v -> "${k}='${v}'"}.join(',')} where ${this.pk_col}='${row[this.pk_col]}';"
    }

    def handleRow(row) {
        Map transformed = this.transform.call(this, row)
        def actions = []
        if (transformed != null && !transformed.empty ) {
            actions <<  generateUpdateSql(row, transformed)
        }
        actions
    }
}