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
package org.kuali.rice.kew.actionrequest;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test exercises various Action Request graph scenarios and tests them for correctness.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
/**
 * This is a description of what this class does - jjhanso don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ActionRequestScenariosTest extends KEWTestCase {

	protected void loadTestData() throws Exception {
		loadXmlFile("ActionRequestsConfig.xml");
	}

    /**
     * Tests InlineRequestsRouteModule routing.
     *
     * @throws Exception
     */
    @Test public void testInlineRequestsRouteModule() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("arh14"), "InlineRequestsDocumentType");
        document.setApplicationContent("<blah><step>step1</step></blah>");
        document.route("");

        TestUtilities.assertAtNode(document, "step1");
        List requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue user1Request = (ActionRequestValue) requests.get(0);
        assertEquals(getPrincipalIdForName("user1"), user1Request.getPrincipalId());

        // open doc as user1 and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("user1"), document.getDocumentId());
        document.setApplicationContent("<blah><step>step2</step></blah>");
        document.approve("");

        TestUtilities.assertAtNode(document, "step2");
        requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue workgroupRequest = (ActionRequestValue) requests.get(0);
        assertEquals(getGroupIdFromGroupName("KR-WKFLW", "TestWorkgroup"), workgroupRequest.getGroupId());

        // open doc as user in TestWorkgroup and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("temay"), document.getDocumentId());
        document.setApplicationContent("<blah><step>step3</step></blah>");
        document.approve("");

        TestUtilities.assertAtNode(document, "step3");
        requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue initiatorRequest = (ActionRequestValue) requests.get(0);
        assertEquals("INITIATOR", initiatorRequest.getRoleName());

        //assertEquals(document.getRouteHeader().getInitiator().getDisplayName(), initiatorRequest.getRecipient().getDisplayName());

        assertFalse("Document should not be FINAL", document.isFinal());

        // open doc as initiator and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("arh14"), document.getDocumentId());
        document.approve("");

        assertTrue("Document should be FINAL", document.isFinal());
    }

    @Test public void testInlineRequestsRouteModule_UsingAttributes() throws Exception {
        /*WorkflowDocument document = WorkflowDocumentFactory.createDocument(new NetworkIdVO("arh14"), "InlineRequestsDocumentType_UsingAttributes");
        try {
            document.route("");
            fail("Bad route succeeded");
        } catch (WorkflowException we) {
            // should throw exception as no approvals were generated
        }*/

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("arh14"), "InlineRequestsDocumentType_UsingAttributes");
        document.setApplicationContent("<blah><step>step1</step></blah>");
        document.route("");

        TestUtilities.assertAtNode(document, "step1");
        List requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue user1Request = (ActionRequestValue) requests.get(0);
        assertEquals(getPrincipalIdForName("user1"), user1Request.getPrincipalId());

        // open doc as user1 and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("user1"), document.getDocumentId());
        document.setApplicationContent("<blah><step>step2</step></blah>");
        document.approve("");

        TestUtilities.assertAtNode(document, "step2");
        requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue workgroupRequest = (ActionRequestValue) requests.get(0);
        assertEquals(getGroupIdFromGroupName("KR-WKFLW", "TestWorkgroup"), workgroupRequest.getGroupId());

        // open doc as user in TestWorkgroup and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("temay"), document.getDocumentId());
        document.setApplicationContent("<blah><step>step3</step></blah>");
        document.approve("");

        TestUtilities.assertAtNode(document, "step3");
        requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("Should be 1 request.", 1, requests.size());
        ActionRequestValue initiatorRequest = (ActionRequestValue) requests.get(0);
        assertEquals("INITIATOR", initiatorRequest.getRoleName());
        //assertEquals(getPrincipalIdForName("INITIATOR"), initiatorRequest.getPrincipalId());
        //assertEquals(document.getRouteHeader().getInitiator().getDisplayName(), initiatorRequest.getRecipient().getDisplayName());

        assertFalse("Document should not be FINAL", document.isFinal());

        // open doc as initiator and route it
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("arh14"), document.getDocumentId());
        document.approve("");

        assertTrue("Document should be FINAL", document.isFinal());
    }

    /**
	 * Test that force action works properly in the face of delegations.
	 * Tests the resolution of KULWF-642.
	 *
	 * @throws Exception
	 */
	@Test public void testForceActionWithDelegation() throws Exception {
		// at first, we'll route the document so that the bug is not exposed and verify the action request graph
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("user1"), "testForceActionWithDelegation");
		document.route("");
		TestUtilities.assertAtNode(document, "Node1");
		List rootRequests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
		assertEquals("Should be 1 root request.", 1, rootRequests.size());
		ActionRequestValue ewestfalRequest = (ActionRequestValue)rootRequests.get(0);
		assertTrue("Request to ewestfal should be force action of true", ewestfalRequest.getForceAction());
		assertEquals("Should have 1 child request.", 1, ewestfalRequest.getChildrenRequests().size());
		ActionRequestValue rkirkendRequest = (ActionRequestValue)ewestfalRequest.getChildrenRequests().get(0);
		assertFalse("Request to rkirkend should be force action of false", rkirkendRequest.getForceAction());

		document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("ewestfal"), "testForceActionWithDelegation");

		// After we route the document it should be at the first node in the document where "ewestfal"
		// is the primary approver with force action = true and "rkirkend" is the primary
		// delegate with force action = false.  In the KULWF-642 bug, the document would have
		// progressed past the first node in an auto-approve scenario even though ewestfal's rule
		// is force action = true;
		document.route("");

		// we should be at the first node in the document
		TestUtilities.assertAtNode(document, "Node1");

		document.approve("");
		assertTrue("Document should be FINAL", document.isFinal());


	}

	/**
	 * Test that Role to Role Delegation works properly.
	 * Implemented to expose the bug and test the fix for KULWF-655.
     * @throws Exception
     */
	@Test public void testRoleToRoleDelegation() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("user1"), "testRoleToRoleDelegation");
		document.route("");

		// after routing the document we should have an approve request to ewestfal, this request should have
		// one primary delegate and three secondary delegates
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
		assertTrue("ewestfal should have an approve request.", document.isApprovalRequested());
		// now check all of ewestfal's delegates
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());

		// now approve as the primary delegator, this is where we were seeing the problem in KULWF-655, the
		// action request graph was not getting properly deactivated and it was not getting associated with the
		// "ActionTaken" properly
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		document.approve("Approving as primary delegate.");

		// after the primary delegate approves, verify that the entire action request graph was
		// deactivated in grand fashion
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
		assertFalse("the primary approver should no longer have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());

		List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
		assertEquals("Wrong number of action requests.", 7, actionRequests.size());
		for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iterator.next();
			assertTrue("Request should be deactivated.", request.isDeactivated());
			if (request.isRoleRequest()) {
				assertEquals("Should be all approve request", ActionRequestPolicy.ALL.getCode(), request.getApprovePolicy());
			} else {
				assertEquals("Should not have first approve policy set", ActionRequestPolicy.FIRST.getCode(), request.getApprovePolicy());
			}
		}

	}

	//testMixedbagRoleToRoleDelegation

	@Test public void testRoleToRoleMixedApprovePoliciesDelegation() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("user1"), "testMixedbagRoleToRoleDelegation");
		document.route("");

		// after routing the document we should have an approve request to ewestfal, this request should have
		// one primary delegate and three secondary delegates
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
		assertTrue("ewestfal should have an approve request.", document.isApprovalRequested());
		// now check all of ewestfal's delegates
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
		assertTrue("Should have an approve request.", document.isApprovalRequested());

		// now approve as the primary delegator, this is where we were seeing the problem in KULWF-655, the
		// action request graph was not getting properly deactivated and it was not getting associated with the
		// "ActionTaken" properly
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		document.approve("Approving as primary delegate.");

		// after the primary delegate approves, verify that the entire action request graph was
		// deactivated in grand fashion
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
		assertFalse("the primary approver should no longer have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
		assertFalse("Should not have an approve request.", document.isApprovalRequested());

		List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
		assertEquals("Wrong number of action requests.", 7, actionRequests.size());
		for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iterator.next();
			assertTrue("Request should be deactivated.", request.isDeactivated());
			if (request.isRoleRequest() && request.getRoleName().equals(RoleToRoleDelegationRole.MAIN_ROLE)) {
				assertEquals("Should be all approve request", ActionRequestPolicy.ALL.getCode(), request.getApprovePolicy());
			} else if (request.isRoleRequest() && request.getRoleName().equals(RoleToRoleDelegationRole.PRIMARY_DELEGATE_ROLE)) {
				assertEquals("Should be first approve request", ActionRequestPolicy.FIRST.getCode(), request.getApprovePolicy());
			} else if (request.isRoleRequest() && request.getRoleName().equals(RoleToRoleDelegationRole.SECONDARY_DELEGATE_ROLE)) {
				assertEquals("Should be first approve request", ActionRequestPolicy.FIRST.getCode(), request.getApprovePolicy());
			} else if (request.isRoleRequest()) {
				fail("the roles have been messed up");
			} else {
				assertEquals("Should not have first approve policy set", ActionRequestPolicy.FIRST.getCode(), request.getApprovePolicy());
			}
		}

	}

	// see: https://test.kuali.org/jira/browse/KULRICE-2001
	@Test public void testUnresolvableRoleAttributeRecipients() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("user1"), "UnresolvableRoleRecipsDocType");
        try {
        	document.route("");
        } catch (Exception e) {
            // this doc has a rule with a role that produces an invalid recipient id
            // should receive an error when it attempts to route to the invalid recipient and trigger exception routing on the document
        	TestUtilities.getExceptionThreader().join();
        	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
            assertTrue("Document should be in exception routing", document.isException());
        }
	}
	
	/*
	 * The test was created to test Groups with with the All approve policy
	 * This is commented out because that is currently not supported in rice.
	 */
