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
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.core.api.util.reflect.BaseTargetedInvocationHandler;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.messaging.bam.service.BAMService;
import org.kuali.rice.ksb.service.KSBServiceLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * A service-side proxy for that records an entry in the BAM for invocations
 * on the proxied service endpoint.
 *
 * @see BAMService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class BAMServerProxy extends BaseTargetedInvocationHandler {

	private ServiceDefinition serviceDefinition;
	
	private BAMServerProxy(Object target, ServiceDefinition serviceDefinition) {
		super(target);
		this.serviceDefinition = serviceDefinition;
	}
	
	public static boolean isBamSupported() {
		return KSBServiceLocator.getBAMService() != null && Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(Config.BAM_ENABLED));
	}
	
	public static Object wrap(Object target, ServiceDefinition serviceDefinition) {
		if (!isBamSupported()) {
			return target;
		}
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), ContextClassLoaderProxy.getInterfacesToProxy(
                target), new BAMServerProxy(target, serviceDefinition));
	}
	
	protected Object invokeInternal(Object proxiedObject, Method method, Object[] arguments) throws Throwable {
		BAMTargetEntry bamTargetEntry = KSBServiceLocator.getBAMService().recordServerInvocation(getTarget(), this.serviceDefinition, method, arguments);
		try {
			return method.invoke(getTarget(), arguments);	
		} catch (Throwable throwable) {
			if (throwable instanceof InvocationTargetException) {
				throwable = throwable.getCause();
			}
			KSBServiceLocator.getBAMService().recordServerInvocationError(throwable, bamTargetEntry);
			throw throwable;
		}
	}
}
