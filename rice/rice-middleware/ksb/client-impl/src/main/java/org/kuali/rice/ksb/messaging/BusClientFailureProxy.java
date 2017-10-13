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

import java.io.InterruptedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.core.api.util.reflect.BaseTargetedInvocationHandler;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;



public class BusClientFailureProxy extends BaseTargetedInvocationHandler {

	private static final Logger LOG = Logger.getLogger(BusClientFailureProxy.class);

	private final Object failoverLock = new Object();
	
	private ServiceConfiguration serviceConfiguration;

	// exceptions that will cause this Proxy to remove the service from the bus
	private static List<Class<?>> serviceRemovalExceptions = new ArrayList<Class<?>>();
	private static List<Integer> serviceRemovalResponseCodes = new ArrayList<Integer>();

	static {
		serviceRemovalExceptions.add(NoHttpResponseException.class);
		serviceRemovalExceptions.add(InterruptedIOException.class);
		serviceRemovalExceptions.add(UnknownHostException.class);
		serviceRemovalExceptions.add(NoRouteToHostException.class);
		serviceRemovalExceptions.add(ConnectTimeoutException.class);
		serviceRemovalExceptions.add(ConnectionPoolTimeoutException.class);
		serviceRemovalExceptions.add(ConnectException.class);
        serviceRemovalExceptions.add(SocketTimeoutException.class);
    }
	
	static {
	    serviceRemovalResponseCodes.add(new Integer(404));
        serviceRemovalResponseCodes.add(new Integer(503));
	}
	
	private BusClientFailureProxy(Object target, ServiceConfiguration serviceConfiguration) {
		super(target);
		this.serviceConfiguration = serviceConfiguration;
	}

	public static Object wrap(Object target, ServiceConfiguration serviceConfiguration) {
		return Proxy.newProxyInstance(ClassLoaderUtils.getDefaultClassLoader(), ContextClassLoaderProxy.getInterfacesToProxy(target), new BusClientFailureProxy(target, serviceConfiguration));
	}

	protected Object invokeInternal(Object proxyObject, Method method, Object[] params) throws Throwable {
		Set<ServiceConfiguration> servicesTried = null;
		
		do {
			try {
				return method.invoke(getTarget(), params);
			} catch (Throwable throwable) {			
				if (isServiceRemovalException(throwable)) {
					synchronized (failoverLock) {
                        LOG.error("Exception caught accessing remote service " + this.serviceConfiguration.getServiceName() + " at " + this.serviceConfiguration.getEndpointUrl(), throwable);
                        if (servicesTried == null) {
							servicesTried = new HashSet<ServiceConfiguration>();
							servicesTried.add(serviceConfiguration);
						}
						Object failoverService = null;
						List<Endpoint> endpoints = KsbApiServiceLocator.getServiceBus().getEndpoints(serviceConfiguration.getServiceName(), serviceConfiguration.getApplicationId());
						for (Endpoint endpoint : endpoints) {
							if (!servicesTried.contains(endpoint.getServiceConfiguration())) {
								failoverService = endpoint.getService();
                                if(Proxy.isProxyClass(failoverService.getClass()) && Proxy.getInvocationHandler(failoverService) instanceof BusClientFailureProxy) {
                                    failoverService = ((BusClientFailureProxy)Proxy.getInvocationHandler(failoverService)).getTarget();
                                }
								servicesTried.add(endpoint.getServiceConfiguration());
                                break; // KULRICE-8728: BusClientFailureProxy doesn't try all endpoint options
							}
						}									
						if (failoverService != null) {
                            LOG.info("Refetched replacement service for service " + this.serviceConfiguration.getServiceName() + " at " + this.serviceConfiguration.getEndpointUrl());
                            // as per KULRICE-4287, reassign target to the new service we just fetched, hopefully this one works better!
							setTarget(failoverService);
						} else {
							LOG.error("Didn't find replacement service throwing exception");
							throw throwable;					
						}
					}
				} else {
					throw throwable;
				}
			}
		} while (true);
	}

	private static boolean isServiceRemovalException(Throwable throwable) {
		LOG.info("Checking for Service Removal Exception: " + throwable.getClass().getName());
		if (serviceRemovalExceptions.contains(throwable.getClass())) {
			LOG.info("Found a Service Removal Exception: " + throwable.getClass().getName());
			return true;
		} else if (throwable instanceof org.kuali.rice.ksb.messaging.HttpException) {
			org.kuali.rice.ksb.messaging.HttpException httpException = (org.kuali.rice.ksb.messaging.HttpException)throwable;
			if (serviceRemovalResponseCodes.contains(httpException.getResponseCode())) {
				LOG.info("Found a Service Removal Exception because of a " + httpException.getResponseCode() + " " + throwable.getClass().getName());
				return true;
			}
		} else if (throwable instanceof org.apache.cxf.transport.http.HTTPException) {
			org.apache.cxf.transport.http.HTTPException httpException = (org.apache.cxf.transport.http.HTTPException)throwable;
			if (serviceRemovalResponseCodes.contains(httpException.getResponseCode())) {
				LOG.info("Found a Service Removal Exception because of a " + httpException.getResponseCode() + " " + throwable.getClass().getName());
				return true;
			}
		}
		if (throwable.getCause() != null) {
			LOG.info("Unwrapping Throwable cause to check for service removal exception from: " + throwable.getClass().getName());
			return isServiceRemovalException(throwable.getCause());
		}
		return false;
	}

}
