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
package org.kuali.rice.kew.mail.service;

import java.util.Collection;

import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.feedback.web.FeedbackForm;
import org.kuali.rice.kim.api.identity.Person;

/**
 * Interface for generating email message content for various types of messages the system needs to send
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see EmailContent
 */
public interface EmailContentService {
    public EmailContent generateImmediateReminder(Person person, ActionItem actionItem, DocumentType documentType);
    public EmailContent generateDailyReminder(Person person, Collection<ActionItem> actionItems);
    public EmailContent generateWeeklyReminder(Person person, Collection<ActionItem> actionItems);
    public EmailContent generateFeedback(FeedbackForm form);

    /* these are more or less helper utilities and probably should live in some core helper class */
    public String getDocumentTypeEmailAddress(DocumentType documentType);
    public String getApplicationEmailAddress();
}
