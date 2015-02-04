/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.document.web.struts;

import org.apache.struts.action.ActionForm;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiLookupAction;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;

public class KualiLookupActions extends KualiLookupAction {


    public void supressActionsIfNeeded(ActionForm form) throws ClassNotFoundException {
        Class businessObjectClass = Class.forName(((LookupForm) form).getBusinessObjectClassName());
        HashMap<String, String> permissionDetails = new HashMap<String, String>();
        String nameSpaceCode = "OLE-VND";
        if (businessObjectClass.getName().equalsIgnoreCase("org.kuali.ole.vnd.businessobject.VendorDetail")) {
            if ((SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                    "Edit Vendor"))) {

                if (!KNSServiceLocator.getDocumentHelperService().getDocumentAuthorizer("OLE_PVEN").canInitiate("OLE_PVEN", GlobalVariables.getUserSession().getPerson())) {
                    ((LookupForm) form).setSupplementalActionsEnabled(true);
                } else {
               //     super.supressActionsIfNeeded(form);
                }
            } else {
              //  super.supressActionsIfNeeded(form);
                System.out.println("aefasfasdfsadfsdaf");
            }
        } else {
            System.out.println("else part");
         //   super.supressActionsIfNeeded(form);
        }
    }
}

    /* System.out.println("1111111111111111111111111111111111");
System.out.println("1111111111111111111111111111111111"+( KIMServiceLocator.getIdentityManagementService().hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
        "Edit Vendor", permissionDetails )));
System.out.println(KNSServiceLocator.getDocumentHelperService().getDocumentAuthorizer("OLE_PVEN").canInitiate("OLE_PVEN", GlobalVariables.getUserSession().getPerson()));    */