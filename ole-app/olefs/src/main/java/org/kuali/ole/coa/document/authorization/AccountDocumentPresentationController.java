/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.coa.document.authorization;

import java.util.Set;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.document.authorization.FinancialSystemMaintenanceDocumentPresentationControllerBase;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.krad.util.GlobalVariables;

public class AccountDocumentPresentationController extends FinancialSystemMaintenanceDocumentPresentationControllerBase {


    @Override
    public Set<String> getConditionallyReadOnlyPropertyNames(MaintenanceDocument document) {
        Set<String> conditionallyReadonlyPropertyNames = super.getConditionallyReadOnlyPropertyNames(document);
        Account vendor = (Account) document.getNewMaintainableObject().getBusinessObject();

        String nameSpaceCode = OLEConstants.Account.ACCOUNT_NAMESPACE;

        boolean hasPermission = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Account.ACCOUNT_RESTRICTIONS);
        boolean hasPermissionVendorFlag = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Account.ACCOUNT_SUFFICIENT_FUND);

            if (!hasPermission) {
                conditionallyReadonlyPropertyNames.add(OLEConstants.Account.ACCOUNT_RESTRICTED_STATUS_CD);
            }
            if (!hasPermissionVendorFlag) {
                conditionallyReadonlyPropertyNames.add(OLEConstants.Account.ACCOUNT_SUFFICIENT_FUND_CODE);
            }
            return conditionallyReadonlyPropertyNames;
    }


}
