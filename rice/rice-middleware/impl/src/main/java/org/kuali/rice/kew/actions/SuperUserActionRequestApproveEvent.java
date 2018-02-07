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
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;

import java.util.ArrayList;
import java.util.List;


/**
 * Super user Approves a single action request.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserActionRequestApproveEvent extends SuperUserActionTakenEvent {
    /**
     * This is the only action which is polymorphic...the action taken code is dynamically determined
     * based on action requested.  All other actions' action taken code is immutable, so the field could otherwise
     * be set to final and initialized in the constructor...however it would not be advisable to perform in the
     * constructor the work required by this class to determine the action taken.  So for now the class initializes
     * the action taken to null (this would be the behavior anyway if the constructor did not enforce an action taken code
     * to be supplied).  An alternative would be to do away with the stored superclass field and simply delegate to a subclass
     * getActionTakenCode implementation when necessary.  It is also not clear that this would be a good choice as it may be
     * called multiple times in arbitrary contexts.
     */
    private static final String UNDEFINED_ACTION_TAKEN_CODE = null;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SuperUserActionRequestApproveEvent.class);
    private String actionRequestId;

    public SuperUserActionRequestApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(UNDEFINED_ACTION_TAKEN_CODE, KewApiConstants.SUPER_USER_ACTION_REQUEST_APPROVE, routeHeader, principal);
    }

    public SuperUserActionRequestApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String actionRequestId, String annotation, boolean runPostProcessor) {
        super(UNDEFINED_ACTION_TAKEN_CODE, KewApiConstants.SUPER_USER_ACTION_REQUEST_APPROVE, routeHeader, principal, annotation, runPostProcessor);
        this.actionRequestId = actionRequestId;
    }

    public void setActionTaken() {
        String actionRequestCode = "";

        ActionRequestValue actionRequest = getActionRequestService().findByActionRequestId(actionRequestId);

        setActionRequest(actionRequest);

        actionRequestCode = actionRequest.getActionRequested();

        ActionType suActionType = ActionType.toSuperUserActionType(ActionType.fromCode(actionRequestCode, true));
        if (suActionType == null) {
            //TODO this should be checked
            LOG.error("Invalid SU delegation action request code: " + actionRequestCode);
            throw new RuntimeException("Invalid SU delegation action request code: " + actionRequestCode);
        } else {
            this.setActionTakenCode(suActionType.getCode());
        }
    }

    protected ActionTakenValue processActionRequests() throws InvalidActionTakenException {
        //this method has been written to process all of the actions though only approvals are currently processed

        DocumentType docType = getRouteHeader().getDocumentType();
//        boolean userAuthorized = getDocumentTypeService().verifySUAuthority(docType, getUser());

        String errorMessage = super.validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            LOG.info("User not authorized");
            List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();
            errors.add(new WorkflowServiceErrorImpl(errorMessage, SuperUserActionTakenEvent.AUTHORIZATION));
            throw new WorkflowServiceErrorException(errorMessage, errors);
        }
//        if (!docType.isSuperUser(getUser())) {
//            List errors = new ArrayList();
//            errors.add(new WorkflowServiceErrorImpl("User not authorized for super user action", SuperUserActionTakenEvent.AUTHORIZATION));
//            throw new WorkflowServiceErrorException("Super User Authorization Error", errors);
//        }

        this.setActionTaken();

        MDC.put("docId", getRouteHeader().getDocumentId());

        LOG.debug("Super User Delegation Action on action request: " + annotation);
        KimPrincipalRecipient superUserRecipient = null;
        if (getActionRequest().getPrincipal() != null) {
        	superUserRecipient = new KimPrincipalRecipient(getActionRequest().getPrincipal());
        }
        
        ActionTakenValue actionTaken = this.saveActionTaken(superUserRecipient);

        LOG.debug("Deactivate this action request");

        ActionRequestValue request = getActionRequest();
        getActionRequestService().deactivateRequest(actionTaken, request);
        if (docType.getSuperUserApproveNotificationPolicy().getPolicyValue() && request.isApproveOrCompleteRequest()) {
        	KEWServiceLocator.getActionRequestService().activateRequest(
        	new ActionRequestFactory(this.getRouteHeader()).createNotificationRequest(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, request.getPrincipal(), this.getActionTakenCode(), getPrincipal(), null));
        }
        notifyActionTaken(actionTaken);

        if (!(KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_FYI_CD.equals(this.getActionTakenCode()) && KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_ACKNOWLEDGED_CD.equals(this.getActionTakenCode()))) {
            if (getRouteHeader().isInException()) {
                LOG.debug("Moving document back to Enroute from Exception");

                String oldStatus = getRouteHeader().getDocRouteStatus();
                this.getRouteHeader().markDocumentEnroute();

                String newStatus = getRouteHeader().getDocRouteStatus();
                this.notifyStatusChange(newStatus, oldStatus);
                KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
            }
            else if (getRouteHeader().isStateSaved()) {
        	if (KewApiConstants.SAVED_REQUEST_RESPONSIBILITY_ID.equals(request.getResponsibilityId())) {
                    LOG.debug("Moving document to Enroute from Saved because action request was request generated by save action");
            	
                    String oldStatus = getRouteHeader().getDocRouteStatus();
                    this.getRouteHeader().markDocumentEnroute();
                    String newStatus = getRouteHeader().getDocRouteStatus();
                    this.notifyStatusChange(newStatus, oldStatus);
                    KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
        	}
            }
        }

        return actionTaken;
    }

    public void recordAction() throws InvalidActionTakenException {
        this.processActionRequests();
        this.queueDocumentProcessing();
    }

    protected void markDocument() throws WorkflowException {
    }
}
