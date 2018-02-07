/**
 * Copyright 2005-2013 The Kuali Foundation
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actions.AcknowledgeAction;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * Unit tests for RouteLogAction -- very incomplete, only tests populateRouteLogFutureRequests
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RouteLogActionTest extends KEWTestCase
{
    RouteLogAction routeLogAction = new RouteLogAction();

    protected void loadTestData() throws Exception 
    {
        loadXmlFile(AcknowledgeAction.class, "ActionsConfig.xml");
    }
    
	/**
     * Test that existing action requests don't show up in future list (KULRICE-2641)
     */
    @SuppressWarnings("unchecked")
	@Test public void testPopulateRouteLogFutureRequests_HasNoExistingRequests() throws Exception {
    	
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), getClass().getSimpleName());
    	document.route("1 - user1 route");
    	verifyFutureRequestState(document);

    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	document.approve("2 - ewestfal approve");
    	verifyFutureRequestState(document);

    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
    	document.approve("3 - user2 approve");
    	verifyFutureRequestState(document);

    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), document.getDocumentId());
    	document.acknowledge("4 - user3 acknowledge");
    	verifyFutureRequestState(document);
    }

	/**
	 * This method runs RouteLogAction.populateRouteLogFutureRequests then checks that the future actions 
	 * aren't "actions taken" or "actions pending".  It also checks that the total number of action requests
	 * add up (taken + pending + future == total)
	 * 
	 * @param document
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private void verifyFutureRequestState(WorkflowDocument document)
			throws WorkflowException, Exception {
		Collection<ActionTakenValue> actionsTaken = KEWServiceLocator.getActionTakenService().findByDocumentId(document.getDocumentId());
    	Collection<ActionRequestValue> actionsPending = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
    	
    	DocumentRouteHeaderValue docRouteHeaderValue = DocumentRouteHeaderValue.from(document.getDocument());
    	RouteLogForm routeLogForm = new RouteLogForm();
    	routeLogAction.populateRouteLogFutureRequests(routeLogForm, docRouteHeaderValue);
        List<ActionRequestValue> futureRootRequests = routeLogForm.getFutureRootRequests();
        
        int takenRequestCount = 0;
        // check that the actions taken aren't in the future list
        for (ActionTakenValue actionTaken : actionsTaken) {
        	if (actionTaken.getActionRequests() != null) {
        		for (ActionRequestValue actionRequestTaken : actionTaken.getActionRequests()) {
        			++takenRequestCount;
        			for (ActionRequestValue futureRequest : futureRootRequests) {
        				assertFalse("action taken is in futureRootRequests",
        						futureRequest.getActionRequestId().equals(actionRequestTaken.getActionRequestId()));
        			}
        		}
        	}
        }

        int pendingRequestsCount = 0;
        // check that the pending requests aren't in the future list
        for (ActionRequestValue pendingAction : actionsPending) {
        	++pendingRequestsCount;
        	for (ActionRequestValue futureRequest : futureRootRequests) {
        		assertFalse("action taken is in futureRootRequests",
        				futureRequest.getActionRequestId().equals(pendingAction.getActionRequestId()));
        	}
        }
        
        // there are 3 route nodes for this document, not counting AdHoc
        assertTrue("taken + pending + future == 3", takenRequestCount + pendingRequestsCount + futureRootRequests.size() == 3);
        
        recursiveValidate(routeLogForm.getFutureRootRequests());
	}

	/**
     * Test that a document that will have no future action requests is handled without exception (KULRICE-2838)
     */
    @Test public void testPopulateRouteLogFutureRequests_ZeroFutureRequests() throws Exception {
    	String user1PrincipalId = getPrincipalIdForName("ewestfal");

    	RouteLogForm routeLogForm = new RouteLogForm();

    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "RouteLogActionTestTrivial");
    	// for this simple doc type, no future requests
    	document.route("");
    	
    	DocumentRouteHeaderValue docRouteHeaderValue = DocumentRouteHeaderValue.from(document.getDocument());

    	try {
    		routeLogAction.populateRouteLogFutureRequests(routeLogForm, docRouteHeaderValue);
    	} catch (Exception e) {
    		fail("calculating future requests where there will be none should not be a problem");
    	}
        
        assertTrue("We're expecting 0 future action requests",routeLogForm.getFutureActionRequestCount()==0);
        assertTrue("We're expecting 0 future action requests",
        		routeLogForm.getFutureRootRequests() == null || routeLogForm.getFutureRootRequests().size() == 0);
    }
    
	/**
	 * This method recurses through the action requests and checks that values are set
	 * appropriately for display in the Future Action Requests section of the Route Log.
	 * 
	 * @param actionRequestValues
	 */
	private void recursiveValidate(List<ActionRequestValue> actionRequestValues) {
		if (actionRequestValues != null) for (ActionRequestValue actionRequestValue : actionRequestValues) {
			assertNotNull(actionRequestValue.getActionRequested());
			assertNotNull(actionRequestValue.getActionRequestedLabel());
			assertNotNull(actionRequestValue.getNodeInstance());
			assertNotNull(actionRequestValue.getNodeInstance().getName());
			assertNotNull(actionRequestValue.getNodeInstance().getRouteNode());
			assertNotNull(actionRequestValue.getNodeInstance().getRouteNode().getNodeType());

			recursiveValidate(actionRequestValue.getChildrenRequests());
		}
	}

}
