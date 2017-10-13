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
package org.kuali.rice.kcb.deliverer;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.exception.ErrorList;
import org.kuali.rice.kcb.api.exception.MessageDeliveryException;
import org.kuali.rice.kcb.api.exception.MessageDismissalException;


/**
 * This class represents the different types of Notification Delivery Types that the system can handle.  
 * For example, an instance of delivery type could be "ActionList" or "Email" or "SMS".  Any deliverer implementation 
 * adhering to this interface can be plugged into the system and will be automatically available for use.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageDeliverer {
    /**
     * This method is responsible for returning a list of preference key names along with their corresponding labels that get 
     * rendered in the UI.  For example, if you were creating a NotificationEmailDeliverer class, one of the key preferences 
     * that this method would return back would be "email_address" and the label would be "Email Address".
     * @return LinkedHashMap
     */
    public LinkedHashMap<String, String> getPreferenceKeys();
    
    /**
     * This method is responsible for validating preference values when a person saves their preferences for the particular
     * NotificationMessageDeliverer.  For example, if "phoneNumber" is one of the preferences for an SMS deliverer, 
     * then this method would be responsible for validating the value entered by a particular user such that it was properly 
     * constructed with hyphens or not, etc.  Errors would be constructed and added to the ErrorList instance and be thrown from 
     * the method if any occurred.
     * @throws ErrorList
     */
    public void validatePreferenceValues(HashMap<String, String> prefs) throws ErrorList;
    
    /**
     * This method returns the human readable name of the plugin.  This name is the 
     * key for this message delivery type. It must be unique and not contain
     * any spaces.
     * @return String
     */
    public String getName();
    
    /**
     * This method returns the human readable Title of the plugin.  This name is the 
     * string used for identifying the plugin in the UI. It may contain
     * spaces characters.
     * @return String
     */
    public String getTitle();
    
    /**
     * This method returns the human readable description for this plugin.
     * @return String
     */
    public String getDescription();
    
    /**
     * This method is responsible for delivering the passed in messageDelivery record.
     * @param messageDelivery The messageDelivery to process
     * @throws MessageDeliveryException
     */
    public void deliver(MessageDelivery messageDelivery) throws MessageDeliveryException;
    
    /**
     * This method handles auto removing a message delivery from a person's list of notifications.
     * @param messageDelivery The messageDelivery to auto remove
     * @throws NotificationAutoRemoveException
     */
    //public void autoRemoveMessageDelivery(MessageDelivery messageDelivery) /*throws NotificationAutoRemoveException*/;
    
    /**
     * This method dismisses/removes the NotificationMessageDelivery so that it is no longer being presented to the user
     * via this deliverer.  Note, whether this action is meaningful is dependent on the deliverer implementation.  If the
     * deliverer cannot control the presentation of the message, then this method need not do anything. 
     * @param messageDelivery the messageDelivery to dismiss
     * @param the user that caused the dismissal; in the case of end-user actions, this will most likely be the user to
     *        which the message was delivered (user recipient in the NotificationMessageDelivery object)
     * @param cause the reason the message was dismissed
     */
    public void dismiss(MessageDelivery messageDelivery, String user, String cause) throws MessageDismissalException;
}
