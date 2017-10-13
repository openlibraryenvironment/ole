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
package org.kuali.rice.ksb.messaging;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;
import org.kuali.rice.ksb.messaging.bam.service.BAMService;
import org.kuali.rice.ksb.messaging.callbacks.SimpleCallback;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;

import javax.xml.namespace.QName;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests distributed Queue scenarios
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class DistributedQueueTest extends KSBTestCase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DistributedQueueTest.class);
    
    public boolean startClient1() {
        return true;
    }

    public boolean startClient2() {
        return true;
    }

    /**
     * If calling a queue with multiple subscribers only one subscriber should be called.
     * 
     * @throws Exception
     */
    @Test
    public void testSuccessfullyCallingQueueOnce() throws Exception {
        QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");
        KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
        testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
        verifyServiceCalls(serviceName);

    }

    @Test
    public void testCallingQueueAsnyc() throws Exception {
        KSBTestUtils.setMessagingToAsync();
        // Execute this 100 times, previously when only running a single iteration, this test would pass most of the
        // time but then would occasionally fail, it was possible to reproduce the failure every time by adding
        // these iterations, this allowed us to fix the underlying problem (an issue with KSB callbacks)
        for (int i = 0; i < 100; i++) {
            LOG.info("testCallingQueueAsnyc, iteration: " + i);
            QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");
            SimpleCallback callback = new SimpleCallback();
            KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, callback);
            synchronized (callback) {
                testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
                callback.waitForAsyncCall();
            }
            verifyServiceCalls(serviceName);
            KSBServiceLocator.getBAMService().clearBAMTables();
        }
    }

    private void verifyServiceCalls(QName serviceName) throws Exception {
        BAMService bamService = KSBServiceLocator.getBAMService();
        List<BAMTargetEntry> bamCalls = bamService.getCallsForService(serviceName);
        assertTrue("No service call recorded", bamCalls.size() > 0);
        boolean foundClientCall = false;
        boolean foundServiceCall = false;
        for (BAMTargetEntry bamEntry : bamCalls) {
            if (bamEntry.getServerInvocation()) {
                foundServiceCall = true;
            } else {
                foundClientCall = true;
            }
        }
        assertTrue("No client call recorded", foundClientCall);
        assertTrue("No service call recorded", foundServiceCall);
        assertEquals("Wrong number of calls recorded", 2, bamCalls.size());
    }

}
