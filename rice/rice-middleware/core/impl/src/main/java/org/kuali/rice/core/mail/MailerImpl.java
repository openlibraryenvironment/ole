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
package org.kuali.rice.core.mail;

import org.kuali.rice.core.api.mail.EmailBcList;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailCcList;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.mail.EmailToList;
import org.kuali.rice.core.api.mail.MailMessage;
import org.kuali.rice.core.api.mail.Mailer;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;


/**
 * Maintains a Java Mail session and is used for sending e-mails.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MailerImpl implements Mailer {

	    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MailerImpl.class);

	    private JavaMailSenderImpl mailSender;	  
	    
		/**
		 * @param mailSender The injected Mail Sender.
		 */
		public void setMailSender(JavaMailSenderImpl mailSender) {
			this.mailSender = mailSender;
		}
	    
		/**
	     * Construct and a send simple email message from a Mail Message.
	     * 
	     * @param message
	     *            the Mail Message
		 * @throws MessagingException 
	     */
		@Override
        @SuppressWarnings("unchecked")
		public void sendEmail(MailMessage message) throws MessagingException {
	        
	        // Construct a simple mail message from the Mail Message
	        SimpleMailMessage smm = new SimpleMailMessage();
	        smm.setTo( (String[])message.getToAddresses().toArray(new String[message.getToAddresses().size()]) );
	        smm.setBcc( (String[])message.getBccAddresses().toArray(new String[message.getBccAddresses().size()]) );
	        smm.setCc( (String[])message.getCcAddresses().toArray(new String[message.getCcAddresses().size()]) );
	        smm.setSubject(message.getSubject());
	        smm.setText(message.getMessage());
	        smm.setFrom(message.getFromAddress());

	        try {
	        	if ( LOG.isDebugEnabled() ) {
	        		LOG.debug( "sendEmail() - Sending message: " + smm.toString() );
	        	}
	            mailSender.send(smm);
	        }
	        catch (Exception e) {
	        	LOG.error("sendEmail() - Error sending email.", e);
				throw new RuntimeException(e);
	        }
	    }
		
		/**
	     * Send an email to a single recipient with the specified subject and message. This is a convenience 
	     * method for simple message addressing.
	     * 
	     * @param from
	     *            sender of the message            
	     * @param to
	     *            list of addresses to which the message is sent
	     * @param subject
	     *            subject of the message
	     * @param body
	     *            body of the message
	     */
		@Override
        public void sendEmail(EmailFrom from, EmailTo to, EmailSubject subject, EmailBody body, boolean htmlMessage) {
	        if (to.getToAddress() == null) {
	            LOG.warn("No To address specified. Refraining from sending mail.");
	            return;
	        }
			try {
		        Address[] singleRecipient = {new InternetAddress(to.getToAddress())};
				sendMessage(from.getFromAddress(),
						    singleRecipient,
						    subject.getSubject(),
						    body.getBody(), 
						    null,
						    null,
						    htmlMessage);
			} catch (Exception e) {
				LOG.error("sendEmail(): ", e);
				throw new RuntimeException(e);
			}
		}

		/**
	     * Send an email to the given "to", "cc", and "bcc" recipients with the specified subject and message.
	     * 
	     * @param from
	     *            sender of the message            
	     * @param to
	     *            list of addresses to which the message is sent
	     * @param subject
	     *            subject of the message
	     * @param body
	     *            body of the message
	     * @param cc
	     *            list of addresses which are to be cc'd on the message
	     * @param bc
	     *            list of addresses which are to be bcc'd on the message
	     */
		@Override
        public void sendEmail(EmailFrom from, EmailToList to, EmailSubject subject, EmailBody body, EmailCcList cc,
                EmailBcList bc, boolean htmlMessage) {
		    if (to.getToAddresses().isEmpty()) {
				LOG.error("List of To addresses must contain at least one entry. Refraining from sending mail.");
				return;
		    }
			try {
			    sendMessage(from.getFromAddress(), 
				            to.getToAddressesAsAddressArray(), 
				            subject.getSubject(), 
				            body.getBody(), 
							(cc == null ? null : cc.getToAddressesAsAddressArray()), 
							(bc == null ? null : bc.getToAddressesAsAddressArray()), 
							htmlMessage);
			} catch (Exception e) {
				LOG.error("sendEmail(): ", e);
				throw new RuntimeException(e);
            }
		}
		
		/**
	     * Send an email to the given recipients with the specified subject and message.
	     * 
	     * @param from
	     *            sender of the message            
	     * @param to
	     *            list of addresses to which the message is sent
	     * @param subject
	     *            subject of the message
	     * @param messageBody
	     *            body of the message
	     * @param cc
	     *            list of addresses which are to be cc'd on the message
	     * @param bcc
	     *            list of addresses which are to be bcc'd on the message
	     */
	    protected void sendMessage(String from, Address[] to, String subject, String messageBody, Address[] cc, Address[] bcc, boolean htmlMessage) throws AddressException, MessagingException, MailException {
		    MimeMessage message = mailSender.createMimeMessage();

	        // From Address
	        message.setFrom(new InternetAddress(from));

	        // To Address(es)
	        if (to != null && to.length > 0) {
	            message.addRecipients(Message.RecipientType.TO, to);
	        } else {
	            LOG.error("No recipients indicated.");
	        }

	        // CC Address(es)
	        if (cc != null && cc.length > 0) {
	            message.addRecipients(Message.RecipientType.CC, cc);
	        }

	        // BCC Address(es)
	        if (bcc != null && bcc.length > 0) {
	            message.addRecipients(Message.RecipientType.BCC, bcc);
	        }

	        // Subject
	        message.setSubject(subject);
	        if (subject == null || "".equals(subject)) {
	            LOG.warn("Empty subject being sent.");
	        }

	        // Message body.
	        if (htmlMessage) {
	            prepareHtmlMessage(messageBody, message);
	        } else {
	            message.setText(messageBody);
	            if (messageBody == null || "".equals(messageBody)) {
	                LOG.warn("Empty message body being sent.");
	            }
	        }

	        // Send the message
	        try {
	        	mailSender.send(message);
	        }
	        catch (Exception e) {
	        	LOG.error("sendMessage(): ", e);
	        	throw new RuntimeException(e);
	        }
	    }

	    protected void prepareHtmlMessage(String messageText, Message message) throws MessagingException {
	        try {
				message.setDataHandler(new DataHandler(new ByteArrayDataSource(messageText, "text/html")));
			} catch (IOException e) {
				LOG.warn(e.getMessage());
				throw new RuntimeException(e);
			}
	    }
}
