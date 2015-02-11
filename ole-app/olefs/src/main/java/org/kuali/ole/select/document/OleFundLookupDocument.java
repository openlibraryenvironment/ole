/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License,  Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing,  software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,  either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.select.businessobject.OleFundLookup;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.krad.document.TransactionalDocumentBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the document Class for Acquisition Search
 */
public class OleFundLookupDocument extends TransactionalDocumentBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleFundLookupDocument.class);

    private String chartOfAccountsCode;
    private String accountNumber;
    private String objectCode;
    private String organizationCode;
    private String keyword;
    private String universityFiscalYear;
    private boolean active;
    private Chart chart;
    private Account account;
    private ObjectCode financialObject;
    private String accountName;
    private String intialFundBalance;
    private String cashBalance;
    private String freeBalance;
    private String intialBudgetAllocation;
    private String carryOverBalance;
    private String transfered;
    private String netAllocation;
    private String encumbrances;
    private String sumPaidInvoice;
    private String sumUnpaidInvoice;
    private String expendedPercentage;
    private String expenEncumPercentage;
    private List<OleFundLookup> finalResults = new ArrayList<OleFundLookup>();


    public OleFundLookupDocument() {
        Date fiscalYear = new Date();
        universityFiscalYear = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear().toString();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
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

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ObjectCode getFinancialObject() {
        return financialObject;
    }

    public void setFinancialObject(ObjectCode financialObject) {
        this.financialObject = financialObject;
    }

    public String getIntialFundBalance() {
        return intialFundBalance;
    }

    public void setIntialFundBalance(String intialFundBalance) {
        this.intialFundBalance = intialFundBalance;
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

    public List<OleFundLookup> getFinalResults() {
        return finalResults;
    }

    public void setFinalResults(List<OleFundLookup> finalResults) {
        this.finalResults = finalResults;
    }


}
