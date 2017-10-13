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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.api.service.KENAPIService;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Required;

/**
 * KEN API service implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KENAPIServiceImpl implements KENAPIService {
    private NotificationChannelService channelService;
    private UserPreferenceService prefsService;

    /**
     * Sets the NotificationChannelService
     * @param ncs the NotificationChannelService
     */
    @Required
    public void setNotificationChannelService(NotificationChannelService ncs) {
        this.channelService = ncs;
    }

    /**
     * Sets the UserPreferenceService
     * @param ups the UserPreferenceService
     */
    @Required
    public void setUserPreferenceService(UserPreferenceService ups) {
        this.prefsService = ups;
    }

    /**
     * @see org.kuali.rice.ken.service.KENAPIService#getAllChannels()
     */
    @Override
    public Collection<String> getAllChannelNames() {
        Collection<NotificationChannelBo> chans = channelService.getAllNotificationChannels();
        Collection<String> chanNames = new ArrayList<String>(chans.size());
        for (NotificationChannelBo c: chans) {
            chanNames.add(c.getName());
        }
        return chanNames;
    }

    /**
     * @see org.kuali.rice.ken.service.KENAPIService#getDeliverersForRecipientAndChannel(java.lang.String, java.lang.String)
     */
    @Override
    public Collection<String> getDeliverersForRecipientAndChannel(String recipient, String channel) {
        if (StringUtils.isBlank(recipient)) {
            throw new RiceIllegalArgumentException("recipient is null or blank");
        }

        if (StringUtils.isBlank(channel)) {
            throw new RiceIllegalArgumentException("channel is null or blank");
        }

        /*NotificationChannel nc = channelService.getNotificationChannelByName(channel);
        if (nc == null) {
            throw new RiceIllegalArgumentException("Invalid channel: '" + channel + "'");
        }
        Collection<UserDelivererConfig> configs = prefsService.getMessageDelivererConfigurationsForUserAndChannel(recipient, nc);
        Collection<String> deliverers = new ArrayList<String>(configs.size());
        for (UserDelivererConfig cfg: configs) {
            deliverers.add(cfg.getDelivererName());
        }
        return deliverers;*/
        return null;
    }

    /**
     * @see org.kuali.rice.ken.service.KENAPIService#getRecipientPreference(java.lang.String, java.lang.String)
     */
    @Override
    public String getRecipientPreference(String recipient, String prefKey) {
        if (StringUtils.isBlank(recipient)) {
            throw new RiceIllegalArgumentException("recipient is null or blank");
        }

        if (StringUtils.isBlank(prefKey)) {
            throw new RiceIllegalArgumentException("prefKey is null or blank");
        }

        /*RecipientPreference rp = prefsService.getUserRecipientPreferences(recipient, prefKey);
        if (rp == null) return null;
        return rp.getValue();
        */
        return null;
    }
}
