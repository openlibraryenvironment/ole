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
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.test.KEWTestCase;

import static org.junit.Assert.*;

/**
 * 
 * Test SuperUserDissaprove actions from WorkflowDocument
 *
 */
public class SuperUserDisapproveTest extends KEWTestCase {
    private static final String DOC_TYPE = NotifySetup.DOCUMENT_TYPE_NAME;
    private static final String DOC_TYPE_WITH_NOTIFY = "SUDisapproveWithNotificationTest";

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }
	
    @Test public void testSuperUserDisapprove() throws Exception {
        superUserDisapprove(false);
    }

    @Test public void testSuperUserDisapproveWithNotification() throws Exception {
        superUserDisapprove(true);
    }

    protected void superUserDisapprove(boolean notify) throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), notify ? DOC_TYPE_WITH_NOTIFY: DOC_TYPE);
        document.route("");

        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId()).approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("WorkflowDocument should indicate jhopf as SuperUser", document.isValidAction(ActionType.SU_DISAPPROVE));
        document.superUserDisapprove("");
        assertTrue("Document should be final after Super User Disapprove", document.isDisapproved());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(document.getInitiatorPrincipalId(), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId()).isAcknowledgeRequested());
	}
	
    @Test public void testSuperUserInitiatorDisapprove() throws Exception {
        superUserInitiatorDisapprove(false);
    }

    @Test public void testSuperUserInitiatorDisapproveWithNotification() throws Exception {
        superUserInitiatorDisapprove(true);
    }
    
    protected void superUserInitiatorDisapprove(boolean notify) throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), notify ? DOC_TYPE_WITH_NOTIFY: DOC_TYPE);
        document.route("");
        
        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId()).approve("");
        WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId()).approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isValidAction(ActionType.SU_DISAPPROVE));
        document.superUserDisapprove("");
        assertTrue("Document should be final after Super User Disapprove", document.isDisapproved());
        // initiator doesn't get Ack for their own action
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(document.getInitiatorPrincipalId(), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId()).isAcknowledgeRequested());
        assertEquals(notify, WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId()).isAcknowledgeRequested());
	}

    @Test public void testSuperUserInitiatorImmediateDisapprove() throws Exception {
        superUserInitiatorImmediateDisapprove(false);
    }

    @Test public void testSuperUserInitiatorImmediateDisapproveWithNotification() throws Exception {
        superUserInitiatorImmediateDisapprove(true);
    }

    protected void superUserInitiatorImmediateDisapprove(boolean notify) throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), notify ? DOC_TYPE_WITH_NOTIFY: DOC_TYPE);
        assertTrue("WorkflowDocument should indicate ewestfal as SuperUser", document.isValidAction(ActionType.SU_DISAPPROVE));
        document.superUserDisapprove("");
        assertTrue("Document should be final after Super User Disapprove", document.isDisapproved());
        // initiator doesn't get Ack for their own action
        assertFalse(WorkflowDocumentFactory.loadDocument(document.getInitiatorPrincipalId(), document.getDocumentId()).isAcknowledgeRequested());
        assertFalse(WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId()).isAcknowledgeRequested());
    }
	
    @Test public void testSuperUserDisapproveInvalidUser() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");
        
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), document.getDocumentId());
        try {
        	assertFalse("WorkflowDocument should not indicate quickstart as SuperUser", document.isValidAction(ActionType.SU_DISAPPROVE));
        	document.superUserDisapprove("");
        	fail("invalid user attempted to SuperUserApprove");
        } catch (Exception e) {
        }
        
	}
	
}
