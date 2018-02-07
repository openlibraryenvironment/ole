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
package org.kuali.rice.ken.dao.impl;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.dao.NotificationDao;
import org.kuali.rice.ken.util.NotificationConstants;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NotificationDaoJpa implements NotificationDao{

	private static final Logger LOG = Logger.getLogger(NotificationDaoJpa.class);
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ken.dao.NotificationDao#findMatchedNotifications(java.sql.Timestamp, org.kuali.rice.core.framework.persistence.dao.GenericDao)
	 */
	@Override
	public Collection findMatchedNotificationsForResolution(Timestamp tm, GenericDao dao) {

		//LOG.info("************************calling OJBNotificationDao.findMatchedNotificationsForResolution(************************ ");

		Criteria criteria = new Criteria(NotificationBo.class.getName());
		criteria.eq(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
		criteria.lte(NotificationConstants.BO_PROPERTY_NAMES.SEND_DATE_TIME, new Timestamp(System.currentTimeMillis()));
		criteria.isNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);

		Collection<NotificationBo> available_notifications = dao.findMatching(NotificationBo.class, criteria, true, RiceConstants.NO_WAIT);

		return available_notifications;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ken.dao.NotificationDao#findMatchedNotificationsForUnlock(java.sql.Timestamp, org.kuali.rice.core.framework.persistence.dao.GenericDao)
	 */
	@Override
	public Collection findMatchedNotificationsForUnlock(NotificationBo not, GenericDao dao) {

		//LOG.info("************************calling OJBNotificationDao.findMatchedNotificationsForForUnlock************************ ");

		Criteria criteria = new Criteria(NotificationBo.class.getName());
		criteria.eq(NotificationConstants.BO_PROPERTY_NAMES.ID, not.getId());

		Collection<NotificationBo> notifications = dao.findMatching(NotificationBo.class, criteria, true, RiceConstants.NO_WAIT);

		return notifications;
	}

}

