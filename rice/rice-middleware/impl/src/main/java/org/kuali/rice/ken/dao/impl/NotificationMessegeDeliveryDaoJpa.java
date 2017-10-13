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
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao;
import org.kuali.rice.ken.util.NotificationConstants;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NotificationMessegeDeliveryDaoJpa implements NotificationMessegeDeliveryDao{

	private static final Logger LOG = Logger.getLogger(NotificationDaoJpa.class);

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao#getUndeliveredMessageDelivers()
	 */
	@Override
	public Collection getUndeliveredMessageDelivers(GenericDao businessObjectDao) {

		//LOG.info("************************calling OJBNotificationMessegeDeliveryDao.getUndeliveredMessageDelivers************************ ");

		Criteria criteria = new Criteria(NotificationMessageDelivery.class.getName());
		criteria.eq(NotificationConstants.BO_PROPERTY_NAMES.MESSAGE_DELIVERY_STATUS, NotificationConstants.MESSAGE_DELIVERY_STATUS.UNDELIVERED);
		criteria.isNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);
		Collection<NotificationMessageDelivery> messageDeliveries = businessObjectDao.findMatching(NotificationMessageDelivery.class, criteria, true, RiceConstants.NO_WAIT);

		return messageDeliveries;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao#getMessageDeliveriesForAutoRemoval(org.kuali.rice.core.framework.persistence.dao.GenericDao)
	 */
	@Override
	public Collection<NotificationMessageDelivery> getMessageDeliveriesForAutoRemoval(Timestamp tm, GenericDao businessObjectDao) {

		//LOG.info("************************calling OJBNotificationMessegeDeliveryDao.getMessageDeliveriesForAutoRemoval************************ ");

		// get all UNDELIVERED/DELIVERED notification notification message delivery records with associated notifications that have and autoRemovalDateTime <= current

		Criteria criteria_STATUS = new Criteria(NotificationMessageDelivery.class.getName());
		criteria_STATUS.eq(NotificationConstants.BO_PROPERTY_NAMES.MESSAGE_DELIVERY_STATUS, NotificationConstants.MESSAGE_DELIVERY_STATUS.DELIVERED);

		Criteria criteria_UNDELIVERED = new Criteria(NotificationMessageDelivery.class.getName());
		criteria_UNDELIVERED.eq(NotificationConstants.BO_PROPERTY_NAMES.MESSAGE_DELIVERY_STATUS, NotificationConstants.MESSAGE_DELIVERY_STATUS.UNDELIVERED);

		// now OR the above two together
		criteria_STATUS.or(criteria_UNDELIVERED);

		Criteria fullQueryCriteria = new Criteria(NotificationMessageDelivery.class.getName());
		fullQueryCriteria.isNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);

		fullQueryCriteria.lte(NotificationConstants.BO_PROPERTY_NAMES.NOTIFICATION_AUTO_REMOVE_DATE_TIME, new Timestamp(System.currentTimeMillis()));

		fullQueryCriteria.and(criteria_STATUS);

		Collection<NotificationMessageDelivery> messageDeliveries = businessObjectDao.findMatching(NotificationMessageDelivery.class, fullQueryCriteria, true, RiceConstants.NO_WAIT);

		return messageDeliveries;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao#getLockedDeliveries(java.lang.Class, org.kuali.rice.core.framework.persistence.dao.GenericDao)
	 */
	@Override
	public Collection<NotificationMessageDelivery> getLockedDeliveries(
			Class clazz, GenericDao dao) {
    	Criteria criteria = new Criteria(clazz.getName());
    	criteria.notNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE);
        Collection<NotificationMessageDelivery> lockedDeliveries = dao.findMatching(clazz, criteria);
		return null;
	}


}
