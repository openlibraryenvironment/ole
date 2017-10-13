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
package org.kuali.rice.ksb.messaging.exceptionhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.GlobalCallbackRegistry;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.TestCallback;
import org.kuali.rice.ksb.messaging.remotedservices.TesetHarnessExplodingQueue;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;
import org.kuali.rice.ksb.util.KSBConstants;
import org.kuali.rice.test.TestUtilities;


/**
 * Tests various exception messaging cases
 *
 * Millis to live - that a message with no home is still sending messages while it's time to live hasn't expired
 * Retry count - that a message configured with a retry count will send x number of messages before being marked exception
 * Being marked as exception - that a message in exception is in the route log and marked with a status of 'E'
 * Defuault retry count - that a message configured with no retry or time to live is retry the default number of times as
 * 	noted in an app constant and a class default if that constant is not a number or doesn't exist
 * App Constant to determine the default time increment works (we need this to effectively test anyway)
 * Things work without the timeincrement constant in place
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ExceptionMessagingTest extends KSBTestCase {


	private QName queueTimeToLiveServiceName = new QName("KEW", "explodingQueueTimeLimit");
	private TestCallback callback = new TestCallback();

	@Override
	public void setUp() throws Exception {
		System.setProperty(KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY, "500");
		System.setProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY, "5");
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
	 * test that service is in queue marked 'E' when the time to live is expired.
	 * @throws Exception
	 */
	@Test public void testTimeToLive() throws Exception {

		KSBJavaService explodingQueue = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(this.queueTimeToLiveServiceName);
		explodingQueue.invoke("");
		TestUtilities.waitForExceptionRouting();
		//this service is on a 3 second wait the queue is on a 1 sec.
		Thread.sleep(10000);

		//verify the entry is in exception routing
		List<PersistedMessageBO> messagesQueued = KSBServiceLocator.getMessageQueueService().findByServiceName(this.queueTimeToLiveServiceName, "invoke");
		PersistedMessageBO message = messagesQueued.get(0);
		assertEquals("Message should be in exception status", KSBConstants.ROUTE_QUEUE_EXCEPTION, message.getQueueStatus());
		assertTrue("Message expiration date should be equal to or earlier than last queue date", message.getExpirationDate().getTime() <= message.getQueueDate().getTime());
	}


}
