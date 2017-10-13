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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.RecipientDelivererConfig;
import org.kuali.rice.kcb.api.message.MessageDTO;
import org.kuali.rice.kcb.api.exception.MessageDeliveryException;
import org.kuali.rice.kcb.api.exception.MessageDismissalException;
import org.kuali.rice.kcb.quartz.MessageProcessingJob;
import org.kuali.rice.kcb.service.MessageDeliveryService;
import org.kuali.rice.kcb.service.MessageService;
import org.kuali.rice.kcb.api.service.MessagingService;
import org.kuali.rice.kcb.service.RecipientPreferenceService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * MessagingService implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessagingServiceImpl implements MessagingService {
    private static final Logger LOG = Logger.getLogger(MessagingServiceImpl.class);

    private MessageService messageService;
    private MessageDeliveryService messageDeliveryService;
    private RecipientPreferenceService recipientPrefs;
    private String jobName;
    private String jobGroup;

    /**
     * Whether to perform the processing  synchronously
     */
    private boolean synchronous;
    
    /**
     * Sets the name of the target job to run to process messages
     * @param jobName the name of the target job to run to process messages
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Sets the group of the target job to run to process messages
     * @param jobGroup Sets the group of the target job to run to process messages
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * Sets the MessageService
     * @param messageService the MessageService
     */
    @Required
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Sets the MessageDeliveryService
     * @param messageDeliveryService the MessageDeliveryService
     */
    @Required
    public void setMessageDeliveryService(MessageDeliveryService messageDeliveryService) {
        this.messageDeliveryService = messageDeliveryService;
    }

    /**
     * Sets whether to perform the processing synchronously
     * @param sync whether to perform the processing synchronously
     */
    public void setSynchronous(boolean sync) {
        LOG.debug("Setting synchronous messaging to: " + sync);
        this.synchronous = sync;
    }

    /**
     * Sets the RecipientPreferencesService
     * @param prefs the RecipientPreferenceService
     */
    @Required
    public void setRecipientPreferenceService(RecipientPreferenceService prefs) {
        this.recipientPrefs = prefs;
    }

    /**
     * @see org.kuali.rice.kcb.service.MessagingService#deliver(org.kuali.rice.kcb.dto.MessageDTO)
     */
    @Override
    public Long deliver(MessageDTO message) throws MessageDeliveryException {
        if (message == null) {
            throw new RiceIllegalArgumentException("message is null");
        }

        Collection<String> delivererTypes = getDelivererTypesForUserAndChannel(message.getRecipient(), message.getChannel());
        LOG.debug("Deliverer types for " + message.getRecipient() + "/" + message.getChannel() + ": " + delivererTypes.size());

        if (delivererTypes.isEmpty()) {
            // no deliverers configured? just skipp it
            LOG.debug("No deliverers are configured for " + message.getRecipient() + "/" + message.getChannel());
            return null;
        }

        final Message m = new Message();
        m.setTitle(message.getTitle());
        m.setDeliveryType(message.getDeliveryType());
        m.setChannel(message.getChannel());
        m.setRecipient(message.getRecipient());
        m.setContentType(message.getContentType());
        m.setUrl(message.getUrl());
        m.setContent(message.getContent());
        m.setOriginId(message.getOriginId());

        LOG.debug("saving message: " +m);
        messageService.saveMessage(m);

        for (String type: delivererTypes) {
            
            MessageDelivery delivery = new MessageDelivery();
            delivery.setDelivererTypeName(type);
            delivery.setMessage(m);

//            MessageDeliverer deliverer = delivererRegistry.getDeliverer(delivery);
//            if (deliverer != null) {
//                deliverer.deliverMessage(delivery);
//            }
        
            LOG.debug("saving messagedelivery: " +delivery);
            messageDeliveryService.saveMessageDelivery(delivery);
        }

        LOG.debug("queuing job");
        queueJob(MessageProcessingJob.Mode.DELIVER, m.getId(), null, null);

        LOG.debug("returning");
        return m.getId();
    }

    /**
     * @see org.kuali.rice.kcb.service.MessagingService#remove(long, java.lang.String, java.lang.String)
     */
    @Override
    public void remove(long messageId, String user, String cause) throws MessageDismissalException {
        /*if (StringUtils.isBlank(messageId)) {
            throw new RiceIllegalArgumentException("message is null");
        } if we switch to String id*/

        if (StringUtils.isBlank(user)) {
            throw new RiceIllegalArgumentException("user is null");
        }

        if (StringUtils.isBlank(cause)) {
            throw new RiceIllegalArgumentException("cause is null");
        }

        Message m = messageService.getMessage(Long.valueOf(messageId));
        if (m == null) {
            throw new MessageDismissalException("No such message: " + messageId);
        }

        remove (m, user, cause);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessagingService#removeByOriginId(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Long removeByOriginId(String originId, String user, String cause) throws MessageDismissalException {
        if (StringUtils.isBlank(originId)) {
            throw new RiceIllegalArgumentException("originId is null");
        }

        Message m = messageService.getMessageByOriginId(originId);
        if (m == null) {
            return null; 
            //throw new MessageDismissalException("No such message with origin id: " + originId);
        }
        remove(m, user, cause);
        return m.getId();
    }

    private void remove(Message message, String user, String cause) {
        queueJob(MessageProcessingJob.Mode.REMOVE, message.getId(), user, cause);
    }

    /**
     * Determines what delivery endpoints the user has configured
     * @param userRecipientId the user
     * @return a Set of NotificationConstants.MESSAGE_DELIVERY_TYPES
     */
    private Collection<String> getDelivererTypesForUserAndChannel(String userRecipientId, String channel) {
        Set<String> deliveryTypes = new HashSet<String>(1);
        
        // manually add the default one since they don't have an option on this one
        //deliveryTypes.add(NotificationConstants.MESSAGE_DELIVERY_TYPES.DEFAULT_MESSAGE_DELIVERY_TYPE);
        
        //now look for what they've configured for themselves
        Collection<RecipientDelivererConfig> deliverers = recipientPrefs.getDeliverersForRecipientAndChannel(userRecipientId, channel);
        
        for (RecipientDelivererConfig cfg: deliverers) {
            deliveryTypes.add(cfg.getDelivererName());
        }
        //return GlobalNotificationServiceLocator.getInstance().getKENAPIService().getDeliverersForRecipientAndChannel(userRecipientId, channel);

        return deliveryTypes;
    }

    private void queueJob(MessageProcessingJob.Mode mode, long messageId, String user, String cause) {
        // queue up the processing job after the transaction has committed
        LOG.debug("registering synchronization");

        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
        	throw new RiceRuntimeException("transaction syncronization is not active " +
        			"(!TransactionSynchronizationManager.isSynchronizationActive())");
        } else if (!TransactionSynchronizationManager.isActualTransactionActive()) {
        	throw new RiceRuntimeException("actual transaction is not active " +
        			"(!TransactionSynchronizationManager.isActualTransactionActive())");
        }

        TransactionSynchronizationManager.registerSynchronization(new QueueProcessingJobSynchronization(
            jobName,
            jobGroup,
            mode,
            messageId,
            user,
            cause,
            synchronous
        ));
    }
    
    public static class QueueProcessingJobSynchronization extends TransactionSynchronizationAdapter {
        private static final Logger LOG = Logger.getLogger(QueueProcessingJobSynchronization.class);
        private final String jobName;
        private final String jobGroup;
        private final MessageProcessingJob.Mode mode;
        private final long messageId;
        private final String user;
        private final String cause;
        private final boolean synchronous;

        private QueueProcessingJobSynchronization(String jobName, String jobGroup, MessageProcessingJob.Mode mode, long messageId, String user, String cause, boolean synchronous) {
            this.jobName = jobName;
            this.jobGroup = jobGroup;
            this.mode = mode;
            this.messageId = messageId;
            this.user = user;
            this.cause = cause;
            this.synchronous = synchronous;
        }

        /*
        @Override
        public void beforeCommit(boolean readOnly) {
            super.beforeCommit(readOnly);
        }*/

        @Override
        public void afterCommit() {
            scheduleJob();
        }
        /*@Override
        public void afterCompletion(int status) {
            if (STATUS_COMMITTED == status) {
                scheduleJob();
            } else {
                LOG.error("Status is not committed.  Not scheduling message processing job.");
            }
        }*/

        private void scheduleJob() {
            LOG.debug("Queueing processing job");
            try {
                Scheduler scheduler = KSBServiceLocator.getScheduler();
                if (synchronous) {
                    LOG.debug("Invoking job synchronously in Thread " + Thread.currentThread());
                    MessageProcessingJob job = new MessageProcessingJob(messageId, mode, user, cause);
                    job.run();
                } else {
                    String uniqueTriggerName = jobName + "-Trigger-" + System.currentTimeMillis() + Math.random();
                    SimpleTrigger trigger = new SimpleTrigger(uniqueTriggerName, jobGroup + "-Trigger");
                    LOG.debug("Scheduling trigger: " + trigger);

                    JobDataMap data = new JobDataMap();
                    data.put("mode", mode.name());
                    data.put("user", user);
                    data.put("cause", cause);
                    data.put("messageId", messageId);

                    trigger.setJobName(jobName);
                    trigger.setJobGroup(jobGroup);
                    trigger.setJobDataMap(data);
                    scheduler.scheduleJob(trigger);
                }
            } catch (SchedulerException se) {
                throw new RuntimeException(se);
            }
        }
    }
}
