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
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Disapproves a document. This deactivates all requests on the document and sends
 * acknowlegde requests to anybody who had already completed or approved the document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DisapproveAction extends ActionTakenEvent {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisapproveAction.class);

    /**
     * @param rh RouteHeader for the document upon which the action is taken.
     * @param principal User taking the action.
     */
    public DisapproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_DENIED_CD, rh, principal);
    }

    /**
     * @param rh RouteHeader for the document upon which the action is taken.
     * @param principal User taking the action.
     * @param annotation User comment on the action taken
     */
    public DisapproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation) {
        super(KewApiConstants.ACTION_TAKEN_DENIED_CD, rh, principal, annotation);
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#isActionCompatibleRequest(java.util.List)
     */
    @Override
    public String validateActionRules() {
    	return validateActionRules(getActionRequestService().findAllPendingRequests(routeHeader.getDocumentId()));
    }

    public String validateActionRules(List<ActionRequestValue> actionRequests) {
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            return "Document is not in a state to be disapproved";
        }
        List<ActionRequestValue> filteredActionRequests = filterActionRequestsByCode(actionRequests, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        if (!isActionCompatibleRequest(filteredActionRequests)) {
            return "No request for the user is compatible " + "with the DISAPPROVE or DENY action";
        }
        return "";
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#isActionCompatibleRequest(java.util.List)
     */
    @Override
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
            String request = actionRequest.getActionRequested();

            // APPROVE request matches all but FYI and ACK
            if ( (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(request)) ||
                 (KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(request)) ) {
                actionCompatible = true;
                break;
            }
        }

        return actionCompatible;
    }

    /**
     * Records the disapprove action. - Checks to make sure the document status allows the action. - Checks that the user has not taken a previous action. - Deactivates the pending requests for this user - Records the action
     *
     * @throws org.kuali.rice.kew.api.exception.InvalidActionTakenException
     */
    public void recordAction() throws InvalidActionTakenException {
        MDC.put("docId", getRouteHeader().getDocumentId());
        updateSearchableAttributesIfPossible();

        LOG.debug("Disapproving document : " + annotation);

        List actionRequests = getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), getDocumentId(), KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        LOG.debug("Checking to see if the action is legal");
        String errorMessage = validateActionRules(actionRequests);
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

        LOG.debug("Record the disapproval action");
        Recipient delegator = findDelegatorForActionRequests(actionRequests);
        ActionTakenValue actionTaken = saveActionTaken(delegator);

        LOG.debug("Deactivate all pending action requests");
        actionRequests = getActionRequestService().findPendingByDoc(getDocumentId());
        getActionRequestService().deactivateRequests(actionTaken, actionRequests);
        notifyActionTaken(actionTaken);

        LOG.debug("Sending Acknowledgements to all previous approvers/completers");
   	 	// Generate the notification requests in the first node we find that the current user has an approve request
        RouteNodeInstance notificationNodeInstance = null;
//        if (actionRequests.size() > 0) { //I don't see why this matters let me know if it does rk
        	notificationNodeInstance = ((ActionRequestValue)actionRequests.get(0)).getNodeInstance();
//        }
        generateAcknowledgementsToPreviousActionTakers(notificationNodeInstance);

        LOG.debug("Disapproving document");
        try {
            String oldStatus = getRouteHeader().getDocRouteStatus();
            routeHeader.markDocumentDisapproved();
            String newStatus = getRouteHeader().getDocRouteStatus();
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(routeHeader);
            notifyStatusChange(newStatus, oldStatus);
        } catch (WorkflowException ex) {
            LOG.warn(ex, ex);
            throw new InvalidActionTakenException(ex.getMessage());
        }
    }
}
