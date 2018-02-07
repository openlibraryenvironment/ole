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

import java.security.cert.X509Certificate;

import org.kuali.rice.core.api.security.credentials.Credentials;
import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.core.api.security.credentials.CredentialsType;
import org.springframework.util.Assert;

/**
 * Implementation of a CredentialsSource that returns an X509 Certificate.
 * <p>
 * Note that this class is for service-to-service authentication, not
 * user-to-service authentication.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 * @see X509Certificate
 */
public final class X509CredentialsSource implements CredentialsSource {

    private final X509Certificate certificate;

    public X509CredentialsSource(final X509Certificate certificate) {
        Assert.notNull(certificate, "certificate cannot be null.");
        this.certificate = certificate;
    }

    
    
    public Credentials getCredentials(final String serviceEndpoint) {
    	return new X509Credentials(certificate);
	}

    public CredentialsType getSupportedCredentialsType() {
        return CredentialsType.X509;
    }
}
