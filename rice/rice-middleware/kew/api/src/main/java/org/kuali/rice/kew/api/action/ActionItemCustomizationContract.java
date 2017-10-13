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
package org.kuali.rice.kew.api.action;

import org.kuali.rice.kew.api.actionlist.DisplayParameters;

/**
 * Contract interface for action item customizations
 */
public interface ActionItemCustomizationContract {

    /**
     * Gets the ID of the action item this customization is for
     * @return the action item id.  Never null.
     */
    String getActionItemId();

    /**
     * Gets the display parameters in this action item customization.
     * @return the display parameters.  May be null.
     */
    DisplayParameters getDisplayParameters();

    /**
     * Gets the set of actions in this action item customization.
     * @return the action set.  Never null.
     */
    ActionSet getActionSet();
}
