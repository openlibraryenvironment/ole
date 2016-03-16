/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.web.struts;

import org.kuali.ole.select.document.OleFundLookupDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;

/**
 * This class is the Form Class for Acquisitions Search
 */
public class OleFundLookupForm extends KualiTransactionalDocumentFormBase {

    private String chartOfAccountsCode;
    private String fundCode;
    private String fundCodeId;
    private String accountNumber;
    private String objectCode;
    private String organizationCode;
    private String keyword;
    private String universityFiscalYear;
    private boolean active;
    private String url;


    private String intialBudgetAllocation;
    private String carryOverBalance;
    private String transfered;
    private String netAllocation;
    private String encumbrances;
    private String sumPaidInvoice;
    private String sumUnpaidInvoice;
    private String expendedPercentage;
    private String expenEncumPercentage;
    private String cashBalance;
    private String freeBalance;
    private OleFundLookupDocument oleFundLookupDocument = new OleFundLookupDocument();


    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_FLU";
    }

    public OleFundLookupDocument getOleFundLookupDocument() {
        return (OleFundLookupDocument) getDocument();
    }

    public void setOleFundLookupDocument(OleFundLookupDocument oleFundLookupDocument) {
        this.oleFundLookupDocument = oleFundLookupDocument;
    }

    public String getUrl() {
        return OLEConstants.DOC_HANDLER_URL;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundCodeId() {
        return fundCodeId;
    }

    public void setFundCodeId(String fundCodeId) {
        this.fundCodeId = fundCodeId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUniversityFiscalYear() {
        return universityFiscalYear;
    }

    public void setUniversityFiscalYear(String universityFiscalYear) {
        this.universityFiscalYear = universityFiscalYear;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIntialBudgetAllocation() {
        return intialBudgetAllocation;
    }

    public void setIntialBudgetAllocation(String intialBudgetAllocation) {
        this.intialBudgetAllocation = intialBudgetAllocation;
    }

    public String getCarryOverBalance() {
        return carryOverBalance;
    }

    public void setCarryOverBalance(String carryOverBalance) {
        this.carryOverBalance = carryOverBalance;
    }

    public String getTransfered() {
        return transfered;
    }

    public void setTransfered(String transfered) {
        this.transfered = transfered;
    }

    public String getNetAllocation() {
        return netAllocation;
    }

    public void setNetAllocation(String netAllocation) {
        this.netAllocation = netAllocation;
    }

    public String getEncumbrances() {
        return encumbrances;
    }

    public void setEncumbrances(String encumbrances) {
        this.encumbrances = encumbrances;
    }

    public String getSumPaidInvoice() {
        return sumPaidInvoice;
    }

    public void setSumPaidInvoice(String sumPaidInvoice) {
        this.sumPaidInvoice = sumPaidInvoice;
    }

    public String getSumUnpaidInvoice() {
        return sumUnpaidInvoice;
    }

    public void setSumUnpaidInvoice(String sumUnpaidInvoice) {
        this.sumUnpaidInvoice = sumUnpaidInvoice;
    }

    public String getExpendedPercentage() {
        return expendedPercentage;
    }

    public void setExpendedPercentage(String expendedPercentage) {
        this.expendedPercentage = expendedPercentage;
    }

    public String getExpenEncumPercentage() {
        return expenEncumPercentage;
    }

    public void setExpenEncumPercentage(String expenEncumPercentage) {
        this.expenEncumPercentage = expenEncumPercentage;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(String freeBalance) {
        this.freeBalance = freeBalance;
    }


}
