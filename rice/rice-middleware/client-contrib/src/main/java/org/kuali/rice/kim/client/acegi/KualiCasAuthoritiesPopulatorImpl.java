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
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Populates the <code>UserDetails</code> associated with a CAS 
 * authenticated user by reading the response.  This is required to pass
 * the Distributed Session Ticket around.
 *  
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiCasAuthoritiesPopulatorImpl implements KualiCasAuthoritiesPopulator {
    private KualiUserDetailsService userDetailsService;
    private static final Log logger = LogFactory.getLog(KualiCasAuthoritiesPopulatorImpl.class);

    
    /**
     * This method validates the Spring configuration
     * 
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }
    
    /**
     * @param userDetailsService the UserDetailsService to set
     */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = (KualiUserDetailsService)userDetailsService;
    }
    
    /**
     * This overridden method should never be used but is required by the 
     * UserDetails interface
     * 
     * @see org.acegisecurity.providers.cas.CasAuthoritiesPopulator#getUserDetails(java.lang.String)
     */
    public UserDetails getUserDetails(String casUserId)
        throws AuthenticationException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserDetails(userID)");
        }
        return this.userDetailsService.loadUserByUsername(casUserId);
    }
    
    /**
     * This overridden method is used to pass the Distributed Session 
     * Ticket around via the {@link KualiTicketResponse}
     * 
     * @see org.kuali.rice.kim.client.acegi.KualiCasAuthoritiesPopulator#getUserDetails(org.kuali.rice.kim.client.acegi.KualiTicketResponse)
     */
    public UserDetails getUserDetails(KualiTicketResponse response) 
        throws AuthenticationException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserDetails(response)");
        }
        return this.userDetailsService.loadUserByTicketResponse(response);
    }

}
