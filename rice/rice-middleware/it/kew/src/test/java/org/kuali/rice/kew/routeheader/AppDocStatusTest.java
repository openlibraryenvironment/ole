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
package org.kuali.rice.kew.routeheader;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.docsearch.TestXMLSearchableAttributeString;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AppDocStatusTest extends KEWTestCase {
    	    
    protected void loadTestData() throws Exception {
    	super.loadTestData();
        loadXmlFile("AppDocStatusTestConfig.xml");
    }
        
    /**
     * 
     * This method performs several positive tests related to Application Document Status
     * For these tests the doctype definition defines a valid set of statuses.
     * It also defines two status transitions in the route path
     * It tests:
     * 	- That the AppDocStatus is properly set by the workflow engine during
     *    appropriate transitions.
     *  - That the AppDocStatus may be retrieved by the client API
     *  - That the AppDocStatus may be set by the client API
     *  - That a history of AppDocStatus transitions is created.
     * 
     */
    @Test public void testValidAppDocStatus() throws Exception {
    	// Create document
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc2");
    	document.saveDocumentData();
    	assertNotNull(document.getDocumentId());
    	assertTrue("Document should be initiatied", document.isInitiated());
    	assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
    	
    	// route document to first stop and check status, etc.
    	document.route("Test Routing.");    	
    	String appDocStatus = document.getDocument().getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Approval in Progress".equalsIgnoreCase(appDocStatus));
        
        // should have generated a request to "bmcgough"
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
    	Set<String> nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertTrue("Wrong node name.", document.getNodeNames().contains("DestinationApproval"));

    	// check action request
        List<ActionRequest> requests = document.getRootActionRequests();
        assertEquals(1, requests.size());
        ActionRequest request = requests.get(0);
        assertEquals(getPrincipalIdForName("bmcgough"), request.getPrincipalId());
        assertEquals(ActionRequestType.APPROVE, request.getActionRequested());
        assertEquals("DestinationApproval", request.getNodeName());
        assertTrue(document.isApprovalRequested());
        
        // approve the document to send it to its next route node
        document.approve("Test approve by bmcgough");
        
        // check status 
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        Document rh = document.getDocument();
    	appDocStatus = rh.getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Submitted".equalsIgnoreCase(appDocStatus));
        
        // should have generated a request to "temay"
    	assertTrue("Document should be enroute", document.isEnroute());
    	nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertTrue("Wrong node name.", nodeNames.contains("TravelerApproval"));
    	document.approve("Test approve by temay");
    	
    	// update the AppDocStatus via client API
        document.setApplicationDocumentStatus("Completed");
        document.saveDocumentData();

        // get a refreshed document and check it out
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
//        assertTrue("Document should be processed.", document.isProcessed());        
        rh = document.getDocument();
    	appDocStatus = rh.getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Completed".equalsIgnoreCase(appDocStatus));
    	
        // check app doc status transition history
        List<org.kuali.rice.kew.api.document.DocumentStatusTransition> history = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatusTransitionHistory(
                document.getDocumentId());
        
        assertEquals(3, history.size());
    	assertTrue("First History record has incorrect status", "Approval In Progress".equalsIgnoreCase(history.get(0).getNewStatus()));
    	assertTrue("Second History record has incorrect old status", "Approval In Progress".equalsIgnoreCase(
                history.get(1).getOldStatus()));
    	assertTrue("Second History record has incorrect new status", "Submitted".equalsIgnoreCase(history.get(1).getNewStatus()));
    	assertTrue("Third History record has incorrect old status", "Submitted".equalsIgnoreCase(history.get(2).getOldStatus()));
    	assertTrue("Third History record has incorrect new status", "Completed".equalsIgnoreCase(history.get(2).getNewStatus()));
               
    	// TODO when we are able to, we should also verify the RouteNodeInstances are correct
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue("Document should be final.", document.isFinal());
    }        

    /**
     * 
     * This method is similar to the above test, except that the doctype definition
     * does NOT specify a valid set of values.  This means that the value can be any valid string.
     * 
     * @throws Exception
     */
    @Test public void testAppDocStatusValuesNotDefined() throws Exception {
    	// Create document
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc1");
    	document.saveDocumentData();
    	assertNotNull(document.getDocumentId());
    	assertTrue("Document should be initiatied", document.isInitiated());
    	assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
    	
    	// route document to first stop and check status, etc.
    	document.route("Test Routing.");    	
    	Document rh = document.getDocument();
    	String appDocStatus = rh.getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Approval in Progress".equalsIgnoreCase(appDocStatus));
        
        // should have generated a request to "bmcgough"
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.isEnroute());
    	Set<String> nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertTrue("Wrong node name.", nodeNames.contains("step1"));

    	// check action request
        List<ActionRequest> requests = document.getRootActionRequests();
        assertEquals(1, requests.size());
        ActionRequest request = requests.get(0);
        assertEquals(getPrincipalIdForName("bmcgough"), request.getPrincipalId());
        assertEquals(ActionRequestType.APPROVE, request.getActionRequested());
        assertEquals("step1", request.getNodeName());
        assertTrue(document.isApprovalRequested());
        
        // approve the document to send it to its next route node
        document.approve("Test approve by bmcgough");
        
        // check status 
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        rh = document.getDocument();
    	appDocStatus = rh.getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Submitted".equalsIgnoreCase(appDocStatus));
        
        // should have generated a request to "temay"
    	assertTrue("Document should be enroute", document.isEnroute());
    	nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.size());
    	assertTrue("Wrong node name.", nodeNames.contains("step2"));
    	document.approve("Test approve by temay");
    	
    	// update the AppDocStatus via client API
        document.setApplicationDocumentStatus("Some Random Value");
        document.saveDocumentData();

        // get a refreshed document and check it out
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
//        assertTrue("Document should be processed.", document.isProcessed());        
        rh = document.getDocument();
    	appDocStatus = rh.getApplicationDocumentStatus();
    	assertTrue("Application Document Status:" + appDocStatus +" is invalid", "Some Random Value".equalsIgnoreCase(appDocStatus));
    	
        // check app doc status transition history
        List<org.kuali.rice.kew.api.document.DocumentStatusTransition> history = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatusTransitionHistory(
                document.getDocumentId());
        
        assertEquals(3, history.size());
    	assertTrue("First History record has incorrect status", "Approval In Progress".equalsIgnoreCase(history.get(0)
                .getNewStatus()));
    	assertTrue("Second History record has incorrect old status", "Approval In Progress".equalsIgnoreCase(
                history.get(1).getOldStatus()));
    	assertTrue("Second History record has incorrect new status", "Submitted".equalsIgnoreCase(history.get(1)
                .getNewStatus()));
    	assertTrue("Third History record has incorrect old status", "Submitted".equalsIgnoreCase(history.get(2).getOldStatus()));
    	assertTrue("Third History record has incorrect new status", "Some Random Value".equalsIgnoreCase(history.get(2)
                .getNewStatus()));
               
    	// TODO when we are able to, we should also verify the RouteNodeInstances are correct
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue("Document should be final.", document.isFinal());
    }        

    /**
     * 
     * This test attempts to set an invalid status value for a document that has a valid set
     * of statuses defined.
     * It expects to throw a WorkflowRuntimeException when attempting to set the invalid status value.
     * 
     * @throws Exception
     */
    @Test public void testInvalidAppDocStatusValue() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc2");
    	document.saveDocumentData();
    	assertNotNull(document.getDocumentId());
    	assertTrue("Document should be initiatied", document.isInitiated());
    	assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
    	    	
    	// update the AppDocStatus via client API
    	boolean gotException = false;
    	try {
    		document.setApplicationDocumentStatus("BAD STATUS");
    		document.saveDocumentData();
    	} catch (Throwable t){
    		gotException = true;
    		WorkflowRuntimeException ex = new WorkflowRuntimeException();
    		assertEquals("WrongExceptionType", t.getClass(), ex.getClass());
    	} finally {
    		assertTrue("Expected WorkflowRuntimeException not thrown.", gotException);
    		
    	}
    }

    /**
     *
     * This method is similar to the above test, except that the doctype definition
     * INHERITS a valid set of application document status values.
     *
     * @throws Exception
     */
    @Test public void testValidInheritedAppDocStatus() throws Exception {
        // Create document
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc3");
        document.saveDocumentData();
        assertNotNull(document.getDocumentId());
        assertTrue("Document should be initiatied", document.isInitiated());
        assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("TestAppDocStatusDoc3");
        assertTrue(ObjectUtils.isNotNull(documentType));
        assertTrue(ObjectUtils.isNotNull(documentType.getValidApplicationStatuses()));
        assertEquals(6,documentType.getValidApplicationStatuses().size());
        LOG.info("valid application status size: " + documentType.getValidApplicationStatuses().size());
        assertTrue(ObjectUtils.isNotNull(documentType.getApplicationStatusCategories()));
        assertEquals(0,documentType.getApplicationStatusCategories().size());
    }

    /**
     *
     * This method an inherited valid set of values and categories.
     *
     * @throws Exception
     */
    @Test public void testValidInheritedAppDocStatusWithCategories() throws Exception {
        // Create document
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc5");
        document.saveDocumentData();
        assertNotNull(document.getDocumentId());
        assertTrue("Document should be initiatied", document.isInitiated());
        assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("TestAppDocStatusDoc5");
        assertTrue(ObjectUtils.isNotNull(documentType));
        assertTrue(ObjectUtils.isNotNull(documentType.getValidApplicationStatuses()));
        LOG.info("valid application status size: " + documentType.getValidApplicationStatuses().size());
        assertEquals(6,documentType.getValidApplicationStatuses().size());
        assertTrue(ObjectUtils.isNotNull(documentType.getApplicationStatusCategories()));
        assertEquals(2,documentType.getApplicationStatusCategories().size());
    }

    /**
     *
     * This method tests a valid set of application document status values that are not inherited due to KEW status (KULRICE-8943).
     *
     * @throws Exception
     */
    @Test public void testInheritedAppDocStatusWithKEWStatus() throws Exception {
        // Create document
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestAppDocStatusDoc6");
        document.saveDocumentData();
        assertNotNull(document.getDocumentId());
        assertTrue("Document should be initiatied", document.isInitiated());
        assertTrue("Invalid route level.", document.getNodeNames().contains("Initiated"));
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("TestAppDocStatusDoc6");
        assertTrue(ObjectUtils.isNotNull(documentType));
        assertTrue(ObjectUtils.isNotNull(documentType.getValidApplicationStatuses()));
        assertEquals(0,documentType.getValidApplicationStatuses().size());
        LOG.info("valid application status size: " + documentType.getValidApplicationStatuses().size());
        assertTrue(ObjectUtils.isNotNull(documentType.getApplicationStatusCategories()));
        assertEquals(0,documentType.getApplicationStatusCategories().size());
    }

    @Test public void testSearching() throws InterruptedException {
        String documentTypeName = "TestAppDocStatusDoc1";

        String initiatorNetworkId = "rkirkend";
        Person initiator = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(initiatorNetworkId);
        String approverNetworkId = "bmcgough";
        Person approver = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(approverNetworkId);
        String travelerNetworkId = "temay";
        Person traveler = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(travelerNetworkId);

        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(initiator.getPrincipalId(), documentTypeName);
        workflowDocument.setTitle("Routing style");

        // no status, not routed
        assertAppDocStatuses(workflowDocument.getDocumentId(), new String [] { });
        assertSearchStatus(documentTypeName, initiator, "Approval in Progress", 0, 0);
        assertSearchStatus(documentTypeName, initiator, "Submitted", 0, 0);

        workflowDocument.route("routing this document.");

        DocumentRouteHeaderValue drhv = KEWServiceLocator.getRouteHeaderService().getRouteHeader(workflowDocument.getDocumentId());
        
        // should be in approval status
        assertAppDocStatuses(workflowDocument.getDocumentId(), new String [] { "Approval in Progress" });
        assertSearchStatus(documentTypeName, initiator, "Approval in Progress", 1, 0); // one document currently in "Approval in Progress" status
        assertSearchStatus(documentTypeName, initiator, "Approval in Progress", 1, drhv.getRouteStatusDate().getTime()); // one transition to "Approval in Progress" status around the time of routing
        assertSearchStatus(documentTypeName, initiator, "Submitted", 0, 0); // none in "Submitted" status

        // approve it out of the "Approval in Progress" state
        workflowDocument = WorkflowDocumentFactory.loadDocument(approver.getPrincipalId(), workflowDocument.getDocumentId());
        RequestedActions actions = workflowDocument.getRequestedActions();
        assertTrue(actions.isApproveRequested());
        workflowDocument.approve("destination approval");

        assertAppDocStatuses(workflowDocument.getDocumentId(), new String [] { "Approval in Progress", "Submitted" });
        assertSearchStatus(documentTypeName, approver, "Approval in Progress", 0, 0); // no documents currently in "Approval in Progress" status
        assertSearchStatus(documentTypeName, approver, "Approval in Progress", 1, drhv.getRouteStatusDate().getTime()); // one transition to "Approval in Progress" status around the time of routing
        assertSearchStatus(documentTypeName, approver, "Submitted", 1, 0); // one document currently in "Submitted" status
        assertSearchStatus(documentTypeName, approver, "Submitted", 1, drhv.getDateLastModified().getMillis()); // one transition to "Submitted" status around the time of approval

        // approve it out of the "Approval in Progress" state
        workflowDocument = WorkflowDocumentFactory.loadDocument(traveler.getPrincipalId(), workflowDocument.getDocumentId());
        actions = workflowDocument.getRequestedActions();
        assertTrue(actions.isApproveRequested());
        workflowDocument.approve("travel approval");

        // no final status, so no transition
        assertAppDocStatuses(workflowDocument.getDocumentId(), new String [] { "Approval in Progress", "Submitted" });
        assertSearchStatus(documentTypeName, traveler, "Approval in Progress", 0, 0); // no documents currently in "Approval in Progress" status
        assertSearchStatus(documentTypeName, traveler, "Approval in Progress", 1, drhv.getRouteStatusDate().getTime()); // one transition to "Approval in Progress" status around the time of routing
        assertSearchStatus(documentTypeName, traveler, "Submitted", 1, 0); // one document currently in "Submitted" status
        assertSearchStatus(documentTypeName, traveler, "Submitted", 1, drhv.getDateLastModified().getMillis()); // one transition to "Submitted" status around the time of approval
    }

    /**
     * Verifies the DocumentSearchService finds documents with a given app document status
     * @param documentTypeName the doc type
     * @param user user for lookup
     * @param appDocStatus the app doc status target
     * @param expected the expected number of results
     * @param changed the time the transition occurred (used for from/to range)
     */
    protected void assertSearchStatus(String documentTypeName, Person user, String appDocStatus, int expected, long changed) {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setApplicationDocumentStatus(appDocStatus);
        if (changed != 0) {
            criteria.setDateApplicationDocumentStatusChangedFrom(new DateTime(changed - 200));
            criteria.setDateApplicationDocumentStatusChangedTo(new DateTime(changed + 200));
        }
        DocumentSearchResults results = KEWServiceLocator.getDocumentSearchService().lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have " + expected + " documents.", expected, results.getSearchResults().size());
    }

    /**
     * Verifies the document application document status history
     * @param documentId the doc id
     * @param appDocStatuses list of app doc statuses in chronological order
     */
    protected void assertAppDocStatuses(String documentId, String[] appDocStatuses) {
        DocumentRouteHeaderValue drhv = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);

        String curStatus = KewApiConstants.UNKNOWN_STATUS;
        if (appDocStatuses.length > 0) {
            curStatus = appDocStatuses[appDocStatuses.length - 1];
        }
        assertEquals(curStatus, drhv.getAppDocStatus());

        List<DocumentStatusTransition> transitions = drhv.getAppDocStatusHistory();
        assertEquals(appDocStatuses.length, transitions.size());
        for (int i = 0; i < appDocStatuses.length; i++) {
            DocumentStatusTransition trans = transitions.get(i);
            assertEquals(appDocStatuses[i], trans.getNewAppDocStatus());
            String prevStatus = null;
            if (i > 0) {
                prevStatus = appDocStatuses[i - 1];
            }
            assertEquals(prevStatus, trans.getOldAppDocStatus());
        }
    }
}
