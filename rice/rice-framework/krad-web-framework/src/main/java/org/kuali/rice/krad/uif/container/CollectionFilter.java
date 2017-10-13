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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.uif.view.View;

import java.io.Serializable;
import java.util.List;

/**
 * Provides filtering on collection data within a <code>CollectionGroup</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface CollectionFilter extends Serializable {

    /**
     * Invoked to filter the collection data before the collection group is
     * built. Note the collection should be retrieved from the model and the valid
     * row indexes must be returned in the return list
     *
     * @param view - view instance for the collection group
     * @param model - object containing the view data and from which the collection should be pulled/updated
     * @param collectionGroup - collection group instance containing configuration for the collection
     * @return the list that contains valid row indexes
     */
    public List<Integer> filter(View view, Object model, CollectionGroup collectionGroup);
}
