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
package org.kuali.rice.kew.actionrequest.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actionrequest.dao.ActionRequestDAO;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentRefreshQueue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.actiontaken.service.ActionTakenService;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.routemodule.RouteModule;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.FutureRequestDocumentStateManager;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kew.util.ResponsibleParty;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;


/**
 * Default implementation of the {@link ActionRequestService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRequestServiceImpl implements ActionRequestService {
    private static final Logger LOG = Logger.getLogger(ActionRequestServiceImpl.class);

    private ActionRequestDAO actionRequestDAO;

    public ActionRequestValue findByActionRequestId(String actionRequestId) {
        return getActionRequestDAO().getActionRequestByActionRequestId(actionRequestId);
    }

    public Map<String, String> getActionsRequested(DocumentRouteHeaderValue routeHeader, String principalId, boolean completeAndApproveTheSame) {
    	return getActionsRequested(principalId, routeHeader.getActionRequests(), completeAndApproveTheSame);
    }
    
    /**
     * Returns a Map of actions that are requested for the given principalId in the given list of action requests.
     * @param principalId
     * @param actionRequests
     * @param completeAndApproveTheSame
     * @return
     */
    protected Map<String, String> getActionsRequested(String principalId, List<ActionRequestValue> actionRequests, boolean completeAndApproveTheSame) {
    	Map<String, String> actionsRequested = new HashMap<String, String>();
        actionsRequested.put(KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
        actionsRequested.put(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
        actionsRequested.put(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "false");
        actionsRequested.put(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "false");
    	String topActionRequested = KewApiConstants.ACTION_REQUEST_FYI_REQ;
        for (ActionRequestValue actionRequest : actionRequests) {
            // we are getting the full list of requests here, so no need to look at role requests, if we did this then
            // we could get a "false positive" for "all approve" roles where only part of the request graph is marked
            // as "done"
            if (!RecipientType.ROLE.getCode().equals(actionRequest.getRecipientTypeCd()) &&
                    actionRequest.isRecipientRoutedRequest(principalId) && actionRequest.isActive()) {
                int actionRequestComparison = ActionRequestValue.compareActionCode(actionRequest.getActionRequested(), topActionRequested, completeAndApproveTheSame);
                if (actionRequest.isFYIRequest() && actionRequestComparison >= 0) {
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_FYI_REQ, "true");
                } else if (actionRequest.isAcknowledgeRequest() && actionRequestComparison >= 0) {
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "true");
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
                    topActionRequested = actionRequest.getActionRequested();
                } else if (actionRequest.isApproveRequest() && actionRequestComparison >= 0) {
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
                    topActionRequested = actionRequest.getActionRequested();
                } else if (actionRequest.isCompleteRequst() && actionRequestComparison >= 0) {
                	actionsRequested.put(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "true");
                	actionsRequested.put(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "false");
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
                    actionsRequested.put(KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
                	if (completeAndApproveTheSame) {
                		actionsRequested.put(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
                	}
                    topActionRequested = actionRequest.getActionRequested();
                }
            }
        }
        return actionsRequested;
    }

    public ActionRequestValue initializeActionRequestGraph(ActionRequestValue actionRequest,
            DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) {
        if (actionRequest.getParentActionRequest() != null) {
            LOG.warn("-->A non parent action request from doc " + document.getDocumentId());
            actionRequest = KEWServiceLocator.getActionRequestService().getRoot(actionRequest);
        }
        propagatePropertiesToRequestGraph(actionRequest, document, nodeInstance);
        return actionRequest;
    }

    private void propagatePropertiesToRequestGraph(ActionRequestValue actionRequest, DocumentRouteHeaderValue document,
            RouteNodeInstance nodeInstance) {
        setPropertiesToRequest(actionRequest, document, nodeInstance);
        for (ActionRequestValue actionRequestValue : actionRequest.getChildrenRequests())
        {
            propagatePropertiesToRequestGraph(actionRequestValue, document, nodeInstance);
        }
    }

    private void setPropertiesToRequest(ActionRequestValue actionRequest, DocumentRouteHeaderValue document,
            RouteNodeInstance nodeInstance) {
        actionRequest.setDocumentId(document.getDocumentId());
        actionRequest.setDocVersion(document.getDocVersion());
        actionRequest.setRouteLevel(document.getDocRouteLevel());
        actionRequest.setNodeInstance(nodeInstance);
        actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());
    }



    public void activateRequests(Collection actionRequests) {
        activateRequests(actionRequests, new ActivationContext(!ActivationContext.CONTEXT_IS_SIMULATION));
    }

    public void activateRequests(Collection actionRequests, boolean simulate) {
        activateRequests(actionRequests, new ActivationContext(simulate));
    }

    public void activateRequests(Collection actionRequests, ActivationContext activationContext) {
        if (actionRequests == null) {
            return;
        }
        PerformanceLogger performanceLogger = null;
        if ( LOG.isInfoEnabled() ) {
        	performanceLogger = new PerformanceLogger();
        }
        activationContext.setGeneratedActionItems(new ArrayList<ActionItem>());
        activateRequestsInternal(actionRequests, activationContext);
        if (!activationContext.isSimulation()) {
            KEWServiceLocator.getNotificationService().notify(ActionItem.to(activationContext.getGeneratedActionItems()));
        }
        if ( LOG.isInfoEnabled() ) {
        	performanceLogger.log("Time to " + (activationContext.isSimulation() ? "simulate activation of " : "activate ")
        			+ actionRequests.size() + " action requests.");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Generated " + activationContext.getGeneratedActionItems().size() + " action items.");
        }
    }

    public void activateRequest(ActionRequestValue actionRequest) {
        activateRequests(Collections.singletonList(actionRequest), new ActivationContext(!ActivationContext.CONTEXT_IS_SIMULATION));
    }

    public void activateRequest(ActionRequestValue actionRequest, boolean simulate) {
        activateRequests(Collections.singletonList(actionRequest), new ActivationContext(simulate));
    }

    public void activateRequest(ActionRequestValue actionRequest, ActivationContext activationContext) {
        activateRequests(Collections.singletonList(actionRequest), activationContext);
    }

    public List activateRequestNoNotification(ActionRequestValue actionRequest, boolean simulate) {
        return activateRequestNoNotification(actionRequest, new ActivationContext(simulate));
    }

    public List activateRequestNoNotification(ActionRequestValue actionRequest, ActivationContext activationContext) {
        activationContext.setGeneratedActionItems(new ArrayList<ActionItem>());
        activateRequestInternal(actionRequest, activationContext);
        return activationContext.getGeneratedActionItems();
    }

    /**
     * Internal helper method for activating a Collection of action requests and their children. Maintains an accumulator
     * for generated action items.
     * @param actionRequests
     * @param activationContext
     */
    private void activateRequestsInternal(Collection actionRequests, ActivationContext activationContext) {
        if (actionRequests == null) {
            return;
        }
        List<?> actionRequestList = new ArrayList<Object>(actionRequests);
        for (int i = 0; i < actionRequestList.size(); i++) {
        	activateRequestInternal((ActionRequestValue) actionRequestList.get(i), activationContext);
        }
    }

    /**
     * Internal helper method for activating a single action requests and it's children. Maintains an accumulator for
     * generated action items.
     */
    private void activateRequestInternal(ActionRequestValue actionRequest, ActivationContext activationContext) {
        PerformanceLogger performanceLogger = null;
        if ( LOG.isInfoEnabled() ) {
        	performanceLogger = new PerformanceLogger();
        }
        if (actionRequest == null || actionRequest.isActive() || actionRequest.isDeactivated()) {
            return;
        }
        processResponsibilityId(actionRequest);
        if (deactivateOnActionAlreadyTaken(actionRequest, activationContext)) {
            return;
        }
        if (deactivateOnInactiveGroup(actionRequest, activationContext)) {
            return;
        }
        if (deactivateOnEmptyGroup(actionRequest, activationContext)) {
        	return;
        }
        actionRequest.setStatus(ActionRequestStatus.ACTIVATED.getCode());
        if (!activationContext.isSimulation()) {
            saveActionRequest(actionRequest);
            activationContext.getGeneratedActionItems().addAll(generateActionItems(actionRequest, activationContext));
        }
        activateRequestsInternal(actionRequest.getChildrenRequests(), activationContext);
        activateRequestInternal(actionRequest.getParentActionRequest(), activationContext);
        if ( LOG.isInfoEnabled() ) {
        	if (activationContext.isSimulation()) {
                performanceLogger.log("Time to simulate activation of request.");
	        } else {
	            performanceLogger.log("Time to activate action request with id " + actionRequest.getActionRequestId());
	        }
        }
    }

    /**
     * Generates ActionItems for the given ActionRequest and returns the List of generated Action Items.
     *
     * @param actionRequest
     * @param activationContext
     * @return the List of generated ActionItems
     */
    private List<ActionItem> generateActionItems(ActionRequestValue actionRequest, ActivationContext activationContext) {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("generating the action items for request " + actionRequest.getActionRequestId());
    	}
        List<ActionItem> actionItems = new ArrayList<ActionItem>();
        if (!actionRequest.isPrimaryDelegator()) {
            if (actionRequest.isGroupRequest()) {
                List<String> principalIds =  KimApiServiceLocator.getGroupService().getMemberPrincipalIds(actionRequest.getGroupId());
                actionItems.addAll(createActionItemsForPrincipals(actionRequest, principalIds));
            } else if (actionRequest.isUserRequest()) {
                ActionItem actionItem = getActionListService().createActionItemForActionRequest(actionRequest);
                actionItems.add(actionItem);
            }
        }
        if (!activationContext.isSimulation()) {
            for (ActionItem actionItem: actionItems) {
            	if ( LOG.isDebugEnabled() ) {
            		LOG.debug("Saving action item: " + actionItems);
            	}
                getActionListService().saveActionItem(actionItem);
            }
        } else {
        	actionRequest.getSimulatedActionItems().addAll(actionItems);
        }
        return actionItems;
    }

    private List<ActionItem> createActionItemsForPrincipals(ActionRequestValue actionRequest, List<String> principalIds) {
        List<ActionItem> actionItems = new ArrayList<ActionItem>();
        for (String principalId: principalIds) {

            ActionItem actionItem = getActionListService().createActionItemForActionRequest(actionRequest);
            actionItem.setPrincipalId(principalId);
            actionItem.setRoleName(actionRequest.getQualifiedRoleName());

            //KULRICE-3307 Prevent workflow from attempting to activate requests for null principals
            String ignoreUnknownPrincipalIdsValue = ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.WORKFLOW_ACTION_IGNORE_UNKOWN_PRINCIPAL_IDS);
            boolean ignoreUnknownPrincipalIds = Boolean.parseBoolean(ignoreUnknownPrincipalIdsValue);

            if(principalId==null && ignoreUnknownPrincipalIds)
            {
                LOG.warn("Ignoring action item with actionRequestID of " + actionRequest.getActionRequestId()  + " due to null principalId.");
            }
            else
            {
                if(principalId==null)
                {
                    IllegalArgumentException e = new IllegalArgumentException("Exception thrown when trying to add action item with null principalId");
                    LOG.error(e);
                    throw e;
                }
                else
                {
                    actionItems.add(actionItem);
                }
            }
        }
        return actionItems;
    }

    private void processResponsibilityId(ActionRequestValue actionRequest) {
    	if (actionRequest.getResolveResponsibility()) {
	        String responsibilityId = actionRequest.getResponsibilityId();
	        try {
	            RouteModule routeModule = KEWServiceLocator.getRouteModuleService().findRouteModule(actionRequest);
	            if (responsibilityId != null && actionRequest.isRouteModuleRequest()) {
	            	if ( LOG.isDebugEnabled() ) {
	            		LOG.debug("Resolving responsibility id for action request id=" + actionRequest.getActionRequestId()
	                        + " and responsibility id=" + actionRequest.getResponsibilityId());
	            	}
	                ResponsibleParty responsibleParty = routeModule.resolveResponsibilityId(actionRequest.getResponsibilityId());
	                if (responsibleParty == null) {
	                    return;
	                }
	                if (responsibleParty.getPrincipalId() != null) {
	                    Principal user = KimApiServiceLocator.getIdentityService()
	                            .getPrincipal(responsibleParty.getPrincipalId());
	                    actionRequest.setPrincipalId(user.getPrincipalId());
	                } else if (responsibleParty.getGroupId() != null) {
	                	actionRequest.setGroupId(responsibleParty.getGroupId());
	                } else if (responsibleParty.getRoleName() != null) {
	                    actionRequest.setRoleName(responsibleParty.getRoleName());
	                }
	            }
	        } catch (Exception e) {
	            LOG.error("Exception thrown when trying to resolve responsibility id " + responsibilityId, e);
	            throw new RuntimeException(e);
	        }
    	}
    }

    protected boolean deactivateOnActionAlreadyTaken(ActionRequestValue actionRequestToActivate,
            ActivationContext activationContext) {

        FutureRequestDocumentStateManager futureRequestStateMngr = null;

        if (actionRequestToActivate.isGroupRequest()) {
            futureRequestStateMngr = new FutureRequestDocumentStateManager(actionRequestToActivate.getRouteHeader(), actionRequestToActivate.getGroup());
        } else if (actionRequestToActivate.isUserRequest()) {
            futureRequestStateMngr = new FutureRequestDocumentStateManager(actionRequestToActivate.getRouteHeader(), actionRequestToActivate.getPrincipalId());
        } else {
            return false;
        }

        if (futureRequestStateMngr.isReceiveFutureRequests()) {
            return false;
        }
        if (!actionRequestToActivate.getForceAction() || futureRequestStateMngr.isDoNotReceiveFutureRequests()) {
            ActionTakenValue previousActionTaken = null;
            if (!activationContext.isSimulation()) {
                previousActionTaken = getActionTakenService().getPreviousAction(actionRequestToActivate);
            } else {
                previousActionTaken = getActionTakenService().getPreviousAction(actionRequestToActivate,
                        activationContext.getSimulatedActionsTaken());
            }
            if (previousActionTaken != null) {
                if ( LOG.isDebugEnabled() ) {
                	LOG.debug("found a satisfying action taken so setting this request done.  Action Request Id "
                            + actionRequestToActivate.getActionRequestId());
                }
                // set up the delegation for an action taken if this is a delegate request and the delegate has
                // already taken action.
                if (!previousActionTaken.isForDelegator() && actionRequestToActivate.getParentActionRequest() != null) {
                    previousActionTaken.setDelegator(actionRequestToActivate.getParentActionRequest().getRecipient());
                    if (!activationContext.isSimulation()) {
                        getActionTakenService().saveActionTaken(previousActionTaken);
                    }
                }
                deactivateRequest(previousActionTaken, actionRequestToActivate, null, activationContext);
                return true;
            }
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Forcing action for action request " + actionRequestToActivate.getActionRequestId());
        }
        return false;
    }
    
    /**
     * Checks if the action request which is being activated has a group with no members.  If this is the case then it will immediately
     * initiate de-activation on the request since a group with no members will result in no action items being generated so should be
     * effectively skipped.
     */
    protected boolean deactivateOnEmptyGroup(ActionRequestValue actionRequestToActivate, ActivationContext activationContext) {
    	if (actionRequestToActivate.isGroupRequest()) {
    		 if (KimApiServiceLocator.getGroupService().getMemberPrincipalIds(actionRequestToActivate.getGroup().getId()).isEmpty()) {
    			 deactivateRequest(null, actionRequestToActivate, null, activationContext);
    			 return true;
         	}
    	}
    	return false;
    }

    /**
     * Checks if the action request which is being activated is being assigned to an inactive group.  If this is the case and if the FailOnInactiveGroup 
     * policy is set to false then it will immediately initiate de-activation on the request
     */
    protected boolean deactivateOnInactiveGroup(ActionRequestValue actionRequestToActivate, ActivationContext activationContext) {
        if (actionRequestToActivate.isGroupRequest()) {
            if (!actionRequestToActivate.getGroup().isActive() && !actionRequestToActivate.getRouteHeader().getDocumentType().getFailOnInactiveGroup().getPolicyValue()) {
                deactivateRequest(null, actionRequestToActivate, null, activationContext);
                return true;
            }
        }
        return false;
    }
    
    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest) {
        deactivateRequest(actionTaken, actionRequest, null, new ActivationContext(!ActivationContext.CONTEXT_IS_SIMULATION));
    }

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest, boolean simulate) {
        deactivateRequest(actionTaken, actionRequest, null, new ActivationContext(simulate));
    }

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest,
            ActivationContext activationContext) {
        deactivateRequest(actionTaken, actionRequest, null, activationContext);
    }

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests) {
        deactivateRequests(actionTaken, actionRequests, null,
                new ActivationContext(!ActivationContext.CONTEXT_IS_SIMULATION));
    }

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, boolean simulate) {
        deactivateRequests(actionTaken, actionRequests, null, new ActivationContext(simulate));
    }

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, ActivationContext activationContext) {
        deactivateRequests(actionTaken, actionRequests, null, activationContext);
    }

    private void deactivateRequests(ActionTakenValue actionTaken, Collection actionRequests,
            ActionRequestValue deactivationRequester, ActivationContext activationContext) {
        if (actionRequests == null) {
            return;
        }
        for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
            deactivateRequest(actionTaken, actionRequest, deactivationRequester, activationContext);
        }
    }

    private void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest,
            ActionRequestValue deactivationRequester, ActivationContext activationContext) {
        if (actionRequest == null || actionRequest.isDeactivated()
                || haltForAllApprove(actionRequest, deactivationRequester)) {
            return;
        }
        actionRequest.setStatus(ActionRequestStatus.DONE.getCode());
        actionRequest.setActionTaken(actionTaken);
        if (actionTaken != null) {
            actionTaken.getActionRequests().add(actionRequest);
        }
        if (!activationContext.isSimulation()) {
            getActionRequestDAO().saveActionRequest(actionRequest);
            deleteActionItems(actionRequest);
        }
        deactivateRequests(actionTaken, actionRequest.getChildrenRequests(), actionRequest, activationContext);
        deactivateRequest(actionTaken, actionRequest.getParentActionRequest(), actionRequest, activationContext);
    }

    /**
     * Returns true if we are dealing with an 'All Approve' request, the requester of the deactivation is a child of the
     * 'All Approve' request, and all of the children have not been deactivated. If all of the children are already
     * deactivated or a non-child request initiated deactivation, then this method returns false. false otherwise.
     * @param actionRequest
     * @param deactivationRequester
     * @return
     */
    private boolean haltForAllApprove(ActionRequestValue actionRequest, ActionRequestValue deactivationRequester) {
        if (ActionRequestPolicy.ALL.getCode().equals(actionRequest.getApprovePolicy())
                && actionRequest.hasChild(deactivationRequester)) {
            boolean allDeactivated = true;
            for (ActionRequestValue childRequest : actionRequest.getChildrenRequests())
            {
                if (!(allDeactivated = childRequest.isDeactivated()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ActionRequestValue> getRootRequests(Collection<ActionRequestValue> actionRequests) {
    	Set<ActionRequestValue> unsavedRequests = new HashSet<ActionRequestValue>();
    	Map<String, ActionRequestValue> requestMap = new HashMap<String, ActionRequestValue>();
    	for (ActionRequestValue actionRequest1 : actionRequests)
    	{
    		ActionRequestValue actionRequest = (ActionRequestValue) actionRequest1;
    		ActionRequestValue rootRequest = getRoot(actionRequest);
    		if (rootRequest.getActionRequestId() != null)
    		{
    			requestMap.put(rootRequest.getActionRequestId(), rootRequest);
    		} else
    		{
    			unsavedRequests.add(rootRequest);
    		}
    	}
    	List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
    	requests.addAll(requestMap.values());
    	requests.addAll(unsavedRequests);
    	return requests;
    }

    public ActionRequestValue getRoot(ActionRequestValue actionRequest) {
        if (actionRequest == null) {
            return null;
        }
        if (actionRequest.getParentActionRequest() != null) {
            return getRoot(actionRequest.getParentActionRequest());
        }
        return actionRequest;
    }
    
    /**
     * Returns all pending requests for a given routing identity
     * @param documentId the id of the document header being routed
     * @return a List of all pending ActionRequestValues for the document
     */
    public List<ActionRequestValue> findAllPendingRequests(String documentId) {
    	ActionRequestDAO arDAO = getActionRequestDAO();
        List<ActionRequestValue> pendingArs = arDAO.findByStatusAndDocId(ActionRequestStatus.ACTIVATED.getCode(), documentId);
        return pendingArs;
    }

    public List findAllValidRequests(String principalId, String documentId, String requestCode) {
        ActionRequestDAO arDAO = getActionRequestDAO();
        Collection pendingArs = arDAO.findByStatusAndDocId(ActionRequestStatus.ACTIVATED.getCode(), documentId);
        return findAllValidRequests(principalId, pendingArs, requestCode);
    }

    public List findAllValidRequests(String principalId, Collection actionRequests, String requestCode) {
        List matchedArs = new ArrayList();
        List<String> arGroups = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
        return filterActionRequestsByCode((List<ActionRequestValue>)actionRequests, principalId, arGroups, requestCode);
    }
    
    /**
	 * Filters action requests based on if they occur after the given requestCode, and if they relate to 
	 * the given principal
	 * @param actionRequests the List of ActionRequestValues to filter
	 * @param principalId the id of the principal to find active requests for
	 * @param principalGroupIds List of group ids that the principal belongs to
	 * @param requestCode the request code for all ActionRequestValues to be after
	 * @return the filtered List of ActionRequestValues
	 */
	public List<ActionRequestValue> filterActionRequestsByCode(List<ActionRequestValue> actionRequests, String principalId, List<String> principalGroupIds, String requestCode) {
		List<ActionRequestValue> filteredActionRequests = new ArrayList<ActionRequestValue>();
		
		List<String> arGroups = null;
        for (ActionRequestValue ar : actionRequests) {
            if (ActionRequestValue.compareActionCode(ar.getActionRequested(), requestCode, true) > 0) {
                continue;
            }
            if (ar.isUserRequest() && principalId.equals(ar.getPrincipalId())) {
            	filteredActionRequests.add(ar);
            } else if (ar.isGroupRequest() && principalGroupIds != null && !principalGroupIds.isEmpty()) {
            	for (String groupId : principalGroupIds) {
            		if (groupId.equals(ar.getGroupId())) {
            			filteredActionRequests.add(ar);
            		}
            	}
            }
        }
		
		return filteredActionRequests;
	}

    public void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds) {
    	PerformanceLogger performanceLogger = null;
    	if ( LOG.isInfoEnabled() ) {
    		performanceLogger = new PerformanceLogger();
    	}
        Collection documentsAffected = getRouteHeaderService().findPendingByResponsibilityIds(responsibilityIds);
        String cacheWaitValue = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.RULE_DETAIL_TYPE, KewApiConstants.RULE_CACHE_REQUEUE_DELAY);
        Long cacheWait = KewApiConstants.DEFAULT_CACHE_REQUEUE_WAIT_TIME;
        if (!org.apache.commons.lang.StringUtils.isEmpty(cacheWaitValue)) {
            try {
                cacheWait = Long.valueOf(cacheWaitValue);
            } catch (NumberFormatException e) {
                LOG.warn("Cache wait time is not a valid number: " + cacheWaitValue);
            }
        }
        if ( LOG.isInfoEnabled() ) {
        	LOG.info("Scheduling requeue of " + documentsAffected.size() + " documents, affected by " + responsibilityIds.size()
                    + " responsibility changes.  Installing a processing wait time of " + cacheWait
                    + " milliseconds to avoid stale rule cache.");
        }
        for (Object aDocumentsAffected : documentsAffected)
        {
            String documentId = (String) aDocumentsAffected;

             String applicationId = null;
             DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByDocumentId(documentId);
                    
             if (documentType != null) {
                applicationId = documentType.getApplicationId();
             }

            if (applicationId == null)
            {
                applicationId = CoreConfigHelper.getApplicationId();
            }
            if(documentType.getRegenerateActionRequestsOnChange().getPolicyValue()) {
                DocumentRefreshQueue documentRequeuer = KewApiServiceLocator.getDocumentRequeuerService(applicationId,
                        documentId, cacheWait);
                documentRequeuer.refreshDocument(documentId);
            }
        }
        if ( LOG.isInfoEnabled() ) {
        	performanceLogger.log("Time to updateActionRequestsForResponsibilityChange");
        }
    }

    /**
     * Deletes an action request and all of its action items following the graph down through the action request's
     * children. This method should be invoked on a top-level action request.
     */
    public void deleteActionRequestGraph(ActionRequestValue actionRequest) {
        deleteActionItems(actionRequest);
        if (actionRequest.getActionTakenId() != null) {
            ActionTakenValue actionTaken = getActionTakenService().findByActionTakenId(actionRequest.getActionTakenId());

            if(actionTaken != null){//iu patch
            getActionTakenService().delete(actionTaken);
            }//iu patch
           
        }
        getActionRequestDAO().delete(actionRequest.getActionRequestId());
        for (ActionRequestValue child: actionRequest.getChildrenRequests()) {
            deleteActionRequestGraph(child);
        }
    }

    /**
     * Deletes the action items for the action request
     * @param actionRequest the action request whose action items to delete
     */
    private void deleteActionItems(ActionRequestValue actionRequest) {
    	List<ActionItem> actionItems = actionRequest.getActionItems();
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("deleting " + actionItems.size() + " action items for action request: " + actionRequest);
    	}
        for (ActionItem actionItem: actionItems) {
        	if ( LOG.isDebugEnabled() ) {
        		LOG.debug("deleting action item: " + actionItem);
        	}
            getActionListService().deleteActionItem(actionItem);
        }
    }


    public List<ActionRequestValue> findByDocumentIdIgnoreCurrentInd(String documentId) {
        return getActionRequestDAO().findByDocumentIdIgnoreCurrentInd(documentId);
    }

    public List<ActionRequestValue> findAllActionRequestsByDocumentId(String documentId) {
        return getActionRequestDAO().findAllByDocId(documentId);
    }

    public List<ActionRequestValue> findAllRootActionRequestsByDocumentId(String documentId) {
        return getActionRequestDAO().findAllRootByDocId(documentId);
    }

    public List<ActionRequestValue> findPendingByActionRequestedAndDocId(String actionRequestedCd, String documentId) {
        return getActionRequestDAO().findPendingByActionRequestedAndDocId(actionRequestedCd, documentId);
    }

    /**
     * @see org.kuali.rice.kew.actionrequest.service.ActionRequestService#getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(java.lang.String, java.lang.String)
     */
    public List<String> getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(String actionRequestedCd, String documentId) {
    	List<String> principalIds = new ArrayList<String>();
    	List<ActionRequestValue> actionRequests = findPendingByActionRequestedAndDocId(actionRequestedCd, documentId);
		for(ActionRequestValue actionRequest: actionRequests){
			if(actionRequest.isUserRequest()){
				principalIds.add(actionRequest.getPrincipalId());
			} else if(actionRequest.isGroupRequest()){
				principalIds.addAll(
						KimApiServiceLocator.getGroupService().getMemberPrincipalIds(actionRequest.getGroupId()));
			}
		}
    	return principalIds;
    }

    public List<ActionRequestValue> findPendingByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        return getActionRequestDAO().findPendingByDocIdAtOrBelowRouteLevel(documentId, routeLevel);
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocId(String documentId) {
        return getRootRequests(findPendingByDoc(documentId));
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
        return getActionRequestDAO().findPendingRootRequestsByDocIdAtRouteNode(documentId, nodeInstanceId);
    }

    public List<ActionRequestValue> findRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId) {
        return getActionRequestDAO().findRootRequestsByDocIdAtRouteNode(documentId, nodeInstanceId);
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel) {
        return getActionRequestDAO().findPendingRootRequestsByDocIdAtOrBelowRouteLevel(documentId, routeLevel);
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteLevel(String documentId, Integer routeLevel) {
        return getActionRequestDAO().findPendingRootRequestsByDocIdAtRouteLevel(documentId, routeLevel);
    }

    public List<ActionRequestValue> findPendingRootRequestsByDocumentType(String documentTypeId) {
        return getActionRequestDAO().findPendingRootRequestsByDocumentType(documentTypeId);
    }

    public void saveActionRequest(ActionRequestValue actionRequest) {
        if (actionRequest.isGroupRequest()) {
             Group group = actionRequest.getGroup();
             if (group == null)  {
                 throw new RiceRuntimeException("Attempted to save an action request with a non-existent group.");
             }
             if (!group.isActive() && actionRequest.getRouteHeader().getDocumentType().getFailOnInactiveGroup().getPolicyValue()) {
        		throw new RiceRuntimeException("Attempted to save an action request with an inactive group.");
        	}
        }
        getActionRequestDAO().saveActionRequest(actionRequest);
    }

    public List<ActionRequestValue> findPendingByDoc(String documentId) {
        return getActionRequestDAO().findAllPendingByDocId(documentId);
    }

    public List<ActionRequestValue> findPendingByDocRequestCdRouteLevel(String documentId, String requestCode, Integer routeLevel) {
        List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
        for (Object object : getActionRequestDAO().findAllPendingByDocId(documentId))
        {
            ActionRequestValue actionRequest = (ActionRequestValue) object;
            if (ActionRequestValue.compareActionCode(actionRequest.getActionRequested(), requestCode, true) > 0)
            {
                continue;
            }
            if (actionRequest.getRouteLevel().intValue() == routeLevel.intValue())
            {
                requests.add(actionRequest);
            }
        }
        return requests;
    }

    public List<ActionRequestValue> findPendingByDocRequestCdNodeName(String documentId, String requestCode, String nodeName) {
        List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
        for (Object object : getActionRequestDAO().findAllPendingByDocId(documentId))
        {
            ActionRequestValue actionRequest = (ActionRequestValue) object;
            if (ActionRequestValue.compareActionCode(actionRequest.getActionRequested(), requestCode, true) > 0)
            {
                continue;
            }
            if (actionRequest.getNodeInstance() != null && actionRequest.getNodeInstance().getName().equals(nodeName))
            {
                requests.add(actionRequest);
            }
        }
        return requests;
    }

    public List findActivatedByGroup(String groupId) {
        return getActionRequestDAO().findActivatedByGroup(groupId);
    }

    private ActionListService getActionListService() {
        return (ActionListService) KEWServiceLocator.getActionListService();
    }

    private ActionTakenService getActionTakenService() {
        return (ActionTakenService) KEWServiceLocator.getActionTakenService();
    }

    public ActionRequestDAO getActionRequestDAO() {
        return actionRequestDAO;
    }

    public void setActionRequestDAO(ActionRequestDAO actionRequestDAO) {
        this.actionRequestDAO = actionRequestDAO;
    }

    private RouteHeaderService getRouteHeaderService() {
        return (RouteHeaderService) KEWServiceLocator.getService(KEWServiceLocator.DOC_ROUTE_HEADER_SRV);
    }

    public List<ActionRequestValue> findByStatusAndDocId(String statusCd, String documentId) {
        return getActionRequestDAO().findByStatusAndDocId(statusCd, documentId);
    }

    public void alterActionRequested(List actionRequests, String actionRequestCd) {
        for (Object actionRequest1 : actionRequests)
        {
            ActionRequestValue actionRequest = (ActionRequestValue) actionRequest1;

            actionRequest.setActionRequested(actionRequestCd);
            for (ActionItem item : actionRequest.getActionItems())
            {
                item.setActionRequestCd(actionRequestCd);
            }

            saveActionRequest(actionRequest);
        }
    }

    // TODO this still won't work in certain cases when checking from the root
    public boolean isDuplicateRequest(ActionRequestValue actionRequest) {
        List<ActionRequestValue> requests = findAllRootActionRequestsByDocumentId(actionRequest.getDocumentId());
        for (ActionRequestValue existingRequest : requests) {
            if (existingRequest.getStatus().equals(ActionRequestStatus.DONE.getCode())
                    && existingRequest.getRouteLevel().equals(actionRequest.getRouteLevel())
                    && ObjectUtils.equals(existingRequest.getPrincipalId(), actionRequest.getPrincipalId())
                    && ObjectUtils.equals(existingRequest.getGroupId(), actionRequest.getGroupId())
                    && ObjectUtils.equals(existingRequest.getRoleName(), actionRequest.getRoleName())
                    && ObjectUtils.equals(existingRequest.getQualifiedRoleName(), actionRequest.getQualifiedRoleName())
                    && existingRequest.getActionRequested().equals(actionRequest.getActionRequested())) {
                return true;
            }
        }
        return false;
    }

    public Recipient findDelegator(List actionRequests) {
        Recipient delegator = null;
        String requestCode = KewApiConstants.ACTION_REQUEST_FYI_REQ;
        for (Object actionRequest1 : actionRequests)
        {
            ActionRequestValue actionRequest = (ActionRequestValue) actionRequest1;
            ActionRequestValue delegatorRequest = findDelegatorRequest(actionRequest);
            if (delegatorRequest != null)
            {
                if (ActionRequestValue.compareActionCode(delegatorRequest.getActionRequested(), requestCode, true) >= 0)
                {
                    delegator = delegatorRequest.getRecipient();
                    requestCode = delegatorRequest.getActionRequested();
                }
            }
        }
        return delegator;
    }

    public Recipient findDelegator(ActionRequestValue actionRequest) {
        ActionRequestValue delegatorRequest = findDelegatorRequest(actionRequest);
        Recipient delegator = null;
        if (delegatorRequest != null) {
            delegator = delegatorRequest.getRecipient();
        }
        return delegator;
    }

    public ActionRequestValue findDelegatorRequest(ActionRequestValue actionRequest) {
        ActionRequestValue parentRequest = actionRequest.getParentActionRequest();
        if (parentRequest != null && !(parentRequest.isUserRequest() || parentRequest.isGroupRequest())) {
            parentRequest = findDelegatorRequest(parentRequest);
        }
        return parentRequest;
    }

    public void deleteByDocumentId(String documentId) {
        actionRequestDAO.deleteByDocumentId(documentId);
    }

    public void deleteByActionRequestId(String actionRequestId) {
        actionRequestDAO.delete(actionRequestId);
    }

    public void validateActionRequest(ActionRequestValue actionRequest) {
        LOG.debug("Enter validateActionRequest(..)");
        List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();

        String actionRequestCd = actionRequest.getActionRequested();
        if (actionRequestCd == null || actionRequestCd.trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest cd null.", "actionrequest.actionrequestcd.empty",
                    actionRequest.getActionRequestId().toString()));
        } else if (!KewApiConstants.ACTION_REQUEST_CD.containsKey(actionRequestCd)) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest cd invalid.", "actionrequest.actionrequestcd.invalid",
                    actionRequest.getActionRequestId().toString()));
        }

        String documentId = actionRequest.getDocumentId();
        if (documentId == null || StringUtils.isEmpty(documentId)) {
        	errors.add(new WorkflowServiceErrorImpl("ActionRequest Document id empty.", "actionrequest.documentid.empty",
                    actionRequest.getActionRequestId().toString()));
        } else if (getRouteHeaderService().getRouteHeader(documentId) == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest Document id invalid.",
                    "actionrequest.documentid.invalid", actionRequest.getActionRequestId().toString()));
        }

        String actionRequestStatus = actionRequest.getStatus();
        if (actionRequestStatus == null || actionRequestStatus.trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest status null.", "actionrequest.actionrequeststatus.empty",
                    actionRequest.getActionRequestId().toString()));
        } else if (ActionRequestStatus.fromCode(actionRequestStatus) == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest status invalid.",
                    "actionrequest.actionrequeststatus.invalid", actionRequest.getActionRequestId().toString()));
        }

        if (actionRequest.getResponsibilityId() == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest responsibility id null.",
                    "actionrequest.responsibilityid.empty", actionRequest.getActionRequestId().toString()));
        }

        Integer priority = actionRequest.getPriority();
        if (priority == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest priority null.", "actionrequest.priority.empty",
                    actionRequest.getActionRequestId().toString()));
        }

        // if(actionRequest.getRouteMethodName() == null || actionRequest.getRouteMethodName().trim().equals("")){
        // errors.add(new WorkflowServiceErrorImpl("ActionRequest route method name null.",
        // "actionrequest.routemethodname.empty", actionRequest.getActionRequestId().toString()));
        // }

        Integer routeLevel = actionRequest.getRouteLevel();
        if (routeLevel == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest route level null.", "actionrequest.routelevel.empty",
                    actionRequest.getActionRequestId().toString()));
        } else if (routeLevel < -1) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest route level invalid.",
                    "actionrequest.routelevel.invalid", actionRequest.getActionRequestId().toString()));
        }

        Integer version = actionRequest.getDocVersion();
        if (version == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest doc version null.", "actionrequest.docversion.empty",
                    actionRequest.getActionRequestId().toString()));
        }

        if (actionRequest.getCreateDate() == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionRequest create date null.", "actionrequest.createdate.empty",
                    actionRequest.getActionRequestId().toString()));
        }

        String recipientType = actionRequest.getRecipientTypeCd();
        if (recipientType != null && !recipientType.trim().equals("")) {
            if (recipientType.equals(KewApiConstants.WORKGROUP)) {
                String workgroupId = actionRequest.getGroupId();
                if (workgroupId == null) {
                    errors.add(new WorkflowServiceErrorImpl("ActionRequest workgroup null.",
                            "actionrequest.workgroup.empty", actionRequest.getActionRequestId().toString()));
                } else if (KimApiServiceLocator.getGroupService().getGroup(workgroupId) == null) {
                    errors.add(new WorkflowServiceErrorImpl("ActionRequest workgroup invalid.",
                            "actionrequest.workgroup.invalid", actionRequest.getActionRequestId().toString()));
                }

            }
            if (recipientType.equals(KewApiConstants.PERSON)) {
                String principalId = actionRequest.getPrincipalId();
                if (principalId == null || principalId.trim().equals("")) {
                    errors.add(new WorkflowServiceErrorImpl("ActionRequest person id null.", "actionrequest.persosn.empty",
                            actionRequest.getActionRequestId().toString()));
                } else {
                	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
                	if (principal == null) {
                		errors.add(new WorkflowServiceErrorImpl("ActionRequest person id invalid.",
                				"actionrequest.personid.invalid", actionRequest.getActionRequestId().toString()));
                	}
                }

                if (recipientType.equals(KewApiConstants.ROLE)
                        && (actionRequest.getRoleName() == null || actionRequest.getRoleName().trim().equals(""))) {
                    errors.add(new WorkflowServiceErrorImpl("ActionRequest role name null.", "actionrequest.rolename.null",
                            actionRequest.getActionRequestId().toString()));
                }
            }
            LOG.debug("Exit validateActionRequest(..) ");
            if (!errors.isEmpty()) {
                throw new WorkflowServiceErrorException("ActionRequest Validation Error", errors);
            }
        }
    }

    public List getDelegateRequests(ActionRequestValue actionRequest) {
        List<ActionRequestValue> delegateRequests = new ArrayList<ActionRequestValue>();
        List requests = getTopLevelRequests(actionRequest);
        for (Object request : requests)
        {
            ActionRequestValue parentActionRequest = (ActionRequestValue) request;
            delegateRequests.addAll(parentActionRequest.getChildrenRequests());
        }
        return delegateRequests;
    }

    public List getTopLevelRequests(ActionRequestValue actionRequest) {
        List<ActionRequestValue> topLevelRequests = new ArrayList<ActionRequestValue>();
        if (actionRequest.isRoleRequest()) {
            topLevelRequests.addAll(actionRequest.getChildrenRequests());
        } else {
            topLevelRequests.add(actionRequest);
        }
        return topLevelRequests;
    }

    public boolean isValidActionRequestCode(String actionRequestCode) {
        return actionRequestCode != null && KewApiConstants.ACTION_REQUEST_CODES.containsKey(actionRequestCode);
    }

    public boolean doesPrincipalHaveRequest(String principalId, String documentId) {
        if (getActionRequestDAO().doesDocumentHaveUserRequest(principalId, documentId)) {
            return true;
        }
        // TODO since we only store the workgroup id for workgroup requests, if the user is in a workgroup that has a request
        // than we need get all the requests with workgroup ids and see if our user is in that group
        List<String> groupIds = getActionRequestDAO().getRequestGroupIds(documentId);
        for (String groupId : groupIds) {
            if (KimApiServiceLocator.getGroupService().isMemberOfGroup(principalId, groupId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionRequestValue getActionRequestForRole(String actionTakenId) {
        return getActionRequestDAO().getRoleActionRequestByActionTakenId(actionTakenId);
    }

}
