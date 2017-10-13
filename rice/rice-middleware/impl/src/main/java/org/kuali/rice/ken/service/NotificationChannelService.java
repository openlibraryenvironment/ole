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

import org.kuali.rice.ken.bo.NotificationChannelBo;

/**
 * Service for accessing {@link org.kuali.rice.ken.bo.NotificationChannelBo}s
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationChannelService {
    /**
     * This method returns all of the registered notification channels in the system.
     * @return Collection
     */
    public Collection<NotificationChannelBo> getAllNotificationChannels();
    
    /**
     * This method retrieves a specific NotificationChannel instance by id.  If none is found, it returns null.
     * @param id
     * @return NotificationChannel
     */
    public NotificationChannelBo getNotificationChannel(String id);
    
    /**
     * This method retrieves a specific NotificationChannel instance by name.  If none is found, it returns null.
     * @param id
     * @return NotificationChannel
     */
    public NotificationChannelBo getNotificationChannelByName(String name);
    
    /**
     * This method retrieves all channels in the system that can be subscribed to.
     * @return Collection
     */
    public Collection<NotificationChannelBo> getSubscribableChannels();
}
