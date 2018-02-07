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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.service.ViewService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krad.web.form.UifFormManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides helper methods that will be used during the request lifecycle
 *
 * <p>
 * Created to avoid duplication of the methods used by the UifHandlerExceptionResolver
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifControllerHelper {
    private static final Logger LOG = Logger.getLogger(UifControllerHelper.class);

    /**
     * Attempts to resolve a view id from the given request
     *
     * <p>
     * First an attempt will be made to find the view id as a request parameter. If no such request parameter
     * is found, the request will be looked at for view type information and a call will be made to the
     * view service to find the view id by type
     * </p>
     *
     * <p>
     * If a view id is found it is stuck in the request as an attribute (under the key
     * {@link org.kuali.rice.krad.uif.UifParameters#VIEW_ID}) for subsequent retrieval
     * </p>
     *
     * @param request instance to resolve view id for
     * @return view id if one is found, null if not found
     */
    public static String getViewIdFromRequest(HttpServletRequest request) {
        String viewId = request.getParameter(UifParameters.VIEW_ID);

        if (StringUtils.isBlank(viewId)) {
            String viewTypeName = request.getParameter(UifParameters.VIEW_TYPE_NAME);

            UifConstants.ViewType viewType = null;
            if (StringUtils.isNotBlank(viewTypeName)) {
                viewType = UifConstants.ViewType.valueOf(viewTypeName);
            }

            if (viewType != null) {
                Map<String, String> parameterMap = KRADUtils.translateRequestParameterMap(request.getParameterMap());
                viewId = getViewService().getViewIdForViewType(viewType, parameterMap);
            }
        }

        if (StringUtils.isNotBlank(viewId)) {
           request.setAttribute(UifParameters.VIEW_ID, viewId);
        }

        return viewId;
    }

    /**
     * Configures the <code>ModelAndView</code> instance containing the form
     * data and pointing to the UIF generic spring view
     *
     * @param form - Form instance containing the model data
     * @param pageId - Id of the page within the view that should be rendered, can
     * be left blank in which the current or default page is rendered
     * @return ModelAndView object with the contained form
     */
    public static ModelAndView getUIFModelAndView(UifFormBase form, String pageId) {
        if (StringUtils.isNotBlank(pageId)) {
            form.setPageId(pageId);
        }

        // create the spring return object pointing to View.jsp
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(UifConstants.DEFAULT_MODEL_NAME, form);
        modelAndView.setViewName(UifConstants.SPRING_VIEW_ID);

        return modelAndView;
    }

    /**
     * After the controller logic is executed, the form is placed into session
     * and the corresponding view is prepared for rendering
     */
    public static void postControllerHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        Object model = modelAndView.getModelMap().get(UifConstants.DEFAULT_MODEL_NAME);
        if (!(model instanceof UifFormBase)) {
            return;
        }

        UifFormBase form = (UifFormBase) model;

        // handle view building if not a redirect
        if (!form.isRequestRedirected()) {
            if (!form.isJsonRequest() && !form.isOriginalComponentRequest()) {
                prepareViewForRendering(request, form);
            }

            // export the component to the request model if an update id has been set
            Component component = null;
            if (StringUtils.isNotBlank(form.getUpdateComponentId())) {
                component = form.getPostedView().getViewIndex().getComponentById(form.getUpdateComponentId());
            } else if (form.isUpdatePageRequest()) {
                component = form.getView().getCurrentPage();
            }

            if (form.isOriginalComponentRequest()) {
                // This needs to be done because scenarios where the templates are not present
                updateViewTemplates(component, form);
            }

            String changeProperties = request.getParameter(UifParameters.CHANGE_PROPERTIES);

            // Change properties on the the final component
            if (StringUtils.isNotBlank(changeProperties) && component != null){
                HashMap<String,Object> changePropertiesMap = new ObjectMapper().readValue(changeProperties,
                        HashMap.class);

                for (String changePropertyPath : changePropertiesMap.keySet()){
                    Object value = changePropertiesMap.get(changePropertyPath);
                    ObjectPropertyUtils.setPropertyValue(component, changePropertyPath, value);
                }
            }

            if (component != null) {
                modelAndView.addObject(UifConstants.COMPONENT_MODEL_NAME, component);
            }
        }

        // expose additional objects to the templates
        modelAndView.addObject(UifParameters.REQUEST, request);
        modelAndView.addObject(KRADConstants.USER_SESSION_KEY, request.getSession().getAttribute(
                KRADConstants.USER_SESSION_KEY));

        Map<String, String> properties = CoreApiServiceLocator.getKualiConfigurationService().getAllProperties();
        modelAndView.addObject(UifParameters.CONFIG_PROPERTIES, properties);

        //modelAndView.addObject(UifParameters.STRING_RENDER_CONTEXT, new StringRenderContext());
    }

    /**
     * Update the view templates with the ones necessary to render the currentComponent passed in
     *
     * @param currentComponent the component to use to update the templates
     * @param form the current form
     */
    protected static void updateViewTemplates(Component currentComponent, UifFormBase form){
        List<Component> components = ComponentUtils.getAllNestedComponents(currentComponent);
        components.add(currentComponent);
        for (Component component : components){
            // add the components template to the views list of components
            if (!component.isSelfRendered() && StringUtils.isNotBlank(component.getTemplate()) &&
                    !form.getPostedView().getViewTemplates().contains(component.getTemplate())) {
                form.getPostedView().getViewTemplates().add(component.getTemplate());
            }

            if (component instanceof Container) {
                LayoutManager layoutManager = ((Container) component).getLayoutManager();

                if ((layoutManager != null)
                        && !form.getPostedView().getViewTemplates().contains(layoutManager.getTemplate())) {
                    form.getPostedView().getViewTemplates().add(layoutManager.getTemplate());
                }
            }
        }
    }

    /**
     * Prepares the <code>View</code> instance contained on the form for rendering
     *
     * @param request - request object
     * @param form - form instance containing the data and view instance
     */
    public static void prepareViewForRendering(HttpServletRequest request, UifFormBase form) {
        // for component refreshes only lifecycle for component is performed
        if (form.isUpdateComponentRequest() || form.isUpdateDialogRequest()) {
            String refreshComponentId = form.getUpdateComponentId();

            View postedView = form.getPostedView();

            // check if the component is nested in a box layout in order to reapply the layout item style
            boolean boxLayoutHorizontalItem = false;
            boolean boxLayoutVerticalItem = false;

            if (form.isUpdateComponentRequest()) {
                Component postedComponent = ComponentUtils.findNestedComponentById(postedView, refreshComponentId);
                if (postedComponent != null && postedComponent.getCssClasses() != null &&
                        postedComponent.getCssClasses().contains("uif-boxLayoutHorizontalItem")) {
                    boxLayoutHorizontalItem = true;
                } else if (postedComponent != null && postedComponent.getCssClasses() != null &&
                        postedComponent.getCssClasses().contains("uif-boxLayoutVerticalItem")) {
                    boxLayoutVerticalItem = true;
                }
            }

            // get a new instance of the component
            Component comp = ComponentFactory.getNewInstanceForRefresh(form.getPostedView(), refreshComponentId);

            // run lifecycle and update in view
            postedView.getViewHelperService().performComponentLifecycle(postedView, form, comp, refreshComponentId);

            // TODO: this should be in ViewHelperServiceImpl#performComponentLifecycle where other
            // adjustments are made, and it should use constants

            // add the layout item style that should happen in the parent BoxLayoutManager
            // and is skipped in a child component refresh
            if (boxLayoutHorizontalItem) {
                comp.addStyleClass("uif-boxLayoutHorizontalItem");
            } else if (boxLayoutVerticalItem) {
                comp.addStyleClass("uif-boxLayoutVerticalItem");
            }

            // regenerate server message content for page
            postedView.getCurrentPage().getValidationMessages().generateMessages(false, postedView, form,
                    postedView.getCurrentPage());
        } else {
            // full view build
            View view = form.getView();

            // set view page to page requested on form
            if (StringUtils.isNotBlank(form.getPageId())) {
                view.setCurrentPageId(form.getPageId());
            }

            Map<String, String> parameterMap = KRADUtils.translateRequestParameterMap(request.getParameterMap());
            parameterMap.putAll(form.getViewRequestParameters());

            // build view which will prepare for rendering
            getViewService().buildView(view, form, parameterMap);
        }
    }

    /**
     * Remove unused forms from breadcrumb history
     *
     * <p>
     * When going back in the breadcrumb history some forms become unused in the breadcrumb history.  Here the unused
     * forms are being determine and removed from the server to free memory.
     * </p>
     *
     * @param uifFormManager
     * @param formKey of the current form
     * @param lastFormKey of the last form
     */
    public static void removeUnusedBreadcrumbs(UifFormManager uifFormManager, String formKey, String lastFormKey) {
        if (StringUtils.isBlank(formKey) || StringUtils.isBlank(lastFormKey) || StringUtils.equals(formKey,
                lastFormKey)) {
            return;
        }

        UifFormBase previousForm = uifFormManager.getSessionForm(lastFormKey);
        if (previousForm == null) {
            return;
        }

        uifFormManager.removeSessionFormByKey(lastFormKey);
    }

    protected static ViewService getViewService() {
        return KRADServiceLocatorWeb.getViewService();
    }
}
