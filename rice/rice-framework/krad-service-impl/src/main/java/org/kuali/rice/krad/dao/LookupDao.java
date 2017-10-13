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
import java.util.Map;

/**
 * Defines basic methods that Lookup Dao's must provide
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface LookupDao {
    /**
     * Retrieves a collection of objects for the businessObjectClass based on the other information passed into the
     * method.
     *
     * @param businessObjectClass - business object being queried on
     * @param formProps - map of form properties
     * @param unbounded - indicates if the search should be unbounded
     * @param usePrimaryKeyValuesOnly - indicates if only primary key values should be used
     * @return Object returned from the search
     */
	public <T extends Object> Collection<T> findCollectionBySearchHelper(
			Class<T> businessObjectClass, Map<String, String> formProps, boolean unbounded,
			boolean usePrimaryKeyValuesOnly);

    /**
     * Retrieves a collection of objects for the businessObjectClass based on the other information passed into the
     * method.
     *
     * @param businessObjectClass - business object being queried on
     * @param formProps - map of form properties
     * @param unbounded - indicates if the search should be unbounded
     * @param usePrimaryKeyValuesOnly - indicates if only primary key values should be used
     * @param searchResultsLimit - used to limit the number of items returned
     * @return Object returned from the search
     */
    public <T extends Object> Collection<T> findCollectionBySearchHelper(
            Class<T> businessObjectClass, Map<String, String> formProps, boolean unbounded,
            boolean usePrimaryKeyValuesOnly, Integer searchResultsLimit);

	/**
	 * Retrieves a Object based on the search criteria, which should uniquely
	 * identify a record.
	 * 
	 * @return Object returned from the search
	 */
	public <T extends Object> T findObjectByMap(T example, Map<String, String> formProps);

	/**
	 * Returns a count of objects based on the given search parameters.
	 * 
	 * @return Long returned from the search
	 */
	public Long findCountByMap(Object example, Map<String, String> formProps);

	/**
	 * Create OJB criteria based on business object, search field and value
	 * 
	 * @return true if the criteria is created successfully; otherwise, return
	 *         false
	 */
	public boolean createCriteria(Object example, String searchValue,
			String propertyName, Object criteria);

	/**
	 * Create OJB criteria based on business object, search field and value
	 * 
	 * @return true if the criteria is created successfully; otherwise, return
	 *         false
	 */
	public boolean createCriteria(Object example, String searchValue,
			String propertyName, boolean caseInsensitive,
			boolean treatWildcardsAndOperatorsAsLiteral, Object criteria);
}
