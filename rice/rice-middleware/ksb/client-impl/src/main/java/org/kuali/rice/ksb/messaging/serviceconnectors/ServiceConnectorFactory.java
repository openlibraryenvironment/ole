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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.core.api.security.credentials.CredentialsSourceFactory;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.JavaServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.RestServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.SoapServiceConfiguration;
import org.kuali.rice.ksb.messaging.AlternateEndpoint;
import org.kuali.rice.ksb.messaging.AlternateEndpointLocation;
import org.kuali.rice.ksb.util.KSBConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Constructs a ServiceConnector based on the provided
 * ServiceInfo/ServiceDefinition. Connects that ServiceConnector to the
 * appropriate CredentialsSource.
 * <p>
 * ServiceConnector will fail if a CredentialsSource for the Service Definition
 * cannot be located.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 * @since 0.9
 * @see ServiceConnector
 * @see ServiceDefinition
 * @see CredentialsSource
 */
public class ServiceConnectorFactory {

	private static final Logger LOG = Logger.getLogger(ServiceConnectorFactory.class);
	
	public static ServiceConnector getServiceConnector(
			final ServiceConfiguration serviceConfiguration) {
		final CredentialsSourceFactory credentialsSourceFactory = (CredentialsSourceFactory) ConfigContext
				.getCurrentContextConfig().getObjects()
				.get(Config.CREDENTIALS_SOURCE_FACTORY);
		final CredentialsSource credentialsSource = credentialsSourceFactory != null ? credentialsSourceFactory
				.getCredentialsForType(serviceConfiguration.getCredentialsType()) : null;
		ServiceConnector serviceConnector = null;

		if (serviceConfiguration.getCredentialsType() != null && credentialsSource == null) {
			throw new RiceRuntimeException("Service requires credentials but no factory or CredentialsSource could be located.");
		}

		String alternateEndpoint = determineAlternateEndpoint(serviceConfiguration);
		URL alternateEndpointUrl = null;
		if (!StringUtils.isBlank(alternateEndpoint)) {
			try {
				alternateEndpointUrl = new URL(alternateEndpoint);
			} catch (MalformedURLException e) {
				throw new IllegalStateException("Encountered invalid alternate endpoint url: " + alternateEndpoint, e);
			}
		}
		
		// TODO switch this to use serviceConfiguration.getType() at some point in the future and allow for
		// this to be easily "pluggable" with new connector types
		//
		// if set in local mode then preempt any protocol connectors
		if (ConfigContext.getCurrentContextConfig().getDevMode()) {
			serviceConnector = new BusLocalConnector(serviceConfiguration);
		} else if (serviceConfiguration instanceof JavaServiceConfiguration) {
			serviceConnector = new HttpInvokerConnector((JavaServiceConfiguration) serviceConfiguration, alternateEndpointUrl);
		} else if (serviceConfiguration instanceof SoapServiceConfiguration) {
			serviceConnector = new SOAPConnector((SoapServiceConfiguration) serviceConfiguration, alternateEndpointUrl);
		} else if (serviceConfiguration instanceof RestServiceConfiguration) {
			serviceConnector = new RESTConnector((RestServiceConfiguration) serviceConfiguration, alternateEndpointUrl);
		}

		if (serviceConnector == null) {
			throw new RiceRuntimeException("Don't support service type of "	+ serviceConfiguration);
		}
		serviceConnector.setCredentialsSource(credentialsSource);

		return serviceConnector;
	}
	
	public static String determineAlternateEndpoint(ServiceConfiguration serviceConfiguration) {
		String alternateEndpointUrl = null;
		List<AlternateEndpointLocation> alternateEndpointLocations = (List<AlternateEndpointLocation>) ConfigContext.getCurrentContextConfig().getObject(KSBConstants.Config.KSB_ALTERNATE_ENDPOINT_LOCATIONS);
		if (alternateEndpointLocations != null) {
		    for (AlternateEndpointLocation alternateEndpointLocation : alternateEndpointLocations) {
		    	if (Pattern.matches(".*" + alternateEndpointLocation.getEndpointHostReplacementPattern() + ".*", serviceConfiguration.getEndpointUrl().toExternalForm())) {
		    		Pattern myPattern = Pattern.compile(alternateEndpointLocation.getEndpointHostReplacementPattern());
		    		Matcher myMatcher = myPattern.matcher(serviceConfiguration.getEndpointUrl().toExternalForm());
		    		String alternateEndpoint = myMatcher.replaceFirst(alternateEndpointLocation.getEndpointHostReplacementValue());
		    		if ( LOG.isInfoEnabled() ) {
		    			LOG.info("Found an alternate url host value ("
		    					+ alternateEndpointLocation.getEndpointHostReplacementValue() + ") for endpoint: "
		    					+ serviceConfiguration.getEndpointUrl() + " -> instead using: " + alternateEndpoint);
		    		}
		    		alternateEndpointUrl = alternateEndpoint;
		    		break;
		    	}
		    }
		}
		List<AlternateEndpoint> alternateEndpoints = (List<AlternateEndpoint>) ConfigContext.getCurrentContextConfig().getObject(KSBConstants.Config.KSB_ALTERNATE_ENDPOINTS);
		if (alternateEndpoints != null) {
		    for (AlternateEndpoint alternateEndpoint : alternateEndpoints) {
		    	if (Pattern.matches(alternateEndpoint.getEndpointUrlPattern(), serviceConfiguration.getEndpointUrl().toExternalForm())) {
		    		if ( LOG.isInfoEnabled() ) {
		    			LOG.info("Found an alternate url for endpoint: " + serviceConfiguration.getEndpointUrl() + " -> instead using: " + alternateEndpoint.getActualEndpoint());
		    		}
		    		alternateEndpointUrl = alternateEndpoint.getActualEndpoint();
		    		break;
		    	}
		    }
		}
		return alternateEndpointUrl;
	}

}
