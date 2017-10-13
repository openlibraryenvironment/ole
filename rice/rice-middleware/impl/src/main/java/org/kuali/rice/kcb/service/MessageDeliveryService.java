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
package org.kuali.rice.kcb.service;

import java.util.Collection;

import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;

/**
 * The MessageDeliveryService class is responsible various functions regarding the 
 * MessageDelivery records that exist within the system.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageDeliveryService {
    /**
     * Saves a MessageDelivery
     * @param delivery the MessageDelivery to save
     */
    public void saveMessageDelivery(MessageDelivery delivery);

    /**
     * Deletes a MessageDelivery
     * @param delivery the MessageDelivery to delete 
     */
    public void deleteMessageDelivery(MessageDelivery delivery);

    /**
     * This method will retrieve a MessageDelivery object from the system, given the id of the 
     * actual record.
     * @param id
     * @return MessageDelivery
     */
    public MessageDelivery getMessageDelivery(Long id);

    /**
     * This method will retrieve a MessageDelivery object from the system, given the external deliverer system id
     * registered with the MessageDelivery.
     * @param id the external deliverer system id
     * @return MessageDelivery
     */
    public MessageDelivery getMessageDeliveryByDelivererSystemId(Long id);

    /**
     * This method will return all MessageDelivery objects in the system 
     * @return Collection<MessageDelivery> list of MessageDelivery objects in the system
     */
    public Collection<MessageDelivery> getAllMessageDeliveries();
    
    /**
     * This method will return all MessageDelievery objects generated for the given Message
     * @param message the message which generated the message deliveries
     * @return collection of NotificationMessageDelivery objects generated for the given Notification for the given user
     */
    public Collection<MessageDelivery> getMessageDeliveries(Message message);
    
    /**
     * Locks and takes all message deliveries of a given message in the system with any of the specified statuses
     * @param messageId the id of the message whose deliveries to take 
     * @param status the statuses of message deliveries to take
     * @return a collection of message deliveries
     */
    public Collection<MessageDelivery> lockAndTakeMessageDeliveries(Long messageId, MessageDeliveryStatus[] status);

    /**
     * Locks and takes all message deliveries in the system with any of the specified statuses
     * @param status the statuses of message deliveries to take
     * @return a collection of message deliveries
     */
    public Collection<MessageDelivery> lockAndTakeMessageDeliveries(MessageDeliveryStatus[] status);
}
