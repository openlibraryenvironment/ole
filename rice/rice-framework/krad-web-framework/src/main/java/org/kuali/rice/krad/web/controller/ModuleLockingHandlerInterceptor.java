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
package org.kuali.rice.krad.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor which checks whether the module the request was made for is locked and if so forwards the
 * request to the module locked controller
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ModuleLockingHandlerInterceptor implements HandlerInterceptor {
    private static final Logger LOG = Logger.getLogger(ModuleLockingHandlerInterceptor.class);

    private KualiModuleService kualiModuleService;
    private String moduleLockedMapping;

    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        // do nothing
    }

    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndview) throws Exception {
        // do nothing
    }

    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        if (isModuleLocked(request)) {
            response.sendRedirect(this.getModuleLockedMapping() + "?" + ModuleLockedController.MODULE_PARAMETER
                    + "=" + getModuleService(request).getModuleConfiguration().getNamespaceCode());

            return false;
        }

        return true;
    }

    /**
     * Determines the module associated with the given request and then checks whether the module is locked
     *
     * @param request request object to pull parameters from
     * @return boolean true if the associated module is locked, false if not or no associated module was found
     */
    protected boolean isModuleLocked(HttpServletRequest request) {
        ModuleService moduleService = getModuleService(request);

        if (moduleService != null && moduleService.isLocked()) {
            String principalId = GlobalVariables.getUserSession().getPrincipalId();
            String namespaceCode = KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE;
            String permissionName = KimConstants.PermissionNames.ACCESS_LOCKED_MODULE;

            Map<String, String> permissionDetails = new HashMap<String, String>();
            Map<String, String> qualification = new HashMap<String, String>();
            if (!KimApiServiceLocator.getPermissionService().isAuthorized(principalId, namespaceCode, permissionName,
                    qualification)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the module service that is associated with the data object class given through the request
     *
     * @param request request object to check parameters for
     * @return ModuleService module service for data object (if found) or null
     */
    protected ModuleService getModuleService(HttpServletRequest request) {
        String boClass = request.getParameter(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE);
        if (StringUtils.isBlank(boClass)) {
            boClass = request.getParameter(KRADConstants.DATA_OBJECT_CLASS_ATTRIBUTE);
        }

        ModuleService moduleService = null;
        if (StringUtils.isNotBlank(boClass)) {
            try {
                moduleService = getKualiModuleService().getResponsibleModuleService(Class.forName(boClass));
            } catch (ClassNotFoundException classNotFoundException) {
                LOG.warn("BO class not found: " + boClass, classNotFoundException);
            }
        } else {
            moduleService = getKualiModuleService().getResponsibleModuleService(this.getClass());
        }

        return moduleService;
    }

    public String getModuleLockedMapping() {
        return this.moduleLockedMapping;
    }

    public void setModuleLockedMapping(String moduleLockedMapping) {
        this.moduleLockedMapping = moduleLockedMapping;
    }

    public void setKualiModuleService(KualiModuleService kualiModuleService) {
        this.kualiModuleService = kualiModuleService;
    }

    protected KualiModuleService getKualiModuleService() {
        if (kualiModuleService == null) {
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        }
        return kualiModuleService;
    }
}
