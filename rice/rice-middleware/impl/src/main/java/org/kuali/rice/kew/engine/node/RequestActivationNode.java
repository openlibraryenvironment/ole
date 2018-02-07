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
package org.kuali.rice.kew.engine.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kew.util.Utilities;


/**
 * A node which will activate any requests on it, returning true when there are no more requests 
 * which require activation.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RequestActivationNode extends RequestActivationNodeBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger( RequestActivationNode.class );
    private static long generatedRequestPriority = 0;

    public SimpleResult process(RouteContext routeContext, RouteHelper routeHelper) throws Exception {
        DocumentRouteHeaderValue document = routeContext.getDocument();
        RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
        if (routeContext.isSimulation()) {
            if (routeContext.getActivationContext().isActivateRequests()) {
                activateRequests(routeContext, document, nodeInstance);
            }
            return new SimpleResult(true);
        } else if (!activateRequests(routeContext, document, nodeInstance) && shouldTransition(document, nodeInstance)) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false);
        }            
    }
    
    public boolean shouldTransition(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) {
        List requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocIdAtRouteNode(document.getDocumentId(), nodeInstance.getRouteNodeInstanceId());
        boolean shouldTransition = true;
        for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
            ActionRequestValue request = (ActionRequestValue) iterator.next();
            if (request.isApproveOrCompleteRequest()) {
                shouldTransition = false;
                break;
            }
        }
        return shouldTransition;
    }

    /**
     * Activates the action requests that are pending at this routelevel of the document. The requests are processed by
     * priority and then request ID. It is implicit in the access that the requests are activated according to the route
     * level above all.
     *
     * <p>FYI and acknowledement requests do not cause the processing to stop. Only action requests for approval or
     * completion cause the processing to stop and then only for route level with a serialized or priority-parallel
     * activation policy. Only requests at the current document's current route level are activated. Inactive requests
     * at a lower level cause a routing exception.</p>
     *
     * <p>Exception routing and adhoc routing are processed slightly differently.</p>
     * 
     * @return true if the any approval actions were activated.
     */
    public boolean activateRequests(RouteContext context, DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) throws WorkflowException {
        MDC.put("docId", document.getDocumentId());
        PerformanceLogger performanceLogger = new PerformanceLogger(document.getDocumentId());
        List<ActionItem> generatedActionItems = new ArrayList<ActionItem>();
        List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
        if (context.isSimulation()) {
        	for (ActionRequestValue ar : context.getDocument().getActionRequests()) {
        		// logic check below duplicates behavior of the ActionRequestService.findPendingRootRequestsByDocIdAtRouteNode(documentId, routeNodeInstanceId) method
				if (ar.getCurrentIndicator()
						&& (ActionRequestStatus.INITIALIZED.getCode().equals(ar.getStatus()) || ActionRequestStatus.ACTIVATED.getCode().equals(ar.getStatus()))
						&& ar.getNodeInstance().getRouteNodeInstanceId().equals(nodeInstance.getRouteNodeInstanceId())
						&& ar.getParentActionRequest() == null) {
					requests.add(ar);
				}
			}
            requests.addAll(context.getEngineState().getGeneratedRequests());
        } else {
            requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocIdAtRouteNode(document.getDocumentId(), nodeInstance.getRouteNodeInstanceId());
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Pending Root Requests " + requests.size());
        }
        boolean activatedApproveRequest = activateRequestsCustom( context, requests, generatedActionItems, document, nodeInstance );

        // now let's send notifications, since this code needs to be able to activate each request individually, we need
        // to collection all action items and then notify after all have been generated
        notify(context, generatedActionItems, nodeInstance);

        performanceLogger.log("Time to activate requests.");
        return activatedApproveRequest;
    }

    protected boolean activateRequestsCustom( RouteContext context, List<ActionRequestValue> requests, List<ActionItem> generatedActionItems, 
    		DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) throws WorkflowException {
        Collections.sort(requests, new Utilities.PrioritySorter());
        String activationType = nodeInstance.getRouteNode().getActivationType();
        if (StringUtils.isBlank(activationType)) {
            // not sure if this is really necessary, but preserves behavior prior to introduction of priority-parallel activation
            activationType = KewApiConstants.ROUTE_LEVEL_SEQUENCE;
        }
        boolean isParallel = KewApiConstants.ROUTE_LEVEL_PARALLEL.equals(activationType);
        boolean isPriorityParallel = KewApiConstants.ROUTE_LEVEL_PRIORITY_PARALLEL.equals(activationType);
        boolean isSequential = KewApiConstants.ROUTE_LEVEL_SEQUENCE.equals(activationType);

        boolean activatedApproveRequest = false;
        if (CollectionUtils.isNotEmpty(requests)) {
            // if doing priority-parallel
            int currentPriority = requests.get(0).getPriority();
            for (ActionRequestValue request : requests ) {
                if (request.getParentActionRequest() != null || request.getNodeInstance() == null) {
                    // 1. disregard request if it's not a top-level request
                    // 2. disregard request if it's a "future" request and hasn't been attached to a node instance yet
                    continue;
                }
                if (activatedApproveRequest && (!context.isSimulation() || !context.getActivationContext().isActivateRequests())) {
                    if (isSequential || (isPriorityParallel && request.getPriority() != currentPriority)) {
                        break;
                    }
                }
                currentPriority = request.getPriority();
                if (request.isActive()) {
                    activatedApproveRequest = activatedApproveRequest || request.isApproveOrCompleteRequest();
                    continue;
                }
                logProcessingMessage(request);
                if ( LOG.isDebugEnabled() ) {
            	    LOG.debug("Activating request: " + request);
                }
                activatedApproveRequest = activateRequest(context, request, nodeInstance, generatedActionItems) || activatedApproveRequest;
            }
        }
        return activatedApproveRequest;
    }
    
    protected boolean activateRequest(RouteContext context, ActionRequestValue actionRequest, RouteNodeInstance nodeInstance, List generatedActionItems) {
        if (actionRequest.isRoleRequest()) {
            List actionRequests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocIdAtRouteNode(actionRequest.getDocumentId(), nodeInstance.getRouteNodeInstanceId());
            for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
                ActionRequestValue siblingRequest = (ActionRequestValue) iterator.next();
                if (actionRequest.getRoleName().equals(siblingRequest.getRoleName())) {
                    generatedActionItems.addAll(KEWServiceLocator.getActionRequestService().activateRequestNoNotification(siblingRequest, context.getActivationContext()));
                }
            }
        }
        generatedActionItems.addAll(KEWServiceLocator.getActionRequestService().activateRequestNoNotification(actionRequest, context.getActivationContext()));
        return actionRequest.isApproveOrCompleteRequest() && ! actionRequest.isDone();
    }
    
    protected void saveActionRequest(RouteContext context, ActionRequestValue actionRequest) {
        if (!context.isSimulation()) {
            KEWServiceLocator.getActionRequestService().saveActionRequest(actionRequest);
        } else {
            actionRequest.setActionRequestId(String.valueOf(generatedRequestPriority++));
            context.getEngineState().getGeneratedRequests().add(actionRequest);    
        }
        
    }
    
    protected void saveDocument(RouteContext context, DocumentRouteHeaderValue document) {
        if (!context.isSimulation()) {
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(document);
        }
    }

    protected void logProcessingMessage(ActionRequestValue request) {
        if (LOG.isDebugEnabled()) {
                RouteNodeInstance nodeInstance = request.getNodeInstance();
            StringBuffer buffer = new StringBuffer();
            buffer.append("Processing AR: ").append(request.getActionRequestId()).append("\n");
            buffer.append("AR Node Name: ").append(nodeInstance != null ? nodeInstance.getName() : "null").append("\n");
            buffer.append("AR RouteLevel: ").append(request.getRouteLevel()).append("\n");
            buffer.append("AR Request Code: ").append(request.getActionRequested()).append("\n");
            buffer.append("AR Request priority: ").append(request.getPriority()).append("\n");
            LOG.debug(buffer);
        }
    }
            
}
