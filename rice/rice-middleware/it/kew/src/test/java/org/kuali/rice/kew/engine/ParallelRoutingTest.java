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
package org.kuali.rice.kew.engine;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;

public class ParallelRoutingTest extends KEWTestCase {
    
    private static final String DOCUMENT_TYPE_NAME = "ParallelDocType";
    private static final String PARALLEL_EMPTY_DOCUMENT_TYPE_NAME = "ParallelEmptyDocType";
    private static final String PARALLEL_EMPTY_DOCUMENT_TYPE_2_NAME = "ParallelEmptyDocType2";
    private static final String ACKNOWLEDGE_1_NODE = "Acknowledge1";
    private static final String WORKFLOW_DOCUMENT_2_NODE = "WorkflowDocument2";
    private static final String WORKFLOW_DOCUMENT_3_NODE = "WorkflowDocument3";
    private static final String JOIN_NODE = "Join";
    private static final String WORKFLOW_DOCUMENT_FINAL_NODE = "WorkflowDocumentFinal";
	
    protected void loadTestData() throws Exception {
        loadXmlFile("EngineConfig.xml");
    }

    @Test public void testParallelRoute() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
        document.saveDocumentData();
        assertTrue("Document should be initiated", document.isInitiated());
        assertEquals("Should be no action requests.", 0, document.getRootActionRequests().size());
        Collection nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 1, nodeInstances.size());
        document.route("Routing for parallel");
        
        // should have generated a request to "bmcgough"
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Incorrect pending action requests.", 1, actionRequests.size());
        ActionRequestValue bRequest = (ActionRequestValue)actionRequests.get(0);
        assertNotNull("Should have been routed through node instance.", bRequest.getNodeInstance());
        assertTrue(document.isApprovalRequested());
        
        document.approve("Approving test");
        
        // document should split at this point and generate an ack to temay and approves to rkirkend and pmckown
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Incorrect pending action requests.", 3, actionRequests.size());
        boolean isToTemay = false;
        boolean isToPmckown = false;
        boolean isToRkirkend = false;
        for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
            if (actionRequest.getPrincipalId().equals(getPrincipalIdForName("temay"))) {
                isToTemay = true;
                assertEquals("Request should be activated.", ActionRequestStatus.ACTIVATED.getCode(), actionRequest.getStatus());
                assertEquals("Wrong action requested.", KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, actionRequest.getActionRequested());
                assertNotNull("Should have been routed through node instance.", actionRequest.getNodeInstance());
                assertEquals("Invalid node.", ACKNOWLEDGE_1_NODE, actionRequest.getNodeInstance().getRouteNode().getRouteNodeName());
            }
            if (actionRequest.getPrincipalId().equals(getPrincipalIdForName("rkirkend"))) {
                isToRkirkend = true;
                assertEquals("Request should be activated.", ActionRequestStatus.ACTIVATED.getCode(), actionRequest.getStatus());
                assertEquals("Wrong action requested.", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionRequest.getActionRequested());
                assertNotNull("Should have been routed through node instance.", actionRequest.getNodeInstance());
                assertEquals("Invalid node.", WORKFLOW_DOCUMENT_2_NODE, actionRequest.getNodeInstance().getRouteNode().getRouteNodeName());
            }
            if (actionRequest.getPrincipalId().equals(getPrincipalIdForName("pmckown"))) {
                isToPmckown = true;
                assertEquals("Request should be activated.", ActionRequestStatus.ACTIVATED.getCode(), actionRequest.getStatus());
                assertEquals("Wrong action requested.", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, actionRequest.getActionRequested());
                assertNotNull("Should have been routed through node instance.", actionRequest.getNodeInstance());
                assertEquals("Invalid node.", WORKFLOW_DOCUMENT_3_NODE, actionRequest.getNodeInstance().getRouteNode().getRouteNodeName());
            }
        }
        assertTrue("No request to temay.", isToTemay);
        assertTrue("No request to pmckown.", isToPmckown);
        assertTrue("No request to rkirkend.", isToRkirkend);
        
        // check that we are at both nodes, one in each branch
        Set<String> nodeNames = document.getNodeNames();
        assertEquals("Wrong number of node names.", 2, nodeNames.size() );
        boolean isNode2 = false;
        boolean isNode3 = false;
        for (String name : nodeNames) {
            if (name.equals(WORKFLOW_DOCUMENT_2_NODE)) {
                isNode2 = true;
            }
            if (name.equals(WORKFLOW_DOCUMENT_3_NODE)) {
                isNode3 = true;
            }
        }
        assertTrue("Not at node2.", isNode2);
        assertTrue("Not at node3.", isNode3);
        nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 2, nodeInstances.size());
        Iterator iterator = nodeInstances.iterator();
        RouteNodeInstance instance1 = (RouteNodeInstance)iterator.next();
        RouteNodeInstance instance2 = (RouteNodeInstance)iterator.next();
        assertNotNull("Node should be in branch.", instance1.getBranch());
        assertNotNull("Node should be in branch.", instance2.getBranch());
        assertTrue("Branches should be different.", !instance1.getBranch().getBranchId().equals(instance2.getBranch().getBranchId()));
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Should have request.", document.isApprovalRequested());
        document.approve("Git-r-dun");
        
        nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 2, nodeInstances.size());
        boolean isAtJoin = false;
        boolean isAtWD3 = false;
        for (Iterator iter = nodeInstances.iterator(); iter.hasNext();) {
            RouteNodeInstance nodeInstance = (RouteNodeInstance) iter.next();
            if (nodeInstance.getRouteNode().getRouteNodeName().equals(JOIN_NODE)) {
                assertEquals("Join branch should be split branch.", instance1.getBranch().getParentBranch().getBranchId(), nodeInstance.getBranch().getBranchId());
                isAtJoin = true;
            }
            if (nodeInstance.getRouteNode().getRouteNodeName().equals(WORKFLOW_DOCUMENT_3_NODE)) {
                isAtWD3 = true;
            }
        }
        assertTrue("Not at join", isAtJoin);
        assertTrue("Not at WD3", isAtWD3);
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        assertTrue("Should have request.", document.isApprovalRequested());
        document.approve("Do it.");
        
        nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 1, nodeInstances.size());
        boolean isAtWDF = false;
        for (Iterator iter = nodeInstances.iterator(); iter.hasNext();) {
            RouteNodeInstance nodeInstance = (RouteNodeInstance) iter.next();
            if (nodeInstance.getRouteNode().getRouteNodeName().equals(WORKFLOW_DOCUMENT_FINAL_NODE)) {
                isAtWDF = true;
            }
        }
        assertTrue("Not at WDF", isAtWDF);
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        assertTrue("Should still be enroute.", document.isEnroute());
        assertTrue("Should have request.", document.isApprovalRequested());
        document.approve("I'm the last approver");
        
        assertTrue("Document should be processed.", document.isProcessed());
        nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        //commented out because the final RouteNodeInstance is now not active when the doc goes final
