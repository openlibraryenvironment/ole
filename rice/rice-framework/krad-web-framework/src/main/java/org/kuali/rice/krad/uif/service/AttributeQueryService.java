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
package org.kuali.rice.krad.uif.service;

import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.field.AttributeQueryResult;

import java.util.Map;

/**
 * Provides methods for executing <code>AttributeQuery</code> instances
 * and preparing the <code>AttributeQueryResult</code> with the result of the query
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface AttributeQueryService {

    /**
     * Executes the <code>AttributeQuery</code> associated with the <code>Suggest</code> widget within
     * the field given by the Id. The given Map of key/value pairs are used to populate the criteria part of the
     * attribute query or as arguments to the query method. The fieldTerm parameter gives the current value
     * of the field that should be matched on. The query is expected to return a list of values to suggest
     *
     * @param view - view instance for which the field belongs
     * @param fieldId - id for the attribute field to perform the query for
     * @param fieldTerm - the partial value of the query field to match
     * @param queryParameters - map of key/value pairs that are parameters to the query
     * @return AttributeQueryResult instance populated with the List<String> data field of result data
     */
    public AttributeQueryResult performFieldSuggestQuery(View view, String fieldId, String fieldTerm,
            Map<String, String> queryParameters);

    /**
     * Executes the <code>AttributeQuery</code> associated with the field given by the id. The given Map of key/value
     * pairs are used to populate the criteria part of the attribute query or as arguments to the query method.
     * The query is expected to return a Map of field name/value pairs (unlike the suggest query which just returns
     * values for one field)
     *
     * @param view - view instance for which the field belongs
     * @param fieldId - id for the attribute field to perform the query for
     * @param queryParameters - map of key/value pairs that are parameters to the query
     * @return AttributeQueryResult instance populated with the Map<String, String> of result field data
     */
    public AttributeQueryResult performFieldQuery(View view, String fieldId, Map<String, String> queryParameters);
}
