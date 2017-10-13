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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.exception.RouteManagerException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.role.RoleRouteModule;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routemodule.RouteModule;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.ClassDumper;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

/**
 * A node implementation which provides integration with KIM Roles for routing.
 * Essentially extends RequestsNode and provides a custom RouteModule
 * implementation.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class RoleNode extends RequestsNode {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger( RoleNode.class );

	@Override
	protected RouteModule getRouteModule(RouteContext context) throws Exception {
		return new RoleRouteModule();
	}
	
	/**
	 * @see org.kuali.rice.kew.engine.node.RequestsNode#processCustom(org.kuali.rice.kew.engine.RouteContext, org.kuali.rice.kew.engine.RouteHelper)
	 */
	@Override
	protected boolean processCustom(RouteContext routeContext, RouteHelper routeHelper) throws Exception {
		DocumentRouteHeaderValue document = routeContext.getDocument();
		RouteNodeInstance nodeInstance = routeContext.getNodeInstance();
		RouteNode node = nodeInstance.getRouteNode();
		// while no routable actions are activated and there are more
		// routeLevels to process
		if ( nodeInstance.isInitial() ) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug( "RouteHeader info inside routing loop\n"
						+ ClassDumper.dumpFields( routeContext.getDocument() ) );
				LOG.debug( "Looking for new actionRequests - routeLevel: "
						+ node.getRouteNodeName() );
			}
			boolean suppressPolicyErrors = isSuppressingPolicyErrors(routeContext);
			List<ActionRequestValue> requests = getNewActionRequests( routeContext );
