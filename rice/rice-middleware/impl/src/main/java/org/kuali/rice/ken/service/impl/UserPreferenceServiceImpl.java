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

import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.UserChannelSubscriptionBo;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.UserPreferenceService;
import org.kuali.rice.ken.util.NotificationConstants;

import java.util.Collection;
import java.util.HashMap;

/**
 * UserPreferenceService implementation - uses the businessObjectDao to get at data in the underlying database.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UserPreferenceServiceImpl implements UserPreferenceService {
    private GenericDao businessObjectDao;
    private NotificationChannelService notificationChannelService;

    private static final Logger LOG = Logger.getLogger(UserPreferenceServiceImpl.class);

    /**
     * Constructs a UserPreferenceServiceImpl 
     * @param businessObjectDao
     * @param notificationChannelService
     */
    public UserPreferenceServiceImpl(GenericDao businessObjectDao, NotificationChannelService notificationChannelService) {
        this.businessObjectDao = businessObjectDao;
        this.notificationChannelService = notificationChannelService;
    }

    /**
     * @see org.kuali.rice.ken.service.UserPreferenceService#getCurrentSubscriptions(java.lang.String)
     */
    public Collection<UserChannelSubscriptionBo> getCurrentSubscriptions(String userid) {
        UserChannelSubscriptionBo userChannelSubscription = new UserChannelSubscriptionBo();
        userChannelSubscription.setUserId(userid);

        return businessObjectDao.findMatchingByExample(userChannelSubscription);
    }

    /**
     * @see org.kuali.rice.ken.service.UserPreferenceService#getSubscription(java.lang.String, java.lang.String)
     */
    public UserChannelSubscriptionBo getSubscription(String channelid, String userid) {
        HashMap<String, String> uniqueKeys = new HashMap<String,String>();

        uniqueKeys.put(NotificationConstants.BO_PROPERTY_NAMES.CHANNEL_ID, channelid);
        uniqueKeys.put(NotificationConstants.BO_PROPERTY_NAMES.USER_ID, userid);

        UserChannelSubscriptionBo
                subscription = (UserChannelSubscriptionBo) businessObjectDao.findByUniqueKey(UserChannelSubscriptionBo.class, uniqueKeys);

        return subscription; 
    }

    /**
     * @see org.kuali.rice.ken.service.UserPreferenceService#subscribeToChannel(org.kuali.rice.ken.bo.UserChannelSubscriptionBo)
     */
    public void subscribeToChannel(UserChannelSubscriptionBo userChannelSubscription) {
        LOG.info("Saving channel subscription");
        try {
            businessObjectDao.save(userChannelSubscription);
        } catch(Exception e) {
            LOG.error("Exception when saving userChannelSubscription");		    
        }
        LOG.debug("Channel subscription saved");
    }

    /**
     * @see org.kuali.rice.ken.service.UserPreferenceService#unsubscribeFromChannel(org.kuali.rice.ken.bo.UserChannelSubscriptionBo)
     */
    public void unsubscribeFromChannel(UserChannelSubscriptionBo userChannelSubscription) {
        LOG.info("unsubscribing from channel"); 
        try {
            businessObjectDao.delete(userChannelSubscription);
        } catch(Exception e) {
            LOG.error("Exception when deleting userChannelSubscription");		    
        }

    }
}
