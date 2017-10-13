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
package org.kuali.rice.krad.datadictionary.state;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of StateMapping.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see StateMapping
 * @since 2.2
 */
@BeanTags({@BeanTag(name = "stateMapping-bean", parent = "StateMapping"),
        @BeanTag(name = "workflowStateMapping-bean", parent = "workflowStateMapping")})
public class StateMappingBase implements StateMapping {

    private Map<String, String> stateNameMessageKeyMap;
    private List<String> states;
    private String statePropertyName;
    private Map<String, String> customClientSideValidationStates;

    /**
     * @see StateMapping#getStateNameMessage(String)
     */
    @Override
    public String getStateNameMessage(String state) {
        String message = null;
        if (StringUtils.isNotBlank(state) && this.getStates().contains(state)) {
            if (this.getStateNameMessageKeyMap() != null) {
                ConfigurationService configService = CoreApiServiceLocator.getKualiConfigurationService();
                String key = this.getStateNameMessageKeyMap().get(state);
                message = configService.getPropertyValueAsString(key);
            }

            if (message == null) {
                message = state;
            }
        }
        return message;
    }

    /**
     * @see StateMapping#getCurrentState(Object object)
     */
    @Override
    public String getCurrentState(Object object) {
        return ObjectPropertyUtils.getPropertyValue(object, this.getStatePropertyName());
    }

    /**
     * @see StateMapping#getNextState(Object)
     */
    @Override
    public String getNextState(Object object) {
        int currentStateIndex = this.getStates().indexOf(this.getCurrentState(object));
        if (currentStateIndex != -1) {
            int index = currentStateIndex + 1;
            if (index == this.getStates().size()) {
                return this.getCurrentState(object);
            } else {
                return this.getStates().get(index);
            }
        } else {
            return this.getCurrentState(object);
        }
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.state.StateMapping#getStateNameMessageKeyMap()
     */
    @Override
    @BeanTagAttribute(name = "stateNameMessageKeyMap", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getStateNameMessageKeyMap() {
        return stateNameMessageKeyMap;
    }

    /**
     * @see StateMapping#setStateNameMessageKeyMap(java.util.Map)
     */
    @Override
    public void setStateNameMessageKeyMap(Map<String, String> stateNameMessageKeyMap) {
        this.stateNameMessageKeyMap = stateNameMessageKeyMap;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.state.StateMapping#getStates()
     */
    @Override
    @BeanTagAttribute(name = "states", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getStates() {
        if (states == null) {
            states = new ArrayList<String>();
        }
        return states;
    }

    /**
     * @see StateMapping#setStates(java.util.List)
     */
    @Override
    public void setStates(List<String> states) {
        this.states = states;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.state.StateMapping#getStatePropertyName()
     */
    @Override
    @BeanTagAttribute(name = "statePropertyName")
    public String getStatePropertyName() {
        return statePropertyName;
    }

    /**
     * @see StateMapping#setStatePropertyName(String)
     */
    @Override
    public void setStatePropertyName(String statePropertyName) {
        this.statePropertyName = statePropertyName;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.state.StateMapping#getCustomClientSideValidationStates()
     */
    @BeanTagAttribute(name = "customClientSideValidationStates", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getCustomClientSideValidationStates() {
        return customClientSideValidationStates;
    }

    /**
     * @see StateMapping#setCustomClientSideValidationStates(java.util.Map)
     */
    public void setCustomClientSideValidationStates(Map<String, String> customClientSideValidationStates) {
        this.customClientSideValidationStates = customClientSideValidationStates;
    }

    /**
     * @see StateMapping#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("StateMappingBase", getStatePropertyName());

        // Checking that propertyName is set
        if (getStatePropertyName() == null) {
            String currentValues[] = {"statePropertyName = null"};
            tracer.createWarning("The State Property Name must be set", currentValues);
        }

        // Checking that states are set
        if (getStates() == null) {
            String currentValues[] = {"states = null"};
            tracer.createWarning("States should be set", currentValues);
        }
    }
}
