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
package org.kuali.rice.ksb.messaging.dao;

import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.PersistedMessagePayload;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;


public interface MessageQueueDAO {

    public void remove(PersistedMessageBO routeQueue);

    public void save(PersistedMessageBO routeQueue);

    public PersistedMessageBO findByRouteQueueId(Long routeQueueId);

    public List<PersistedMessageBO> findAll();

    public List<PersistedMessageBO> findAll(int maxRows);

    public List<PersistedMessageBO> getNextDocuments(Integer maxDocuments);

    public List<PersistedMessageBO> findByServiceName(QName serviceName, String methodName);

    /**
         * Finds the persisted messages that match the values passed into the criteriaValues Map, with an EqualTo criteria
         * for each.
         * 
         * @param criteriaValues
         *                A Map of Key/Value pairs, where the Key is a string holding the field name, and the Value is a
         *                string holding the value to match.
         * @return A populated (or empty) list containing the results of the search. If no matches are made, an empty list
         *         will be returned.
         */
    public List<PersistedMessageBO> findByValues(Map<String, String> criteriaValues, int maxRows);

    public PersistedMessagePayload findByPersistedMessageByRouteQueueId(Long routeQueueId);
}
