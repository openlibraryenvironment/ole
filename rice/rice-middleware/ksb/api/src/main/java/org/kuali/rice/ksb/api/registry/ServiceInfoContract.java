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
package org.kuali.rice.ksb.api.registry;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

/**
 * Defines the contract for information about a service that is published in
 * the {@link ServiceRegistry}.
 * 
 * @see ServiceRegistry
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ServiceInfoContract extends Versioned {

	/**
	 * Returns the identifier for the service.
	 * 
	 * @return the identifier for the service, will only be null if the
	 * service has not yet been published to the registry
	 */
	public String getServiceId();

	/**
	 * Returns the name of the service as a qualified name consisting of a
	 * namespace and a name.
	 * 
	 * @return the name of the service, should never be null
	 */
	public QName getServiceName();

	/**
	 * Returns the URL of the service as a string.
	 * 
	 * @return the url of the service, should never be null or blank
	 */
	public String getEndpointUrl();
	
	/**
	 * Returns the id of the instance that published and owns the service.
	 * 
	 * @return the instance id of this service, should never be null or blank
	 */
	public String getInstanceId();
	
	/**
	 * Returns the id of the application that published and owns the service.
	 * 
	 * @return the application id of this service, should never be null or blank
	 */
	public String getApplicationId();

	/**
	 * Return the IP address of the server on which the application is running which
	 * published and owns the service.  This value could be either an IPv4 or
	 * IPv6 address, but it should never return a null or empty string.
	 * 
	 * @return the IP address of this service, should never be null or blank
	 */
	public String getServerIpAddress();
	
	/**
	 * Returns the type of this service.  Will generally distinguish the format
	 * of the data being brokered by the service (i.e. SOAP, REST, Java
	 * Serialization, etc.)
	 * 
	 * @return the type of this service, should never be null or blank
	 */
	public String getType();
	
	/**
	 * Returns the version information of this service.  The publisher of the
	 * service can use any value they choose for the service versions.
	 * However, there is one standard version which represents a service
	 * without any version information, and that is {@link CoreConstants.Versions#UNSPECIFIED}.
	 * 
	 * @return the version of this service, or {@link CoreConstants.Versions#UNSPECIFIED}
	 * if no version has been secified, should never return a null or blank value
	 */
	public String getServiceVersion();
	
	/**
	 * Return the status of the service endpoint represented by this service.
	 * 
	 * @return the status of this service
	 */
	public ServiceEndpointStatus getStatus();
	
	/**
	 * Returns the id of the service descriptor for this service.  This id can
	 * be used to help locate the {@link ServiceDescriptorContract} for this
	 * service which includes more detailed information on this service.
	 * 
	 * @return the id of the service descriptor for this service, will only return
	 * a null value if the service has not yet been published
	 */
	public String getServiceDescriptorId();

	/**
	 * Returns a checksum value for the {@link ServiceConfiguration} stored in the
	 * {@link ServiceDescriptorContract} for this service.  This allows for fast
	 * comparison of services during various registry operations.
	 * 
	 * @return the checksum for this service, should never return a null or blank value
	 */
	public String getChecksum();
	
}
