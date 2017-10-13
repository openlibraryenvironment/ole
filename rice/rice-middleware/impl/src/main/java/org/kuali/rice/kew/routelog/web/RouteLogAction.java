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
package org.kuali.rice.kew.routelog.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.node.RouteNodeInstanceState;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.doctype.service.DocumentSecurityService;
import org.kuali.rice.kew.dto.DTOConverter.RouteNodeInstanceLoader;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A Struts Action used to display the routelog.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RouteLogAction extends KewKualiAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RouteLogAction.class);
    private static Comparator<ActionRequestValue> ROUTE_LOG_ACTION_REQUEST_SORTER = new Utilities.RouteLogActionRequestSorter();
    
    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        RouteLogForm rlForm = (RouteLogForm) form;
        String documentId = null;
        if (! org.apache.commons.lang.StringUtils.isEmpty(rlForm.getDocumentId())) {
        	documentId = rlForm.getDocumentId();
        } else if (! org.apache.commons.lang.StringUtils.isEmpty(rlForm.getDocId())) {
        	documentId =rlForm.getDocId();
        } else {
        	throw new WorkflowRuntimeException("No paramater provided to fetch document");
        }

        DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);

        DocumentSecurityService security = KEWServiceLocator.getDocumentSecurityService();
        if (!security.routeLogAuthorized(getUserSession().getPrincipalId(), routeHeader, new SecuritySession(GlobalVariables.getUserSession().getPrincipalId()))) {
          return mapping.findForward("NotAuthorized");
        }
        
        fixActionRequestsPositions(routeHeader);
        populateRouteLogFormActionRequests(rlForm, routeHeader);

        rlForm.setLookFuture(routeHeader.getDocumentType().getLookIntoFuturePolicy().getPolicyValue().booleanValue());

        if (rlForm.isShowFuture()) {
            try {
                populateRouteLogFutureRequests(rlForm, routeHeader);
            } catch (Exception e) {
                String errorMsg = "Unable to determine Future Action Requests";
                LOG.info(errorMsg,e);
                rlForm.setShowFutureError(errorMsg);
            }
        }
        request.setAttribute("routeHeader", routeHeader);
        
		// check whether action message logging should be enabled, user must
		// have KIM permission for doc type 
        boolean isAuthorizedToAddRouteLogMessage = KEWServiceLocator.getDocumentTypePermissionService()
				.canAddRouteLogMessage(GlobalVariables.getUserSession().getPrincipalId(), routeHeader);
		if (isAuthorizedToAddRouteLogMessage) {
			rlForm.setEnableLogAction(true);
		} else {
			rlForm.setEnableLogAction(false);
		}
        
        return super.execute(mapping, rlForm, request, response);
    }

    @SuppressWarnings("unchecked")
	public void populateRouteLogFormActionRequests(RouteLogForm rlForm, DocumentRouteHeaderValue routeHeader) {
        List<ActionRequestValue> rootRequests = getActionRequestService().getRootRequests(routeHeader.getActionRequests());
        Collections.sort(rootRequests, ROUTE_LOG_ACTION_REQUEST_SORTER);
        rootRequests = switchActionRequestPositionsIfPrimaryDelegatesPresent(rootRequests);
        int arCount = 0;
        for ( ActionRequestValue actionRequest : rootRequests ) {
            if (actionRequest.isPending()) {
                arCount++;

                if (ActionRequestStatus.INITIALIZED.getCode().equals(actionRequest.getStatus())) {
                    actionRequest.setDisplayStatus("PENDING");
                } else if (ActionRequestStatus.ACTIVATED.getCode().equals(actionRequest.getStatus())) {
                    actionRequest.setDisplayStatus("IN ACTION LIST");
                }
            }
        }
        rlForm.setRootRequests(rootRequests);
        rlForm.setPendingActionRequestCount(arCount);
    }

    @SuppressWarnings("unchecked")
	private ActionRequestValue switchActionRequestPositionIfPrimaryDelegatePresent( ActionRequestValue actionRequest ) {
    	
    	/**
    	 * KULRICE-4756 - The main goal here is to fix the regression of what happened in Rice 1.0.2 with the display
    	 * of primary delegate requests.  The delegate is displayed at the top-most level correctly on action requests
    	 * that are "rooted" at a "role" request.
    	 * 
    	 * If they are rooted at a principal or group request, then the display of the primary delegator at the top-most
    	 * level does not happen (instead it shows the delegator and you have to expand the request to see the primary
    	 * delegate).
    	 * 
    	 * Ultimately, the KAI group and Rice BA need to come up with a specification for how the Route Log should
    	 * display delegate information.  For now, will fix this so that in the non "role" case, it will put the
    	 * primary delegate as the outermost request *except* in the case where there is more than one primary delegate.
    	 */
    	
    	if (!actionRequest.isRoleRequest()) {
    		List<ActionRequestValue> primaryDelegateRequests = actionRequest.getPrimaryDelegateRequests();
    		// only display primary delegate request at top if there is only *one* primary delegate request
    		if ( primaryDelegateRequests.size() != 1) {
    			return actionRequest;
    		}
    		ActionRequestValue primaryDelegateRequest = primaryDelegateRequests.get(0);
    		actionRequest.getChildrenRequests().remove(primaryDelegateRequest);
    		primaryDelegateRequest.setChildrenRequests(actionRequest.getChildrenRequests());
    		primaryDelegateRequest.setParentActionRequest(actionRequest.getParentActionRequest());
    		primaryDelegateRequest.setParentActionRequestId(actionRequest.getParentActionRequestId());
    		
    		actionRequest.setChildrenRequests( new ArrayList<ActionRequestValue>(0) );
    		actionRequest.setParentActionRequest(primaryDelegateRequest);
    		actionRequest.setParentActionRequestId(primaryDelegateRequest.getActionRequestId());
    		
    		primaryDelegateRequest.getChildrenRequests().add(0, actionRequest);
    		
    		for (ActionRequestValue delegateRequest : primaryDelegateRequest.getChildrenRequests()) {
    			delegateRequest.setParentActionRequest(primaryDelegateRequest);
    			delegateRequest.setParentActionRequestId(primaryDelegateRequest.getActionRequestId());
    		}
    		
    		return primaryDelegateRequest;
    	}
    	
    	return actionRequest;
    }

    private List<ActionRequestValue> switchActionRequestPositionsIfPrimaryDelegatesPresent( Collection<ActionRequestValue> actionRequests ) {
    	List<ActionRequestValue> results = new ArrayList<ActionRequestValue>( actionRequests.size() );
    	for ( ActionRequestValue actionRequest : actionRequests ) {
			results.add( switchActionRequestPositionIfPrimaryDelegatePresent(actionRequest) );
    	}
    	return results;
    }
    
    @SuppressWarnings("unchecked")
    private void fixActionRequestsPositions(DocumentRouteHeaderValue routeHeader) {
        for (ActionTakenValue actionTaken : routeHeader.getActionsTaken()) {
            Collections.sort((List<ActionRequestValue>) actionTaken.getActionRequests(), ROUTE_LOG_ACTION_REQUEST_SORTER);
            actionTaken.setActionRequests( actionTaken.getActionRequests() );
        }
    }
    
    /**
     * executes a simulation of the future routing, and sets the futureRootRequests and futureActionRequestCount
     * properties on the provided RouteLogForm.
     * 
     * @param rlForm the RouteLogForm --used in a write-only fashion.
     * @param document the DocumentRouteHeaderValue for the document whose future routing is being simulated.
     * @throws Exception
     */
    public void populateRouteLogFutureRequests(RouteLogForm rlForm, DocumentRouteHeaderValue document) throws Exception {

        RoutingReportCriteria reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId()).build();
        String applicationId = document.getDocumentType().getApplicationId();

        // gather the IDs for action requests that predate the simulation
		Set<String> preexistingActionRequestIds = getActionRequestIds(document);
        
		// run the simulation
        DocumentDetail documentDetail = KewApiServiceLocator.getWorkflowDocumentActionsService(applicationId).executeSimulation(reportCriteria);

        // fabricate our ActionRequestValueS from the results
        List<ActionRequestValue> futureActionRequests = 
        	reconstituteActionRequestValues(documentDetail, preexistingActionRequestIds);

        Collections.sort(futureActionRequests, ROUTE_LOG_ACTION_REQUEST_SORTER);
        
        futureActionRequests = switchActionRequestPositionsIfPrimaryDelegatesPresent(futureActionRequests);
        
        int pendingActionRequestCount = 0;
        for (ActionRequestValue actionRequest: futureActionRequests) {
            if (actionRequest.isPending()) {
                pendingActionRequestCount++;

                if (ActionRequestStatus.INITIALIZED.getCode().equals(actionRequest.getStatus())) {
                    actionRequest.setDisplayStatus("PENDING");
                } else if (ActionRequestStatus.ACTIVATED.getCode().equals(actionRequest.getStatus())) {
                    actionRequest.setDisplayStatus("IN ACTION LIST");
                }
            }
        }

        rlForm.setFutureRootRequests(futureActionRequests);
        rlForm.setFutureActionRequestCount(pendingActionRequestCount);
    }


	/**
	 * This utility method returns a Set of LongS containing the IDs for the ActionRequestValueS associated with 
	 * this DocumentRouteHeaderValue. 
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getActionRequestIds(DocumentRouteHeaderValue document) {
		Set<String> actionRequestIds = new HashSet<String>();

		List<ActionRequestValue> actionRequests = 
			KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
		
		if (actionRequests != null) {
			for (ActionRequestValue actionRequest : actionRequests) {
				if (actionRequest.getActionRequestId() != null) {
					actionRequestIds.add(actionRequest.getActionRequestId());
				}
			}
		}
		return actionRequestIds;
	}

	/**
	 * This method creates ActionRequestValue objects from the DocumentDetailDTO output from
	 * 
	 * @param documentDetail contains the DTOs from which the ActionRequestValues are reconstituted
	 * @param preexistingActionRequestIds this is a Set of ActionRequest IDs that will not be reconstituted
	 * @return the ActionRequestValueS that have been created
	 */
	private List<ActionRequestValue> reconstituteActionRequestValues(DocumentDetail documentDetail,
			Set<String> preexistingActionRequestIds) {

        RouteNodeInstanceFabricator routeNodeInstanceFabricator = 
    		new RouteNodeInstanceFabricator(KEWServiceLocator.getRouteNodeService());

        if (documentDetail.getRouteNodeInstances() != null && !documentDetail.getRouteNodeInstances().isEmpty()) {
        	for (org.kuali.rice.kew.api.document.node.RouteNodeInstance routeNodeInstanceVO : documentDetail.getRouteNodeInstances()) {
        		routeNodeInstanceFabricator.importRouteNodeInstanceDTO(routeNodeInstanceVO);
        	}
		}
        
        List<ActionRequest> actionRequestVOs = documentDetail.getActionRequests();
        List<ActionRequestValue> futureActionRequests = new ArrayList<ActionRequestValue>();
        if (actionRequestVOs != null) {
			for (ActionRequest actionRequestVO : actionRequestVOs) {
				if (actionRequestVO != null) {
					if (!preexistingActionRequestIds.contains(actionRequestVO.getId())) {
						ActionRequestValue converted = ActionRequestValue.from(actionRequestVO,
                                routeNodeInstanceFabricator);
						futureActionRequests.add(converted);
					}
				}
			}
		}
		return futureActionRequests;
	}
    
    private ActionRequestService getActionRequestService() {
        return (ActionRequestService) KEWServiceLocator.getService(KEWServiceLocator.ACTION_REQUEST_SRV);
    }
    
    private UserSession getUserSession() {
        return GlobalVariables.getUserSession();
    }
    
    /**
     * Creates dummy RouteNodeInstances based on imported data from RouteNodeInstanceDTOs.
     * It is then able to vend those RouteNodeInstanceS back by their IDs.
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     *
     */
    private static class RouteNodeInstanceFabricator implements RouteNodeInstanceLoader {

    	private Map<String,Branch> branches = new HashMap<String, Branch>();
    	private Map<String, RouteNodeInstance> routeNodeInstances =
                new HashMap<String, RouteNodeInstance>();
    	private Map<String,RouteNode> routeNodes = new HashMap<String, RouteNode>();
    	private Map<String,NodeState> nodeStates = new HashMap<String, NodeState>();

    	private RouteNodeService routeNodeService;
    	
    	/**
		 * This constructs a FutureRouteNodeInstanceFabricator, which will generate bogus
		 * RouteNodeInstances for SimulationEngine results
		 * 
		 */
		public RouteNodeInstanceFabricator(RouteNodeService routeNodeService) {
			this.routeNodeService = routeNodeService;
		}

		/**
		 * 
		 * This method looks at the given RouteNodeInstanceDTO and imports it (and all it's ancestors)
		 * as dummy RouteNodeInstanceS
		 * 
		 * @param nodeInstanceDTO
		 */
		public void importRouteNodeInstanceDTO(org.kuali.rice.kew.api.document.node.RouteNodeInstance nodeInstanceDTO) {
			_importRouteNodeInstanceDTO(nodeInstanceDTO);
		}
		
		/**
		 * helper method for {@link #importRouteNodeInstanceDTO(org.kuali.rice.kew.api.document.node.RouteNodeInstance)} which does all
		 * the work.  The public method just wraps this one but hides the returned RouteNodeInstance,
		 * which is used for the recursive call to populate the nextNodeInstanceS inside our 
		 * RouteNodeInstanceS.
		 * 
		 * @param nodeInstanceDTO
		 * @return
		 */
    	private RouteNodeInstance _importRouteNodeInstanceDTO(org.kuali.rice.kew.api.document.node.RouteNodeInstance nodeInstanceDTO) {
    		if (nodeInstanceDTO == null) {
    			return null;
    		}
    		RouteNodeInstance nodeInstance = new RouteNodeInstance();
    		nodeInstance.setActive(nodeInstanceDTO.isActive());

    		nodeInstance.setComplete(nodeInstanceDTO.isComplete());
    		nodeInstance.setDocumentId(nodeInstanceDTO.getDocumentId());
    		nodeInstance.setInitial(nodeInstanceDTO.isInitial());

    		Branch branch = getBranch(nodeInstanceDTO.getBranchId());
    		nodeInstance.setBranch(branch);

    		if (nodeInstanceDTO.getRouteNodeId() != null) {
    			RouteNode routeNode = routeNodeService.findRouteNodeById(nodeInstanceDTO.getRouteNodeId());

    			if (routeNode == null) {
    				routeNode = getRouteNode(nodeInstanceDTO.getRouteNodeId());
    				routeNode.setNodeType(nodeInstanceDTO.getName());
    			}

    			nodeInstance.setRouteNode(routeNode);

    			if (routeNode.getBranch() != null) {
        			branch.setName(routeNode.getBranch().getName());
        		} 
    		}

    		RouteNodeInstance process = getRouteNodeInstance(nodeInstanceDTO.getProcessId());
    		nodeInstance.setProcess(process);

    		nodeInstance.setRouteNodeInstanceId(nodeInstanceDTO.getId());

    		List<NodeState> nodeState = new ArrayList<NodeState>();
    		if (nodeInstanceDTO.getState() != null) {
				for (RouteNodeInstanceState stateDTO : nodeInstanceDTO.getState()) {
					NodeState state = getNodeState(stateDTO.getId());
					if (state != null) {
						state.setKey(stateDTO.getKey());
						state.setValue(stateDTO.getValue());
						state.setStateId(stateDTO.getId());
						state.setNodeInstance(nodeInstance);
						nodeState.add(state);
					}
				}
			}
    		nodeInstance.setState(nodeState);

    		List<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();


    		for (org.kuali.rice.kew.api.document.node.RouteNodeInstance nextNodeInstanceVO : nodeInstanceDTO.getNextNodeInstances()) {
    			// recurse to populate nextNodeInstances
    			nextNodeInstances.add(_importRouteNodeInstanceDTO(nextNodeInstanceVO));
    		}
            nodeInstance.setNextNodeInstances(nextNodeInstances);

    		routeNodeInstances.put(nodeInstance.getRouteNodeInstanceId(), nodeInstance);
    		return nodeInstance;
    	}
    	
		/**
		 * This method returns a dummy RouteNodeInstance for the given ID, or null if it hasn't
		 * imported from a RouteNodeInstanceDTO with that ID
		 * 
		 * @see org.kuali.rice.kew.dto.DTOConverter.RouteNodeInstanceLoader#load(String)
		 */
		@Override
		public RouteNodeInstance load(String routeNodeInstanceID) {
			return routeNodeInstances.get(routeNodeInstanceID);
		}


    	/**
    	 * This method creates bogus BranchES as needed
    	 * 
    	 * @param branchId
    	 * @return
    	 */
    	private Branch getBranch(String branchId) {
    		Branch result = null;

    		if (branchId != null) {
    			// if branch doesn't exist, create it
    			if (!branches.containsKey(branchId)) {
    				result = new Branch();
    				result.setBranchId(branchId);
    				branches.put(branchId, result);
    			} else {
    				result = branches.get(branchId);
    			}
    		}
    		return result;
    	}

    	/**
    	 * This method creates bogus RouteNodeS as needed
    	 * 
    	 * @param routeNodeId
    	 * @return
    	 */
    	private RouteNode getRouteNode(String routeNodeId) {
    		RouteNode result = null;

    		if (routeNodeId != null) {
    			// if RouteNode doesn't exist, create it
    			if (!routeNodes.containsKey(routeNodeId)) {
    				result = new RouteNode();
    				result.setRouteNodeId(routeNodeId);
    				routeNodes.put(routeNodeId, result);
    			} else {
    				result = routeNodes.get(routeNodeId);
    			}
    		}
    		return result;
    	}

    	/**
    	 * This method creates bogus RouteNodeInstanceS as needed
    	 * 
    	 * @param routeNodeInstanceId
    	 * @return
    	 */
    	public RouteNodeInstance getRouteNodeInstance(String routeNodeInstanceId) {
    		RouteNodeInstance result = null;

    		if (routeNodeInstanceId != null) {
    			// if RouteNodeInstance doesn't exist, create it
    			if (!routeNodeInstances.containsKey(routeNodeInstanceId)) {
                    result = new RouteNodeInstance();
    				result.setRouteNodeInstanceId(routeNodeInstanceId);
    				routeNodeInstances.put(routeNodeInstanceId, result);
    			} else {
    				result = routeNodeInstances.get(routeNodeInstanceId);
    			}
    		}
    		return result;
    	}

    	/**
    	 * This method creates bogus NodeStateS as needed
    	 * 
    	 * @param nodeStateId
    	 * @return
    	 */
    	private NodeState getNodeState(String nodeStateId) {
    		NodeState result = null;

    		if (nodeStateId != null) {
    			// if NodeState doesn't exist, create it
    			if (!nodeStates.containsKey(nodeStateId)) {
    				result = new NodeState();
    				result.setNodeStateId(nodeStateId);
    				nodeStates.put(nodeStateId, result);
    			} else {
    				result = nodeStates.get(nodeStateId);
    			}
    		}
    		return result;
    	}

    } // end inner class FutureRouteNodeInstanceFabricator

    /**
     * Logs a new message to the route log for the current document, then refreshes the action taken list to display
     * back the new message in the route log tab. User must have permission to log a message for the doc type and the
     * request must be coming from the route log tab display (not the route log page).
     */
	public ActionForward logActionMessageInRouteLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RouteLogForm routeLogForm = (RouteLogForm) form;

		String documentId = null;
		if (!org.apache.commons.lang.StringUtils.isEmpty(routeLogForm.getDocumentId())) {
			documentId = routeLogForm.getDocumentId();
		} else if (!org.apache.commons.lang.StringUtils.isEmpty(routeLogForm.getDocId())) {
			documentId = routeLogForm.getDocId();
		} else {
			throw new WorkflowRuntimeException("No paramater provided to fetch document");
		}
		
		DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		
		// check user has permission to add a route log message
		boolean isAuthorizedToAddRouteLogMessage = KEWServiceLocator.getDocumentTypePermissionService()
				.canAddRouteLogMessage(GlobalVariables.getUserSession().getPrincipalId(), routeHeader);

		if (!isAuthorizedToAddRouteLogMessage) {
			throw new InvalidActionTakenException("Principal with name '"
					+ GlobalVariables.getUserSession().getPrincipalName()
					+ "' is not authorized to add route log messages for documents of type '"
					+ routeHeader.getDocumentType().getName());
		}

		LOG.info("Logging new action message for user " + GlobalVariables.getUserSession().getPrincipalName()
				+ ", route header " + routeHeader);
		KEWServiceLocator.getWorkflowDocumentService().logDocumentAction(
				GlobalVariables.getUserSession().getPrincipalId(), routeHeader,
				routeLogForm.getNewRouteLogActionMessage());

		routeLogForm.setNewRouteLogActionMessage("");

		// retrieve routeHeader again to pull new action taken
		routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId, true);
		fixActionRequestsPositions(routeHeader);
		request.setAttribute("routeHeader", routeHeader);

		return mapping.findForward(getDefaultMapping());
	}
    
}
