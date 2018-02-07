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
package org.kuali.rice.kew.mail.service.impl;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionItemContract;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.mail.CustomEmailAttribute;
import org.kuali.rice.kew.mail.DailyEmailJob;
import org.kuali.rice.kew.mail.WeeklyEmailJob;
import org.kuali.rice.kew.mail.service.ActionListEmailService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.UserOptionsService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * ActionListeEmailService which generates messages whose body and subject can be customized via KEW
 * configuration parameters, 'immediate.reminder.email.message' and
 * 'immediate.reminder.email.subject'. The immediate reminder email message key should specify a
 * MessageFormat string. See code for the parameters to this MessageFormat.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListEmailServiceImpl implements ActionListEmailService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(ActionListEmailServiceImpl.class);

    private static final String DEFAULT_EMAIL_FROM_ADDRESS = "admin@localhost";

    private static final String ACTION_LIST_REMINDER = "Action List Reminder";

    private static final String IMMEDIATE_REMINDER_EMAIL_MESSAGE_KEY = "immediate.reminder.email.message";

    private static final String IMMEDIATE_REMINDER_EMAIL_SUBJECT_KEY = "immediate.reminder.email.subject";

    private static final String DAILY_TRIGGER_NAME = "Daily Email Trigger";
    private static final String DAILY_JOB_NAME = "Daily Email";
    private static final String WEEKLY_TRIGGER_NAME = "Weekly Email Trigger";
    private static final String WEEKLY_JOB_NAME = "Weekly Email";

    private String deploymentEnvironment;

    private Mailer mailer;

    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    public String getDocumentTypeEmailAddress(DocumentType documentType) {
        String fromAddress = (documentType == null ? null : documentType
                .getNotificationFromAddress());
        if (org.apache.commons.lang.StringUtils.isEmpty(fromAddress)) {
            fromAddress = getApplicationEmailAddress();
        }
        return fromAddress;
    }

    public String getApplicationEmailAddress() {
        // first check the configured value
        String fromAddress = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                KewApiConstants.KEW_NAMESPACE,
                KRADConstants.DetailTypes.MAILER_DETAIL_TYPE,
                KewApiConstants.EMAIL_REMINDER_FROM_ADDRESS);
        // if there's no value configured, use the default
        if (org.apache.commons.lang.StringUtils.isEmpty(fromAddress)) {
            fromAddress = DEFAULT_EMAIL_FROM_ADDRESS;
        }
        return fromAddress;
    }

    protected String getHelpLink() {
        return getHelpLink(null);
    }

    protected String getHelpLink(DocumentType documentType) {
        return "For additional help, email " + "<mailto:"
                + getDocumentTypeEmailAddress(documentType) + ">";
    }

    public EmailSubject getEmailSubject() {
        String subject = ConfigContext.getCurrentContextConfig().getProperty(IMMEDIATE_REMINDER_EMAIL_SUBJECT_KEY);
        if (subject == null) {
            subject = ACTION_LIST_REMINDER;
        }
        return new EmailSubject(subject);
    }

    public EmailSubject getEmailSubject(String customSubject) {
        String subject = ConfigContext.getCurrentContextConfig().getProperty(IMMEDIATE_REMINDER_EMAIL_SUBJECT_KEY);
        if (subject == null) {
            subject = ACTION_LIST_REMINDER;
        }
        return new EmailSubject(subject + " " + customSubject);
    }

    protected EmailFrom getEmailFrom(DocumentType documentType) {
        return new EmailFrom(getDocumentTypeEmailAddress(documentType));
    }

    protected EmailTo getEmailTo(Person user) {
        String address = user.getEmailAddressUnmasked();
        if (!isProduction()) {
            LOG.info("If this were production, email would be sent to "+ user.getEmailAddressUnmasked());
            address = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                KewApiConstants.KEW_NAMESPACE,
                KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE,
                KewApiConstants.ACTIONLIST_EMAIL_TEST_ADDRESS);
        }
        return new EmailTo(address);
    }

    protected void sendEmail(Person user, EmailSubject subject,
            EmailBody body) {
        sendEmail(user, subject, body, null);
    }

    protected void sendEmail(Person user, EmailSubject subject,
            EmailBody body, DocumentType documentType) {
        try {
            if (StringUtils.isNotBlank(user.getEmailAddressUnmasked())) {
                mailer.sendEmail(getEmailFrom(documentType),
                    getEmailTo(user),
                    subject,
                    body,
                    false);
            }
        } catch (Exception e) {
            LOG.error("Error sending Action List email to " + user.getEmailAddressUnmasked(), e);
        }
    }
    
    /**
     * 
     * This method takes in a type of email which is being sent, an action item
     * which is being checked and a user's preferences and it checks to see if
     * the action item should be included in the given kind of email based on
     * the user's preferences.
     * 
     * @param actionItem
     * @param preferences
     * @param emailSetting
     * @return
     */
    protected boolean checkEmailNotificationPreferences(ActionItemContract actionItem, Preferences preferences, String emailSetting) {
        boolean shouldSend = true;
        // Check the user's primary and secondary delegation email preferences
        if (actionItem.getDelegationType() != null) {
            if (KimConstants.KimUIConstants.DELEGATION_PRIMARY.equalsIgnoreCase(actionItem.getDelegationType().getCode())) {
                shouldSend = KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getNotifyPrimaryDelegation());
            } else if (KimConstants.KimUIConstants.DELEGATION_SECONDARY.equalsIgnoreCase(actionItem.getDelegationType().getCode())) {
                shouldSend = KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getNotifySecondaryDelegation());
            }
        }
        if(!shouldSend) {
            // If the action item has a delegation type and the user's
            // preference for the delegation type is false, the item should not
            // be included
            return false;
        }

        // Check the request code of the action item and the complete, approve,
        // acknowledge, or FYI notification preference accordingly
        boolean checkRequestCodePreferences = ((StringUtils.equals(actionItem.getActionRequestCd(), KewApiConstants.ACTION_REQUEST_COMPLETE_REQ) && 
                                                StringUtils.equals(preferences.getNotifyComplete(), KewApiConstants.PREFERENCES_YES_VAL)) ||
                                               (StringUtils.equals(actionItem.getActionRequestCd(), KewApiConstants.ACTION_REQUEST_APPROVE_REQ) && 
                                                StringUtils.equals(preferences.getNotifyApprove(), KewApiConstants.PREFERENCES_YES_VAL)) ||
                                               (StringUtils.equals(actionItem.getActionRequestCd(), KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ) && 
                                                StringUtils.equals(preferences.getNotifyAcknowledge(), KewApiConstants.PREFERENCES_YES_VAL)) ||
                                               (StringUtils.equals(actionItem.getActionRequestCd(), KewApiConstants.ACTION_REQUEST_FYI_REQ) && 
                                                StringUtils.equals(preferences.getNotifyFYI(), KewApiConstants.PREFERENCES_YES_VAL)));

        DocumentRouteHeaderValue document =  KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId());
        DocumentType documentType = null;
        Boolean suppressImmediateEmailsOnSuActionPolicy = false;
        if (document != null) {
            documentType = document.getDocumentType();
            suppressImmediateEmailsOnSuActionPolicy = documentType.getSuppressImmediateEmailsOnSuActionPolicy().getPolicyValue();
        }

        if (suppressImmediateEmailsOnSuActionPolicy) {
            String docId = actionItem.getDocumentId();
            LOG.warn("checkEmailNotificationPreferences processing document: " + docId + " of type: " + documentType.getName() + " and getSuppressImmediateEmailsOnSuActionPolicy set to true.");

            List<ActionTakenValue> actionsTaken = document.getActionsTaken();
            if (actionsTaken != null && actionsTaken.size() > 0) {
                Collections.sort(actionsTaken, new Comparator<ActionTakenValue>() {

                    @Override
                    // Sort by date in descending order
                    public int compare(ActionTakenValue o1, ActionTakenValue o2) {
                        if (o1 == null && o2 == null)
                            return 0;
                        if (o1 == null)
                            return -1;
                        if (o2 == null)
                            return 1;

                        if (o1.getActionDate() == null && o2.getActionDate() == null)
                            return 0;
                        if (o1.getActionDate() == null)
                            return -1;
                        if (o2.getActionDate() == null)
                            return 1;

                        return o2.getActionDate().compareTo(o1.getActionDate());
                    }
                });
            }

            ActionTakenValue mostRecentActionTaken = (ActionTakenValue) actionsTaken.get(0);
            if (mostRecentActionTaken !=null && mostRecentActionTaken.isSuperUserAction()) {
                return false;
            }
        }

        // If the user has document type notification preferences check them to
        // see if the action item should be included in the email.
        if(preferences.getDocumentTypeNotificationPreferences().size() > 0) {   
            while(ObjectUtils.isNotNull(documentType)) {
                // Check to see if there is a notification preference for the
                // given document type in the user's preferences
                String documentTypePreference = preferences.getDocumentTypeNotificationPreference(documentType.getName());
                if(StringUtils.isNotBlank(documentTypePreference)) {
                    // If a document type notification preference is found,
                    // check the type of email being sent
                    if(StringUtils.equals(emailSetting, KewApiConstants.EMAIL_RMNDR_DAY_VAL) || StringUtils.equals(emailSetting, KewApiConstants.EMAIL_RMNDR_WEEK_VAL)) {
                        // If a daily or weekly email is being sent, include
                        // the action item unless the notification preference
                        // is 'None'
                        return !StringUtils.equals(documentTypePreference, KewApiConstants.EMAIL_RMNDR_NO_VAL);
                    } else if(StringUtils.equals(emailSetting, KewApiConstants.EMAIL_RMNDR_IMMEDIATE)) {
                        // Otherwise if this is an immediate notification check
                        // the action item request code preferences
                        return StringUtils.equals(emailSetting, documentTypePreference) && checkRequestCodePreferences;
                    } else {
                        // Otherwise the emailSetting is "None" so no email
                        // should be sent
                        return false;
                    }
                }
                // If no matches were found, continue checking the document
                // type hierarchy
                documentType = documentType.getParentDocType();
            }
        }

        // If no document type notification preference is found, include the
        // item if the email setting matches the user's default email
        // notification preference
        if(StringUtils.equals(emailSetting, preferences.getEmailNotification())) {
            if(StringUtils.equals(emailSetting, KewApiConstants.EMAIL_RMNDR_IMMEDIATE)) {
                // If this is an immediate notification check
                // the request code of the action item with the user's preferences
                return checkRequestCodePreferences;
            } else {
                // Otherwise just return true if the email setting isn't "None"
                return !StringUtils.equals(emailSetting, KewApiConstants.EMAIL_RMNDR_NO_VAL);
            }
        }
        return false;
    }

    public void sendImmediateReminder(org.kuali.rice.kew.api.action.ActionItem actionItem, Boolean skipOnApprovals) {
        if (actionItem == null) {
            LOG.warn("Request to send immediate reminder to recipient of a null action item... aborting.");
            return;
        }
        
        if (actionItem.getPrincipalId() == null) {
            LOG.warn("Request to send immediate reminder to null recipient of an action item... aborting.");
            return;
        }

        if (skipOnApprovals != null && skipOnApprovals.booleanValue()
                && actionItem.getActionRequestCd().equals(KewApiConstants.ACTION_REQUEST_APPROVE_REQ)) {
            LOG.debug("As requested, skipping immediate reminder notification on action item approval for " + actionItem.getPrincipalId());
            return;
        }
        
        Preferences preferences = KewApiServiceLocator.getPreferencesService().getPreferences(actionItem.getPrincipalId());
        if(!checkEmailNotificationPreferences(actionItem, preferences, KewApiConstants.EMAIL_RMNDR_IMMEDIATE)) {
            LOG.debug("Email suppressed due to the user's preferences");
            return;
        }

        boolean shouldSendActionListEmailNotification = sendActionListEmailNotification();
        if (shouldSendActionListEmailNotification) {
            LOG.debug("sending immediate reminder");

            Person person = KimApiServiceLocator.getPersonService().getPerson(actionItem.getPrincipalId());
            
            DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(
                    actionItem.getDocumentId());
            StringBuffer emailBody = new StringBuffer(buildImmediateReminderBody(person, actionItem,
                    document.getDocumentType()));
            StringBuffer emailSubject = new StringBuffer();
            try {
                CustomEmailAttribute customEmailAttribute = document.getCustomEmailAttribute();
                if (customEmailAttribute != null) {
                    Document routeHeaderVO = DocumentRouteHeaderValue.to(document);
                    ActionRequestValue actionRequest = KEWServiceLocator
                            .getActionRequestService().findByActionRequestId(actionItem.getActionRequestId());
                    ActionRequest actionRequestVO = ActionRequestValue.to(actionRequest);
                    customEmailAttribute.setRouteHeaderVO(routeHeaderVO);
                    customEmailAttribute.setActionRequestVO(actionRequestVO);
                    String customBody = customEmailAttribute
                            .getCustomEmailBody();
                    if (!org.apache.commons.lang.StringUtils.isEmpty(customBody)) {
                        emailBody.append(customBody);
                    }
                    String customEmailSubject = customEmailAttribute
                            .getCustomEmailSubject();
                    if (!org.apache.commons.lang.StringUtils.isEmpty(customEmailSubject)) {
                        emailSubject.append(customEmailSubject);
                    }
                }
            } catch (Exception e) {
                LOG
                        .error(
                                "Error when checking for custom email body and subject.",
                                e);
            }
            LOG.debug("Sending email to " + person);
            sendEmail(person, getEmailSubject(emailSubject.toString()),
                    new EmailBody(emailBody.toString()), document
                            .getDocumentType());
        }

    }

    protected boolean isProduction() {
        return ConfigContext.getCurrentContextConfig().isProductionEnvironment();
    }

    public void sendDailyReminder() {
        LOG.info("Starting SendDailyReminder");
        if (sendActionListEmailNotification()) {
            Collection<String> users = getUsersWithEmailSetting(KewApiConstants.EMAIL_RMNDR_DAY_VAL);
            for (Iterator<String> userIter = users.iterator(); userIter.hasNext();) {
            	String principalId = userIter.next();
                try {
                    Collection<ActionItemActionListExtension> actionItems = getActionListService().getActionList(principalId, null);
                    if (actionItems != null && actionItems.size() > 0) {
                        sendPeriodicReminder(principalId, actionItems,
                                KewApiConstants.EMAIL_RMNDR_DAY_VAL);
                    }
                } catch (Exception e) {
                    LOG.error("Error sending daily action list reminder to user: " + principalId, e);
                }
            }
        }
        LOG.info("Daily action list emails successfully sent");
    }

    public void sendWeeklyReminder() {
        LOG.info("Starting sendWeeklyReminder"); 	
        if (sendActionListEmailNotification()) {
            Collection<String> users = getUsersWithEmailSetting(KewApiConstants.EMAIL_RMNDR_WEEK_VAL);
            for (Iterator<String> userIter = users.iterator(); userIter.hasNext();) {
            	String principalId = userIter.next();
                try {
                    Collection<ActionItemActionListExtension> actionItems = getActionListService().getActionList(principalId, null);
                    if (actionItems != null && actionItems.size() > 0) {
                        sendPeriodicReminder(principalId, actionItems,
                                KewApiConstants.EMAIL_RMNDR_WEEK_VAL);
                    }
                } catch (Exception e) {
                    LOG.error("Error sending weekly action list reminder to user: " + principalId, e);
                }
            }
        }
        LOG.info("Weekly action list emails successfully sent");
    }

    protected void sendPeriodicReminder(String principalId, Collection<ActionItemActionListExtension> actionItems, String emailSetting) {
        String emailBody = null;
        actionItems = filterActionItemsToNotify(principalId, actionItems, emailSetting);
        // if there are no action items after being filtered, there's no
        // reason to send the email
        if (actionItems.isEmpty()) {
            return;
        }
        if (KewApiConstants.EMAIL_RMNDR_DAY_VAL.equals(emailSetting)) {
            emailBody = buildDailyReminderBody(actionItems);
        } else if (KewApiConstants.EMAIL_RMNDR_WEEK_VAL.equals(emailSetting)) {
            emailBody = buildWeeklyReminderBody(actionItems);
        }
        Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
        sendEmail(person, getEmailSubject(), new EmailBody(emailBody));
    }

    /**
     * Returns a filtered Collection of {@link ActionItem}s which are filtered according to the
     * user's preferences. If they have opted not to recieve secondary or primary delegation emails
     * then they will not be included.
     */
    protected Collection<ActionItemActionListExtension> filterActionItemsToNotify(String principalId, Collection<ActionItemActionListExtension> actionItems, String emailSetting) {
        List<ActionItemActionListExtension> filteredItems = new ArrayList<ActionItemActionListExtension>();
        Preferences preferences = KewApiServiceLocator.getPreferencesService().getPreferences(principalId);
        for (ActionItemActionListExtension actionItem : actionItems) {
            if (!actionItem.getPrincipalId().equals(principalId)) {
                LOG.warn("Encountered an ActionItem with an incorrect workflow ID.  Was " + actionItem.getPrincipalId()
                        +
                        " but expected " + principalId);
                continue;
            }
            if (checkEmailNotificationPreferences(actionItem, preferences, emailSetting)) {
                filteredItems.add(actionItem);
            }
        }
        return filteredItems;
    }

    protected Collection<String> getUsersWithEmailSetting(String setting) {
        Set<String> users = new HashSet<String>();
        Collection<UserOptions> userOptions = getUserOptionsService().retrieveEmailPreferenceUserOptions(setting);
        for(UserOptions userOption : userOptions) {
            String principalId = userOption.getWorkflowId();
            try {
                if (!users.contains(principalId)) {
                    users.add(principalId);
                } else {
                    LOG.info("User " + principalId + " was already added to the list, so not adding again.");
                }
            } catch (Exception e) {
                LOG.error("error retrieving workflow user with ID: "
                        + principalId);
            }
        }
        return users;
    }

    /**
     * 0 = actionItem.getDocumentId() 1 =
     * actionItem.getRouteHeader().getInitiatorUser().getDisplayName() 2 =
     * actionItem.getRouteHeader().getDocumentType().getName() 3 = actionItem.getDocTitle() 4 =
     * docHandlerUrl 5 = getActionListUrl() 6 = getPreferencesUrl() 7 = getHelpLink(documentType)
     */
    private static final MessageFormat DEFAULT_IMMEDIATE_REMINDER = new MessageFormat(
            "Your Action List has an eDoc(electronic document) that needs your attention: \n\n"
                    +
                    "Document ID:\t{0,number,#}\n"
                    +
                    "Initiator:\t\t{1}\n"
                    +
                    "Type:\t\tAdd/Modify {2}\n"
                    +
                    "Title:\t\t{3}\n"
                    +
                    "\n\n"
                    +
                    "To respond to this eDoc: \n"
                    +
                    "\tGo to {4}\n\n"
                    +
                    "\tOr you may access the eDoc from your Action List: \n"
                    +
                    "\tGo to {5}, and then click on the numeric Document ID: {0,number,#} in the first column of the List. \n"
                    +
                    "\n\n\n" +
                    "To change how these email notifications are sent(daily, weekly or none): \n" +
                    "\tGo to {6}\n" +
                    "\n\n\n" +
                    "{7}\n\n\n"
            );

    /**
     * 0 = actionItem.getDocumentId() 1 =
     * actionItem.getRouteHeader().getInitiatorUser().getDisplayName() 2 =
     * actionItem.getRouteHeader().getDocumentType().getName() 3 = actionItem.getDocTitle() 4 =
     * getActionListUrl() 5 = getPreferencesUrl() 6 = getHelpLink(documentType)
     */
    private static final MessageFormat DEFAULT_IMMEDIATE_REMINDER_NO_DOC_HANDLER = new MessageFormat(
            "Your Action List has an eDoc(electronic document) that needs your attention: \n\n" +
                    "Document ID:\t{0,number,#}\n" +
                    "Initiator:\t\t{1}\n" +
                    "Type:\t\tAdd/Modify {2}\n" +
                    "Title:\t\t{3}\n" +
                    "\n\n" +
                    "To respond to this eDoc you may use your Action List: \n" +
                    "\tGo to {4}, and then take actions related to Document ID: {0,number,#}. \n" +
                    "\n\n\n" +
                    "To change how these email notifications are sent(daily, weekly or none): \n" +
                    "\tGo to {5}\n" +
                    "\n\n\n" +
                    "{6}\n\n\n"
            );

    public String buildImmediateReminderBody(Person person, org.kuali.rice.kew.api.action.ActionItem actionItem, DocumentType documentType) {
        String docHandlerUrl = documentType.getResolvedDocumentHandlerUrl();
        if (StringUtils.isNotBlank(docHandlerUrl)) {
            if (!docHandlerUrl.contains("?")) {
                docHandlerUrl += "?";
            } else {
                docHandlerUrl += "&";
            }
            docHandlerUrl += KewApiConstants.DOCUMENT_ID_PARAMETER + "="
                    + actionItem.getDocumentId();
            docHandlerUrl += "&" + KewApiConstants.COMMAND_PARAMETER + "="
                    + KewApiConstants.ACTIONLIST_COMMAND;
        }
        StringBuffer sf = new StringBuffer();

        /*sf
        		.append("Your Action List has an eDoc(electronic document) that needs your attention: \n\n");
        sf.append("Document ID:\t" + actionItem.getDocumentId() + "\n");
        sf.append("Initiator:\t\t");
        try {
        	sf.append(actionItem.getRouteHeader().getInitiatorUser()
        			.getDisplayName()
        			+ "\n");
        } catch (Exception e) {
        	LOG.error("Error retrieving initiator for action item "
        			+ actionItem.getDocumentId());
        	sf.append("\n");
        }
        sf.append("Type:\t\t" + "Add/Modify "
        		+ actionItem.getRouteHeader().getDocumentType().getName()
        		+ "\n");
        sf.append("Title:\t\t" + actionItem.getDocTitle() + "\n");
        sf.append("\n\n");
        sf.append("To respond to this eDoc: \n");
        sf.append("\tGo to " + docHandlerUrl + "\n\n");
        sf.append("\tOr you may access the eDoc from your Action List: \n");
        sf.append("\tGo to " + getActionListUrl()
        		+ ", and then click on the numeric Document ID: "
        		+ actionItem.getDocumentId()
        		+ " in the first column of the List. \n");
        sf.append("\n\n\n");
        sf
        		.append("To change how these email notifications are sent(daily, weekly or none): \n");
        sf.append("\tGo to " + getPreferencesUrl() + "\n");
        sf.append("\n\n\n");
        sf.append(getHelpLink(documentType) + "\n\n\n");*/

        MessageFormat messageFormat = null;
        String stringMessageFormat = ConfigContext.getCurrentContextConfig().getProperty(
                IMMEDIATE_REMINDER_EMAIL_MESSAGE_KEY);
        LOG.debug("Immediate reminder email message from configuration (" + IMMEDIATE_REMINDER_EMAIL_MESSAGE_KEY
                + "): " + stringMessageFormat);
        if (stringMessageFormat == null) {
            messageFormat = DEFAULT_IMMEDIATE_REMINDER;
        } else {
            messageFormat = new MessageFormat(stringMessageFormat);
        }
        String initiatorUser = (person == null ? "" : person.getName());

        if (StringUtils.isNotBlank(docHandlerUrl)) {
            Object[] args = {actionItem.getDocumentId(),
                    initiatorUser,
                    documentType.getName(),
                    actionItem.getDocTitle(),
                    docHandlerUrl,
                    getActionListUrl(),
                    getPreferencesUrl(),
                    getHelpLink(documentType)
            };

            messageFormat.format(args, sf, new FieldPosition(0));

            LOG.debug("default immediate reminder: " + DEFAULT_IMMEDIATE_REMINDER.format(args));
        } else {
            Object[] args = {actionItem.getDocumentId(),
                    initiatorUser,
                    documentType.getName(),
                    actionItem.getDocTitle(),
                    getActionListUrl(),
                    getPreferencesUrl(),
                    getHelpLink(documentType)
            };

            messageFormat.format(args, sf, new FieldPosition(0));

            LOG.debug("default immediate reminder: " + DEFAULT_IMMEDIATE_REMINDER_NO_DOC_HANDLER.format(args));
        }
        LOG.debug("immediate reminder: " + sf);

        // for debugging purposes on the immediate reminder only
        if (!isProduction()) {
            try {
                sf.append("Action Item sent to " + actionItem.getPrincipalId());
                if (actionItem.getDelegationType() != null) {
                    sf.append(" for delegation type "
                            + actionItem.getDelegationType());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return sf.toString();
    }

    public String buildDailyReminderBody(Collection<ActionItemActionListExtension> actionItems) {
        StringBuffer sf = new StringBuffer();
        sf.append(getDailyWeeklyMessageBody(actionItems));
        sf
                .append("To change how these email notifications are sent (immediately, weekly or none): \n");
        sf.append("\tGo to " + getPreferencesUrl() + "\n");
        // sf.append("\tSend as soon as you get an eDoc\n\t" +
        // getPreferencesUrl() + "\n\n");
        // sf.append("\tSend weekly\n\t" + getPreferencesUrl() + "\n\n");
        // sf.append("\tDo not send\n\t" + getPreferencesUrl() + "\n");
        sf.append("\n\n\n");
        sf.append(getHelpLink() + "\n\n\n");
        return sf.toString();
    }

    public String buildWeeklyReminderBody(Collection<ActionItemActionListExtension> actionItems) {
        StringBuffer sf = new StringBuffer();
        sf.append(getDailyWeeklyMessageBody(actionItems));
        sf
                .append("To change how these email notifications are sent (immediately, daily or none): \n");
        sf.append("\tGo to " + getPreferencesUrl() + "\n");
        // sf.append("\tSend as soon as you get an eDoc\n\t" +
        // getPreferencesUrl() + "\n\n");
        // sf.append("\tSend daily\n\t" + getPreferencesUrl() + "\n\n");
        // sf.append("\tDo not send\n\t" + getPreferencesUrl() + "\n");
        sf.append("\n\n\n");
        sf.append(getHelpLink() + "\n\n\n");
        return sf.toString();
    }

    String getDailyWeeklyMessageBody(Collection<ActionItemActionListExtension> actionItems) {
        StringBuffer sf = new StringBuffer();
        HashMap<String, Integer> docTypes = getActionListItemsStat(actionItems);

        sf
                .append("Your Action List has "
                        + actionItems.size()
                        + " eDocs(electronic documents) that need your attention: \n\n");
        for(String docTypeName : docTypes.keySet()) {
            sf.append("\t" + ((Integer) docTypes.get(docTypeName)).toString()
                    + "\t" + docTypeName + "\n");
        }
        sf.append("\n\n");
        sf.append("To respond to each of these eDocs: \n");
        sf
                .append("\tGo to "
                        + getActionListUrl()
                        + ", and then click on its numeric Document ID in the first column of the List.\n");
        sf.append("\n\n\n");
        return sf.toString();
    }

    private HashMap<String, Integer> getActionListItemsStat(Collection<ActionItemActionListExtension> actionItems) {
        HashMap<String, Integer> docTypes = new LinkedHashMap<String, Integer>();
        Collection<org.kuali.rice.kew.api.action.ActionItem> apiActionItems = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>();
        for(ActionItem actionItem : actionItems) {
            apiActionItems.add(ActionItem.to(actionItem));
        }
        Map<String, DocumentRouteHeaderValue> routeHeaders = KEWServiceLocator.getRouteHeaderService().getRouteHeadersForActionItems(apiActionItems);
        Iterator<ActionItemActionListExtension> iter = actionItems.iterator();

        while (iter.hasNext()) {
            String docTypeName = routeHeaders.get(iter.next().getDocumentId()).getDocumentType().getName();
            if (docTypes.containsKey(docTypeName)) {
                docTypes.put(docTypeName, new Integer(docTypes.get(docTypeName).intValue() + 1));
            } else {
                docTypes.put(docTypeName, new Integer(1));
            }
        }
        return docTypes;
    }

    protected boolean sendActionListEmailNotification() {
        if (LOG.isDebugEnabled())
            LOG.debug("actionlistsendconstant: "
                    + CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                            KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE,
                            KewApiConstants.ACTION_LIST_SEND_EMAIL_NOTIFICATION_IND));

        return KewApiConstants.ACTION_LIST_SEND_EMAIL_NOTIFICATION_VALUE
                .equals(CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                        KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE,
                        KewApiConstants.ACTION_LIST_SEND_EMAIL_NOTIFICATION_IND));
    }

    public void scheduleBatchEmailReminders() throws Exception {
        String emailBatchGroup = "Email Batch";
        String dailyCron = ConfigContext.getCurrentContextConfig()
                .getProperty(KewApiConstants.DAILY_EMAIL_CRON_EXPRESSION);
        if (!StringUtils.isBlank(dailyCron)) {
            LOG.info("Scheduling Daily Email batch with cron expression: " + dailyCron);
            CronTrigger dailyTrigger = new CronTrigger(DAILY_TRIGGER_NAME, emailBatchGroup, dailyCron);
            JobDetail dailyJobDetail = new JobDetail(DAILY_JOB_NAME, emailBatchGroup, DailyEmailJob.class);
            dailyTrigger.setJobName(dailyJobDetail.getName());
            dailyTrigger.setJobGroup(dailyJobDetail.getGroup());
            addJobToScheduler(dailyJobDetail);
            addTriggerToScheduler(dailyTrigger);
        } else {
            LOG.warn("No " + KewApiConstants.DAILY_EMAIL_CRON_EXPRESSION
                    + " parameter was configured.  Daily Email batch was not scheduled!");
        }

        String weeklyCron = ConfigContext.getCurrentContextConfig().getProperty(
                KewApiConstants.WEEKLY_EMAIL_CRON_EXPRESSION);
        if (!StringUtils.isBlank(weeklyCron)) {
            LOG.info("Scheduling Weekly Email batch with cron expression: " + weeklyCron);
            CronTrigger weeklyTrigger = new CronTrigger(WEEKLY_TRIGGER_NAME, emailBatchGroup, weeklyCron);
            JobDetail weeklyJobDetail = new JobDetail(WEEKLY_JOB_NAME, emailBatchGroup, WeeklyEmailJob.class);
            weeklyTrigger.setJobName(weeklyJobDetail.getName());
            weeklyTrigger.setJobGroup(weeklyJobDetail.getGroup());
            addJobToScheduler(weeklyJobDetail);
            addTriggerToScheduler(weeklyTrigger);
        } else {
            LOG.warn("No " + KewApiConstants.WEEKLY_EMAIL_CRON_EXPRESSION
                    + " parameter was configured.  Weekly Email batch was not scheduled!");
        }
    }

    private void addJobToScheduler(JobDetail jobDetail) throws SchedulerException {
        getScheduler().addJob(jobDetail, true);
    }

    private void addTriggerToScheduler(Trigger trigger) throws SchedulerException {
        boolean triggerExists = (getScheduler().getTrigger(trigger.getName(), trigger.getGroup()) != null);
        if (!triggerExists) {
            try {
                getScheduler().scheduleJob(trigger);
            } catch (ObjectAlreadyExistsException ex) {
                getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
            }
        } else {
            getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
        }
    }

    private Scheduler getScheduler() {
        return KSBServiceLocator.getScheduler();
    }

    private UserOptionsService getUserOptionsService() {
        return (UserOptionsService) KEWServiceLocator
                .getUserOptionsService();
    }

    protected ActionListService getActionListService() {
        return (ActionListService) KEWServiceLocator.getActionListService();
    }

    public String getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public void setDeploymentEnvironment(String deploymentEnvironment) {
        this.deploymentEnvironment = deploymentEnvironment;
    }

    protected String getActionListUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.WORKFLOW_URL_KEY)
                + "/" + "ActionList.do";
    }

    protected String getPreferencesUrl() {
        return ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.WORKFLOW_URL_KEY)
                + "/" + "Preferences.do";
    }
}
