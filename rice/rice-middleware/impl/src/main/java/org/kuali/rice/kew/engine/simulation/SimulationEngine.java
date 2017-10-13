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
package org.kuali.rice.kew.engine.simulation;

import org.apache.log4j.MDC;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.exception.DocumentSimulatedRouteException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.EngineState;
import org.kuali.rice.kew.engine.OrchestrationConfig;
import org.kuali.rice.kew.engine.ProcessContext;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.StandardWorkflowEngine;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NoOpNode;
import org.kuali.rice.kew.engine.node.NodeJotter;
import org.kuali.rice.kew.engine.node.NodeType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RequestsNode;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.SimpleNode;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * A WorkflowEngine implementation which runs simulations.  This object is not thread-safe
 * and therefore a new instance needs to be instantiated on every use.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimulationEngine extends StandardWorkflowEngine implements SimulationWorkflowEngine {

    public SimulationEngine () {
        super();
    }
    public SimulationEngine(RouteNodeService routeNodeService, RouteHeaderService routeHeaderService,
            ParameterService parameterService, OrchestrationConfig config) {
        super(routeNodeService, routeHeaderService, parameterService, config);
    }

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SimulationEngine.class);

	private SimulationCriteria criteria;
    private SimulationResults results;

    @Override
    public SimulationResults runSimulation(SimulationCriteria criteria) throws Exception {
        try {
            this.criteria = criteria;
            this.results = new SimulationResults();
            validateCriteria(criteria);
            process(criteria.getDocumentId(), null);
            return results;
        } finally {
            //nulling out the results & criteria since these really should only be local variables.
            this.criteria = null;
            this.results = null;
        }
    }

    @Override
    public void process(String documentId, String nodeInstanceId) throws InvalidActionTakenException, DocumentSimulatedRouteException {
    	RouteContext context = RouteContext.createNewRouteContext();
    	try {
    		ActivationContext activationContext = new ActivationContext(ActivationContext.CONTEXT_IS_SIMULATION);
    		if (criteria.isActivateRequests() == null) {
    		    activationContext.setActivateRequests(!criteria.getActionsToTake().isEmpty());
    		} else {
    		    activationContext.setActivateRequests(criteria.isActivateRequests().booleanValue());
    		}
    		context.setActivationContext(activationContext);
    		context.setEngineState(new EngineState());
    		// suppress policy errors when running a simulation for the purposes of display on the route log
    		RequestsNode.setSuppressPolicyErrors(context);
    		DocumentRouteHeaderValue document = createSimulationDocument(documentId, criteria, context);
            document.setInitiatorWorkflowId("simulation");
    		if ( (criteria.isDocumentSimulation()) && ( (document.isProcessed()) || (document.isFinal()) ) ) {
    			results.setDocument(document);
    			return;
    		}
    		routeDocumentIfNecessary(document, criteria, context);
    		results.setDocument(document);
    		documentId = document.getDocumentId();
    		
    		// detect if MDC already has docId param (to avoid nuking it below)
    		boolean mdcHadDocId = MDC.get("docId") != null;
    		if (!mdcHadDocId) { MDC.put("docId", documentId); }
    		
    		PerformanceLogger perfLog = new PerformanceLogger(documentId);
    		try {
    		    if ( LOG.isInfoEnabled() ) {
    		        LOG.info("Processing document for Simulation: " + documentId);
    		    }
    			List<RouteNodeInstance> activeNodeInstances = getRouteNodeService().getActiveNodeInstances(document);
    			List<RouteNodeInstance> nodeInstancesToProcess = determineNodeInstancesToProcess(activeNodeInstances, criteria.getDestinationNodeName());

    			context.setDocument(document);
    			// TODO set document content
    			context.setEngineState(new EngineState());
    			ProcessContext processContext = new ProcessContext(true, nodeInstancesToProcess);
    			while (! nodeInstancesToProcess.isEmpty()) {
    				RouteNodeInstance nodeInstance = (RouteNodeInstance)nodeInstancesToProcess.remove(0);
    				if ( !nodeInstance.isActive() ) {
    					continue;
    				}
    				NodeJotter.jotNodeInstance(context.getDocument(), nodeInstance);
    				context.setNodeInstance(nodeInstance);
    				processContext = processNodeInstance(context, helper);
    				if (!hasReachedCompletion(processContext, context.getEngineState().getGeneratedRequests(), nodeInstance, criteria)) {
    					if (processContext.isComplete()) {
    						if (!processContext.getNextNodeInstances().isEmpty()) {
    							nodeInstancesToProcess.addAll(processContext.getNextNodeInstances());
    						}
    						context.getActivationContext().getSimulatedActionsTaken().addAll(processPotentialActionsTaken(context, document, nodeInstance, criteria));
    					}
    				} else {
    					context.getActivationContext().getSimulatedActionsTaken().addAll(processPotentialActionsTaken(context, document, nodeInstance, criteria));
    				}
    			}
    			List simulatedActionRequests = context.getEngineState().getGeneratedRequests();
    			Collections.sort(simulatedActionRequests, new Utilities.RouteLogActionRequestSorter());
    			results.setSimulatedActionRequests(simulatedActionRequests);
    			results.setSimulatedActionsTaken(context.getActivationContext().getSimulatedActionsTaken());
            } catch (InvalidActionTakenException e) {
                throw e;
            } catch (Exception e) {
                String errorMsg = "Error running simulation for document " + ((criteria.isDocumentSimulation()) ? "id " + documentId.toString() : "type " + criteria.getDocumentTypeName());
                LOG.error(errorMsg,e);
                throw new DocumentSimulatedRouteException(errorMsg, e);
    		} finally {
    			perfLog.log("Time to run simulation.");
    			RouteContext.clearCurrentRouteContext();
    			
    			if (!mdcHadDocId) { MDC.remove("docID"); }
    		}
    	} finally {
    		RouteContext.releaseCurrentRouteContext();
    	}
    }

    /**
     * If there are multiple paths, we need to figure out which ones we need to follow for blanket approval.
     * This method will throw an exception if a node with the given name could not be located in the routing path.
     * This method is written in such a way that it should be impossible for there to be an infinate loop, even if
     * there is extensive looping in the node graph.
     */
    private List<RouteNodeInstance> determineNodeInstancesToProcess(List<RouteNodeInstance> activeNodeInstances, String nodeName) throws InvalidActionTakenException {
        if (org.apache.commons.lang.StringUtils.isEmpty(nodeName)) {
            return activeNodeInstances;
        }
        List<RouteNodeInstance> nodeInstancesToProcess = new ArrayList<RouteNodeInstance>();
        for (RouteNodeInstance nodeInstance : activeNodeInstances) {
            if (nodeName.equals(nodeInstance.getName())) {
                // one of active node instances is node instance to stop at
                return new ArrayList<RouteNodeInstance>();
            } else {
                if (isNodeNameInPath(nodeName, nodeInstance)) {
                    nodeInstancesToProcess.add(nodeInstance);
                }
            }
        }
        if (nodeInstancesToProcess.size() == 0) {
            throw new InvalidActionTakenException("Could not locate a node with the given name in the blanket approval path '" + nodeName + "'.  " +
                    "The document is probably already passed the specified node or does not contain the node.");
        }
        return nodeInstancesToProcess;
    }

    private boolean isNodeNameInPath(String nodeName, RouteNodeInstance nodeInstance) {
        boolean isInPath = false;
        for (Iterator<RouteNode> iterator = nodeInstance.getRouteNode().getNextNodes().iterator(); iterator.hasNext();) {
            RouteNode nextNode = (RouteNode) iterator.next();
            isInPath = isInPath || isNodeNameInPath(nodeName, nextNode, new HashSet<String>());
        }
        return isInPath;
    }

    private boolean isNodeNameInPath(String nodeName, RouteNode node, Set<String> inspected) {
        boolean isInPath = !inspected.contains(node.getRouteNodeId()) && node.getRouteNodeName().equals(nodeName);
        inspected.add(node.getRouteNodeId());
        if (helper.isSubProcessNode(node)) {
            ProcessDefinitionBo subProcess = node.getDocumentType().getNamedProcess(node.getRouteNodeName());
            RouteNode subNode = subProcess.getInitialRouteNode();
            if (subNode != null) {
                isInPath = isInPath || isNodeNameInPath(nodeName, subNode, inspected);
            }
        }
        for (Iterator<RouteNode> iterator = node.getNextNodes().iterator(); iterator.hasNext();) {
            RouteNode nextNode = (RouteNode) iterator.next();
            isInPath = isInPath || isNodeNameInPath(nodeName, nextNode, inspected);
        }
        return isInPath;
    }

    private boolean hasReachedCompletion(ProcessContext processContext, List actionRequests, RouteNodeInstance nodeInstance, SimulationCriteria criteria) {
        if (!criteria.getDestinationRecipients().isEmpty()) {
            for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
                ActionRequestValue request = (ActionRequestValue) iterator.next();
                for (Iterator<Recipient> userIt = criteria.getDestinationRecipients().iterator(); userIt.hasNext();) {
                    Recipient recipient = (Recipient) userIt.next();
                    if (request.isRecipientRoutedRequest(recipient)) {
                        if ( (org.apache.commons.lang.StringUtils.isEmpty(criteria.getDestinationNodeName())) || (criteria.getDestinationNodeName().equals(request.getNodeInstance().getName())) ) {
                            return true;
                        }
                    }
                }
            }
        }
        return (org.apache.commons.lang.StringUtils.isEmpty(criteria.getDestinationNodeName()) && processContext.isComplete() && processContext.getNextNodeInstances().isEmpty())
            || nodeInstance.getRouteNode().getRouteNodeName().equals(criteria.getDestinationNodeName());
    }

    private List<ActionTakenValue> processPotentialActionsTaken(RouteContext routeContext, DocumentRouteHeaderValue routeHeader, RouteNodeInstance justProcessedNode, SimulationCriteria criteria) {
    	List<ActionTakenValue> actionsTaken = new ArrayList<ActionTakenValue>();
    	List requestsToCheck = new ArrayList();
    	requestsToCheck.addAll(routeContext.getEngineState().getGeneratedRequests());
        requestsToCheck.addAll(routeHeader.getActionRequests());
    	List<ActionRequestValue> pendingActionRequestValues = getCriteriaActionsToDoByNodeName(requestsToCheck, justProcessedNode.getName());
        List<ActionTakenValue> actionsToTakeForNode = generateActionsToTakeForNode(justProcessedNode.getName(), routeHeader, criteria, pendingActionRequestValues);

        for (ActionTakenValue actionTaken : actionsToTakeForNode)
        {
            KEWServiceLocator.getActionRequestService().deactivateRequests(actionTaken, pendingActionRequestValues, routeContext.getActivationContext());
            actionsTaken.add(actionTaken);
//            routeContext.getActivationContext().getSimulatedActionsTaken().add(actionTaken);
        }
    	return actionsTaken;
    }

    private List<ActionTakenValue> generateActionsToTakeForNode(String nodeName, DocumentRouteHeaderValue routeHeader, SimulationCriteria criteria, List<ActionRequestValue> pendingActionRequests) {
        List<ActionTakenValue> actions = new ArrayList<ActionTakenValue>();
        if ( (criteria.getActionsToTake() != null) && (!criteria.getActionsToTake().isEmpty()) ) {
            for (SimulationActionToTake simAction : criteria.getActionsToTake()) {
                if (nodeName.equals(simAction.getNodeName())) {
                    actions.add(createDummyActionTaken(routeHeader, simAction.getUser(), simAction.getActionToPerform(), findDelegatorForActionRequests(pendingActionRequests)));
                }
            }
        }
        return actions;
    }

    private List<ActionRequestValue> getCriteriaActionsToDoByNodeName(List generatedRequests, String nodeName) {
    	List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
        for (Iterator iterator = generatedRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue request = (ActionRequestValue) iterator.next();
            if ( (request.isPending()) && request.getNodeInstance() != null && nodeName.equals(request.getNodeInstance().getName())) {
            	requests.add(request);
            }
        }
        return requests;
    }

    private void validateCriteria(SimulationCriteria criteria) {
    	if (criteria.getDocumentId() == null && org.apache.commons.lang.StringUtils.isEmpty(criteria.getDocumentTypeName())) {
		throw new IllegalArgumentException("No document type name or document id given, cannot simulate a document without a document type name or a document id.");
    	}
    	if (criteria.getXmlContent() == null) {
    		criteria.setXmlContent("");
    	}
    }

    /**
     * Creates the document to run the simulation against by loading it from the database or creating a fake document for
     * simulation purposes depending on the passed simulation criteria.
     *
     * If the documentId is available, we load the document from the database, otherwise we create one based on the given
     * DocumentType and xml content.
     */
    private DocumentRouteHeaderValue createSimulationDocument(String documentId, SimulationCriteria criteria, RouteContext context) {
    	DocumentRouteHeaderValue document = null;
    	if (criteria.isDocumentSimulation()) {
            document = getDocumentForSimulation(documentId);
            if (!org.apache.commons.lang.StringUtils.isEmpty(criteria.getXmlContent())) {
                document.setDocContent(criteria.getXmlContent());
            }
    	} else if (criteria.isDocumentTypeSimulation()) {
        	DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(criteria.getDocumentTypeName());
        	if (documentType == null) {
        		throw new IllegalArgumentException("Specified document type could not be found for name '"+criteria.getDocumentTypeName()+"'");
        	}
        	documentId = context.getEngineState().getNextSimulationId().toString();
        	document = new DocumentRouteHeaderValue();
        	context.setDocument(document);
        	document.setDocumentId(documentId);
        	document.setCreateDate(new Timestamp(System.currentTimeMillis()));
        	document.setDocContent(criteria.getXmlContent());
        	document.setDocRouteLevel(new Integer(0));
        	document.setDocumentTypeId(documentType.getDocumentTypeId());
    		document.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_INITIATED_CD);
    		initializeDocument(document);
        }
        if (document == null) {
        	throw new IllegalArgumentException("Workflow simulation engine could not locate document with id "+documentId);
        }
        for (ActionRequestValue actionRequest : document.getActionRequests()) {
        	actionRequest = (ActionRequestValue) deepCopy(actionRequest);
        	document.getSimulatedActionRequests().add(actionRequest);
        	for (ActionItem actionItem : actionRequest.getActionItems()) {
        		actionRequest.getSimulatedActionItems().add((ActionItem) deepCopy(actionItem));
        	}
        }
        context.setDocument(document);
        installSimulationNodeInstances(context, criteria);
		return document;
    }

    private DocumentRouteHeaderValue getDocumentForSimulation(String documentId) {
        DocumentRouteHeaderValue document = getRouteHeaderService().getRouteHeader(documentId);
        return (DocumentRouteHeaderValue)deepCopy(document);
    }

    private Serializable deepCopy(Serializable src) {
        Serializable obj = null;
        if (src != null) {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                ByteArrayOutputStream serializer = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(serializer);
                oos.writeObject(src);

                ByteArrayInputStream deserializer = new ByteArrayInputStream(serializer.toByteArray());
                ois = new ObjectInputStream(deserializer);
                obj = (Serializable) ois.readObject();
            }
            catch (IOException e) {
                throw new RuntimeException("unable to complete deepCopy from src '" + src.toString() + "'", e);
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException("unable to complete deepCopy from src '" + src.toString() + "'", e);
            }
            finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                }
                catch (IOException e) {
                    // ignoring this IOException, since the streams are going to be abandoned now anyway
                }
            }
        }
        return obj;
    }

    private void routeDocumentIfNecessary(DocumentRouteHeaderValue document, SimulationCriteria criteria, RouteContext routeContext) throws InvalidActionTakenException {
    	if (criteria.getRoutingUser() != null) {
            ActionTakenValue action = createDummyActionTaken(document, criteria.getRoutingUser(), KewApiConstants.ACTION_TAKEN_ROUTED_CD, null);
    		routeContext.getActivationContext().getSimulatedActionsTaken().add(action);
            simulateDocumentRoute(action, document, criteria.getRoutingUser(), routeContext);
    	}
    }

    /**
     * Looks at the rule templates and/or the startNodeName and creates the appropriate node instances to run simulation against.
     * After creating the node instances, it hooks them all together and installs a "terminal" simulation node to stop the simulation
     * node at the end of the simulation.
     */
    private void installSimulationNodeInstances(RouteContext context, SimulationCriteria criteria) {
    	DocumentRouteHeaderValue document = context.getDocument();
    	List<RouteNode> simulationNodes = new ArrayList<RouteNode>();
    	if (!criteria.getNodeNames().isEmpty()) {
    		for (String nodeName : criteria.getNodeNames()) {
				if ( LOG.isDebugEnabled() ) {
				    LOG.debug("Installing simulation starting node '"+nodeName+"'");
				}
	    		List<RouteNode> nodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(document.getDocumentType(), true);
	    		boolean foundNode = false;
	    		for (RouteNode node : nodes) {
					if (node.getRouteNodeName().equals(nodeName)) {
						simulationNodes.add(node);
						foundNode = true;
						break;
					}
				}
	    		if (!foundNode) {
	    			throw new IllegalArgumentException("Could not find node on the document type for the given name '"+nodeName+"'");
	    		}
    		}
    	} else if (!criteria.getRuleTemplateNames().isEmpty()) {
    		List<RouteNode> nodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(document.getDocumentType(), true);
    		for (String ruleTemplateName : criteria.getRuleTemplateNames()) {
				boolean foundNode = false;
				for (RouteNode node : nodes) {
					String routeMethodName = node.getRouteMethodName();
					if (node.isFlexRM() && ruleTemplateName.equals(routeMethodName)) {
						simulationNodes.add(node);
						foundNode = true;
						break;
					}
				}
				if (!foundNode) {
	    			throw new IllegalArgumentException("Could not find node on the document type with the given rule template name '"+ruleTemplateName+"'");
	    		}
			}
    	} else if (criteria.isFlattenNodes()) {
    		// if they want to flatten the nodes, we will essentially process all simple nodes that are defined on the DocumentType
            List<RouteNode> nodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(document.getDocumentType(), true);
            for ( RouteNode node : nodes ) {
                try {
	                if ( NodeType.fromNode( node ).isTypeOf( SimpleNode.class ) 
	                		&& !NodeType.fromNode( node ).isTypeOf( NoOpNode.class ) ) {
	                    simulationNodes.add(node);
	                }
                } catch (ResourceUnavailableException ex) {
					LOG.warn( "Unable to determine node type in simulator: " + ex.getMessage() );
				}
            }
    	} else {
    	    // in this case, we want to let the document proceed from it's current active node
    		return;
    	}
    	
    	// hook all of the simulation nodes together
    	Branch defaultBranch = document.getInitialRouteNodeInstances().get(0).getBranch();
    	// clear out the initial route node instances, we are going to build a new node path based on what we want to simulate
    	document.getInitialRouteNodeInstances().clear();

    	RouteNodeInstance currentNodeInstance = null;//initialNodeInstance;
    	for (RouteNode simulationNode : simulationNodes) {
			RouteNodeInstance nodeInstance = helper.getNodeFactory().createRouteNodeInstance(document.getDocumentId(), simulationNode);
			nodeInstance.setBranch(defaultBranch);
			if (currentNodeInstance == null) {
				document.getInitialRouteNodeInstances().add(nodeInstance);
				nodeInstance.setActive(true);
				saveNode(context, nodeInstance);
			} else {
				currentNodeInstance.addNextNodeInstance(nodeInstance);
				saveNode(context, currentNodeInstance);
			}
			currentNodeInstance = nodeInstance;
		}
    	installSimulationTerminationNode(context, document.getDocumentType(), currentNodeInstance);
    }

    private void installSimulationTerminationNode(RouteContext context, DocumentType documentType, RouteNodeInstance lastNodeInstance) {
    	RouteNode terminationNode = new RouteNode();
    	terminationNode.setDocumentType(documentType);
    	terminationNode.setDocumentTypeId(documentType.getDocumentTypeId());
    	terminationNode.setNodeType(NoOpNode.class.getName());
    	terminationNode.setRouteNodeName("SIMULATION_TERMINATION_NODE");
    	RouteNodeInstance terminationNodeInstance = helper.getNodeFactory().createRouteNodeInstance(lastNodeInstance.getDocumentId(), terminationNode);
    	terminationNodeInstance.setBranch(lastNodeInstance.getBranch());
    	lastNodeInstance.addNextNodeInstance(terminationNodeInstance);
    	saveNode(context, lastNodeInstance);
    }

    // below is pretty much a copy of RouteDocumentAction... but actions have to be faked for now
    private void simulateDocumentRoute(ActionTakenValue actionTaken, DocumentRouteHeaderValue document, Person user, RouteContext routeContext) throws InvalidActionTakenException {
        if (document.isRouted()) {
            throw new WorkflowRuntimeException("Document can not simulate a route if it has already been routed");
        }
    	ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
        // TODO delyea - deep copy below
        List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>();
        for (Iterator iter = actionRequestService.findPendingByDoc(document.getDocumentId()).iterator(); iter.hasNext();) {
            ActionRequestValue arv = (ActionRequestValue) deepCopy( (ActionRequestValue) iter.next() );
            for (ActionItem actionItem : arv.getActionItems()) {
        		arv.getSimulatedActionItems().add((ActionItem) deepCopy(actionItem));
        	}
            actionRequests.add(arv);//(ActionRequestValue)deepCopy(arv));
        }
//        actionRequests.addAll(actionRequestService.findPendingByDoc(document.getDocumentId()));
        LOG.debug("Simulate Deactivating all pending action requests");
        // deactivate any requests for the user that routed the document.
        for (Iterator<ActionRequestValue> iter = actionRequests.iterator(); iter.hasNext();) {
            ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
            // requests generated to the user who is routing the document should be deactivated
            if ( (user.getPrincipalId().equals(actionRequest.getPrincipalId())) && (actionRequest.isActive()) ) {
            	actionRequestService.deactivateRequest(actionTaken, actionRequest, routeContext.getActivationContext());
            }
            // requests generated by a save action should be deactivated
            else if (KewApiConstants.SAVED_REQUEST_RESPONSIBILITY_ID.equals(actionRequest.getResponsibilityId())) {
            	actionRequestService.deactivateRequest(actionTaken, actionRequest, routeContext.getActivationContext());
            }
        }

//        String oldStatus = document.getDocRouteStatus();
        document.markDocumentEnroute();
//        String newStatus = document.getDocRouteStatus();
//        notifyStatusChange(newStatus, oldStatus);
//        getRouteHeaderService().saveRouteHeader(document);
    }

    private ActionTakenValue createDummyActionTaken(DocumentRouteHeaderValue routeHeader, Person userToPerformAction, String actionToPerform, Recipient delegator) {
        ActionTakenValue val = new ActionTakenValue();
        val.setActionTaken(actionToPerform);
        if (KewApiConstants.ACTION_TAKEN_ROUTED_CD.equals(actionToPerform)) {
            val.setActionTaken(KewApiConstants.ACTION_TAKEN_COMPLETED_CD);
        }
		val.setAnnotation("");
		val.setDocVersion(routeHeader.getDocVersion());
		val.setDocumentId(routeHeader.getDocumentId());
		val.setPrincipalId(userToPerformAction.getPrincipalId());

		if (delegator != null) {
			if (delegator instanceof KimPrincipalRecipient) {
				val.setDelegatorPrincipalId(((KimPrincipalRecipient) delegator).getPrincipalId());
			} else if (delegator instanceof KimGroupRecipient) {
				Group group = ((KimGroupRecipient) delegator).getGroup();
				val.setDelegatorGroupId(group.getId());
			} else{
				throw new IllegalArgumentException("Invalid Recipient type received: " + delegator.getClass().getName());
			}
		}

		//val.setRouteHeader(routeHeader);
		val.setCurrentIndicator(Boolean.TRUE);
		return val;
    }

	/**
	 * Used by actions taken
	 *
	 * Returns the highest priority delegator in the list of action requests.
	 */
	private Recipient findDelegatorForActionRequests(List<ActionRequestValue> actionRequests) {
		return KEWServiceLocator.getActionRequestService().findDelegator(actionRequests);
	}

    /**
     * Executes a "saveNode" for the simulation engine, this does not actually save the document, but rather
     * assigns it some simulation ids.
     *
     * Resolves KULRICE-368
     */
    @Override
    protected void saveNode(RouteContext context, RouteNodeInstance nodeInstance) {
		// we shold be in simulation mode here

    	if (nodeInstance.getRouteNodeInstanceId() == null) {
    		nodeInstance.setRouteNodeInstanceId(context.getEngineState().getNextSimulationId());
    	}
    	
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
