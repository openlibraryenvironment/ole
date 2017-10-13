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
 * A DbCommand that deletes obsolete rule attributes from the system
 */
class DeleteOldDocSearchAttributesCommand extends RecordSelectPerform {
    def OLD_TYPES = [ "DocumentSearchCriteriaProcessorAttribute",
                      "DocumentSearchGeneratorAttribute",
                      "DocumentSearchResultProcessorAttribute",
                      "DocumentSearchXMLResultProcessorAttribute" ]

    def DeleteOldDocSearchAttributesCommand() {
        super("KREW_RULE_ATTR_T")
    }

    def String generateSelectSql() {
        "select * from ${table} where " + OLD_TYPES.collect { "RULE_ATTR_TYP_CD=\"${it}\"" }.join(" or ")
    }

    def handleRow(row) {
        // delete the association on doctype, then the rule tmpl association, and then the attr definition itself
        [
          "delete from KREW_DOC_TYP_ATTR_T where RULE_ATTR_ID=${row['RULE_ATTR_ID']}",
          // doc search attributes should only be associated with doc types KREW_DOC_TYP_ATTR_T not on templates!
          // so we shouldn't have to clean up the rule template sub-tree
          //"delete from KREW_RULE_EXT_VAL_T where RULE_EXT_ID in (select RULE_EXT_ID from KREW_RULE_EXT_T where RULE_TMPL_ATTR_ID in (select RULE_TMPL_ATTR_ID from KREW_RULE_TMPL_ATTR_T where RULE_ATTR_ID=${row['RULE_ATTR_ID']}))",
          //"delete from KREW_RULE_EXT_T where RULE_EXT_ID in (select RULE_EXT_ID from KREW_RULE_EXT_T where RULE_TMPL_ATTR_ID in (select RULE_TMPL_ATTR_ID from KREW_RULE_TMPL_ATTR_T where RULE_ATTR_ID=${row['RULE_ATTR_ID']}))",
          //"delete from KREW_RULE_TMPL_ATTR_T where RULE_ATTR_ID=${row['RULE_ATTR_ID']}",
          "delete from KREW_RULE_ATTR_T where RULE_ATTR_ID=${row['RULE_ATTR_ID']}",
        ]
    }

    def help() {
        return "Deletes obsolete doc search attributes from the system"
    }
}
