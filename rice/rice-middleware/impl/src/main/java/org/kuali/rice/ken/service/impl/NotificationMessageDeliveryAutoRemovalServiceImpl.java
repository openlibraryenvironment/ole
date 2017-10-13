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
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.deliverer.NotificationMessageDeliverer;
import org.kuali.rice.ken.deliverer.impl.KEWActionListMessageDeliverer;
import org.kuali.rice.ken.exception.NotificationAutoRemoveException;
import org.kuali.rice.ken.service.NotificationMessageDeliveryAutoRemovalService;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.ProcessingResult;
import org.kuali.rice.ken.util.NotificationConstants;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Auto removes expired message deliveries.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationMessageDeliveryAutoRemovalServiceImpl extends ConcurrentJob<NotificationMessageDelivery> implements NotificationMessageDeliveryAutoRemovalService {
    private GenericDao businessObjectDao;
    private NotificationMessageDeliveryService messageDeliveryService;

    /**
     * Constructs a NotificationMessageDeliveryDispatchServiceImpl instance.
     * @param businessObjectDao
     * @param txManager
     * @param executor
     * @param messageDeliveryRegistryService
     */
    public NotificationMessageDeliveryAutoRemovalServiceImpl(GenericDao businessObjectDao, PlatformTransactionManager txManager,
	    ExecutorService executor, NotificationMessageDeliveryService messageDeliveryService) {
        super(txManager, executor);
        this.messageDeliveryService = messageDeliveryService;
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * @see org.kuali.rice.ken.service.impl.ConcurrentJob#takeAvailableWorkItems()
     */
    @Override
    protected Collection<NotificationMessageDelivery> takeAvailableWorkItems() {
        return messageDeliveryService.takeMessageDeliveriesForAutoRemoval();
    }

    /**
     * @see org.kuali.rice.ken.service.impl.ConcurrentJob#processWorkItem(java.lang.Object)
     */
    @Override
    protected Collection<String> processWorkItems(Collection<NotificationMessageDelivery> messageDeliveries) {
        NotificationMessageDelivery firstMessageDelivery = messageDeliveries.iterator().next();

        KEWActionListMessageDeliverer deliverer = new KEWActionListMessageDeliverer();
        Collection<String> successes = new ArrayList<String>();
        for (NotificationMessageDelivery delivery: messageDeliveries) {
            successes.addAll(autoRemove(deliverer, delivery));
        }
        return successes;
    }

    /**
     * Auto-removes a single message delivery
     * @param messageDeliverer the message deliverer
     * @param messageDelivery the message delivery to auto-remove
     * @return collection of strings indicating successful auto-removals
     */
    protected Collection<String> autoRemove(NotificationMessageDeliverer messageDeliverer, NotificationMessageDelivery messageDelivery) {
        List<String> successes = new ArrayList<String>(1);

        // we have our message deliverer, so tell it to auto remove the message
        try {
            messageDeliverer.autoRemoveMessageDelivery(messageDelivery);
            LOG.debug("Auto-removal of message delivery '" + messageDelivery.getId() + "' for notification '" + messageDelivery.getNotification().getId() + "' was successful.");
            successes.add("Auto-removal of message delivery '" + messageDelivery.getId() + "' for notification '" + messageDelivery.getNotification().getId() + "' was successful.");
        } catch (NotificationAutoRemoveException nmde) {
            LOG.error("Error auto-removing message " + messageDelivery);
            throw new RuntimeException(nmde);
        }
        
        // unlock item
        // now update the status of the delivery message instance to AUTO_REMOVED and persist
        markAutoRemoved(messageDelivery);

        return successes;
    }

    /**
     * Marks a MessageDelivery as having been auto-removed, and unlocks it
     * @param messageDelivery the messageDelivery instance to mark
     */
    protected void markAutoRemoved(NotificationMessageDelivery messageDelivery) {
        messageDelivery.setMessageDeliveryStatus(NotificationConstants.MESSAGE_DELIVERY_STATUS.AUTO_REMOVED);
        // mark as unlocked
        messageDelivery.setLockedDateValue(null);
        businessObjectDao.save(messageDelivery);
    }

    /**
     * @see org.kuali.rice.ken.service.impl.ConcurrentJob#unlockWorkItem(java.lang.Object)
     */
    @Override
    protected void unlockWorkItem(NotificationMessageDelivery delivery) {
        messageDeliveryService.unlockMessageDelivery(delivery);
    }

    /**
     * This implementation looks up all UNDELIVERED/DELIVERED message deliveries with an autoRemoveDateTime <= current date time and then iterates 
     * over each to call the appropriate functions to do the "auto-removal" by "canceling" each associated notification 
     * workflow document.
     * @see org.kuali.rice.ken.service.NotificationMessageDeliveryDispatchService#processAutoRemovalOfDeliveredNotificationMessageDeliveries()
     */
    public ProcessingResult processAutoRemovalOfDeliveredNotificationMessageDeliveries() {
        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] STARTING NOTIFICATION AUTO-REMOVAL PROCESSING");

        ProcessingResult result = run();
        
        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] FINISHED NOTIFICATION AUTO-REMOVAL PROCESSING - Successes = " + result.getSuccesses().size() + ", Failures = " + result.getFailures().size());

        return result;
    }
}
