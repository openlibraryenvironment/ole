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
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.RoutingReportActionToTake;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.TestRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.util.FutureRequestDocumentStateManager;
import org.kuali.rice.kew.api.KewApiConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests users requesting to see all future requests, not seeing any future requests on documents and the clearing of those
 * statuses on documents.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FutureRequestsTest extends KEWTestCase {

    /**
     * Verify future requests status are being preserved on {@link DocumentRouteHeaderValue} objects when set from the
     * {@link WorkflowDocumentFactory}
     *
     * @throws Exception
     */
    @Test
    public void testSavingFutureRequestsStatuses() throws Exception {
        List<String> ids = new ArrayList<String>();
        ids.add(getPrincipalIdForName("user1"));
        TestRuleAttribute.setRecipientPrincipalIds("TestRole", "TestRole-user1", ids);

        // Test receiving future requests

        String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(rkirkendPrincipalId, "TestDocumentType");
        document.setReceiveFutureRequests();
        document.route("");

        DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        
        FutureRequestDocumentStateManager futRequestStateMan = new FutureRequestDocumentStateManager(routeHeader, rkirkendPrincipalId);
        assertTrue(futRequestStateMan.isReceiveFutureRequests());
        assertFalse(futRequestStateMan.isClearFutureRequestState());
        assertFalse(futRequestStateMan.isDoNotReceiveFutureRequests());

        // Test not receiving future requests

        document = WorkflowDocumentFactory.createDocument(rkirkendPrincipalId, "TestDocumentType");
        document.setDoNotReceiveFutureRequests();
        document.route("");

        routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        
        futRequestStateMan = new FutureRequestDocumentStateManager(routeHeader, rkirkendPrincipalId);
        assertFalse(futRequestStateMan.isReceiveFutureRequests());
        assertFalse(futRequestStateMan.isClearFutureRequestState());
        assertTrue(futRequestStateMan.isDoNotReceiveFutureRequests());

        // test clearing state from existing document
        document = WorkflowDocumentFactory.loadDocument(rkirkendPrincipalId, document.getDocumentId());
        document.setClearFutureRequests();
        document.approve("");

        routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        futRequestStateMan = new FutureRequestDocumentStateManager(routeHeader, rkirkendPrincipalId);
        assertFalse(futRequestStateMan.isReceiveFutureRequests());
        assertTrue(futRequestStateMan.isClearFutureRequestState());
        assertFalse(futRequestStateMan.isDoNotReceiveFutureRequests());

        // reload the route header
        routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        int deactivatedCount = 0;
        for (BranchState state : routeHeader.getRootBranchState()) {
            if (state.getKey().contains(FutureRequestDocumentStateManager.FUTURE_REQUESTS_VAR_KEY)) {
                fail("state clearing should have removed all future request vars");
            } else if (state.getKey().contains(FutureRequestDocumentStateManager.DEACTIVATED_REQUESTS_VARY_KEY)) {
                deactivatedCount++;
            }
        }
        assertEquals(2, deactivatedCount);
        // test standard scenario of not setting a future request status on the document
        document = WorkflowDocumentFactory.createDocument(rkirkendPrincipalId, "TestDocumentType");
        document.route("");
        routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        futRequestStateMan = new FutureRequestDocumentStateManager(routeHeader, rkirkendPrincipalId);
        assertFalse(futRequestStateMan.isReceiveFutureRequests());
        assertFalse(futRequestStateMan.isClearFutureRequestState());
        assertFalse(futRequestStateMan.isDoNotReceiveFutureRequests());
    }

    /**
     * Tests future requests work with routing and force action rules
     *
     * @throws Exception
     */
    @Test
    public void testFutureRequestsWithRouting() throws Exception {
        this.loadXmlFile(this.getClass(), "FutureRequestsConfig.xml");

        String user1PrincipalId = getPrincipalIdForName("user1");
        String user2PrincipalId = getPrincipalIdForName("user2");

        // Node 1 - user1 approval (forceAction true)
        //          user2 approval (forceAction false)
        // Node 2 - NonSIT approval (forceAction false)
        //          user1 approval (forceAction true)
        // Node 3 - user2 approval (forceAction false)
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "FutureRequestsDoc");
        document.setDoNotReceiveFutureRequests();
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(user1PrincipalId, document.getDocumentId());
        assertFalse(document.isApprovalRequested());

        document = WorkflowDocumentFactory.loadDocument(user2PrincipalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
        document.setReceiveFutureRequests();
        document.approve("");

        // should have another request from second rule that is not force action because
        // of policy
        document = WorkflowDocumentFactory.loadDocument(user2PrincipalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());

        // user2 should have action items. user1 should not
        assertEquals(1, KEWServiceLocator.getActionListService().getActionList(user2PrincipalId, new ActionListFilter()).size());
        assertEquals(1, KEWServiceLocator.getActionListService().getActionList(user1PrincipalId, new ActionListFilter()).size());

        document.approve("");

        // test for request to user2 and not a workgroup
        document = WorkflowDocumentFactory.loadDocument(user2PrincipalId, document.getDocumentId());
        assertTrue(document.isApprovalRequested());
    }

    /**
     * Tests future requests operation
     *
     * @throws Exception
     */
    @Test
    public void testFutureRequestsWithRoutingAndWorkflowInfoActionRequestCheck() throws Exception {
        this.loadXmlFile(this.getClass(), "FutureRequestsConfig.xml");

        String user1PrincipalId = getPrincipalIdForName("user1");
        String user2PrincipalId = getPrincipalIdForName("user2");

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "FutureRequestsDoc");
        document.route("");

        // Node1
        //user1 should have approval requested
        document = WorkflowDocumentFactory.loadDocument(user1PrincipalId, document.getDocumentId());
        WorkflowDocumentActionsService actionService = KewApiServiceLocator.getWorkflowDocumentActionsService();
        RoutingReportCriteria.Builder reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        reportCriteria.setTargetPrincipalIds(Collections.singletonList(user1PrincipalId));
        String actionToTakeNode = "Node1";
        reportCriteria.setActionsToTake(Collections.singletonList(RoutingReportActionToTake.Builder.create(
                KewApiConstants.ACTION_TAKEN_APPROVED_CD, user1PrincipalId, actionToTakeNode)));
        assertTrue("User " + user1PrincipalId + " should have approval requests on the document",
                actionService.documentWillHaveAtLeastOneActionRequest(reportCriteria.build(),
                        Collections.singletonList(KewApiConstants.ACTION_REQUEST_APPROVE_REQ), false));

        reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        reportCriteria.setTargetPrincipalIds(Collections.singletonList(user1PrincipalId));
        actionToTakeNode = "Node1";
        reportCriteria.setActionsToTake(Collections.singletonList(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD, user1PrincipalId, actionToTakeNode)));
        DocumentDetail documentVO = KewApiServiceLocator.getWorkflowDocumentActionsService().executeSimulation(reportCriteria.build());
        assertTrue("User " + user1PrincipalId + " should have one or more approval requests on the document", documentVO.getActionRequests().size() > 0);

        reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        String delyeaPrincipalId = getPrincipalIdForName("delyea");
        reportCriteria.setTargetPrincipalIds(Collections.singletonList(user1PrincipalId));
        actionToTakeNode = "Node1";
        reportCriteria.setActionsToTake(Collections.singletonList(RoutingReportActionToTake.Builder.create(KewApiConstants.ACTION_TAKEN_APPROVED_CD, user1PrincipalId, actionToTakeNode)));
        documentVO = actionService.executeSimulation(reportCriteria.build());
        assertTrue("User " + delyeaPrincipalId + " should not have any requests on the document but executeSimulation() method should return all action requests anyway", documentVO.getActionRequests().size() > 0);

        document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "FutureRequestsDoc");
        document.setDoNotReceiveFutureRequests();
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(user1PrincipalId, document.getDocumentId());
        assertFalse(document.isApprovalRequested());

        // user1 should not have approval requested
        reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        reportCriteria.setTargetPrincipalIds(Collections.singletonList(user1PrincipalId));
        assertFalse("User " + user1PrincipalId + " should not have any approval request on the document", actionService.documentWillHaveAtLeastOneActionRequest(reportCriteria.build(), Collections.singletonList(KewApiConstants.ACTION_REQUEST_APPROVE_REQ), false));

        // user2 should have approval requested
        reportCriteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId());
        reportCriteria.setTargetPrincipalIds(Collections.singletonList(user2PrincipalId));
        assertTrue("User " + user2PrincipalId + " should have any approval request on the document", actionService.documentWillHaveAtLeastOneActionRequest(reportCriteria.build(), Collections.singletonList(KewApiConstants.ACTION_REQUEST_APPROVE_REQ), false));

    }
    
    /**
     * 
     * This method tests future action request with duplicate nodes. Both on a straight path
     * and a split path.  
     * 
     * This test was written because of KULRICE-4074.  Branch data was not being save when we
     * call saveRoutingData.
     * 
     * @throws Exception
     */
    @Test
    public void testFutureRequestsWithDuplicateNodesSplit() throws Exception {
    	// This file has the split node that mimmics KC
    	testFutureRequestsWithDuplicateNodesImpl("FutureRequestsConfig2.xml");    	    	    
    }
    
    @Test
    public void testFutureRequestsWithDuplicateNodesStraight() throws Exception {
    	// This file has no split node
    	testFutureRequestsWithDuplicateNodesImpl("FutureRequestsConfig3.xml");
    }
    
    private void testFutureRequestsWithDuplicateNodesImpl(String fileName) throws Exception{    	
    	this.loadXmlFile(this.getClass(), fileName);
    	

        String user1PrincipalId = getPrincipalIdForName("user1");
        String user2PrincipalId = getPrincipalIdForName("user2");
        String user3PrincipalId = getPrincipalIdForName("earl");

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "FutureRequestsDoc");
        document.route("");
       

        // Node1
        //user1 should have approval requested
        document = WorkflowDocumentFactory.loadDocument(user3PrincipalId, document.getDocumentId());
        assertTrue("should have approval status", document.isApprovalRequested());

        document.setReceiveFutureRequests();
        
        /*
         *  Uncomment the following line to duplicate the error in KC
         */        
        document.saveDocumentData();
        
        document.approve("route node 1");

        document =WorkflowDocumentFactory.loadDocument(user1PrincipalId, document.getDocumentId());
        assertFalse("should not have approval status 1", document.isApprovalRequested());

        document = WorkflowDocumentFactory.loadDocument(user2PrincipalId, document.getDocumentId());
        assertTrue("should have approval status 2", document.isApprovalRequested());
        document.approve("routing node 2");
        
        // Node3
        //user1 should have approval requested bc of future action requests
        document = WorkflowDocumentFactory.loadDocument(user3PrincipalId, document.getDocumentId());
        assertTrue("should have approval status 3", document.isApprovalRequested());
        document.approve("routing node 3");

    }
}
