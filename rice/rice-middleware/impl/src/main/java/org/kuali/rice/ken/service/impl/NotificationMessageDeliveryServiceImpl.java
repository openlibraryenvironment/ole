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

import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.util.NotificationConstants;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//import org.kuali.rice.core.jpa.criteria.Criteria;

/**
 * NotificationService implementation - this is the default out-of-the-box implementation of the service that uses the 
 * businessObjectDao to get at the data via our OOTB DBMS.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationMessageDeliveryServiceImpl implements NotificationMessageDeliveryService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	.getLogger(NotificationMessageDeliveryServiceImpl.class);
    
    private GenericDao businessObjectDao;
    private NotificationMessegeDeliveryDao ntdDao;
    
    /**
     * Constructs a NotificationServiceImpl class instance.
     * @param businessObjectDao
     */
    public NotificationMessageDeliveryServiceImpl(GenericDao businessObjectDao, NotificationMessegeDeliveryDao ntdDao) {
        this.businessObjectDao = businessObjectDao;
        this.ntdDao = ntdDao;
    }

    /**
     * This is the default implementation that uses the businessObjectDao.
     * @param id
     * @return NotificationMessageDelivery
     */
    public NotificationMessageDelivery getNotificationMessageDelivery(Long id) {
        HashMap<String, Long> primaryKeys = new HashMap<String, Long>();
        primaryKeys.put(NotificationConstants.BO_PROPERTY_NAMES.ID, id);
	
        return (NotificationMessageDelivery) businessObjectDao.findByPrimaryKey(NotificationMessageDelivery.class, primaryKeys);
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationMessageDeliveryService#getNotificationMessageDeliveryByDelivererId(java.lang.String)
     */
    //switch to JPA criteria
    public NotificationMessageDelivery getNotificationMessageDeliveryByDelivererId(String id) {
    	Map<String, Object> c = new HashMap<String, Object>();
    	c.put(NotificationConstants.BO_PROPERTY_NAMES.DELIVERY_SYSTEM_ID, id);	
        Collection<NotificationMessageDelivery> results = businessObjectDao.findMatching(NotificationMessageDelivery.class, c);
        
        if (results == null || results.size() == 0) return null;
        if (results.size() > 1) {
            throw new RuntimeException("More than one message delivery found with the following delivery system id: " + id);
        }
        return results.iterator().next();
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationMessageDeliveryService#getNotificationMessageDeliveries()
     */
    public Collection<NotificationMessageDelivery> getNotificationMessageDeliveries() {
        return businessObjectDao.findAll(NotificationMessageDelivery.class);
    }
    
    /**
     * @see org.kuali.rice.ken.service.NotificationMessageDeliveryService#getNotificationMessageDeliveries(java.lang.Long, java.lang.String)
     */
    //switch to JPA criteria
    public Collection<NotificationMessageDelivery> getNotificationMessageDeliveries(NotificationBo notification, String userRecipientId) {

    	Map<String, Object> c = new HashMap<String, Object>();
    	c.put(NotificationConstants.BO_PROPERTY_NAMES.NOTIFICATION, notification.getId());
    	c.put(NotificationConstants.BO_PROPERTY_NAMES.USER_RECIPIENT_ID, userRecipientId);
    	
        return businessObjectDao.findMatching(NotificationMessageDelivery.class, c);
    }

    /**
     * This method is responsible for atomically finding all untaken, undelivered messagedeliveries, marking them as taken
     * and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).

     * @return a list of available message deliveries that have been marked as taken by the caller
     */
    //switch to JPA criteria
    public Collection<NotificationMessageDelivery> takeMessageDeliveriesForDispatch() {
        // DO WITHIN TRANSACTION: get all untaken messagedeliveries, and mark as "taken" so no other thread/job takes them
        // need to think about durability of work list

        // get all undelivered message deliveries
        Collection<NotificationMessageDelivery> messageDeliveries = ntdDao.getUndeliveredMessageDelivers(businessObjectDao);

        
        LOG.debug("Retrieved " + messageDeliveries.size() + " available message deliveries: " + System.currentTimeMillis());

        // mark messageDeliveries as taken
        for (NotificationMessageDelivery delivery: messageDeliveries) {
            delivery.setLockedDateValue(new Timestamp(System.currentTimeMillis()));
            businessObjectDao.save(delivery);
        }
        return messageDeliveries;
    }
    
    /**
     * This method is responsible for atomically finding all untaken message deliveries that are ready to be autoremoved,
     * marking them as taken and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).
     * @return a list of notifications to be autoremoved that have been marked as taken by the caller
     */
    public Collection<NotificationMessageDelivery> takeMessageDeliveriesForAutoRemoval() {
        // get all UNDELIVERED/DELIVERED notification notification message delivery records with associated notifications that have and autoRemovalDateTime <= current
    	Collection<NotificationMessageDelivery> messageDeliveries = ntdDao.getMessageDeliveriesForAutoRemoval(new Timestamp(System.currentTimeMillis()), businessObjectDao);
    	
        for (NotificationMessageDelivery d: messageDeliveries) {
            d.setLockedDateValue(new Timestamp(System.currentTimeMillis()));
            businessObjectDao.save(d);
        }
        
        return messageDeliveries;
    
    }

    /**
     * Unlocks the specified messageDelivery object
     * @param messageDelivery the message delivery to unlock
     */
    public void unlockMessageDelivery(NotificationMessageDelivery messageDelivery) {
    	Map<String, Long> c = new HashMap<String, Long>();
    	c.put(NotificationConstants.BO_PROPERTY_NAMES.DELIVERY_SYSTEM_ID, messageDelivery.getId());	

        Collection<NotificationMessageDelivery> deliveries = businessObjectDao.findMatching(NotificationMessageDelivery.class, c, true, RiceConstants.NO_WAIT);
        if (deliveries == null || deliveries.size() == 0) {
            throw new RuntimeException("NotificationMessageDelivery #" + messageDelivery.getId() + " not found to unlock");
        }

        NotificationMessageDelivery d = deliveries.iterator().next();
        d.setLockedDateValue(null);

        businessObjectDao.save(d);
    }
}
