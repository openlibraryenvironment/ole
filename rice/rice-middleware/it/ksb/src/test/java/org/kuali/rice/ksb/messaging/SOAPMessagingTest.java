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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;
import org.kuali.rice.ksb.messaging.bam.service.BAMService;
import org.kuali.rice.ksb.messaging.callbacks.SimpleCallback;
import org.kuali.rice.ksb.messaging.remotedservices.SOAPService;
import org.kuali.rice.ksb.messaging.remotedservices.ServiceCallInformationHolder;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 * Tests that queues work over soap
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SOAPMessagingTest extends KSBTestCase {

    public boolean startClient1() {
	return true;
    }

    @Test
    public void testSuccessfullyCallingSOAPTopic() throws Exception {
	// ensure test harness has entries for TestClient1
	KsbApiServiceLocator.getServiceBus().synchronize();

	QName serviceName = new QName("testNameSpace", "soap-repeatTopic");

		SOAPService testJavaAsyncService = (SOAPService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
	testJavaAsyncService.doTheThing("The param");
	verifyServiceCalls(serviceName);

		assertTrue("Test harness topic never called", ((Boolean) ServiceCallInformationHolder.stuff.get("TestHarnessCalled")).booleanValue());
		assertTrue("Cliet1 app topic never called", ((Boolean) ServiceCallInformationHolder.stuff.get("Client1SOAPServiceCalled")).booleanValue());
    }

    @Test
    public void testSuccessfullyCallingSOAPTopicAsync() throws Exception {
	KSBTestUtils.setMessagingToAsync();

	QName serviceName = new QName("testNameSpace", "soap-repeatTopic");

	SimpleCallback callback = new SimpleCallback();
	SOAPService testJavaAsyncService = (SOAPService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
	synchronized (callback) {
	    testJavaAsyncService.doTheThing("The param");
	    callback.waitForAsyncCall(3000);
	}
	verifyServiceCalls(serviceName);
		assertTrue("Test harness topic never called", ((Boolean) ServiceCallInformationHolder.stuff.get("TestHarnessCalled")).booleanValue());
		assertTrue("Cliet1 app topic never called", ((Boolean) ServiceCallInformationHolder.stuff.get("Client1SOAPServiceCalled")).booleanValue());
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
