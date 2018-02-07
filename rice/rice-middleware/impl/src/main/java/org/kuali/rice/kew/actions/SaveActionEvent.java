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
import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.krad.util.KRADConstants;


import java.util.List;


/**
 * Saves a document.  Puts the document in the persons action list that saved the document.
 * This can currently only be done by the initiator of the document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SaveActionEvent extends ActionTakenEvent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SaveActionEvent.class);

    private static final String RESPONSIBILITY_DESCRIPTION = "Initiator needs to complete document.";

    public SaveActionEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
	super(KewApiConstants.ACTION_TAKEN_SAVED_CD, routeHeader, principal);
    }

    public SaveActionEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation) {
	super(KewApiConstants.ACTION_TAKEN_SAVED_CD, routeHeader, principal, annotation);
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#isActionCompatibleRequest(java.util.List)
     */
    @Override
    public String validateActionRules() {
    	return validateActionRulesCustom(true);
    }

    private String validateActionRulesCustom(boolean checkIfActionIsValid) {
    	if (checkIfActionIsValid && (!getRouteHeader().isValidActionToTake(getActionPerformedCode()))) {
    		return "Document is not in a state to be saved";
    	}
    	// check state before checking kim
        if (!StringUtils.equals(getPrincipal().getPrincipalName(), KRADConstants.SYSTEM_USER)) {
    	    if (! KEWServiceLocator.getDocumentTypePermissionService().canSave(getPrincipal().getPrincipalId(), getRouteHeader())) {
    		    return "User is not authorized to Save document";
            }
    	}
    	return "";
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules(java.util.List)
     */
    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
    	return validateActionRules();
    }

    public void recordAction() throws InvalidActionTakenException {
	MDC.put("docId", getRouteHeader().getDocumentId());
	LOG.debug("Checking to see if the action is legal");
	/* Code below for variable 'checkIfActionIsValid' is used to identify when the 
	 * DocumentRouteHeaderValue 'legal actions' should be checked for the current
	 * document.  The 'legal actions' for a document that is in status ENROUTE or 
	 * EXCEPTION will currently say that a Save action is not valid to be performed
	 * however we still want to allow the Save action to occur if called for backward
	 * compatibility issues.
	 */
	boolean checkIfActionIsValid = true;
	if (getRouteHeader().isEnroute() || getRouteHeader().isInException()) {
	    // if document is enroute or exception... don't check if the action is valid... we will assume it is valid
	    checkIfActionIsValid = false;
	}
	String errorMessage = validateActionRulesCustom(checkIfActionIsValid);
	if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
	    throw new InvalidActionTakenException(errorMessage);
	}

	updateSearchableAttributesIfPossible();

	//    if (getRouteHeader().isValidActionToTake(getActionTakenCode())) {
	if (getRouteHeader().isStateInitiated()) {
	    LOG.debug("Record the save action");
	    ActionTakenValue actionTaken = saveActionTaken();
	    //getRouteHeader().getActionRequests().add(generateSaveRequest());
	    this.getActionRequestService().saveActionRequest(generateSaveRequest());
	    notifyActionTaken(actionTaken);
	    LOG.debug("Marking document saved");
	    try {
		String oldStatus = getRouteHeader().getDocRouteStatus();
		getRouteHeader().markDocumentSaved();
		String newStatus = getRouteHeader().getDocRouteStatus();
		notifyStatusChange(newStatus, oldStatus);
		KEWServiceLocator.getRouteHeaderService().saveRouteHeader(routeHeader);
	    } catch (WorkflowException ex) {
		LOG.warn(ex, ex);
		throw new InvalidActionTakenException(ex.getMessage());
	    }
	}
    }

    protected ActionRequestValue generateSaveRequest() {
        RouteNodeInstance initialNode = null;
        List initialNodes = KEWServiceLocator.getRouteNodeService().getInitialNodeInstances(getDocumentId());
    	if (!initialNodes.isEmpty()) {
    	    initialNode = (RouteNodeInstance)initialNodes.get(0);
    	}
        //RouteNodeInstance initialNode = (RouteNodeInstance) KEWServiceLocator.getRouteNodeService().getInitialNodeInstances(getDocumentId()).get(0);
    	ActionRequestFactory arFactory = new ActionRequestFactory(getRouteHeader(), initialNode);
    	ActionRequestValue saveRequest = arFactory.createActionRequest(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,
                0, new KimPrincipalRecipient(getPrincipal()), RESPONSIBILITY_DESCRIPTION, KewApiConstants.SAVED_REQUEST_RESPONSIBILITY_ID,
    		Boolean.TRUE, annotation);
    	//      this.getActionRequestService().saveActionRequest(saveRequest);
    	this.getActionRequestService().activateRequest(saveRequest);
    	return saveRequest;
    }

}
