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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.bo.BusinessObject;

import java.util.Collection;
import java.util.Map;


/**
 * This class provides collection retrievals to populate key value pairs of business objects.
 * 
 * 
 */
public interface KeyValuesService {

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
     * instance. This will only retrieve business objects by class type. Performs a sort on the result collection on the given sort
     * field.
     * 
     * @param clazz
     * @param sortField - name of the field in the class to sort results by
     * @param sortAscending - boolean indicating whether to sort ascending or descending
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAllOrderBy(Class<T> clazz, String sortField, boolean sortAscending);

    /**
     * This method retrieves a collection of business objects populated with data, such that each record in the database populates a
     * new object instance. This will retrieve business objects by class type and also by criteria passed in as key-value pairs,
     * specifically attribute name and its expected value.
     * 
     * @param clazz
     * @param fieldValues
     * @return
     */
    public <T extends BusinessObject> Collection<T> findMatching(Class<T> clazz, Map<String, Object> fieldValues);
    
    /**
     * Retrieves a collection of business objects populated with data, such that each record in the database populates a new object
     * instance. This will only retrieve business objects by class type.
     * 
     * @param clazz
     * @return
     */
    public <T extends BusinessObject> Collection<T> findAllInactive(Class<T> clazz);

}
