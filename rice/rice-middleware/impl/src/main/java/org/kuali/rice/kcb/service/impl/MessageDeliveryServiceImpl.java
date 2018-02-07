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
package org.kuali.rice.kcb.service.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;
import org.kuali.rice.kcb.service.MessageDeliveryService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * MessageDeliveryService implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageDeliveryServiceImpl extends BusinessObjectServiceImpl implements MessageDeliveryService {
    private static final Logger LOG = Logger.getLogger(MessageDeliveryServiceImpl.class);

    /**
     * Number of processing attempts to make.  {@link MessageDelivery}s with this number or more of attempts
     * will not be selected for further processing.
     */
    private int maxProcessAttempts;

    /**
     * Sets the max processing attempts
     * @param maxProcessAttempts the max delivery attempts
     */
    public void setMaxProcessAttempts(int maxProcessAttempts) {
        this.maxProcessAttempts = maxProcessAttempts;
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#saveMessageDelivery(org.kuali.rice.kcb.bo.MessageDelivery)
     */
    public void saveMessageDelivery(MessageDelivery delivery) {
        dao.save(delivery);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#deleteMessageDelivery(java.lang.Long)
     */
    public void deleteMessageDelivery(MessageDelivery messageDelivery) {
        dao.delete(messageDelivery);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#getMessageDeliveries()
     */
    public Collection<MessageDelivery> getAllMessageDeliveries() {
        return dao.findAll(MessageDelivery.class);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#getMessageDelivery(java.lang.Long)
     */
    public MessageDelivery getMessageDelivery(Long id) {
        Map<String, Object> fields = new HashMap<String, Object>(1);
        fields.put(MessageDelivery.ID_FIELD, id);
        return (MessageDelivery) dao.findByPrimaryKey(MessageDelivery.class, fields);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#getMessageDeliveryByDelivererSystemId(java.lang.Long)
     */
    public MessageDelivery getMessageDeliveryByDelivererSystemId(Long id) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(MessageDelivery.SYSTEMID_FIELD, id);
        Collection<MessageDelivery> results = dao.findMatching(MessageDelivery.class, criteria);
        if (results == null || results.size() == 0) return null;
        if (results.size() > 1) {
            throw new RuntimeException("More than one message delivery found with the following delivery system id: " + id);
        }
        return results.iterator().next();
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDeliveryService#getMessageDeliveries(org.kuali.rice.kcb.bo.Message)
     */
    public Collection<MessageDelivery> getMessageDeliveries(Message message) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(MessageDelivery.MESSAGEID_FIELD, message.getId());
        return dao.findMatching(MessageDelivery.class, criteria);
    }

    /* This method is responsible for atomically finding messagedeliveries, marking them as taken
     * and returning them to the caller for processing.
     * NOTE: it is important that this method execute in a SEPARATE dedicated transaction; either the caller should
     * NOT be wrapped by Spring declarative transaction and this service should be wrapped (which is the case), or
     * the caller should arrange to invoke this from within a newly created transaction).
     */
    public Collection<MessageDelivery> lockAndTakeMessageDeliveries(MessageDeliveryStatus[] statuses) {
        return lockAndTakeMessageDeliveries(null, statuses);
    }
    public Collection<MessageDelivery> lockAndTakeMessageDeliveries(Long messageId, MessageDeliveryStatus[] statuses) {
        LOG.debug("========>> ENTERING LockAndTakeMessageDeliveries: " + Thread.currentThread());
        // DO WITHIN TRANSACTION: get all untaken messagedeliveries, and mark as "taken" so no other thread/job takes them
        // need to think about durability of work list

        // get all undelivered message deliveries
        Criteria criteria = new Criteria();
        criteria.addIsNull(MessageDelivery.LOCKED_DATE);
        if (messageId != null) {
            criteria.addEqualTo(MessageDelivery.MESSAGEID_FIELD, messageId);
        }
        criteria.addLessThan(MessageDelivery.PROCESS_COUNT, maxProcessAttempts);
        Collection<String> statusCollection = new ArrayList<String>(statuses.length);
        for (MessageDeliveryStatus status: statuses) {
            statusCollection.add(status.name());
        }
        criteria.addIn(MessageDelivery.DELIVERY_STATUS, statusCollection);
        // implement our select for update hack
        Collection<MessageDelivery> messageDeliveries = dao.findMatching(MessageDelivery.class, criteria, true, RiceConstants.NO_WAIT);

        //LOG.debug("Retrieved " + messageDeliveries.size() + " available message deliveries: " + System.currentTimeMillis());

        // mark messageDeliveries as taken
        for (MessageDelivery delivery: messageDeliveries) {
            LOG.debug("Took: " + delivery);
            delivery.setLockedDate(new Timestamp(System.currentTimeMillis()));
            dao.save(delivery);
        }

        LOG.debug("<<=======  LEAVING LockAndTakeMessageDeliveries: " + Thread.currentThread());
        return messageDeliveries;
    }
}
