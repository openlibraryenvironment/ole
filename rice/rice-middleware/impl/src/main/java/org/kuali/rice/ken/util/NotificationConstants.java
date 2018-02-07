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
package org.kuali.rice.ken.util;

import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.kew.api.KewApiConstants;

/**
 * This class houses all constants for the NotificationSystem.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class NotificationConstants {

    /**
     * This inner class holds contants that are used for parsing and resolving of content 
     * inside of the XML message format that represents a notification request.
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class XML_MESSAGE_CONSTANTS {
        public static final String MESSAGE_OPEN = "<message><![CDATA[";
        public static final String MESSAGE_CLOSE = "]]></message>";
        public static final String CONTENT_SIMPLE_OPEN = "<content xmlns=\"ns:notification/ContentTypeSimple\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"ns:notification/ContentTypeSimple resource:notification/ContentTypeSimple\">";
        public static final String CONTENT_EVENT_OPEN = "<content xmlns=\"ns:notification/ContentTypeEvent\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"ns:notification/ContentTypeEvent resource:notification/ContentTypeEvent\">";
        public static final String CONTENT_CLOSE = "</content>";

        private XML_MESSAGE_CONSTANTS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different content types for the Notification System.  These are static out of the box content types that have specific UIs built for them.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class CONTENT_TYPES {
        public static final String EVENT_CONTENT_TYPE = "Event";
        public static final String SIMPLE_CONTENT_TYPE = "Simple";

        private CONTENT_TYPES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different delivery types for the Notification System.
     * FYI - user gets notification in list, but can remove without actually viewing details and taking action
     * ACK - user gets notification in list, but has to view details and conciously press the acknowledge button
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class DELIVERY_TYPES {
        public static final String FYI = "FYI";
        public static final String ACK = "ACK";

        private DELIVERY_TYPES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * String that indicates the cause of a dismissal of a messageDelivery was
     * due to autoremoval
     */
    public static final String AUTO_REMOVE_CAUSE = "autoremove";
    public static final String ACK_CAUSE = "ack";
    public static final String FYI_CAUSE = "fyi";

    /**
     * This class holds constants for different detail views of a notification.
     * INLINE - display the details inline (i.e. without any menus around them)
     * NORMAL_VIEW - display the details in a normal view with all of the appropriate menus
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class NOTIFICATION_DETAIL_VIEWS {
        public static final String NORMAL_VIEW = "displayActionListView";
        public static final String INLINE = "displayActionListInlineView";
        public static final String DOC_SEARCH_VIEW = "displayDocSearchView";

        private NOTIFICATION_DETAIL_VIEWS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Constants for request processing (web layer).
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class NOTIFICATION_CONTROLLER_CONSTANTS {
        public static final String MSG_DELIVERY_ID = "messageDeliveryId";
        public static final String DELIVERER_ID = "delivererId";
        public static final String DOC_ID = "docId";
        public static final String COMMAND = "command";
        public static final String STANDALONE_WINDOW = "standaloneWindow";

        private NOTIFICATION_CONTROLLER_CONSTANTS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different delivery status flags for the notification system.
     * UNDELIVERED - the notification has not been delivered by the delivery machine
     * DELIVERED - the notification has been delivered by the delivery machine
     * REMOVED - the notification has been removed by the delivery machine
     * AUTO_REMOVED - the notification was auto removed by the delivery machine
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class MESSAGE_DELIVERY_STATUS {
        public static final String UNDELIVERED = "UNDELIVERED";
        public static final String DELIVERED = "DELIVERED";
        public static final String REMOVED = "REMOVED";
        public static final String AUTO_REMOVED = "AUTO_REMOVED";

        private MESSAGE_DELIVERY_STATUS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different message delivery types for the notification system.
     * KEW_ACTION_LIST_MESSAGE_DELIVERY_TYPE - the KEW action list
     * DEFAULT_MESSAGE_DELIVERY_TYPE - the default message delivery type
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class MESSAGE_DELIVERY_TYPES {
        public static final String KEW_ACTION_LIST_MESSAGE_DELIVERY_TYPE = "KEWActionList"; 
        public static final String DEFAULT_MESSAGE_DELIVERY_TYPE = KEW_ACTION_LIST_MESSAGE_DELIVERY_TYPE;

        private MESSAGE_DELIVERY_TYPES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different recipient types for the notification system.
     * USER - the notification specifically has a user as one of its recipients
     * GROUP - the notification specifically has a group as one of its recipients
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class RECIPIENT_TYPES {
        public static final String USER = "USER";
        public static final String GROUP = "GROUP";

        private RECIPIENT_TYPES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different processing flags for the notification system.
     * RESOLVED - the notification has been resolved by the system and specific message deliveries have been created
     * UNRESOLVED - the notification has not been resolved by the system
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class PROCESSING_FLAGS {
        public static final String RESOLVED = "RESOLVED";
        public static final String UNRESOLVED = "UNRESOLVED";

        private PROCESSING_FLAGS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Flags for record locking
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class LOCKED_FLAG {
        public static final String UNLOCKED = "U";
        public static final String LOCKED = "L";

        private LOCKED_FLAG() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different response statuses for sending notifications.
     * SUCCESS - the notification message was successfully received and will be processed and delivered by the system
     * FAILURE - the notification message was received but there were problems and it will not be processed and delivered by the system.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class RESPONSE_STATUSES {
        public static final String SUCCESS = "Success";
        public static final String FAILURE = "Failure";

        private RESPONSE_STATUSES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * Different response messages for sending notifications.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class RESPONSE_MESSAGES {
        public static final String SUCCESSFULLY_RECEIVED = "The notification message has been successfully received by the system and will be processed and delivered.";
        public static final String PRODUCER_NOT_AUTHORIZED_FOR_CHANNEL = "The notification message was received by the system; however, " +
            "we cannot process it because the Producer specified cannot send notifications for the Notification Channel specified.";
        public static final String INVALID_RECIPIENT = "The notification message was received by the system; however, we cannot process it because one of " +
            "the recipients that was specified was invalid and is not registered in the system.";
        public static final String ERROR_SAVING_NOTIFICATION = "The notification message was received by the system; however, we cannot process it because " +
            "there was a problem when trying to save the notification to the database.";
        public static final String INVALID_DELIVERY_TYPE = "The notification message was received by the system; however, we cannot process it because " +
            "the specified delivery type was invalid.";
        public static final String INVALID_REMOVE_DATE = "The notification message was received by the system; however, we cannot process it because " +
    "the specified auto-remove date is before the send date.";

        private RESPONSE_MESSAGES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * This class houses constants that represent the property names for the business objects.  This should 
     * be used for building database queries.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class BO_PROPERTY_NAMES {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String NAMESPACE = "namespace";
        public static final String CHANNEL_ID = "channel.id";
        public static final String RECIPIENT_TYPE = "recipientType";
        public static final String RECIPIENT_ID = "recipientId";
        public static final String REVIEWER_ID = "reviewerId";
        public static final String RECIPIENTS_RECIPIENT_ID = "recipients.recipientId";
        public static final String PROPERTY = "property";
        public static final String VALUE = "value";
        public static final String USER_ID = "userId";
        public static final String CONTENT = "content";
        public static final String PRODUCER_ID = "producerId";
        public static final String CONTENT_TYPE_ID = "contentType.id";
        public static final String CONTENT_TYPE_NAME = "contentType.name";
        public static final String PROCESSING_FLAG = "processingFlag";
        public static final String SEND_DATE_TIME = "sendDateTimeValue";
        public static final String NOTIFICATION_AUTO_REMOVE_DATE_TIME = "notification.autoRemoveDateTimeValue";
        public static final String MESSAGE_DELIVERY_STATUS = "messageDeliveryStatus";
        public static final String LOCKED_DATE = "lockedDateValue";
        // NotificationMessageDelivery
        public static final String NOTIFICATION = "notification";
        public static final String USER_RECIPIENT_ID = "userRecipientId";
        public static final String DELIVERY_SYSTEM_ID = "deliverySystemId";

        private BO_PROPERTY_NAMES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * This inner class is used to hold constants needed for KEW integration.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class KEW_CONSTANTS {
        public static final String NOTIFICATION_DOC_TYPE = "KualiNotification";
        public static final String NOTIFICATION_SYSTEM_USER = "notsys";
        public static final String NOTIFICATION_SYSTEM_USER_NAME = "Notification System";
        public static final String NOTIFICATION_SYSTEM_USER_PARAM = "ken.system.user";
        public static final String FYI_AD_HOC_ROUTE = KewApiConstants.ACTION_REQUEST_FYI_REQ;
        public static final String ACK_AD_HOC_ROUTE = KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ;
        public static final String GENERIC_DELIVERY_ANNOTATION = "The notification message has been delivered by the Notification System.";
        public static final String GENERIC_AUTO_REMOVE_ANNOTATION = "The notification message has been auto-removed by the Notification System.";
        public static final String NOTIFICATION_ADMIN_GROUP_NAME = "NotificationAdmin";
        public static final String SEND_NOTIFICATION_REQ_DOC_TYPE = "SendNotificationRequest";

        private KEW_CONSTANTS() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    /**
     * This inner class is used to hold constants around needed for the default system producer which all channels must be exposed 
     * to for the generic message sending form to work with.
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class NOTIFICATION_PRODUCERS {
        public static final NotificationProducerBo NOTIFICATION_SYSTEM_PRODUCER = buildNotificationSystemProducer();

        public static final String NOTIFICATION_SYSTEM_PRODUCER_NAME = "Notification System";
        public static final String NOTIFICATION_SYSTEM_PRODUCER_DESCRIPTION = "This producer represents messages sent from the general message sending form.";
        public static final String NOTIFICATION_SYSTEM_PRODUCER_CONTACT_INFO = "admins-notsys@cornell.edu";

        /**
         * This method is a helper to build the static NotificationProducer Notification System Producer instance, which is the 
         * default producer that all channels automatically have added to them when they are created.
         * @return NotificationProducer
         */
        private static final NotificationProducerBo buildNotificationSystemProducer() {
            NotificationProducerBo producer = new NotificationProducerBo();
            producer.setName(NOTIFICATION_SYSTEM_PRODUCER_NAME);
            producer.setDescription(NOTIFICATION_SYSTEM_PRODUCER_DESCRIPTION);
            producer.setContactInfo(NOTIFICATION_SYSTEM_PRODUCER_CONTACT_INFO);
            return producer;
        }

        private NOTIFICATION_PRODUCERS() {
            throw new UnsupportedOperationException("do not call");
        }
    }
}
