/*
 * Copyright 2005-2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.web.struts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.businessobject.OleRequestor;
import org.kuali.ole.select.document.service.OleRequestorService;
import org.kuali.ole.select.document.service.impl.OleRequestorServiceImpl;
import org.kuali.ole.select.document.web.struts.PersonRequestorLookUpForm;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.action.KualiLookupAction;
import org.kuali.rice.kns.web.struts.form.KualiForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class handles Actions for lookup flow
 */

public class PersonRequestorLookUpAction extends KualiLookupAction {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PersonRequestorLookUpAction.class);

    /**
     * This method is take care of creating a New Requestor
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("PersonRequestorLookUpAction : createNew() method -- Starts");
        PersonRequestorLookUpForm personRequestorLookUpForm = (PersonRequestorLookUpForm) form;

        String firstName = personRequestorLookUpForm.getFirstName();
        String lastName = personRequestorLookUpForm.getLastName();

        if (firstName != null && lastName != null) {
            OleRequestor oleRequestor = new OleRequestor();
            oleRequestor.setRequestorFirstName(firstName);
            oleRequestor.setRequestorLastName(lastName);
            oleRequestor.setRequestorEmail(personRequestorLookUpForm.getEmail());
            oleRequestor.setRequestorPhoneNumber(personRequestorLookUpForm.getPhoneNumber());
            oleRequestor.setRequestorTypeId(personRequestorLookUpForm.getRequestorTypeId());
            oleRequestor.setRefKrimId(personRequestorLookUpForm.getRefKrimId());

            OleRequestorService oleRequestorService = SpringContext.getBean(OleRequestorServiceImpl.class);
            oleRequestorService.saveRequestor(oleRequestor);
            KNSGlobalVariables.getMessageList().add(RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL);
        } else {
            if (firstName == null) {
                KNSGlobalVariables.getMessageList().add(RiceKeyConstants.ERROR_CUSTOM, "First Name Field is a requried field");
            }
            if (lastName == null) {
                KNSGlobalVariables.getMessageList().add(RiceKeyConstants.ERROR_CUSTOM, "Last Name Field is a requried field");
            }

        }
        LOG.debug("PersonRequestorLookUpAction : createNew() method -- End");
        ((KualiForm) form).setMethodToCall("search");
        super.search(mapping, form, request, response);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }


}
