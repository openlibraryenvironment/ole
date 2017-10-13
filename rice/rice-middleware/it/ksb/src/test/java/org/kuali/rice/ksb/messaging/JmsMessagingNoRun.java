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



/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JmsMessagingNoRun {
    // TODO renable this test
	
//	private TestClient1 testClient1;
//	
//	@Override
//	public void setUp() throws Exception {
//		testClient1 = new TestClient1();
//		super.setUp();
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List getLifecycles() {
//		List lifecycles = super.getLifecycles();
//		lifecycles.add(testClient1);
//		return lifecycles;
//	}
//	
//	@Test public void testJmsMessage() throws Exception {
//		QName serviceName = new QName("TestCl1", "jmsService");
//		TestServiceInterface jmsService = (TestServiceInterface) KSBServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
//		jmsService.invoke();
//	}

//	private void verifyServiceCalls() throws Exception {
//		BAMService bamService = KSBServiceLocator.getBAMService();
//		List<BAMTargetEntry> bamCalls = bamService.getCallsForService(QName.valueOf("{TestCl1}jmsService"));
//		assertTrue("No service call recorded", bamCalls.size() > 0);
//		boolean foundClientCall = false;
//		boolean foundServiceCall = false;
//		for (BAMTargetEntry bamEntry : bamCalls) {
//			if (bamEntry.getServerInvocation()) {
//				foundServiceCall = true;
//			} else {
//				foundClientCall = true;
//			}
//		}
//		assertTrue("No client call recorded", foundClientCall);
//		assertTrue("No service call recorded", foundServiceCall);
//	}
	
}
