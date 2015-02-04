package org.kuali.ole.deliver.batch;

import org.junit.Test;
import org.kuali.ole.OleBaseTestCase;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.mail.MailerImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: peris-kole
 * Date: 5/5/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleMailer_UT {
    @Test
    public void testSendMail() {
        Object mailer = GlobalResourceLoader.getService("mailer");
        assertNotNull(mailer);
        try {
            ((MailerImpl) mailer).sendEmail(new EmailFrom("peris@kuali.org"), new EmailTo("peris@kuali.org"), new EmailSubject("test subject"), new EmailBody("test body"), false);
        } catch (Exception e) {
            fail();
        }
    }

        @Test
       public void testSendNotice(){
            Object mailer = GlobalResourceLoader.getService("oleMailer");
            assertNotNull(mailer);
            try {
                ((OleMailer) mailer).sendEmail(new EmailFrom("cheenu311@gmail.com"), new EmailTo("maheswaran.g@htcindia.com"), new EmailSubject("Test Notice"), new EmailBody("Test Notice body"), false);
            } catch (Exception e) {
                fail();
            }

    }
}
