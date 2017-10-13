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
package org.kuali.rice.kew.routemodule;

import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class RoutingReportServiceTest extends KEWTestCase {
    

    protected void loadTestData() throws Exception {
        loadXmlFile("RouteModuleConfig.xml");
    }

    /**
     * Tests the report() method against a sequential document type.
     */
    @Test public void testReportSequential() throws Exception {
        
        
        // route a document to the first node
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
        document.route("");
        
        // there should now be 1 active node and 2 pending requests on the document
        Collection activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        List requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("Should be one active node.", 1, activeNodeInstances.size());
        String activeNodeId = ((RouteNodeInstance)activeNodeInstances.iterator().next()).getRouteNodeInstanceId();
        assertEquals("Should be 2 pending requests.", 2, requests.size());
        
        // now, lets "get our report on", the WorkflowInfo.executeSimulation method will call the service's report method.
        RoutingReportCriteria criteria = RoutingReportCriteria.Builder.createByDocumentId(document.getDocumentId()).build();
        
        long start = System.currentTimeMillis();
        DocumentDetail documentDetail = KewApiServiceLocator.getWorkflowDocumentActionsService().executeSimulation(
                criteria);
        long end = System.currentTimeMillis();
        System.out.println("Time to run routing report: " + (end-start)+" milliseconds.");
        
        // document detail should have all of our requests on it, 2 activated approves, 1 initialized approve, 2 initialized acknowledges
        assertEquals("There should be 5 requests.", 5, documentDetail.getActionRequests().size());
        boolean approveToBmcgough = false;
        boolean approveToRkirkend = false;
        boolean approveToPmckown = false;
        boolean ackToTemay = false;
        boolean ackToJhopf = false;
        for (ActionRequest requestVO : documentDetail.getActionRequests()) {
            String netId = getPrincipalNameForId(requestVO.getPrincipalId()); 
            if (netId.equals("bmcgough")) {
                assertEquals("Should be approve.", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, requestVO.getActionRequested().getCode());
                assertEquals("Should be activated.", ActionRequestStatus.ACTIVATED, requestVO.getStatus());
                assertEquals("Wrong node name", SeqSetup.WORKFLOW_DOCUMENT_NODE, requestVO.getNodeName());
                approveToBmcgough = true;
            } else if (netId.equals("rkirkend")) {
                assertEquals("Should be approve.", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, requestVO.getActionRequested().getCode());
                assertEquals("Should be activated.", ActionRequestStatus.ACTIVATED, requestVO.getStatus());
                assertEquals("Wrong node name", SeqSetup.WORKFLOW_DOCUMENT_NODE, requestVO.getNodeName());
                approveToRkirkend = true;
            } else if (netId.equals("pmckown")) {
                assertEquals("Should be approve.", KewApiConstants.ACTION_REQUEST_APPROVE_REQ, requestVO.getActionRequested().getCode());
                assertEquals("Should be initialized.", ActionRequestStatus.INITIALIZED, requestVO.getStatus());
                assertEquals("Wrong node name", SeqSetup.WORKFLOW_DOCUMENT_2_NODE, requestVO.getNodeName());
                approveToPmckown = true;
            } else if (netId.equals("temay")) {
                assertEquals("Should be acknowledge.", KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested().getCode());
                assertEquals("Should be initialized.", ActionRequestStatus.INITIALIZED, requestVO.getStatus());
                assertEquals("Wrong node name", SeqSetup.ACKNOWLEDGE_1_NODE, requestVO.getNodeName());
                ackToTemay = true;
            } else if (netId.equals("jhopf")) {
                assertEquals("Should be acknowledge.", KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested().getCode());
                assertEquals("Should be initialized.", ActionRequestStatus.INITIALIZED, requestVO.getStatus());
                assertEquals("Wrong node name", SeqSetup.ACKNOWLEDGE_2_NODE, requestVO.getNodeName());
                ackToJhopf = true;
            } 
            assertNotNull(requestVO.getId());
        }
        assertTrue("There should be an approve to bmcgough", approveToBmcgough);
        assertTrue("There should be an approve to rkirkend", approveToRkirkend);
        assertTrue("There should be an approve to pmckown", approveToPmckown);
        assertTrue("There should be an ack to temay", ackToTemay);
        assertTrue("There should be an ack to jhopf", ackToJhopf);
        
        // assert that the report call didn't save any of the nodes or requests
        activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(document.getDocumentId());
        requests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("Should be one active node.", 1, activeNodeInstances.size());
        assertEquals("Should be at the same node.", activeNodeId, ((RouteNodeInstance)activeNodeInstances.iterator().next()).getRouteNodeInstanceId());
        assertEquals("Should be 2 pending requests.", 2, requests.size());
        
        // test reporting to a specified target node
        criteria = RoutingReportCriteria.Builder.createByDocumentIdAndTargetNodeName(document.getDocumentId(), SeqSetup.ACKNOWLEDGE_1_NODE).build();
        documentDetail = KewApiServiceLocator.getWorkflowDocumentActionsService().executeSimulation(criteria);
        
        // document detail should have all of our requests except for the final acknowledge
        assertEquals("There should be 4 requets.", 4, documentDetail.getActionRequests().size());
        // assert that we don't have an acknowledge to jhopf
        for (ActionRequest requestVO : documentDetail.getActionRequests()) {
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("jhopf"))) {
                fail("There should be no request to jhopf");
            }
        }
    }

    private static class SeqSetup {
        public static final String DOCUMENT_TYPE_NAME = "SeqDocType";
        public static final String ADHOC_NODE = "AdHoc";
        public static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
        public static final String WORKFLOW_DOCUMENT_2_NODE = "WorkflowDocument2";
        public static final String ACKNOWLEDGE_1_NODE = "Acknowledge1";
        public static final String ACKNOWLEDGE_2_NODE = "Acknowledge2";
    }
    
    private static class DynSetup {
        public static final String DOCUMENT_TYPE_NAME = "DynChartOrgDocType";
        public static final String INITIAL_NODE = "Initial";
        public static final String CHART_ORG_NODE = "ChartOrg";
        public static final String SPLIT_NODE_NAME = "Organization Split";
        public static final String JOIN_NODE_NAME = "Organization Join";
        public static final String REQUEST_NODE_NAME = "Organization Request";
    }
}
