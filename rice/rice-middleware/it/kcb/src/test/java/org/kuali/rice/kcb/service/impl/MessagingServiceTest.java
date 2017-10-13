/**
 * Copyright 2005-2013 The Kuali Foundation
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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;
import org.kuali.rice.kcb.api.message.MessageDTO;
import org.kuali.rice.kcb.quartz.MessageProcessingJob;
import org.kuali.rice.kcb.test.KCBTestCase;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Tests MessagingService 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
/*
@PerTestUnitTestData({
    @UnitTestData(filename = "file:ken/src/main/config/sql/KENBootstrap.sql", delimiter = "/"),
    @UnitTestData(filename = "classpath:org/kuali/rice/kcb/test/KENTestData.sql")
}
)*/
@BaselineMode(Mode.CLEAR_DB)
public class MessagingServiceTest extends KCBTestCase {
    private CountDownLatch signal = new CountDownLatch(1);

    @Override
    public void setUp() throws Exception {
        super.setUp();
    
        services.getRecipientPreferenceService().saveRecipientDelivererConfig("testuser5", "mock", new String[] { "Test Channel #1" });
        services.getRecipientPreferenceService().saveRecipientDelivererConfig("testuser5", "sms", new String[] { "Test Channel #1" });
        services.getRecipientPreferenceService().saveRecipientDelivererConfig("testuser5", "broken", new String[] { "Test Channel #1" }); // this one throws exceptions
        services.getRecipientPreferenceService().saveRecipientDelivererConfig("testuser5", "bogus", new String[] { "Test Channel #1" }); // this one doesn't exist
        
        assertEquals(4, services.getRecipientPreferenceService().getDeliverersForRecipientAndChannel("testuser5", "Test Channel #1").size());
    }

    protected long deliver() throws Exception {
        MessageDTO message = new MessageDTO();
        message.setContent("test content 1");
        message.setChannel("Test Channel #1");
        message.setContentType("test content type 1");
        message.setDeliveryType("test delivery type 1");
        message.setRecipient("testuser5");
        message.setTitle("test title 1");
        message.setOriginId("origin id");

        registerJobListener();

        long id = services.getMessagingService().deliver(message);

        waitForNextJobCompletion();

        Collection<MessageDelivery> deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        assertNotNull(deliveries);
        int delivCount = services.getRecipientPreferenceService().getDeliverersForRecipientAndChannel("testuser5", "Test Channel #1").size();
        assertEquals(delivCount, deliveries.size());
        assertTrue(deliveries.size() > 0);
        int failed = 0;
        for (MessageDelivery delivery: deliveries) {
            if ("broken".equals(delivery.getDelivererTypeName()) || "bogus".equals(delivery.getDelivererTypeName())) {
                assertEquals(MessageDeliveryStatus.UNDELIVERED.name(), delivery.getDeliveryStatus());
                assertEquals(1, delivery.getProcessCount().intValue());
                failed++;
            } else {
                assertEquals(MessageDeliveryStatus.DELIVERED.name(), delivery.getDeliveryStatus());
            }
        }

        assertEquals(2, failed);
        
        // try up till max attempts, which for now is 3
        
        waitForNextJobCompletion();

        failed = 0;
        deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        for (MessageDelivery delivery: deliveries) {
            if ("broken".equals(delivery.getDelivererTypeName()) || "bogus".equals(delivery.getDelivererTypeName())) {
                assertEquals(MessageDeliveryStatus.UNDELIVERED.name(), delivery.getDeliveryStatus());
                assertEquals(2, delivery.getProcessCount().intValue());
                failed++;
            } else {
                assertEquals(MessageDeliveryStatus.DELIVERED.name(), delivery.getDeliveryStatus());
            }
        }

        assertEquals(2, failed);
        
        waitForNextJobCompletion();

        failed = 0;
        deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        for (MessageDelivery delivery: deliveries) {
            if ("broken".equals(delivery.getDelivererTypeName()) || "bogus".equals(delivery.getDelivererTypeName())) {
                assertEquals(MessageDeliveryStatus.UNDELIVERED.name(), delivery.getDeliveryStatus());
                assertEquals(3, delivery.getProcessCount().intValue());
                failed++;
            } else {
                assertEquals(MessageDeliveryStatus.DELIVERED.name(), delivery.getDeliveryStatus());
            }
        }

        assertEquals(2, failed);
        
        // finally the last attempt, nothing should have changed
        
        waitForNextJobCompletion();

        failed = 0;
        deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        for (MessageDelivery delivery: deliveries) {
            if ("broken".equals(delivery.getDelivererTypeName()) || "bogus".equals(delivery.getDelivererTypeName())) {
                assertEquals(MessageDeliveryStatus.UNDELIVERED.name(), delivery.getDeliveryStatus());
                assertEquals(3, delivery.getProcessCount().intValue());
                failed++;
            } else {
                assertEquals(MessageDeliveryStatus.DELIVERED.name(), delivery.getDeliveryStatus());
            }
        }

        assertEquals(2, failed);

        return id;
    }

    @Test
    public void testDeliver() throws Exception {
        Assert.assertFalse(TransactionSynchronizationManager.isActualTransactionActive());

        deliver();
    }
    
    @Test
    public void testDismiss() throws Exception {
        Assert.assertFalse(TransactionSynchronizationManager.isActualTransactionActive());

        long id = deliver();

        registerJobListener();

        services.getMessagingService().remove(id, "a user", "a cause");
        
        waitForNextJobCompletion();

        Collection<MessageDelivery> deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        assertNotNull(deliveries);
        // should be all gone except for the 2 bad deliveries
        assertEquals(2, deliveries.size());
        for (MessageDelivery d: deliveries) {
            assertTrue("broken".equals(d.getDelivererTypeName()) || "bogus".equals(d.getDelivererTypeName()));
        }
    }
    
    @Test
    public void testDismissByOriginId() throws Exception {
        Assert.assertFalse(TransactionSynchronizationManager.isActualTransactionActive());

        long id = deliver();

        registerJobListener();

        services.getMessagingService().removeByOriginId("origin id", "a user", "a cause");
        
        waitForNextJobCompletion();

        Collection<MessageDelivery> deliveries = services.getMessageDeliveryService().getAllMessageDeliveries();
        assertNotNull(deliveries);
        // should be all gone except for the 2 bad deliveries
        assertEquals(2, deliveries.size());
        
        // should be all gone except for the 2 bad deliveries
        assertEquals(2, deliveries.size());
        for (MessageDelivery d: deliveries) {
            assertTrue("broken".equals(d.getDelivererTypeName()) || "bogus".equals(d.getDelivererTypeName()));
        }
    }
    
    protected void registerJobListener() throws SchedulerException {
        KSBServiceLocator.getScheduler().addGlobalJobListener(new JobListenerSupport() {
            @Override
            public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
                log.info("Job was executed: " + context);
                if (MessageProcessingJob.NAME.equals(context.getJobDetail().getName())) {
                    signal.countDown();
                }
            }
            public String getName() {
                return System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(10);
            }
        });
    }

    protected void waitForNextJobCompletion() throws InterruptedException {
        log.info("Waiting for job to complete...");
        signal.await(100, TimeUnit.SECONDS); // time limit so as not to hang tests if something goes wrong
        signal = new CountDownLatch(1);
        log.info("Job completed...");
    }
}
