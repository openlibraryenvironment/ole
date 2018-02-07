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
 * Defines the contract for which specifies attribute fields that should be included as part of document search criteria
 * on the document search user interface.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchCriteriaConfigurationContract {

    /**
     * Returns the additional attribute fields that should be included as part of the document search criteria on the
     * document search user interface.
     *
     * @return the search attribute fields that are part of this configuration
     */
    List<AttributeFields> getSearchAttributeFields();

}
