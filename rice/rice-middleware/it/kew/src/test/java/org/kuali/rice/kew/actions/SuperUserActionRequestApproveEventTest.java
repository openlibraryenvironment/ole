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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;

/**
 * Tests the super user actions available on the API.
 */
public class SuperUserActionRequestApproveEventTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testSuperUserActionsOnEnroute() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.adHocToPrincipal(ActionRequestType.FYI, "", getPrincipalIdForName("rkirkend"), "", true);
        document.adHocToPrincipal(ActionRequestType.APPROVE, "", getPrincipalIdForName("jhopf"), "", true);
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an FYI request.", document.isFYIRequested());

        String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(rkirkendPrincipalId, document.getDocumentId(), KewApiConstants.ACTION_REQUEST_FYI_REQ);
        assertEquals("There should only be 1 fyi request to rkirkend.", 1, actionRequests.size());
        document = WorkflowDocumentFactory.loadDocument(rkirkendPrincipalId, document.getDocumentId());
        document.superUserTakeRequestedAction(actionRequests.get(0).getActionRequestId().toString(), "");

        // FYI should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(rkirkendPrincipalId, document.getDocumentId());
        assertFalse("rkirkend should no longer have an FYI request.", document.isFYIRequested());

        // doc should still be enroute
        assertTrue("Document should still be ENROUTE", document.isEnroute());

    }

    @Test public void testSuperUserActionsOnFinal() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SuperUserApproveActionRequestFyiTest");
        document.adHocToPrincipal(ActionRequestType.FYI, "", getPrincipalIdForName("rkirkend"), "", true);
        document.route("");

        // doc should still be final
        assertEquals("Document should be FINAL", DocumentStatus.FINAL, document.getStatus());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an FYI request.", document.isFYIRequested());

        String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(rkirkendPrincipalId, document.getDocumentId(), KewApiConstants.ACTION_REQUEST_FYI_REQ);
        assertEquals("There should only be 1 fyi request to rkirkend.", 1, actionRequests.size());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.superUserTakeRequestedAction(actionRequests.get(0).getActionRequestId().toString(), "");

        // FYI should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(rkirkendPrincipalId, document.getDocumentId());
        assertFalse("rkirkend should no longer have an FYI request.", document.isFYIRequested());
    }
    
    @Test public void testSuperUserActionsOnProcessed() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SuperUserApproveActionRequestFyiTest");
        document.adHocToPrincipal(ActionRequestType.ACKNOWLEDGE, "", getPrincipalIdForName("jhopf"), "", true);
        document.adHocToPrincipal(ActionRequestType.FYI, "", getPrincipalIdForName("rkirkend"), "", true);
        document.route("");

        // doc should still be processed
        assertEquals("Document should be PROCESSED", DocumentStatus.PROCESSED, document.getStatus());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should have an FYI request.", document.isFYIRequested());

        String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
        List<ActionRequestValue> fyiActionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(rkirkendPrincipalId, document.getDocumentId(), KewApiConstants.ACTION_REQUEST_FYI_REQ);
        assertEquals("There should only be 1 fyi request to rkirkend.", 1, fyiActionRequests.size());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.superUserTakeRequestedAction(fyiActionRequests.get(0).getActionRequestId().toString(), "");

        // FYI should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(rkirkendPrincipalId, document.getDocumentId());
        assertFalse("rkirkend should no longer have an FYI request.", document.isFYIRequested());

        // doc should still be processed
        assertEquals("Document should be PROCESSED", DocumentStatus.PROCESSED, document.getStatus());

        String jhopfPrincipalId = getPrincipalIdForName("jhopf");
        List<ActionRequestValue> ackActionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(jhopfPrincipalId, document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ);
        assertEquals("There should only be 1 ACK request to jhopf.", 1, ackActionRequests.size());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.superUserTakeRequestedAction(ackActionRequests.get(0).getActionRequestId().toString(), "");

        // ACK should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(jhopfPrincipalId, document.getDocumentId());
        assertFalse("jhopf should no longer have an ACK request.", document.isAcknowledgeRequested());

        // doc should be final
        assertEquals("Document should be FINAL", DocumentStatus.FINAL, document.getStatus());
    }
    

    @Test public void testSuperUserActionRoutesDocumentToEnroute() throws Exception {
	String documentId = testSuperUserActionRoutesDocument("SuperUserApproveActionRequestApproveTest");
	WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), documentId);
        // doc should now be enroute
        assertEquals("Document should be ENROUTE", DocumentStatus.ENROUTE, document.getStatus());
    }

    @Test public void testSuperUserActionRoutesDocumentToFinal() throws Exception {
	String documentId = testSuperUserActionRoutesDocument("SuperUserApproveActionRequestFyiTest");
	WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), documentId);
        // doc should now be enroute
        assertEquals("Document should be FINAL", DocumentStatus.FINAL, document.getStatus());
    }

    private String testSuperUserActionRoutesDocument(String documentType) throws Exception {
    	String ewestfalPrincipalId = getPrincipalIdForName("ewestfal");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(ewestfalPrincipalId, documentType);
        document.saveDocument("");
        // doc should saved
        assertEquals("Document should be SAVED", DocumentStatus.SAVED, document.getStatus());

        document = WorkflowDocumentFactory.loadDocument(ewestfalPrincipalId, document.getDocumentId());
        assertTrue("ewestfal should have Complete request", document.isCompletionRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertFalse("rkirkend should not have Complete request", document.isCompletionRequested());
        assertFalse("rkirkend should not have Approve request", document.isApprovalRequested());
        assertTrue("rkirkend should be a super user of the document", document.isValidAction(ActionType.SU_APPROVE));
        
        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(ewestfalPrincipalId, document.getDocumentId(), KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
        assertEquals("There should only be 1 complete request to ewestfal as result of the save.", 1, actionRequests.size());
        document.superUserTakeRequestedAction(actionRequests.get(0).getActionRequestId().toString(), "");

        // Complete should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(ewestfalPrincipalId, document.getDocumentId());
        assertFalse("ewestfal should not have Complete request", document.isCompletionRequested());

        return document.getDocumentId();
    }

    @Test public void testSavedDocumentSuperUserAdhocActionsApprove() throws Exception {
	String initiatorNetworkId = "ewestfal";
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(initiatorNetworkId), "SuperUserApproveActionRequestFyiTest");
        String adhocActionUserNetworkId = "jhopf";
        document.adHocToPrincipal(ActionRequestType.APPROVE, "", getPrincipalIdForName(adhocActionUserNetworkId), "", true);
        document.saveDocument("");
        // doc should be saved
        assertEquals("Document should be SAVED", DocumentStatus.SAVED, document.getStatus());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("ewestfal should have Complete request", document.isCompletionRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertFalse("rkirkend should not have Complete request", document.isCompletionRequested());
        assertFalse("rkirkend should not have Approve request", document.isApprovalRequested());
        assertTrue("rkirkend should be a super user of the document", document.isValidAction(ActionType.SU_APPROVE));
        String adhocPrincipalId = getPrincipalIdForName(adhocActionUserNetworkId);
        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllValidRequests(adhocPrincipalId, document.getDocumentId(), ActionRequestType.APPROVE.getCode());
        assertEquals("There should only be 1 approve request to " + adhocActionUserNetworkId + ".", 1, actionRequests.size());
        document.superUserTakeRequestedAction(actionRequests.get(0).getActionRequestId().toString(), "");

        // approve should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(adhocPrincipalId, document.getDocumentId());
        assertFalse(adhocPrincipalId + " should not have approve request", document.isApprovalRequested());

        // complete should no longer be requested
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(initiatorNetworkId), document.getDocumentId());
        assertTrue(initiatorNetworkId + " should not have complete request", document.isCompletionRequested());

        // doc should still be saved
        assertEquals("Document should be SAVED", DocumentStatus.SAVED, document.getStatus());
    }

}
