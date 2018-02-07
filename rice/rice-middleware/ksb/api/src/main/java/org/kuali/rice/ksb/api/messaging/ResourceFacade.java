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
package org.kuali.rice.ksb.api.messaging;

/**
 * ResourceFacade interface.  RESTful services can be registered with more than one resource class,
 * and the ResourceFacade allows you to select the resource class by resource name
 * (the URI sub-path past the service root) or resource class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ResourceFacade {

	/**
	 * get the resource corresponding to the given sub-path
	 */
	public <R> R getResource(String resourceName);

	/**
	 * get the resource for the provided type
	 */
	public <R> R getResource(Class<R> resourceClass);

	public boolean isSingleResourceService();

}
