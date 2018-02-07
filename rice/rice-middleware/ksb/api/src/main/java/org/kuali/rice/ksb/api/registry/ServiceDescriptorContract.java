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

import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

/**
 * A service descriptor includes a serialized XML representation of the
 * {@link ServiceConfiguration} for the service.
 * 
 * <p>It's expected that a client of this service which needs this information
 * will materialize this value when it's needed to obtain additional
 * information about the configuration information for the service.  The
 * process for materializing the information can be dependent upon the
 * type of the {@link ServiceInfo} which contains this descriptor. 
 * 
 * <p>The separation of this descriptor information from the main
 * {@link ServiceInfo} allows for clients of the registry to only load the
 * basic information about the services in the registry, and then pull in the
 * full descriptor for the services they actually need to invoke and work with. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ServiceDescriptorContract extends Versioned {

	/**
	 * Returns the id of this service descriptor.  This value should be unique
	 * across service descriptors.  This value is generated when the service
	 * is published to the registry, so this value can be null if the service
	 * has not yet been published to the registry.
	 * 
	 * @return the id of this service descriptor, or null if the service and
	 * it's descriptor has not yet been published to the registry
	 */
	String getId();
	
	/**
	 * Returns an XML value which can be used to materialize the {@link ServiceConfiguration}
	 * for the service.
	 * 
	 * @return the XML representation of the {@link ServiceConfiguration} for the service,
	 * should never return a null or blank value
	 */
	String getDescriptor();
	
}
