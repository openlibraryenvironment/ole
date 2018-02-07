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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.mail.service.EmailContentService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 * ActionListEmailService implementation whose content is configurable/parameterizable
 * via a pluggable EmailContentService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CustomizableActionListEmailServiceImpl extends ActionListEmailServiceImpl {
    private static final Logger LOG = Logger.getLogger(CustomizableActionListEmailServiceImpl.class);

    private EmailContentService contentService;

    // ---- Spring property

    public void setEmailContentGenerator(EmailContentService contentService) {
        this.contentService = contentService;
    }

    protected EmailContentService getEmailContentGenerator() {
        return contentService;
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
        
        if (!sendActionListEmailNotification()) {
            LOG.debug("not sending immediate reminder");
            return;
        }
        
        // since this is a message for a single document, we can customize the from
        // line based on DocumentType
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId());
        Person person = KimApiServiceLocator.getPersonService().getPerson(actionItem.getPrincipalId());
        if (person != null) {
            EmailContent content = getEmailContentGenerator().generateImmediateReminder(person, actionItem, document.getDocumentType());
            sendEmail(person, new EmailSubject(content.getSubject()),
                        new EmailBody(content.getBody()), document.getDocumentType());
        }
    }

    @Override
    protected void sendPeriodicReminder(String principalId, Collection<ActionItemActionListExtension> actionItems, String emailSetting) {
        actionItems = filterActionItemsToNotify(principalId, actionItems, emailSetting);
        Collection<org.kuali.rice.kew.api.action.ActionItem> apiActionItems = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>();
        for(ActionItem actionItem : actionItems) {
            apiActionItems.add(ActionItem.to(actionItem));
        }
        // if there are no action items after being filtered, there's no
        // reason to send the email
        if (actionItems.isEmpty()) {
            return;
        }
        EmailContent content;
        Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
        if (KewApiConstants.EMAIL_RMNDR_DAY_VAL.equals(emailSetting)) {
            content = getEmailContentGenerator().generateDailyReminder(person, apiActionItems);
        } else if (KewApiConstants.EMAIL_RMNDR_WEEK_VAL.equals(emailSetting)) {
            content = getEmailContentGenerator().generateWeeklyReminder(person, apiActionItems);
        } else {
            // else...refactor this...
            throw new RuntimeException("invalid email setting. this code needs refactoring");
        }
        sendEmail(person, new EmailSubject(content.getSubject()), new EmailBody(content.getBody()));
    }

}
