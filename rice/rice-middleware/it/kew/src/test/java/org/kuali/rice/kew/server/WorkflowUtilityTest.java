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
package org.kuali.rice.kew.server;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.RoutingReportActionToTake;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.actionlist.ActionListService;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.rule.Rule;
import org.kuali.rice.kew.api.rule.RuleReportCriteria;
import org.kuali.rice.kew.api.rule.RuleResponsibility;
import org.kuali.rice.kew.api.rule.RuleService;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeDateTime;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeFloat;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeLong;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeString;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.test.BaselineTestCase;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class WorkflowUtilityTest extends KEWTestCase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WorkflowUtilityTest.class);

	protected void loadTestData() throws Exception {
        loadXmlFile("WorkflowUtilityConfig.xml");
    }

	@Test
    public void testGetDocumentDetailByAppId() throws WorkflowException{
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.setApplicationDocumentId("123456789");
        document.route("");
        WorkflowDocumentService documentService = KewApiServiceLocator.getWorkflowDocumentService();
        DocumentDetail doc= documentService.getDocumentDetailByAppId(SeqSetup.DOCUMENT_TYPE_NAME, "123456789");
    	
    	assertNotNull(doc);
    	
        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.setApplicationDocumentId("123456789");
        document.route("");

        try{
        	documentService.getDocumentDetailByAppId(SeqSetup.DOCUMENT_TYPE_NAME, "123456789");
        	assertTrue(false);
        }catch(RiceIllegalStateException e){
        	assertTrue(true);
        }
        
        try{
        	documentService.getDocumentDetailByAppId("notExist", "wrong");
        	assertTrue(false);
        }catch(RiceIllegalStateException e){
        	assertTrue(true);
        }
        
        try{
        	documentService.getDocumentDetailByAppId("notExist", null);
        	assertTrue(false);
        }catch(RiceIllegalArgumentException e){
        	assertTrue(true);
        }
    	
        try{
        	documentService.getDocumentDetailByAppId(null, null);
        	assertTrue(false);
        }catch(RiceIllegalArgumentException e){
        	assertTrue(true);
        }
        
    }

    @Test public void testGetActionsRequested() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.route("");
        assertActionsRequested("ewestfal", document.getDocumentId(), false);
        assertActionsRequested("bmcgough", document.getDocumentId(), true);
        assertActionsRequested("rkirkend", document.getDocumentId(), true);
    }

    protected void assertActionsRequested(String principalName, String documentId, boolean shouldHaveRequest) throws Exception {
    	RequestedActions attrSet = KewApiServiceLocator.getWorkflowDocumentActionsService().
                determineRequestedActions(documentId, getPrincipalIdForName(principalName));
    	assertNotNull("Actions requested should be populated", attrSet);
        if (shouldHaveRequest) {
    	    assertTrue("Actions requested should be populated with at least one entry", !attrSet.getRequestedActions().isEmpty());
        } else {
            assertTrue("Principal should have no requests", attrSet.getRequestedActions().isEmpty());
        }
    }

    @Test
    public void testIsUserInRouteLog() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.route("");
        assertTrue(document.isEnroute());
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("ewestfal"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("temay"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("temay"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));

        // test that we can run isUserInRouteLog on a SAVED document
        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.saveDocument("");
        assertTrue(document.isSaved());
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("ewestfal"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("temay"), false));
        assertFalse("User should not be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), false));

        // now look all up in the future of this saved document
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("temay"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));
    }

    @Test public void testIsUserInRouteLogAfterReturnToPrevious() throws Exception {
	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.route("");
        assertTrue(document.isEnroute());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());

        // bmcgough and rkirkend should be in route log
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertFalse("User should NOT be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        // Phil of the future
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));
        TestUtilities.assertAtNode(document, "WorkflowDocument");

        document.returnToPreviousNode("", "AdHoc");
        TestUtilities.assertAtNode(document, "AdHoc");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());

        document.approve("");

        // we should be back where we were
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        TestUtilities.assertAtNode(document, "WorkflowDocument");

        // now verify that is route log authenticated still works
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertFalse("User should NOT be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));

        // let's look at the revoked node instances

        List revokedNodeInstances = KEWServiceLocator.getRouteNodeService().getRevokedNodeInstances(KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId()));
        assertNotNull(revokedNodeInstances);
        assertEquals(2, revokedNodeInstances.size());

        // let's approve past this node and another
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        document.approve("");

        // should be at WorkflowDocument2
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        TestUtilities.assertAtNode(document, "WorkflowDocument2");
        assertTrue(document.isApprovalRequested());
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));

        // now return back to WorkflowDocument
        document.returnToPreviousNode("", "WorkflowDocument");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        // Phil should no longer be non-future route log authenticated
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertFalse("User should NOT be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), false));
        assertTrue("User should be authenticated.", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("pmckown"), true));
    }
    
    @Test
    public void testIsUserInRouteLogWithSplits() throws Exception {
    	loadXmlFile("WorkflowUtilitySplitConfig.xml");
    	
    	// initialize the split node to both branches
    	TestSplitNode.setLeftBranch(true);
    	TestSplitNode.setRightBranch(true);
    	
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("admin"), "UserInRouteLog_Split");
        document.route("");
        
        // document should be in ewestfal action list
        document = TestUtilities.switchByPrincipalName("ewestfal", document);
        assertTrue("should have approve", document.isApprovalRequested());
        TestUtilities.assertAtNode(document, "BeforeSplit");
        
        // now let's run some simulations
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("ewestfal"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("natjohns"), true));
        assertFalse("should NOT be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("user1"), true));
        
        // now let's activate only the left branch and make sure the split is properly executed
        TestSplitNode.setRightBranch(false);
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertFalse("should NOT be in route log because right branch is not active", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("natjohns"), true));
        
        // now let's do a flattened evaluation, it should hit both branches
        assertTrue("should be in route log", wdas.isUserInRouteLogWithOptionalFlattening(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true, true));
        assertTrue("should be in route log", wdas.isUserInRouteLogWithOptionalFlattening(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true, true));
        assertTrue("should be in route log because we've flattened nodes", wdas.isUserInRouteLogWithOptionalFlattening(document.getDocumentId(), getPrincipalIdForName("jhopf"), true, true));
        assertTrue("should be in route log", wdas.isUserInRouteLogWithOptionalFlattening(document.getDocumentId(), getPrincipalIdForName("natjohns"), true, true));
        
        // now let's switch to the right branch
        TestSplitNode.setRightBranch(true);
        TestSplitNode.setLeftBranch(false);
        
        assertFalse("should NOT be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertFalse("should NOT be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("natjohns"), true));

        // now let's switch back to the left branch and approve it
        TestSplitNode.setLeftBranch(true);
        TestSplitNode.setRightBranch(false);
        
        // now let's approve it so that we're inside the right branch of the split
        document.approve("");
        // shoudl be at SplitLeft1 node
        TestUtilities.assertAtNode(document, "SplitLeft1");
        
        document = TestUtilities.switchByPrincipalName("rkirkend", document);
        assertTrue("should have an approve request", document.isApprovalRequested());
        
        // now let's run the simulation so we can test running from inside a split branch
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("rkirkend"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("bmcgough"), true));
        assertFalse("should NOT be in route log because right branch is not active", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("jhopf"), true));
        assertTrue("should be in route log", wdas.isUserInRouteLog(document.getDocumentId(), getPrincipalIdForName("natjohns"), true));
    }
    
    public abstract interface ReportCriteriaGenerator { public abstract RoutingReportCriteria buildCriteria(WorkflowDocument workflowDoc) throws Exception; public boolean isCriteriaRouteHeaderBased();}

    private class ReportCriteriaGeneratorUsingXML implements ReportCriteriaGenerator {
        public RoutingReportCriteria buildCriteria(WorkflowDocument workflowDoc) throws Exception {
            RoutingReportCriteria.Builder criteria = RoutingReportCriteria.Builder.createByDocumentTypeName(
                    workflowDoc.getDocumentTypeName());
            criteria.setXmlContent(workflowDoc.getDocumentContent().getApplicationContent());
            return criteria.build();
        }
        public boolean isCriteriaRouteHeaderBased() {
            return false;
        }
    }

    private class ReportCriteriaGeneratorUsingDocumentId implements ReportCriteriaGenerator {
        public RoutingReportCriteria buildCriteria(WorkflowDocument workflowDoc) throws Exception {
            RoutingReportCriteria.Builder criteria = RoutingReportCriteria.Builder.createByDocumentId(workflowDoc.getDocumentId());
            return criteria.build();
        }
        public boolean isCriteriaRouteHeaderBased() {
            return true;
        }
    }

    @Test public void testDocumentWillHaveApproveOrCompleteRequestAtNode_DocumentId() throws Exception {
        runDocumentWillHaveApproveOrCompleteRequestAtNode(SeqSetup.DOCUMENT_TYPE_NAME,new ReportCriteriaGeneratorUsingDocumentId());
    }

    @Test public void testDocumentWillHaveApproveOrCompleteRequestAtNode_XmlContent() throws Exception {
        runDocumentWillHaveApproveOrCompleteRequestAtNode(SeqSetup.DOCUMENT_TYPE_NAME,new ReportCriteriaGeneratorUsingXML());
    }

    @Test public void testDocumentWillHaveApproveOrCompleteRequestAtNode_ForceAction_DocumentId() throws Exception {
        runDocumentWillHaveApproveOrCompleteRequestAtNode_ForceAction("SimulationTestDocumenType_ForceAction",new ReportCriteriaGeneratorUsingDocumentId());
    }

    @Test public void testDocumentWillHaveApproveOrCompleteRequestAtNode_ForceAction_XmlContent() throws Exception {
        runDocumentWillHaveApproveOrCompleteRequestAtNode_ForceAction("SimulationTestDocumenType_ForceAction",new ReportCriteriaGeneratorUsingXML());
    }

    private void runDocumentWillHaveApproveOrCompleteRequestAtNode_ForceAction(String documentType, ReportCriteriaGenerator generator) throws Exception {
      /*
        name="WorkflowDocument"
          -  rkirkend - Approve - false
        name="WorkflowDocument2"
          -  rkirkend - Approve - false
        name="WorkflowDocument3"
          -  rkirkend - Approve - true
        name="WorkflowDocument4"
          -  rkirkend - Approve - false
          -  jitrue   - Approve - true
      */
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();

        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), documentType);
        RoutingReportCriteria.Builder builder = RoutingReportCriteria.Builder.createByDocumentId(doc.getDocumentId());
        builder.setXmlContent(doc.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument2");
        builder.setRoutingPrincipalId(getPrincipalIdForName("bmcgough"));
        assertTrue("Document should have at least one unfulfilled approve/complete request",wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));
        builder.setTargetPrincipalIds(Collections.singletonList(getPrincipalIdForName("bmcgough")));
        assertFalse("Document should not have any unfulfilled approve/complete requests",wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(doc.getDocumentId());
        builder.setXmlContent(doc.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument4");
        builder.setRoutingPrincipalId(getPrincipalIdForName("bmcgough"));
        List<RoutingReportActionToTake.Builder> actionsToTake = new ArrayList<RoutingReportActionToTake.Builder>();
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("rkirkend"),"WorkflowDocument3"));
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,
                getPrincipalIdForName("jitrue"), "WorkflowDocument4"));

        builder.setActionsToTake(actionsToTake);
        assertFalse("Document should not have any unfulfilled approve/complete requests",
                wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(
                        new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,
                                KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(doc.getDocumentId());
        builder.setXmlContent(doc.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument4");
        actionsToTake = new ArrayList<RoutingReportActionToTake.Builder>();
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("rkirkend"),"WorkflowDocument3"));
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("jitrue"),"WorkflowDocument4"));
        builder.setActionsToTake(actionsToTake);
        assertFalse("Document should not have any unfulfilled approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), documentType);
        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setRoutingPrincipalId(getPrincipalIdForName("rkirkend"));
        builder.setTargetNodeName("WorkflowDocument");
        assertFalse("Document should not have any approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setRoutingPrincipalId(getPrincipalIdForName("rkirkend"));
        builder.setTargetNodeName("WorkflowDocument2");
        assertFalse("Document should not have any approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setRoutingPrincipalId(getPrincipalIdForName("rkirkend"));
        builder.setTargetPrincipalIds(Collections.singletonList(getPrincipalIdForName("rkirkend")));
        assertFalse("Document should not have any approve/complete requests for user rkirkend", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        document.route("");
        assertEquals("Document should be enroute", DocumentStatus.ENROUTE, document.getStatus());
        assertEquals("Document route node is incorrect", "WorkflowDocument3", document.getNodeNames().iterator().next());
        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument4");
        assertTrue("At least one unfulfilled approve/complete request should have been generated", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetPrincipalIds(Collections.singletonList(getPrincipalIdForName("rkirkend")));
        assertTrue("At least one unfulfilled approve/complete request should have been generated for rkirkend", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument4");
        assertTrue("At least one unfulfilled approve/complete request should have been generated", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        // if rkirkend approvers the document here it will move to last route node and no more simulations need to be run
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        document.approve("");
        assertEquals("Document should be enroute", DocumentStatus.ENROUTE, document.getStatus());
        assertEquals("Document route node is incorrect", "WorkflowDocument4", document.getNodeNames().iterator().next());
    }

    private void runDocumentWillHaveApproveOrCompleteRequestAtNode(String documentType,ReportCriteriaGenerator generator) throws Exception {
      /*
        name="WorkflowDocument"
          -  bmcgough - Approve - false
          -  rkirkend - Approve - false
        name="WorkflowDocument2"
          -  pmckown - Approve - false
        name="Acknowledge1"
          -  temay - Ack - false
        name="Acknowledge2"
          -  jhopf - Ack - false
      */
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), documentType);
        RoutingReportCriteria.Builder builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument2");
        builder.setRoutingPrincipalId(getPrincipalIdForName("bmcgough"));
        assertTrue("Document should have one unfulfilled approve/complete request", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));
        builder.setTargetPrincipalIds(Collections.singletonList(getPrincipalIdForName("bmcgough")));
        assertFalse("Document should not have any unfulfilled approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument2");
        builder.setRoutingPrincipalId(getPrincipalIdForName("bmcgough"));
        List<RoutingReportActionToTake.Builder> actionsToTake = new ArrayList<RoutingReportActionToTake.Builder>();
//        actionsToTake[0] = new ReportActionToTakeDTO(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("rkirkend"),"WorkflowDocument");
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("pmckown"),"WorkflowDocument2"));
        builder.setActionsToTake(actionsToTake);
        assertFalse("Document should not have any unfulfilled approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument2");
        actionsToTake = new ArrayList<RoutingReportActionToTake.Builder>();
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("bmcgough"),"WorkflowDocument"));
        actionsToTake.add(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD,getPrincipalIdForName("rkirkend"),"WorkflowDocument"));
        builder.setActionsToTake(actionsToTake);
        builder.setRoutingPrincipalId(getPrincipalIdForName("pmckown"));
        assertFalse("Document should not have any unfulfilled approve/complete requests", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), documentType);
        document.route("");
        assertTrue(document.isEnroute());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        document.approve("");

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("WorkflowDocument2");
        assertTrue("Document should have one unfulfilled approve/complete request", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        document.approve("");
        assertTrue(document.isProcessed());

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("Acknowledge1");
        assertFalse("Document should not have any unfulfilled approve/complete requests when in processed status", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}), false));

        builder = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        builder.setXmlContent(document.getDocumentContent().getApplicationContent());
        builder.setTargetNodeName("Acknowledge1");
        assertTrue("Document should have one unfulfilled Ack request when in final status", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ}), false));
        if (generator.isCriteriaRouteHeaderBased()) {
            assertFalse("Document should have no unfulfilled Ack request generated when in final status", wdas.documentWillHaveAtLeastOneActionRequest(builder.build(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ}), true));
        }

        // if temay acknowledges the document here it will move to processed and no more simulations would need to be tested
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        document.acknowledge("");
        assertTrue(document.isProcessed());
    }

    @Test public void testIsLastApprover() throws Exception {
        // test the is last approver in route level against our sequential document type
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.saveDocumentData();

        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();

        // the initial "route level" should have no requests initially so it should return false
        assertFalse("Should not be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.ADHOC_NODE));

        // app specific route a request to a workgroup at the initial node (TestWorkgroup)
		String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "TestWorkgroup");
        document.adHocToGroup(ActionRequestType.APPROVE, "AdHoc", "", groupId, "", false);

        assertTrue("Should be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.ADHOC_NODE));

        // app specific route a request to a member of the workgroup (jitrue)
        document.adHocToPrincipal(ActionRequestType.APPROVE, "AdHoc", "", getPrincipalIdForName("jitrue"), "", false);
        // member of the workgroup with the user request should be last approver
        assertTrue("Should be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("jitrue"), SeqSetup.ADHOC_NODE));
        // other members of the workgroup will not be last approvers because they don't satisfy the individuals request (ewestfal)
        assertFalse("Should not be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.ADHOC_NODE));

        // route the document, should stay at the adhoc node until those requests have been completed
        document.route("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jitrue"), document.getDocumentId());
        assertEquals("Document should be at adhoc node.", SeqSetup.ADHOC_NODE, document.getNodeNames().iterator().next());
        assertTrue("Approve should be requested.", document.isApprovalRequested());
        document.approve("");

        // document should now be at the WorkflowDocument node with a request to bmcgough and rkirkend
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Approve should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Approve should be requested.", document.isApprovalRequested());
        // since there are two requests, neither should be last approver
        assertFalse("Should not be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("bmcgough"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Should not be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("rkirkend"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        document.approve("");

        // request to rirkend has been satisfied, now request to bmcgough is only request remaining at level so he should be last approver
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Approve should be requested.", document.isApprovalRequested());
        assertTrue("Should be last approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("bmcgough"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        document.approve("");

    }

    /**
     * This method tests how the isLastApproverAtNode method deals with force action requests, there is an app constant
     * with the value specified in KewApiConstants.IS_LAST_APPROVER_ACTIVATE_FIRST which dictates whether or not to simulate
     * activation of initialized requests before running the method.
     *
     * Tests the fix to issue http://fms.dfa.cornell.edu:8080/browse/KULWF-366
     */
    @Test public void testIsLastApproverActivation() throws Exception {
        // first test without the parameter set
        Parameter lastApproverActivateParameter = CoreFrameworkServiceLocator.getParameterService().getParameter(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.FEATURE_DETAIL_TYPE, KewApiConstants.IS_LAST_APPROVER_ACTIVATE_FIRST_IND);
        assertNotNull("last approver parameter should exist.", lastApproverActivateParameter);
        assertTrue("initial parameter value should be null or empty.", StringUtils.isBlank(lastApproverActivateParameter.getValue()));
        String originalParameterValue = lastApproverActivateParameter.getValue();
        
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.LAST_APPROVER_DOCUMENT_TYPE_NAME);
        document.route("");

        // at the first node (WorkflowDocument) we should have a request to rkirkend, bmcgough and to ewestfal with forceAction=true,
        assertEquals("We should be at the WorkflowDocument node.", SeqSetup.WORKFLOW_DOCUMENT_NODE, document.getNodeNames().iterator().next());
        assertFalse("ewestfal should have not have approve because it's initiated", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertFalse("rkirkend should not have approve because it's initiated", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("bmcgough should have approve", document.isApprovalRequested());
        List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should be 3 pending requests.", 3, actionRequests.size());
        // the requests to bmcgough should be activated, the request to rkirkend should be initialized,
        // and the request to ewestfal should be initialized and forceAction=true
        boolean foundBmcgoughRequest = false;
        boolean foundRkirkendRequest = false;
        boolean foundEwestfalRequest = false;
        for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
            String netId = getPrincipalNameForId(actionRequest.getPrincipalId());
            if ("bmcgough".equals(netId)) {
                assertTrue("Request to bmcgough should be activated.", actionRequest.isActive());
                foundBmcgoughRequest = true;
            } else if ("rkirkend".equals(netId)) {
                assertTrue("Request to rkirkend should be initialized.", actionRequest.isInitialized());
                foundRkirkendRequest = true;
            } else if ("ewestfal".equals(netId)) {
                assertTrue("Request to ewestfal should be initialized.", actionRequest.isInitialized());
                assertTrue("Request to ewestfal should be forceAction.", actionRequest.getForceAction().booleanValue());
                foundEwestfalRequest = true;
            }
        }
        assertTrue("Did not find request to bmcgough.", foundBmcgoughRequest);
        assertTrue("Did not find request to rkirkend.", foundRkirkendRequest);
        assertTrue("Did not find request to ewestfal.", foundEwestfalRequest);

        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        // at this point, neither bmcgough, rkirkend nor ewestfal should be the last approver
        assertFalse("Bmcgough should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("bmcgough"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Rkirkend should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("rkirkend"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // approve as bmcgough
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");

        // still, neither rkirkend nor ewestfal should be "final approver"
        // at this point, neither bmcgough, rkirkend nor ewestfal should be the last approver
        assertFalse("Rkirkend should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("rkirkend"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // approve as rkirkend
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        document.approve("");

        // should be one pending activated to ewestfal now
        actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should be 1 pending requests.", 1, actionRequests.size());
        ActionRequestValue actionRequest = (ActionRequestValue)actionRequests.get(0);
        assertTrue("Should be activated.", actionRequest.isActive());

        // ewestfal should now be the final approver
        assertTrue("Ewestfal should be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // approve as ewestfal to send to next node
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("ewestfal should have approve request", document.isApprovalRequested());
        document.approve("");

        // should be at the workflow document 2 node
        assertEquals("Should be at the WorkflowDocument2 Node.", SeqSetup.WORKFLOW_DOCUMENT_2_NODE, document.getNodeNames().iterator().next());
        // at this node there should be two requests, one to ewestfal with forceAction=false and one to pmckown,
        // since we haven't set the application constant, the non-force action request won't be activated first so pmckown
        // will not be the final approver
        assertFalse("Pmckown should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("pmckown"), SeqSetup.WORKFLOW_DOCUMENT_2_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_2_NODE));
        actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should be 2 action requests.", 2, actionRequests.size());

        // Now set up the app constant that checks force action properly and try a new document
        String parameterValue = "Y";
        Parameter.Builder b = Parameter.Builder.create(lastApproverActivateParameter);
        b.setValue(parameterValue);
        CoreFrameworkServiceLocator.getParameterService().updateParameter(b.build());

        lastApproverActivateParameter = CoreFrameworkServiceLocator.getParameterService().getParameter(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.FEATURE_DETAIL_TYPE, KewApiConstants.IS_LAST_APPROVER_ACTIVATE_FIRST_IND);
        assertNotNull("Parameter should not be null.", lastApproverActivateParameter);
        assertEquals("Parameter should be Y.", parameterValue, lastApproverActivateParameter.getValue());


        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.LAST_APPROVER_DOCUMENT_TYPE_NAME);
        document.route("");
        
        // on this document type approval progression will go as follows:
        // Workflow Document   (Sequential): bmcgough (1, fa=false),  rkirkend (2, fa=false), ewestfal (3, fa=true)
        // Workflow Document 2 (Sequential): pmckown (1, fa=false), ewestfal (2, fa=false)

        // at this point, neither bmcgough, rkirkend nor ewestfal should be the last approver
        assertFalse("Bmcgough should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("bmcgough"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Rkirkend should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("rkirkend"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // approve as bmcgough
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.approve("");

        // now there is just a request to rkirkend and ewestfal, since ewestfal is force action true, neither should be final approver
        assertFalse("Rkirkend should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("rkirkend"), SeqSetup.WORKFLOW_DOCUMENT_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // verify that ewestfal does not have permissions to approve the document yet since his request has not yet been activated
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertFalse("Ewestfal should not have permissions to approve", document.isApprovalRequested());
        
        // approve as rkirkend
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("Ewestfal should now have permission to approve", document.isApprovalRequested());
        
        // ewestfal should now be the final approver
        assertTrue("Ewestfal should now be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_NODE));

        // approve as ewestfal to send it to the next node
        document.approve("");

        TestUtilities.assertAtNode(document, SeqSetup.WORKFLOW_DOCUMENT_2_NODE);
        List<ActionRequestValue> requests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocId(document.getDocumentId());
        assertEquals("We should have 2 requests here.", 2, requests.size());
        
        // now, there are requests to pmckown and ewestfal here, the request to ewestfal is forceAction=false and since ewestfal
        // routed the document, this request should be auto-approved.  However, it's priority is 2 so it is activated after the
        // request to pmckown which is the situation we are testing
        assertTrue("Pmckown should be the last approver at this node.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("pmckown"), SeqSetup.WORKFLOW_DOCUMENT_2_NODE));
        assertFalse("Ewestfal should not be the final approver.", wdas.isLastApproverAtNode(document.getDocumentId(), getPrincipalIdForName("ewestfal"), SeqSetup.WORKFLOW_DOCUMENT_2_NODE));

        // if we approve as pmckown, the document should go into acknowledgement and become processed
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should be processed.", document.isProcessed());

        // set parameter value back to it's original value
        Parameter.Builder b2 = Parameter.Builder.create(lastApproverActivateParameter);
        b2.setValue("");
        CoreFrameworkServiceLocator.getParameterService().updateParameter(b2.build());
    }

    @Test public void testIsFinalApprover() throws Exception {
        // for this document, pmckown should be the final approver
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        assertFinalApprover(document);
    }

    @Test public void testIsFinalApproverChild() throws Exception {
        // 12-13-2005: HR ran into a bug where this method was not correctly locating the final approver node when using a document type whic
        // inherits the route from a parent, so we will incorporate this into the unit test to prevent regression
        WorkflowDocument childDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.CHILD_DOCUMENT_TYPE_NAME);
        assertFinalApprover(childDocument);
    }

    /**
     * Factored out so as not to duplicate a bunch of code between testIsFinalApprover and testIsFinalApproverChild.
     */
    private void assertFinalApprover(WorkflowDocument document) throws Exception {
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute.", document.isEnroute());
        assertTrue("Should have approve request.", document.isApprovalRequested());

        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        // bmcgough is not the final approver
        assertFalse("Should not be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("bmcgough")));
        // approve as bmcgough
        document.approve("");

        // should be to Ryan now, who is also not the final approver on the document
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be enroute.", document.isEnroute());
        assertTrue("Should have approve request.", document.isApprovalRequested());
        assertFalse("Should not be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("rkirkend")));
        document.approve("");

        // should be to Phil now, who *IS* the final approver on the document
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        assertTrue("Document should be enroute.", document.isEnroute());
        assertTrue("Should have approve request.", document.isApprovalRequested());
        assertTrue("Should be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("pmckown")));

        // now adhoc an approve to temay, phil should no longer be the final approver
        document.adHocToPrincipal(ActionRequestType.APPROVE, SeqSetup.WORKFLOW_DOCUMENT_2_NODE,
                "", getPrincipalIdForName("temay"), "", true);
        assertFalse("Should not be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("pmckown")));
        assertFalse("Should not be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("temay")));

        // now approve as temay and then adhoc an ack to jeremy
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        assertTrue("SHould have approve request.", document.isApprovalRequested());
        document.approve("");

        // phil should be final approver again
        assertTrue("Should be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("pmckown")));
        document.adHocToPrincipal(ActionRequestType.ACKNOWLEDGE, SeqSetup.WORKFLOW_DOCUMENT_2_NODE,
                "", getPrincipalIdForName("jhopf"), "", true);
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("Should have acknowledge request.", document.isAcknowledgeRequested());

        // now there should be an approve to phil and an ack to jeremy, so phil should be the final approver and jeremy should not
        assertTrue("Should be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("pmckown")));
        assertFalse("Should not be final approver.", wdas.isFinalApprover(document.getDocumentId(), getPrincipalIdForName("jhopf")));

        // after approving as phil, the document should go processed
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should be processed.", document.isProcessed());
    }
    
    @Test public void testGetPrincipalIdsInRouteLog() throws Exception {
    	Set<String> NonSITMembers = new HashSet<String>(
    			Arrays.asList(
						new String[] {
								getPrincipalIdForName("user1"), 
								getPrincipalIdForName("user2"), 
								getPrincipalIdForName("user3"), 
								getPrincipalIdForName("dewey")}
				)
    	);
    	
    	Set<String> WorkflowAdminMembers = new HashSet<String>(
    			Arrays.asList(
    					new String[] {
    							getPrincipalIdForName("ewestfal"), 
    							getPrincipalIdForName("rkirkend"), 
    							getPrincipalIdForName("jhopf"), 
    							getPrincipalIdForName("bmcgough"), 
    							getPrincipalIdForName("shenl"), 
    							getPrincipalIdForName("quickstart")
    					}
    			)
    	);
    	
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), RouteLogTestSetup.DOCUMENT_TYPE_NAME);
		document.route("");

        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
		// just look at the current node
		List<String> principalIds = wdas.getPrincipalIdsInRouteLog(document.getDocumentId(), false);
		// should contain ewestfal and NonSIT group members
		assertTrue(principalIds.contains(getPrincipalIdForName("ewestfal")));
		assertTrue(principalIds.containsAll(NonSITMembers));
		
		// should NOT contain jitrue and WorkflowAdmin group members as they are in the rule for the future node
		assertFalse(principalIds.contains(getPrincipalIdForName("jitrue")));
		assertFalse(principalIds.containsAll(WorkflowAdminMembers));
		
		// this time look at future nodes too
		principalIds = 	wdas.getPrincipalIdsInRouteLog(document.getDocumentId(), true);
		
		// should contain ewestfal and NonSIT group members
		assertTrue(principalIds.contains(getPrincipalIdForName("ewestfal")));
		assertTrue(principalIds.containsAll(NonSITMembers));

		// should also contain jitrue and WorkflowAdmin group members
		assertTrue(principalIds.contains(getPrincipalIdForName("jitrue")));
		assertTrue(principalIds.containsAll(WorkflowAdminMembers));
    }

    @Test public void testRoutingReportOnDocumentType() throws Exception {
        RoutingReportCriteria.Builder criteria = RoutingReportCriteria.Builder.createByDocumentTypeName("SeqDocType");
    	criteria.setRuleTemplateNames(Collections.singletonList("WorkflowDocumentTemplate"));
        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
    	DocumentDetail documentDetail = wdas.executeSimulation(criteria.build());
    	assertNotNull(documentDetail);
    	assertEquals("Should have been 2 requests generated.", 2, documentDetail.getActionRequests().size());

    	// let's try doing both WorkflowDocumentTemplate and WorkflowDocumentTemplate2 together
    	criteria.setRuleTemplateNames(Arrays.asList(new String[] {"WorkflowDocumentTemplate", "WorkflowDocument2Template"}));
    	documentDetail = wdas.executeSimulation(criteria.build());
    	assertEquals("Should have been 3 requests generated.", 3, documentDetail.getActionRequests().size());

    	boolean foundRkirkend = false;
    	boolean foundBmcgough = false;
    	boolean foundPmckown = false;
    	for (ActionRequest actionRequest : documentDetail.getActionRequests()) {
			String netId = getPrincipalNameForId(actionRequest.getPrincipalId());
			if (netId.equals("rkirkend")) {
				foundRkirkend = true;
				assertEquals(SeqSetup.WORKFLOW_DOCUMENT_NODE, actionRequest.getNodeName());
			} else if (netId.equals("bmcgough")) {
				foundBmcgough = true;
				assertEquals(SeqSetup.WORKFLOW_DOCUMENT_NODE, actionRequest.getNodeName());
			} else if (netId.equals("pmckown")) {
				foundPmckown = true;
				assertEquals(SeqSetup.WORKFLOW_DOCUMENT_2_NODE, actionRequest.getNodeName());
			}
		}
    	assertTrue("Did not find request for rkirkend", foundRkirkend);
    	assertTrue("Did not find request for bmcgough", foundBmcgough);
    	assertTrue("Did not find request for pmckown", foundPmckown);

    }

    @Test public void testRoutingReportOnDocumentId() throws Exception {
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "SeqDocType");

        WorkflowDocumentActionsService wdas = KewApiServiceLocator.getWorkflowDocumentActionsService();
        RoutingReportCriteria.Builder criteria = RoutingReportCriteria.Builder.createByDocumentId(doc.getDocumentId());
        criteria.setRuleTemplateNames(Collections.singletonList("WorkflowDocumentTemplate"));
        DocumentDetail documentDetail = wdas.executeSimulation(criteria.build());
        assertNotNull(documentDetail);
        assertEquals("Document id returned should be the same as the one passed in", doc.getDocumentId(),
                documentDetail.getDocument().getDocumentId());
        assertEquals("Wrong number of action requests generated", 2, documentDetail.getActionRequests().size());

        // let's try doing both WorkflowDocumentTemplate and WorkflowDocumentTemplate2 together
        criteria.setRuleTemplateNames(Arrays.asList(new String[] { "WorkflowDocumentTemplate", "WorkflowDocument2Template" }));
        documentDetail = wdas.executeSimulation(criteria.build());
        assertEquals("Should have been 3 requests generated.", 3, documentDetail.getActionRequests().size());

        boolean foundRkirkend = false;
        boolean foundBmcgough = false;
        boolean foundPmckown = false;
        for (ActionRequest actionRequest : documentDetail.getActionRequests()) {
            String netId = getPrincipalNameForId(actionRequest.getPrincipalId());
            if (netId.equals("rkirkend")) {
                foundRkirkend = true;
                assertEquals(SeqSetup.WORKFLOW_DOCUMENT_NODE, actionRequest.getNodeName());
            } else if (netId.equals("bmcgough")) {
                foundBmcgough = true;
                assertEquals(SeqSetup.WORKFLOW_DOCUMENT_NODE, actionRequest.getNodeName());
            } else if (netId.equals("pmckown")) {
                foundPmckown = true;
                assertEquals(SeqSetup.WORKFLOW_DOCUMENT_2_NODE, actionRequest.getNodeName());
            }
        }
        assertTrue("Did not find request for rkirkend", foundRkirkend);
        assertTrue("Did not find request for bmcgough", foundBmcgough);
        assertTrue("Did not find request for pmckown", foundPmckown);

    }
    
    protected void verifyEmptyArray(String qualifier, Object[] array) {
    	assertNotNull("Array should not be empty", array);
        assertEquals("Number of " + qualifier + "s Returned Should be 0",0,array.length);
    }

    private void verifyEmptyCollection(String qualifier, Collection collection) {
    	assertNotNull("Array should not be empty", collection);
        assertEquals("Number of " + qualifier + "s Returned Should be 0",0,collection.size());
    }

    @Test public void testRuleReportGeneralFunction() throws Exception {
        this.ruleExceptionTest(null, "Sending in null RuleReportCriteriaDTO should throw Exception");

        RuleReportCriteria.Builder ruleReportCriteria = RuleReportCriteria.Builder.create();
        this.ruleExceptionTest(ruleReportCriteria.build(), "Sending in empty RuleReportCriteriaDTO should throw Exception");

        ruleReportCriteria.setResponsiblePrincipalId("hobo_man");
        this.ruleExceptionTest(ruleReportCriteria.build(), "Sending in an invalid principle ID should throw Exception");

        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setResponsibleGroupId("-1234567");
        this.ruleExceptionTest(ruleReportCriteria.build(), "Sending in an invalid Workgroup ID should throw Exception");

        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setRuleExtensions(Collections.singletonMap("key", "value"));
        this.ruleExceptionTest(ruleReportCriteria.build(), "Sending in one or more RuleExtentionVO objects with no Rule Template Name should throw Exception");


        RuleService wdas = KewApiServiceLocator.getRuleService();
        List<Rule> rules = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setConsiderGroupMembership(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of Rules Returned Should be 3",3,rules.size());

        rules = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setActionRequestCodes(Collections.singletonList(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));
        ruleReportCriteria.setConsiderGroupMembership(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("temay"));
        rules = wdas.ruleReport(ruleReportCriteria.build());
        verifyEmptyCollection("Rule", rules);

        rules = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setActionRequestCodes(Collections.singletonList(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));
        ruleReportCriteria.setConsiderGroupMembership(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of Rules Returned Should be 1",1,rules.size());
        // check the rule returned

        Rule ruleVO = rules.get(0);
        assertEquals("Rule Document Type is not " + RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,ruleVO.getDocTypeName());
        assertEquals("Rule Template Named returned is not " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2,RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2,ruleVO.getRuleTemplateName());
        assertEquals("Rule did not have force action set to false",Boolean.FALSE,ruleVO.isForceAction());
        assertEquals("Number of Rule Responsibilities returned is incorrect",2,ruleVO.getRuleResponsibilities().size());

        for (RuleResponsibility responsibility : ruleVO.getRuleResponsibilities()) {
            String responsibilityPrincipalName = getPrincipalNameForId(responsibility.getPrincipalId());
            if ("temay".equals(responsibilityPrincipalName)) {
                assertEquals("Rule user is not correct","temay",responsibilityPrincipalName);
                assertEquals("Rule priority is incorrect",Integer.valueOf(1),responsibility.getPriority());
                assertEquals("Rule should be Ack Request",KewApiConstants.ACTION_REQUEST_APPROVE_REQ,responsibility.getActionRequestedCd());
            } else if ("ewestfal".equals(responsibilityPrincipalName)) {
                assertEquals("Rule user is not correct","ewestfal",responsibilityPrincipalName);
                assertEquals("Rule priority is incorrect",Integer.valueOf(2),responsibility.getPriority());
                assertEquals("Rule should be Ack Request",KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ,responsibility.getActionRequestedCd());
            } else {
                fail("Network ID of user for this responsibility is neither temay or ewestfal");
            }
        }

        rules = null;
        ruleVO = null;
        RuleResponsibility responsibilityVO = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setConsiderGroupMembership(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("temay"));
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of Rules returned is not correct",2,rules.size());
        for (Rule rule : rules) {
            if (RuleTestGeneralSetup.RULE_TEST_TEMPLATE_1.equals(rule.getRuleTemplateName())) {
                assertEquals("Rule Document Type is not " + RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,rule.getDocTypeName());
                assertEquals("Rule Template Named returned is not " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_1,RuleTestGeneralSetup.RULE_TEST_TEMPLATE_1,rule.getRuleTemplateName());
                assertEquals("Rule did not have force action set to true",Boolean.TRUE,rule.isForceAction());
                assertEquals("Number of Rule Responsibilities Returned Should be 1",1,rule.getRuleResponsibilities().size());
                responsibilityVO = rule.getRuleResponsibilities().get(0);
                assertEquals("Rule user is incorrect","temay",getPrincipalNameForId(responsibilityVO.getPrincipalId()));
                assertEquals("Rule priority is incorrect",Integer.valueOf(3),responsibilityVO.getPriority());
                assertEquals("Rule action request is incorrect",KewApiConstants.ACTION_REQUEST_APPROVE_REQ,responsibilityVO.getActionRequestedCd());
            } else if (RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2.equals(rule.getRuleTemplateName())) {
                assertEquals("Rule Document Type is not " + RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,rule.getDocTypeName());
                assertEquals("Rule Template Named returned is not " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2,RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2,rule.getRuleTemplateName());
                assertEquals("Rule did not have force action set to false",Boolean.FALSE,rule.isForceAction());
                assertEquals("Number of Rule Responsibilities returned is incorrect",2,rule.getRuleResponsibilities().size());
                for (RuleResponsibility responsibility : rule.getRuleResponsibilities()) {

                    String responsibilityPrincipalName = getPrincipalNameForId(responsibility.getPrincipalId());
                    if ("temay".equals(responsibilityPrincipalName)) {
                        assertEquals("Rule user is not correct","temay",responsibilityPrincipalName);
                        assertEquals("Rule priority is incorrect",Integer.valueOf(1),responsibility.getPriority());
                        assertEquals("Rule should be Ack Request",KewApiConstants.ACTION_REQUEST_APPROVE_REQ,responsibility.getActionRequestedCd());
                    } else if ("ewestfal".equals(responsibilityPrincipalName)) {
                        assertEquals("Rule user is not correct","ewestfal",responsibilityPrincipalName);
                        assertEquals("Rule priority is incorrect",Integer.valueOf(2),responsibility.getPriority());
                        assertEquals("Rule should be Ack Request",KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ,responsibility.getActionRequestedCd());
                    } else {
                        fail("Network ID of user for this responsibility is neither temay or ewestfal");
                    }
                }
            } else {
                fail("Rule Template of returned rule is not of type " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_1 + " nor " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_2);
            }
        }

        rules = null;
        ruleVO = null;
        responsibilityVO = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setResponsibleGroupId(RuleTestGeneralSetup.RULE_TEST_GROUP_ID);
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of Rules Returned Should be 1",1,rules.size());
        ruleVO = rules.get(0);
        assertEquals("Rule Document Type is not " + RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,ruleVO.getDocTypeName());
        assertEquals("Rule Template Named returned is not " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_3,RuleTestGeneralSetup.RULE_TEST_TEMPLATE_3,ruleVO.getRuleTemplateName());
        assertEquals("Rule did not have force action set to true",Boolean.TRUE,ruleVO.isForceAction());
        assertEquals("Number of Rule Responsibilities Returned Should be 1",1,ruleVO.getRuleResponsibilities().size());
        responsibilityVO = ruleVO.getRuleResponsibilities().get(0);
        Group ruleTestGroup = KimApiServiceLocator.getGroupService().getGroup(responsibilityVO.getGroupId());
        assertEquals("Rule workgroup id is incorrect",RuleTestGeneralSetup.RULE_TEST_GROUP_ID, ruleTestGroup.getId());
        assertEquals("Rule priority is incorrect",Integer.valueOf(1),responsibilityVO.getPriority());
        assertEquals("Rule action request is incorrect",KewApiConstants.ACTION_REQUEST_FYI_REQ,responsibilityVO.getActionRequestedCd());

        rules = null;
        ruleVO = null;
        responsibilityVO = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestGeneralSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("user1"));
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of Rules Returned Should be 1",1,rules.size());
        ruleVO = rules.get(0);
        assertEquals("Rule Document Type is not " + RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,RuleTestGeneralSetup.DOCUMENT_TYPE_NAME,ruleVO.getDocTypeName());
        assertNotSame("Rule Template Named returned is not " + RuleTestGeneralSetup.RULE_TEST_TEMPLATE_3,
                RuleTestGeneralSetup.RULE_TEST_TEMPLATE_3, ruleVO.getRuleTemplateName());
        assertEquals("Rule did not have force action set to true",Boolean.TRUE,ruleVO.isForceAction());
        assertEquals("Number of Rule Responsibilities Returned Should be 1",1,ruleVO.getRuleResponsibilities().size());
        responsibilityVO = ruleVO.getRuleResponsibilities().get(0);
        assertEquals("Rule workgroup id is incorrect",RuleTestGeneralSetup.RULE_TEST_GROUP_ID, ruleTestGroup.getId());
        assertEquals("Rule priority is incorrect",Integer.valueOf(1),responsibilityVO.getPriority());
        assertEquals("Rule action request is incorrect",KewApiConstants.ACTION_REQUEST_FYI_REQ,responsibilityVO.getActionRequestedCd());
    }

    /**
     * Tests specific rule scenario relating to standard org review routing
     *
     * @throws Exception
     */
    @Test public void testRuleReportOrgReviewTest() throws Exception {
        loadXmlFile("WorkflowUtilityRuleReportConfig.xml");

        RuleService wdas = KewApiServiceLocator.getRuleService();

        RuleReportCriteria.Builder ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("user1"));
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        List<Rule> rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",2,rules.size());

        ruleReportCriteria = null;
        rules = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("user1"));
        ruleReportCriteria.setConsiderGroupMembership(Boolean.FALSE.booleanValue());
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",1,rules.size());

        ruleReportCriteria = null;
        rules = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        Map<String, String> ruleExtensions = new HashMap<String, String>();
        ruleExtensions.put(RuleTestOrgReviewSetup.RULE_TEST_CHART_CODE_NAME, "BA");
        ruleExtensions.put(RuleTestOrgReviewSetup.RULE_TEST_ORG_CODE_NAME,"FMOP");
        ruleReportCriteria.setRuleExtensions(ruleExtensions);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",2,rules.size());

        ruleReportCriteria = null;
        rules = null;
        ruleExtensions = new HashMap<String, String>();
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("ewestfal"));
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE.booleanValue());
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",1,rules.size());
        Rule ruleVO = rules.get(0);
        assertEquals("Rule Document Type is not " + RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME,RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME,ruleVO.getDocTypeName());
        assertEquals("Rule Template Named returned is not " + RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE,RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE,ruleVO.getRuleTemplateName());
        assertEquals("Rule did not have force action set to true",Boolean.TRUE,ruleVO.isForceAction());
        assertEquals("Number of Rule Responsibilities Returned Should be 1",1,ruleVO.getRuleResponsibilities().size());
        RuleResponsibility responsibilityVO = ruleVO.getRuleResponsibilities().get(0);
        Group ruleTestGroup2 = KimApiServiceLocator.getGroupService().getGroup(responsibilityVO.getGroupId());
        assertEquals("Rule workgroup name is incorrect",RuleTestOrgReviewSetup.RULE_TEST_WORKGROUP2,ruleTestGroup2.getName());
        assertEquals("Rule priority is incorrect",Integer.valueOf(4),responsibilityVO.getPriority());
        assertEquals("Rule action request is incorrect",KewApiConstants.ACTION_REQUEST_FYI_REQ,responsibilityVO.getActionRequestedCd());
        ruleExtensions = ruleVO.getRuleExtensionMap();
        assertEquals("Number of Rule Extensions Returned Should be 2",2,ruleExtensions.size());
        for (Map.Entry<String, String> entry : ruleExtensions.entrySet()) {
            // if rule key is chartCode.... should equal UA
            // else if rule key is orgCode.... should equal VPIT
            // otherwise error
            if (RuleTestOrgReviewSetup.RULE_TEST_CHART_CODE_NAME.equals(entry.getKey())) {
                assertEquals("Rule Extension for key '" + RuleTestOrgReviewSetup.RULE_TEST_CHART_CODE_NAME + "' is incorrect","UA",entry.getValue());
            } else if (RuleTestOrgReviewSetup.RULE_TEST_ORG_CODE_NAME.equals(entry.getKey())) {
                assertEquals("Rule Extension for key '" + RuleTestOrgReviewSetup.RULE_TEST_ORG_CODE_NAME + "' is incorrect","VPIT",entry.getValue());
            } else {
                fail("Rule Extension has attribute key that is neither '" + RuleTestOrgReviewSetup.RULE_TEST_CHART_CODE_NAME +
                        "' nor '" + RuleTestOrgReviewSetup.RULE_TEST_ORG_CODE_NAME + "'");
            }
        }

        ruleReportCriteria = null;
        rules = null;
        ruleVO = null;
        responsibilityVO = null;
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        ruleReportCriteria.setResponsiblePrincipalId(getPrincipalIdForName("user1"));
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE);
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",2,rules.size());

        ruleReportCriteria = null;
        rules = null;
        ruleVO = null;
        responsibilityVO = null;
        ruleExtensions = new HashMap<String, String>();
        ruleReportCriteria = RuleReportCriteria.Builder.create();
        ruleReportCriteria.setDocumentTypeName(RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME);
        ruleReportCriteria.setRuleTemplateName(RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE);
        ruleExtensions.put(RuleTestOrgReviewSetup.RULE_TEST_CHART_CODE_NAME, "UA");
        ruleExtensions.put(RuleTestOrgReviewSetup.RULE_TEST_ORG_CODE_NAME, "FMOP");
        ruleReportCriteria.setRuleExtensions(ruleExtensions);
        ruleReportCriteria.setIncludeDelegations(Boolean.FALSE);
        rules = wdas.ruleReport(ruleReportCriteria.build());
        assertEquals("Number of rules returned is incorrect",1,rules.size());
        ruleVO = rules.get(0);
        assertEquals("Rule Document Type is not " + RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME,RuleTestOrgReviewSetup.DOCUMENT_TYPE_NAME,ruleVO.getDocTypeName());
        assertEquals("Rule Template Named returned is not " + RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE,RuleTestOrgReviewSetup.RULE_TEST_TEMPLATE,ruleVO.getRuleTemplateName());
        assertEquals("Rule did not have force action set to true",Boolean.TRUE,ruleVO.isForceAction());
        assertEquals("Number of Rule Responsibilities Returned Should be 1",1,ruleVO.getRuleResponsibilities().size());
        responsibilityVO = ruleVO.getRuleResponsibilities().get(0);
        ruleTestGroup2 = KimApiServiceLocator.getGroupService().getGroup(responsibilityVO.getGroupId());
        assertEquals("Rule workgroup name is incorrect",RuleTestOrgReviewSetup.RULE_TEST_WORKGROUP, ruleTestGroup2.getName());
        assertEquals("Rule priority is incorrect",Integer.valueOf(1),responsibilityVO.getPriority());
        assertEquals("Rule action request is incorrect",KewApiConstants.ACTION_REQUEST_APPROVE_REQ,responsibilityVO.getActionRequestedCd());
    }

    @Test public void testGetUserActionItemCount() throws Exception {
        String principalId = getPrincipalIdForName("ewestfal");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(principalId, SeqSetup.DOCUMENT_TYPE_NAME);
        document.route("");
        assertTrue(document.isEnroute());

        ActionListService als = KewApiServiceLocator.getActionListService();
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(0), als.getUserActionItemCount(principalId));
        principalId = getPrincipalIdForName("bmcgough");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));
        principalId = getPrincipalIdForName("rkirkend");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));

        TestUtilities.assertAtNode(document, "WorkflowDocument");
        document.returnToPreviousNode("", "AdHoc");
        TestUtilities.assertAtNode(document, "AdHoc");
        // verify count after return to previous
        principalId = getPrincipalIdForName("ewestfal");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        // expect one action item for approval request
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));
        principalId = getPrincipalIdForName("bmcgough");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertFalse(document.isApprovalRequested());
        assertTrue(document.isFYIRequested());
        // expect one action item for fyi action request
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));
        principalId = getPrincipalIdForName("rkirkend");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertFalse(document.isApprovalRequested());
        // expect no action items
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(0), als.getUserActionItemCount(
                principalId));

        principalId = getPrincipalIdForName("ewestfal");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        document.approve("");
        TestUtilities.assertAtNode(document, "WorkflowDocument");

        // we should be back where we were
        principalId = getPrincipalIdForName("ewestfal");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertFalse(document.isApprovalRequested());
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(0), als.getUserActionItemCount(
                principalId));
        principalId = getPrincipalIdForName("bmcgough");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));
        principalId = getPrincipalIdForName("rkirkend");
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        assertEquals("Count is incorrect for user " + principalId, Integer.valueOf(1), als.getUserActionItemCount(
                principalId));
    }

    @Test public void testGetActionItems() throws Exception {
        String initiatorNetworkId = "ewestfal";
        String user1NetworkId = "bmcgough";
        String user2NetworkId ="rkirkend";
        String initiatorPrincipalId = getPrincipalIdForName(initiatorNetworkId);
        String user1PrincipalId = getPrincipalIdForName(user1NetworkId);
        String user2PrincipalId = getPrincipalIdForName(user2NetworkId);
        String principalId = getPrincipalIdForName(initiatorNetworkId);
        String docTitle = "this is the doc title";
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(principalId, SeqSetup.DOCUMENT_TYPE_NAME);
        document.setTitle(docTitle);
        document.route("");
        assertTrue(document.isEnroute());

        ActionListService als = KewApiServiceLocator.getActionListService();
        List<ActionItem> actionItems = als.getAllActionItems(document.getDocumentId());
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item should be Approve request", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionItem.getActionRequestCd());
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("User should be one of '" + user1NetworkId + "' or '" + user2NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()) || user2PrincipalId.equals(actionItem.getPrincipalId()));
        }

        principalId = getPrincipalIdForName(user2NetworkId);
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        TestUtilities.assertAtNode(document, "WorkflowDocument");
        document.returnToPreviousNode("", "AdHoc");
        TestUtilities.assertAtNode(document, "AdHoc");
        // verify count after return to previous
        actionItems = als.getAllActionItems(document.getDocumentId());
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("Action Items should be Approve or FYI requests only", KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(actionItem.getActionRequestCd()) || KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(actionItem.getActionRequestCd()));
            if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(actionItem.getActionRequestCd())) {
                assertTrue("User should be '" + initiatorNetworkId + "'", initiatorPrincipalId.equals(actionItem.getPrincipalId()));
            } else if (KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(actionItem.getActionRequestCd())) {
                assertTrue("User should be  '" + user1NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()));
            }
        }

        principalId = getPrincipalIdForName(initiatorNetworkId);
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        document.approve("");
        TestUtilities.assertAtNode(document, "WorkflowDocument");

        // we should be back where we were
        actionItems = als.getAllActionItems(document.getDocumentId());
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item should be Approve request", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionItem.getActionRequestCd());
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("User should be one of '" + user1NetworkId + "' or '" + user2NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()) || user2PrincipalId.equals(actionItem.getPrincipalId()));
        }
    }

    @Test public void testGetActionItems_ActionRequestCodes() throws Exception {
        String initiatorNetworkId = "ewestfal";
        String user1NetworkId = "bmcgough";
        String user2NetworkId ="rkirkend";
        String initiatorPrincipalId = getPrincipalIdForName(initiatorNetworkId);
        String user1PrincipalId = getPrincipalIdForName(user1NetworkId);
        String user2PrincipalId = getPrincipalIdForName(user2NetworkId);
        String principalId = getPrincipalIdForName(initiatorNetworkId);
        String docTitle = "this is the doc title";
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(principalId, SeqSetup.DOCUMENT_TYPE_NAME);
        document.setTitle(docTitle);
        document.route("");
        assertTrue(document.isEnroute());

        ActionListService als = KewApiServiceLocator.getActionListService();
        List<ActionItem> actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}));
        verifyEmptyCollection("Action Item", actionItems);
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ}));
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item should be Approve request", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionItem.getActionRequestCd());
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("User should be one of '" + user1NetworkId + "' or '" + user2NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()) || user2PrincipalId.equals(actionItem.getPrincipalId()));
        }

        principalId = getPrincipalIdForName(user2NetworkId);
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        TestUtilities.assertAtNode(document, "WorkflowDocument");
        document.returnToPreviousNode("", "AdHoc");
        TestUtilities.assertAtNode(document, "AdHoc");
        // verify count after return to previous
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(new String[]{KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}));
        verifyEmptyCollection("Action Item", actionItems);
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(
                new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ}));
        assertEquals("Incorrect number of action items returned",1,actionItems.size());
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(
                new String[]{KewApiConstants.ACTION_REQUEST_FYI_REQ}));
        assertEquals("Incorrect number of action items returned",1,actionItems.size());
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(
                new String[]{KewApiConstants.ACTION_REQUEST_FYI_REQ, KewApiConstants.ACTION_REQUEST_APPROVE_REQ}));
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("Action Items should be Approve or FYI requests only", KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(actionItem.getActionRequestCd()) || KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(actionItem.getActionRequestCd()));
            if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(actionItem.getActionRequestCd())) {
                assertTrue("User should be '" + initiatorNetworkId + "'", initiatorPrincipalId.equals(actionItem.getPrincipalId()));
            } else if (KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(actionItem.getActionRequestCd())) {
                assertTrue("User should be  '" + user1NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()));
            } else {
                fail("Should not have found action request with requested action '" + KewApiConstants.ACTION_REQUEST_CD.get(actionItem.getActionRequestCd()) + "'");
            }
        }

        principalId = getPrincipalIdForName(initiatorNetworkId);
        document = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        document.approve("");
        TestUtilities.assertAtNode(document, "WorkflowDocument");

        // we should be back where we were
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(
                new String[]{KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}));
        verifyEmptyCollection("Action Item", actionItems);
        actionItems = als.getActionItems(document.getDocumentId(), Arrays.asList(
                new String[]{KewApiConstants.ACTION_REQUEST_APPROVE_REQ}));
        assertEquals("Incorrect number of action items returned",2,actionItems.size());
        for (ActionItem actionItem : actionItems) {
            assertEquals("Action Item should be Approve request", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionItem.getActionRequestCd());
            assertEquals("Action Item has incorrect doc title", docTitle, actionItem.getDocTitle());
            assertTrue("User should be one of '" + user1NetworkId + "' or '" + user2NetworkId + "'", user1PrincipalId.equals(actionItem.getPrincipalId()) || user2PrincipalId.equals(actionItem.getPrincipalId()));
        }
    }

    /**
     * This method routes two test documents of the type specified.  One has the given title and another has a dummy title.
     */
    private void setupPerformDocumentSearchTests(String documentTypeName, String expectedRouteNodeName, String docTitle) throws WorkflowException {
        String userNetworkId = "ewestfal";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Respect my Authoritah");
        workflowDocument.route("routing this document.");
        if (StringUtils.isNotBlank(expectedRouteNodeName)) {
        	assertEquals("Document is not at expected routeNodeName", expectedRouteNodeName, workflowDocument.getNodeNames().iterator().next());
        }

        userNetworkId = "rkirkend";
        workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);
        workflowDocument.setTitle(docTitle);
        workflowDocument.route("routing this document.");
        if (StringUtils.isNotBlank(expectedRouteNodeName)) {
        	assertEquals("Document is not at expected routeNodeName", expectedRouteNodeName, workflowDocument.getNodeNames().iterator().next());
        }
    }

    @Test public void testPerformDocumentSearch_WithUser_CustomThreshold() throws Exception {
        runTestPerformDocumentSearch_CustomThreshold(getPrincipalIdForName("user2"));
    }

    @Test public void testPerformDocumentSearch_NoUser_CustomThreshold() throws Exception {
    	runTestPerformDocumentSearch_CustomThreshold(null);
    }

    private void runTestPerformDocumentSearch_CustomThreshold(String principalId) throws Exception {
        String documentTypeName = SeqSetup.DOCUMENT_TYPE_NAME;
        String docTitle = "Routing Style";
        setupPerformDocumentSearchTests(documentTypeName, null, docTitle);

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId,
                criteria.build());
        assertEquals("Search results should have two documents.", 2, results.getSearchResults().size());

        int threshold = 1;
        criteria.setMaxResults(1);
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertTrue("Search results should signify search went over the given threshold: " + threshold, results.isOverThreshold());
        assertEquals("Search results should have one document.", threshold, results.getSearchResults().size());
    }

    @Test public void testPerformDocumentSearch_WithUser_BasicCriteria() throws Exception {
        runTestPerformDocumentSearch_BasicCriteria(getPrincipalIdForName("user2"));
    }

    @Test public void testPerformDocumentSearch_NoUser_BasicCriteria() throws Exception {
    	runTestPerformDocumentSearch_BasicCriteria(null);
    }

    private void runTestPerformDocumentSearch_BasicCriteria(String principalId) throws Exception {
        String documentTypeName = SeqSetup.DOCUMENT_TYPE_NAME;
        String docTitle = "Routing Style";
        setupPerformDocumentSearchTests(documentTypeName, null, docTitle);
        String userNetworkId = "delyea";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Get Outta Dodge");
        workflowDocument.route("routing this document.");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setTitle(docTitle);
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId,
                criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setInitiatorPrincipalName("rkirkend");
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setInitiatorPrincipalName("user1");
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should have three documents.", 3, results.getSearchResults().size());
        // now verify that the search returned the proper document id
        boolean foundValidDocId = false;
        for (DocumentSearchResult result : results.getSearchResults()) {
            if (result.getDocument().getDocumentId().equals(workflowDocument.getDocumentId())) {
                foundValidDocId = true;
                break;
            }
        }
        assertTrue("Should have found document search result with specified document id", foundValidDocId);
    }

    @Test public void testPerformDocumentSearch_WithUser_RouteNodeSearch() throws Exception {
        runTestPerformDocumentSearch_RouteNodeSearch(getPrincipalIdForName("user2"));
    }

    @Test public void testPerformDocumentSearch_NoUser_RouteNodeSearch() throws Exception {
    	runTestPerformDocumentSearch_RouteNodeSearch(null);
    }

    private void runTestPerformDocumentSearch_RouteNodeSearch(String principalId) throws Exception {
        String documentTypeName = SeqSetup.DOCUMENT_TYPE_NAME;
        setupPerformDocumentSearchTests(documentTypeName, SeqSetup.WORKFLOW_DOCUMENT_NODE, "Doc Title");

        // test exception thrown when route node specified and no doc type specified
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setRouteNodeName(SeqSetup.ADHOC_NODE);
        try {
            KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            fail("Exception should have been thrown when specifying a route node name but no document type name");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // test exception thrown when route node specified does not exist on document type
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setRouteNodeName("This is an invalid route node name!!!");
        try {
            DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            assertTrue("No results should have been returned for route node name that does not exist on the specified documentType.", results.getSearchResults().isEmpty());
        } catch (Exception e) {
            fail("Exception should not have been thrown when specifying a route node name that does not exist on the specified document type name");
        }

        runPerformDocumentSearch_RouteNodeSearch(principalId, SeqSetup.ADHOC_NODE, documentTypeName, 0, 0, 2);
        runPerformDocumentSearch_RouteNodeSearch(principalId, SeqSetup.WORKFLOW_DOCUMENT_NODE, documentTypeName, 0, 2, 0);
        runPerformDocumentSearch_RouteNodeSearch(principalId, SeqSetup.WORKFLOW_DOCUMENT_2_NODE, documentTypeName, 2, 0, 0);
    }

    @Test public void testPerformDocumentSearch_RouteNodeSpecial() throws RemoteException, WorkflowException {
        String documentTypeName = "DocumentWithSpecialRouteNodes";
        setupPerformDocumentSearchTests(documentTypeName, "Level1", "Doc Title");
        runPerformDocumentSearch_RouteNodeSearch(null, "Level5", documentTypeName, 0, 0, 2);
        runPerformDocumentSearch_RouteNodeSearch(null, "Level1", documentTypeName, 0, 2, 0);
        runPerformDocumentSearch_RouteNodeSearch(null, "Level3", documentTypeName, 2, 0, 0);

    }

    private void runPerformDocumentSearch_RouteNodeSearch(String principalId, String routeNodeName, String documentTypeName, int countBeforeNode, int countAtNode, int countAfterNode) throws RemoteException, WorkflowException {

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setRouteNodeName(routeNodeName);
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId,
                criteria.build());
        assertEquals("Wrong number of search results when checking docs at default node logic.", countAtNode, results.getSearchResults().size());

        criteria.setRouteNodeLookupLogic(RouteNodeLookupLogic.EXACTLY);
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Wrong number of search results when checking docs at exact node.", countAtNode, results.getSearchResults().size());

        criteria.setRouteNodeLookupLogic(RouteNodeLookupLogic.BEFORE);
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Wrong number of search results when checking docs before node.", countBeforeNode, results.getSearchResults().size());

        criteria.setRouteNodeLookupLogic(RouteNodeLookupLogic.AFTER);
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Wrong number of search results when checking docs after node.", countAfterNode, results.getSearchResults().size());
    }

    @Test public void testPerformDocumentSearch_WithUser_SearchAttributes() throws Exception {
    	runTestPerformDocumentSearch_SearchAttributes(getPrincipalIdForName("user2"));
    }

    @Test public void testPerformDocumentSearch_NoUser_SearchAttributes() throws Exception {
    	runTestPerformDocumentSearch_SearchAttributes(null);
    }

    private void runTestPerformDocumentSearch_SearchAttributes(String principalId) throws Exception {
        String documentTypeName = SeqSetup.DOCUMENT_TYPE_NAME;
        String docTitle = "Routing Style";
        setupPerformDocumentSearchTests(documentTypeName, null, docTitle);

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, Collections.singletonList(TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE));
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId,
                criteria.build());
        assertEquals("Search results should have two documents.", 2, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, Collections.singletonList("fred"));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put("fakeproperty", Collections.singletonList("doesntexist"));
        try {
            results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (Exception e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, Collections.singletonList(TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString()));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should have two documents.", 2, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, Collections.singletonList("1111111"));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put("fakeymcfakefake", Collections.singletonList("99999999"));
        try {
            results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (IllegalStateException ise) {
        } catch (Exception e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, Collections.singletonList(TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString()));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should have two documents.", 2, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, Collections.singletonList("215.3548"));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put("fakeylostington", Collections.singletonList("9999.9999"));
        try {
            results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (Exception e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, Collections.singletonList(
                DocumentSearchInternalUtils.getDisplayValueWithDateOnly(new Timestamp(
                        TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS))));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should have two documents.", 2, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, Collections.singletonList("07/06/1979"));
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.getDocumentAttributeValues().put("lastingsfakerson", Collections.singletonList("07/06/2007"));
        try {
            results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(principalId, criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (Exception e) {}
    }

    @Test public void testGetSearchableAttributeDateTimeValuesByKey() throws Exception {
        String documentTypeName = SeqSetup.DOCUMENT_TYPE_NAME;
        String userNetworkId = "ewestfal";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Respect my Authoritah");
        workflowDocument.route("routing this document.");
        userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument2 = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(userNetworkId), documentTypeName);
        workflowDocument2.setTitle("Routing Style");
        workflowDocument2.route("routing this document.");

        WorkflowDocumentService wds = KewApiServiceLocator.getWorkflowDocumentService();
        List<DateTime> dateTimes = wds.getSearchableAttributeDateTimeValuesByKey(workflowDocument.getDocumentId(), TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY);
        assertNotNull("dateTimes should not be null", dateTimes);
        assertTrue("dateTimes should not be empty", !dateTimes.isEmpty());
        verifyTimestampToSecond(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS, dateTimes.get(0).getMillis());

        dateTimes = wds.getSearchableAttributeDateTimeValuesByKey(workflowDocument2.getDocumentId(), TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY);
        assertNotNull("dateTimes should not be null", dateTimes);
        assertTrue("dateTimes should not be empty", !dateTimes.isEmpty());
        verifyTimestampToSecond(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS, dateTimes.get(0).getMillis());
    }

    protected void verifyTimestampToSecond(Long originalTimeInMillis, Long testTimeInMillis) throws Exception {
        Calendar testDate = Calendar.getInstance();
        testDate.setTimeInMillis(originalTimeInMillis);
        testDate.set(Calendar.MILLISECOND, 0);
        Calendar attributeDate = Calendar.getInstance();
        attributeDate.setTimeInMillis(testTimeInMillis);
        attributeDate.set(Calendar.MILLISECOND, 0);
        assertEquals("The month value for the searchable attribute is wrong",testDate.get(Calendar.MONTH),attributeDate.get(Calendar.MONTH));
        assertEquals("The date value for the searchable attribute is wrong",testDate.get(Calendar.DATE),attributeDate.get(Calendar.DATE));
        assertEquals("The year value for the searchable attribute is wrong",testDate.get(Calendar.YEAR),attributeDate.get(Calendar.YEAR));
        assertEquals("The hour value for the searchable attribute is wrong",testDate.get(Calendar.HOUR),attributeDate.get(Calendar.HOUR));
        assertEquals("The minute value for the searchable attribute is wrong",testDate.get(Calendar.MINUTE),attributeDate.get(Calendar.MINUTE));
        assertEquals("The second value for the searchable attribute is wrong",testDate.get(Calendar.SECOND),attributeDate.get(Calendar.SECOND));
    }

    private void ruleExceptionTest(RuleReportCriteria ruleReportCriteria, String message) {
        try {
            KewApiServiceLocator.getRuleService().ruleReport(ruleReportCriteria);
            fail(message);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private class RuleTestGeneralSetup {
        public static final String DOCUMENT_TYPE_NAME = "RuleTestDocType";
        public static final String RULE_TEST_TEMPLATE_1 = "WorkflowDocumentTemplate";
        public static final String RULE_TEST_TEMPLATE_2 = "WorkflowDocument2Template";
        public static final String RULE_TEST_TEMPLATE_3 = "WorkflowDocument3Template";
        public static final String RULE_TEST_GROUP_ID = "3003"; // the NonSIT group
    }

    private class RuleTestOrgReviewSetup {
        public static final String DOCUMENT_TYPE_NAME = "OrgReviewTestDocType";
        public static final String RULE_TEST_TEMPLATE = "OrgReviewTemplate";
        public static final String RULE_TEST_WORKGROUP = "Org_Review_Group";
        public static final String RULE_TEST_WORKGROUP2 = "Org_Review_Group_2";
        public static final String RULE_TEST_CHART_CODE_NAME = "chartCode";
        public static final String RULE_TEST_ORG_CODE_NAME = "orgCode";
    }

    private class SeqSetup {
        public static final String DOCUMENT_TYPE_NAME = "SeqDocType";
        public static final String LAST_APPROVER_DOCUMENT_TYPE_NAME = "SeqLastApproverDocType";
        public static final String CHILD_DOCUMENT_TYPE_NAME = "SeqChildDocType";
        public static final String ADHOC_NODE = "AdHoc";
        public static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
        public static final String WORKFLOW_DOCUMENT_2_NODE = "WorkflowDocument2";
        public static final String ACKNOWLEDGE_1_NODE = "Acknowledge1";
        public static final String ACKNOWLEDGE_2_NODE = "Acknowledge2";
    }
    
    private class RouteLogTestSetup {
        public static final String DOCUMENT_TYPE_NAME = "UserAndGroupTestDocType";
        public static final String RULE_TEST_TEMPLATE_1 = "WorkflowDocumentTemplate";
        public static final String RULE_TEST_TEMPLATE_2 = "WorkflowDocument2Template";
        public static final String RULE_TEST_GROUP_ID = "3003"; // the NonSIT group
    }

}