//        assertEquals("Wrong number of active nodes.", 1, nodeInstances.size());
//        isAtWDF = false;
//        for (Iterator iter = nodeInstances.iterator(); iter.hasNext();) {
//            RouteNodeInstance nodeInstance = (RouteNodeInstance) iter.next();
//            if (nodeInstance.getRouteNode().getRouteNodeName().equals(WORKFLOW_DOCUMENT_FINAL_NODE)) {
//                isAtWDF = true;
//            }
//        }
//        assertTrue("Not at WDF", isAtWDF);
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        assertTrue("Should have request.", document.isAcknowledgeRequested());
        document.acknowledge("");
        assertTrue(document.isFinal());
    }
    
    /**
     * Tests that the document route past the join properly when there are parallel branches that don't generate requests.
     * This was coded in response to a bug found while testing with ERA in order to track it down and fix it.
     */
    @Test public void testEmptyParallelBranches() throws Exception {
        
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), PARALLEL_EMPTY_DOCUMENT_TYPE_NAME);
        document.saveDocumentData();
        assertTrue("Document should be initiated", document.isInitiated());
        assertEquals("Should be no action requests.", 0, document.getRootActionRequests().size());
        Collection<? extends Object> nodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 1, nodeInstances.size());
        document.route("");
        
        // should have generated a request to "bmcgough"
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Incorrect pending action requests.", 1, actionRequests.size());
        ActionRequestValue bRequest = (ActionRequestValue)actionRequests.get(0);
        assertNotNull("Should have been routed through node instance.", bRequest.getNodeInstance());
        assertTrue(document.isApprovalRequested());
        
        document.approve("");
        
        // now the document should have split, passed through nodes in each branch which didn't generate requests,
        // and then passed the join node and generated requests at WorkflowDocumentFinal
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        assertTrue(document.isApprovalRequested());
        
    }
    
    /**
     * This runs the test with the adhoc approvers branch second instead of first
     *//*
    public void testEmptyParallelBranchesSwitched() throws Exception {
        
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(new NetworkIdVO("ewestfal"), PARALLEL_EMPTY_DOCUMENT_TYPE_2_NAME);
        document.saveDocumentData();
        assertTrue("Document should be initiated", document.isInitiated());
        assertEquals("Should be no action requests.", 0, document.getActionRequests().length);
        assertEquals("Invalid route level.", new Integer(0), document.getRouteHeader().getDocRouteLevel());
        Collection nodeInstances = SpringServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        assertEquals("Wrong number of active nodes.", 1, nodeInstances.size());
        document.route("");
        
        // should have generated a request to "bmcgough"
        document = WorkflowDocumentFactory.loadDocument(new NetworkIdVO("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        List actionRequests = TestUtilities.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Incorrect pending action requests.", 1, actionRequests.size());
        ActionRequestValue bRequest = (ActionRequestValue)actionRequests.get(0);
        assertNotNull("Should have been routed through node instance.", bRequest.getRouteNodeInstance());
        assertTrue(document.isApprovalRequested());
        
        document.approve("");
        
        // now the document should have split, passed through nodes in each branch which didn't generate requests,
        // and then passed the join node and generated requests at WorkflowDocumentFinal
        document = WorkflowDocumentFactory.loadDocument(new NetworkIdVO("xqi"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
        assertTrue(document.isApprovalRequested());
        
    }*/
    
    @Test public void testAdhocApproversJoinScenario() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "AdHocApproversDocType");
        document.route("");
        
        // should send an approve to bmcgough
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Bmcgough should have approve request.", document.isApprovalRequested());
        document.approve("");
        
        // at this point the document should pass the split, and end up at the WorkflowDocument2 node and the AdHocApproversJoin node
        // after bypassing the AdHocJoinPoint
        Set<String> nodeNames = document.getNodeNames();
        assertEquals("There should be two node names.", 2, nodeNames.size());
        assertTrue("Should be at WorkflowDocument2 node.", nodeNames.contains("WorkflowDocument2"));
        assertTrue("Should be at WorkflowDocument2 node.", nodeNames.contains("AdHocApproversJoin"));
        
        // pmckown has the request at the adhoc approvers node, if we approve as him then the document should _not_ transition out
        // of it's current nodes
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pmckown"), document.getDocumentId());
        assertTrue("Pmckown should have approve request.", document.isApprovalRequested());
        document.approve("");
        
        // the document should still be at the same nodes
        nodeNames = document.getNodeNames();
        assertEquals("There should be two node names.", 2, nodeNames.size());
        assertTrue("Should be at WorkflowDocument2 node.", nodeNames.contains("WorkflowDocument2"));
        assertTrue("Should be at WorkflowDocument2 node.", nodeNames.contains("AdHocApproversJoin"));
    
        // at WorkflowDocument2, rkirkend is the approver, if we approve as him we should end up at the WorkflowDocumentFinal node
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Rkirkend should have approve request.", document.isApprovalRequested());
        document.approve("");
        
        // the document should now be at WorkflowDocumentFinal with a request to xqi
        nodeNames = document.getNodeNames();
        assertEquals("There should be one node name.", 1, nodeNames.size());
        assertTrue("Should be at WorkflowDocumentFinal node.", nodeNames.contains("WorkflowDocumentFinal"));
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("xqi"), document.getDocumentId());
        assertTrue("Document should still be enroute.", document.isEnroute());
        document.approve("");
        assertTrue("Document should now be final.", document.isFinal());
        
    }
    
}
