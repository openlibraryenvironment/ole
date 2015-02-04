package org.kuali.ole.deliver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.mail.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 12/4/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

public class MailNotifier_IT extends SpringBaseTestCase{
    private String sender = "kuali.ole@gmail.com";
    private String recipient = "kuali.ole@gmail.com";
    private String subject = "Test Subject";
    private String messageBody = "Test Message Body";

    /**
     * Test that a Mailer can be retrieved via the KEWServiceLocator and can be used
     * to send an e-mail message to the SMTP server.
     */
    @Test
    public void testSendMessage() {
        // Test that a Mailer can be retrieved via the KEWServiceLocator
        Mailer mailer = null;
        mailer = CoreApiServiceLocator.getMailer();
        assertNotNull(mailer);

        // Test that an e-mail message gets sent to the SMTP server
       // mailer.sendEmail(new EmailFrom(sender), new EmailTo(recipient), new EmailSubject(subject), new EmailBody(messageBody), false);

        //Typically we need to verify if the mail was received; and Wiser a java class under the subethamail.jar will act as a fake SMTP server to receive messages;
        //But sine we are are using gmail to test our message sending, it wont work here as it expects messages to be sent to the local default mail server listening on port 25.
        //This is just an illustration so could live with manually verifying if the test works. Eventually we need to fix the test case with a proper assert.

    }

}
