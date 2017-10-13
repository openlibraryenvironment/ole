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
package org.kuali.rice.krad.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

/**
 * This is the generic data access interface for business objects. This should be used for unit testing purposes only.
 * 
 * 
 */
public interface BusinessObjectDao {
    /**
     * Saves any object that implements the BusinessObject interface.
     * 
     * @param bo
     */
    public PersistableBusinessObject save(PersistableBusinessObject bo);

    /**
     * Saves a List of BusinessObjects.
     * 
     * @param businessObjects
     */
    public List<? extends PersistableBusinessObject> save(List businessObjects);

    /**
     * Retrieves an object instance identified by its primary key. For composite keys, use {@link #findByPrimaryKey(Class, Map)}
     * 
     * @param clazz
     * @param primaryKey
     * @return
     */
    public <T extends BusinessObject> T findBySinglePrimaryKey(Class<T> clazz, Object primaryKey);
    
    /**
     * Retrieves an object instance identified bys it primary keys and values. This can be done by constructing a map where the key
     * to the map entry is the primary key attribute and the value of the entry being the primary key value. For composite keys,
     * pass in each primaryKey attribute and its value as a map entry.
     * 
     * @param clazz
     * @param primaryKeys
     * @return
     */
    public <T extends BusinessObject> T findByPrimaryKey(Class<T> clazz, Map<String, ?> primaryKeys);
    
    /**
	 * Retrieves an object, based on its PK object
	 * 
	 * @param clazz the class of the object to retrieve
	 * @param pkObject the value of the primary key
	 * @return the retrieved PersistableBusinessObject
	 */
	public abstract <T  extends BusinessObject> T findByPrimaryKeyUsingKeyObject(Class<T> clazz, Object pkObject);

    /**
     * Retrieves an object instance identified by the class of the given object and the object's primary key values.
     * 
     * @param object
     * @return
     */
    public PersistableBusinessObject retrieve(PersistableBusinessObject object);

    /**
     * Retrieves a collection of business objects populated with data, such that each record in the database populates a new object
     * instance. This will only retrieve business objects by class type.
     * 
     * @param clazz
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAll(Class<T> clazz);
    
    /**
     * Retrieves a collection of business objects populated with data, such that each record in the database populates a new object
     * instance. This will only retrieve business objects by class type.
     * 
     * Adds criteria on active column to return only active records. Assumes there exist a mapping for PropertyConstants.Active
     * 
     * @param clazz
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAllActive(Class<T> clazz);

    public <T extends BusinessObject> Collection<T> findAllInactive(Class<T> clazz);
    
    /**
     * Retrieves a collection of business objects populated with data, such that each record in the database populates a new object
     * instance. This will only retrieve business objects by class type. Orders the results by the given field.
     * 
     * @param clazz
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAllOrderBy(Class<T> clazz, String sortField, boolean sortAscending);
    
    /**
     * Retrieves a collection of business objects populated with data, such that each record in the database populates a new object
     * instance. This will only retrieve business objects by class type. Orders the results by the given field.
     * 
     * Adds criteria on active column to return only active records. Assumes there exist a mapping for PropertyConstants.Active
     * @param clazz
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAllActiveOrderBy(Class<T> clazz, String sortField, boolean sortAscending);

    /**
     * This method retrieves a collection of business objects populated with data, such that each record in the database populates a
     * new object instance. This will retrieve business objects by class type and also by criteria passed in as key-value pairs,
     * specifically attribute name-expected value.
     * 
     * @param clazz
     * @param fieldValues
     * @return
     */
    public <T extends BusinessObject> Collection<T> findMatching(Class<T> clazz, Map<String, ?> fieldValues);
    
    /**
     * Finds all entities matching the passed in Rice JPA criteria
     * 
     * @param <T> the type of the entity that will be returned
     * @param criteria the criteria to form the query with
     * @return a Collection (most likely a List) of all matching entities 
     */
    //public abstract <T extends BusinessObject> Collection<T> findMatching(Criteria criteria);
    
    /**
     * This method retrieves a collection of business objects populated with data, such that each record in the database populates a
     * new object instance. This will retrieve business objects by class type and also by criteria passed in as key-value pairs,
     * specifically attribute name-expected value.
     * 
     * Adds criteria on active column to return only active records. Assumes there exist a mapping for PropertyConstants.Active
     * 
     * @param clazz
     * @param fieldValues
     * @return
     */
    public <T extends BusinessObject> Collection<T> findMatchingActive(Class<T> clazz, Map<String, ?> fieldValues);

    /**
     * @param clazz
     * @param fieldValues
     * @return count of BusinessObjects of the given class whose fields match the values in the given Map.
     */
    public int countMatching(Class clazz, Map<String, ?> fieldValues);

    
    /**
     * 
     * This method returns the number of matching result given the positive criterias and
     * negative criterias. The negative criterias are the ones that will be set to 
     * "notEqualTo" or "notIn"
     * 
     * @param clazz
     * @param positiveFieldValues  Map of fields and values for positive criteria
     * @param negativeFieldValues  Map of fields and values for negative criteria
     * @return
     */
    public int countMatching(Class clazz, Map<String, ?> positiveFieldValues, Map<String, ?> negativeFieldValues);
    
    /**
     * This method retrieves a collection of business objects populated with data, such that each record in the database populates a
     * new object instance. This will retrieve business objects by class type and also by criteria passed in as key-value pairs,
     * specifically attribute name-expected value. Orders the results by the given field.
     * 
     * @param clazz
     * @param fieldValues
     * @return
     */
    public <T extends BusinessObject> Collection<T> findMatchingOrderBy(Class<T> clazz, Map<String, ?> fieldValues, String sortField, boolean sortAscending);

    /**
     * Deletes a business object from the database.
     * 
     * @param bo
     */
    public void delete(PersistableBusinessObject bo);

    /**
     * Deletes each business object in the given List from the database.
     * 
     * @param boList
     */
    public void delete(List<? extends PersistableBusinessObject> boList);

    /**
     * Deletes the business objects matching the given fieldValues
     * 
     * @param clazz
     * @param fieldValues
     */
    public void deleteMatching(Class clazz, Map<String, ?> fieldValues);
    
    /**
     * Merges the given business object, but tells the ORM that the object is to be treated as Read Only,
     * and even if it has changes, it will not be persisted to the database 
     * 
     * @param bo the business object to managed
     * @return the managed copied of the business object
     */
    public PersistableBusinessObject manageReadOnly(PersistableBusinessObject bo);
}
