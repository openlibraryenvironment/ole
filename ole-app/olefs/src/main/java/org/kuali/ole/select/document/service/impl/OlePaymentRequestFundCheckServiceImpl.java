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
package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.gl.businessobject.Balance;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OlePaymentRequestFundCheckService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OlePaymentRequestFundCheckServiceImpl implements OlePaymentRequestFundCheckService {

    @Override
    public boolean hasSufficientFundCheckRequired(SourceAccountingLine accLine) {
        boolean hasSufficientFundRequired = false;
        Map searchMap = new HashMap();
        Map<String, Object> key = new HashMap<String, Object>();
        String chartCode = accLine.getChartOfAccountsCode();
        String accNo = accLine.getAccountNumber();
        String objectCd = accLine.getFinancialObjectCode();
        String fundCodeType = getFundCode(chartCode, accNo);
        KualiDecimal budgetAllocation = KualiDecimal.ZERO;
        KualiDecimal expenseAmt = KualiDecimal.ZERO;
        if (fundCodeType != null && fundCodeType.equals(OLEConstants.ACCOUNT_FUND_CODE)) {
            budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
            expenseAmt = getSumPaidInvoicesForAccount(chartCode, accNo, objectCd);
            expenseAmt = expenseAmt.subtract(accLine.getAmount());
            if (expenseAmt.isNegative()) {
                budgetAllocation = budgetAllocation.subtract(expenseAmt.negated());
            } else {
                budgetAllocation = budgetAllocation.subtract(expenseAmt);
            }
            if (accLine.getAmount().isGreaterThan(budgetAllocation)) {
                hasSufficientFundRequired = true;
            } else {
                hasSufficientFundRequired = false;
            }
        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
            budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
            expenseAmt = getSumPaidInvoicesForObject(chartCode, accNo, objectCd);
            expenseAmt = expenseAmt.subtract(accLine.getAmount());
            if (expenseAmt.isNegative()) {
                budgetAllocation = budgetAllocation.subtract(expenseAmt.negated());
            } else {
                budgetAllocation = budgetAllocation.subtract(expenseAmt);
            }
            if (accLine.getAmount().isGreaterThan(budgetAllocation)) {
                hasSufficientFundRequired = true;
            } else {
                hasSufficientFundRequired = false;
            }
        }
        return hasSufficientFundRequired;
    }

    private String getFundCode(String chartCode, String accountNumber) {
        Map accountMap = new HashMap();
        accountMap.put("chartOfAccountsCode", chartCode);
        accountMap.put("accountNumber", accountNumber);
        Account account = SpringContext.getBean(BusinessObjectService.class)
                .findByPrimaryKey(Account.class, accountMap);
        boolean fundCheckIndication = false;
        if (account != null) {
            fundCheckIndication = account.isPendingAcctSufficientFundsIndicator();
        }

        if (fundCheckIndication) {
            String fundCode = account.getAccountSufficientFundsCode();
            return fundCode;
        }
        return null;
    }

    public KualiDecimal getSumPaidInvoicesForAccount(String chartCode, String accountNo, String objectCode) {
        KualiDecimal paidInvoices = KualiDecimal.ZERO;
        List<OlePaymentRequestDocument> payDocList = (List<OlePaymentRequestDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OlePaymentRequestDocument.class);
        if (payDocList.size() > 0) {
            for (OlePaymentRequestDocument olePaymentRequestDocument : payDocList) {
                Integer payReqId = olePaymentRequestDocument.getPurapDocumentIdentifier();
                Map docMap = new HashMap();
                docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                List<OlePaymentRequestItem> itemList = (List<OlePaymentRequestItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OlePaymentRequestItem.class, docMap);
                HashMap acctMap = new HashMap();
                for (OlePaymentRequestItem olePaymentRequestItem : itemList) {
                    Integer itemIdentifier = olePaymentRequestItem.getItemIdentifier();
                    acctMap.put("itemIdentifier", olePaymentRequestItem.getItemIdentifier());
                    acctMap.put("chartOfAccountsCode", chartCode);
                    acctMap.put("accountNumber", accountNo);
                    List<PaymentRequestAccount> olePaymentRequestAccount = (List<PaymentRequestAccount>) SpringContext
                            .getBean(BusinessObjectService.class).findMatching(PaymentRequestAccount.class, acctMap);
                    for (PaymentRequestAccount payReqAcct : olePaymentRequestAccount) {
                        paidInvoices = paidInvoices.add(payReqAcct.getAmount());
                    }
                }
            }
        }
        return paidInvoices;
    }


    public KualiDecimal getSumPaidInvoicesForObject(String chartCode, String accountNo, String objectCode) {
        KualiDecimal paidInvoices = KualiDecimal.ZERO;
        List<OlePaymentRequestDocument> payDocList = (List<OlePaymentRequestDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OlePaymentRequestDocument.class);
        if (payDocList.size() > 0) {
            for (OlePaymentRequestDocument olePaymentRequestDocument : payDocList) {
                Integer payReqId = olePaymentRequestDocument.getPurapDocumentIdentifier();
                Map docMap = new HashMap();
                docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                List<OlePaymentRequestItem> itemList = (List<OlePaymentRequestItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OlePaymentRequestItem.class, docMap);
                HashMap acctMap = new HashMap();
                for (OlePaymentRequestItem olePaymentRequestItem : itemList) {
                    acctMap.put("itemIdentifier", olePaymentRequestItem.getItemIdentifier());
                    acctMap.put("chartOfAccountsCode", chartCode);
                    acctMap.put("accountNumber", accountNo);
                    acctMap.put("financialObjectCode", objectCode);
                    List<PaymentRequestAccount> olePaymentRequestAccount = (List<PaymentRequestAccount>) SpringContext
                            .getBean(BusinessObjectService.class).findMatching(PaymentRequestAccount.class, acctMap);
                    for (PaymentRequestAccount payReqAcct : olePaymentRequestAccount) {
                        paidInvoices = paidInvoices.add(payReqAcct.getAmount());
                    }
                }
            }
        }
        return paidInvoices;
    }

    private KualiDecimal getBudgetAllocationForObject(String chartCode, String accountNumber, String objectCode) {
        Map budgetMap = new HashMap();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String univFiscalYear = df.format(date);
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        budgetMap.put("universityFiscalYear",
                universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        budgetMap.put("chartOfAccountsCode", chartCode);
        budgetMap.put("accountNumber", accountNumber);
        budgetMap.put("objectCode", objectCode);
        budgetMap.put(OLEPropertyConstants.BALANCE_TYPE_CODE, OLEPropertyConstants.BAL_TYP_CODE);
        Balance balance = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Balance.class, budgetMap);
        KualiDecimal initialBudgetAmount = KualiDecimal.ZERO;
        if (balance != null) {
            initialBudgetAmount = balance.getAccountLineAnnualBalanceAmount();
        }
        Map encMap = new HashMap();
        encMap.put("chartOfAccountsCode", chartCode);
        encMap.put("accountNumber", accountNumber);
        OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                .findByPrimaryKey(OleSufficientFundCheck.class, encMap);
        KualiDecimal amount = KualiDecimal.ZERO;
        if (oleSufficientFundCheck != null) {
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.add((initialBudgetAmount.multiply(amount))
                            .divide(new KualiDecimal(100)));
                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.subtract((initialBudgetAmount.multiply(amount))
                            .divide(new KualiDecimal(100)));

                }
            } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_CASH.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.add(amount);
                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.subtract(amount);
                }
            }
        }
        return initialBudgetAmount;
    }

    private KualiDecimal getBudgetAllocationForAccount(String chartCode, String accountNumber, String objectCode) {
        Map budgetMap = new HashMap();
        KualiDecimal initialBudgetAmount = KualiDecimal.ZERO;
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        budgetMap.put("universityFiscalYear",
                universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        budgetMap.put("chartOfAccountsCode", chartCode);
        budgetMap.put("accountNumber", accountNumber);
        budgetMap.put(OLEPropertyConstants.BALANCE_TYPE_CODE, OLEPropertyConstants.BAL_TYP_CODE);
        List<Balance> balance = (List<Balance>) SpringContext.getBean(BusinessObjectService.class).findMatching(
                Balance.class, budgetMap);
        if (balance.size() > 0) {
            for (Balance budgetBalance : balance) {
                initialBudgetAmount = initialBudgetAmount.add(budgetBalance.getAccountLineAnnualBalanceAmount());
            }
        }
        Map encMap = new HashMap();
        encMap.put("chartOfAccountsCode", chartCode);
        encMap.put("accountNumber", accountNumber);
        OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                .findByPrimaryKey(OleSufficientFundCheck.class, encMap);
        KualiDecimal amount = KualiDecimal.ZERO;
        if (oleSufficientFundCheck != null) {
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.add((initialBudgetAmount.multiply(amount))
                            .divide(new KualiDecimal(100)));
                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.subtract((initialBudgetAmount.multiply(amount))
                            .divide(new KualiDecimal(100)));

                }
            } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_CASH.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.add(amount);
                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    initialBudgetAmount = initialBudgetAmount.subtract(amount);
                }
            }
        }
        return initialBudgetAmount;
    }


}