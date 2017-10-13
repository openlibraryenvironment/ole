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
import org.kuali.rice.krad.uif.widget.Accordion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Accordion group class used to stack groups by there header titles in an accordion layout
 */
@BeanTags({@BeanTag(name = "accordionGroup-bean", parent = "Uif-AccordionGroup"),
        @BeanTag(name = "accordionSection-bean", parent = "Uif-AccordionSection"),
        @BeanTag(name = "accordionSubSection-bean", parent = "Uif-AccordionSubSection"),
        @BeanTag(name = "disclosure-accordionSection-bean", parent = "Uif-Disclosure-AccordionSection"),
        @BeanTag(name = "disclosure-accordionSubSection-bean", parent = "Uif-Disclosure-AccordionSubSection")})
public class AccordionGroup extends Group {

    private static final long serialVersionUID = 7230145606607506418L;

    private Accordion accordionWidget;

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(accordionWidget);

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View, Object,
     *      org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);
        this.addDataAttribute(UifConstants.DataAttributes.TYPE, "Uif-AccordionGroup");
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
     * Gets the widget which contains any configuration for the accordion widget component used to render
     * this AccordionGroup
     *
     * @return the accordionWidget
     */
    @BeanTagAttribute(name = "accordionWidget", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Accordion getAccordionWidget() {
        return this.accordionWidget;
    }

    /**
     * Set the accordionWidget
     *
     * @param the accordionWidget to set
     */
    public void setAccordionWidget(Accordion accordionWidget) {
        this.accordionWidget = accordionWidget;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        AccordionGroup accordionGroupCopy = (AccordionGroup) component;

        if (this.accordionWidget != null) {
            accordionGroupCopy.setAccordionWidget((Accordion) this.accordionWidget.copy());
        }
    }
}
