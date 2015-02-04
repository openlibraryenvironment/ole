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

package org.kuali.ole.module.purap.businessobject;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.util.PurApObjectUtils;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;

/**
 * Payment Request Account Business Object.
 */
public class InvoiceAccount extends PurApAccountingLineBase {

    private KualiDecimal disencumberedAmount = KualiDecimal.ZERO;
    private KualiDecimal existingAmount;
    private BigDecimal bigDecimalAmount;

    /**
     * Default constructor.
     */
    public InvoiceAccount() {
        this.setAmount(null);
        this.setAccountLinePercent(null);
        this.setSequenceNumber(0);
    }

    /**
     * Constructor.
     *
     * @param item - payment request item
     * @param poa  - purchase order account
     */
    public InvoiceAccount(InvoiceItem item, PurchaseOrderAccount poa) {
        this();
        // copy base attributes
        PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, poa, this);
        // copy percent
        this.setSequenceNumber(poa.getSequenceNumber());
        this.setAccountLinePercent(poa.getAccountLinePercent());
        setItemIdentifier(item.getItemIdentifier());
        setInvoiceItem(item);
    }

    public KualiDecimal getDisencumberedAmount() {
        return disencumberedAmount;
    }

    public void setDisencumberedAmount(KualiDecimal disencumberedAmount) {
        this.disencumberedAmount = disencumberedAmount;
    }

    public InvoiceItem getInvoiceItem() {
        return super.getPurapItem();
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        super.setPurapItem(invoiceItem);
    }

    /**
     * Caller of this method should take care of creating InvoiceItems
     */
    public void copyFrom(InvoiceAccount other) {
        super.copyFrom(other);
        setDisencumberedAmount(other.getDisencumberedAmount());
    }

    @Override
    public void setFinancialSubObjectCode(String financialSubObjectCode) {
        super.setFinancialSubObjectCode(StringUtils.trimToNull(financialSubObjectCode));
    }

    @Override
    public void setFinancialObjectCode(String financialObjectCode) {
        super.setFinancialObjectCode(StringUtils.trimToNull(financialObjectCode));
    }

    @Override
    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        super.setChartOfAccountsCode(StringUtils.trimToNull(chartOfAccountsCode));
    }

    @Override
    public void setBalanceTypeCode(String balanceTypeCode) {
        super.setBalanceTypeCode(StringUtils.trimToNull(balanceTypeCode));
    }

    @Override
    public void setAccountNumber(String accountNumber) {
        super.setAccountNumber(StringUtils.trimToNull(accountNumber));
    }

    @Override
    public void setReferenceTypeCode(String referenceTypeCode) {
        super.setReferenceTypeCode(StringUtils.trimToNull(referenceTypeCode));
    }

    @Override
    public void setProjectCode(String projectCode) {
        super.setProjectCode(StringUtils.trimToNull(projectCode));
    }

    @Override
    public void setOverrideCode(String overrideCode) {
        super.setOverrideCode(StringUtils.trimToNull(overrideCode));
    }

    @Override
    public void setReferenceOriginCode(String originCode) {
        super.setReferenceOriginCode(StringUtils.trimToNull(originCode));
    }

    @Override
    public void setSubAccountNumber(String subAccountNumber) {
        super.setSubAccountNumber(StringUtils.trimToNull(subAccountNumber));
    }

    @Override
    public String getSubAccountNumber() {
        return StringUtils.trimToNull(super.getSubAccountNumber());
    }

    @Override
    public String getFinancialDocumentLineTypeCode() {
        return StringUtils.trimToNull(super.getFinancialDocumentLineTypeCode());
    }

    @Override
    public String getObjectTypeCode() {
        return StringUtils.trimToNull(super.getObjectTypeCode());
    }

    @Override
    public void setFinancialDocumentLineTypeCode(String financialDocumentLineTypeCode) {
        super.setFinancialDocumentLineTypeCode(StringUtils.trimToNull(financialDocumentLineTypeCode));
    }

    @Override
    public String getFinancialSubObjectCode() {
        return StringUtils.trimToNull(super.getFinancialSubObjectCode());
    }

    @Override
    public String getFinancialObjectCode() {
        return StringUtils.trimToNull(super.getFinancialObjectCode());
    }

    @Override
    public String getChartOfAccountsCode() {
        return StringUtils.trimToNull(super.getChartOfAccountsCode());
    }

    @Override
    public String getBalanceTypeCode() {
        return StringUtils.trimToNull(super.getBalanceTypeCode());
    }

    @Override
    public String getDebitCreditCode() {
        return StringUtils.trimToNull(super.getDebitCreditCode());
    }

    @Override
    public String getEncumbranceUpdateCode() {
        return StringUtils.trimToNull(super.getEncumbranceUpdateCode());
    }

    @Override
    public String getProjectCode() {
        return StringUtils.trimToNull(super.getProjectCode());
    }

    @Override
    public String getReferenceTypeCode() {
        return StringUtils.trimToNull(super.getReferenceTypeCode());
    }

    @Override
    public String getOverrideCode() {
        return StringUtils.trimToNull(super.getOverrideCode());
    }

    public KualiDecimal getExistingAmount() {
        return existingAmount;
    }

    public void setExistingAmount(KualiDecimal existingAmount) {
        this.existingAmount = existingAmount;
    }


    public BigDecimal getBigDecimalAmount() {
        if(this.getAmount()!=null){
            return this.getAmount().bigDecimalValue();
        }else{
            return bigDecimalAmount;
        }
    }

    public void setBigDecimalAmount(BigDecimal bigDecimalAmount) {
        if(bigDecimalAmount!=null){
            this.setAmount(new KualiDecimal(bigDecimalAmount));
        }else{
            this.bigDecimalAmount = bigDecimalAmount;
        }
    }
}
