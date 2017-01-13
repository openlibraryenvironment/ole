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
package org.kuali.ole.select.document.web.struts;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.CreditMemoAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.document.web.struts.PurchasingAccountsPayableFormBase;
import org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase;
import org.kuali.ole.module.purap.document.web.struts.VendorCreditMemoAction;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.select.document.service.OleCreditMemoService;
import org.kuali.ole.select.document.service.OlePurapAccountingService;
import org.kuali.ole.select.document.validation.event.OleCreditMemoDescEvent;
import org.kuali.ole.select.document.validation.event.OleForeignCurrencyCreditMemoEvent;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class OleVendorCreditMemoAction extends VendorCreditMemoAction {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleVendorCreditMemoAction.class);
    private static transient OlePurapAccountingService olePurapAccountingService;

    private boolean currencyTypeIndicator = true;
    /**
     * @see org.kuali.ole.module.purap.document.web.struts.AccountsPayableActionBase#calculate(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward calculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OleVendorCreditMemoForm creditForm = (OleVendorCreditMemoForm) form;
        OleVendorCreditMemoDocument creditDoc = (OleVendorCreditMemoDocument) creditForm.getDocument();
        if (creditDoc.getVendorDetail().getCurrencyType()!=null){
            if(creditDoc.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        List<OleCreditMemoItem> item = creditDoc.getItems();

        if (!(creditDoc.getVendorDetail() == null || (creditDoc.getVendorDetail() != null &&
                currencyTypeIndicator))) {

            LOG.debug("###########Foreign Currency Field Calculation###########");
            for (int i = 0; item.size() > i; i++) {
                OleCreditMemoItem items = (OleCreditMemoItem) creditDoc.getItem(i);
                Long id = creditDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                Map documentNumberMap = new HashMap();
                documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                Iterator iterator = exchangeRateList.iterator();
                if (iterator.hasNext()) {
                    OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                    items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                    //creditDoc.setForeignVendorInvoiceAmount(creditDoc.getVendorInvoiceAmount().bigDecimalValue().multiply(tempOleExchangeRate.getExchangeRate()));
                }
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new OleForeignCurrencyCreditMemoEvent(creditDoc, items));
                    if (rulePassed) {
                        SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                        if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                            if(!items.getItemForeignUnitCost().equals(new KualiDecimal("0.00"))) {
                                items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                                items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue());
                            }
                        }
                    }
                } else {
                    if (items.getItemExchangeRate() != null && items.getForeignCurrencyExtendedPrice() != null) {

                        if (items.isAdditionalChargeUsd()) {
                            items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue());
                        } else {
                            items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP));

                        }
                    }
                }
            }
        }
        creditDoc.setProrateBy(creditDoc.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : creditDoc.isProrateManual() ? OLEConstants.MANUAL_PRORATE :
                creditDoc.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : creditDoc.isNoProrate() ? OLEConstants.NO_PRORATE : null);
        if(!getOleCreditMemoService().validateVendorCreditMemo(creditDoc)) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.PRORATE_OPTION_REQ);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }
        if ((creditDoc.getProrateBy() != null) && (creditDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || creditDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || creditDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            LOG.debug("Calculation for ProrateItemSurcharge");
            getOleCreditMemoService().calculateCreditMemo(creditDoc);
        } else {
            for (OleCreditMemoItem items : item) {
                if (items.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                    items.setItemSurcharge(BigDecimal.ZERO);
                }
            }
        }

        return super.calculate(mapping, form, request, response);
    }

    /**
     * Add a new item to the document.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleVendorCreditMemoForm purchasingForm = (OleVendorCreditMemoForm) form;
        OleCreditMemoItem item = (OleCreditMemoItem) purchasingForm.getNewPurchasingItemLine();

        OleVendorCreditMemoDocument document = (OleVendorCreditMemoDocument) purchasingForm.getDocument();
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String titleId = null;
        boolean isBibFileExist = false;
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        while (itemIterator.hasNext()) {
            OleCreditMemoItem tempItem = (OleCreditMemoItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                itemCounter++;
            }
        }
        String itemNo = String.valueOf(itemCounter);

        HashMap<String, String> dataMap = new HashMap<String, String>();
        item.setBibInfoBean(new BibInfoBean());
        if (item.getBibInfoBean().getDocStoreOperation() == null) {
            item.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
        }
        String fileName = document.getDocumentNumber() + "_" + itemNo;

        setItemDescription(item, fileName);


        boolean ruleFlag = getKualiRuleService().applyRules(new OleCreditMemoDescEvent(document, item));

        if (ruleFlag) {
            if (item.getItemDescription() == null || item.getItemDescription().isEmpty()) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_REQUIRED, new String[]{"Line Item"});
            } else if (item.getPoUnitPrice() == null || item.getPoUnitPrice().equals(BigDecimal.ZERO)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_UNIT_PRICE_REQUIRED);
            } else {
                if (document.getVendorDetail().getCurrencyType()!=null){
                    if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                        currencyTypeIndicator=true;
                    }
                    else{
                        currencyTypeIndicator=false;
                    }
                }

                if ((document.getVendorDetail() == null) || (document.getVendorDetail().getVendorName() != null && currencyTypeIndicator)) {

                    item = (OleCreditMemoItem) purchasingForm.getAndResetNewPurchasingItemLine();
                    document.creditMemoCalculation(item);
                    document.addItem(item);
                } else {
                    boolean rulePassed = getKualiRuleService().applyRules(new OleForeignCurrencyCreditMemoEvent(document, item));
                    if (rulePassed) {
                        LOG.debug("###########Foreign Currency Field Calculation for Payment Request ###########");

                        Long id = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map documentNumberMap = new HashMap();
                        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        BusinessObjectService businessObjectService = SpringContext
                                .getBean(BusinessObjectService.class);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                        }
                        if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                            item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(item.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                            item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue());

                        }
                        item = (OleCreditMemoItem) purchasingForm.getAndResetNewPurchasingItemLine();
                        document.addItem(item);
                    }
                }
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }


    /**
     * Delete an item from the document.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleVendorCreditMemoForm creditMemoForm = (OleVendorCreditMemoForm) form;
        OleVendorCreditMemoDocument creditMemoDocument = (OleVendorCreditMemoDocument) creditMemoForm.getDocument();
        creditMemoDocument.deleteItem(getSelectedLine(request));
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward continueCreditMemo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleVendorCreditMemoForm rqForm = (OleVendorCreditMemoForm) form;
        OleVendorCreditMemoDocument document = (OleVendorCreditMemoDocument) rqForm.getDocument();
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedContinuePurapEvent(document));
        if (!rulePassed) {
            return super.continueCreditMemo(mapping, form, request, response);
        }
        ActionForward forward = super.continueCreditMemo(mapping, form, request, response);
        if(forward != null){
            return forward;
        }
        SpringContext.getBean(OlePurapService.class).getInitialCollapseSections(document);
        List<OleCreditMemoItem> items = document.getItems();
        OleCreditMemoItem newLineItem = (OleCreditMemoItem) rqForm.getNewPurchasingItemLine();
        document.setProrateQty(true);
        document.setProrateBy(document.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : document.isProrateManual() ? OLEConstants.MANUAL_PRORATE : document.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR :
                 document.isNoProrate() ? OLEConstants.NO_PRORATE : null);
        if (document.getVendorDetail().getCurrencyType()!=null){
            if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }

        if (document.getVendorDetail() != null && (!currencyTypeIndicator)) {
            Long currencyTypeId = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
            Map documentNumberMap = new HashMap();
            documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
            BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
            List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            for (OleCreditMemoItem item : items) {
                iterator = exchangeRateList.iterator();
                if (iterator.hasNext()) {
                    OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                    item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                }
            }
            iterator = exchangeRateList.iterator();
            if (iterator.hasNext()) {
                OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                newLineItem.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
            }
        }
        rqForm.getAndResetNewPurchasingItemLine();

        return forward;
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = super.route(mapping, form, request, response);
        this.calculate(mapping, form, request, response);
        return forward;
    }


    /**
     * @see org.kuali.ole.sys.web.struts.KualiAccountingDocumentActionBase#insertSourceLine(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward insertSourceLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // It would be preferable to find a way to genericize the KualiAccountingDocument methods but this will work for now
        PurchasingAccountsPayableFormBase purapForm = (PurchasingAccountsPayableFormBase) form;

        // index of item selected
        int itemIndex = getSelectedLine(request);
        PurApItem item = null;

        // if custom processing of an accounting line is not done then insert a line generically.
        if (processCustomInsertAccountingLine(purapForm, request) == false) {
            String errorPrefix = null;
            PurApAccountingLine line = null;

            boolean rulePassed = false;
            if (itemIndex >= 0) {
                item = ((PurchasingAccountsPayableDocument) purapForm.getDocument()).getItem((itemIndex));
                //Calculating the dollar amount for the accounting Line.
                PurApAccountingLine lineItem = item.getNewSourceLine();
                if (lineItem.getAccountLinePercent() != null) {
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                } else if (lineItem.getAmount() != null && lineItem.getAccountLinePercent() == null) {
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 0, RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                }
                line = (PurApAccountingLine) ObjectUtils.deepCopy(lineItem);
                //end
                //SpringContext.getBean(AccountService.class).populateAccountingLineChartIfNeeded(line);
                errorPrefix = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + Integer.toString(itemIndex) + "]." + OLEConstants.NEW_SOURCE_ACCT_LINE_PROPERTY_NAME;
                rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, purapForm.getDocument(), line));
            } else if (itemIndex == -2) {
                //corrected: itemIndex == -2 is the only case for distribute account
                //This is the case when we're inserting an accounting line for distribute account.
                line = ((PurchasingFormBase) purapForm).getAccountDistributionnewSourceLine();
                //SpringContext.getBean(AccountService.class).populateAccountingLineChartIfNeeded(line);
                errorPrefix = PurapPropertyConstants.ACCOUNT_DISTRIBUTION_NEW_SRC_LINE;
                rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, purapForm.getDocument(), line));
            }

            if (rulePassed) {
                // add accountingLine
                SpringContext.getBean(PersistenceService.class).retrieveNonKeyFields(line);
                if (itemIndex >= 0) {
                    insertAccountingLine(purapForm, item, line);
                    // clear the temp account
                    item.resetAccount();
                } else if (itemIndex == -2) {
                    //this is the case for distribute account
                    ((PurchasingFormBase) purapForm).addAccountDistributionsourceAccountingLine(line);
                }
            }
        }

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    private OleCreditMemoService getOleCreditMemoService() {
        return SpringContext.getBean(OleCreditMemoService.class);
    }


    private void setItemDescription(OleCreditMemoItem item, String fileName) {
        if (OleDocstoreResponse.getInstance().getEditorResponse() != null) {
            Map<String, OLEEditorResponse> oleEditorResponses = OleDocstoreResponse.getInstance().getEditorResponse();
            OLEEditorResponse oleEditorResponse = oleEditorResponses.get(fileName);
            Bib bib = oleEditorResponse != null ? oleEditorResponse.getBib() : null;
            bib = (Bib) bib.deserializeContent(bib);
            if (bib != null) {
                String title = (bib.getTitle() != null&& !bib.getTitle().isEmpty()) ? bib.getTitle() + ", " : "";
                String author = (bib.getAuthor()!=null && !bib.getAuthor().isEmpty()) ? bib.getAuthor() + ", " : "";
                String publisher = (bib.getPublisher()!=null && !bib.getPublisher().isEmpty()) ? bib.getPublisher() + ", " : "";
                String isbn = (bib.getIsbn()!=null && !bib.getIsbn().isEmpty()) ? bib.getIsbn() + ", " : "";
                String description = title + author + publisher + isbn;
                item.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(bib.getId()));
                item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
            }
            if (bib != null) {
                item.setBibUUID(bib.getId());
                item.setItemTitleId(bib.getId());
            }
            OleDocstoreResponse.getInstance().getEditorResponse().remove(oleEditorResponse);
        }
    }

    /**
     * Calls methods to perform credit allowed calculation and total credit memo amount.
     *
     * @param apDoc An AccountsPayableDocument
     */
    @Override
    protected void customCalculate(PurchasingAccountsPayableDocument apDoc) {
        OleVendorCreditMemoDocument cmDocument = (OleVendorCreditMemoDocument) apDoc;

        // call service method to finish up calculation
        SpringContext.getBean(OleCreditMemoService.class).calculateCreditMemo(cmDocument);

        // notice we're ignoring the boolean because these are just warnings they shouldn't halt anything
        SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(cmDocument));
        // }
    }

    /**
     * This method refreshs the Accounting Line for the below line Items based on the Prorations option selected
     */
    public ActionForward proratedSurchargeRefresh(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleVendorCreditMemoForm vendorCreditMemoForm = (OleVendorCreditMemoForm) form;
        OleVendorCreditMemoDocument creditMemoDocument = (OleVendorCreditMemoDocument) vendorCreditMemoForm.getDocument();
        for (OleCreditMemoItem item : (List<OleCreditMemoItem>) creditMemoDocument.getItems()) {
            if (item.getItemType().isAdditionalChargeIndicator()) {
                List<PurApItem> items = new ArrayList<>();

                /*for (OlePurchaseOrderDocument olePurchaseOrderDocument : oleInvoiceDocument.getPurchaseOrderDocuments()) {
                    for (OlePurchaseOrderItem purItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                        if (purItem.isItemForInvoice()) {
                            items.add(purItem);
                        }
                    }
                }*/
                if (items.size() == 0) {
                    for (OleCreditMemoItem invoiceItem : (List<OleCreditMemoItem>) creditMemoDocument.getItems()) {
                        items.add(invoiceItem);
                    }
                }
                else {
                    for (OleCreditMemoItem invoiceItem : (List<OleCreditMemoItem>) creditMemoDocument.getItems()) {
                        if (!(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                            items.add(invoiceItem);
                        }
                    }
                }
                List<PurApAccountingLine> distributedAccounts = null;
                List<SourceAccountingLine> summaryAccounts = null;
                summaryAccounts = getOlePurapAccountingService().generateSummaryForManual(items);
                distributedAccounts = getOlePurapAccountingService().generateAccountDistributionForProrationByManual(summaryAccounts,CreditMemoAccount.class);
                if (CollectionUtils.isNotEmpty(distributedAccounts)) {
                    item.setSourceAccountingLines(distributedAccounts);
                }

                if (creditMemoDocument.isProrateDollar() || creditMemoDocument.isProrateQty() || creditMemoDocument.isNoProrate()) {
                    calculate(mapping, vendorCreditMemoForm, request, response);
                }
            }

        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public static OlePurapAccountingService getOlePurapAccountingService() {
        if (olePurapAccountingService == null) {
            olePurapAccountingService = SpringContext.getBean(OlePurapAccountingService.class);
        }
        return olePurapAccountingService;
    }

    public ActionForward selectVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //ActionForward forward = super.clearVendor(mapping, form, request, response);
        OleVendorCreditMemoForm vendorCreditMemoForm = (OleVendorCreditMemoForm) form;
        OleVendorCreditMemoDocument creditMemoDocument = (OleVendorCreditMemoDocument) vendorCreditMemoForm.getDocument();
        if (creditMemoDocument.getVendorAliasName() != null && creditMemoDocument.getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, creditMemoDocument.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                Map vendorDetailMap = new HashMap();
                vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                VendorDetail vendorDetail = businessObject.findByPrimaryKey(VendorDetail.class, vendorDetailMap);
                creditMemoDocument.setVendorDetail(vendorDetail);
                creditMemoDocument.setVendorHeaderGeneratedIdentifier(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                creditMemoDocument.setVendorDetailAssignedIdentifier(vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                creditMemoDocument.setVendorName(vendorDetail.getVendorName());
                VendorAddress vendorAddress = businessObject.findByPrimaryKey(VendorAddress.class, vendorDetailMap);
                if (vendorAddress != null) {
                    creditMemoDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
                    creditMemoDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
                    creditMemoDocument.setVendorCityName(vendorAddress.getVendorCityName());
                    creditMemoDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
                    creditMemoDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
                    creditMemoDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
                    creditMemoDocument.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
                }
                refresh(mapping, form, request, response);
            } else {     /* If there is no matching vendor found*/
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}
