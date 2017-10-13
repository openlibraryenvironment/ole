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
package org.kuali.rice.kew.rule.service.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.role.service.RoleService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routemodule.TestDocContent;
import org.kuali.rice.kew.routemodule.TestRecipient;
import org.kuali.rice.kew.routemodule.TestResponsibility;
import org.kuali.rice.kew.routemodule.TestRouteLevel;
import org.kuali.rice.kew.routemodule.TestRouteModuleXMLHelper;
import org.kuali.rice.kew.rule.TestRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;

/**
 * Tests the role re-resolving.  This test depends on the route queue being synchronous.
 */
public class RoleServiceTest extends KEWTestCase {

	private static final String TEST_ROLE = "TestRole";
	private static final String TEST_GROUP_1 = "TestGroup1";
	private static final String TEST_GROUP_2 = "TestGroup2";
	private RoleService roleService;
	private String documentId;
	private List<String> group1 = new ArrayList<String>();
	private List<String> group2 = new ArrayList<String>();


	protected void setUpAfterDataLoad() throws Exception {
		super.setUpAfterDataLoad();
		roleService = KEWServiceLocator.getRoleService();
		initializeAttribute();
		documentId = routeDocument();
	}

	private void initializeAttribute() throws Exception {
		group1.add(getPrincipalIdForName("jhopf"));
		group1.add(getPrincipalIdForName("pmckown"));
		group2.add(getPrincipalIdForName("xqi"));
		group2.add(getPrincipalIdForName("tbazler"));
		TestRuleAttribute.addRole(TEST_ROLE);
		TestRuleAttribute.addQualifiedRole(TEST_ROLE, TEST_GROUP_1);
		TestRuleAttribute.addQualifiedRole(TEST_ROLE, TEST_GROUP_2);
		TestRuleAttribute.setRecipientPrincipalIds(TEST_ROLE, TEST_GROUP_1, group1);
		TestRuleAttribute.setRecipientPrincipalIds(TEST_ROLE, TEST_GROUP_2, group2);
	}

