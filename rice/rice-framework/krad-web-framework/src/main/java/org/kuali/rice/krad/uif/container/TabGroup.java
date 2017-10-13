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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Tabs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A group that presents its child Groups as tabs.  Items in this group's item list must be Groups
 * themselves.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see Group
 */
@BeanTags(
        {@BeanTag(name = "tabGroup-bean", parent = "Uif-TabGroup"), @BeanTag(name = "tabSection-bean", parent = "Uif-TabSection"),
                @BeanTag(name = "tabSubSection-bean", parent = "Uif-TabSubSection")})
public class TabGroup extends Group {
    private static final long serialVersionUID = 3L;

    private Tabs tabsWidget;

    public TabGroup() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(tabsWidget);

        return components;
    }

    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);
        this.addDataAttribute(UifConstants.DataAttributes.TYPE, "Uif-TabGroup");
    }

    /**
     * Only groups are supported for this group.
     *
     * @see org.kuali.rice.krad.web.view.container.ContainerBase#getSupportedComponents()
     */
    @Override
    public Set<Class<? extends Component>> getSupportedComponents() {
        Set<Class<? extends Component>> supportedComponents = new HashSet<Class<? extends Component>>();
        supportedComponents.add(Group.class);

        return supportedComponents;
    }

    /**
     * Gets the widget which contains any configuration for the tab widget component used to render
     * this TabGroup
     *
     * @return the tabsWidget
     */
    @BeanTagAttribute(name = "tabsWidget", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Tabs getTabsWidget() {
        return this.tabsWidget;
    }

    /**
     * @param tabsWidget the tabsWidget to set
     */
    public void setTabsWidget(Tabs tabsWidget) {
        this.tabsWidget = tabsWidget;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        TabGroup tabGroupCopy = (TabGroup) component;

        if(tabsWidget != null) {
            tabGroupCopy.setTabsWidget((Tabs)this.getTabsWidget().copy());
        }
    }

}
