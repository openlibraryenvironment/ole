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
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.service.NotificationAuthorizationService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.List;


/**
 * NotificationAuthorizationService implementation - this is the default out-of-the-box implementation of the service.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationAuthorizationServiceImpl implements NotificationAuthorizationService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NotificationAuthorizationServiceImpl.class);

    private GenericDao businessObjectDao;

    /**
     * Constructs a NotificationAuthorizationServiceImpl class instance.
     * @param businessObjectDao
     */
    public NotificationAuthorizationServiceImpl(GenericDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationAuthorizationService#isProducerAuthorizedToSendNotificationForChannel(org.kuali.rice.ken.bo.NotificationProducerBo, org.kuali.rice.ken.bo.NotificationChannelBo)
     */
    public boolean isProducerAuthorizedToSendNotificationForChannel(NotificationProducerBo producer, NotificationChannelBo channel) {
        List<Long> channelIds = producer.getChannelIds();

        if(channelIds.contains(channel.getId())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Implements by calling the is user member of service in KEW's workgroup service, looking for a specific membership
     * in the "NotificationAdmin" workgroup.
     * @see org.kuali.rice.ken.service.NotificationAuthorizationService#isUserAdministrator(java.lang.String)
     */
    public boolean isUserAdministrator(String userId) {
    	String groupNameId = NotificationConstants.KEW_CONSTANTS.NOTIFICATION_ADMIN_GROUP_NAME;
	    Person user = KimApiServiceLocator.getPersonService().getPerson(userId);
	    if (user == null) {
	        return false;
	    }

        final GroupService groupService = KimApiServiceLocator.getGroupService();
        Group group = groupService.getGroupByNamespaceCodeAndName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE,
                groupNameId);
		return group == null ? false : groupService.isMemberOfGroup(user.getPrincipalId(), group.getId());
    }
}
