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
package org.kuali.ole.select.batch;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.service.OleAccountService;
import org.kuali.ole.select.service.OleNotifyService;
import org.kuali.ole.select.service.impl.OleNotifyServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.*;

public class OleAccountTemporaryRestrictedStep extends AbstractStep {

    /**
     * @see org.kuali.ole.sys.batch.Step#execute(java.lang.String, java.util.Date)
     */
    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        // TODO Auto-generated method stub
        boolean notify = false;
        List<Account> TempRestrictedAccountDetails = SpringContext.getBean(OleAccountService.class).getTemporaryRestrictedAccounts();
        Map fiscalOfficerMap = new HashMap();

        Date currenDate = SpringContext.getBean(DateTimeService.class).getCurrentSqlDate();
        OleNotifyService service = SpringContext.getBean(OleNotifyServiceImpl.class);
        ConfigurationService kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);

        for (Account TRestrictedAccount : TempRestrictedAccountDetails) {
            String fiscalOfficerPrincipalName = TRestrictedAccount.getAccountFiscalOfficerUser().getPrincipalName();
            Date tempRestrictedAccountStatusDate = TRestrictedAccount.getAccountRestrictedStatusDate();
            if (tempRestrictedAccountStatusDate.compareTo(currenDate) < 0) {
                notify = true;
                List<String> user = new ArrayList<String>();
                user.add(fiscalOfficerPrincipalName);
                try {
                    service.notify(user, "Account Restricted Status Date for the Following Temporary Restricted AccountNumber : " + TRestrictedAccount.getAccountNumber() + " is equal to currentDate. You can update AccountRestrictedStatusCode or can extend AccountRestrictedStatusDate ");
                } catch (WorkflowException ex) {
                    // TODO Auto-generated catch block
                    throw new RuntimeException(ex);
                }

                try {

                    GlobalVariables.setUserSession(new UserSession(kualiConfigurationService.getPropertyValueAsString(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                    GlobalVariables.getUserSession().getPrincipalName();
                    editAccountTemporaryRestrictedRecordDocument(TRestrictedAccount);
                } catch (Exception ex) {
                    // TODO Auto-generated catch block
                    throw new RuntimeException(ex);
                }
            }
        }
        return notify;

    }


    public void editAccountTemporaryRestrictedRecordDocument(Account TRestrictedAccount) throws Exception {

        MaintenanceDocument accountMaintDoc = null;

        Account newAccountRecord = new Account();
        try {

            org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);

            DocumentService documentService = new DocumentServiceImpl();
            accountMaintDoc = (MaintenanceDocument) documentService.getNewDocument(OLEConstants.DocumentTypeAttributes.ACCOUNTING_DOCUMENT_TYPE_NAME);
            accountMaintDoc.getDocumentHeader().setDocumentDescription(OleSelectNotificationConstant.OLE_ACCOUNT_TEMP_RESTRICTED_EDIT_DOCUMENT_DESCRIPTION);
            accountMaintDoc.getOldMaintainableObject().setBusinessObject(TRestrictedAccount);
            newAccountRecord = (Account) ObjectUtils.deepCopy(TRestrictedAccount);
            accountMaintDoc.getNewMaintainableObject().setBusinessObject(newAccountRecord);
            accountMaintDoc.getNewMaintainableObject().setMaintenanceAction(OLEConstants.MAINTENANCE_EDIT_ACTION);
            accountMaintDoc.getNewMaintainableObject().setDocumentNumber(accountMaintDoc.getDocumentNumber());
            accountMaintDoc.validateBusinessRules(new RouteDocumentEvent(accountMaintDoc));
            documentService.routeDocument(accountMaintDoc, null, null);

        } catch (WorkflowException ex) {
            throw new RuntimeException(ex);
        }
    }

}

