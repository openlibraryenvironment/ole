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
public class DelayedAsynchronousServiceTest extends KSBTestCase {

    public boolean startClient1() {
	    return true;
    }

    public boolean startClient2() {
	    return true;
    }

    @Test public void testDelayedAsynchronousServiceCall() throws Exception {
        KSBTestUtils.setMessagingToAsync();

        QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");

        // Queue up the service to be called asynchronously after 5 seconds
        KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, "context", "value1", "value2", 5000);
    	testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
        verifyServiceCalls(serviceName, false);

        // sleep for 1 second, should not have been called
        Thread.sleep(1000);
        verifyServiceCalls(serviceName, false);

        // sleep for 1 second, should not have been called
        Thread.sleep(1000);
        verifyServiceCalls(serviceName, false);

        // sleep for 1 second, should not have been called
        Thread.sleep(1000);
        verifyServiceCalls(serviceName, false);

        Thread.sleep(1000);
        verifyServiceCalls(serviceName, false);

        // TODO this isn't the best test ever because it's relying on waits and timing which is most likely doomed to occasional
        // failure in the CI environment.  If this occurs than I may need to yank this.  A better long term solution would be
        // to allow for the use of callbacks for delayed asynchronous services but that's not something I wanted to try
        // to tackle at the moment

        // now sleep for 3 more seconds, the call should be invoked
        Thread.sleep(3000);
        verifyServiceCalls(serviceName, true);

    }

    private void verifyServiceCalls(QName serviceName, boolean shouldHaveBeenCalled) throws Exception {
        BAMService bamService = KSBServiceLocator.getBAMService();
        List<BAMTargetEntry> bamCalls = bamService.getCallsForService(serviceName);
        if (!shouldHaveBeenCalled) {
            assertTrue("A service call should not have been recorded yet.", bamCalls.size() == 0);
        } else {
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

}
