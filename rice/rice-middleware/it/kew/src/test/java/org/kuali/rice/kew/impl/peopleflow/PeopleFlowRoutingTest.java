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
package org.kuali.rice.kew.impl.peopleflow;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDefinition;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDelegate;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowMember;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowService;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.common.delegate.DelegateMember;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * An integration test which tests document types with nodes on them which route to PeopleFlows in various configurations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class PeopleFlowRoutingTest extends KEWTestCase {

    private static final String NAMESPACE_CODE = "TEST";
    private static final String PEOPLE_FLOW_1 = "PeopleFlow1";
    private static final String PEOPLE_FLOW_2 = "PeopleFlow2";
    private static final String PEOPLE_FLOW_3 = "PeopleFlow3";
    private static final String PEOPLE_FLOW_4 = "PeopleFlow4";
    private static final String PEOPLE_FLOW_5 = "PeopleFlow5";
    private static final String PEOPLE_FLOW_6 = "PeopleFlow6";
    private static final String PEOPLE_FLOW_7 = "PeopleFlow7";
    private static final String PEOPLE_FLOW_8 = "PeopleFlow8";
    private static final String PEOPLE_FLOW_9 = "PeopleFlow9";
    private static final String PEOPLE_FLOW_10 = "PeopleFlow10";
    private static final String PEOPLE_FLOW_11 = "PeopleFlow11";
    private static final String PEOPLE_FLOW_12 = "PeopleFlow12";
    private static final String PEOPLE_FLOW_13 = "PeopleFlow13";

    private static final String SINGLE_PEOPLE_FLOW_PARALLEL_APPROVE = "SinglePeopleFlow-Parallel-Approve";
    private static final String SINGLE_PEOPLE_FLOW_SEQUENTIAL_APPROVE = "SinglePeopleFlow-Sequential-Approve";
    private static final String SINGLE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE = "SinglePeopleFlow-PriorityParallel-Approve";
    private static final String MULTIPLE_PEOPLE_FLOW_PRIORITY_PARALLEL = "MultiplePeopleFlow-PriorityParallel";
    private static final String DELEGATE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE = "DelegatePeopleFlow-PriorityParallel-Approve";

    private static final String ROLE_DELEGATE_PEOPLE_FLOW_JUST_KIM_DELEGATE = "RoleDelegatePeopleFlow-JustKimDelegate";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_PRIMARY_DELEGATE = "RoleDelegatePeopleFlow-PrimaryDelegate";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_SECONDARY_DELEGATE = "RoleDelegatePeopleFlow-SecondaryDelegate";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_DELEGATE_ROLE_HAS_KIM_DELEGATE = "RoleDelegatePeopleFlow-DelegateRoleHasKimDelegate";

    private static final String ROLE_DELEGATE_PEOPLE_FLOW_PRINCIPAL_MEMBER_HAS_ROLE_DELEGATE = "RoleDelegatePeopleFlow-PrincipalMemberHasRoleDelegate";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MEMBER_HAS_PRINCIPAL_DELEGATE = "RoleDelegatePeopleFlow-RoleMemberHasPrincipalDelegate";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS = "RoleDelegatePeopleFlow-MultipleRoleMembers";

    private static final String ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_FIRST_APPROVE = "RoleDelegatePeopleFlow-MultipleRoleMembers-FirstApprove";
    private static final String ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_DELEGATE_FIRST_APPROVE = "RoleDelegatePeopleFlow-MultipleRoleMembers-DelegateFirstApprove";
    private static final String GROUP_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS = "GroupDelegatePeopleFlow-MultipleRoleMembers";

    private PeopleFlowService peopleFlowService;

    private String user1;
    private String user2;
    private String user3;
    private String testuser1;
    private String testuser2;
    private String testuser3;
    private String ewestfal;
    private String fran;
    private String earl;
    private String testWorkgroup;
    private String ppfTestRole1;
    private String ppfTestRole2;
    private String ppfTestRole3;
    private String ppfTestRole4;
    private String ppfTestGroup1;

    protected void loadTestData() throws Exception {
        loadXmlFile("PeopleFlowRoutingTest.xml");
    }

    @Before
    public void setupServiceUnderTest() {
        setPeopleFlowService(KewApiServiceLocator.getPeopleFlowService());

        // setup principal and group ids
        user1 = getPrincipalIdForName("user1");
        user2 = getPrincipalIdForName("user2");
        user3 = getPrincipalIdForName("user3");
        testuser1 = getPrincipalIdForName("testuser1");
        testuser2 = getPrincipalIdForName("testuser2");
        testuser3 = getPrincipalIdForName("testuser3");
        ewestfal = getPrincipalIdForName("ewestfal");
        fran = getPrincipalIdForName("fran");
        earl = getPrincipalIdForName("earl");
        testWorkgroup = getGroupIdForName("KR-WKFLW", "TestWorkgroup");

        createTestRolesIfNotExists();
        ppfTestRole1 = getRoleIdForName(NAMESPACE_CODE, "ppfTestRole1");
        ppfTestRole2 = getRoleIdForName(NAMESPACE_CODE, "ppfTestRole2");
        ppfTestRole3 = getRoleIdForName(NAMESPACE_CODE, "ppfTestRole3");
        ppfTestRole4 = getRoleIdForName(NAMESPACE_CODE, "ppfTestRole4");

        ppfTestGroup1 = getGroupIdForName(NAMESPACE_CODE, "ppfTestGroup1");
    }

    protected void setPeopleFlowService(PeopleFlowService peopleFlowService) {
        this.peopleFlowService = peopleFlowService;
    }

    protected PeopleFlowService getPeopleFlowService() {
        return peopleFlowService;
    }

    private void createTestRolesIfNotExists() {
        RoleService roleService = KimApiServiceLocator.getRoleService();
        GroupService groupService = KimApiServiceLocator.getGroupService();

        if (groupService.getGroupByNamespaceCodeAndName(NAMESPACE_CODE, "ppfTestGroup1") == null) {
            Group.Builder testgroup1Builder = Group.Builder.create(NAMESPACE_CODE, "ppfTestGroup1", "1");
            testgroup1Builder.setActive(true);
            Group testgroup1Group = groupService.createGroup(testgroup1Builder.build());

            groupService.addPrincipalToGroup("earl", testgroup1Group.getId());
            groupService.addPrincipalToGroup("fran", testgroup1Group.getId());
        }

        if (getRoleIdForName(NAMESPACE_CODE, "ppfTestRole1") == null) {

            // build ppfTestRole1 which has one member, user1.  user1 has a primary delegate, ewestfal.

            Role.Builder testRole1Builder = Role.Builder.create(null, "ppfTestRole1", NAMESPACE_CODE, "test role 1",
                    "1" /* default role type */);

            Role roleTestRole1 = roleService.createRole(testRole1Builder.build());
            RoleMember user1RoleMember = roleService.assignPrincipalToRole(user1, NAMESPACE_CODE, "ppfTestRole1", Collections.<String, String>emptyMap());

            DelegateType.Builder delegateTypeBuilder =
                    DelegateType.Builder.create(roleTestRole1.getId(), DelegationType.PRIMARY,
                            Collections.<DelegateMember.Builder>emptyList());
            delegateTypeBuilder.setKimTypeId("1");

            DelegateType delegateType = roleService.createDelegateType(delegateTypeBuilder.build());

            DelegateMember.Builder testRole1DelegateBuilder = DelegateMember.Builder.create();
            testRole1DelegateBuilder.setActiveFromDate(DateTime.now());
            testRole1DelegateBuilder.setDelegationId(delegateType.getDelegationId());
            testRole1DelegateBuilder.setRoleMemberId(user1RoleMember.getId());
            testRole1DelegateBuilder.setMemberId(ewestfal);
            testRole1DelegateBuilder.setType(MemberType.PRINCIPAL);

            DelegateMember testRole1Delegate = roleService.createDelegateMember(testRole1DelegateBuilder.build());
        }

        if (getRoleIdForName(NAMESPACE_CODE, "ppfTestRole2") == null) {
            // build ppfTestRole2 which has one member, user2

            Role.Builder testRole2Builder = Role.Builder.create(null, "ppfTestRole2", NAMESPACE_CODE, "test role 2",
                    "1" /* default role type */);

            Role roleTestRole2 = roleService.createRole(testRole2Builder.build());
            RoleMember user2RoleMember = roleService.assignPrincipalToRole(user2, NAMESPACE_CODE, "ppfTestRole2", Collections.<String, String>emptyMap());
        }

        if (getRoleIdForName(NAMESPACE_CODE, "ppfTestRole3") == null) {
            // build ppfTestRole2 which has one member, user2

            Role.Builder testRole3Builder = Role.Builder.create(null, "ppfTestRole3", NAMESPACE_CODE, "test role 3",
                    "1" /* default role type */);

            Role roleTestRole3 = roleService.createRole(testRole3Builder.build());
            RoleMember user1RoleMember = roleService.assignPrincipalToRole(user1, NAMESPACE_CODE, "ppfTestRole3", Collections.<String, String>emptyMap());
            RoleMember user2RoleMember = roleService.assignPrincipalToRole(user2, NAMESPACE_CODE, "ppfTestRole3", Collections.<String, String>emptyMap());
        }

        if (getRoleIdForName(NAMESPACE_CODE, "ppfTestRole4") == null) {
            // build ppfTestRole2 which has one member, user2

            Role.Builder testRole4Builder = Role.Builder.create(null, "ppfTestRole4", NAMESPACE_CODE, "test role 4",
                    "1" /* default role type */);

            Role roleTestRole4 = roleService.createRole(testRole4Builder.build());
            RoleMember testuser1RoleMember = roleService.assignPrincipalToRole(testuser1, NAMESPACE_CODE, "ppfTestRole4", Collections.<String, String>emptyMap());
            RoleMember testuser2RoleMember = roleService.assignPrincipalToRole(testuser2, NAMESPACE_CODE, "ppfTestRole4", Collections.<String, String>emptyMap());
        }
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> user1
     * Priority 2:
     *   -> user2
     * Priority 3:
     *   -> TestWorkgroup
     *
     * </pre>
     */
    private void createSimplePeopleFlow() {
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_1);
        peopleFlow.addPrincipal(user1).setPriority(1);
        peopleFlow.addPrincipal(user2).setPriority(2);
        peopleFlow.addGroup(testWorkgroup).setPriority(3);
        peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    @Test
    public void test_SinglePeopleFlow_Parallel_Approve() throws Exception {
        createSimplePeopleFlow();

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user3 should not have an approval request since they initiated the document
        document.switchPrincipal(user3);

        // user1, user2, and TestWorkgroup (of which ewestfal is a member) should have the request
        assertApproveRequested(document, user1, user2, ewestfal);

        // now approve as each, the document should be FINAL at the end
        document.switchPrincipal(ewestfal);
        document.approve("approving as ewestfal");
        assertTrue("Document should still be enroute.", document.isEnroute());
        document.switchPrincipal(user1);
        document.approve("approving as user1");
        assertTrue("Document should still be enroute.", document.isEnroute());
        document.switchPrincipal(user2);
        document.approve("approving as user2");
        assertTrue("Document should now be FINAL.", document.isFinal());
    }

    @Test
    public void test_SinglePeopleFlow_Sequential_Approve() throws Exception {
        createSimplePeopleFlow();

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_SEQUENTIAL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user3 should not have an approval request since they initiated the document
        document.switchPrincipal(user3);

        // document should be routed to user1, user2, and TestWorkgroup (of which "ewestfal" is a member) but only
        // user1 request should be activated

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 3 root action requests", 3, rootActionRequests.size());
        ActionRequest user1Request = null;
        ActionRequest user2Request = null;
        ActionRequest testWorkgroupRequest = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                } else if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                }
            } else if (recipientType == RecipientType.GROUP) {
                if (testWorkgroup.equals(actionRequest.getGroupId())) {
                    testWorkgroupRequest = actionRequest;
                }
            }
        }
        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.INITIALIZED, user2Request.getStatus());
        assertNotNull(testWorkgroupRequest);
        assertEquals(ActionRequestStatus.INITIALIZED, testWorkgroupRequest.getStatus());

        // let's double-check that before we start approving
        assertApproveNotRequested(document, user2, ewestfal);

        // user1 should have the request for approval, however
        assertApproveRequested(document, user1);

        // approve as user1
        document.switchPrincipal(user1);
        document.approve("");

        // should still be enroute
        assertTrue(document.isEnroute());

        // now user1 should no longer have it, it should be activated approve to user2 with TestWorkgroup still initialized but not activated
        assertApproveNotRequested(document, user1, ewestfal);
        assertApproveRequested(document, user2);

        // approve as user2
        document.switchPrincipal(user2);
        document.approve("");
        // should still be enroute
        assertTrue(document.isEnroute());

        // now user1 and user2 have approved, should be activated to TestWorkgroup of which ewestfal is a member
        assertApproveNotRequested(document, user2, user1);

        // ewestfal should have an approve request because he is a member of TestWorkgroup
        assertApproveRequested(document, ewestfal);
        document.switchPrincipal(ewestfal);
        document.approve("");

        // now document should be final!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> user1
     *   -> user2
     * Priority 2:
     *   -> testuser1
     *   -> testuser2
     * Priority 10:
     *   -> TestWorkgroup
     *   -> testuser3
     *
     * </pre>
     */
    private void createPriorityParallelPeopleFlow() {
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_2);
        peopleFlow.addPrincipal(user1).setPriority(1);
        peopleFlow.addPrincipal(user2).setPriority(1);
        peopleFlow.addPrincipal(testuser1).setPriority(2);
        peopleFlow.addPrincipal(testuser2).setPriority(2);
        // add the last two at a priority which is not contiguous, should still work as expected
        peopleFlow.addGroup(testWorkgroup).setPriority(10);
        peopleFlow.addPrincipal(testuser3).setPriority(10);
        peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    @Test
    public void test_SinglePeopleFlow_PriorityParallel_Approve() throws Exception {
        createPriorityParallelPeopleFlow();

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user3 should not have an approval request since they initiated the document
        document.switchPrincipal(user3);

        // document should be routed to user1, user2, testuser1, testuser2, TestWorkgroup, and testuser3
        // But only user1 and user2 requests should be activated since those are at priority 1

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 6 root action requests", 6, rootActionRequests.size());
        ActionRequest user1Request = null;
        ActionRequest user2Request = null;
        ActionRequest testuser1Request = null;
        ActionRequest testuser2Request = null;
        ActionRequest testWorkgroupRequest = null;
        ActionRequest testuser3Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                } else if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                } else if (testuser1.equals(actionRequest.getPrincipalId())) {
                    testuser1Request = actionRequest;
                } else if (testuser2.equals(actionRequest.getPrincipalId())) {
                    testuser2Request = actionRequest;
                } else if (testuser3.equals(actionRequest.getPrincipalId())) {
                    testuser3Request = actionRequest;
                }
            } else if (recipientType == RecipientType.GROUP) {
                if (testWorkgroup.equals(actionRequest.getGroupId())) {
                    testWorkgroupRequest = actionRequest;
                }
            }
        }
        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user2Request.getStatus());
        assertNotNull(testuser1Request);
        assertEquals(ActionRequestStatus.INITIALIZED, testuser1Request.getStatus());
        assertNotNull(testuser2Request);
        assertEquals(ActionRequestStatus.INITIALIZED, testuser2Request.getStatus());
        assertNotNull(testWorkgroupRequest);
        assertEquals(ActionRequestStatus.INITIALIZED, testWorkgroupRequest.getStatus());
        assertNotNull(testuser3Request);
        assertEquals(ActionRequestStatus.INITIALIZED, testuser3Request.getStatus());

        // let's double-check that before we start approving

        assertApproveRequested(document, user1, user2);
        assertApproveNotRequested(document, testuser1, testuser2, testuser3, ewestfal);

        // approve as user1 and user2
        document.switchPrincipal(user1);
        document.approve("");
        assertApproveRequested(document, user2);
        assertApproveNotRequested(document, user1);
        document.switchPrincipal(user2);
        document.approve("");
        assertTrue(document.isEnroute());

        assertApproveRequested(document, testuser1, testuser2);
        assertApproveNotRequested(document, user1, user2, testuser3, ewestfal);

        // approve as testuser1 and testuser2
        document.switchPrincipal(testuser1);
        document.approve("");
        document.switchPrincipal(testuser2);
        document.approve("");
        assertTrue(document.isEnroute());

        assertApproveRequested(document, testuser3, ewestfal);
        assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

        // now approve as ewestfal and testuser3, then doc should be final
        document.switchPrincipal(testuser3);
        document.approve("");
        document.switchPrincipal(ewestfal);
        document.approve("");
        assertTrue(document.isFinal());
    }

    /**
     * Defines PeopleFlow as follows:
     *
     * <pre>
     *
     * 1 - PeopleFlow - TEST:PeopleFlow1
     *   -> Priority 1
     *   ----> user1
     *   -> Priority 2
     *   ----> user2
     * 2 - PeopleFlow - TEST:PeopleFlow2
     *   -> Priority 1
     *   ----> testuser1
     *   -> Priority 2
     *   ----> testuser2
     * 3 - PeopleFlow - TEST:PeopleFlow3
     *   -> Priority 1
     *   ----> TestWorkgroup
     *   -> Priority 10
     *   ----> testuser3
     *
     * </pre>
     */
    private void createMultiplePeopleFlows() {
        PeopleFlowDefinition.Builder peopleFlow1 = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_1);
        peopleFlow1.addPrincipal(user1).setPriority(1);
        peopleFlow1.addPrincipal(user2).setPriority(2);
        peopleFlowService.createPeopleFlow(peopleFlow1.build());

        PeopleFlowDefinition.Builder peopleFlow2 = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_2);
        peopleFlow2.addPrincipal(testuser1).setPriority(1);
        peopleFlow2.addPrincipal(testuser2).setPriority(2);
        peopleFlowService.createPeopleFlow(peopleFlow2.build());

        PeopleFlowDefinition.Builder peopleFlow3 = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_3);
        peopleFlow3.addGroup(testWorkgroup).setPriority(1);
        peopleFlow3.addPrincipal(testuser3).setPriority(10);
        peopleFlowService.createPeopleFlow(peopleFlow3.build());
    }


    @Test
    public void test_MultiplePeopleFlow_PriorityParallel() throws Exception {
        createMultiplePeopleFlows();

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, MULTIPLE_PEOPLE_FLOW_PRIORITY_PARALLEL);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user3 should not have an approval request since they initiated the document
        document.switchPrincipal(user3);

        // document should only send requests to the first people flow which should be user1 and user2
        // But only user1 request should be activated

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 2 root action requests", 2, rootActionRequests.size());
        ActionRequest user1Request = null;
        ActionRequest user2Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                } else if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                }
            }
        }
        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.INITIALIZED, user2Request.getStatus());

        // now approve as user1
        document.switchPrincipal(user1);
        document.approve("");
        document.switchPrincipal(user2);
        assertTrue(document.isApprovalRequested());
        document.approve("");

        // at this point, it should transition to the next people flow and generate Acknowledge requests to testuser1 and testuser2,
        // and then generate an activated approve request to TestWorkgroup (of which "ewestfal" is a member) and then an
        // initialized approve request to testuser3
        assertTrue(document.isEnroute());
        assertApproveRequested(document, ewestfal);
        assertApproveNotRequested(document, user1, user2, testuser1, testuser2, testuser3);
        assertAcknowledgeRequested(document, testuser1, testuser2);

        // now load as ewestfal (member of TestWorkgroup) and approve
        document.switchPrincipal(ewestfal);
        assertTrue(document.isApprovalRequested());
        document.approve("");
        assertTrue(document.isEnroute());

        // now the only remaining approval request should be to testuser3
        assertApproveRequested(document, testuser3);
        // just for fun, let's take testuser2's acknowledge action
        document.switchPrincipal(testuser2);
        assertTrue(document.isAcknowledgeRequested());
        document.acknowledge("");
        assertTrue(document.isEnroute());

        // testuser3 should still have an approve request, let's take it
        assertApproveRequested(document, testuser3);
        document.switchPrincipal(testuser3);
        document.approve("");

        // document should now be in the processed state
        assertTrue(document.isProcessed());
        // load the last ack to testuser1 and take it
        document.switchPrincipal(testuser1);
        assertTrue(document.isAcknowledgeRequested());
        document.acknowledge("");

        // now the document should be final!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> testuser3
     *   ----> ppfTestRole2 - primary delegate
     *
     * </pre>
     *
     * this test will ensure that the delegate, a role is properly delegated to when the member is a principal
     */
    @Test
    public void test_PrincipalMember_roleDelegate() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(testuser3, MemberType.PRINCIPAL);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(ppfTestRole2, MemberType.ROLE);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActionRequestPolicy(ActionRequestPolicy.FIRST);

        createSimplePeopleFlow(PEOPLE_FLOW_8, member, delegate);

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_PRINCIPAL_MEMBER_HAS_ROLE_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // testuser3 and delegate user2 (as member of ppfTestRole2) should have
        // activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest testuser3Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (testuser3.equals(actionRequest.getPrincipalId())) {
                    testuser3Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(testuser3Request);
        assertEquals(ActionRequestStatus.ACTIVATED, testuser3Request.getStatus());

        // check delegate requests on testuser3Request now
        assertEquals(1, testuser3Request.getChildRequests().size());
        ActionRequest user2Request = testuser3Request.getChildRequests().get(0);
        assertEquals(user2, user2Request.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, user2Request.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, user2Request.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user2, testuser3);
        assertApproveNotRequested(document, user1, user3, testuser1, testuser2, ewestfal);

        // approve as user2 who is the lone member of testrole2
        document.switchPrincipal(user2);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user2, testuser3);

        // document should now be FINAL!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole2
     *   ----> testuser3 - primary delegate
     *
     * </pre>
     *
     * ensure that the delegate, a principal, gets the requests when the member is a role
     */
    @Test
    public void test_RoleMember_principalDelegate() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(ppfTestRole2, MemberType.ROLE);
        member.setActionRequestPolicy(ActionRequestPolicy.FIRST);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(testuser3, MemberType.PRINCIPAL);
        delegate.setDelegationType(DelegationType.PRIMARY);

        createSimplePeopleFlow(PEOPLE_FLOW_9, member, delegate);

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MEMBER_HAS_PRINCIPAL_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user2 (as member of ppfTestRole2) and delegate testuser3 should have
        // activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest user2Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user2Request.getStatus());

        // check delegate requests on testuser3Request now
        assertEquals(1, user2Request.getChildRequests().size());
        ActionRequest testuser3Request = user2Request.getChildRequests().get(0);
        assertEquals(testuser3, testuser3Request.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, testuser3Request.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, testuser3Request.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user2, testuser3);
        assertApproveNotRequested(document, user1, user3, testuser1, testuser2, ewestfal);

        // approve as testuser3, the delegate
        document.switchPrincipal(testuser3);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user2, testuser3);

        // document should now be FINAL!
        assertTrue(document.isFinal());

    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole1 (has delegate ewestfal defined in KIM)
     *
     * </pre>
     *
     * The desired behavior is that the KIM delegate gets requests
     */
    @Test
    public void test_RoleDelegate_justKimDelegate() throws Exception {
        createSimpleRoleDelegatePeopleFlow(PEOPLE_FLOW_4, // PeopleFlow name
                ppfTestRole1,  // member role ID
                null,          // no delegate
                null           // ^^ so no DelegationType
        );

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_JUST_KIM_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user1 (as member of ppfTestRole1) and ewestfal (as user1's primary KIM delegate for ppfTestRole1) should have
        // activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest user1Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());

        // check delegate requests on user1Request now
        assertEquals(1, user1Request.getChildRequests().size());
        ActionRequest ewestfalDelegateRequest = user1Request.getChildRequests().get(0);
        assertEquals(ewestfal, ewestfalDelegateRequest.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, ewestfalDelegateRequest.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, ewestfalDelegateRequest.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user1, ewestfal);
        assertApproveNotRequested(document, user2, user3, testuser1, testuser2, testuser3);

        // approve as ewestfal who is user1's primary KIM delegate for testrole1
        document.switchPrincipal(ewestfal);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user1, ewestfal);

        // document should now be FINAL!
        assertTrue(document.isFinal());
    }



    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole1 (has delegate ewestfal defined in KIM)
     *   ----> ppfTestRole2 - primary delegate
     *
     * </pre>
     *
     * The desired behavior is that the delegate defined in the PeopleFlow gets the requests, overriding the KIM
     * delegate which gets ignored.  Since the delegation type is 'primary', only ppfTestRole2's member gets requests.
     */
    @Test
    public void test_RoleDelegate_primaryDelegateRole() throws Exception {
        createSimpleRoleDelegatePeopleFlow(PEOPLE_FLOW_5,    // PeopleFlow name
                ppfTestRole1,     // member role ID
                ppfTestRole2,     // delegate role ID
                DelegationType.PRIMARY);

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_PRIMARY_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user1 (as member of ppfTestRole1) and user2 (as a member of user1's primary delegate role for ppfTestRole1)
        // should have activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest user1Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());

        // check delegate requests on user1Request now
        assertEquals(1, user1Request.getChildRequests().size());
        ActionRequest user2DelegateRequest = user1Request.getChildRequests().get(0);
        assertEquals(user2, user2DelegateRequest.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, user2DelegateRequest.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, user2DelegateRequest.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user1, user2);
        assertApproveNotRequested(document, user3, testuser1, testuser2, testuser3, ewestfal);

        // approve as user2 who is user1's primary PPF delegate for testrole1
        document.switchPrincipal(user2);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user1, user2);

        // document should now be FINAL!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole1 (has delegate ewestfal defined in KIM)
     *   ----> ppfTestRole2 - secondary delegate
     *
     * </pre>
     *
     * The desired behavior is that the delegate defined in the PeopleFlow gets the requests, overriding the KIM
     * delegate which gets ignored.  Since the delegation type is 'secondary', both ppfTestRole2 and ppfTestRole2 get
     * requests.
     */
    @Test
    public void test_RoleDelegate_secondaryDelegateRole() throws Exception {
        createSimpleRoleDelegatePeopleFlow(
                PEOPLE_FLOW_6,    // PeopleFlow name
                ppfTestRole1,     // member role ID
                ppfTestRole2,     // delegate role ID
                DelegationType.SECONDARY
        );

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_SECONDARY_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user1 (as member of ppfTestRole1) and user2 (as a member of user1's primary delegate role for ppfTestRole1)
        // should have activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest user1Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());

        // check delegate requests on user1Request now
        assertEquals(1, user1Request.getChildRequests().size());
        ActionRequest user2DelegateRequest = user1Request.getChildRequests().get(0);
        assertEquals(user2, user2DelegateRequest.getPrincipalId());
        assertEquals(DelegationType.SECONDARY, user2DelegateRequest.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, user2DelegateRequest.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user1, user2);
        assertApproveNotRequested(document, user3, testuser1, testuser2, testuser3, ewestfal);

        // approve as user2 who is user1's primary PPF delegate for testrole1
        document.switchPrincipal(user2);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user1, user2);

        // document should now be FINAL!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole2
     *   ----> ppfTestRole1 - primary delegate (+ has delegate ewestfal defined in KIM)
     *
     * </pre>
     *
     * KEW is limited to only one level of delegation, so the behavior will be to respect the delegate defined in the
     * PeopleFlow, but ignore the KIM delegate otherwise we'd have to add support for multi-level delegation.  Since
     * the PeopleFlow delegation is 'primary', ppfTestRole1 will get the requests.
     */
    @Test
    public void test_RoleDelegate_delegateRoleHasKimDelegate() throws Exception {
        createSimpleRoleDelegatePeopleFlow(PEOPLE_FLOW_7,    // PeopleFlow name
                ppfTestRole2,     // member role ID
                ppfTestRole1,     // delegate role ID
                DelegationType.PRIMARY);

        // now route a document to it
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_DELEGATE_ROLE_HAS_KIM_DELEGATE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user2 (as member of ppfTestRole2) and user1 (as a member of user1's primary delegate role for ppfTestRole2)
        // should have activated requests, make sure the requests look correct

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 1 root action requests", 1, rootActionRequests.size());
        ActionRequest user2Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                }
            }
        }

        // now let's ensure we got the requests we wanted
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user2Request.getStatus());

        // check delegate requests on user1Request now
        assertEquals(1, user2Request.getChildRequests().size());
        ActionRequest user1DelegateRequest = user2Request.getChildRequests().get(0);
        assertEquals(user1, user1DelegateRequest.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, user1DelegateRequest.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, user1DelegateRequest.getStatus());

        // let's run through the approvals for this peopleflow
        assertApproveRequested(document, user1, user2);
        assertApproveNotRequested(document, user3, testuser1, testuser2, testuser3, ewestfal);

        // approve as user1 who is user2's primary PPF delegate for testrole2
        document.switchPrincipal(user1);
        document.approve("");

        // at this point all priorty1 folks should have approvals
        assertApproveNotRequested(document, user1, user2);

        // document should now be FINAL!
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole3
     *   ----> ppfTestRole4 - primary delegate
     *
     * </pre>
     *
     * this test will ensure that approvals work as expected when a role delegates to a role and both have
     * the action request policy ALL
     */
    @Test
    public void test_RoleDelegate_RoleMemberWithMultiplePrincipals_AllApprove() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(ppfTestRole3, MemberType.ROLE);
        member.setActionRequestPolicy(ActionRequestPolicy.ALL);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(ppfTestRole4, MemberType.ROLE);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActionRequestPolicy(ActionRequestPolicy.ALL);
        createSimplePeopleFlow(PEOPLE_FLOW_10, member, delegate);

        // prove that if both members approve, the doc goes final
        {
            // route a document to the peopleflow
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // ppfTestRole3 members (user1, user2) delegate role ppfTestRole4 members (testuser1, testuser2) should have
            // activated requests

            // let's run through the approvals for this peopleflow
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as testuser1 who a member of ppfTestRole3
            document.switchPrincipal(user1);
            document.approve("");

            // approve as testuser2 who is the second member of ppfTestRole3
            document.switchPrincipal(user2);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }

        // prove that if both delegates approve, the doc goes final
        {
            // route a document to the peopleflow
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // ppfTestRole3 members (user1, user2) delegate role ppfTestRole4 members (testuser1, testuser2) should have
            // activated requests

            // let's run through the approvals for this peopleflow
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as testuser1 who a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            // approve as testuser2 who is the second member of ppfTestRole4
            document.switchPrincipal(testuser2);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }

        // Prove that if a member approves, then both delegates need to approve due to the action request policy of
        // ALL for the doc to go final
        {
            // route a document to the peopleflow
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // ppfTestRole3 members (user1, user2) delegate role ppfTestRole4 members (testuser1, testuser2) should have
            // activated requests

            // let's run through the approvals for this peopleflow
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as user1 who a member of ppfTestRole3
            document.switchPrincipal(user1);
            document.approve("");

            assertApproveNotRequested(document, user1);

            // approve as testuser1 who a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            // approve as testuser2 who is the second member of ppfTestRole4
            document.switchPrincipal(testuser2);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole3
     *   ----> ppfTestRole4 - primary delegate
     *
     * </pre>
     *
     * The action request policy for each role is set to FIRST.
     * this test will verify that when one delegate approves, the document goes final.
     */
    @Test
    public void test_RoleDelegate_RoleMemberWithMultiplePrincipals_FirstApprove() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(ppfTestRole3, MemberType.ROLE);
        member.setActionRequestPolicy(ActionRequestPolicy.FIRST);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(ppfTestRole4, MemberType.ROLE);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActionRequestPolicy(ActionRequestPolicy.FIRST);

        createSimplePeopleFlow(PEOPLE_FLOW_11, member, delegate);

        // First, prove that just one of the delegates needs to approve for the doc to go final:
        {
            // route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_FIRST_APPROVE);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // verify the expected initial state
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as testuser1 who a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            assertApproveNotRequested(document, user1, user2, testuser1, testuser2, user3, ewestfal);

            assertTrue(document.isFinal());
        }

        // prove that just one of the members needs to approve for the doc to go final:
        {
            // route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_FIRST_APPROVE);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // verify the expected initial state
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as testuser1 who a member of ppfTestRole4
            document.switchPrincipal(user1);
            document.approve("");

            assertApproveNotRequested(document, user1, user2, testuser1, testuser2, user3, ewestfal);

            assertTrue(document.isFinal());
        }

    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestRole3
     *   ----> ppfTestRole4 - primary delegate
     *
     * </pre>
     *
     * this test will ensure that the delegate, a role is properly delegated to when the member is a principal
     */
    @Test
    public void test_RoleDelegate_RoleMemberWithMultiplePrincipals_DelegateFirstApprove() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(ppfTestRole3, MemberType.ROLE);
        member.setActionRequestPolicy(ActionRequestPolicy.ALL);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(ppfTestRole4, MemberType.ROLE);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActionRequestPolicy(ActionRequestPolicy.FIRST);
        createSimplePeopleFlow(PEOPLE_FLOW_12, member, delegate);

        // prove that both members need to approve before the doc goes final (due to the
        // ALL action request policy on the member role).
        {
            // now route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_DELEGATE_FIRST_APPROVE);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());


            // verify initial state
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as user1 who is a member of ppfTestRole3
            document.switchPrincipal(user1);
            document.approve("");

            assertApproveRequested(document, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user1);

            // approve as testuser1 who is a member of ppfTestRole3
            document.switchPrincipal(user2);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }

        // prove that only one delegate needs to approve due to action request policy FIRST on the delegate role
        {
            // now route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_DELEGATE_FIRST_APPROVE);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());


            // let's run through the approvals for this peopleflow
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as testuser1 who is a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }

        // prove that if one member approves and then one delegate approves, the doc goes final (due to the
        // ALL action request policy on the member role).
        {
            // now route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, ROLE_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS_DELEGATE_FIRST_APPROVE);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());


            // verify initial state
            assertApproveRequested(document, user1, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user3, testuser3, ewestfal);

            // approve as user1 who is a member of ppfTestRole3
            document.switchPrincipal(user1);
            document.approve("");

            assertApproveRequested(document, user2, testuser1, testuser2);
            assertApproveNotRequested(document, user1);

            // approve as testuser1 who is a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> ppfTestGroup1
     *   ----> ppfTestRole4 - primary delegate
     *
     * </pre>
     *
     * this test will verify that groups can delegate to roles, and that the ALL approval logic will hold for the
     * delegate
     */
    @Test
    public void test_GroupDelegate_RoleMemberWithMultiplePrincipals_AllApprove() throws Exception {
        PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(ppfTestGroup1, MemberType.GROUP);
        PeopleFlowDelegate.Builder delegate = PeopleFlowDelegate.Builder.create(ppfTestRole4, MemberType.ROLE);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActionRequestPolicy(ActionRequestPolicy.ALL);
        createSimplePeopleFlow(PEOPLE_FLOW_13, member, delegate);

        // prove that one group member approving makes the doc go final
        {
            // now route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    GROUP_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            // let's run through the approvals for this peopleflow
            assertApproveRequested(document, earl, fran, testuser1, testuser2);
            assertApproveNotRequested(document, user1, user2, user3, testuser3, ewestfal);

            // approve as earl who a member of ppfTestGroup1
            document.switchPrincipal(earl);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, earl, fran, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }

        // prove that both delegates need to approve (due to action request policy ALL) before the doc goes final
        {
            // now route a document to it
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3,
                    GROUP_DELEGATE_PEOPLE_FLOW_ROLE_MULTIPLE_MEMBERS);
            document.route("");
            assertTrue("Document should be enroute.", document.isEnroute());

            assertApproveRequested(document, earl, fran, testuser1, testuser2);
            assertApproveNotRequested(document, user1, user2, user3, testuser3, ewestfal);

            // approve as testuser1 who a member of ppfTestRole4
            document.switchPrincipal(testuser1);
            document.approve("");

            assertFalse(document.isFinal());

            // approve as testuser2 who a member of ppfTestRole4
            document.switchPrincipal(testuser2);
            document.approve("");

            // at this point all priorty1 folks should have approvals
            assertApproveNotRequested(document, user1, user2, earl, fran, testuser1, testuser2);

            // document should now be FINAL!
            assertTrue(document.isFinal());
        }
    }

    /**
     * creates a simple PeopleFlow in the TEST namespace with (by default) a member of type role having a delegate of
     * type role as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> memberRole
     *   ----> delegateRole
     *
     * </pre>
     *
     * The member and delegate roles are specified by ID as memberRoleId and delegateRoleId.
     * The type of the delegate (primary or secondary) is specified by delegationType.
     * If the delegate ID is null, no delegate will be added.
     *
     * @param peopleFlowName the name to give the new PeopleFlow (the namespace will always be TEST)
     * @param memberRoleId the ID of the role for the PeopleFlow member
     * @param delegateRoleId the ID of the role for the PeopleFlow member's delegate
     * @param delegationType the delegate's type (primary or secondary)
     * @return the PeopleFlowDefinition created
     */
    private PeopleFlowDefinition createSimpleRoleDelegatePeopleFlow(String peopleFlowName, String memberRoleId, String delegateRoleId,
            DelegationType delegationType) {
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, peopleFlowName);

        // build stop 1
        // build role member
        PeopleFlowMember.Builder memberBuilder = peopleFlow.addRole(memberRoleId);
        memberBuilder.setPriority(1);
        memberBuilder.setActionRequestPolicy(ActionRequestPolicy.FIRST);

        if (delegateRoleId != null) {
            // build primary delegate role1
            PeopleFlowDelegate.Builder delegateBuilder =
                    PeopleFlowDelegate.Builder.create(delegateRoleId, MemberType.ROLE);
            delegateBuilder.setDelegationType(delegationType);
            delegateBuilder.setActionRequestPolicy(ActionRequestPolicy.FIRST);

            memberBuilder.getDelegates().add(delegateBuilder);
        }

        return peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    /**
     * creates a simple PeopleFlow in the TEST namespace with a member having a delegate, like so:
     *
     * <pre>
     *
     * Priority 1:
     *   -> member
     *   ----> delegate
     *
     * </pre>
     *
     * @return the PeopleFlowDefinition created
     */
    private PeopleFlowDefinition createSimplePeopleFlow(String peopleFlowName, PeopleFlowMember.Builder member, PeopleFlowDelegate.Builder delegate) {
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, peopleFlowName);

        member.setPriority(1);

        if (delegate != null) {
            // link delegate to member
            member.getDelegates().add(delegate);
        }

        peopleFlow.getMembers().add(member);

        return peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    /**
     * Defines a PeopleFlow as follows:
     *
     * <pre>
     *
     * Priority 1:
     *   -> user1
     *   -> user2
     *   ----> testuser2 - primary delegate
     * Priority 2:
     *   -> testuser1
     *   ----> TestWorkgroup - secondary delegate
     *   ----> testuser3 - primary delegate
     *   -> user3
     *
     * </pre>
     */
    private void createDelegatePeopleFlow() {
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_1);
        peopleFlow.addPrincipal(user1).setPriority(1);

        PeopleFlowMember.Builder user2Builder = peopleFlow.addPrincipal(user2);
        user2Builder.setPriority(1);
        PeopleFlowDelegate.Builder user2PrimaryDelegateBuilder = PeopleFlowDelegate.Builder.create(testuser2,
                MemberType.PRINCIPAL);
        user2PrimaryDelegateBuilder.setDelegationType(DelegationType.PRIMARY);
        user2Builder.getDelegates().add(user2PrimaryDelegateBuilder);

        PeopleFlowMember.Builder testuser1Builder = peopleFlow.addPrincipal(testuser1);
        testuser1Builder.setPriority(2);
        PeopleFlowDelegate.Builder testWorkgroupSecondaryDelegateBuilder = PeopleFlowDelegate.Builder.create(testWorkgroup, MemberType.GROUP);
        testWorkgroupSecondaryDelegateBuilder.setDelegationType(DelegationType.SECONDARY);
        testuser1Builder.getDelegates().add(testWorkgroupSecondaryDelegateBuilder);
        PeopleFlowDelegate.Builder testuser3PrimaryDelegateBuilder = PeopleFlowDelegate.Builder.create(testuser3, MemberType.PRINCIPAL);
        testuser3PrimaryDelegateBuilder.setDelegationType(DelegationType.PRIMARY);
        testuser1Builder.getDelegates().add(testuser3PrimaryDelegateBuilder);

        peopleFlow.addPrincipal(user3).setPriority(2);

        peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    @Test
    public void test_DelegatePeopleFlow_PriorityParallel_Approve() throws Exception {
        createDelegatePeopleFlow();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, DELEGATE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // user3 should not have an approval request since they initiated the document
        document.switchPrincipal(user3);

        // user1, user2, and testuser2 (as user2's primary delegate) should all have activated requests, make sure the requests look correct
        // user3 and testuser1 should have root requests as well, TestWorkgroup and testuser3 should be delegates of testuser1

        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals("Should have 4 root action requests", 4, rootActionRequests.size());
        ActionRequest user1Request = null;
        ActionRequest user2Request = null;
        ActionRequest user3Request = null;
        ActionRequest testuser1Request = null;
        for (ActionRequest actionRequest : rootActionRequests) {
            RecipientType recipientType = actionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (user1.equals(actionRequest.getPrincipalId())) {
                    user1Request = actionRequest;
                } else if (user2.equals(actionRequest.getPrincipalId())) {
                    user2Request = actionRequest;
                } else if (user3.equals(actionRequest.getPrincipalId())) {
                    user3Request = actionRequest;
                } else if (testuser1.equals(actionRequest.getPrincipalId())) {
                    testuser1Request = actionRequest;
                }
            }
        }
        // now let's ensure we got the requests we wanted
        assertNotNull(user1Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user1Request.getStatus());
        assertNotNull(user2Request);
        assertEquals(ActionRequestStatus.ACTIVATED, user2Request.getStatus());
        assertNotNull(user3Request);
        assertEquals(ActionRequestStatus.INITIALIZED, user3Request.getStatus());
        assertNotNull(testuser1Request);
        assertEquals(ActionRequestStatus.INITIALIZED, testuser1Request.getStatus());

        // check delegate requests on user2Request now
        assertEquals(1, user2Request.getChildRequests().size());
        ActionRequest testuser2DelegateRequest = user2Request.getChildRequests().get(0);
        assertEquals(testuser2, testuser2DelegateRequest.getPrincipalId());
        assertEquals(DelegationType.PRIMARY, testuser2DelegateRequest.getDelegationType());
        assertEquals(ActionRequestStatus.ACTIVATED, testuser2DelegateRequest.getStatus());

        // check delegate requests on testuser1Request now
        assertEquals(2, testuser1Request.getChildRequests().size());
        ActionRequest testWorkgroupRequest = null;
        ActionRequest testuser3Request = null;
        for (ActionRequest childActionRequest : testuser1Request.getChildRequests()) {
            RecipientType recipientType = childActionRequest.getRecipientType();
            if (recipientType == RecipientType.PRINCIPAL) {
                if (testuser3.equals(childActionRequest.getPrincipalId())) {
                    testuser3Request = childActionRequest;
                }
            } else if (recipientType == RecipientType.GROUP) {
                if (testWorkgroup.equals(childActionRequest.getGroupId())) {
                    testWorkgroupRequest = childActionRequest;
                }
            }
        }
        assertNotNull(testWorkgroupRequest);
        assertEquals(ActionRequestStatus.INITIALIZED, testWorkgroupRequest.getStatus());
        assertEquals(DelegationType.SECONDARY, testWorkgroupRequest.getDelegationType());
        assertNotNull(testuser3Request);
        assertEquals(ActionRequestStatus.INITIALIZED, testuser3Request.getStatus());
        assertEquals(DelegationType.PRIMARY, testuser3Request.getDelegationType());

        // whew! now that that's done, let's run through the approvals for this peopleflow
        assertApproveRequested(document, user1, user2, testuser2);
        assertApproveNotRequested(document, testuser1, user3, ewestfal, testuser3);
        // approve as testuser2 who is user2's primary delegate
        document.switchPrincipal(testuser2);
        document.approve("");

        // now approve as user1, this should push it to priority 2 in the peopleflow
        assertApproveRequested(document, user1);
        assertApproveNotRequested(document, user2, testuser2, testuser1, user3, ewestfal, testuser3);
        document.switchPrincipal(user1);
        document.approve("");

        // at this point all priorty2 folks should have approvals
        assertApproveRequested(document, testuser1, user3, ewestfal, testuser3);
        assertApproveNotRequested(document, user1, user2, testuser2);
        // approve as ewestfal, a member of TestWorkgroup which is a delegate of testuser1
        document.switchPrincipal(ewestfal);
        document.approve("");

        // the only remaining approval at this point should be to user3, note that user3 initiated the document, but forceAction should = true by default on peopleflows
        assertApproveRequested(document, user3);
        assertApproveNotRequested(document, user1, user2, testuser2, testuser1, ewestfal, testuser3);
        document.switchPrincipal(user3);
        document.approve("");

        // document should now be FINAL!
        assertTrue(document.isFinal());

    }

    private String roleId = null;
    /**
     * Defines a PeopleFlow with a single first approve role, that role has two members, one principal and one group.
     */
    private void createFirstApproveRolePeopleFlow() {
        RoleService roleService = KimApiServiceLocator.getRoleService();
        Role role = roleService.getRoleByNamespaceCodeAndName("KR-SYS", "Technical Administrator");
        assertNotNull("Technical Administrator role should exist!", role);
        assertEquals(2, roleService.getRoleMembers(Collections.singletonList(role.getId()),
                new HashMap<String, String>()).size());
        roleId = role.getId();

        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_2);
        PeopleFlowMember.Builder memberBuilder = peopleFlow.addRole(role.getId());
        memberBuilder.setPriority(1);
        memberBuilder.setActionRequestPolicy(ActionRequestPolicy.FIRST);
        peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    @Test
    public void test_FirstApproveRolePeopleFlow() throws Exception {
        createFirstApproveRolePeopleFlow();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // should have 1 root requests which is a role request with 2 children, one a principal request to the "admin"
        // prinicipal, and one a group request to the WorkflowAdmin group
        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals(1, rootActionRequests.size());
        ActionRequest roleRequest = rootActionRequests.get(0);
        assertEquals(ActionRequestPolicy.FIRST, roleRequest.getRequestPolicy());
        assertEquals(roleId, roleRequest.getRoleName());
        assertEquals(RecipientType.ROLE, roleRequest.getRecipientType());
        assertEquals(2, roleRequest.getChildRequests().size());
        for (ActionRequest childRequest : roleRequest.getChildRequests()) {
            if (RecipientType.PRINCIPAL.equals(childRequest.getRecipientType())) {
                assertEquals(getPrincipalIdForName("admin"), childRequest.getPrincipalId());
            } else if (RecipientType.GROUP.equals(childRequest.getRecipientType())) {
                assertEquals(getGroupIdForName("KR-WKFLW", "WorkflowAdmin"), childRequest.getGroupId());
            } else {
                fail("Found a child request i didn't expect with a recipient type of: " + childRequest.getRecipientType());
            }
        }

        // should be able to approve as a member of the group on Technical Administrator role (which is WorkflowAdmin) as
        // well as the 'admin' principal
        document.switchPrincipal(getPrincipalNameForId("admin"));
        assertTrue(document.isApprovalRequested());
        document.switchPrincipal(getPrincipalIdForName("bmcgough"));
        assertTrue(document.isApprovalRequested());

        // now approve as a member of WorkflowAdmin
        document.approve("");

        // document should now be final because it's first approve
        assertTrue(document.isFinal());

        // now try it by approving as admin, and ensure that the first approval works as well in that case
        document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());
        document.switchPrincipal(getPrincipalIdForName("bmcgough"));
        assertTrue(document.isApprovalRequested());
        document.switchPrincipal(getPrincipalNameForId("admin"));
        assertTrue(document.isApprovalRequested());
        // now approve as admin
        document.approve("");
        // document should now be final because it's first approve
        assertTrue(document.isFinal());
    }

    /**
     * Defines a PeopleFlow with a single all approve role, that role has two members, one principal and one group.
     */
    private void createAllApproveRolePeopleFlow() {
        RoleService roleService = KimApiServiceLocator.getRoleService();
        Role role = roleService.getRoleByNamespaceCodeAndName("KR-SYS", "Technical Administrator");
        assertNotNull("Technical Administrator role should exist!", role);
        assertEquals(2, roleService.getRoleMembers(Collections.singletonList(role.getId()),
                new HashMap<String, String>()).size());
        roleId = role.getId();

        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, PEOPLE_FLOW_2);
        PeopleFlowMember.Builder memberBuilder = peopleFlow.addRole(role.getId());
        memberBuilder.setPriority(1);
        memberBuilder.setActionRequestPolicy(ActionRequestPolicy.ALL);
        peopleFlowService.createPeopleFlow(peopleFlow.build());
    }

    @Test
    public void test_AllApproveRolePeopleFlow() throws Exception {
        createAllApproveRolePeopleFlow();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user3, SINGLE_PEOPLE_FLOW_PRIORITY_PARALLEL_APPROVE);
        document.route("");
        assertTrue("Document should be enroute.", document.isEnroute());

        // should have 1 root requests which is a role request with 2 children, one a principal request to the "admin"
        // prinicipal, and one a group request to the WorkflowAdmin group
        List<ActionRequest> rootActionRequests = document.getRootActionRequests();
        assertEquals(1, rootActionRequests.size());
        ActionRequest roleRequest = rootActionRequests.get(0);
        assertEquals(ActionRequestPolicy.ALL, roleRequest.getRequestPolicy());
        assertEquals(roleId, roleRequest.getRoleName());
        assertEquals(RecipientType.ROLE, roleRequest.getRecipientType());
        assertEquals(2, roleRequest.getChildRequests().size());
        for (ActionRequest childRequest : roleRequest.getChildRequests()) {
            if (RecipientType.PRINCIPAL.equals(childRequest.getRecipientType())) {
                assertEquals(getPrincipalIdForName("admin"), childRequest.getPrincipalId());
            } else if (RecipientType.GROUP.equals(childRequest.getRecipientType())) {
                assertEquals(getGroupIdForName("KR-WKFLW", "WorkflowAdmin"), childRequest.getGroupId());
            } else {
                fail("Found a child request i didn't expect with a recipient type of: " + childRequest.getRecipientType());
            }
        }

        // should be able to approve as a member of the group on Technical Administrator role (which is WorkflowAdmin) as
        // well as the 'admin' principal
        document.switchPrincipal(getPrincipalNameForId("admin"));
        assertTrue(document.isApprovalRequested());
        document.switchPrincipal(getPrincipalIdForName("bmcgough"));
        assertTrue(document.isApprovalRequested());

        // now approve as a member of WorkflowAdmin
        document.approve("");

        // document should still be enroute because this is an all approve situation and both the group and admin need to approve
        assertTrue(document.isEnroute());
        assertFalse(document.isApprovalRequested());

        document.switchPrincipal(getPrincipalNameForId("admin"));
        assertTrue(document.isApprovalRequested());
        document.approve("");
        // after approving as admin, the last of the two role requests have been completed, document should be final
        assertTrue(document.isFinal());
    }
    
    private void assertApproveRequested(WorkflowDocument document, String... principalIds) {
        for (String principalId : principalIds) {
            document.switchPrincipal(principalId);
            assertTrue("Approve should have been requested for '" + principalId + "'", document.isApprovalRequested());
        }
    }

    private void assertAcknowledgeRequested(WorkflowDocument document, String... principalIds) {
        for (String principalId : principalIds) {
            document.switchPrincipal(principalId);
            assertTrue("Acknowledge should have been requested for '" + principalId + "'", document.isAcknowledgeRequested());
        }
    }

    private void assertApproveNotRequested(WorkflowDocument document, String... principalIds) {
        for (String principalId : principalIds) {
            document.switchPrincipal(principalId);
            assertFalse("Approve should *NOT* have been requested for '" + principalId + "'", document.isApprovalRequested());
        }
    }

}
