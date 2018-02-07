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
package org.kuali.rice.krad.demo.uif.controller;

import org.kuali.rice.krad.demo.uif.form.KradSampleAppForm;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.view.ViewTheme;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic controller for the KRAD sample application
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/kradsampleapp")
public class KradSampleAppController extends UifControllerBase {

    @Override
    protected KradSampleAppForm createInitialForm(HttpServletRequest request) {
        return new KradSampleAppForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        //TODO tbd
        return super.start(form, result, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=changeTheme")
    public ModelAndView changeTheme(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        changeTheme(form);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=validateView")
    public ModelAndView validateView(@ModelAttribute("KualiForm") UifFormBase uiTestForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        KRADServiceLocatorWeb.getViewValidationService().validateView(uiTestForm);
        return getUIFModelAndView(uiTestForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addGrowl")
    public ModelAndView addGrowl(@ModelAttribute("KualiForm") UifFormBase uiTestForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        String extraInfo = (String)request.getParameter("extraInfo");
        if(extraInfo == null){
            extraInfo = "none";
        }
        GlobalVariables.getMessageMap().addGrowlMessage("Growl Message", "demo.fakeGrowl", extraInfo);
        return getUIFModelAndView(uiTestForm);
    }

    private void changeTheme(UifFormBase form) {
        String theme = ((KradSampleAppForm) form).getThemeName();
        if (theme != null) {
            ViewTheme newTheme = (ViewTheme) (KRADServiceLocatorWeb.getDataDictionaryService().getDictionaryObject(
                    theme));
            if (newTheme != null) {
                form.getPostedView().setTheme(newTheme);
                form.getView().setTheme(newTheme);
            }
        }
    }
}
