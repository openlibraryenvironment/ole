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

import org.kuali.rice.ken.bo.UserChannelSubscriptionBo;

/**
 * Service for accessing user preferences in the KEN system.{@link UserPreference}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface UserPreferenceService {
    /**
     * This method retrieves all of the current channel subscriptions for a user.
     * @param userid
     * @return Collection
     */
    public Collection<UserChannelSubscriptionBo> getCurrentSubscriptions(String userid);
    
    /**
     * This method retrieves the UserChannelSubscription instance given the two unique keys that are 
     * passed in.
     * @param channelid
     * @param userid
     * @return UserChannelSubscription
     */
    public UserChannelSubscriptionBo getSubscription(String channelid, String userid);

    /**
     * This method will add a channel subscription into the system.
     * @param userChannelSubscription
     */
    public void subscribeToChannel(UserChannelSubscriptionBo userChannelSubscription);

    /**
     * This method will remove a channel subscription from the system.
     * @param userChannelSubscription
     */
    public void unsubscribeFromChannel(UserChannelSubscriptionBo userChannelSubscription);
}
