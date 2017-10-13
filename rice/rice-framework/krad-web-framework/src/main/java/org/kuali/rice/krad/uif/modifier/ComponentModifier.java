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
package org.kuali.rice.krad.uif.modifier;

import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.Ordered;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Provides modification functionality for a <code>Component</code>
 *
 * <p>
 * <code>ComponentModifier</code> instances are configured by the component's
 * dictionary definition. They can be used to provide dynamic initialization
 * behavior for a certain type of component or all components based on the
 * getComponentsForLifecycle method. In addition they can do dynamic generation of
 * new <code>Component</code> instances, or replacement of the components or
 * their properties.
 * </p>
 *
 * <p>
 * Modifiers provide for more usability and flexibility of component
 * configuration. For instance if a <code>Group</code> definition is already
 * configured that is close to what the developer needs, but they need to make
 * global changes of the group, then can invoke or create a
 * <code>ComponentModifier</code> for the group to apply those changes. The
 * configuration can then inherit the exiting group definition and then specify
 * the modifier to run with the component's componentModifiers property.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ComponentModifier extends UifDictionaryBean, Serializable, Ordered {

    /**
     * Should be called to initialize the ComponentModifier
     *
     * <p>
     * This is where component modifiers can set defaults and setup other necessary
     * state. The initialize method should only be called once per layout
     * manager lifecycle and is invoked within the initialize phase of the view
     * lifecylce.
     * </p>
     *
     * <p>
     * Note if the component modifier holds nested components, they should be initialized
     * in this method by calling the view helper service
     * </p>
     *
     * @param view - View instance the component modifier is a part of
     * @parma model - object instance containing the view data
     * @param component - Component the modifier is configured on
     * @see org.kuali.rice.krad.uif.service.ViewHelperService#performInitialization
     */
    public void performInitialization(View view, Object model, Component component);

    /**
     * Invoked within the configured phase of the component lifecycle. This is
     * where the <code>ComponentModifier</code> should perform its work against
     * the given <code>Component</code> instance
     *
     * @param view - the view instance to which the component belongs
     * @param model - top level object containing the view data
     * @param component - the component instance to modify
     * @see org.kuali.rice.krad.uif.modifier.ComponentModifier#performModification
     *      (View, Object, Component)
     */
    public void performModification(View view, Object model, Component component);

    /**
     * <code>Set</code> of <code>Component</code> classes that may be sent to
     * the modifier
     *
     * <p>
     * If an empty or null list is returned, it is assumed the modifier supports
     * all components. The returned set will be used by the dictionary
     * validation
     * </p>
     *
     * @return Set component classes
     */
    public Set<Class<? extends Component>> getSupportedComponents();

    /**
     * List of components that are maintained by the modifier as prototypes for creating other component instances
     *
     * <p>
     * Prototypes are held for configuring how a component should be created during the lifecycle. An example of this
     * are the fields in a collection group that are created for each collection record. They only participate in the
     * initialize phase.
     * </p>
     *
     * @return List<Component> child component prototypes
     */
    public List<Component> getComponentPrototypes();

    /**
     * Indicates what phase of the component lifecycle the
     * <code>ComponentModifier</code> should be invoked in (INITIALIZE,
     * APPLY_MODEL, or FINALIZE)
     *
     * @return String view lifecycle phase
     * @see org.kuali.rice.krad.uif.UifConstants.ViewPhases
     */
    public String getRunPhase();

    /**
     * Conditional expression to evaluate for determining whether the component
     * modifier should be run. If the expression evaluates to true the modifier
     * will be executed, otherwise it will not be executed
     *
     * @return String el expression that should evaluate to boolean
     */
    public String getRunCondition();

    /**
     * @see org.springframework.core.Ordered#getOrder()
     */
    public int getOrder();

    /**
     * @see org.kuali.rice.krad.uif.component.Ordered#setOrder(int)
     */
    public void setOrder(int order);

    /**
     * Copy the object
     *
     * @return the copied object
     */
    public <T> T copy();
}
