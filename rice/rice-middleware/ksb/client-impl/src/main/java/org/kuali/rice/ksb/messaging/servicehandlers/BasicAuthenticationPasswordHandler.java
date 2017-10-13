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

import org.apache.ws.security.WSPasswordCallback;

/**
 * CallbackHandler that sets the password if the callback is an instance of WSPasswordCallback
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class BasicAuthenticationPasswordHandler implements CallbackHandler {

    private String password;

    /**
     * Initialize the BasicAuthenticationPasswordHandler with the password
     *
     * @param password the password to use
     */
    public BasicAuthenticationPasswordHandler(String password) {
        this.password = password;
    }

    /**
     * @param callbacks an array of Callback objects
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[]
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        if (callbacks[0] != null && callbacks[0] instanceof WSPasswordCallback) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
            pc.setPassword(password);
        }
    }
}
