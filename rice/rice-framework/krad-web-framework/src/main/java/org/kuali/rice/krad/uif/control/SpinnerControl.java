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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.widget.Spinner;

import java.util.List;

/**
 * Text control that as decorated with a spinner widget (allowing the control value to be modified using the
 * spinner)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "spinnerControl-bean", parent = "Uif-SpinnerControl")
public class SpinnerControl extends TextControl {
    private static final long serialVersionUID = -8267606288443759880L;

    private Spinner spinner;

    public SpinnerControl() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(getSpinner());

        return components;
    }

    /**
     * Spinner widget that should decorate the control
     *
     * @return Spinner
     */
    @BeanTagAttribute(name="spinner",type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Spinner getSpinner() {
        return spinner;
    }

    /**
     * Setter for the control's spinner widget instance
     *
     * @param spinner
     */
    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        SpinnerControl spinnerControlCopy = (SpinnerControl) component;

        if(this.spinner != null) {
            spinnerControlCopy.setSpinner((Spinner)this.spinner.copy());
        }
    }
}
