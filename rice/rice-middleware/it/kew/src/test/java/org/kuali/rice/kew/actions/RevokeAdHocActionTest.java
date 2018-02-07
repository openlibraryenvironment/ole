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
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class RevokeAdHocActionTest extends KEWTestCase {

	private static final String ADH0C_DOC = "AdhocRouteTest";
	private String docId;

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    /**
     * Tests revoking by action request id.
     */
    @Test public void testRevokeByActionRequestId() throws Exception {
    	WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), ADH0C_DOC);
    	docId = doc.getDocumentId();
    	doc.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "annotation1", getPrincipalIdForName("dewey"), "respDesc1", false);

    	// check the action requests
    	TestUtilities.assertNumberOfPendingRequests(docId, 1);
    	TestUtilities.assertUserHasPendingRequest(docId, "dewey");

    	// now revoke by a bad action request id, we should recieve a WorkflowException
    	try {
    		doc.revokeAdHocRequestById("123456789", "");
    		fail("Revoking by a bad action request id should have thrown an exception!");
    	} catch (InvalidActionTakenException e) {}

    	// revoke by the real action request id
    	List<ActionRequest> actionRequestVOs = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(docId);
    	assertEquals(1, actionRequestVOs.size());
    	String actionRequestId = actionRequestVOs.get(0).getId();
    	doc.revokeAdHocRequestById(actionRequestId, "");

    	// there should now be no pending requests
    	TestUtilities.assertNumberOfPendingRequests(docId, 0);

    	// route the document, this doc type is configured to route to user1
    	doc.route("");
    	doc = getDocument("user1");
    	assertTrue(doc.isApprovalRequested());

    	// now attempt to revoke this non-adhoc request by id, it should throw an error
    	actionRequestVOs = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(docId);
    	for (int index = 0; index < actionRequestVOs.size(); index++) {
    		if (actionRequestVOs.get(index).isPending()) {
    			try {
    				doc.revokeAdHocRequestById(actionRequestVOs.get(index).getId().toString(), "");
    				fail("Attempted to revoke by an invalid action request id, should have thrown an error!");
    			} catch (InvalidActionTakenException e) {}
    		}
    	}

    }

    /**
     * Tests revoking by user and workgroup.
     */
    @Test public void testRevokeByUserAndGroup() throws Exception {
    	// ad hoc the document to dewey (twice) and the workgroup WorkflowAdmin
    	WorkflowDocument doc =WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), ADH0C_DOC);
    	docId = doc.getDocumentId();
    	String principalId = getPrincipalIdForName("dewey");
    	doc.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "annotationDewey1", principalId, "respDesc1", false);
    	doc.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "annotationDewey2", principalId, "respDesc1", false);
    	String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "WorkflowAdmin");
    	doc.adHocToGroup(ActionRequestType.APPROVE, "AdHoc", "Annotation WorkflowAdmin",  groupId, "respDesc2", true);

    	TestUtilities.assertNumberOfPendingRequests(docId, 3);
    	TestUtilities.assertUserHasPendingRequest(docId, "dewey");
    	TestUtilities.assertUserHasPendingRequest(docId, "quickstart");

    	// route the document, this should activate the ad hoc requests
    	doc.route("");
    	assertTrue(doc.isEnroute());
    	TestUtilities.assertAtNodeNew(doc, "AdHoc");
    	TestUtilities.assertNumberOfPendingRequests(docId, 3);

    	String testGroupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "TestWorkgroup");
    	// try revoking by a user and workgroup without adhoc requests, it should effectively be a no-op
    	AdHocRevoke revoke = AdHocRevoke.createRevokeFromPrincipal(getPrincipalIdForName("ewestfal"));
    	doc.revokeAdHocRequests(revoke, "This should be a no-op");
    	revoke = AdHocRevoke.createRevokeFromGroup(testGroupId);
    	doc.revokeAdHocRequests(revoke, "This should be a no-op");
    	doc = getDocument("rkirkend");
    	TestUtilities.assertNumberOfPendingRequests(docId, 3);
    	TestUtilities.assertUserHasPendingRequest(docId, "dewey");
    	TestUtilities.assertUserHasPendingRequest(docId, "quickstart");

    	// now revoke each request in turn, after the second one is invoked the document should transition to it's next route level
    	// and route to user1
    	revoke = AdHocRevoke.createRevokeFromPrincipal(getPrincipalIdForName("dewey"));
    	doc.revokeAdHocRequests(revoke, "revokeUser");
    	TestUtilities.assertNumberOfPendingRequests(docId, 1);
    	doc = getDocument("dewey");
    	assertFalse("dewey should no longer have an approve request.", doc.isApprovalRequested());
    	revoke = AdHocRevoke.createRevokeFromGroup(groupId);
    	doc.revokeAdHocRequests(revoke, "revokeWorkgroup");

    	// the doc should now transition to the next node
    	doc = getDocument("user1");
    	TestUtilities.assertAtNodeNew(doc, "One");
    	assertTrue("user1 should have an approve request.", doc.isApprovalRequested());
    	doc.approve("");

    	// doc should now be final
    	assertTrue("doc should be final", doc.isFinal());
    }

    /**
     * Tests revoking by node name.
     */
    @Test public void testRevokeByNodeName() throws Exception {
    	// ad hoc requests at the AdHoc node and then revoke the entire node
    	WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), ADH0C_DOC);
    	docId = doc.getDocumentId();
    	doc.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "annotationDewey1", getPrincipalIdForName("dewey"), "respDesc1", false);
    	String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "WorkflowAdmin");
    	doc.adHocToGroup(ActionRequestType.APPROVE, "AdHoc", "Annotation WorkflowAdmin", groupId, "respDesc2", true);
    	TestUtilities.assertNumberOfPendingRequests(docId, 2);

    	// now revoke the node
    	doc.revokeAdHocRequests(AdHocRevoke.createRevokeAtNode("AdHoc"), "");
    	TestUtilities.assertNumberOfPendingRequests(docId, 0);
    	// send an Acknowledge to the AdHoc node prior to routing
    	doc.adHocToPrincipal(ActionRequestType.ACKNOWLEDGE, "AdHoc", "annotationEwestfal1", getPrincipalIdForName("ewestfal"), "respDesc1", false);

    	// route the document
    	doc = getDocument("rkirkend");
    	doc.route("");
    	TestUtilities.assertAtNodeNew(doc, "One");

    	// ewestfal should have an acknowledge request
    	doc = getDocument("ewestfal");
    	assertTrue(doc.isAcknowledgeRequested());

    	// approve the document, it should go PROCESSED
    	doc = getDocument("user1");
    	assertTrue(doc.isApprovalRequested());
    	doc.approve("");
    	assertTrue(doc.isProcessed());

    	// revoke at the "One" node where there are no requests, it should be a no-op (the document should stay processed)
    	doc.revokeAdHocRequests(AdHocRevoke.createRevokeAtNode("One"), "");
    	doc = getDocument("ewestfal");
    	assertTrue(doc.isProcessed());

    	// now revoke the ACKNOWLEDGE to ewestfal by revoking at the "AdHoc" node, the document should go FINAL
    	doc.revokeAdHocRequests(AdHocRevoke.createRevokeAtNode("AdHoc"), "");
    	doc = getDocument("ewestfal");
    	assertTrue(doc.isFinal());
    }

    /**
     * Tests the behavior revocation of ad hoc requests prior to the routing of the document.
     *
     * @throws Exception
     */
    @Test public void testRevokePriorToRouting() throws Exception {
    	// ad hoc the document to dewey and the workgroup WorkflowAdmin
    	WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), ADH0C_DOC);
    	docId = doc.getDocumentId();
    	doc.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "annotation1", getPrincipalIdForName("dewey"), "respDesc1", false);

    	doc = getDocument("dewey");
    	assertFalse("User andlee should not have an approve request yet.  Document not yet routed.", doc.isApprovalRequested());
    	String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "WorkflowAdmin");
    	doc.adHocToGroup(ActionRequestType.APPROVE, "AdHoc", "annotation2", groupId , "respDesc2", true);
    	doc = getDocument("quickstart");
    	assertFalse("User should not have approve request yet.  Document not yet routed.", doc.isApprovalRequested());

    	// the document should be initiated at this point
    	assertTrue("Document should still be intitiated.", doc.isInitiated());

    	// check and revoke the actual ActionRequestVOs
    	// reaquire the document as the initiator
    	doc = getDocument("rkirkend");
    	List<ActionRequest> actionRequestVOs = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(doc.getDocumentId());
    	assertEquals("There should be 2 ad hoc requests.", 2, actionRequestVOs.size());
    	for (ActionRequest requestVO : actionRequestVOs) {
    		assertTrue("Should be an ad hoc request.", requestVO.isAdHocRequest());
    		// revoke by id
    		doc.revokeAdHocRequestById(requestVO.getId().toString(), "");
    	}

    	// now the document should have no pending action requests on it
    	List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(docId);
    	assertEquals("There should be no pending requests.", 0, actionRequests.size());

    	// check that the "ActionTakens" have been properly recorded
    	Collection actionTakens = KEWServiceLocator.getActionTakenService().findByDocIdAndAction(docId, KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD);
    	assertEquals("There should be 2 'AdHocRevoked' action takens", 2, actionTakens.size());

    	// now check that the document is still intiated
    	doc = getDocument("rkirkend");
    	assertTrue("Document should still be intitiated.", doc.isInitiated());
    }

    /**
     * Tests the revocation of ad hoc requests after a blanket approve.  The goal of this test is to verify that revocation of
     * ad hoc requests doesn't have any adverse effects on the notification requests
     */
    @Test public void testRevokeAfterBlanketApprove() throws Exception {
    	WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), ADH0C_DOC);
    	docId = doc.getDocumentId();
    	// send an FYI to the AdHoc node prior to blanket approving
    	doc.adHocToPrincipal(ActionRequestType.FYI, "AdHoc", "annotationEwestfal1", getPrincipalIdForName("ewestfal"), "respDesc1", false);

    	// blanket approve the document
    	doc.blanketApprove("");
    	assertTrue(doc.isProcessed());

    	// ewestfal should have his ad hoc FYI and user1 should have an ack from the blanket approve
    	doc = getDocument("ewestfal");
    	assertTrue(doc.isFYIRequested());
    	doc = getDocument("user1");
    	assertTrue(doc.isAcknowledgeRequested());

    	// revoke all ad hoc requests
    	doc.revokeAllAdHocRequests("revoking all adhocs");
    	assertTrue(doc.isProcessed());
    	TestUtilities.assertNumberOfPendingRequests(docId, 1);

    	// user1 should still have acknowledge request
    	assertTrue(doc.isAcknowledgeRequested());

    	// ewestfal should no longer have an fyi
    	doc = getDocument("ewestfal");
    	assertFalse(doc.isFYIRequested());
    }

    private WorkflowDocument getDocument(String netid) throws WorkflowException {
    	return WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(netid), docId);
    }

}
