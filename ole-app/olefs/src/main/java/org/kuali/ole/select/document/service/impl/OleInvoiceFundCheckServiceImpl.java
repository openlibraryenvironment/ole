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
import org.kuali.ole.gl.businessobject.Balance;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OleInvoiceFundCheckService;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
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

public class OleInvoiceFundCheckServiceImpl implements OleInvoiceFundCheckService {
    protected static final Logger LOG = Logger.getLogger(OleInvoiceFundCheckServiceImpl.class);
    protected ConfigurationService kualiConfigurationService;
    private OleSelectDocumentService oleSelectDocumentService;

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
                budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                expenseAmt = getSumPaidInvoicesForAccount(chartCode, accNo, objectCd);
                budgetAllocation = budgetAllocation.subtract(expenseAmt);
            } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                    || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                expenseAmt = getSumPaidInvoicesForAccount(chartCode, accNo, objectCd);
                expenseAmt = expenseAmt.subtract(accLine.getAmount());
                budgetAllocation = budgetAllocation.subtract(expenseAmt);

            }
            if (accLine.getAmount().isGreaterThan(budgetAllocation)) {
                hasSufficientFundRequired = true;
            } else {
                hasSufficientFundRequired = false;
            }
        } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
                if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                        || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                    expenseAmt = getSumPaidInvoicesForObject(chartCode, accNo, objectCd);
                    budgetAllocation = budgetAllocation.subtract(expenseAmt);
                } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                        || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                    expenseAmt = getSumPaidInvoicesForObject(chartCode, accNo, objectCd);
                    expenseAmt = expenseAmt.subtract(accLine.getAmount());
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
        List<OleInvoiceDocument> invDocList = (List<OleInvoiceDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OleInvoiceDocument.class);
        if (invDocList.size() > 0) {
            for (OleInvoiceDocument oleInvoiceDocument : invDocList) {
                Integer payReqId = oleInvoiceDocument.getPurapDocumentIdentifier();
                Map docMap = new HashMap();
                docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OleInvoiceItem.class, docMap);
                HashMap acctMap = new HashMap();
                for (OleInvoiceItem oleInvoiceItem : itemList) {
                    Integer itemIdentifier = oleInvoiceItem.getItemIdentifier();
                    acctMap.put("itemIdentifier", oleInvoiceItem.getItemIdentifier());
                    acctMap.put("chartOfAccountsCode", chartCode);
                    acctMap.put("accountNumber", accountNo);
                    List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                            .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                    for (InvoiceAccount invAcct : oleInvoiceAccount) {
                        try {
                            OleInvoiceDocument paidInvDoc =  SpringContext.getBean(
                                    BusinessObjectService.class).findBySinglePrimaryKey(OleInvoiceDocument.class, oleInvoiceItem.getPurapDocumentIdentifier());

                            if (paidInvDoc.getDocumentHeader().getDocumentNumber() == null) {
                                paidInvDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(paidInvDoc.getDocumentNumber()));
                            }
                            WorkflowDocument workflowDocument = null;
                            //GlobalVariables.setUserSession(new UserSession("ole-khuntley"));
                            String user = null;
                            if (GlobalVariables.getUserSession() == null) {
                                /*kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                                user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                                GlobalVariables.setUserSession(new UserSession(user));*/
                                GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                            }


                            workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(paidInvDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                            if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException())) {
                                paidInvoices = paidInvoices.add(invAcct.getAmount());
                            }
                        }
                        catch(Exception e) {
                            LOG.error("Exception while calculating the Sufficient Fund for Invoice Document"+e);
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        List<OlePaymentRequestDocument> payDocList = (List<OlePaymentRequestDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OlePaymentRequestDocument.class);
        if (payDocList.size() > 0) {
            for (OlePaymentRequestDocument olePaymentRequestDocument : payDocList) {
                Integer invIdentifier = olePaymentRequestDocument.getInvoiceIdentifier();
                Integer payReqId = olePaymentRequestDocument.getPurapDocumentIdentifier();
                if (invIdentifier == null) {
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
                            try {
                                OlePaymentRequestDocument paidPaymentDoc =  SpringContext.getBean(
                                        BusinessObjectService.class).findBySinglePrimaryKey(OlePaymentRequestDocument.class, olePaymentRequestItem.getPurapDocumentIdentifier());

                                if (paidPaymentDoc.getDocumentHeader().getDocumentNumber() == null) {
                                    paidPaymentDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(paidPaymentDoc.getDocumentNumber()));
                                }
                                WorkflowDocument workflowDocument = null;
                                //GlobalVariables.setUserSession(new UserSession("ole-khuntley"));
                                String user = null;
                                if (GlobalVariables.getUserSession() == null) {
                                    /*kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                                    user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                                    GlobalVariables.setUserSession(new UserSession(user));*/
                                    GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                                }

                                workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(paidPaymentDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                                if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException())) {
                                    paidInvoices = paidInvoices.add(payReqAcct.getAmount());
                                }
                            }
                            catch(Exception e) {
                                LOG.error("Exception while calculating the Sufficient Fund for Invoice Document"+e);
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }


        return paidInvoices;
    }


    public KualiDecimal getSumPaidInvoicesForObject(String chartCode, String accountNo, String objectCode) {
        KualiDecimal paidInvoices = KualiDecimal.ZERO;
        List<OleInvoiceDocument> invDocList = (List<OleInvoiceDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OleInvoiceDocument.class);
        if (invDocList.size() > 0) {
            for (OleInvoiceDocument oleInvoiceDocument : invDocList) {
                Integer payReqId = oleInvoiceDocument.getPurapDocumentIdentifier();
                Map docMap = new HashMap();
                docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OleInvoiceItem.class, docMap);
                HashMap acctMap = new HashMap();
                for (OleInvoiceItem oleInvoiceItem : itemList) {
                    acctMap.put("itemIdentifier", oleInvoiceItem.getItemIdentifier());
                    acctMap.put("chartOfAccountsCode", chartCode);
                    acctMap.put("accountNumber", accountNo);
                    acctMap.put("financialObjectCode", objectCode);
                    List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                            .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                    for (InvoiceAccount invAcct : oleInvoiceAccount) {

                        try {
                                OleInvoiceDocument paidInvDoc =  SpringContext.getBean(
                                        BusinessObjectService.class).findBySinglePrimaryKey(OleInvoiceDocument.class, oleInvoiceItem.getPurapDocumentIdentifier());

                            if (paidInvDoc.getDocumentHeader().getDocumentNumber() == null) {
                                paidInvDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(paidInvDoc.getDocumentNumber()));
                            }
                                WorkflowDocument workflowDocument = null;
                                //lobalVariables.setUserSession(new UserSession("ole-khuntley"));
                            String user = null;
                            if (GlobalVariables.getUserSession() == null) {
                                /*kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                                user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                                GlobalVariables.setUserSession(new UserSession(user));*/
                                GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                            }
                                workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(paidInvDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                            if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException())) {
                                    paidInvoices = paidInvoices.add(invAcct.getAmount());
                            }
                        }
                            catch(Exception e) {
                                LOG.error("Exception while calculating the Sufficient Fund for Invoice Document"+e);
                                throw new RuntimeException(e);
                        }
                     }
                }
            }

        }


        List<OlePaymentRequestDocument> payDocList = (List<OlePaymentRequestDocument>) SpringContext.getBean(
                BusinessObjectService.class).findAll(OlePaymentRequestDocument.class);
        if (payDocList.size() > 0) {
            for (OlePaymentRequestDocument olePaymentRequestDocument : payDocList) {
                Integer invIdentifier = olePaymentRequestDocument.getInvoiceIdentifier();
                Integer payReqId = olePaymentRequestDocument.getPurapDocumentIdentifier();
                if (invIdentifier == null) {
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
                            try {
                                OlePaymentRequestDocument paidPaymentDoc =  SpringContext.getBean(
                                        BusinessObjectService.class).findBySinglePrimaryKey(OlePaymentRequestDocument.class, olePaymentRequestItem.getPurapDocumentIdentifier());

                                if (paidPaymentDoc.getDocumentHeader().getDocumentNumber() == null) {
                                    paidPaymentDoc.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(paidPaymentDoc.getDocumentNumber()));
                                }
                                WorkflowDocument workflowDocument = null;
                                //GlobalVariables.setUserSession(new UserSession("ole-khuntley"));
                                String user = null;
                                if (GlobalVariables.getUserSession() == null) {
                                    /*kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                                    user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                                    GlobalVariables.setUserSession(new UserSession(user));*/
                                    GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR)));
                                }
                                workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(paidPaymentDoc.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                                if(!(workflowDocument.isCanceled() || workflowDocument.isDisapproved() || workflowDocument.isException())) {
                                    paidInvoices = paidInvoices.add(payReqAcct.getAmount());
                                }
                            }
                            catch(Exception e) {
                                LOG.error("Exception while calculating the Sufficient Fund for Invoice Document"+e);
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
        return paidInvoices;
    }

    private KualiDecimal getBudgetAllocationForObject(String chartCode, String accountNumber, String objectCode) {
        Map budgetMap = new HashMap();
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
        KualiDecimal budgetIncrease = getBudgetAdjustmentIncreaseForObject(chartCode,accountNumber,objectCode);
        KualiDecimal budgetDecrease = getBudgetAdjustmentDecreaseForObject(chartCode,accountNumber,objectCode);
        initialBudgetAmount = initialBudgetAmount.add(budgetIncrease).subtract(budgetDecrease);
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
       return  initialBudgetAmount;
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
        KualiDecimal budgetIncrease = getBudgetAdjustmentIncreaseForAccount(chartCode,accountNumber,objectCode);
        KualiDecimal budgetDecrease = getBudgetAdjustmentDecreaseForAccount(chartCode,accountNumber,objectCode);
        initialBudgetAmount = initialBudgetAmount.add(budgetIncrease).subtract(budgetDecrease);
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

    public boolean isBudgetReviewRequired(OleInvoiceDocument oleInvoiceDocument) {
    	
    	 if((SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
             return false;
         }
        List<SourceAccountingLine> sourceAccountingLineList = oleInvoiceDocument.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            Map searchMap = new HashMap();
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.BLOCK_USE)) {
                sufficientFundCheck = hasSufficientFundCheckRequired(accLine);
                if (sufficientFundCheck) {
                    GlobalVariables.getMessageMap().putError(
                            OLEConstants.SufficientFundCheck.ERROR_MSG_FOR_INSUFF_FUND, RiceKeyConstants.ERROR_CUSTOM,
                            OLEConstants.SufficientFundCheck.INSUFF_FUND_INV + accLine.getAccountNumber());
                    return sufficientFundCheck;
                }
            }
        }

        return sufficientFundCheck;
    }
    public KualiDecimal getBudgetAdjustmentIncreaseForObject(String chartCode, String accountNo,
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

    public OleSelectDocumentService getOleSelectDocumentService() {
        if(oleSelectDocumentService == null){
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }

}