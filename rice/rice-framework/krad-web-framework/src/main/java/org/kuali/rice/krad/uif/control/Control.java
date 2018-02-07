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
package org.kuali.rice.krad.uif.control;

import org.kuali.rice.krad.uif.element.ContentElement;

/**
 * Represents an interactive element in the UI (typically an HTML control)
 * <p>
 * Each control that can be rendered in the UIF should be an implement the
 * <code>Control</code> interface. The control is a regular component, thus has
 * a corresponding template that will render the control for the UI. Controls
 * provide the mechanism for gathering data from the User or for the User to
 * initiate an action. HTML controls must be rendered within a <code>Form</code>
 * element.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Control extends ContentElement {

    /**
     * Unique index of the control within the tab order
     *
     * <p>
     * Tab index provides a way to set the order users will tab through the
     * controls. The control with index 1 will receive focus when the page is
     * rendered. Tabing from the field will then take the user to the control
     * with index 2, then index 3, and so on.
     * </p>
     *
     * @return int the tab index for the control
     */
    public int getTabIndex();

    /**
     * Setter for the controls tab order index
     *
     * @param tabIndex
     */
    public void setTabIndex(int tabIndex);

    /**
     * Indicates whether the control is disabled (doesn't allow input)
     *
     * @return boolean true if the control is disabled, false if not
     */
    public boolean isDisabled();

    /**
     * Setter for the disabled indicator
     *
     * @param disabled
     */
    public void setDisabled(boolean disabled);

    /**
     * If the control is disabled, gives a reason for why which will be displayed as a tooltip
     * on the control
     *
     * @return String disabled reason text
     * @see #isDisabled()
     */
    public String getDisabledReason();

    /**
     * Setter for the disabled reason text
     *
     * @param disabledReason
     */
    public void setDisabledReason(String disabledReason);
}


