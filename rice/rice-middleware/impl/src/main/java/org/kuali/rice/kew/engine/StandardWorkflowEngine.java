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
package org.kuali.rice.kew.engine;

import org.apache.log4j.MDC;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.ProcessResult;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.RouteNodeUtils;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.engine.transition.Transition;
import org.kuali.rice.kew.engine.transition.TransitionEngine;
import org.kuali.rice.kew.engine.transition.TransitionEngineFactory;
import org.kuali.rice.kew.exception.RouteManagerException;
import org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.krad.util.KRADConstants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * The standard and supported implementation of the WorkflowEngine.  Runs a processing loop against a given
 * Document, processing nodes on the document until the document is completed or a node halts the
 * processing.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StandardWorkflowEngine implements WorkflowEngine {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(StandardWorkflowEngine.class);

	protected final RouteHelper helper = new RouteHelper();
	protected RouteNodeService routeNodeService;
    protected RouteHeaderService routeHeaderService;
    protected ParameterService parameterService;
    protected OrchestrationConfig config;

    public StandardWorkflowEngine() {}

	protected StandardWorkflowEngine(RouteNodeService routeNodeService, RouteHeaderService routeHeaderService, 
	        ParameterService parameterService, OrchestrationConfig config) {
	    this.routeNodeService = routeNodeService;
	    this.routeHeaderService = routeHeaderService;
	    this.parameterService = parameterService;
	    this.config = config;
	}

//	public void setRunPostProcessorLogic(boolean runPostProcessorLogic) {
//	    this.runPostProcessorLogic = runPostProcessorLogic;
//	}

	public boolean isRunPostProcessorLogic() {
	    return this.config.isRunPostProcessorLogic();
	}

	public void process(String documentId, String nodeInstanceId) throws Exception {
		if (documentId == null) {
			throw new IllegalArgumentException("Cannot process a null document id.");
		}
		MDC.put("docId", documentId);
		boolean success = true;
		RouteContext context = RouteContext.createNewRouteContext();
		try {
			if ( LOG.isInfoEnabled() ) {
				LOG.info("Aquiring lock on document " + documentId);
			}
			KEWServiceLocator.getRouteHeaderService().lockRouteHeader(documentId, true);
			if ( LOG.isInfoEnabled() ) {
				LOG.info("Aquired lock on document " + documentId);
			}

			DocumentRouteHeaderValue document = getRouteHeaderService().getRouteHeader(documentId);
			context.setDocument(document);
			lockAdditionalDocuments(document);

			if ( LOG.isInfoEnabled() ) {
				LOG.info("Processing document: " + documentId + " : " + nodeInstanceId);
			}

			try {
	            document = notifyPostProcessorBeforeProcess(document, nodeInstanceId);
	            context.setDocument(document);
            } catch (Exception e) {
                LOG.warn("Problems contacting PostProcessor before engine process", e);
                throw new RouteManagerException("Problems contacting PostProcessor:  " + e.getMessage());
            }
            if (!document.isRoutable()) {
				LOG.debug("Document not routable so returning with doing no action");
				return;
			}
			List<RouteNodeInstance> nodeInstancesToProcess = new LinkedList<RouteNodeInstance>();
			if (nodeInstanceId == null) {
				// pulls the node instances from the passed in document
				nodeInstancesToProcess.addAll(RouteNodeUtils.getActiveNodeInstances(document));
			} else {
				RouteNodeInstance instanceNode = RouteNodeUtils.findRouteNodeInstanceById(nodeInstanceId,document);
				if (instanceNode == null) {
					throw new IllegalArgumentException("Invalid node instance id: " + nodeInstanceId);
				}
				nodeInstancesToProcess.add(instanceNode);
			}

			context.setEngineState(new EngineState());
			ProcessContext processContext = new ProcessContext(true, nodeInstancesToProcess);
			try {
				while (!nodeInstancesToProcess.isEmpty()) {
					context.setNodeInstance((RouteNodeInstance) nodeInstancesToProcess.remove(0));
					processContext = processNodeInstance(context, helper);
					if (processContext.isComplete() && !processContext.getNextNodeInstances().isEmpty()) {
						nodeInstancesToProcess.addAll(processContext.getNextNodeInstances());
					}
				}
				context.setDocument(nodePostProcess(context));
			} catch (Exception e) {
				success = false;
				// TODO throw a new 'RoutingException' which holds the
				// RoutingState
				throw new RouteManagerException(e, context);
			}
		} finally {
			if ( LOG.isInfoEnabled() ) {
				LOG.info((success ? "Successfully processed" : "Failed to process") + " document: " + documentId + " : " + nodeInstanceId);
			}
			try {
	            notifyPostProcessorAfterProcess(context.getDocument(), nodeInstanceId, success);
            } catch (Exception e) {
                LOG.warn("Problems contacting PostProcessor after engine process", e);
                throw new RouteManagerException("Problems contacting PostProcessor", e, context);
            }
			RouteContext.clearCurrentRouteContext();
			MDC.remove("docId");
		}
	}

	protected ProcessContext processNodeInstance(RouteContext context, RouteHelper helper) throws Exception {
		RouteNodeInstance nodeInstance = context.getNodeInstance();
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("Processing node instance: " + nodeInstance.getRouteNode().getRouteNodeName());
		}
		if (checkAssertions(context)) {
			// returning an empty context causes the outer loop to terminate
			return new ProcessContext();
		}
		TransitionEngine transitionEngine = TransitionEngineFactory.createTransitionEngine(nodeInstance);
		ProcessResult processResult = transitionEngine.isComplete(context);
		nodeInstance.setInitial(false);

		// if this nodeInstance already has next node instance we don't need to
		// go to the TE
		if (processResult.isComplete()) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug("Routing node has completed: " + nodeInstance.getRouteNode().getRouteNodeName());
			}

			context.getEngineState().getCompleteNodeInstances().add(nodeInstance.getRouteNodeInstanceId());
			List nextNodeCandidates = invokeTransition(context, context.getNodeInstance(), processResult, transitionEngine);

			// iterate over the next node candidates sending them through the
			// transition engine's transitionTo method
			// one at a time for a potential switch. Place the transition
			// engines result back in the 'actual' next node
			// list which we put in the next node before doing work.
			List<RouteNodeInstance> nodesToActivate = new ArrayList<RouteNodeInstance>();
			if (!nextNodeCandidates.isEmpty()) {
				// KULRICE-4274: Hierarchy Routing Node issues
				// No longer change nextNodeInstances in place, instead we create a local and assign our local list below
				// the loop so the post processor doesn't save a RouteNodeInstance in an intermediate state
				ArrayList<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();

				for (Iterator nextIt = nextNodeCandidates.iterator(); nextIt.hasNext();) {
					RouteNodeInstance nextNodeInstance = (RouteNodeInstance) nextIt.next();
					transitionEngine = TransitionEngineFactory.createTransitionEngine(nextNodeInstance);
					RouteNodeInstance currentNextNodeInstance = nextNodeInstance;
					nextNodeInstance = transitionEngine.transitionTo(nextNodeInstance, context);
					// if the next node has changed, we need to remove our
					// current node as a next node of the original node
					if (!currentNextNodeInstance.equals(nextNodeInstance)) {
						currentNextNodeInstance.getPreviousNodeInstances().remove(nodeInstance);
					}
					// before adding next node instance, be sure that it's not
					// already linked via previous node instances
					// this is to prevent the engine from setting up references
					// on nodes that already reference each other.
					// the primary case being when we are walking over an
					// already constructed graph of nodes returned from a
					// dynamic node - probably a more sensible approach would be
					// to check for the existence of the link and moving on
					// if it's been established.
					nextNodeInstance.getPreviousNodeInstances().remove(nodeInstance);
					nextNodeInstances.add(nextNodeInstance);
					handleBackwardCompatibility(context, nextNodeInstance);
					// call the post processor
					notifyNodeChange(context, nextNodeInstance);
					nodesToActivate.add(nextNodeInstance);
 					// TODO update document content on context?
 				}
 				// assign our local list here so the post processor doesn't save a RouteNodeInstance in an intermediate state
				for (RouteNodeInstance nextNodeInstance : nextNodeInstances) {
					nodeInstance.addNextNodeInstance(nextNodeInstance);
				}
 			}
 
 			// deactive the current active node
			nodeInstance.setComplete(true);
			nodeInstance.setActive(false);
			// active the nodes we're transitioning into
			for (RouteNodeInstance nodeToActivate : nodesToActivate) {
				nodeToActivate.setActive(true);
			}
		} else {
		    nodeInstance.setComplete(false);
        }

		saveNode(context, nodeInstance);
		return new ProcessContext(nodeInstance.isComplete(), nodeInstance.getNextNodeInstances());
	}

	/**
	 * Checks various assertions regarding the processing of the current node.
	 * If this method returns true, then the node will not be processed.
	 *
	 * This method will throw an exception if it deems that the processing is in
	 * a illegal state.
	 */
	private boolean checkAssertions(RouteContext context) throws Exception {
		if (context.getNodeInstance().isComplete()) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug("The node has already been completed: " + context.getNodeInstance().getRouteNode().getRouteNodeName());
			}
			return true;
		}
		if (isRunawayProcessDetected(context.getEngineState())) {
//			 TODO more info in message
			throw new WorkflowException("Detected runaway process.");
		}
		return false;
	}

	/**
	 * Invokes the transition and returns the next node instances to transition
	 * to from the current node instance on the route context.
	 *
	 * This is a 3-step process:
	 *
	 * <pre>
	 *  1) If the node instance already has next nodes, return those,
	 *  2) otherwise, invoke the transition engine for the node, if the resulting node instances are not empty, return those,
	 *  3) lastly, if our node is in a process and no next nodes were returned from it's transition engine, invoke the
	 *     transition engine of the process node and return the resulting node instances.
	 * </pre>
	 */
	/*
	 * private List invokeTransition(RouteContext context, RouteNodeInstance
	 * nodeInstance, ProcessResult processResult, TransitionEngine
	 * transitionEngine) throws Exception { List nextNodeInstances =
	 * nodeInstance.getNextNodeInstances(); if (nextNodeInstances.isEmpty()) {
	 * Transition result = transitionEngine.transitionFrom(context,
	 * processResult); nextNodeInstances = result.getNextNodeInstances(); if
	 * (nextNodeInstances.isEmpty() && nodeInstance.isInProcess()) {
	 * transitionEngine =
	 * TransitionEngineFactory.createTransitionEngine(nodeInstance.getProcess());
	 * nextNodeInstances = invokeTransition(context, nodeInstance.getProcess(),
	 * processResult, transitionEngine); } } return nextNodeInstances; }
	 */

	private List invokeTransition(RouteContext context, RouteNodeInstance nodeInstance, ProcessResult processResult, TransitionEngine transitionEngine) throws Exception {
		List nextNodeInstances = nodeInstance.getNextNodeInstances();
		if (nextNodeInstances.isEmpty()) {
			Transition result = transitionEngine.transitionFrom(context, processResult);
			nextNodeInstances = result.getNextNodeInstances();
			if (nextNodeInstances.isEmpty() && nodeInstance.isInProcess()) {
				transitionEngine = TransitionEngineFactory.createTransitionEngine(nodeInstance.getProcess());
				context.setNodeInstance(nodeInstance);
				nextNodeInstances = invokeTransition(context, nodeInstance.getProcess(), processResult, transitionEngine);
			}
		}
		return nextNodeInstances;
	}

	/*
	 * private List invokeTransition(RouteContext context, RouteNodeInstance
	 * process, ProcessResult processResult) throws Exception {
	 * RouteNodeInstance nodeInstance = (context.getRouteNodeInstance() ; List
	 * nextNodeInstances = nodeInstance.getNextNodeInstances(); if
	 * (nextNodeInstances.isEmpty()) { TransitionEngine transitionEngine =
	 * TransitionEngineFactory.createTransitionEngine(nodeInstance); Transition
	 * result = transitionEngine.transitionFrom(context, processResult);
	 * nextNodeInstances = result.getNextNodeInstances(); if
	 * (nextNodeInstances.isEmpty() && nodeInstance.isInProcess()) {
	 * transitionEngine =
	 * TransitionEngineFactory.createTransitionEngine(nodeInstance.getProcess());
	 * nextNodeInstances = invokeTransition(context, nodeInstance.getProcess(),
	 * processResult, transitionEngine); } } return nextNodeInstances; }
	 *
	 */private void notifyNodeChange(RouteContext context, RouteNodeInstance nextNodeInstance) throws Exception {
		if (!context.isSimulation()) {
			RouteNodeInstance nodeInstance = context.getNodeInstance();
			// if application document status transition has been defined, update the status
			String nextStatus = nodeInstance.getRouteNode().getNextDocStatus();
			if (nextStatus != null && nextStatus.length() > 0){
				context.getDocument().updateAppDocStatus(nextStatus);
			}

			DocumentRouteLevelChange event = new DocumentRouteLevelChange(context.getDocument().getDocumentId(), context.getDocument().getAppDocId(), CompatUtils.getLevelForNode(context.getDocument().getDocumentType(), context.getNodeInstance()
					.getRouteNode().getRouteNodeName()), CompatUtils.getLevelForNode(context.getDocument().getDocumentType(), nextNodeInstance.getRouteNode().getRouteNodeName()), nodeInstance.getRouteNode().getRouteNodeName(), nextNodeInstance
					.getRouteNode().getRouteNodeName(), nodeInstance.getRouteNodeInstanceId(), nextNodeInstance.getRouteNodeInstanceId());
			context.setDocument(notifyPostProcessor(context.getDocument(), nodeInstance, event));
		}
	}

	private void handleBackwardCompatibility(RouteContext context, RouteNodeInstance nextNodeInstance) {
		context.getDocument().setDocRouteLevel(new Integer(context.getDocument().getDocRouteLevel().intValue() + 1)); // preserve
																														// route
																														// level
																														// concept
																														// if
																														// possible
		saveDocument(context);
	}

	private void saveDocument(RouteContext context) {
		if (!context.isSimulation()) {
			getRouteHeaderService().saveRouteHeader(context.getDocument());
		}
	}

	private void saveBranch(RouteContext context, Branch branch) {
		if (!context.isSimulation()) {
			KEWServiceLocator.getRouteNodeService().save(branch);
		}
	}

	protected void saveNode(RouteContext context, RouteNodeInstance nodeInstance) {
		if (!context.isSimulation()) {
			getRouteNodeService().save(nodeInstance);
		} else {
			// if we are in simulation mode, lets go ahead and assign some id
			// values to our beans
			for (Iterator<RouteNodeInstance> iterator = nodeInstance.getNextNodeInstances().iterator(); iterator.hasNext();) {
				RouteNodeInstance routeNodeInstance = (RouteNodeInstance) iterator.next();
				if (routeNodeInstance.getRouteNodeInstanceId() == null) {
					routeNodeInstance.setRouteNodeInstanceId(context.getEngineState().getNextSimulationId());
				}
			}
			if (nodeInstance.getProcess() != null && nodeInstance.getProcess().getRouteNodeInstanceId() == null) {
				nodeInstance.getProcess().setRouteNodeInstanceId(context.getEngineState().getNextSimulationId());
			}
			if (nodeInstance.getBranch() != null && nodeInstance.getBranch().getBranchId() == null) {
				nodeInstance.getBranch().setBranchId(context.getEngineState().getNextSimulationId());
			}
		}
	}

	// TODO extract this into some sort of component which handles transitioning
	// document state
	protected DocumentRouteHeaderValue nodePostProcess(RouteContext context) throws InvalidActionTakenException {
		DocumentRouteHeaderValue document = context.getDocument();
        Collection<RouteNodeInstance> activeNodes = RouteNodeUtils.getActiveNodeInstances(document);
        boolean moreNodes = false;
		for (Iterator<RouteNodeInstance> iterator = activeNodes.iterator(); iterator.hasNext();) {
			RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
			moreNodes = moreNodes || !nodeInstance.isComplete();
		}
		List pendingRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
		boolean activeApproveRequests = false;
		boolean activeAckRequests = false;
		for (Iterator iterator = pendingRequests.iterator(); iterator.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iterator.next();
			activeApproveRequests = request.isApproveOrCompleteRequest() || activeApproveRequests;
			activeAckRequests = request.isAcknowledgeRequest() || activeAckRequests;
		}
		// TODO is the logic for going processed still going to be valid?
		if (!document.isProcessed() && (!moreNodes || !activeApproveRequests)) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug("No more nodes for this document " + document.getDocumentId());
			}
			// TODO perhaps the policies could also be factored out?
			checkDefaultApprovalPolicy(document);
            document.setApprovedDate(new Timestamp(System.currentTimeMillis()));

			LOG.debug("Marking document processed");
			DocumentRouteStatusChange event = new DocumentRouteStatusChange(document.getDocumentId(), document.getAppDocId(), document.getDocRouteStatus(), KewApiConstants.ROUTE_HEADER_PROCESSED_CD);
			document.markDocumentProcessed();
			// saveDocument(context);
			notifyPostProcessor(context, event);
		}

		// if document is processed and no pending action requests put the
		// document into the finalized state.
		if (document.isProcessed()) {
			DocumentRouteStatusChange event = new DocumentRouteStatusChange(document.getDocumentId(), document.getAppDocId(), document.getDocRouteStatus(), KewApiConstants.ROUTE_HEADER_FINAL_CD);
			List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
			if (actionRequests.isEmpty()) {
				document.markDocumentFinalized();
				// saveDocument(context);
				notifyPostProcessor(context, event);
			} else {
				boolean markFinalized = true;
				for (Iterator iter = actionRequests.iterator(); iter.hasNext();) {
					ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
					if (KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ.equals(actionRequest.getActionRequested())) {
						markFinalized = false;
					}
				}
				if (markFinalized) {
					document.markDocumentFinalized();
					// saveDocument(context);
					this.notifyPostProcessor(context, event);
				}
			}
		}
		saveDocument(context);
		return document;
	}

	/**
	 * Check the default approval policy for the document. If the default
	 * approval policy is no and no approval action requests have been created
	 * then throw an execption so that the document will get thrown into
	 * exception routing.
	 *
	 * @throws RouteManagerException
	 */
	private void checkDefaultApprovalPolicy(DocumentRouteHeaderValue document) throws RouteManagerException {
		if (!document.getDocumentType().getDefaultApprovePolicy().getPolicyValue().booleanValue()) {
			LOG.debug("Checking if any requests have been generated for the document");
			List requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
			boolean approved = false;
			for (Iterator iter = requests.iterator(); iter.hasNext();) {
				ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
				if (actionRequest.isApproveOrCompleteRequest() && actionRequest.isDone()) { // &&
																							// !(actionRequest.getRouteMethodName().equals(KewApiConstants.ADHOC_ROUTE_MODULE_NAME)
																							// &&
																							// actionRequest.isReviewerUser()
																							// &&
																							// document.getInitiatorWorkflowId().equals(actionRequest.getWorkflowId())))
																							// {
					LOG.debug("Found at least one processed approve request so document can be approved");
					approved = true;
					break;
				}
			}
			if (!approved) {
				LOG.debug("Document requires at least one request and none are present");
				// TODO what route method name to pass to this?
				throw new RouteManagerException("Document should have generated at least one approval request.");
			}
		}
	}

	private DocumentRouteHeaderValue notifyPostProcessor(RouteContext context, DocumentRouteStatusChange event) {
		DocumentRouteHeaderValue document = context.getDocument();
		if (context.isSimulation()) {
			return document;
		}
		if (hasContactedPostProcessor(context, event)) {
			return document;
		}
		String documentId = event.getDocumentId();
		PerformanceLogger performanceLogger = new PerformanceLogger(documentId);
		ProcessDocReport processReport = null;
		PostProcessor postProc = null;
        try {
            // use the document's post processor unless specified by the runPostProcessorLogic not to
            if (!isRunPostProcessorLogic()) {
                postProc = new DefaultPostProcessor();
            } else {
                postProc = document.getDocumentType().getPostProcessor();
            }
        } catch (Exception e) {
            LOG.error("Error retrieving PostProcessor for document " + document.getDocumentId(), e);
            throw new RouteManagerException("Error retrieving PostProcessor for document " + document.getDocumentId(), e);
        }
		try {
			processReport = postProc.doRouteStatusChange(event);
		} catch (Exception e) {
			LOG.error("Error notifying post processor", e);
			throw new RouteManagerException(KewApiConstants.POST_PROCESSOR_FAILURE_MESSAGE, e);
		} finally {
			performanceLogger.log("Time to notifyPostProcessor of event " + event.getDocumentEventCode() + ".");
		}

		if (!processReport.isSuccess()) {
			LOG.warn("PostProcessor failed to process document: " + processReport.getMessage());
			throw new RouteManagerException(KewApiConstants.POST_PROCESSOR_FAILURE_MESSAGE + processReport.getMessage());
		}
		return document;
	}

	/**
	 * Returns true if the post processor has already been contacted about a
	 * PROCESSED or FINAL post processor change. If the post processor has not
	 * been contacted, this method will record on the document that it has been.
	 *
	 * This is because, in certain cases, a document could end up in exception
	 * routing after it has already gone PROCESSED or FINAL (i.e. on Mass Action
	 * processing) and we don't want to re-contact the post processor in these
	 * cases.
	 */
	private boolean hasContactedPostProcessor(RouteContext context, DocumentRouteStatusChange event) {
		// get the initial node instance, the root branch is where we will store
		// the state
		Branch rootBranch = context.getDocument().getRootBranch();
		String key = null;
		if (KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equals(event.getNewRouteStatus())) {
			key = KewApiConstants.POST_PROCESSOR_PROCESSED_KEY;
		} else if (KewApiConstants.ROUTE_HEADER_FINAL_CD.equals(event.getNewRouteStatus())) {
			key = KewApiConstants.POST_PROCESSOR_FINAL_KEY;
		} else {
			return false;
		}
		BranchState branchState = null;
		if (rootBranch != null) {
		    branchState = rootBranch.getBranchState(key);
		} else {
		    return false;
		}
		if (branchState == null) {
			branchState = new BranchState();
			branchState.setKey(key);
			branchState.setValue("true");
			rootBranch.addBranchState(branchState);
			saveBranch(context, rootBranch);
			return false;
		}
		return "true".equals(branchState.getValue());
	}

	/**
	 * TODO in some cases, someone may modify the route header in the post
	 * processor, if we don't save before and reload after we will get an
	 * optimistic lock exception, we need to work on a better solution for this!
	 * TODO get the routeContext in this method - it should be a better object
	 * than the nodeInstance
	 */
	private DocumentRouteHeaderValue notifyPostProcessor(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance, DocumentRouteLevelChange event) {
		getRouteHeaderService().saveRouteHeader(document);
		ProcessDocReport report = null;
		try {
	        PostProcessor postProcessor = null;
	        // use the document's post processor unless specified by the runPostProcessorLogic not to
	        if (!isRunPostProcessorLogic()) {
	            postProcessor = new DefaultPostProcessor();
	        } else {
	            postProcessor = document.getDocumentType().getPostProcessor();
	        }
			report = postProcessor.doRouteLevelChange(event);
		} catch (Exception e) {
			LOG.warn("Problems contacting PostProcessor", e);
			throw new RouteManagerException("Problems contacting PostProcessor:  " + e.getMessage());
		}
		document = getRouteHeaderService().getRouteHeader(document.getDocumentId());
		if (!report.isSuccess()) {
			LOG.error("PostProcessor rejected route level change::" + report.getMessage(), report.getProcessException());
			throw new RouteManagerException("Route Level change failed in post processor::" + report.getMessage());
		}
		return document;
	}

    /**
     * TODO get the routeContext in this method - it should be a better object
     * than the nodeInstance
     */
	private DocumentRouteHeaderValue notifyPostProcessorBeforeProcess(DocumentRouteHeaderValue document, String nodeInstanceId) {
	    return notifyPostProcessorBeforeProcess(document, nodeInstanceId, new BeforeProcessEvent(document.getDocumentId(),document.getAppDocId(),nodeInstanceId));
	}

    /**
     * TODO get the routeContext in this method - it should be a better object
     * than the nodeInstance
     */
    private DocumentRouteHeaderValue notifyPostProcessorBeforeProcess(DocumentRouteHeaderValue document, String nodeInstanceId, BeforeProcessEvent event) {
        ProcessDocReport report = null;
        try {
            PostProcessor postProcessor = null;
            // use the document's post processor unless specified by the runPostProcessorLogic not to
            if (!isRunPostProcessorLogic()) {
                postProcessor = new DefaultPostProcessor();
            } else {
                postProcessor = document.getDocumentType().getPostProcessor();
            }
            report = postProcessor.beforeProcess(event);
        } catch (Exception e) {
            LOG.warn("Problems contacting PostProcessor", e);
            throw new RouteManagerException("Problems contacting PostProcessor:  " + e.getMessage());
        }
        document = getRouteHeaderService().getRouteHeader(document.getDocumentId());
        if (!report.isSuccess()) {
            LOG.error("PostProcessor rejected route level change::" + report.getMessage(), report.getProcessException());
            throw new RouteManagerException("Route Level change failed in post processor::" + report.getMessage());
        }
        return document;
    }

    protected void lockAdditionalDocuments(DocumentRouteHeaderValue document) throws Exception {
		DocumentLockingEvent lockingEvent = new DocumentLockingEvent(document.getDocumentId(), document.getAppDocId());
		// TODO this shows up in a few places and could totally be extracted to a method
		PostProcessor postProcessor = null;
        // use the document's post processor unless specified by the runPostProcessorLogic not to
        if (!isRunPostProcessorLogic()) {
            postProcessor = new DefaultPostProcessor();
        } else {
            postProcessor = document.getDocumentType().getPostProcessor();
        }
        List<String> documentIdsToLock = postProcessor.getDocumentIdsToLock(lockingEvent);
        if (documentIdsToLock != null && !documentIdsToLock.isEmpty()) {
        	for (String documentId : documentIdsToLock) {
        		if ( LOG.isInfoEnabled() ) {
    				LOG.info("Aquiring additional lock on document " + documentId);
    			}
        		getRouteHeaderService().lockRouteHeader(documentId, true);
        		if ( LOG.isInfoEnabled() ) {
        			LOG.info("Aquired lock on document " + documentId);
        		}
        	}
        }
	}

    /**
     * TODO get the routeContext in this method - it should be a better object
     * than the nodeInstance
     */
    private DocumentRouteHeaderValue notifyPostProcessorAfterProcess(DocumentRouteHeaderValue document, String nodeInstanceId, boolean successfullyProcessed) {
    	if (document == null) {
    		// this could happen if we failed to acquire the lock on the document
    		return null;
    	}
        return notifyPostProcessorAfterProcess(document, nodeInstanceId, new AfterProcessEvent(document.getDocumentId(),document.getAppDocId(),nodeInstanceId,successfullyProcessed));
    }

    /**
     * TODO get the routeContext in this method - it should be a better object
     * than the nodeInstance
     */
    private DocumentRouteHeaderValue notifyPostProcessorAfterProcess(DocumentRouteHeaderValue document, String nodeInstanceId, AfterProcessEvent event) {
        ProcessDocReport report = null;
        try {
            PostProcessor postProcessor = null;
            // use the document's post processor unless specified by the runPostProcessorLogic not to
            if (!isRunPostProcessorLogic()) {
                postProcessor = new DefaultPostProcessor();
            } else {
                postProcessor = document.getDocumentType().getPostProcessor();
            }
            report = postProcessor.afterProcess(event);
        } catch (Exception e) {
            throw new RouteManagerException("Problems contacting PostProcessor.",e);
        }
        document = getRouteHeaderService().getRouteHeader(document.getDocumentId());
        if (!report.isSuccess()) {
            LOG.error("PostProcessor rejected route level change::" + report.getMessage(), report.getProcessException());
            throw new RouteManagerException("Route Level change failed in post processor::" + report.getMessage());
        }
        return document;
    }

	/**
	 * This method initializes the document by materializing and activating the
	 * first node instance on the document.
	 */
	public void initializeDocument(DocumentRouteHeaderValue document) {
		// we set up a local route context here just so that we are able to
		// utilize the saveNode method at the end of
		// this method. Incidentally, this was changed from pulling the existing
		// context out because it would override
		// the document in the route context in the case of a document being
		// initialized for reporting purposes.
		RouteContext context = new RouteContext();
		context.setDocument(document);

		if (context.getEngineState() == null) {
			context.setEngineState(new EngineState());
		}

		ProcessDefinitionBo process = document.getDocumentType().getPrimaryProcess();

        if (process == null) {
            throw new IllegalDocumentTypeException("DocumentType '" + document.getDocumentType().getName() + "' has no primary process configured!");
        }

        if (process.getInitialRouteNode() != null) {
            RouteNodeInstance nodeInstance = helper.getNodeFactory().createRouteNodeInstance(document.getDocumentId(), process.getInitialRouteNode());
            nodeInstance.setActive(true);
            helper.getNodeFactory().createBranch(KewApiConstants.PRIMARY_BRANCH_NAME, null, nodeInstance);
            document.getInitialRouteNodeInstances().add(nodeInstance);
            saveNode(context, nodeInstance);
        }
	}

    private boolean isRunawayProcessDetected(EngineState engineState) throws NumberFormatException {
	    String maxNodesConstant = getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.MAX_NODES_BEFORE_RUNAWAY_PROCESS);
	    int maxNodes = (org.apache.commons.lang.StringUtils.isEmpty(maxNodesConstant)) ? 50 : Integer.valueOf(maxNodesConstant);
	    return engineState.getCompleteNodeInstances().size() > maxNodes;
	}

    protected RouteNodeService getRouteNodeService() {
		return routeNodeService;
	}

	protected RouteHeaderService getRouteHeaderService() {
		return routeHeaderService;
	}

    protected ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = CoreFrameworkServiceLocator.getParameterService();
        }
		return parameterService;
	}

    public void setRouteNodeService(RouteNodeService routeNodeService) {
        this.routeNodeService = routeNodeService;
    }

    public void setRouteHeaderService(RouteHeaderService routeHeaderService) {
        this.routeHeaderService = routeHeaderService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
