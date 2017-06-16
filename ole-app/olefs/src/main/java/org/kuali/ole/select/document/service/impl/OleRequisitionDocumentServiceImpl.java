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

import org.apache.log4j.Logger;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.gl.businessobject.AccountBalance;
import org.kuali.ole.gl.businessobject.Balance;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderAccount;
import org.kuali.ole.select.businessobject.OleRequisitionAccount;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleRequisitionDocumentServiceImpl implements OleRequisitionDocumentService {
    protected static final Logger LOG = Logger.getLogger(OleInvoiceFundCheckServiceImpl.class);
    protected ConfigurationService kualiConfigurationService;
    private OleSelectDocumentService oleSelectDocumentService;
    private BusinessObjectService businessObjectService;

    /*@Override
    public boolean hasSufficientFundsOnRequisition(SourceAccountingLine accLine) {
        boolean hasSufficientFundRequired = false;
        String chartCode = accLine.getChartOfAccountsCode();
        String accNo = accLine.getAccountNumber();
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        int currentFiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        String objectCd = accLine.getFinancialObjectCode();
        String fundCodeType = getFundCode(chartCode, accNo);
     //   KualiDecimal budgetAllocation = KualiDecimal.ZERO;
        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal glPendingAmt = KualiDecimal.ZERO;
        Map<String, Object> fundKey = new HashMap<String, Object>();
        String notificationOption = null;
        fundKey.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        fundKey.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
        OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                OleSufficientFundCheck.class, fundKey);
        if (account != null) {
            notificationOption = account.getNotificationOption();
        }

        if (fundCodeType != null && fundCodeType.equals(OLEConstants.ACCOUNT_FUND_CODE)) {

            if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                    || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
              //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
            //    encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
           //     budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                    || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
              //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
              //  encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(accLine.getAmount());
              //  budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }

            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }

        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
            if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                    || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
              //  budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                //    encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
            //    budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                    || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
              //  budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                // encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
              //  budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
               // encumbranceAmt = encumbranceAmt.subtract(accLine.getAmount());
            }
            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForObject(chartCode, accNo, objectCd, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }
        }
        return hasSufficientFundRequired;
    }
*/
    public boolean hasSufficientFundsOnRequisition(SourceAccountingLine accLine, String notificationOption,int currentFiscalYear) {
        boolean hasSufficientFundRequired = false;
        String chartCode = accLine.getChartOfAccountsCode();
        String accNo = accLine.getAccountNumber();
        //UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        //int currentFiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        String objectCd = accLine.getFinancialObjectCode();
        String fundCodeType = getFundCode(chartCode, accNo);
        //   KualiDecimal budgetAllocation = KualiDecimal.ZERO;
        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal glPendingAmt = KualiDecimal.ZERO;
        /*Map<String, Object> fundKey = new HashMap<String, Object>();
        String notificationOption = null;
        fundKey.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        fundKey.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
        OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                OleSufficientFundCheck.class, fundKey);
        if (account != null) {
            notificationOption = account.getNotificationOption();
        }*/

        if (fundCodeType != null && fundCodeType.equals(OLEConstants.ACCOUNT_FUND_CODE)) {

            if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                    || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                //    encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
                //     budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                    || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                //  encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(accLine.getAmount());
                //  budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }

            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }

        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
            if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                    || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                //  budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                //    encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
                //    budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                    || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                //  budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                // encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
                //  budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
                // encumbranceAmt = encumbranceAmt.subtract(accLine.getAmount());
            }
            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForObject(chartCode, accNo, objectCd, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }
        }
        return hasSufficientFundRequired;
    }


    private String getFundCode(String chartCode, String accountNumber) {
        Map accountMap = new HashMap();
        accountMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        accountMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
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

    private KualiDecimal getFundBalanceForAccount(String chartCode, String accountNumber, int currentFiscalYear) {
        KualiDecimal budgetAmt = KualiDecimal.ZERO;
        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal amount = KualiDecimal.ZERO;
        Map objCodeMap = new HashMap();
        objCodeMap.put(OLEConstants.FINANCIAL_OBJECT_TYPE_CODE, OLEConstants.BALANCE_TYPE_EXTERNAL_ENCUMBRANCE);
        objCodeMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, currentFiscalYear);
        List<ObjectCode> objCodeList = (List<ObjectCode>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(ObjectCode.class, objCodeMap);
        if (objCodeList.size() > 0) {
            for (ObjectCode objectCode : objCodeList) {
                Map encMap = new HashMap();
                encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
                encMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, currentFiscalYear);
                encMap.put(OLEConstants.OBJECT_CODE, objectCode.getFinancialObjectCode());
                List<AccountBalance> encumList = (List<AccountBalance>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(AccountBalance.class, encMap);
                if (encumList.size() > 0) {
                    for (AccountBalance acctBalance : encumList) {
                        budgetAmt = budgetAmt.add(acctBalance.getCurrentBudgetLineBalanceAmount());
                        Map fundCheckMap = new HashMap();
                        fundCheckMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                        fundCheckMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
                        OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                                .findByPrimaryKey(OleSufficientFundCheck.class, fundCheckMap);
                        if (oleSufficientFundCheck != null) {
                            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                                    .getEncumbExpenseConstraintType())) {
                                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                                        .getEncumbExpenseMethod())) {
                                    budgetAmt = budgetAmt.add((budgetAmt.multiply(amount))
                                            .divide(new KualiDecimal(100)));
                                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                                        .getEncumbExpenseMethod())) {
                                    budgetAmt = budgetAmt.subtract((budgetAmt.multiply(amount))
                                            .divide(new KualiDecimal(100)));

                                }
                            } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_CASH.equals(oleSufficientFundCheck
                                    .getEncumbExpenseConstraintType())) {
                                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                                        .getEncumbExpenseMethod())) {
                                    budgetAmt = budgetAmt.add(amount);
                                } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                                        .getEncumbExpenseMethod())) {
                                    budgetAmt = budgetAmt.subtract(amount);
                                }
                            }
                        }
                        fundBalanceAmt = fundBalanceAmt.add(acctBalance.getAccountLineEncumbranceBalanceAmount().add(acctBalance.getAccountLineActualsBalanceAmount()));
                        fundBalanceAmt = budgetAmt.subtract(fundBalanceAmt);
                    }
                }
            }
        }
        return fundBalanceAmt;
    }

    private KualiDecimal getGLPendingAmtForObject(String chartCode, String accountNumber, String objectCode,int currentFiscalYear) {
        Map encMap = new HashMap();
        Map itemMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        encMap.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, objectCode);
        encMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, currentFiscalYear);

        KualiDecimal glPendingAmt = KualiDecimal.ZERO;
        List<GeneralLedgerPendingEntry> encumList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, encMap);
        if (encumList.size() > 0) {
            for (GeneralLedgerPendingEntry glEntry : encumList) {
                if(glEntry.getTransactionDebitCreditCode().equals("D")) {
                    glPendingAmt = glPendingAmt.add(glEntry.getTransactionLedgerEntryAmount());
                }
                else {
                    glPendingAmt = glPendingAmt.subtract(glEntry.getTransactionLedgerEntryAmount());
                }

            }
        }
        return glPendingAmt;
    }

    private KualiDecimal getGLPendingAmtForAccount(String chartCode, String accountNumber, int currentFiscalYear) {
        Map encMap = new HashMap();
        Map itemMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        encMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, currentFiscalYear);
        encMap.put(OLEPropertyConstants.TRANSACTION_ENTRY_OFFSET_INDICATOR, Boolean.FALSE);


        KualiDecimal glPendingAmt = KualiDecimal.ZERO;
        List<GeneralLedgerPendingEntry> encumList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, encMap);
        if (encumList.size() > 0) {
            for (GeneralLedgerPendingEntry glEntry : encumList) {
                if(glEntry.getTransactionDebitCreditCode().equals("D")) {
                    glPendingAmt = glPendingAmt.add(glEntry.getTransactionLedgerEntryAmount());
                }
                else {
                    glPendingAmt = glPendingAmt.subtract(glEntry.getTransactionLedgerEntryAmount());
                }

            }
        }
        return glPendingAmt;
    }

    private KualiDecimal getFundBalanceForObject(String chartCode, String accountNumber, String objectCode,int currentFiscalYear) {
        Map encMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        encMap.put(OLEPropertyConstants.OBJECT_CODE, objectCode);
        encMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, currentFiscalYear);

        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal budgetAmt = KualiDecimal.ZERO;
        KualiDecimal amount = KualiDecimal.ZERO;
        List<AccountBalance> encumList = (List<AccountBalance>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(AccountBalance.class, encMap);
        if (encumList.size() > 0) {
            for (AccountBalance acctBalance : encumList) {
                budgetAmt = budgetAmt.add(acctBalance.getCurrentBudgetLineBalanceAmount());
                Map fundCheckMap = new HashMap();
                fundCheckMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                fundCheckMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
                OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                        .findByPrimaryKey(OleSufficientFundCheck.class, fundCheckMap);
                if (oleSufficientFundCheck != null) {
                    if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                            .getEncumbExpenseConstraintType())) {
                        amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                        if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                                .getEncumbExpenseMethod())) {
                            budgetAmt = budgetAmt.add((budgetAmt.multiply(amount))
                                    .divide(new KualiDecimal(100)));
                        } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                                .getEncumbExpenseMethod())) {
                            budgetAmt = budgetAmt.subtract((budgetAmt.multiply(amount))
                                    .divide(new KualiDecimal(100)));

                        }
                    } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_CASH.equals(oleSufficientFundCheck
                            .getEncumbExpenseConstraintType())) {
                        amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                        if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                                .getEncumbExpenseMethod())) {
                            budgetAmt = budgetAmt.add(amount);
                        } else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                                .getEncumbExpenseMethod())) {
                            budgetAmt = budgetAmt.subtract(amount);
                        }
                    }
                }
                fundBalanceAmt = fundBalanceAmt.add(acctBalance.getAccountLineEncumbranceBalanceAmount().add(acctBalance.getAccountLineActualsBalanceAmount()));
                fundBalanceAmt = budgetAmt.subtract(fundBalanceAmt);
            }
        }
        return fundBalanceAmt;
    }


    /*private KualiDecimal getBudgetAllocationForObject(String chartCode, String accountNumber, String objectCode) {
        Map budgetMap = new HashMap();
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        budgetMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR,
                universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        budgetMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        budgetMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        budgetMap.put(OLEPropertyConstants.OBJECT_CODE, objectCode);
        budgetMap.put(OLEPropertyConstants.BALANCE_TYPE_CODE, OLEPropertyConstants.BAL_TYP_CODE);
        List<Balance> balanceList = (List<Balance>) SpringContext.getBean(BusinessObjectService.class).findMatching(Balance.class, budgetMap);
        KualiDecimal initialBudgetAmount = KualiDecimal.ZERO;
        if(balanceList.size() > 0) {
            for(Balance balance : balanceList) {
                if (balance != null) {
                    initialBudgetAmount = initialBudgetAmount.add(balance.getAccountLineAnnualBalanceAmount());
                }
            }
        }
        Map encMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                .findByPrimaryKey(OleSufficientFundCheck.class, encMap);
        KualiDecimal amount = KualiDecimal.ZERO;

        KualiDecimal budgetIncrease = getBudgetAdjustmentIncreaseForObject(chartCode,accountNumber,objectCode);
        KualiDecimal budgetDecrease = getBudgetAdjustmentDecreaseForObject(chartCode,accountNumber,objectCode);
        initialBudgetAmount = initialBudgetAmount.add(budgetIncrease).subtract(budgetDecrease);
        if (oleSufficientFundCheck != null) {
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
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
                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
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

    private KualiDecimal getEncumbranceForAccount(String chartCode, String accountNumber, String objectCode) {
        Map encMap = new HashMap();
        Map itemMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        KualiDecimal encumAmount = KualiDecimal.ZERO;
        List<OlePurchaseOrderAccount> encumList = (List<OlePurchaseOrderAccount>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(OlePurchaseOrderAccount.class, encMap);
        if (encumList.size() > 0) {
            for (OlePurchaseOrderAccount olePurchaseOrderAccount : encumList) {
                itemMap.put(OLEConstants.PO_ITEM_ID, olePurchaseOrderAccount.getItemIdentifier());
                List<PurchaseOrderItem> encumPoItemList = (List<PurchaseOrderItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(PurchaseOrderItem.class, itemMap);
                for(PurchaseOrderItem poItm: encumPoItemList)  {
                    try {
                        PurchaseOrderDocument encumPoDoc =  SpringContext.getBean(
                                BusinessObjectService.class).findBySinglePrimaryKey(PurchaseOrderDocument.class, poItm.getDocumentNumber());

                        if (encumPoDoc.getDocumentHeader().getDocumentNumber() == null) {
                            encumPoDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(encumPoDoc.getDocumentNumber()));
                        }
                        WorkflowDocument  workflowDocument = null;
                        if (GlobalVariables.getUserSession() == null) {
                            GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                        }
                        workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(encumPoDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                        if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException() | workflowDocument.isSaved())) {
                            encumAmount = encumAmount.add(olePurchaseOrderAccount.getAmount());
                        }
                    }
                    catch(Exception e) {
                        LOG.error("Exception while calculating the Sufficient Fund for Requisition Document"+e);
                        throw new RuntimeException(e);
                    }
                }
                itemMap.clear();
            }
        }
        return encumAmount;
    }



    private KualiDecimal getBudgetAllocationForAccount(String chartCode, String accountNumber, String objectCode) {
        Map budgetMap = new HashMap();
        KualiDecimal initialBudgetAmount = KualiDecimal.ZERO;
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        budgetMap.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR,
                universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        budgetMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        budgetMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        budgetMap.put(OLEPropertyConstants.BALANCE_TYPE_CODE, OLEPropertyConstants.BAL_TYP_CODE);
        List<Balance> balance = (List<Balance>) SpringContext.getBean(BusinessObjectService.class).findMatching(
                Balance.class, budgetMap);
        if (balance.size() > 0) {
            for (Balance budgetBalance : balance) {
                initialBudgetAmount = initialBudgetAmount.add(budgetBalance.getAccountLineAnnualBalanceAmount());
            }
        }
        Map encMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        OleSufficientFundCheck oleSufficientFundCheck = SpringContext.getBean(BusinessObjectService.class)
                .findByPrimaryKey(OleSufficientFundCheck.class, encMap);
        KualiDecimal amount = KualiDecimal.ZERO;
        KualiDecimal budgetIncrease = getBudgetAdjustmentIncreaseForAccount(chartCode,accountNumber,objectCode);
        KualiDecimal budgetDecrease = getBudgetAdjustmentDecreaseForAccount(chartCode,accountNumber,objectCode);
        initialBudgetAmount.add(budgetIncrease).subtract(budgetDecrease);

        if (oleSufficientFundCheck != null) {
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
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
                amount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
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




    private KualiDecimal getEncumbranceForObject(String chartCode, String accountNumber, String objectCode) {
        Map encMap = new HashMap();
        Map itemMap = new HashMap();
        encMap.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        encMap.put(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        encMap.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, objectCode);
        KualiDecimal encumAmount = KualiDecimal.ZERO;
        List<OlePurchaseOrderAccount> encumList = (List<OlePurchaseOrderAccount>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(OlePurchaseOrderAccount.class, encMap);
        if (encumList.size() > 0) {
            for (OlePurchaseOrderAccount olePurchaseOrderAccount : encumList) {
                itemMap.put(OLEConstants.PO_ITEM_ID, olePurchaseOrderAccount.getItemIdentifier());
                List<PurchaseOrderItem> encumPoItemList = (List<PurchaseOrderItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(PurchaseOrderItem.class, itemMap);
                for(PurchaseOrderItem purchaseOrderItem: encumPoItemList)  {
                    try {
                        PurchaseOrderDocument encumPoDoc =  SpringContext.getBean(
                                BusinessObjectService.class).findBySinglePrimaryKey(PurchaseOrderDocument.class, purchaseOrderItem.getDocumentNumber());

                        if (encumPoDoc.getDocumentHeader().getDocumentNumber() == null) {
                            encumPoDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(encumPoDoc.getDocumentNumber()));
                        }
                        WorkflowDocument  workflowDocument = null;
                        if (GlobalVariables.getUserSession() == null) {
                            GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                        }
                        workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(encumPoDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                        if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException() || workflowDocument.isSaved())) {
                            encumAmount = encumAmount.add(olePurchaseOrderAccount.getAmount());
                        }
                    }
                    catch(Exception e) {
                        LOG.error("Exception while calculating the Sufficient Fund for Requisition Document"+e);
                        throw new RuntimeException(e);
                    }
                }
                itemMap.clear();
            }
        }
        return encumAmount;
    }
*/


    public boolean hasSufficientFundsOnBlanketApproveRequisition(SourceAccountingLine accLine, String notificationOption,int currentFiscalYear) {

        boolean hasSufficientFundRequired = false;
        String chartCode = accLine.getChartOfAccountsCode();
        String accNo = accLine.getAccountNumber();
        String objectCd = accLine.getFinancialObjectCode();
        String fundCodeType = getFundCode(chartCode, accNo);
        //  KualiDecimal budgetAllocation = KualiDecimal.ZERO;
        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal glPendingAmt = KualiDecimal.ZERO;

        if (fundCodeType != null && fundCodeType.equals(OLEConstants.ACCOUNT_FUND_CODE)) {

            if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)) {
                //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                //   encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
                //        budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }

            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }

        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
            if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)) {
                //    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                //  encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd,currentFiscalYear);
                //     budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }
            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            }   else {
                glPendingAmt = getGLPendingAmtForObject(chartCode, accNo, objectCd, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }
        }
        return hasSufficientFundRequired;
    }



    /*public boolean hasSufficientFundsOnBlanketApproveRequisition(SourceAccountingLine accLine) {
        boolean hasSufficientFundRequired = false;
        String chartCode = accLine.getChartOfAccountsCode();
        String accNo = accLine.getAccountNumber();
        String objectCd = accLine.getFinancialObjectCode();
        String fundCodeType = getFundCode(chartCode, accNo);
      //  KualiDecimal budgetAllocation = KualiDecimal.ZERO;
        KualiDecimal fundBalanceAmt = KualiDecimal.ZERO;
        KualiDecimal glPendingAmt = KualiDecimal.ZERO;
        Map<String, Object> fundKey = new HashMap<String, Object>();
        String notificationOption = null;
        fundKey.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        fundKey.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
        OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                OleSufficientFundCheck.class, fundKey);
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        int currentFiscalYear =  universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        if (account != null) {
            notificationOption = account.getNotificationOption();
        }

        if (fundCodeType != null && fundCodeType.equals(OLEConstants.ACCOUNT_FUND_CODE)) {

            if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)) {
              //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
             //   encumbranceAmt = getEncumbranceForAccount(chartCode, accNo, objectCd);
                fundBalanceAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
        //        budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }

            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            } else {
                glPendingAmt = getGLPendingAmtForAccount(chartCode, accNo, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    hasSufficientFundRequired = false;

                }
            }

        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
           if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)) {
            //    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
              //  encumbranceAmt = getEncumbranceForObject(chartCode, accNo, objectCd);
               fundBalanceAmt = getFundBalanceForObject(chartCode, accNo, objectCd,currentFiscalYear);
           //     budgetAllocation = budgetAllocation.subtract(encumbranceAmt);
            }
            if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                hasSufficientFundRequired = true;
            }   else {
                    glPendingAmt = getGLPendingAmtForObject(chartCode, accNo, objectCd, currentFiscalYear);
                fundBalanceAmt = fundBalanceAmt.subtract(glPendingAmt);
                    if (accLine.getAmount().isGreaterThan(fundBalanceAmt)) {
                        hasSufficientFundRequired = true;
                    } else {
                        hasSufficientFundRequired = false;

                    }
                }
            }
        return hasSufficientFundRequired;
    }*/


   /* public KualiDecimal getBudgetAdjustmentIncreaseForObject(String chartCode, String accountNo,
                                          String objectCode) {
        Map searchMap = new HashMap();
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("accountNumber", accountNo);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("financialDocumentTypeCode", OLEConstants.DOC_TYP_CD);
        searchMap.put("financialBalanceTypeCode", OLEConstants.BAL_TYP_CD);
        searchMap.put("financialDocumentApprovedCode", OLEConstants.FDOC_APPR_CD);
        List<GeneralLedgerPendingEntry> generalLedgerPendingEntryList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, searchMap);
        KualiDecimal budgetIncrease = KualiDecimal.ZERO;
        if (generalLedgerPendingEntryList.size() > 0) {
            for (GeneralLedgerPendingEntry entry : generalLedgerPendingEntryList) {
                if (entry.getTransactionLedgerEntryAmount().isGreaterThan(KualiDecimal.ZERO)) {
                    budgetIncrease = budgetIncrease.add(entry.getTransactionLedgerEntryAmount());
                }
        }
        }

        return budgetIncrease;
    }

    public KualiDecimal getBudgetAdjustmentDecreaseForObject(String chartCode, String accountNo,
                                            String objectCode) {
        Map searchMap = new HashMap();
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("accountNumber", accountNo);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("financialDocumentTypeCode", OLEConstants.DOC_TYP_CD);
        searchMap.put("financialBalanceTypeCode", OLEConstants.BAL_TYP_CD);
        searchMap.put("financialDocumentApprovedCode", OLEConstants.FDOC_APPR_CD);
        List<GeneralLedgerPendingEntry> generalLedgerPendingEntryList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, searchMap);
        KualiDecimal budgetDecrease = KualiDecimal.ZERO;
        if (generalLedgerPendingEntryList.size() > 0) {
            for (GeneralLedgerPendingEntry entry : generalLedgerPendingEntryList) {
                if (entry.getTransactionLedgerEntryAmount().isLessThan(KualiDecimal.ZERO)) {
                    budgetDecrease = budgetDecrease.add(entry.getTransactionLedgerEntryAmount());
                }
            }
        }

        return budgetDecrease.negated();
    }


    public KualiDecimal getBudgetAdjustmentIncreaseForAccount(String chartCode, String accountNo,
                                                    String objectCode) {
        Map searchMap = new HashMap();
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("accountNumber", accountNo);
        searchMap.put("financialDocumentTypeCode", OLEConstants.DOC_TYP_CD);
        searchMap.put("financialBalanceTypeCode", OLEConstants.BAL_TYP_CD);
        searchMap.put("financialDocumentApprovedCode", OLEConstants.FDOC_APPR_CD);
        List<GeneralLedgerPendingEntry> generalLedgerPendingEntryList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, searchMap);
        KualiDecimal budgetIncrease = KualiDecimal.ZERO;
        if (generalLedgerPendingEntryList.size() > 0) {
            for (GeneralLedgerPendingEntry entry : generalLedgerPendingEntryList) {
                if (entry.getTransactionLedgerEntryAmount().isGreaterThan(KualiDecimal.ZERO)) {
                    budgetIncrease = budgetIncrease.add(entry.getTransactionLedgerEntryAmount());
                }
            }
        }

        return budgetIncrease;
    }

    public KualiDecimal getBudgetAdjustmentDecreaseForAccount(String chartCode, String accountNo,
                                                    String objectCode) {
        Map searchMap = new HashMap();
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("accountNumber", accountNo);
        searchMap.put("financialDocumentTypeCode", OLEConstants.DOC_TYP_CD);
        searchMap.put("financialBalanceTypeCode", OLEConstants.BAL_TYP_CD);
        searchMap.put("financialDocumentApprovedCode", OLEConstants.FDOC_APPR_CD);
        List<GeneralLedgerPendingEntry> generalLedgerPendingEntryList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, searchMap);
        KualiDecimal budgetDecrease = KualiDecimal.ZERO;
        if (generalLedgerPendingEntryList.size() > 0) {
            for (GeneralLedgerPendingEntry entry : generalLedgerPendingEntryList) {
                if (entry.getTransactionLedgerEntryAmount().isLessThan(KualiDecimal.ZERO)) {
                    budgetDecrease = budgetDecrease.add(entry.getTransactionLedgerEntryAmount());
                }
            }
        }

        return budgetDecrease.negated();
    }
*/
    public OleSelectDocumentService getOleSelectDocumentService() {
        if(oleSelectDocumentService == null){
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }

    public String getParameter(String key) {
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID_OLE, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT, key);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public String getPurchaseOrderTypes() {
        List<PurchaseOrderType> purchaseOrderTypes = ( List<PurchaseOrderType>)getBusinessObjectService().findAll(PurchaseOrderType.class);
        StringBuffer orderTypes = new StringBuffer();
        if(purchaseOrderTypes.size() > 0) {
            for(PurchaseOrderType purchaseOrderType : purchaseOrderTypes) {
                orderTypes.append(purchaseOrderType.getPurchaseOrderTypeId().toString());
                orderTypes.append(":");
                orderTypes.append(purchaseOrderType.getPurchaseOrderType());
                orderTypes.append(";");
            }
        }
        return orderTypes.toString();
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

}
