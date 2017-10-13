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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actions.ActionTakenEvent;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentOrchestrationQueue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.DocumentProcessingOptions;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.BlanketApproveEngine;
import org.kuali.rice.kew.engine.CompatUtils;
import org.kuali.rice.kew.engine.OrchestrationConfig;
import org.kuali.rice.kew.engine.OrchestrationConfig.EngineCapability;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;


/**
 * Does the sync work for blanket approves requested by client apps.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BlanketApproveAction extends ActionTakenEvent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BlanketApproveAction.class);
    private Set<String> nodeNames;

    public BlanketApproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal) {
        this(rh, principal, DEFAULT_ANNOTATION, (Set<String>) null);
    }

    public BlanketApproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation, Integer routeLevel) {
        this(rh, principal, annotation, convertRouteLevel(rh.getDocumentType(), routeLevel));
    }

    public BlanketApproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation, String nodeName) {
        this(rh, principal, annotation, Collections.singleton(nodeName));
    }

    public BlanketApproveAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation, Set<String> nodeNames) {
        super(KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD, rh, principal, annotation, DEFAULT_RUN_POSTPROCESSOR_LOGIC, false);
        this.nodeNames = (nodeNames == null ? new HashSet<String>() : nodeNames);
    }

    private static Set<String> convertRouteLevel(DocumentType documentType, Integer routeLevel) {
        Set<String> nodeNames = new HashSet<String>();
        if (routeLevel == null) {
            return nodeNames;
        }
        RouteNode node = CompatUtils.getNodeForLevel(documentType, routeLevel);
        if (node == null) {
            throw new WorkflowRuntimeException("Could not locate a valid node for the given route level: " + routeLevel);
        }
        nodeNames.add(node.getRouteNodeName());
        return nodeNames;
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        return validateActionRules(getActionRequestService().findAllPendingRequests(routeHeader.getDocumentId()));
    }

    public String validateActionRules(List<ActionRequestValue> actionRequests) {
        if ( (nodeNames != null) && (!nodeNames.isEmpty()) ) {
            String nodeName = isGivenNodeListValid();
            if (!org.apache.commons.lang.StringUtils.isEmpty(nodeName)) {
                return "Document already at or beyond route node " + nodeName;
            }
        }
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            return "Document is not in a state to be approved";
        }
        List<ActionRequestValue> filteredActionRequests = filterActionRequestsByCode(actionRequests, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        if (!isActionCompatibleRequest(filteredActionRequests)) {
            return "No request for the user is compatible with the BlanketApprove Action";
        }
    	// check state before checking kim
        if (! KEWServiceLocator.getDocumentTypePermissionService().canBlanketApprove(getPrincipal().getPrincipalId(), getRouteHeader())) {
            return "User is not authorized to BlanketApprove document";
        }
        return "";
    }

    private String isGivenNodeListValid() {
        for (Iterator<String> iterator = nodeNames.iterator(); iterator.hasNext();) {
            String nodeName = (String) iterator.next();
            if (nodeName == null) {
                iterator.remove();
                continue;
            }
            if (!getRouteNodeService().isNodeInPath(getRouteHeader(), nodeName)) {
                return nodeName;
            }
        }
        return "";
    }

    public void recordAction() throws InvalidActionTakenException {
        MDC.put("docId", getRouteHeader().getDocumentId());
        updateSearchableAttributesIfPossible();

        List<ActionRequestValue> actionRequests = getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), getDocumentId(), KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        String errorMessage = validateActionRules(actionRequests);
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

        LOG.debug("Checking to see if the action is legal");

            LOG.debug("Blanket approving document : " + annotation);

            if (getRouteHeader().isStateInitiated() || getRouteHeader().isStateSaved()) {
                markDocumentEnroute(getRouteHeader());
                getRouteHeader().setRoutedByUserWorkflowId(getPrincipal().getPrincipalId());
            }

            LOG.debug("Record the blanket approval action");
            Recipient delegator = findDelegatorForActionRequests(actionRequests);
            ActionTakenValue actionTaken = saveActionTaken(delegator);

            LOG.debug("Deactivate pending action requests for user");
            getActionRequestService().deactivateRequests(actionTaken, actionRequests);
            notifyActionTaken(actionTaken);

            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());

//        } else {
//            LOG.warn("Document not in state to be approved.");
//            throw new InvalidActionTakenException("Document is not in a state to be approved");
//        }
            
          queueDeferredWork(actionTaken);
    }

    protected void queueDeferredWork(ActionTakenValue actionTaken) {
        try {
        	final boolean shouldIndex = getRouteHeader().getDocumentType().hasSearchableAttributes() && RouteContext.getCurrentRouteContext().isSearchIndexingRequestedForContext();

            String applicationId = routeHeader.getDocumentType().getApplicationId();
            DocumentOrchestrationQueue blanketApprove = KewApiServiceLocator.getDocumentOrchestrationQueue(
                    routeHeader.getDocumentId(), applicationId);
            org.kuali.rice.kew.api.document.OrchestrationConfig orchestrationConfig =
                    org.kuali.rice.kew.api.document.OrchestrationConfig.create(actionTaken.getActionTakenId(), nodeNames);
            DocumentProcessingOptions options = DocumentProcessingOptions.create(true, shouldIndex);
            blanketApprove.orchestrateDocument(routeHeader.getDocumentId(), getPrincipal().getPrincipalId(),
                    orchestrationConfig, options);
        } catch (Exception e) {
            LOG.error(e);
            throw new WorkflowRuntimeException(e);
        }
    }
    
    public void performDeferredBlanketApproveWork(ActionTakenValue actionTaken, DocumentProcessingOptions processingOptions) throws Exception {

        if (getRouteHeader().isInException()) {
            LOG.debug("Moving document back to Enroute from Exception");

            markDocumentEnroute(getRouteHeader());

        }
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.BLANKET_APPROVAL, nodeNames, actionTaken, processingOptions.isSendNotifications(), processingOptions.isRunPostProcessor());
        BlanketApproveEngine blanketApproveEngine = KEWServiceLocator.getWorkflowEngineFactory().newEngine(config);
        blanketApproveEngine.process(getRouteHeader().getDocumentId(), null);
   
        queueDocumentProcessing();
   }

    protected void markDocumentEnroute(DocumentRouteHeaderValue routeHeader) throws InvalidActionTakenException {
        String oldStatus = routeHeader.getDocRouteStatus();
        routeHeader.markDocumentEnroute();

        String newStatus = routeHeader.getDocRouteStatus();
        notifyStatusChange(newStatus, oldStatus);
        KEWServiceLocator.getRouteHeaderService().saveRouteHeader(routeHeader);
    }

    private RouteNodeService getRouteNodeService() {
        return KEWServiceLocator.getRouteNodeService();
    }
}