//	@Test public void testGroupRecipientsWithAllApprovePolicy() throws Exception {
//        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdFromPrincipalName("user1"), "testGroupAllApprovePolicy");
//        document.route("");
//        
//        assertTrue("Should have approval policy of All", document.getActionRequests()[0].getApprovePolicy().equals(ActionRequestPolicy.ALL.getCode()));
//        
//    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
//		assertTrue("ewestfal should have an approve request.", document.isApprovalRequested());
//		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
//		assertTrue("Should have an approve request.", document.isApprovalRequested());
//		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
//		assertTrue("Should have an approve request.", document.isApprovalRequested());
//		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
//		assertTrue("Should have an approve request.", document.isApprovalRequested());
		
		//approve document as jitrue
		//document.approve("Approving as primary jitrue.");
		
		//make sure other group members still have approve requests.
		//document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("ewestfal"), document.getDocumentId());
		//assertTrue("ewestfal should have an approve request.", document.isApprovalRequested());
		//document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jhopf"), document.getDocumentId());
		//assertTrue("Should have an approve request.", document.isApprovalRequested());
		//document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("xqi"), document.getDocumentId());
		//assertTrue("Should have an approve request.", document.isApprovalRequested());
		
		//document = WorkflowDocumentFactory.loadDocument(getPrincipalIdFromPrincipalName("jitrue"), document.getDocumentId());
		//assertTrue("Should NOT have an approve request.", document.isApprovalRequested());
//	}


	private String getPrincipalIdFromPrincipalName(String principalName) {
	    return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName).getPrincipalId();
	}

    private String getGroupIdFromGroupName(String namespace, String groupName) {
        return KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespace, groupName).getId();
    }

    private String getRoleIdFromRoleName(String namespaceCode, String roleName) {
        return KimApiServiceLocator.getRoleService().getRoleIdByNamespaceCodeAndName(namespaceCode, roleName);
    }
}
