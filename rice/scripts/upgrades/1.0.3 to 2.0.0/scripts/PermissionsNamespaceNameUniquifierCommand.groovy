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

/**
 * DbCommand that emits SQL that will append the primary key to NM col of any KRIM_PERM_T
 * records with duplicate (NMSPC_CD,NM) fields
 */
class PermissionsNamespaceNameUniquifierCommand extends RecordUniquifier {
    /* Exclude core permissions that are fixed by upgrade scripts */
    def static PERMISSIONS_TO_EXCLUDE = [
        PERM_ID: (140..152) + (155..156) + (161..168) + [170] + (172..174) +
                 [ 180, 181, 183, 259, 261, 264, 265, 289, 290, 298, 299, 306, 307 ] +
                 (332..334) + [378] + (701..703) + [707] + (719..721) + (801..803) +
                 [807, 814] + (819..821) + (833..836) + [840,841]
    ]

    def PermissionsNamespaceNameUniquifierCommand() {
        super("KRIM_PERM_T", "PERM_ID", {
          self, row ->
            [ NM: row['NM'] + " " + row[self.pk_col] ]
        }, ["NMSPC_CD", "NM"], PERMISSIONS_TO_EXCLUDE)
    }

    def addExclusion(select, exclude_where) {
        select + " and " + exclude_where
    }
}