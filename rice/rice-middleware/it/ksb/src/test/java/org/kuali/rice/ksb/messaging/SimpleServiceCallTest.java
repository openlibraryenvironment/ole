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
import org.kuali.rice.ksb.messaging.service.KSBXMLService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;

import javax.xml.namespace.QName;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Tests calling services in a very simple scenario. This test could probably go now that more 'feature-full' tests are out
 * there.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimpleServiceCallTest extends KSBTestCase {

	public boolean startClient1() {
		return true;
	}

    /**
     * This test ensures that doing a toString on a service proxy does not cause infinite loop problems along the lines
     * as reported in https://jira.kuali.org/browse/KULRICE-6708
     */
    @Test
    public void testToStringOnService() {
        KSBTestUtils.setMessagingToAsync();
		QName serviceName = new QName("TestCl1", "testJavaAsyncService");
		SimpleCallback callback = new SimpleCallback();
	    KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, callback);
        String toStringValue = testJavaAsyncService.toString();
        System.out.println("toString value on async service: " + toStringValue);
        assertTrue("toString should have started with 'Service call proxy' but was instead: " + toStringValue, toStringValue.startsWith("Service call proxy"));
    }
	
    @Test
    public void testAsyncJavaCall() throws Exception {
	    KSBTestUtils.setMessagingToAsync();
		
		QName serviceName = new QName("TestCl1", "testJavaAsyncService");
		SimpleCallback callback = new SimpleCallback();
	KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper()
		.getServiceAsynchronously(serviceName, callback);
	    synchronized (callback) {
	        testJavaAsyncService.invoke(new MessagingTestObject("message content"));
	        callback.waitForAsyncCall();
	    }
		verifyServiceCalls(serviceName);
	}
	
    @Test
    public void testAsyncXmlCall() throws Exception {
	    KSBTestUtils.setMessagingToAsync();
		
		QName serviceName = new QName("TestCl1", "testXmlAsyncService");
		SimpleCallback callback = new SimpleCallback();
	KSBXMLService testXmlAsyncService = (KSBXMLService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(
		serviceName, callback);
	    synchronized (callback) {	
	        testXmlAsyncService.invoke("message content");
	        callback.waitForAsyncCall();
	    }
		verifyServiceCalls(serviceName);
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
	}
	
}
