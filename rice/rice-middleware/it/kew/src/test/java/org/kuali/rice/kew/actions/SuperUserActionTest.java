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
package org.kuali.rice.kew.actions;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the super user actions available on the API.
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class SuperUserActionTest extends KEWTestCase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SuperUserActionTest.class);

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }
	
    @Test public void testSuperUserApprove() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("WorkflowDocument should indicate jhopf as SuperUser", document.isValidAction(ActionType.SU_BLANKET_APPROVE));
        document.superUserBlanketApprove("");
        assertTrue("Document should be 'processed' after Super User Approve", document.isProcessed());
        List requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertTrue("Should be active requests still", requests.size() == 2);//number of acks and fyi's configured through rules
        for (Iterator iter = requests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			if (request.isApproveOrCompleteRequest()) {
				fail("There should be no approve or complete requests after su approve");
			}
		}
	}

    // SUApprove == SUBlanketApprove...what?
    @Test public void testSuperUserApproveDisallowedOnFinalNode() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SUApproveFinalNodeDisallowed");
        document.route("");

        // approve down to last node, at every step (except the last) jhopf should be able to su_approve
        // omit "bmcgough", the last approver
        String nodeBeforeLast = null;
        for (String user: new String[] { "jhopf", "ewestfal", "rkirkend", "natjohns" }) {
            document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
            System.err.println(document.getCurrentNodeNames());
            nodeBeforeLast = document.getCurrentNodeNames().iterator().next();
            assertTrue("jhopf should be able to SU Approve", document.isValidAction(ActionType.SU_BLANKET_APPROVE));

            WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(user), document.getDocumentId()).approve("");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        // it's the last node, no SU Approve for you!
        assertFalse("jhopf should NOT be able to SU Approve", document.isValidAction(ActionType.SU_BLANKET_APPROVE));

        // move back a step
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        document.superUserReturnToPreviousNode(ReturnPoint.create("Split"), "returning to non-final node");
        // now we can SU Approve
        assertTrue("jhopf should be able to SU Approve", document.isValidAction(ActionType.SU_BLANKET_APPROVE));
        document.superUserBlanketApprove("blanket approving as jhopf");

        //assertTrue(document.isFinal());
        assertTrue(document.isProcessed());
        assertTrue(document.isApproved());
        assertFalse("jhopf should NOT be able to SU Approve", document.isValidAction(ActionType.SU_BLANKET_APPROVE));

        List<ActionRequestValue> requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should be active requests still", 5, requests.size());//number of acks and fyi's configured through rules
        for (ActionRequestValue request: requests) {
            System.err.println(request.getActionRequestedLabel() + " -> " + request.getPrincipal().getPrincipalName());
            if (request.isApproveOrCompleteRequest()) {
                fail("There should be no approve or complete requests after su approve");
            }
        }
    }
	
    @Test public void testSuperUserApproveExceptionCases() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "SUApproveDocument");
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        try {
        	document.approve("");
        } catch (Exception e) {
        }
        TestUtilities.getExceptionThreader().join();
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be in exception routing", document.isException());
        document.superUserBlanketApprove("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be final", document.isFinal());
        
        List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertTrue("Should be no active requests for SU Approved document", actionRequests.isEmpty());
    }
    
    @Test public void testSuperUserApproveExceptionCasesWithNotifications() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "SUApproveDocumentNotifications");
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        try {
        	document.approve("");
        } catch (Exception e) {
        }
        TestUtilities.getExceptionThreader().join();
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be in exception routing", document.isException());
        document.superUserBlanketApprove("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be 'processed'", document.isProcessed());
        
        List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertFalse("Should be active requests for SU Approved document", actionRequests.isEmpty());
        for (Iterator iter = actionRequests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			assertTrue("Should be an ack notification request", request.isAcknowledgeRequest());
		}
    }
    
    @Test public void testSuperUserInitiatorApprove() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isValidAction(ActionType.SU_BLANKET_APPROVE));
        document.superUserBlanketApprove("");
        assertTrue("Document should be 'processed' after Super User Approve", document.isProcessed());
        List requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertTrue("Should be active requests still", requests.size() == 2);//number of acks and fyi's configured through rules
        for (Iterator iter = requests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			if (request.isApproveOrCompleteRequest()) {
				fail("There should be no approve or complete requests after su approve");
			}
		}
	}
	
	@Test public void testSuperUserApproveWithNotifications() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "NotificationTestChild");
        assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isValidAction(ActionType.SU_BLANKET_APPROVE));
        document.superUserBlanketApprove("");
        assertTrue("Document should be 'processed' after Super User Approve", document.isProcessed());
        List requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertTrue("Should be active requests still", requests.size() > 2);//number of acks and fyi's configured through rules - we need these for approvals too
        for (Iterator iter = requests.iterator(); iter.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iter.next();
			if (request.isApproveOrCompleteRequest()) {
				fail("There should be no approve or complete requests after su approve");
			}
		} 
	}
	
	@Test public void testSuperUserApproveInvalidUser() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId());
        try {
        	assertFalse("WorkflowDocument should not indicate quickstart as SuperUser", document.isValidAction(ActionType.SU_BLANKET_APPROVE));
        	document.superUserBlanketApprove("");
        	fail("invalid user attempted to SuperUserApprove");
        } catch (Exception e) {
        }
        
	}
	
	@Test public void testSuperUserActionDisregardPostProcessing() throws Exception {
		
		String bmcgoughPrincipalId = getPrincipalIdForName("bmcgough");
		
	    // verify that the post processor class still throws exceptions when post processing document
        WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            document.superUserBlanketApprove("");
            fail("Document should throw exception from post processor");
        } catch (Exception e) {
        }
        
        // test that ignoring the post processor works correctly
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            KEWServiceLocator.getWorkflowDocumentService().superUserCancelAction(bmcgoughPrincipalId, KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId()), "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserCancelAction");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            KEWServiceLocator.getWorkflowDocumentService().superUserDisapproveAction(bmcgoughPrincipalId, KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId()), "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserDisapproveAction");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            KEWServiceLocator.getWorkflowDocumentService().superUserApprove(bmcgoughPrincipalId, KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId()), "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserApprove");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            KEWServiceLocator.getWorkflowDocumentService().superUserNodeApproveAction(bmcgoughPrincipalId, document.getDocumentId(), "Acknowledge1", "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserNodeApprove");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
            KEWServiceLocator.getWorkflowDocumentService().superUserReturnDocumentToPreviousNode(bmcgoughPrincipalId, document.getDocumentId(), "WorkflowDocumentTemplate", "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserReturnDocumentToPreviousNode");
        }

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), generateDummyEnrouteDocument("ewestfal").getDocumentId());
        try {
        	String actionRequestId = null;
            // get actionRequestId to use... there should only be one active action request
            List<ActionRequest> actionRequests = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(document.getDocumentId());
            for (ActionRequest actionRequest : actionRequests) {
                if (actionRequest.isActivated()) {
                    // if we already found an active action request fail the test
                    if (actionRequestId != null) {
                        fail("Found two active action requests for document.  Ids: " + actionRequestId + "  &  " + actionRequest.getId());
                    }
                    actionRequestId = actionRequest.getId();
                }
            }
            
            KEWServiceLocator.getWorkflowDocumentService().superUserActionRequestApproveAction(bmcgoughPrincipalId, document.getDocumentId(), actionRequestId, "", false);
        } catch (Exception e) {
            LOG.error("Exception Found:", e);
            fail("Document should not throw an exception when ignoring post processor during superUserActionRequestApproveAction");
        }

	}
	
	private WorkflowDocument generateDummyEnrouteDocument(String initiatorNetworkId) throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(initiatorNetworkId), "SuperUserActionInvalidPostProcessor");
        assertEquals("Document should be at start node","AdHoc", document.getNodeNames().iterator().next());
        document.route("");
        assertEquals("Document should be at WorkflowDocument2 node","WorkflowDocument2", document.getNodeNames().iterator().next());
        assertEquals("Document should be enroute", DocumentStatus.ENROUTE, document.getStatus());
        return document;
	}
	
	
}
