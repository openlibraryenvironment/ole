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

import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PostDataLoadEncryptionService {

    static final String POST_DATA_LOAD_ENCRYPTION_SERVICE = "postDataLoadEncryptionService";
	
	void checkArguments(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames);
	
    void checkArguments(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames, boolean checkOjbEncryptConfig);

    void createBackupTable(Class<? extends PersistableBusinessObject> businessObjectClass);

    void prepClassDescriptor(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames);

    void truncateTable(Class<? extends PersistableBusinessObject> businessObjectClass);

    void encrypt(PersistableBusinessObject businessObject, Set<String> attributeNames);

    void restoreClassDescriptor(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames);

    void restoreTableFromBackup(Class<? extends PersistableBusinessObject> businessObjectClass);

    void dropBackupTable(Class<? extends PersistableBusinessObject> businessObjectClass);
    
    boolean doesBackupTableExist(String tableName);
    
    void truncateTable(String tableName);
    
    void createBackupTable(String tableName);

    List<Map<String, String>> retrieveUnencryptedColumnValuesFromBackupTable(String tableName, final List<String> columnNames, int numberOfRowsToCommitAfter);
    
    boolean performEncryption(String tableName, List<Map<String, String>> columnsToEncrypt) throws Exception ;
    
    void restoreTableFromBackup(String tableName);

    void dropBackupTable(String tableName);
}
