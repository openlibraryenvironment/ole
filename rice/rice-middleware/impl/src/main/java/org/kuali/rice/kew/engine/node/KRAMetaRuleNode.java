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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.rule.Rule;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.exception.RouteManagerException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.FlexRM;
import org.kuali.rice.kew.rule.KRAMetaRuleEngine;
import org.kuali.rice.kew.rule.RuleExpressionResult;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;


/**
 * Node that implements a KRAMetaRule, with multiple request/response phases
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRAMetaRuleNode extends IteratedRequestActivationNode {
	private static String SUPPRESS_POLICY_ERRORS_KEY = "_suppressPolicyErrorsRequestActivationNode";

	protected List<ActionRequestValue> generateUninitializedRequests(ActionRequestFactory arFactory, RouteContext context, RuleExpressionResult result) throws WorkflowException {
		FlexRM flexRM = new FlexRM();
		flexRM.makeActionRequests(arFactory, result.getResponsibilities(), context, Rule.Builder.create(result.getRule().getDefinition()).build(), context.getDocument(), null, null);
		return new ArrayList<ActionRequestValue>(arFactory.getRequestGraphs());
	}

	protected List<ActionRequestValue> initializeRequests(List<ActionRequestValue> requests, RouteContext context) {
		// RequestNode.getNewActionRequests
		List<ActionRequestValue> newRequests = new ArrayList<ActionRequestValue>();
		for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
			actionRequest = KEWServiceLocator.getActionRequestService().initializeActionRequestGraph(actionRequest, context.getDocument(), context.getNodeInstance());
			saveActionRequest(context, actionRequest);
			newRequests.add(actionRequest);
		}
		return newRequests;
	}

	@Override
	protected boolean generateNewRequests(boolean initial, RouteContext context, RouteHelper routeHelper)
	throws WorkflowException, Exception {
		RouteNodeInstance nodeInstance = context.getNodeInstance();
		RouteNode nodeDef = nodeInstance.getRouteNode();

		// get the expression
		Map<String, String> cfg = Utilities.getKeyValueCollectionAsMap(nodeDef.getConfigParams());
		String expression = cfg.get("expression");
		if (StringUtils.isEmpty(expression)) {
			throw new WorkflowException("No meta-rule expression supplied in node " + nodeDef);
		}
		KRAMetaRuleEngine engine = new KRAMetaRuleEngine(expression);

		// get the current statement
		int curStatement = getCurStatement(nodeInstance);

		engine.setCurStatement(curStatement);

		if (engine.isDone()) {
			return false;
		}

		// generate next round of action requests
		RuleExpressionResult result = engine.processSingleStatement(context);
		if (!result.isSuccess()) {
			return false;
		}

		boolean suppressPolicyErrors = isSuppressingPolicyErrors(context);
		boolean pastFinalApprover = isPastFinalApprover(context.getDocument(), nodeInstance);

		// actionRequests.addAll(makeActionRequests(context, rule, routeHeader, null, null));
		// copied from parts of FlexRM and RequestsNode
		ActionRequestFactory arFactory = new ActionRequestFactory(context.getDocument(), context.getNodeInstance());
		List<ActionRequestValue> requests = generateUninitializedRequests(arFactory, context, result);
		requests = initializeRequests(requests, context);
		// RequestNode
		if ((requests.isEmpty()) && nodeDef.getMandatoryRouteInd().booleanValue() && ! suppressPolicyErrors) {
			LOG.warn("no requests generated for mandatory route - " + nodeDef.getRouteNodeName());
			throw new RouteManagerException("No requests generated for mandatory route " + nodeDef.getRouteNodeName() + ":" + nodeDef.getRouteMethodName(), context);
		}
		// determine if we have any approve requests for FinalApprover checks
		boolean hasApproveRequest = false;
		for (Iterator iter = requests.iterator(); iter.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
			hasApproveRequest = actionRequest.isApproveOrCompleteRequest() || hasApproveRequest;
		}
		// if final approver route level and no approve request send to exception routing
		if (nodeDef.getFinalApprovalInd().booleanValue()) {
			// we must have an approve request generated if final approver level.
			if (!hasApproveRequest && ! suppressPolicyErrors) {
				throw new RuntimeException("No Approve Request generated after final approver");
			}
		} else if (pastFinalApprover) {
			// we can't allow generation of approve requests after final approver. This guys going to exception routing.
			if (hasApproveRequest && ! suppressPolicyErrors) {
				throw new RuntimeException("Approve Request generated after final approver");
			}
		}

		// increment to next statement
		nodeInstance.getNodeState("stmt").setValue(String.valueOf(curStatement + 1));
		return !requests.isEmpty();
	}

	/**
	 * @param nodeInstance the current node instance under execution
	 * @return the meta-rule statement this node instance should process
	 */
	protected static int getCurStatement(RouteNodeInstance nodeInstance) {
		int statement = 0;
		NodeState nodeState = nodeInstance.getNodeState("stmt");
		if (nodeState == null) {
			nodeState = new NodeState();
			nodeState.setKey("stmt");
			nodeState.setNodeInstance(nodeInstance);
			nodeInstance.addNodeState(nodeState);
		}
		if (StringUtils.isEmpty(nodeState.getValue())) {
			nodeState.setValue("0");
		} else {
			statement = Integer.parseInt(nodeState.getValue());
		}
		return statement;
	}

	@Override
	protected RequestFulfillmentCriteria getRequestFulfillmentCriteria(RouteContext routeContext) {
		return super.getRequestFulfillmentCriteria(routeContext);
	}


	// -- copied from request node; a lot of this action request evaluating code should probably go into helper classes or factored into a common subclass

	/**
	 * The method will get a key value which can be used for comparison purposes. If the node instance has a primary key value, it will be returned. However, if the node instance has not been saved to the database (i.e. during a simulation) this method will return the node instance passed in.
	 */
	private Object getKey(RouteNodeInstance nodeInstance) {
		String id = nodeInstance.getRouteNodeInstanceId();
		return (id != null ? (Object) id : (Object) nodeInstance);
	}

	/**
	 * Checks if the document has past the final approver node by walking backward through the previous node instances.
	 * Ignores any previous nodes that have been "revoked".
	 */
	private boolean isPastFinalApprover(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) {
		FinalApproverContext context = new FinalApproverContext();
		List revokedNodeInstances = KEWServiceLocator.getRouteNodeService().getRevokedNodeInstances(document);
		Set revokedNodeInstanceIds = new HashSet();
		for (Iterator iterator = revokedNodeInstances.iterator(); iterator.hasNext(); ) {
			RouteNodeInstance revokedNodeInstance = (RouteNodeInstance) iterator.next();
			revokedNodeInstanceIds.add(revokedNodeInstance.getRouteNodeInstanceId());
		}
		isPastFinalApprover(nodeInstance.getPreviousNodeInstances(), context, revokedNodeInstanceIds);
		return context.isPast;
	}

	private void isPastFinalApprover(List previousNodeInstances, FinalApproverContext context, Set revokedNodeInstanceIds) {
		if (previousNodeInstances != null && !previousNodeInstances.isEmpty()) {
			for (Iterator iterator = previousNodeInstances.iterator(); iterator.hasNext();) {
				if (context.isPast) {
					return;
				}
				RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
				if (context.inspected.contains(getKey(nodeInstance))) {
					continue;
				} else {
					context.inspected.add(getKey(nodeInstance));
				}
				if (Boolean.TRUE.equals(nodeInstance.getRouteNode().getFinalApprovalInd())) {
					// if the node instance has been revoked (by a Return To Previous action for example)
					// then we don't want to consider that node when we determine if we are past final
					// approval or not
					if (!revokedNodeInstanceIds.contains(nodeInstance.getRouteNodeInstanceId())) {
						context.isPast = true;
					}
					return;
				}
				isPastFinalApprover(nodeInstance.getPreviousNodeInstances(), context, revokedNodeInstanceIds);
			}
		}
	}
	public static boolean isSuppressingPolicyErrors(RouteContext routeContext) {
		Boolean suppressPolicyErrors = (Boolean)routeContext.getParameters().get(SUPPRESS_POLICY_ERRORS_KEY);
		if (suppressPolicyErrors == null || ! suppressPolicyErrors) {
			return false;
		}
		return true;
	}

	private class FinalApproverContext {
		public Set inspected = new HashSet();
		public boolean isPast = false;
	}

}
