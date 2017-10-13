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
package org.kuali.rice.ksb.api.bus.support;

import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

/**
 * A simple immutable implementation of an {@link Endpoint} which simply
 * wraps a {@link ServiceConfiguration} and it's associated service implementation.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class BasicEndpoint implements Endpoint {

	private final ServiceConfiguration serviceConfiguration;
	private final Object service;
	
	private BasicEndpoint(ServiceConfiguration serviceConfiguration, Object service) {
		if (serviceConfiguration == null) {
			throw new IllegalArgumentException("serviceConfiguration must not be null");
		}
		if (service == null) {
			throw new IllegalArgumentException("service must not be null");
		}
		this.serviceConfiguration = serviceConfiguration;
		this.service = service;
	}
	
	/**
	 * Constructs a new basic endpoint from the given service configuration and
	 * service instance.
	 * 
	 * @param serviceConfiguration the service configuration to include in this endpoint
	 * @param service the service implementation instance to include in this endpoint
	 * 
	 * @return the constructed {@code BasicEndpoint} which contains the given
	 * configuration and service, will never return null
	 * 
	 * @throws IllegalArgumentException if either serviceConfiguration or service are null
	 */
	public static BasicEndpoint newEndpoint(ServiceConfiguration serviceConfiguration, Object service) {
		return new BasicEndpoint(serviceConfiguration, service);
	}
	
	@Override
	public ServiceConfiguration getServiceConfiguration() {
		return serviceConfiguration;
	}

	@Override
	public Object getService() {
		return service;
	}

}
