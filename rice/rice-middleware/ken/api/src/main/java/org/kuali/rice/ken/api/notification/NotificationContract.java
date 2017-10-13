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
package org.kuali.rice.ken.api.notification;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.ken.api.common.KenIdentifiable;

import java.util.List;

public interface NotificationContract extends KenIdentifiable, Versioned, GloballyUnique {

    /**
     * This is the namespace code for the Group.
     *
     * <p>
     * This is a namespace code assigned to a Group.  Together with name, it makes up another unique identifier for Group
     * </p>
     *
     * @return namespaceCode
     */
    DateTime getCreationDateTime();

    List<? extends NotificationRecipientContract> getRecipients();

    List<? extends NotificationSenderContract> getSenders();

    DateTime getAutoRemoveDateTime();

    NotificationChannelContract getChannel();

    String getContent();

    NotificationContentTypeContract getContentType();

    String getDeliveryType();

    NotificationPriorityContract getPriority();

    NotificationProducerContract getProducer();
    
    DateTime getSendDateTime();

    String getProcessingFlag();

    DateTime getLockedDate();

    String getTitle();

    String getContentMessage();

    /**
     * Gets the custom document type name
     *
     * <p>
     * If null, the system will use the default {@code KualiNotification} document type when routing the notification
     * </p>
     *
     * @return the custom document type name for this Notification, or null if undefined
     * @since 2.3.1
     */
    String getDocTypeName();
}
