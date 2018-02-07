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
package org.kuali.rice.ksb;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.MessagingTestObject;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.ksb.test.KSBTestCase;

//@Ignore
public class StartBusTest extends KSBTestCase {

	@Override
	public boolean startClient1() {
		return true;
	}

	@Test
	public void testStartTheBus() {
		QName serviceName = new QName("TestCl1", "testJavaAsyncService");
		
		KsbApiServiceLocator.getServiceBus().synchronize();
		
		KSBJavaService testJavaAsyncService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
		testJavaAsyncService.invoke(new MessagingTestObject("message content"));
		
		// verifyServiceCalls(serviceName);
	}

	// @Test public void testStartClient1() throws Exception {
	// new TestClient1().start();
	// }
	//	
	// @Test public void testStartClient2() throws Exception {
	// new TestClient1().start();
	// new TestClient2().start();
	// }
	//	
}
