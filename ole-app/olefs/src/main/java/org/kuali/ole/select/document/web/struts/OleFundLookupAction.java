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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.gl.Constant;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.gl.OJBUtility;
import org.kuali.ole.gl.batch.service.AccountBalanceCalculator;
import org.kuali.ole.gl.businessobject.AccountBalance;
import org.kuali.ole.gl.businessobject.Balance;
import org.kuali.ole.gl.businessobject.Entry;
import org.kuali.ole.gl.businessobject.TransientBalanceInquiryAttributes;
import org.kuali.ole.gl.businessobject.lookup.AccountBalanceLookupableHelperServiceImpl;
import org.kuali.ole.gl.businessobject.lookup.BusinessObjectFieldConverter;
import org.kuali.ole.gl.service.AccountBalanceService;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleFundLookupDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SystemOptions;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.OptionsService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.BeanPropertyComparator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

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
    private AccountBalanceCalculator postAccountBalance;
    private AccountBalanceService accountBalanceService;
    private OptionsService optionsService;
    protected GeneralLedgerPendingEntryService generalLedgerPendingEntryService;
    public BusinessObjectDictionaryService businessObjectDictionaryService;
    protected Class businessObjectClass ;

    public BusinessObjectDictionaryService getBusinessObjectDictionaryService() {
        return businessObjectDictionaryService != null ? businessObjectDictionaryService : KNSServiceLocator
                .getBusinessObjectDictionaryService();
    }

    protected GeneralLedgerPendingEntryService getGeneralLedgerPendingEntryService() {
        return SpringContext.getBean(GeneralLedgerPendingEntryService.class);
    }


    public AccountBalanceCalculator getPostAccountBalance() {
        return SpringContext.getBean(AccountBalanceCalculator.class);
    }

    public void setPostAccountBalance(AccountBalanceCalculator postAccountBalance) {
        this.postAccountBalance = postAccountBalance;
    }

    public AccountBalanceService getAccountBalanceService() {
        return SpringContext.getBean(AccountBalanceService.class);
    }

    public void setAccountBalanceService(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public OptionsService getOptionsService() {
        return SpringContext.getBean(OptionsService.class);
    }

    public void setOptionsService(OptionsService optionsService) {
        this.optionsService = optionsService;
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

        List balanceList = null;
        List fundCodeAcctBalanceList = null;
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
        if(oleFundDocument.getFundCode() != null){
            oleFundDocument.setFundCode(oleFundDocument.getFundCode());
        }
        if(oleFundDocument.getOrganizationCode() != null){
            oleFundDocument.setOrganizationCode(oleFundDocument.getOrganizationCode().toUpperCase());
        }
        if(oleFundDocument.getObjectCode() != null){
            oleFundDocument.setObjectCode(oleFundDocument.getObjectCode().toUpperCase());
        }
        if(oleFundDocument.getFundCode() != null) {
            if(!validateFundCode(oleFundDocument.getFundCode())){
                GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.FUND_CD_NOT_FOUND});
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
            else if (oleFundDocument.getUniversityFiscalYear() == null) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS,
                        OLEKeyConstants.FISCAL_YR_REQUIRED);
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        }
        else {
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
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS,
                    OLEKeyConstants.FISCAL_YR_REQUIRED);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }

        if(oleFundDocument.getChartOfAccountsCode().equals("*")){
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.CHART_CODE_WILDCARD_SEARCH});
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
            }
    /*    if(oleFundDocument.getUniversityFiscalYear() != null){
            UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
            if(!universityDateService.getCurrentFiscalYear().equals(oleFundDocument.getUniversityFiscalYear())){
                GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, OLEKeyConstants.ERROR_CUSTOM, new String[]{OLEConstants.UNIV_FIS_YR_FOUND});
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        }
*/

        Map map = new HashMap();
        if(oleFundDocument.getFundCode() != null) {
            Map searchMap = new HashMap();
            searchMap.put(OLEConstants.FUND_CODE_ID, oleFundDocument.getFundCodeId());
            List<OleFundCodeAccountingLine> OleFundCodeAccountingList =(List<OleFundCodeAccountingLine>) SpringContext.getBean(BusinessObjectService.class).findMatching(OleFundCodeAccountingLine.class, searchMap);
            for(OleFundCodeAccountingLine oleFundCodeAccountingLine : OleFundCodeAccountingList) {
                map.put(OLEConstants.OleFundLookupDocument.ACC_NO,oleFundCodeAccountingLine.getAccountNumber());
                map.put(OLEConstants.OleFundLookupDocument.CHART_CODE,oleFundCodeAccountingLine.getChartCode().toUpperCase());
                map.put("objectCode",oleFundCodeAccountingLine.getObjectCode());
                map.put("sunAccountNumber","");
                map.put("universityFiscalYear",oleFundDocument.getUniversityFiscalYear() );
                map.put("dummyBusinessObject.pendingEntryOption", "All");
                map.put("dummyBusinessObject.consolidationOption","Consolidation" );
                map.put("backLocation", "portal.do");
                map.put("docFormKey","88888888" );
                map.put("subObjectCode","" );
                if(balanceList != null) {
                    fundCodeAcctBalanceList = getSearchResults(map);
                    if(fundCodeAcctBalanceList.size() > 0) {
                        balanceList.addAll(fundCodeAcctBalanceList);
                    }
                    fundCodeAcctBalanceList.clear();

                }
                else {
                    balanceList = getSearchResults(map);
                }
            }
        }
        else {
            map.put(OLEConstants.OleFundLookupDocument.CHART_CODE,oleFundDocument.getChartOfAccountsCode().toUpperCase() );
            map.put(OLEConstants.OleFundLookupDocument.ACC_NO, oleFundDocument.getAccountNumber().toUpperCase());
            map.put("objectCode",oleFundDocument.getObjectCode());

        map.put("sunAccountNumber","");
        map.put("universityFiscalYear",oleFundDocument.getUniversityFiscalYear() );
        map.put("dummyBusinessObject.pendingEntryOption", "All");
        map.put("dummyBusinessObject.consolidationOption","Consolidation" );
        map.put("backLocation", "portal.do");
        map.put("docFormKey","88888888" );
        map.put("subObjectCode","" );
            balanceList = getSearchResults(map);
        }
        if(balanceList.size() <= 0  && !oleFundDocument.getAccountNumber().equals("*")){
            boolean accountAvailable = checkAccountEntry(oleFundDocument.getAccountNumber(),oleFundDocument.getChartOfAccountsCode());
            if(accountAvailable){
                AccountBalance accountBalance = new AccountBalance();
                accountBalance.setChartOfAccountsCode(oleFundDocument.getChartOfAccountsCode());
                accountBalance.setAccountNumber(oleFundDocument.getAccountNumber());
                accountBalance.setObjectCode(oleFundDocument.getObjectCode());
                accountBalance.setUniversityFiscalYear(Integer.parseInt(oleFundDocument.getUniversityFiscalYear()));
                accountBalance.setCurrentBudgetLineBalanceAmount(KualiDecimal.ZERO);
                accountBalance.setAccountLineEncumbranceBalanceAmount(KualiDecimal.ZERO);
                accountBalance.setAccountLineActualsBalanceAmount(KualiDecimal.ZERO);
                balanceList.add(accountBalance);
            }
        }
        //CollectionIncomplete<AccountBalance> fundbBalanceResultList = null;
        List<AccountBalance> fundbBalanceResultList = new ArrayList<AccountBalance>();
        CollectionIncomplete<AccountBalance> fundbBalanceList = (CollectionIncomplete<AccountBalance>) balanceList;
        List<String> accountNameList = new ArrayList<String>();
        List<String> orgCodeList = new ArrayList<String>();
        if(oleFundDocument.getKeyword() != null && oleFundDocument.getOrganizationCode() != null){
            accountNameList  = getKeyword(oleFundDocument.getKeyword());
            orgCodeList = getOrganizationCd(oleFundDocument.getOrganizationCode());
            for (AccountBalance balance : fundbBalanceList) {
                if((accountNameList.contains(getAccountName(balance.getAccountNumber()))) &&
                        (orgCodeList.contains(getOrganizationCode(balance.getAccountNumber())))){
                    fundbBalanceResultList.add(balance);

                }
            }
        }else if(oleFundDocument.getKeyword() != null){
            accountNameList  = getKeyword(oleFundDocument.getKeyword());
            for (AccountBalance balance : fundbBalanceList) {
                if((accountNameList.contains(getAccountName(balance.getAccountNumber())))){
                    fundbBalanceResultList.add(balance);

                }
            }
        }else if(oleFundDocument.getOrganizationCode() != null){
            orgCodeList = getOrganizationCd(oleFundDocument.getOrganizationCode());
            for (AccountBalance balance : fundbBalanceList) {
                if(orgCodeList.contains(getOrganizationCode(balance.getAccountNumber()))){
                    fundbBalanceResultList.add(balance);

                }
            }
        }else if(oleFundDocument.getKeyword() == null && oleFundDocument.getOrganizationCode() == null){
            fundbBalanceResultList = fundbBalanceList;
        }
        if(fundbBalanceResultList != null){
            finalSearchResult.addAll(updateSearchResults(fundbBalanceResultList));
        }
        if(finalSearchResult.size() > 0){
        oleFundDocument.setFinalResults(finalSearchResult);
        }else {
            GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS,
                    OLEKeyConstants.ERROR_NO_RESULTS_FOUND);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public List updateSearchResults(List<AccountBalance> fundbBalanceList) {
        for (AccountBalance balance : fundbBalanceList) {
            OleFundLookup oleFundLookup = new OleFundLookup();
            String chartCode =  balance.getChartOfAccountsCode();
            String accountNumber = balance.getAccountNumber();
            String objectCode = balance.getObjectCode();
            int fiscalYear = balance.getUniversityFiscalYear();
            KualiDecimal encumbranceAmount = balance.getAccountLineEncumbranceBalanceAmount();
            KualiDecimal sumPaidInvoice = balance.getAccountLineActualsBalanceAmount();
            KualiDecimal sumUnpaidInvoice = KualiDecimal.ZERO;
            KualiDecimal budgetIncrease = KualiDecimal.ZERO;
            KualiDecimal budgetDecrease = KualiDecimal.ZERO;
            KualiDecimal initialBudgetAllocation = balance.getCurrentBudgetLineBalanceAmount();
            KualiDecimal netAllocation = balance.getCurrentBudgetLineBalanceAmount();
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



    public List getSearchResults(Map fieldValues) {


        Collection searchResultsCollection = null;

        // get the pending entry option. This method must be prior to the get search results
        String pendingEntryOption = this.getSelectedPendingEntryOption(fieldValues);

        // KFSMI-410: added one more node for consolidationOption
        String consolidationOption = (String) fieldValues.get(GeneralLedgerConstants.DummyBusinessObject.CONSOLIDATION_OPTION);
        // test if the consolidation option is selected or not
        boolean isConsolidated = true;


        if (isConsolidated) {
            fieldValues.remove("dummyBusinessObject.consolidationOption");
            Iterator availableBalanceIterator = getAccountBalanceService().findConsolidatedAvailableAccountBalance(fieldValues);
            searchResultsCollection = buildConsolidedAvailableBalanceCollection(availableBalanceIterator);
        }


        // update search results according to the selected pending entry option
        updateByPendingLedgerEntry(searchResultsCollection, fieldValues, pendingEntryOption, isConsolidated, false);

        // Put the search related stuff in the objects
        for (Iterator iter = searchResultsCollection.iterator(); iter.hasNext();) {
            AccountBalance ab = (AccountBalance) iter.next();
            TransientBalanceInquiryAttributes dbo = ab.getDummyBusinessObject();
            dbo.setConsolidationOption(consolidationOption);
            dbo.setPendingEntryOption(pendingEntryOption);
        }

        // get the actual size of all qualified search results
        Integer recordCount = getAccountBalanceService().getAvailableAccountBalanceCount(fieldValues, isConsolidated);
        Long actualSize = OJBUtility.getResultActualSize(searchResultsCollection, recordCount, fieldValues, new AccountBalance());
        // Get the entry in Account
  /*      SystemOptions option = getBusinessObjectService().findBySinglePrimaryKey(SystemOptions.class, Integer.parseInt((String)fieldValues.get(OLEConstants.FISCAL_YEAR)));
        if(option != null){
            if(searchResultsCollection.size() < 1) {
                String accountNumber = fieldValues.get(OLEConstants.ACCOUNT_NUMBER).toString();
                String chartCode = fieldValues.get(OLEConstants.CHART_CODE).toString();
                List<Account> accountList =  checkAccountEntry(accountNumber,chartCode);
                for (Iterator<Account> accountIterator = accountList.iterator(); accountIterator.hasNext(); ) {
                    Account account = accountIterator.next();
                    AccountBalance balance = new AccountBalance();
                    balance.setChartOfAccountsCode(account.getChartOfAccountsCode());
                    balance.setAccountNumber(account.getAccountNumber());
                    String fiscalYear = fieldValues.get(OLEConstants.FISCAL_YEAR).toString();
                    balance.setUniversityFiscalYear(Integer.parseInt(fiscalYear));
                    balance.setObjectCode(Constant.CONSOLIDATED_OBJECT_TYPE_CODE);
                    balance.setSubAccountNumber(Constant.CONSOLIDATED_OBJECT_TYPE_CODE);
                    balance.setSubObjectCode(Constant.CONSOLIDATED_OBJECT_TYPE_CODE);
                    balance.setCurrentBudgetLineBalanceAmount(KualiDecimal.ZERO);
                    balance.setAccountLineActualsBalanceAmount(KualiDecimal.ZERO);
                    balance.setAccountLineEncumbranceBalanceAmount(KualiDecimal.ZERO);
                    searchResultsCollection.add(balance);
                }
            }
        }*/
        return this.buildSearchResultList(searchResultsCollection, actualSize);
    }


    protected String getSelectedPendingEntryOption(Map fieldValues) {
        // truncate the non-property filed
        String pendingEntryOption = (String) fieldValues.get(Constant.PENDING_ENTRY_OPTION);
        fieldValues.remove(Constant.PENDING_ENTRY_OPTION);

        return pendingEntryOption;
    }

    private Collection buildConsolidedAvailableBalanceCollection(Iterator iterator) {
        Collection balanceCollection = new ArrayList();

        // build available balance collection throught analyzing the input iterator
        while (iterator.hasNext()) {
            Object avaiableAccountBalance = iterator.next();

            if (avaiableAccountBalance.getClass().isArray()) {
                int i = 0;
                Object[] array = (Object[]) avaiableAccountBalance;
                AccountBalance accountBalance = new AccountBalance();

                accountBalance.setUniversityFiscalYear(new Integer(array[i++].toString()));
                accountBalance.setChartOfAccountsCode(array[i++].toString());

                accountBalance.setAccountNumber(array[i++].toString());
                accountBalance.setSubAccountNumber(Constant.CONSOLIDATED_SUB_ACCOUNT_NUMBER);

                accountBalance.setObjectCode(array[i++].toString());
                accountBalance.setSubObjectCode(Constant.CONSOLIDATED_SUB_OBJECT_CODE);

                String objectTypeCode = array[i++].toString();
                accountBalance.getFinancialObject().setFinancialObjectTypeCode(objectTypeCode);

                KualiDecimal budgetAmount = new KualiDecimal(array[i++].toString());
                accountBalance.setCurrentBudgetLineBalanceAmount(budgetAmount);

                KualiDecimal actualsAmount = new KualiDecimal(array[i++].toString());
                accountBalance.setAccountLineActualsBalanceAmount(actualsAmount);

                KualiDecimal encumbranceAmount = new KualiDecimal(array[i].toString());
                accountBalance.setAccountLineEncumbranceBalanceAmount(encumbranceAmount);

                KualiDecimal variance = calculateVariance(accountBalance);
                accountBalance.getDummyBusinessObject().setGenericAmount(variance);

                balanceCollection.add(accountBalance);
            }
        }
        return balanceCollection;
    }

    private KualiDecimal calculateVariance(AccountBalance balance) {

        KualiDecimal variance = new KualiDecimal(0.0);
        KualiDecimal budgetAmount = balance.getCurrentBudgetLineBalanceAmount();
        KualiDecimal actualsAmount = balance.getAccountLineActualsBalanceAmount();
        KualiDecimal encumbranceAmount = balance.getAccountLineEncumbranceBalanceAmount();

        // determine if the object type code is one of the given codes
        if (ObjectUtils.isNull(balance.getFinancialObject()) || StringUtils.isBlank(balance.getFinancialObject().getFinancialObjectTypeCode())) {
            balance.refreshReferenceObject("financialObject"); // refresh if we need to...
        }
        ObjectCode financialObject = balance.getFinancialObject();
        String objectTypeCode = (financialObject == null) ? Constant.EMPTY_STRING : financialObject.getFinancialObjectTypeCode();

        SystemOptions options = getOptionsService().getOptions(balance.getUniversityFiscalYear());
        if (ObjectUtils.isNull(options)) {
            options = getOptionsService().getCurrentYearOptions();
        }
        String[] objectTypeCodeList = new String[3];
        objectTypeCodeList[0] = options.getFinObjTypeExpendNotExpCode();
        objectTypeCodeList[1] = options.getFinObjTypeExpNotExpendCode();
        objectTypeCodeList[2] = options.getFinObjTypeExpenditureexpCd();

        boolean isObjectTypeCodeInList = ArrayUtils.contains(objectTypeCodeList, objectTypeCode);

        // calculate the variance based on the object type code of the balance
        if (isObjectTypeCodeInList) {
            variance = budgetAmount.subtract(actualsAmount);
            variance = variance.subtract(encumbranceAmount);
        }
        else {
            variance = actualsAmount.subtract(budgetAmount);
        }
        return variance;
    }

    protected void updateByPendingLedgerEntry(Collection entryCollection, Map fieldValues, String pendingEntryOption, boolean isConsolidated, boolean isCostShareInclusive) {


        updateEntryCollection(entryCollection, fieldValues, false, isConsolidated, isCostShareInclusive);

    }

    protected void updateEntryCollection(Collection entryCollection, Map fieldValues, boolean isApproved, boolean isConsolidated, boolean isCostShareExcluded) {

        // convert the field names of balance object into corresponding ones of pending entry object
        Map pendingEntryFieldValues = BusinessObjectFieldConverter.convertToTransactionFieldValues(fieldValues);

        // go through the pending entries to update the balance collection
        Iterator pendingEntryIterator = getGeneralLedgerPendingEntryService().findPendingLedgerEntriesForAccountBalance(pendingEntryFieldValues, isApproved);
        while (pendingEntryIterator.hasNext()) {
            GeneralLedgerPendingEntry pendingEntry = (GeneralLedgerPendingEntry) pendingEntryIterator.next();

            if (isCostShareExcluded) {
                if (ObjectUtils.isNotNull(pendingEntry.getSubAccount()) && ObjectUtils.isNotNull(pendingEntry.getSubAccount().getA21SubAccount())) {
                    if (OLEConstants.SubAccountType.COST_SHARE.equals(pendingEntry.getSubAccount().getA21SubAccount().getSubAccountTypeCode())) {
                        // Don't process this one
                        continue;
                    }
                }
            }

            // if consolidated, change the following fields into the default values for consolidation
            if (isConsolidated) {
                pendingEntry.setSubAccountNumber(Constant.CONSOLIDATED_SUB_ACCOUNT_NUMBER);
                pendingEntry.setFinancialSubObjectCode(Constant.CONSOLIDATED_SUB_OBJECT_CODE);
                pendingEntry.setFinancialObjectTypeCode(Constant.CONSOLIDATED_OBJECT_TYPE_CODE);
            }

            AccountBalance accountBalance = getPostAccountBalance().findAccountBalance(entryCollection, pendingEntry);
            getPostAccountBalance().updateAccountBalance(pendingEntry, accountBalance);

            // recalculate the variance after pending entries are combined into account balances
            if (accountBalance.getDummyBusinessObject() == null) {
                accountBalance.setDummyBusinessObject(new TransientBalanceInquiryAttributes());
            }
            KualiDecimal variance = calculateVariance(accountBalance);
            accountBalance.getDummyBusinessObject().setGenericAmount(variance);
        }
    }

    public boolean checkAccountEntry(String accountNumber,String chartCode) {
        boolean exists = false;
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.ACCOUNT_NUMBER, accountNumber);
        searchMap.put(OLEConstants.CHART_CODE,chartCode);
        List<Account> accountList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        if(accountList.size() > 0){
            return true;
        }
        return false;
    }

    protected List buildSearchResultList(Collection searchResultsCollection, Long actualSize) {
        CollectionIncomplete results = new CollectionIncomplete(searchResultsCollection, actualSize);

        // sort list if default sort column given
        List searchResults = results;
        List defaultSortColumns = getDefaultSortColumns();
        if (defaultSortColumns.size() > 0) {
            Collections.sort(results, new BeanPropertyComparator(defaultSortColumns, true));
        }
        return searchResults;
    }

    public List<String> getDefaultSortColumns() {
        return getBusinessObjectDictionaryService().getLookupDefaultSortFieldNames(getBusinessObjectClass());
    }

    public Class getBusinessObjectClass() {
        return OleFundLookup.class;
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

    public boolean validateFundCode(String fundCode) {
        Map searchMap = new HashMap();
        searchMap.put(OLEConstants.FUND_CODE, fundCode);
        List<OleFundCode> oleFundCodeList =(List<OleFundCode>) SpringContext.getBean(BusinessObjectService.class).findMatching(OleFundCode.class, searchMap);
        if (oleFundCodeList.size() > 0) {
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

    public ActionForward clear (ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response)throws Exception {
        OleFundLookupForm oleFundLookupForm = (OleFundLookupForm) form;
        OleFundLookupDocument oleFundLookDocument = (OleFundLookupDocument) oleFundLookupForm.getDocument();
        oleFundLookDocument.setAccountNumber(null);
        oleFundLookDocument.setKeyword(null);
        oleFundLookDocument.setChartOfAccountsCode(null);
        oleFundLookDocument.setFundCode(null);
        oleFundLookDocument.setObjectCode(null);
        oleFundLookDocument.setOrganizationCode(null);
        oleFundLookDocument.setUniversityFiscalYear(null);
        oleFundLookupForm.setOleFundLookupDocument(null);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }


    @Override
    public ActionForward cancel (ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response)throws Exception {
        OleFundLookupForm lookupForm = (OleFundLookupForm) form;
        return returnToSender(request, mapping, lookupForm);
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

    public String convertNegativeSign (KualiDecimal convertTo){
        if (convertTo.isLessThan(KualiDecimal.ZERO)) {
            String converted = convertTo.toString().replace("-", "");
            return "(" + converted + ")";
        }
        return convertTo.toString();
    }

    public List<String> getKeyword(String accountName){
        Map searchMap = new HashMap();
        List<String> accountNameList = new ArrayList<String>();
        searchMap.put("accountName", accountName);
        List<Account> accounts = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        if (accounts.size() > 0) {
            for(Account account : accounts){
                accountNameList.add(account.getAccountName());
            }
        }
        return accountNameList;
    }

    public List<String> getOrganizationCd(String orgCd){
        Map searchMap = new HashMap();
        List<String> orgCdList = new ArrayList<String>();
        searchMap.put("organizationCode", orgCd);
        List<Account> accounts = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(Account.class, searchMap);
        if (accounts.size() > 0) {
            for(Account account : accounts){
                orgCdList.add(account.getOrganizationCode());
            }
        }
        return orgCdList;
    }


}



