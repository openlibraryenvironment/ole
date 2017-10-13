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
package org.kuali.rice.ken.service.impl;

import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationResponseBo;
import org.kuali.rice.ken.dao.NotificationDao;
import org.kuali.rice.ken.deliverer.impl.KEWActionListMessageDeliverer;
import org.kuali.rice.ken.service.NotificationAuthorizationService;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.NotificationRecipientService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.util.NotificationConstants;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;

//import org.kuali.rice.core.jpa.criteria.Criteria;

/**
 * NotificationService implementation - this is the default out-of-the-box implementation of the service.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationServiceImpl implements NotificationService {
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	.getLogger(NotificationServiceImpl.class);

	private GenericDao businessObjectDao;
	private NotificationDao notDao;
	private NotificationMessageContentService messageContentService;
	private NotificationAuthorizationService notificationAuthorizationService;
	private NotificationRecipientService notificationRecipientService;
	private NotificationWorkflowDocumentService notificationWorkflowDocumentService;
	private NotificationMessageDeliveryService notificationMessageDeliveryService;

	/**
	 * Constructs a NotificationServiceImpl class instance.
	 * @param businessObjectDao
	 * @param messageContentService
	 * @param notificationAuthorizationService
	 * @param notificationRecipientService
	 * @param notificationWorkflowDocumentService
	 * @param notificationMessageDeliveryService
	 */
	public NotificationServiceImpl(GenericDao businessObjectDao, NotificationMessageContentService messageContentService,
			NotificationAuthorizationService notificationAuthorizationService, NotificationRecipientService notificationRecipientService, 
			NotificationWorkflowDocumentService notificationWorkflowDocumentService, 
			NotificationMessageDeliveryService notificationMessageDeliveryService,
			NotificationDao notDao) {
		this.businessObjectDao = businessObjectDao;
		this.messageContentService = messageContentService;
		this.notificationAuthorizationService = notificationAuthorizationService;
		this.notificationRecipientService = notificationRecipientService;
		this.notificationWorkflowDocumentService = notificationWorkflowDocumentService;
		this.notificationMessageDeliveryService = notificationMessageDeliveryService;
		this.notDao = notDao;
	}

	/**
	 * This is the default implementation that uses the businessObjectDao.
	 * @see org.kuali.rice.ken.service.NotificationService#getNotification(java.lang.Long)
	 */
	public NotificationBo getNotification(Long id) {
		HashMap<String, Long> primaryKeys = new HashMap<String, Long>();
		primaryKeys.put(NotificationConstants.BO_PROPERTY_NAMES.ID, id);

		return (NotificationBo) businessObjectDao.findByPrimaryKey(NotificationBo.class, primaryKeys);
	}

	/**
	 * This method is responsible for parsing out the notification message which is sent in as a String 
	 * of XML.  It calls the appropriate services to validate the message content, converts it to a BO, 
	 * and then passes it to another service where its content and meta-data is validated and if successful, it 
	 * is saved.
	 * @see org.kuali.rice.ken.service.NotificationService#sendNotification(java.lang.String)
	 */
	public NotificationResponseBo sendNotification(String notificationMessageAsXml) throws IOException, XmlException {
		// try to parse out the XML with the message content service
		NotificationBo notification = messageContentService.parseNotificationRequestMessage(notificationMessageAsXml);

		// now call out to the meat of the notification sending - this will validate users, groups, producers, and save
		return sendNotification(notification);
	}

	/**
	 * @see org.kuali.rice.ken.service.NotificationService#sendNotification(org.kuali.rice.ken.bo.NotificationBo)
	 */
	public NotificationResponseBo sendNotification(NotificationBo notification) {
		NotificationResponseBo response = new NotificationResponseBo();

		// make sure that the producer is able to send notifications on behalf of the channel
		boolean producerAuthorizedForChannel = notificationAuthorizationService.isProducerAuthorizedToSendNotificationForChannel(notification.getProducer(), notification.getChannel());
		if(!producerAuthorizedForChannel) {
			LOG.error("Producer " + notification.getProducer() + " is not authorized to send messages to channel " + notification.getChannel());
			response.setStatus(NotificationConstants.RESPONSE_STATUSES.FAILURE);
			response.setMessage(NotificationConstants.RESPONSE_MESSAGES.PRODUCER_NOT_AUTHORIZED_FOR_CHANNEL);
			return response;
		}

		// make sure that the recipients are valid
		for(int i = 0; i < notification.getRecipients().size(); i++) {
			NotificationRecipientBo recipient = notification.getRecipient(i);
			boolean validRecipient = notificationRecipientService.isRecipientValid(recipient.getRecipientId(), recipient.getRecipientType());
			if(!validRecipient) {
				response.setStatus(NotificationConstants.RESPONSE_STATUSES.FAILURE);
				response.setMessage(NotificationConstants.RESPONSE_MESSAGES.INVALID_RECIPIENT + " - recipientId=" + 
						recipient.getRecipientId() + ", recipientType=" + recipient.getRecipientType());
				return response;
			}
		}

		// set the creationDateTime attribute to the current timestamp if it's currently null
		if (notification.getCreationDateTime() == null) {
			notification.setCreationDateTimeValue(new Timestamp(System.currentTimeMillis()));
		}

		// set the sendDateTime attribute to the current timestamp if it's currently null
		if(notification.getSendDateTime() == null) {
			notification.setSendDateTimeValue(new Timestamp(System.currentTimeMillis()));
		}

		// if the autoremove time is before the send date time, reject the notification
		if (notification.getAutoRemoveDateTime() != null) {
			if (notification.getAutoRemoveDateTimeValue().before(notification.getSendDateTimeValue()))  {
				response.setStatus(NotificationConstants.RESPONSE_STATUSES.FAILURE);
				response.setMessage(NotificationConstants.RESPONSE_MESSAGES.INVALID_REMOVE_DATE);
				return response;
			}
		}

		// make sure the delivery types are valid
		if(!notification.getDeliveryType().equalsIgnoreCase(NotificationConstants.DELIVERY_TYPES.ACK) && 
				!notification.getDeliveryType().equalsIgnoreCase(NotificationConstants.DELIVERY_TYPES.FYI)) {
			response.setStatus(NotificationConstants.RESPONSE_STATUSES.FAILURE);
			response.setMessage(NotificationConstants.RESPONSE_MESSAGES.INVALID_DELIVERY_TYPE + " - deliveryType=" + 
					notification.getDeliveryType());
			return response;
		}

		// now try to persist the object
		try {
			businessObjectDao.save(notification);
		} catch(Exception e) {
			response.setStatus(NotificationConstants.RESPONSE_STATUSES.FAILURE);
			response.setMessage(NotificationConstants.RESPONSE_MESSAGES.ERROR_SAVING_NOTIFICATION);
			return response;
		}

		// everything looks good!
		response.setMessage(NotificationConstants.RESPONSE_MESSAGES.SUCCESSFULLY_RECEIVED);
		response.setNotificationId(notification.getId());
		return response;
	}

	/**
	 * This is the default implementation that uses the businessObjectDao and its findMatching method.
	 * @see org.kuali.rice.ken.service.NotificationService#getNotificationsForRecipientByType(java.lang.String, java.lang.String)
	 */
	public Collection getNotificationsForRecipientByType(String contentTypeName, String recipientId) {
		HashMap<String, String> queryCriteria = new HashMap<String, String>();
		queryCriteria.put(NotificationConstants.BO_PROPERTY_NAMES.CONTENT_TYPE_NAME, contentTypeName);
		queryCriteria.put(NotificationConstants.BO_PROPERTY_NAMES.RECIPIENTS_RECIPIENT_ID, recipientId);

		return businessObjectDao.findMatching(NotificationBo.class, queryCriteria);
	}

	/**
	 * @see org.kuali.rice.ken.service.NotificationService#dismissNotificationMessageDelivery(java.lang.Long, java.lang.String)
	 */
	public void dismissNotificationMessageDelivery(Long id, String user, String cause) {
		// TODO: implement pessimistic locking on the message delivery
		NotificationMessageDelivery nmd = notificationMessageDeliveryService.getNotificationMessageDelivery(id);
		dismissNotificationMessageDelivery(nmd, user, cause);
	}

	/**
	 * @see org.kuali.rice.ken.service.NotificationService#dismissNotificationMessageDelivery(org.kuali.rice.ken.bo.NotificationMessageDelivery, java.lang.String, java.lang.String)
	 */   
	public void dismissNotificationMessageDelivery(NotificationMessageDelivery nmd, String user, String cause) {
		// get the notification that generated this particular message delivery
		NotificationBo notification = nmd.getNotification();

		// get all of the other deliveries of this notification for the user
		Collection<NotificationMessageDelivery> userDeliveries = notificationMessageDeliveryService.getNotificationMessageDeliveries(notification, nmd.getUserRecipientId());

		final String targetStatus;
		// if the cause was our internal "autoremove" cause, then we need to indicate
		// the message was autoremoved instead of normally dismissed
		if (NotificationConstants.AUTO_REMOVE_CAUSE.equals(cause)) {
			targetStatus = NotificationConstants.MESSAGE_DELIVERY_STATUS.AUTO_REMOVED;
		} else {
			targetStatus = NotificationConstants.MESSAGE_DELIVERY_STATUS.REMOVED;
		}

		KEWActionListMessageDeliverer deliverer = new KEWActionListMessageDeliverer();
		// TODO: implement pessimistic locking on all these message deliveries
		// now, do dispatch in reverse...dismiss each message delivery via the appropriate deliverer
		for (NotificationMessageDelivery messageDelivery: userDeliveries) {

			// don't attempt to dismiss undelivered message deliveries
			if (!NotificationConstants.MESSAGE_DELIVERY_STATUS.DELIVERED.equals(messageDelivery.getMessageDeliveryStatus())) {
				LOG.info("Skipping dismissal of non-delivered message delivery #" + messageDelivery.getId());
			} else if (targetStatus.equals(messageDelivery.getMessageDeliveryStatus())) {
				LOG.info("Skipping dismissal of already removed message delivery #" + messageDelivery.getId());
			} else {
				LOG.debug("Dismissing message delivery #" + messageDelivery.getId() + " " + messageDelivery.getVersionNumber());//.getLockVerNbr());

				// we have our message deliverer, so tell it to dismiss the message
				//try {
				deliverer.dismissMessageDelivery(messageDelivery, user, cause);
				//} catch (NotificationMessageDismissalException nmde) {
				//LOG.error("Error dismissing message " + messageDelivery, nmde);
				//throw new RuntimeException(nmde);
				//}
			}

			// by definition we have succeeded at this point if no exception was thrown by the messageDeliverer
			// so update the status of the delivery message instance to indicate its dismissal
			// if the message delivery was not actually delivered in the first place, we still need to mark it as
			// removed here so delivery is not attempted again
			messageDelivery.setMessageDeliveryStatus(targetStatus);
			// TODO: locking
			// mark as unlocked
			//messageDelivery.setLockedDate(null);
			LOG.debug("Saving message delivery #" + messageDelivery.getId() + " " + messageDelivery.getVersionNumber());
			businessObjectDao.save(messageDelivery);

			LOG.debug("Message delivery '" + messageDelivery.getId() + "' for notification '" + messageDelivery.getNotification().getId() + "' was successfully dismissed.");
		}
	}

	/**
	 * This method is responsible for atomically finding all untaken, unresolved notifications that are ready to be sent,
	 * marking them as taken and returning them to the caller for processing.
	 * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
	 * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
	 * the caller should arrange to invoke this from within a newly created transaction).
	 * @return a list of available notifications that have been marked as taken by the caller
	 */
	//switch to JPA criteria
	public Collection<NotificationBo> takeNotificationsForResolution() {
		// get all unprocessed notifications with sendDateTime <= current
//		Criteria criteria = new Criteria();
//		criteria.addEqualTo(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
//		criteria.addLessOrEqualThan(NotificationConstants.BO_PROPERTY_NAMES.SEND_DATE_TIME, new Timestamp(System.currentTimeMillis()));
//		criteria.addIsNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);
		//criteria = Util.makeSelectForUpdate(criteria);

		//		Criteria criteria = new Criteria(Notification.class.getName());
		//		criteria.eq(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
		//		criteria.lte(NotificationConstants.BO_PROPERTY_NAMES.SEND_DATE_TIME, new Timestamp(System.currentTimeMillis()));
		//		criteria.isNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);

		//Collection<Notification> available_notifications = businessObjectDao.findMatching(Notification.class, criteria, true, RiceConstants.NO_WAIT);
		
		Collection<NotificationBo> available_notifications = notDao.findMatchedNotificationsForResolution(new Timestamp(System.currentTimeMillis()), businessObjectDao);

		//LOG.debug("Available notifications: " + available_notifications.size());

		// mark as "taken"
		if (available_notifications != null) {
			for (NotificationBo notification: available_notifications) {
				LOG.info("notification: " + notification);
				notification.setLockedDateValue(new Timestamp(System.currentTimeMillis()));
				businessObjectDao.save(notification);
			}
		}


		return available_notifications;
	}

	/**
	 * Unlocks specified notification
	 * @param notification the notification object to unlock
	 */
	//switch to JPA criteria
	public void unlockNotification(NotificationBo notification) {
//		Map<String, Long> criteria = new HashMap<String, Long>();
//		criteria.put(NotificationConstants.BO_PROPERTY_NAMES.ID, notification.getId());
//		Criteria criteria = new Criteria();
//		criteria.addEqualTo(NotificationConstants.BO_PROPERTY_NAMES.ID, notification.getId());
		//criteria = Util.makeSelectForUpdate(criteria);

		//		Criteria criteria = new Criteria(Notification.class.getName());
		//		criteria.eq(NotificationConstants.BO_PROPERTY_NAMES.ID, notification.getId());

		//Collection<Notification> notifications = businessObjectDao.findMatching(Notification.class, criteria, true, RiceConstants.NO_WAIT);
		
		Collection<NotificationBo> notifications = notDao.findMatchedNotificationsForUnlock(notification, businessObjectDao);
		
		if (notifications == null || notifications.size() == 0) {
			throw new RuntimeException("Notification #" + notification.getId() + " not found to unlock");
		}

		NotificationBo n = notifications.iterator().next();
		n.setLockedDateValue(null);

		businessObjectDao.save(n);
	}
}
