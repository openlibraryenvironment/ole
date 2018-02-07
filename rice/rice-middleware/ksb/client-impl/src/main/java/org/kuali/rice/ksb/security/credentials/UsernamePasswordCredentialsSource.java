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
package org.kuali.rice.ksb.security.credentials;

import org.kuali.rice.core.api.security.credentials.Credentials;
import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.core.api.security.credentials.CredentialsType;
import org.springframework.util.Assert;

/**
 * Implementation of a CredentialsSource that contains a username and password.
 * <p>
 * Note that this implementation is for service-to-service authentication. It
 * cannot handle user-to-service authentication.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public final class UsernamePasswordCredentialsSource implements CredentialsSource {

    /** The username. */
    private final String username;

    /** The password. */
    private final String password;

    public Credentials getCredentials(final String serviceEndpoint) {
    	return new UsernamePasswordCredentials(username, password);
	}

	/**
     * Constructor that accepts the username and password for which to construct
     * UsernamePasswordAuthenticationToken's from.
     * 
     * @param username the username.
     * @param password the password.
     */
    public UsernamePasswordCredentialsSource(final String username,
        final String password) {
        Assert.notNull(username, "username cannot be null.");
        Assert.notNull(password, "password cannote be null.");

        this.username = username;
        this.password = password;
    }
    
    public CredentialsType getSupportedCredentialsType() {
        return CredentialsType.USERNAME_PASSWORD;
    }
}
