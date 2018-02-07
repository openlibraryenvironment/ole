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
package org.kuali.rice.kew.role.service.impl;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kns.kim.role.DerivedRoleTypeServiceBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ActionRequestDerivedRoleTypeServiceImpl class. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRequestDerivedRoleTypeServiceImplTest extends KEWTestCase {
	
	private static final String APPROVE_REQUEST_RECIPIENT_ROLE_NAME = "Approve Request Recipient";
	private static final String NON_AD_HOC_APPROVE_REQUEST_RECIPIENT_ROLE_NAME = "Non-Ad Hoc Approve Request Recipient";
	private static final String ACKNOWLEDGE_REQUEST_RECIPIENT_ROLE_NAME = "Acknowledge Request Recipient";
	private static final String FYI_REQUEST_RECIPIENT_ROLE_NAME = "FYI Request Recipient";
	
	
	
	@Override
	protected void loadTestData() throws Exception {
		loadXmlFile("ActionRequestDerivedRoleTypeServiceImplTest.xml");
	}



	@Test public void testHasApplicationRole() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "ActionRequestDerivedRoleTypeServiceImplTest");
		
		// let's send an adhoc request to rkirkend
		document.adHocToPrincipal(ActionRequestType.APPROVE, "", getPrincipalIdForName("rkirkend"), "", true);
		document.route("");
		
		DerivedRoleTypeServiceBase roleTypeService = new ActionRequestDerivedRoleTypeServiceImpl();
		
		Map<String, String> qualifications = Collections
                .singletonMap(KimConstants.AttributeConstants.DOCUMENT_NUMBER, "" + document.getDocumentId());
		
		// rkirkend should have role as an approver
		assertTrue("rkirkend should have role.", roleTypeService.hasDerivedRole(getPrincipalIdForName("rkirkend"), null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		
		// now verify it returns false for the non-adhoc case since we created request to rkirkend as an adhoc request
		assertFalse("rkirkend should not have role for non-adhoc (since this was an adhoc request)", roleTypeService.hasDerivedRole(getPrincipalIdForName("rkirkend"), null, null, NON_AD_HOC_APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		
		// switch to bmcgough and send an acknowledge
		document = TestUtilities.switchByPrincipalName("bmcgough", document);
		document.adHocToPrincipal(ActionRequestType.ACKNOWLEDGE, "", getPrincipalIdForName("bmcgough"), "", true);
		assertTrue("bmcgough should have ack", document.isAcknowledgeRequested());
		
		// bmcgough should not have role as an approver
		assertFalse("bmcgough should not have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("bmcgough"), null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		// bmcgough should have role as an ack
		assertTrue("bmcgough should have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("bmcgough"), null, null, ACKNOWLEDGE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		// bmcgough should not have role as an fyi
		assertFalse("bmcgough should not have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("bmcgough"), null, null, FYI_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		
		// now approve as "rkirkend", should generate a requeest to jhopf
		document = TestUtilities.switchByPrincipalName("rkirkend", document);
		assertTrue(document.isApprovalRequested());
		document.approve("");
		
		// jhopf should now have approve request, not as the result of an adhoc request
		document = TestUtilities.switchByPrincipalName("jhopf", document);
		assertTrue(document.isApprovalRequested());
		assertTrue("jhopf should have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("jhopf"), null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		assertTrue("jhopf should have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("jhopf"), null, null, NON_AD_HOC_APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		
		// send an fyi to "pmckown"
		document.adHocToPrincipal(ActionRequestType.FYI, "", getPrincipalIdForName("pmckown"), "", true);
		document = TestUtilities.switchByPrincipalName("pmckown", document);
		assertTrue(document.isFYIRequested());
		assertTrue("pmckown should have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("pmckown"), null, null, FYI_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
				
		// create a new doc and "save" as ewestfal, this should generate a "complete" request to ewestfal
		document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "ActionRequestDerivedRoleTypeServiceImplTest");
		document.saveDocument("");
		assertTrue(document.isSaved());
		assertTrue(document.isCompletionRequested());
		assertTrue(document.isApprovalRequested());
		
		// the ActionRequestDerivedRoleTypeService does not consider COMPLETE and APPROVE to be the same, let's verify that's the case
		assertFalse("ewestfal should not have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("ewestfal"), null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		assertFalse("ewestfal should not have role", roleTypeService.hasDerivedRole(getPrincipalIdForName("ewestfal"), null, null, NON_AD_HOC_APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
		
	}

    @Test
    public void testDynamicRoleCacheability() {
        RoleService roleService = KimApiServiceLocator.getRoleService();
        Role r = roleService.getRoleByNamespaceCodeAndName("KR-WKFLW", "Approve Request Recipient");
        assertTrue(roleService.isDynamicRoleMembership(r.getId()));
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "ActionRequestDerivedRoleTypeServiceImplTest");

        // let's send an adhoc request to rkirkend
        String rkirkend = getPrincipalIdForName("rkirkend");
        document.adHocToPrincipal(ActionRequestType.APPROVE, "ad-hoc-ing will result in request to member of derive role",
                rkirkend, "approve this ad-hoc'd doc responsibility", true);
        document.route("");

        RoleTypeService roleTypeService = new ActionRequestDerivedRoleTypeServiceImpl();

        Map<String, String> qualifications = Collections.singletonMap(KimConstants.AttributeConstants.DOCUMENT_NUMBER, document.getDocumentId());

        // rkirkend should have role as an approver
        assertTrue("rkirkend should have role.", roleTypeService.hasDerivedRole(rkirkend, null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));

        boolean firstResult = roleService.principalHasRole(rkirkend, Arrays.asList(new String[]{r.getId()}), qualifications);
        assertTrue(firstResult);

        document = WorkflowDocumentFactory.loadDocument(rkirkend, document.getDocumentId());
        document.approve("approved ad-hoc request. rkirkend is no longer a derived approver!");

        // rkirkend should no longer have role as an approver
        assertFalse("rkirkend should have NO LONGER have derived approver role.", roleTypeService.hasDerivedRole(rkirkend, null, null, APPROVE_REQUEST_RECIPIENT_ROLE_NAME, qualifications));
        boolean afterApprove = roleService.principalHasRole(rkirkend, Arrays.asList(new String[]{r.getId()}), qualifications);
        assertFalse(afterApprove);
    }
	
}
