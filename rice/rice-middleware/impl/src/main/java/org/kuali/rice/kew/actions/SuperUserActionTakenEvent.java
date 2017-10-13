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

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;

import java.util.ArrayList;
import java.util.List;


/**
 * Super class for all super user action takens.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
abstract class SuperUserActionTakenEvent extends ActionTakenEvent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SuperUserActionTakenEvent.class);

    protected final String superUserAction;
    //protected DocumentRouteStatusChange event;
    private ActionRequestValue actionRequest;
    public static String AUTHORIZATION = "general.routing.superuser.notAuthorized";

    protected SuperUserActionTakenEvent(String actionTakenCode, String superUserAction, DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(actionTakenCode, routeHeader, principal);
        this.superUserAction = superUserAction;
    }

    protected SuperUserActionTakenEvent(String actionTakenCode, String superUserAction, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor) {
        super(actionTakenCode, routeHeader, principal, annotation, runPostProcessor);
        this.superUserAction = superUserAction;
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        DocumentType docType = getRouteHeader().getDocumentType();
        String principalId =   getPrincipal().getPrincipalId();
        String docId =  getRouteHeader().getDocumentId();
        List<RouteNodeInstance> currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(docId);
        String documentStatus =  KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(docId).getCode();

        boolean canAdministerRouting = KEWServiceLocator.getDocumentTypePermissionService().canAdministerRouting(principalId, docType);
        boolean canSuperUserApproveSingleActionRequest = ((KEWServiceLocator.getDocumentTypePermissionService().canSuperUserApproveSingleActionRequest
                (principalId, docType, currentNodeInstances, documentStatus)) && ((KewApiConstants.SUPER_USER_ACTION_REQUEST_APPROVE).equals(getSuperUserAction())));
        boolean canSuperUserApproveDocument = ((KEWServiceLocator.getDocumentTypePermissionService().canSuperUserApproveDocument
                (principalId, docType, currentNodeInstances, documentStatus)) && ((KewApiConstants.SUPER_USER_APPROVE).equals(getSuperUserAction())));
        boolean canSuperUserDisapproveDocument = ((KEWServiceLocator.getDocumentTypePermissionService().canSuperUserDisapproveDocument
                (principalId, docType, currentNodeInstances, documentStatus)) && ((KewApiConstants.SUPER_USER_DISAPPROVE).equals(getSuperUserAction())));

        String s = this.superUserAction;
        if (!(canAdministerRouting ||
              canSuperUserApproveSingleActionRequest ||
              canSuperUserApproveDocument ||
              canSuperUserDisapproveDocument )) {
              return "User not authorized to take super user action " + getSuperUserAction() + " on document " + docId;
        }
        return "";
    }

    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
    	return validateActionRules();
    }

    public void recordAction() throws InvalidActionTakenException {

        String errorMessage = validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            LOG.info("User not authorized");
            List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();
            errors.add(new WorkflowServiceErrorImpl(errorMessage, AUTHORIZATION));
            throw new WorkflowServiceErrorException(errorMessage, errors);
        }

        ActionTakenValue actionTaken = processActionRequests();

        try {
        	String oldStatus = getRouteHeader().getDocRouteStatus();
        	//if the document is initiated then set it enroute so we can transition to any other status
        	if (getRouteHeader().isStateInitiated()) {
        		getRouteHeader().markDocumentEnroute();
        		notifyStatusChange(getRouteHeader().getDocRouteStatus(), oldStatus);
        	}
            markDocument();
            String newStatus = getRouteHeader().getDocRouteStatus();
            notifyStatusChange(newStatus, oldStatus);
        } catch (Exception ex) {
            LOG.error("Caught Exception talking to post processor", ex);
            throw new RuntimeException(ex.getMessage());
        }
        
        processActionTaken(actionTaken);
    }

    protected abstract void markDocument() throws WorkflowException;

    protected ActionTakenValue processActionRequests() throws InvalidActionTakenException {
        LOG.debug("Processing pending action requests");

        ActionTakenValue actionTaken = saveActionTaken();

        List<ActionRequestValue> actionRequests = getActionRequestService().findPendingByDoc(getDocumentId());

        for (ActionRequestValue actionRequest : actionRequests)
        {
            getActionRequestService().deactivateRequest(actionTaken, actionRequest);
        }

        notifyActionTaken(actionTaken);

        return actionTaken;
    }

    /**
     * Allows subclasses to perform any post-processing after the action has been taken
     */
    protected void processActionTaken(ActionTakenValue actionTaken) {
        // no default impl
    }

    public ActionRequestValue getActionRequest() {
        return actionRequest;
    }

    public void setActionRequest(ActionRequestValue actionRequest) {
        this.actionRequest = actionRequest;
    }

    public String getSuperUserAction() {
        return superUserAction;
    }
}
