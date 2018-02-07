/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.ksb.impl.bus;

import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.serviceconnectors.ServiceConnectorFactory;

public class LazyEndpoint implements Endpoint {

	private final Object lock = new Object();
	
	private final ServiceConfiguration serviceConfiguration;
	private volatile Object service;
	
	public LazyEndpoint(ServiceConfiguration serviceConfiguration) {
		if (serviceConfiguration == null) {
			throw new IllegalArgumentException("serviceConfiguration was null");
		}
		this.serviceConfiguration = serviceConfiguration;
	}
	
	@Override
	public ServiceConfiguration getServiceConfiguration() {
		return this.serviceConfiguration;
	}

	@Override
	public Object getService() {
		// double-checked locking idiom - see Effective Java, Item 71
		Object internalService = this.service;
		if (internalService == null) {
			synchronized (lock) {
				internalService = this.service;
				if (internalService == null) {
					this.service = internalService = ServiceConnectorFactory.getServiceConnector(serviceConfiguration).getService(); 
				}
			}
		}
		return internalService;
	}

}
