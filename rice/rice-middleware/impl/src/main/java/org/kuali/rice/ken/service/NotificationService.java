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
package org.kuali.rice.ken.service;

import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationResponseBo;

import java.io.IOException;
import java.util.Collection;

/**
 * The NotificationService class is responsible for processing notification messages 
 * that come into the system.  It also is able to retrieve notifications that have 
 * already been entered/processed by the system.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationService {
    /**
     * This method allows consumers to send notification messages.  This particular service 
     * accepts the XML format of the notification and then marshals it out into the actual 
     * business object construct, for further processing.  The response is also sent back as 
     * a String of XML.
     * @param notificationMessageAsXml
     * @return NotificationResponse response object
     */
    public NotificationResponseBo sendNotification(String notificationMessageAsXml) throws IOException, XmlException;
    
    /**
     * This method allows consumers to send notification messages.  This particular service 
     * accepts the actual business object.
     * @param notification
     * @return NotificationResponse
     */
    public NotificationResponseBo sendNotification(NotificationBo notification);
    
    /**
     * This method will retrieve a Notification object from the system, given the id of the 
     * actual notification record.
     * @param id
     * @return Notification
     */
    public NotificationBo getNotification(Long id);
    
    /**
     * This method will retrieve a collection of notifications given a contentTypeName and recipientId.
     * @param contentTypeName the name of the content type
     * @param recipientId the recipient id
     * @return Collection of notifications
     */
    public Collection<NotificationBo> getNotificationsForRecipientByType(String contentTypeName, String recipientId);
    
    /**
     * This method will "dismiss"/remove a NotificationMessageDelivery with the specified cause.
     * @param id the id of the NotificationMessageDelivery that was acted upon
     * @param user the user or entity that performed the dismissal
     * @param cause the cause of the dismissal (e.g. an end user action or auto-removal by the system)
     */
    public void dismissNotificationMessageDelivery(Long id, String user, String cause);
    
    
    /**
     * This method is responsible for atomically finding all untaken, unresolved notifications that are ready to be sent,
     * marking them as taken and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).
     * @return a list of notifications to be resolved that have been marked as taken by the caller
     */
    public Collection<NotificationBo> takeNotificationsForResolution();
    
    /**
     * Unlocks specified notification
     * @param notification the notification object to unlock
     */
    public void unlockNotification(NotificationBo notification);
}
