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
package org.kuali.rice.ksb.api;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.messaging.MessageHelper;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;

/**
 * A static service locator which aids in locating the various services that
 * form the Kuali Service Bus API.
 */
public class KsbApiServiceLocator {

	public static final String SERVICE_BUS = "rice.ksb.serviceBus";
	public static final String SERVICE_REGISTRY = "rice.ksb.serviceRegistry";
    public static final String MESSAGE_HELPER = "rice.ksb.messageHelper";

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static ServiceBus getServiceBus() {
        return getService(SERVICE_BUS);
    }
    
    public static ServiceRegistry getServiceRegistry() {
    	return getService(SERVICE_REGISTRY);
    }

    public static MessageHelper getMessageHelper() {
        return getService(MESSAGE_HELPER);
    }
}
