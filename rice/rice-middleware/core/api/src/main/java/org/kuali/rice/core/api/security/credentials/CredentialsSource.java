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
package org.kuali.rice.core.api.security.credentials;

/**
 * Inteface for abstracting out how we obtain credentials to access secure
 * applications across the bus.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public interface CredentialsSource {

    /**
     * Returns the supported CredentialsType. This should never be null.
     * 
     * @return the supported credentials type.
     */
    CredentialsType getSupportedCredentialsType();

    /**
     * Returns the SecurityContext which should have the proper credentials to
     * give to any system requiring credentials. This class has no guarantees
     * what a user will do with the credentials and should thus always return a
     * thread-safe version.
     * 
     * @param serviceDefinition The Service Definition for which we want
     * credentials for.
     * @return the credentials, or null if credentials could not be obtained.
     */
    Credentials getCredentials(String serviceEndpoint);
}
