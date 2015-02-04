/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PRQSDocumentsStrings;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.document.AccountsPayableDocument;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedExpiredAccountWarningEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedTradeInWarningEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KualiRuleService;

/**
 * Business pre rule(s) applicable to Invoice documents.
 */
public class InvoiceDocumentPreRules extends AccountsPayableDocumentPreRulesBase {

    /**
     * Default Constructor
     */
    public InvoiceDocumentPreRules() {
        super();
    }

    /**
     * Main hook point to perform rules check.
     *
     * @see org.kuali.rice.kns.rules.PromptBeforeValidationBase#doPrompts(org.kuali.rice.krad.document.Document)
     */
    @Override
    public boolean doPrompts(Document document) {
        boolean preRulesOK = true;

        InvoiceDocument invoice = (InvoiceDocument) document;
        if ((!SpringContext.getBean(PurapService.class).isFullDocumentEntryCompleted(invoice)) || (StringUtils.equals(invoice.getApplicationDocumentStatus(), PurapConstants.InvoiceStatuses.APPDOC_AWAITING_ACCOUNTS_PAYABLE_REVIEW))) {
            if (!confirmPayDayNotOverThresholdDaysAway(invoice)) {
                return false;
            }
            if (!confirmUnusedTradeIn(invoice)) {
                return false;
            }
            if (!confirmEncumberNextFiscalYear(invoice)) {
                return false;
            }

            if (!confirmEncumberPriorFiscalYear(invoice)) {
                return false;
            }
        }
        if (SpringContext.getBean(PurapService.class).isFullDocumentEntryCompleted(invoice)) {
            if (!confirmExpiredAccount(invoice)) {
                return false;
            }
        }


        preRulesOK &= super.doPrompts(document);
        return preRulesOK;
    }

    /**
     * Prompts user to confirm with a Yes or No to a question being asked.
     *
     * @param questionType    - type of question
     * @param messageConstant - key to retrieve message
     * @return - true if overriding, false otherwise
     */
    protected boolean askForConfirmation(String questionType, String messageConstant) {

        String questionText = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(messageConstant);
        if (questionText.contains("{")) {
            questionText = prepareQuestionText(questionType, questionText);
        } else if (StringUtils.equals(messageConstant, OLEKeyConstants.ERROR_ACCOUNT_EXPIRED) || StringUtils.equals(messageConstant, PurapKeyConstants.WARNING_ITEM_TRADE_IN_AMOUNT_UNUSED)) {
            questionText = questionType;
        }


        boolean confirmOverride = super.askOrAnalyzeYesNoQuestion(questionType, questionText);

        if (!confirmOverride) {
            event.setActionForwardName(OLEConstants.MAPPING_BASIC);
            return false;
        }
        return true;
    }

    /**
     * Creates the actual text of the question, replacing place holders like pay date threshold with an actual constant value.
     *
     * @param questionType - type of question
     * @param questionText - actual text of question pulled from resource file
     * @return - question text with place holders replaced
     */
    protected String prepareQuestionText(String questionType, String questionText) {
        if (StringUtils.equals(questionType, PRQSDocumentsStrings.THRESHOLD_DAYS_OVERRIDE_QUESTION)) {
            questionText = StringUtils.replace(questionText, "{0}", new Integer(PurapConstants.PRQS_PAY_DATE_DAYS_BEFORE_WARNING).toString());
        }
        return questionText;
    }

    /**
     * Validates if the pay date threshold has not been passed, if so confirmation is required by the user to
     * exceed the threshold.
     *
     * @param invoice - invoice document
     * @return - true if threshold has not been surpassed or if user confirmed ok to override, false otherwise
     */
    public boolean confirmPayDayNotOverThresholdDaysAway(InvoiceDocument invoice) {

        // If the pay date is more than the threshold number of days in the future, ask for confirmation.
        //boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedPayDateNotOverThresholdDaysAwayEvent("", invoice));

        //if (!rulePassed) {
        // The problem is that rulePassed always return true
        int thresholdDays = PurapConstants.PRQS_PAY_DATE_DAYS_BEFORE_WARNING;
        if ((invoice.getInvoicePayDate() != null) && SpringContext.getBean(PurapService.class).isDateMoreThanANumberOfDaysAway(invoice.getInvoicePayDate(), thresholdDays)) {
            return askForConfirmation(PRQSDocumentsStrings.THRESHOLD_DAYS_OVERRIDE_QUESTION, PurapKeyConstants.MESSAGE_PAYMENT_REQUEST_PAYDATE_OVER_THRESHOLD_DAYS);
        }
        return true;
    }

    public boolean confirmUnusedTradeIn(InvoiceDocument invoice) {
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedTradeInWarningEvent("", invoice));

