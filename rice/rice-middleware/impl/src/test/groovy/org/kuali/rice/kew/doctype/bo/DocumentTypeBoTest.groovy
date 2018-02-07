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
package org.kuali.rice.kew.doctype.bo

import org.junit.Test
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo
import org.junit.Assert
import org.kuali.rice.kew.api.doctype.DocumentTypeContract
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy
import org.kuali.rice.kew.api.doctype.DocumentType
import org.kuali.rice.kew.api.doctype.DocumentTypeAttribute
import org.kuali.rice.kew.doctype.DocumentTypePolicy
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy
import org.kuali.rice.kew.doctype.DocumentTypePolicy

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

class DocumentTypeBoTest {
    def DOCUMENTTYPE_POLICIES = [
      new DocumentTypePolicy(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_ROUTE.code, true),
      new DocumentTypePolicy(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_SAVE.code, false),
      new DocumentTypePolicy(policyName: org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ENROUTE_ERROR_SUPPRESSION.code,
                             policyValue:  false, policyStringValue: "STRINGVALUE")
    ]

    @Test
    public void testEquals() {
        DocumentTypeContract immutable = create();
        org.kuali.rice.kew.doctype.bo.DocumentType bo = org.kuali.rice.kew.doctype.bo.DocumentType.from(immutable)
        Assert.assertEquals(immutable, org.kuali.rice.kew.doctype.bo.DocumentType.to(bo))
    }

    /**
     * Tests policy map generation: DocumentTypePolicy -> (policyValue Boolean).toString()
     */
    @Test
    public void testGetPolicies() {
        org.kuali.rice.kew.doctype.bo.DocumentType dt = new org.kuali.rice.kew.doctype.bo.DocumentType();
        dt.documentTypePolicies = DOCUMENTTYPE_POLICIES;
        assertEquals([
            (org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_ROUTE): Boolean.TRUE.toString(),
            (org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_SAVE): Boolean.FALSE.toString(),
            (org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ENROUTE_ERROR_SUPPRESSION): Boolean.FALSE.toString()
        ], dt.policies)
    }

    /**
     * Tests that getPolicyByName works, specifically DT hierarchy traversal
     */
    @Test
    public void testGetPolicyByName_boolean() {
        org.kuali.rice.kew.doctype.bo.DocumentType parentdt = [ getParentDocType: { null } ] as org.kuali.rice.kew.doctype.bo.DocumentType
        parentdt.documentTypePolicies = DOCUMENTTYPE_POLICIES;
        org.kuali.rice.kew.doctype.bo.DocumentType childdt = [ getParentDocType: { parentdt } ] as org.kuali.rice.kew.doctype.bo.DocumentType

        def policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_ROUTE.code, false)
        assertTrue(policy.policyValue)
        assertTrue(policy.inheritedFlag)

        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_SAVE.code, true)
        assertFalse(policy.policyValue)
        assertTrue(policy.inheritedFlag)

        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ENROUTE_ERROR_SUPPRESSION.code, true)
        assertFalse(policy.policyValue)
        assertEquals("STRINGVALUE", policy.policyStringValue)
        assertTrue(policy.inheritedFlag)

        // test default
        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ALLOW_SU_FINAL_APPROVAL.code, true)
        assertTrue(policy.policyValue)
        assertTrue(policy.inheritedFlag)
    }

    /**
     * Tests that getPolicyByName works, specifically DT hierarchy traversal
     */
    @Test
    public void testGetPolicyByName_string() {
        org.kuali.rice.kew.doctype.bo.DocumentType parentdt = [ getParentDocType: { null } ] as org.kuali.rice.kew.doctype.bo.DocumentType
        parentdt.documentTypePolicies = DOCUMENTTYPE_POLICIES;
        org.kuali.rice.kew.doctype.bo.DocumentType childdt = [ getParentDocType: { parentdt } ] as org.kuali.rice.kew.doctype.bo.DocumentType

        def policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_ROUTE.code, "MUST_ROUTE")
        assertTrue(policy.policyValue)
        assertTrue(policy.inheritedFlag)

        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_SAVE.code, "MUST_SAVE")
        assertFalse(policy.policyValue)
        assertTrue(policy.inheritedFlag)

        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ENROUTE_ERROR_SUPPRESSION.code, "ERROR_SUPPRESSION")
        assertFalse(policy.policyValue)
        assertEquals("STRINGVALUE", policy.policyStringValue)
        assertTrue(policy.inheritedFlag)

        // test default
        policy = childdt.getPolicyByName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.ALLOW_SU_FINAL_APPROVAL.code, "DEFAULT VALUE")
        assertTrue(policy.policyValue)
        assertEquals("DEFAULT VALUE", policy.policyStringValue)
        assertTrue(policy.inheritedFlag)
    }

    public static DocumentType create() {
        return DocumentType.Builder.create(new DocumentTypeContract() {
            def String id = "fakeid"
            def Long versionNumber = 3
            def String name = "DocumentTypeTestName"
            def Integer documentTypeVersion = 5
            def String label = "documenttypetest label"
            def String description = "documenttypetest description"
            def String parentId = "fakeparentid"
            def boolean active = true
            def String unresolvedDocHandlerUrl = "http://fakedochandlerurl"
            def String resolvedDocumentHandlerUrl = "http://fakedochandlerurl"
            def String helpDefinitionUrl = "http://fakehelpdefinitionurl"
            def String docSearchHelpUrl = "http://fakedocsearchhelpurl"
            def String postProcessorName = "postprocessor name"
            def String applicationId = "application id"
            def boolean current = true
            def String blanketApproveGroupId = "fakeblanketapprovegroupid"
            def String superUserGroupId = "fakesuperusergroupid"
            def Map<org.kuali.rice.kew.api.doctype.DocumentTypePolicy, String> getPolicies() {
               def policies = new HashMap<org.kuali.rice.kew.api.doctype.DocumentTypePolicy, String>();
               policies.put(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.DEFAULT_APPROVE, "true")
               policies
            }
            def List<DocumentTypeAttribute> documentTypeAttributes = Collections.EMPTY_LIST
            def String authorizer = "fakeDocumentTypeAuthorizer"
        }).build()
    }
}
