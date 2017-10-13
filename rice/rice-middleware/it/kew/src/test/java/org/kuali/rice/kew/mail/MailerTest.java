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
package org.kuali.rice.kew.mail;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.subethamail.wiser.Wiser;

/**
 * Tests email content generation
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MailerTest extends KEWTestCase {
    
    private String sender = "testSender@test.kuali.org";
    private String recipient = "testRecipient@test.kuali.org";
    private String subject = "Test Subject";
    private String messageBody = "Test Message Body";
    
	/**
	 * Test that a Mailer can be retrieved via the KEWServiceLocator and can be used 
	 * to send an e-mail message to the SMTP server.
	 */
	@Test
	public void testSendMessage() {
		// Initialize SMTP server
		Wiser smtpServer = new Wiser();
		smtpServer.setPort(55000);
		smtpServer.start();
		
		// Test that a Mailer can be retrieved via the KEWServiceLocator
		Mailer mailer = null;
		mailer = CoreApiServiceLocator.getMailer();
		assertNotNull(mailer);
		
		// Test that an e-mail message gets sent to the SMTP server
		mailer.sendEmail(new EmailFrom(sender), new EmailTo(recipient), new EmailSubject(subject), new EmailBody(messageBody), false);
		Assert.assertEquals(1, smtpServer.getMessages().size());

        // Shutdown the SMTP server
        smtpServer.stop();        
	}    

}
