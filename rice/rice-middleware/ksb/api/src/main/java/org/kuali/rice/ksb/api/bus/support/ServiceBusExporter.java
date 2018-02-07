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
package org.kuali.rice.ksb.api.bus.support;

import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.springframework.beans.factory.InitializingBean;

/**
 * Used from Spring to register service definitions from an already configured and started KSB.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceBusExporter implements InitializingBean {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ServiceBusExporter.class);
	
	private ServiceDefinition serviceDefinition;
	private boolean forceSync = false;
	private ServiceBus serviceBus;

	public void afterPropertiesSet() {
		if (getServiceDefinition() == null) {
			throw new IllegalStateException("serviceDefinition must be set");
		}
		getServiceDefinition().validate();
		if ( LOG.isInfoEnabled() ) {
			LOG.info("Attempting to export service with service name '" + getServiceDefinition().getServiceName());
		}
		if (getServiceBus() == null) {
			setServiceBus(autoLocateServiceBus());
		}
		if (getServiceBus() == null) {
			throw new IllegalStateException("serviceBus could not be located and was not set, must not be null");
		}
		getServiceBus().publishService(getServiceDefinition(), forceSync);
	}
	
	protected ServiceBus autoLocateServiceBus() {
		return KsbApiServiceLocator.getServiceBus();
	}

	public ServiceDefinition getServiceDefinition() {
		return this.serviceDefinition;
	}

	public void setServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}
	
	public boolean isForceSync() {
		return this.forceSync;
	}

	public void setForceRefresh(boolean forceSync) {
		this.forceSync = forceSync;
	}

	public ServiceBus getServiceBus() {
		return this.serviceBus;
	}

	public void setServiceBus(ServiceBus serviceBus) {
		this.serviceBus = serviceBus;
	}

}
