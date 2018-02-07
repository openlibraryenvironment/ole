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

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the out of box authentication method used by rice.  It authenticates any username/password pairs that match  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiTestAuthenticationProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        if (authentication.getPrincipal().equals(authentication.getCredentials())) {
        	Authentication auth = authenticateNow(authentication);
        	return auth;
        } else {
        	return authentication;
        }
    }

    private UsernamePasswordAuthenticationToken authenticateNow(Authentication authentication) throws AuthenticationException {
    	return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_KUALI_USER")});
    }
    
    public boolean supports(Class authentication) {
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)) {
            return true;
        } else {
            return false;
        }
    }
}
