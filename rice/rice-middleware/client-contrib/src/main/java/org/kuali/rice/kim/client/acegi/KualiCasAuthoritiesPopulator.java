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
package org.kuali.rice.kim.client.acegi;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.cas.CasAuthoritiesPopulator;
import org.acegisecurity.userdetails.UserDetails;


/**
 * Populates the <code>UserDetails</code> associated with a CAS authenticated
 * CAS ticket response.
 *
 * <p>
 * Kuali authentication expects a formated response from CAS that includes
 * The username and the authentication method (as an attribute).  At this time
 * The authentication method is stored as a <code>GrantedAuthority</code>
 * </p>
 *
 * <p>
 * Implementations should not perform any caching. They will only be called
 * when a refresh is required.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org) 
*/
public interface KualiCasAuthoritiesPopulator extends CasAuthoritiesPopulator {
   //~ Methods ========================================================================================================

   /**
    * Obtains the granted authorities for the specified user.<P>May throw any
    * <code>AuthenticationException</code> or return <code>null</code> if the authorities are unavailable.</p>
    *
    * @param casUserId as obtained from the CAS validation service
    *
    * @return the details of the indicated user (at minimum the granted authorities and the username)
    *
    * @throws AuthenticationException DOCUMENT ME!
    */
   UserDetails getUserDetails(KualiTicketResponse response)
       throws AuthenticationException;
}
