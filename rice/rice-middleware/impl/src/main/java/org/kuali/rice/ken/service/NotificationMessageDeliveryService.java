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

import java.util.Collection;

import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;

/**
 * The NotificationMessageDeliveryService class is responsible various functions regarding the 
 * NotificationMessageDelivery records that exist within the system.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationMessageDeliveryService {
    /**
     * This method will retrieve a NotificationMessageDelivery object from the system, given the id of the 
     * actual record.
     * @param id
     * @return NotificationMessageDelivery
     */
    public NotificationMessageDelivery getNotificationMessageDelivery(Long id);

    /**
     * This method will retrieve a NotificationMessageDelivery object from the system, given the external deliverer system id
     * registered with the NotificationMessageDelivery.
     * @param id the external deliverer system id
     * @return NotificationMessageDelivery
     */
    public NotificationMessageDelivery getNotificationMessageDeliveryByDelivererId(String id);

    /**
     * This method will return all NotificationMessageDelivery objects in the system 
     * actual record.
     * @return List<NotificationMessageDelivery> list of NotificationMessageDelivery objects in the system
     */
    public Collection<NotificationMessageDelivery> getNotificationMessageDeliveries();
    
    /**
     * This method will return all NotificationMessageDelievery objects generated for the given Notification for the given user
     * @param notification the notification which generated the message deliveries
     * @param userRecipientId the id of the user whose message deliveries to obtain
     * @return collection of NotificationMessageDelivery objects generated for the given Notification for the given user
     */
    public Collection<NotificationMessageDelivery> getNotificationMessageDeliveries(NotificationBo notification, String userRecipientId);
    
    /**
     * This method is responsible for atomically finding all untaken, undelivered message deliveries, marking them as taken
     * and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).
     * @return a list of available message deliveries that have been marked as taken by the caller
     */
    public Collection<NotificationMessageDelivery> takeMessageDeliveriesForDispatch();
    
    /**
     * This method is responsible for atomically finding all untaken message deliveries that are ready to be autoremoved,
     * marking them as taken and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).
     * @return a list of notifications to be autoremoved that have been marked as taken by the caller
     */
    public Collection<NotificationMessageDelivery> takeMessageDeliveriesForAutoRemoval();
    
    /**
     * Unlocks the specified messageDelivery object
     * @param messageDelivery the message delivery to unlock
     */
    public void unlockMessageDelivery(NotificationMessageDelivery messageDelivery);
}
