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
package org.kuali.rice.krad.uif.element;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.field.FieldSecurity;

/**
 * Action field security adds the take action flags to the standard component security
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "actionSecurity-bean")
public class ActionSecurity extends FieldSecurity {
    private static final long serialVersionUID = 585138507596582667L;

    private boolean performActionAuthz;
    private boolean performLineActionAuthz;

    public ActionSecurity() {
        super();

        performActionAuthz = false;
        performLineActionAuthz = false;
    }

    /**
     * Indicates whether the action field has take action authorization and KIM should be consulted
     *
     * @return true if the action field has perform action authorization, false if not
     */
    @BeanTagAttribute(name = "performActionAuthz")
    public boolean isPerformActionAuthz() {
        return performActionAuthz;
    }

    /**
     * Setter for the perform action authorization flag
     *
     * @param performActionAuthz
     */
    public void setPerformActionAuthz(boolean performActionAuthz) {
        this.performActionAuthz = performActionAuthz;
    }

    /**
     * Indicates whether the line action field has take action authorization and KIM should be consulted
     *
     * @return true if the line action field has perform action authorization, false if not
     */
    @BeanTagAttribute(name = "performLineActionAuthz")
    public boolean isPerformLineActionAuthz() {
        return performLineActionAuthz;
    }

    /**
     * Setter for the perform line action authorization flag
     *
     * @param performLineActionAuthz
     */
    public void setPerformLineActionAuthz(boolean performLineActionAuthz) {
        this.performLineActionAuthz = performLineActionAuthz;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        ActionSecurity actionSecurityCopy = (ActionSecurity) component;
        actionSecurityCopy.setPerformActionAuthz(this.performActionAuthz);
        actionSecurityCopy.setPerformLineActionAuthz(this.performLineActionAuthz);
    }
}
