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

import javax.mail.MessagingException;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mail.MailMessage;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.krad.exception.InvalidAddressException;
import org.kuali.rice.krad.service.MailService;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * Default implementation of mail service
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MailServiceImpl implements MailService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MailServiceImpl.class);

    private String batchMailingList;
    private Mailer mailer;

    private String nonProductionNotificationMailingList;
    private boolean realNotificationsEnabled = true;

    /**
     * The injected Mailer.
     */
    public void setMailer(Mailer mailer) {
    	this.mailer = mailer;
    }
    
    /**
     * 
     */
    public MailServiceImpl() {
        super();
    }

    /**
     * Sets the batchMailingList attribute value.
     * @param batchMailingList The batchMailingList to set.
     */
    public void setBatchMailingList(String batchMailingList) {
        this.batchMailingList = batchMailingList;
    }

    /**
     * @see org.kuali.rice.krad.service.MailService#getBatchMailingList()
     */
    public String getBatchMailingList() {
        return batchMailingList;
    }

	/**
	 * This overridden method ...
	 * @throws MessagingException 
	 * 
	 * @see org.kuali.rice.krad.service.MailService#sendMessage(org.kuali.rice.core.api.mail.MailMessage)
	 */
	@Override
	public void sendMessage(MailMessage message) throws InvalidAddressException, MessagingException {
		mailer.sendEmail(composeMessage(message));		
	}
	
    protected MailMessage composeMessage(MailMessage message){

        MailMessage mm = new MailMessage();

        // If realNotificationsEnabled is false, mails will be sent to nonProductionNotificationMailingList
        if(!isRealNotificationsEnabled()){
            getNonProductionMessage(message);
        }

        String app = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(CoreConstants.Config.APPLICATION_ID);
        String env = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.ENVIRONMENT_KEY);
        
        mm.setToAddresses(message.getToAddresses());
        mm.setBccAddresses(message.getBccAddresses());
        mm.setCcAddresses(message.getCcAddresses());
        mm.setSubject(app + " " + env + ": " + message.getSubject());
        mm.setMessage(message.getMessage());
        mm.setFromAddress(message.getFromAddress());
        return mm;
    }

    public String getNonProductionNotificationMailingList() {
        return this.nonProductionNotificationMailingList;
    }

    /**
     * @param nonProductionNotificationMailingList the nonProductionNotificationMailingList to set
     */
    public void setNonProductionNotificationMailingList(
            String nonProductionNotificationMailingList) {
        this.nonProductionNotificationMailingList = nonProductionNotificationMailingList;
    }

    /**
     * @return the realNotificationsEnabled
     */
    public boolean isRealNotificationsEnabled() {
        return this.realNotificationsEnabled;
    }

    /**
     * @param realNotificationsEnabled the realNotificationsEnabled to set
     */
    public void setRealNotificationsEnabled(boolean realNotificationsEnabled) {
        this.realNotificationsEnabled = realNotificationsEnabled;
    }

    protected MailMessage getNonProductionMessage(MailMessage message){
        StringBuilder buf = new StringBuilder();
        buf.append("Email To: ").append(message.getToAddresses()).append("\n");
        buf.append("Email CC: ").append(message.getCcAddresses()).append("\n");
        buf.append("Email BCC: ").append(message.getBccAddresses()).append("\n\n");
        buf.append(message.getMessage());

        message.getToAddresses().clear();
        //Note: If the non production notification mailing list is blank, sending this message will throw an exception
        message.addToAddress(getNonProductionNotificationMailingList());
        message.getBccAddresses().clear();
        message.getCcAddresses().clear();
        message.setMessage(buf.toString());

        return message;
    }
}
