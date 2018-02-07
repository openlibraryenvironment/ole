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
package org.kuali.rice.krad.web.filter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthenticationException;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * A login filter which forwards to a login page that allows for the desired
 * authentication ID to be entered (with testing password if option enabled)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DummyLoginFilter implements Filter {
    private String loginPath;
    private boolean showPassword = false;

    @Override
	public void init(FilterConfig config) throws ServletException {
        loginPath = ConfigContext.getCurrentContextConfig().getProperty("loginPath");
        showPassword = Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty("showPassword")).booleanValue();

        if (loginPath == null) {
            loginPath = "/kr-login/login?methodToCall=start&viewId=DummyLoginView&dataObjectClassName=org.kuali.rice.krad.web.login.DummyLoginForm";
        }
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final UserSession session = KRADUtils.getUserSessionFromRequest(request);

        if (session == null) {
            loginRequired(request, response, chain);

            return;

        } else {
            // Perform request as signed in user
            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getRemoteUser() {
                    return session.getPrincipalName();
                }
            };
        }
        chain.doFilter(request, response);
    }

    private void loginRequired(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (StringUtils.isNotBlank(request.getParameter("__login_user"))) {
            performLoginAttempt(request, response);
        } else {
            // ignore ajax calls from login screen
            if (StringUtils.equals(request.getPathInfo(),"/listener")) {
               return;
            }

            // allow redirect and form submit from login screen
            if (StringUtils.equals(request.getPathInfo(),"/login")) {
                chain.doFilter(request, response);
            } else {
                // no session has been established and this is not a login form submission, so redirect to login page
                response.sendRedirect(getLoginRedirectUrl(request));
            }
        }
    }

    private void performLoginAttempt(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        IdentityService auth = KimApiServiceLocator.getIdentityService();
        final String user = request.getParameter("__login_user");
        String password = request.getParameter("__login_pw");

        // if passwords are used, they cannot be blank
        if (showPassword && StringUtils.isBlank(password)) {
            handleInvalidLogin(request, response);
            return;
        }

        // Very simple password checking. Nothing hashed or encrypted. This is strictly for demonstration purposes only.
        //    password must have non null value on krim_prncpl_t record
        Principal principal = showPassword ? auth.getPrincipalByPrincipalNameAndPassword(user, password) : auth.getPrincipalByPrincipalName(user);
        if (principal == null) {
            handleInvalidLogin(request, response);
            return;
        }

        UserSession userSession = new UserSession(user);

        // Test if session was successfully build for this user
        if ( userSession.getPerson() == null ) {
            throw new AuthenticationException("Invalid User: " + user  );
        }

        request.getSession().setAttribute(KRADConstants.USER_SESSION_KEY, userSession);

        // wrap the request with the signed in user
        // UserLoginFilter and WebAuthenticationService will build the session
        request = new HttpServletRequestWrapper(request) {
            @Override
            public String getRemoteUser() {
                return user;
            }
        };

        StringBuilder redirectUrl = new StringBuilder(ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.APPLICATION_URL_KEY));
        redirectUrl.append(findTargetUrl(request));
        response.sendRedirect(redirectUrl.toString());
    }

    /**
     * Handles and invalid login attempt.
     *
     * Sets error message and redirects to login screen
     *
     * @param request the incoming request
     * @param response the outgoing response
     * @throws javax.servlet.ServletException if unable to handle the invalid login
     * @throws java.io.IOException if unable to handle the invalid login
     */
	private void handleInvalidLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder redirectUrl = new StringBuilder(getLoginRedirectUrl(request));
        redirectUrl.append("&login_message=Invalid Login");
        response.sendRedirect(redirectUrl.toString());
    }

    @Override
	public void destroy() {
    	loginPath = null;
    }

    /**
     * Construct Url to login screen with original target Url in returnLocation property
     *
     * @param request
     * @return Url string
     * @throws IOException
     */
    private String getLoginRedirectUrl(HttpServletRequest request) throws IOException {
        String targetUrl = findTargetUrl(request);

        StringBuilder redirectUrl = new StringBuilder(ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.APPLICATION_URL_KEY));
        redirectUrl.append(loginPath);
        redirectUrl.append("&returnLocation=");
        redirectUrl.append(URLEncoder.encode(targetUrl,"UTF-8"));

        return redirectUrl.toString();
    }

    /**
     * Construct a url from a HttpServletRequest with login properties removed
     *
     * @param request
     * @return Url string
     */
    private String findTargetUrl(HttpServletRequest request) {
        StringBuilder targetUrl = new StringBuilder();
        targetUrl.append(request.getServletPath());

        if (StringUtils.isNotBlank(request.getPathInfo())) {
            targetUrl.append(request.getPathInfo());
        }

        // clean login params from query string
        if (StringUtils.isNotBlank(request.getQueryString())) {
            targetUrl.append("?");

            for (String keyValuePair : request.getQueryString().split("&")) {
                if (isValidProperty(keyValuePair)) targetUrl.append("&").append(keyValuePair);
            }

        }

        // clean up delimiters and return url string
        return targetUrl.toString().replace("&&","&").replace("?&","?");
    }

    /**
     * Test if property is needed (ie: Not a login property)
     *
     * @param keyValuePair
     * @return Boolean
     */
    private Boolean isValidProperty(String keyValuePair) {
        int eq = keyValuePair.indexOf("=");

        if (eq < 0) {
            // key with no value
            return Boolean.FALSE;
        }

        String key = keyValuePair.substring(0, eq);
        if (!key.equals("__login_pw")
                && !key.equals("__login_user")
                && !key.equals("login_message")
                && !key.equals("returnLocation")) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

}
