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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * Initializes authenticationService with user credentials.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BasicAuthenticationCredentials implements InitializingBean {

    private BasicAuthenticationService authenticationService;
    private String serviceNameSpaceURI;
    private String localServiceName;
    private String username;
    private String password;

    /**
     * Register the service credentials for the service
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (this.validate()) {
            this.getAuthenticationService().registerServiceCredentials(this);
        }
    }

    /**
     * Gets the BasicAuthenticationService to use
     *
     * @return BasicAuthenticationService the BasicAuthenticationService
     */
    public BasicAuthenticationService getAuthenticationService() {
        if (this.authenticationService == null) {
            this.authenticationService = KSBServiceLocator.getBasicAuthenticationService();
        }
        return authenticationService;
    }

    /**
     * Sets the authenticationService
     *
     * @param authenticationService the BasicAthenticationService to set
     */
    public void setAuthenticationService(BasicAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Gets the serviceNameSpaceURI
     *
     * @return String serviceNameSpaceURI of this BasicAuthenticationCredentials
     */
    public String getServiceNameSpaceURI() {
        return serviceNameSpaceURI;
    }

    /**
     * Sets the serviceNameSpaceURI
     *
     * @param serviceNameSpaceURI the serviceNameSpaceURI to set
     */
    public void setServiceNameSpaceURI(String serviceNameSpaceURI) {
        this.serviceNameSpaceURI = serviceNameSpaceURI;
    }

    /**
     * Gets the localServiceName
     *
     * @return String localServiceName of this BasicAuthenticationCredentials
     */
    public String getLocalServiceName() {
        return localServiceName;
    }

    /**
     * Sets the localServiceName
     *
     * @param localServiceName the localServiceName to set
     */
    public void setLocalServiceName(String localServiceName) {
        this.localServiceName = localServiceName;
    }

    /**
     * Gets the username
     *
     * @return username the username of this BasicAuthenticationCredentials
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password
     *
     * @return password the password of this BasicAuthenticationCredentials
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validates authenticationService, serviceNameSpaceURI, localServiceName, username, and password
     *
     * @return true if the authentictionSerivce is not null and the serviceNameSpaceURI, localServiceName,
     *         username, and password are not blank
     */
    protected boolean validate() {
        return this.getAuthenticationService() != null &&
                StringUtils.isNotBlank(this.getServiceNameSpaceURI()) &&
                StringUtils.isNotBlank(this.getLocalServiceName()) &&
                StringUtils.isNotBlank(this.getUsername()) &&
                StringUtils.isNotBlank(this.getPassword());
    }
}
