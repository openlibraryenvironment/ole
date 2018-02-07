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
package org.kuali.rice.kim.api.identity;

import javax.servlet.http.HttpServletRequest;

/**
 * This service is used to extract the name of the authenticated principal from
 * an incoming http request.  Depending on the implementation of this service,
 * it may extract information from a request which has already been authenticated
 * (i.e. via another service like CAS or Shiboleth).  Alternatively, this
 * implementation might actually perform the authentication itself based on
 * information available on the http request.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface AuthenticationService {
	
	/**
	 * Returns the principalName of the principal that has authenticated with
	 * the incoming HttpServletRequest.  Implementations of this method might
	 * perform actual authentication or merely extract the existing
	 * authenticated principal's name off of the incoming request.
	 * 
	 * @param request the incoming HttpServletRequest
	 * @return the principalName of the authenticated principal, or null if
	 * the principal could not be authenticated
	 */
    String getPrincipalName(HttpServletRequest request);
}
