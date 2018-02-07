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

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.CompatUtils;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.NodeGraphSearchCriteria;
import org.kuali.rice.kew.engine.node.NodeGraphSearchResult;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Returns a document to a previous node in the route.
 *
 * Current implementation only supports returning to a node on the main branch of the
 * document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ReturnToPreviousNodeAction extends ActionTakenEvent {
    protected static final Logger LOG = Logger.getLogger(ReturnToPreviousNodeAction.class);

    // ReturnToPrevious returns to initial node when sent a null node name
    protected static final String INITIAL_NODE_NAME = null;
    protected static final boolean DEFAULT_SEND_NOTIFICATIONS = true;

    private final RouteHelper helper = new RouteHelper();
    protected final String nodeName;
    private boolean superUserUsage;
    private final boolean sendNotifications;
    private final boolean sendNotificationsForPreviousRequests;

    public ReturnToPreviousNodeAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        this(KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD, routeHeader,  principal, DEFAULT_ANNOTATION, INITIAL_NODE_NAME, DEFAULT_SEND_NOTIFICATIONS);
    }

    public ReturnToPreviousNodeAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String nodeName, boolean sendNotifications) {
        this(KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD, routeHeader, principal, annotation, nodeName, sendNotifications);
    }
    
    public ReturnToPreviousNodeAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String nodeName, boolean sendNotifications, boolean runPostProcessorLogic) {
        this(KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD, routeHeader, principal, annotation, nodeName, sendNotifications, runPostProcessorLogic);
    }

    /**
     * Constructor used to override the action taken code...e.g. when being performed as part of a Move action
     */
    protected ReturnToPreviousNodeAction(String overrideActionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String nodeName, boolean sendNotifications) {
        this(overrideActionTakenCode, routeHeader, principal, annotation, nodeName, sendNotifications, DEFAULT_RUN_POSTPROCESSOR_LOGIC);
    }
    
    /**
     * Constructor used to override the action taken code...e.g. when being performed as part of a Move action
     */
    protected ReturnToPreviousNodeAction(String overrideActionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String nodeName, boolean sendNotifications, boolean runPostProcessorLogic) {
        super(overrideActionTakenCode, routeHeader, principal, annotation, runPostProcessorLogic);
        this.nodeName = nodeName;
        this.sendNotifications = isPolicySet(routeHeader.getDocumentType(), DocumentTypePolicy.NOTIFY_PENDING_ON_RETURN, sendNotifications);
        this.sendNotificationsForPreviousRequests = isPolicySet(routeHeader.getDocumentType(), DocumentTypePolicy.NOTIFY_COMPLETED_ON_RETURN);
    }

    /**
     * Revokes requests, deactivating them with the specified ActionTakenValue.  Sends FYI notifications if sendNotifications is true.
     * TODO will this work properly in the case of an ALL APPROVE role requests with some of the requests already completed?
     */
    private void revokePendingRequests(List<ActionRequestValue> pendingRequests, ActionTakenValue actionTaken, PrincipalContract principal, Recipient delegator) {
        revokeRequests(pendingRequests);
        getActionRequestService().deactivateRequests(actionTaken, pendingRequests);
        if (sendNotifications) {
            generateNotificationsForRevokedRequests(pendingRequests, principal, delegator);
        }
    }

    /**
     * Revokes requests (not deactivating them).  Sends FYI notifications if sendNotifications is true.
     */
    private void revokePreviousRequests(List<ActionRequestValue> actionRequests, PrincipalContract principal, Recipient delegator) {
        revokeRequests(actionRequests);
        if (sendNotificationsForPreviousRequests) {
            generateNotificationsForRevokedRequests(actionRequests, principal, delegator);
        }
    }

    /**
     * Generates FYIs for revoked ActionRequests
     * @param revokedRequests the revoked actionrequests
     * @param principal principal taking action, omitted from notifications
     * @param delegator delegator to omit from notifications
     */
    private void generateNotificationsForRevokedRequests(List<ActionRequestValue> revokedRequests, PrincipalContract principal, Recipient delegator) {
        ActionRequestFactory arFactory = new ActionRequestFactory(getRouteHeader());
        List<ActionRequestValue> notificationRequests = arFactory.generateNotifications(revokedRequests, principal, delegator, KewApiConstants.ACTION_REQUEST_FYI_REQ, getActionTakenCode());
        getActionRequestService().activateRequests(notificationRequests);
    }

    /**
     * Takes a list of root action requests and marks them and all of their children as "non-current".
     */
    private void revokeRequests(List<ActionRequestValue> actionRequests) {
        for (Iterator<ActionRequestValue> iterator = actionRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue actionRequest = iterator.next();
            actionRequest.setCurrentIndicator(Boolean.FALSE);
            if (actionRequest.getActionTaken() != null) {
                actionRequest.getActionTaken().setCurrentIndicator(Boolean.FALSE);
                KEWServiceLocator.getActionTakenService().saveActionTaken(actionRequest.getActionTaken());
            }
            revokeRequests(actionRequest.getChildrenRequests());
            KEWServiceLocator.getActionRequestService().saveActionRequest(actionRequest);
        }
    }

    /**
     * Template method that determines what action request to generate when returning to initiator
     * @return the ActionRequestType
     */
    protected ActionRequestType getReturnToInitiatorActionRequestType() {
        return ActionRequestType.APPROVE;
    }
    
    private void processReturnToInitiator(RouteNodeInstance newNodeInstance) {
	    // important to pull this from the RouteNode's DocumentType so we get the proper version
        RouteNode initialNode = newNodeInstance.getRouteNode().getDocumentType().getPrimaryProcess().getInitialRouteNode();
        if (initialNode != null) {
            if (newNodeInstance.getRouteNode().getRouteNodeId().equals(initialNode.getRouteNodeId())) {
                LOG.debug("Document was returned to initiator");
                ActionRequestFactory arFactory = new ActionRequestFactory(getRouteHeader(), newNodeInstance);
                ActionRequestValue notificationRequest = arFactory.createNotificationRequest(getReturnToInitiatorActionRequestType().getCode(), determineInitialNodePrincipal(getRouteHeader()), getActionTakenCode(), getPrincipal(), "Document initiator");
                getActionRequestService().activateRequest(notificationRequest);
            }
        }
    }

    /**
     * Determines which principal to generate an actionqrequest when the document is returned to the initial node
     * By default this is the document initiator.
     * @param routeHeader the document route header
     * @return a Principal
     */
    protected PrincipalContract determineInitialNodePrincipal(DocumentRouteHeaderValue routeHeader) {
        return routeHeader.getInitiatorPrincipal();
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#isActionCompatibleRequest(java.util.List)
     */
    @Override
    public String validateActionRules() {
    	return validateActionRules(getActionRequestService().findAllPendingRequests(routeHeader.getDocumentId()));
    }

    public String validateActionRules(List<ActionRequestValue> actionRequests) {
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            String docStatus = getRouteHeader().getDocRouteStatus();
            return "Document of status '" + docStatus + "' cannot taken action '" + KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS + "' to node name "+nodeName;
        }
        List<ActionRequestValue> filteredActionRequests = findApplicableActionRequests(actionRequests);
        if (! isActionCompatibleRequest(filteredActionRequests) && ! isSuperUserUsage()) {
            return "No request for the user is compatible with the " + ActionType.fromCode(this.getActionTakenCode()).getLabel() + " action";
        }
        return "";
    }

    /**
     * Allows subclasses to determine which actionrequests to inspect for purposes of action validation
     * @param actionRequests all actionrequests for this document
     * @return a (possibly) filtered list of actionrequests
     */
    protected List<ActionRequestValue> findApplicableActionRequests(List<ActionRequestValue> actionRequests) {
        return filterActionRequestsByCode(actionRequests, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#isActionCompatibleRequest(java.util.List)
     */
    @Override
    public boolean isActionCompatibleRequest(List<ActionRequestValue> requests) {
        String actionTakenCode = getActionPerformedCode();

        // Move is always correct because the client application has authorized it
        if (KewApiConstants.ACTION_TAKEN_MOVE_CD.equals(actionTakenCode)) {
            return true;
        }

        // can always cancel saved or initiated document
        if (routeHeader.isStateInitiated() || routeHeader.isStateSaved()) {
            return true;
        }

        boolean actionCompatible = false;
        Iterator<ActionRequestValue> ars = requests.iterator();
        ActionRequestValue actionRequest = null;

        while (ars.hasNext()) {
            actionRequest = ars.next();

            //if (actionRequest.isWorkgroupRequest() && !actionRequest.getWorkgroup().hasMember(this.delegator)) {
            // TODO might not need this, if so, do role check
            /*if (actionRequest.isWorkgroupRequest() && !actionRequest.getWorkgroup().hasMember(this.user)) {
                continue;
            }*/

            String request = actionRequest.getActionRequested();

            if ( (KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(request)) ||
                 (KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ.equals(request)) ||
                 (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(request)) ||
                 (KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(request)) ) {
                actionCompatible = true;
                break;
            }

            // RETURN_TO_PREVIOUS_ROUTE_LEVEL action available only if you've been routed a complete or approve request
            if (KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD.equals(actionTakenCode) &&
                    (KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(request) || KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(request))) {
                actionCompatible = true;
            }
        }

        return actionCompatible;
    }

    public void recordAction() throws InvalidActionTakenException {
        MDC.put("docId", getRouteHeader().getDocumentId());
        updateSearchableAttributesIfPossible();
        LOG.debug("Returning document " + getRouteHeader().getDocumentId() + " to previous node: " + nodeName + ", annotation: " + annotation);

        List actionRequests = getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), getDocumentId(), KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        String errorMessage = validateActionRules(actionRequests);
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

            Collection activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(getRouteHeader().getDocumentId());
            NodeGraphSearchCriteria criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, activeNodeInstances, nodeName);
            NodeGraphSearchResult result = KEWServiceLocator.getRouteNodeService().searchNodeGraph(criteria);
            validateReturnPoint(nodeName, activeNodeInstances, result);

            LOG.debug("Record the returnToPreviousNode action");
            // determines the highest priority delegator in the list of action requests
            // this delegator will be used to save the action taken, and omitted from notification request generation
            Recipient delegator = findDelegatorForActionRequests(actionRequests);
            ActionTakenValue actionTaken = saveActionTaken(Boolean.FALSE, delegator);

            LOG.debug("Finding requests in return path and setting current indicator to FALSE");
            List<ActionRequestValue> doneRequests = new ArrayList<ActionRequestValue>();
            List<ActionRequestValue> pendingRequests = new ArrayList<ActionRequestValue>();
            for (RouteNodeInstance nodeInstance : (List<RouteNodeInstance>)result.getPath()) {
            	// mark the node instance as having been revoked
            	KEWServiceLocator.getRouteNodeService().revokeNodeInstance(getRouteHeader(), nodeInstance);
                List<ActionRequestValue> nodeRequests = getActionRequestService().findRootRequestsByDocIdAtRouteNode(getRouteHeader().getDocumentId(), nodeInstance.getRouteNodeInstanceId());
                for (ActionRequestValue request : nodeRequests) {
                    if (request.isDone()) {
                        doneRequests.add(request);
                    } else {
                        pendingRequests.add(request);
                    }
                }
            }
            revokePreviousRequests(doneRequests, getPrincipal(), delegator);
            LOG.debug("Change pending requests to FYI and activate for docId " + getRouteHeader().getDocumentId());
            revokePendingRequests(pendingRequests, actionTaken, getPrincipal(), delegator);
            notifyActionTaken(actionTaken);
            executeNodeChange(activeNodeInstances, result);
            sendAdditionalNotifications();
    }

    /**
     * Template method subclasses can use to send addition notification upon a return to previous action.
     * This occurs after the postprocessors have been called and the node has been changed
     */
    protected void sendAdditionalNotifications() {
        // no implementation
    }

    /**
     * This method runs various validation checks on the nodes we ended up at so as to make sure we don't
     * invoke strange return scenarios.
     */
    private void validateReturnPoint(String nodeName, Collection activeNodeInstances, NodeGraphSearchResult result) throws InvalidActionTakenException {
    	RouteNodeInstance resultNodeInstance = result.getResultNodeInstance();
        if (result.getResultNodeInstance() == null) {
            throw new InvalidActionTakenException("Could not locate return point for node name '"+nodeName+"'.");
        }
        assertValidNodeType(resultNodeInstance);
        assertValidBranch(resultNodeInstance, activeNodeInstances);
        assertValidProcess(resultNodeInstance, activeNodeInstances);
        assertFinalApprovalNodeNotInPath(result.getPath());
    }

    private void assertValidNodeType(RouteNodeInstance resultNodeInstance) throws InvalidActionTakenException {
        // the return point can only be a simple or a split node
        if (!helper.isSimpleNode(resultNodeInstance.getRouteNode()) && !helper.isSplitNode(resultNodeInstance.getRouteNode())) {
        	throw new InvalidActionTakenException("Can only return to a simple or a split node, attempting to return to " + resultNodeInstance.getRouteNode().getNodeType());
        }
    }

    private void assertValidBranch(RouteNodeInstance resultNodeInstance, Collection activeNodeInstances) throws InvalidActionTakenException {
        // the branch of the return point needs to be the same as one of the branches of the active nodes or the same as the root branch
        boolean inValidBranch = false;
        if (resultNodeInstance.getBranch().getParentBranch() == null) {
        	inValidBranch = true;
        } else {
        	for (Iterator iterator = activeNodeInstances.iterator(); iterator.hasNext(); ) {
				RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
				if (nodeInstance.getBranch().getBranchId().equals(resultNodeInstance.getBranch().getBranchId())) {
					inValidBranch = true;
					break;
				}
			}
        }
        if (!inValidBranch) {
        	throw new InvalidActionTakenException("Returning to an illegal branch, can only return to node within the same branch as an active node or to the primary branch.");
        }
    }

    private void assertValidProcess(RouteNodeInstance resultNodeInstance, Collection activeNodeInstances) throws InvalidActionTakenException {
        // if we are in a process, we need to return within the same process
        if (resultNodeInstance.isInProcess()) {
        	boolean inValidProcess = false;
        	for (Iterator iterator = activeNodeInstances.iterator(); iterator.hasNext(); ) {
				RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
				if (nodeInstance.isInProcess() && nodeInstance.getProcess().getRouteNodeInstanceId().equals(nodeInstance.getProcess().getRouteNodeInstanceId())) {
					inValidProcess = true;
					break;
				}
        	}
        	if (!inValidProcess) {
        		throw new InvalidActionTakenException("Returning into an illegal process, cannot return to node within a previously executing process.");
        	}
        }
    }

    /**
     * Cannot return past a COMPLETE final approval node.  This means that you can return from an active and incomplete final approval node.
     * @param path
     * @throws InvalidActionTakenException
     */
    private void assertFinalApprovalNodeNotInPath(List path) throws InvalidActionTakenException {
    	for (Iterator iterator = path.iterator(); iterator.hasNext(); ) {
			RouteNodeInstance  nodeInstance = (RouteNodeInstance ) iterator.next();
			// if we have a complete final approval node in our path, we cannot return past it
			if (nodeInstance.isComplete() && Boolean.TRUE.equals(nodeInstance.getRouteNode().getFinalApprovalInd())) {
				throw new InvalidActionTakenException("Cannot return past or through the final approval node '"+nodeInstance.getName()+"'.");
			}
		}
    }

    private void executeNodeChange(Collection activeNodes, NodeGraphSearchResult result) throws InvalidActionTakenException {
        Integer oldRouteLevel = null;
        Integer newRouteLevel = null;
        if (CompatUtils.isRouteLevelCompatible(getRouteHeader())) {
            int returnPathLength = result.getPath().size()-1;
            oldRouteLevel = getRouteHeader().getDocRouteLevel();
            newRouteLevel = oldRouteLevel - returnPathLength;
            LOG.debug("Changing route header "+ getRouteHeader().getDocumentId()+" route level for backward compatibility to "+newRouteLevel);
            getRouteHeader().setDocRouteLevel(newRouteLevel);
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(routeHeader);
        }
        List<RouteNodeInstance> startingNodes = determineStartingNodes(result.getPath(), activeNodes);
        RouteNodeInstance newNodeInstance = materializeReturnPoint(startingNodes, result);
        for (RouteNodeInstance activeNode : startingNodes)
        {
            notifyNodeChange(oldRouteLevel, newRouteLevel, activeNode, newNodeInstance);
        }
        processReturnToInitiator(newNodeInstance);
    }

    private void notifyNodeChange(Integer oldRouteLevel, Integer newRouteLevel, RouteNodeInstance oldNodeInstance, RouteNodeInstance newNodeInstance) throws InvalidActionTakenException {
        try {
            LOG.debug("Notifying post processor of route node change '"+oldNodeInstance.getName()+"'->'"+newNodeInstance.getName());
            PostProcessor postProcessor = routeHeader.getDocumentType().getPostProcessor();
            KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
            DocumentRouteLevelChange routeNodeChange = new DocumentRouteLevelChange(routeHeader.getDocumentId(),
                    routeHeader.getAppDocId(),
                    oldRouteLevel, newRouteLevel,
                    oldNodeInstance.getName(), newNodeInstance.getName(),
                    oldNodeInstance.getRouteNodeInstanceId(), newNodeInstance.getRouteNodeInstanceId());
            ProcessDocReport report = postProcessor.doRouteLevelChange(routeNodeChange);
            setRouteHeader(KEWServiceLocator.getRouteHeaderService().getRouteHeader(getDocumentId()));
            if (!report.isSuccess()) {
                LOG.warn(report.getMessage(), report.getProcessException());
                throw new InvalidActionTakenException(report.getMessage());
            }
        } catch (Exception ex) {
            throw new WorkflowRuntimeException(ex.getMessage());
        }
    }

    private List<RouteNodeInstance> determineStartingNodes(List path, Collection<RouteNodeInstance> activeNodes) {
    	List<RouteNodeInstance> startingNodes = new ArrayList<RouteNodeInstance>();
        for (RouteNodeInstance activeNodeInstance : activeNodes)
        {
            if (isInPath(activeNodeInstance, path))
            {
                startingNodes.add(activeNodeInstance);
            }
        }
    	return startingNodes;
    }

    private boolean isInPath(RouteNodeInstance nodeInstance, List<RouteNodeInstance> path) {
        for (RouteNodeInstance pathNodeInstance : path)
        {
            if (pathNodeInstance.getRouteNodeInstanceId().equals(nodeInstance.getRouteNodeInstanceId()))
            {
                return true;
            }
        }
    	return false;
    }

    private RouteNodeInstance materializeReturnPoint(Collection<RouteNodeInstance> startingNodes, NodeGraphSearchResult result) {
        RouteNodeService nodeService = KEWServiceLocator.getRouteNodeService();
        RouteNodeInstance returnInstance = result.getResultNodeInstance();
        RouteNodeInstance newNodeInstance = helper.getNodeFactory().createRouteNodeInstance(getDocumentId(), returnInstance.getRouteNode());
        newNodeInstance.setBranch(returnInstance.getBranch());
        newNodeInstance.setProcess(returnInstance.getProcess());
        newNodeInstance.setComplete(false);
        newNodeInstance.setActive(true);
        nodeService.save(newNodeInstance);
        for (RouteNodeInstance activeNodeInstance : startingNodes) {
            // TODO what if the activeNodeInstance already has next nodes?
            activeNodeInstance.setComplete(true);
            activeNodeInstance.setActive(false);
            activeNodeInstance.setInitial(false);
            activeNodeInstance.addNextNodeInstance(newNodeInstance);
        }
        for (RouteNodeInstance activeNodeInstance : startingNodes)
        {
            nodeService.save(activeNodeInstance);
        }
        // TODO really we need to call transitionTo on this node, how can we do that?
        // this isn't an issue yet because we only allow simple nodes and split nodes at the moment which do no real
        // work on transitionTo but we may need to enhance that in the future
        return newNodeInstance;
    }

    public boolean isSuperUserUsage() {
        return superUserUsage;
    }
    public void setSuperUserUsage(boolean superUserUsage) {
        this.superUserUsage = superUserUsage;
    }

}
