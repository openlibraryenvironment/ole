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
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.remotedservices.TesetHarnessExplodingQueue;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;
import org.kuali.rice.ksb.util.KSBConstants;
import org.kuali.rice.test.TestUtilities;

import javax.xml.namespace.QName;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Tests exception retries in KSB messaging.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ExceptionRetryCountTest extends KSBTestCase {


	private QName retryCountServiceName = new QName("KEW", "testExplodingRetryCount");
	private TestCallback callback = new TestCallback();

	@Override
	public void setUp() throws Exception {
		System.setProperty(KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY, "500");
	System.setProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY, "2");
		super.setUp();
		GlobalCallbackRegistry.getCallbacks().clear();
		GlobalCallbackRegistry.getCallbacks().add(this.callback);
		TestCallback.clearCallbacks();
		TesetHarnessExplodingQueue.NUM_CALLS = 0;
	}

	@Override
	public void tearDown() throws Exception {
	    try {
		KSBServiceLocator.getScheduler().shutdown();
	    } finally {
		super.tearDown();
	    }
	}

	/**
	 * Test that a message with retry count gets retried that many times.
	 *
	 * @throws Exception
	 */
    @Test
    public void testRetryCount() throws Exception {
		//Turn the requeue up very high so the message will go through all it's requeues immediately

		ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY, "10000");

	KSBJavaService explodingQueue = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(
		this.retryCountServiceName);
		explodingQueue.invoke("");
		TestUtilities.waitForExceptionRouting();

		this.callback.pauseUntilNumberCallbacksUsingStaticCounter(3, this.retryCountServiceName);

	//pause to let save to queue in status 'E' happen
	int i = 0;
	while (i++ < 30) {
	    List<PersistedMessageBO> queuedItems = KSBServiceLocator.getMessageQueueService().findAll();
	    if (queuedItems.size() != 1) {
		fail("test setup wrong should have a single item in the queue.");
	    }
	    PersistedMessageBO message = queuedItems.get(0);
	    if (message.getQueueStatus().equals("E")) {
		break;
	    }
	    System.out.println("Message not saved to queue in 'E' status.  Sleeping 1 sec.");
	    Thread.sleep(1000);
	}

		assertEquals("Service should have been called 3 times", 3, TesetHarnessExplodingQueue.NUM_CALLS);

	    List<PersistedMessageBO> messagesQueued = KSBServiceLocator.getMessageQueueService().findByServiceName(
		this.retryCountServiceName, "invoke");
		PersistedMessageBO message = messagesQueued.get(0);
		assertEquals("Message should be in exception status", KSBConstants.ROUTE_QUEUE_EXCEPTION, message.getQueueStatus());
		assertEquals("Message retry count not what was configured", new Integer(2), message.getRetryCount());
	}

}
