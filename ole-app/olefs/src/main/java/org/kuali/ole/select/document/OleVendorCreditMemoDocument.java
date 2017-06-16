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

package org.kuali.ole.select.document;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.CreditMemoItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.document.service.PaymentRequestService;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.select.businessobject.OleInvoiceType;
import org.kuali.ole.select.document.service.OleCreditMemoService;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Credit Memo Document Business Object. Contains the fields associated with the main document table.
 */
public class OleVendorCreditMemoDocument extends VendorCreditMemoDocument {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleVendorCreditMemoDocument.class);


    private static transient BusinessObjectService businessObjectService;
    private static transient ConfigurationService kualiConfigurationService;
    private static transient FileProcessingService fileProcessingService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient BibInfoService bibInfoService;

    private boolean prorateQty;
    private boolean prorateDollar;
    private boolean prorateManual;
    private boolean noProrate;

    private String prorateBy;
    private boolean currencyTypeIndicator = true;

    private String vendorAliasName;
    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    private Integer invoiceIdentifier;

    public OleVendorCreditMemoDocument() {
        super();
    }

    @Override
    public Class getItemClass() {
        return OleCreditMemoItem.class;
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OleVendorCreditMemoDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = getConfigurationService().getPropertyValueAsString(
                OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    public String getBibeditorEditURL() {
        String bibeditorEditURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
    }

    public String getBibeditorViewURL() {
        String bibeditorViewURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
    }
    public String getDublinEditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorEditURL();

    }
    public String getDublinEditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorViewURL();
    }
    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OleVendorCreditMemoDocument.fileProcessingService = fileProcessingService;
    }

    public static BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
        }
        return bibInfoService;
    }

    public String getMarcXMLFileDirLocation() throws Exception {
        String externaleDirectory = getFileProcessingService().getMarcXMLFileDirLocation();
        return externaleDirectory;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OleVendorCreditMemoDocument.bibInfoWrapperService = bibInfoWrapperService;
    }

    public boolean isProrateQty() {
        return prorateQty;
    }

    public void setProrateQty(boolean prorateQty) {
        this.prorateQty = prorateQty;
    }

    public boolean isProrateDollar() {
        return prorateDollar;
    }

    public void setProrateDollar(boolean prorateDollar) {
        this.prorateDollar = prorateDollar;
    }

    public boolean isProrateManual() {
        return prorateManual;
    }

    public void setProrateManual(boolean prorateManual) {
        this.prorateManual = prorateManual;
    }

    public boolean isNoProrate() {
        return noProrate;
    }

    public void setNoProrate(boolean noProrate) {
        this.noProrate = noProrate;
    }

    public String getProrateBy() {
        return prorateBy;
    }

    public void setProrateBy(String prorateBy) {
        this.prorateBy = prorateBy;
    }

    /**
     * This method is used to check the status of the document for displaying view and edit buttons in line item
     *
     * @return boolean
     */
    public boolean getIsSaved() {
        if (this.getDocumentHeader().getWorkflowDocument().isSaved()
                || this.getDocumentHeader().getWorkflowDocument().isInitiated()) {
            return true;
        }
        return false;
    }

    @Override
    public void prepareForSave(KualiDocumentEvent event) {

        if (event instanceof AttributedContinuePurapEvent) {
            SpringContext.getBean(OleCreditMemoService.class).populateDocumentAfterInit(this);
        }
        customPrepareForSave(event);
    }

    @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();

        try {
            if (this.getVendorAliasName() == null) {
                populateVendorAliasName();
            }
            Map purchaseOrderTypeIdMap = new HashMap();
            if (this.getPurchaseOrderTypeId() != null) {
                purchaseOrderTypeIdMap.put("purchaseOrderTypeId", this.getPurchaseOrderTypeId());
                List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
                if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                    PurchaseOrderType purchaseOrderTypeDoc = purchaseOrderTypeDocumentList.get(0);
                    this.setOrderType(purchaseOrderTypeDoc);
                }
            }
            List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
            BigDecimal newUnitPrice = new BigDecimal(0);
            BigDecimal hundred = new BigDecimal(100);
            List<OleCreditMemoItem> item = this.getItems();

            for (int i = 0; item.size() > i; i++) {
                OleCreditMemoItem items = (OleCreditMemoItem) this.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    newUnitPrice = items.getItemUnitPrice().abs();

                    if (items.getItemUnitPrice() != null && items.getExtendedPrice() != null) {
                        items.setItemSurcharge(items.getExtendedPrice().bigDecimalValue()
                                .subtract(newUnitPrice.multiply(items.getItemQuantity().bigDecimalValue()))
                                .setScale(4, RoundingMode.HALF_UP));
                    } else {
                        items.setItemSurcharge(BigDecimal.ZERO);
                    }
                }
            }
            if (this.getVendorDetail().getCurrencyType()!=null){
                if(this.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }

            if (this.getVendorDetail() != null && (!currencyTypeIndicator)) {
                Long currencyTypeId = this.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                Map documentNumberMap = new HashMap();
                documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                List<OleExchangeRate> exchangeRateList = (List) getBusinessObjectService().findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                Iterator iterator = exchangeRateList.iterator();
                for (OleCreditMemoItem items : item) {
                    iterator = exchangeRateList.iterator();
                    if (iterator.hasNext()) {
                        OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                        items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                    }
                }

            }

            List<OleCreditMemoItem> items = this.getItems();


            String itemDescription = "";

            for (OleCreditMemoItem singleItem : items) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Title id while retriving ------>" + singleItem.getItemTitleId());
                }


                if (singleItem.getItemTitleId() != null) {
                  /*  LOG.debug("###########inside processAfterRetrieve ole credit memo item###########");
                    HashMap<String, String> queryMap = new HashMap<String, String>();
                    queryMap.put(OleSelectConstant.DocStoreDetails.ITEMLINKS_KEY, singleItem.getItemTitleId());
                    List<DocInfoBean> docStoreResult = getBibInfoWrapperService().searchBibInfo(queryMap);
                    Iterator bibIdIterator = docStoreResult.iterator();
                    if (bibIdIterator.hasNext()) {
                        DocInfoBean docInfoBean = (DocInfoBean) bibIdIterator.next();

                        if (docInfoBean.getBibIdentifier() == null) {
                            singleItem.setBibUUID(docInfoBean.getUniqueId());
                        } else {
                            singleItem.setBibUUID(docInfoBean.getBibIdentifier());
                        }
                    }*/

                    Bib bib =  new BibMarc();
                    DocstoreClientLocator docstoreClientLocator=new DocstoreClientLocator();
                    if(singleItem.getItemTitleId()!=null && singleItem.getItemTitleId()!=""){
                        bib= docstoreClientLocator.getDocstoreClient().retrieveBib(singleItem.getItemTitleId());
                        singleItem.setBibUUID(bib.getId());
                        singleItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(singleItem.getItemTitleId()));
                    }

                    itemDescription = ((bib.getTitle() != null && !bib.getTitle().isEmpty()) ? bib.getTitle() + "," : "") +
                            ((bib.getAuthor() != null && !bib.getAuthor().isEmpty()) ? bib.getAuthor() + "," : "") +
                            ((bib.getPublisher() != null && !bib.getPublisher().isEmpty()) ? bib.getPublisher() + "," : "") +
                            ((bib.getIsbn() != null && !bib.getIsbn().isEmpty()) ? bib.getIsbn() + "," : "");
                    if (!itemDescription.isEmpty()) {
                        itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                                itemDescription.substring(0, itemDescription.lastIndexOf(","));
                    }
                    StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
                    itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
                    singleItem.setItemDescription(itemDescription);
                }
            }
            if (this.getProrateBy() != null) {
                this.setProrateQty(this.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY));
                this.setProrateManual(this.getProrateBy().equals(OLEConstants.MANUAL_PRORATE));
                this.setProrateDollar(this.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR));
                this.setNoProrate(this.getProrateBy().equals(OLEConstants.NO_PRORATE));
            }

        } catch (Exception e) {
            LOG.error("Exception in OleVendorCreditMemoDocument:processAfterRetrieve " + e);
            throw new RuntimeException(e);
        }
    }

    public void creditMemoCalculation(OleCreditMemoItem item) {
        item.setItemUnitPrice(item.getPoUnitPrice());
        BigDecimal calcExtendedPrice = item.getItemUnitPrice().multiply(item.getItemQuantity().bigDecimalValue());
        KualiDecimal extendedPrice = new KualiDecimal(calcExtendedPrice.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR));
        item.setPoTotalAmount(extendedPrice);
    }

    private void setItemDescription(OleCreditMemoItem singleItem) throws Exception {
        if (singleItem.getOleOrderRecord() != null) {
            Map<String, ?> bibAssociatedFieldValuesMap = singleItem.getOleOrderRecord().getOleBibRecord().getBibAssociatedFieldsValueMap();
            List titleList = (List) bibAssociatedFieldValuesMap.get("Title_search");
            String title = titleList != null && !titleList.isEmpty() ? (String) (titleList).get(0) : null;
            List authorList = (List) bibAssociatedFieldValuesMap.get("Author_search");
            String author = authorList != null && !authorList.isEmpty() ? (String) (authorList).get(0) : null;
            List publisherList = (List) bibAssociatedFieldValuesMap.get("Publisher_search");
            String publisher = publisherList != null && !publisherList.isEmpty() ? (String) (publisherList).get(0) : null;
            List isbnList = (List) bibAssociatedFieldValuesMap.get("020a");
            String isbn = isbnList != null && !isbnList.isEmpty() ? (String) (isbnList).get(0) : null;

            singleItem.setBibUUID(singleItem.getOleOrderRecord().getOleBibRecord().getBibUUID());
            singleItem.setItemDescription(title + "," + author + "," + publisher + "," + isbn);
        }
    }

    @Override
    public OlePaymentRequestDocument getPaymentRequestDocument() {
        if ((ObjectUtils.isNull(paymentRequestDocument)) && (ObjectUtils.isNotNull(getPaymentRequestIdentifier()))) {
            setPaymentRequestDocument(SpringContext.getBean(PaymentRequestService.class).getPaymentRequestById(getPaymentRequestIdentifier()));
        }
        return (OlePaymentRequestDocument) this.paymentRequestDocument;
    }

    /**
     * Provides answers to the following splits:
     * RequiresInvoiceAttachment
     * HasPrepaidInvoiceType
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(java.lang.String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.OlePaymentRequest.HAS_INVOICE_TYPE)) {
            return hasInvoiceType();
        }
        else if (nodeName.equals(OLEConstants.OlePaymentRequest.HAS_PREPAID_INVOICE_TYPE)) {
            return hasPrepaidInvoiceType();
        }
        else if (nodeName.equals(PurapWorkflowConstants.REQUIRES_IMAGE_ATTACHMENT)) {
            return requiresAccountsPayableReviewRouting();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }


    private boolean hasInvoiceType() {
        if (this.getInvoiceTypeId() != null) {
            return true;
        }
        return false;
    }

    private boolean hasPrepaidInvoiceType() {
        if (this.getInvoiceTypeId() != null) {
            Map<String, String> invoiceMap = new HashMap<String, String>();
            invoiceMap.put("invoiceTypeId", this.getInvoiceTypeId().toString());
            OleInvoiceType invoiceType = this.getBusinessObjectService().findByPrimaryKey(OleInvoiceType.class, invoiceMap);
            if (invoiceType != null &&
                    invoiceType.getInvoiceType().equals("Prepay") ||
                    invoiceType.getInvoiceType().equals("Deposit")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public KualiDecimal getGrandTotal() {
        KualiDecimal grandTotal = KualiDecimal.ZERO;

        if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR)
                || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE) || this.prorateBy.equals(OLEConstants.NO_PRORATE))) {

            for (CreditMemoItem item : (List<CreditMemoItem>) getItems()) {
                item.refreshReferenceObject(PurapPropertyConstants.ITEM_TYPE);

                if (item.getTotalAmount() != null) {

                    if (!StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_RESTCK_FEE_CODE, item.getItemTypeCode())) {
                        if (item.getExtendedPrice() != null) {
                            grandTotal = grandTotal.add(item.getExtendedPrice());
                        }
                    }
                }
            }
        } else {
            /*for (int i=0; i<this.getItems().size();i++) {*/
            for (CreditMemoItem item : (List<CreditMemoItem>) getItems()) {
                if (!StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_RESTCK_FEE_CODE, item.getItemTypeCode()) && !StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CRDT_CODE, item.getItemTypeCode())) {
                    if (item.getExtendedPrice() != null) {
                        grandTotal = grandTotal.add(item.getExtendedPrice());
                    }
                }
            }
        }
        /*}*/
        return grandTotal;
    }

    @Override
    public KualiDecimal getTotalDollarAmountAllItems(String[] excludedTypes) {
        if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR)
                || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE) || this.prorateBy.equals(OLEConstants.NO_PRORATE))) {
            return getTotalDollarAmountWithExclusions(excludedTypes, false);
        } else {
            return getTotalDollarAmountWithExclusions(excludedTypes, true);
        }
    }

    @Override
    public KualiDecimal getTotalPreTaxDollarAmountAllItems(String[] excludedTypes) {
        if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR)
                || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE) || this.prorateBy.equals(OLEConstants.NO_PRORATE))) {
            return getTotalPreTaxDollarAmountWithExclusions(excludedTypes, false);
        } else {
            return getTotalPreTaxDollarAmountWithExclusions(excludedTypes, true);
        }
    }

    @Override
    public KualiDecimal getTotalPreTaxDollarAmountAboveLineItems() {
        if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) ||
                this.prorateBy.equals(OLEConstants.MANUAL_PRORATE) || this.prorateBy.equals(OLEConstants.NO_PRORATE))) {
            KualiDecimal addChargeItem = KualiDecimal.ZERO;
            KualiDecimal lineItemPreTaxTotal = KualiDecimal.ZERO;
            KualiDecimal prorateSurcharge = KualiDecimal.ZERO;
            List<OleCreditMemoItem> item = this.getItems();
            for (OleCreditMemoItem items : item) {
                if (items.getItemType().isQuantityBasedGeneralLedgerIndicator() && items.getExtendedPrice() != null && items.getExtendedPrice().compareTo(KualiDecimal.ZERO) != 0) {
                    if (items.getItemSurcharge() != null) {
                        prorateSurcharge = new KualiDecimal(items.getItemSurcharge());
                    }
                    addChargeItem = addChargeItem.add(items.getExtendedPrice().subtract(prorateSurcharge.multiply(items.getItemQuantity())));
                }
            }
            lineItemPreTaxTotal = addChargeItem;
            return lineItemPreTaxTotal;
        } else {
            return super.getTotalPreTaxDollarAmountAboveLineItems();
        }
    }

    public Integer getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    public void setInvoiceIdentifier(Integer invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
    }

    private void populateVendorAliasName() {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, this.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            this.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
    }
}
