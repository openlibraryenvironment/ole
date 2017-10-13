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
package org.kuali.rice.ken.web.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.service.NotificationAuthorizationService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Spring HandlerInterceptor implementation that implements security.  For now this just
 * adds a flag to the request indicating whether the authenticated user is a Notification
 * System administrator.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(SecurityInterceptor.class);

    /**
     * Request attribute key under which to register the userIsAdmin flag
     */
    private static final String USER_IS_ADMIN_KEY = "userIsAdmin";

    protected NotificationAuthorizationService notificationAuthzService;
    
    /**
     * Sets the NotificationAuthorizationService member
     * @param notificationAuthzService NotificationAuthorizationService used to determine whether user is administrator
     */
    public void setNotificationAuthorizationService(NotificationAuthorizationService notificationAuthzService) {
        this.notificationAuthzService = notificationAuthzService;
    }

    /**
     * Decorate the incoming request with an attribute that indicates whether the user is a Notification System administrator
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String user = request.getRemoteUser();
        boolean isAdmin = false;
        if (user != null) {
            isAdmin = notificationAuthzService.isUserAdministrator(user);
        }
        LOG.debug("Setting request attribute '" + USER_IS_ADMIN_KEY + "' to " + isAdmin);
        request.setAttribute(USER_IS_ADMIN_KEY, Boolean.valueOf(isAdmin));
        return true;
    }
}
