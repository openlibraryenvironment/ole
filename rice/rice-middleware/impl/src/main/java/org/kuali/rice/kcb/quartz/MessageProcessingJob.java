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
package org.kuali.rice.kcb.quartz;

import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;
import org.kuali.rice.kcb.deliverer.BulkMessageDeliverer;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.api.exception.MessageDeliveryProcessingException;
import org.kuali.rice.kcb.quartz.ProcessingResult.Failure;
import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.service.MessageDelivererRegistryService;
import org.kuali.rice.kcb.service.MessageDeliveryService;
import org.kuali.rice.kcb.service.MessageService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Job that delivers messages to endpoints.  This job is not really stateful,
 * but should not be executed concurrently.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageProcessingJob extends ConcurrentJob<MessageDelivery> implements StatefulJob {
    public static final String NAME = "MessageProcessingJobDetail";
    public static final String GROUP = "KCB-Delivery";

    public static enum Mode {
        DELIVER, REMOVE
    }

    private static final Logger LOG = Logger.getLogger(MessageProcessingJob.class);
    
    private GenericDao dao;
    private MessageDelivererRegistryService registry;
    private MessageDeliveryService messageDeliveryService;
    private Long messageId;
    private Mode mode = null;
    private String user;
    private String cause;

    public MessageProcessingJob(Long messageId, Mode mode, String user, String cause) {
        this();
        this.messageId = messageId;
        this.mode = mode;
        this.user = user;
        this.cause = cause;
    }


    public MessageProcessingJob() {
        dao = GlobalKCBServiceLocator.getInstance().getKcbGenericDao();
        registry = GlobalKCBServiceLocator.getInstance().getMessageDelivererRegistryService();
        messageDeliveryService = GlobalKCBServiceLocator.getInstance().getMessageDeliveryService();
        txManager = GlobalKCBServiceLocator.getInstance().getTransactionManager();
    }

    /**
     * Sets the {@link GenericDao}
     * @param dao the {@link GenericDao}
     */
    @Required
    public void setGenericDao(GenericDao dao) {
        this.dao = dao;
    }

    /**
     * Sets the {@link MessageDelivererRegistryService}
     * @param registry the {@link MessageDelivererRegistryService}
     */
    @Required
    public void setMessageDelivererRegistry(MessageDelivererRegistryService registry) {
        this.registry = registry;
    }
    
    /**
     * Sets the {@link MessageDeliveryService}
     * @param messageDeliveryService the {@link MessageDeliveryService}
     */
    @Required
    public void setMessageDeliveryService(MessageDeliveryService messageDeliveryService) {
        this.messageDeliveryService = messageDeliveryService;
    }

    @Override
    protected Collection<MessageDelivery> takeAvailableWorkItems() {
        MessageDeliveryStatus[] statuses;
        switch (mode) {
            case DELIVER: {
                statuses = new MessageDeliveryStatus[] { MessageDeliveryStatus.UNDELIVERED };
                break;
            }
            case REMOVE: {
                if (messageId == null) {
                    throw new IllegalStateException("Message id must be specified for message removal mode");
                }
                statuses = new MessageDeliveryStatus[] { MessageDeliveryStatus.DELIVERED, MessageDeliveryStatus.UNDELIVERED };
                break;
            }
            default:
                throw new RuntimeException("Invalid mode: " + mode);
        }
        for (MessageDeliveryStatus status: statuses) {
            LOG.debug("Taking message deliveries with status: " + status);
        }
        Collection<MessageDelivery> ds = messageDeliveryService.lockAndTakeMessageDeliveries(messageId, statuses);
        LOG.debug("Took " + ds.size() + " deliveries");
        for (MessageDelivery md: ds) {
            LOG.debug(md);
            md.setProcessCount(md.getProcessCount().intValue() + 1);
        }
        return ds;
    }

    @Override
    protected void unlockWorkItem(MessageDelivery item) {
        item.setLockedDate(null);
        dao.save(item);
    }

    /**
     * Group work items by deliverer and notification, so that deliveries to bulk deliverers are grouped
     * by notification
     * @see org.kuali.rice.ken.service.impl.ConcurrentJob#groupWorkItems(java.util.Collection)
     */
    @Override
    protected Collection<Collection<MessageDelivery>> groupWorkItems(Collection<MessageDelivery> workItems, ProcessingResult<MessageDelivery> result) {
        Collection<Collection<MessageDelivery>> groupedWorkItems = new ArrayList<Collection<MessageDelivery>>(workItems.size());

        Map<String, Collection<MessageDelivery>> bulkWorkUnits = new HashMap<String, Collection<MessageDelivery>>();
        for (MessageDelivery messageDelivery: workItems) {
            
            MessageDeliverer deliverer = registry.getDeliverer(messageDelivery);
            if (deliverer == null) {
                LOG.error("Error obtaining message deliverer for message delivery: " + messageDelivery);
                result.addFailure(new Failure<MessageDelivery>(messageDelivery, "Error obtaining message deliverer for message delivery"));
                unlockWorkItemAtomically(messageDelivery);
                continue;
            }

            if (deliverer instanceof BulkMessageDeliverer) {
                // group by bulk-deliverer+message combo
                String key = messageDelivery.getDelivererTypeName() + ":" + messageDelivery.getMessage().getId();
                Collection<MessageDelivery> workUnit = bulkWorkUnits.get(key);
                if (workUnit == null) {
                    workUnit = new LinkedList<MessageDelivery>();
                    bulkWorkUnits.put(key, workUnit);
                }
                workUnit.add(messageDelivery);
            } else {
                ArrayList<MessageDelivery> l = new ArrayList<MessageDelivery>(1);
                l.add(messageDelivery);
                groupedWorkItems.add(l);
            }
        }

        return groupedWorkItems;
    }
    
    
    @Override
    protected Collection<MessageDelivery> processWorkItems(Collection<MessageDelivery> messageDeliveries) {
        MessageDelivery firstMessageDelivery = messageDeliveries.iterator().next();
        // get our hands on the appropriate MessageDeliverer instance
        MessageDeliverer messageDeliverer = registry.getDeliverer(firstMessageDelivery);
        if (messageDeliverer == null) {
            throw new RuntimeException("Message deliverer could not be obtained");
        }
    
        if (messageDeliveries.size() > 1) {
            // this is a bulk deliverer, so we need to batch the MessageDeliveries
            if (!(messageDeliverer instanceof BulkMessageDeliverer)) {
                throw new RuntimeException("Discrepency in dispatch service: deliverer for list of message deliveries is not a BulkMessageDeliverer");
            }
            return bulkProcess((BulkMessageDeliverer) messageDeliverer, messageDeliveries, mode);
        } else {
            return process(messageDeliverer, firstMessageDelivery, mode);
        }
    }

    /**
     * Implements delivery of a single MessageDelivery
     * @param deliverer the deliverer
     * @param messageDelivery the delivery
     * @return collection of strings indicating successful deliveries
     */
    protected Collection<MessageDelivery> process(MessageDeliverer messageDeliverer, MessageDelivery messageDelivery, Mode mode) {
        // we have our message deliverer, so tell it to deliver the message
        try {
            if (mode == Mode.DELIVER) {
                messageDeliverer.deliver(messageDelivery);
                // if processing was successful, set the count back to zero
                messageDelivery.setProcessCount(Integer.valueOf(0));
                // by definition we have succeeded at this point if no exception was thrown by the messageDeliverer
                // so update the status of the delivery message instance to DELIVERED (and unmark as taken)
                // and persist
                updateStatusAndUnlock(messageDelivery, mode == Mode.DELIVER ? MessageDeliveryStatus.DELIVERED : MessageDeliveryStatus.REMOVED);
            } else {
                messageDeliverer.dismiss(messageDelivery, user, cause);
                // don't need to set the processing count down to zero because we are just deleting the record entirely
                messageDeliveryService.deleteMessageDelivery(messageDelivery);
            }
        } catch (MessageDeliveryProcessingException nmde) {
            LOG.error("Error processing message delivery " + messageDelivery, nmde);
            throw new RuntimeException(nmde);
        }

        LOG.debug("Message delivery '" + messageDelivery.getId() + "' for message '" + messageDelivery.getMessage().getId() + "' was successfully processed.");
        //PerformanceLog.logDuration("Time to dispatch notification delivery for notification " + messageDelivery.getMessage().getId(), System.currentTimeMillis() - messageDelivery.getNotification().getSendDateTime().getTime());

        List<MessageDelivery> success = new ArrayList<MessageDelivery>(1);
        success.add(messageDelivery);
        return success;
    }

    /**
     * Implements bulk delivery of a collection of {@link MessageDelivery}s
     * @param deliverer the deliverer
     * @param messageDeliveries the deliveries
     * @return collection of strings indicating successful deliveries
     */
    protected Collection<MessageDelivery> bulkProcess(BulkMessageDeliverer messageDeliverer, Collection<MessageDelivery> messageDeliveries,  Mode mode) {
        MessageDeliveryStatus targetStatus = (mode == Mode.DELIVER ? MessageDeliveryStatus.DELIVERED : MessageDeliveryStatus.REMOVED);
        // we have our message deliverer, so tell it to deliver the message
        try {
            if (mode == Mode.DELIVER) {
                messageDeliverer.bulkDeliver(messageDeliveries);
            } else {
                messageDeliverer.bulkDismiss(messageDeliveries);
            }
        } catch (MessageDeliveryProcessingException nmde) {
            LOG.error("Error bulk-delivering messages " + messageDeliveries, nmde);
            throw new RuntimeException(nmde);
        }

        // by definition we have succeeded at this point if no exception was thrown by the messageDeliverer
        // so update the status of the delivery message instance to DELIVERED (and unmark as taken)
        // and persist
        List<MessageDelivery> successes = new ArrayList<MessageDelivery>(messageDeliveries.size());
        for (MessageDelivery nmd: messageDeliveries) {
            successes.add(nmd);
            LOG.debug("Message delivery '" + nmd.getId() + "' for notification '" + nmd.getMessage().getId() + "' was successfully delivered.");
            //PerformanceLog.logDuration("Time to dispatch notification delivery for notification " + nmd.getMessage().getId(), System.currentTimeMillis() - nmd.getNotification().getSendDateTime().getTime());
            if (mode == Mode.REMOVE) {
                messageDeliveryService.deleteMessageDelivery(nmd);
            } else {
                nmd.setProcessCount(0);
                updateStatusAndUnlock(nmd, targetStatus);                
            }
        }
        
        return successes;
    }

    @Override
    protected void finishProcessing(ProcessingResult<MessageDelivery> result) {
        LOG.debug("Message processing job: " + result.getSuccesses().size() + " processed, " + result.getFailures().size() + " failures");
        Set<Long> messageIds = new HashSet<Long>(result.getSuccesses().size());
        for (MessageDelivery md: result.getSuccesses()) {
            messageIds.add(md.getMessage().getId());
        }
        MessageService ms = GlobalKCBServiceLocator.getInstance().getMessageService();
        for (Long id: messageIds) {
            LOG.debug("Finishing processing message " + id);
            //if (Mode.REMOVE == mode) {
            
            Message m = ms.getMessage(id);
            
            Collection<MessageDelivery> c = messageDeliveryService.getMessageDeliveries(m);
            if (c.size() == 0) {
                LOG.debug("Deleting message " + m);
                ms.deleteMessage(m);
            } else {
                LOG.debug("Message " + m.getId() + " has " + c.size() + " deliveries");
                for (MessageDelivery md: c) {
                    LOG.debug(md);
                }
            }
        }
    }

    /**
     * Marks a MessageDelivery as having been delivered, and unlocks it
     * @param messageDelivery the messageDelivery instance to mark
     */
    protected void updateStatusAndUnlock(MessageDelivery messageDelivery, MessageDeliveryStatus status) {
        messageDelivery.setDeliveryStatus(status);
        // mark as unlocked
        messageDelivery.setLockedDate(null);
        dao.save(messageDelivery);
    }

    @Override
    public ProcessingResult<MessageDelivery> run() {
        LOG.debug("MessageProcessingJob running in Thread " + Thread.currentThread() + ": " + mode + " " + user + " " + cause);
        return super.run();
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String mode = context.getMergedJobDataMap().getString("mode");
        if (mode != null) {
            this.mode = Mode.valueOf(mode);
        } else {
            this.mode = Mode.DELIVER;
        }
        this.user = context.getMergedJobDataMap().getString("user");
        this.cause = context.getMergedJobDataMap().getString("cause");
        if (context.getMergedJobDataMap().containsKey("messageId")) {
            this.messageId = context.getMergedJobDataMap().getLong("messageId");
        }
        LOG.debug("==== message processing job: " + this.mode + " message id: " + this.messageId + "====");
        super.run();
    }
}
