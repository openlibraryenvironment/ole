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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.framework.persistence.ojb.conversion.OjbKualiEncryptDecryptFieldConversion;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.exception.ClassNotPersistableException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.impl.PersistenceServiceImplBase;

import java.util.Collections;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDataLoadEncryptionServiceImpl extends PersistenceServiceImplBase implements PostDataLoadEncryptionService {
    protected Logger LOG = Logger.getLogger(PostDataLoadEncryptionServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private EncryptionService encryptionService;
    private PostDataLoadEncryptionDao postDataLoadEncryptionDao;

    @Override
    public void checkArguments(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames) {
    	checkArguments(businessObjectClass, attributeNames, true);
    }

    @Override
    public void checkArguments(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames, boolean checkOjbEncryptConfig) {
	if ((businessObjectClass == null) || (attributeNames == null)) {
	    throw new IllegalArgumentException(
		    "PostDataLoadEncryptionServiceImpl.encrypt does not allow a null business object Class or attributeNames Set");
	}
	final ClassDescriptor classDescriptor;
	try {
	    classDescriptor = getClassDescriptor(businessObjectClass);
	} catch (ClassNotPersistableException e) {
	    throw new IllegalArgumentException(
		    "PostDataLoadEncryptionServiceImpl.encrypt does not handle business object classes that do not have a corresponding ClassDescriptor defined in the OJB repository",
		    e);
	}
	for (String attributeName : attributeNames) {
	    if (classDescriptor.getFieldDescriptorByName(attributeName) == null) {
		throw new IllegalArgumentException(
			new StringBuffer("Attribute ")
				.append(attributeName)
				.append(
					" specified to PostDataLoadEncryptionServiceImpl.encrypt is not in the OJB repository ClassDescriptor for Class ")
				.append(businessObjectClass).toString());
	    }
	    if (checkOjbEncryptConfig && !(classDescriptor.getFieldDescriptorByName(attributeName).getFieldConversion() instanceof OjbKualiEncryptDecryptFieldConversion)) {
		throw new IllegalArgumentException(
			new StringBuffer("Attribute ")
				.append(attributeName)
				.append(" of business object Class ")
				.append(businessObjectClass)
				.append(
					" specified to PostDataLoadEncryptionServiceImpl.encrypt is not configured for encryption in the OJB repository")
				.toString());
	    }
	}
    }

    @Override
    public void createBackupTable(Class<? extends PersistableBusinessObject> businessObjectClass) {
	postDataLoadEncryptionDao.createBackupTable(getClassDescriptor(businessObjectClass).getFullTableName());
    }

    @Override
    public void prepClassDescriptor(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames) {
	ClassDescriptor classDescriptor = getClassDescriptor(businessObjectClass);
	for (String attributeName : attributeNames) {
	    classDescriptor.getFieldDescriptorByName(attributeName).setFieldConversionClassName(
		    FieldConversionDefaultImpl.class.getName());
	}
    }

    @Override
    public void truncateTable(Class<? extends PersistableBusinessObject> businessObjectClass) {
	postDataLoadEncryptionDao.truncateTable(getClassDescriptor(businessObjectClass).getFullTableName());
    }

    @Override
    public void encrypt(PersistableBusinessObject businessObject, Set<String> attributeNames) {
	for (String attributeName : attributeNames) {
	    try {
		PropertyUtils.setProperty(businessObject, attributeName, encryptionService.encrypt(PropertyUtils
			.getProperty(businessObject, attributeName)));
	    } catch (Exception e) {
		throw new RuntimeException(new StringBuffer(
			"PostDataLoadEncryptionServiceImpl caught exception while attempting to encrypt attribute ").append(
			attributeName).append(" of Class ").append(businessObject.getClass()).toString(), e);
	    }
	}
	businessObjectService.save(businessObject);
    }

    @Override
    public void restoreClassDescriptor(Class<? extends PersistableBusinessObject> businessObjectClass, Set<String> attributeNames) {
	ClassDescriptor classDescriptor = getClassDescriptor(businessObjectClass);
	for (String attributeName : attributeNames) {
	    classDescriptor.getFieldDescriptorByName(attributeName).setFieldConversionClassName(
		    OjbKualiEncryptDecryptFieldConversion.class.getName());
	}
	businessObjectService.countMatching(businessObjectClass, Collections.<String, Object>emptyMap());
    }

    @Override
    public void restoreTableFromBackup(Class<? extends PersistableBusinessObject> businessObjectClass) {
	postDataLoadEncryptionDao.restoreTableFromBackup(getClassDescriptor(businessObjectClass).getFullTableName());
    }

    @Override
    public void dropBackupTable(Class<? extends PersistableBusinessObject> businessObjectClass) {
	postDataLoadEncryptionDao.dropBackupTable(getClassDescriptor(businessObjectClass).getFullTableName());
    }

    @Override
    public boolean doesBackupTableExist(String tableName){
        return postDataLoadEncryptionDao.doesBackupTableExist(tableName);
    }
    
    @Override
    public void createBackupTable(String tableName) {
        postDataLoadEncryptionDao.createBackupTable(tableName);
        postDataLoadEncryptionDao.addEncryptionIndicatorToBackupTable(tableName);
    }
    
    @Override
    public void truncateTable(String tableName) {
        postDataLoadEncryptionDao.truncateTable(tableName);
    }

    @Override
    public List<Map<String, String>> retrieveUnencryptedColumnValuesFromBackupTable(String tableName, final List<String> columnNames, int numberOfRowsToCommitAfter) {
        return postDataLoadEncryptionDao.retrieveUnencryptedColumnValuesFromBackupTable(tableName, columnNames, numberOfRowsToCommitAfter);
    }

    @Override
    public boolean performEncryption(final String tableName, final List<Map<String, String>> rowsToEncryptColumnsNameValueMap) throws Exception {
        List<Map<String, List<String>>> rowsToEncryptColumnNameOldNewValuesMap = new ArrayList<Map<String, List<String>>>();
        for(Map<String, String> columnsNameValueMap: rowsToEncryptColumnsNameValueMap){
            rowsToEncryptColumnNameOldNewValuesMap.add(getColumnNamesEncryptedValues(tableName, columnsNameValueMap));
        }
        return postDataLoadEncryptionDao.performEncryption(tableName, rowsToEncryptColumnNameOldNewValuesMap); 
    }

    public Map<String, List<String>> getColumnNamesEncryptedValues(String tableName, final Map<String, String> columnNamesValues) {
        List<String> oldNewValues = new ArrayList<String>();
        String columnOldValue;
        Map<String, List<String>> columnNameOldNewValuesMap = new HashMap<String, List<String>>();
        for (String columnName: columnNamesValues.keySet()) {
            try {
                oldNewValues = new ArrayList<String>();
                columnOldValue = columnNamesValues.get(columnName);
                //List chosen over a java object (for old and new value) for better performance
                oldNewValues.add(PostDataLoadEncryptionDao.UNENCRYPTED_VALUE_INDEX, columnOldValue);
                oldNewValues.add(PostDataLoadEncryptionDao.ENCRYPTED_VALUE_INDEX, encryptionService.encrypt(columnOldValue));
                columnNameOldNewValuesMap.put(columnName, oldNewValues);
            } catch (Exception e) {
                throw new RuntimeException(new StringBuffer(
                "PostDataLoadEncryptionServiceImpl caught exception while attempting to encrypt Column ").append(
                columnName).append(" of Table ").append(tableName).toString(), e);
            }
        }
        return columnNameOldNewValuesMap;
    }

    @Override
    public void restoreTableFromBackup(String tableName) {
        postDataLoadEncryptionDao.dropEncryptionIndicatorFromBackupTable(tableName);
        postDataLoadEncryptionDao.restoreTableFromBackup(tableName);
    }

    @Override
    public void dropBackupTable(String tableName) {
        postDataLoadEncryptionDao.dropBackupTable(tableName);
    }    

    public void setPostDataLoadEncryptionDao(PostDataLoadEncryptionDao postDataLoadEncryptionDao) {
	this.postDataLoadEncryptionDao = postDataLoadEncryptionDao;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
	this.encryptionService = encryptionService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
	this.businessObjectService = businessObjectService;
    }
}
