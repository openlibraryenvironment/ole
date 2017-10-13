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

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Populates a UserDetails object with ticket or username and 
 * Authentication Method
 *  
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiUserDetailsServiceImpl implements KualiUserDetailsService, InitializingBean
{
    private static final Log logger = LogFactory.getLog(KualiUserDetailsServiceImpl.class);

    public void afterPropertiesSet() throws Exception {}
    
    /**
     * This overridden method appends the Distributed Session Ticket to the
     * granted authorities
     * 
     * @see org.kuali.rice.kim.client.acegi.KualiUserDetailsService#loadUserByTicketResponse(org.kuali.rice.kim.client.acegi.KualiTicketResponse)
     */
    public UserDetails loadUserByTicketResponse(KualiTicketResponse response) {
        GrantedAuthority[] authorities = new GrantedAuthority[1];
        authorities[0]= new GrantedAuthorityImpl(response.getDistributedSessionToken());
        if (logger.isDebugEnabled()) {
            logger.debug("loadUserByTicketResponse:" + response.getDistributedSessionToken());
        }
        return loadUserByUsernameAndAuthorities(response.getUser(), authorities); 
    }

    /**
     * This overridden method ...
     * 
     * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    public UserDetails loadUserByUsername(String username)
    {
        if (logger.isDebugEnabled()) {
            logger.debug("loadUserByUsername");
        }
        return loadUserByUsernameAndAuthorities(username, new GrantedAuthority[0]);        
    }
    
    /**
     * This method is necessary for loading users by the ticket response
     * 
     * @param username
     * @param authorities
     * @return the UserDetails
     */
    public UserDetails loadUserByUsernameAndAuthorities(String username, GrantedAuthority[] authorities) {
        if (logger.isDebugEnabled()) {
            logger.debug("loadUserByUsernameAndAuthorities");
        }
        GrantedAuthority[] newAuthorities = new GrantedAuthority[authorities.length+1];
        System.arraycopy(authorities, 0, newAuthorities, 0, authorities.length);
        newAuthorities[authorities.length]= new GrantedAuthorityImpl("ROLE_KUALI_USER");
        logger.warn("setting granted authorities:" + newAuthorities.toString());
        UserDetails user = new User(username, "empty_password", true, true, true, true, newAuthorities);    
        return user;
    }

   
}