        if (!rulePassed) {
            return askForConfirmation(PRQSDocumentsStrings.UNUSED_TRADE_IN_QUESTION, PurapKeyConstants.WARNING_ITEM_TRADE_IN_AMOUNT_UNUSED);
        }
        return true;
    }

    public boolean confirmExpiredAccount(InvoiceDocument invoice) {
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedExpiredAccountWarningEvent("", invoice));

        if (!rulePassed) {
            return askForConfirmation(PRQSDocumentsStrings.EXPIRED_ACCOUNT_QUESTION, OLEKeyConstants.ERROR_ACCOUNT_EXPIRED);
        }
        return true;
    }

    public boolean confirmEncumberNextFiscalYear(InvoiceDocument invoice) {
        Integer fiscalYear = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear();
        if (invoice.getPurchaseOrderDocument().getPostingYear().intValue() > fiscalYear) {
            return askForConfirmation(PRQSDocumentsStrings.ENCUMBER_NEXT_FISCAL_YEAR_QUESTION, PurapKeyConstants.WARNING_ENCUMBER_NEXT_FY);
        }

        return true;
    }

    public boolean confirmEncumberPriorFiscalYear(InvoiceDocument invoice) {

        Integer fiscalYear = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear();
        if (invoice.getPurchaseOrderDocument().getPostingYear().intValue() == fiscalYear && SpringContext.getBean(InvoiceService.class).allowBackpost(invoice)) {
            return askForConfirmation(PRQSDocumentsStrings.ENCUMBER_PRIOR_FISCAL_YEAR_QUESTION, PurapKeyConstants.WARNING_ENCUMBER_PRIOR_FY);
        }
        return true;
    }

    /**
     * @see AccountsPayableDocumentPreRulesBase#getDocumentName()
     */
    @Override
    public String getDocumentName() {
        return "Invoice";
    }

    /**
     * @see AccountsPayableDocumentPreRulesBase#createInvoiceNoMatchQuestionText(org.kuali.ole.module.purap.document.AccountsPayableDocument)
     */
    @Override
    public String createInvoiceNoMatchQuestionText(AccountsPayableDocument accountsPayableDocument) {

        String questionText = super.createInvoiceNoMatchQuestionText(accountsPayableDocument);

        CurrencyFormatter cf = new CurrencyFormatter();
        InvoiceDocument invoice = (InvoiceDocument) accountsPayableDocument;

        StringBuffer questionTextBuffer = new StringBuffer("");
        questionTextBuffer.append(questionText);

        questionTextBuffer.append("[br][br][b]Summary Detail Below:[b][br][br][table questionTable]");
        questionTextBuffer.append("[tr][td leftTd]Vendor Invoice Amount entered on start screen:[/td][td rightTd]" + (String) cf.format(invoice.getInitialAmount()) + "[/td][/tr]");
        questionTextBuffer.append("[tr][td leftTd]Invoice Total Prior to Additional Charges:[/td][td rightTd]" + (String) cf.format(invoice.getTotalPreTaxDollarAmountAboveLineItems()) + "[/td][/tr]");


        //only add this line if invoice has a discount
        if (invoice.isDiscount()) {
            questionTextBuffer.append("[tr][td leftTd]Total Before Discount:[/td][td rightTd]" + (String) cf.format(invoice.getGrandPreTaxTotalExcludingDiscount()) + "[/td][/tr]");
        }

        //if sales tax is enabled, show additional summary lines
        boolean salesTaxInd = SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.ENABLE_SALES_TAX_IND);
        if (salesTaxInd) {
            questionTextBuffer.append("[tr][td leftTd]Grand Total Prior to Tax:[/td][td rightTd]" + (String) cf.format(invoice.getGrandPreTaxTotal()) + "[/td][/tr]");
            questionTextBuffer.append("[tr][td leftTd]Grand Total Tax:[/td][td rightTd]" + (String) cf.format(invoice.getGrandTaxAmount()) + "[/td][/tr]");
        }

        questionTextBuffer.append("[tr][td leftTd]Grand Total:[/td][td rightTd]" + (String) cf.format(invoice.getGrandTotal()) + "[/td][/tr][/table]");

        return questionTextBuffer.toString();

    }

    @Override
    protected boolean checkCAMSWarningStatus(PurchasingAccountsPayableDocument purapDocument) {
        return PurapConstants.CAMSWarningStatuses.PAYMENT_REQUEST_STATUS_WARNING_NO_CAMS_DATA.contains(purapDocument.getApplicationDocumentStatus());
    }

    /**
     * Determines if the amount entered on the init tab is mismatched with the grand total of the document.
     *
     * @param accountsPayableDocument
     * @return
     */
    @Override
    protected boolean validateInvoiceTotalsAreMismatched(AccountsPayableDocument accountsPayableDocument) {
        boolean mismatched = false;
        InvoiceDocument payReqDoc = (InvoiceDocument) accountsPayableDocument;
        String[] excludeArray = {PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE};

        //  if UseTax is included, then the invoiceInitialAmount should be compared against the 
        // total amount NOT INCLUDING tax
        if (payReqDoc.isUseTaxIndicator()) {
            if (payReqDoc.getTotalPreTaxDollarAmountAllItems(excludeArray).compareTo(accountsPayableDocument.getInitialAmount()) != 0 && !accountsPayableDocument.isUnmatchedOverride()) {
                mismatched = true;
            }
        }

        //  if NO UseTax, then the invoiceInitialAmount should be compared against the 
        // total amount INCLUDING sales tax (since if the vendor invoices with sales tax, then we pay it)
        else {
            if (accountsPayableDocument.getTotalDollarAmountAllItems(excludeArray).compareTo(accountsPayableDocument.getInitialAmount()) != 0 && !accountsPayableDocument.isUnmatchedOverride()) {
                mismatched = true;
            }
        }

        return mismatched;
    }

}
