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
package org.kuali.rice.krad.labs.sessionPolicy;

import org.kuali.rice.krad.labs.KradLabsController;
import org.kuali.rice.krad.labs.KradLabsForm;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller class for the session policy lab view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/sessionPolicy")
public class LabsSessionPolicyController extends KradLabsController {

    @RequestMapping(params = "methodToCall=killSession")
    public ModelAndView killSession(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();

        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=sessionTimeout")
    public ModelAndView sessionTimeout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        GlobalVariables.getMessageMap().addGrowlMessage("Result", "labs.methodInvoked", "sessionTimeout");

        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=setSessionTimeout")
    public ModelAndView setSessionTimeout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        // for testing purposes set the session timeout to three minutes
        request.getSession().setMaxInactiveInterval(120000);
        ((KradLabsForm) form).setSessionTimeoutInterval(120000);

        return getUIFModelAndView(form);
    }
}
