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
package org.kuali.rice.core.framework.persistence.dao;

import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This is the generic data access interface for business objects. 
 * This class was adapted from the Kuali Nervous System
 * (org.kuali.rice.krad.dao.BusinessObjectDao).
 * It's not as generic as it could be as it relies on the OJB criteria object...
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface GenericDao {
    /**
     * Saves any object
     * 
     * @param bo
     */
    public void save(Object bo);

    /**
     * Saves a List of objects
     * 
     * @param businessObjects
     */
    public void save(List businessObjects);

    /**
     * Retrieves an object instance identified bys it primary key.
     * 
     * @param clazz the class
     * @param id the primary key value
     * @return Object
     */
    public Object findById(Class clazz, Object keyValue);
    
    /**
     * Retrieves an object instance identified by its primary keys and
     * values. This can be done by constructing a map where the key to the
     * map entry is the primary key attribute and the value of the entry
     * being the primary key value. For composite keys, pass in each
     * primaryKey attribute and its value as a map entry.
     * 
     * @param clazz
     * @param primaryKeys
     * @return Object
     */
    public Object findByPrimaryKey(Class clazz, Map primaryKeys);
    
    /**
     * This method should be used to try and locate an object instance by passing 
     * in unique keys and values. This can be done by constructing a map where the key to the
     * map entry is the unique key attribute and the value of the entry
     * being the unique key value. For composite keys, pass in each
     * unique key attribute and its value as a map entry.
     * 
     * @param clazz
     * @param uniqueKeys
     * @return Object
     */
    public Object findByUniqueKey(Class clazz, Map uniqueKeys);

    /**
     * Retrieves an object instance identified by the class of the given
     * object and the object's primary key values.
     * 
     * @param object
     * @return Object
     */
    public Object retrieve(Object object);
    
    /**
     * This method allows you to pass in an object that has some fields filled in, 
     * and will query underneath by automatically constructing a select statement 
     * whose where clause is built automatically by looking at the non-null 
     * attributes and using their values as part of the query.  This is basically 
     * a query by "template" method.
     * @param object
     * @return Collection
     */
    public Collection findMatchingByExample(Object object);

    /**
     * Retrieves a collection of business objects populated with data, such
     * that each record in the database populates a new object instance.
     * This will only retrieve business objects by class type.
     * 
     * @param clazz
     * @return Collection
     */
    public Collection findAll(Class clazz);

    /**
     * Retrieves a collection of business objects populated with data, such
     * that each record in the database populates a new object instance.
     * This will only retrieve business objects by class type. Orders the
     * results by the given field.
     * 
     * @param clazz
     * @return Collection
     */
    public Collection findAllOrderBy(Class clazz, String sortField,
        boolean sortAscending);

    /**
     * This method retrieves a collection of business objects populated with
     * data, such that each record in the database populates a new object
     * instance. This will retrieve business objects by class type and also
     * by criteria passed in as key-value pairs, specifically attribute
     * name-expected value.
     * 
     * @param clazz
     * @param fieldValues
     * @return Collection
     */
    public Collection findMatching(Class clazz, Map fieldValues);
    
    /**
     * This method allows for a more flexible search by allowing the programmer to 
     * construct the criteria however they need to and then pass that in for execution.
     * @param clazz
     * @param criteria
     * @return Collection
     */
    public Collection findMatching(Class clazz, org.apache.ojb.broker.query.Criteria criteria);

    /**
     * This method allows for a more flexible search by allowing the programmer to 
     * construct the criteria however they need to and then pass that in for execution.
     * @param clazz
     * @param criteria
     * @param selectForUpdate whether to perform a select for update query
     * @param wait millis to wait for select for update
     * @return Collection
     */
    public Collection findMatching(Class clazz, org.apache.ojb.broker.query.Criteria criteria, boolean selectForUpdate, long wait);
    
    public Collection findMatching(Class clazz, Map criteria, boolean selectForUpdate, long wait);

    /**
     * @param clazz
     * @param fieldValues
     * @return count of BusinessObjects of the given class whose fields
     *         match the values in the given Map.
     */
    public int countMatching(Class clazz, Map fieldValues);

    /**
     * 
     * This method returns the number of matching result given the positive
     * criterias and negative criterias. The negative criterias are the ones
     * that will be set to "notEqualTo" or "notIn"
     * 
     * @param clazz
     * @param positiveFieldValues
     *                Map of fields and values for positive criteria
     * @param negativeFieldValues
     *                Map of fields and values for negative criteria
     * @return int
     */
    public int countMatching(Class clazz, Map positiveFieldValues,
        Map negativeFieldValues);

    /**
     * This method retrieves a collection of business objects populated with
     * data, such that each record in the database populates a new object
     * instance. This will retrieve business objects by class type and also
     * by criteria passed in as key-value pairs, specifically attribute
     * name-expected value. Orders the results by the given field.
     * 
     * @param clazz
     * @param fieldValues
     * @return Collection
     */
    public Collection findMatchingOrderBy(Class clazz, Map fieldValues,
        String sortField, boolean sortAscending);

    /**
     * Deletes a business object from the database.
     * 
     * @param bo
     */
    public void delete(Object bo);

    /**
     * Deletes each business object in the given List from the database.
     * 
     * @param boList
     */
    public void delete(List<Object> boList);

    /**
     * Deletes the business objects matching the given fieldValues
     * 
     * @param clazz
     * @param fieldValues
     */
    public void deleteMatching(Class clazz, Map fieldValues);
    
    /**
     * This method allows for a more flexible search by allowing the programmer to 
     * construct the criteria however they need to and then pass that in for execution.
     * @param clazz
     * @param criteria
     * @return Collection
     */
    public Collection findMatching(Class clazz, Criteria criteria);

    /**
     * This method allows for a more flexible search by allowing the programmer to 
     * construct the criteria however they need to and then pass that in for execution.
     * @param clazz
     * @param criteria
     * @param selectForUpdate whether to perform a select for update query
     * @param wait millis to wait for select for update
     * @return Collection
     */
    public Collection findMatching(Class clazz, Criteria criteria, boolean selectForUpdate, long wait);

}
