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
package org.kuali.rice.ksb.messaging.serviceconnectors;

import org.apache.commons.collections.MapUtils;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.ksb.api.bus.support.RestServiceConfiguration;
import org.kuali.rice.ksb.api.messaging.ResourceFacade;
import org.kuali.rice.ksb.messaging.BusClientFailureProxy;
import org.kuali.rice.ksb.messaging.bam.BAMClientProxy;
import org.kuali.rice.ksb.security.soap.CredentialsOutHandler;
import org.kuali.rice.ksb.service.KSBServiceLocator;

import java.net.URL;
import java.util.Map;

/**
 * implementation of {@link ResourceFacade}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ResourceFacadeImpl implements ResourceFacade {

	private static final Logger LOG = Logger.getLogger(ResourceFacadeImpl.class);

	private final RestServiceConfiguration serviceConfiguration;
	private CredentialsSource credentialsSource;
	private URL actualEndpointUrl;

	public ResourceFacadeImpl(final RestServiceConfiguration serviceConfiguration, URL actualEndpointUrl) {
		if (serviceConfiguration == null) {
			throw new IllegalArgumentException("serviceConfiguration cannot be null");
		}
		if (actualEndpointUrl == null) {
			throw new IllegalArgumentException("actual endpoint url cannot be null");
		}
		this.serviceConfiguration = serviceConfiguration;
		this.actualEndpointUrl = actualEndpointUrl;
	}
	
	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.ksb.api.messaging.ResourceFacade#getResource(java.lang.Class)
	 */
	public <R> R getResource(Class<R> resourceClass) {
		if (resourceClass == null) throw new IllegalArgumentException("resourceClass argument must not be null");

		if (!serviceConfiguration.hasClass(resourceClass.getName())) {
			throw new IllegalArgumentException("Service " + serviceConfiguration.getServiceName() +
					" does not contain an implementation of type " + resourceClass.getName());
		}

		return getServiceProxy(resourceClass);
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.ksb.api.messaging.ResourceFacade#getResource(java.lang.String)
	 */
	public <R> R getResource(String resourceName) {

		String resourceClassName = null;

		Map<String, String> resourceToClassNameMap = serviceConfiguration.getResourceToClassNameMap();

		if (resourceName != null && resourceToClassNameMap != null)
			resourceClassName = resourceToClassNameMap.get(resourceName);
		else
			resourceClassName = serviceConfiguration.getResourceClass();

		if (resourceClassName == null)
			throw new RiceRuntimeException("No resource class name was found for the specified resourceName: " + resourceName);

		Class<?> resourceClass = null;

		try {
			resourceClass = Class.forName(resourceClassName);
		} catch (ClassNotFoundException e) {
			throw new RiceRuntimeException("Configured resource class " + resourceClassName +
					" in service " + serviceConfiguration.getServiceName() + " is not loadable", e);
		}

		
		// allow this to class cast if the types don't match, up to the caller to ensure this is correct
		@SuppressWarnings("unchecked")
        R serviceProxy = (R)getServiceProxy(resourceClass);
		return serviceProxy;
	}

	/**
	 * This method ...
	 *
	 * @param resourceClass
	 * @return
	 */
	private <R> R getServiceProxy(Class<R> resourceClass) {
		JAXRSClientFactoryBean clientFactory;

        clientFactory = new JAXRSClientFactoryBean();
        clientFactory.setBus(KSBServiceLocator.getCXFBus());

        clientFactory.setResourceClass(resourceClass);
        clientFactory.setAddress(actualEndpointUrl.toString());
        BindingFactoryManager bindingFactoryManager = KSBServiceLocator.getCXFBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory bindingFactory = new JAXRSBindingFactory();
        bindingFactory.setBus(KSBServiceLocator.getCXFBus());

        bindingFactoryManager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, bindingFactory);

        //Set logging interceptors
        if (LOG.isDebugEnabled()) {
        	clientFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        if (getCredentialsSource() != null) {
            clientFactory.getOutInterceptors().add(new CredentialsOutHandler(getCredentialsSource(), serviceConfiguration));
        }

        if (LOG.isDebugEnabled()) {
        	clientFactory.getInInterceptors().add(new LoggingInInterceptor());
        }

        Object service = clientFactory.create();
        return getServiceProxyWithFailureMode(resourceClass, service, serviceConfiguration);
	}
	
	public boolean isSingleResourceService() {
		return MapUtils.isEmpty(serviceConfiguration.getResourceToClassNameMap());
	}

	public void setCredentialsSource(final CredentialsSource credentialsSource) {
		this.credentialsSource = credentialsSource;
	}

	protected CredentialsSource getCredentialsSource() {
		return this.credentialsSource;
	}

	protected <R> R getServiceProxyWithFailureMode(final Class<R> resourceClass, final Object service, final RestServiceConfiguration serviceConfiguration) {
		Object bamWrappedClientProxy = BAMClientProxy.wrap(service, serviceConfiguration);
		Object proxy = BusClientFailureProxy.wrap(bamWrappedClientProxy, serviceConfiguration);
		if (!resourceClass.isInstance(proxy)) {
			throw new IllegalArgumentException("Wrapped proxy is of the wrong type " + proxy.getClass() + ", expected " + resourceClass);
		}
		return resourceClass.cast(proxy);
	}

}
