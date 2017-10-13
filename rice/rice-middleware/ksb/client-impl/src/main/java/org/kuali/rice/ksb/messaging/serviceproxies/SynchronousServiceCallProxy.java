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

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderProxy;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.messaging.AsynchronousCallback;
import org.kuali.rice.ksb.messaging.MessageServiceInvoker;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.util.KSBConstants;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.List;


/**
 * Used to Call a service synchronously but through the messaging code within workflow. Used to when switching generally
 * asynchronously called services to synchronously called services. Generally for testing purposes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class SynchronousServiceCallProxy extends AsynchronousServiceCallProxy {

    private SynchronousServiceCallProxy(List<Endpoint> endpoints, AsynchronousCallback callback,
	    Serializable context, String value1, String value2) {
	super(endpoints, callback, context, value1, value2);
    }

    public static Object createInstance(List<Endpoint> endpoints, AsynchronousCallback callback,
	    Serializable context, String value1, String value2) {
	if (endpoints == null || endpoints.isEmpty()) {
	    throw new RuntimeException("Cannot create service proxy, no service(s) passed in.");
	}
	try {
	return Proxy.newProxyInstance(ClassLoaderUtils.getDefaultClassLoader(), ContextClassLoaderProxy
		.getInterfacesToProxy(endpoints.get(0).getService()), new SynchronousServiceCallProxy(
				endpoints, callback, context, value1, value2));
	} catch (Exception e) {
	    throw new RiceRuntimeException(e);
    }
    }

    @Override
    protected void executeMessage(PersistedMessageBO message) {
	if (!Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGING_OFF))) {
	    new MessageServiceInvoker(message).run();
	}
    }
}
