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
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Action;

import java.util.HashSet;
import java.util.Set;

/**
 * Special <code>Group</code> that renders a navigation section
 *
 * <p>
 * Only supports <code>Action</code> instances within the container. These
 * are used to provide the items (or individual links) within the navigation.
 * The navigationType determines how the navigation will be rendered (menu,
 * tabs, dropdown, ...)
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "navigationGroup-bean", parent = "Uif-NavigationGroupBase"),
        @BeanTag(name = "menuNavigationGroup-bean", parent = "Uif-MenuNavigationGroup"),
        @BeanTag(name = "tabNavigationGroup-bean", parent = "Uif-TabNavigationGroup")})
public class NavigationGroup extends Group {
    private static final long serialVersionUID = -7263923392768546340L;

    private String navigationType;

    public NavigationGroup() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.web.view.container.ContainerBase#getSupportedComponents()
     */
    @Override
    public Set<Class<? extends Component>> getSupportedComponents() {
        Set<Class<? extends Component>> supportedComponents = new HashSet<Class<? extends Component>>();
        supportedComponents.add(Action.class);

        return supportedComponents;
    }

    /**
     * Type of navigation that should be rendered. For example a menu or tab
     * navigation. Used by the rendering script to choose an appropriate plug-in
     *
     * @return navigation type
     * @see org.kuali.rice.krad.uif.UifConstants.NavigationType
     */
    @BeanTagAttribute(name = "navigationType")
    public String getNavigationType() {
        return this.navigationType;
    }

    /**
     * Setter for the navigation type
     *
     * @param navigationType
     */
    public void setNavigationType(String navigationType) {
        this.navigationType = navigationType;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        NavigationGroup navigationGroupCopy = (NavigationGroup) component;
        navigationGroupCopy.setNavigationType(this.getNavigationType());
    }
}
