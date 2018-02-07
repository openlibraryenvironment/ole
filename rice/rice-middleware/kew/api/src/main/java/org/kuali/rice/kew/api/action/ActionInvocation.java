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
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * Represents the definition of an Action invocation against an action item.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = ActionInvocation.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionInvocation.Constants.TYPE_NAME, propOrder = {
        ActionInvocation.Elements.ACTION,
        ActionInvocation.Elements.ACTION_ITEM_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class ActionInvocation extends AbstractDataTransferObject {

    @XmlElement(name = Elements.ACTION, required = true)
    private final ActionType action;

    @XmlElement(name = Elements.ACTION_ITEM_ID, required = true)
    private final String actionItemId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    @SuppressWarnings("unused")
    private ActionInvocation() {
        this.action = null;
        this.actionItemId = null;
    }

    private ActionInvocation(ActionType action, String actionItemId) {
        if (action == null) {
			throw new IllegalArgumentException("action was null");
		}
        if (StringUtils.isBlank(actionItemId)) {
			throw new IllegalArgumentException("actionItemId was a null or blank value");
		}
		this.actionItemId = actionItemId;
		this.action = action;
    }

    /**
     * Creates a new {@code ActionInvocation} which indicates that the specified action should be executed against the
     * action item with the given id.
     *
     * @param action the action to execute against the action item
     * @param actionItemId the id of the action item against which to execute the action
     *
     * @return a new {@code ActionInvocation} containing the provided values
     *
     * @throws IllegalArgumentException if {@code action} is null or {@code actionItemId} is a null or blank value
     */
	public static ActionInvocation create(ActionType action, String actionItemId) {
        return new ActionInvocation(action, actionItemId);
	}
		
	public ActionType getAction() {
		return action;
	}
	
	public String getActionItemId() {
		return actionItemId;
	}

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "actionInvocation";
        final static String TYPE_NAME = "ActionInvocationType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ACTION = "action";
        final static String ACTION_ITEM_ID = "actionItemId";
    }
		
}
