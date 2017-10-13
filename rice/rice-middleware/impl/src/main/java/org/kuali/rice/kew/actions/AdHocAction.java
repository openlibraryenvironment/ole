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
package org.kuali.rice.kew.actions;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.group.Group;


import java.util.List;


/**
 * Responsible for creating adhoc requests that are requested from the client.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AdHocAction extends ActionTakenEvent {
    /**
     * AdHoc actions don't actually result in an action being taken...it's a special case that generates other action requests
     */
    private static final String NO_ACTION_TAKEN_CODE = null;

	private String actionRequested;
	private String nodeName;
    private Integer priority;
	private String responsibilityDesc;
	private Boolean forceAction;
	private Recipient recipient;
	private String annotation;
	private String requestLabel;

    public AdHocAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(NO_ACTION_TAKEN_CODE, routeHeader, principal);
    }

	public AdHocAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String actionRequested, String nodeName, Integer priority, Recipient recipient, String responsibilityDesc, Boolean forceAction, String requestLabel) {
		super(NO_ACTION_TAKEN_CODE, routeHeader, principal, annotation);
		this.actionRequested = actionRequested;
		this.nodeName = nodeName;
        this.priority = priority;
		this.responsibilityDesc = responsibilityDesc;
		this.forceAction = forceAction;
		this.recipient = recipient;
		this.annotation = annotation;
		this.requestLabel = requestLabel;
	}

	public void recordAction() throws InvalidActionTakenException {
		String errorMessage = validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }
		List targetNodes = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(getDocumentId());
        String error = adhocRouteAction(targetNodes, false);
        if (!org.apache.commons.lang.StringUtils.isEmpty(error)) {
            throw new InvalidActionTakenException(error);
        }
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        List targetNodes = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(getDocumentId());
        return validateActionRulesInternal(targetNodes);
    }
    
    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
    	return validateActionRules();
    }

    private String validateActionRulesInternal(List<RouteNodeInstance> targetNodes) {
    	// recipient will be null when this is invoked from ActionRegistry.getValidActions
    	if (recipient != null) {
    		if (recipient instanceof KimPrincipalRecipient) {
    			KimPrincipalRecipient principalRecipient = (KimPrincipalRecipient)recipient;
    			if (!KEWServiceLocator.getDocumentTypePermissionService().canReceiveAdHocRequest(principalRecipient.getPrincipalId(), getRouteHeader(), actionRequested)) {
    				return "The principal '" + principalRecipient.getPrincipal().getPrincipalName() + "' does not have permission to recieve ad hoc requests on DocumentType '" + getRouteHeader().getDocumentType().getName() + "'";
    			}
    		} else if (recipient instanceof KimGroupRecipient) {
    			Group group = ((KimGroupRecipient)recipient).getGroup();
    			if (!KEWServiceLocator.getDocumentTypePermissionService().canGroupReceiveAdHocRequest("" + group.getId(), getRouteHeader(), actionRequested)) {
    				return "The group '" + group.getName() + "' does not have permission to recieve ad hoc requests on DocumentType '" + getRouteHeader().getDocumentType().getName() + "'";
    			}
    		} else {
    			return "Invalid Recipient type encountered: " + recipient.getClass();
    		}
    	}
        return adhocRouteAction(targetNodes, true);
    }

    private String adhocRouteAction(List<RouteNodeInstance> targetNodes, boolean forValidationOnly) {
        //if (targetNodes.isEmpty()) {
        //    return "Could not locate an node instance on the document with the name '" + nodeName + "'";
        //}
        boolean requestCreated = false;
        for (RouteNodeInstance routeNode : targetNodes)
        {
            // if the node name is null, then adhoc it to the first available node
            if (nodeName == null || routeNode.getName().equals(nodeName))
            {
                String message = createAdHocRequest(routeNode, forValidationOnly);
                if (StringUtils.isNotBlank(message))
                {
                    return message;
                }
                requestCreated = true;
                if (nodeName == null)
                {
                    break;
                }
            }
        }
        
        if (!requestCreated && targetNodes.isEmpty()) {
            String message = createAdHocRequest(null, forValidationOnly);
            if (StringUtils.isNotBlank(message)) {
                return message;
            }
            requestCreated = true;
        }
        
        if (!requestCreated) {
            return "Didn't create request.  The node name " + nodeName + " given is probably invalid ";
        }
        return "";
    }
    
    private String createAdHocRequest(RouteNodeInstance routeNode, boolean forValidationOnly) {
                ActionRequestValue adhocRequest = new ActionRequestValue();
                if (!forValidationOnly) {
                    ActionRequestFactory arFactory = new ActionRequestFactory(routeHeader, routeNode);
                    adhocRequest = arFactory.createActionRequest(actionRequested, recipient, responsibilityDesc, forceAction, annotation);
                    adhocRequest.setResponsibilityId(KewApiConstants.ADHOC_REQUEST_RESPONSIBILITY_ID);
                    adhocRequest.setRequestLabel(requestLabel);
                    if (priority != null) {
                        adhocRequest.setPriority(priority);
                    }
                } else {
                    adhocRequest.setActionRequested(actionRequested);
                }
                if (adhocRequest.isApproveOrCompleteRequest() && ! (routeHeader.isEnroute() || routeHeader.isStateInitiated() ||
                        routeHeader.isStateSaved())) {
            return "Cannot AdHoc a Complete or Approve request when document is in state '" + routeHeader.getDocRouteStatusLabel() + "'.";
                }
                if (!forValidationOnly) {
     
                	if (routeHeader.isDisaproved() || routeHeader.isCanceled() || routeHeader.isFinal() || routeHeader.isProcessed()) {
                        getActionRequestService().activateRequest(adhocRequest);
                    } else {
                        KEWServiceLocator.getActionRequestService().saveActionRequest(adhocRequest);
                    }
                }
        return "";
    }
}
