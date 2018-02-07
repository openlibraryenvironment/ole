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

import org.acegisecurity.providers.AbstractAuthenticationToken;

/**
 * An indicator used to determine that the session expired 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiDistributedSessionExpiredAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -370555068587002773L;

    public boolean isAuthenticated() {
        return false;
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.acegisecurity.Authentication#getCredentials()
     */
    public Object getCredentials() {
        // TODO lsymms - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    /**
     * This overridden method ...
     * 
     * @see org.acegisecurity.Authentication#getPrincipal()
     */
    public Object getPrincipal() {
        // TODO lsymms - THIS METHOD NEEDS JAVADOCS
        return null;
    }

}
