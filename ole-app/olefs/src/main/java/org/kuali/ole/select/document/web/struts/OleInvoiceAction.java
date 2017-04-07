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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.document.web.struts.InvoiceAction;
import org.kuali.ole.module.purap.document.web.struts.PurchasingAccountsPayableFormBase;
import org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OleInvoiceNote;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.validation.event.OleDiscountInvoiceEvent;
import org.kuali.ole.select.document.validation.event.OleForeignCurrencyInvoiceEvent;
import org.kuali.ole.select.document.validation.event.OleInvoiceDescEvent;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class is the KualiForm class for Ole Invoice Action
 */
public class OleInvoiceAction extends InvoiceAction {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInvoiceAction.class);

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.AccountsPayableActionBase#calculate(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward calculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ActionForward forward = super.calculate(mapping, form, request, response);
        /* calculateCurrency(mapping, form, request, response); */
        OleInvoiceForm paymentForm = (OleInvoiceForm) form;
        OleInvoiceDocument payDoc = (OleInvoiceDocument) paymentForm.getDocument();
        payDoc.setProrateBy(payDoc.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : payDoc.isProrateManual() ? OLEConstants.MANUAL_PRORATE : payDoc.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : null);
        boolean manualProrateValidFlag = true;
        if ((payDoc.getProrateBy() != null) && (payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || payDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            if (payDoc.getProrateBy() != null && payDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                // Validates the prorate surchanges if prorate by manual
                manualProrateValidFlag = getOleInvoiceService().validateProratedSurcharge(payDoc);
            }
            if (manualProrateValidFlag) {

                List<OleInvoiceItem> item = payDoc.getItems();
                if (payDoc.getVendorDetail() == null || (payDoc.getVendorDetail() != null && payDoc.getVendorDetail().getVendorHeader().getVendorForeignIndicator() != true)) {
                    for (int i = 0; item.size() > i; i++) {
                        OleInvoiceItem items = (OleInvoiceItem) payDoc.getItem(i);
                        if (items.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                            boolean rulePassed = getKualiRuleService().applyRules(
                                    new OleDiscountInvoiceEvent(payDoc, items));
                            if (rulePassed) {
                                items.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(items));
                            }
                        }
                    }
                } else {

                    LOG.debug("###########Foreign Currency Field Calculation###########");
                    for (int i = 0; item.size() > i; i++) {
                        OleInvoiceItem items = (OleInvoiceItem) payDoc.getItem(i);
                        Long id = payDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map documentNumberMap = new HashMap();
                        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        BusinessObjectService businessObjectService = SpringContext
                                .getBean(BusinessObjectService.class);
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
                                    new OleForeignCurrencyInvoiceEvent(payDoc, items));
                            if (rulePassed) {
                                SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                                if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                                    items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                                    items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue());
                                    items.setItemListPrice(items.getItemUnitCostUSD());
                                    items.setPurchaseOrderItemUnitPrice(items.getItemUnitPrice());
                                }
                            }

                        } else {
                            if (items.getItemExchangeRate() != null && items.getForeignCurrencyExtendedPrice() != null) {
                                // added for jira - OLE-2203
                                if (items.isAdditionalChargeUsd()) {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue());
                                } else {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP));
                                }
                            }
                        }
                    }
                }
                getOleInvoiceService().calculateProrateItemSurcharge(payDoc);
            }
        }

        if (payDoc.getProrateBy() == null && manualProrateValidFlag) {
            getOleInvoiceService().calculateWithoutProrates(payDoc);
        }

        return super.calculate(mapping, form, request, response);
    }

    /**
     * Add a new Note to the selected InvoiceItem.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */

    public ActionForward addNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addNote Method of OleInvoiceAction");
        OleInvoiceForm paymentForm = (OleInvoiceForm) form;
        OleInvoiceDocument paymentDocument = (OleInvoiceDocument) paymentForm.getDocument();
        int line = this.getSelectedLine(request);
        OleInvoiceItem item = (OleInvoiceItem) ((PurchasingAccountsPayableDocument) paymentForm.getDocument()).getItem(line);
        OleInvoiceNote note = new OleInvoiceNote();
        note.setNote(item.getNote());
//        boolean rulePassed = getKualiRuleService().applyRules(new OleNoteTypeEvent(purDocument,note));
//        if(rulePassed){
        item.getNotes().add(note);
        LOG.debug("Adding InvoiceNote to the InvoiceItem");
        item.setNote(null);
        LOG.debug("Leaving addNote Method of OleInvoiceAction");
