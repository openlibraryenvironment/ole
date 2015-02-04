/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.module.purap.PurapConstants.PurapDocTypeCodes;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;

import java.sql.Timestamp;

/**
 * Payment Request Summary Account Business Object.
 */
public class OleInvoiceAccountsPayableSummaryAccount extends InvoiceAccount {

    private Integer creditMemoIdentifier;
    private Timestamp updateTimestamp;
    private Integer invoiceIdentifier;

    public OleInvoiceAccountsPayableSummaryAccount() {
    }

    public OleInvoiceAccountsPayableSummaryAccount(SourceAccountingLine account, Integer purapDocumentIdentifier, String docType) {

        if (PurapDocTypeCodes.INVOICE_DOCUMENT.equals(docType)) {
            this.setInvoiceIdentifier(purapDocumentIdentifier);
        } else if (PurapDocTypeCodes.CREDIT_MEMO_DOCUMENT.equals(docType)) {
            this.setCreditMemoIdentifier(purapDocumentIdentifier);
        }
        this.setChartOfAccountsCode(account.getChartOfAccountsCode());
        this.setAccountNumber(account.getAccountNumber());
        this.setSubAccountNumber(account.getSubAccountNumber());
        this.setFinancialObjectCode(account.getFinancialObjectCode());
        this.setFinancialSubObjectCode(account.getFinancialSubObjectCode());
        this.setProjectCode(account.getProjectCode());
        this.setOrganizationReferenceId(account.getOrganizationReferenceId());
        this.setAmount(account.getAmount());
        this.setSequenceNumber(account.getSequenceNumber());
        this.setUpdateTimestamp(SpringContext.getBean(DateTimeService.class).getCurrentTimestamp());
    }

    /**
     * ItemIdentifier is not a valid field in this table because it is the summary of accounts for the document, not per item
     *
     * @deprecated
     */
    @Override
    public Integer getItemIdentifier() {
        return super.getItemIdentifier();
    }

    /**
     * ItemIdentifier is not a valid field in this table because it is the summary of accounts for the document, not per item
     *
     * @deprecated
     */
    @Override
    public void setItemIdentifier(Integer itemIdentifier) {
        super.setItemIdentifier(itemIdentifier);
    }

    public Integer getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    public void setInvoiceIdentifier(Integer invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
    }


    public Integer getCreditMemoIdentifier() {
        return creditMemoIdentifier;
    }

    public void setCreditMemoIdentifier(Integer creditMemoIdentifier) {
        this.creditMemoIdentifier = creditMemoIdentifier;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

}
