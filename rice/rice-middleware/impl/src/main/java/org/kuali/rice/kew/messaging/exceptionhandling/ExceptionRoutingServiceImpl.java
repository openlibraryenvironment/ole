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
package org.kuali.rice.kew.messaging.exceptionhandling;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.MDC;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.exception.RouteManagerException;
import org.kuali.rice.kew.exception.WorkflowDocumentExceptionRoutingService;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.role.RoleRouteModule;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;


public class ExceptionRoutingServiceImpl implements WorkflowDocumentExceptionRoutingService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ExceptionRoutingServiceImpl.class);

    public void placeInExceptionRouting(String errorMessage, PersistedMessageBO persistedMessage, String documentId) throws Exception {
 	 	RouteNodeInstance nodeInstance = null;
 	 	KEWServiceLocator.getRouteHeaderService().lockRouteHeader(documentId, true);
 	 	DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
 	 	RouteContext routeContext = establishRouteContext(document, null);
 	 	List<RouteNodeInstance> activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(documentId);
 	 	if (!activeNodeInstances.isEmpty()) {
 	 		// take the first active nodeInstance found.
 	 		nodeInstance = activeNodeInstances.get(0);
 	 	}
 	 	placeInExceptionRouting(errorMessage, nodeInstance, persistedMessage, routeContext, document, true);
 	 }
    
    public void placeInExceptionRouting(Throwable throwable, PersistedMessageBO persistedMessage, String documentId) throws Exception {
    	placeInExceptionRouting(throwable, persistedMessage, documentId, true);
    }
    
    /**
     * In our case here, our last ditch effort to put the document into exception routing will try to do so without invoking
     * the Post Processor for do route status change to "Exception" status.
     */
    public void placeInExceptionRoutingLastDitchEffort(Throwable throwable, PersistedMessageBO persistedMessage, String documentId) throws Exception {
    	placeInExceptionRouting(throwable, persistedMessage, documentId, false);
    }
    
    protected void placeInExceptionRouting(Throwable throwable, PersistedMessageBO persistedMessage, String documentId, boolean invokePostProcessor) throws Exception {
    	KEWServiceLocator.getRouteHeaderService().lockRouteHeader(documentId, true);
    	DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
    	throwable = unwrapRouteManagerExceptionIfPossible(throwable);
        RouteContext routeContext = establishRouteContext(document, throwable);
        RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
    	Throwable cause = determineActualCause(throwable, 0);
        String errorMessage = (cause != null && cause.getMessage() != null) ? cause.getMessage() : "";
    	placeInExceptionRouting(errorMessage, nodeInstance, persistedMessage, routeContext, document, invokePostProcessor);
    }
    
    protected void placeInExceptionRouting(String errorMessage, RouteNodeInstance nodeInstance, PersistedMessageBO persistedMessage, RouteContext routeContext, DocumentRouteHeaderValue document, boolean invokePostProcessor) throws Exception {
    	String documentId = document.getDocumentId();
        MDC.put("docId", documentId);
        PerformanceLogger performanceLogger = new PerformanceLogger(documentId);
        try {

            // mark all active requests to initialized and delete the action items
            List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(documentId);
            for (ActionRequestValue actionRequest : actionRequests) {
                if (actionRequest.isActive()) {
                    actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());
                    for (ActionItem actionItem : actionRequest.getActionItems()) {
                        KEWServiceLocator.getActionListService().deleteActionItem(actionItem);
                    }
                    KEWServiceLocator.getActionRequestService().saveActionRequest(actionRequest);
                }
            }

            LOG.debug("Generating exception request for doc : " + documentId);
            if (errorMessage == null) {
            	errorMessage = "";
            }
            if (errorMessage.length() > KewApiConstants.MAX_ANNOTATION_LENGTH) {
                errorMessage = errorMessage.substring(0, KewApiConstants.MAX_ANNOTATION_LENGTH);
            }
            List<ActionRequestValue> exceptionRequests = new ArrayList<ActionRequestValue>();
            if (nodeInstance.getRouteNode().isExceptionGroupDefined()) {
            	exceptionRequests = generateExceptionGroupRequests(routeContext);
            } else {
            	exceptionRequests = generateKimExceptionRequests(routeContext);
            }
            if (exceptionRequests.isEmpty()) {
                LOG.warn("Failed to generate exception requests for exception routing!");
            }
            activateExceptionRequests(routeContext, exceptionRequests, errorMessage, invokePostProcessor);

            if (persistedMessage == null) {
                LOG.warn("Attempting to delete null persisted message.");
            } else {
                KSBServiceLocator.getMessageQueueService().delete(persistedMessage);
            }
        } finally {
            performanceLogger.log("Time to generate exception request.");
            MDC.remove("docId");
        }
    }

    protected void notifyStatusChange(DocumentRouteHeaderValue routeHeader, String newStatusCode, String oldStatusCode) throws InvalidActionTakenException {
        DocumentRouteStatusChange statusChangeEvent = new DocumentRouteStatusChange(routeHeader.getDocumentId(), routeHeader.getAppDocId(), oldStatusCode, newStatusCode);
        try {
            LOG.debug("Notifying post processor of status change "+oldStatusCode+"->"+newStatusCode);
            PostProcessor postProcessor = routeHeader.getDocumentType().getPostProcessor();
            ProcessDocReport report = postProcessor.doRouteStatusChange(statusChangeEvent);
            if (!report.isSuccess()) {
                LOG.warn(report.getMessage(), report.getProcessException());
                throw new InvalidActionTakenException(report.getMessage());
            }
        } catch (Exception ex) {
            LOG.warn(ex, ex);
            throw new WorkflowRuntimeException(ex);
        }
    }
    
    protected List<ActionRequestValue> generateExceptionGroupRequests(RouteContext routeContext) {
    	RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
    	ActionRequestFactory arFactory = new ActionRequestFactory(routeContext.getDocument(), null);
    	ActionRequestValue exceptionRequest = arFactory.createActionRequest(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, new Integer(0), new KimGroupRecipient(nodeInstance.getRouteNode().getExceptionWorkgroup()), "Exception Workgroup for route node " + nodeInstance.getName(), KewApiConstants.EXCEPTION_REQUEST_RESPONSIBILITY_ID, Boolean.TRUE, "");
    	return Collections.singletonList(exceptionRequest);
    }
    
    protected List<ActionRequestValue> generateKimExceptionRequests(RouteContext routeContext) throws Exception {
    	RoleRouteModule roleRouteModule = new RoleRouteModule();
    	roleRouteModule.setNamespace(KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE);
    	roleRouteModule.setResponsibilityTemplateName(KewApiConstants.EXCEPTION_ROUTING_RESPONSIBILITY_TEMPLATE_NAME);
    	List<ActionRequestValue> requests = roleRouteModule.findActionRequests(routeContext);
    	processExceptionRequests(requests);
    	return requests;
    }
    
    
    
    /**
     * Takes the given list of Action Requests and ensures their attributes are set properly for exception
     * routing requests.  Namely, this ensures that all "force action" values are set to "true".
     */
    protected void processExceptionRequests(List<ActionRequestValue> exceptionRequests) {
    	if (exceptionRequests != null) {
    		for (ActionRequestValue actionRequest : exceptionRequests) {
    			processExceptionRequest(actionRequest);
    		}
    	}
    }
    
    /**
     * Processes a single exception request, ensuring that it's force action flag is set to true and it's node instance is set to null.
     * It then recurses through any children requests.
     */
    protected void processExceptionRequest(ActionRequestValue actionRequest) {
    	actionRequest.setForceAction(true);
    	actionRequest.setNodeInstance(null);
    	processExceptionRequests(actionRequest.getChildrenRequests());
    }
    
    /**
     * End IU Customization
     * @param routeContext
     * @param exceptionRequests
     * @param exceptionMessage
     * @throws Exception
     */
    
    protected void activateExceptionRequests(RouteContext routeContext, List<ActionRequestValue> exceptionRequests, String exceptionMessage, boolean invokePostProcessor) throws Exception {
    	setExceptionAnnotations(exceptionRequests, exceptionMessage);
    	// TODO is there a reason we reload the document here?
    	DocumentRouteHeaderValue rh = KEWServiceLocator.getRouteHeaderService().getRouteHeader(routeContext.getDocument().getDocumentId());
    	String oldStatus = rh.getDocRouteStatus();
    	rh.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_EXCEPTION_CD);
    	if (invokePostProcessor) {
    		notifyStatusChange(rh, KewApiConstants.ROUTE_HEADER_EXCEPTION_CD, oldStatus);
    	}
    	KEWServiceLocator.getRouteHeaderService().saveRouteHeader(rh);
    	KEWServiceLocator.getActionRequestService().activateRequests(exceptionRequests);
    }
    
    /**
     * Sets the exception message as the annotation on the top-level Action Requests
     */
    protected void setExceptionAnnotations(List<ActionRequestValue> actionRequests, String exceptionMessage) {
    	for (ActionRequestValue actionRequest : actionRequests) {
    		actionRequest.setAnnotation(exceptionMessage);
    	}
    }

    private Throwable unwrapRouteManagerExceptionIfPossible(Throwable throwable) {
    	if (throwable instanceof InvocationTargetException) {
    		throwable = throwable.getCause();
    	}
    	if (throwable != null && (! (throwable instanceof RouteManagerException)) && throwable.getCause() instanceof RouteManagerException) {
    		throwable = throwable.getCause();
    	}
    	return throwable;
    }

    protected Throwable determineActualCause(Throwable throwable, int depth) {
    	if (depth >= 10) {
    		return throwable;
    	}
    	if ((throwable instanceof InvocationTargetException) || (throwable instanceof RouteManagerException)) {
    		if (throwable.getCause() != null) {
    			return determineActualCause(throwable.getCause(), ++depth);
    		}
    	}
    	return throwable;
    }
    
    protected RouteContext establishRouteContext(DocumentRouteHeaderValue document, Throwable throwable) {
    	RouteContext routeContext = new RouteContext();
        if (throwable instanceof RouteManagerException) {
            RouteManagerException rmException = (RouteManagerException) throwable;
            routeContext = rmException.getRouteContext();
        } else {
        	routeContext.setDocument(document);
            List<RouteNodeInstance> activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
            if (!activeNodeInstances.isEmpty()) {
                // take the first active nodeInstance found.
                RouteNodeInstance nodeInstance = (RouteNodeInstance) activeNodeInstances.get(0);
                routeContext.setNodeInstance(nodeInstance);
            }
        }
        if (routeContext.getNodeInstance() == null) {
            // get the initial node instance
            routeContext.setNodeInstance((RouteNodeInstance) document.getInitialRouteNodeInstances().get(0));
        }
        return routeContext;
    }
}
