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

/**
 * An {@code Endpoint} contains a reference to the {@link ServiceConfiguration}
 * for a service as well as a proxy to the service endpoint that can be invoked.
 * 
 * <p>This service can be cast to the appropriate service interface in order to
 * invoke the desired operations.
 */
public interface Endpoint {

	/**
	 * Returns the service configuration information for this endpoint.
	 * 
	 * @return the service configuration for this endpoint, should never return null
	 */
	ServiceConfiguration getServiceConfiguration();
	
	/**
	 * Returns a reference to the service that can be invoked through this
	 * endpoint.  This could potentially be a proxy to the service (in the case
	 * that the endpoint is pointing to a remote service) so calling code
	 * should cast this object to the appropriate service interface before
	 * using.
	 * 
	 * <p>It is the client's responsibility to ensure that they are casting the
	 * service to the correct interface(s) based on their knowledge of what
	 * interface the service should implement.
	 * 
	 * @return a reference to the service object which can be used to invoke
	 * operations against the endpoint, should never return null
	 */
	Object getService();
	
}
