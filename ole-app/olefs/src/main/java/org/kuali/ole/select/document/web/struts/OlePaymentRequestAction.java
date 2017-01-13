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


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PaymentRequestStatuses;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PaymentRequestService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.document.web.struts.PaymentRequestAction;
import org.kuali.ole.module.purap.document.web.struts.PurchasingAccountsPayableFormBase;
import org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OlePaymentRequestService;
import org.kuali.ole.select.document.validation.event.OleDiscountPaymentRequestEvent;
import org.kuali.ole.select.document.validation.event.OleForeignCurrencyPaymentRequestEvent;
import org.kuali.ole.select.document.validation.event.OlePaymentRequestDescEvent;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
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


/**
 * This class is the KualiForm class for Ole Payment Request Action
 */
public class OlePaymentRequestAction extends PaymentRequestAction {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePaymentRequestAction.class);

    private boolean currencyTypeIndicator = true;

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.AccountsPayableActionBase#calculate(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward calculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*ActionForward forward = super.calculate(mapping, form, request, response);*/
        /* calculateCurrency(mapping, form, request, response); */
        PurchasingAccountsPayableFormBase purchasingForm = (PurchasingAccountsPayableFormBase) form;
        List<PurApItem> purApItems = ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItems();
        for(PurApItem purApItem:purApItems){
            List<KualiDecimal> existingAmount=new ArrayList<>();
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof PaymentRequestAccount) {
                    if(((PaymentRequestAccount)oldSourceAccountingLine).getExistingAmount()!=null){
                        existingAmount.add(((PaymentRequestAccount)oldSourceAccountingLine).getExistingAmount());
                    }
                }
            }
            int count=0;
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){

                if (ObjectUtils.isNotNull(account.getAccountLinePercent()) || ObjectUtils.isNotNull(account.getAmount())) {
                    if (account.getAmount()!=null&&count<existingAmount.size()&&existingAmount.size() != 0 && !existingAmount.get(count).toString().equals(account.getAmount().toString())) {
                        KualiDecimal calculatedPercent = new KualiDecimal(account.getAmount().multiply(new KualiDecimal(100)).divide(purApItem.getTotalAmount()).toString());
                        account.setAccountLinePercent(calculatedPercent.bigDecimalValue().setScale(OLEConstants.BIG_DECIMAL_SCALE,BigDecimal.ROUND_CEILING));
                    }

                    else {
                        if(account.getAccountLinePercent().intValue()==100&&(account.getAmount()==null||account.getAccount()!=null)){
                            KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                            account.setAmount(calculatedAmount);
                        }
                        else{
                            KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                            account.setAmount(calculatedAmount);
                        }
                    }
                }
                count++;
            }
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof PaymentRequestAccount) {
                    ((PaymentRequestAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                }
            }
        }
        OlePaymentRequestForm paymentForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument payDoc = (OlePaymentRequestDocument) paymentForm.getDocument();
        Map invMap = new HashMap();
        if (payDoc.getInvoiceIdentifier() != null) {
            invMap.put(PurapConstants.PRQSDocumentsStrings.PUR_ID, payDoc.getInvoiceIdentifier());
            payDoc.getInvoiceIdentifier();
            OleInvoiceDocument oleInvoice = SpringContext
                    .getBean(org.kuali.rice.krad.service.BusinessObjectService.class).findByPrimaryKey(OleInvoiceDocument.class, invMap);
            payDoc.setProrateBy(oleInvoice.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : oleInvoice.isProrateManual() ? OLEConstants.MANUAL_PRORATE :
                    oleInvoice.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : oleInvoice.isNoProrate() ? OLEConstants.NO_PRORATE :
                            OLEConstants.DEFAULT_PRORATE_BY_INVOICE);

            List<OleInvoiceItem> item = oleInvoice.getItems();
            for (int i = 0; item.size() > i; i++) {
                OleInvoiceItem items = (OleInvoiceItem) oleInvoice.getItem(i);
                if (items.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_FREIGHT_CODE) && LOG.isDebugEnabled()) {
                    LOG.debug("Item Type Code >>>>>>>>>>>" + items.getItemTypeCode());
                    LOG.debug("items.getItemUnitPrice() >>>>>>>>>" + items.getItemUnitPrice());
                    LOG.debug("items.getTotalAmount() >>>>>>>>" + items.getTotalAmount());

                }
            }

        } else {
            payDoc.setProrateBy(payDoc.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : payDoc.isProrateManual() ? OLEConstants.MANUAL_PRORATE :
                    payDoc.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : payDoc.isNoProrate() ? OLEConstants.NO_PRORATE : null);
        }
        boolean manualProrateValidFlag = true;
        if ((payDoc.getProrateBy() != null) && (payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || payDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            if (payDoc.getProrateBy() != null && payDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                // Validates the prorate surchanges if prorate by manual
                manualProrateValidFlag = getOlePaymentRequestService().validateProratedSurcharge(payDoc);
            }
            if (manualProrateValidFlag) {

                List<OlePaymentRequestItem> item = payDoc.getItems();
                if (payDoc.getVendorDetail().getCurrencyType()!=null){
                    if(payDoc.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                        currencyTypeIndicator=true;
                    }
                    else{
                        currencyTypeIndicator=false;
                    }
                }
                if (payDoc.getVendorDetail() == null || (payDoc.getVendorDetail() != null && currencyTypeIndicator)) {
                    for (int i = 0; item.size() > i; i++) {
                        OlePaymentRequestItem items = (OlePaymentRequestItem) payDoc.getItem(i);
                        if (items.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                            boolean rulePassed = getKualiRuleService().applyRules(
                                    new OleDiscountPaymentRequestEvent(payDoc, items));
                            if (rulePassed) {
                                items.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(items).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                } else {

                    LOG.debug("###########Foreign Currency Field Calculation###########");
                    for (int i = 0; item.size() > i; i++) {
                        OlePaymentRequestItem items = (OlePaymentRequestItem) payDoc.getItem(i);
                        Long id = payDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map documentNumberMap = new HashMap();
                        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext
                                .getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(
                                OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                            payDoc.setForeignVendorInvoiceAmount(payDoc.getVendorInvoiceAmount().bigDecimalValue()
                                    .multiply(tempOleExchangeRate.getExchangeRate()));
                        }
                        if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                            boolean rulePassed = getKualiRuleService().applyRules(
                                    new OleForeignCurrencyPaymentRequestEvent(payDoc, items));
                            if (rulePassed) {
                                SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                                if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                                    if(!items.getItemForeignUnitCost().equals(new KualiDecimal("0.00"))) {
                                        items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                                        items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                                        items.setItemListPrice(items.getItemUnitCostUSD());
                                        //items.setPurchaseOrderItemUnitPrice(items.getItemUnitPrice());
                                    }
                                }
                            }

                        } else {
                            if (items.getItemExchangeRate() != null && items.getForeignCurrencyExtendedPrice() != null) {
                                // added for jira - OLE-2203
                                if (items.isAdditionalChargeUsd()) {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                                } else {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                            }
                        }
                    }
                }
                getOlePaymentRequestService().calculateProrateItemSurcharge(payDoc);
            }
        }

        if (payDoc.getProrateBy() == null && manualProrateValidFlag) {
            getOlePaymentRequestService().calculateWithoutProrates(payDoc);
        }
        List<PurApItem> newpurApItems = ((PurchasingAccountsPayableDocument) paymentForm.getDocument()).getItems();
        for(PurApItem purApItem:newpurApItems){
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){
                KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                account.setAmount(calculatedAmount);
            }
        }
        return super.calculate(mapping, form, request, response);
    }

    /**
     * Add a new Note to the selected PaymentRequestItem.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */

    public ActionForward addNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addNote Method of OlePaymentRequestAction");
        OlePaymentRequestForm paymentForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument paymentDocument = (OlePaymentRequestDocument) paymentForm.getDocument();
        int line = this.getSelectedLine(request);
        OlePaymentRequestItem item = (OlePaymentRequestItem) ((PurchasingAccountsPayableDocument) paymentForm.getDocument()).getItem(line);
        OlePaymentRequestNote note = new OlePaymentRequestNote();
        note.setNote(item.getNote());
//        boolean rulePassed = getKualiRuleService().applyRules(new OleNoteTypeEvent(purDocument,note));
//        if(rulePassed){
        item.getNotes().add(note);
        LOG.debug("Adding InvoiceNote to the PaymentRequestItem");
        item.setNote(null);
        LOG.debug("Leaving addNote Method of OlePaymentRequestAction");
//        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * deletes the selected InvoiceNote for the selected PaymentRequestItem
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteNote Method of OlePaymentRequestAction");
        OlePaymentRequestForm paymentForm = (OlePaymentRequestForm) form;

        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OlePaymentRequestItem item = (OlePaymentRequestItem) ((PurchasingAccountsPayableDocument) paymentForm.getDocument()).getItem((itemIndex));
        item.getNotes().remove(noteIndex);
        LOG.debug("Note deleted for the selected Item");
        LOG.debug("Leaving deleteNote Method of OlePaymentRequestAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
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
        OlePaymentRequestForm purchasingForm = (OlePaymentRequestForm) form;
        OlePaymentRequestItem item = (OlePaymentRequestItem) purchasingForm.getNewPurchasingItemLine();
        item.getNewSourceLine().setAccountLinePercent(new BigDecimal(100));
        // purchasingForm.getNewPurchasingItemLine().setItemDescription((item.getBibInfoBean().getTitle() != null ?
        // item.getBibInfoBean().getTitle() : "") + (item.getBibInfoBean().getAuthor() != null ? "," +
        // item.getBibInfoBean().getAuthor() : "") + (item.getBibInfoBean().getPublisher() != null ? "," +
        // item.getBibInfoBean().getPublisher() : "") + (item.getBibInfoBean().getIsbn() != null ? "," +
        // item.getBibInfoBean().getIsbn() : ""));
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) purchasingForm.getDocument();
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String titleId = null;
        boolean isBibFileExist = false;
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        while (itemIterator.hasNext()) {
            OlePaymentRequestItem tempItem = (OlePaymentRequestItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                itemCounter++;
            }
        }
        String itemNo = String.valueOf(itemCounter);
        //String itemNo = String.valueOf(document.getItems().size() - 8);
        HashMap<String, String> dataMap = new HashMap<String, String>();
        item.setBibInfoBean(new BibInfoBean());
        if (item.getBibInfoBean().getDocStoreOperation() == null) {
            item.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
        }
        String tokenId = document.getDocumentNumber() + "_" + itemNo;

        setItemDescription(item, tokenId);

        /*dataMap.put(OleSelectConstant.FILEPATH, fileProcessingService.getMarcXMLFileDirLocation());
        dataMap.put(OleSelectConstant.FILENAME, fileName);
        if (fileProcessingService.isCreateFileExist(dataMap)) {
            isBibFileExist = true;
        }
        if (isBibFileExist) {
            titleId = docStore.getTitleIdByMarcXMLFileProcessing(item.getBibInfoBean(), dataMap);
            item.setItemTitleId(titleId);
            BibInfoBean xmlBibInfoBean = new BibInfoBean();
            dataMap.put(OleSelectConstant.TITLE_ID, titleId);
            dataMap.put(OleSelectConstant.DOC_CATEGORY_TYPE, OleSelectConstant.DOC_CATEGORY_TYPE_ITEMLINKS);
            xmlBibInfoBean = docStore.getBibInfo(dataMap);
            item.setBibInfoBean(xmlBibInfoBean);
            purchasingForm.getNewPurchasingItemLine().setItemDescription((item.getBibInfoBean().getTitle() != null ? item.getBibInfoBean().getTitle() : "") + (item.getBibInfoBean().getAuthor() != null ? "," + item.getBibInfoBean().getAuthor() : "") + (item.getBibInfoBean().getPublisher() != null ? "," + item.getBibInfoBean().getPublisher() : "") + (item.getBibInfoBean().getIsbn() != null ? "," + item.getBibInfoBean().getIsbn() : ""));

            HashMap<String,String> queryMap = new HashMap<String,String>();
            queryMap.put(OleSelectConstant.DocStoreDetails.ITEMLINKS_KEY, item.getItemTitleId());
            List<DocInfoBean> docStoreResult = docStore.searchBibInfo(queryMap);
            Iterator bibIdIterator = docStoreResult.iterator();
            if(bibIdIterator.hasNext()){
                DocInfoBean docInfoBean = (DocInfoBean)bibIdIterator.next();
                item.setBibUUID(docInfoBean.getUniqueId());
            }
        }*/
        boolean ruleFlag = getKualiRuleService().applyRules(new OlePaymentRequestDescEvent(document, item));
        if (ruleFlag) {
            if (document.getVendorDetail().getCurrencyType()!=null){
                if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }
            if ((document.getVendorDetail() == null) || (document.getVendorDetail().getVendorName() != null && currencyTypeIndicator)) {
                boolean rulePassed = getKualiRuleService().applyRules(new OleDiscountPaymentRequestEvent(document, item));
                if (rulePassed) {
                    purchasingForm.getNewPurchasingItemLine().setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
                    item = (OlePaymentRequestItem) purchasingForm.getAndResetNewPurchasingItemLine();
                    document.addItem(item);
                }
            } else {
                boolean rulePassed = getKualiRuleService().applyRules(new OleForeignCurrencyPaymentRequestEvent(document, item));
                if (rulePassed) {
                    LOG.debug("###########Foreign Currency Field Calculation for Payment Request ###########");
                    SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(item);
                    Long id = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                    Map documentNumberMap = new HashMap();
                    documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                    org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                    List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                    Iterator iterator = exchangeRateList.iterator();
                    if (iterator.hasNext()) {
                        OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                        item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                    }
                    if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                        item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(item.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                        item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                        item.setItemListPrice(item.getItemUnitCostUSD());
                    }
                    item = (OlePaymentRequestItem) purchasingForm.getAndResetNewPurchasingItemLine();
                    document.addItem(item);
                }
            }
        }
        /*if(item.getDonorCode() == null){
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectPropertyConstants.ERROR_DONOR_CODE,"donorCode");
        }*/
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
        OlePaymentRequestForm purchasingForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument purDocument = (OlePaymentRequestDocument) purchasingForm.getDocument();
        purDocument.deleteItem(getSelectedLine(request));
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward continuePREQ(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OlePaymentRequestForm rqForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) rqForm.getDocument();
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedContinuePurapEvent(document));
        if (!rulePassed) {
            return super.continuePREQ(mapping, form, request, response);
        }
        ActionForward forward = super.continuePREQ(mapping, form, request, response);
        SpringContext.getBean(OlePurapService.class).getInitialCollapseSections(document);
        List<OlePaymentRequestItem> items = document.getItems();
        OlePaymentRequestItem newLineItem = (OlePaymentRequestItem) rqForm.getNewPurchasingItemLine();
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
            org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            for (OlePaymentRequestItem item : items) {
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
        this.calculate(mapping, form, request, response);
        ActionForward forward = super.route(mapping, form, request, response);
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) ((OlePaymentRequestForm) form).getDocument();
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OlePaymentRequestItem tempItem = (OlePaymentRequestItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)
                    || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                List<PurApAccountingLine> accountingLineBase = tempItem.getSourceAccountingLines();
                if (accountingLineBase != null) {
                    for (int accountingLine = 0; accountingLine < accountingLineBase.size(); accountingLine++) {
                        String accountNumber = accountingLineBase.get(accountingLine).getAccountNumber();
                        String chartOfAccountsCode = accountingLineBase.get(accountingLine).getChartOfAccountsCode();
                        Map<String, String> criteria = new HashMap<String, String>();
                        criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
                        criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
                        Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                                Account.class, criteria);
                        rulePassed = checkForValidAccount(account);
                        if (!rulePassed) {
                            return mapping.findForward(OLEConstants.MAPPING_BASIC);
                        }
                    }
                }
            }
        }
       /* if (rulePassed) {
            this.calculate(mapping, form, request, response);
        }*/
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
            AccountingLineBase accountingLineBase = (AccountingLineBase) item.getNewSourceLine();
            if (accountingLineBase != null) {
                String accountNumber = accountingLineBase.getAccountNumber();
                String chartOfAccountsCode = accountingLineBase.getChartOfAccountsCode();
                Map<String, String> criteria = new HashMap<String, String>();
                criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
                criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
                Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class,
                        criteria);
                rulePassed = checkForValidAccount(account);
            }

          /*  if (rulePassed) {
                // add accountingLine
                SpringContext.getBean(PersistenceService.class).retrieveNonKeyFields(line);
                if (itemIndex >=0) {
                    insertAccountingLine(purapForm, item, line);
                    // clear the temp account
                    item.resetAccount();
                }
                else if (itemIndex == -2) {
                    //this is the case for distribute account
                    ((PurchasingFormBase)purapForm).addAccountDistributionsourceAccountingLine(line);
                }
            }*/
            if (rulePassed) {
                // add accountingLine
                SpringContext.getBean(PersistenceService.class).retrieveNonKeyFields(line);

                PurApAccountingLine newSourceLine = item.getNewSourceLine();
                List<PurApAccountingLine> existingSourceLine = item.getSourceAccountingLines();

                BigDecimal initialValue = new BigDecimal(0);

                for (PurApAccountingLine accountLine : existingSourceLine) {
                    initialValue = initialValue.add(accountLine.getAccountLinePercent());
                }
                if (itemIndex >= 0) {

                    if ((newSourceLine.getAccountLinePercent().intValue() <= OleSelectConstant.ACCOUNTINGLINE_PERCENT_HUNDRED && newSourceLine.getAccountLinePercent().intValue() <= OleSelectConstant.MAX_PERCENT.subtract(initialValue).intValue()) && newSourceLine.getAccountLinePercent().intValue() > OleSelectConstant.ZERO) {
                        if (OleSelectConstant.MAX_PERCENT.subtract(initialValue).intValue() != OleSelectConstant.ZERO) {
                            insertAccountingLine(purapForm, item, line);
                        }
                    }else {
                        checkAccountingLinePercent(newSourceLine);

                    }
                    for(PurApAccountingLine oldSourceAccountingLine:item.getSourceAccountingLines()) {
                        if(oldSourceAccountingLine instanceof PaymentRequestAccount) {
                            ((PaymentRequestAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                        }
                    }
                    List<PurApAccountingLine> existingAccountingLine = item.getSourceAccountingLines();
                    BigDecimal totalPercent = new BigDecimal(100);
                    BigDecimal initialPercent = new BigDecimal(0);
                    for (PurApAccountingLine purApAccountingLine : existingAccountingLine) {
                        initialPercent = initialPercent.add(purApAccountingLine.getAccountLinePercent());

                    }
                    initialPercent = totalPercent.subtract(initialPercent);
                    BigDecimal maxPercent = initialPercent.max(OleSelectConstant.ZERO_PERCENT);
                    if (maxPercent.intValue() == OleSelectConstant.ZERO) {
                        item.resetAccount(OleSelectConstant.ZERO_PERCENT);

                    } else {
                        item.resetAccount(initialPercent);

                    }
                } else if (itemIndex == -2) {
                    //this is the case for distribute account
                    ((PurchasingFormBase) purapForm).addAccountDistributionsourceAccountingLine(line);
                }
            }
        }


        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    private void checkAccountingLinePercent(PurApAccountingLine newSourceLine) {
        if (newSourceLine.getAccountLinePercent().intValue() >= OleSelectConstant.ACCOUNTINGLINE_PERCENT_HUNDRED) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OleSelectPropertyConstants.ERROR_PERCENT_SHOULD_GREATER, OleSelectConstant.PERCENT);
        } else if (newSourceLine.getAccountLinePercent().intValue() == OleSelectConstant.ZERO) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OleSelectPropertyConstants.ERROR_PERCENT_ZERO, OleSelectConstant.PERCENT);
        } else {

        }

    }

    private boolean checkForValidAccount(Account account) {
        boolean result = true;
        if (account != null) {
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER,
                        new String[]{OleSelectConstant.PAYMENT_REQUEST});
                result = false;
            }
        }
        return result;
    }

    private void setItemDescription(OlePaymentRequestItem item, String tokenId) {
        if (OleDocstoreResponse.getInstance().getEditorResponse() != null) {
            Map<String, OLEEditorResponse> oleEditorResponses = OleDocstoreResponse.getInstance().getEditorResponse();
            OLEEditorResponse oleEditorResponse = oleEditorResponses.get(tokenId);
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

    private OlePaymentRequestService getOlePaymentRequestService() {
        return SpringContext.getBean(OlePaymentRequestService.class);
    }

    @Override
    protected void customCalculate(PurchasingAccountsPayableDocument apDoc) {
        OlePaymentRequestDocument preqDoc = (OlePaymentRequestDocument) apDoc;
        if ((preqDoc.getProrateBy() != null) && (preqDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || preqDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || preqDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            // set amounts on any empty
            preqDoc.updateExtendedPriceOnItems();

            // calculation just for the tax area, only at tax review stage
            // by now, the general calculation shall have been done.
            if (preqDoc.getApplicationDocumentStatus().equals(PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                SpringContext.getBean(PaymentRequestService.class).calculateTaxArea(preqDoc);
                return;
            }

            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
            // Calculate Payment request before rules since the rule check totalAmount.
            SpringContext.getBean(OlePaymentRequestService.class).calculatePaymentRequest(preqDoc, true);
            SpringContext.getBean(KualiRuleService.class).applyRules(
                    new AttributedCalculateAccountsPayableEvent(preqDoc));
        } else {
            super.customCalculate(preqDoc);
        }
    }

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        this.calculate(mapping, form, request, response);
        ActionForward forward = super.blanketApprove(mapping, form, request, response);
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) ((OlePaymentRequestForm) form).getDocument();
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OlePaymentRequestItem tempItem = (OlePaymentRequestItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)
                    || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                List<PurApAccountingLine> accountingLineBase = tempItem.getSourceAccountingLines();
                if (accountingLineBase != null) {
                    for (int accountingLine = 0; accountingLine < accountingLineBase.size(); accountingLine++) {
                        String accountNumber = accountingLineBase.get(accountingLine).getAccountNumber();
                        String chartOfAccountsCode = accountingLineBase.get(accountingLine).getChartOfAccountsCode();
                        Map<String, String> criteria = new HashMap<String, String>();
                        criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
                        criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
                        Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                                Account.class, criteria);
                        rulePassed = checkForValidAccount(account);
                        if (!rulePassed) {
                            return mapping.findForward(OLEConstants.MAPPING_BASIC);
                        }
                    }
                }
            }
        }
        return forward;
    }

    public ActionForward selectVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //ActionForward forward = super.clearVendor(mapping, form, request, response);
        OlePaymentRequestForm preqForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) preqForm.getDocument();
        if (document.getVendorAliasName() != null && document.getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, document.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                Map vendorDetailMap = new HashMap();
                vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                VendorDetail vendorDetail = businessObject.findByPrimaryKey(VendorDetail.class, vendorDetailMap);
                document.setVendorDetail(vendorDetail);
                document.setVendorHeaderGeneratedIdentifier(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                document.setVendorDetailAssignedIdentifier(vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                document.setVendorName(vendorDetail.getVendorName());
                VendorAddress vendorAddress = businessObject.findByPrimaryKey(VendorAddress.class, vendorDetailMap);
                if (vendorAddress != null) {
                    document.setVendorLine1Address(vendorAddress.getVendorLine1Address());
                    document.setVendorLine2Address(vendorAddress.getVendorLine2Address());
                    document.setVendorCityName(vendorAddress.getVendorCityName());
                    document.setVendorStateCode(vendorAddress.getVendorStateCode());
                    document.setVendorPostalCode(vendorAddress.getVendorZipCode());
                    document.setVendorCountryCode(vendorAddress.getVendorCountryCode());
                    document.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
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

    public ActionForward relinkPO(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        OlePaymentRequestForm preqForm = (OlePaymentRequestForm) form;
        OlePaymentRequestDocument document = (OlePaymentRequestDocument) preqForm.getDocument();
        int itemIndex = getSelectedLine(request);
        OlePaymentRequestItem item = (OlePaymentRequestItem) ((PurchasingAccountsPayableDocument)document).getItem((itemIndex));
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put("itemIdentifier",item.getPoItemIdentifier().toString());
        OleInvoiceItem invoiceItem = getBusinessObjectService().findByPrimaryKey(OleInvoiceItem.class,criteriaMap);
        invoiceItem.setPoItemIdentifier(Integer.valueOf(item.getPurchaseOrderItemID()));
        invoiceItem.setItemTitleId(item.getItemTitleId());
        invoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(document.getAccountsPayablePurchasingDocumentLinkIdentifier());
        if(invoiceItem.getInvoiceDocument()!=null && invoiceItem.getInvoiceDocument().getDocumentNumber()!=null ){
            invoiceItem.getInvoiceDocument().setDocumentHeader((SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceItem.getInvoiceDocument().getDocumentNumber())));
            getBusinessObjectService().save(invoiceItem.getInvoiceDocument());
        }
        getBusinessObjectService().save(document);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

}