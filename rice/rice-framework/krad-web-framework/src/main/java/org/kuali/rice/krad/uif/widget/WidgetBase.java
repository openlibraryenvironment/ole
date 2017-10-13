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
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentBase;

/**
 * Base class for Widgets
 *
 * <p>
 * Sets the component type name for all widget components and provides default
 * implementation of performFinalize
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "widget-bean", parent = "Uif-WidgetBase")
public abstract class WidgetBase extends ComponentBase implements Widget {
    private static final long serialVersionUID = -917582902829056830L;

    public WidgetBase() {
        super();
    }

    public WidgetBase(WidgetBase another)   {
        another.setAdditionalComponentsToRefresh(this.getAdditionalComponentsToRefresh());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentTypeName()
     */
    @Override
    public String getComponentTypeName() {
        return "widget";
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        WidgetBase WidgetBaseCopy = (WidgetBase) component;
    }
}
