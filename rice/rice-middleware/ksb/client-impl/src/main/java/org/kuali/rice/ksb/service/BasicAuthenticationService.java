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
package org.kuali.rice.ksb.service;

import javax.xml.namespace.QName;

/**
 * This class is used to register and validate services with basic authentication.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BasicAuthenticationService {

    /**
     * Registers the service credentials of the service
     *
     * @param credentials the credentials for the service
     */
    public void registerServiceCredentials(BasicAuthenticationCredentials credentials);

    /**
     * Registers the connection credentials of the service
     *
     * @param credentials the credentials for the service
     */
    public void registerConnectionCredentials(BasicAuthenticationConnectionCredentials credentials);

    /**
     * Gets the connection credentials for the given serviceNameSpaceURI and serviceName
     *
     * @param serviceNameSpaceURI the serviceNameSpaceURI of the service
     * @param serviceName the serviceName of the service
     * @return the BasicAuthenticationConnectionCredentials for the given serviceNameSpaceURI and serviceName
     */
    public BasicAuthenticationConnectionCredentials getConnectionCredentials(String serviceNameSpaceURI,
            String serviceName);

    /**
     * Validates service authentication for the given serviceNameSpaceURI, serviceName, username and password
     *
     * @param serviceNameSpaceURI the serviceNameSpaceURI of the service
     * @param serviceName the serviceName of the service
     * @param username the username for the service
     * @param password the password for the service
     * @return true if the service can be authenticated
     */
    public boolean checkServiceAuthentication(String serviceNameSpaceURI, QName serviceName, String username,
            String password);

}
