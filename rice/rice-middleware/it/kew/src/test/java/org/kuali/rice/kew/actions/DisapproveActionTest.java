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
import mocks.MockEmailNotificationService;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.api.KewApiConstants;

public class DisapproveActionTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testDisapprove() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");//ewestfal had force action rule

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");

        //this be the role delegate of jitrue
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("natjohns"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("This user should have an approve request", document.isApprovalRequested());
        // assert that the document is at the same node before and after disapprove
        TestUtilities.assertAtNode(document, NotifySetup.NOTIFY_FINAL_NODE);
        document.disapprove("");
        TestUtilities.assertAtNode(document, NotifySetup.NOTIFY_FINAL_NODE);
        // reload just to double check
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        TestUtilities.assertAtNode(document, NotifySetup.NOTIFY_FINAL_NODE);

        assertTrue("Document should be disapproved", document.isDisapproved());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        //jitrue while part of original approval chain did not take approve action and therefore should
        //not get action
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jitrue"), document.getDocumentId());
        assertFalse("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("natjohns"), document.getDocumentId());
        assertTrue("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        //shenl part of approval chain but didn't take action
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("shenl"), document.getDocumentId());
        assertFalse("ack should be requested as part of disapprove notification", document.isAcknowledgeRequested());

        //check that all the emailing went right.
        assertEquals("jhopf should have been sent an approve email", 1, getMockEmailService().immediateReminderEmailsSent("jhopf", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("jhopf should have been sent an ack email", 1, getMockEmailService().immediateReminderEmailsSent("jhopf", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        assertEquals("ewestfal should have been sent an approve email", 1, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("ewestfal should have been sent an ack email", 1, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        //rkirkend is a primary delegate and therefore should not receive email notification
        assertEquals("rkirkend should not have been sent an approve email", 0, getMockEmailService().immediateReminderEmailsSent("rkirkend", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("rkirkend should have been sent an ack email", 1, getMockEmailService().immediateReminderEmailsSent("rkirkend", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        //temay is rkirkend primary delegate she should have received notification
        assertEquals("temay should have been sent an approve email", 1, getMockEmailService().immediateReminderEmailsSent("temay", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

        //there should be no ack emails for temay
        assertEquals("temay should have been sent an ack email", 0, getMockEmailService().immediateReminderEmailsSent("temay", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        // pmckown is a secondary delegate here so he should NOT have received a notification
        assertEquals("pmckown should not have been sent an approve email", 0, getMockEmailService().immediateReminderEmailsSent("pmckown", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("pmckown should not have been sent an ack email", 0, getMockEmailService().immediateReminderEmailsSent("pmckown", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        //this is a secondary delegator and should receive notifications
        assertEquals("jitrue should have been sent an approve email", 1, getMockEmailService().immediateReminderEmailsSent("jitrue", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        //no ack emails to jitrue
        assertEquals("jitrue should have been sent an ack email", 0, getMockEmailService().immediateReminderEmailsSent("jitrue", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        //the 2nd delegates should NOT receive notifications by default
        assertEquals("natjohns should not have been sent an approve email", 0, getMockEmailService().immediateReminderEmailsSent("natjohns", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        //2ndary delegate
        assertEquals("natjohns should not have been sent an ack email", 1, getMockEmailService().immediateReminderEmailsSent("natjohns", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));
        assertEquals("shenl should not have been sent an approve email", 0, getMockEmailService().immediateReminderEmailsSent("shenl", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("shenl should not have been sent an ack email", 0, getMockEmailService().immediateReminderEmailsSent("shenl", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));

        assertEquals("bmcgough should have been sent an approve email", 1, getMockEmailService().immediateReminderEmailsSent("bmcgough", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("bmcgough should not have been sent an ack email", 0, getMockEmailService().immediateReminderEmailsSent("bmcgough", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ));
    }

    /**
     * Tests whether the initator who disapproved a doc gets an acknowledgement
     *
     */
    @Ignore("This test will fail until KULRICE-752 is resolved")
    @Test public void testInitiatorRoleDisapprove() throws WorkflowException {
        // test initiator disapproval of their own doc via InitiatorRoleAttribute
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "InitiatorRoleApprovalTest");
        document.route("routing document");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("arh14"), document.getDocumentId());
        document.disapprove("disapproving the document");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("arh14"), document.getDocumentId());
        assertFalse("Initiator should not have an Ack request from disapproval because they were the disapprover user", document.isAcknowledgeRequested());
    }

    /**
     * Tests whether the initator who disapproved a doc gets an acknowledgement
     * 
     */
    @Ignore("This test will fail until KULRICE-752 is resolved")
    @Test public void testInitiatorDisapprove() throws WorkflowException {
        // test initiator disapproval, via normal request with forceAction=true
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.disapprove("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertFalse("Initiator should not have an Ack request from disapproval because they were the disapprover user", document.isAcknowledgeRequested());
    }

    @Test public void testDisapproveByArbitraryRecipient() throws WorkflowException {
        // test approval by some other person
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "BlanketApproveSequentialTest");
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        document.disapprove("disapproving as bmcgough");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertFalse("Acknowledge was incorrectly sent to non-initiator disapprover", document.isAcknowledgeRequested());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("Acknowledge was not sent to initiator", document.isAcknowledgeRequested());
    }

    private MockEmailNotificationService getMockEmailService() {
        return (MockEmailNotificationService)KEWServiceLocator.getActionListEmailService();
    }
}
