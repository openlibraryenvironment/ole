/*
 * Copyright 2013 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.SufficientFundsCode;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;
import java.util.LinkedHashMap;


public class OleSufficientFundCheck extends PersistableBusinessObjectBase implements MutableInactivatable {

    private Integer sufficientFundCheckId;
    private String chartOfAccountsCode;
    private String accountNumber;
    private String encumbExpenseMethod;
    private String encumbExpenseConstraintType;
    private BigDecimal encumbranceAmount;
    private BigDecimal expenseAmount;
    // private String encumbExpenseType;
    /*
     * private String expenseMethod; private String expenseConstraintType; private BigDecimal expenseAmount; private String
     * expenseType;
     */
    private Account account;
    private boolean active;
    private OleSufficientFundsCheckType oleSufficientFundsCheckType;
    private SufficientFundsCode sufficientFundsCode;
    private String notificationOption;


    public Integer getSufficientFundCheckId() {
        return sufficientFundCheckId;
    }


    public void setSufficientFundCheckId(Integer sufficientFundCheckId) {
        this.sufficientFundCheckId = sufficientFundCheckId;
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


    /*
     * public String getExpenseMethod() { return expenseMethod; } public void setExpenseMethod(String expenseMethod) {
     * this.expenseMethod = expenseMethod; } public String getExpenseConstraintType() { return expenseConstraintType; } public void
     * setExpenseConstraintType(String expenseConstraintType) { this.expenseConstraintType = expenseConstraintType; } public
     * BigDecimal getExpenseAmount() { return expenseAmount; } public void setExpenseAmount(BigDecimal expenseAmount) {
     * this.expenseAmount = expenseAmount; } public String getExpenseType() { return expenseType; } public void
     * setExpenseType(String expenseType) { this.expenseType = expenseType; }
     */

    public String getEncumbExpenseMethod() {
        return encumbExpenseMethod;
    }


    public void setEncumbExpenseMethod(String encumbExpenseMethod) {
        this.encumbExpenseMethod = encumbExpenseMethod;
    }


    public String getEncumbExpenseConstraintType() {
        return encumbExpenseConstraintType;
    }


    public void setEncumbExpenseConstraintType(String encumbExpenseConstraintType) {
        this.encumbExpenseConstraintType = encumbExpenseConstraintType;
    }


    /*
     * public String getEncumbExpenseType() { return encumbExpenseType; } public void setEncumbExpenseType(String encumbExpenseType)
     * { this.encumbExpenseType = encumbExpenseType; }
     */


    public BigDecimal getEncumbranceAmount() {
        return encumbranceAmount;
    }


    public void setEncumbranceAmount(BigDecimal encumbranceAmount) {
        this.encumbranceAmount = encumbranceAmount;
    }


    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }


    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }


    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }


    public OleSufficientFundsCheckType getOleSufficientFundsCheckType() {
        return oleSufficientFundsCheckType;
    }

    public void setOleSufficientFundsCheckType(OleSufficientFundsCheckType oleSufficientFundsCheckType) {
        this.oleSufficientFundsCheckType = oleSufficientFundsCheckType;
    }


    public SufficientFundsCode getSufficientFundsCode() {
        return sufficientFundsCode;
    }

    public void setSufficientFundsCode(SufficientFundsCode sufficientFundsCode) {
        this.sufficientFundsCode = sufficientFundsCode;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String getNotificationOption() {
        return notificationOption;
    }


    public void setNotificationOption(String notificationOption) {
        this.notificationOption = notificationOption;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("sufficientFundCheckId", this.sufficientFundCheckId);
        return m;
    }

}