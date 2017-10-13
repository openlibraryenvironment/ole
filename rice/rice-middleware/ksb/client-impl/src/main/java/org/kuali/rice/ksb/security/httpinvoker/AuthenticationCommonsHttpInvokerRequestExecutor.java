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
package org.kuali.rice.ksb.security.httpinvoker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.kuali.rice.core.api.security.credentials.CredentialsSource;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.KSBHttpInvokerRequestExecutor;
import org.kuali.rice.ksb.security.credentials.UsernamePasswordCredentials;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.util.Assert;


/**
 * Extension to {@link KSBHttpInvokerRequestExecutor} that retrieves
 * credentials from the CredentialsSource and places them in a BASIC HTTP
 * Authorization header.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public final class AuthenticationCommonsHttpInvokerRequestExecutor extends
    KSBHttpInvokerRequestExecutor {

    /**
     * Source of the credentials to pass via BASIC AUTH.
     */
    private final CredentialsSource credentialsSource;

    /**
     * Details about the service that the CredentialsSource may need.
     */
    private final ServiceConfiguration serviceConfiguration;

    /**
     * Constructor that accepts the CredentialsSource and Service Info.
     * 
     * @param credentialsSource the source of credentials.
     * @param serviceInfo information about the service.
     */
    public AuthenticationCommonsHttpInvokerRequestExecutor(final HttpClient httpClient, 
        final CredentialsSource credentialsSource, final ServiceConfiguration serviceConfiguration) {
        super(httpClient);
        Assert.notNull(credentialsSource, "credentialsSource cannot be null.");
        Assert.notNull(serviceConfiguration, "serviceConfiguration cannot be null.");
        this.credentialsSource = credentialsSource;
        this.serviceConfiguration = serviceConfiguration;
    }

    /**
     * Overridden to obtain the Credentials from the CredentialsSource and pass
     * them via HTTP BASIC Authorization.
     */

    protected void setRequestBody(final HttpInvokerClientConfiguration config,
        final PostMethod postMethod, final ByteArrayOutputStream baos) throws IOException {
    	final UsernamePasswordCredentials credentials = (UsernamePasswordCredentials) this.credentialsSource.getCredentials(this.serviceConfiguration.getEndpointUrl().toExternalForm());

        final String base64 = credentials.getUsername() + ":"
        + credentials.getPassword();
        postMethod.addRequestHeader("Authorization", "Basic " + new String(Base64.encodeBase64(base64.getBytes())));

        if (logger.isDebugEnabled()) {
            logger
                .debug("HttpInvocation now presenting via BASIC authentication CredentialsSource-derived: "
                    + credentials.getUsername());
        }
        
        super.setRequestBody(config, postMethod, baos);
    }
}
