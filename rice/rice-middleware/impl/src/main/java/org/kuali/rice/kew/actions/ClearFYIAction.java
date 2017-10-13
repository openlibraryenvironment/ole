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

import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;


import java.util.Iterator;
import java.util.List;


/**
 *
 * ClearFYIAction deactivates the user requests.
 *
 * The routeheader is first checked to make sure the action is valid for the document.
 * Next the user is checked to make sure he/she has not taken a previous action on this
 * document at the actions responsibility or below. Any requests related to this user
 * are deactivated.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClearFYIAction extends ActionTakenEvent {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ClearFYIAction.class);

    /**
     * @param rh
     *            RouteHeader for the document upon which the action is taken.
     * @param principal
     *            User taking the action.
     */
    public ClearFYIAction(DocumentRouteHeaderValue rh, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_FYI_CD, rh, principal);
    }

    /**
     * @param rh
     *            RouteHeader for the document upon which the action is taken.
     * @param principal
     *            User taking the action.
     * @param annotation
     *            User comment on the action taken
     */
    public ClearFYIAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation) {
        super(KewApiConstants.ACTION_TAKEN_FYI_CD, rh, principal, annotation);
    }
    
    /**
     * Method to check if the Action is currently valid on the given document
     * @return  returns an error message to give system better identifier for problem
     */
    public String validateActionRules() {
        return validateActionRules(getActionRequestService().findAllPendingRequests(routeHeader.getDocumentId()));
    }

    public String validateActionRules(List<ActionRequestValue> actionRequests) {
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            return "Document is not in a state to have FYI processed";
        }
        List<ActionRequestValue> filteredActionRequests = filterActionRequestsByCode(actionRequests, KewApiConstants.ACTION_REQUEST_FYI_REQ);
        if (!isActionCompatibleRequest(filteredActionRequests)) {
            return "No request for the user is compatible " + "with the ClearFYI action";
        }
        return "";
    }

    public boolean isActionCompatibleRequest(List requests) {

        // can always cancel saved or initiated document
        if (routeHeader.isStateInitiated() || routeHeader.isStateSaved()) {
            return true;
        }

        boolean actionCompatible = false;
        Iterator ars = requests.iterator();
        ActionRequestValue actionRequest = null;

        while (ars.hasNext()) {
            actionRequest = (ActionRequestValue) ars.next();

            //FYI request matches all but deny and cancel
            if (KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(actionRequest.getActionRequested())) {
                actionCompatible = true;
                break;
            }
        }

        return actionCompatible;
    }

    /**
     * Processes the clear FYI action. - Checks to make sure the document status allows the action. - Checks that the user has not taken a previous action. - Deactivates the pending requests for this user
     *
     * @throws InvalidActionTakenException
     * @throws ResourceUnavailableException
     */
    public void recordAction() throws InvalidActionTakenException {
        MDC.put("docId", getRouteHeader().getDocumentId());
        updateSearchableAttributesIfPossible();

        LOG.debug("Clear FYI for document : " + annotation);
        LOG.debug("Checking to see if the action is legal");

        List actionRequests = getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), getDocumentId(), KewApiConstants.ACTION_REQUEST_FYI_REQ);
        if (actionRequests == null || actionRequests.isEmpty()) {
        	DocumentTypePolicy allowUnrequested = getRouteHeader().getDocumentType().getAllowUnrequestedActionPolicy();
        	if (allowUnrequested != null) {
        		if (!allowUnrequested.getPolicyValue()) {
        			throw new InvalidActionTakenException("No request for the user is compatible " + "with the ClearFYI action. " + "Doctype policy ALLOW_UNREQUESTED_ACTION is set to false and someone else likely just took action on the document.");
        		}
        	}
        }
        
        String errorMessage = validateActionRules(actionRequests);
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

        ActionTakenValue actionTaken = saveActionTaken(findDelegatorForActionRequests(actionRequests));

        LOG.debug("Deactivate all pending action requests");
        getActionRequestService().deactivateRequests(actionTaken, actionRequests);
        notifyActionTaken(actionTaken);
    }
}