// Debugging code to force an empty action request				
//				if ( document.getDocumentType().getName().equals( "SACC" ) ) {
//					LOG.fatal( "DEBUGGING CODE IN PLACE - SACC DOCUMENT ACTION REQUESTS CLEARED" );
//					requests.clear();
//				}
			// for mandatory routes, requests must be generated
			if ( requests.isEmpty() && !suppressPolicyErrors) {
				Responsibility resp = getFirstResponsibilityWithMandatoryRouteFlag( document, node );
				if ( resp != null ) {
					throw new RouteManagerException( "No requests generated for KIM Responsibility-based mandatory route.\n" +
							"Document Id:    " + document.getDocumentId() + "\n" +
							"DocumentType:   " + document.getDocumentType().getName() + "\n" +
							"Route Node:     " + node.getRouteNodeName() + "\n" + 
							"Responsibility: " + resp,
							routeContext );
				}
			}
			// determine if we have any approve requests for FinalApprover
			// checks
			if ( !suppressPolicyErrors ) {				
				verifyFinalApprovalRequest( document, requests, nodeInstance, routeContext );
			}
		}
		return true; // to indicate custom processing performed
	}
	
	/**
	 * Checks for any mandatory route responsibilities for the given document type and node.
	 * 
	 * Stops once it finds a responsibility for the document and node.
	 */	
	protected Responsibility getFirstResponsibilityWithMandatoryRouteFlag( DocumentRouteHeaderValue document, RouteNode node ) {
		// iterate over the document hierarchy
		// gather responsibilities - merge based on route level
        Predicate p = and(
                equal("template.namespaceCode", KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE),
                equal("template.name", KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME),
                equal("active", "Y"),
                equal("attributes[routeNodeName]", node.getRouteNodeName())
                // KULRICE-8538 -- Check the document type while we're looping through the results below.  If it is added
                // into the predicate, no rows are ever returned.
                // equal("attributes[documentTypeName]", docType.getName())
        );
        QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
        builder.setPredicates(p);
        List<Responsibility> responsibilities = KimApiServiceLocator.getResponsibilityService().findResponsibilities(builder.build()).getResults();


        DocumentType docType = document.getDocumentType();
        while ( docType != null ) {
            // once we find a responsibility, stop, since this overrides any parent
            // responsibilities for this node
            if ( !responsibilities.isEmpty() ) {
                // if any has required=true - return true
                for ( Responsibility resp : responsibilities ) {
                    String documentTypeName = resp.getAttributes().get( KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME);
                    if (StringUtils.isNotEmpty(documentTypeName) && StringUtils.equals(documentTypeName, docType.getName())){
                        if ( Boolean.parseBoolean( resp.getAttributes().get( KimConstants.AttributeConstants.REQUIRED ) ) ) {
                            return resp;
                        }
                    }
                }
            }
			docType = docType.getParentDocType();
		}
		return null;
	}

	/**
	 * Activates the action requests that are pending at this routelevel of the
	 * document. The requests are processed by priority and then request ID. It
	 * is implicit in the access that the requests are activated according to
	 * the route level above all.
	 * <p>
	 * FYI and acknowledgment requests do not cause the processing to stop. Only
	 * action requests for approval or completion cause the processing to stop
	 * and then only for route level with a serialized activation policy. Only
	 * requests at the current document's current route level are activated.
	 * Inactive requests at a lower level cause a routing exception.
	 * <p>
	 * Exception routing and adhoc routing are processed slightly differently.
	 * 
	 * @return True if the any approval actions were activated.
	 * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
	 * @throws WorkflowException
	 */
	@SuppressWarnings("unchecked")
	public boolean activateRequests(RouteContext context, DocumentRouteHeaderValue document,
			RouteNodeInstance nodeInstance) throws WorkflowException {
		MDC.put( "docId", document.getDocumentId() );
		PerformanceLogger performanceLogger = new PerformanceLogger( document.getDocumentId() );
		List<ActionItem> generatedActionItems = new ArrayList<ActionItem>();
		List<ActionRequestValue> requests = new ArrayList<ActionRequestValue>();
		if ( context.isSimulation() ) {
			for ( ActionRequestValue ar : context.getDocument().getActionRequests() ) {
				// logic check below duplicates behavior of the
				// ActionRequestService.findPendingRootRequestsByDocIdAtRouteNode(documentId,
				// routeNodeInstanceId) method
				if ( ar.getCurrentIndicator()
						&& (ActionRequestStatus.INITIALIZED.getCode().equals( ar.getStatus() ) || ActionRequestStatus.ACTIVATED.getCode()
								.equals( ar.getStatus() ))
						&& ar.getNodeInstance().getRouteNodeInstanceId().equals(
								nodeInstance.getRouteNodeInstanceId() )
						&& ar.getParentActionRequest() == null ) {
					requests.add( ar );
				}
			}
			requests.addAll( context.getEngineState().getGeneratedRequests() );
		} else {
			requests = KEWServiceLocator.getActionRequestService()
					.findPendingRootRequestsByDocIdAtRouteNode( document.getDocumentId(),
							nodeInstance.getRouteNodeInstanceId() );
		}
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Pending Root Requests " + requests.size() );
		}
		boolean requestActivated = activateRequestsCustom( context, requests, generatedActionItems,
				document, nodeInstance );
		// now let's send notifications, since this code needs to be able to
		// activate each request individually, we need
		// to collection all action items and then notify after all have been
		// generated
        notify(context, generatedActionItems, nodeInstance);

        performanceLogger.log( "Time to activate requests." );
		return requestActivated;
	}
	
    protected static class RoleRequestSorter implements Comparator<ActionRequestValue> {
        public int compare(ActionRequestValue ar1, ActionRequestValue ar2) {
        	int result = 0;
        	// compare descriptions (only if both not null)
        	if ( ar1.getResponsibilityDesc() != null && ar2.getResponsibilityDesc() != null ) {
        		result = ar1.getResponsibilityDesc().compareTo( ar2.getResponsibilityDesc() );
        	}
            if ( result != 0 ) return result;
        	// compare priority
            result = ar1.getPriority().compareTo(ar2.getPriority());
            if ( result != 0 ) return result;
            // compare action request type
            result = ActionRequestValue.compareActionCode(ar1.getActionRequested(), ar2.getActionRequested(), true);
            if ( result != 0 ) return result;
            // compare action request ID
            if ( (ar1.getActionRequestId() != null) && (ar2.getActionRequestId() != null) ) {
                result = ar1.getActionRequestId().compareTo(ar2.getActionRequestId());
            } else {
                // if even one action request id is null at this point return then the two are equal
                result = 0;
            }
            return result;
        }
    }
    protected static final Comparator<ActionRequestValue> ROLE_REQUEST_SORTER = new RoleRequestSorter();

	
	protected boolean activateRequestsCustom(RouteContext context,
			List<ActionRequestValue> requests, List<ActionItem> generatedActionItems,
			DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance)
			throws WorkflowException {
		Collections.sort( requests, ROLE_REQUEST_SORTER );
		String activationType = nodeInstance.getRouteNode().getActivationType();
		boolean isParallel = KewApiConstants.ROUTE_LEVEL_PARALLEL.equals( activationType );
		boolean requestActivated = false;
		String groupToActivate = null;
		Integer priorityToActivate = null;
		for ( ActionRequestValue request : requests ) {
			// if a request has already been activated and we are not parallel routing
			// or in the simulator, break out of the loop and exit
			if ( requestActivated
					&& !isParallel
					&& (!context.isSimulation() || !context.getActivationContext()
							.isActivateRequests()) ) {
				break;
			}
			if ( request.getParentActionRequest() != null || request.getNodeInstance() == null ) {
				// 1. disregard request if it's not a top-level request
				// 2. disregard request if it's a "future" request and hasn't
				// been attached to a node instance yet
				continue;
			}
			if ( request.isApproveOrCompleteRequest() ) {
				boolean thisRequestActivated = false;
				// capture the priority and grouping information for this request
				// We only need this for Approval requests since FYI and ACK requests are non-blocking
				if ( priorityToActivate == null ) {
				 	priorityToActivate = request.getPriority();
				}
				if ( groupToActivate == null ) {
					groupToActivate = request.getResponsibilityDesc();
				}
				// check that the given request is found in the current group to activate
				// check priority and grouping from the request (stored in the responsibility description)
				if ( StringUtils.equals( groupToActivate, request.getResponsibilityDesc() )
						&& (
								(priorityToActivate != null && request.getPriority() != null && priorityToActivate.equals(request.getPriority()))
							||  (priorityToActivate == null && request.getPriority() == null)
							)
						) {
					// if the request is already active, note that we have an active request
					// and move on to the next request
					if ( request.isActive() ) {
						requestActivated = true;
						continue;
					}
					logProcessingMessage( request );
					if ( LOG.isDebugEnabled() ) {
						LOG.debug( "Activating request: " + request );
					}
					// this returns true if any requests were activated as a result of this call
					thisRequestActivated = activateRequest( context, request, nodeInstance,
							generatedActionItems );
					requestActivated |= thisRequestActivated;
				}
				// if this request was not activated and no request has been activated thus far
				// then clear out the grouping and priority filters
				// as this represents a case where the person with the earlier priority
				// did not need to approve for this route level due to taking
				// a prior action
				if ( !thisRequestActivated && !requestActivated ) {
					priorityToActivate = null;
					groupToActivate = null;
				}
			} else {
				logProcessingMessage( request );
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "Activating request: " + request );
				}
				requestActivated = activateRequest( context, request, nodeInstance,
						generatedActionItems )
						|| requestActivated;
			}
		}
		return requestActivated;
	}
}
