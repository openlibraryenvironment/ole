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

import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.BlanketApproveEngine;
import org.kuali.rice.kew.engine.OrchestrationConfig;
import org.kuali.rice.kew.engine.OrchestrationConfig.EngineCapability;
import org.kuali.rice.kew.exception.*;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Does a node level super user approve action.  All approve/complete requests outstanding for
 * this node are satisfied by this action.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SuperUserNodeApproveEvent extends SuperUserActionTakenEvent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SuperUserNodeApproveEvent.class);
    private String nodeName;

    public SuperUserNodeApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_SU_ROUTE_LEVEL_APPROVED_CD, KewApiConstants.SUPER_USER_ROUTE_LEVEL_APPROVE, routeHeader, principal);
    }

    public SuperUserNodeApproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor, String nodeName) {
        super(KewApiConstants.ACTION_TAKEN_SU_ROUTE_LEVEL_APPROVED_CD, KewApiConstants.SUPER_USER_ROUTE_LEVEL_APPROVE, routeHeader, principal, annotation, runPostProcessor);
        this.nodeName = nodeName;
    }

    public void recordAction() throws InvalidActionTakenException {

        if (org.apache.commons.lang.StringUtils.isEmpty(nodeName)) {
            throw new InvalidActionTakenException("No approval node name set");
        }

        DocumentType docType = getRouteHeader().getDocumentType();

        String errorMessage = super.validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            LOG.info("User not authorized");
            List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();
            errors.add(new WorkflowServiceErrorImpl(errorMessage, SuperUserActionTakenEvent.AUTHORIZATION));
            throw new WorkflowServiceErrorException(errorMessage, errors);
        }

        ActionTakenValue actionTaken = saveActionTaken();

        notifyActionTaken(actionTaken);

            if (getRouteHeader().isInException()) {
                LOG.debug("Moving document back to Enroute from Exception");
                String oldStatus = getRouteHeader().getDocRouteStatus();
                getRouteHeader().markDocumentEnroute();
                String newStatus = getRouteHeader().getDocRouteStatus();
                notifyStatusChange(newStatus, oldStatus);
                KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
            }

            OrchestrationConfig config = new OrchestrationConfig(EngineCapability.BLANKET_APPROVAL, Collections.singleton(nodeName), actionTaken, docType.getSuperUserApproveNotificationPolicy().getPolicyValue(), isRunPostProcessorLogic());
            try {
            	BlanketApproveEngine blanketApproveEngine = KEWServiceLocator.getWorkflowEngineFactory().newEngine(config);
    			blanketApproveEngine.process(getRouteHeader().getDocumentId(), null);
            } catch (Exception e) {
            	if (e instanceof RuntimeException) {
        		throw (RuntimeException)e;
        	} else {
        		throw new WorkflowRuntimeException(e.toString(), e);
        	}
            }

        //queueDocument();
    }

    protected void markDocument() throws WorkflowException {
        // do nothing since we are overriding the entire behavior
    }





}
