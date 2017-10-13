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

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class SequentialRoutingTest extends KEWTestCase {
    
    
    private static final String DOCUMENT_TYPE_NAME = "SeqDocType";
	private static final String ADHOC_NODE = "AdHoc";
	private static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
    private static final String ACKNOWLEDGE_1_NODE = "Acknowledge1";
    private static final String ACKNOWLEDGE_2_NODE = "Acknowledge2";
	    
    protected void loadTestData() throws Exception {
        loadXmlFile("EngineConfig.xml");
    }
        
    @Test public void testSequentialRoute() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
    	document.saveDocumentData();
    	assertNotNull(document.getDocumentId());

        DocumentRouteHeaderValue drhv = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        System.out.println(drhv.getInitialRouteNodeInstances().size());

    	assertTrue("Document should be initiatied", document.isInitiated());
    	Set<String> nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertEquals("Wrong node name.", ADHOC_NODE, nodeNames.iterator().next());
    	document.route("Routing sequentially.");
        
        // should have generated a request to "bmcgough"
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
    	nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertEquals("Wrong node name.", WORKFLOW_DOCUMENT_NODE, nodeNames.iterator().next());
        List<ActionRequest> requests = document.getRootActionRequests();
        assertEquals(1, requests.size());
        ActionRequest request = requests.get(0);
        assertEquals(getPrincipalIdForName("bmcgough"), request.getPrincipalId());
        assertEquals(ActionRequestType.APPROVE, request.getActionRequested());
        TestUtilities.assertAtNode(document, WORKFLOW_DOCUMENT_NODE);
        assertTrue(document.isApprovalRequested());
        document.approve("Test approve by bmcgough");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        assertTrue("Document should be processed.", document.isProcessed());
        requests = document.getRootActionRequests();
        assertEquals(3, requests.size());
        boolean toTemay = false;
        boolean toJhopf = false;
        for (int i = 0; i < requests.size(); i++) {
            ActionRequest requestVO = requests.get(i);
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("temay"))) {
                toTemay = true;
                assertEquals(ActionRequestType.ACKNOWLEDGE, requestVO.getActionRequested());
                assertEquals(ACKNOWLEDGE_1_NODE, requestVO.getNodeName());
                assertEquals(ActionRequestStatus.ACTIVATED, requestVO.getStatus());
            } else if (requestVO.getPrincipalId().equals(getPrincipalIdForName("jhopf"))) {
                toJhopf = true;
                assertEquals(ActionRequestType.ACKNOWLEDGE, requestVO.getActionRequested());
                assertEquals(ACKNOWLEDGE_2_NODE, requestVO.getNodeName());
                assertEquals(ActionRequestStatus.ACTIVATED, requestVO.getStatus());
            }
        }
        assertTrue("Should be an acknowledge to temay", toTemay);
        assertTrue("Should be an acknowledge to jhopf", toJhopf);
//        assertEquals(ACKNOWLEDGE_2_NODE, document.getRouteHeader().getNodeNames()[0]);
        // have temay take her acknowledge
        document.acknowledge("Temay taking acknowledge");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("Document should be processed.", document.isProcessed());
        requests = document.getRootActionRequests();
        toTemay = false;
        toJhopf = false;
        for (int i = 0; i < requests.size(); i++) {
            ActionRequest requestVO = requests.get(i);
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("temay"))) {
                toTemay = true;
                assertEquals(ActionRequestType.ACKNOWLEDGE, requestVO.getActionRequested());
                assertEquals(ActionRequestStatus.DONE, requestVO.getStatus());
            } else if (requestVO.getPrincipalId().equals(getPrincipalIdForName("jhopf"))) {
                toJhopf = true;
                assertEquals(ActionRequestType.ACKNOWLEDGE, requestVO.getActionRequested());
                assertEquals(ACKNOWLEDGE_2_NODE, requestVO.getNodeName());
                assertEquals(ActionRequestStatus.ACTIVATED, requestVO.getStatus());
            }
        }
        assertTrue("Should be a DONE acknowledge to temay", toTemay);
        assertTrue("Should be an acknowledge to jhopf", toJhopf);
        // have jhopf take his acknowledge, this should cause the document to go final
        document.acknowledge("Jhopf taking acknowledge");
        
    	// TODO when we are able to, we should also verify the RouteNodeInstances are correct
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue("Document should be final.", document.isFinal());
        
        verifyRoutingPath(document.getDocumentId());
    }        

    private void verifyRoutingPath(String documentId) {
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        List initial = document.getInitialRouteNodeInstances();
        assertEquals(1, initial.size());
        RouteNodeInstance adhoc = (RouteNodeInstance)initial.get(0);
        assertEquals(ADHOC_NODE, adhoc.getRouteNode().getRouteNodeName());
        assertEquals(0, adhoc.getPreviousNodeInstances().size());
        
        List next = adhoc.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance wd = (RouteNodeInstance)next.get(0);
        assertEquals(WORKFLOW_DOCUMENT_NODE, wd.getRouteNode().getRouteNodeName());
        assertEquals(1, wd.getPreviousNodeInstances().size());
        
        next = wd.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance ack1 = (RouteNodeInstance)next.get(0);
        assertEquals(ACKNOWLEDGE_1_NODE, ack1.getRouteNode().getRouteNodeName());
        assertEquals(1, ack1.getPreviousNodeInstances().size());
        
        next = ack1.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance ack2 = (RouteNodeInstance)next.get(0);
        assertEquals(ACKNOWLEDGE_2_NODE, ack2.getRouteNode().getRouteNodeName());
        assertEquals(1, ack2.getPreviousNodeInstances().size());
        
        next = ack2.getNextNodeInstances();
        assertEquals(0, next.size());
    }

}
