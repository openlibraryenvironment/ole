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
package org.kuali.rice.kew.resourceloader;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.ServiceLocator;
import org.kuali.rice.core.impl.resourceloader.BaseWrappingResourceLoader;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.plugin.PluginRegistry;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * A resource loader which is responsible for loading resources from the Workflow ConfigContext.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CoreResourceLoader extends BaseWrappingResourceLoader {

	public static final QName NAME = new QName(CoreConfigHelper.getApplicationId(), "KEW_SPRING+PLUGIN_REGISTRY_CONTAINER_RESOURCE_LOADER");

	private final PluginRegistry registry;

	public CoreResourceLoader(ServiceLocator serviceLocator, PluginRegistry registry) {
		super(CoreResourceLoader.NAME, serviceLocator);
		this.registry = registry;
	}

	/**
	 * Overrides the standard getService method to looks for the service in the plugin if it can't find it in the core.
	 */
	public Object getService(QName serviceName) {
		if (isRemoteService(serviceName)) {
			return null;
		}
		Object service = super.getService(serviceName);
		if (service == null && getRegistry() != null) {
		    service = getRegistry().getService(serviceName);
		}
		return service;
	}



	@Override
	public Object getObject(ObjectDefinition objectDefinition) {
	    Object object = super.getObject(objectDefinition);
	    if (object == null && getRegistry() != null) {
		object = getRegistry().getObject(objectDefinition);
	    }
	    return object;
	}

	@Override
	protected boolean shouldWrapService(QName serviceName, Object service) {
		// transaction template is not wrappable because it does not implement an interface
		if (serviceName.getLocalPart().equals("transactionTemplate")) {
			return false;
		}
		return super.shouldWrapService(serviceName, service);
	}
	
	
	
	@Override
	public void stop() throws Exception {
		if (getRegistry() != null) {
			registry.stop();
		}
		super.stop();
	}

	/**
	 * Returns true if the given service name is one which should be loaded from the service bus.  This is used
	 * primarily for embedded clients that want to reference the workgroup and user services on a standalone
	 * server.
	 */
	protected boolean isRemoteService(QName serviceName) {
	    return (useRemoteEmailServices() &&
			    serviceName.getLocalPart().equals(KewApiServiceLocator.IMMEDIATE_EMAIL_REMINDER_QUEUE.getLocalPart()));
	}
		
	protected boolean useRemoteEmailServices() {
	    String useRemoteEmailServicesValue = ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.USE_REMOTE_EMAIL_SERVICES);
	    if (!StringUtils.isBlank(useRemoteEmailServicesValue)) {
	        return new Boolean(useRemoteEmailServicesValue.trim());
	    }
	    return false;
	}

	public PluginRegistry getRegistry() {
		return registry;
	}
	
}
