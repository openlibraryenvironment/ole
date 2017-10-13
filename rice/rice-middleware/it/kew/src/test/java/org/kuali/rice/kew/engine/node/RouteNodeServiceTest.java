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
package org.kuali.rice.kew.engine.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;

public class RouteNodeServiceTest extends KEWTestCase {

    private RouteNodeService routeNodeService;
    
    protected void setUpAfterDataLoad() throws Exception {
        routeNodeService = KEWServiceLocator.getRouteNodeService();
    }

    protected void loadTestData() throws Exception {
        loadXmlFile("NodeConfig.xml");
    }
    
    @Test public void testGetFlattenedNodeInstances() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SeqDocType");
        document.saveDocument("");
        
        DocumentRouteHeaderValue serverDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        List routeNodeInstances = routeNodeService.getFlattenedNodeInstances(serverDocument, true);
        assertEquals(1, routeNodeInstances.size());
        assertEquals("AdHoc", ((RouteNodeInstance)routeNodeInstances.get(0)).getName());
        
        document.blanketApprove("");
        assertTrue(document.isProcessed());
        
        serverDocument = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        routeNodeInstances = routeNodeService.getFlattenedNodeInstances(serverDocument, true);
        assertEquals(4, routeNodeInstances.size());
        assertEquals("AdHoc", ((RouteNodeInstance)routeNodeInstances.get(0)).getName());
        assertEquals("WorkflowDocument", ((RouteNodeInstance)routeNodeInstances.get(1)).getName());
        assertEquals("Acknowledge1", ((RouteNodeInstance)routeNodeInstances.get(2)).getName());
        assertEquals("Acknowledge2", ((RouteNodeInstance)routeNodeInstances.get(3)).getName());
    }

    @Test public void testSearchNodeGraphSequentailBackward() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SeqDocType");
    	document.blanketApprove("", "WorkflowDocument");
    	List activeNodeInstances = routeNodeService.getActiveNodeInstances(document.getDocumentId());
    	NodeGraphSearchCriteria criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, activeNodeInstances, "AdHoc");
    	NodeGraphSearchResult result = routeNodeService.searchNodeGraph(criteria);
    	assertEquals("Path should have two nodes.", 2, result.getPath().size());
    	RouteNodeInstance resultNodeInstance = result.getResultNodeInstance();
    	assertNotNull("Should have a resulting node instance.", resultNodeInstance);
    	assertEquals("Result node should be the adhoc node.", "AdHoc", resultNodeInstance.getName());
    	
    	// take it to the end
    	document.blanketApprove("");
    	assertTrue("Document should be processed.", document.isProcessed());
    	List terminalNodeInstances = routeNodeService.getTerminalNodeInstances(document.getDocumentId());
    	criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, terminalNodeInstances, "AdHoc");
    	result = routeNodeService.searchNodeGraph(criteria);
    	assertEquals("Path should have 4 nodes.", 4, result.getPath().size());
    	resultNodeInstance = result.getResultNodeInstance();
    	assertNotNull("Should have a resulting node instance.", resultNodeInstance);
    	assertEquals("Result node should be the adhoc node.", "AdHoc", resultNodeInstance.getName());
    	
    	// now try searching from the Ack1 node for the WorkflowDocument node
    	RouteNodeInstance ack1NodeInstance = null;
    	for (Iterator iterator = result.getPath().iterator(); iterator.hasNext(); ) {
			RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
			if (nodeInstance.getName().equals("Acknowledge1")) {
				ack1NodeInstance = nodeInstance;
				break;
			}
		}
    	assertNotNull("Could not locate the Acknowledge1 node in the path.", ack1NodeInstance);
    	List<RouteNodeInstance> startNodes = new ArrayList<RouteNodeInstance>();
    	startNodes.add(ack1NodeInstance);
    	criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, startNodes, "WorkflowDocument");
    	result = routeNodeService.searchNodeGraph(criteria);
    	// since we started at 'Acknowledge1' there should just be 'Acknowledge1' and 'WorkflowDocument' in the path
    	assertEquals("Path should have 2 nodes.", 2, result.getPath().size());
    	resultNodeInstance = result.getResultNodeInstance();
    	assertNotNull("Should have a resulting node instance.", resultNodeInstance);
    	assertEquals("Result node should be the workflow document node.", "WorkflowDocument", resultNodeInstance.getName());
    }
    
    @Test public void testSearchNodeGraphParallelBackward() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "ParallelDocType");
    	document.blanketApprove("", new String[] { "WorkflowDocument2", "WorkflowDocument3" });
    	List activeNodeInstances = routeNodeService.getActiveNodeInstances(document.getDocumentId());
    	assertEquals("Should be 2 active nodes.", 2, activeNodeInstances.size());
    	Set<? extends String> nodeNames = TestUtilities.createNodeInstanceNameSet(activeNodeInstances);
    	assertTrue("Should be at WorkflowDocument2 node.", nodeNames.contains("WorkflowDocument2"));
    	assertTrue("Should be at the WorkflowDocument3 node.", nodeNames.contains("WorkflowDocument3"));
    	
    	
    	// search backward to the adhoc node
    	NodeGraphSearchCriteria criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, activeNodeInstances, "AdHoc");
    	NodeGraphSearchResult result = routeNodeService.searchNodeGraph(criteria);
    	assertEquals("Path should have eight nodes.", 8, result.getPath().size());
    	RouteNodeInstance resultNodeInstance = result.getResultNodeInstance();
    	assertNotNull("Should have a resulting node instance.", resultNodeInstance);
    	assertEquals("Result node should be the adhoc node.", "AdHoc", resultNodeInstance.getName());
    	nodeNames = TestUtilities.createNodeInstanceNameSet(result.getPath());
    	// the following nodes should be in the list of 8
    	assertTrue(nodeNames.contains("WorkflowDocument3"));
    	assertTrue(nodeNames.contains("WorkflowDocument5"));
    	assertTrue(nodeNames.contains("WorkflowDocument2"));
    	assertTrue(nodeNames.contains("Acknowledge1"));
    	assertTrue(nodeNames.contains("WorkflowDocument4"));
    	assertTrue(nodeNames.contains("Split"));
    	assertTrue(nodeNames.contains("WorkflowDocument"));
    	assertTrue(nodeNames.contains("AdHoc"));
    	
    	// extract our active node instances
    	RouteNodeInstance workflowDocument2Node = null;
    	RouteNodeInstance workflowDocument3Node = null;
    	for (Iterator iterator = activeNodeInstances.iterator(); iterator.hasNext(); ) {
			RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
			if (nodeInstance.getName().equals("WorkflowDocument2")) {
				workflowDocument2Node = nodeInstance;
			} else if (nodeInstance.getName().equals("WorkflowDocument3")) {
				workflowDocument3Node = nodeInstance;
			}
		}
    	assertNotNull("Could not locate WorkflowDocument2 node.", workflowDocument2Node);
    	assertNotNull("Could not locate WorkflowDocument3 node.", workflowDocument3Node);
    	
    	// now try searching backward for WorkflowDocument4 from WorkflowDocument2, this should keep us on the branch
    	List<RouteNodeInstance> startNodeInstances = new ArrayList<RouteNodeInstance>();
    	startNodeInstances.add(workflowDocument2Node);
    	criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, activeNodeInstances, "WorkflowDocument4");
    	result = routeNodeService.searchNodeGraph(criteria);
    	assertEquals("Path should have three nodes.", 3, result.getPath().size());
    	resultNodeInstance = result.getResultNodeInstance();
    	assertEquals("Result node should be the WorkflowDocument4 node.", "WorkflowDocument4", resultNodeInstance.getName());
    	nodeNames = TestUtilities.createNodeInstanceNameSet(result.getPath());
    	// the following nodes should be in the list of 3
    	assertTrue(nodeNames.contains("WorkflowDocument2"));
    	assertTrue(nodeNames.contains("Acknowledge1"));
    	assertTrue(nodeNames.contains("WorkflowDocument4"));
    	
    	// try searching backward for WorkflowDocument5
    	startNodeInstances = new ArrayList<RouteNodeInstance>();
    	startNodeInstances.add(workflowDocument3Node);
    	criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD, activeNodeInstances, "WorkflowDocument5");
    	result = routeNodeService.searchNodeGraph(criteria);
    	assertEquals("Path should have two nodes.", 2, result.getPath().size());
    	resultNodeInstance = result.getResultNodeInstance();
    	assertEquals("Result node should be the WorkflowDocument5 node.", "WorkflowDocument5", resultNodeInstance.getName());
    	nodeNames = TestUtilities.createNodeInstanceNameSet(result.getPath());
    	// the following nodes should be in the list of 2
    	assertTrue(nodeNames.contains("WorkflowDocument3"));
    	assertTrue(nodeNames.contains("WorkflowDocument5"));
    }
    
    /**
     * currently searching forward does not work and needs to be implemented, we'll stub in a test
     * that shows it throws an UnsupportedOperationException and then this test can be modified when the
     * functionality is implemented.
     */
    @Test public void testSearchNodeGraphForward() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SeqDocType");
    	document.route("");
    	List initialNodeInstances = KEWServiceLocator.getRouteNodeService().getInitialNodeInstances(document.getDocumentId());
    	NodeGraphSearchCriteria criteria = new NodeGraphSearchCriteria(NodeGraphSearchCriteria.SEARCH_DIRECTION_FORWARD, initialNodeInstances, "WorkflowDocument");
    	try {
    		routeNodeService.searchNodeGraph(criteria);
    		fail("Should have thrown UnsupportedOperationException");
    	} catch (UnsupportedOperationException e) {
    	}
    }

}
