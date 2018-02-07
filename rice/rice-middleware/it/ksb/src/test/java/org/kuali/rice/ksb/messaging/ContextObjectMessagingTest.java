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
import org.kuali.rice.ksb.messaging.callbacks.SimpleCallback;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.test.KSBTestCase;

import javax.xml.namespace.QName;

import static org.junit.Assert.assertEquals;


/**
 * Test that a context object passed through messaging is preserved in
 * 
 * async queue async topic
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class ContextObjectMessagingTest extends KSBTestCase {

    public boolean startClient1() {
	return true;
    }

    @Test
    public void testCallingQueueAsnyc() throws Exception {
	
	KSBTestUtils.setMessagingToAsync();
	QName serviceName = new QName("testAppsSharedQueue", "sharedQueue");
	String contextObject = "my_context_object";
	SimpleCallback callback = new SimpleCallback();
	
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, callback, contextObject);
	    
	synchronized (callback) {
	    testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
	    callback.waitForAsyncCall();
	}

	Object contextAfterMessaging = callback.getMethodCall().getContext();
	assertEquals(contextObject, contextAfterMessaging);
    }

    @Test
    public void testCallingAsyncTopics() throws Exception {
	KSBTestUtils.setMessagingToAsync();
	QName serviceName = new QName("testAppsSharedTopic", "sharedTopic");

	SimpleCallback callback = new SimpleCallback();
	String contextObject = "my_context_object";
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, callback, contextObject);
	
	synchronized (callback) {
	    testJavaAsyncService.invoke(new ClientAppServiceSharedPayloadObj("message content", false));
	    callback.waitForAsyncCall();    
	}
	
	Object contextAfterMessaging = callback.getMethodCall().getContext();
	assertEquals(contextObject, contextAfterMessaging);
    }

}
