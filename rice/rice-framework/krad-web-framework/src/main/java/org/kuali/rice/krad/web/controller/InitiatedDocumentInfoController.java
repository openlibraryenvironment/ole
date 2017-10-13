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
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.krad.web.form.InitiatedDocumentInfoForm;
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
 * Controller for <code>MaintenanceDocumentView</code> screens which operate on
 * <code>MaintenanceDocument</code> instances
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value="/initdocinfo")

public class InitiatedDocumentInfoController extends UifControllerBase {
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(InitiatedDocumentInfoController.class);

    @Override
    public InitiatedDocumentInfoForm createInitialForm(HttpServletRequest request) {

        InitiatedDocumentInfoForm initiatedDocumentInfoForm = new InitiatedDocumentInfoForm();

        return initiatedDocumentInfoForm;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm")UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        InitiatedDocumentInfoForm initiatedDocumentInfoForm = (InitiatedDocumentInfoForm) form;
        return super.start(form, result, request, response);
    }
}
