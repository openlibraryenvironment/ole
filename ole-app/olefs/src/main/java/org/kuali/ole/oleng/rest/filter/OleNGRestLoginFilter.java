package org.kuali.ole.oleng.rest.filter;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.AuthenticationService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthenticationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by SheikS on 1/28/2016.
 */
public class OleNGRestLoginFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(OleNGRestLoginFilter.class);

    private FilterConfig filterConfig;
    private IdentityService identityService;
    private PermissionService permissionService;

    private String userName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing Rest Login Filter");
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("Chaining Rest Login Filter");
        userName = ((HttpServletRequest) servletRequest).getHeader("userName");
        if(StringUtils.isBlank(userName)) {
            userName = getDefaultUserForRestCall();
        }
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest hsreq = (HttpServletRequest) servletRequest;
            servletRequest = new HttpServletRequestWrapper(hsreq) {
                public String getRemoteUser() {
                    return userName;
                }
            };
        }
        authenticateUserAndCreateUserSession((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getDefaultUserForRestCall() {
        return ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OleNGConstants.DEFAULT_USER_FOR_REST_CALLS);
    }

    private void authenticateUserAndCreateUserSession(HttpServletRequest request) {
        String principalName = ((AuthenticationService) GlobalResourceLoader.getResourceLoader().getService(
                new QName("kimAuthenticationService"))).getPrincipalName(request);
        if (StringUtils.isBlank(principalName)) {
            throw new AuthenticationException("Blank User from AuthenticationService - This should never happen.");
        }

        Principal principal = getIdentityService().getPrincipalByPrincipalName(principalName);
        if (principal == null) {
            throw new AuthenticationException("Unknown User: " + principalName);
        }

        if (!isAuthorizedToLogin(principal.getPrincipalId())) {
            throw new AuthenticationException(
                    "You cannot log in, because you are not an active Kuali user.\nPlease ask someone to activate your account if you need to use Kuali Systems.\nThe user id provided was: "
                            + principalName + ".\n");
        }

        final UserSession userSession = new UserSession(principalName);
        if (userSession.getPerson() == null) {
            throw new AuthenticationException("Invalid User: " + principalName);
        }

        GlobalVariables.setUserSession(userSession);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }


    private boolean isAuthorizedToLogin(String principalId) {
        return getPermissionService().isAuthorized(principalId, KimConstants.KIM_TYPE_DEFAULT_NAMESPACE,
                KimConstants.PermissionNames.LOG_IN, Collections.singletonMap("principalId", principalId));
    }

    private PermissionService getPermissionService() {
        if (this.permissionService == null) {
            this.permissionService = KimApiServiceLocator.getPermissionService();
        }
        return this.permissionService;
    }

    private IdentityService getIdentityService() {
        if (this.identityService == null) {
            this.identityService = KimApiServiceLocator.getIdentityService();
        }
        return this.identityService;
    }
}
