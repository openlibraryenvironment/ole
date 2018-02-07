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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

@XmlRootElement(name = RequestedActions.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RequestedActions.Constants.TYPE_NAME, propOrder = {
		RequestedActions.Elements.COMPLETE_REQUESTED,
		RequestedActions.Elements.APPROVE_REQUESTED,
		RequestedActions.Elements.ACKNOWLEDGE_REQUESTED,
		RequestedActions.Elements.FYI_REQUESTED,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RequestedActions extends AbstractDataTransferObject {
    
	private static final long serialVersionUID = -6600754341497697330L;

    @XmlElement(name = Elements.COMPLETE_REQUESTED, required = true)
    private final boolean completeRequested;
	
	@XmlElement(name = Elements.APPROVE_REQUESTED, required = true)
    private final boolean approveRequested;
	
	@XmlElement(name = Elements.ACKNOWLEDGE_REQUESTED, required = true)
	private final boolean acknowledgeRequested;
	
	@XmlElement(name = Elements.FYI_REQUESTED, required = true)
	private final boolean fyiRequested;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private RequestedActions() {
    	this.completeRequested = false;
    	this.approveRequested = false;
    	this.acknowledgeRequested = false;
    	this.fyiRequested = false;
    }
    
    private RequestedActions(boolean completeRequested, boolean approveRequested, boolean acknowledgeRequested, boolean fyiRequested) {
    	this.completeRequested = completeRequested;
    	this.approveRequested = approveRequested;
    	this.acknowledgeRequested = acknowledgeRequested;
    	this.fyiRequested = fyiRequested;
    }
    
    public static RequestedActions create(boolean completeRequested, boolean approveRequested, boolean acknowledgeRequested, boolean fyiRequested) {
    	return new RequestedActions(completeRequested, approveRequested, acknowledgeRequested, fyiRequested);
    }
    
	public boolean isCompleteRequested() {
		return completeRequested;
	}

	public boolean isApproveRequested() {
		return approveRequested;
	}

	public boolean isAcknowledgeRequested() {
		return acknowledgeRequested;
	}

	public boolean isFyiRequested() {
		return fyiRequested;
    }

    /**
     * Returns a Set of {@link ActionRequestType}s which indicate the actions which have been requested.  This will
     * essentially contain request types for any of the request-related methods on this class which return "true".  If
     * no actions are requested, the empty set will be returned (this method will never return null).
     *
     * @return an unmodifiable Set of action request types which have been requested, or an empty set if no actions are
     * requested
     */
    public Set<ActionRequestType> getRequestedActions() {
        EnumSet<ActionRequestType> requestedActions = EnumSet.noneOf(ActionRequestType.class);
        if (isCompleteRequested()) {
            requestedActions.add(ActionRequestType.COMPLETE);
        }
        if (isApproveRequested()) {
            requestedActions.add(ActionRequestType.APPROVE);
        }
        if (isAcknowledgeRequested()) {
            requestedActions.add(ActionRequestType.ACKNOWLEDGE);
        }
        if (isFyiRequested()) {
            requestedActions.add(ActionRequestType.FYI);
        }
        return Collections.unmodifiableSet(requestedActions);
    }

    /**
     * Returns true if this set of requested actions contains the given action request type.
     *
     * @param actionRequestType the {@link ActionRequestType} to check for, can't be null
     *
     * @return true if the action is requested, false otherwise
     *
     * @throws IllegalArgumentException if actionRequestType is null
     */
    public boolean contains(ActionRequestType actionRequestType) {
        if (actionRequestType == null) {
            throw new IllegalArgumentException("actionRequestType was null");
        }
        return getRequestedActions().contains(actionRequestType);
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "requestedActions";
        final static String TYPE_NAME = "RequestedActionsType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String COMPLETE_REQUESTED = "completeRequested";
        final static String APPROVE_REQUESTED = "approveRequested";
        final static String ACKNOWLEDGE_REQUESTED = "acknowledgeRequested";
        final static String FYI_REQUESTED = "fyiRequested";
    }

}
