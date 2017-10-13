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
package org.kuali.rice.ksb.config.wss4j;

import org.apache.ws.security.WSPasswordCallback;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;


/**
 * Workflow CryptoPasswordCallbackHandler which retrieves the keystore password
 * from the workflow Config.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CryptoPasswordCallbackHandler implements CallbackHandler {

    /**
     * The actual CallBackHandler implementation.
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                String password = ConfigContext.getCurrentContextConfig().getKeystorePassword();
                if (password == null) {
                	throw new ConfigurationException("Could not locate the webservice password.  Should be configured as the '" + Config.KEYSTORE_PASSWORD + "' property.");
                }
                pc.setPassword(password);
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}
