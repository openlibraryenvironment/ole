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
package org.kuali.ole.gl.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.service.AccountService;
import org.kuali.ole.coa.service.ObjectLevelService;
import org.kuali.ole.coa.service.ObjectTypeService;
import org.kuali.ole.fp.document.YearEndDocument;
import org.kuali.ole.gl.batch.dataaccess.SufficientFundsDao;
import org.kuali.ole.gl.businessobject.SufficientFundBalances;
import org.kuali.ole.gl.businessobject.SufficientFundRebuild;
import org.kuali.ole.gl.businessobject.Transaction;
import org.kuali.ole.gl.dataaccess.SufficientFundBalancesDao;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.gl.service.SufficientFundsServiceConstants;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OleSufficientFundCheck;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SufficientFundsItem;
import org.kuali.ole.sys.businessobject.SystemOptions;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.GeneralLedgerPostingDocument;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.OptionsService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * The base implementation of SufficientFundsService
 */
@Transactional
public class SufficientFundsServiceImpl implements SufficientFundsService, SufficientFundsServiceConstants {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SufficientFundsServiceImpl.class);

    private AccountService accountService;
    private ObjectLevelService objectLevelService;
    private ConfigurationService kualiConfigurationService;
    private SufficientFundsDao sufficientFundsDao;
    private SufficientFundBalancesDao sufficientFundBalancesDao;
    private OptionsService optionsService;
    private GeneralLedgerPendingEntryService generalLedgerPendingEntryService;
    private BusinessObjectService businessObjectService;

    /**
     * Default constructor
     */
    public SufficientFundsServiceImpl() {
        super();
    }

    /**
     * This operation derives the acct_sf_finobj_cd which is used to populate the General Ledger Pending entry table, so that later
     * we can do Suff Fund checking against that entry
     *
     * @param financialObject the object code being checked against
     * @param accountSufficientFundsCode the kind of sufficient funds checking turned on in this system
     * @return the object code that should be used for the sufficient funds inquiry, or a blank String
     * @see org.kuali.ole.gl.service.SufficientFundsService#getSufficientFundsObjectCode(org.kuali.ole.coa.businessobject.ObjectCode,
     *      java.lang.String)
     */
    @Override
    public String getSufficientFundsObjectCode(ObjectCode financialObject, String accountSufficientFundsCode) {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Obtaining SF Object code for: " + accountSufficientFundsCode + " - " + financialObject.getFinancialObjectCode() );
        }
        if (OLEConstants.SF_TYPE_NO_CHECKING.equals(accountSufficientFundsCode)) {
            return OLEConstants.NOT_AVAILABLE_STRING;
        }
        else if (OLEConstants.SF_TYPE_ACCOUNT.equals(accountSufficientFundsCode)) {
            return "    ";
        }
        else if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(accountSufficientFundsCode)) {
            return "    ";
        }
        else if (OLEConstants.SF_TYPE_OBJECT.equals(accountSufficientFundsCode)) {
            return financialObject.getFinancialObjectCode();
        }
        else if (OLEConstants.SF_TYPE_LEVEL.equals(accountSufficientFundsCode)) {
            return financialObject.getFinancialObjectLevelCode();
        }
        else if (OLEConstants.SF_TYPE_CONSOLIDATION.equals(accountSufficientFundsCode)) {
            financialObject.refreshReferenceObject("financialObjectLevel");
            return financialObject.getFinancialObjectLevel().getFinancialConsolidationObjectCode();
        }
        else {
            throw new IllegalArgumentException("Invalid Sufficient Funds Code: " + accountSufficientFundsCode);
        }
    }

    /**
     * Checks for sufficient funds on a single document
     *
     * @param document document to check
     * @return Empty List if has sufficient funds for all accounts, List of SufficientFundsItem if not
     * @see org.kuali.ole.gl.service.SufficientFundsService#checkSufficientFunds(org.kuali.rice.krad.document.FinancialDocument)
     */
    @Override
    public List<SufficientFundsItem> checkSufficientFunds(GeneralLedgerPostingDocument document) {
        LOG.debug("checkSufficientFunds() started");

        return checkSufficientFunds(document.getPendingLedgerEntriesForSufficientFundsChecking());
    }

    /**
     * checks to see if a document is a <code>YearEndDocument</code>
     *
     * @param documentClass the class of a Document to check
     * @return true if the class implements <code>YearEndDocument</code>
     */
    @SuppressWarnings("unchecked")
    protected boolean isYearEndDocument(Class documentClass) {
        return YearEndDocument.class.isAssignableFrom(documentClass);
    }

    /**
     * Checks for sufficient funds on a list of transactions
     *
     * @param transactions list of transactions
     * @return Empty List if has sufficient funds for all accounts, List of SufficientFundsItem if not
     * @see org.kuali.ole.gl.service.SufficientFundsService#checkSufficientFunds(java.util.List)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<SufficientFundsItem> checkSufficientFunds(List<? extends Transaction> transactions) {
        LOG.debug("checkSufficientFunds() started");

        for (Transaction e : transactions) {
            e.refreshNonUpdateableReferences();
        }

        List<SufficientFundsItem> summaryItems = summarizeTransactions(transactions);
        for (Iterator iter = summaryItems.iterator(); iter.hasNext();) {
            SufficientFundsItem item = (SufficientFundsItem) iter.next();
            if ( LOG.isDebugEnabled() ) {
                LOG.debug("checkSufficientFunds() " + item.toString());
            }
            if (hasSufficientFundsOnItem(item)) {
                iter.remove();
            }
        }

        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Sufficient Funds Check Complete.  Returning: " + summaryItems );
        }
        return summaryItems;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SufficientFundsItem> checkSufficientFundsForPREQ(List<? extends Transaction> transactions) {
        LOG.debug("checkSufficientFunds() started");

        for (Transaction e : transactions) {
            e.refreshNonUpdateableReferences();
        }

        List<SufficientFundsItem> summaryItems = summarizeTransactions(transactions);
        for (Iterator iter = summaryItems.iterator(); iter.hasNext();) {
            SufficientFundsItem item = (SufficientFundsItem) iter.next();
            if (LOG.isDebugEnabled()) {
                LOG.debug("checkSufficientFunds() " + item.toString());
            }
            if (hasSufficientFundsOnPREQItem(item)) {
                iter.remove();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("PREQ Sufficient Funds Check Complete.  Returning: " + summaryItems);
        }
        return summaryItems;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SufficientFundsItem> checkSufficientFundsForInvoice(List<? extends Transaction> transactions) {
        LOG.debug("checkSufficientFunds() for Invoice started");

        for (Transaction e : transactions) {
            e.refreshNonUpdateableReferences();
        }

        List<SufficientFundsItem> summaryItems = summarizeTransactions(transactions);
        for (Iterator iter = summaryItems.iterator(); iter.hasNext();) {
            SufficientFundsItem item = (SufficientFundsItem) iter.next();
            if (LOG.isDebugEnabled()) {
                LOG.debug("checkSufficientFunds() for Invoice " + item.toString());
            }
           /* if (hasSufficientFundsOnPREQItem(item)) {
                iter.remove();
            }*/
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Invoice Sufficient Funds Check Complete.  Returning: " + summaryItems);
        }
        return summaryItems;
    }

    /**
     * For each transaction, fetches the appropriate sufficient funds item to check against
     *
     * @param transactions a list of Transactions
     * @return a List of corresponding SufficientFundsItem
     */
    @SuppressWarnings("unchecked")
    protected List<SufficientFundsItem> summarizeTransactions(List<? extends Transaction> transactions) {
        Map<String, SufficientFundsItem> items = new HashMap<String, SufficientFundsItem>();

        SystemOptions currentYear = optionsService.getCurrentYearOptions();

        // loop over the given transactions, grouping into SufficientFundsItem objects
        // which are keyed by the appropriate chart/account/SF type, and derived object value
        // see getSufficientFundsObjectCode() for the "object" used for grouping
        for (Object element : transactions) {
            Transaction tran = (Transaction) element;

            SystemOptions year = tran.getOption();
            if (year == null) {
                year = currentYear;
            }
            if (ObjectUtils.isNull(tran.getAccount())) {
                throw new IllegalArgumentException("Invalid account: " + tran.getChartOfAccountsCode() + "-" + tran.getAccountNumber());
            }
            SufficientFundsItem sfi = new SufficientFundsItem(year, tran, getSufficientFundsObjectCode(tran.getFinancialObject(), tran.getAccount().getAccountSufficientFundsCode()));
            sfi.setDocumentTypeCode(tran.getFinancialDocumentTypeCode());

            if (items.containsKey(sfi.getKey())) {
                SufficientFundsItem item = items.get(sfi.getKey());
                item.add(tran);
            }
            else {
                items.put(sfi.getKey(), sfi);
            }
        }

        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "Returning Summarized transactions for sufficient funds checking: " + items );
        }
        return new ArrayList<SufficientFundsItem>(items.values());
    }

    /**
     * Given a sufficient funds item record, determines if there are sufficient funds available for the transaction
     *
     * @param item the item to check
     * @return true if there are sufficient funds available, false otherwise
     */
    protected boolean hasSufficientFundsOnItem(SufficientFundsItem item) {
        if (item.getAmount().equals(KualiDecimal.ZERO)) {
            LOG.debug("hasSufficientFundsOnItem() Transactions with zero amounts shold pass");
            return true;
        }

        if (!item.getYear().isBudgetCheckingOptionsCode()) {
            LOG.debug("hasSufficientFundsOnItem() No sufficient funds checking");
            return true;
        }

        if (!item.getAccount().isPendingAcctSufficientFundsIndicator()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("hasSufficientFundsOnItem() No checking on eDocs for account " + item.getAccount().getChartOfAccountsCode() + "-" + item.getAccount().getAccountNumber());
            }
            return true;
        }

        // exit sufficient funds checking if not enabled for an account
        if (OLEConstants.SF_TYPE_NO_CHECKING.equals(item.getAccountSufficientFundsCode())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("hasSufficientFundsOnItem() sufficient funds not enabled for account " + item.getAccount().getChartOfAccountsCode() + "-" + item.getAccount().getAccountNumber());
            }
            return true;
        }

        ObjectTypeService objectTypeService = SpringContext.getBean(ObjectTypeService.class);
        List<String> expenseObjectTypes = objectTypeService.getCurrentYearExpenseObjectTypes();

        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                && !item.getFinancialObject().getChartOfAccounts().getFinancialCashObjectCode()
                .equals(item.getFinancialObject().getFinancialObjectCode())) {
            LOG.debug("hasSufficientFundsOnItem() SF checking is cash and transaction is not cash");
            return true;
        }

        else if (!OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                && !expenseObjectTypes.contains(item.getFinancialObjectType().getCode())) {
            LOG.debug("hasSufficientFundsOnItem() SF checking is budget and transaction is not expense");
            return true;
        }

        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, item.getYear().getUniversityFiscalYear());
        keys.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, item.getAccount().getChartOfAccountsCode());
        keys.put(OLEPropertyConstants.ACCOUNT_NUMBER, item.getAccount().getAccountNumber());
        keys.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, item.getSufficientFundsObjectCode());
        SufficientFundBalances sfBalance = businessObjectService.findByPrimaryKey(SufficientFundBalances.class, keys);

        if (sfBalance == null) {
            Map criteria = new HashMap();
            criteria.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, item.getAccount().getChartOfAccountsCode());
            criteria.put(OLEPropertyConstants.ACCOUNT_NUMBER_FINANCIAL_OBJECT_CODE, item.getAccount().getAccountNumber());

            Collection sufficientFundRebuilds = businessObjectService.findMatching(SufficientFundRebuild.class, criteria);
            if (sufficientFundRebuilds != null && sufficientFundRebuilds.size() > 0) {
                LOG.debug("hasSufficientFundsOnItem() No balance record and waiting on rebuild, no sufficient funds");
                return false;
            }
            else {
                sfBalance = new SufficientFundBalances();
                sfBalance.setAccountActualExpenditureAmt(KualiDecimal.ZERO);
                sfBalance.setAccountEncumbranceAmount(KualiDecimal.ZERO);
                sfBalance.setCurrentBudgetBalanceAmount(KualiDecimal.ZERO);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Current SF Balances: Budget:      " + sfBalance.getCurrentBudgetBalanceAmount());
            LOG.debug("Current SF Balances: Actuals:     " + sfBalance.getAccountEncumbranceAmount());
            LOG.debug("Current SF Balances: Encumbrance: " + sfBalance.getAccountActualExpenditureAmt());
        }

        KualiDecimal balanceAmount = item.getAmount();
        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                || item.getYear().getBudgetCheckingBalanceTypeCd().equals(item.getBalanceTyp().getCode())) {
            // We need to change the sign on the amount because the amount in the item is an increase in cash. We only care
            // about decreases in cash.

            // Also, negating if this is a balance type code of budget checking and the transaction is a budget transaction.

            balanceAmount = balanceAmount.negated();
        }

        if (balanceAmount.isNegative()) {
            LOG.debug("hasSufficientFundsOnItem() balanceAmount is negative, allow transaction to proceed");
            return true;
        }

        PendingAmounts priorYearPending = new PendingAmounts();
        // if we're checking the CASH_AT_ACCOUNT type, then we need to consider the prior year pending transactions
        // if the balance forwards have not been run
        if ((OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode()))
                && (!item.getYear().isFinancialBeginBalanceLoadInd())) {
            priorYearPending = getPriorYearSufficientFundsBalanceAmount(item);
        }

        PendingAmounts pending = getPendingBalanceAmount(item);

        KualiDecimal availableBalance = null;
        KualiDecimal freeBalance = null;
        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())) {
            // if the beginning balances have not loaded for the transaction FY, pull the remaining balance from last year
            if (!item.getYear().isFinancialBeginBalanceLoadInd()) {
                availableBalance = sfBalance.getCurrentBudgetBalanceAmount().add(priorYearPending.budget) // add the remaining
                        // budget from last year
                        // (assumed to carry to
                        // this year's)
                        .add(pending.actual) // any pending expenses (remember sense is negated)
                        .subtract(sfBalance.getAccountEncumbranceAmount()) // subtract the encumbrances (not reflected in cash yet)
                        .subtract(priorYearPending.encumbrance);
            }
            else { // balance forwards have been run, don't need to consider prior year remaining budget
                availableBalance = sfBalance.getCurrentBudgetBalanceAmount().add(pending.actual)
                        .subtract(sfBalance.getAccountEncumbranceAmount());
            }
        }
        else {
            availableBalance = sfBalance.getCurrentBudgetBalanceAmount() // current budget balance
                    .add(pending.budget) // pending budget entries
                    .subtract(sfBalance.getAccountActualExpenditureAmt()) // minus all current and pending actuals and encumbrances
                    .subtract(pending.actual).subtract(sfBalance.getAccountEncumbranceAmount())
                    .subtract(pending.encumbrance);
            freeBalance = sfBalance.getCurrentBudgetBalanceAmount()
                    .subtract(sfBalance.getAccountActualExpenditureAmt()).subtract(pending.actual);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("hasSufficientFundsOnItem() balanceAmount: " + balanceAmount + " availableBalance: " + availableBalance);
        }
        String chart = item.getAccount().getChartOfAccountsCode();
        String account = item.getAccount().getAccountNumber();
        String sfCode = item.getAccount().getAccountSufficientFundsCode();

        Map<String, Object> key = new HashMap<String, Object>();
        key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chart);
        key.put(OLEPropertyConstants.ACCOUNT_NUMBER, account);
        OleSufficientFundCheck oleSufficientFundCheck = businessObjectService.findByPrimaryKey(
                OleSufficientFundCheck.class, key);
        String option = "";
        boolean checkBalance;
        if (oleSufficientFundCheck != null) {
            option = oleSufficientFundCheck.getNotificationOption() != null ? oleSufficientFundCheck
                    .getNotificationOption() : "";
            if (option.equals(OLEPropertyConstants.BUD_REVIEW)) {
                checkBalance = checkEncumbrance(item, balanceAmount, availableBalance, sfBalance);
                return checkBalance;
            }
            /*
             * else if (option.equals(OLEPropertyConstants.NOTIFICATION)) { OleNotifyServiceImpl notification = new
             * OleNotifyServiceImpl(); List user = new ArrayList(); try { user.add(OLEConstants.NOTIFICATION_APPROVER);
             * notification.notify(user, OLEConstants.ACC_NUM + account + OLEConstants.EXC_BUD_AMT); return true; } catch
             * (WorkflowException exception) { LOG.debug("Exception occured while notification sent to budget reviewer" +
             * exception); } }
             */
            else if (option.equals(OLEPropertyConstants.WARNING_MSG)) {
                return true;
            }
            else if (option.equals(OLEPropertyConstants.BLOCK_USE)) {
                return true;
            }

        }
        else {
            checkBalance = checkEncumbrance(item, balanceAmount, availableBalance, sfBalance);
            return checkBalance;
        }
        return false;
    }

    public boolean checkEncumbrance(SufficientFundsItem item, KualiDecimal balanceAmount,
                                    KualiDecimal availableBalance, SufficientFundBalances sfBalance) {
        String chart = item.getAccount().getChartOfAccountsCode();
        String account = item.getAccount().getAccountNumber();
        String sfCode = item.getAccount().getAccountSufficientFundsCode();

        Map<String, Object> key = new HashMap<String, Object>();
        key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chart);
        key.put(OLEPropertyConstants.ACCOUNT_NUMBER, account);
        OleSufficientFundCheck oleSufficientFundCheck = businessObjectService.findByPrimaryKey(
                OleSufficientFundCheck.class, key);
        if (oleSufficientFundCheck != null) {
            KualiDecimal encumbranceAmount = KualiDecimal.ZERO;
            KualiDecimal encumAvailableAmount = null;
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                encumbranceAmount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    encumAvailableAmount = availableBalance.add((sfBalance.getCurrentBudgetBalanceAmount()
                            .multiply(encumbranceAmount)).divide(new KualiDecimal(100)));
                }
                else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    encumAvailableAmount = availableBalance.subtract((sfBalance.getCurrentBudgetBalanceAmount()
                            .multiply(encumbranceAmount)).divide(new KualiDecimal(100)));
                }
                if (balanceAmount.compareTo(encumAvailableAmount) > 0) {
                    return false;
                }
                return true;
            }
            else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_HASH.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                encumbranceAmount = new KualiDecimal(oleSufficientFundCheck.getEncumbranceAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    encumAvailableAmount = availableBalance.add(encumbranceAmount);
                }
                else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    encumAvailableAmount = availableBalance.subtract(encumbranceAmount);
                }
                if (balanceAmount.compareTo(encumAvailableAmount) > 0) {
                    return false;
                }
                return true;
            }
        }
        if (availableBalance.isGreaterThan(balanceAmount)) {
            return true;
        }
        return false;
    }


    protected boolean hasSufficientFundsOnPREQItem(SufficientFundsItem item) {


        if (item.getAmount().equals(KualiDecimal.ZERO)) {
            LOG.debug("hasSufficientFundsOnItem() Transactions with zero amounts shold pass");
            return true;
        }

        if (!item.getYear().isBudgetCheckingOptionsCode()) {
            LOG.debug("hasSufficientFundsOnItem() No sufficient funds checking");
            return true;
        }

        if (!item.getAccount().isPendingAcctSufficientFundsIndicator()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("hasSufficientFundsOnItem() No checking on eDocs for account "
                        + item.getAccount().getChartOfAccountsCode() + "-" + item.getAccount().getAccountNumber());
            }
            return true;
        }

        // exit sufficient funds checking if not enabled for an account
        if (OLEConstants.SF_TYPE_NO_CHECKING.equals(item.getAccountSufficientFundsCode())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("hasSufficientFundsOnItem() sufficient funds not enabled for account "
                        + item.getAccount().getChartOfAccountsCode() + "-" + item.getAccount().getAccountNumber());
            }
            return true;
        }

        ObjectTypeService objectTypeService = SpringContext.getBean(ObjectTypeService.class);
        List<String> expenseObjectTypes = objectTypeService.getCurrentYearExpenseObjectTypes();

        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                && !item.getFinancialObject().getChartOfAccounts().getFinancialCashObjectCode()
                .equals(item.getFinancialObject().getFinancialObjectCode())) {
            LOG.debug("hasSufficientFundsOnItem() SF checking is cash and transaction is not cash");
            return true;
        }

        else if (!OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                && !expenseObjectTypes.contains(item.getFinancialObjectType().getCode())) {
            LOG.debug("hasSufficientFundsOnItem() SF checking is budget and transaction is not expense");
            return true;
        }

        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, item.getYear().getUniversityFiscalYear());
        keys.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, item.getAccount().getChartOfAccountsCode());
        keys.put(OLEPropertyConstants.ACCOUNT_NUMBER, item.getAccount().getAccountNumber());
        keys.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, item.getSufficientFundsObjectCode());
        SufficientFundBalances sfBalance = businessObjectService.findByPrimaryKey(SufficientFundBalances.class, keys);
        if (sfBalance == null) {
            Map criteria = new HashMap();
            criteria.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, item.getAccount().getChartOfAccountsCode());
            criteria.put(OLEPropertyConstants.ACCOUNT_NUMBER_FINANCIAL_OBJECT_CODE, item.getAccount()
                    .getAccountNumber());

            Collection sufficientFundRebuilds = businessObjectService.findMatching(SufficientFundRebuild.class,
                    criteria);
            if (sufficientFundRebuilds != null && sufficientFundRebuilds.size() > 0) {
                LOG.debug("hasSufficientFundsOnItem() No balance record and waiting on rebuild, no sufficient funds");
                return false;
            }
            else {
                sfBalance = new SufficientFundBalances();
                sfBalance.setAccountActualExpenditureAmt(KualiDecimal.ZERO);
                sfBalance.setAccountEncumbranceAmount(KualiDecimal.ZERO);
                sfBalance.setCurrentBudgetBalanceAmount(KualiDecimal.ZERO);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Current SF Balances: Budget:      " + sfBalance.getCurrentBudgetBalanceAmount());
            LOG.debug("Current SF Balances: Actuals:     " + sfBalance.getAccountEncumbranceAmount());
            LOG.debug("Current SF Balances: Encumbrance: " + sfBalance.getAccountActualExpenditureAmt());
        }

        KualiDecimal balanceAmount = item.getAmount();
        if (balanceAmount.isLessThan(KualiDecimal.ZERO)) {
            balanceAmount = balanceAmount.negated();
        }
        // balanceAmount = balanceAmount.negated();
        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())
                || item.getYear().getBudgetCheckingBalanceTypeCd().equals(item.getBalanceTyp().getCode())) {
            // We need to change the sign on the amount because the amount in the item is an increase in cash. We only care
            // about decreases in cash.

            // Also, negating if this is a balance type code of budget checking and the transaction is a budget transaction.

            balanceAmount = balanceAmount.negated();
        }

        /*
         * if (balanceAmount.isNegative()) {
         * LOG.debug("hasSufficientFundsOnItem() balanceAmount is negative, allow transaction to proceed"); return true; }
         */

        PendingAmounts priorYearPending = new PendingAmounts();
        // if we're checking the CASH_AT_ACCOUNT type, then we need to consider the prior year pending transactions
        // if the balance forwards have not been run
        if ((OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode()))
                && (!item.getYear().isFinancialBeginBalanceLoadInd())) {
            priorYearPending = getPriorYearSufficientFundsBalanceAmount(item);
        }

        PendingAmounts pending = getPendingBalanceAmount(item);

        KualiDecimal availableBalance = KualiDecimal.ZERO;
        KualiDecimal freeBalance = KualiDecimal.ZERO;
        KualiDecimal expenditures = KualiDecimal.ZERO;

        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(item.getAccount().getAccountSufficientFundsCode())) {
            // if the beginning balances have not loaded for the transaction FY, pull the remaining balance from last year
            if (!item.getYear().isFinancialBeginBalanceLoadInd()) {
                availableBalance = sfBalance.getCurrentBudgetBalanceAmount().add(priorYearPending.budget) // add the remaining
                        // budget from last year
                        // (assumed to carry to
                        // this year's)
                        .add(pending.actual) // any pending expenses (remember sense is negated)
                        .subtract(sfBalance.getAccountEncumbranceAmount()) // subtract the encumbrances (not reflected in cash yet)
                        .subtract(priorYearPending.encumbrance);
            }
            else { // balance forwards have been run, don't need to consider prior year remaining budget
                availableBalance = sfBalance.getCurrentBudgetBalanceAmount().add(pending.actual)
                        .subtract(sfBalance.getAccountEncumbranceAmount());
            }
        }
        else {
            availableBalance = sfBalance.getCurrentBudgetBalanceAmount()

                    // pending budget entries
                    .subtract(sfBalance.getAccountActualExpenditureAmt())
                            // minus all current and pending actuals and encumbrances
                    .subtract(pending.actual);
            freeBalance = sfBalance.getCurrentBudgetBalanceAmount()
                    .subtract(sfBalance.getAccountActualExpenditureAmt()).subtract(pending.actual);
        }
        /*
         * Map searchCriteria = new HashMap(); searchCriteria.put("chartOfAccountsCode",
         * item.getAccount().getChartOfAccountsCode()); searchCriteria.put("accountNumber", item.getAccount().getAccountNumber());
         * searchCriteria.put("financialObjectCode", item.getSufficientFundsObjectCode());
         * searchCriteria.put("financialDocumentTypeCode", "OLE_PREQ"); // searchCriteria.put("transactionDebitCreditCode", null);
         * List<GeneralLedgerPendingEntry> pendingList = (List<GeneralLedgerPendingEntry>) SpringContext.getBean(
         * BusinessObjectService.class).findMatching(GeneralLedgerPendingEntry.class, searchCriteria); for
         * (GeneralLedgerPendingEntry pendingAmountEntry : pendingList) { if (pendingAmountEntry.getTransactionDebitCreditCode() ==
         * null) { expenditures = expenditures.add(pendingAmountEntry.getTransactionLedgerEntryAmount()); } }
         */
        if (item.getAccount().getAccountSufficientFundsCode().equals(OLEConstants.SF_TYPE_OBJECT)) {
            expenditures = getSumPaidInvoicesForObject(item.getAccount().getChartOfAccountsCode(), item.getAccount()
                    .getAccountNumber(), item.getSufficientFundsObjectCode());
        }
        else if (item.getAccount().getAccountSufficientFundsCode().equals(OLEConstants.SF_TYPE_ACCOUNT)) {
            expenditures = getSumPaidInvoicesForAccount(item.getAccount().getChartOfAccountsCode(), item.getAccount()
                    .getAccountNumber(), item.getSufficientFundsObjectCode());
        }
        availableBalance = availableBalance.subtract(expenditures);
        availableBalance = availableBalance.subtract(item.getAmount());
        String chart = item.getAccount().getChartOfAccountsCode();
        String account = item.getAccount().getAccountNumber();
        String sfCode = item.getAccount().getAccountSufficientFundsCode();
        Map<String, Object> key = new HashMap<String, Object>();
        key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chart);
        key.put(OLEPropertyConstants.ACCOUNT_NUMBER, account);
        OleSufficientFundCheck oleSufficientFundCheck = businessObjectService.findByPrimaryKey(
                OleSufficientFundCheck.class, key);
        if (oleSufficientFundCheck != null) {
            String option = oleSufficientFundCheck.getNotificationOption() != null ? oleSufficientFundCheck
                    .getNotificationOption() : "";
            boolean checkBalance;

            if (option.equals(OLEPropertyConstants.BUD_REVIEW)) {
                checkBalance = checkExpense(item, balanceAmount, availableBalance, sfBalance, freeBalance);
                return checkBalance;
            }/*
              * else if (option.equals(OLEPropertyConstants.NOTIFICATION)) { OleNotifyServiceImpl notification = new
              * OleNotifyServiceImpl(); List user = new ArrayList(); try { user.add(OLEConstants.NOTIFICATION_APPROVER);
              * notification.notify(user, "Account number " + account + " exceeds budget amount"); return true; } catch
              * (WorkflowException exception) { LOG.debug("Exception occured while notification sent to budget reviewer" +
              * exception); } }
              */


        }
        else {
            if (availableBalance.isGreaterThan(balanceAmount)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkExpense(SufficientFundsItem item, KualiDecimal balanceAmount, KualiDecimal availableBalance,
                                SufficientFundBalances sfBalance, KualiDecimal freeBalance) {
        String chart = item.getAccount().getChartOfAccountsCode();
        String account = item.getAccount().getAccountNumber();
        String sfCode = item.getAccount().getAccountSufficientFundsCode();

        Map<String, Object> key = new HashMap<String, Object>();
        key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chart);
        key.put(OLEPropertyConstants.ACCOUNT_NUMBER, account);
        OleSufficientFundCheck oleSufficientFundCheck = businessObjectService.findByPrimaryKey(
                OleSufficientFundCheck.class, key);
        if (oleSufficientFundCheck != null) {
            KualiDecimal expenseAvailableAmount = KualiDecimal.ZERO;
            KualiDecimal expenseAmount = KualiDecimal.ZERO;
            if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_PERCENTAGE.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                expenseAmount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    expenseAvailableAmount = availableBalance.add(
                            sfBalance.getCurrentBudgetBalanceAmount().multiply(expenseAmount)).divide(
                            new KualiDecimal(100));
                }
                else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    expenseAvailableAmount = availableBalance.subtract((sfBalance.getCurrentBudgetBalanceAmount()
                            .multiply(expenseAmount)).divide(new KualiDecimal(100)));
                }
                if (balanceAmount.compareTo(expenseAvailableAmount) > 0) {
                    return false;
                }
                return true;
            }
            else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_TYP_HASH.equals(oleSufficientFundCheck
                    .getEncumbExpenseConstraintType())) {
                expenseAmount = new KualiDecimal(oleSufficientFundCheck.getExpenseAmount());
                if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_OVER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    expenseAvailableAmount = availableBalance.add(expenseAmount);
                }
                else if (OLEPropertyConstants.SUFFICIENT_FUND_ENC_UNDER.equals(oleSufficientFundCheck
                        .getEncumbExpenseMethod())) {
                    expenseAvailableAmount = availableBalance.subtract(expenseAmount);
                }
                if (balanceAmount.compareTo(expenseAvailableAmount) > 0) {
                    return false;
                }
                return true;
            }
        }
        if (availableBalance.isGreaterThan(balanceAmount)) {
            return true;
        }
        return false;
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

    /**
     * An inner class to hold summary totals of pending ledger entry amounts
     */
    protected class PendingAmounts {
        public KualiDecimal budget = KualiDecimal.ZERO;
        public KualiDecimal actual = KualiDecimal.ZERO;
        public KualiDecimal encumbrance = KualiDecimal.ZERO;
    }

    /**
     * Given a sufficient funds item to check, gets the prior year sufficient funds balance to check against
     *
     * @param item the sufficient funds item to check against
     * @return a PendingAmounts record with the pending budget and encumbrance
     */
    protected PendingAmounts getPriorYearSufficientFundsBalanceAmount(SufficientFundsItem item) {
        PendingAmounts amounts = new PendingAmounts();

        // This only gets called for sufficient funds type of Cash at Account (H). The object code in the table for this type is
        // always
        // 4 spaces.
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, Integer.valueOf(item.getYear().getUniversityFiscalYear().intValue() - 1));
        keys.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, item.getAccount().getChartOfAccountsCode());
        keys.put(OLEPropertyConstants.ACCOUNT_NUMBER, item.getAccount().getAccountNumber());
        keys.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, "    ");
        SufficientFundBalances bal = businessObjectService.findByPrimaryKey(SufficientFundBalances.class, keys);

        if (bal != null) {
            amounts.budget = bal.getCurrentBudgetBalanceAmount();
            amounts.encumbrance = bal.getAccountEncumbranceAmount();
        }

        if ( LOG.isDebugEnabled() ) {
            LOG.debug("getPriorYearSufficientFundsBalanceAmount() budget      " + amounts.budget);
            LOG.debug("getPriorYearSufficientFundsBalanceAmount() encumbrance " + amounts.encumbrance);
        }
        return amounts;
    }

    /**
     * Totals the amounts of actual, encumbrance, and budget amounts from related pending entries
     *
     * @param item a sufficient funds item to find pending amounts for
     * @return the totals encapsulated in a PendingAmounts object
     */
    @SuppressWarnings("unchecked")
    protected PendingAmounts getPendingBalanceAmount(SufficientFundsItem item) {
        LOG.debug("getPendingBalanceAmount() started");

        Integer fiscalYear = item.getYear().getUniversityFiscalYear();
        String chart = item.getAccount().getChartOfAccountsCode();
        String account = item.getAccount().getAccountNumber();
        String sfCode = item.getAccount().getAccountSufficientFundsCode();

        PendingAmounts amounts = new PendingAmounts();

        if (OLEConstants.SF_TYPE_CASH_AT_ACCOUNT.equals(sfCode)) {
            // Cash checking
            List years = new ArrayList();
            years.add(item.getYear().getUniversityFiscalYear());

            // If the beginning balance isn't loaded, we need to include cash from
            // the previous fiscal year
            if (!item.getYear().isFinancialBeginBalanceLoadInd()) {
                years.add(item.getYear().getUniversityFiscalYear() - 1);
            }

            // Calculate the pending actual amount
            // Get Cash (debit amount - credit amount)
            amounts.actual = generalLedgerPendingEntryService.getCashSummary(years, chart, account, true);
            amounts.actual = amounts.actual.subtract(generalLedgerPendingEntryService.getCashSummary(years, chart, account, false));

            // Get Payables (credit amount - debit amount)
            amounts.actual = amounts.actual.add(generalLedgerPendingEntryService.getActualSummary(years, chart, account, true));
            amounts.actual = amounts.actual.subtract(generalLedgerPendingEntryService.getActualSummary(years, chart, account, false));
        }
        else {
            // Non-Cash checking

            // Get expenditure (debit - credit)
            amounts.actual = generalLedgerPendingEntryService.getExpenseSummary(fiscalYear, chart, account, item.getSufficientFundsObjectCode(), true, item.getDocumentTypeCode().startsWith("YE"));
            amounts.actual = amounts.actual.subtract(generalLedgerPendingEntryService.getExpenseSummary(fiscalYear, chart, account, item.getSufficientFundsObjectCode(), false, item.getDocumentTypeCode().startsWith("YE")));

            // Get budget
            amounts.budget = generalLedgerPendingEntryService.getBudgetSummary(fiscalYear, chart, account, item.getSufficientFundsObjectCode(), item.getDocumentTypeCode().startsWith("YE"));

            // Get encumbrance (debit - credit)
            amounts.encumbrance = generalLedgerPendingEntryService.getEncumbranceSummary(fiscalYear, chart, account, item.getSufficientFundsObjectCode(), true, item.getDocumentTypeCode().startsWith("YE"));
            amounts.encumbrance = amounts.encumbrance.subtract(generalLedgerPendingEntryService.getEncumbranceSummary(fiscalYear, chart, account, item.getSufficientFundsObjectCode(), false, item.getDocumentTypeCode().startsWith("YE")));
        }

        if ( LOG.isDebugEnabled() ) {
            LOG.debug("getPendingBalanceAmount() actual      " + amounts.actual);
            LOG.debug("getPendingBalanceAmount() budget      " + amounts.budget);
            LOG.debug("getPendingBalanceAmount() encumbrance " + amounts.encumbrance);
        }
        return amounts;
    }

    /**
     * Purge the sufficient funds balance table by year/chart
     *
     * @param chart the chart of sufficient fund balances to purge
     * @param year the fiscal year of sufficient fund balances to purge
     */
    @Override
    public void purgeYearByChart(String chart, int year) {
        sufficientFundsDao.purgeYearByChart(chart, year);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setGeneralLedgerPendingEntryService(GeneralLedgerPendingEntryService generalLedgerPendingEntryService) {
        this.generalLedgerPendingEntryService = generalLedgerPendingEntryService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    public void setObjectLevelService(ObjectLevelService objectLevelService) {
        this.objectLevelService = objectLevelService;
    }

    public void setOptionsService(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    public void setSufficientFundBalancesDao(SufficientFundBalancesDao sufficientFundBalancesDao) {
        this.sufficientFundBalancesDao = sufficientFundBalancesDao;
    }

    public void setSufficientFundsDao(SufficientFundsDao sufficientFundsDao) {
        this.sufficientFundsDao = sufficientFundsDao;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}