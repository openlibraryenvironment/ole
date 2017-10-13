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
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Loads the module locked view when a user accesses a module which has been locked for maintenance
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
public class ModuleLockedController extends UifControllerBase {
    
    public static final String MODULE_PARAMETER = "moduleNamespace";
    
    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new UifFormBase();
    }

    /**
     * Retrieves the module locked message test from a system parameter and then returns the message view
     */
    @RequestMapping(value = "/module-locked")
    public ModelAndView moduleLocked(@ModelAttribute("KualiForm") UifFormBase form,
            @RequestParam(value = MODULE_PARAMETER, required = true) String moduleNamespaceCode) {
        ParameterService parameterSerivce = CoreFrameworkServiceLocator.getParameterService();
        
        String messageParamComponentCode = KRADConstants.DetailTypes.ALL_DETAIL_TYPE;
        String messageParamName = KRADConstants.SystemGroupParameterNames.OLTP_LOCKOUT_MESSAGE_PARM;
        String lockoutMessage = parameterSerivce.getParameterValueAsString(moduleNamespaceCode,
                messageParamComponentCode, messageParamName);

        if (StringUtils.isBlank(lockoutMessage)) {
            String defaultMessageParamName = KRADConstants.SystemGroupParameterNames.OLTP_LOCKOUT_DEFAULT_MESSAGE;
            lockoutMessage = parameterSerivce.getParameterValueAsString(KRADConstants.KNS_NAMESPACE,
                    messageParamComponentCode, defaultMessageParamName);
        }
        
        return getMessageView(form, "Module Locked", lockoutMessage);
    }

}
