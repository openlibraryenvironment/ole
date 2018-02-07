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
package org.kuali.rice.kew.framework.document.search;

import java.util.List;

/**
 * Defines the contract for an object containing result values that are used to customize document search results.
 * Defines a set of additional custom values for document search results that can be defined and returned by an
 * application which is customizing the document search for a specific document type.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchResultValuesContract {

    /**
     * Returns an unmodifiable list of the result values, one for each customized document.
     *
     * @return the list of customized document search result values, will never be null but may be empty
     */
    List<? extends DocumentSearchResultValueContract> getResultValues();

}