	private String routeDocument() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
        document.setApplicationContent(TestRouteModuleXMLHelper.toXML(generateDocContent()));
        document.route("testing only");
        return document.getDocumentId();
	}

	@Test public void testReResolveQualifiedRole() throws Exception {
		DocumentRouteHeaderValue loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		assertEquals(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, loadedDocument.getDocRouteStatus());
		List requests = getTestRoleRequests(loadedDocument);
		assertEquals("Incorrect number of role control requests.", 2, requests.size());
		assertRequestGraphs(requests);

		// change the membership in TEST_GROUP_1
		List<String> newGroup1Recipients = new ArrayList<String>();
		newGroup1Recipients.add(getPrincipalIdForName("bmcgough"));
		newGroup1Recipients.add(getPrincipalIdForName("xqi"));
		newGroup1Recipients.add(getPrincipalIdForName("rkirkend"));
		TestRuleAttribute.setRecipientPrincipalIds(TEST_ROLE, TEST_GROUP_1, newGroup1Recipients);
		roleService.reResolveQualifiedRole(loadedDocument, TEST_ROLE, TEST_GROUP_1);
		loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		assertEquals(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, loadedDocument.getDocRouteStatus());
		requests = getTestRoleRequests(loadedDocument);
        // rkirkend is the initiator so his action should count for the TEST_GROUP_1 role after re-resolving, leaving only a single role request
		assertEquals("Incorrect number of role control requests.", 1, requests.size());
		assertRequestGraphs(requests);
		assertInitiatorRequestDone(TEST_ROLE, TEST_GROUP_1);

        // if we attempt to re-resolve with an non-existant qualified role, it _should_ be legal
        roleService.reResolveQualifiedRole(loadedDocument, TEST_ROLE, "random cool name");
        requests = getTestRoleRequests(loadedDocument);
        assertEquals("Incorrect number of role control requests.", 1, requests.size());
        assertRequestGraphs(requests);
	}

	@Test public void testReResolveQualifiedRoleErrors() throws Exception {
        // attempting to re-resolve with null values should throw exceptions
        try {
            roleService.reResolveQualifiedRole((DocumentRouteHeaderValue)null, null, null);
            fail("Exception should have been thrown when null values are passed.");
        } catch (Exception e) {}

        DocumentRouteHeaderValue loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        try {
            roleService.reResolveQualifiedRole(loadedDocument, null, null);
            fail("Exception should have been thrown when null values are passed.");
        } catch (Exception e) {}

        // need to have a valid role name
        try {
            roleService.reResolveQualifiedRole(loadedDocument, "GimpyRoleName", TEST_GROUP_1);
            fail("Exception should be thrown when attempting to re-resolve with a bad role name.");
        } catch (Exception e) {}

        // now blanket approve a document to make it processed
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
        document.setApplicationContent(TestRouteModuleXMLHelper.toXML(generateDocContent()));
        document.blanketApprove("");
        DocumentRouteHeaderValue baDoc = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        try {
            roleService.reResolveQualifiedRole(baDoc, TEST_ROLE, TEST_GROUP_1);
            fail("Shouldn't be able to resolve on a document with no active nodes.");
        } catch (Exception e) {}

    }

	@Test public void testReResolveRole() throws Exception {
		DocumentRouteHeaderValue loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		assertEquals(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, loadedDocument.getDocRouteStatus());
		List requests = getTestRoleRequests(loadedDocument);
		assertEquals("Incorrect number of role control requests.", 2, requests.size());
		assertRequestGraphs(requests);

		// change membership in TEST_GROUP_1 and TEST_GROUP_2
		List<String> newGroup1 = new ArrayList<String>();
		List<String> newGroup2 = new ArrayList<String>();
		newGroup2.add(getPrincipalIdForName("ewestfal"));
		newGroup2.add(getPrincipalIdForName("jthomas"));
		// TEST_GROUP_1 should now be an empty role, therefore there should not be a request generated for it after re-resolution
		TestRuleAttribute.setRecipientPrincipalIds(TEST_ROLE, TEST_GROUP_1, newGroup1);
		TestRuleAttribute.setRecipientPrincipalIds(TEST_ROLE, TEST_GROUP_2, newGroup2);
		// re-resolve entire role
		roleService.reResolveRole(loadedDocument, TEST_ROLE);
		loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		assertEquals(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, loadedDocument.getDocRouteStatus());
		requests = getTestRoleRequests(loadedDocument);
		// should be 1 because group 1 has no members
		assertEquals("Incorrect number of role control requests.", 1, requests.size());
		assertRequestGraphs(requests);
	}

	@Test public void testReResolveRoleErrors() throws Exception {
        // attempting to re-resolve with null values should throw exceptions
        try {
            roleService.reResolveRole((DocumentRouteHeaderValue)null, null);
            fail("Exception should have been thrown when null values are passed.");
        } catch (RiceIllegalArgumentException e) {}

        DocumentRouteHeaderValue loadedDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        try {
            roleService.reResolveRole(loadedDocument, null);
            fail("Exception should have been thrown when null values are passed.");
        } catch (Exception e) {}

        // need to have a valid role name
        try {
            roleService.reResolveRole(loadedDocument, "GimpyRoleName");
            fail("Exception should be thrown when attempting to re-resolve with a bad role name.");
        } catch (Exception e) {}

        // now blanket approve a document to make it processed
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
        document.setApplicationContent(TestRouteModuleXMLHelper.toXML(generateDocContent()));
        document.blanketApprove("");
        DocumentRouteHeaderValue baDoc = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        try {
            roleService.reResolveRole(baDoc, TEST_ROLE);
            fail("Shouldn't be able to re-resolve on a document with no active nodes.");
        } catch (Exception e) {}
    }

	/**
	 * Extract requests sent to TestRole.
	 */
	private List getTestRoleRequests(DocumentRouteHeaderValue document) {
		List testRoleRequests = new ArrayList();
		List requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocIdAtRouteLevel(document.getDocumentId(), document.getDocRouteLevel());
		for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
			if (TEST_ROLE.equals(actionRequest.getRoleName())) {
				testRoleRequests.add(actionRequest);
			}
		}
		return testRoleRequests;
	}

	private void assertRequestGraphs(List requests) throws Exception {
		for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iterator.next();
			if (TEST_GROUP_1.equals(request.getQualifiedRoleName())) {
				assertQualifiedRoleRequest(request, TEST_ROLE, TEST_GROUP_1);
			} else if (TEST_GROUP_2.equals(request.getQualifiedRoleName())) {
				assertQualifiedRoleRequest(request, TEST_ROLE, TEST_GROUP_2);
			}
		}
	}

	private void assertQualifiedRoleRequest(ActionRequestValue request, String roleName, String qualifiedRoleName) throws Exception {
		assertActionRequest(request, roleName, qualifiedRoleName);
		List<String> recipients = TestRuleAttribute.getRecipientPrincipalIds(roleName, qualifiedRoleName);
		assertEquals("Incorrect number of children requests.", recipients.size(), request.getChildrenRequests().size());
		for (Iterator childIt = request.getChildrenRequests().iterator(); childIt.hasNext();) {
			ActionRequestValue childRequest = (ActionRequestValue) childIt.next();
			assertActionRequest(childRequest, roleName, qualifiedRoleName);
			assertTrue("Child request to invalid user: "+childRequest.getPrincipalId(), containsUser(recipients, childRequest.getPrincipalId()));
			assertEquals("Child request should have no children.", 0, childRequest.getChildrenRequests().size());
		}
	}

	private void assertActionRequest(ActionRequestValue request, String roleName, String qualifiedRoleName) {
		assertEquals("Incorrect role name.", roleName, request.getRoleName());
		assertEquals("Incorrect qualified role name.", qualifiedRoleName, request.getQualifiedRoleName());
		assertEquals("Incorrect qualified role name label.", qualifiedRoleName, request.getQualifiedRoleNameLabel());
		assertTrue("Request should be activated or done.", ActionRequestStatus.ACTIVATED.getCode().equals(request.getStatus()) ||
				ActionRequestStatus.DONE.getCode().equals(request.getStatus()));
	}

	private boolean containsUser(List<String> principalIds, String principalId) throws Exception {
		return principalIds.contains(principalId);
	}

	/**
	 * Gets all "DONE" action requests that are to the initiator (rkirkend).  It then verifies that the initiator has a
	 * complete request and a re-resolved request.
	 */
	private void assertInitiatorRequestDone(String roleName, String qualifiedRoleNameLabel) throws Exception {
        Principal initiator = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName("rkirkend");
		List requests = KEWServiceLocator.getActionRequestService().findByStatusAndDocId(ActionRequestStatus.DONE.getCode(), documentId);
		for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
			ActionRequestValue request = (ActionRequestValue) iterator.next();
			if (!initiator.getPrincipalId().equals(request.getPrincipalId())) {
				iterator.remove();
			}
		}
		assertEquals("Initiator should have a complete request and their re-resolved request.", 2, requests.size());
		int roleRequestCount = 0;
		for (Iterator iterator = requests.iterator(); iterator.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
			if (TEST_ROLE.equals(actionRequest.getRoleName())) {
				roleRequestCount++;
				assertActionRequest(actionRequest, roleName, qualifiedRoleNameLabel);
				assertTrue("Initiator request should have a parent.", actionRequest.getParentActionRequest() != null);
			}
		}
		assertEquals("There should be 1 DONE request from the result of the role re-resolve.", 1, roleRequestCount);
	}

	private TestDocContent generateDocContent() {
		TestDocContent docContent = new TestDocContent();
		List routeLevels = new ArrayList();
		TestRouteLevel routeLevel1 = new TestRouteLevel();
		routeLevels.add(routeLevel1);
		docContent.setRouteLevels(routeLevels);
		routeLevel1.setPriority(1);
		List responsibilities = new ArrayList();
		routeLevel1.setResponsibilities(responsibilities);
		TestResponsibility responsibility1 = new TestResponsibility();
		responsibility1.setActionRequested(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
		responsibility1.setPriority(1);
		TestRecipient recipient1 = new TestRecipient();
		recipient1.setId(getPrincipalIdForName("rkirkend"));
		recipient1.setType(RecipientType.PRINCIPAL.getCode());
		responsibility1.setRecipient(recipient1);
		responsibilities.add(responsibility1);
		return docContent;
	}

}
