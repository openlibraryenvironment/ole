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

import org.apache.log4j.Logger;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.web.form.HistoryManager;
import org.kuali.rice.krad.web.form.UifFormManager;
import org.kuali.rice.krad.uif.util.ProcessLogger;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring controller intercepter for KRAD controllers
 *
 * <p>
 * Provides infrastructure for preparing the form and view before and after the controller is invoked.
 * Included in this is form session management and preparation of the view for rendering
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifControllerHandlerInterceptor implements HandlerInterceptor {
    private static final Logger LOG = Logger.getLogger(UifControllerHandlerInterceptor.class);

    /**
     * Before the controller executes the user session is set on GlobalVariables
     * and messages are cleared, in addition setup for the history manager and a check on missing session
     * forms is performed
     *
     * TODO: do we need to clear the messages before this so that formatting and
     * validation errors done during the binding are not cleared out?
     *
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        final UserSession session = KRADUtils.getUserSessionFromRequest(request);

        GlobalVariables.setUserSession(session);
        GlobalVariables.clear();

        // add the HistoryManager for storing HistoryFlows to the session
        if (request.getSession().getAttribute(UifConstants.HistoryFlow.HISTORY_MANAGER) == null){
            request.getSession().setAttribute(UifConstants.HistoryFlow.HISTORY_MANAGER, new HistoryManager());
        }
        ProcessLogger.trace("pre-handle");

        return true;
    }

    /**
     * After the controller logic is executed, the form is placed into session
     * and the corresponding view is prepared for rendering
     *
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        UifControllerHelper.postControllerHandle(request, response, handler, modelAndView);
        ProcessLogger.trace("post-handle");
    }

    /**
     * After the view is rendered we can do some cleaning to reduce the size of the form storage in memory
     *
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        ProcessLogger.trace("after-completion");
        UifFormManager uifFormManager = (UifFormManager) request.getSession().getAttribute(UifParameters.FORM_MANAGER);
        UifFormBase uifForm = (UifFormBase) request.getAttribute(UifConstants.REQUEST_FORM);

        if ((uifForm == null) || (uifForm.getView() == null && uifForm.getPostedView() == null)) {
            return;
        }

        // perform form session handling
        boolean persistFormToSession = uifForm.getView() != null ? uifForm.getView().isPersistFormToSession() :
                uifForm.getPostedView().isPersistFormToSession();

        // cleaning of view structure
        if (uifForm.isRequestRedirected() || uifForm.isUpdateNoneRequest()) {
            // view wasn't rendered, just set to null and leave previous posted view
            uifForm.setView(null);
        } else if (uifForm.isBuildViewRequest()) {
            // full view render, clean view and back up
            View view = uifForm.getView();
            if (view != null) {
                view.getViewHelperService().cleanViewAfterRender(view);
            }

            uifForm.setPostedView(view);
            uifForm.setView(null);
        } else {
            // partial refresh on posted view
            View postedView = uifForm.getPostedView();
            if (postedView != null) {
                postedView.getViewHelperService().cleanViewAfterRender(postedView);
            }
        }

        // remove the session transient variables from the request form before adding it to the list of
        // Uif session forms
        if (persistFormToSession && (uifFormManager != null)) {
            uifFormManager.purgeForm(uifForm);
            uifFormManager.addSessionForm(uifForm);
        }
        ProcessLogger.trace("after-completion-end");
    }

}
