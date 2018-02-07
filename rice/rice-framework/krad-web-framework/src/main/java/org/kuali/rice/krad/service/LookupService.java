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

import java.util.Collection;
import java.util.Map;

/**
 * Defines business logic methods that support the Lookup framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface LookupService {

    /**
     * Returns a collection of objects based on the given search parameters.
     * Will not limit results, so the returned Collection could be huge.
     *                                                         o
     * @param example
     * @param formProps
     * @return
     */
    public <T extends Object> Collection<T> findCollectionBySearchUnbounded(Class<T> example,
            Map<String, String> formProps);

    /**
     * Returns a collection of objects based on the given search parameters.
     *
     * @return Collection returned from the search
     */
    public <T extends Object> Collection<T> findCollectionBySearch(Class<T> example, Map<String, String> formProps);

    public <T extends Object> Collection<T> findCollectionBySearchHelper(Class<T> example,
            Map<String, String> formProperties, boolean unbounded);

    public <T extends Object> Collection<T> findCollectionBySearchHelper(Class<T> example,
            Map<String, String> formProperties, boolean unbounded, Integer searchResultsLimit);

    /**
     * Retrieves a Object based on the search criteria, which should uniquely
     * identify a record.
     *
     * @return Object returned from the search
     */
    public <T extends Object> T findObjectBySearch(Class<T> example, Map<String, String> formProps);

    public boolean allPrimaryKeyValuesPresentAndNotWildcard(Class<?> boClass, Map<String, String> formProps);
}
