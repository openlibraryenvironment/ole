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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.cas.CasProcessingFilter;
import org.kuali.rice.kim.sesn.DistributedSession;

/**
 * This class is the main integration point for implementing the 
 * distributed session in ACEGI. 
 * 
 * TODO: Need to add check for missing DST (update 
 * {@link org.kuali.rice.kim.sesn.DistributedSession})  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.acegisecurity.ui.cas.CasProcessingFilter#attemptAuthentication
 */
public class KualiDistributedSessionFilter extends CasProcessingFilter {
    
    private DistributedSession distributedSession;
    
    //~ Methods ========================================================================================================

    /**
     * This overridden method gets called if requiresAuthentication is true.  
     * If Session is Invalid, throw a {@link KualiDistribtedSessionExpiredException}.  
     * The session is determined invalid if the authentication is of type 
     * {@link KualiDistribtedSessionExpiredAuthentication}.  Otherwise it 
     * would have to verify if the DST is valid twice. 
     *
     * @return the authentication result of the super method
     * @see org.acegisecurity.ui.cas.CasProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest)
     */
    public Authentication attemptAuthentication(final HttpServletRequest request)
        throws AuthenticationException { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof KualiDistributedSessionExpiredAuthentication) {
            logger.debug("Authentication is dead in attemptAuthentication, setting authentication to null and throwing KualiDistributedSessionExpiredException");
            SecurityContextHolder.getContext().setAuthentication(null);

            throw new KualiDistributedSessionExpiredException("Session Expired");
        }
       
        return super.attemptAuthentication(request);
    }
    
    /**
     * This overridden method checks if the DST is valid.  If it's not, the 
     * authentication is set to a new, non-authenticated, 
     * {@link KualiDistributedSessionExpiredAuthentication} which is the 
     * indication for {@link attemptAuthentication} that the session has 
     * expired 
     * 
     * @return true if DST is inValid or if super method returns true
     * @see org.acegisecurity.ui.AbstractProcessingFilter#requiresAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        boolean bSesnValid = this.isSesnValid();
        
        if (!bSesnValid) {
            if (this.getDST() != null) {
                logger.debug("session invalid, setting dead authentication, and pushing through to attemptAuthentication");
                SecurityContextHolder.getContext().setAuthentication(new KualiDistributedSessionExpiredAuthentication());
                return true;
            }
        }
        
        return super.requiresAuthentication(request, response);
    }
    
   
    /**
     * This method determines if the stored Distributed Session Ticket is
     * valid.
     * 
     * @return true if valid, false if not
     */
    private boolean isSesnValid() {
        String sDST = this.getDST();
        
        if (sDST != null) {
            if (distributedSession.isSesnValid(sDST)) {
                logger.debug("Session Valid");
                distributedSession.touchSesn(sDST);
                return true;
            } else {
                distributedSession.clearSesn(sDST);
            }
        }
        logger.debug("Session Not Valid");
        
        return false;
    }
    
    /**
     * This method retrieves the Distributed Session Ticket
     * 
     * @return the Distributed Session Ticket if valid or null
     */
    private String getDST() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sDST = null;
        
        if (authentication != null) {
            GrantedAuthority[] authorities = authentication.getAuthorities();
            if (logger.isDebugEnabled()) {
                logger.debug("Granted Authority Count:" + authorities.length);
            }
            
            for (int i = 0; i < authorities.length; i++) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Authority:" + authorities[i]);
                }
                if (authorities[i].toString().startsWith(DistributedSession.getPrefix())) {
                    sDST = authorities[0].toString();
                }
            }
        }
        else {
            logger.debug("Authentication is NULL");            
        }
        
        return sDST;
    }

    /**
     * @param distributedSession the distributedSession to set
     */
    public void setDistributedSession(DistributedSession distributedSession) {
        this.distributedSession = distributedSession;
    }

}
