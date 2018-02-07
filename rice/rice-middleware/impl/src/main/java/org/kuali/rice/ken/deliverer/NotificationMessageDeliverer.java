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
package org.kuali.rice.ken.deliverer;

import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.exception.NotificationAutoRemoveException;
import org.kuali.rice.ken.exception.NotificationMessageDeliveryException;
import org.kuali.rice.ken.exception.NotificationMessageDismissalException;

/**
 * This class represents the different types of Notification Delivery Types that the system can handle.  
 * For example, an instance of delivery type could be "ActionList" or "Email" or "SMS".  Any deliverer implementation 
 * adhering to this interface can be plugged into the system and will be automatically available for use.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationMessageDeliverer {
    /**
     * This method is responsible for delivering the passed in messageDelivery record.
     * @param messageDelivery The messageDelivery to process
     * @throws NotificationMessageDeliveryException
     */
    public void deliverMessage(NotificationMessageDelivery messageDelivery) throws NotificationMessageDeliveryException;
    
    /**
     * This method handles auto removing a message delivery from a person's list of notifications.
     * @param messageDelivery The messageDelivery to auto remove
     * @throws NotificationAutoRemoveException
     */
    public void autoRemoveMessageDelivery(NotificationMessageDelivery messageDelivery) throws NotificationAutoRemoveException;
    
    /**
     * This method dismisses/removes the NotificationMessageDelivery so that it is no longer being presented to the user
     * via this deliverer.  Note, whether this action is meaningful is dependent on the deliverer implementation.  If the
     * deliverer cannot control the presentation of the message, then this method need not do anything. 
     * @param messageDelivery the messageDelivery to dismiss
     * @param the user that caused the dismissal; in the case of end-user actions, this will most likely be the user to
     *        which the message was delivered (user recipient in the NotificationMessageDelivery object)
     * @param cause the reason the message was dismissed
     */
    public void dismissMessageDelivery(NotificationMessageDelivery messageDelivery, String user, String cause) throws NotificationMessageDismissalException;
}
