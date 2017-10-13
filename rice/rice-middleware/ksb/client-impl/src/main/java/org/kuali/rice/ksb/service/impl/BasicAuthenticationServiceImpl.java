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
package org.kuali.rice.ksb.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.ksb.service.BasicAuthenticationConnectionCredentials;
import org.kuali.rice.ksb.service.BasicAuthenticationCredentials;
import org.kuali.rice.ksb.service.BasicAuthenticationService;

/**
 * Implements the BasicAuthenticationService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.ksb.service.BasicAuthenticationService
 */

public class BasicAuthenticationServiceImpl implements BasicAuthenticationService {

    private Map<QName, List<BasicAuthenticationCredentials>> serviceCredentialsMap;
    private Map<QName, BasicAuthenticationConnectionCredentials> connectionCredentialsMap;

    /**
     * Constructs BasicAuthenticationServiceImpl with a serviceCredentialsMap and a connectionCredentialsMap
     */
    public BasicAuthenticationServiceImpl() {
        this.serviceCredentialsMap = Collections.synchronizedMap(
                new HashMap<QName, List<BasicAuthenticationCredentials>>());
        this.connectionCredentialsMap = Collections.synchronizedMap(
                new HashMap<QName, BasicAuthenticationConnectionCredentials>());
    }

    public boolean checkServiceAuthentication(String serviceNameSpaceURI, QName serviceName, String username,
            String password) {
        List<BasicAuthenticationCredentials> credentialsList = serviceCredentialsMap.get(serviceName);
        if (credentialsList != null) {
            synchronized (credentialsList) {
                for (BasicAuthenticationCredentials credentials : credentialsList) {
                    if (StringUtils.equals(username, credentials.getUsername()) && StringUtils.equals(
                            serviceNameSpaceURI, credentials.getServiceNameSpaceURI())) {
                        return StringUtils.equals(password, credentials.getPassword());
                    }
                }
            }
        }
        return false;
    }

    public BasicAuthenticationConnectionCredentials getConnectionCredentials(String serviceNameSpaceURI,
            String serviceName) {
        return connectionCredentialsMap.get(new QName(serviceNameSpaceURI, serviceName));
    }

    public void registerServiceCredentials(BasicAuthenticationCredentials credentials) {
        synchronized (serviceCredentialsMap) {
            QName serviceName = new QName(credentials.getServiceNameSpaceURI(), credentials.getLocalServiceName());
            List<BasicAuthenticationCredentials> credentialsList = serviceCredentialsMap.get(serviceName);
            if (credentialsList == null) {
                credentialsList = Collections.synchronizedList(new ArrayList<BasicAuthenticationCredentials>());
                serviceCredentialsMap.put(serviceName, credentialsList);
            }
            credentialsList.add(credentials);
        }
    }

    public void registerConnectionCredentials(BasicAuthenticationConnectionCredentials credentials) {
        synchronized (connectionCredentialsMap) {
            connectionCredentialsMap.put(new QName(credentials.getServiceNameSpaceURI(),
                    credentials.getLocalServiceName()), credentials);
        }
    }

}
