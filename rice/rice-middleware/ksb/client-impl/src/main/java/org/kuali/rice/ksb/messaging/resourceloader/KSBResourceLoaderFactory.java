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
package org.kuali.rice.ksb.messaging.resourceloader;

import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.ServiceBus;

import javax.xml.namespace.QName;



/**
 * Creates KSBs root resource loader with all the correct children attached ready for starting.
 * Uses config object to store QNames so everything is good with the current context classloader.
 *
 * Can grab any KSB specific resource loader from the resource loading stack.
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KSBResourceLoaderFactory {
	
	private static final String KSB_ROOT_RESOURCE_LOACER_NAME = "KSB_ROOT_RESOURCE_LOADER";
	private static final String KSB_REMOTE_RESOURCE_LOADER_LOCAL_NAME = "KSB_REMOTE_RESOURCE_LOADER";

	private static void initialize() {
		String applicationId = CoreConfigHelper.getApplicationId();
		if (getRootResourceLoaderName() == null) {
			setRootResourceLoaderName(new QName(applicationId, KSB_ROOT_RESOURCE_LOACER_NAME));
		}
		if (getRemoteResourceLoaderName() == null) {
			setRemoteResourceLoaderName(new QName(applicationId, KSB_REMOTE_RESOURCE_LOADER_LOCAL_NAME));
		}
	}

	public static ResourceLoader createRootKSBRemoteResourceLoader() {
		initialize();
		ResourceLoader rootResourceLoader = new BaseResourceLoader(getRootResourceLoaderName());
		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
		if (serviceBus == null) {
			throw new IllegalStateException("Failed to locate the ServiceBus");
		}
		rootResourceLoader.addResourceLoader(new ServiceBusResourceLoader(getRemoteResourceLoaderName(), serviceBus));
		return rootResourceLoader;
	}

	public static QName getRootResourceLoaderName() {
		return (QName)ConfigContext.getCurrentContextConfig().getObject(KSB_ROOT_RESOURCE_LOACER_NAME);
	}

	public static void setRootResourceLoaderName(QName name) {
		ConfigContext.getCurrentContextConfig().putObject(KSB_ROOT_RESOURCE_LOACER_NAME, name);
	}

	public static QName getRemoteResourceLoaderName() {
		return (QName)ConfigContext.getCurrentContextConfig().getObject(KSB_REMOTE_RESOURCE_LOADER_LOCAL_NAME);
	}

	public static void setRemoteResourceLoaderName(QName remoteResourceLoaderName) {
		ConfigContext.getCurrentContextConfig().putObject(KSB_REMOTE_RESOURCE_LOADER_LOCAL_NAME, remoteResourceLoaderName);
	}

}
