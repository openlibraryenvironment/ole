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
package org.kuali.rice.kew.notification;

import mocks.MockEmailNotificationService;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class NotificationServiceTest extends KEWTestCase {

	protected void loadTestData() throws Exception {
        loadXmlFile("NotificationConfig.xml");
    }

	/**
	 * Tests that when a user is routed to twice at the same time that only email is sent to them.
	 */
	@Test public void testNoDuplicateEmails() throws Exception {
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "NotificationTest");
		document.route("");

		assertEquals("rkirkend should only have one email.", 1, getMockEmailService().immediateReminderEmailsSent("rkirkend", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		assertEquals("ewestfal should only have one email.", 1, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		assertEquals("jhopf should only have one email.", 1, getMockEmailService().immediateReminderEmailsSent("jhopf", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		// bmcgough is doing primary delegation so he should not recieve an email notification
		assertEquals("bmcgough should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("bmcgough", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		// jitrue should have no email because he is a secondary delegate and his default preferences should be set up to not send an email
		assertEquals("jitrue should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("jitrue", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		// user1 took action so they should _not_ be sent any emails
		assertEquals("user1 should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("user1", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

	}

	/**
	 * Tests that the notification preferences for emails work properly.  There are four different preferences:
	 *
	 * 1) Email notification type (none, immediate, daily, weekly) - defaults to immediate
	 * 2) Send primary delegation notifications - defaults to true
	 * 3) Send secondary delegation notifications - defaults to false
	 */
	@Test public void testEmailPreferences() throws Exception {
		String ewestfalPrincipalId = getPrincipalIdForName("ewestfal");
		String jitruePrincipalId = getPrincipalIdForName("jitrue");
		String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
		String jhopfPrincipalId = getPrincipalIdForName("jhopf");
		String bmcgoughPrincipalId = getPrincipalIdForName("bmcgough");
		String user1PrincipalId = getPrincipalIdForName("user1");

		// test that the users with secondary delegations have default preferences
		assertDefaultNotificationPreferences(ewestfalPrincipalId);
		assertDefaultNotificationPreferences(jitruePrincipalId);
		assertDefaultNotificationPreferences(rkirkendPrincipalId);
		assertDefaultNotificationPreferences(jhopfPrincipalId);
		assertDefaultNotificationPreferences(bmcgoughPrincipalId);
		// the rest of the default setup is actually tested by testNoDuplicateEmails

		// now turn on secondary notification for ewestfal and jitrue, turn off email notification for ewestfal
		Preferences prefs = getPreferencesService().getPreferences(ewestfalPrincipalId);
        Preferences.Builder preferencesBuilder = Preferences.Builder.create(prefs);
		preferencesBuilder.setNotifySecondaryDelegation(KewApiConstants.PREFERENCES_YES_VAL);
		preferencesBuilder.setEmailNotification(KewApiConstants.EMAIL_RMNDR_NO_VAL);
		getPreferencesService().savePreferences(ewestfalPrincipalId, preferencesBuilder.build());
		prefs = getPreferencesService().getPreferences(jitruePrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
		preferencesBuilder.setNotifySecondaryDelegation(KewApiConstants.PREFERENCES_YES_VAL);
		getPreferencesService().savePreferences(jitruePrincipalId, preferencesBuilder.build());

		// also turn off primary delegation notification for rkirkend
		prefs = getPreferencesService().getPreferences(rkirkendPrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
		preferencesBuilder.setNotifyPrimaryDelegation(KewApiConstants.PREFERENCES_NO_VAL);
		getPreferencesService().savePreferences(rkirkendPrincipalId, preferencesBuilder.build());

		// also turn notification to daily for bmcgough
		prefs = getPreferencesService().getPreferences(bmcgoughPrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
		preferencesBuilder.setEmailNotification(KewApiConstants.EMAIL_RMNDR_DAY_VAL);
		getPreferencesService().savePreferences(bmcgoughPrincipalId, preferencesBuilder.build());

		// also turn off notification for jhopf
		prefs = getPreferencesService().getPreferences(jhopfPrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
		preferencesBuilder.setEmailNotification(KewApiConstants.EMAIL_RMNDR_NO_VAL);
		getPreferencesService().savePreferences(jhopfPrincipalId, preferencesBuilder.build());

		// route the document
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "NotificationTest");
		document.route("");

		// both ewestfal and jitrue should have one email
		assertEquals("ewestfal should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
		assertEquals("jitrue should have one email.", 1, getMockEmailService().immediateReminderEmailsSent("jitrue", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

		// rkirkend (the primary delegate) should now have no emails
		assertEquals("rkirkend should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("rkirkend", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

		// jhopf should now have no emails since his top-level requests are no longer notified
		assertEquals("jhopf should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("jhopf", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

		// bmcgough should now have no emails since his notification preferences is DAILY
		assertEquals("bmcgough should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("bmcgough", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
	}


    /**
     * Tests email for individual document type
     */
    @Test public void testIndDocTypeEmailPreferences() throws Exception {
        String ewestfalPrincipalId = getPrincipalIdForName("ewestfal");
        String jitruePrincipalId = getPrincipalIdForName("jitrue");
        String rkirkendPrincipalId = getPrincipalIdForName("rkirkend");
        String jhopfPrincipalId = getPrincipalIdForName("jhopf");
        String bmcgoughPrincipalId = getPrincipalIdForName("bmcgough");
        String user1PrincipalId = getPrincipalIdForName("user1");

        Preferences prefs = getPreferencesService().getPreferences(ewestfalPrincipalId);
        Preferences.Builder preferencesBuilder = Preferences.Builder.create(prefs);

        prefs = getPreferencesService().getPreferences(ewestfalPrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
        preferencesBuilder.setEmailNotification(KewApiConstants.EMAIL_RMNDR_WEEK_VAL);
        preferencesBuilder.setDocumentTypeNotificationPreference("NotificationTest", "immediate");
        getPreferencesService().savePreferences(ewestfalPrincipalId, preferencesBuilder.build());

        // also turn off notification for jhopf
        prefs = getPreferencesService().getPreferences(jhopfPrincipalId);
        preferencesBuilder = Preferences.Builder.create(prefs);
        preferencesBuilder.setEmailNotification(KewApiConstants.EMAIL_RMNDR_NO_VAL);
        preferencesBuilder.setDocumentTypeNotificationPreference("NotificationTest", "daily");
        getPreferencesService().savePreferences(jhopfPrincipalId, preferencesBuilder.build());

        // route the document
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "NotificationTest");
        document.route("");

        assertEquals("ewestfal should have one emails.", 1, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("rkirkend should only have one email.", 1, getMockEmailService().immediateReminderEmailsSent("rkirkend", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("jhopf should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("jhopf", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("jitrue should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("jitrue", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
        assertEquals("bmcgough should have no emails.", 0, getMockEmailService().immediateReminderEmailsSent("bmcgough", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));
    }

	/**
	 * Tests that the fromNotificationAddress on the document type works properly.  Used to test implementation of KULWF-628.
	 */
	@Test public void testDocumentTypeNotificationFromAddress() throws Exception {
		String user1PrincipalId = getPrincipalIdForName("user1");

		// first test that the notification from addresses are configured correctly
		DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("NotificationTest");
		assertNull("Wrong notification from address, should be null.", documentType.getNotificationFromAddress());
        assertNull("Wrong actual notification from address, should be null.", documentType.getActualNotificationFromAddress());

		// test the parent document type
		documentType = KEWServiceLocator.getDocumentTypeService().findByName("NotificationFromAddressParent");
		assertEquals("Wrong notification from address.", "fakey@mcfakey.com", documentType.getNotificationFromAddress());

		// test a child document type which overrides the parent's address
		documentType = KEWServiceLocator.getDocumentTypeService().findByName("NotificationFromAddressChild");
		assertEquals("Wrong notification from address.", "fakey@mcchild.com", documentType.getNotificationFromAddress());

		// test a child document type which doesn't override the parent's address
		documentType = KEWServiceLocator.getDocumentTypeService().findByName("NotificationFromAddressChildInherited");
		assertEquals("Wrong notification from address.", "fakey@mcfakey.com", documentType.getNotificationFromAddress());

		// Do an app specific route to a document which should send an email to fakey@mcchild.com
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, "NotificationFromAddressChild");
		document.adHocToPrincipal(ActionRequestType.APPROVE, "Initial", "", getPrincipalIdForName("ewestfal"), "", true);
		document.route("");

		// verify that ewestfal was sent an email
		assertEquals("ewestfal should have an email.", 1, getMockEmailService().immediateReminderEmailsSent("ewestfal", document.getDocumentId(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ));

		// we currently have no way from this test to determine the email address used for notification
	}

	private void assertDefaultNotificationPreferences(String principalId) throws Exception {
		Preferences prefs = getPreferencesService().getPreferences(principalId);
		assertEquals(KewApiConstants.EMAIL_RMNDR_IMMEDIATE, prefs.getEmailNotification());
		assertEquals(KewApiConstants.PREFERENCES_YES_VAL, prefs.getNotifyPrimaryDelegation());
		assertEquals(KewApiConstants.PREFERENCES_NO_VAL, prefs.getNotifySecondaryDelegation());
	}

	private PreferencesService getPreferencesService() {
		return KewApiServiceLocator.getPreferencesService();
	}

	private MockEmailNotificationService getMockEmailService() {
		return (MockEmailNotificationService)KEWServiceLocator.getActionListEmailService();
	}

}
