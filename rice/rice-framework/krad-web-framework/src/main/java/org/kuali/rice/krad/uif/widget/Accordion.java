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
package org.kuali.rice.krad.uif.widget;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * Accordion widget class used to create an accordion based layout
 */
@BeanTag(name = "accordion-bean", parent = "Uif-Accordion")
public class Accordion extends WidgetBase {

    private static final long serialVersionUID = 8156445677141475527L;
    private String active;

    /**
     * The active option determines which accordion item is open by default.  Set "false" as the string for
     * no item open, use zero-based index to select an item to be open by default.
     *
     * @return the accordion item that is open by default, "false" if no items open
     */
    @BeanTagAttribute(name = "active")
    public String getActive() {
        return active;
    }

    /**
     * Set the accordion item open by default (zero-based index), "false" if no item should be open
     *
     * @param active
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Accordion accordionCopy = (Accordion) component;
        accordionCopy.setActive(this.active);
    }
}
