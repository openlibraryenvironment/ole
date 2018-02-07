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
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.cas.CasAuthenticationProvider;
import org.acegisecurity.providers.cas.CasAuthenticationToken;
import org.acegisecurity.providers.cas.StatelessTicketCache;
import org.acegisecurity.ui.cas.CasProcessingFilter;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link CasAuthenticationProvider} implementation that integrates with 
 * Kuali Identity Management (KIM).<p>This 
 * <code>CasAuthenticationProvider</code> is capable of validating {@link
 * UsernamePasswordAuthenticationToken} requests which contains a 
 * distributed session token. It can also validate a previously created 
 * {@link CasAuthenticationToken}.</p>
 *
 * Verifies the the <code>UserDetails</code> based on a valid CAS ticket 
 * response.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
*/
public class KualiCasAuthenticationProvider extends CasAuthenticationProvider {
    
    private static final Log logger = LogFactory.getLog(KualiCasAuthenticationProvider.class);

    /**
     * This overridden method is copied from CAS verbatim.  For some reason 
     * {@link authenticateNow} would not override and the super method 
     * would get called until did this method was also overridden.
     * 
     * @see org.acegisecurity.providers.cas.CasAuthenticationProvider#authenticate(org.acegisecurity.Authentication)
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StatelessTicketCache statelessTicketCache = this.getStatelessTicketCache();
        String key = this.getKey();
        if (!supports(authentication.getClass())) {
            return null;
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken
            && (!CasProcessingFilter.CAS_STATEFUL_IDENTIFIER.equals(authentication.getPrincipal().toString())
            && !CasProcessingFilter.CAS_STATELESS_IDENTIFIER.equals(authentication.getPrincipal().toString()))) {
            // UsernamePasswordAuthenticationToken not CAS related
            return null;
        }

        // If an existing CasAuthenticationToken, just check we created it
        if (authentication instanceof CasAuthenticationToken) {
            if (key.hashCode() == ((CasAuthenticationToken) authentication).getKeyHash()) {
                return authentication;
            } else {
                throw new BadCredentialsException(messages.getMessage("CasAuthenticationProvider.incorrectKey",
                        "The presented CasAuthenticationToken does not contain the expected key"));
            }
        }

        // Ensure credentials are presented
        if ((authentication.getCredentials() == null) || "".equals(authentication.getCredentials())) {
            throw new BadCredentialsException(messages.getMessage("CasAuthenticationProvider.noServiceTicket",
                    "Failed to provide a CAS service ticket to validate"));
        }

        boolean stateless = false;

        if (authentication instanceof UsernamePasswordAuthenticationToken
            && CasProcessingFilter.CAS_STATELESS_IDENTIFIER.equals(authentication.getPrincipal())) {
            stateless = true;
        }

        CasAuthenticationToken result = null;

        if (stateless) {
            // Try to obtain from cache
            result = statelessTicketCache.getByTicketId(authentication.getCredentials().toString());
        }

        if (result == null) {
            result = this.authenticateNow(authentication);
            result.setDetails(authentication.getDetails());
        }

        if (stateless) {
            // Add to cache
            statelessTicketCache.putTicketInCache(result);
        }

        return result;
    }
    
    /**
     * This overridden method is differs from the super method by 
     * populating the user details by passing the full response
     * 
     * @see org.acegisecurity.providers.cas.CasAuthenticationProvider#authenticateNow(Authentication authentication)
     */
    private CasAuthenticationToken authenticateNow(Authentication authentication) throws AuthenticationException {
        // Validate
        KualiTicketResponse response = (KualiTicketResponse)this.getTicketValidator().confirmTicketValid(authentication.getCredentials().toString());

        // Check proxy list is trusted
        this.getCasProxyDecider().confirmProxyListTrusted(response.getProxyList());
        if (logger.isDebugEnabled()) {
            logger.debug("authenticationNOW:" + response);
        }
        // Lookup user details      
        logger.debug("\n\npopulating authorities\n\n");
        UserDetails userDetails = ((KualiCasAuthoritiesPopulator)this.getCasAuthoritiesPopulator()).getUserDetails(response);        

        // Construct CasAuthenticationToken
        return new CasAuthenticationToken(this.getKey(), userDetails, authentication.getCredentials(),
            userDetails.getAuthorities(), userDetails, response.getProxyList(), response.getProxyGrantingTicketIou());
    }
}
