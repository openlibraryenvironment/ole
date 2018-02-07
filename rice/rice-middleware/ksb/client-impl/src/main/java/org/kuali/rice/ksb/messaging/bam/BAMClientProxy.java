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
package org.kuali.rice.ksb.messaging.bam;

import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.core.api.util.reflect.BaseTargetedInvocationHandler;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.bam.service.BAMService;
import org.kuali.rice.ksb.service.KSBServiceLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * A client-side proxy for that records an entry in the BAM for invocations
 * on the proxied service.
 *
 * @see BAMService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BAMClientProxy extends BaseTargetedInvocationHandler {

	private ServiceConfiguration serviceConfiguration;
	
	private BAMClientProxy(Object target, ServiceConfiguration serviceConfiguration) {
		super(target);
		this.serviceConfiguration = serviceConfiguration;
	}
	
	public static boolean isBamSupported() {
		return KSBServiceLocator.getBAMService() != null && Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(Config.BAM_ENABLED));
	}
	
	public static Object wrap(Object target, ServiceConfiguration serviceConfiguration) {
		if (!isBamSupported()) {
			return target;
		}
		return Proxy.newProxyInstance(ClassLoaderUtils.getDefaultClassLoader(), ContextClassLoaderProxy.getInterfacesToProxy(target), new BAMClientProxy(target, serviceConfiguration));
	}
	
	protected Object invokeInternal(Object proxyObject, Method method, Object[] arguments) throws Throwable {
		BAMTargetEntry bamTargetEntry = KSBServiceLocator.getBAMService().recordClientInvocation(this.serviceConfiguration, getTarget(), method, arguments);
		try {
			return method.invoke(getTarget(), arguments);	
		} catch (Throwable throwable) {
			if (throwable instanceof InvocationTargetException) {
				throwable = throwable.getCause();
			}
			KSBServiceLocator.getBAMService().recordClientInvocationError(throwable, bamTargetEntry);
			throw throwable;
		}
	}
}
