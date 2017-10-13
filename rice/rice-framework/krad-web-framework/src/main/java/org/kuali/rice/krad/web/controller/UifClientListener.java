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

import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that receives various ajax requests from the client to manager server side state
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/listener")
public class UifClientListener extends UifControllerBase {

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new UifFormBase();
    }

    /**
     * Invoked from the client when the user is leaving a view (by the portal tabs or other mechanism) to clear
     * the form from session storage
     *
     * @param formKeyToClear key of form that should be cleared
     * @return String json success string
     */
    @RequestMapping(params = "methodToCall=clearForm")
    public
    @ResponseBody
    String clearForm(@RequestParam("formKeyToClear") String formKeyToClear, HttpServletRequest request,
            HttpServletResponse response) {

        // clear form from session
        GlobalVariables.getUifFormManager().removeFormWithHistoryFormsByKey(formKeyToClear);

        return "{\"status\":\"success\"}";
    }

    /**
     * Invoked from the client to retrieve text for a message
     *
     * @param key - key for the message
     * @return String response in JSON format containing the message text
     */
    @RequestMapping(params = "methodToCall=retrieveMessage")
    public
    @ResponseBody
    String retrieveMessage(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        // namespace and component are not required, therefore may be null
        String namespace = request.getParameter(KRADPropertyConstants.NAMESPACE_CODE);
        String componentCode = request.getParameter(KRADPropertyConstants.COMPONENT_CODE);

        String messageText = KRADServiceLocatorWeb.getMessageService().getMessageText(namespace, componentCode, key);

        if (messageText == null) {
            messageText = "";
        }
        else {
            messageText = ScriptUtils.escapeJSONString(messageText);
        }

        return "{\"messageText\":\"" + messageText + "\"}";
    }

    /**
     * Invoked from the session timeout warning dialog to keep a session alive on behalf of a user
     *
     * @return String json success string
     */
    @RequestMapping(params = "methodToCall=keepSessionAlive")
    public
    @ResponseBody
    String keepSessionAlive(HttpServletRequest request, HttpServletResponse response) {
        return "{\"status\":\"success\"}";
    }

    /**
     * Invoked from the session timeout warning dialog to log the user out, forwards to logout message view
     */
    @RequestMapping(params = "methodToCall=logout")
    public ModelAndView logout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();

        return getUIFModelAndViewWithInit(form, UifConstants.LOGGED_OUT_VIEW_ID);
    }

}
