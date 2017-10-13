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
package org.kuali.rice.kns.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.form.BackdoorForm;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Struts Action which permits a user to execute a backdoor login to masquerade
 * as another user.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BackdoorAction extends KualiAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BackdoorAction.class);
    private List<Permission> perms;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        this.initForm(request, form);
        return super.execute(mapping, form, request, response);
    }

    public ActionForward menu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("menu");
        return mapping.findForward("basic");
    }

    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return portal(mapping, form, request, response);
    }
    
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("start");
        return portal(mapping, form, request, response);
    }

    public ActionForward portal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
    	LOG.debug("portal started");
    	return mapping.findForward("viewPortal");
    }

    public ActionForward administration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("administration");
        return mapping.findForward("administration");
    }

    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("logout");
        
        String forward = "viewPortal";
        UserSession uSession = getUserSession(request);
        
        if (uSession.isBackdoorInUse()) {
            uSession.clearBackdoorUser();
            setFormGroupPermission((BackdoorForm)form, request);
            //request.setAttribute("reloadPage","true");
            
            org.kuali.rice.krad.UserSession KnsUserSession;
            KnsUserSession = GlobalVariables.getUserSession();
            KnsUserSession.clearBackdoorUser();
        }
        else {
            forward = "logout";
        }
        
        return mapping.findForward(forward);
    }

    public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("login");
        UserSession uSession = getUserSession(request);
        BackdoorForm backdoorForm = (BackdoorForm) form;

        uSession.clearObjectMap();

        if (!isBackdoorAuthorized(uSession, request)) {
            request.setAttribute("backdoorRestriction", "User " + uSession.getActualPerson().getPrincipalName()
                    + " not permitted to use backdoor functionality inside application: "
                    + ConfigContext.getCurrentContextConfig().getProperty("app.code") + ".");
            return logout(mapping, form, request, response);
        }

        //if backdoor Id is empty or equal to currently logged in user, clear backdoor id
        if (uSession.isBackdoorInUse() &&
                (StringUtils.isEmpty(backdoorForm.getBackdoorId())
                || uSession.getLoggedInUserPrincipalName().equals(backdoorForm.getBackdoorId()))) {
            return logout(mapping, form, request, response);
        }

        try {
        	uSession.setBackdoorUser(backdoorForm.getBackdoorId());
        } catch (RiceRuntimeException e) {
        	LOG.warn("invalid backdoor id " + backdoorForm.getBackdoorId(), e);
            //Commenting this out since it is not being read anywhere
            //request.setAttribute("badbackdoor", "Invalid backdoor Id given '" + backdoorForm.getBackdoorId() + "'");
            return mapping.findForward("invalid_backdoor_portal");
        }

        setFormGroupPermission(backdoorForm, request);
        
        return mapping.findForward("portal");
    }

    private void setFormGroupPermission(BackdoorForm backdoorForm, HttpServletRequest request) {
    	// based on whether or not they have permission to use the fictional "AdministrationAction", kind of a hack for now since I don't have time to
    	// split this single action up and I can't pass the methodToCall to the permission check
    	Map<String, String> permissionDetails = new HashMap<String, String>();
    	permissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, KewApiConstants.KEW_NAMESPACE);
    	permissionDetails.put(KimConstants.AttributeConstants.ACTION_CLASS, "org.kuali.rice.kew.web.backdoor.AdministrationAction");
    	boolean isAdmin = KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(getUserSession(request)
                .getPrincipalId(), KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.USE_SCREEN,
                permissionDetails, new HashMap<String, String>());
        backdoorForm.setIsAdmin(isAdmin);
    }

    public void initForm(HttpServletRequest request, ActionForm form) throws Exception {
    	BackdoorForm backdoorForm = (BackdoorForm) form;

    	Boolean showBackdoorLogin = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.BACKDOOR_DETAIL_TYPE, KewApiConstants.SHOW_BACK_DOOR_LOGIN_IND);
        backdoorForm.setShowBackdoorLogin(showBackdoorLogin);
        setFormGroupPermission(backdoorForm, request);
        if (backdoorForm.getGraphic() != null) {
        	request.getSession().setAttribute("showGraphic", backdoorForm.getGraphic());
        }
    }

    public static UserSession getUserSession(HttpServletRequest request) {
        return GlobalVariables.getUserSession();
    }

    public boolean isBackdoorAuthorized(UserSession uSession, HttpServletRequest request) {
        boolean isAuthorized = true;

        //we should check to see if a kim permission exists for the requested application first
        Map<String, String> permissionDetails = new HashMap<String, String>();
        String requestAppCode = ConfigContext.getCurrentContextConfig().getProperty("app.code");
        permissionDetails.put(KimConstants.AttributeConstants.APP_CODE, requestAppCode);
        List<Permission> perms = KimApiServiceLocator.getPermissionService().findPermissionsByTemplate(
                KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE, KimConstants.PermissionTemplateNames.BACKDOOR_RESTRICTION);
        for (Permission kpi : perms) {
            if (kpi.getAttributes().values().contains(requestAppCode)) {
                //if a permission exists, is the user granted permission to use backdoor?
                isAuthorized = KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(
                        uSession.getActualPerson().getPrincipalId(), KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE,
                        KimConstants.PermissionTemplateNames.BACKDOOR_RESTRICTION, permissionDetails,
                        Collections.<String, String>emptyMap());
            }
        }
        if (!isAuthorized) {
            LOG.warn("Attempt to backdoor was made by user: "
                    + uSession.getPerson().getPrincipalId()
                    + " into application with app code: "
                    + requestAppCode
                    + " but they do not have appropriate permissions. Backdoor processing aborted.");
        }
        return isAuthorized;
    }
}
