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
package org.kuali.rice.ksb.api.bus;

import java.net.URL;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.security.credentials.CredentialsType;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;



/**
 * Defines the common parameters for the publication and export of a service
 * to the {@link ServiceBus} and {@link ServiceRegistry}.  Implementations of
 * this interface may define additional properties that are required for the
 * publication of services of that particular type.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ServiceDefinition {
	
	/**
	 * Validates the service definition after creation of the service definition.
	 * It's intended that portions of the KSB that handle publication and export
	 * of services to the bus will execute this prior to successful export of
	 * the service.
	 * 
	 * @throws ConfigurationException if this service definition is not
	 * configured properly
	 */
	void validate();

	/**
	 * Establishes and returns an {@link Endpoint} to this service which
	 * generates the {@link ServiceConfiguration} for this service definition
	 * as well as including the actual service implementation as provided by
	 * {@link #getService()}.
	 * 
	 * <p>The {@link #validate()} method should be invoked prior to executing
	 * this method in order to ensure that the appropriate internal state for
	 * the {@link ServiceDefinition} has been established.
	 * 
	 * @return the established endpoint, should never return null
	 */
	Endpoint establishEndpoint();
	
	/**
	 * Return the actual service implementation to publish and export to the
	 * service bus.
	 * 
	 * @return the service to publish
	 */
	Object getService();
	
	/**
	 * Returns the qualified name for this service.
	 * 
	 * @return the qualified name for this service
	 */
	QName getServiceName();
	
	/**
	 * Returns the URL of the endpoint which provides this service.
	 * 
	 * @return the endpoint URL of the service
	 */
	URL getEndpointUrl();
	
	/**
	 * Returns the {@link ClassLoader} that should be set as the context
	 * classloader on the thread prior to any invocations on the service
	 * 
	 * @return the classloader for this service
	 */
	ClassLoader getServiceClassLoader();
	
	/**
	 * Returns the url path to export the service under.
	 * 
	 * @return the url path to export the service under
	 */
	String getServicePath();

    /**
	 * Returns the id of the specific instance of the application which owns this service
	 *
	 * @return the id of the specific instance of the application which owns this service
	 */
	String getInstanceId();

	/**
	 * Returns the id of the application which owns this service.
	 * 
	 * @return the id of the application which owns this service
	 */
	String getApplicationId();

	/**
	 * Returns the version of this service.
	 * 
	 * @return the version of this service
	 */
	String getServiceVersion();
	
	/**
	 * Returns the type of this service.
	 * 
	 * @return the type of this service
	 */
	String getType();
	
	/**
	 * Return true if this service uses queue-style messaging, false if it uses
	 * topic-style messaging.
	 * 
	 * @return true if this service uses queue-style messaging, false if it uses
	 * topic-style messaging
	 */
	boolean isQueue();
	
	/**
	 * Returns the processing priority for messages that are sent to this service.
	 * 
	 * @return the message processing priority for this service
	 */
	Integer getPriority();
	
	/**
	 * Returns the retry attempts to use when processing messages sent to this
	 * service.
	 * 
	 * @return the retry attempts for this service
	 */
	Integer getRetryAttempts();
	
	/**
	 * Returns the maximum amount of milliseconds a message to this service can
	 * live and attempt to be processed successfully by this service before it's
	 * forced into processing by it's exception handler.
	 *  
	 * @return the maximum lifetime for this message, if null then this message has
	 * an infinite lifetime
	 */
	Long getMillisToLive();
	
	/**
	 * Returns the name of the exception handler to invoke whenever messages to
	 * this service fail to be sent.  If null, the default message exception
	 * handler will be used.
	 * 
	 * @return the name of the message exception handler for this service, or
	 * null if the default handler should be used
	 */
	String getMessageExceptionHandler();
	
	/**
	 * Returns true if this service is secured by standard KSB security features.
	 * 
	 * @return true if this service is secured, false otherwise
	 */
	Boolean getBusSecurity();
	
	/**
	 * Returns the type of security credentials that should be used when
	 * attempting to authorize access to this service.
	 * 
	 * @return the type of security credentials to use when access this service
	 */
	CredentialsType getCredentialsType();

	/**
	 * Returns whether the service is secured with basic authentication
	 *
	 * @return true if this service is secured with basic authentication
	 */
	boolean isBasicAuthentication();
}
