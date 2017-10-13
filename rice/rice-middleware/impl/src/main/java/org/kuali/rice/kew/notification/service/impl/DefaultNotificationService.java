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
package org.kuali.rice.kew.notification.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ComparatorUtils;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.actionitem.ActionItemComparator;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.mail.ImmediateEmailReminderQueue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.notification.service.NotificationService;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * The default implementation of the NotificationService.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DefaultNotificationService implements NotificationService {

	protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());

	private static final Comparator notificationPriorityComparator = ComparatorUtils.reversedComparator(new ActionItemComparator());

	/**
	 * Queues up immediate email processors for ActionItem notification.  Prioritizes the list of
	 * Action Items passed in and attempts to not send out multiple emails to the same user.
	 */
	public void notify(List<ActionItem> actionItems) {
		// sort the list of action items using the same comparator as the Action List
		Collections.sort(actionItems, notificationPriorityComparator);
		Set sentNotifications = new HashSet();
		for (Iterator iterator = actionItems.iterator(); iterator.hasNext();) {
			ActionItem actionItem = (ActionItem) iterator.next();
            if (!sentNotifications.contains(actionItem.getPrincipalId()) && shouldNotify(actionItem)) {
                sentNotifications.add(actionItem.getPrincipalId());
				sendNotification(actionItem);
			}
		}
	}

	/**
	 * Sends a notification
	 * @param actionItem the action item
	 */
	protected void sendNotification(ActionItem actionItem) {
        ImmediateEmailReminderQueue immediateEmailQueue = KewApiServiceLocator.getImmediateEmailReminderQueue();
        immediateEmailQueue.sendReminder(actionItem, RouteContext.getCurrentRouteContext().isDoNotSendApproveNotificationEmails());
        // TODO: JLR - replace with direct call to ActionListEmailService 
	}

	protected boolean shouldNotify(ActionItem actionItem) {
		try {
            boolean sendEmail = true;
            // Removed preferences items since they will be checked before it sends
            // the email in the action list email service

			// don't send notification if this action item came from a SAVE action and the NOTIFY_ON_SAVE policy is not set
			if (sendEmail && isItemOriginatingFromSave(actionItem) && !shouldNotifyOnSave(actionItem)) {
				sendEmail = false;
			}
			return sendEmail;
		} catch (Exception e) {
			throw new WorkflowRuntimeException("Error loading user with workflow id " + actionItem.getPrincipalId() + " for notification.", e);
		}
	}

	/**
	 * Returns true if the ActionItem doesn't represent a request generated from a "SAVE" action or, if it does,
	 * returns true if the document type policy
	 */
	protected boolean isItemOriginatingFromSave(ActionItem actionItem) {
		return actionItem.getResponsibilityId() != null && actionItem.getResponsibilityId().equals(KewApiConstants.SAVED_REQUEST_RESPONSIBILITY_ID);
	}

	protected boolean shouldNotifyOnSave(ActionItem actionItem) {
		DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId());
		DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findById(document.getDocumentTypeId());
		return documentType.getNotifyOnSavePolicy().getPolicyValue().booleanValue();
	}

    public void removeNotification(List<ActionItem> actionItems) {
        // nothing
    }
}
