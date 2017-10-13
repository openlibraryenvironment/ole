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
package org.kuali.rice.krad.service.impl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.mail.MailMessage;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KualiFeedbackService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This class implements the KualiFeedbackService and contains logic
 * to send a feedback email to the feedback email list with information from
 * users.
 */
public class KualiFeedbackServiceImpl implements KualiFeedbackService {
	
	private static final Logger LOG = Logger.getLogger(KualiFeedbackServiceImpl.class);
	
	private static final String FEEDBACK_EMAIL_SUBJECT_PARAM = "feedback.email.subject";
	private static final String FEEDBACK_EMAIL_BODY_PARAM = "feedback.email.body";

	/**
	 * A Mailer for sending report.
	 */
	private Mailer mailer;
	/**
	 * An email template is used to construct an email to be sent by the mail service.
	 */
	private MailMessage messageTemplate;

	/**
	 * This mails the report using the mail service from the mail template.
	 * 
	 * @see org.kuali.rice.krad.service.KualiExceptionIncidentService#emailReport(String, String)
	 */
	@Override
	public void emailReport(String subject, String message) throws Exception {
		if (LOG.isTraceEnabled()) {
			String lm=String.format("ENTRY %s;%s",
					(subject==null)?"null":subject.toString(),
							(message==null)?"null":message.toString());
			LOG.trace(lm);
		}

		if (mailer == null) {
			String errorMessage = "mailer property of KualiExceptionIncidentServiceImpl is null";
			LOG.fatal(errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		// Send mail
		MailMessage msg = createMailMessage(subject, message);
		mailer.sendEmail(msg);

		if (LOG.isTraceEnabled()) {
			LOG.trace("EXIT");
		}
	}

	@Override
	public void sendFeedback(String documentId, String componentName, String description) throws Exception {
		this.emailReport(this.createFeedbackReportSubject(), this.createFeedbackReportMessage(documentId, componentName, description));
	}
	
	private String createFeedbackReportSubject() {
		String env = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.ENVIRONMENT_KEY);
		String formatString = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(FEEDBACK_EMAIL_SUBJECT_PARAM);
		String subject = MessageFormat.format(formatString, env);
		return subject;
	}

	private String createFeedbackReportMessage(String documentId, String componentName, String description) {
		documentId = (documentId == null) ? "" : documentId;
		componentName = (componentName == null) ? "" : componentName;
		description = (description == null) ? "" : description;
		
		String principalName = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
		principalName = (principalName == null) ? "" : principalName;
		
		String formatString = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(FEEDBACK_EMAIL_BODY_PARAM);
		String message = MessageFormat.format(formatString, documentId, principalName, componentName, description);
		return message;
	}

	/**
     * Creates an instance of MailMessage from the inputs using the given
     * template.
	 *
     * @param subject the subject line text
     * @param message the body of the email message
     * @return MailMessage
     * @throws IllegalStateException if the <codeREPORT_MAIL_LIST</code> is not set
     * or messageTemplate does not have ToAddresses already set.
     */
    @SuppressWarnings("unchecked")
    protected MailMessage createMailMessage(String subject, String message)
            throws Exception {
		if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s%n%s",
                    (subject==null) ? "null" : subject.toString(),
                    (message==null) ? "null" : message.toString());
            LOG.trace(lm);
        }
        
        MailMessage messageTemplate = this.getMessageTemplate();
        if (messageTemplate == null) {
            throw new IllegalStateException(String.format(
                    "%s.templateMessage is null or not set",
                    this.getClass().getName()));
        }
        
        // Copy input message reference for creating an instance of mail message
        MailMessage msg=new MailMessage();
        
        msg.setFromAddress(this.getFromAddress());
    	msg.setToAddresses(this.getToAddresses());
        msg.setBccAddresses(this.getBccAddresses());
        msg.setCcAddresses(this.getCcAddresses());

        // Set mail message subject
        msg.setSubject((subject==null) ? "" : subject);

        // Set mail message body
        msg.setMessage((message==null) ? "" : message);
        
        if (LOG.isTraceEnabled()) {
            String lm = String.format("EXIT %s", (msg==null) ? "null" : msg.toString());
            LOG.trace(lm);
        }

        return msg;
	}

	protected String getFromAddress() {    
		Person actualUser = GlobalVariables.getUserSession().getActualPerson();
        
        String fromEmail = actualUser.getEmailAddress();
        if (StringUtils.isNotBlank(fromEmail)) {
        	return fromEmail;
    	} else {
        	return this.getMessageTemplate().getFromAddress();
    	}
	}
	
	protected Set<String> getToAddresses() {
		// First check if message template already define mailing list
        Set<String> emails = this.getMessageTemplate().getToAddresses();
        if (emails == null || emails.isEmpty()) {
    		String mailingList = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(this.getToAddressesPropertyName());
            if (StringUtils.isBlank(mailingList)) {
                String em = REPORT_MAIL_LIST + " is not set or messageTemplate does not have ToAddresses already set.";
                LOG.error(em);
                throw new IllegalStateException(em);
            } else {
                return new HashSet<String>(Arrays.asList(StringUtils.split(mailingList,
                                KRADConstants.FIELD_CONVERSIONS_SEPARATOR)));
            }
        } else {
            return emails;
        }
	}
	
	protected String getToAddressesPropertyName() {
		return REPORT_MAIL_LIST;
	}
	
	protected Set<String> getCcAddresses() {
		return this.getMessageTemplate().getCcAddresses();
	}
	
	protected Set<String> getBccAddresses() {
		return this.getMessageTemplate().getBccAddresses();
	}

	public Mailer getMailer() {
		return mailer;
	}

	public final void setMailer(Mailer mailer) {
		this.mailer = mailer;
	}

	public MailMessage getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(MailMessage messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
}
