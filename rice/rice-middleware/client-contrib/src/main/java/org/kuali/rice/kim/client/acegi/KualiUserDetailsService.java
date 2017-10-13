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

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;


/**
 * Defines an interface for implementations that wish to provide data 
 * access services to the {@link DaoAuthenticationProvider}.
 *
 * <p>
 * Kuali Requires CAS to provide the <code>Authentication Source</code> so
 * a method is require to get user based on the <code>response</code> 
 * object </p>
 *  
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface KualiUserDetailsService extends UserDetailsService {
    //~ Methods ========================================================================================================

    /**
     * Locates the user based on the response. In the actual implementation, the search may possibly be case
     * insensitive, or case insensitive depending on how the implementaion instance is configured. In this case, the
     * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
     * actually requested.  Also populates the <code>Authentication Source</code> as a <code>GrantedAuthority</code>
     *
     * @param response the reponse from the TicketValidator presented to the {@link DaoAuthenticationProvider}
     *
     * @return a fully populated user record (never <code>null</code>)
     *
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     * @throws DataAccessException if user could not be found for a repository-specific reason
     */
    UserDetails loadUserByTicketResponse(KualiTicketResponse response)
        throws UsernameNotFoundException, DataAccessException;
}
