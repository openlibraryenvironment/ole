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
package org.kuali.rice.kew.impl.document.search;

import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;

import java.util.Map;

/**
 * Handles translating between parameters submitted to the document search and {@link org.kuali.rice.kew.api.document.search.DocumentSearchCriteria}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchCriteriaTranslator {

    /**
     * Translates the given map of fields values into a {@link org.kuali.rice.kew.api.document.search.DocumentSearchCriteria}.  The given map of
     * field values is keyed based on the name of the field being submitted and the value represents that field value,
     * which may contain wildcards and other logical operators supported by the KNS lookup framework.
     *
     * @param fieldValues the map of field names and values from which to populate the criteria
     * @return populated document search criteria which contains the various criteria components populated based on the
     * interpretation of the given field values
     */
    DocumentSearchCriteria translateFieldsToCriteria(Map<String, String> fieldValues);

    /**
     * Translates the given {@link org.kuali.rice.kew.api.document.search.DocumentSearchCriteria} into a map of fields values.  Reverse of
     * {@link #translateFieldsToCriteria(java.util.Map)}
     *
     * @param criteria document search criteria
     * @return Map populated with the various criteria components
     */
    Map<String, String[]> translateCriteriaToFields(DocumentSearchCriteria criteria);
}