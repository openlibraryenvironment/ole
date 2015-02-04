/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.select.service.OleAccountService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleAccountServiceImpl implements OleAccountService {

    private BusinessObjectService businessObjectService;

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        }
        return businessObjectService;
    }

    /**
     * @see org.kuali.ole.select.service.OleAccountService#getTemporaryRestrictedAccounts()
     */
    @Override
    public List<Account> getTemporaryRestrictedAccounts() {
        // TODO Auto-generated method stub
        String accountRestrictedStatusCode = "T";
        Map TRestrictedAccountMap = new HashMap();
        TRestrictedAccountMap.put("accountRestrictedStatusCode", accountRestrictedStatusCode);
        businessObjectService = getBusinessObjectService();
        List<Account> accountList = (List<Account>) businessObjectService.findMatching(Account.class, TRestrictedAccountMap);
        return accountList;
    }

    /**
     * @see org.kuali.ole.select.service.OleAccountService#getAccountDetailsWithAccountNumber(java.lang.String)
     */
    @Override
    public Account getAccountDetailsWithAccountNumber(String accountNumber) {
        // TODO Auto-generated method stub
        Map accountNumberMap = new HashMap();
        accountNumberMap.put("accountNumber", accountNumber);
        businessObjectService = getBusinessObjectService();
        return (Account) businessObjectService.findMatching(Account.class, accountNumberMap);
    }

    /**
     * @see org.kuali.ole.select.service.OleAccountService#getAllAccountDetails()
     */
    @Override
    public List<Account> getAllAccountDetails() {
        // TODO Auto-generated method stub
        businessObjectService = getBusinessObjectService();
        return (List<Account>) businessObjectService.findAll(Account.class);
    }

    /**
     * @see org.kuali.ole.select.service.OleAccountService#getMatchingAccountDetails(java.util.Map)
     */
    @Override
    public List<Account> getMatchingAccountDetails(Map<String, String> parameters) {
        // TODO Auto-generated method stub
        businessObjectService = getBusinessObjectService();
        return (List<Account>) businessObjectService.findMatching(Account.class, parameters);
    }

    /**
     * @see org.kuali.ole.vnd.document.service.VendorService#shouldVendorRouteForApproval(java.lang.String)
     */
    @Override
    public boolean shouldAccountRouteForApproval(String documentId) {
        boolean shouldRoute = false;
        MaintenanceDocument newDoc = null;
        try {
            DocumentService documentService = KRADServiceLocatorWeb.getDocumentService();
            newDoc = (MaintenanceDocument) documentService.getByDocumentHeaderId(documentId);
        } catch (WorkflowException we) {
            throw new RuntimeException("A WorkflowException was thrown which prevented the loading of " + "the comparison document (" + documentId + ")", we);
        }

        if (ObjectUtils.isNotNull(newDoc)) {
            Account account = (Account) newDoc.getNewMaintainableObject().getBusinessObject();
            if (account.getSubFundGroupCode() != null && !account.getSubFundGroupCode().isEmpty() &&
                    account.getSubFundGroupCode().equals(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                shouldRoute = true;
            }
        }
        return shouldRoute;
    }


}
