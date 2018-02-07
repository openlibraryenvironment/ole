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

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.registry.ServiceDescriptor;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;

public final class RemoteService {
	
	private final ServiceInfo serviceInfo;
	private final ServiceRegistry serviceRegistry;
	
	private final Object endpointAcquisitionLock = new Object();
	private volatile Endpoint endpoint;
	
	public RemoteService(ServiceInfo serviceInfo, ServiceRegistry serviceRegistry) {
		validateServiceInfo(serviceInfo);
		if (serviceRegistry == null) {
			throw new IllegalArgumentException("serviceRegistry cannot be null");
		}
		this.serviceInfo = serviceInfo;
		this.serviceRegistry = serviceRegistry;
	}
	
	private static void validateServiceInfo(ServiceInfo serviceInfo) {
		if (serviceInfo == null) {
			throw new IllegalArgumentException("serviceInfo cannot be null");
		}
		if (serviceInfo.getServiceId() == null) {
			throw new IllegalArgumentException("serviceInfo must have a serviceId but was null");
		}
	}
	
	public QName getServiceName() {
		return serviceInfo.getServiceName();
	}
		
	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	
	public Endpoint getEndpoint() {
		// double-checked locking idiom - see Effective Java, Item 71
		Endpoint internalEndpoint = this.endpoint;
		if (internalEndpoint == null) {
			synchronized (endpointAcquisitionLock) {
				internalEndpoint = this.endpoint;
				if (internalEndpoint == null) {
					this.endpoint = internalEndpoint = new LazyEndpoint(constructServiceConfiguration()); 
				}
			}
		}
		return internalEndpoint;
	}
	
	protected ServiceConfiguration constructServiceConfiguration() {
		ServiceDescriptor serviceDescriptor = serviceRegistry.getServiceDescriptor(serviceInfo.getServiceDescriptorId());
		if (serviceDescriptor == null) {
			throw new IllegalStateException("Failed to locate ServiceDescriptor for ServiceInfo with serviceDescriptorId=" + serviceInfo.getServiceDescriptorId());
		} else if (StringUtils.isBlank(serviceDescriptor.getDescriptor())) {
			throw new IllegalStateException("ServiceDescriptor descriptor value is blank or null for descriptor with serviceEndpointId=" + serviceInfo.getServiceId());
		}
		return ServiceConfigurationSerializationHandler.unmarshallFromXml(serviceDescriptor.getDescriptor());
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof RemoteService)) {
			return false;
		}
		return serviceInfo.equals(((RemoteService)obj).getServiceInfo());
    }

	@Override
    public int hashCode() {
        return serviceInfo.hashCode();
    }
	
}