//        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * deletes the selected InvoiceNote for the selected InvoiceItem
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteNote Method of OleInvoiceAction");
        OleInvoiceForm paymentForm = (OleInvoiceForm) form;

        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleInvoiceItem item = (OleInvoiceItem) ((PurchasingAccountsPayableDocument) paymentForm.getDocument()).getItem((itemIndex));
        item.getNotes().remove(noteIndex);
        LOG.debug("Note deleted for the selected Item");
        LOG.debug("Leaving deleteNote Method of OleInvoiceAction");
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
        OleInvoiceForm purchasingForm = (OleInvoiceForm) form;
        OleInvoiceItem item = (OleInvoiceItem) purchasingForm.getNewPurchasingItemLine();
        // purchasingForm.getNewPurchasingItemLine().setItemDescription((item.getBibInfoBean().getTitle() != null ?
        // item.getBibInfoBean().getTitle() : "") + (item.getBibInfoBean().getAuthor() != null ? "," +
        // item.getBibInfoBean().getAuthor() : "") + (item.getBibInfoBean().getPublisher() != null ? "," +
        // item.getBibInfoBean().getPublisher() : "") + (item.getBibInfoBean().getIsbn() != null ? "," +
        // item.getBibInfoBean().getIsbn() : ""));
        OleInvoiceDocument document = (OleInvoiceDocument) purchasingForm.getDocument();
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String titleId = null;
        boolean isBibFileExist = false;
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        while (itemIterator.hasNext()) {
            OleInvoiceItem tempItem = (OleInvoiceItem) itemIterator.next();
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
        String fileName = document.getDocumentNumber() + "_" + itemNo;

        setItemDescription(item, fileName);

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
        boolean ruleFlag = getKualiRuleService().applyRules(new OleInvoiceDescEvent(document, item));
        if (ruleFlag) {
            if ((document.getVendorDetail() == null) || (document.getVendorDetail().getVendorName() != null && !document.getVendorDetail().getVendorHeader().getVendorForeignIndicator())) {
                boolean rulePassed = getKualiRuleService().applyRules(new OleDiscountInvoiceEvent(document, item));
                if (rulePassed) {
                    purchasingForm.getNewPurchasingItemLine().setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item));
                    item = (OleInvoiceItem) purchasingForm.getAndResetNewPurchasingItemLine();
                    document.addItem(item);
                }
            } else {
                boolean rulePassed = getKualiRuleService().applyRules(new OleForeignCurrencyInvoiceEvent(document, item));
                if (rulePassed) {
                    LOG.debug("###########Foreign Currency Field Calculation for Invoice ###########");
                    SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(item);
                    Long id = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                    Map documentNumberMap = new HashMap();
                    documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                    BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                    List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                    Iterator iterator = exchangeRateList.iterator();
                    if (iterator.hasNext()) {
                        OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                        item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                    }
                    if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                        item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(item.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                        item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue());
                        item.setItemListPrice(item.getItemUnitCostUSD());
                    }
                    item = (OleInvoiceItem) purchasingForm.getAndResetNewPurchasingItemLine();
                    document.addItem(item);
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
        OleInvoiceForm purchasingForm = (OleInvoiceForm) form;
        OleInvoiceDocument purDocument = (OleInvoiceDocument) purchasingForm.getDocument();
        purDocument.deleteItem(getSelectedLine(request));
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward continuePRQS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleInvoiceForm rqForm = (OleInvoiceForm) form;
        OleInvoiceDocument document = (OleInvoiceDocument) rqForm.getDocument();
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedContinuePurapEvent(document));
        if (!rulePassed) {
            return super.continuePRQS(mapping, form, request, response);
        }
        ActionForward forward = super.continuePRQS(mapping, form, request, response);
        List<OleInvoiceItem> items = document.getItems();
        OleInvoiceItem newLineItem = (OleInvoiceItem) rqForm.getNewPurchasingItemLine();
        if (document.getVendorDetail() != null && document.getVendorDetail().getVendorHeader().getVendorForeignIndicator()) {
            Long currencyTypeId = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
            Map documentNumberMap = new HashMap();
            documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
            BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
            List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            for (OleInvoiceItem item : items) {
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
        OleInvoiceDocument document = (OleInvoiceDocument) ((OleInvoiceForm) form).getDocument();
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OleInvoiceItem tempItem = (OleInvoiceItem) itemIterator.next();
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
        if (rulePassed) {
            this.calculate(mapping, form, request, response);
        }
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
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
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

    private void setItemDescription(OleInvoiceItem item, String fileName) {
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
                item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
            }
            if (bib != null) {
                item.setBibUUID(bib.getId());
                item.setItemTitleId(bib.getId());
            }
            OleDocstoreResponse.getInstance().getEditorResponse().remove(oleEditorResponse);
        }
    }

    private OleInvoiceService getOleInvoiceService() {
        return SpringContext.getBean(OleInvoiceService.class);
    }

    @Override
    protected void customCalculate(PurchasingAccountsPayableDocument apDoc) {
        OleInvoiceDocument preqDoc = (OleInvoiceDocument) apDoc;
        if ((preqDoc.getProrateBy() != null) && (preqDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || preqDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || preqDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            // set amounts on any empty
            preqDoc.updateExtendedPriceOnItems();

            // calculation just for the tax area, only at tax review stage
            // by now, the general calculation shall have been done.
            if (preqDoc.getApplicationDocumentStatus().equals(InvoiceStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                SpringContext.getBean(InvoiceService.class).calculateTaxArea(preqDoc);
                return;
            }

            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
            // Calculate Payment request before rules since the rule check totalAmount.
            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(preqDoc, true);
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
        OleInvoiceDocument document = (OleInvoiceDocument) ((OleInvoiceForm) form).getDocument();
        Iterator itemIterator = document.getItems().iterator();
        int itemCounter = 0;
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OleInvoiceItem tempItem = (OleInvoiceItem) itemIterator.next();
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
}