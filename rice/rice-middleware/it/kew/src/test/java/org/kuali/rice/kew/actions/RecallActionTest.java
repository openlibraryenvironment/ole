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

import junit.framework.Assert;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.impl.KimDocumentTypeAuthorizer;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.attribute.KimAttribute;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttribute;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RecallActionTest extends KEWTestCase {
    /**
     * test postprocessor for testing afterActionTaken hook
     */
    public static class RecallTestPostProcessor extends DefaultPostProcessor {
        public static ActionType afterActionTakenType;
        public static ActionTakenEvent afterActionTakenEvent;
        @Override
        public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
            afterActionTakenType = performed;
            afterActionTakenEvent = event;
            return super.afterActionTaken(performed, event);
        }
    }

    public static class RecallTestDocumentTypeAuthorizer extends KimDocumentTypeAuthorizer {
        public static String CUSTOM_RECALL_KIM_TYPE_NAME = "Dynamic Type";
        public static String CUSTOM_RECALL_QUALIFIER_NAME = "Dynamic Qualifier";
        public static String CUSTOM_RECALL_QUALIFIER_VALUE = "Dynamic Qualifier Value";
        // we have to use a detail already defined for the recall permission - app doc status seems the most application-controlled
        public static String CUSTOM_RECALL_DETAIL_NAME = KimConstants.AttributeConstants.APP_DOC_STATUS;
        public static String CUSTOM_RECALL_DETAIL_VALUE = "Dynamic Recall Permission Detail Value";

        public static boolean buildPermissionDetailsInvoked = false;
        public static boolean buildRoleQualifiersInvoked= false;

        @Override
        protected Map<String, String> buildDocumentTypePermissionDetails(DocumentType documentType, String documentStatus, String actionRequestedCode, String routeNodeName) {
            buildPermissionDetailsInvoked = true;
            Map<String, String> details = super.buildDocumentTypePermissionDetails(documentType, documentStatus, actionRequestedCode, routeNodeName);
            details.put(CUSTOM_RECALL_DETAIL_NAME, CUSTOM_RECALL_DETAIL_VALUE);
            return details;
        }

        @Override
        protected Map<String, String> buildDocumentRoleQualifiers(DocumentRouteHeaderValue document, String routeNodeName) {
            buildRoleQualifiersInvoked = true;
            Map<String, String> qualifiers = super.buildDocumentRoleQualifiers(document, routeNodeName);
            qualifiers.put(CUSTOM_RECALL_QUALIFIER_NAME, CUSTOM_RECALL_QUALIFIER_VALUE);
            return qualifiers;
        }
    }

    private static final String RECALL_TEST_DOC = "RecallTest";
    private static final String RECALL_TEST_RESTRICTED_DOC = "RecallTestRestricted";
    private static final String RECALL_TEST_NOROUTING_DOC = "RecallTestNoRouting";
    private static final String RECALL_TEST_ONLYADHOC_DOC = "RecallTestOnlyAdhoc";
    private static final String RECALL_NOTIFY_TEST_DOC = "RecallWithPrevNotifyTest";
    private static final String RECALL_NO_PENDING_NOTIFY_TEST_DOC = "RecallWithoutPendingNotifyTest";
    private static final String RECALL_NOTIFY_THIRDPARTY_TEST_DOC = "RecallWithThirdPartyNotifyTest";

    private String EWESTFAL = null;
    private String JHOPF = null;
    private String RKIRKEND = null;
    private String NATJOHNS = null;
    private String BMCGOUGH = null;

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Override
    protected void setUpAfterDataLoad() throws Exception {
        super.setUpAfterDataLoad();
        EWESTFAL = getPrincipalIdForName("ewestfal");
        JHOPF = getPrincipalIdForName("jhopf");
        RKIRKEND = getPrincipalIdForName("rkirkend");
        NATJOHNS = getPrincipalIdForName("natjohns");
        BMCGOUGH = getPrincipalIdForName("bmcgough");

        RecallTestPostProcessor.afterActionTakenType = null;
        RecallTestPostProcessor.afterActionTakenEvent = null;
    }

    protected void assertAfterActionTakenCalled(ActionType performed, ActionType taken) {
        assertEquals(performed, RecallTestPostProcessor.afterActionTakenType);
        assertNotNull(RecallTestPostProcessor.afterActionTakenEvent);
        assertEquals(taken, RecallTestPostProcessor.afterActionTakenEvent.getActionTaken().getActionTaken());
    }

    @Test(expected=InvalidActionTakenException.class) public void testCantRecallUnroutedDoc() {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.recall("recalling", true);
    }

    @Test public void testRecallAsInitiatorBeforeAnyApprovals() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        document.recall("recalling", true);

        assertTrue("Document should be recalled", document.isRecalled());
        assertAfterActionTakenCalled(ActionType.RECALL, ActionType.RECALL);

        //verify that the document is truly dead - no more action requests or action items.

        List requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should not have any active requests", 0, requests.size());

        Collection<ActionItem> actionItems = KEWServiceLocator.getActionListService().findByDocumentId(document.getDocumentId());
        assertEquals("Should not have any action items", 0, actionItems.size());
    }

    @Test public void testRecallValidActionsTaken() throws Exception {
        // just complete
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_RESTRICTED_DOC);
        document.route("routing");
        document.recall("recalling", true);

        // save and complete
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_RESTRICTED_DOC);
        document.saveDocument("saving");
        document.route("routing");
        document.recall("recalling", true);
    }

    @Test
    public void testRecallInvalidActionsTaken() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_RESTRICTED_DOC);
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId());
        document.approve("");

        try {
            document.recall("recalling", true);
            fail("Recall should NOT have succeeded.  Expected InvalidActionTakenException due to invalid 'APROVE' prior action taken.");
        } catch (InvalidActionTakenException iate) {
            assertTrue(iate.getMessage().contains("Invalid prior action taken: 'APPROVE'"));
        }
    }

    @Test public void testRecallOnlyAdhocRouting() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_ONLYADHOC_DOC);
        // adhoc it to someone to prevent doc from going final - final is itself an invalid state for recall
        document.adHocToPrincipal(ActionRequestType.APPROVE, "adhoc approve to JHOPF", JHOPF, "adhocing to prevent finalization", true);
        document.route("routing");
        try {
            document.recall("recalling", true);
            fail("Recall should NOT have succeeded.  Expected InvalidActionTakenException due to absence of non-adhoc route nodes.");
        } catch (InvalidActionTakenException iate) {
            assertTrue(iate.getMessage().contains("No non-adhoc route nodes defined"));
        }
    }

    @Test public void testRecallAsInitiatorAfterSingleApproval() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(EWESTFAL, document.getDocumentId());
        document.recall("recalling", true);

        assertTrue("Document should be recalled", document.isRecalled());
        assertAfterActionTakenCalled(ActionType.RECALL, ActionType.RECALL);

        //verify that the document is truly dead - no more action requests or action items.

        List requests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should not have any active requests", 0, requests.size());

        Collection<ActionItem> actionItems = KEWServiceLocator.getActionListService().findByDocumentId(document.getDocumentId());
        assertEquals("Should not have any action items", 0, actionItems.size());

        // can't recall recalled doc
        assertFalse(document.getValidActions().getValidActions().contains(ActionType.RECALL));
    }

    @Test(expected=InvalidActionTakenException.class)
    public void testRecallInvalidWhenProcessed() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        for (String user: new String[] { JHOPF, EWESTFAL, RKIRKEND, NATJOHNS, BMCGOUGH }) {
            document = WorkflowDocumentFactory.loadDocument(user, document.getDocumentId());
            document.approve("");
        }

        document.refresh();
        assertTrue("Document should be processed", document.isProcessed());
        assertTrue("Document should be approved", document.isApproved());
        assertFalse("Document should not be final", document.isFinal());

        document = WorkflowDocumentFactory.loadDocument(EWESTFAL, document.getDocumentId());
        document.recall("recalling when processed should fail", true);
    }

    @Test(expected=InvalidActionTakenException.class)
    public void testRecallInvalidWhenFinal() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        for (String user: new String[] { JHOPF, EWESTFAL, RKIRKEND, NATJOHNS, BMCGOUGH }) {
            document = WorkflowDocumentFactory.loadDocument(user, document.getDocumentId());
            document.approve("");
        }
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        document.acknowledge("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jthomas"), document.getDocumentId());
        document.fyi();

        for (ActionRequest a: document.getRootActionRequests()) {
            System.err.println(a);
            if (a.isAcknowledgeRequest() || a.isFyiRequest()) {
                System.err.println(a.getPrincipalId());
                System.err.println(KimApiServiceLocator.getIdentityService().getPrincipal(a.getPrincipalId()).getPrincipalName());
            }
        }

        assertFalse("Document should not be processed", document.isProcessed());
        assertTrue("Document should be approved", document.isApproved());
        assertTrue("Document should be final", document.isFinal());

        document = WorkflowDocumentFactory.loadDocument(EWESTFAL, document.getDocumentId());
        document.recall("recalling when processed should fail", true);
    }

    @Test public void testRecallToActionListAsInitiatorBeforeAnyApprovals() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        document.recall("recalling", false);

        assertTrue("Document should be saved", document.isSaved());
        assertEquals(1, document.getCurrentNodeNames().size());
        assertTrue(document.getCurrentNodeNames().contains("AdHoc"));
        assertAfterActionTakenCalled(ActionType.RECALL, ActionType.COMPLETE);

        // initiator has completion request
        assertTrue(document.isCompletionRequested());
        // can't recall saved doc
        assertFalse(document.getValidActions().getValidActions().contains(ActionType.RECALL));

        // first approver has FYI
        assertTrue(WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId()).isFYIRequested());

        document.complete("completing");

        assertTrue("Document should be enroute", document.isEnroute());

        assertTrue(WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId()).isApprovalRequested());
    }

    private static final String PERM_APP_DOC_STATUS = "recallable by admins";
    private static final String ROUTE_NODE = "NotifyFirst";
    private static final String ROUTE_STATUS = "R";

    protected Permission createRecallPermission(String docType, String appDocStatus, String routeNode, String routeStatus) {
        return createPermissionForTemplate(KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION + " for test case", docType, appDocStatus, routeNode, routeStatus);
    }

    protected Permission createRouteDocumentPermission(String docType, String appDocStatus, String routeNode, String routeStatus) {
        return createPermissionForTemplate(KewApiConstants.KEW_NAMESPACE, KewApiConstants.ROUTE_PERMISSION, KewApiConstants.KEW_NAMESPACE, KewApiConstants.ROUTE_PERMISSION + " for test case", docType, appDocStatus, routeNode, routeStatus);
    }

    protected Permission createPermissionForTemplate(String template_ns, String template_name, String permission_ns, String permission_name, String docType, String appDocStatus, String routeNode, String routeStatus) {
        Template permTmpl = KimApiServiceLocator.getPermissionService().findPermTemplateByNamespaceCodeAndName(template_ns, template_name);
        assertNotNull(permTmpl);
        Permission.Builder permission = Permission.Builder.create(permission_ns, permission_name);
        permission.setDescription(permission_name);
        permission.setTemplate(Template.Builder.create(permTmpl));
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, docType);
        attrs.put(KimConstants.AttributeConstants.APP_DOC_STATUS, appDocStatus);
        attrs.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME, routeNode);
        attrs.put(KimConstants.AttributeConstants.ROUTE_STATUS_CODE, routeStatus);
        permission.setActive(true);
        permission.setAttributes(attrs);

        // save the permission and check that's it's wired up correctly
        Permission perm = KimApiServiceLocator.getPermissionService().createPermission(permission.build());
        assertEquals(perm.getTemplate().getId(), permTmpl.getId());
        int num = 1;
        if (appDocStatus != null) num++;
        if (routeNode != null) num++;
        if (routeStatus != null) num++;
        assertEquals(num, perm.getAttributes().size());
        assertEquals(docType, perm.getAttributes().get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME));
        assertEquals(appDocStatus, perm.getAttributes().get(KimConstants.AttributeConstants.APP_DOC_STATUS));
        assertEquals(routeNode, perm.getAttributes().get(KimConstants.AttributeConstants.ROUTE_NODE_NAME));
        assertEquals(routeStatus, perm.getAttributes().get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE));

        return perm;
    }

    // disable the existing Recall Permission assigned to Initiator Role for test purposes
    protected void disableInitiatorRecallPermission() {
        Permission p = KimApiServiceLocator.getPermissionService().findPermByNamespaceCodeAndName("KR-WKFLW", "Recall Document");
        Permission.Builder pb = Permission.Builder.create(p);
        pb.setActive(false);
        KimApiServiceLocator.getPermissionService().updatePermission(pb.build());
    }

    // setter for Kim Priority Parameter (used for useKimPermission method call)
    protected void setKimPriorityOnDocumentTypeParameterValue(String parameterValue) {
        if(CoreFrameworkServiceLocator.getParameterService().parameterExists(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND)) {
            Parameter kimPriorityOverDocTypePolicyParameter = CoreFrameworkServiceLocator.getParameterService().getParameter(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND);
            Parameter.Builder b = Parameter.Builder.create(kimPriorityOverDocTypePolicyParameter);
            b.setValue(parameterValue);
            CoreFrameworkServiceLocator.getParameterService().updateParameter(b.build());
        }
    }

    protected String getKimPriorityOnDocumentTypeParameterValue() {
        if(CoreFrameworkServiceLocator.getParameterService().parameterExists(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND)) {
            return CoreFrameworkServiceLocator.getParameterService().getParameter(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND).getValue();
        }
        return null;
    }


    /**
     * Tests that a new permission can be configured with the Recall Permission template and that matching works correctly
     * against the new permission
     */
    @Test public void testRecallPermissionMatching() {
        disableInitiatorRecallPermission();
        createRecallPermission(RECALL_TEST_DOC, PERM_APP_DOC_STATUS, ROUTE_NODE, ROUTE_STATUS);

        Map<String, String> details = new HashMap<String, String>();
        details.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, RECALL_TEST_DOC);
        details.put(KimConstants.AttributeConstants.APP_DOC_STATUS, PERM_APP_DOC_STATUS);
        details.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME, ROUTE_NODE);
        details.put(KimConstants.AttributeConstants.ROUTE_STATUS_CODE, ROUTE_STATUS);

        // test all single field mismatches
        for (Map.Entry<String, String> entry: details.entrySet()) {
            Map<String, String> testDetails = new HashMap<String, String>(details);
            // change a single detail to a non-matching value
            testDetails.put(entry.getKey(), entry.getValue() + " BOGUS ");
            assertFalse("non-matching " + entry.getKey() + " detail should cause template to not match", KimApiServiceLocator.getPermissionService().isPermissionDefinedByTemplate(KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, testDetails));
        }

        assertTrue("template should match details", KimApiServiceLocator.getPermissionService().isPermissionDefinedByTemplate(KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, details));
    }

    @Test public void testRecallPermissionTemplate() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");

        // nope, technical admins can't recall
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));

        // create a recall permission for the RECALL_TEST_DOC doctype
        Permission perm = createRecallPermission(RECALL_TEST_DOC, PERM_APP_DOC_STATUS, ROUTE_NODE, ROUTE_STATUS);

        // assign the permission to Technical Administrator role
        Role techadmin = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName("KR-SYS", "Technical Administrator");
        KimApiServiceLocator.getRoleService().assignPermissionToRole(perm.getId(), techadmin.getId());

        // our recall permission is assigned to the technical admin role

        // but the doc will not match...
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_NOTIFY_TEST_DOC);
        document.route(PERM_APP_DOC_STATUS);
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));

        // .. the app doc status will not match...
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");
        // technical admins can't recall since the app doc status is not correct
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));

        // ... the node will not match ...
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");
        WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId()).approve(""); // approve past notifyfirstnode
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));

        // ... the doc status will not match (not recallable anyway) ...
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.route("");
        document.cancel("cancelled");
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));

        // everything should match
        document = WorkflowDocumentFactory.createDocument(EWESTFAL, RECALL_TEST_DOC);
        document.setApplicationDocumentStatus(PERM_APP_DOC_STATUS);
        document.route("");
        // now technical admins can recall by virtue of having the recall permission on this doc
        assertTrue(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
        assertTrue(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId()).getValidActions().getValidActions().contains(ActionType.RECALL));
    }

    @Test public void testRecallToActionListAsInitiatorAfterApprovals() throws Exception {
        this.testRecallToActionListAsInitiatorAfterApprovals(RECALL_TEST_DOC);
    }

    @Test public void testRecallToActionListAsInitiatorWithNotificationAfterApprovals() throws Exception {
        this.testRecallToActionListAsInitiatorAfterApprovals(RECALL_NOTIFY_TEST_DOC);
    }

    @Test public void testRecallToActionListAsInitiatorWithoutPendingNotificationAfterApprovals() throws Exception {
        this.testRecallToActionListAsInitiatorAfterApprovals(RECALL_NO_PENDING_NOTIFY_TEST_DOC);
    }

    @Test public void testRecallToActionListAsInitiatorWithThirdPartyNotificationAfterApprovals() throws Exception {
        this.testRecallToActionListAsInitiatorAfterApprovals(RECALL_NOTIFY_THIRDPARTY_TEST_DOC);
    }

    /**
     * Tests that the document is returned to the *recaller*'s action list, not the original initiator
     * @throws Exception
     */
    @Test public void testRecallToActionListAsThirdParty() throws Exception {
        Permission perm = createRecallPermission(RECALL_TEST_DOC, null, null, null);
        // assign the permission to Technical Administrator role
        Role techadmin = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName("KR-SYS", "Technical Administrator");
        KimApiServiceLocator.getRoleService().assignPermissionToRole(perm.getId(), techadmin.getId());
        // recall as 'admin' user
        testRecallToActionListAfterApprovals(EWESTFAL, getPrincipalIdForName("admin"), RECALL_TEST_DOC);
    }

    // the three tests below test permutations of recall permission and derived role assignment
    protected void assignRoutePermissionToTechAdmin() {
        // assign Route Document permission to the Technical Administrator role
        Permission routePerm = createRouteDocumentPermission(RECALL_TEST_DOC, null, null, null);
        Role techadmin = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName("KR-SYS", "Technical Administrator");
        KimApiServiceLocator.getRoleService().assignPermissionToRole(routePerm.getId(), techadmin.getId());
    }
    protected void assignRecallPermissionToDocumentRouters() {
        // assign Recall permission to the Document Router derived role
        Permission recallPerm = createRecallPermission(RECALL_TEST_DOC, null, null, null);
        Role documentRouterDerivedRole = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName("KR-WKFLW", "Document Router");
        KimApiServiceLocator.getRoleService().assignPermissionToRole(recallPerm.getId(), documentRouterDerivedRole.getId());
    }
    /**
     * Tests that simply assigning the Route Document permission to the Technical Admin role *without* assigning the
     * Recall permission to the Document Router derived role, is NOT sufficient to enable recall.
     */
    @Test public void testRoutePermissionAssignmentInsufficientForRouterToRecallDoc() throws Exception {
        assignRoutePermissionToTechAdmin();
        // recall as 'admin' (Tech Admin) user
        testRecallToActionListAfterApprovals(EWESTFAL, getPrincipalIdForName("admin"), RECALL_TEST_DOC, false);
    }
    /**
     * Tests that simply assigning the recall permission to the Document Router derived role *without* assigning the
     * Route Document permission to the Technical Admin role, is NOT sufficient to enable recall.
     */
    @Test public void testRecallPermissionAssignmentInsufficientForRouterToRecallDoc() throws Exception {
        assignRecallPermissionToDocumentRouters();
        // recall as 'admin' (Tech Admin) user
        testRecallToActionListAfterApprovals(EWESTFAL, getPrincipalIdForName("admin"), RECALL_TEST_DOC, false);
    }
    /**
     * Tests that we can use the Route Document derived role to assign Recall permission to document routers.
     */
    @Test public void testRecallToActionListAsRouterDerivedRole() throws Exception {
        // assign both! derived role works its magic
        assignRoutePermissionToTechAdmin();
        assignRecallPermissionToDocumentRouters();
        // recall as 'admin' user (Tech Admin) user
        testRecallToActionListAfterApprovals(EWESTFAL, getPrincipalIdForName("admin"), RECALL_TEST_DOC);
    }

    /**
     * Creates a new role with recall permission qualified with doc type and custom app doc status
     * @param ns role namespace
     * @param name role name
     * @param recallPerm the pre-created Recall permission
     * @return the new recall-capable Role
     */
    protected Role createRoleWithRecallPermission(String ns, String name, Permission recallPerm, String kimTypeName, String roleQualifierName) {
        // create a custom attribute for role qualification
        KimAttribute.Builder attribute = KimAttribute.Builder.create("org.kuali.rice.kim.bo.impl.KimAttributes", roleQualifierName, "KR-SYS");
        attribute.setAttributeLabel(roleQualifierName);
        attribute.setActive(true);
        KimAttributeBo customAttribute = KRADServiceLocator.getBusinessObjectService().save(KimAttributeBo.from(attribute.build()));

        // create a custom kim type for the custom attribute
        KimType.Builder kimType = KimType.Builder.create();
        kimType.setNamespaceCode("KR-SYS");
        kimType.setName(kimTypeName);
        kimType.setActive(true);
        KimTypeAttribute.Builder kimTypeAttribute = KimTypeAttribute.Builder.create();
        kimTypeAttribute.setKimAttribute(KimAttribute.Builder.create(customAttribute));
        kimTypeAttribute.setActive(true);
        kimType.setAttributeDefinitions(Collections.singletonList(kimTypeAttribute));
        KimTypeBo customKimType = KRADServiceLocator.getBusinessObjectService().save(KimTypeBo.from(kimType.build()));

        // create a new role
        Role.Builder role = Role.Builder.create();
        role.setActive(true);
        role.setDescription("RecallTest custom recall role");
        role.setName(ns);
        role.setNamespaceCode(name);
        role.setKimTypeId(customKimType.getId());
        Role customRole = KimApiServiceLocator.getRoleService().createRole(role.build());

        KimApiServiceLocator.getRoleService().assignPermissionToRole(recallPerm.getId(), customRole.getId());

        List<String> recallCapableRoleIds = KimApiServiceLocator.getPermissionService().getRoleIdsForPermission(recallPerm.getNamespaceCode(), recallPerm.getName());
        Assert.assertFalse("No recall-capable roles found", recallCapableRoleIds.isEmpty());
        Assert.assertTrue("New role is not associated with Recall permission", recallCapableRoleIds.contains(customRole.getId()));

        return customRole;
    }

    /**
     * Assigns user to role with single qualification
     * @param principalId the principal to assign to role
     * @param role the role object
     * @param roleQualifierName the role qualifier name
     * @param roleQualifierValue the role qualifier value
     */
    protected void assignUserQualifiedRole(String principalId, Role role, String roleQualifierName, String roleQualifierValue) {
        // assign user to role triggered by dynamic, custom role qualifications
        Map<String, String> qualifications = new HashMap<String, String>();
        qualifications.put(roleQualifierName, roleQualifierValue);
        KimApiServiceLocator.getRoleService().assignPrincipalToRole(getPrincipalIdForName("arh14"), role.getNamespaceCode(), role.getName(), qualifications);

        Collection<String> ids = KimApiServiceLocator.getRoleService().getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), qualifications);
        Assert.assertTrue("Qualified role assignment failed", ids.contains(principalId));
    }

    /**
     * Tests that an application can customize document type routing authorization via documenttypeauthorizer
     */
    @Test public void testRecallWithCustomDocumentTypeAuthorizer() throws Exception {
        // arh14 is not associated with our doc routing, will be authorized by custom documenttypeauthorizer
        final String ARH14 = getPrincipalIdForName("arh14");

        // remove existing initiator recall permission
        disableInitiatorRecallPermission();

        RecallTestDocumentTypeAuthorizer.buildPermissionDetailsInvoked = false;
        RecallTestDocumentTypeAuthorizer.buildRoleQualifiersInvoked = false;

        // confirm arh14 can't recall doc
        testRecallToActionListAfterApprovals(EWESTFAL, getPrincipalIdForName("arh14"), RECALL_TEST_DOC, false);

        final String RECALL_ROLE_NM = "CustomRecall";
        final String RECALL_ROLE_NS = "KR-SYS";

        // assign permission triggered by dynamic, custom permission details
        Permission recallPerm = createRecallPermission(RECALL_TEST_DOC, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_VALUE, null, null);
        Role recallRole = createRoleWithRecallPermission(RECALL_ROLE_NM, RECALL_ROLE_NS, recallPerm, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_KIM_TYPE_NAME, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_NAME);
        assignUserQualifiedRole(ARH14, recallRole, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_NAME, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_VALUE);

        Map<String, String> d = new HashMap<String, String>();
        d.put(RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_NAME, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_VALUE);
        d.put(KewApiConstants.DOCUMENT_TYPE_NAME_DETAIL, RECALL_TEST_DOC);
        d.put(KewApiConstants.ROUTE_NODE_NAME_DETAIL, ROUTE_NODE);
        d.put(KewApiConstants.DOCUMENT_STATUS_DETAIL, ROUTE_STATUS);
        Map<String, String> q = new HashMap<String, String>();
        q.put(RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_NAME, RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_VALUE);
        // test that arh14 has recall permission via new recall role with proper qualifications
        List<Permission> permissions = KimApiServiceLocator.getPermissionService().getAuthorizedPermissionsByTemplate(ARH14, KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, d, q);
        Assert.assertEquals(1, permissions.size());
        Assert.assertEquals(recallPerm.getId(), permissions.get(0).getId());

        // verify that arh14 *still* can't recall doc - we have to set the custom documenttypeauthorizer first
        testRecallToActionListAfterApprovals(EWESTFAL, ARH14, RECALL_TEST_DOC, false);

        // now update the doctype with custom documenttype authorizer
        org.kuali.rice.kew.api.doctype.DocumentType dt = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(RECALL_TEST_DOC);
        org.kuali.rice.kew.api.doctype.DocumentType.Builder b = org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(dt);
        b.setAuthorizer(RecallTestDocumentTypeAuthorizer.class.getName());

        KEWServiceLocator.getDocumentTypeService().save(DocumentType.from(b));

        Assert.assertEquals(RecallTestDocumentTypeAuthorizer.class.getName(), KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(RECALL_TEST_DOC).getAuthorizer());

        // custom documenttypeauthorizer has not been invoked yet
        Assert.assertFalse(RecallTestDocumentTypeAuthorizer.buildPermissionDetailsInvoked);
        Assert.assertFalse(RecallTestDocumentTypeAuthorizer.buildRoleQualifiersInvoked);

        // arh14 should *now* be able to recall!
        testRecallToActionListAfterApprovals(EWESTFAL, ARH14, RECALL_TEST_DOC);

        Assert.assertTrue(RecallTestDocumentTypeAuthorizer.buildPermissionDetailsInvoked);
        Assert.assertTrue(RecallTestDocumentTypeAuthorizer.buildRoleQualifiersInvoked);

        // final counter tests - change the actual dynamic values to ensure match fails
        String orig = RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_VALUE;
        try {
            RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_VALUE = "I will not match";
            testRecallToActionListAfterApprovals(EWESTFAL, ARH14, RECALL_TEST_DOC, false);
        } finally {
            RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_QUALIFIER_VALUE = orig;
        }

        orig = RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_VALUE;
        try {
            RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_VALUE = "I won't match either";
            testRecallToActionListAfterApprovals(EWESTFAL, ARH14, RECALL_TEST_DOC, false);
        } finally {
            RecallTestDocumentTypeAuthorizer.CUSTOM_RECALL_DETAIL_VALUE = orig;
        }
    }

    protected void testRecallToActionListAsInitiatorAfterApprovals(String doctype) {
        testRecallToActionListAfterApprovals(EWESTFAL, EWESTFAL, doctype);
    }

    // Implements various permutations of recalls - with and without doctype policies/notifications of various sorts
    // and as initiator or a third party recaller
    protected void testRecallToActionListAfterApprovals(String initiator, String recaller, String doctype) {
        testRecallToActionListAfterApprovals(initiator, recaller, doctype, true);
    }
    protected void testRecallToActionListAfterApprovals(String initiator, String recaller, String doctype, boolean expect_recall_success) {
        boolean notifyPreviousRecipients = !RECALL_TEST_DOC.equals(doctype);
        boolean notifyPendingRecipients = !RECALL_NO_PENDING_NOTIFY_TEST_DOC.equals(doctype);
        String[] thirdPartiesNotified = RECALL_NOTIFY_THIRDPARTY_TEST_DOC.equals(doctype) ? new String[] { "quickstart", "admin" } : new String[] {};
        
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(initiator, doctype);
        document.route("");

        WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(initiator, document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(RKIRKEND, document.getDocumentId()).approve("");

        document = WorkflowDocumentFactory.loadDocument(recaller, document.getDocumentId());
        System.err.println(document.getValidActions().getValidActions());
        if (expect_recall_success) {
            assertTrue("recaller '" + recaller + "' should be able to RECALL", document.getValidActions().getValidActions().contains(ActionType.RECALL));
        } else {
            assertFalse("recaller '" + recaller + "' should NOT be able to RECALL", document.getValidActions().getValidActions().contains(ActionType.RECALL));
            return;
        }
        document.recall("recalling", false);

        assertTrue("Document should be saved", document.isSaved());
        assertAfterActionTakenCalled(ActionType.RECALL, ActionType.COMPLETE);

        // the recaller has a completion request
        assertTrue(document.isCompletionRequested());
        
        // pending approver has FYI
        assertEquals(notifyPendingRecipients, WorkflowDocumentFactory.loadDocument(NATJOHNS, document.getDocumentId()).isFYIRequested());
        // third approver has FYI
        assertEquals(notifyPreviousRecipients, WorkflowDocumentFactory.loadDocument(RKIRKEND, document.getDocumentId()).isFYIRequested());
        // second approver does not have FYI - approver is initiator, FYI is skipped
        assertFalse(WorkflowDocumentFactory.loadDocument(initiator, document.getDocumentId()).isFYIRequested());
        // first approver has FYI
        assertEquals(notifyPreviousRecipients, WorkflowDocumentFactory.loadDocument(JHOPF, document.getDocumentId()).isFYIRequested());

        if (!ArrayUtils.isEmpty(thirdPartiesNotified)) {
            for (String recipient: thirdPartiesNotified) {
                assertTrue("Expected FYI to be sent to: " + recipient, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName(recipient), document.getDocumentId()).isFYIRequested());
            }
        }
        
        // omit JHOPF, and see if FYI is subsumed by approval request
        for (String user: new String[] { RKIRKEND, NATJOHNS }) {
            WorkflowDocumentFactory.loadDocument(user, document.getDocumentId()).fyi();
        }

        document.complete("completing");

        assertTrue("Document should be enroute", document.isEnroute());

        // generation of approval requests nullify FYIs (?)
        // if JHOPF had an FYI, he doesn't any longer
        for (String user: new String[] { JHOPF, RKIRKEND, NATJOHNS }) {
            document = WorkflowDocumentFactory.loadDocument(user, document.getDocumentId());
            assertFalse(getPrincipalNameForId(user) + " should not have an FYI", document.isFYIRequested());
        }

        // submit all approvals
        for (String user: new String[] { JHOPF, initiator, RKIRKEND, NATJOHNS, BMCGOUGH }) {
            document = WorkflowDocumentFactory.loadDocument(user, document.getDocumentId());
            assertTrue(getPrincipalNameForId(user) + " should have approval request", document.isApprovalRequested());
            document.approve("approving");
        }

        // 2 acks outstanding, we're PROCESSED
        assertTrue("Document should be processed", document.isProcessed());
        assertTrue("Document should be approved", document.isApproved());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        document.acknowledge("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jthomas"), document.getDocumentId());
        document.fyi();

        assertTrue("Document should be approved", document.isApproved());
        assertTrue("Document should be final", document.isFinal());
    }
}
