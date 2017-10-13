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
package org.kuali.rice.ksb.messaging.serviceproxies;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.core.api.util.reflect.BaseInvocationHandler;
import org.kuali.rice.core.api.util.reflect.TargetedInvocationHandler;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.api.messaging.AsynchronousCallback;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Standard default proxy used to call services asynchronously. Persists the method call to the db so call is never
 * lost and only sent when transaction is committed.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AsynchronousServiceCallProxy extends BaseInvocationHandler implements TargetedInvocationHandler {

    private static final Logger LOG = Logger.getLogger(AsynchronousServiceCallProxy.class);

    private AsynchronousCallback callback;

    private List<Endpoint> endpoints;

    private Serializable context;

    private String value1;

    private String value2;

    protected AsynchronousServiceCallProxy(List<Endpoint> endpoints, AsynchronousCallback callback,
            Serializable context, String value1, String value2) {
        this.endpoints = endpoints;
        this.callback = callback;
        this.context = context;
        this.value1 = value1;
        this.value2 = value2;
    }

    public static Object createInstance(List<Endpoint> endpoints, AsynchronousCallback callback, Serializable context,
            String value1, String value2) {
        if (endpoints == null || endpoints.isEmpty()) {
            throw new RuntimeException("Cannot create service proxy, no service(s) passed in.");
        }
        try {
            return Proxy.newProxyInstance(ClassLoaderUtils.getDefaultClassLoader(),
                    ContextClassLoaderProxy.getInterfacesToProxy(endpoints.get(0).getService()),
                    new AsynchronousServiceCallProxy(endpoints, callback, context, value1, value2));
        } catch (Exception e) {
            throw new RiceRuntimeException(e);
        }
    }

    @Override
    protected Object invokeInternal(Object proxy, Method method, Object[] arguments) throws Throwable {

        if (LOG.isDebugEnabled()) {
            LOG.debug("creating messages for method invocation: " + method.getName());
        }
        // there are multiple service calls to make in the case of topics.
        AsynchronousCall methodCall = null;
        PersistedMessageBO message = null;
        synchronized (this) {
            // consider moving all this topic invocation stuff to the service
            // invoker for speed reasons
            for (Endpoint endpoint : this.endpoints) {
                ServiceConfiguration serviceConfiguration = endpoint.getServiceConfiguration();
                methodCall = new AsynchronousCall(method.getParameterTypes(), arguments, serviceConfiguration,
                        method.getName(), this.callback, this.context);
                message = PersistedMessageBO.buildMessage(serviceConfiguration, methodCall);
                message.setValue1(this.value1);
                message.setValue2(this.value2);
                saveMessage(message);
                executeMessage(message);
                // only do one iteration if this is a queue. The load balancing
                // will be handled when the service is
                // fetched by the MessageServiceInvoker through the GRL (and
                // then through the RemoteResourceServiceLocatorImpl)
                if (serviceConfiguration.isQueue()) {
                    break;
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("finished creating messages for method invocation: " + method.getName());
        }
        return null;
    }

    @Override
    protected String proxyToString(Object proxy) {
        StringBuilder builder = new StringBuilder();
        builder.append("Service call proxy (" + getClass().getName() + ") - for endpoints with service name: ");
        if (CollectionUtils.isNotEmpty(this.endpoints)) {
            builder.append(this.endpoints.get(0).getServiceConfiguration().getServiceName().toString());
        } else {
            builder.append("<< no endpoints on this proxy!!! >>");
        }
        return builder.toString();
    }

    protected void saveMessage(PersistedMessageBO message) {
        message.setQueueStatus(KSBConstants.ROUTE_QUEUE_ROUTING);
        KSBServiceLocator.getMessageQueueService().save(message);
    }

    protected void executeMessage(PersistedMessageBO message) throws Exception {
        MessageSender.sendMessage(message);
    }

    /**
     * Returns the List<RemotedServiceHolder> of asynchronous services which will be invoked by calls to this proxy.
     * This is a List because, in the case of Topics, there can be more than one service invoked.
     */
    public Object getTarget() {
        return this.endpoints;
    }

    public AsynchronousCallback getCallback() {
        return this.callback;
    }

    public void setCallback(AsynchronousCallback callback) {
        this.callback = callback;
    }

}
