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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.messaging.AsynchronousCallback;
import org.kuali.rice.ksb.api.messaging.MessageHelper;
import org.kuali.rice.ksb.messaging.serviceproxies.AsynchronousServiceCallProxy;
import org.kuali.rice.ksb.messaging.serviceproxies.DelayedAsynchronousServiceCallProxy;
import org.kuali.rice.ksb.messaging.serviceproxies.SynchronousServiceCallProxy;
import org.kuali.rice.ksb.util.KSBConstants;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MessageHelperImpl implements MessageHelper {

    @Override
    public <T> T getServiceAsynchronously(QName qname) {
        return (T) getServiceAsynchronously(qname, null, null, null, null, null);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, String applicationId) {
        return (T) getServiceAsynchronously(qname, applicationId, null, null, null, null);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback) {
        return (T) getServiceAsynchronously(qname, null, callback, null, null, null);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback, Serializable context) {
        return (T) getServiceAsynchronously(qname, null, callback, context, null, null);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback, Serializable context, String value1, String value2) {
        return (T) getServiceAsynchronously(qname, null, callback, context, value1, value2);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, String applicationId, AsynchronousCallback callback,
            Serializable context, String value1, String value2) {

    	List<Endpoint> endpoints = KsbApiServiceLocator.getServiceBus().getEndpoints(qname);
        endpoints = filterEndpointsByApplicationId(endpoints, applicationId);
    	if (endpoints.isEmpty()) {
    		throw new RuntimeException("Cannot create service proxy, failed to locate any endpoints with the given service name: " + qname + (applicationId != null ? ", and application id: " + applicationId : ""));
    	}
        return (T) createProxy(syncMode(), endpoints, callback, context, value1, value2);
    }

    public <T> T getDelayedAsynchronousServiceCallProxy(QName qname, String applicationId, Serializable context, String value1, String value2, long delayMilliseconds) {
    	List<Endpoint> endpoints = KsbApiServiceLocator.getServiceBus().getEndpoints(qname);
        endpoints = filterEndpointsByApplicationId(endpoints, applicationId);
    	if (endpoints.isEmpty()) {
    		throw new RuntimeException("Cannot create service proxy, failed to locate any endpoints with the given service name: " + qname);
    	}
        return (T) createProxyDelayed(syncMode(), endpoints, context, value1, value2, delayMilliseconds);
    }

    @Override
    public <T> T getServiceAsynchronously(QName qname, Serializable context, String value1, String value2, long delayMilliseconds) {
        return (T) getDelayedAsynchronousServiceCallProxy(qname, null, context, value1, value2, delayMilliseconds);
    }

    public <T> T getServiceAsynchronously(QName qname, String applicationId, Serializable context, String value1, String value2, long delayMilliseconds) {
        return (T) getDelayedAsynchronousServiceCallProxy(qname, applicationId, context, value1, value2, delayMilliseconds);
    }

    @Override
    public <T> List<T> getAllRemoteServicesAsynchronously(QName qname) {
        List<Endpoint> endpoints = KsbApiServiceLocator.getServiceBus().getRemoteEndpoints(qname);
    	if (endpoints.isEmpty()) {
    		throw new RuntimeException("Cannot create service proxy, failed to locate any endpoints with the given service name: " + qname);
    	}
        final List<T> proxies = new ArrayList<T>();
        final boolean syncMode = syncMode();
        final String instanceId = KsbApiServiceLocator.getServiceBus().getInstanceId();
        for (Endpoint e : endpoints) {
            if (!e.getServiceConfiguration().getInstanceId().equals(instanceId)) {
                proxies.add(MessageHelperImpl.<T>createProxy(syncMode, endpoints, null, null, null, null));
            }
        }
        return proxies;
    }

    public static <T> T createProxy(boolean sync, List<Endpoint> endpoints, AsynchronousCallback callback, Serializable context, String value1, String value2) {
        return sync ? (T) SynchronousServiceCallProxy.createInstance(endpoints, callback, context, value1, value2)
                : (T) AsynchronousServiceCallProxy.createInstance(endpoints, callback, context, value1, value2);
    }

    public static <T> T createProxyDelayed(boolean sync, List<Endpoint> endpoints,Serializable context, String value1, String value2, long delayMilliseconds) {
        return sync ? (T) SynchronousServiceCallProxy.createInstance(endpoints, null, context, value1, value2)
                : (T) DelayedAsynchronousServiceCallProxy.createInstance(endpoints, context, value1, value2, delayMilliseconds);
    }

    private static boolean syncMode() {
        return KSBConstants.MESSAGING_SYNCHRONOUS.equals(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGE_DELIVERY));
    }

    private List<Endpoint> filterEndpointsByApplicationId(List<Endpoint> endpoints, String applicationId) {
        if (StringUtils.isBlank(applicationId)) {
            return endpoints;
        }
        List<Endpoint> filteredEndpoints = new ArrayList<Endpoint>();
        for (Endpoint endpoint : endpoints) {
            if (endpoint.getServiceConfiguration().getApplicationId().equals(applicationId)) {
                filteredEndpoints.add(endpoint);
            }
        }
        return filteredEndpoints;
    }

}
