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
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.exception.RouteManagerException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routemodule.RouteModule;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.ClassDumper;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * A node which generates {@link ActionRequestValue} objects from a
 * {@link RouteModule}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RequestsNode extends RequestActivationNode {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger( RequestsNode.class );

	protected static final String SUPPRESS_POLICY_ERRORS_KEY = "_suppressPolicyErrorsRequestActivationNode";

	public final SimpleResult process(RouteContext routeContext, RouteHelper routeHelper)
			throws Exception {
		try {
            if (processCustom(routeContext, routeHelper)) {
                return super.process(routeContext, routeHelper);
            }
            RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
            boolean isInitial = nodeInstance.isInitial();
            int currentIteration = 0;
            List<ActionRequestValue> requestsGenerated = new ArrayList<ActionRequestValue>();
            while (true) {
                detectRunawayProcess(routeContext, currentIteration++);
                if (isInitial) {
                    requestsGenerated = generateRequests(routeContext);
                    // need to set to false because we could iterate more than once here when the node is still in
                    // "initial" state
                    isInitial = false;
                }
                SimpleResult simpleResult = super.process(routeContext, routeHelper);
                if (simpleResult.isComplete()) {
                    RouteModule routeModule = getRouteModule(routeContext);
                    boolean moreRequestsAvailable = routeModule.isMoreRequestsAvailable(routeContext);
                    if (!moreRequestsAvailable) {
                        applyPoliciesOnExit(requestsGenerated, routeContext);
                        return simpleResult;
                    } else {
                        requestsGenerated = generateRequests(routeContext);
                    }
                } else {
                    return simpleResult;
                }
            }
		} catch ( RouteManagerException ex ) {
			// just re-throw - no need to wrap
			throw ex;
		} catch ( Exception e ) {
			LOG.error( "Caught exception routing", e );
			throw new RouteManagerException( e.getMessage(), e, routeContext );
		}
	}

    protected List<ActionRequestValue> generateRequests(RouteContext routeContext) throws Exception {
        DocumentRouteHeaderValue document = routeContext.getDocument();
		RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
		RouteNode node = nodeInstance.getRouteNode();
        if (LOG.isDebugEnabled()) {
            LOG.debug("RouteHeader info inside routing loop\n" + ClassDumper.dumpFields(document));
            LOG.debug("Looking for new actionRequests - routeLevel: " + node.getRouteNodeName());
        }
        boolean suppressPolicyErrors = isSuppressingPolicyErrors(routeContext);
        List<ActionRequestValue> requests = getNewActionRequests(routeContext);
        // determine if we have any approve requests for FinalApprover checks
        if (!suppressPolicyErrors) {
            verifyFinalApprovalRequest(document, requests, nodeInstance, routeContext);
        }
        return requests;
    }

    /**
     * Applies policies that should get checked prior to transitioning out of this node.  The default implementation of
     * this method checks the "mandatory" policy.
     *
     * @param requestsGenerated the requests generated on the current iteration of the route module
     * @param routeContext the current route context
     */
    protected void applyPoliciesOnExit(List<ActionRequestValue> requestsGenerated, RouteContext routeContext) {
        DocumentRouteHeaderValue document = routeContext.getDocument();
        RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
        RouteNode node = nodeInstance.getRouteNode();
        // for mandatory routes, requests must be generated
        if (node.isMandatory() && !isSuppressingPolicyErrors(routeContext) && CollectionUtils.isEmpty(requestsGenerated)) {
            List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findRootRequestsByDocIdAtRouteNode(document.getDocumentId(), nodeInstance.getRouteNodeInstanceId());
            if (actionRequests.isEmpty()) {
                LOG.warn("no requests generated for mandatory route - " + node.getRouteNodeName());
                throw new RouteManagerException(
                    "No requests generated for mandatory route " + node.getRouteNodeName() + ":" + node
                            .getRouteMethodName(), routeContext);
            }
        }
    }

    /** Used by subclasses to replace the functioning of the process method.
	 * 
	 * @return <b>true</b> if custom processing was performed and the base implementation
	 * in {@link #process(RouteContext, RouteHelper)} should be skipped.
	 */
	protected boolean processCustom(RouteContext routeContext, RouteHelper routeHelper) throws Exception {
		return false;
	}
	
	/**
	 * Verifies the state of the action requests when a final approval action is involved.
	 * 
	 * Throws a RouteManagerException if actions were not generated correctly.
	 */
	protected void verifyFinalApprovalRequest( DocumentRouteHeaderValue document, List<ActionRequestValue> requests, RouteNodeInstance nodeInstance, RouteContext routeContext ) throws RouteManagerException {
		boolean pastFinalApprover = isPastFinalApprover( document, nodeInstance );
		boolean hasApproveRequest = false;
		for ( ActionRequestValue actionRequest : requests ) {
			if ( actionRequest.isApproveOrCompleteRequest() ) {
				hasApproveRequest = true;
				break;
			}
		}
		// if final approver route level and no approve request send to
		// exception routing
		if ( nodeInstance.getRouteNode().getFinalApprovalInd().booleanValue() ) {
			// we must have an approve request generated if final
			// approver level.
			if ( !hasApproveRequest ) {
				throw new RouteManagerException(
						"No Approve Request generated after final approver", routeContext );
			}
		} else if ( pastFinalApprover ) {
			// we can't allow generation of approve requests after final
			// approver. This guys going to exception routing.
			if ( hasApproveRequest ) {
				throw new RouteManagerException(
						"Approve Request generated after final approver", routeContext );
			}
		}
	}

	public List<ActionRequestValue> getNewActionRequests(RouteContext context) throws Exception {
		RouteNodeInstance nodeInstance = context.getNodeInstance();
		String routeMethodName = nodeInstance.getRouteNode().getRouteMethodName();
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Looking for action requests in " + routeMethodName + " : "
					+ nodeInstance.getRouteNode().getRouteNodeName() );
		}
		List<ActionRequestValue> newRequests = new ArrayList<ActionRequestValue>();
		try {
			RouteModule routeModule = getRouteModule( context );
			List<ActionRequestValue> requests = routeModule.findActionRequests( context );
			for ( ActionRequestValue actionRequest : requests ) {
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "Request generated by RouteModule '" + routeModule + "' for node "
							+ nodeInstance + ":" + actionRequest );
				}
				actionRequest = KEWServiceLocator.getActionRequestService()
						.initializeActionRequestGraph( actionRequest, context.getDocument(),
								nodeInstance );
				saveActionRequest( context, actionRequest );
				newRequests.add( actionRequest );
			}
		} catch ( WorkflowException ex ) {
			LOG.warn( "Caught WorkflowException during routing", ex );
			throw new RouteManagerException( ex, context );
		}
		return newRequests;
	}

	/**
	 * Returns the RouteModule which should handle generating requests for this
	 * RequestsNode.
	 */
	protected RouteModule getRouteModule(RouteContext context) throws Exception {
		return KEWServiceLocator.getRouteModuleService().findRouteModule(
				context.getNodeInstance().getRouteNode() );
	}

	/**
	 * Checks if the document has past the final approver node by walking
	 * backward through the previous node instances. Ignores any previous nodes
	 * that have been "revoked".
	 */
	protected boolean isPastFinalApprover(DocumentRouteHeaderValue document,
			RouteNodeInstance nodeInstance) {
		FinalApproverContext context = new FinalApproverContext();
		List revokedNodeInstances = KEWServiceLocator.getRouteNodeService()
				.getRevokedNodeInstances( document );
		Set revokedNodeInstanceIds = new HashSet();
		for ( Iterator iterator = revokedNodeInstances.iterator(); iterator.hasNext(); ) {
			RouteNodeInstance revokedNodeInstance = (RouteNodeInstance)iterator.next();
			revokedNodeInstanceIds.add( revokedNodeInstance.getRouteNodeInstanceId() );
		}
		isPastFinalApprover( nodeInstance.getPreviousNodeInstances(), context,
				revokedNodeInstanceIds );
		return context.isPast;
	}

	protected void isPastFinalApprover(List previousNodeInstances, FinalApproverContext context,
			Set revokedNodeInstanceIds) {
		if ( previousNodeInstances != null && !previousNodeInstances.isEmpty() ) {
			for ( Iterator iterator = previousNodeInstances.iterator(); iterator.hasNext(); ) {
				if ( context.isPast ) {
					return;
				}
				RouteNodeInstance nodeInstance = (RouteNodeInstance)iterator.next();
				if ( context.inspected.contains( getKey( nodeInstance ) ) ) {
					continue;
				} else {
					context.inspected.add( getKey( nodeInstance ) );
				}
				if ( Boolean.TRUE.equals( nodeInstance.getRouteNode().getFinalApprovalInd() ) ) {
					// if the node instance has been revoked (by a Return To
					// Previous action for example)
					// then we don't want to consider that node when we
					// determine if we are past final
					// approval or not
					if ( !revokedNodeInstanceIds.contains( nodeInstance.getRouteNodeInstanceId() ) ) {
						context.isPast = true;
					}
					return;
				}
				isPastFinalApprover( nodeInstance.getPreviousNodeInstances(), context,
						revokedNodeInstanceIds );
			}
		}
	}

	/**
	 * The method will get a key value which can be used for comparison
	 * purposes. If the node instance has a primary key value, it will be
	 * returned. However, if the node instance has not been saved to the
	 * database (i.e. during a simulation) this method will return the node
	 * instance passed in.
	 */
	protected Object getKey(RouteNodeInstance nodeInstance) {
		String id = nodeInstance.getRouteNodeInstanceId();
		return (id != null ? (Object)id : (Object)nodeInstance);
	}

    protected void detectRunawayProcess(RouteContext routeContext, int currentIteration) throws NumberFormatException {
	    String maxNodesConstant = getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.MAX_NODES_BEFORE_RUNAWAY_PROCESS);
	    int maxNodes = (org.apache.commons.lang.StringUtils.isEmpty(maxNodesConstant)) ? 50 : Integer.valueOf(maxNodesConstant);
	    if (currentIteration > maxNodes) {
            throw new RouteManagerException("Detected a runaway process within RequestsNode for document with id '" + routeContext.getDocument().getDocumentId() + "' after " + currentIteration + " iterations.");
        }
	}

	protected class FinalApproverContext {
		public Set inspected = new HashSet();

		public boolean isPast = false;
	}

	public static boolean isSuppressingPolicyErrors(RouteContext routeContext) {
		Boolean suppressPolicyErrors = (Boolean)routeContext.getParameters().get(
				SUPPRESS_POLICY_ERRORS_KEY );
		if ( suppressPolicyErrors == null || !suppressPolicyErrors ) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static void setSuppressPolicyErrors(RouteContext routeContext) {
		routeContext.getParameters().put( SUPPRESS_POLICY_ERRORS_KEY, Boolean.TRUE );
	}

    protected ParameterService getParameterService() {
		return CoreFrameworkServiceLocator.getParameterService();
	}
}
