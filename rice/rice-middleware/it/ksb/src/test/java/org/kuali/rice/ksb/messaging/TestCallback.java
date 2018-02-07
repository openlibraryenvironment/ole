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

import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.api.messaging.AsynchronousCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class TestCallback implements AsynchronousCallback {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8558495197148582373L;

	public static boolean callbackCalled;

	public static List<Long> CURRENT_MILLIS_WHEN_CALLED;

	public static int NUMBER_CALL_BACKS = 0;

	public static Map<QName, Integer> SERVICE_CALL_COUNT_TRACKED = new HashMap<QName, Integer>();
	public static Map<QName, List<AsynchronousCall>> SERVICES_CALLS_TRACKED = new HashMap<QName, List<AsynchronousCall>>();

	static {
		CURRENT_MILLIS_WHEN_CALLED = new ArrayList<Long>();
	}

	private int numberCallbacks = 0;

	public static boolean isCallbackCalled() {
		return callbackCalled;
	}

	public static void setCallbackCalled(boolean callbackCalled) {
		TestCallback.callbackCalled = callbackCalled;
	}

	public synchronized void callback(Serializable returnObject, AsynchronousCall methodCall) {
		CURRENT_MILLIS_WHEN_CALLED.add(System.currentTimeMillis());
		NUMBER_CALL_BACKS++;
		setCallbackCalled(true);
		this.numberCallbacks++;
		QName serviceName = methodCall.getServiceConfiguration().getServiceName();
		Integer callCount = SERVICE_CALL_COUNT_TRACKED.get(serviceName);
		if (callCount == null) {
			SERVICE_CALL_COUNT_TRACKED.put(methodCall.getServiceConfiguration().getServiceName(), 1);
		} else {
			SERVICE_CALL_COUNT_TRACKED.put(methodCall.getServiceConfiguration().getServiceName(), callCount + 1);
		}
		
		List<AsynchronousCall> serviceCallsTracked = SERVICES_CALLS_TRACKED.get(serviceName);
		if (serviceCallsTracked == null) {
			serviceCallsTracked = new ArrayList<AsynchronousCall>();
			SERVICES_CALLS_TRACKED.put(serviceName, serviceCallsTracked);
		}
		serviceCallsTracked.add(methodCall);
		
		System.out.println("!!!Callback called number callbacks " + this.numberCallbacks);
	}

	public static void clearCallbacks() {
		NUMBER_CALL_BACKS = 0;
		SERVICE_CALL_COUNT_TRACKED = new HashMap<QName, Integer>();
	}

	/**
	 * sometimes it's more convenient to use a non static counter above when
	 * doing in memory queueing. Other times when doing persistent async
	 * messaging a static counter is needed. Everything could be converted to
	 * this method if the tests using the above method clear the static count
	 * before putting testing against callbacks.
	 * 
	 * @param callbacks
	 * @param millisDelay
	 */
	public void pauseUntilNumberCallbacksUsingStaticCounter(int callbacks, QName serviceName) {
		int numPauses = 0;
		while (true) {
			synchronized (this.getClass()) {
				if (serviceName == null) {
					if (NUMBER_CALL_BACKS >= callbacks) {
						System.out.println("!!!Returning number callback met");
						return;
					}
				} else {
					if (SERVICE_CALL_COUNT_TRACKED.get(serviceName) != null && SERVICE_CALL_COUNT_TRACKED.get(serviceName) >= callbacks) {
						System.out.println("!!!Returning number callback met for service " + serviceName);
						return;
					}
						// attributes will not be an exact match.
					for (Map.Entry<QName, Integer> serviceCall : SERVICE_CALL_COUNT_TRACKED.entrySet()) {
						if (serviceCall.getKey().getLocalPart().lastIndexOf(serviceName.getLocalPart()) > -1 && serviceCall.getKey().getNamespaceURI().equals(serviceName.getNamespaceURI()) && serviceCall.getValue() >= callbacks) {
							System.out.println("!!!Returning number callback met for service " + serviceName);
							return;
						}
					}
				}
			}
			if (numPauses > 60 * 1) {
				return;
			}
			try {
				numPauses++;
				System.out.println("!!!Test callback pausing for 1 second");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			    // nothing to do
			}
		}
	}
}
