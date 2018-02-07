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

/**
 * Represents a service endpoint that has been published to the service registry.
 * Includes a reference to both {@link ServiceInfoContract} and
 * {@link ServiceDescriptorContract} instances which compose the two different
 * pieces of information about a service endpoint.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ServiceEndpointContract {

	/**
	 * Returns the service information for this endpoint.
	 * 
	 * @return the service information for this endpoint, should never return null
	 */
	ServiceInfoContract getInfo();
	
	/**
	 * Returns the service descriptor for this endpoint.
	 * 
	 * @return the service descriptor for this endpoint, should never return null
	 */
	ServiceDescriptorContract getDescriptor();
	
}
