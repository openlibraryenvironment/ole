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
package org.kuali.rice.kew.api.action;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = ActionItemCustomization.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionItemCustomization.Constants.TYPE_NAME, propOrder = {
        ActionItemCustomization.Elements.ACTION_ITEM_ID,
        ActionItemCustomization.Elements.ACTION_SET,
        ActionItemCustomization.Elements.DISPLAY_PARAMETERS,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class ActionItemCustomization extends AbstractDataTransferObject implements ActionItemCustomizationContract {

    @XmlElement(name = Elements.ACTION_ITEM_ID, required = false)
    private final String actionItemId;
    @XmlElement(name = Elements.ACTION_SET, required = true)
    private final ActionSet actionSet;
    @XmlElement(name = Elements.DISPLAY_PARAMETERS, required = true)
    private final DisplayParameters displayParameters;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
    
    /**
     * Private constructor used only by JAXB.
     * 
     */
    private ActionItemCustomization() {
        this.actionItemId = null;
        this.actionSet = null;
        this.displayParameters = null;
    }
    
    private ActionItemCustomization(Builder builder) {
        this.actionItemId = builder.getActionItemId();
        this.actionSet = builder.getActionSet();
        this.displayParameters = builder.getDisplayParameters();
    }

    @Override
    public String getActionItemId() {
        return this.actionItemId;
    }
    
    @Override
    public ActionSet getActionSet() {
        return this.actionSet;
    }

    @Override
    public DisplayParameters getDisplayParameters() {
        return this.displayParameters;
    }
    
    /**
     * A builder which can be used to construct {@link ActionItemCustomization} instances.  
     * Enforces the constraints of the {@link ActionItemCustomizationContract}. 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, ActionItemCustomizationContract
    {
        
        private String actionItemId;
        private ActionSet actionSet;        
        private DisplayParameters displayParameters;
        
        private Builder(String actionItemId, ActionSet actionSet, DisplayParameters displayParameters) {
            setActionItemId(actionItemId);
            setActionSet(actionSet);
            setDisplayParameters(displayParameters);
        }
        
        public static Builder create(String actionItemId, ActionSet actionSet, DisplayParameters displayParameters) {
            return new Builder(actionItemId, actionSet, displayParameters);
        }
        
        public static Builder create(ActionItemCustomizationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder = create(contract.getActionItemId(), contract.getActionSet(), contract.getDisplayParameters());
            builder.setActionItemId(contract.getActionItemId());
            return builder;
        }
                    
        @Override
        public ActionItemCustomization build() {
            return new ActionItemCustomization(this);
        }

        @Override
        public ActionSet getActionSet() {
            return this.actionSet;
        }

        public DisplayParameters getDisplayParameters() {
            return this.displayParameters;
        }

        public String getActionItemId() {
            return this.actionItemId;
        }
        
        public void setActionItemId(String actionItemId) {
            if (StringUtils.isBlank(actionItemId)) {
                throw new IllegalArgumentException("actionItemId is blank");
            }
            this.actionItemId = actionItemId;
        }
        
        public void setActionSet(ActionSet actionSet) {
            if (actionSet == null) {
                throw new IllegalArgumentException("actionSet is null");
            }
            this.actionSet = actionSet;
        }
        
        public void setDisplayParameters(DisplayParameters displayParameters) {
            this.displayParameters = displayParameters;
        }
    }    
    
    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "actionItemCustomization";
        final static String TYPE_NAME = "ActionItemCustomizationType";
    }

    /**
      * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
      * 
      */
    static class Elements {
        final static String ACTION_ITEM_ID = "actionItemId";
        final static String ACTION_SET = "actionSet";
        final static String DISPLAY_PARAMETERS = "displayParameters";
    }
}
