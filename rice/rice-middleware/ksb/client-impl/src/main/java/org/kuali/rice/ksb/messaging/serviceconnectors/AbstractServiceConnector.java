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

import java.net.URL;

import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.BusClientFailureProxy;
import org.kuali.rice.ksb.messaging.bam.BAMClientProxy;
import org.springframework.util.Assert;


/**
 * Abstract implementation of the ServiceConnector that holds the ServiceInfo
 * and the CredentialsSource as well as providing convenience proxy methods.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 * 
 */
public abstract class AbstractServiceConnector implements ServiceConnector {

	/**
	 * Maintains the information about the service.  This should never be null.
	 */
	private ServiceConfiguration serviceConfiguration;
	private URL alternateEndpointUrl;

	/**
	 * Maintains the credentials needed by the service.  This may be null.
	 */
	private CredentialsSource credentialsSource;

	public AbstractServiceConnector(final ServiceConfiguration serviceConfiguration) {
		this(serviceConfiguration, null);
	}
	
	public AbstractServiceConnector(final ServiceConfiguration serviceConfiguration, URL alternateEndpointUrl) {
		Assert.notNull(serviceConfiguration, "serviceConfiguration cannot be null");
		this.serviceConfiguration = serviceConfiguration;
		this.alternateEndpointUrl = alternateEndpointUrl;
	}
	
	public URL getActualEndpointUrl() {
		if (alternateEndpointUrl != null) {
            return alternateEndpointUrl;
        }
        return getServiceConfiguration().getEndpointUrl();
	}

	public ServiceConfiguration getServiceConfiguration() {
		return this.serviceConfiguration;
	}

	public void setCredentialsSource(final CredentialsSource credentialsSource) {
		this.credentialsSource = credentialsSource;
	}

	protected CredentialsSource getCredentialsSource() {
		return this.credentialsSource;
	}

	protected Object getServiceProxyWithFailureMode(final Object service,
			final ServiceConfiguration serviceConfiguration) {
		Object bamWrappedClientProxy = BAMClientProxy.wrap(service, serviceConfiguration);
		return BusClientFailureProxy.wrap(bamWrappedClientProxy, serviceConfiguration);
	}
}
