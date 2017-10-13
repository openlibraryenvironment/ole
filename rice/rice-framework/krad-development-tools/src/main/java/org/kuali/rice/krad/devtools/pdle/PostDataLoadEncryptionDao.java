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

import java.util.List;
import java.util.Map;


/**
 * This interface defines the DB access methods required by the PostDataLoadEncryptionService
 */
public interface PostDataLoadEncryptionDao {
    
    final int UNENCRYPTED_VALUE_INDEX = 0;
    final int ENCRYPTED_VALUE_INDEX = 1;
    
    void createBackupTable(String tableName);

    void truncateTable(String tableName);

    void restoreTableFromBackup(String tableName);

    void dropBackupTable(String tableName);
          
    boolean doesBackupTableExist(String tableName);

    void addEncryptionIndicatorToBackupTable(String tableName);

    void dropEncryptionIndicatorFromBackupTable(String tableName);

    void updateColumnValuesInBackupTable(String tableName, Map<String, List<String>> columnNameOldNewValuesMap);

    List<Map<String, String>> retrieveUnencryptedColumnValuesFromBackupTable(String tableName, final List<String> columnNames, int numberOfRowsToCommitAfter);

    String getUpdateBackupTableColumnsSql(String tableName, Map<String, List<String>> columnNameOldNewValuesMap);

    boolean performEncryption(final String tableName, final List<Map<String, List<String>>> rowsToEncryptColumnNameOldNewValuesMap) throws Exception;
        
}
