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
package mocks;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.feedback.web.FeedbackForm;
import org.kuali.rice.kew.mail.service.impl.StyleableEmailContentServiceImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;

/**
 * This is a class used to substitute for a StyleableEmailContentServiceImpl class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MockStyleableEmailContentServiceImpl extends StyleableEmailContentServiceImpl implements MockStyleableEmailContentService {

    private boolean wasAccessed = false;

    @Override
    public EmailContent generateImmediateReminder(Person user, ActionItem actionItem, DocumentType documentType) {
        wasAccessed = true;
        return super.generateImmediateReminder(user, actionItem, documentType);
    }

    @Override
    public EmailContent generateDailyReminder(Person user, Collection<ActionItem> actionItems) {
        wasAccessed = true;
        return super.generateDailyReminder(user, actionItems);
    }

    @Override
    public EmailContent generateWeeklyReminder(Person user, Collection<ActionItem> actionItems) {
        wasAccessed = true;
        return super.generateWeeklyReminder(user, actionItems);
    }

    @Override
    public EmailContent generateFeedback(FeedbackForm form) {
        wasAccessed = true;
        return super.generateFeedback(form);
    }

    /**
     * This overridden method is used in case the action item has an null route header attached
     *
     * @see org.kuali.rice.kew.mail.service.impl.StyleableEmailContentServiceImpl#getRouteHeader(org.kuali.rice.kew.actionitem.ActionItem)
     */
    @Override
    public DocumentRouteHeaderValue getRouteHeader(ActionItem actionItem) {
    	DocumentRouteHeaderValue routeHeader = null;
        if (actionItem.getDocumentId() != null) {
            routeHeader = super.getRouteHeader(actionItem);
        }
        if (routeHeader == null) {
        	routeHeader = new DocumentRouteHeaderValue();
        	routeHeader.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_ENROUTE_CD);
        	routeHeader.setCreateDate(new Timestamp(new Date().getTime()));
        }
        return routeHeader;
    }

    @Override
    public Map<String,DocumentRouteHeaderValue> getRouteHeaders(Collection<ActionItem> actionItems) {
    	Map<String,DocumentRouteHeaderValue> routeHeaders = super.getRouteHeaders(actionItems);
    	DocumentRouteHeaderValue routeHeader = null;
    	for (ActionItem actionItem : actionItems) {
    		if (routeHeaders.get(actionItem.getDocumentId()) == null) {
    			routeHeader = new DocumentRouteHeaderValue();
            	routeHeader.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_ENROUTE_CD);
            	routeHeader.setCreateDate(new Timestamp(new Date().getTime()));
            	routeHeaders.put(actionItem.getDocumentId(), routeHeader);
    		}
    	}
    	return routeHeaders;
    }
    
    /**
     * This method returns whether this service is being used
     */
    public boolean wasServiceAccessed() {
        return this.wasAccessed;
    }

    /**
     * This method returns whether this service is being used
     */
    public void resetServiceAccessed() {
        this.wasAccessed = false;
    }

}
