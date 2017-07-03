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
import org.kuali.ole.module.purap.PurapParameterConstants;
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
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
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
            UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
            int currentFiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
            // KualiDecimal budgetAllocation = KualiDecimal.ZERO;
            KualiDecimal expenseAmt = KualiDecimal.ZERO;
            KualiDecimal invAmt = KualiDecimal.ZERO;
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
                    //       budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                    expenseAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);

                    //    budgetAllocation = budgetAllocation.subtract(expenseAmt);
                } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                        || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                    //  budgetAllocation = getBudgetAllocationForAccount(chartCode, accNo, objectCd);
                    expenseAmt = getFundBalanceForAccount(chartCode, accNo, currentFiscalYear);
                    expenseAmt = expenseAmt.subtract(accLine.getAmount());
                    //   budgetAllocation = budgetAllocation.subtract(expenseAmt);

                }
                if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    glPendingAmt = getGLPendingAmtForAccount(chartCode, accNo, currentFiscalYear);
                    expenseAmt = expenseAmt.subtract(glPendingAmt);
                    if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                        hasSufficientFundRequired = true;
                    } else {
                        invAmt = getSumPaidInvoicesForAccount(chartCode, accNo);
                        expenseAmt = expenseAmt.subtract(invAmt);
                        if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                            hasSufficientFundRequired = true;
                        } else {
                            hasSufficientFundRequired = false;
                        }
                    }
                }
            } else if (fundCodeType != null && fundCodeType.equals(OLEConstants.OBJECT_FUND_CODE)) {
                if (notificationOption.equals(OLEPropertyConstants.BLOCK_USE)
                        || notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                    //    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                    expenseAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
                    //    budgetAllocation = budgetAllocation.subtract(expenseAmt);
                } else if (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW)
                        || notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                    //    budgetAllocation = getBudgetAllocationForObject(chartCode, accNo, objectCd);
                    expenseAmt = getFundBalanceForObject(chartCode, accNo, objectCd, currentFiscalYear);
                    expenseAmt = expenseAmt.subtract(accLine.getAmount());
                    // budgetAllocation = budgetAllocation.subtract(expenseAmt);
                }
                if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                    hasSufficientFundRequired = true;
                } else {
                    glPendingAmt = getGLPendingAmtForObject(chartCode, accNo, objectCd, currentFiscalYear);
                    expenseAmt = expenseAmt.subtract(glPendingAmt);
                    if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                        hasSufficientFundRequired = true;
                    } else {
                        invAmt = getSumPaidInvoicesForObject(chartCode, accNo, objectCd);
                        expenseAmt = expenseAmt.subtract(invAmt);
                        if (accLine.getAmount().isGreaterThan(expenseAmt)) {
                            hasSufficientFundRequired = true;
                        } else {
                            hasSufficientFundRequired = false;

                        }
                    }
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

    public KualiDecimal getSumPaidInvoicesForAccount(String chartCode, String accountNo) {
        KualiDecimal paidInvoices = KualiDecimal.ZERO;

        Map docTypeMap = new HashMap();
        docTypeMap.put("name", "OLE_PRQS");
        docTypeMap.put("active", Boolean.TRUE);
        docTypeMap.put("currentInd", Boolean.TRUE);

        List<DocumentType>  documentTypeList = (List<DocumentType>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(DocumentType.class,docTypeMap);
        for(DocumentType documentType : documentTypeList) {
            Map docHdrMap = new HashMap();
            docHdrMap.put(OLEPropertyConstants.DOCUMENT_TYPE_ID, documentType.getDocumentTypeId());
            docHdrMap.put("docRouteStatus", "S");
            List<DocumentRouteHeaderValue> documentHeaderList = (List<DocumentRouteHeaderValue>) SpringContext.getBean(
                    BusinessObjectService.class).findMatching(DocumentRouteHeaderValue.class, docHdrMap);
            for (DocumentRouteHeaderValue documentHeader : documentHeaderList) {
                Map invMap = new HashMap();
                invMap.put(OLEConstants.DOC_NUMBER, documentHeader.getDocumentId());
                List<OleInvoiceDocument> invDocList = (List<OleInvoiceDocument>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OleInvoiceDocument.class,invMap);
                if (invDocList.size() > 0) {
                    for (OleInvoiceDocument oleInvoiceDocument : invDocList) {
                        Integer payReqId = oleInvoiceDocument.getPurapDocumentIdentifier();
                        Map docMap = new HashMap();
                        docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                        docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                        List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) SpringContext.getBean(
                                BusinessObjectService.class).findMatching(OleInvoiceItem.class, docMap);
                        if (itemList.size() > 0) {
                            HashMap acctMap = new HashMap();
                            for (OleInvoiceItem oleInvoiceItem : itemList) {
                                Integer itemIdentifier = oleInvoiceItem.getItemIdentifier();
                                acctMap.put("itemIdentifier", itemIdentifier);
                                acctMap.put("chartOfAccountsCode", chartCode);
                                acctMap.put("accountNumber", accountNo);
                                List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                                        .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                                if (oleInvoiceAccount.size() > 0) {
                                    for (InvoiceAccount invAcct : oleInvoiceAccount) {
                                        paidInvoices = paidInvoices.add(invAcct.getAmount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for(DocumentType documentType : documentTypeList) {
            Map docHdrMap = new HashMap();
            docHdrMap.put(OLEPropertyConstants.DOCUMENT_TYPE_ID, documentType.getDocumentTypeId());
            docHdrMap.put("docRouteStatus", "R");
            List<DocumentRouteHeaderValue> documentHeaderList = (List<DocumentRouteHeaderValue>) SpringContext.getBean(
                    BusinessObjectService.class).findMatching(DocumentRouteHeaderValue.class, docHdrMap);
            for (DocumentRouteHeaderValue documentHeader : documentHeaderList) {
                Map invMap = new HashMap();
                docTypeMap.put(OLEConstants.DOC_NUMBER, documentHeader.getDocumentId());
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
                        if (itemList.size() > 0) {
                            HashMap acctMap = new HashMap();
                            for (OleInvoiceItem oleInvoiceItem : itemList) {
                                Integer itemIdentifier = oleInvoiceItem.getItemIdentifier();
                                acctMap.put("itemIdentifier", itemIdentifier);
                                acctMap.put("chartOfAccountsCode", chartCode);
                                acctMap.put("accountNumber", accountNo);
                                List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                                        .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                                if (oleInvoiceAccount.size() > 0) {
                                    for (InvoiceAccount invAcct : oleInvoiceAccount) {
                                        paidInvoices = paidInvoices.add(invAcct.getAmount());
                                    }
                                }
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
        Map docTypeMap = new HashMap();
        docTypeMap.put("name", "OLE_PRQS");
        docTypeMap.put("active", Boolean.TRUE);
        docTypeMap.put("currentInd", Boolean.TRUE);
        List<DocumentType>  documentTypeList = (List<DocumentType>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(DocumentType.class,docTypeMap);
        for(DocumentType documentType : documentTypeList) {
            Map docHdrMap = new HashMap();
            docHdrMap.put(OLEPropertyConstants.DOCUMENT_TYPE_ID, documentType.getDocumentTypeId());
            docHdrMap.put("docRouteStatus", "S");
            List<DocumentRouteHeaderValue> documentHeaderList = (List<DocumentRouteHeaderValue>) SpringContext.getBean(
                    BusinessObjectService.class).findMatching(DocumentRouteHeaderValue.class, docHdrMap);
            for (DocumentRouteHeaderValue documentHeader : documentHeaderList) {
                Map invMap = new HashMap();
                invMap.put(OLEConstants.DOC_NUMBER, documentHeader.getDocumentId());
                List<OleInvoiceDocument> invDocList = (List<OleInvoiceDocument>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OleInvoiceDocument.class, invMap);
                if (invDocList.size() > 0) {
                    for (OleInvoiceDocument oleInvoiceDocument : invDocList) {
                        Integer payReqId = oleInvoiceDocument.getPurapDocumentIdentifier();
                        Map docMap = new HashMap();
                        docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                        docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                        List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) SpringContext.getBean(
                                BusinessObjectService.class).findMatching(OleInvoiceItem.class, docMap);
                        HashMap acctMap = new HashMap();
                        if (itemList.size() > 0) {
                            for (OleInvoiceItem oleInvoiceItem : itemList) {
                                acctMap.put("itemIdentifier", oleInvoiceItem.getItemIdentifier());
                                acctMap.put("chartOfAccountsCode", chartCode);
                                acctMap.put("accountNumber", accountNo);
                                acctMap.put("financialObjectCode", objectCode);
                                List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                                        .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                                if (oleInvoiceAccount.size() > 0) {
                                    for (InvoiceAccount invAcct : oleInvoiceAccount) {
                                        paidInvoices = paidInvoices.add(invAcct.getAmount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for(DocumentType documentType : documentTypeList) {
            Map docHdrMap = new HashMap();
            docHdrMap.put(OLEPropertyConstants.DOCUMENT_TYPE_ID, documentType.getDocumentTypeId());
            docHdrMap.put("docRouteStatus", "R");
            List<DocumentRouteHeaderValue> documentHeaderList = (List<DocumentRouteHeaderValue>) SpringContext.getBean(
                    BusinessObjectService.class).findMatching(DocumentRouteHeaderValue.class, docHdrMap);
            for (DocumentRouteHeaderValue documentHeader : documentHeaderList) {
                Map invMap = new HashMap();
                invMap.put(OLEConstants.DOC_NUMBER, documentHeader.getDocumentId());
                List<OleInvoiceDocument> invDocList = (List<OleInvoiceDocument>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OleInvoiceDocument.class, invMap);
                if (invDocList.size() > 0) {
                    for (OleInvoiceDocument oleInvoiceDocument : invDocList) {
                        Integer payReqId = oleInvoiceDocument.getPurapDocumentIdentifier();
                        Map docMap = new HashMap();
                        docMap.put(OLEConstants.PUR_AP_IDEN, payReqId);
                        docMap.put(OLEConstants.ITM_TYP_CD_KEY, OLEConstants.ITM_TYP_CD);
                        List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) SpringContext.getBean(
                                BusinessObjectService.class).findMatching(OleInvoiceItem.class, docMap);
                        HashMap acctMap = new HashMap();
                        if (itemList.size() > 0) {
                            for (OleInvoiceItem oleInvoiceItem : itemList) {
                                acctMap.put("itemIdentifier", oleInvoiceItem.getItemIdentifier());
                                acctMap.put("chartOfAccountsCode", chartCode);
                                acctMap.put("accountNumber", accountNo);
                                acctMap.put("financialObjectCode", objectCode);
                                List<InvoiceAccount> oleInvoiceAccount = (List<InvoiceAccount>) SpringContext
                                        .getBean(BusinessObjectService.class).findMatching(InvoiceAccount.class, acctMap);
                                if (oleInvoiceAccount.size() > 0) {
                                    for (InvoiceAccount invAcct : oleInvoiceAccount) {
                                        paidInvoices = paidInvoices.add(invAcct.getAmount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return paidInvoices;
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
                                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
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
                                amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
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
                        amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
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
                        amount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
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

    public boolean isBudgetReviewRequired(OleInvoiceDocument oleInvoiceDocument) {
        boolean sufficientFundCheck = false;
        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {

            if ((SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
                return false;
            }
            List<SourceAccountingLine> sourceAccountingLineList = oleInvoiceDocument.getSourceAccountingLines();
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
        }

        return sufficientFundCheck;
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
