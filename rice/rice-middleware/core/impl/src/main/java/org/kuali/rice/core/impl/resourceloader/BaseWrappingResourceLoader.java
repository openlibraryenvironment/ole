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
package org.kuali.rice.core.impl.resourceloader;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.ServiceLocator;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;

import javax.xml.namespace.QName;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A BaseResourceLoader implementation which wraps services with a Proxy that
 * switches the current context ClassLoader of the Thread.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BaseWrappingResourceLoader extends BaseResourceLoader {

	private static final String[] PACKAGES_TO_FILTER = new String[] { "org.springframework" };
	private Set<QName> servicesToCache = new HashSet<QName>();
	private final ConcurrentHashMap<QName, Object> serviceCache = new ConcurrentHashMap<QName, Object>();

	public BaseWrappingResourceLoader(QName name, ClassLoader classLoader, ServiceLocator serviceLocator) {
		super(name, classLoader, serviceLocator);
	}

	public BaseWrappingResourceLoader(QName name, ClassLoader classLoader) {
		super(name, classLoader);
	}

	public BaseWrappingResourceLoader(QName name, ServiceLocator serviceLocator) {
		super(name, serviceLocator);
	}

	public BaseWrappingResourceLoader(QName name) {
		super(name);
	}



	@Override
	public void start() throws Exception {
	    String servicesToCacheFromConfig = ConfigContext.getCurrentContextConfig().getProperty(RiceConstants.SERVICES_TO_CACHE);
	    if (!StringUtils.isEmpty(servicesToCacheFromConfig)) {
		String[] services = servicesToCacheFromConfig.split(",");
		for (String serviceName : services) {
		    serviceName = serviceName.trim();
		    try {
			servicesToCache.add(QName.valueOf(serviceName));
			LOG.info("Adding service " + serviceName + " to service cache.");
		    } catch (IllegalArgumentException e) {
			LOG.error("Failed to parse serviceName into QName from property " + RiceConstants.SERVICES_TO_CACHE +".  Service name given was: " + serviceName);
		    }
		}
	    }
	    super.start();
	}

	@Override
	public Object getService(QName serviceName) {
	    Object service = serviceCache.get(serviceName);
	    if (service != null) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Service with QName " + serviceName + " was retrieved from the service cache.");
		}
		return service;
	    }
	    return super.getService(serviceName);
	}

	protected Object postProcessService(QName serviceName, Object service) {
		if (service != null && shouldWrapService(serviceName, service)) {
			service = ContextClassLoaderProxy.wrap(service, ClassLoaderUtils.getInterfacesToProxy(service, getClassLoader(), getPackageNamesToFilter()), getClassLoader());
		}
		cacheService(serviceName, service);
		return service;
	}

	protected Object postProcessObject(ObjectDefinition definition, Object object) {
		if (object != null && shouldWrapObject(definition, object)) {
			return ContextClassLoaderProxy.wrap(object, ClassLoaderUtils.getInterfacesToProxy(object, getClassLoader(), getPackageNamesToFilter()), getClassLoader(), getClassLoader());
		}
		return object;
	}

	protected void cacheService(QName serviceName, Object service) {
	    if (shouldCacheService(serviceName, service)) {
		LOG.debug("Adding service " + serviceName + " to the service cache.");
		    serviceCache.put(serviceName, service);
	    }
	}

	protected String[] getPackageNamesToFilter() {
		return PACKAGES_TO_FILTER;
	}

	protected boolean shouldWrapService(QName serviceName, Object service) {
		return true;
	}

	protected boolean shouldCacheService(QName serviceName, Object service) {
	    return servicesToCache.contains(serviceName) && service != null;
	}

	protected boolean shouldWrapObject(ObjectDefinition definition, Object object) {
		return true;
	}

}
