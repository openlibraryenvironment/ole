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
package org.kuali.rice.ksb.messaging.servicehandlers;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;

import org.apache.ws.security.WSPasswordCallback;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.ksb.service.KSBServiceLocator;

/**
 * CallbackHandler that verifies the password and username is correct for a service
 * secured with basic authentication.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BasicAuthenticationHandler implements CallbackHandler {

    private String serviceNameSpaceURI;
    private QName localServiceName;

    /**
     * Initialize the BasicAuthenticationHandler with the serviceNameSpaceURI and localServiceName
     *
     * @param serviceNameSpaceURI the serviceNameSpaceURI to use
     * @param serviceName the serviceName to use
     */
    public BasicAuthenticationHandler(String serviceNameSpaceURI, QName serviceName) {
        this.serviceNameSpaceURI = serviceNameSpaceURI;
        this.localServiceName = serviceName;
    }

    /**
     * @param callbacks an array of Callback objects
     * @throws RiceRuntimeException if the username or password is invalid
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        if (callbacks[0] != null && callbacks[0] instanceof WSPasswordCallback) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
            if (!KSBServiceLocator.getBasicAuthenticationService().checkServiceAuthentication(this.serviceNameSpaceURI,
                    this.localServiceName, pc.getIdentifier(), pc.getPassword())) {
                throw new RiceRuntimeException("Invalid username or password");
            }
        }
    }
}
