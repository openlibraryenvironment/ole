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

import org.kuali.rice.kew.api.document.attribute.DocumentAttributeContract;

import java.util.List;
import java.util.Map;

/**
 * Defines the contract for an object containing a customized result value for a specific document which is part of a
 * set of document search results.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchResultValueContract {

    /**
     * Returns the id of the document for which this customized result value applies.
     *
     * @return the document id of the customized document search result
     */
    String getDocumentId();

    /**
     * Returns the customized document attribute values for this document search result.
     *
     * @return the customized document attribute values for this document search result
     */
    List<? extends DocumentAttributeContract> getDocumentAttributes();

}
