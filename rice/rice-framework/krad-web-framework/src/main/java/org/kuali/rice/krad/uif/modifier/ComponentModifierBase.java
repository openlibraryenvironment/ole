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

import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for <code>ComponentModifier</code> implementations
 *
 * <p>
 * Holds run phase property and defaults to the INITIALIZE phase, and the order
 * property for setting the order in which the component modifier will be
 * invoked
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ComponentModifierBase extends UifDictionaryBeanBase implements ComponentModifier {
	private static final long serialVersionUID = -8284332412469942130L;

	private String runPhase;
	private String runCondition;
	private int order;

	public ComponentModifierBase() {
        super();

		runPhase = UifConstants.ViewPhases.INITIALIZE;
		order = 0;
	}

    /**
     * Default performInitialization impl (does nothing)
     *
     * @see org.kuali.rice.krad.uif.modifier.ComponentModifierBase#performInitialization
     */
    @Override
    public void performInitialization(View view, Object model, Component component) {

    }

    /**
     * @see org.kuali.rice.krad.uif.modifier.ComponentModifierBase#getComponentPrototypes()
     */
    public List<Component> getComponentPrototypes() {
        List<Component> components = new ArrayList<Component>();

        return components;
    }

    /**
	 * @see org.kuali.rice.krad.uif.modifier.ComponentModifier#getRunPhase()
	 */
    @BeanTagAttribute(name="runPhase")
	public String getRunPhase() {
		return this.runPhase;
	}

	/**
	 * Setter for the component initializer run phase
	 *
	 * @param runPhase
	 */
	public void setRunPhase(String runPhase) {
		this.runPhase = runPhase;
	}

	/**
	 * @see org.kuali.rice.krad.uif.modifier.ComponentModifier#getRunCondition()
	 */
    @BeanTagAttribute(name="runCondition")
	public String getRunCondition() {
		return this.runCondition;
	}

	/**
	 * Setter for the component modifiers run condition
	 *
	 * @param runCondition
	 */
	public void setRunCondition(String runCondition) {
		this.runCondition = runCondition;
	}

	/**
	 * @see org.springframework.core.Ordered#getOrder()
	 */
    @BeanTagAttribute(name="order")
	public int getOrder() {
		return this.order;
	}

	/**
	 * @see org.kuali.rice.krad.uif.component.Ordered#setOrder(int)
	 */
	public void setOrder(int order) {
		this.order = order;
	}

    /**
     * Copies the properties over for the copy method
     *
     */
    protected <T> void copyProperties(T componentModifier) {
        ComponentModifierBase componentModifierBase = (ComponentModifierBase) componentModifier;
        componentModifierBase.setOrder(this.order);
        componentModifierBase.setRunCondition(this.runCondition);
        componentModifierBase.setRunPhase(this.runPhase);
    }
}
