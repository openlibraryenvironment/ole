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
import org.apache.log4j.Logger;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.BlanketApproveEngine;
import org.kuali.rice.kew.engine.OrchestrationConfig;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.OrchestrationConfig.EngineCapability;
import org.kuali.rice.kew.engine.node.RequestsNode;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Does a super user approve action.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SuperUserApproveEvent extends SuperUserActionTakenEvent {

	private static final Logger LOG = Logger.getLogger(SuperUserApproveEvent.class);
    private final boolean allowFinalApproval;

    public SuperUserApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        this(routeHeader, principal, DEFAULT_ANNOTATION, DEFAULT_RUN_POSTPROCESSOR_LOGIC);
    }

    public SuperUserApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor) {
        super(KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD, KewApiConstants.SUPER_USER_APPROVE, routeHeader, principal, annotation, runPostProcessor);
        this.allowFinalApproval = isPolicySet(routeHeader.getDocumentType(), DocumentTypePolicy.ALLOW_SU_FINAL_APPROVAL, true);
    }

    @Override
    public String validateActionRules() {
        String error = super.validateActionRules();
        if (StringUtils.isBlank(error)) {
            if (!allowFinalApproval && KEWServiceLocator.getRouteNodeService().findFutureNodeNames(getRouteHeader().getDocumentId()).isEmpty()) {
                error = "Super User Approval disallowed on final node by " + DocumentTypePolicy.ALLOW_SU_FINAL_APPROVAL.getCode() + " policy";
            }
        }
        return error;
    }

	public void recordAction() throws InvalidActionTakenException {
		// TODO: this is used because calling this code from SuperUserAction without
        // it causes an optimistic lock
        //setRouteHeader(KEWServiceLocator.getRouteHeaderService().getRouteHeader(getDocumentId(), true));

		DocumentType docType = getRouteHeader().getDocumentType();

        String errorMessage = validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            LOG.info("User not authorized");
            List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();
            errors.add(new WorkflowServiceErrorImpl(errorMessage, AUTHORIZATION));
            throw new WorkflowServiceErrorException(errorMessage, errors);
        }

        ActionTakenValue actionTaken = saveActionTaken();

	        notifyActionTaken(actionTaken);

		if (getRouteHeader().isInException() || getRouteHeader().isStateInitiated()) {
			LOG.debug("Moving document back to Enroute");
			String oldStatus = getRouteHeader().getDocRouteStatus();
			getRouteHeader().markDocumentEnroute();
			String newStatus = getRouteHeader().getDocRouteStatus();
			notifyStatusChange(newStatus, oldStatus);
			KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
		}

		OrchestrationConfig config = new OrchestrationConfig(EngineCapability.BLANKET_APPROVAL, new HashSet<String>(), actionTaken, docType.getSuperUserApproveNotificationPolicy().getPolicyValue(), isRunPostProcessorLogic());
		RequestsNode.setSuppressPolicyErrors(RouteContext.getCurrentRouteContext());
		try {
			completeAnyOutstandingCompleteApproveRequests(actionTaken, docType.getSuperUserApproveNotificationPolicy().getPolicyValue());
			BlanketApproveEngine blanketApproveEngine = KEWServiceLocator.getWorkflowEngineFactory().newEngine(config);
			blanketApproveEngine.process(getRouteHeader().getDocumentId(), null);
		} catch (Exception e) {
			LOG.error("Failed to orchestrate the document to SuperUserApproved.", e);
			throw new InvalidActionTakenException("Failed to orchestrate the document to SuperUserApproved.", e);
		}

	}

	@SuppressWarnings("unchecked")
	protected void completeAnyOutstandingCompleteApproveRequests(ActionTakenValue actionTaken, boolean sendNotifications) throws Exception {
		List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findPendingByActionRequestedAndDocId(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, getDocumentId());
		actionRequests.addAll(KEWServiceLocator.getActionRequestService().findPendingByActionRequestedAndDocId(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, getDocumentId()));
		for (ActionRequestValue actionRequest : actionRequests) {
			KEWServiceLocator.getActionRequestService().deactivateRequest(actionTaken, actionRequest);
		}
		if (sendNotifications) {
			new ActionRequestFactory(this.getRouteHeader()).generateNotifications(actionRequests, getPrincipal(), this.findDelegatorForActionRequests(actionRequests), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD);
		}
	}

	protected void markDocument() throws WorkflowException {
		// do nothing since we are overriding the entire behavior
	}
}
