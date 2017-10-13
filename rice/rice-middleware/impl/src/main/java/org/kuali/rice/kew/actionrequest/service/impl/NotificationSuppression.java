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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.service.KEWServiceLocator;

/**
 * This utility class encapsulates functions used to provide notification suppression
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NotificationSuppression {

    public static final String SUPPRESS_NOTIFY_KEY_START = "SuppressNotify";
    
	/**
	 * add metadata (a NodeState) to the route node so that if this action request is regenerated 
	 * verbatim,  the notification email will suppressed (since it is a duplicate!).
	 * @param nodeInstance where additional NodeState will be added
	 * @param actionRequestValue 
	 */
    public void addNotificationSuppression(
    		RouteNodeInstance nodeInstance, ActionRequestValue actionRequestValue) {

    	// iterative depth first traversal of the action request tree
    	LinkedList<ActionRequestValue> stack = new LinkedList<ActionRequestValue>();
    	// push
    	stack.add(actionRequestValue);

    	while (stack.size() > 0) {
    		// pop our next action request 
    		ActionRequestValue childActionRequest = stack.removeLast(); 

    		// process this action request only if it is a leaf
    		if (childActionRequest.getChildrenRequests() == null || 
    				childActionRequest.getChildrenRequests().size() == 0) {
    			List<String> requestKeys = getSuppressNotifyNodeStateKeys(childActionRequest);
    			if (requestKeys != null) for (String requestKey : requestKeys) { 
    				if (nodeInstance.getNodeState(requestKey) == null) { // only add once
    					NodeState ns = new NodeState();
    					ns.setKey(requestKey);
    					ns.setValue("notification suppression");
    					nodeInstance.addNodeState(ns);
    				}
    			}
    		}

    		// put child action requests on the stack
    		if (childActionRequest.getChildrenRequests() != null) {
    			// equivalent to 'push' all
    			stack.addAll(childActionRequest.getChildrenRequests());
    		}
    	}
    }
	
	/**
	 * This method filters any ActionItems whose related ActionRequestValueS have been flagged for notification
	 * suppression.
	 * 
	 * @param actionItems the ActionItemS to filter
	 * @param routeNodeInstance the RouteNodeInstance that the actionItems are associated with
	 */
	protected void filterNotificationSuppressedActionItems(List<ActionItem> actionItems, 
			final RouteNodeInstance routeNodeInstance) {
		
		// remove all actionItems from the collection whose request has a suppress notification node state element
		CollectionUtils.filter(actionItems, new Predicate() {
			public boolean evaluate(Object object) {
				boolean result = true;
				ActionItem actionItem = (ActionItem)object;
				ActionRequestValue actionRequest = 
					KEWServiceLocator.getActionRequestService().findByActionRequestId(actionItem.getActionRequestId());
				
				List<String> suppressNotificationKeys = getSuppressNotifyNodeStateKeys(actionRequest);
				if (suppressNotificationKeys != null && suppressNotificationKeys.size() > 0) {
					// if any of the keys are not present, we need to notify
					boolean containsAll = true;
					for (String key : suppressNotificationKeys) {
						if (routeNodeInstance.getNodeState(key) == null) {
							containsAll = false;
							break;
						}
					}
					// actionItem will be filtered if this Predicate returns false
					result = !containsAll; // only filters if all keys are present
				}
				return result;
			}
		});
	}
	
	/**
	 * 
	 * <p>This method takes care of notification for ActionItemS.  It has logic for suppressing notifications 
     * when the RouteNodeInstance has NodeState specifically hinting for notification suppression for a given 
     * ActionItem.
	 * 
	 * <p>A side effect is that any notification suppression NodeStateS will be removed
	 * from the RouteNodeInstance after notifications are sent.
	 * 
	 * @param actionItems a list of ActionItemS related to the given routeNodeInstance
	 * @param routeNodeInstance the RouteNodeInstance related to the given actionItems
	 */
	public void notify(List<ActionItem> actionItems, RouteNodeInstance routeNodeInstance) {
		
		if (actionItems != null && actionItems.size() > 0) {
			actionItems = new ArrayList<ActionItem>(actionItems); // defensive copy since we will filter
			filterNotificationSuppressedActionItems(actionItems, routeNodeInstance);
			// notify for any actionItems that were not filtered
			if (actionItems.size() > 0) { 
			    KEWServiceLocator.getNotificationService().notify(ActionItem.to(actionItems)); 
			}
			deleteNotificationSuppression(routeNodeInstance);
		}
	}

	/**
	 * This method removes all NodeStates related to notification suppression, saving the RouteNodeInstance if there
	 * were any removed.
	 * 
	 * @param routeNodeInstance
	 */
	@SuppressWarnings("unchecked")
	private void deleteNotificationSuppression(
			final RouteNodeInstance routeNodeInstance) {
		// remove all suppress notification node states
		List<NodeState> nodeStates = routeNodeInstance.getState();
		if (nodeStates != null && nodeStates.size() > 0) {
			List<String> nodeStateKeysToRemove = new ArrayList<String>(nodeStates.size());

			for (NodeState nodeState : nodeStates) {
				if (nodeState.getKey().startsWith(NotificationSuppression.SUPPRESS_NOTIFY_KEY_START)) {
					nodeStateKeysToRemove.add(nodeState.getKey());
				}
			}
			if (nodeStateKeysToRemove.size() > 0) {
				for (String nodeStateKeyToRemove : nodeStateKeysToRemove) {
					routeNodeInstance.removeNodeState(nodeStateKeyToRemove);
				}
				KEWServiceLocator.getRouteNodeService().save(routeNodeInstance);
			}
		}
	}

	
    /**
     * Builds keys for action requests used for notification suppression.
     * <p>NOTE: This method needs to stay in sync with {@link #getSuppressNotifyNodeStateKeys(org.kuali.rice.kew.dto.ActionRequestDTO)}
     * Any changes here must be made there as well!
     * @param a
     * @return List
     */
	protected List<String> getSuppressNotifyNodeStateKeys(ActionRequest a) {
		List<String> results = Collections.emptyList(); 
		if (a != null) {
			results = new ArrayList<String>(3);
			addSuppressNotifyNodeStateKey(results, RecipientType.PRINCIPAL.getCode(), a.getPrincipalId());
			addSuppressNotifyNodeStateKey(results, RecipientType.GROUP.getCode(), a.getGroupId());
			addSuppressNotifyNodeStateKey(results, RecipientType.ROLE.getCode(), a.getQualifiedRoleName());
		}
		return results;
    }

    /**
     * Builds keys for action requests used for notification suppression.
     * <p>NOTE: This method needs to stay in sync with {@link #getSuppressNotifyNodeStateKeys(org.kuali.rice.kew.actionrequest.ActionRequestValue)}
     * Any changes here must be made there as well!
     * @param a
     * @return List
     */
	protected List<String> getSuppressNotifyNodeStateKeys(ActionRequestValue a) {
		List<String> results = Collections.emptyList(); 
		if (a != null) {
			results = new ArrayList<String>(3);
			addSuppressNotifyNodeStateKey(results, RecipientType.PRINCIPAL.getCode(), a.getPrincipalId());
			addSuppressNotifyNodeStateKey(results, RecipientType.GROUP.getCode(), a.getGroupId());
			addSuppressNotifyNodeStateKey(results, RecipientType.ROLE.getCode(), a.getQualifiedRoleName());
		}
		return results;
	}

	
	/**
	 * This method adds a suppress notify key to the passed in list
	 * 
	 * @param results the list that the key will be added to
	 * @param responsiblePartyType
	 * @param responsiblePartyId
	 */
	private void addSuppressNotifyNodeStateKey(List<String> results, String responsiblePartyType,
			String responsiblePartyId) {
		if (responsiblePartyId != null && responsiblePartyType != null) {
			StringBuilder sb = new StringBuilder(SUPPRESS_NOTIFY_KEY_START);
			sb.append("(");
			sb.append(responsiblePartyType);
			sb.append(",");
			sb.append(responsiblePartyId);
			sb.append(")");
			results.add(sb.toString());
		}
	}
	
}
