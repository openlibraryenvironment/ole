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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.gl.businessobject.Balance;
import org.kuali.ole.gl.businessobject.Entry;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleFundLookupDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * This class is the Action Class for OleFundLookup
 */
public class OleFundLookupAction extends KualiTransactionalDocumentActionBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleFundLookupAction.class);
    List<OleFundLookup> searchResult = new ArrayList<OleFundLookup>();
    List<OleFundLookup> searchPendingList = new ArrayList<OleFundLookup>();
    List<OleFundLookup> searchEntryList = new ArrayList<OleFundLookup>();
    List<OleFundLookup> searchMergedPendingList = new ArrayList<OleFundLookup>();
    List<OleFundLookup> finalSearchResult = new ArrayList<OleFundLookup>();

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * This method returns the list of values that satisfy the search criteria input from OleFundLookup page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        List<Balance> balanceList = null;
        List<GeneralLedgerPendingEntry> pendingList = null;
        List<Entry> entryList = null;
        finalSearchResult.clear();
        searchResult.clear();
        searchPendingList.clear();
        searchMergedPendingList.clear();
        searchEntryList.clear();
        LOG.debug("Inside searchRequisitions of OleFundLookupAction");
        OleFundLookupForm oleFundLookupForm = (OleFundLookupForm) form;
        OleFundLookupDocument oleFundDocument = oleFundLookupForm.getOleFundLookupDocument();
        if(oleFundDocument.getKeyword() != null){
            oleFundDocument.setKeyword(oleFundDocument.getKeyword().toUpperCase());
        }
        if(oleFundDocument.getAccountNumber() != null){
            oleFundDocument.setAccountNumber(oleFundDocument.getAccountNumber().toUpperCase());
        }
        if(oleFundDocument.getChartOfAccountsCode() != null){
            oleFundDocument.setChartOfAccountsCode(oleFundDocument.getChartOfAccountsCode().toUpperCase());
        }
        if(oleFundDocument.getOrganizationCode() != null){
            oleFundDocument.setOrganizationCode(oleFundDocument.getOrganizationCode().toUpperCase());
        }
        if(oleFundDocument.getObjectCode() != null){
            oleFundDocument.setObjectCode(oleFundDocument.getObjectCode().toUpperCase());
        }
        if (oleFundDocument.getChartOfAccountsCode() == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS,
                    OLEKeyConstants.CHART_CODE_REQUIRED);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        } else if (oleFundDocument.getAccountNumber() == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS,
                    OLEKeyConstants.ACC_NO_IS_REQUIRED);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        } else if (oleFundDocument.getObjectCode() == null) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS,
                    OLEKeyConstants.OBJ_CODE_REQUIRED);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        } else if (oleFundDocument.getUniversityFiscalYear() == null) {
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }


        if(!validateChartCode(oleFundDocument.getChartOfAccountsCode())){
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.CHART_CODE_NOT_FOUND});
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        if(!validateAccountNumber(oleFundDocument.getAccountNumber())){
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.ACC_NO_NOT_FOUND});
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        if(!validateObjectCode(oleFundDocument.getObjectCode())){
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.OBJ_CODE_NOT_FOUND});
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        if(oleFundDocument.getUniversityFiscalYear() != null){
            UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
            if(!universityDateService.getCurrentFiscalYear().equals(oleFundDocument.getUniversityFiscalYear())){
                GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.UNIV_FIS_YR_FOUND});
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        }


        if(oleFundDocument.getOrganizationCode() != null || oleFundDocument.getKeyword() != null){
            Map searchCriteria = new HashMap();
            if(oleFundDocument.getOrganizationCode() != null){
                searchCriteria.put(OLEConstants.OleFundLookupDocument.ORG_CODE,oleFundDocument.getOrganizationCode().toUpperCase());
            }
            if(oleFundDocument.getKeyword() != null) {
                searchCriteria.put(OLEConstants.OleFundLookupDocument.ACC_NAME, oleFundDocument.getKeyword().toUpperCase());
            }
            if(oleFundDocument.getChartOfAccountsCode() != null){
                searchCriteria.put(OLEConstants.OleFundLookupDocument.CHART_CODE, oleFundDocument.getChartOfAccountsCode().toUpperCase());
            }
            if(oleFundDocument.getAccountNumber() != null){
                searchCriteria.put(OLEConstants.OleFundLookupDocument.ACC_NO, oleFundDocument.getAccountNumber().toUpperCase());
            }

            if(searchCriteria != null){
                String accountNumber = oleFundDocument.getAccountNumber();
                List<Account> accounts = (List) getLookupService().findCollectionBySearchHelper(Account.class, searchCriteria, true);
                searchCriteria.clear();
                List<Account> orgAccounts;
                List<Account> keywordList;
                List<String> organizationCodeList = new ArrayList<String>();
                List<String> accountNameList = new ArrayList<String>();
                if(oleFundDocument.getOrganizationCode() != null){
                    searchCriteria.clear();
                    searchCriteria.put(OLEConstants.OleFundLookupDocument.ORG_CODE,oleFundDocument.getOrganizationCode().toUpperCase());
                    orgAccounts = (List)getLookupService().findCollectionBySearchHelper(Account.class, searchCriteria, true);
                    if(orgAccounts.size() >0){
                    for(Account account : orgAccounts){
                        organizationCodeList.add(account.getOrganizationCode());
                    }
                    }
                }
                if(oleFundDocument.getKeyword() != null){
                    searchCriteria.clear();
                    searchCriteria.put(OLEConstants.OleFundLookupDocument.ACC_NAME,oleFundDocument.getKeyword());
                    keywordList = (List)getLookupService().findCollectionBySearchHelper(Account.class, searchCriteria, true);
                    if(keywordList.size() >0){
                        for(Account account : keywordList){
                            accountNameList.add(account.getAccountName());
                        }
                    }
                }

                //List<Account> accounts = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class,searchCriteria);
                if(accounts.size() > 0){
                    for(Account account : accounts){
                        if(oleFundDocument.getOrganizationCode() == null || organizationCodeList.contains(account.getOrganizationCode()) && (oleFundDocument.getKeyword() == null || accountNameList.contains(account.getAccountName()))){
                            //if(oleFundDocument.getKeyword() == null || accountNameList.contains(account.getAccountName())){
                        oleFundDocument.setAccountNumber(account.getAccountNumber());
                        balanceList = getBalanceEntries(oleFundDocument);
                        pendingList = getGLPendingEntries(oleFundDocument);
                        entryList = getEntries(oleFundDocument);
                        Map<String,String> searchResultMap = new HashMap<String,String>();
                        if(balanceList.size() > 0){
                            for(Balance balance : balanceList){
                                String value =  balance.getChartOfAccountsCode()+"-"+balance.getAccountNumber()+"-"+balance.getObjectCode()+"-"+balance.getUniversityFiscalYear();
                                searchResultMap.put(value,value);
                            }
                        }
                        if(pendingList.size() > 0){
                            for(GeneralLedgerPendingEntry glPendingEntry : pendingList){
                                String value = glPendingEntry.getChartOfAccountsCode()+"-"+glPendingEntry.getAccountNumber()+"-"+glPendingEntry.getFinancialObjectCode()+"-"+glPendingEntry.getUniversityFiscalYear();
                                searchResultMap.put(value,value);
                            }
                        }
                        if(entryList.size() > 0){
                            for(Entry entry : entryList){
                                String value = entry.getChartOfAccountsCode()+"-"+entry.getAccountNumber()+"-"+entry.getFinancialObjectCode()+"-"+entry.getUniversityFiscalYear();
                                searchResultMap.put(value,value);
                            }
                        }

                        if(balanceList.size()<= 0 && pendingList.size()<= 0 && entryList.size() <= 0){
                            boolean valid = checkAccountEntry(account.getAccountNumber(), account.getChartOfAccountsCode(),oleFundDocument.getKeyword());
                            if(!valid){
                                GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS,
                                        OLEKeyConstants.ERROR_NO_RESULTS_FOUND);
                            }else{
                                OleFundLookup oleFundLookup = new OleFundLookup();
                                oleFundLookup.setChartOfAccountsCode(account.getChartOfAccountsCode());
                                oleFundLookup.setAccountName(getAccountName(account.getAccountNumber()));
                                oleFundLookup.setAccountNumber(account.getAccountNumber());
                                if(oleFundDocument.getObjectCode().equals(OLEConstants.ALL))     {
                                    oleFundLookup.setObjectCode(OLEConstants.ALL_OBJ_CD);
                                } else{
                                    oleFundLookup.setObjectCode(oleFundDocument.getObjectCode());
                                }
                                oleFundLookup.setOrganizationCode(getOrganizationCode(account.getAccountNumber()));
                                oleFundLookup.setCashBalance(KualiDecimal.ZERO.toString());
                                oleFundLookup.setFreeBalance(KualiDecimal.ZERO.toString());
                                oleFundLookup.setIntialBudgetAllocation(KualiDecimal.ZERO.toString());
                                oleFundLookup.setNetAllocation(KualiDecimal.ZERO.toString());
                                oleFundLookup.setEncumbrances(KualiDecimal.ZERO.toString());
                                oleFundLookup.setSumPaidInvoice(KualiDecimal.ZERO.toString());
                                oleFundLookup.setSumUnpaidInvoice(KualiDecimal.ZERO.toString());
                                oleFundLookup.setExpendedPercentage(KualiDecimal.ZERO.toString());
                                oleFundLookup.setExpenEncumPercentage(KualiDecimal.ZERO.toString());
                                searchResult.add(oleFundLookup);
                            }
                        }

                        List searchList = new ArrayList();
                        searchList.addAll(searchResultMap.values());
                        finalSearchResult.clear();
                        finalSearchResult.addAll(updateSearchResults(searchList));
                        finalSearchResult.addAll(searchResult);
                    }else{
                                GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS,
                                        OLEKeyConstants.ERROR_NO_RESULTS_FOUND);

                        }
                }
                    oleFundDocument.setAccountNumber(accountNumber);

                }else{
                    GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS,
                            OLEKeyConstants.ERROR_NO_RESULTS_FOUND);
                }
            }
        }else{
            balanceList = getBalanceEntries(oleFundDocument);
            pendingList = getGLPendingEntries(oleFundDocument);
            entryList = getEntries(oleFundDocument);
            Map<String,String> searchResultMap = new HashMap<String,String>();
            if(balanceList.size() > 0){
                for(Balance balance : balanceList){
                    String value =  balance.getChartOfAccountsCode()+"-"+balance.getAccountNumber()+"-"+balance.getObjectCode()+"-"+balance.getUniversityFiscalYear();
                    searchResultMap.put(value,value);
                }
            }
            if(pendingList.size() > 0){
                for(GeneralLedgerPendingEntry glPendingEntry : pendingList){
                    String value = glPendingEntry.getChartOfAccountsCode()+"-"+glPendingEntry.getAccountNumber()+"-"+glPendingEntry.getFinancialObjectCode()+"-"+glPendingEntry.getUniversityFiscalYear();
                    searchResultMap.put(value,value);
                }
            }
            if(entryList.size() > 0){
                for(Entry entry : entryList){
                    String value = entry.getChartOfAccountsCode()+"-"+entry.getAccountNumber()+"-"+entry.getFinancialObjectCode()+"-"+entry.getUniversityFiscalYear();
                    searchResultMap.put(value,value);
                }
            }

            if(balanceList.size()<= 0 && pendingList.size()<= 0 && entryList.size() <= 0){
                List<Account> accountList = getAccountList(oleFundDocument.getAccountNumber(), oleFundDocument.getChartOfAccountsCode());

                if (accountList.size() > 0) {
                    for (Iterator<Account> accountIterator = accountList.iterator(); accountIterator.hasNext(); ) {
                    Account account = accountIterator.next();
                    OleFundLookup oleFundLookup = new OleFundLookup();
                    oleFundLookup.setChartOfAccountsCode(account.getChartOfAccountsCode());
                    oleFundLookup.setAccountName(getAccountName(account.getAccountNumber()));
                    oleFundLookup.setAccountNumber(account.getAccountNumber());
                        if(oleFundDocument.getObjectCode().equals(OLEConstants.ALL))     {
                            oleFundLookup.setObjectCode(OLEConstants.ALL_OBJ_CD);
                        } else{
                            oleFundLookup.setObjectCode(oleFundDocument.getObjectCode());
                        }
                    oleFundLookup.setOrganizationCode(getOrganizationCode(account.getAccountNumber()));
                    oleFundLookup.setCashBalance(KualiDecimal.ZERO.toString());
                    oleFundLookup.setFreeBalance(KualiDecimal.ZERO.toString());
                    oleFundLookup.setIntialBudgetAllocation(KualiDecimal.ZERO.toString());
                    oleFundLookup.setNetAllocation(KualiDecimal.ZERO.toString());
                    oleFundLookup.setEncumbrances(KualiDecimal.ZERO.toString());
                    oleFundLookup.setSumPaidInvoice(KualiDecimal.ZERO.toString());
                    oleFundLookup.setSumUnpaidInvoice(KualiDecimal.ZERO.toString());
                    oleFundLookup.setExpendedPercentage(KualiDecimal.ZERO.toString());
                    oleFundLookup.setExpenEncumPercentage(KualiDecimal.ZERO.toString());
                    searchResult.add(oleFundLookup);

                }
            }
        }
            List searchList = new ArrayList();
            searchList.addAll(searchResultMap.values());
            finalSearchResult.addAll(updateSearchResults(searchList));
            finalSearchResult.addAll(searchResult);
        }


        Collections.sort(finalSearchResult, new Comparator<OleFundLookup>() {
            public int compare(OleFundLookup fundLookup1, OleFundLookup fundLookup2) {
                String firstString = fundLookup1.getAccountNumber();
                String secondString = fundLookup2.getAccountNumber();
                if (secondString == null || firstString == null) {
                    return 0;
                }
                int lengthFirstStr = firstString.length();
                int lengthSecondStr = secondString.length();
                int index1 = 0;
                int index2 = 0;
                while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
                    char ch1 = firstString.charAt(index1);
                    char ch2 = secondString.charAt(index2);

                    char[] space1 = new char[lengthFirstStr];
                    char[] space2 = new char[lengthSecondStr];

                    int loc1 = 0;
                    int loc2 = 0;

                    do {
                        space1[loc1++] = ch1;
                        index1++;

                        if (index1 < lengthFirstStr) {
                            ch1 = firstString.charAt(index1);
                        } else {
                            break;
                        }
                    } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

                    do {
                        space2[loc2++] = ch2;
                        index2++;

                        if (index2 < lengthSecondStr) {
                            ch2 = secondString.charAt(index2);
                        } else {
                            break;
                        }
                    } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

                    String str1 = new String(space1);
                    String str2 = new String(space2);

                    int result;

                    if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                        Integer firstNumberToCompare = new Integer(Integer
                                .parseInt(str1.trim()));
                        Integer secondNumberToCompare = new Integer(Integer
                                .parseInt(str2.trim()));
                        result = firstNumberToCompare.compareTo(secondNumberToCompare);
                    } else {
                        result = str1.compareTo(str2);
                    }

                    if (result != 0) {
                        return result;
                    }
                }
                return lengthFirstStr - lengthSecondStr;
            }
        });


        Collections.sort(finalSearchResult, new Comparator<OleFundLookup>() {
            public int compare(OleFundLookup fundLookup1, OleFundLookup fundLookup2) {
                String firstString = fundLookup1.getObjectCode();
                String secondString = fundLookup2.getObjectCode();
                if (secondString == null || firstString == null) {
                    return 0;
                }
                int lengthFirstStr = firstString.length();
                int lengthSecondStr = secondString.length();
                int index1 = 0;
                int index2 = 0;
                while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
                    char ch1 = firstString.charAt(index1);
                    char ch2 = secondString.charAt(index2);

                    char[] space1 = new char[lengthFirstStr];
                    char[] space2 = new char[lengthSecondStr];

                    int loc1 = 0;
                    int loc2 = 0;

                    do {
                        space1[loc1++] = ch1;
                        index1++;

                        if (index1 < lengthFirstStr) {
                            ch1 = firstString.charAt(index1);
                        } else {
                            break;
                        }
                    } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

                    do {
                        space2[loc2++] = ch2;
                        index2++;

                        if (index2 < lengthSecondStr) {
                            ch2 = secondString.charAt(index2);
                        } else {
                            break;
                        }
                    } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

                    String str1 = new String(space1);
                    String str2 = new String(space2);

                    int result;

                    if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                        Integer firstNumberToCompare = new Integer(Integer
                                .parseInt(str1.trim()));
                        Integer secondNumberToCompare = new Integer(Integer
                                .parseInt(str2.trim()));
                        result = firstNumberToCompare.compareTo(secondNumberToCompare);
                    } else {
                        result = str1.compareTo(str2);
                    }

                    if (result != 0) {
                        return result;
                    }
                }
                return lengthFirstStr - lengthSecondStr;
            }
        });
        oleFundDocument.setFinalResults(finalSearchResult);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }


    public List<OleFundLookup> updateSearchResults(List<String> entryList) {
        for (int i=0; i< entryList.size();i++) {
            OleFundLookup oleFundLookup = new OleFundLookup();
            String[] searchString = entryList.get(i).split("-");
            String chartCode =  searchString[0];
            String accountNumber = searchString[1];
            String objectCode = searchString[2];
            int fiscalYear = Integer.parseInt(searchString[3]);
            KualiDecimal encumbranceAmount = getEncumbrance(chartCode, accountNumber,objectCode, fiscalYear);
            KualiDecimal sumPaidInvoice = getSumPaidInvoices(chartCode, accountNumber,objectCode, fiscalYear);
            KualiDecimal sumUnpaidInvoice = getSumUnpaidInvoices(chartCode, accountNumber, objectCode);
            KualiDecimal budgetIncrease = getBudgetIncrease(fiscalYear,chartCode, accountNumber, objectCode);
            KualiDecimal budgetDecrease = getBudgetDecrease(fiscalYear,chartCode, accountNumber, objectCode);
            KualiDecimal initialBudgetAllocation = getInitialBudgetAllocation(chartCode,accountNumber,objectCode,fiscalYear);
            KualiDecimal netAllocation = (initialBudgetAllocation.add(budgetIncrease)).subtract(budgetDecrease);
            KualiDecimal encumExpPercentage = ((sumPaidInvoice.add(encumbranceAmount)).multiply(new KualiDecimal(100)));
            if(!encumExpPercentage.isZero() && netAllocation.isGreaterThan(KualiDecimal.ZERO)){
                encumExpPercentage = encumExpPercentage.divide(netAllocation);
            }else{
                encumExpPercentage = KualiDecimal.ZERO;
            }
            KualiDecimal expenPercentage = sumPaidInvoice.multiply(new KualiDecimal(100));
            if(!expenPercentage.isZero() && netAllocation.isGreaterThan(KualiDecimal.ZERO)){
                expenPercentage = expenPercentage.divide(netAllocation);
            }else{
                expenPercentage = KualiDecimal.ZERO;
            }
            oleFundLookup.setChartOfAccountsCode(chartCode);
            oleFundLookup.setAccountNumber(accountNumber);
            oleFundLookup.setAccountName(getAccountName(accountNumber));
            oleFundLookup.setOrganizationCode(getOrganizationCode(accountNumber));
            oleFundLookup.setObjectCode(objectCode);
            oleFundLookup.setCashBalance(convertNegativeSign(netAllocation.subtract(sumPaidInvoice)));
            oleFundLookup.setFreeBalance(convertNegativeSign((netAllocation.subtract(sumPaidInvoice)).subtract(encumbranceAmount)));
            oleFundLookup.setIntialBudgetAllocation(initialBudgetAllocation.toString());
            oleFundLookup.setNetAllocation(convertNegativeSign(netAllocation));
            oleFundLookup.setEncumbrances(convertNegativeSign(encumbranceAmount));
            oleFundLookup.setSumPaidInvoice(convertNegativeSign(sumPaidInvoice));
            oleFundLookup.setSumUnpaidInvoice(convertNegativeSign(sumUnpaidInvoice));
            oleFundLookup.setExpendedPercentage(convertNegativeSign(expenPercentage));
            oleFundLookup.setExpenEncumPercentage(convertNegativeSign(encumExpPercentage));
            searchEntryList.add(oleFundLookup);
        }
        return searchEntryList;

    }

    public List<Entry> getEntries(OleFundLookupDocument oleFundDocument) {
        Map searchMap = new HashMap();
        searchMap.put("accountNumber", oleFundDocument.getAccountNumber());
        searchMap.put("chartOfAccountsCode", oleFundDocument.getChartOfAccountsCode());
        searchMap.put("financialObjectCode", oleFundDocument.getObjectCode());
        searchMap.put("universityFiscalYear", oleFundDocument.getUniversityFiscalYear());
        List<Entry> list = new ArrayList<Entry>();
        List<Entry> entryList = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(Entry.class, searchMap);
        for(Entry entry : entryList){
            if(!entry.getTransactionLedgerEntryDescription().equals("TP Generated Offset") && !entry.getTransactionLedgerEntryDescription().equals("GENERATED OFFSET")){
                list.add(entry);
            }
        }
        return list;
    }

    public List<Balance> getBalanceEntries(OleFundLookupDocument oleFundDocument) {
        Map searchMap = new HashMap();
        searchMap.put("accountNumber", oleFundDocument.getAccountNumber());
        searchMap.put("chartOfAccountsCode", oleFundDocument.getChartOfAccountsCode());
        searchMap.put("objectCode", oleFundDocument.getObjectCode());
        searchMap.put("universityFiscalYear", oleFundDocument.getUniversityFiscalYear());
        searchMap.put("balanceTypeCode","CB");
        List<Balance> balanceList = new ArrayList<Balance>();
        balanceList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Balance.class, searchMap);
        return balanceList;
    }

    public List<GeneralLedgerPendingEntry> getGLPendingEntries(OleFundLookupDocument oleFundDocument) {
        Map searchMap = new HashMap();
        searchMap.put("accountNumber", oleFundDocument.getAccountNumber());
        searchMap.put("chartOfAccountsCode", oleFundDocument.getChartOfAccountsCode());
        searchMap.put("financialObjectCode", oleFundDocument.getObjectCode());
        searchMap.put("universityFiscalYear", oleFundDocument.getUniversityFiscalYear());
        searchMap.put("transactionEntryOffsetIndicator","N");
        List<GeneralLedgerPendingEntry> pendingEntryList = new ArrayList<GeneralLedgerPendingEntry>();
        pendingEntryList = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(GeneralLedgerPendingEntry.class, searchMap);
        return pendingEntryList;
    }


    public KualiDecimal getInitialBudgetAllocation(String chartCode,String accountNumber,String financialObjectCode,int fiscalYear){
        KualiDecimal initialBUdgetAllocation = KualiDecimal.ZERO;
        Map searchMap = new HashMap();
        searchMap.put("accountNumber", accountNumber);
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("objectCode", financialObjectCode);
        searchMap.put("balanceTypeCode", "CB");
        searchMap.put("universityFiscalYear",fiscalYear);
        List<Balance> balances = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(Balance.class, searchMap);
        if (balances.size() > 0) {
            for (Balance balance : balances) {
                if (balance.getAccountLineAnnualBalanceAmount().isGreaterThan(KualiDecimal.ZERO)) {
                    initialBUdgetAllocation = initialBUdgetAllocation.add(balance.getAccountLineAnnualBalanceAmount());
                }
            }
        }
        return initialBUdgetAllocation;
    }

    public String getAccountName(String accountNumber) {
        Map searchMap = new HashMap();
        String accountName = "";
        searchMap.put("accountNumber", accountNumber);
        List<Account> accounts = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        if (accounts.size() > 0) {
            return accounts.get(0).getAccountName();
        }
        return accountName;
    }

    public String getOrganizationCode(String accountNumber) {
        Map searchMap = new HashMap();
        String organizationCode = "";
        searchMap.put("accountNumber", accountNumber);
        List<Account> accounts = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        if (accounts.size() > 0) {
            return accounts.get(0).getOrganizationCode();
        }
        return organizationCode;
    }

    public KualiDecimal getEncumbrance(String chartCode, String accountNumber, String objectCode, Integer fiscalYear) {

        Map searchMap = new HashMap();
        KualiDecimal encumbranceAmount = KualiDecimal.ZERO;
        KualiDecimal creditAmount = KualiDecimal.ZERO;
        KualiDecimal debitAmount = KualiDecimal.ZERO;
        searchMap.put("accountNumber", accountNumber);
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("universityFiscalYear", fiscalYear);
        searchMap.put("financialBalanceTypeCode", "EX");
        searchMap.put("financialDocumentApprovedCode", "A");
        List<GeneralLedgerPendingEntry> poPendingEntry = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(GeneralLedgerPendingEntry.class, searchMap);
        if (poPendingEntry.size() > 0) {
            for (GeneralLedgerPendingEntry generalLedgerPendingEntry : poPendingEntry) {
                if (generalLedgerPendingEntry.getTransactionDebitCreditCode().equals("D")) {
                    debitAmount = debitAmount.add(generalLedgerPendingEntry.getTransactionLedgerEntryAmount());
                } else if (generalLedgerPendingEntry.getTransactionDebitCreditCode().equals("C")) {
                    if(!generalLedgerPendingEntry.getFinancialDocumentTypeCode().equals("OLE_POR")) {
                        creditAmount = creditAmount.add(generalLedgerPendingEntry.getTransactionLedgerEntryAmount());
                    }
                }
            }
        }

        searchMap.clear();
        searchMap.put("accountNumber", accountNumber);
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("universityFiscalYear", fiscalYear);
        searchMap.put("financialBalanceTypeCode", "EX");

        List<Entry> poEntry = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(Entry.class, searchMap);
        if (poEntry.size() > 0) {
            for (Entry entry : poEntry) {
                if (entry.getTransactionDebitCreditCode().equals("D")) {
                    debitAmount = debitAmount.add(entry.getTransactionLedgerEntryAmount());
                } else if (entry.getTransactionDebitCreditCode().equals("C")) {
                    if(!entry.getFinancialDocumentTypeCode().equals("OLE_POR")){
                        creditAmount = creditAmount.add(entry.getTransactionLedgerEntryAmount());
                    }
                }
            }
        }
        encumbranceAmount = debitAmount.subtract(creditAmount);
        return encumbranceAmount;

    }


    public KualiDecimal getSumPaidInvoices (String chartCode, String accountNumber, String objectCode, Integer
            fiscalYear){

        KualiDecimal paidInvoice = KualiDecimal.ZERO;
        KualiDecimal credit = KualiDecimal.ZERO;
        KualiDecimal debit = KualiDecimal.ZERO;

        Map searchMap = new HashMap();
        searchMap.put("accountNumber", accountNumber);
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("universityFiscalYear", fiscalYear);
        searchMap.put("financialBalanceTypeCode", "AC");
        searchMap.put("financialDocumentApprovedCode", "A");
        List<GeneralLedgerPendingEntry> preqPendingEntry = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(GeneralLedgerPendingEntry.class, searchMap);
        if (preqPendingEntry.size() > 0) {
            for (GeneralLedgerPendingEntry generalLedgerPendingEntry : preqPendingEntry) {
                if (generalLedgerPendingEntry.getTransactionDebitCreditCode().equals("D")) {
                    debit = debit.add(generalLedgerPendingEntry.getTransactionLedgerEntryAmount());
                } else if (generalLedgerPendingEntry.getTransactionDebitCreditCode().equals("C")) {
                    credit = credit.add(generalLedgerPendingEntry.getTransactionLedgerEntryAmount());
                }

            }
        }


        searchMap.clear();
        searchMap.put("accountNumber", accountNumber);
        searchMap.put("chartOfAccountsCode", chartCode);
        searchMap.put("financialObjectCode", objectCode);
        searchMap.put("universityFiscalYear", fiscalYear);
        searchMap.put("financialBalanceTypeCode", "AC");
        //searchMap.put("financialDocumentTypeCode", "OLE_PREQ");
        List<Entry> entryList = (List) SpringContext.getBean(BusinessObjectService.class).
                findMatching(Entry.class, searchMap);
        if (entryList.size() > 0) {
            for (Entry entry : entryList) {
                if (entry.getTransactionDebitCreditCode().equals("D")) {
                    debit = debit.add(entry.getTransactionLedgerEntryAmount());
                } else if (entry.getTransactionDebitCreditCode().equals("C")) {
                    credit = credit.add(entry.getTransactionLedgerEntryAmount());
                }
            }
        }
        if(debit.isGreaterThan(credit)){
            if(credit.isLessEqual(KualiDecimal.ZERO)){
                credit = credit.negated();
            }
            paidInvoice = debit.subtract(credit);
        } else{
            paidInvoice = credit.subtract(debit);
        }
        return paidInvoice;
    }

    public KualiDecimal getBudgetIncrease (Integer fiscalYear, String chartCode, String accountNo,
                                           String objectCode){
        Map searchMap = new HashMap();
        searchMap.put("universityFiscalYear", fiscalYear);
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

    public KualiDecimal getBudgetDecrease (Integer fiscalYear, String chartCode, String accountNo,
                                           String objectCode){
        Map searchMap = new HashMap();
        searchMap.put("universityFiscalYear", fiscalYear);
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
                if (entry.getTransactionLedgerEntryAmount().isNegative()) {
                    budgetDecrease = budgetDecrease.add(entry.getTransactionLedgerEntryAmount().multiply(new KualiDecimal(-1)));
                }
            }
        }
        return budgetDecrease;
    }

    public KualiDecimal getSumUnpaidInvoices (String chartCode, String accountNo, String objectCode){
        Map payMap = new HashMap();
        payMap.put("chartOfAccountsCode", chartCode);
        payMap.put("accountNumber", accountNo);
        payMap.put("financialObjectCode", objectCode);
        Map docMap = new HashMap();
        docMap.put("financialDocumentStatusCode", OLEConstants.FIN_DOC_STS_CD);
        KualiDecimal amount = KualiDecimal.ZERO;
        List<FinancialSystemDocumentHeader> docList = (List<FinancialSystemDocumentHeader>) SpringContext.getBean(
                BusinessObjectService.class).findMatching(FinancialSystemDocumentHeader.class, docMap);
        if (docList.size() > 0) {
            for (FinancialSystemDocumentHeader financialSystemDocumentHeader : docList) {
                String fdocNo = financialSystemDocumentHeader.getDocumentNumber();
                Map reqMap = new HashMap();
                reqMap.put("documentNumber", fdocNo);
                List<OlePaymentRequestDocument> reqList = (List<OlePaymentRequestDocument>) SpringContext.getBean(
                        BusinessObjectService.class).findMatching(OlePaymentRequestDocument.class, reqMap);
                if (reqList.size() > 0) {
                    for (OlePaymentRequestDocument oleRequestDocument : reqList) {
                        Integer payReqItmId = oleRequestDocument.getPurapDocumentIdentifier();
                        Map itmMap = new HashMap();
                        itmMap.put("purapDocumentIdentifier", payReqItmId);
                        List<OlePaymentRequestItem> itemList = (List<OlePaymentRequestItem>) SpringContext.getBean(
                                BusinessObjectService.class).findMatching(OlePaymentRequestItem.class, itmMap);
                        if (itemList.size() > 0) {
                            for (OlePaymentRequestItem olePaymentRequestItem : itemList) {
                                Integer itemIdentifier = olePaymentRequestItem.getItemIdentifier();
                                Map itemMap = new HashMap();
                                itemMap.put("itemIdentifier", itemIdentifier);
                                itemMap.put("chartOfAccountsCode", chartCode);
                                itemMap.put("accountNumber", accountNo);
                                itemMap.put("financialObjectCode", objectCode);
                                List<PaymentRequestAccount> payReqList = (List<PaymentRequestAccount>) SpringContext
                                        .getBean(BusinessObjectService.class).findMatching(PaymentRequestAccount.class,
                                                itemMap);
                                if (payReqList.size() > 0) {
                                    for (PaymentRequestAccount paymentRequestAccount : payReqList) {
                                        amount = amount.add(paymentRequestAccount.getAmount());

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return amount;
    }

    @Override
    public ActionForward cancel (ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response)throws Exception {
        OleFundLookupForm lookupForm = (OleFundLookupForm) form;
        return returnToSender(request, mapping, lookupForm);
    }


    @Override
    public ActionForward refresh (ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                  HttpServletResponse response)throws Exception {
        return super.refresh(mapping, form, request, response);
    }


    /**
     * This method clears the search criteria's that is on the FundLookup Search Page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clear (ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response)throws Exception {
        OleFundLookupForm oleFundLookupForm = (OleFundLookupForm) form;
        OleFundLookupDocument oleFundLookDocument = (OleFundLookupDocument) oleFundLookupForm.getDocument();
        oleFundLookDocument.setAccountNumber(null);
        oleFundLookDocument.setKeyword(null);
        oleFundLookDocument.setChartOfAccountsCode(null);
        oleFundLookDocument.setObjectCode(null);
        oleFundLookDocument.setOrganizationCode(null);
        oleFundLookDocument.setUniversityFiscalYear(null);
        oleFundLookupForm.setOleFundLookupDocument(null);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public String convertNegativeSign (KualiDecimal convertTo){
        if (convertTo.isLessThan(KualiDecimal.ZERO)) {
            String converted = convertTo.toString().replace("-", "");
            return "(" + converted + ")";
        }
        return convertTo.toString();
    }

    public boolean validateChartCode(String chartCode) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.CHART_CODE, chartCode);
        Chart chart = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Chart.class, searchMap);
        if (chart != null) {
            return true;
        }
        return false;
    }

    public boolean validateAccountNumber(String accountNumber) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.ACCOUNT_NUMBER, accountNumber);
        Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, searchMap);
        if (account != null) {
            return true;
        }
        return false;
    }

    public boolean validateObjectCode(String objectCode) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.FINANCIAL_OBJECT_CODE_PROPERTY_NAME, objectCode);
        ObjectCode code = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(ObjectCode.class, searchMap);
        if (code != null) {
            return true;
        }
        return false;
    }

    public boolean checkAccountEntry(String accountNumber,String chartCode,String accountName) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.ACCOUNT_NUMBER, accountNumber);
        searchMap.put(OLEConstants.CHART_CODE,chartCode);
        Account account =  SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class, searchMap);
        if (account != null && accountName != null) {
             searchMap.clear();
            searchMap.put(OLEConstants.ACCOUNT_NUMBER, accountNumber);
            searchMap.put(OLEConstants.OleFundLookupDocument.ACC_NAME,accountName);
            List<Account> accountList = (List) getLookupService().findCollectionBySearchHelper(Account.class, searchMap, true);
            if(accountList.size() > 0){
                if(account.getAccountName().equals(accountList.get(0).getAccountName())){
                    return true;
                }else{
                    return false;
                }
            }
        }
        else if (account != null){
            return true;
        }
        return false;
    }


    public List<Account> getAccountList(String accountNumber,String chartCode) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.ACCOUNT_NUMBER, accountNumber);
        searchMap.put(OLEConstants.CHART_CODE,chartCode);
        List<Account> accountList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        return accountList;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }


}