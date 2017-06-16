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
package org.kuali.ole.module.purap.document.web.struts;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.*;
import org.kuali.ole.module.purap.PurapConstants.PODocumentsStrings;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.*;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OlePurchaseOrderAmendmentDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OlePurchaseOrderService;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.select.document.validation.event.CopiesPurchaseOrderEvent;
import org.kuali.ole.select.document.validation.event.DiscountPurchaseOrderEvent;
import org.kuali.ole.select.document.validation.event.ForeignCurrencyPOEvent;
import org.kuali.ole.select.document.validation.event.OlePurchaseOrderDescEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.BlankFormFile;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.struts.form.pojo.PojoForm;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Struts Action for Purchase Order document.
 */
public class OlePurchaseOrderAction extends PurchaseOrderAction {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderAction.class);
    private final String UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkIn&stringContent=";
    private final String CHECKOUT_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkOut&uuid=";
    private final String CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING = "docAction=ingestContent&stringContent=";
    private static transient ConfigurationService kualiConfigurationService;
    private DocstoreClientLocator docstoreClientLocator;
    private boolean currencyTypeIndicator = true;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    /**
     * Takes care of storing the action form in the User session and forwarding to the prlookup action.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward performPRLookup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward forward = super.performLookup(mapping, form, request, response);
        String path = forward.getPath();
        if (path.contains("kr/lookup.do")) {
            path = path.replace("kr/lookup.do", "prlookup.do");
            // path = path.replace("kr/lookup.do", "ptrnlookup.do");
        } else if (path.contains("lookup.do")) {
            path = path.replace("lookup.do", "prlookup.do");
            // path = path.replace("lookup.do", "ptrnlookup.do");
        }
        forward.setPath(path);

        return forward;

    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchaseOrderAction#calculate(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward calculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PurchasingAccountsPayableFormBase purchasingForm = (PurchasingAccountsPayableFormBase) form;
        List<PurApItem> purApItems = ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItems();
        for(PurApItem purApItem:purApItems){
            List<KualiDecimal> existingAmount=new ArrayList<>();
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof OlePurchaseOrderAccount) {
                    if(((OlePurchaseOrderAccount)oldSourceAccountingLine).getExistingAmount()!=null){
                        existingAmount.add(((OlePurchaseOrderAccount)oldSourceAccountingLine).getExistingAmount());
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
                if(oldSourceAccountingLine instanceof OlePurchaseOrderAccount) {
                    ((OlePurchaseOrderAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                }
            }
        }
        ActionForward forward = super.calculate(mapping, form, request, response);
        /* calculateCurrency(mapping, form, request, response); */
        purchasingForm = (PurchasingAccountsPayableFormBase) form;
        PurchasingDocument purDoc = (PurchasingDocument) purchasingForm.getDocument();
        PurchasingFormBase formBase = (PurchasingFormBase) form;

        PurchaseOrderDocument purchaseDoc = (PurchaseOrderDocument) formBase.getDocument();
        List<OlePurchaseOrderItem> purItem = purchaseDoc.getItems();
        if (purchaseDoc.getVendorDetail().getCurrencyType()!=null){
            if(purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        if (purDoc.getVendorDetail() == null || (purDoc.getVendorDetail() != null && currencyTypeIndicator)) {
            for (int i = 0; purDoc.getItems().size() > i; i++) {
                OlePurchaseOrderItem item = (OlePurchaseOrderItem) purDoc.getItem(i);
                if ((item.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new DiscountPurchaseOrderEvent(purchaseDoc, item));
                    if (rulePassed) {
                        item.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    rulePassed = getKualiRuleService().applyRules(new CopiesPurchaseOrderEvent(purDoc, item));
                }

            }
        } else {
            LOG.debug("###########Foreign Currency Field Calculation in olepurchaseOrder action###########");
            BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
            for (int i = 0; purItem.size() > i; i++) {
                OlePurchaseOrderItem items = (OlePurchaseOrderItem) purchaseDoc.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new ForeignCurrencyPOEvent(purchaseDoc, items));
                    if (rulePassed) {
                        SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                        Long id = purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map currencyTypeMap = new HashMap();
                        currencyTypeMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, currencyTypeMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            String documentNumber = purchaseDoc.getDocumentNumber();
                            Map documentNumberMap = new HashMap();
                            documentNumberMap.put(OLEPropertyConstants.DOCUMENT_NUMBER, documentNumber);
                            List<OlePurchaseOrderItem> currenctExchangeRateList = (List) businessObjectService.findMatching(OlePurchaseOrderItem.class, documentNumberMap);
                            Iterator iterate = currenctExchangeRateList.iterator();
                            if (iterate.hasNext()) {
                                OlePurchaseOrderItem tempCurrentExchangeRate = (OlePurchaseOrderItem) iterate.next();
                                String poCurrencyType = null;
                                if (tempCurrentExchangeRate.getPurchaseOrder().getVendorDetail().getCurrencyType() != null) {
                                    poCurrencyType = tempCurrentExchangeRate.getPurchaseOrder().getVendorDetail().getCurrencyType().getCurrencyType();
                                }
                                String poaCurrencyType = purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyType();
                                if (poCurrencyType != null && (poCurrencyType.equalsIgnoreCase(poaCurrencyType)) && !items.isLatestExchangeRate() && !purchaseDoc.getIsPODoc() && ((purchaseDoc instanceof PurchaseOrderAmendmentDocument) || (purchaseDoc instanceof PurchaseOrderSplitDocument) || (purchaseDoc instanceof PurchaseOrderReopenDocument))) {
                                    items.setItemExchangeRate(tempCurrentExchangeRate.getItemExchangeRate());
                                } else {
                                    items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                                }
                            }
                            if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                                items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(items.getItemExchangeRate(), 6, RoundingMode.HALF_UP)));
                                items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                                items.setItemListPrice(items.getItemUnitCostUSD());
                            }
                        }
                    }
                }
            }
        }

        purchasingForm = (PurchasingAccountsPayableFormBase) form;
        List<PurApItem> newpurApItems = ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItems();
        for(PurApItem purApItem:newpurApItems){
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){
                KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                account.setAmount(calculatedAmount);
            }
        }
        forward = super.calculate(mapping, form, request, response);
        //    setEnumerationToCopies(purItem);


        // Added for SFC - Start

        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = purDoc.getSourceAccountingLines();
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            String notificationOption = null;
            boolean sufficientFundCheck;
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
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    GlobalVariables.getMessageMap().putError(
                            OLEConstants.SufficientFundCheck.ERROR_MSG_FOR_INSUFF_FUND, RiceKeyConstants.ERROR_CUSTOM,
                            OLEConstants.SufficientFundCheck.INSUFF_FUND_POA + accLine.getAccountNumber());
                }
            }
        }

        // End

        // formBase.setCalculated(true);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside the OlePurchaseOrderAction class Calculate" + formBase.getNewPurchasingItemLine().getItemUnitPrice());
        }
        return forward;
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchaseOrderAction#addItem(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("###########Inside AddItem in olePurchaseOrderAction ###########");
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        OlePurchaseOrderItem purchaseOrderItem = (OlePurchaseOrderItem) purchasingForm.getNewPurchasingItemLine();
        purchaseOrderItem.getNewSourceLine().setAccountLinePercent(new BigDecimal(100));
        //purchasingForm.getNewPurchasingItemLine().setItemDescription((purchaseOrderItem.getBibInfoBean().getTitle() != null ? purchaseOrderItem.getBibInfoBean().getTitle() : "") + (purchaseOrderItem.getBibInfoBean().getAuthor() != null ? "," + purchaseOrderItem.getBibInfoBean().getAuthor() : "") + (purchaseOrderItem.getBibInfoBean().getPublisher() != null ? "," + purchaseOrderItem.getBibInfoBean().getPublisher() : "") + (purchaseOrderItem.getBibInfoBean().getIsbn() != null ? "," + purchaseOrderItem.getBibInfoBean().getIsbn() : ""));
        PurchaseOrderDocument document = (PurchaseOrderDocument) purchasingForm.getDocument();

        // changes done for BibEditor starts

        OlePurchaseOrderForm oleForm = (OlePurchaseOrderForm) form;
        PurchaseOrderDocument doc = (PurchaseOrderDocument) oleForm.getDocument();
        Iterator itemIterator = doc.getItems().iterator();
        int itemCounter = 0;
        while (itemIterator.hasNext()) {
            OlePurchaseOrderItem tempItem = (OlePurchaseOrderItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                itemCounter++;
            }
        }
        String itemNo = String.valueOf(itemCounter);
        //String itemNo = String.valueOf(doc.getItems().size() - 4);
        HashMap<String, String> dataMap = new HashMap<String, String>();
        BibInfoBean xmlBibInfoBean = new BibInfoBean();
        if (purchaseOrderItem.getBibInfoBean() == null) {
            purchaseOrderItem.setBibInfoBean(xmlBibInfoBean);
            if (purchaseOrderItem.getBibInfoBean().getDocStoreOperation() == null) {
                purchaseOrderItem.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
            }
        }
        String fileName = document.getDocumentNumber() + "_" + itemNo;

        // Modified for jira OLE - 2437 starts

        setItemDescription(purchaseOrderItem, fileName);
        //    purchaseOrderItem.setStartingCopyNumber(new KualiInteger(1));

        // Modified for jira OLE - 2437 ends

       /* dataMap.put(OleSelectConstant.FILEPATH, fileProcessingService.getMarcXMLFileDirLocation());
        dataMap.put(OleSelectConstant.FILENAME, fileName);
        if (fileProcessingService.isCreateFileExist(dataMap)) {
            isBibFileExist = true;
        }
        if (isBibFileExist) {
            titleId = docStore.getTitleIdByMarcXMLFileProcessing(purchaseOrderItem.getBibInfoBean(), dataMap);
            purchaseOrderItem.setItemTitleId(titleId);
            dataMap.put(OleSelectConstant.TITLE_ID, titleId);
            dataMap.put(OleSelectConstant.DOC_CATEGORY_TYPE, OleSelectConstant.DOC_CATEGORY_TYPE_ITEMLINKS);
            xmlBibInfoBean = docStore.getBibInfo(dataMap);
            purchaseOrderItem.setBibInfoBean(xmlBibInfoBean);
            purchasingForm.getNewPurchasingItemLine().setItemDescription((purchaseOrderItem.getBibInfoBean().getTitle() != null ? purchaseOrderItem.getBibInfoBean().getTitle() : "") + (purchaseOrderItem.getBibInfoBean().getAuthor() != null ? "," + purchaseOrderItem.getBibInfoBean().getAuthor() : "") + (purchaseOrderItem.getBibInfoBean().getPublisher() != null ? "," + purchaseOrderItem.getBibInfoBean().getPublisher() : "") + (purchaseOrderItem.getBibInfoBean().getIsbn() != null ? "," + purchaseOrderItem.getBibInfoBean().getIsbn() : ""));

            HashMap<String,String> queryMap = new HashMap<String,String>();
            queryMap.put(OleSelectConstant.DocStoreDetails.ITEMLINKS_KEY, purchaseOrderItem.getItemTitleId());
            List<DocInfoBean> docStoreResult = docStore.searchBibInfo(queryMap);
            Iterator bibIdIterator = docStoreResult.iterator();
            if(bibIdIterator.hasNext()){
                DocInfoBean docInfoBean = (DocInfoBean)bibIdIterator.next();
                purchaseOrderItem.setBibUUID(docInfoBean.getUniqueId());
            }
        }*/
        // changes done for BibEditor ends
        if (document.getVendorDetail().getCurrencyType()!=null){
            if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        boolean ruleFlag = getKualiRuleService().applyRules(new OlePurchaseOrderDescEvent(document, purchaseOrderItem));
        if (ruleFlag) {
            if ((document.getVendorDetail() == null) || (document.getVendorDetail().getVendorName() != null && currencyTypeIndicator)) {
                boolean rulePassed = getKualiRuleService().applyRules(new DiscountPurchaseOrderEvent(document, purchaseOrderItem));
                if (rulePassed) {
                    purchasingForm.getNewPurchasingItemLine().setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(purchaseOrderItem).setScale(2, BigDecimal.ROUND_HALF_UP));
                    super.addItem(mapping, purchasingForm, request, response);
                }
            } else {
                boolean rulePassed = getKualiRuleService().applyRules(new ForeignCurrencyPOEvent(document, purchaseOrderItem));
                if (rulePassed) {
                    LOG.debug("###########Foreign Currency Field additem for purchase Order ###########");
                    SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(purchaseOrderItem);
                    Long id = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                    Map documentNumberMap = new HashMap();
                    documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                    BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                    List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                    Iterator iterator = exchangeRateList.iterator();
                    if (iterator.hasNext()) {
                        OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                        purchaseOrderItem.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                    }
                    if (purchaseOrderItem.getItemExchangeRate() != null && purchaseOrderItem.getItemForeignUnitCost() != null) {
                        purchaseOrderItem.setItemUnitCostUSD(new KualiDecimal(purchaseOrderItem.getItemForeignUnitCost().bigDecimalValue().divide(purchaseOrderItem.getItemExchangeRate(), 6, RoundingMode.HALF_UP)));
                        purchaseOrderItem.setItemUnitPrice(purchaseOrderItem.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                        purchaseOrderItem.setItemListPrice(purchaseOrderItem.getItemUnitCostUSD());
                    }
                    super.addItem(mapping, purchasingForm, request, response);
                }
            }
        }
        if(purchaseOrderItem.getClaimDate()==null){
            VendorDetail vendorDetail =document.getVendorDetail();
            if( vendorDetail!=null ){
                String claimInterval = vendorDetail.getClaimInterval();
                if (StringUtils.isNotBlank(claimInterval)) {
                    Integer actIntvl = Integer.parseInt(claimInterval);
                    purchaseOrderItem.setClaimDate(new java.sql.Date(DateUtils.addDays(new java.util.Date(), actIntvl).getTime()));
                }
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Add Note for the selected PurchaseOrderItem
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return An ActionForward
     * @throws Exception
     */

    public ActionForward addNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addNote Method of PurchaseOrderAction");
        PurchaseOrderForm purchasingForm = (PurchaseOrderForm) form;
        int line = this.getSelectedLine(request);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem(line);
        OlePurchaseOrderNotes note = new OlePurchaseOrderNotes();
        note.setNote(item.getNote());
        note.setNoteTypeId(item.getNoteTypeId());
        item.getNotes().add(note);
        LOG.debug("Adding Note to PurchaseOrderItem");
        item.setNote(null);
        item.setNoteTypeId(null);
        LOG.debug("Leaving addNote Method of PurchaseOrderAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * deletes the selected Note for the selected POItem
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteNote Method of PurchaseOrderAction");
        PurchaseOrderForm purchasingForm = (PurchaseOrderForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem((itemIndex));
        item.getNotes().remove(noteIndex);
        LOG.debug("Note deleted for the selected Item");
        LOG.debug("Leaving deleteNote Method of PurchaseOrderAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * This method is overridden to change receivingitem url with respect to OLE
     *
     * @see org.kuali.ole.module.purap.document.web.struts.PurchaseOrderAction#createReceivingLine(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward createReceivingLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside createReceivingLine Method of OlePurchaseOrderAction");
        PurchaseOrderForm poForm = (PurchaseOrderForm) form;
        PurchaseOrderDocument document = (PurchaseOrderDocument) poForm.getDocument();

        String basePath = getApplicationBaseUrl();
        String methodToCallDocHandler = "continueReceivingLine";
        String methodToCallReceivingLine = "initiate";

        //set parameters
        Properties parameters = new Properties();
        parameters.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, methodToCallDocHandler);
        parameters.put(OLEConstants.PARAMETER_COMMAND, methodToCallReceivingLine);
        parameters.put(OLEConstants.DOCUMENT_TYPE_NAME, "OLE_RCVL");
        parameters.put("purchaseOrderId", document.getPurapDocumentIdentifier().toString());

        //create url
        // Changed receivingUrl to point to the OLE receiving URL (OLE-2057)
        String receivingUrl = UrlFactory.parameterizeUrl(basePath + "/" + "selectOleLineItemReceiving.do", parameters);

        //create forward
        ActionForward forward = new ActionForward(receivingUrl, true);
        LOG.debug("Leaving createReceivingLine Method of OlePurchaseOrderAction");
        return forward;
    }

    /**
     * Creates a URL to be used in printing the purchase order.
     *
     * @param basePath     String: The base path of the current URL
     * @param docId        String: The document ID of the document to be printed
     * @param methodToCall String: The name of the method that will be invoked to do this particular print
     * @return The URL
     */
    @Override
    protected String getUrlForPrintPO(String basePath, String docId, String methodToCall) {
        StringBuffer result = new StringBuffer(basePath);
        result.append("/purapOlePurchaseOrder.do?methodToCall=");
        result.append(methodToCall);
        result.append("&docId=");
        result.append(docId);
        result.append("&command=displayDocSearchView");

        return result.toString();
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        PurchasingDocument purDoc = (PurchasingDocument) purchasingForm.getDocument();
        if ((purchasingForm.getDocTypeName()).equalsIgnoreCase("OLE_POA")) {
            //   setDocstoreDataForCopies((OlePurchaseOrderAmendmentDocument) purDoc);
        }
        // if form is not yet calculated, return and prompt user to calculate
        if (requiresCalculate(purchasingForm)) {
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, PurapKeyConstants.ERROR_PURCHASING_REQUIRES_CALCULATE);

            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        // call prorateDiscountTradeIn
        SpringContext.getBean(PurapService.class).prorateForTradeInAndFullOrderDiscount(purDoc);
        this.calculate(mapping, purchasingForm, request, response);
        PurchaseOrderDocument purchaseDoc = (PurchaseOrderDocument) purchasingForm.getDocument();
        List<OlePurchaseOrderItem> purItem = purchaseDoc.getItems();
        for (int i = OLEConstants.ZERO; purDoc.getItems().size() > i; i++) {
            OlePurchaseOrderItem item = (OlePurchaseOrderItem) purDoc.getItem(i);
            if ((item.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                if (item.getCopyList().size()==OLEConstants.ZERO && item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(OLEConstants.ONE.kualiDecimalValue())
                        && !item.getItemNoOfParts().isGreaterThan(OLEConstants.ONE) ) {
                    OleCopy oleCopy = new OleCopy();
                    oleCopy.setLocation(item.getItemLocation());
                    oleCopy.setBibId(item.getItemTitleId());
                    oleCopy.setCopyNumber(item.getSingleCopyNumber()!=null && !item.getSingleCopyNumber().isEmpty()?item.getSingleCopyNumber():null);
                    oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                        oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                    }
                    List<OleCopy> copyList = new ArrayList<>();
                    copyList.add(oleCopy);
                    item.setCopyList(copyList);
                }
            }
            if (item.getItemIdentifier() != null && item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(OLEConstants.ONE.kualiDecimalValue())
                    && !item.getItemNoOfParts().isGreaterThan(OLEConstants.ONE)) {
                if(item.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                    Map<String, String> map = new HashMap<>();
                    map.put(OLEConstants.PO_ID, item.getItemIdentifier().toString());
                    List<OleCopy> oleCopyList = (List<OleCopy>) SpringContext.getBean(BusinessObjectService.class).findMatching(OleCopy.class, map);
                    if (oleCopyList.size() == 1) {
                        item.getCopyList().get(0).setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                    }
                }
            }
            if (item.getItemIdentifier() != null) {
                Map map = new HashMap();
                map.put(OLEConstants.PO_ID, item.getItemIdentifier().toString());
                List<OLELinkPurapDonor> linkPurapDonors = (List<OLELinkPurapDonor>) getBusinessObjectService().findMatching(OLELinkPurapDonor.class, map);
                if (linkPurapDonors != null && linkPurapDonors.size() > 0) {
                    getBusinessObjectService().delete(linkPurapDonors);
                }
            }
        }
        return super.route(mapping, form, request, response);

    }

//    public void setDocstoreDataForCopies(OlePurchaseOrderAmendmentDocument purDoc) throws Exception {
//        List<OlePurchaseOrderItem> items = new ArrayList<OlePurchaseOrderItem>();
//        items = purDoc.getItems();
//        Iterator iterator = items.iterator();
//        while (iterator.hasNext()) {
//            Object object = iterator.next();
//            if (object instanceof OlePurchaseOrderItem) {
//                OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) object;
//                List<String> itemTitleIdsList = new ArrayList<String>();
//                List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//                // for (OlePurchaseOrderItem itemTitleId : items) {
//                if (null != singleItem.getItemTitleId()) {
//                    if (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
//                            || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
//                        itemTitleIdsList.add(singleItem.getItemTitleId());
//                        workBibDocuments = getWorkBibDocuments(itemTitleIdsList);
//                        for (WorkBibDocument workBibDocument : workBibDocuments) {
//
//
//                        }
//                    }
//                }
//                // }
//                // setEnumerationToCopies(singleItem);
//                /*
//                 * if (null != singleItem.getItemTitleId() && workBibDocuments.size() > 0) { }
//                 */
//            }
//        }
//    }

    public void setEnumerationToCopies(List<OlePurchaseOrderItem> purItem) {
        String partEnumerationCopy = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_COPY);
        String partEnumerationVolume = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_VOLUME);
        for (int singleItem = 0; purItem.size() > singleItem; singleItem++) {
            List<OleCopies> purItemCopies = purItem.get(singleItem).getCopies();
            for (int copies = 0; copies < purItemCopies.size(); copies++) {
                purItemCopies.get(copies).setParts(purItem.get(singleItem).getItemNoOfParts());
                int startingCopyNumber = purItemCopies.get(copies).getStartingCopyNumber().intValue();
                StringBuffer enumeration = new StringBuffer();
                for (int noOfCopies = 0; noOfCopies < purItemCopies.get(copies).getItemCopies().intValue(); noOfCopies++) {
                    for (int noOfParts = 0; noOfParts < purItemCopies.get(copies).getParts().intValue(); noOfParts++) {
                        int newNoOfCopies = startingCopyNumber + noOfCopies;
                        int newNoOfParts = noOfParts + 1;
                        if (noOfCopies + 1 == purItemCopies.get(copies).getItemCopies().intValue()
                                && newNoOfParts == purItemCopies.get(copies).getParts().intValue()) {
                            enumeration = enumeration.append(
                                    partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                                    partEnumerationVolume + newNoOfParts);
                        } else {
                            enumeration = enumeration.append(
                                    partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                                    partEnumerationVolume + newNoOfParts + OLEConstants.COMMA_TO_SEPARATE_ENUMERATION);
                        }
                    }
                }
                purItemCopies.get(copies).setPartEnumeration(enumeration.toString());
            }
        }
    }


    /**
     * For use with a specific set of methods of this class that create new purchase order-derived document types in response to
     * user actions, including <code>closePo</code>, <code>reopenPo</code>, <code>paymentHoldPo</code>, <code>removeHoldPo</code>,
     * <code>splitPo</code>, <code>amendPo</code>, and <code>voidPo</code>. It employs the question framework to ask
     * the user for a response before creating and routing the new document. The response should consist of a note detailing a
     * reason, and either yes or no. This method can be better understood if it is noted that it will be gone through twice (via the
     * question framework); when each question is originally asked, and again when the yes/no response is processed, for
     * confirmation.
     *
     * @param mapping      These are boiler-plate.
     * @param form         "
     * @param request      "
     * @param response     "
     * @param questionType A string identifying the type of question being asked.
     * @param confirmType  A string identifying which type of question is being confirmed.
     * @param documentType A string, the type of document to create
     * @param notePrefix   A string to appear before the note in the BO Notes tab
     * @param messageType  A string to appear on the PO once the question framework is done, describing the action taken
     * @param operation    A string, the verb to insert in the original question describing the action to be taken
     * @return An ActionForward
     * @throws Exception
     */
    @Override
    protected ActionForward askQuestionsAndPerformDocumentAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionType, String confirmType, String documentType, String notePrefix, String messageType, String operation) throws Exception {
        LOG.debug("askQuestionsAndPerformDocumentAction started.");
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        PurchaseOrderDocument po = (PurchaseOrderDocument) kualiDocumentFormBase.getDocument();
        Object question = request.getParameter(OLEConstants.QUESTION_INST_ATTRIBUTE_NAME);
        String reason = request.getParameter(OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME);
        String noteText = "";
        String noteOne = "";
        String noteTwo = "";

        try {
            ConfigurationService kualiConfiguration = SpringContext.getBean(ConfigurationService.class);
            String[] reasons = null;
            // Start in logic for confirming the proposed operation.
            if (ObjectUtils.isNull(question)) {
                String message = "";
                if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_SPLIT_DOCUMENT)) {
                    message = kualiConfiguration.getPropertyValueAsString(PurapKeyConstants.PURCHASE_ORDER_SPLIT_QUESTION_TEXT);
                } else {
                    String key = kualiConfiguration.getPropertyValueAsString(PurapKeyConstants.PURCHASE_ORDER_QUESTION_DOCUMENT);
                    message = StringUtils.replace(key, "{0}", operation);
                }
                // Ask question if not already asked.
                return this.performQuestionWithInput(mapping, form, request, response, questionType, message, OLEConstants.CONFIRMATION_QUESTION, questionType, "");
            } else {
                Object buttonClicked = request.getParameter(OLEConstants.QUESTION_CLICKED_BUTTON);
                if (question.equals(questionType) && buttonClicked.equals(ConfirmationQuestion.NO)) {

                    // If 'No' is the button clicked, just reload the doc
                    return returnToPreviousPage(mapping, kualiDocumentFormBase);
                } else if (question.equals(confirmType) && buttonClicked.equals(SingleConfirmationQuestion.OK)) {

                    // This is the case when the user clicks on "OK" in the end.
                    // After we inform the user that the close has been rerouted, we'll redirect to the portal page.
                    return mapping.findForward(OLEConstants.MAPPING_PORTAL);
                } else {
                    // Have to check length on value entered.
                    String introNoteMessage = notePrefix + OLEConstants.BLANK_SPACE;
                    int noteTextLength = 0;
                    // Build out full message.
                    if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT)) {
                        if (StringUtils.isNotBlank(reason)) {
                            reasons = reason.split("/");
                            noteOne = introNoteMessage + reasons[0];
                            if (!reasons[1].equalsIgnoreCase(null)) {
                                noteTwo = introNoteMessage + reasons[1];
                                noteTextLength = noteTwo.length();
                            }

                        }
                    } else {
                    noteText = introNoteMessage + reason;
                        noteTextLength = noteText.length();
                    }

                    // Get note text max length from DD.
                    int noteTextMaxLength = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getAttributeMaxLength(Note.class, OLEConstants.NOTE_TEXT_PROPERTY_NAME).intValue();

                    String message = "";
                    String key = kualiConfiguration.getPropertyValueAsString(PurapKeyConstants.PURCHASE_ORDER_QUESTION_DOCUMENT);
                    message = StringUtils.replace(key, "{0}", operation);

                    if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT))  {
                        if (reasons == null || (reasons[0].trim().equalsIgnoreCase("null") && (reasons[1].trim().equalsIgnoreCase("null")))) {
                            reason = "";
                            return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response, questionType, message, OLEConstants.CONFIRMATION_QUESTION, questionType, "", reason, OLEConstants.ERROR_CANCELLATION_REASON_REQUIRED, OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME, "");
                        } else if (!reasons[1].equalsIgnoreCase(null) && (noteTextLength > noteTextMaxLength)) {
                            reason = "";
                            return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response, questionType, message, OLEConstants.CONFIRMATION_QUESTION, questionType, "", reason, OLEConstants.ERROR_REASON, OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME, new Integer(noteTextMaxLength - noteTextLength).toString());
                        }
                    }

                    if (StringUtils.isBlank(reason) || (noteTextLength > noteTextMaxLength)) {
                        // Figure out exact number of characters that the user can enter.
                        int reasonLimit = noteTextMaxLength - noteTextLength;

                        if (ObjectUtils.isNull(reason)) {
                            // Prevent a NPE by setting the reason to a blank string.
                            reason = "";
                        }

                        return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response, questionType, message, OLEConstants.CONFIRMATION_QUESTION, questionType, "", reason, PurapKeyConstants.ERROR_PURCHASE_ORDER_REASON_REQUIRED, OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME, new Integer(reasonLimit).toString());
                    }
                }
            }
            // Below used as a place holder to allow code to specify actionForward to return if not a 'success question'
            ActionForward returnActionForward = null;
            if (!po.isPendingActionIndicator()) {
                /*
                 * Below if-else code block calls PurchaseOrderService methods that will throw ValidationException objects if errors
                 * occur during any process in the attempt to perform its actions. Assume, if these return successfully, that the
                 * PurchaseOrderDocument object returned from each is the newly created document and that all actions in the method
                 * were run correctly. NOTE: IF BELOW IF-ELSE IS EDITED THE NEW METHODS CALLED MUST THROW ValidationException OBJECT
                 * IF AN ERROR IS ADDED TO THE GlobalVariables
                 */
                if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_SPLIT_DOCUMENT)) {
                    po.setPendingSplit(true);
                    // Save adding the note for after the items are picked.
                    ((PurchaseOrderForm) kualiDocumentFormBase).setSplitNoteText(noteText);
                    returnActionForward = mapping.findForward(OLEConstants.MAPPING_BASIC);
                } else {
                    String newStatus = null;
                    if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT)) {

                        newStatus = PurchaseOrderStatuses.APPDOC_AMENDMENT;
                        po = SpringContext.getBean(PurchaseOrderService.class).createAndSavePotentialChangeDocument(po, documentType, newStatus);
                        returnActionForward = mapping.findForward(OLEConstants.MAPPING_BASIC);
                    } else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT)) {
                        newStatus = PurchaseOrderStatuses.APPDOC_PENDING_REOPEN;
                        po = SpringContext.getBean(PurchaseOrderService.class).createAndSavePotentialChangeDocument(po, documentType, newStatus);
                    } else {
                        if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.APPDOC_PENDING_CLOSE;
                        }
                        /*else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.PENDING_REOPEN;
                        }*/
                        else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.APPDOC_PENDING_VOID;
                        } else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.APPDOC_PENDING_PAYMENT_HOLD;
                        } else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.APPDOC_PENDING_REMOVE_HOLD;
                        } else if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_RETRANSMIT_DOCUMENT)) {
                            newStatus = PurchaseOrderStatuses.APPDOC_PENDING_RETRANSMIT;
                        }
                        po = SpringContext.getBean(PurchaseOrderService.class).createAndRoutePotentialChangeDocument(po, documentType, kualiDocumentFormBase.getAnnotation(), combineAdHocRecipients(kualiDocumentFormBase), newStatus);
                    }
                    if (!GlobalVariables.getMessageMap().hasNoErrors()) {
                        throw new ValidationException("errors occurred during new PO creation");
                    }

                    String previousDocumentId = kualiDocumentFormBase.getDocId();
                    // Assume at this point document was created properly and 'po' variable is new PurchaseOrderDocument created
                    kualiDocumentFormBase.setDocument(po);
                    kualiDocumentFormBase.setDocId(po.getDocumentNumber());
                    kualiDocumentFormBase.setDocTypeName(po.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
                    if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT)) {
                        Note noteObjOne = new Note();
                        noteObjOne.setNoteText(noteOne);
                        noteObjOne.setNoteTypeCode(OLEConstants.NoteTypeEnum.BUSINESS_OBJECT_NOTE_TYPE.getCode());
                        kualiDocumentFormBase.setNewNote(noteObjOne);

                        kualiDocumentFormBase.setAttachmentFile(new BlankFormFile());

                        insertBONote(mapping, kualiDocumentFormBase, request, response);

                        if (!reasons[1].trim().equalsIgnoreCase("null")) {
                            Note noteObjTwo = new Note();
                            noteObjTwo.setNoteText(noteTwo);
                            noteObjTwo.setNoteTypeCode(OLEConstants.NoteTypeEnum.BUSINESS_OBJECT_NOTE_TYPE.getCode());
                            kualiDocumentFormBase.setNewNote(noteObjTwo);

                            kualiDocumentFormBase.setAttachmentFile(new BlankFormFile());

                            insertBONote(mapping, kualiDocumentFormBase, request, response);
                        }
                    } else {
                    Note newNote = new Note();
                    if (documentType.equals(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT)) {
                        noteText = noteText + " (Previous Document Id is " + previousDocumentId + ")";
                    }
                    newNote.setNoteText(noteText);
                    newNote.setNoteTypeCode(OLEConstants.NoteTypeEnum.BUSINESS_OBJECT_NOTE_TYPE.getCode());
                    kualiDocumentFormBase.setNewNote(newNote);
                    kualiDocumentFormBase.setAttachmentFile(new BlankFormFile());
                    insertBONote(mapping, kualiDocumentFormBase, request, response);
                }
             }
                if (StringUtils.isNotEmpty(messageType)) {
                    KNSGlobalVariables.getMessageList().add(messageType);
                }
            }
            if (ObjectUtils.isNotNull(returnActionForward)) {
                return returnActionForward;
            } else {

                return this.performQuestionWithoutInput(mapping, form, request, response, confirmType, kualiConfiguration.getPropertyValueAsString(messageType), PODocumentsStrings.SINGLE_CONFIRMATION_QUESTION, questionType, "");
            }
        } catch (ValidationException ve) {
            throw ve;
        }
    }

    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("<<<---------Inside OlePurchaseOrderAction Refresh------>>>");
        ActionForward forward = super.refresh(mapping, form, request, response);
        OlePurchaseOrderForm rqForm = (OlePurchaseOrderForm) form;
        PurchaseOrderDocument document = (PurchaseOrderDocument) rqForm.getDocument();
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) rqForm.getNewPurchasingItemLine();

        if (document.getVendorDetail().getCurrencyType()!=null){
            if(document.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, document.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, document.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            document.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
        else {
            document.setVendorAliasName("");
        }

        // To set PurchaseOrderTransmissionMethod depend on vendor transmission format
        if (document.getVendorDetail() != null) {
            boolean activePreferredFound = false;
            if (document.getVendorDetail().getVendorTransmissionFormat().size() > 0) {
                List<VendorTransmissionFormatDetail> vendorTransmissionFormat = document.getVendorDetail().getVendorTransmissionFormat();
                for (VendorTransmissionFormatDetail iter : vendorTransmissionFormat) {
                    if (iter.isVendorPreferredTransmissionFormat() && iter.isActive()) {
                        if (iter.getVendorTransmissionFormat().getVendorTransmissionFormat() != null) {
                            activePreferredFound = true;
                            if (iter.getVendorTransmissionFormat().getVendorTransmissionFormat().equalsIgnoreCase(OleSelectConstant.VENDOR_TRANSMISSION_FORMAT_EDI)) {
                                document.setPurchaseOrderTransmissionMethodCode(OleSelectConstant.METHOD_OF_PO_TRANSMISSION_NOPR);
                            } else {
                                document.setPurchaseOrderTransmissionMethodCode(SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.PURAP_DEFAULT_PO_TRANSMISSION_CODE));
                            }
                        }
                    }
                }
            }
            /*if (!activePreferredFound){
                document.setPurchaseOrderTransmissionMethodCode(SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.PURAP_DEFAULT_PO_TRANSMISSION_CODE));
            }*/
            if ( (!currencyTypeIndicator) && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                Long currencyTypeId = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                Map documentNumberMap = new HashMap();
                documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                Iterator iterator = exchangeRateList.iterator();
                if (iterator.hasNext()) {
                    OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                    item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                }
            }
        }
        return forward;
    }

    @Override
    public ActionForward amendPo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        ActionForward findForward = super.amendPo(mapping, form, request, response);
        OlePurchaseOrderForm rqForm = (OlePurchaseOrderForm) form;
        OlePurchaseOrderAmendmentDocument olePurchaseOrderAmendmentDocument = new OlePurchaseOrderAmendmentDocument();
        if ((rqForm.getDocTypeName()).equalsIgnoreCase("OLE_POA")) {
            rqForm.getAndResetNewPurchasingItemLine();
        }
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        Document document = kualiDocumentFormBase.getDocument();
        // prepare for the reload action - set doc id and command
        kualiDocumentFormBase.setDocId(document.getDocumentNumber());
        kualiDocumentFormBase.setCommand(DOCUMENT_LOAD_COMMANDS[1]);
        // forward off to the doc handler
        docHandler(mapping, form, request, response);

        return findForward;
    }

    /**
     * Is executed when the user clicks on the "print" button on a Purchase Order Print Document page. On a non
     * javascript enabled browser, it will display a page with 2 buttons. One is to display the PDF, the other is to view the PO
     * tabbed page where the PO document statuses, buttons, etc have already been updated (the updates of those occurred while the
     * <code>performPurchaseOrderFirstTransmitViaPrinting</code> method is invoked. On a javascript enabled browser, it will
     * display both the PO tabbed page containing the updated PO document info and the pdf on the next window/tab of the browser.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    @Override
    public ActionForward firstTransmitPrintPo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchaseOrderDocument poa = (PurchaseOrderDocument) ((PurchaseOrderForm) form).getDocument();
        String poDocId = ((PurchaseOrderForm) form).getDocId();
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        try {
            SpringContext.getBean(OlePurchaseOrderService.class).performPurchaseOrderFirstTransmitViaPrinting(poDocId, baosPDF);
        } finally {
            if (baosPDF != null) {
                baosPDF.reset();
            }
        }
        String basePath = getApplicationBaseUrl();
        String docId = ((PurchaseOrderForm) form).getDocId();
        String methodToCallPrintPurchaseOrderPDF = "printPurchaseOrderPDFOnly";
        String methodToCallDocHandler = "docHandler";
        String printPOPDFUrl = getUrlForPrintPO(basePath, docId, methodToCallPrintPurchaseOrderPDF);
        String displayPOTabbedPageUrl = getUrlForPrintPO(basePath, docId, methodToCallDocHandler);
        request.setAttribute("printPOPDFUrl", printPOPDFUrl);
        request.setAttribute("displayPOTabbedPageUrl", displayPOTabbedPageUrl);
        String label = "";
        if (OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT.equalsIgnoreCase(poa.getDocumentHeader().getWorkflowDocument().getDocumentTypeName())) {
            label = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getDocumentLabelByTypeName(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT);
        } else {
            label = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getDocumentLabelByTypeName(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER);
        }
        request.setAttribute("purchaseOrderLabel", label);

        return mapping.findForward("printPurchaseOrderPDF");
    }

    public ActionForward printPo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchaseOrderDocument poa = (PurchaseOrderDocument) ((PurchaseOrderForm) form).getDocument();
        String poDocId = ((PurchaseOrderForm) form).getDocId();
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        try {
            SpringContext.getBean(OlePurchaseOrderService.class).performPurchaseOrderFirstTransmitViaPrinting(poDocId, baosPDF);
            //SpringContext.getBean(OlePurchaseOrderService.class).purchaseOrderFirstTransmitViaPrinting(poDocId, baosPDF);

        } finally {
            if (baosPDF != null) {
                baosPDF.reset();
            }
        }
        String basePath = getApplicationBaseUrl();
        String docId = ((PurchaseOrderForm) form).getDocId();
        String methodToCallPrintPurchaseOrderPDF = "printPurchaseOrderPDFOnly";
        String methodToCallDocHandler = "docHandler";
        String printPOPDFUrl = getUrlForPrintPO(basePath, docId, methodToCallPrintPurchaseOrderPDF);
        String displayPOTabbedPageUrl = getUrlForPrintPO(basePath, docId, methodToCallDocHandler);
        request.setAttribute("printPOPDFUrl", printPOPDFUrl);
        request.setAttribute("displayPOTabbedPageUrl", displayPOTabbedPageUrl);
        String label = "";
        if (OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT.equalsIgnoreCase(poa.getDocumentHeader().getWorkflowDocument().getDocumentTypeName())) {
            label = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getDocumentLabelByTypeName(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT);
        } else {
            label = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getDocumentLabelByTypeName(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER);
        }
        request.setAttribute("purchaseOrderLabel", label);

        return mapping.findForward("printPurchaseOrderPDF");
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
                        if(oldSourceAccountingLine instanceof OlePurchaseOrderAccount) {
                            ((OlePurchaseOrderAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
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
                GlobalVariables.getMessageMap()
                        .putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                                OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER,
                                new String[]{OleSelectConstant.PURCHASE_ORDER});
                result = false;
            }
        }
        return result;
    }

    private void setItemDescription(OlePurchaseOrderItem item, String fileName) throws Exception{
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
                item.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(bib.getId().toString()));
                item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
            }
            if (bib != null) {
                item.setBibUUID(bib.getId());
                item.setItemTitleId(bib.getId());
                item.setLinkToOrderOption(oleEditorResponse.getLinkToOrderOption());
            }
            OleDocstoreResponse.getInstance().getEditorResponse().remove(oleEditorResponse);
        }
    }

    // Added for Jira OLE-1900 Starts

    public ActionForward addCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        LOG.debug("Inside addCopy Method of OleRequisitionAction");
        OlePurchaseOrderForm purchasingForm = (OlePurchaseOrderForm) form;
        OlePurchaseOrderAmendmentDocument purDocument = (OlePurchaseOrderAmendmentDocument) purchasingForm
                .getDocument();
        int line = this.getSelectedLine(request);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem(line);
        OleRequisitionCopies itemCopy = new OleRequisitionCopies();
        OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
        boolean isValid = true;
        List<String> volChar = new ArrayList<>();
        String[] volNumbers = item.getVolumeNumber() != null ? item.getVolumeNumber().split(",") : new String[0];
        for (String volStr : volNumbers) {
            volChar.add(volStr);
        }
        Integer itemCount = volChar.size();
        isValid = oleCopyHelperService.checkCopyEntry(
                item.getItemCopies(), item.getLocationCopies(), itemCount, item.getItemQuantity(), item.getItemNoOfParts(), item.getCopies(), item.getVolumeNumber(), false);
        if (isValid) {
            itemCopy.setItemCopies(item.getItemCopies());
            itemCopy.setLocationCopies(item.getLocationCopies());
            itemCopy.setParts(item.getItemNoOfParts());
            itemCopy.setStartingCopyNumber(item.getStartingCopyNumber());
            itemCopy.setCaption(item.getCaption());
            itemCopy.setVolumeNumber(item.getVolumeNumber());
            List<OleCopy> copyList = oleCopyHelperService.setCopyValues(itemCopy, item.getItemTitleId(), volChar);
            // Whenever there is a single copy, the first copy in the copies table is deleted and replaced with the newly added copy from the copies subsection.
            if (StringUtils.isNotEmpty(item.getItemLocation())) {
                if (!item.getItemLocation().equalsIgnoreCase(item.getLocationCopies()) && !item.isLocationFlag() && item.getCopyList().size()==1) {
                    KRADServiceLocator.getBusinessObjectService().delete(item.getCopyList().get(0));
                    item.getCopyList().clear();
                    item.getCopyList().addAll(copyList);
                    item.setLocationFlag(true);
                }
                else {
                    if (item.getCopyList().size() == 1) {
                        KRADServiceLocator.getBusinessObjectService().delete(item.getCopyList().get(0));
                        item.getCopyList().clear();
                    }
                    item.getCopyList().addAll(copyList);
                }
            }
            else {// For Multiple copies, the copy gets added.
                item.getCopyList().addAll(copyList);
            }
            item.getCopies().add(itemCopy);
            item.setParts(null);
            item.setItemCopies(null);
            item.setPartEnumeration(null);
            item.setLocationCopies(null);
            item.setCaption(null);
            item.setVolumeNumber(null);
           /* if (item.getCopies().size() > 0) {
                int startingCopies = 1;
                for (int copy = 0; copy < item.getCopies().size(); copy++) {
                    startingCopies = startingCopies + item.getCopies().get(copy).getItemCopies().intValue();
                }
                item.setStartingCopyNumber(new KualiInteger(startingCopies));
            }*/
        }

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public boolean checkForCopiesAndLocation(OlePurchaseOrderItem item) {
        boolean isValid = true;
        if (null == item.getItemCopies() || null == item.getLocationCopies()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_ITEMCOPIES_OR_LOCATIONCOPIES_SHOULDNOT_BE_NULL, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForItemCopiesGreaterThanQuantity(OlePurchaseOrderItem item) {
        boolean isValid = true;
        if (item.getItemCopies().isGreaterThan(item.getItemQuantity())) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForTotalCopiesGreaterThanQuantity(OlePurchaseOrderItem item) {
        boolean isValid = true;
        int copies = 0;
        if (item.getCopies().size() > 0) {
            for (int itemCopies = 0; itemCopies < item.getCopies().size(); itemCopies++) {
                copies = copies + item.getCopies().get(itemCopies).getItemCopies().intValue();
            }
            if (item.getItemQuantity().isLessThan(item.getItemCopies().add(new KualiDecimal(copies)))) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * This method takes purchaseorderItem as parameter, it will calculate and set partEnumerations and startingCopyNumber for each
     * lineItem
     *
     * @param item
     * @return OleRequisitionCopies
     */
    public OleRequisitionCopies setCopyValues(OlePurchaseOrderItem item) {
        OleRequisitionCopies itemCopy = new OleRequisitionCopies();
        itemCopy.setParts(item.getItemNoOfParts());
        itemCopy.setItemCopies(item.getItemCopies());
        StringBuffer enumeration = new StringBuffer();
        if (item.getStartingCopyNumber() != null && item.getStartingCopyNumber().isNonZero()) {
            itemCopy.setStartingCopyNumber(item.getStartingCopyNumber());
        } else {
            int startingCopies = 1;
            for (int copy = 0; copy < item.getCopies().size(); copy++) {
                startingCopies = startingCopies + item.getCopies().get(copy).getItemCopies().intValue();
            }
            itemCopy.setStartingCopyNumber(new KualiInteger(startingCopies));
        }
        String partEnumerationCopy = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_COPY);
        String partEnumerationVolume = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_VOLUME);
        int startingCopyNumber = itemCopy.getStartingCopyNumber().intValue();
        for (int noOfCopies = 0; noOfCopies < item.getItemCopies().intValue(); noOfCopies++) {
            for (int noOfParts = 0; noOfParts < item.getItemNoOfParts().intValue(); noOfParts++) {
                int newNoOfCopies = startingCopyNumber + noOfCopies;
                int newNoOfParts = noOfParts + 1;
                if (noOfCopies + 1 == item.getItemCopies().intValue()
                        && newNoOfParts == item.getItemNoOfParts().intValue()) {
                    enumeration = enumeration.append(
                            partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                            partEnumerationVolume + newNoOfParts);
                } else {
                    enumeration = enumeration.append(
                            partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                            partEnumerationVolume + newNoOfParts + OLEConstants.COMMA_TO_SEPARATE_ENUMERATION);
                }
            }
        }
        itemCopy.setPartEnumeration(enumeration.toString());
        itemCopy.setLocationCopies(item.getLocationCopies());
        return itemCopy;
    }

    /**
     * Remove a Copy for the selected Item .
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteCopy Method of OleRequisitionAction");
        OlePurchaseOrderForm purchasingForm = (OlePurchaseOrderForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int copyIndex = Integer.parseInt(indexes[1]);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem((itemIndex));
        List<OleCopy> copyList = new ArrayList<>();
        for(int i=0;i<item.getCopyList().size();i++){
            OleCopy oleCopy = item.getCopyList().get(i);
            if(item.getCopies().get(copyIndex).getLocationCopies().equalsIgnoreCase(oleCopy.getLocation())){
                copyList.add(oleCopy);
            }
        }
        for(OleCopy copy : copyList){
            item.getCopyList().remove(copy);
            item.getDeletedCopiesList().add(copy);
        }
        item.getCopies().remove(copyIndex);
        LOG.debug("Selected Copy is Remove");
        LOG.debug("Leaving deleteCopy Method of OleRequisitionAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Add a Payment History for selected Item.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward addPaymentHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        OlePurchaseOrderForm purchasingForm = (OlePurchaseOrderForm) form;
        int line = this.getSelectedLine(request);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem(line);
        OleRequisitionPaymentHistory paymentHistory = new OleRequisitionPaymentHistory();
        paymentHistory.setPaymentHistory("");
        item.getRequisitionPaymentHistory().add(paymentHistory);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Remove a Payment History for selected Item
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deletePaymentHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {

        OlePurchaseOrderForm purchasingForm = (OlePurchaseOrderForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int copyIndex = Integer.parseInt(indexes[1]);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem((itemIndex));
        item.getRequisitionPaymentHistory().remove(copyIndex);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    /**
     * This method takes List of UUids as parameter and creates a LinkedHashMap with instance as key and id as value. and calls
     * Docstore's QueryServiceImpl class getWorkBibRecords method and return workBibDocument for passed instance Id.
     *
     * @param instanceIdsList
     * @return List<WorkBibDocument>
     */
//    private List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList) {
//        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
//        for (String instanceId : instanceIdsList) {
//            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
//            instanceIdMap.put(DocType.INSTANCE.getDescription(), instanceId);
//            instanceIdMapList.add(instanceIdMap);
//        }
//
//        QueryService queryService = QueryServiceImpl.getInstance();
//        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//        try {
//            workBibDocuments = queryService.getWorkBibRecords(instanceIdMapList);
//        } catch (Exception ex) {
//            // TODO Auto-generated catch block
//            ex.printStackTrace();
//        }
//        return workBibDocuments;
//    }

    // Added for Jira OLE-1900 Ends

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        PurchasingDocument document = (PurchasingDocument) ((PurchasingFormBase) form).getDocument();
        this.calculate(mapping, purchasingForm, request, response);
        Iterator itemIterator = document.getItems().iterator();
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OlePurchaseOrderItem tempItem = (OlePurchaseOrderItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                if (tempItem.getCopyList().size()==OLEConstants.ZERO && tempItem.getItemQuantity() != null && tempItem.getItemNoOfParts() != null && !tempItem.getItemQuantity().isGreaterThan(OLEConstants.ONE.kualiDecimalValue())
                        && !tempItem.getItemNoOfParts().isGreaterThan(OLEConstants.ONE)) {
                    OleCopy oleCopy = new OleCopy();
                    oleCopy.setLocation(tempItem.getItemLocation());
                    oleCopy.setBibId(tempItem.getItemTitleId());
                    if (StringUtils.isNotBlank(tempItem.getLinkToOrderOption()) && (tempItem.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || tempItem.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                        oleCopy.setCopyNumber(tempItem.getSingleCopyNumber() != null && !tempItem.getSingleCopyNumber().isEmpty() ? tempItem.getSingleCopyNumber() : null);
                    }
                    oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    List<OleCopy> copyList = new ArrayList<>();
                    copyList.add(oleCopy);
                    tempItem.setCopyList(copyList);
                }
                if(tempItem.getItemIdentifier()!=null && tempItem.getItemQuantity() != null && tempItem.getItemNoOfParts() != null && !tempItem.getItemQuantity().isGreaterThan(OLEConstants.ONE.kualiDecimalValue())
                        && !tempItem.getItemNoOfParts().isGreaterThan(OLEConstants.ONE)){
                    if(tempItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                        Map<String, String> map = new HashMap<>();
                        map.put(OLEConstants.PO_ID, tempItem.getItemIdentifier().toString());
                        List<OleCopy> oleCopyList = (List<OleCopy>) SpringContext.getBean(BusinessObjectService.class).findMatching(OleCopy.class, map);
                        if (oleCopyList.size() == 1) {
                            tempItem.getCopyList().get(0).setCopyNumber(tempItem.getSingleCopyNumber() != null && !tempItem.getSingleCopyNumber().isEmpty() ? tempItem.getSingleCopyNumber() : null);
                        }
                    }
                }
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
            if (tempItem.getItemIdentifier() != null) {
                Map map = new HashMap();
                map.put(OLEConstants.PO_ID, tempItem.getItemIdentifier().toString());
                List<OLELinkPurapDonor> linkPurapDonors = (List<OLELinkPurapDonor>) getBusinessObjectService().findMatching(OLELinkPurapDonor.class, map);
                if (linkPurapDonors != null && linkPurapDonors.size() > 0) {
                    getBusinessObjectService().delete(linkPurapDonors);
                }
            }
        }
        return super.blanketApprove(mapping, form, request, response);
    }

    public ActionForward selectVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OlePurchaseOrderForm purchasingForm = (OlePurchaseOrderForm) form;
        OlePurchaseOrderAmendmentDocument document = (OlePurchaseOrderAmendmentDocument) purchasingForm.getDocument();
        if ((purchasingForm.getDocTypeName()).equalsIgnoreCase("OLE_POA")) {
            if (document.getVendorAliasName() != null && document.getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
                Map vendorAliasMap = new HashMap();
                vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, document.getVendorAliasName());
                org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
                if (vendorAliasList != null && vendorAliasList.size() > 0) {  /* if there matching vendor found for the given vendor alias name */
                    Map vendorDetailMap = new HashMap();
                    vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                    vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                    VendorDetail vendorDetail = businessObject.findByPrimaryKey(VendorDetail.class, vendorDetailMap);
                    document.setVendorDetail(vendorDetail);
                    document.setVendorHeaderGeneratedIdentifier(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                    document.setVendorDetailAssignedIdentifier(vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                    refreshVendor(mapping, form, request, response);
                } else {     /* If there is no matching vendor found*/
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
                }
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward refreshVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingAccountsPayableFormBase baseForm = (PurchasingAccountsPayableFormBase) form;
        PurchasingDocument document = (PurchasingDocument) baseForm.getDocument();
        if (StringUtils.equals(OLEConstants.REFRESH_VENDOR_CALLER, VendorConstants.VENDOR_LOOKUPABLE_IMPL) && document.getVendorDetailAssignedIdentifier() != null && document.getVendorHeaderGeneratedIdentifier() != null) {
            document.setVendorContractGeneratedIdentifier(null);
            document.refreshReferenceObject(OLEConstants.VENDOR_CONTRACT);

            // retrieve vendor based on selection from vendor lookup
            document.refreshReferenceObject(OLEConstants.VENDOR_DETAILS);
            document.templateVendorDetail(document.getVendorDetail());

            // populate default address based on selected vendor
            VendorAddress defaultAddress = null;
            if(document.getVendorDetail()!=null && document.getVendorDetail().getVendorAddresses()!=null && document.getVendorDetail().getVendorHeader()!=null && document.getVendorDetail().getVendorHeader().getVendorType()!=null && document.getVendorDetail().getVendorHeader().getVendorType().getAddressType()!=null && document.getVendorDetail().getVendorHeader().getVendorType().getAddressType().getVendorAddressTypeCode()!=null){
                defaultAddress = SpringContext.getBean(VendorService.class).getVendorDefaultAddress(document.getVendorDetail().getVendorAddresses(), document.getVendorDetail().getVendorHeader().getVendorType().getAddressType().getVendorAddressTypeCode(), document.getDeliveryCampusCode());
            }
            document.templateVendorAddress(defaultAddress);
        }
        return super.refresh(mapping, form, request, response);
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object question = request.getParameter(OLEConstants.QUESTION_INST_ATTRIBUTE_NAME);
        // this should probably be moved into a protected instance variable
        ConfigurationService kualiConfiguration = SpringContext.getBean(ConfigurationService.class);

        // logic for cancel question
        if (question == null) {

            // ask question if not already asked
            return this.performQuestionWithoutInput(mapping, form, request, response, OLEConstants.DOCUMENT_CANCEL_QUESTION, kualiConfiguration.getPropertyValueAsString("document.question.cancel.text"), OLEConstants.CONFIRMATION_QUESTION, OLEConstants.MAPPING_CANCEL, "");
        } else {
            Object buttonClicked = request.getParameter(OLEConstants.QUESTION_CLICKED_BUTTON);
            if ((OLEConstants.DOCUMENT_CANCEL_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {

                // if no button clicked just reload the doc
                return mapping.findForward(OLEConstants.MAPPING_BASIC);
            }
            // else go to cancel logic below
        }
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        OlePurchaseOrderForm purchaseOrderForm = (OlePurchaseOrderForm) form;
        if ((purchaseOrderForm.getDocTypeName()).equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT)) {
            List<Note> noteList = new ArrayList<Note>();

            if (kualiDocumentFormBase.getDocument().getNotes().size() > 0) {
                for (Note note : (List<Note>) kualiDocumentFormBase.getDocument().getNotes()) {
                    noteList.add(note);
                    getBusinessObjectService().delete(note);
                }
            }
            SpringContext.getBean(DocumentService.class).cancelDocument(kualiDocumentFormBase.getDocument(), kualiDocumentFormBase.getAnnotation());
            if (noteList.size() > 0) {
                getBusinessObjectService().save(noteList);
            }

            return returnToSender(request, mapping, kualiDocumentFormBase);
        }
        return returnToSender(request, mapping, kualiDocumentFormBase);
    }

    public ActionForward addDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = true;
        PurchaseOrderForm purchasingForm = (PurchaseOrderForm) form;
        int line = this.getSelectedLine(request);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem(line);
        Map map = new HashMap();
        if (item.getDonorCode() != null) {
            map.put(OLEConstants.DONOR_CODE, item.getDonorCode());
            List<OLEDonor> oleDonorList = (List<OLEDonor>) getLookupService().findCollectionBySearch(OLEDonor.class, map);
            if (oleDonorList != null && oleDonorList.size() > 0) {
                OLEDonor oleDonor = oleDonorList.get(0);
                if (oleDonor != null) {
                    for (OLELinkPurapDonor oleLinkPurapDonor : item.getOleDonors()) {
                        if (oleLinkPurapDonor.getDonorCode().equalsIgnoreCase(item.getDonorCode())) {
                            flag = false;
                            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                                    OLEConstants.DONOR_CODE_EXISTS, new String[]{});
                            return mapping.findForward(OLEConstants.MAPPING_BASIC);
                        }
                    }
                    if (flag) {
                        OLELinkPurapDonor donor = new OLELinkPurapDonor();
                        donor.setDonorId(oleDonor.getDonorId());
                        donor.setDonorCode(oleDonor.getDonorCode());
                        item.getOleDonors().add(donor);
                        item.setDonorCode(null);
                    }
                }
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.ERROR_DONOR_CODE, new String[]{});
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward deleteDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        PurchaseOrderForm purchasingForm = (PurchaseOrderForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int donorIndex = Integer.parseInt(indexes[1]);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem((itemIndex));
        item.getOleDonors().remove(donorIndex);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    protected ActionForward performQuestionWithInput(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context) throws Exception {
        return performQuestion(mapping, form, request, response, questionId, questionText, questionType, caller, context, true, "", "", "", "");
    }


    private ActionForward performQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context, boolean showReasonField, String reason, String errorKey, String errorPropertyName, String errorParameter) throws Exception {
        Properties parameters = new Properties();

        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, "start");
        parameters.put(KRADConstants.DOC_FORM_KEY, GlobalVariables.getUserSession().addObjectWithGeneratedKey(form));
        parameters.put(KRADConstants.CALLING_METHOD, caller);
        parameters.put(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME, questionId);
        parameters.put(KRADConstants.QUESTION_IMPL_ATTRIBUTE_NAME, questionType);
        //parameters.put(KRADConstants.QUESTION_TEXT_ATTRIBUTE_NAME, questionText);
        parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation(request, mapping));
        parameters.put(KRADConstants.QUESTION_CONTEXT, context);
        parameters.put(KRADConstants.QUESTION_SHOW_REASON_FIELD, Boolean.toString(showReasonField));
        parameters.put(KRADConstants.QUESTION_REASON_ATTRIBUTE_NAME, reason);
        parameters.put(KRADConstants.QUESTION_ERROR_KEY, errorKey);
        parameters.put(KRADConstants.QUESTION_ERROR_PROPERTY_NAME, errorPropertyName);
        parameters.put(KRADConstants.QUESTION_ERROR_PARAMETER, errorParameter);
        parameters.put(KRADConstants.QUESTION_ANCHOR, form instanceof KualiForm ? org.apache.commons.lang.ObjectUtils.toString(((KualiForm) form).getAnchor()) : "");
        Object methodToCallAttribute = request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (methodToCallAttribute != null) {
            parameters.put(KRADConstants.METHOD_TO_CALL_PATH, methodToCallAttribute);
            ((PojoForm) form).registerEditableProperty(String.valueOf(methodToCallAttribute));
        }

        if (form instanceof KualiDocumentFormBase) {
            String docNum = ((KualiDocumentFormBase) form).getDocument().getDocumentNumber();
            if(docNum != null){
                parameters.put(KRADConstants.DOC_NUM, ((KualiDocumentFormBase) form)
                        .getDocument().getDocumentNumber());
            }
        }

        // KULRICE-8077: PO Quote Limitation of Only 9 Vendors
        String questionTextAttributeName = KRADConstants.QUESTION_TEXT_ATTRIBUTE_NAME + questionId;
        GlobalVariables.getUserSession().addObject(questionTextAttributeName, (Object)questionText);
        String questionUrl = null;
        if (questionId.equalsIgnoreCase(PODocumentsStrings.VOID_QUESTION)) {
            questionUrl = UrlFactory.parameterizeUrl(getApplicationBaseUrl() + OLEConstants.QUESTION_ACTION , parameters);
        } else {
            questionUrl = UrlFactory.parameterizeUrl(getApplicationBaseUrl() + "/kr/" + KRADConstants.QUESTION_ACTION, parameters);
        }

        return new ActionForward(questionUrl, true);
    }

    @Override
    protected ActionForward performQuestionWithInputAgainBecauseOfErrors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context, String reason, String errorKey, String errorPropertyName, String errorParameter) throws Exception {
        return performQuestion(mapping, form, request, response, questionId, questionText, questionType, caller, context, true, reason, errorKey, errorPropertyName, errorParameter);
    }

    public ActionForward populateAccountingLines(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {

        OlePurchaseOrderForm olePurchaseOrderForm = (OlePurchaseOrderForm) form;
        int line = this.getSelectedLine(request);
        OlePurchaseOrderItem item = (OlePurchaseOrderItem) ((PurchasingAccountsPayableDocument) olePurchaseOrderForm.getDocument()).getItem(line);
        Map fundMap = new HashMap();
        if (item.getFundCode() != null && !item.getFundCode().trim().isEmpty()) {
            fundMap.put(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE, item.getFundCode());
            List<OleFundCode> oleFundCodes = (List<OleFundCode>) getLookupService().findCollectionBySearch(OleFundCode.class, fundMap);
            if (oleFundCodes != null && oleFundCodes.size() > 0) {
                OleFundCode oleFundCode = oleFundCodes.get(0);
                if (oleFundCode != null) {
                    if (oleFundCode.getOleFundCodeAccountingLineList() != null) {
                        OlePurchaseOrderAccount account = new OlePurchaseOrderAccount();
                        List<PurApAccountingLine> accountingLines = new ArrayList<PurApAccountingLine>();
                        for (OleFundCodeAccountingLine accountingLine : oleFundCode.getOleFundCodeAccountingLineList()) {
                            if (accountingLine.getPercentage() != null) {
                                account.setAccountLinePercent(accountingLine.getPercentage());
                            }
                            account.setChartOfAccountsCode(accountingLine.getChartCode());
                            account.setAccountNumber(accountingLine.getAccountNumber());
                            account.setSubAccountNumber(accountingLine.getSubAccount());
                            account.setFinancialObjectCode(accountingLine.getObjectCode());
                            account.setFinancialSubObjectCode(accountingLine.getSubObject());
                            account.setProjectCode(accountingLine.getProject());
                            account.setOrganizationReferenceId(accountingLine.getOrgRefId());
                            accountingLines.add((PurApAccountingLine) account);
                        }
                        item.getSourceAccountingLines().addAll(accountingLines);
                    }
                }
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.ERROR_FUND_CODE, new String[]{});
            }
        }else{
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.EMPTY_FUND_CODE, new String[]{});
        }
        item.setFundCode(null);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

}


