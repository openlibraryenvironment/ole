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

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

@XmlRootElement(name = AdHocCommand.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AdHocCommand.Constants.TYPE_NAME, propOrder = {
		AdHocCommand.Elements.ACTION_REQUESTED_CODE,
		AdHocCommand.Elements.NODE_NAME,
        AdHocCommand.Elements.PRIORITY,
		AdHocCommand.Elements.RESPONSIBILITY_DESCRIPTION,
		AdHocCommand.Elements.FORCE_ACTION,
		AdHocCommand.Elements.REQUEST_LABEL,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
abstract class AdHocCommand extends AbstractDataTransferObject {

	private static final long serialVersionUID = -4181802858756667726L;

	@XmlElement(name = Elements.ACTION_REQUESTED_CODE, required = true)
	private final String actionRequestedCode;
	
	@XmlElement(name = Elements.NODE_NAME, required = false)
	private final String nodeName;

    @XmlElement(name = Elements.PRIORITY, required = false)
	private final Integer priority;

	@XmlElement(name = Elements.RESPONSIBILITY_DESCRIPTION, required = false)
	private final String responsibilityDescription;
	
	@XmlElement(name = Elements.FORCE_ACTION, required = true)
	private final boolean forceAction;
	
	@XmlElement(name = Elements.REQUEST_LABEL, required = false)
	private final String requestLabel;
	
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
    protected AdHocCommand() {
    	this.actionRequestedCode = null;
    	this.nodeName = null;
        this.priority = null;
    	this.responsibilityDescription = null;
    	this.forceAction = false;
    	this.requestLabel = null;
    }
    
    protected AdHocCommand(Builder<?> builder) {
    	this.actionRequestedCode = builder.getActionRequested().getCode();
    	this.nodeName = builder.getNodeName();
        this.priority = builder.getPriority();
    	this.responsibilityDescription = builder.getResponsibilityDescription();
    	this.forceAction = builder.isForceAction();
    	this.requestLabel = builder.getRequestLabel();
    }
    
    public ActionRequestType getActionRequested() {
		return ActionRequestType.fromCode(actionRequestedCode);
	}

	public String getNodeName() {
		return nodeName;
	}

    public Integer getPriority() {
        return priority;
    }

    public String getResponsibilityDescription() {
		return responsibilityDescription;
	}

	public boolean isForceAction() {
		return forceAction;
	}

	public String getRequestLabel() {
		return requestLabel;
	}

	protected static abstract class Builder<T> implements Serializable {
		
		private static final long serialVersionUID = -3002495884401672488L;

		private ActionRequestType actionRequested;
    	private String nodeName;
        private Integer priority;
    	private String responsibilityDescription;
    	private boolean forceAction;
    	private String requestLabel;
    	
    	public abstract T build();
    	
    	protected Builder(ActionRequestType actionRequested, String nodeName) {
    		setActionRequested(actionRequested);
    		setNodeName(nodeName);
    		setForceAction(true);
    	}

		public ActionRequestType getActionRequested() {
			return actionRequested;
		}

		public String getNodeName() {
			return nodeName;
		}

        public Integer getPriority() {
            return priority;
        }

        public String getResponsibilityDescription() {
			return responsibilityDescription;
		}

		public boolean isForceAction() {
			return forceAction;
		}

		public String getRequestLabel() {
			return requestLabel;
		}

		public void setActionRequested(ActionRequestType actionRequested) {
			if (actionRequested == null) {
				throw new IllegalArgumentException("actionRequested was null");
			}
			this.actionRequested = actionRequested;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public void setResponsibilityDescription(String responsibilityDescription) {
			this.responsibilityDescription = responsibilityDescription;
		}

		public void setForceAction(boolean forceAction) {
			this.forceAction = forceAction;
		}

		public void setRequestLabel(String requestLabel) {
			this.requestLabel = requestLabel;
		}
    	
    }
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "adHocCommand";
        final static String TYPE_NAME = "AdHocCommandType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ACTION_REQUESTED_CODE = "actionRequestedCode";
        final static String NODE_NAME = "nodeName";
        final static String PRIORITY = "priority";
        final static String RESPONSIBILITY_DESCRIPTION = "responsibilityDescription";
        final static String FORCE_ACTION = "forceAction";
        final static String REQUEST_LABEL = "requestLabel";
    }

}
