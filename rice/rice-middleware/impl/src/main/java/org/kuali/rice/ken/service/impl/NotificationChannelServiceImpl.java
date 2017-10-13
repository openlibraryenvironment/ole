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

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.service.NotificationChannelService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * NotificationChannelService implementation - uses the businessObjectDao to get at data in the underlying database.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationChannelServiceImpl implements NotificationChannelService {
    private GenericDao businessObjectDao;

    /**
     * Constructs a NotificationChannelServiceImpl.java.
     * @param businessObjectDao
     */
    public NotificationChannelServiceImpl(GenericDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationChannelService#getNotificationChannel(java.lang.String)
     */
    public NotificationChannelBo getNotificationChannel(String id) {
        Map<String,  String> primaryKeys = new HashMap<String, String>();
        primaryKeys.put("id", id);
        return (NotificationChannelBo) businessObjectDao.findByPrimaryKey(NotificationChannelBo.class, primaryKeys);
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationChannelService#getNotificationChannelByName(java.lang.String)
     */
    public NotificationChannelBo getNotificationChannelByName(String name) {
        Map<String,  String> fields = new HashMap<String, String>();
        fields.put("name", name);
        Collection<NotificationChannelBo> found = businessObjectDao.findMatching(NotificationChannelBo.class, fields);
        assert(found.size() <= 1);
        if (found.size() == 0) return null;
        return found.iterator().next();
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationChannelService#getSubscribableChannels()
     */
    public Collection getSubscribableChannels() {
        Map<String, Boolean> fieldValues = new HashMap<String, Boolean>();
        String sortField = new String("name");
        fieldValues.put("subscribable", true);
        return businessObjectDao.findMatchingOrderBy(NotificationChannelBo.class, fieldValues, sortField, true);
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationChannelService#getAllNotificationChannels()
     */
    public Collection getAllNotificationChannels() {
        String sortField = new String("name");
        return businessObjectDao.findAllOrderBy(NotificationChannelBo.class, sortField, true);
    }
}
