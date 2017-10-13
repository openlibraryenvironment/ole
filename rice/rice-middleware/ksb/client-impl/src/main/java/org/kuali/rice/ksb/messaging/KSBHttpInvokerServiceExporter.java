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
package org.kuali.rice.ksb.messaging;

import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.messaging.bam.BAMServerProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocationTraceInterceptor;

import java.util.Arrays;
import java.util.List;


public class KSBHttpInvokerServiceExporter extends HttpInvokerServiceExporter {
	
	private List<Class<?>> serviceInterfaces;
	private ServiceDefinition serviceDefinition;
	private boolean registerTraceInterceptor = true;
	
	public ServiceDefinition getServiceDefinition() {
		return this.serviceDefinition;
	}

	public void setServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}
	
	/**
	 * @see org.springframework.remoting.support.RemoteExporter#setRegisterTraceInterceptor(boolean)
	 */
	@Override
	public void setRegisterTraceInterceptor(boolean registerTraceInterceptor) {
	    // HttpInvokerServiceExporter.registerTraceInterceptor is no longer exposed, capture its value if set
	    this.registerTraceInterceptor = registerTraceInterceptor;
	    super.setRegisterTraceInterceptor(registerTraceInterceptor);
	}

	protected Object getProxyForService() {
		checkService();
		checkServiceInterface();
		ProxyFactory proxyFactory = new ProxyFactory();
		for (Class<?> serviceInterface : getServiceInterfaces()) {
			proxyFactory.addInterface(serviceInterface);
		}
		if (registerTraceInterceptor == true) {
			proxyFactory.addAdvice(new RemoteInvocationTraceInterceptor(getExporterName()));
		}
		ClassLoader classLoader = serviceDefinition.getServiceClassLoader();
		if (classLoader == null) {
		    classLoader = ClassLoaderUtils.getDefaultClassLoader();
		}
		Object service = ContextClassLoaderProxy.wrap(getService(), classLoader);
		service = BAMServerProxy.wrap(service, getServiceDefinition());
		proxyFactory.setTarget(service);
		return proxyFactory.getProxy(classLoader);
	}

	@Override
	protected void checkServiceInterface() throws IllegalArgumentException {
		if (this.serviceInterfaces == null) {
		    this.serviceInterfaces = Arrays.asList(ContextClassLoaderProxy.getInterfacesToProxy(getService()));
		}
		if (getServiceInterfaces().isEmpty()) {
			throw new IllegalArgumentException("At least one service interface should be defined.");
		}
	}
	
	public List<Class<?>> getServiceInterfaces() {
		return this.serviceInterfaces;
	}

	public void setServiceInterfaces(List<Class<?>> serviceInterfaces) {
		this.serviceInterfaces = serviceInterfaces;
	}
	
	public Object getService() {
		return super.getService();
	}
	
}
