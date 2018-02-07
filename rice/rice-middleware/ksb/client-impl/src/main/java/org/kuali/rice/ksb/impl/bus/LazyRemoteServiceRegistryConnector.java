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

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.ksb.api.registry.RemoveAndPublishResult;
import org.kuali.rice.ksb.api.registry.ServiceDescriptor;
import org.kuali.rice.ksb.api.registry.ServiceEndpoint;
import org.kuali.rice.ksb.api.registry.ServiceEndpointStatus;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;
import org.kuali.rice.ksb.impl.cxf.interceptors.ImmutableCollectionsInInterceptor;
import org.kuali.rice.ksb.security.soap.CXFWSS4JInInterceptor;
import org.kuali.rice.ksb.security.soap.CXFWSS4JOutInterceptor;
import org.kuali.rice.ksb.util.KSBConstants;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * TODO... 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LazyRemoteServiceRegistryConnector implements ServiceRegistry {

    private static final String SERVICE_REGISTRY_SECURITY_CONFIG = "rice.ksb.serviceRegistry.security";

	private final Object initLock = new Object();
	private volatile ServiceRegistry delegate;
	
	// injected
	private Bus cxfBus;
	
	public void setCxfBus(Bus cxfBus) {
		this.cxfBus = cxfBus;
	}
	
	@Override
	public List<ServiceInfo> getOnlineServicesByName(QName serviceName)
			throws RiceIllegalArgumentException {
		return getDelegate().getOnlineServicesByName(serviceName);
	}

	@Override
	public List<ServiceInfo> getAllOnlineServices() {
		return getDelegate().getAllOnlineServices();
	}

	@Override
	public List<ServiceInfo> getAllServices() {
		return getDelegate().getAllServices();
	}
	
	@Override
	public List<ServiceInfo> getAllServicesForInstance(String instanceId) {
		return getDelegate().getAllServicesForInstance(instanceId);
	}

    @Override
    public List<ServiceInfo> getAllServicesForApplication(String applicationId) {
        return getDelegate().getAllServicesForApplication(applicationId);
    }

	@Override
	public ServiceDescriptor getServiceDescriptor(String serviceDescriptorId)
			throws RiceIllegalArgumentException {
		return getDelegate().getServiceDescriptor(serviceDescriptorId);
	}

	@Override
	public List<ServiceDescriptor> getServiceDescriptors(
			List<String> serviceDescriptorIds)
			throws RiceIllegalArgumentException {
		return getDelegate().getServiceDescriptors(serviceDescriptorIds);
	}

	@Override
	public ServiceEndpoint publishService(ServiceEndpoint serviceEndpoint)
			throws RiceIllegalArgumentException {
		return getDelegate().publishService(serviceEndpoint);
	}

	@Override
	public List<ServiceEndpoint> publishServices(List<ServiceEndpoint> serviceEndpoints)
			throws RiceIllegalArgumentException {
		return getDelegate().publishServices(serviceEndpoints);
	}

	@Override
	public ServiceEndpoint removeServiceEndpoint(String serviceId)
			throws RiceIllegalArgumentException {
		return getDelegate().removeServiceEndpoint(serviceId);
	}

	@Override
	public List<ServiceEndpoint> removeServiceEndpoints(List<String> serviceIds)
			throws RiceIllegalArgumentException {
		return getDelegate().removeServiceEndpoints(serviceIds);
	}

	@Override
	public RemoveAndPublishResult removeAndPublish(List<String> removeServiceIds,
			List<ServiceEndpoint> publishServiceEndpoints) {
		return getDelegate().removeAndPublish(removeServiceIds, publishServiceEndpoints);
	}

	@Override
	public boolean updateStatus(String serviceId, ServiceEndpointStatus status) throws RiceIllegalArgumentException {
		return getDelegate().updateStatus(serviceId, status);
	}

	@Override
	public List<String> updateStatuses(List<String> serviceIds, ServiceEndpointStatus status) throws RiceIllegalArgumentException {
		return getDelegate().updateStatuses(serviceIds, status);
	}

	@Override
	public void takeInstanceOffline(String instanceId)
			throws RiceIllegalArgumentException {
        // if Service Registry has not been initialized by this point, do not shutdown
        if (this.delegate != null) {
		    getDelegate().takeInstanceOffline(instanceId);
        }
	}
	
	private ServiceRegistry getDelegate() {
		// double-checked locking idiom - see Effective Java, Item 71
		ServiceRegistry internalDelegate = this.delegate;
		if (internalDelegate == null) {
			synchronized (initLock) {
				internalDelegate = this.delegate;
				if (internalDelegate == null) {
					this.delegate = internalDelegate = initializeRemoteServiceRegistry();
				}
			}
		}
		return internalDelegate;
	}
	
	protected ServiceRegistry initializeRemoteServiceRegistry() {
		String registryBootstrapUrl = ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.REGISTRY_SERVICE_URL);
		if (StringUtils.isBlank(registryBootstrapUrl)) {
			throw new RiceRuntimeException("Failed to load registry bootstrap service from url: " + registryBootstrapUrl);
		}
		ClientProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
		clientFactory.setServiceClass(ServiceRegistry.class);
		clientFactory.setBus(cxfBus);
		clientFactory.setAddress(registryBootstrapUrl);

        boolean registrySecurity = ConfigContext.getCurrentContextConfig().getBooleanProperty(SERVICE_REGISTRY_SECURITY_CONFIG, true);

		// Set security interceptors
		clientFactory.getOutInterceptors().add(new CXFWSS4JOutInterceptor(registrySecurity));
		clientFactory.getInInterceptors().add(new CXFWSS4JInInterceptor(registrySecurity));

        //Set transformation interceptors
        clientFactory.getInInterceptors().add(new ImmutableCollectionsInInterceptor());
		
		Object service = clientFactory.create();
		if (!(service instanceof ServiceRegistry)) {
			throw new RiceRuntimeException("Endpoint to service registry at URL '" + registryBootstrapUrl + "' was not an instance of ServiceRegistry, instead was: " + service);
		}
		return (ServiceRegistry)service;
	}
	
}
