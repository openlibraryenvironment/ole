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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

public class RouteDocumentTest extends KEWTestCase {

	public static final String DOCUMENT_TYPE_NAME = "BlanketApproveSequentialTest";
    public static final String DOCUMENT_TYPE_POLICY_TEST_NAME = "BlanketApprovePolicyTest";
    
    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }
        
	/**
     * Tests that an exception is thrown if you try to execute a "route" command on an already routed document.
     */
    @Test public void testRouteAlreadyRoutedDocument() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), DOCUMENT_TYPE_NAME);
    	document.route("");
    	
    	assertTrue("Document should be ENROUTE.", document.isEnroute());
    	assertFalse("There should not be a request to ewestfal.", document.isApprovalRequested());
    	
    	// verify that only 1 action taken has been performed
    	Collection<ActionTakenValue> actionTakens = KEWServiceLocator.getActionTakenService().findByDocumentId(document.getDocumentId());
    	assertEquals("There should be only 1 action taken.", 1, actionTakens.size());
    	
    	// now try and route the document again, an exception should be thrown
    	try {
    		document.route("");
    		fail("A WorkflowException should have been thrown.");
    	} catch (InvalidActionTakenException e) {
    		e.printStackTrace();
    	}
    	
    	// verify that there is still only 1 action taken (the transaction above should have rolled back)
    	actionTakens = KEWServiceLocator.getActionTakenService().findByDocumentId(document.getDocumentId());
    	assertEquals("There should still be only 1 action taken.", 1, actionTakens.size());
    }
	
    /**
     * Tests that an exception is not thrown if you try to execute a "route" command on a document you did not initiate.
     */
    @Test public void testRouteDocumentAsNonInitiatorUser() throws Exception {
        WorkflowDocument firstDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), DOCUMENT_TYPE_POLICY_TEST_NAME);
        WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), firstDocument.getDocumentId());
        try {
            document.route("");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown but should not have have been... Exception was of type " + e.getClass().getName() + " and message was " + e.getMessage());
        }
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), firstDocument.getDocumentId());
        assertEquals("Document should be in Enroute status.", DocumentStatus.ENROUTE, document.getStatus());

        // verify that there is 1 action taken
        Collection<ActionTakenValue> actionTakens = KEWServiceLocator.getActionTakenService().findByDocumentId(document.getDocumentId());
        assertEquals("There should be 1 action taken.", 1, actionTakens.size());
    }
    
    /**
     * Tests that an exception is not thrown if you try to execute a "route" command on a document you did not initiate.
     */
    @Test public void testRouteDefaultDocumentAsNonInitiatorUser() throws Exception {
        WorkflowDocument firstDocument = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), DOCUMENT_TYPE_NAME);
        WorkflowDocument document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), firstDocument.getDocumentId());
        try {
            document.route("");
            fail("Exception should have been thrown.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse("Document should not be ENROUTE.", document.isEnroute());
//        assertFalse("There should not be a request to user2.", document.isApprovalRequested());
//        
//        // verify that there are no actions taken
//        Collection actionTakens = KEWServiceLocator.getActionTakenService().findByDocumentId(document.getDocumentId());
//        assertEquals("There should be 0 actions taken.", 0, actionTakens.size());
    }
    
}
