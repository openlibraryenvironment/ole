/*
 * Copyright 2006 The Kuali Foundation
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
package org.kuali.ole.coa.document;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.coa.businessobject.AccountDelegateGlobal;
import org.kuali.ole.coa.businessobject.AccountDelegateGlobalDetail;
import org.kuali.ole.coa.businessobject.AccountGlobal;
import org.kuali.ole.coa.businessobject.AccountGlobalDetail;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.util.KfsDateUtils;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.DocumentService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class GlobalDocumentTest extends KualiTestBase {

    private static final Log LOG = LogFactory.getLog(GlobalDocumentTest.class);

    private static final String KNOWN_DOCUMENT_TYPENAME = "OLE_GDLG";
    private static final String GLOBAL_DELEGATE_TYPENAME = "OLE_GDLG";
    private static final String GLOBAL_ACCOUNT_TYPENAME = "OLE_GACC";


    @Test
    public void testGlobalDelegateMaintenanceDocumentCreation_goodDocTypeName() throws Exception {
        MaintenanceDocument doc = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(KNOWN_DOCUMENT_TYPENAME);
        assertNotNull(doc);
        assertNotNull(doc.getNewMaintainableObject());
        assertEquals("org.kuali.ole.coa.businessobject.AccountDelegateGlobal", doc.getNewMaintainableObject().getBoClass().getName());
    }

    @Test
    public final void testGetNewDocument_globalDelegateMaintDoc() throws Exception {

        MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(GLOBAL_DELEGATE_TYPENAME);

        // make sure the doc is setup
        assertNotNull(document);
        assertNotNull(document.getDocumentHeader());
        assertNotNull(document.getDocumentHeader().getDocumentNumber());

        assertEquals("Global document should always appear as a New.", true, document.isNew());
        assertEquals("Global document should never appear as an edit.", false, document.isEdit());

        Maintainable newMaintainable = document.getNewMaintainableObject();
        assertNotNull("New Maintainable should never be null.", newMaintainable);
        assertEquals("BO Class should be DelegateGlobal.", AccountDelegateGlobal.class, newMaintainable.getBoClass());

        PersistableBusinessObject newBo = newMaintainable.getBusinessObject();
        assertNotNull("New BO should never be null.", newBo);
        assertEquals("New BO should be of the correct class.", AccountDelegateGlobal.class, newBo.getClass());

    }

    @Test
    public final void testGetNewDocument_globalAccountMaintDoc() throws Exception {

        MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(GLOBAL_ACCOUNT_TYPENAME);

        // make sure the doc is setup
        assertNotNull(document);
        assertNotNull(document.getDocumentHeader());
        assertNotNull(document.getDocumentHeader().getDocumentNumber());

        assertEquals("Global document should always appear as a New.", true, document.isNew());
        assertEquals("Global document should never appear as an edit.", false, document.isEdit());

        Maintainable newMaintainable = document.getNewMaintainableObject();
        assertNotNull("New Maintainable should never be null.", newMaintainable);
        assertEquals("BO Class should be AccountGlobal.", AccountGlobal.class, newMaintainable.getBoClass());

        PersistableBusinessObject newBo = newMaintainable.getBusinessObject();
        assertNotNull("New BO should never be null.", newBo);
        assertEquals("New BO should be of the correct class.", AccountGlobal.class, newBo.getClass());
    }

    @Test
    public final void testSaveDocument_globalDelegate() throws Exception {

        MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(GLOBAL_DELEGATE_TYPENAME);

        // get local references to the Maintainable and the BO
        Maintainable newMaintainable = document.getNewMaintainableObject();
        AccountDelegateGlobal bo = (AccountDelegateGlobal) newMaintainable.getBusinessObject();
        String finDocNumber = document.getDocumentNumber();
        document.getDocumentHeader().setDocumentDescription("blah");

        System.err.println("DOC_NBR = " + finDocNumber);

        List<AccountDelegateGlobalDetail> changes = new ArrayList();

        AccountDelegateGlobalDetail change = new AccountDelegateGlobalDetail();
        change.setAccountDelegatePrimaryRoutingIndicator(false);
        change.setAccountDelegateStartDate(KfsDateUtils.newDate(2006, 6, 1));
        change.setAccountDelegateUniversalId("OLE6137600107");
        change.setApprovalFromThisAmount(KualiDecimal.ZERO);
        change.setApprovalToThisAmount(KualiDecimal.ZERO);
        change.setFinancialDocumentTypeCode(OLEConstants.ROOT_DOCUMENT_TYPE);
        changes.add(change);

        bo.setDelegateGlobals(changes);

        AccountGlobalDetail account;

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031400");
        bo.addAccount(account);

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031420");
        bo.addAccount(account);

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031467");
        bo.addAccount(account);
        SpringContext.getBean(DocumentService.class).saveDocument(document);
        
        document = (MaintenanceDocument)SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(document.getDocumentNumber());

        // now that it worked, lets cancel the doc so it doesnt lock for others
        SpringContext.getBean(DocumentService.class).cancelDocument(document, "cancelling test document");

    }

    @Test
    public final void testSaveAndLoadDocument_globalDelegate() throws Exception {

        MaintenanceDocument document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(GLOBAL_DELEGATE_TYPENAME);

        // get local references to the Maintainable and the BO
        Maintainable newMaintainable = document.getNewMaintainableObject();
        AccountDelegateGlobal bo = (AccountDelegateGlobal) newMaintainable.getBusinessObject();
        String finDocNumber = document.getDocumentNumber();
        document.getDocumentHeader().setDocumentDescription("blah");

        List<AccountDelegateGlobalDetail> changes = new ArrayList();

        AccountDelegateGlobalDetail change = new AccountDelegateGlobalDetail();
        change.setAccountDelegatePrimaryRoutingIndicator(false);
        change.setAccountDelegateStartDate(KfsDateUtils.newDate(2006, 6, 1));
        change.setAccountDelegateUniversalId("OLE6137600107");
        change.setApprovalFromThisAmount(KualiDecimal.ZERO);
        change.setApprovalToThisAmount(KualiDecimal.ZERO);
        change.setFinancialDocumentTypeCode(OLEConstants.ROOT_DOCUMENT_TYPE);
        changes.add(change);
        bo.setDelegateGlobals(changes);

        AccountGlobalDetail account;

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031400");
        bo.addAccount(account);

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031420");
        bo.addAccount(account);

        account = new AccountGlobalDetail();
        account.setDocumentNumber(finDocNumber);
        account.setChartOfAccountsCode("BL");
        account.setAccountNumber("1031467");
        bo.addAccount(account);

        SpringContext.getBean(DocumentService.class).saveDocument(document);

        // clear the document, and re-load it from the DB
        document = null;
        document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(finDocNumber);
        assertNotNull("Document should not be null after loaded from the DB.", document);
        assertNotNull("Document Header should not be null after loaded from the DB.", document.getDocumentHeader());
        assertNotNull("Document FinDocNumber should not be null after loaded from the DB.", document.getDocumentHeader().getDocumentNumber());

        // document should show up as a 'New' document
        assertEquals("Global document should always appear as a New.", true, document.isNew());
        assertEquals("Global document should never appear as an edit.", false, document.isEdit());

        // Maintainable should be populated and contain the right class
        newMaintainable = document.getNewMaintainableObject();
        assertNotNull("New Maintainable should never be null.", newMaintainable);
        assertEquals("BO Class should be DelegateGlobal.", AccountDelegateGlobal.class, newMaintainable.getBoClass());

        // BO should be non-null and the right class
        bo = (AccountDelegateGlobal) newMaintainable.getBusinessObject();
        assertNotNull("New BO should never be null.", bo);
        assertEquals("New BO should be of the correct class.", AccountDelegateGlobal.class, bo.getClass());

        // List should contain 3 elements
        assertNotNull("AccountDetail list should not be null.", bo.getAccountGlobalDetails());
        List accounts = bo.getAccountGlobalDetails();
        assertEquals("AccountDetail list should not be empty.", false, accounts.isEmpty());
        assertEquals("AccountDetail list should contain 3 elements.", 3, accounts.size());

        // make sure all the accounts are non-null and at least have the Chart populated
        for (Iterator iter = accounts.iterator(); iter.hasNext();) {
            AccountGlobalDetail accountDetail = (AccountGlobalDetail) iter.next();

            assertNotNull("AccountDetailChange should not be null.", accountDetail);
            assertNotNull("ChartOfAccountsCode", accountDetail.getChartOfAccountsCode());
            assertEquals("Account Chart should be known.", "BL", accountDetail.getChartOfAccountsCode());
        }

        // now that it worked, lets cancel the doc so it doesnt lock for others
        SpringContext.getBean(DocumentService.class).cancelDocument(document, "cancelling test document");

    }
}

