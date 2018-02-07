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

import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.callbacks.SimpleCallback;
import org.kuali.rice.ksb.messaging.remotedservices.ServiceCallInformationHolder;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 * simple test verifying if distributed topics are working.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DistributedTopicTest extends KSBTestCase {
	
	public boolean startClient1() {
		return true;
	}
	
	@Test
	public void testSuccessfullyCallingSyncTopics() throws Exception {
		
		KsbApiServiceLocator.getServiceBus().synchronize();
		QName serviceName = new QName("testAppsSharedTopic", "sharedTopic");
		
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
		testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
		
		assertTrue("Test harness topic never called", ((Boolean)ServiceCallInformationHolder.stuff.get("TestHarnessCalled")).booleanValue());
		assertTrue("Client1 app topic never called", ((Boolean)ServiceCallInformationHolder.stuff.get("Client1Called")).booleanValue());
	}
	
	@Test public void testCallingAsyncTopics() throws Exception {
	    	KSBTestUtils.setMessagingToAsync();
		
		QName serviceName = new QName("testAppsSharedTopic", "sharedTopic");
		
		SimpleCallback simpleCallback = new SimpleCallback();
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, simpleCallback);
		synchronized (simpleCallback) {
		    testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
		    simpleCallback.waitForAsyncCall();
		}
		//because topic invocation doesn't happen in the message service invoker like it should this wait is really on half the answer.  We need to wait long and poll 
		//to determine if the client1 has been called.
		
		int i = 0;
		while (i < 100) {
		    if (ServiceCallInformationHolder.stuff.get("Client1Called") != null) {
			break;
		    }
		    Thread.sleep(1000);
		    i++;
		}
	
		assertTrue("Test harness topic never called", ((Boolean)ServiceCallInformationHolder.stuff.get("TestHarnessCalled")).booleanValue());
		assertTrue("Client1 app topic never called", ((Boolean)ServiceCallInformationHolder.stuff.get("Client1Called")).booleanValue());
	
	}
	
}
