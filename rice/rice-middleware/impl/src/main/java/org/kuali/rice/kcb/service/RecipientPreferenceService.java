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
import java.util.HashMap;

import org.kuali.rice.kcb.bo.RecipientDelivererConfig;
import org.kuali.rice.kcb.bo.RecipientPreference;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.exception.ErrorList;

/**
 * Service for accessing user preferences in the KEN system.{@link UserPreference}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RecipientPreferenceService {
    /**
     * This method will get all  user recipient preferences from the system.
     * @param recipientId
     */
    public HashMap<String, String> getRecipientPreferences(String recipientId);

    /**
     * This method will save a user recipient preferences in the system.
     * @param userid
     * @param prefs a hashmap of key/values
     * @param deliveryTypeName name of deliverer
     */
    public void saveRecipientPreferences(String userid, HashMap<String, String> prefs, MessageDeliverer deliverer) throws ErrorList;
    
    /**
     * This method will get a specific user recipient preferences from the system.
     * @param recipientId
     * @param key
     */
    public RecipientPreference getRecipientPreference(String recipientId, String key);

    /**
     * This method will save a specific user recipient preferences in the system.
     * @param pref the preferences
     */
    public void saveRecipientPreference(RecipientPreference pref);
    
    /**
     * This method will delete a specific user recipient preferences from the system.
     * @param pref the preferences
     */
    public void deleteRecipientPreference(RecipientPreference pref);

    // deliverer configuration
    
    /**
     * This method will remove all user deliverer configuration preferences in the system.
     * @param recipientId the recipient id
     */
    public void removeRecipientDelivererConfigs(String recipientId);
    
    /**
     * This method will save a user deliverer configuration preferences in the system.
     * @param recipientId the recipient id
     * @param delivererName the deliverer name
     * @param channels the channels for which to enable the deliverer
     */
    public void saveRecipientDelivererConfig(String recipientId, String delivererName, String[] channels);

    /**
     * This method will retrieve all of the message deliverer configurations for a given user, associated with a 
     * particular channel.
     * @param recipientId
     * @param channel
     */
    public Collection<RecipientDelivererConfig> getDeliverersForRecipientAndChannel(String recipientId, String channel);
    
    /**
     * This method will retrieve all of the message deliverer configurations for a given user 
     * @param recipientId
     */
    public Collection<RecipientDelivererConfig> getDeliverersForRecipient(String recipientId);
}
