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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

@BaselineMode(Mode.CLEAR_DB)
public class SaveActionEventTest extends KEWTestCase {
    
    public static final String DOCUMENT_TYPE_NAME = "SaveActionEventTest";
    public static final String DOCUMENT_TYPE_NAME_NON_INITIATOR = "SaveActionEventTestNonInitiator";
    public static final String ADHOC_NODE = "AdHoc";
    public static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
    
    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }
    
    @Test public void testSaveActionEvent() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
        document.saveDocumentData();
        assertTrue("Document should be initiated.", document.isInitiated());
        List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("There should be no action requests.", 0, actionRequests.size());
        assertTrue("Document should be initiated.", document.isInitiated());
        document.saveDocument("");
        
        // document should be SAVED now at the AdHoc node
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertEquals("Document should be at AdHoc node.", ADHOC_NODE, document.getNodeNames().iterator().next());
        assertTrue("Document should be SAVED.", document.isSaved());
        // there should now be one COMPLETE request to the initiator
        actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("There should be one COMPLETE request to the initiator.", 1, actionRequests.size());
        assertTrue("Initiator should have complete request.", document.isCompletionRequested());
        ActionRequestValue savedRequest = (ActionRequestValue)actionRequests.get(0);
        assertNotNull(savedRequest);
        assertTrue("Saved request should be a complete request.", savedRequest.isCompleteRequst());
        assertEquals("Request should be at the AdHoc node.", ADHOC_NODE, savedRequest.getNodeInstance().getName());
        
        // if we try and call route document as rkirkend, it should throw an InvalidActionTakenException
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        try {
            document.route("");
            fail("RouteDocument should have thrown an exception because we aren't the initiator");
        } catch (InvalidActionTakenException e) {}
        
        
        // now, route document as the initiator
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.route("Routing Rowdy Roddy Pipper");
        
        // document should be marked as ENROUTE, move to the WorkflowDocument node, 
        // and approve requests to rkirkend and bmcgough should be generated
        actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(document.getDocumentId());
        assertEquals("Should be 2 pending requests.", 2, actionRequests.size());
        // rkirkend should have request
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be in routing.", document.isEnroute());
        assertTrue("rkirkend should have approve request.", document.isApprovalRequested());
        // bmcgough should have request
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be in routing.", document.isEnroute());
        assertTrue("bmcgough should have approve request.", document.isApprovalRequested());
        
        // check node and requests
        assertEquals("Document should be at WorkflowDocument node.", WORKFLOW_DOCUMENT_NODE, document.getNodeNames().iterator().next());
        for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue request = (ActionRequestValue) iterator.next();
            assertNotNull(request.getNodeInstance());
            assertEquals("Request should be at WorkflowDocument node.", WORKFLOW_DOCUMENT_NODE, request.getNodeInstance().getName());
        }
        
        // now, since saveDocument effectively sets the status to saved and generates a complete request, let make sure that we can
        // take a complete or approve action against the doc to make it transition
        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
        document.saveDocument("");
        assertTrue("Document should be saved.", document.isSaved());
        assertTrue("Should have complete request.", document.isCompletionRequested());
        assertEquals("Document should be at AdHoc node.", ADHOC_NODE, document.getNodeNames().iterator().next());
        // take the complete action
        document.complete("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be enroute.", document.isEnroute());
        assertTrue("Should have approve request.", document.isApprovalRequested());
        assertEquals("Document should be at WorkflowDocument node.", WORKFLOW_DOCUMENT_NODE, document.getNodeNames().iterator().next());

        // try above scenario with approve because approve should count for completion
        document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
        document.saveDocument("");
        assertTrue("Document should be saved.", document.isSaved());
        assertTrue("Should have complete request.", document.isCompletionRequested());
        assertTrue("Should also indicate approval is valid.", document.isApprovalRequested());
        assertEquals("Document should be at AdHoc node.", ADHOC_NODE, document.getNodeNames().iterator().next());
        // take the approve action
        document.approve("");
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("Document should be enroute.", document.isEnroute());
        assertTrue("Should have approve request.", document.isApprovalRequested());
        assertEquals("Document should be at WorkflowDocument node.", WORKFLOW_DOCUMENT_NODE, document.getNodeNames().iterator().next());
    }
    
    
    /**
     * Tests for when INITIATOR_MUST_SAVE policy is equal to true (default value).  In this case if non-initiator user
     * attempts a save of a document with this policy an exception should be thrown
     */
    @Test public void testDefaultInitiatorMustSavePolicy() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
    	document.saveDocumentData();
    	
    	// verify that there are no requests that have been generated
    	List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
    	assertEquals("There should be no action requests.", 0, actionRequests.size());
    	
    	// try saving as a user who's not ewestfal
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
    	assertTrue(document.isInitiated());
    	try {
    		document.saveDocument("");
    		fail("A WorkflowException should have been thrown.");
    	} catch (InvalidActionTakenException e) {
    		e.printStackTrace();
    	}
    	
    	// ensure that the request did not get generated
    	actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
    	assertEquals("There should be no action requests.", 0, actionRequests.size());
    	
    	// now save it as the intiator and it should be successful and generate a request
    	document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	document.saveDocument("");
    	assertTrue(document.isSaved());
    	actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
    	assertEquals("There should be one action request.", 1, actionRequests.size());
    }

    /**
     * Tests for when INITIATOR_MUST_SAVE policy is equal to false.  In this case if non-initiator user
     * attempts a save of a document with this policy an exception should NOT be thrown
     */
    @Test public void testFalseInitiatorMustSavePolicy() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME_NON_INITIATOR);
        document.saveDocumentData();
        
        // verify that there are no requests that have been generated
        List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("There should be no action requests.", 0, actionRequests.size());
        
        // try saving as a user who's not ewestfal
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue(document.isInitiated());
        document.saveDocument("");
        
        // ensure that the document was saved and the request was generated
        assertTrue(document.isSaved());
        actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(document.getDocumentId());
        assertEquals("There should be one action request.", 1, actionRequests.size());
    }
}
