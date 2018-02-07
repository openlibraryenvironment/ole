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
package org.kuali.rice.ksb.messaging.serviceconnectors;

import javax.xml.namespace.QName;

import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.bam.BAMClientProxy;


/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BusLocalConnector extends AbstractServiceConnector {

	public BusLocalConnector(final ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
	}

	private Object getServiceProxy(Object service) {
		return BAMClientProxy.wrap(service, getServiceConfiguration());
	}
	
	public Object getService() {
		QName serviceName = getServiceConfiguration().getServiceName();
		Endpoint localServiceEndpoint = KsbApiServiceLocator.getServiceBus().getLocalEndpoint(serviceName);
		if (localServiceEndpoint == null) {
			throw new IllegalStateException("Failed to locate a local service with the name: " + serviceName);
		}
	    return getServiceProxy(KsbApiServiceLocator.getServiceBus().getLocalEndpoint(serviceName));
	}
	
}
