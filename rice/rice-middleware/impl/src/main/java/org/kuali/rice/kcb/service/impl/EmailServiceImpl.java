/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kcb.service.impl;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.service.EmailService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.springframework.beans.factory.annotation.Required;

/**
 * This class is responsible for implementing the service that sends emails to individuals.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @auther Aaron Godert (ag266 at cornell dot edu)
 */
public class EmailServiceImpl implements EmailService {

	private static Logger LOG = Logger.getLogger(EmailServiceImpl.class);

	private static final String FORMAT_TEXT_HTML = "text/html";
	private static final String FORMAT_TEXT_PLAIN = "text/plain";

    // values injected into these from Spring
    private String weburl;
    private String defaultSender = "kcb@localhost";

    private final String DETAILACTION = "DetailView.form";
    
    private Mailer mailer;
    
    public void setMailer(Mailer mailer){
    	this.mailer = mailer;
    }

    /**
     * Sets the weburl attribute value (injected from Spring).
     * @param weburl
     */
    @Required
    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    /**
     * Sets the default sender address to use if no valid producer email address
     * was specified in the message
     * @param defaultSender the default sender address to use if no valid producer email address was specified in the message
     */
    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }

    /**
     * First constructs the appropriately formatted mail message then sends it off.
     * @see org.kuali.rice.kcb.service.EmailService#sendNotificationEmail(org.kuali.rice.kcb.bo.MessageDelivery, java.lang.String, java.lang.String)
     */
    public Long sendEmail(MessageDelivery messageDelivery, String recipientEmailAddress, String emailFormat) throws Exception {
        // reconcile the need for custom rendering depending on message producer
        // or can we just have a single URL that redirects to original dochandler?

        Message message = messageDelivery.getMessage();
        String channelName = message.getChannel();

        String producer = message.getProducer();
        String sender = defaultSender;
        if (producer != null) {
            try {
                InternetAddress[] addresses = InternetAddress.parse(producer, false);
                if (addresses.length > 0) {
                    sender = addresses[0].toString();
                }
            } catch (AddressException ae) {
                // not a valid email address
            }
        }

        String title = messageDelivery.getMessage().getTitle();
        String subject = (channelName == null ? "" : channelName + " ") + (!StringUtils.isBlank(title) ? " - " + title : "");

        String format = FORMAT_TEXT_PLAIN;
        String linebreak = "\n\n";

        // NOTE: we don't set the docId parameter in the link
        // This forces the detail view to not render an acknowledge
        // button
        String link = weburl +"/"+ DETAILACTION +"?" 
        + NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.MSG_DELIVERY_ID +"="+ messageDelivery.getId();

        if (emailFormat == null || emailFormat.equals("text")) {
        	// defaults values are good for text
        } else {  // html format
            format = FORMAT_TEXT_HTML;
            link = "<a href='"+ link +"'>Notification Detail</a>";
            linebreak = "<br /><br />";
        }

        LOG.debug("link: "+link);

        // construct the message
        StringBuffer sb = new StringBuffer();
        sb.append("You have a new notification. Click the link below to review its details.");
        sb.append(linebreak);
        sb.append(linebreak);
        sb.append(link);
        String content = sb.toString();

        LOG.debug("subject: "+subject);
        LOG.debug("sender: "+sender);
        LOG.debug("recipient: "+recipientEmailAddress);
        LOG.debug("content: "+content);

        // actually do the send
        mailer.sendEmail(new EmailFrom(sender), new EmailTo(recipientEmailAddress), new EmailSubject(subject), new EmailBody(content), !FORMAT_TEXT_PLAIN.equals(format));

        return null;
    }
}
