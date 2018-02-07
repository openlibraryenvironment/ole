/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.test.web;

import javax.servlet.ServletContext;

import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * Subclass of MockHttpServletRequest that initializes the request with a user session
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowServletRequest extends MockHttpServletRequest {
    public WorkflowServletRequest() {
        super();
    }
    public WorkflowServletRequest(ServletContext context, String method, String requestURI) {
        super(context, method, requestURI);
    }
    public WorkflowServletRequest(ServletContext context) {
        super(context);
    }
    public WorkflowServletRequest(String method, String requestURI) {
        super(method, requestURI);
    }

    public WorkflowServletRequest(String user) {
        setUser(user);
    }

    public void setUser(String user) {
        Principal wfuser;
        if (user == null) {
            wfuser = null;
        } else {
            wfuser = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(user);
        }
        setWorkflowUser(wfuser);
    }

    public String getUser() {
        Principal user = getWorkflowUser();
        if (user == null) return null;
        return user.getPrincipalName();
    }

    public void setBackdoorId(String backdoorId) {
        UserSession session = getUserSession();
        if (session == null) {
            throw new IllegalStateException("Session must be set before backdoor id is set");
        }
        session.setBackdoorUser(backdoorId);
    }

    public void setWorkflowUser(Principal user) {
        if (user == null) {
            setUserSession(null);
        } else {
            setUserSession(new UserSession(user.getPrincipalName()));
        }
    }

    public Principal getWorkflowUser() {
        UserSession session = getUserSession();
        if (session == null) return null;
        return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(session.getLoggedInUserPrincipalName());
    }

    public String getBackdoorPrincipalId() {
        UserSession session = getUserSession();
        if (session == null) return null;
        return session.getPrincipalId();
    }

    public void setUserSession(UserSession userSession) {
        getSession().setAttribute(KRADConstants.USER_SESSION_KEY, userSession);
    }

    public UserSession getUserSession() {
        return (UserSession) getSession().getAttribute(KRADConstants.USER_SESSION_KEY);
    }
}
