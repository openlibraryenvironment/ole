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
package org.kuali.ole.select.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMappingOptionsBo;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderDocTypes;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderAmendmentDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.*;
import org.kuali.ole.module.purap.document.service.impl.InvoiceServiceImpl;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.module.purap.util.PurApObjectUtils;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.bo.OleInvoiceEncumbranceNotification;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.select.document.service.OleCreditMemoService;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OlePurapAccountingService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.ole.service.OleOrderRecordService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.util.AutoPopulatingList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class has implementation for PaymentRequest with respect to OLE.
 */
public class OleInvoiceServiceImpl extends InvoiceServiceImpl implements OleInvoiceService {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInvoiceServiceImpl.class);

    protected PurchaseOrderService purchaseOrderService;
    protected PurapService purapService;
    protected DocumentService documentService;
    protected NoteService noteService;
    private OlePurapAccountingService olePurapAccountingService;
    private PurapAccountingService purapAccountingService;
    protected ConfigurationService kualiConfigurationService;
    List<OleInvoiceEncumbranceNotification> invoiceEncumbranceNotificationList;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;
    private OleOrderRecordService oleOrderRecordService;
    DataCarrierService dataCarrierService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.DATA_CARRIER_SERVICE);
    private OleSelectDocumentService oleSelectDocumentService;
    private KualiDecimal accountingAmount;
    private BigDecimal percentage;

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public OleOrderRecordService getOleOrderRecordService() {
        if (oleOrderRecordService == null) {
            oleOrderRecordService = SpringContext.getBean(OleOrderRecordService.class);
        }
        return oleOrderRecordService;
    }


    @Override
    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    @Override
    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @Override
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }


    @Override
    public void setPurapAccountingService(PurapAccountingService purapAccountingService) {
        this.purapAccountingService = purapAccountingService;
    }

    public void setOlePurapAccountingService(OlePurapAccountingService olePurapAccountingService) {
        this.olePurapAccountingService = olePurapAccountingService;
    }

    /**
     * This method deletes unneeded items and updates the totals on the po and does any additional processing based on items
     *
     * @see org.kuali.ole.select.document.service.OleInvoiceService#completePaymentDocument(org.kuali.ole.select.document.OleInvoiceDocument)
     */
    @Override
    public void completePaymentDocument(OleInvoiceDocument invoiceDocument) {
        LOG.debug("Inside CompletePaymentDocument");

        PurchaseOrderDocument poDoc = null;

        if (invoiceDocument != null && invoiceDocument instanceof InvoiceDocument) {
            // delete unentered items
            purapService.deleteUnenteredItems(invoiceDocument);

            //     poDoc = invoiceDocument.getPurchaseOrderDocument(invoiceDocument.getPurchaseOrderIdentifier());
        }
        updatePaymentTotalsOnPurchaseOrder(invoiceDocument);


        //poDoc.setVendorName(invoiceDocument.getVendorDetail().getVendorName());

        // spawnPoAmendmentForUnorderedItems(invoiceDocument, poDoc);

        purapService.saveDocumentNoValidation(invoiceDocument);

        LOG.debug("Leaving CompletePaymentDocument");
    }

    protected void updatePaymentTotalsOnPurchaseOrder(OleInvoiceDocument invoiceDocument) {
        LOG.debug("Inside updatePaymentTotalsOnPurchaseOrder");
        List<Integer> poList = new ArrayList();
        Integer invPoId = 0;
        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
            if (!(poList.contains(invoiceItem.getPurchaseOrderIdentifier()))) {
                poList.add(invoiceItem.getPurchaseOrderIdentifier());
            }
        }
        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
            for (Integer purchaseOrderId : poList) {
                if (invoiceItem.getItemType() != null && invoiceItem.getItemType().getItemTypeCode() != null && StringUtils.equalsIgnoreCase(invoiceItem.getItemType().getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) &&
                        purchaseOrderId != null && invPoId != null && invPoId.compareTo(purchaseOrderId) != 0 && invoiceItem.getPurchaseOrderIdentifier().compareTo(purchaseOrderId) == '0') {
                    PurchaseOrderDocument poDoc = invoiceDocument.getPurchaseOrderDocument(purchaseOrderId);
                    OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) poDoc.getItemByLineNumber(invoiceItem.getItemLineNumber());

                    if (ObjectUtils.isNotNull(poItem)) {

                        KualiDecimal poItemReceivedTotal = poItem.getOutstandingQuantity();

                        KualiDecimal itemQuantity = invoiceItem.getItemQuantity();

                        if (ObjectUtils.isNull(itemQuantity)) {
                            itemQuantity = KualiDecimal.ZERO;
                        }
                        if (ObjectUtils.isNull(poItemReceivedTotal)) {
                            poItemReceivedTotal = KualiDecimal.ZERO;
                        }
                    /* Modified for OLE - 2516
                      poItem.setItemQuantity(itemQuantity);
                    */
                        // poItem.setItemUnitPrice(invoiceItem.getItemUnitPrice());
                    }
                    purapService.saveDocumentNoValidation(poDoc);
                }
                invPoId = purchaseOrderId;
            }

        }
        LOG.debug("Leaving updatePaymentTotalsOnPurchaseOrder");
    }


    /**
     * Spawns PO amendments for new unordered items on a Invoice document.
     *
     * @param invoice Document
     * @param po
     */
    protected void spawnPoAmendmentForUnorderedItems(OleInvoiceDocument invoice, PurchaseOrderDocument po) {

        LOG.debug("Inside spawnPoAmendmentForUnorderedItems");
        final PurchaseOrderDocument currentPO = po;
        if (invoice != null && invoice instanceof OleInvoiceDocument) {
            OleInvoiceDocument rlDoc = invoice;

            //if a new item has been added spawn a purchase order amendment
            if (hasNewUnorderedItem(invoice)) {
                String newSessionUserId = getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER);
                try {

                    LogicContainer logicToRun = new LogicContainer() {
                        @Override
                        public Object runLogic(Object[] objects) throws Exception {
                            OleInvoiceDocument rlDoc = (OleInvoiceDocument) objects[0];
                            String poDocNumber = (String) objects[1];

                            //create a PO amendment
                            PurchaseOrderAmendmentDocument amendmentPo = (PurchaseOrderAmendmentDocument) purchaseOrderService.createAndSavePotentialChangeDocument(currentPO, PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT, PurchaseOrderStatuses.APPDOC_AMENDMENT);

                            //add new lines to amendement
                            addUnorderedItemsToAmendment(amendmentPo, rlDoc);

                            //route amendment
                            documentService.routeDocument(amendmentPo, null, null);

                            //add note to amendment po document
                            String note = "Purchase Order Amendment " + amendmentPo.getPurapDocumentIdentifier() + " (document id " + amendmentPo.getDocumentNumber() + ") created for new unordered line items (document id " + rlDoc.getDocumentNumber() + ")";

                            Note noteObj = documentService.createNoteFromDocument(amendmentPo, note);
                            amendmentPo.addNote(noteObj);
                            documentService.saveDocumentNotes(amendmentPo);
                            noteService.save(noteObj);

                            return null;
                        }
                    };

                    purapService.performLogicWithFakedUserSession(newSessionUserId, logicToRun, rlDoc, po.getDocumentNumber());
                } catch (WorkflowException e) {
                    String errorMsg = "Workflow Exception caught: " + e.getLocalizedMessage();
                    throw new RuntimeException(errorMsg, e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        LOG.debug("Leaving spawnPoAmendmentForUnorderedItems");
    }

    /**
     * Checks the item list for newly added items.
     *
     * @param paymentDoc
     * @return
     */
    protected boolean hasNewUnorderedItem(OleInvoiceDocument paymentDoc) {
        LOG.debug("Inside hasNewUnorderedItem");
        boolean itemAdded = false;

        for (OleInvoiceItem prItem : (List<OleInvoiceItem>) paymentDoc.getItems()) {
            if (PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE.equals(prItem.getItemTypeCode())) {
                itemAdded = true;
                break;
            }
        }
        LOG.debug("Leaving hasNewUnorderedItem");
        return itemAdded;
    }

    /**
     * Adds an unordered item to a po amendment document.
     *
     * @param amendment
     * @param rlDoc
     */
    protected void addUnorderedItemsToAmendment(PurchaseOrderAmendmentDocument amendment, OleInvoiceDocument rlDoc) {

        LOG.debug("Inside addUnorderedItemsToAmendment");
        OlePurchaseOrderItem poi = null;

        for (OleInvoiceItem rlItem : (List<OleInvoiceItem>) rlDoc.getItems()) {
            if (PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE.equals(rlItem.getItemTypeCode())) {
                poi = createPoItemFromPaymentLine(rlItem);
                poi.setDocumentNumber(amendment.getDocumentNumber());
                poi.refreshNonUpdateableReferences();
                amendment.addItem(poi);
            }
        }
        LOG.debug("Leaving addUnorderedItemsToAmendment");

    }

    /**
     * Creates a PO item from invoice Line item.
     *
     * @param rlItem
     * @return
     */
    protected OlePurchaseOrderItem createPoItemFromPaymentLine(OleInvoiceItem rlItem) {
        LOG.debug("Inside createPoItemFromPaymentLine");
        OlePurchaseOrderItem poi = new OlePurchaseOrderItem();
        poi.setItemActiveIndicator(true);
        poi.setItemTypeCode(rlItem.getItemTypeCode());
        poi.setItemLineNumber(rlItem.getItemLineNumber());
        poi.setItemCatalogNumber(rlItem.getItemCatalogNumber());
        poi.setItemDescription(rlItem.getItemDescription());
        poi.setItemQuantity(rlItem.getItemQuantity());
        poi.setItemUnitOfMeasureCode(rlItem.getItemUnitOfMeasureCode());
        poi.setItemUnitPrice(rlItem.getItemUnitPrice());
//        poi.setSourceAccountingLines(rlItem.getSourceAccountingLines());
        poi.setItemNoOfParts(rlItem.getItemNoOfParts());
        poi.setItemListPrice(rlItem.getItemListPrice());
        poi.setItemDiscount(rlItem.getItemDiscount());
        poi.setItemDiscountType(rlItem.getItemDiscountType());
        poi.setFormatTypeId(rlItem.getFormatTypeId());

        //Foreign Currency
        poi.setItemCurrencyType(rlItem.getItemCurrencyType());
        poi.setItemForeignListPrice((rlItem.getItemForeignListPrice()));
        poi.setItemForeignDiscount((rlItem.getItemForeignDiscount()));
        poi.setItemForeignDiscountAmt((rlItem.getItemForeignDiscountAmt()));
        poi.setItemForeignDiscountType(rlItem.getItemForeignDiscountType());
        poi.setItemForeignUnitCost((rlItem.getItemForeignUnitCost()));
        poi.setItemExchangeRate((rlItem.getItemExchangeRate()));
        poi.setItemUnitCostUSD((rlItem.getItemUnitCostUSD()));
        poi.setItemInvoicedTotalAmount(rlItem.getTotalAmount());
        poi.setBibInfoBean(rlItem.getBibInfoBean());
        poi.setItemTitleId(rlItem.getItemTitleId());
        setAccountingLinesFromPayment(rlItem, poi);

        LOG.debug("Leaving createPoItemFromPaymentLine");
        return poi;
    }

    /**
     * Setting Accounting Lines For POA from newLineItem
     *
     * @param payItem
     * @param purItem
     */

    public void setAccountingLinesFromPayment(OleInvoiceItem payItem, OlePurchaseOrderItem purItem) {
        LOG.debug("Inside setAccountingLinesFromPayment");
        for (int i = 0; i < payItem.getSourceAccountingLines().size(); i++) {
            // OLE-3669 : Ensuring that the purchasing document has enough accounting lines
            // for copying from the payment
            while (purItem.getSourceAccountingLines().size() < i + 1) {
                PurchaseOrderAccount poAccount = new PurchaseOrderAccount();
                poAccount.setPurchaseOrderItem(purItem);
                purItem.getSourceAccountingLines().add(poAccount);
            }
            purItem.getSourceAccountingLine(i).copyFrom(payItem.getSourceAccountingLine(i));
            purItem.getSourceAccountingLine(i).setAccountLinePercent(payItem.getSourceAccountingLine(i).getAccountLinePercent());
        }
        LOG.debug("Leaving setAccountingLinesFromPayment");
    }

    /**
     * @see org.kuali.ole.select.document.service.OleInvoiceService#calculateProrateItemSurcharge(org.kuali.ole.select.document.OleInvoiceDocument)
     */
    @Override
    public void calculateProrateItemSurcharge(OleInvoiceDocument invoiceDocument) {
        LOG.debug("Inside Calculation for ProrateItemSurcharge");
        //  KualiDecimal addChargeItem = invoiceDocument.getGrandPreTaxTotalExcludingDiscount().subtract(invoiceDocument.getLineItemPreTaxTotal());
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OleInvoiceItem> item = invoiceDocument.getItems();
        for (OleInvoiceItem items : item) {
            if (items.getItemType().isAdditionalChargeIndicator() && items.getExtendedPrice() != null) {
                addChargeItem = addChargeItem.add(items.getExtendedPrice().bigDecimalValue());
            }
        }
        List<PurApItem> items = new ArrayList<>();
        /*List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = invoiceDocument.getPurchaseOrderDocuments();
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
            for (OlePurchaseOrderItem purItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                purItem.setItemListPrice(new KualiDecimal(purItem.getInvoiceItemListPrice()));
                if (purItem.isItemForInvoice() && purItem.getItemListPrice().compareTo(KualiDecimal.ZERO) >= 0) {
                    items.add(purItem);
                } else {
                    purItem.setItemSurcharge(BigDecimal.ZERO);
                }
                if (purItem.getItemListPrice().compareTo(KualiDecimal.ZERO) < 0) {
                    purItem.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(purItem));
                }
            }

        }
        if (items.size() == 0) {*/
        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
               /* if(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.getRelatedViews() != null) {
                    invoiceItem.setRelatedViews(null);
                }*/
            items.add(invoiceItem);
        }
       /* }
        else {
            for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                if (!(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    items.add(invoiceItem);
                }
            }
        }*/
        List<BigDecimal> newUnitPriceList = new ArrayList<>();
        BigDecimal totalExtPrice = new BigDecimal(0);
        BigDecimal newUnitPrice = new BigDecimal(0);
        BigDecimal extPrice = new BigDecimal(0);
        BigDecimal unitPricePercent = new BigDecimal(0);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal one = new BigDecimal(1);
        BigDecimal totalSurCharge = new BigDecimal(0);
        BigDecimal totalItemQuantity = new BigDecimal(0);
        BigDecimal itemSurchargeCons = new BigDecimal(0);
        for (PurApItem purItem : items) {
            if(purItem instanceof OlePurchaseOrderItem) {
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) purItem;
                //  purItem.setItemListPrice(new KualiDecimal(purItem.getInvoiceItemListPrice()));
                if ((poItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) && !ObjectUtils.isNull(poItem.getNoOfCopiesInvoiced()) && poItem.getItemListPrice().compareTo(KualiDecimal.ZERO) >= 0) {
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        totalItemQuantity = totalItemQuantity.add(poItem.getNoOfCopiesInvoiced().bigDecimalValue());
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) || invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        if (poItem.getItemDiscount() == null) {
                            poItem.setItemDiscount(KualiDecimal.ZERO);
                        }
                        if (poItem.getItemDiscountType() != null && poItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                            newUnitPrice = (hundred.subtract(poItem.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(poItem.getItemListPrice().bigDecimalValue());
                        } else {
                            newUnitPrice = poItem.getItemListPrice().bigDecimalValue().subtract(poItem.getItemDiscount().bigDecimalValue());
                        }
                        newUnitPriceList.add(newUnitPrice);
                        extPrice = newUnitPrice.multiply(poItem.getNoOfCopiesInvoiced().bigDecimalValue());
                        totalExtPrice = totalExtPrice.add(extPrice);
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                        if (poItem.getItemSurcharge() == null) {
                            poItem.setItemSurcharge(BigDecimal.ZERO);
                        }
                        totalSurCharge = totalSurCharge.add(poItem.getNoOfCopiesInvoiced().bigDecimalValue().multiply(poItem.getItemSurcharge()));
                    }
                }
            }

            else if(purItem instanceof OleInvoiceItem)   {
                OleInvoiceItem invItem = (OleInvoiceItem) purItem;
                if ((invItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) && !ObjectUtils.isNull(invItem.getItemQuantity()) && invItem.getItemListPrice().compareTo(KualiDecimal.ZERO) >= 0) {
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        totalItemQuantity = totalItemQuantity.add(invItem.getItemQuantity().bigDecimalValue());
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) || invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        if (invItem.getItemDiscount() == null) {
                            invItem.setItemDiscount(KualiDecimal.ZERO);
                        }
                        if (invItem.getItemDiscountType() != null && invItem.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                            newUnitPrice = (hundred.subtract(invItem.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(invItem.getItemListPrice().bigDecimalValue());
                        } else {
                            newUnitPrice = invItem.getItemListPrice().bigDecimalValue().subtract(invItem.getItemDiscount().bigDecimalValue());
                        }
                        newUnitPriceList.add(newUnitPrice);
                        extPrice = newUnitPrice.multiply(invItem.getItemQuantity().bigDecimalValue());
                        totalExtPrice = totalExtPrice.add(extPrice);
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                        if (invItem.getItemSurcharge() == null) {
                            invItem.setItemSurcharge(BigDecimal.ZERO);
                        }
                        totalSurCharge = totalSurCharge.add(invItem.getItemQuantity().bigDecimalValue().multiply(invItem.getItemSurcharge()));
                    }
                }
            }
        }

        if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
            if (totalItemQuantity.compareTo(BigDecimal.ZERO) != 0) {
                itemSurchargeCons = one.divide(totalItemQuantity, 8, RoundingMode.HALF_UP);
            }
        }
        for (int i = 0, j = 0; items.size() > i; i++) {
            PurApItem purItem = items.get(i);
            if(purItem instanceof OlePurchaseOrderItem) {
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) purItem;

                if (poItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && newUnitPriceList.size() > j && !ObjectUtils.isNull(poItem.getNoOfCopiesInvoiced())) {
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR)) {
                        if (totalExtPrice.compareTo(BigDecimal.ZERO) != 0) {
                            unitPricePercent = newUnitPriceList.get(j).divide(totalExtPrice, 8, RoundingMode.HALF_UP);
                        }
                        poItem.setItemSurcharge(unitPricePercent.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                        poItem.setItemUnitPrice(newUnitPriceList.get(j).add(poItem.getItemSurcharge()));
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        poItem.setItemSurcharge(itemSurchargeCons.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                        poItem.setItemUnitPrice(newUnitPriceList.get(j).add(poItem.getItemSurcharge()));
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) && poItem.getItemSurcharge() != null) {
                        poItem.setItemUnitPrice(newUnitPriceList.get(j).add(poItem.getItemSurcharge()));
                    }
                    j++;
                }
            }
            else if(purItem instanceof OleInvoiceItem)   {
                OleInvoiceItem invItem = (OleInvoiceItem) purItem;
                if (invItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && newUnitPriceList.size() > j && !ObjectUtils.isNull(invItem.getItemQuantity())) {
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR)) {
                        if (totalExtPrice.compareTo(BigDecimal.ZERO) != 0) {
                            unitPricePercent = newUnitPriceList.get(j).divide(totalExtPrice, 8, RoundingMode.HALF_UP);
                        }
                        invItem.setItemSurcharge(unitPricePercent.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                        invItem.setItemUnitPrice(newUnitPriceList.get(j).add(invItem.getItemSurcharge()));
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                        invItem.setItemSurcharge(itemSurchargeCons.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                        invItem.setItemUnitPrice(newUnitPriceList.get(j).add(invItem.getItemSurcharge()));
                    }
                    if (invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) && invItem.getItemSurcharge() != null) {
                        invItem.setItemUnitPrice(newUnitPriceList.get(j).add(invItem.getItemSurcharge()));
                    }
                    j++;
                }

            }
        }
        if (invoiceDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
            if (totalSurCharge.compareTo(addChargeItem) != 0) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_PAYMENT_REQUEST_TOTAL_MISMATCH);
            }
        }
        LOG.debug("Leaving Calculation for ProrateItemSurcharge");
    }

    @Override
    public void calculateInvoice(InvoiceDocument invoice, boolean updateDiscount) {
        LOG.debug("calculateInvoice() started");

        // general calculation, i.e. for the whole preq document
        if (ObjectUtils.isNull(invoice.getInvoicePayDate()) || invoice.getInvoicePayDate().before(new Date())) {
            invoice.setInvoicePayDate(calculatePayDate(invoice.getInvoiceDate(), invoice.getVendorPaymentTerms()));
        }

        distributeAccounting(invoice);

        //   purapService.calculateTax(invoice);

        // do proration for full order and trade in
        purapService.prorateForTradeInAndFullOrderDiscount(invoice);

        // do proration for payment terms discount
        if (updateDiscount) {
            //  calculateDiscount(invoice);
        }

        //    distributeAccounting(invoice);
    }

    @Override
    protected void distributeAccounting(InvoiceDocument invoiceDocument)  {
        // update the account amounts before doing any distribution
        purapAccountingService.updateAccountAmounts(invoiceDocument);
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) invoiceDocument;
        for (InvoiceItem item : (List<InvoiceItem>) invoiceDocument.getItems()) {
            KualiDecimal totalAmount = KualiDecimal.ZERO;
            KualiDecimal totalQty = KualiDecimal.ZERO;
            KualiDecimal totalSurcharge = KualiDecimal.ZERO;
            List<PurApAccountingLine> distributedAccounts = null;
            List<SourceAccountingLine> summaryAccounts = null;
            Set excludedItemTypeCodes = new HashSet();
            boolean canProrate = false;
            excludedItemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE);

            // skip above the line
            if (item.getItemType().isLineItemIndicator()) {
                continue;
            }
            /*if (item.getExtendedPrice() == null || item.getExtendedPrice().compareTo(KualiDecimal.ZERO) == 0) {
                item.setSourceAccountingLines(new ArrayList<PurApAccountingLine>());
            }*/
            if ((ObjectUtils.isNotNull(item.getExtendedPrice())) && (KualiDecimal.ZERO.compareTo(item.getExtendedPrice()) != 0) && !"DISC".equals(item.getItemTypeCode())) {
                if ((StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE, item.getItemType().getItemTypeCode())) && (invoiceDocument.getGrandTotal() != null) && ((KualiDecimal.ZERO.compareTo(invoiceDocument.getGrandTotal()) != 0))) {

                    // No discount is applied to other item types other than item line
                    // See KFSMI-5210 for details

                    // total amount should be the line item total, not the grand total
                    totalAmount = invoiceDocument.getLineItemTotal();

                    // prorate item line accounts only
                    Set includedItemTypeCodes = new HashSet();
                    includedItemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                    summaryAccounts = olePurapAccountingService.generateSummaryIncludeItemTypesAndNoZeroTotals(invoiceDocument.getItems(), includedItemTypeCodes);
                    distributedAccounts = olePurapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, InvoiceAccount.class);

                    // update amounts on distributed accounts
                    purapAccountingService.updateAccountAmountsWithTotal(distributedAccounts, item.getTotalAmount());
                } else {
                    OleInvoiceItem poi = null;
                    if (item.getItemType().isLineItemIndicator()) {
                        List<OleInvoiceItem> items = invoiceDocument.getItems();
                        poi = items.get(item.getItemLineNumber() - 1);
                        // throw error if line numbers don't match
                        // MSU Contribution DTT-3014 OLEMI-8483 OLECNTRB-974
                /*
                 * List items = po.getItems(); if (items != null) { for (Object object : items) { PurchaseOrderItem item =
                 * (PurchaseOrderItem) object; if (item != null && item.getItemLineNumber().equals(this.getItemLineNumber())) { poi
                 * = item; break; } } }
                 */
                    } else {
                        poi = (OleInvoiceItem) SpringContext.getBean(PurapService.class).getBelowTheLineByType(invoiceDocument, item.getItemType());
                    }

                    List<PurApItem> items = new ArrayList<>();
/*
                    for (OlePurchaseOrderDocument olePurchaseOrderDocument : invoiceDocument.getPurchaseOrderDocuments()) {
                        for (OlePurchaseOrderItem purItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                            if (purItem.isItemForInvoice()) {
                                items.add(purItem);
                                if (purItem.getItemQuantity() != null) {
                                    totalQty = totalQty.add(purItem.getItemQuantity());
                                }
                            }
                        }
                    }*/
                    if (items.size() == 0) {
                        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                            if(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() ) {
                                //     invoiceItem.setRelatedViews(null);
                                if (invoiceItem.getItemQuantity() != null) {
                                    totalQty = totalQty.add(invoiceItem.getItemQuantity());
                                }
                                if (invoiceItem.getItemListPrice().isNonZero()) {
                                    canProrate = true;
                                }
                            }

                            if (!(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) && invoiceItem.getExtendedPrice()!=null) {
                                totalSurcharge = totalSurcharge.add(invoiceItem.getExtendedPrice());
                            }
                            if (invoiceItem.getItemUnitPrice() != null ) {
                                if (invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO) != 0 || oleInvoiceDocument.isProrateQty()) {
                                    items.add(invoiceItem);
                                }

                            }
                        }
                    }
                    else {
                        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                            if (!(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                                if (invoiceItem.getItemUnitPrice() != null && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO) != 0) {
                                    items.add(invoiceItem);
                                }
                                if(invoiceItem.getExtendedPrice()!=null){
                                    totalSurcharge = totalSurcharge.add(invoiceItem.getExtendedPrice());
                                }
                            }
                        }
                    }

                    totalAmount = getTotalDollarAmountWithExclusionsSubsetItems(null, true, items);//invoiceDocument.getTotalDollarAmountAboveLineItems();
                    totalAmount = totalAmount.subtract(totalSurcharge);
                    purapAccountingService.updateAccountAmounts(invoiceDocument);
                    if(oleInvoiceDocument.isProrateManual()){
                        summaryAccounts = olePurapAccountingService.generateSummaryForManual(items);
                        distributedAccounts = olePurapAccountingService.generateAccountDistributionForProrationByManual(summaryAccounts,InvoiceAccount.class);
                    }

                    if(oleInvoiceDocument.isProrateDollar() || oleInvoiceDocument.isProrateQty()){
                        summaryAccounts = olePurapAccountingService.generateSummary(items);
                    }
                    if(oleInvoiceDocument.isProrateDollar() && totalAmount.isNonZero()){
                        distributedAccounts = olePurapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, InvoiceAccount.class);
                    }else if(oleInvoiceDocument.isProrateQty() && totalQty.isNonZero()) {
                        distributedAccounts = olePurapAccountingService.generateAccountDistributionForProrationByQty(summaryAccounts, totalQty, PurapConstants.PRORATION_SCALE, InvoiceAccount.class);
                    }
                }
                if (CollectionUtils.isNotEmpty(distributedAccounts) && !oleInvoiceDocument.isProrateManual()) {
                        item.setSourceAccountingLines(distributedAccounts);
                }
                else if (!oleInvoiceDocument.isNoProrate() && !oleInvoiceDocument.isProrateManual()) {
                    item.setSourceAccountingLines(new ArrayList<PurApAccountingLine>());
                }
            }
            // update the item
            purapAccountingService.updateItemAccountAmounts(item);

        }
        // update again now that distribute is finished. (Note: we may not need this anymore now that I added updateItem line above
        purapAccountingService.updateAccountAmounts(invoiceDocument);

    }


    /**
     * This method...
     *
     * @param excludedTypes
     * @param includeBelowTheLine
     * @param itemsForTotal
     * @return
     */
    public KualiDecimal getTotalDollarAmountWithExclusionsSubsetItems(String[] excludedTypes, boolean includeBelowTheLine, List<PurApItem> itemsForTotal) {
        if (excludedTypes == null) {
            excludedTypes = new String[]{};
        }

        KualiDecimal total = new KualiDecimal(BigDecimal.ZERO);
        for (PurApItem item : itemsForTotal) {
            item.refreshReferenceObject(PurapPropertyConstants.ITEM_TYPE);
            ItemType it = item.getItemType();
            if ((includeBelowTheLine || it.isLineItemIndicator()) && !ArrayUtils.contains(excludedTypes, it.getItemTypeCode())) {
                KualiDecimal totalAmount = item.getTotalAmount();
                KualiDecimal itemTotal = (totalAmount != null) ? totalAmount : KualiDecimal.ZERO;
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.InvoiceService#getInvoiceByDocumentNumber(String)
     */
    @Override
    public OleInvoiceDocument getInvoiceByDocumentNumber(String documentNumber) {
        LOG.debug("getInvoiceByDocumentNumber() started");

        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                OleInvoiceDocument doc = (OleInvoiceDocument) documentService.getByDocumentHeaderId(documentNumber);
                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting invoice document from document service";
                LOG.error("Exception While getting invoice document based on document number " + errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return null;
    }

    /**
     * This method is validates the prorate surchanges if prorate by manual
     *
     * @see org.kuali.ole.select.document.service.OleInvoiceService#validateProratedSurcharge(org.kuali.ole.select.document.OleInvoiceDocument)
     */
    @Override
    public boolean validateProratedSurcharge(OleInvoiceDocument invoiceDocument) {

        List<OleInvoiceItem> items = invoiceDocument.getItems();
        boolean manuvalProrateValidFlag = false;
        BigDecimal proratedSurchargeAmount = new BigDecimal(0);
        for (int i = 0; items.size() > i; i++) {
            OleInvoiceItem item = (OleInvoiceItem) invoiceDocument.getItem(i);
            if ("".equals(item.getItemTitleId()) || item.getItemTitleId() == null) {
                if (item.getItemUnitPrice() != null && !"".equals(item.getItemUnitPrice())) {
                    manuvalProrateValidFlag = true;
                }
            }
        }
        if (manuvalProrateValidFlag) {
            for (int i = 0; items.size() > i; i++) {
                OleInvoiceItem item = (OleInvoiceItem) invoiceDocument.getItem(i);
                if (!"".equals(item.getItemTitleId()) && item.getItemTitleId() != null) {
                    if (item.getItemSurcharge() != null && item.getItemSurcharge().compareTo(new BigDecimal(0)) != 0) {
                        proratedSurchargeAmount = proratedSurchargeAmount.add(item.getItemSurcharge());
                    }
                }
            }
            if (proratedSurchargeAmount.compareTo(new BigDecimal(0)) == 0) {
                manuvalProrateValidFlag = false;
                invoiceDocument.setProrateBy(null);
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_PAYMENT_REQUEST_TOTAL_MISMATCH);
            }
        }
        return manuvalProrateValidFlag;
    }

    /**
     * This method is for caluclate the total amount without select proprate by Quantity,doller and manual
     *
     * @see org.kuali.ole.select.document.service.OleInvoiceService#calculateWithoutProrates(org.kuali.ole.select.document.OleInvoiceDocument)
     */
    @Override
    public void calculateWithoutProrates(OleInvoiceDocument invoiceDocument) {
        LOG.debug("Inside Calculation for with out  prorate");
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OleInvoiceItem> items = invoiceDocument.getItems();

        for (OleInvoiceItem item : items) {
            if (item.getItemTitleId() != null && !"".equals(item.getItemTitleId())) {
                if (!item.getItemListPrice().equals(item.getExtendedPrice())) {
                    item.setItemUnitPrice(item.getItemListPrice().bigDecimalValue());
                    item.setExtendedPrice(item.getItemListPrice());
                    item.setItemSurcharge(BigDecimal.ZERO);
                }
            }
        }

        for (OleInvoiceItem item : items) {
            if (!item.getItemType().isQuantityBasedGeneralLedgerIndicator()
                    && !item.getItemTypeCode().equalsIgnoreCase(
                    PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE)
                    && item.getItemUnitPrice() != null) {
                addChargeItem = addChargeItem.add(item.getItemUnitPrice());
            }
        }
        List<BigDecimal> newUnitPriceList = new ArrayList<>();
        BigDecimal totalExtPrice = new BigDecimal(0);
        BigDecimal newUnitPrice = new BigDecimal(0);
        BigDecimal extPrice = new BigDecimal(0);
        BigDecimal unitPricePercent = new BigDecimal(0);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal one = new BigDecimal(1);
        BigDecimal totalSurCharge = new BigDecimal(0);
        BigDecimal totalItemQuantity = new BigDecimal(0);
        BigDecimal itemSurchargeCons = new BigDecimal(0);
        for (int i = 0; items.size() > i; i++) {
            OleInvoiceItem item = (OleInvoiceItem) invoiceDocument.getItem(i);
            if ((item.getItemType().isQuantityBasedGeneralLedgerIndicator())
                    && !ObjectUtils.isNull(item.getItemQuantity())) {
                if (item.getItemSurcharge() == null) {
                    item.setItemSurcharge(BigDecimal.ZERO);
                }
                if (invoiceDocument.getProrateBy() == null) {
                    if (item.getItemDiscount() == null) {
                        item.setItemDiscount(KualiDecimal.ZERO);
                    }
                    if (item.getItemDiscountType() != null
                            && item.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                        newUnitPrice =SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        newUnitPrice = SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    newUnitPriceList.add(newUnitPrice);
                    extPrice = newUnitPrice.multiply(item.getItemQuantity().bigDecimalValue());
                    totalExtPrice = totalExtPrice.add(extPrice);
                }
                totalSurCharge = totalSurCharge.add(item.getItemQuantity().bigDecimalValue()
                        .multiply(item.getItemSurcharge()));
            }
        }
        for (int i = 0, j = 0; items.size() > i; i++) {
            OleInvoiceItem item = (OleInvoiceItem) invoiceDocument.getItem(i);
            if (item.getItemType().isQuantityBasedGeneralLedgerIndicator() && newUnitPriceList.size() > j
                    && !ObjectUtils.isNull(item.getItemQuantity())) {
                if (item.getItemSurcharge() != null) {
                    item.setItemUnitPrice(newUnitPriceList.get(j).add(item.getItemSurcharge()));
                }
                j++;
            }
        }
        LOG.debug("Leaving Calculation for with out  prorate");
    }

    public void createPaymentRequestOrCreditMemoDocument(OleInvoiceDocument inv) {
        List<OleInvoiceItem> negativeItems = new ArrayList<>();
        List<OleInvoiceItem> positiveItems = new ArrayList<>();
        List<OleInvoiceItem> lineItems = new ArrayList<>();
        List<OleInvoiceItem> positiveLineItems = new ArrayList<>();
        List<OleInvoiceItem> negativeLineItems = new ArrayList<>();
        Boolean isItemLevelDebit = null;
        Boolean isAdditionalChargeLevelDebit = null;
        Boolean isItemLevelCredit = null;
        Boolean isAdditionalChargeLevelCredit = null;
        BigDecimal firstPOTotalUnitPrice=BigDecimal.ZERO;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) ObjectUtils.deepCopy(inv);
        calculateProrateForPOLevel(oleInvoiceDocument);
        for(OleInvoiceItem item : (List<OleInvoiceItem>)oleInvoiceDocument.getItems()){
            if (item.isDebitItem()  &&
                    (item.getItemListPrice().isNonZero() ||
                    (item.getItemUnitPrice()!=null && item.getItemUnitPrice().compareTo(BigDecimal.ZERO)!=0))){

                if(lineItems.size()==0 && item.getPurchaseOrderIdentifier()!=null){
                    lineItems.add(item);
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.add(item.getItemUnitPrice());
                }else if(lineItems.size()>0 && item.getPurchaseOrderIdentifier()!=null &&
                        lineItems.get(0).getPurchaseOrderIdentifier().compareTo(item.getPurchaseOrderIdentifier())==0){
                    lineItems.add(item);
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.add(item.getItemUnitPrice());
                }

                if(item.getItemType().isQuantityBasedGeneralLedgerIndicator()){
                    if(isItemLevelDebit==null){
                        isItemLevelDebit = true;
                    }
                    isItemLevelDebit &= item.isDebitItem();

                    if(lineItems.size()>0 && item.getItemListPrice().isNonZero()){
                        positiveLineItems.add(item);
                    }
                }
                if(item.getItemType().isAdditionalChargeIndicator()){
                    if(isAdditionalChargeLevelDebit==null){
                        isAdditionalChargeLevelDebit = true;
                    }
                    isAdditionalChargeLevelDebit &= item.isDebitItem();
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.add(item.getItemUnitPrice());

                }
                if(item.getItemListPrice().isNonZero()){
                    positiveItems.add(item);
                }

            }
            else if(!item.isDebitItem() &&
                    (item.getItemListPrice().isNonZero() ||
                    (item.getItemUnitPrice()!=null && item.getItemUnitPrice().compareTo(BigDecimal.ZERO)!=0))){

                if(lineItems.size()==0 && item.getPurchaseOrderIdentifier()!=null){
                    lineItems.add(item);
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.subtract(item.getItemUnitPrice());
                }else if(lineItems.size()>0 && item.getPurchaseOrderIdentifier()!=null &&
                        lineItems.get(0).getPurchaseOrderIdentifier().compareTo(item.getPurchaseOrderIdentifier())==0){
                    lineItems.add(item);
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.subtract(item.getItemUnitPrice());
                }

                if(item.getItemType().isQuantityBasedGeneralLedgerIndicator()){
                    if(isItemLevelCredit==null){
                        isItemLevelCredit = true;
                    }
                    isItemLevelCredit &= !item.isDebitItem();

                    if(lineItems.size()>0 && item.getItemListPrice().isNonZero()){
                        negativeLineItems.add(item);
                    }
                }
                if(item.getItemType().isAdditionalChargeIndicator()){
                    if(isAdditionalChargeLevelCredit==null){
                        isAdditionalChargeLevelCredit = true;
                    }
                    isAdditionalChargeLevelCredit &= !item.isDebitItem();
                    firstPOTotalUnitPrice = firstPOTotalUnitPrice.subtract(item.getItemUnitPrice());

                }
                if(item.getItemListPrice().isNonZero()){
                    negativeItems.add(item);
                }
            }
        }
        positiveLineItems.removeAll(lineItems);
        negativeLineItems.removeAll(lineItems);
        /*if((isItemLevelDebit == null && isAdditionalChargeLevelDebit!=null && isAdditionalChargeLevelDebit) ||
                (isAdditionalChargeLevelDebit == null && isItemLevelDebit!=null && isItemLevelDebit) ||
                (isItemLevelCredit == null && isAdditionalChargeLevelCredit!=null && isAdditionalChargeLevelCredit) ||
                (isAdditionalChargeLevelCredit == null && isItemLevelCredit!=null && isItemLevelCredit)
                        && !(isItemLevelCredit!=null && isItemLevelCredit && isItemLevelDebit!=null && isItemLevelDebit)){
           *//* if(new KualiDecimal(firstPOTotalUnitPrice).isNegative()){
                createCreditMemoDocument(inv, lineItems,true);
            }else{
                createPaymentRequestDocument(oleInvoiceDocument, lineItems,true);
            }*//*
            if (positiveLineItems.size() > 0) {
                createPaymentRequestDocument(oleInvoiceDocument, positiveLineItems,false);
            }
            if (negativeLineItems.size() > 0) {
                createCreditMemoDocument(inv, negativeLineItems,false);
            }

        }else{*/
        if (positiveItems.size() > 0) {
            createPaymentRequestDocument(oleInvoiceDocument, positiveItems,true);
        }
        if (negativeItems.size() > 0) {
            createCreditMemoDocument(oleInvoiceDocument, negativeItems,true);
        }
    //    }
    }

    public void createPaymentRequestDocument(OleInvoiceDocument inv, List<OleInvoiceItem> items,boolean flag) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating Payment Request document");
        }

        KNSGlobalVariables.getMessageList().clear();

        //   validateInvoiceOrderValidForPREQCreation(inv);

        if (LOG.isDebugEnabled()) {
            if (inv.isInvoiceCancelIndicator()) {
                LOG.debug("Not possible to convert cancelled Invoice details into payment request");
            } else {
                LOG.debug("Payment request document creation validation succeeded");
            }
        }

        if (inv.isInvoiceCancelIndicator()) {
            LOG.debug("Invoice Cancelled, Payment Request not created");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Total Dollar Amount above line items >>>>" + inv.getTotalDollarAmountAboveLineItems());
        }
        Integer invId = inv.getPurapDocumentIdentifier();
        Integer poId = 0;
        List<Integer> poList = new ArrayList();

        List<OleInvoiceItem> oleInvoiceItemList = items;
        KualiDecimal invItemCount = new KualiDecimal(0);
        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItemList) {
            if (oleInvoiceItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                invItemCount = invItemCount.add(oleInvoiceItem.getItemQuantity());
            }
            if ((!(poList.contains(oleInvoiceItem.getPurchaseOrderIdentifier()))) && oleInvoiceItem.getExtendedPrice().isNonZero()) {
                poList.add(oleInvoiceItem.getPurchaseOrderIdentifier());
            }
        }

        String prorateBy = inv.getProrateBy();

        OlePaymentRequestDocument preqDoc = null;
        Integer invPoId = 0;
        for (Integer purchaseOrderId : poList) {
            if (purchaseOrderId != null && invPoId.compareTo(purchaseOrderId) != 0) {
                try {
                    preqDoc = (OlePaymentRequestDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_PREQ");
                    preqDoc.setImmediatePaymentIndicator(inv.getImmediatePaymentIndicator());
                    preqDoc.setInvoiceDate(inv.getInvoiceDate());
                    preqDoc.setInvoiceNumber(inv.getInvoiceNumber());
                    preqDoc.setVendorInvoiceAmount(inv.getVendorInvoiceAmount().abs());
                    preqDoc.setVendorDetail(inv.getVendorDetail());
                    preqDoc.setVendorName(inv.getVendorName());
                    preqDoc.setVendorHeaderGeneratedIdentifier(inv.getVendorHeaderGeneratedIdentifier());
                    preqDoc.setVendorDetailAssignedIdentifier(inv.getVendorDetailAssignedIdentifier());
                    preqDoc.setVendorNumber(inv.getVendorNumber());
                    preqDoc.setVendorHeaderGeneratedIdentifier(inv.getVendorHeaderGeneratedIdentifier());
                    preqDoc.setVendorDetailAssignedIdentifier(inv.getVendorDetailAssignedIdentifier());
                    preqDoc.setVendorPaymentTerms(inv.getVendorPaymentTerms());
                    if (inv.getVendorPaymentTerms() != null) {
                        preqDoc.setVendorPaymentTermsCode(inv.getVendorPaymentTerms().getVendorPaymentTermsCode());
                    }
                    preqDoc.setVendorShippingTitleCode(inv.getVendorShippingTitleCode());
                    preqDoc.setVendorShippingPaymentTerms(inv.getVendorShippingPaymentTerms());
                    preqDoc.setVendorCityName(inv.getVendorCityName());
                    preqDoc.setVendorLine1Address(inv.getVendorLine1Address());
                    preqDoc.setVendorLine2Address(inv.getVendorLine2Address());
                    preqDoc.setVendorAttentionName(inv.getVendorAttentionName());
                    preqDoc.setVendorPostalCode(inv.getVendorPostalCode());
                    preqDoc.setVendorStateCode(inv.getVendorStateCode());
                    preqDoc.setVendorAttentionName(inv.getVendorAttentionName());
                    preqDoc.setVendorAddressInternationalProvinceName(inv.getVendorAddressInternationalProvinceName());
                    preqDoc.setVendorCountryCode(inv.getVendorCountryCode());
                    preqDoc.setVendorCountry(inv.getVendorCountry());
                    preqDoc.setVendorCustomerNumber(inv.getVendorCustomerNumber());
                    preqDoc.setAccountsPayableProcessorIdentifier(inv.getAccountsPayableProcessorIdentifier());
                    preqDoc.setProcessingCampusCode(inv.getProcessingCampusCode());
                    preqDoc.setPurchaseOrderIdentifier(purchaseOrderId);
                    //
                    //preqDoc.setClosePurchaseOrderIndicator(oleInvoiceItem.isClosePurchaseOrderIndicator());
                    preqDoc.setPaymentRequestPayDate(inv.getInvoicePayDate());
                    preqDoc.setImmediatePaymentIndicator(inv.getImmediatePaymentIndicator());
                    preqDoc.setPaymentRequestCostSource(inv.getInvoiceCostSource());
                    preqDoc.setProrateBy(inv.getProrateBy());
                    preqDoc.setProrateDollar(inv.isProrateDollar());
                    preqDoc.setProrateQty(inv.isProrateQty());
                    preqDoc.setProrateManual(inv.isProrateManual());
                    preqDoc.setNoProrate(inv.isNoProrate());
                    preqDoc.setForeignVendorInvoiceAmount(inv.getForeignVendorInvoiceAmount());


                    if (inv.getPaymentMethodId() != null) {
                        OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class)
                                .findBySinglePrimaryKey(OlePaymentMethod.class, inv.getPaymentMethodId());
                        preqDoc.setPaymentMethod(olePaymentMethod);
                        preqDoc.getPaymentMethod().setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
                        preqDoc.setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
                    }

                    preqDoc.setInvoiceIdentifier(inv.getPurapDocumentIdentifier());
                    preqDoc.setBankCode(inv.getBankCode());
                    preqDoc.setBank(inv.getBank());
                } catch (WorkflowException e) {
                    String extraDescription = "Error=" + e.getMessage();
                    LOG.error("Exception creating Payment request document - " + e.getMessage());
                }

                Map invItemMap = new HashMap();
                invItemMap.put(PurapConstants.PRQSDocumentsStrings.PUR_ID, inv.getPurapDocumentIdentifier());
                invItemMap.put(PurapConstants.PRQSDocumentsStrings.PO_ID, purchaseOrderId);
                List<OleInvoiceItem> invoiceItems = (List<OleInvoiceItem>) businessObjectService.findMatchingOrderBy(OleInvoiceItem.class, invItemMap, PurapConstants.PRQSDocumentsStrings.PO_ID, true);
                List<OleInvoiceItem> invoiceItemList = new ArrayList();
                if(inv.getProrateBy() != null && (inv.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || inv.getProrateBy().equalsIgnoreCase(OLEConstants.PRORATE_BY_DOLLAR) || inv.getProrateBy().equalsIgnoreCase(OLEConstants.MANUAL_PRORATE))) {
                    for(OleInvoiceItem item : (List<OleInvoiceItem>)inv.getItems()) {
                        if(item.getPurchaseOrderIdentifier().intValue() == invoiceItems.get(0).getPurchaseOrderIdentifier().intValue()) {
                            invoiceItemList.add(item);
                        }
                    }
                } else {
                    invoiceItemList = invoiceItems;
                }

                KualiDecimal itemCount = new KualiDecimal(0);
                KualiDecimal itemPrice = new KualiDecimal(0);
                PurchaseOrderDocument poDoc = inv.getPurchaseOrderDocument(purchaseOrderId);
                if (poDoc == null) {
                    throw new RuntimeException("Purchase Order document (invPoId=" + invPoId + ") does not exist in the system");
                }

                preqDoc.getDocumentHeader().setDocumentDescription(createPreqDocumentDescription(poDoc.getPurapDocumentIdentifier(), inv.getVendorName()));

                try {
                    preqDoc.updateAndSaveAppDocStatus(PurapConstants.PaymentRequestStatuses.APPDOC_IN_PROCESS);
                } catch (WorkflowException we) {
                    throw new RuntimeException("Unable to save route status data for document: " + preqDoc.getDocumentNumber(), we);
                }

                SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(preqDoc));

                SpringContext.getBean(PaymentRequestService.class).calculatePaymentRequest(preqDoc, false);
                HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = SpringContext.getBean(AccountsPayableService.class).expiredOrClosedAccountsList(poDoc);
                if (expiredOrClosedAccountList == null) {
                    expiredOrClosedAccountList = new HashMap();
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug(expiredOrClosedAccountList.size() + " accounts has been found as Expired or Closed");
                }
                List<OlePaymentRequestItem> olePaymentRequestItems = new ArrayList<>();
                // int itemLineNumberCount = 0;
                for (OleInvoiceItem invoiceItem : invoiceItemList) {
                    if ((flag || invoiceItem.isDebitItem()) && invoiceItem.getExtendedPrice().isNonZero()) {
                        OlePaymentRequestItem olePaymentRequestItem = new OlePaymentRequestItem(invoiceItem, preqDoc, expiredOrClosedAccountList);
                        if(flag && !invoiceItem.isDebitItem()){
                            olePaymentRequestItem.setItemListPrice(olePaymentRequestItem.getItemListPrice().negated());
                            olePaymentRequestItem.setItemUnitPrice(olePaymentRequestItem.getItemUnitPrice().negate());
                            olePaymentRequestItem.setExtendedPrice(olePaymentRequestItem.getExtendedPrice().negated());
                            for(PurApAccountingLine purApAccountingLine : olePaymentRequestItem.getSourceAccountingLines()){
                                purApAccountingLine.setAmount(purApAccountingLine.getAmount().negated());
                            }
                        }
                        olePaymentRequestItems.add(olePaymentRequestItem);
                        if (invoiceItem.isReopenPurchaseOrderIndicator()) {
                            preqDoc.setReopenPurchaseOrderIndicator(invoiceItem.isReopenPurchaseOrderIndicator());
                        }
                        if (invoiceItem.isClosePurchaseOrderIndicator()) {
                            preqDoc.setClosePurchaseOrderIndicator(invoiceItem.isClosePurchaseOrderIndicator());
                        }
                        if (preqDoc.getAccountsPayablePurchasingDocumentLinkIdentifier() == null) {
                            preqDoc.setAccountsPayablePurchasingDocumentLinkIdentifier(invoiceItem.getAccountsPayablePurchasingDocumentLinkIdentifier());
                        }
                        preqDoc.setReceivingDocumentRequiredIndicator(invoiceItem.isReceivingDocumentRequiredIndicator());
                    }
                }

                invPoId = purchaseOrderId;
                preqDoc.setItems(olePaymentRequestItems);

                try {
                    SpringContext.getBean(PaymentRequestService.class).populateAndSavePaymentRequest(preqDoc);

                    SpringContext.getBean(PaymentRequestService.class).autoApprovePaymentRequest(preqDoc);
                } catch (WorkflowException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    String extraDescription = GlobalVariables.getMessageMap().toString();
                }
            }


            if (GlobalVariables.getMessageMap().hasErrors()) {

                LOG.error("***************Error in rules processing - " + GlobalVariables.getMessageMap());
                Map<String, AutoPopulatingList<ErrorMessage>> errorMessages = GlobalVariables.getMessageMap().getErrorMessages();

                String errors = errorMessages.toString();
            }

            if (KNSGlobalVariables.getMessageList().size() > 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Payment request contains " + KNSGlobalVariables.getMessageList().size() + " warning message(s)");
                    for (int i = 0; i < KNSGlobalVariables.getMessageList().size(); i++) {
                        LOG.debug("Warning " + i + "  - " + KNSGlobalVariables.getMessageList().get(i));
                    }
                }
            }


            String routingAnnotation = null;
            if (!inv.isInvoiceCancelIndicator()) {
                routingAnnotation = "Routed by New Invoice Creation";
            }
        }
    }


    public void populateInvoice(OleInvoiceDocument invoiceDocument) {

        PurchaseOrderDocument purchaseOrderDocument = invoiceDocument.getPurchaseOrderDocument();

        // make a call to search for expired/closed accounts
        HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = getAccountsPayableService().getExpiredOrClosedAccountList(invoiceDocument);

        //SpringContext.getBean(OleInvoiceService.class).populateInvoiceFromPurchaseOrders(invoiceDocument,expiredOrClosedAccountList);

        // invoiceDocument.getDocumentHeader().setDocumentDescription(createPreqDocumentDescription(invoiceDocument.getPurchaseOrderIdentifier(), invoiceDocument.getVendorName()));

        // write a note for expired/closed accounts if any exist and add a message stating there were expired/closed accounts at the
        // top of the document
        getAccountsPayableService().generateExpiredOrClosedAccountNote(invoiceDocument, expiredOrClosedAccountList);

        // set indicator so a message is displayed for accounts that were replaced due to expired/closed status
        if (!expiredOrClosedAccountList.isEmpty()) {
            invoiceDocument.setContinuationAccountIndicator(true);
        }

        // add discount item
        calculateDiscount(invoiceDocument);
        // distribute accounts (i.e. proration)
        distributeAccounting(invoiceDocument);

        // set bank code to default bank code in the system parameter
        Bank defaultBank = bankService.getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);
        }
    }

    /*
    * This method is overridden to populate Ole InvoiceDocument from PurchaseOrder Document
    * @see org.kuali.ole.module.purap.document.InvoiceDocument#populateInvoiceFromPurchaseOrder(org.kuali.ole.module.purap.document.PurchaseOrderDocument, java.util.HashMap)
    */
    public OleInvoiceDocument populateInvoiceFromPurchaseOrders(OleInvoiceDocument invoiceDocument,
                                                                HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        LOG.debug("Inside populateInvoiceFromPurchaseOrders method ");
        List<OleInvoiceItem> items = new ArrayList<>();
        if (expiredOrClosedAccountList == null) {
            expiredOrClosedAccountList = new HashMap<>();
        }

        List<OleInvoiceItem> invoiceItemList = new ArrayList<>();
        OleInvoiceItem oleInvoiceItem;

        for (int count = 0; count < invoiceDocument.getItems().size(); count++) {
            OleInvoiceItem invoiceditem = (OleInvoiceItem) invoiceDocument.getItems().get(count);
            if (!(invoiceditem.getItemType().getItemTypeCode().equals("ITEM"))) {
                invoiceItemList.add(invoiceditem);
            }
        }
        invoiceDocument.setItems(invoiceItemList);
        if (invoiceDocument.getPurchaseOrderDocuments() != null && invoiceDocument.getPurchaseOrderDocuments().size() > 0) {
            for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                if (invoiceItem.getItemType().isAdditionalChargeIndicator() && invoiceItem.getItemUnitPrice() != null)
                    invoiceItem.setPurchaseOrderIdentifier(invoiceDocument.getPurchaseOrderDocuments().get(0).getPurapDocumentIdentifier());
            }
        }

        for (OlePurchaseOrderDocument po : invoiceDocument.getPurchaseOrderDocuments()) {
           /* if(this.encumberedItemExistsForInvoicing(po))
            {*/
            for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) po.getItems()) {
                // check to make sure it's eligible for payment (i.e. active and has encumbrance available
                //if (this.poItemEligibleForAp(invoiceDocument, poi)) {
                if (poi.isItemForInvoice()) {
                    OleInvoiceItem invoiceItem = new OleInvoiceItem(poi, invoiceDocument, expiredOrClosedAccountList);

                    invoiceItem.setClosePurchaseOrderIndicator(po.isClosePO());
                    invoiceItem.setReopenPurchaseOrderIndicator(po.getIsReOpenPO());
                    PurchasingCapitalAssetItem purchasingCAMSItem = po.getPurchasingCapitalAssetItemByItemIdentifier(poi.getItemIdentifier());
                    if (purchasingCAMSItem != null) {
                        invoiceItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                    }
                    invoiceItem.setUseTaxIndicator(po.isUseTaxIndicator());
                    invoiceItem.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
                    invoiceItem.setPostingYear(po.getPostingYear());
                    invoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());
                    // copy usetaxitems over
                    invoiceItem.getUseTaxItems().clear();
                    for (PurApItemUseTax useTax : poi.getUseTaxItems()) {
                        invoiceItem.getUseTaxItems().add(useTax);
                    }
                    invoiceDocument.getItems().add(invoiceItem);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Size**********************" + invoiceDocument.getItems().size());
                    }
                }


                //}
            }
            //}
            invoiceDocument.setInvoicePayDate(calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
            invoiceDocument.setTotalDollarAmount(invoiceDocument.getTotalDollarAmount().add(po.getTotalDollarAmount()));

            if (invoiceDocument.getInvoiceTypeHdnId() != null && !invoiceDocument.getInvoiceTypeHdnId().isEmpty()) {
                invoiceDocument.setInvoiceTypeId(Integer.valueOf(invoiceDocument.getInvoiceTypeHdnId()));
            }
            if (invoiceDocument.getPaymentMethodIdentifier() != null  && !invoiceDocument.getPaymentMethodIdentifier().isEmpty()) {
                invoiceDocument.setPaymentMethodId(Integer.valueOf(invoiceDocument.getPaymentMethodIdentifier()));
            }
            if (invoiceDocument.getInvoiceSubTypeHdnId() != null && !invoiceDocument.getInvoiceSubTypeHdnId().isEmpty()) {
                invoiceDocument.setInvoiceSubTypeId(Integer.valueOf(invoiceDocument.getInvoiceSubTypeHdnId()));
            }

            if (invoiceDocument.getInvoiceAmount() != null && !invoiceDocument.getInvoiceAmount().isEmpty()) {
                invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getInvoiceAmount()));
            }

            if (invoiceDocument.getForeignInvoiceAmount() != null && !invoiceDocument.getForeignInvoiceAmount().isEmpty()) {
                invoiceDocument.setForeignVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignInvoiceAmount()).bigDecimalValue());
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                    if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyExchangeRate())) {
                        try {
                            Double.parseDouble(invoiceDocument.getInvoiceCurrencyExchangeRate());
                            invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(new BigDecimal(invoiceDocument.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)));
                        }
                        catch (NumberFormatException nfe) {
                            throw new RuntimeException("Invalid Exchange Rate", nfe);
                        }
                    }   else {
                        BigDecimal exchangeRate = getExchangeRate(invoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                        invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(exchangeRate, 4, RoundingMode.HALF_UP)));
                    }
                }
            }

            Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
            if (defaultBank != null) {
                invoiceDocument.setBankCode(defaultBank.getBankCode());
                invoiceDocument.setBank(defaultBank);
            }
        }


        invoiceDocument.setInvoicePayDate(this.calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));


         /* invoice total */

        BigDecimal addChargeItem=BigDecimal.ZERO;
        List<OleInvoiceItem> item = invoiceDocument.getItems();
        for(OleInvoiceItem invoiceditem : item){
            if(invoiceditem.getItemType().isAdditionalChargeIndicator() && invoiceditem.getExtendedPrice()!=null){
                addChargeItem =addChargeItem.add(invoiceditem.getExtendedPrice().bigDecimalValue());
            }
        }
        if (invoiceDocument.getTotalDollarAmount() != null ) {
            invoiceDocument.setInvoiceItemTotal(invoiceDocument.getTotalDollarAmount().subtract(new KualiDecimal(addChargeItem)).toString());
            invoiceDocument.setDocumentTotalAmount(invoiceDocument.getTotalDollarAmount().toString());
        }
        /* invoice total */

        // add missing below the line
        //getPurapService().addBelowLineItems(invoiceDocument);
        //invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

        //fix up below the line items
        //getInvoiceService().removeIneligibleAdditionalCharges(invoiceDocument);
        LOG.debug("Leaving populateInvoiceFromPurchaseOrders method ");
        return invoiceDocument;
    }

    public String saveInvoiceDocument(OleInvoiceDocument invoiceDocument) throws WorkflowException {
        /*invoiceDocument = populateInvoiceFromPurchaseOrders(invoiceDocument, null);*/
        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
        invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());

        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);
        }
        Person currentUser = GlobalVariables.getUserSession().getPerson();
        invoiceDocument.setAccountsPayableProcessorIdentifier(currentUser.getPrincipalId());
        invoiceDocument.setProcessingCampusCode(currentUser.getCampusCode());
        DocumentService documentService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.DOCUMENT_HEADER_SERVICE);
        invoiceDocument = (OleInvoiceDocument) documentService.saveDocument(invoiceDocument);


        return invoiceDocument.getDocumentNumber();

    }

    public String routeInvoiceDocument(OleInvoiceDocument invoiceDocument) throws WorkflowException {
        // invoiceDocument = populateInvoiceFromPurchaseOrders(invoiceDocument,null);
        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
        invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());

        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);
        }
        Person currentUser = GlobalVariables.getUserSession().getPerson();
        invoiceDocument.setAccountsPayableProcessorIdentifier(currentUser.getPrincipalId());
        invoiceDocument.setProcessingCampusCode(currentUser.getCampusCode());

        DocumentService documentService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.DOCUMENT_HEADER_SERVICE);
        invoiceDocument = (OleInvoiceDocument) documentService.routeDocument(invoiceDocument, null, null);


        return invoiceDocument.getDocumentNumber();

    }


    public OleInvoiceDocument populateVendorDetail(String vendorNumber, OleInvoiceDocument oleInvoiceDocument) {
        Map<String, String> criteria = new HashMap<>();
        String[] vendorIds = vendorNumber != null ? vendorNumber.split("-") : new String[0];
        criteria.put(OLEConstants.InvoiceDocument.VENDOR_HEADER_IDENTIFIER, vendorIds.length > 0 ? vendorIds[0] : "");
        criteria.put(OLEConstants.InvoiceDocument.VENDOR_DETAIL_IDENTIFIER, vendorIds.length > 1 ? vendorIds[1] : "");
        VendorDetail vendorDetail = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, criteria);
        if (vendorDetail != null) {
            oleInvoiceDocument.setVendorDetail(vendorDetail);
            oleInvoiceDocument.setVendorName(vendorDetail.getVendorName());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorNumber(vendorDetail.getVendorNumber());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorFaxNumber(vendorDetail.getDefaultFaxNumber());
            //oleInvoiceDocument.
            if (vendorDetail.getPaymentMethodId() != null) {
                oleInvoiceDocument.setPaymentMethodIdentifier(vendorDetail.getPaymentMethodId().toString());
                oleInvoiceDocument.setPaymentMethodId(vendorDetail.getPaymentMethodId());
            }

            if (vendorDetail.getVendorPaymentTerms() != null) {
                oleInvoiceDocument.setVendorPaymentTerms(vendorDetail.getVendorPaymentTerms());
                oleInvoiceDocument.setVendorPaymentTermsCode(vendorDetail.getVendorPaymentTerms().getVendorPaymentTermsCode());

            }
            if (vendorDetail.getVendorShippingTitle() != null) {
                oleInvoiceDocument.setVendorShippingTitleCode(vendorDetail.getVendorShippingTitle().getVendorShippingTitleCode());
            }
            if (vendorDetail.getVendorShippingPaymentTerms() != null) {
                oleInvoiceDocument.setVendorShippingPaymentTerms(vendorDetail.getVendorShippingPaymentTerms());
            }

            for (VendorAddress vendorAddress : vendorDetail.getVendorAddresses()) {
                if (vendorAddress.isVendorDefaultAddressIndicator()) {
                    oleInvoiceDocument.setVendorCityName(vendorAddress.getVendorCityName());
                    oleInvoiceDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
                    oleInvoiceDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
                    oleInvoiceDocument.setVendorAttentionName(vendorAddress.getVendorAttentionName());
                    oleInvoiceDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
                    oleInvoiceDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
                    oleInvoiceDocument.setVendorAttentionName(vendorAddress.getVendorAttentionName());
                    oleInvoiceDocument.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
                    oleInvoiceDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
                    oleInvoiceDocument.setVendorCountry(vendorAddress.getVendorCountry());
                    //oleInvoiceDocument.setNoteLine1Text(vendorAddress.getNoteLine2Text
                }
            }
        }

        return oleInvoiceDocument;
    }


    public void createCreditMemoDocument(OleInvoiceDocument invoiceDocument, List<OleInvoiceItem> items,boolean flag) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating Payment Request document");
        }

        KNSGlobalVariables.getMessageList().clear();

        //   validateInvoiceOrderValidForPREQCreation(inv);

        if (LOG.isDebugEnabled()) {
            if (invoiceDocument.isInvoiceCancelIndicator()) {
                LOG.debug("Not possible to convert cancelled Invoice details into payment request");
            } else {
                LOG.debug("Payment request document creation validation succeeded");
            }
        }

        if (invoiceDocument.isInvoiceCancelIndicator()) {
            LOG.debug("Invoice Cancelled, Payment Request not created");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Total Dollar Amount above line items >>>>" + invoiceDocument.getTotalDollarAmountAboveLineItems());
        }
        Integer invId = invoiceDocument.getPurapDocumentIdentifier();
        Integer poId = 0;
        List<Integer> poList = new ArrayList();

        List<OleInvoiceItem> oleInvoiceItemList = items;
        KualiDecimal invTotalAmount = new KualiDecimal(0);
        KualiDecimal invItemCount = new KualiDecimal(0);
        invTotalAmount = invoiceDocument.getTotalDollarAmountAboveLineItems();
        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItemList) {
            if (oleInvoiceItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                invItemCount = invItemCount.add(oleInvoiceItem.getItemQuantity());
            }
            if (!(poList.contains(oleInvoiceItem.getPurchaseOrderIdentifier()))) {
                poList.add(oleInvoiceItem.getPurchaseOrderIdentifier());
            }
        }

        String prorateBy = invoiceDocument.getProrateBy();
        OleVendorCreditMemoDocument vendorCreditMemoDocument = null;

        Integer invoicePoId = 0;
        for (Integer purchaseOrderId : poList) {
            if (purchaseOrderId!=null && invoicePoId.compareTo(purchaseOrderId) != 0) {

                try {
                    //GlobalVariables.setUserSession(new UserSession("ole-khuntley"));
                    String user = null;
                    if (GlobalVariables.getUserSession() == null) {
                        kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                        user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                        GlobalVariables.setUserSession(new UserSession(user));
                    }

                    vendorCreditMemoDocument = (OleVendorCreditMemoDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_CM");
                    //  PaymentRequestDocument  preqDoc = new PaymentRequestDocument();
                    //vendorCreditMemoDocument.setCreditMemoDate(invoiceDocument.getInvoiceDate());
                    vendorCreditMemoDocument.setCreditMemoNumber(invoiceDocument.getInvoiceNumber());
                    //vendorCreditMemoDocument.setVendorInvoiceAmount(invoiceDocument.getVendorInvoiceAmount());
                    vendorCreditMemoDocument.setVendorDetail(invoiceDocument.getVendorDetail());
                    vendorCreditMemoDocument.setVendorName(invoiceDocument.getVendorName());
                    vendorCreditMemoDocument.setVendorHeaderGeneratedIdentifier(invoiceDocument.getVendorHeaderGeneratedIdentifier());
                    vendorCreditMemoDocument.setVendorDetailAssignedIdentifier(invoiceDocument.getVendorDetailAssignedIdentifier());
                    vendorCreditMemoDocument.setVendorNumber(invoiceDocument.getVendorNumber());
                    vendorCreditMemoDocument.setVendorHeaderGeneratedIdentifier(invoiceDocument.getVendorHeaderGeneratedIdentifier());
                    vendorCreditMemoDocument.setVendorDetailAssignedIdentifier(invoiceDocument.getVendorDetailAssignedIdentifier());
                    //vendorCreditMemoDocument.setVendorFaxNumber(invoiceDocument.getDefaultFaxNumber());
                    //oleInvoiceDocument.
                   /* vendorCreditMemoDocument.setVendorPaymentTerms(invoiceDocument.getVendorPaymentTerms());
                    vendorCreditMemoDocument.setVendorPaymentTermsCode(invoiceDocument.getVendorPaymentTerms().getVendorPaymentTermsCode());
                    vendorCreditMemoDocument.setVendorShippingTitleCode(invoiceDocument.getVendorShippingTitleCode());
                    vendorCreditMemoDocument.setVendorShippingPaymentTerms(invoiceDocument.getVendorShippingPaymentTerms());*/
                    vendorCreditMemoDocument.setVendorCityName(invoiceDocument.getVendorCityName());
                    vendorCreditMemoDocument.setVendorLine1Address(invoiceDocument.getVendorLine1Address());
                    vendorCreditMemoDocument.setVendorLine2Address(invoiceDocument.getVendorLine2Address());
                    vendorCreditMemoDocument.setVendorAttentionName(invoiceDocument.getVendorAttentionName());
                    vendorCreditMemoDocument.setVendorPostalCode(invoiceDocument.getVendorPostalCode());
                    vendorCreditMemoDocument.setVendorStateCode(invoiceDocument.getVendorStateCode());
                    vendorCreditMemoDocument.setVendorAttentionName(invoiceDocument.getVendorAttentionName());
                    vendorCreditMemoDocument.setVendorAddressInternationalProvinceName(invoiceDocument.getVendorAddressInternationalProvinceName());
                    vendorCreditMemoDocument.setVendorCountryCode(invoiceDocument.getVendorCountryCode());
                    vendorCreditMemoDocument.setVendorCountry(invoiceDocument.getVendorCountry());
                    vendorCreditMemoDocument.setVendorCustomerNumber(invoiceDocument.getVendorCustomerNumber());
                    vendorCreditMemoDocument.setAccountsPayableProcessorIdentifier(invoiceDocument.getAccountsPayableProcessorIdentifier());
                    vendorCreditMemoDocument.setProcessingCampusCode(invoiceDocument.getProcessingCampusCode());
                    vendorCreditMemoDocument.setPurchaseOrderIdentifier(purchaseOrderId);
                    // vendorCreditMemoDocument.setReopenPurchaseOrderIndicator(oleInvoiceItem.isReopenPurchaseOrderIndicator());
                    // vendorCreditMemoDocument.setClosePurchaseOrderIndicator(oleInvoiceItem.isClosePurchaseOrderIndicator());
                    vendorCreditMemoDocument.setCreditMemoDate(invoiceDocument.getInvoiceDate());
                    //  vendorCreditMemoDocument.setImmediatePaymentIndicator(invoiceDocument.getImmediatePaymentIndicator());
                    // vendorCreditMemoDocument.setPaymentRequestCostSource(invoiceDocument.getInvoiceCostSource());
                    //   LOG.info("invoiceDocument.getPaymentMethod().getPaymentMethodId() >>>>>>>>>" + invoiceDocument.getPaymentMethod().getPaymentMethodId());

                    // vendorCreditMemoDocument.setGrandTotal(invoiceDocument.getTotalDollarAmount());
                    vendorCreditMemoDocument.setVendorCustomerNumber(invoiceDocument.getVendorCustomerNumber());
                    vendorCreditMemoDocument.setAccountsPayableProcessorIdentifier(invoiceDocument.getAccountsPayableProcessorIdentifier());
                    vendorCreditMemoDocument.setProcessingCampusCode(invoiceDocument.getProcessingCampusCode());
                    vendorCreditMemoDocument.setPurchaseOrderIdentifier(purchaseOrderId);
                    vendorCreditMemoDocument.setProrateBy(invoiceDocument.getProrateBy());
                    vendorCreditMemoDocument.setProrateDollar(invoiceDocument.isProrateDollar());
                    vendorCreditMemoDocument.setProrateQty(invoiceDocument.isProrateQty());
                    vendorCreditMemoDocument.setProrateManual(invoiceDocument.isProrateManual());
                    vendorCreditMemoDocument.setNoProrate(invoiceDocument.isNoProrate());


                    if (invoiceDocument.getPaymentMethodId() != null) {
                        OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(OlePaymentMethod.class, invoiceDocument.getPaymentMethodId());
                        vendorCreditMemoDocument.setOlePaymentMethod(olePaymentMethod);
                        //vendorCreditMemoDocument.getPaymentMethod().setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
                        vendorCreditMemoDocument.setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
                    }

                    vendorCreditMemoDocument.setInvoiceIdentifier(invoiceDocument.getPurapDocumentIdentifier());
                   /* if (invoiceDocument.getAccountsPayablePurchasingDocumentLinkIdentifier() != null) {
                        vendorCreditMemoDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(invoiceDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
                    } */
                    vendorCreditMemoDocument.setBankCode(invoiceDocument.getBankCode());
                    vendorCreditMemoDocument.setBank(invoiceDocument.getBank());
                    //vendorCreditMemoDocument.setProcessingCampusCode(invoiceDocument.getProcessingCampusCode());
                } catch (WorkflowException e) {
                    String extraDescription = "Error=" + e.getMessage();
                    LOG.error("Exception creating Payment request document - " + e.getMessage());
                }

                Map invItemMap = new HashMap();
                invItemMap.put(PurapConstants.PRQSDocumentsStrings.PUR_ID, invoiceDocument.getPurapDocumentIdentifier());
                invItemMap.put(PurapConstants.PRQSDocumentsStrings.PO_ID, purchaseOrderId);
                List<OleInvoiceItem> invoiceItems = (List<OleInvoiceItem>) businessObjectService.findMatchingOrderBy(OleInvoiceItem.class, invItemMap, PurapConstants.PRQSDocumentsStrings.PO_ID, true);
                List<OleInvoiceItem> invoiceItemList = new ArrayList();
                if(invoiceDocument.getProrateBy() != null && (invoiceDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || invoiceDocument.getProrateBy().equalsIgnoreCase(OLEConstants.PRORATE_BY_DOLLAR) || invoiceDocument.getProrateBy().equalsIgnoreCase(OLEConstants.MANUAL_PRORATE))) {
                    for(OleInvoiceItem item : (List<OleInvoiceItem>)invoiceDocument.getItems()) {
                        if(item.getPurchaseOrderIdentifier().intValue() == invoiceItems.get(0).getPurchaseOrderIdentifier().intValue()) {
                            invoiceItemList.add(item);
                        }
                    }
                } else {
                    invoiceItemList = invoiceItems;
                }
                KualiDecimal itemCount = new KualiDecimal(0);
                KualiDecimal itemPrice = new KualiDecimal(0);
                PurchaseOrderDocument poDoc = invoiceDocument.getPurchaseOrderDocument(purchaseOrderId);
                if (poDoc == null) {
                    throw new RuntimeException("Purchase Order document " + purchaseOrderId + " does not exist in the system");
                }
                if (vendorCreditMemoDocument.getDocumentHeader() != null) {
                    vendorCreditMemoDocument.getDocumentHeader().setDocumentDescription(createPreqDocumentDescription(poDoc.getPurapDocumentIdentifier(), invoiceDocument.getVendorName()));
                }

                try {
                    vendorCreditMemoDocument.updateAndSaveAppDocStatus(PurapConstants.PaymentRequestStatuses.APPDOC_IN_PROCESS);
                } catch (WorkflowException we) {
                    throw new RuntimeException("Unable to save route status data for document: " + vendorCreditMemoDocument.getDocumentNumber(), we);
                }
                List<OleCreditMemoItem> creditMemoItems = new ArrayList<>();

                HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = SpringContext.getBean(AccountsPayableService.class).expiredOrClosedAccountsList(poDoc);
                //int itemLineNumberCount = 0;
                for (OleInvoiceItem invoiceItem : invoiceItemList) {
                    if ((flag || !invoiceItem.isDebitItem()) && invoiceItem.getExtendedPrice().isNonZero()) {

                        OleCreditMemoItem creditMemoItem = new OleCreditMemoItem(invoiceItem, vendorCreditMemoDocument, expiredOrClosedAccountList);
                        if(flag && invoiceItem.isDebitItem()){
                            creditMemoItem.setItemUnitPrice(creditMemoItem.getItemUnitPrice().negate());
                            creditMemoItem.setExtendedPrice(creditMemoItem.getExtendedPrice().negated());
                            for(PurApAccountingLine purApAccountingLine : creditMemoItem.getSourceAccountingLines()){
                                purApAccountingLine.setAmount(purApAccountingLine.getAmount().negated());
                            }
                        }
                        creditMemoItems.add(creditMemoItem);
                        if (vendorCreditMemoDocument.getAccountsPayablePurchasingDocumentLinkIdentifier() == null) {
                            vendorCreditMemoDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(invoiceItem.getAccountsPayablePurchasingDocumentLinkIdentifier());
                        }
                    }

                }
                vendorCreditMemoDocument.setItems(creditMemoItems);
                vendorCreditMemoDocument.setCreditMemoAmount(vendorCreditMemoDocument.getTotalDollarAmount());
                SpringContext.getBean(OleCreditMemoService.class).calculateCreditMemo(vendorCreditMemoDocument);

                SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(vendorCreditMemoDocument));

                if (expiredOrClosedAccountList == null) {
                    expiredOrClosedAccountList = new HashMap();
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug(expiredOrClosedAccountList.size() + " accounts has been found as Expired or Closed");
                }

                invoicePoId = purchaseOrderId;


                SpringContext.getBean(CreditMemoService.class).populateAndSaveCreditMemo(vendorCreditMemoDocument);

                SpringContext.getBean(OleCreditMemoService.class).autoApproveCreditMemo(vendorCreditMemoDocument);
            }
        }
    }

    @Override
    public OleInvoiceDocument getInvoiceDocumentById(Integer invoiceIdentifier) {
        OleInvoiceDocument invoiceDocument = invoiceDao.getDocumentByInvoiceId(invoiceIdentifier);
        return invoiceDocument;
    }


    public boolean autoApprovePaymentRequest(OleInvoiceDocument doc) {
        try {
            // Much of the rice frameworks assumes that document instances that are saved via DocumentService.saveDocument are
            // those
            // that were dynamically created by PojoFormBase (i.e., the Document instance wasn't created from OJB). We need to
            // make
            // a deep copy and materialize collections to fulfill that assumption so that collection elements will delete
            // properly

            // TODO: maybe rewriting PurapService.calculateItemTax could be rewritten so that the a deep copy doesn't need to be
            // made
            // by taking advantage of OJB's managed array lists
            try {
                ObjectUtils.materializeUpdateableCollections(doc);
                for (OleInvoiceItem item : (List<OleInvoiceItem>) doc.getItems()) {
                    ObjectUtils.materializeUpdateableCollections(item);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            doc = (OleInvoiceDocument) ObjectUtils.deepCopy(doc);
            //purapService.updateStatus(doc, PaymentRequestStatuses.AUTO_APPROVED);
            //doc.updateAndSaveAppDocStatus(PurapConstants.PaymentRequestStatuses.APPDOC_INITIATE);

            // documentService.routeDocument(doc, "auto-approving: Document Created from Invoice.", null);
            saveInvoiceDocument(doc);
        } catch (WorkflowException we) {
            LOG.error("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
            // throw a runtime exception up so that we can force a rollback
            throw new RuntimeException("Exception encountered when approving document number " + doc.getDocumentNumber() + ".", we);
        }
        return true;
    }

    public OleInvoiceDocument populateInvoiceDocument (OleInvoiceDocument invoiceDocument) {
        Boolean isItemLevelDebit = null;
        Boolean isAdditionalChargeLevelDebit = null;
        Boolean isItemLevelCredit = null;
        Boolean isAdditionalChargeLevelCredit = null;
        if (invoiceDocument.getPurchaseOrderDocuments() != null && invoiceDocument.getPurchaseOrderDocuments().size() > 0) {
            for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                if (invoiceItem.getItemType().isAdditionalChargeIndicator() && invoiceItem.getItemUnitPrice() != null)
                    invoiceItem.setPurchaseOrderIdentifier(invoiceDocument.getPurchaseOrderDocuments().get(0).getPurapDocumentIdentifier());
            }
        }
        Integer poIdentifier=null;
        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
            if (invoiceItem.isDebitItem()  &&
                    (invoiceItem.getItemListPrice().isNonZero() ||
                            (invoiceItem.getItemUnitPrice()!=null && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO)!=0))){
                if(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()){
                    if(isItemLevelDebit==null){
                        isItemLevelDebit = true;
                    }
                    isItemLevelDebit &= invoiceItem.isDebitItem();
                }
                if(invoiceItem.getItemType().isAdditionalChargeIndicator()){
                    if(isAdditionalChargeLevelDebit==null){
                        isAdditionalChargeLevelDebit = true;
                    }
                    isAdditionalChargeLevelDebit &= invoiceItem.isDebitItem();
                }

            }
            else if(!invoiceItem.isDebitItem() &&
                    (invoiceItem.getItemListPrice().isNonZero() ||
                            (invoiceItem.getItemUnitPrice()!=null && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO)!=0))){
                if(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()){
                    if(isItemLevelCredit==null){
                        isItemLevelCredit = true;
                    }
                    isItemLevelCredit &= !invoiceItem.isDebitItem();
                }
                if(invoiceItem.getItemType().isAdditionalChargeIndicator()){
                    if(isAdditionalChargeLevelCredit==null){
                        isAdditionalChargeLevelCredit = true;
                    }
                    isAdditionalChargeLevelCredit &= !invoiceItem.isDebitItem();
                }
            }
            if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() &&
                    invoiceItem.getItemListPrice().isNonZero() && poIdentifier==null) {
                poIdentifier = invoiceItem.getPurchaseOrderIdentifier();
            }
        }
        boolean flag = (isItemLevelDebit == null && isAdditionalChargeLevelDebit!=null && isAdditionalChargeLevelDebit) ||
                (isAdditionalChargeLevelDebit == null && isItemLevelDebit!=null && isItemLevelDebit) ||
                (isItemLevelCredit == null && isAdditionalChargeLevelCredit!=null && isAdditionalChargeLevelCredit) ||
                (isAdditionalChargeLevelCredit == null && isItemLevelCredit!=null && isItemLevelCredit) &&
                        !(isItemLevelCredit!=null && isItemLevelCredit && isItemLevelDebit!=null && isItemLevelDebit);
        if(!flag){
            Integer poIdentifierForCredit = null;
            Integer poIdentifierForDebit = null;
            if (invoiceDocument.getItems() != null && invoiceDocument.getItems().size() > 0) {
                for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>)invoiceDocument.getItems()) {
                    if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.isDebitItem() &&
                            invoiceItem.getItemListPrice().isNonZero()) {
                        poIdentifierForDebit = invoiceItem.getPurchaseOrderIdentifier();
                        break;
                    }
                }
            }
            if (invoiceDocument.getItems() != null && invoiceDocument.getItems().size() > 0) {
                for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>)invoiceDocument.getItems()) {
                    if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && !invoiceItem.isDebitItem() &&
                            invoiceItem.getItemListPrice().isNonZero()) {
                        poIdentifierForCredit = invoiceItem.getPurchaseOrderIdentifier();
                        break;
                    }
                }
            }

        }else{
            for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                if (invoiceItem.getItemType().isAdditionalChargeIndicator() && invoiceItem.getItemUnitPrice() != null) {
                    invoiceItem.setPurchaseOrderIdentifier(poIdentifier);
                }
            }
        }
        //}
        if (ObjectUtils.isNull(invoiceDocument.getInvoicePayDate()) || invoiceDocument.getInvoicePayDate().before(new Date())) {
            invoiceDocument.setInvoicePayDate(calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
        }

        //invoiceDocument.setTotalDollarAmount(invoiceDocument.getTotalDollarAmount().add(po.getTotalDollarAmount()));

        if (invoiceDocument.getInvoiceTypeHdnId() != null && !invoiceDocument.getInvoiceTypeHdnId().isEmpty()) {
            invoiceDocument.setInvoiceTypeId(Integer.valueOf(invoiceDocument.getInvoiceTypeHdnId()));
        }
        if (invoiceDocument.getPaymentMethodIdentifier() != null  && !invoiceDocument.getPaymentMethodIdentifier().isEmpty()) {
            invoiceDocument.setPaymentMethodId(Integer.valueOf(invoiceDocument.getPaymentMethodIdentifier()));
        }
        if (invoiceDocument.getInvoiceSubTypeHdnId() != null && !invoiceDocument.getInvoiceSubTypeHdnId().isEmpty()) {
            invoiceDocument.setInvoiceSubTypeId(Integer.valueOf(invoiceDocument.getInvoiceSubTypeHdnId()));
        }

        if (invoiceDocument.getInvoiceAmount() != null && !invoiceDocument.getInvoiceAmount().isEmpty()) {
            invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getInvoiceAmount()));
        }

        if (invoiceDocument.getForeignInvoiceAmount() != null && !invoiceDocument.getForeignInvoiceAmount().isEmpty()) {
            invoiceDocument.setForeignVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignInvoiceAmount()).bigDecimalValue());
            if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyExchangeRate())) {
                    try {
                        Double.parseDouble(invoiceDocument.getInvoiceCurrencyExchangeRate());
                    }
                    catch (NumberFormatException nfe) {
                        throw new RuntimeException("Invalid Exchange Rate", nfe);
                    }
                    invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(new BigDecimal(invoiceDocument.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)));
                    invoiceDocument.setInvoiceAmount(invoiceDocument.getVendorInvoiceAmount().toString());
                }   else {
                    BigDecimal exchangeRate = getExchangeRate(invoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                    invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(exchangeRate, 4, RoundingMode.HALF_UP)));
                    invoiceDocument.setInvoiceAmount(invoiceDocument.getVendorInvoiceAmount().toString());
                }

            }
        }

        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);
        }

        if (ObjectUtils.isNull(invoiceDocument.getInvoicePayDate())) {
            invoiceDocument.setInvoicePayDate(this.calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
        }



        /* invoice total */

        BigDecimal addChargeItem=BigDecimal.ZERO;
        List<OleInvoiceItem> item = invoiceDocument.getItems();
        for(OleInvoiceItem invoiceditem : item){
            if(invoiceditem.getItemType().isAdditionalChargeIndicator() && invoiceditem.getExtendedPrice()!=null){
                addChargeItem =addChargeItem.add(invoiceditem.getExtendedPrice().bigDecimalValue());
            }
        }
        if (invoiceDocument.getTotalDollarAmount() != null ) {
            invoiceDocument.setInvoiceItemTotal(invoiceDocument.getTotalDollarAmount().subtract(new KualiDecimal(addChargeItem)).toString());
            invoiceDocument.setDocumentTotalAmount(invoiceDocument.getTotalDollarAmount().toString());
        }
        return invoiceDocument;
    }

    public OleInvoiceDocument populateInvoiceItems (OleInvoiceDocument invoiceDocument, List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList) {
        //int invoiceItemNumberCnt = getLastItemLineNumber(invoiceDocument);

        LOG.debug("Inside populateInvoiceItems method ");
        List<OleInvoiceItem> items = new ArrayList<>();
        Boolean receiveRequired = false;
        HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = new HashMap<>();
        BigDecimal addChargeItem = BigDecimal.ZERO;
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList){
        if (olePurchaseOrderDocument.isReceivingDocumentRequiredIndicator()) {
            receiveRequired = true;
        }
            /* if(this.encumberedItemExistsForInvoicing(po))
            {*/
        for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
            // check to make sure it's eligible for payment (i.e. active and has encumbrance available
            //if (this.poItemEligibleForAp(invoiceDocument, poi)) {
            if (poi.isItemForInvoice()) {
                OleInvoiceItem invoiceItem = new OleInvoiceItem(poi, invoiceDocument, expiredOrClosedAccountList);
                // invoiceItem.setItemLineNumber(++invoiceItemNumberCnt);
                invoiceItem.setClosePurchaseOrderIndicator(olePurchaseOrderDocument.isClosePO());
                invoiceItem.setReopenPurchaseOrderIndicator(olePurchaseOrderDocument.getIsReOpenPO());
                PurchasingCapitalAssetItem purchasingCAMSItem = olePurchaseOrderDocument.getPurchasingCapitalAssetItemByItemIdentifier(poi.getItemIdentifier());
                if (purchasingCAMSItem != null) {
                    invoiceItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                }
                invoiceItem.setUseTaxIndicator(olePurchaseOrderDocument.isUseTaxIndicator());
                invoiceItem.setPurchaseOrderIdentifier(olePurchaseOrderDocument.getPurapDocumentIdentifier());
                invoiceItem.setPostingYear(olePurchaseOrderDocument.getPostingYear());
                invoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(olePurchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
                invoiceItem.setReceivingDocumentRequiredIndicator(olePurchaseOrderDocument.isReceivingDocumentRequiredIndicator());
                if (!poi.getOpenQuantity().equals("0.00")) {
                    BigDecimal copiesOrdered = new BigDecimal(invoiceItem.getOleCopiesOrdered());
                    BigDecimal outStandingQuantity = new BigDecimal(poi.getOpenQuantity());
                    BigDecimal updatedOpenQuantity = outStandingQuantity.subtract(copiesOrdered);
                    invoiceItem.setOleOpenQuantity(String.valueOf(updatedOpenQuantity));
                } else {
                    invoiceItem.setOleOpenQuantity(poi.getOpenQuantity());
                }
                if (invoiceItem.getItemType().isAdditionalChargeIndicator() && invoiceItem.getExtendedPrice() != null) {
                    addChargeItem = addChargeItem.add(invoiceItem.getExtendedPrice().bigDecimalValue());
                }
                // copy usetaxitems over
                invoiceItem.getUseTaxItems().clear();
                for (PurApItemUseTax useTax : poi.getUseTaxItems()) {
                    invoiceItem.getUseTaxItems().add(useTax);
                }
                invoiceItem.setPurchaseOrderEndDate(olePurchaseOrderDocument.getPoEndDate());
                //SpringContext.getBean(PurapAccountingService.class).updateItemAccountAmounts(invoiceItem);
                //   this.calculateAccount(invoiceItem);
                invoiceDocument.getItems().add(invoiceItem);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Size**********************" + invoiceDocument.getItems().size());
                }
            }
        }
        invoiceDocument.setTotalDollarAmount(invoiceDocument.getTotalDollarAmount().add(olePurchaseOrderDocument.getTotalDollarAmount()));
        }
        //  List<OleInvoiceItem> item = invoiceDocument.getItems();
       /* for(OleInvoiceItem invoiceditem : item){
            if(invoiceditem.getItemType().isAdditionalChargeIndicator() && invoiceditem.getExtendedPrice()!=null){
                addChargeItem =addChargeItem.add(invoiceditem.getExtendedPrice().bigDecimalValue());
            }
        }*/
        if (invoiceDocument.getTotalDollarAmount() != null) {
            invoiceDocument.setInvoiceItemTotal(invoiceDocument.getTotalDollarAmount().subtract(new KualiDecimal(addChargeItem)).toString());
            invoiceDocument.setDocumentTotalAmount(invoiceDocument.getTotalDollarAmount().toString());
        }
        invoiceDocument.setDocumentTotalAmount(invoiceDocument.getInvoicedItemTotal());
        // invoiceDocument.setPurchaseOrderDocuments(new ArrayList<OlePurchaseOrderDocument>());
        invoiceDocument.getPurchaseOrderDocuments().removeAll(olePurchaseOrderDocumentList);
        invoiceDocument.setReceivingDocumentRequiredIndicator(receiveRequired);
        return invoiceDocument;
    }

    /**
     * This method calculates the Amount and the Percent in Accounting Line if the Invoiced List Price changed
     */
    public void calculateAccount(PurApItem purapItem) {
        purapItem.setExtendedPrice(purapItem.calculateExtendedPrice());
        List<PurApAccountingLine> purApAccountingLines = purapItem.getSourceAccountingLines();
        BigDecimal totalPercent = BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal sumAmt = BigDecimal.ZERO;
        BigDecimal sumPercentage = BigDecimal.ZERO;
        int counter = 0;
        int accLineSize = purApAccountingLines.size();
        PurApAccountingLine lastAccount = null;
        for (PurApAccountingLine account : purApAccountingLines) {
            if (purapItem.getTotalAmount() != null && !purapItem.getTotalAmount().equals(KualiDecimal.ZERO)) {
                if (account.getAccountLinePercent() != null && (account.getAmount() == null || account.getAmount().equals(KualiDecimal.ZERO))) {
                    BigDecimal percent = account.getAccountLinePercent().divide(new BigDecimal(100));
                    account.setAmount(new KualiDecimal(purapItem.getTotalAmount().bigDecimalValue().multiply(percent)));
                } else if (account.getAmount() != null && account.getAmount().isNonZero() && account.getAccountLinePercent() == null) {
                    KualiDecimal dollar = account.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((purapItem.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
                    account.setAccountLinePercent(dollarToPercent);
                } else if (account.getAmount() != null && account.getAmount().isZero() && account.getAccountLinePercent() == null) {
                    account.setAccountLinePercent(new BigDecimal(0));
                } else if((account.getAmount()!=null&&account.getAccountLinePercent() != null) ||
                        (account.getAmount()!=null&& account.getAccountLinePercent().intValue()== 100)){
                    if (counter == accLineSize - 1) {
                        account.setAccountLinePercent(new BigDecimal(100).subtract(sumPercentage));
                        account.setAmount(purapItem.getTotalAmount().subtract(new KualiDecimal(sumAmt)));
                    } else {
                        BigDecimal percent = account.getAccountLinePercent().divide(new BigDecimal(100));
                        account.setAmount(new KualiDecimal(purapItem.getTotalAmount().bigDecimalValue().multiply(percent)));
                }

                }
                totalPercent = totalPercent.add(account.getAccountLinePercent());
                totalAmt = totalAmt.add(account.getAmount().bigDecimalValue());
                lastAccount = account;
            } else {
                account.setAmount(KualiDecimal.ZERO);
            }
            ++counter;
            sumAmt = sumAmt.add(account.getAmount().bigDecimalValue());
            sumPercentage = sumPercentage.add(account.getAccountLinePercent());
        }
        // If Total Percent or Total Amount mis matches,percentage is divided across accounting lines.
        if(totalPercent.intValue() != 100 ||
                (purapItem.getTotalAmount()!=null && totalAmt.compareTo(purapItem.getTotalAmount().bigDecimalValue())!=0)){

           // for (PurApAccountingLine account : purApAccountingLines) {
              //  if (purapItem.getTotalAmount() != null && !purapItem.getTotalAmount().equals(KualiDecimal.ZERO)) {
                   // BigDecimal percent = BigDecimal.ONE.divide(new BigDecimal(purApAccountingLines.size()), BigDecimal.ROUND_CEILING, BigDecimal.ROUND_HALF_UP);
                   // account.setAmount((purapItem.getTotalAmount().multiply(new KualiDecimal(percent))));

            if(lastAccount != null) {
                lastAccount.setAccountLinePercent(lastAccount.getAccountLinePercent().add(new BigDecimal(100).subtract(totalPercent)));
                lastAccount.setAmount(lastAccount.getAmount().add(new KualiDecimal(purapItem.getTotalAmount().bigDecimalValue().subtract(totalAmt))));
            }
              //  } else {
              //      lastAccount.setAmount(KualiDecimal.ZERO);
              //  }
          //  }
        }

    }

    @Override
    public void convertPOItemToInvoiceItem(OleInvoiceDocument oleInvoiceDocument, OlePurchaseOrderDocument olePurchaseOrderDocument) {
        boolean poItemsSelected = false;

        for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
            if (poi.isItemForInvoice()) {
                poItemsSelected = true;
                break;
            }
        }

        if (poItemsSelected) {
            oleInvoiceDocument = this.populateInvoiceItems(oleInvoiceDocument, Arrays.asList(olePurchaseOrderDocument));
            //   oleInvoiceDocument = this.populateInvoiceDocument(oleInvoiceDocument);
            /*SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(oleInvoiceDocument);*/
        } else {
            oleInvoiceDocument.getPurchaseOrderDocuments().remove(olePurchaseOrderDocument);
            GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_PO_SELECTED);
        }
    }

    /**
     * This method prepares the warning message for the Invoice Document based on the Invoice Amount
     * and the Grand Total
     */
    public String createInvoiceNoMatchQuestionText(OleInvoiceDocument invoiceDocument) {

        String questionText = null;
        StringBuffer questionTextBuffer = new StringBuffer("");
        if (invoiceDocument.getInvoiceAmount() != null && invoiceDocument.getInvoicedGrandTotal() != null ) {
            KualiDecimal invoiceAmount = new KualiDecimal(invoiceDocument.getInvoiceAmount());
            KualiDecimal invoiceGrandTotal = new KualiDecimal(invoiceDocument.getInvoicedGrandTotal());
            if(!invoiceAmount.equals(invoiceGrandTotal)) {
                questionText = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.AP_QUESTION_CONFIRM_INVOICE_MISMATCH);
                questionText = StringUtils.replace(questionText, "{0}", "");
                //String questionText = super.createInvoiceNoMatchQuestionText(invoiceDocument);

                CurrencyFormatter cf = new CurrencyFormatter();
                //PaymentRequestDocument preq = (PaymentRequestDocument) invoiceDocument;


                questionTextBuffer.append(questionText);
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                    String currencyType = getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
                    if (StringUtils.isNotBlank(currencyType)) {
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME) && invoiceDocument.getForeignVendorInvoiceAmount() != null) {
                            if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                try {
                                    Double.parseDouble(invoiceDocument.getInvoiceCurrencyExchangeRate());
                                }
                                catch (NumberFormatException nfe) {
                                    throw new RuntimeException("Invalid Exchange Rate", nfe);
                                }
                                invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(new BigDecimal(invoiceDocument.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)));
                            }   else {
                                BigDecimal exchangeRate = getExchangeRate(invoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                                invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount().divide(exchangeRate, 4, RoundingMode.HALF_UP)));
                            }
                        }
                    }

                }
                questionTextBuffer.append("[br][br][b]Summary Detail Below:[b][br][br][table questionTable]");
                questionTextBuffer.append("[tr][td leftTd]Vendor Invoice Amount :[/td][td rightTd]" + (String) cf.format(invoiceDocument.getVendorInvoiceAmount()) + "[/td][/tr]");
                questionTextBuffer.append("[tr][td leftTd]Invoice Total Prior to Additional Charges:[/td][td rightTd]" + (String) cf.format(new KualiDecimal(invoiceDocument.getInvoicedItemTotal())) + "[/td][/tr]");


                //only add this line if payment request has a discount
                if (invoiceDocument.isDiscount()) {
                    questionTextBuffer.append("[tr][td leftTd]Total Before Discount:[/td][td rightTd]" + (String) cf.format(invoiceDocument.getGrandPreTaxTotalExcludingDiscount()) + "[/td][/tr]");
                }

                //if sales tax is enabled, show additional summary lines
                boolean salesTaxInd = SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.ENABLE_SALES_TAX_IND);
                if (salesTaxInd) {
                    questionTextBuffer.append("[tr][td leftTd]Grand Total Prior to Tax:[/td][td rightTd]" + (String) cf.format(invoiceDocument.getGrandPreTaxTotal()) + "[/td][/tr]");
                    questionTextBuffer.append("[tr][td leftTd]Grand Total Tax:[/td][td rightTd]" + (String) cf.format(invoiceDocument.getGrandTaxAmount()) + "[/td][/tr]");
                }

                questionTextBuffer.append("[tr][td leftTd]Grand Total:[/td][td rightTd]" + (String) cf.format(new KualiDecimal(invoiceDocument.getInvoicedGrandTotal())) + "[/td][/tr]");

                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                    String currencyType = getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
                    if (StringUtils.isNotBlank(currencyType)) {
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            questionTextBuffer.append("[tr][td] [/td][/tr]");
                            questionTextBuffer.append("[tr][td leftTd]Foreign Vendor Invoice Amount :[/td][td rightTd]" + (String) cf.format(new KualiDecimal(invoiceDocument.getForeignVendorInvoiceAmount())) + "[/td][/tr]");
                            questionTextBuffer.append("[tr][td leftTd]Foreign Invoice Total Prior to Additional Charges:[/td][td rightTd]" + (String) cf.format(new KualiDecimal(invoiceDocument.getInvoicedForeignItemTotal())) + "[/td][/tr]");
                            questionTextBuffer.append("[tr][td leftTd]Foreign Grand Total:[/td][td rightTd]" + (String) cf.format(new KualiDecimal(invoiceDocument.getInvoicedForeignGrandTotal())) + "[/td][/tr]");
                        }
                    }
                }
                questionTextBuffer.append("[/table]");
            }
        }

        return questionTextBuffer.toString();

    }


    /**
     * This method prepares the warning message for the Invoice Document based on the Invoice Amount
     * and the Grand Total
     */
    public String createSubscriptionDateOverlapQuestionText(OleInvoiceDocument invoiceDocument) {
        boolean isOverlap = false;
        List<OleInvoiceDocument> overlapInvDocumentList = new ArrayList<OleInvoiceDocument>();
        List<OleInvoiceItem> invItems = (List<OleInvoiceItem>) invoiceDocument.getItems();
        List<OleInvoiceItem> subscriptionInvItems = new ArrayList<OleInvoiceItem>();
        List<OleInvoiceItem> subscriptionInvoicedItems = new ArrayList<OleInvoiceItem>();

        for (OleInvoiceItem invoiceItem : invItems) {
            if (invoiceItem.isSubscriptionOverlap()) {
                subscriptionInvItems.add(invoiceItem);
            }
        }
        for (OleInvoiceItem subscriptionInvItem : subscriptionInvItems) {
            if (subscriptionInvItem.getPoItemIdentifier() != null) {
                Map matchPOItem = new HashMap();
                matchPOItem.put("poItemIdentifier", subscriptionInvItem.getPoItemIdentifier());
                List<OleInvoiceItem> itemList = (List<OleInvoiceItem>) getBusinessObjectService().findMatching(OleInvoiceItem.class, matchPOItem);

                for (OleInvoiceItem invoicedItem : itemList) {
                    if (invoicedItem.getSubscriptionFromDate() != null && invoicedItem.getSubscriptionToDate() != null) {
                        subscriptionInvoicedItems.add(invoicedItem);
                    }
                }

                for (OleInvoiceItem oleInvoiceItem : subscriptionInvoicedItems) {
                    if(!oleInvoiceItem.getItemIdentifier().equals(subscriptionInvItem.getItemIdentifier())) {

                        if (oleInvoiceItem.getPoItemIdentifier().equals(subscriptionInvItem.getPoItemIdentifier())) {

                            if (subscriptionInvItem.getSubscriptionFromDate().compareTo(oleInvoiceItem.getSubscriptionFromDate()) >= 0 && subscriptionInvItem.getSubscriptionFromDate().compareTo(oleInvoiceItem.getSubscriptionToDate()) <= 0) {
                                isOverlap = true;
                                overlapInvDocumentList.add((OleInvoiceDocument) oleInvoiceItem.getInvoiceDocument());
                            } else if (subscriptionInvItem.getSubscriptionToDate().compareTo(oleInvoiceItem.getSubscriptionFromDate()) >= 0 && subscriptionInvItem.getSubscriptionToDate().compareTo(oleInvoiceItem.getSubscriptionToDate()) <= 0) {
                                isOverlap = true;
                                overlapInvDocumentList.add((OleInvoiceDocument) oleInvoiceItem.getInvoiceDocument());
                            } else if ((subscriptionInvItem.getSubscriptionFromDate().compareTo(oleInvoiceItem.getSubscriptionFromDate()) < 0) && (subscriptionInvItem.getSubscriptionToDate().compareTo(oleInvoiceItem.getSubscriptionToDate()) > 0)) {
                                isOverlap = true;
                                overlapInvDocumentList.add((OleInvoiceDocument) oleInvoiceItem.getInvoiceDocument());
                            }
                        }
                    }
                }
            }
        }
        String questionText = null;
        StringBuffer questionTextBuffer = new StringBuffer("");
        if (isOverlap) {
            questionText = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.AP_QUESTION_CONFIRM_INVOICE_SUBSCRIPTION_DATE_OVERLAP);
            questionText = StringUtils.replace(questionText, "{0}", "");
            //String questionText = super.createInvoiceNoMatchQuestionText(invoiceDocument);


            questionTextBuffer.append(questionText);

            questionTextBuffer.append("[br][br][b]Summary Detail Below:[b][br][br][table questionTable]");
            questionTextBuffer.append("[tr][td leftTd]Following Invoices Subscription Date Overlap with current Invoice :[/td][td rightTd] [/td][/tr]");
            questionTextBuffer.append("[tr][td leftTd][/td][td rightTd]");

            for (OleInvoiceDocument overlapInvDocument : overlapInvDocumentList) {
                questionTextBuffer.append(overlapInvDocument.getDocumentNumber() + "    ");
            }
            questionTextBuffer.append("[/td][/tr][/table]");
        }

        overlapInvDocumentList.clear();
        subscriptionInvItems.clear();
        return questionTextBuffer.toString();

    }


   /* public Integer getLastItemLineNumber(OleInvoiceDocument invoiceDocument){
        int  lastItemLineNumber = 0;
        if(invoiceDocument.getItems() != null){
            for(InvoiceItem item : (List<OleInvoiceItem>)invoiceDocument.getItems()) {
                 if(item.getItemTypeCode().equals("ITEM")){
                     lastItemLineNumber++;
                 }
            }
        }
        return lastItemLineNumber;
    }*/

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    public String[] getCollapseSections() {
        LOG.debug("Inside getCollapseSections()");
        String[] collapseSections = new String[]{};
        try {
            collapseSections = parameterService.getParameterValuesAsString(Class.forName(PurapConstants.PURAP_DETAIL_TYPE_CODE_MAP.get(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT)),
                    OLEConstants.INVOICE_COLLAPSE_SECTIONS_ON_PO_ADD).toArray(new String[]{});
        }
        catch (Exception e) {
            LOG.error("Exception while getting the default Collapse section on Invoice Document"+e);
            throw new RuntimeException(e);
        }
        LOG.debug("Leaving getCollapseSections()");
        return collapseSections;
    }

    public String[] getDefaultCollapseSections() {
        LOG.debug("Inside getDefaultCollapseSections()");
        String[] collapseSections = new String[]{};
        try {
            collapseSections = parameterService.getParameterValuesAsString(Class.forName(PurapConstants.PURAP_DETAIL_TYPE_CODE_MAP.get(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT)),
                    OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
        }
        catch (Exception e) {
            LOG.error("Exception while getting the default Collapse section on Invoice Document"+e);
            throw new RuntimeException(e);
        }
        LOG.debug("Leaving getDefaultCollapseSections()");
        return collapseSections;
    }

    public boolean canCollapse(String sectionName, String[] collapseSections) {
        LOG.debug("Inside method canCollapse()");
        List<String> sectionLists = Arrays.asList(collapseSections);
        if (sectionLists.contains(sectionName)) {
            return false;
        }
        return true;
    }

    /**
     * This method checks whether duplication occured on the Invoice Document.
     * @param invoiceDocument
     * @return isDuplicationExists
     */

    public boolean isDuplicationExists(OleInvoiceDocument invoiceDocument, OLEInvoiceForm invoiceForm, String actionName) {
        LOG.debug("Inside method isDuplicationExists()");
        boolean isDuplicationExists = false;
        if(invoiceDocument.getInvoiceNumber()!=null && !invoiceDocument.getInvoiceNumber().equalsIgnoreCase("")){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(OLEConstants.InvoiceDocument.INVOICE_NUMBER, invoiceDocument.getInvoiceNumber().toString());
            //map.put(OLEConstants.InvoiceDocument.INVOICE_DATE, invoiceDocument.getInvoiceDate());
            map.put(OLEConstants.InvoiceDocument.VENDOR_GENERATED_IDENTIFIER, invoiceDocument.getVendorHeaderGeneratedIdentifier().toString());
            map.put(OLEConstants.InvoiceDocument.VENDOR_DETAIL_ASSIGNED_GENERATED_IDENTIFIER, invoiceDocument.getVendorDetailAssignedIdentifier().toString());
            List<OleInvoiceDocument> documents = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
            StringBuffer duplicationMessage = new StringBuffer();
            //String msg = ;
            duplicationMessage.append("\n");
            duplicationMessage.append(OleSelectConstant.DUPLICATE_INVOICE + " ");
            if (documents.size() > 0) {
                for (OleInvoiceDocument invDoc : documents) {
                    if (invDoc.getDocumentNumber() != null &&
                            invoiceDocument.getDocumentNumber() != null &&
                            !invDoc.getDocumentNumber().equalsIgnoreCase(invoiceDocument.getDocumentNumber())) {
                        String docNum = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEKeyConstants.LOAD_DUPLICATE_INVOICE);
                        docNum =  StringUtils.replace(docNum, "{0}", invDoc.getDocumentNumber());
                        duplicationMessage.append(docNum + " ");
                        isDuplicationExists = true;
                    }
                }
            }
            if (isDuplicationExists && actionName.equals("save")) {
                duplicationMessage.append(OleSelectConstant.QUES_FOR_DUPLICATE_INVOICE_FOR_SAVE);
                invoiceDocument.setDuplicateFlag(true);
                invoiceForm.setDuplicationMessage(duplicationMessage.toString());
            }
            else if (isDuplicationExists && actionName.equals("route")) {
                duplicationMessage.append(OleSelectConstant.QUES_FOR_DUPLICATE_INVOICE_FOR_SUBMIT);
                invoiceDocument.setDuplicateApproveFlag(true);
                invoiceForm.setDuplicationMessage(duplicationMessage.toString());
            }else if (isDuplicationExists && actionName.equals("approve")) {
                duplicationMessage.append(OleSelectConstant.QUES_FOR_DUPLICATE_INVOICE_FOR_APPROVE);
                invoiceDocument.setDuplicateApproveFlag(true);
                invoiceForm.setDuplicationApproveMessage(duplicationMessage.toString());
            } else {
                invoiceForm.setDuplicationMessage(duplicationMessage.toString());
            }
        }
        LOG.debug("Leaving method isDuplicationExists()");
        return isDuplicationExists;
    }


    public String getPaymentMethodType(String paymentId){
        if(paymentId != null && !(paymentId.equals(""))){
            Map payMap = new HashMap();
            payMap.put("paymentMethodId", paymentId);
            OlePaymentMethod olePaymentMethod = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePaymentMethod.class,payMap);
            if(olePaymentMethod != null){
                return olePaymentMethod.getPaymentMethod();
            }
        }
        return "";
    }

    public boolean validateDepositAccount(OleInvoiceDocument oleInvoiceDocument) {
        boolean valid = true;
        if (getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier()).equals(OLEConstants.DEPOSIT)) {
            for (OleInvoiceItem item : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
                KualiDecimal invoiceAmount = new KualiDecimal(item.getInvoiceListPrice());
                if (invoiceAmount.isLessEqual(KualiDecimal.ZERO) && item.getItemTypeCode().equals("ITEM")) {
                    return false;
                }
            }
        }
        return valid;
    }


    public boolean isNotificationRequired(OleInvoiceDocument oleInvoiceDocument) {
        boolean isAmountExceeds = false;
        List<OleInvoiceEncumbranceNotification> invoiceEncumbranceNotificationList = new ArrayList<>();
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItems) {
            if (oleInvoiceItem.getExtendedPrice() != null && oleInvoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                KualiDecimal extendedCost = oleInvoiceItem.getExtendedPrice();
                List<PurApAccountingLine> sourceAccountingLines = oleInvoiceItem.getSourceAccountingLines();
                for (PurApAccountingLine accLine : sourceAccountingLines) {
                    Map<String, Object> key = new HashMap<String, Object>();
                    String chartCode = accLine.getChartOfAccountsCode();
                    String accNo = accLine.getAccountNumber();
                    key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                    key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                    Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                            Account.class, key);
                    if (account != null) {
                        KualiDecimal thresholdAmount = null;
                        KualiDecimal thresholdPercentLimit = null;
                        KualiDecimal thresholdPercentage = null;
                        KualiDecimal purchaseOrderAmount = KualiDecimal.ZERO;
                        if (oleInvoiceItem.getPurchaseOrderItemUnitPrice() != null) {
                            purchaseOrderAmount = new KualiDecimal(oleInvoiceItem.getPurchaseOrderItemUnitPrice());
                        }
                        if (account.getThresholdAmount() != null && oleInvoiceItem.getExtendedPrice() != null) {
                            thresholdAmount = account.getThresholdAmount();
                            thresholdAmount = thresholdAmount.add(purchaseOrderAmount);
                        }
                        if (account.getThresholdPercentage() != null && oleInvoiceItem.getExtendedPrice() != null) {
                            thresholdPercentLimit = calculateThresholdAmount(account.getThresholdPercentage(), purchaseOrderAmount);
                            thresholdPercentLimit = thresholdPercentLimit.add(purchaseOrderAmount);
                        }
                        if (thresholdAmount != null && thresholdPercentLimit != null) {
                            if (extendedCost.isGreaterThan(thresholdAmount) && extendedCost.isGreaterThan(thresholdPercentLimit)) {
                                OleInvoiceEncumbranceNotification invoiceEncumbranceNotification = new OleInvoiceEncumbranceNotification();
                                invoiceEncumbranceNotification.setItemTitle(oleInvoiceItem.getItemDescription());
                                invoiceEncumbranceNotification.setPurchaseOrderIdentifier(oleInvoiceItem.getPurchaseOrderIdentifier().toString());
                                invoiceEncumbranceNotification.setPurchaseOrderAmount(purchaseOrderAmount.toString());
                                invoiceEncumbranceNotification.setInvoiceAmount(oleInvoiceItem.getExtendedPrice().toString());
                                invoiceEncumbranceNotification.setDifferenceByThresholdAmount((extendedCost.subtract(purchaseOrderAmount)).toString());
                                invoiceEncumbranceNotificationList.add(invoiceEncumbranceNotification);
                            }
                        } else if (thresholdAmount != null) {
                            if (extendedCost.isGreaterThan(thresholdAmount)) {
                                OleInvoiceEncumbranceNotification invoiceEncumbranceNotification = new OleInvoiceEncumbranceNotification();
                                invoiceEncumbranceNotification.setItemTitle(oleInvoiceItem.getItemDescription());
                                invoiceEncumbranceNotification.setPurchaseOrderIdentifier(oleInvoiceItem.getPurchaseOrderIdentifier().toString());
                                invoiceEncumbranceNotification.setPurchaseOrderAmount(purchaseOrderAmount.toString());
                                invoiceEncumbranceNotification.setInvoiceAmount(oleInvoiceItem.getExtendedPrice().toString());
                                invoiceEncumbranceNotification.setDifferenceByThresholdAmount((extendedCost.subtract(purchaseOrderAmount)).toString());
                                invoiceEncumbranceNotificationList.add(invoiceEncumbranceNotification);
                            }
                        } else if (thresholdPercentLimit != null) {
                            if (extendedCost.isGreaterThan(thresholdPercentLimit)) {
                                OleInvoiceEncumbranceNotification invoiceEncumbranceNotification = new OleInvoiceEncumbranceNotification();
                                invoiceEncumbranceNotification.setItemTitle(oleInvoiceItem.getItemDescription());
                                invoiceEncumbranceNotification.setPurchaseOrderIdentifier(oleInvoiceItem.getPurchaseOrderIdentifier().toString());
                                invoiceEncumbranceNotification.setPurchaseOrderAmount(purchaseOrderAmount.toString());
                                invoiceEncumbranceNotification.setInvoiceAmount(oleInvoiceItem.getExtendedPrice().toString());
                                invoiceEncumbranceNotification.setDifferenceByThresholdAmount((extendedCost.subtract(purchaseOrderAmount)).toString());
                                invoiceEncumbranceNotificationList.add(invoiceEncumbranceNotification);
                            }
                        }
                    }
                }
            }
        }
        if (invoiceEncumbranceNotificationList != null && invoiceEncumbranceNotificationList.size() > 0) {
            this.invoiceEncumbranceNotificationList = invoiceEncumbranceNotificationList;
            return true;
        }
        oleInvoiceDocument.setAmountExceeds(isAmountExceeds);
        return isAmountExceeds;
    }


    public KualiDecimal calculateThresholdAmount(KualiDecimal thresholdPercentage, KualiDecimal purchaseOrderAmount) {
        KualiDecimal thresholdLimt = (purchaseOrderAmount.multiply(thresholdPercentage)).divide(new KualiDecimal(100));
        return thresholdLimt;
    }



    public String createInvoiceAmountExceedsThresholdText(OleInvoiceDocument oleinvoiceDocument) {
        StringBuffer warningMessageBuffer = new StringBuffer("");
        warningMessageBuffer.append("Warning: Some Titles on this Invoice are billed at a significantly higher rate than the PO line indicated:");
        warningMessageBuffer.append("<br/><br/><b>Summary Detail Below :</b><br/><br/><table border=\"1\">");
        warningMessageBuffer.append("<tr><th>Title</th>").
                append("<th>PO #</th>").
                append("<th>PO Price</th>").append("<th>Invoiced Price</th>").
                append("<th>Difference</th>");
        List<OleInvoiceEncumbranceNotification> oleInvoiceEncumbranceNotificationList = this.invoiceEncumbranceNotificationList;
        for (int accCount = 0; accCount < oleInvoiceEncumbranceNotificationList.size(); accCount++) {
            warningMessageBuffer.append("<tr><td>").append(oleInvoiceEncumbranceNotificationList.get(accCount).getItemTitle()).append("</td><td>").
                    append(oleInvoiceEncumbranceNotificationList.get(accCount).getPurchaseOrderIdentifier()).append("</td><td>").
                    append(oleInvoiceEncumbranceNotificationList.get(accCount).getPurchaseOrderAmount()).append("</td><td>").
                    append(oleInvoiceEncumbranceNotificationList.get(accCount).getInvoiceAmount()).append("</td><td>").
                    append(oleInvoiceEncumbranceNotificationList.get(accCount).getDifferenceByThresholdAmount());

        }
        warningMessageBuffer.append("</td></tr></table>");
        warningMessageBuffer.append("<br/>Do you want to approve the invoice anyway?");
        return warningMessageBuffer.toString();
    }

    public OleInvoiceRecord populateValuesFromProfile(BibMarcRecord bibMarcRecord){
        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();
        mapDataFieldsToInvoiceRecord(oleInvoiceRecord,bibMarcRecord);
        setDefaultAndConstantValuesToInvoiceRecord(oleInvoiceRecord);
        checkForForeignCurrency(oleInvoiceRecord);
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());
        return oleInvoiceRecord;
    }

    private void mapDataFieldsToInvoiceRecord(OleInvoiceRecord oleInvoiceRecord,BibMarcRecord bibMarcRecord) {
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            Collections.sort(oleBatchProcessProfileDataMappingOptionsBoList, new Comparator<OLEBatchProcessProfileDataMappingOptionsBo>() {
                @Override
                public int compare(OLEBatchProcessProfileDataMappingOptionsBo obj1, OLEBatchProcessProfileDataMappingOptionsBo obj2) {
                    int result = obj1.getDestinationField().compareTo(obj2.getDestinationField());
                    if(result != 0){
                        return result;
                    }
                    return obj1.getPriority() < obj2.getPriority() ? -1 : obj1.getPriority() > obj2.getPriority() ? 1 : 0;
                }
            });
            List<String> failureRecords = new ArrayList<>();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                if (StringUtils.isNotBlank(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDataTypeDestinationField()) &&
                        StringUtils.isNotBlank(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField()) && StringUtils.isNotBlank(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    String sourceField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField();
                    String sourceFields[] = sourceField.split("\\$");
                    if (sourceFields.length == 2) {
                        String dataField = sourceFields[0].trim();
                        String tagField = sourceFields[1].trim();
                        if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_ITEM_IDENTIFIER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String vendorItemIdentifier = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(vendorItemIdentifier)){
                                oleInvoiceRecord.setVendorItemIdentifier(vendorItemIdentifier);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_VENDOR_ITEM_IDENTIFIER +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String vendorNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(vendorNumber)) {
                                boolean validVendorNumber = getOleOrderRecordService().validateVendorNumber(vendorNumber);
                                if(!validVendorNumber){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_VENDOR_NUMBER + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + vendorNumber);
                                    vendorNumber = null;
                                }
                                oleInvoiceRecord.setVendorNumber(vendorNumber);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_VENDOR_NUMBER + " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String listPrice = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(listPrice)){
                                if(!StringUtils.isBlank(listPrice)){
                                    boolean validListPrice = getOleOrderRecordService().validateDestinationFieldValues(listPrice);
                                    if(!validListPrice){
                                        failureRecords.add(org.kuali.ole.OLEConstants.INVALID_INVOICED_PRICE + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + listPrice);
                                        listPrice = null;
                                    }
                                    else {
                                        listPrice = Float.parseFloat(listPrice) + "";
                                    }
                                    oleInvoiceRecord.setListPrice(listPrice);
                                }
                            }
                            else{
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_INVOICED_PRICE +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String foreignListPrice = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(foreignListPrice)){
                                if(!StringUtils.isBlank(foreignListPrice)){
                                    boolean validForeignListPrice = getOleOrderRecordService().validateDestinationFieldValues(foreignListPrice);
                                    if(!validForeignListPrice){
                                        failureRecords.add(org.kuali.ole.OLEConstants.INVALID_FOREIGN_INVOICED_PRICE + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + foreignListPrice);
                                        foreignListPrice = null;
                                    }
                                    else {
                                        foreignListPrice = Float.parseFloat(foreignListPrice) + "";
                                    }
                                    oleInvoiceRecord.setForeignListPrice(foreignListPrice);
                                }
                            }
                            else{
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_INVOICED_FOREIGN_PRICE +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String quantity = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(quantity)) {
                                boolean validQuantity = getOleOrderRecordService().validateForNumber(quantity);
                                if(!validQuantity){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_QTY + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + quantity);
                                    quantity = null;
                                }
                                oleInvoiceRecord.setQuantity(quantity);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_QTY + " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_TYPES.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String itemType = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(itemType)){
                                Map<String,String> itemTypeMap = new HashMap<>();
                                itemTypeMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_TYPE_CODE, itemType);
                                List<ItemType> itemTypeList = (List) getBusinessObjectService().findMatching(ItemType.class, itemTypeMap);
                                if(itemTypeList != null && itemTypeList.size() == 0){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_ITEM_TYPE_CD + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + itemType);
                                }
                                oleInvoiceRecord.setItemType(org.kuali.ole.OLEConstants.ITEM_TYP);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_ITEM_TYPE + " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String invoiceNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(invoiceNumber)) {
                                /*boolean validInvoiceNumber = getOleOrderRecordService().validateForNumber(invoiceNumber);
                                if(!validInvoiceNumber){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_INV_NMBR + "  "+ dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + invoiceNumber);
                                    invoiceNumber = null;
                                }*/
                                oleInvoiceRecord.setInvoiceNumber(invoiceNumber);
                            }
                            else {
                                //failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_INVOICE_NUMBER +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_DATE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String invoiceDate = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(invoiceDate)){
                                boolean validInvoiceDate = validateInvoiceDate(invoiceDate);
                                if(!validInvoiceDate){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_INV_DT + "  " + dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  "  + invoiceDate + "  " +  "Allowed format is yyyymmdd");
                                }
                                oleInvoiceRecord.setInvoiceDate(invoiceDate);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_INVOICE_DATE +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_DESCRIPTION.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String itemDescription = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(itemDescription)){
                                oleInvoiceRecord.setItemDescription(itemDescription);
                            }
                            else {
                                //failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_ITEM_DESCRIPTION +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String accountNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(accountNumber)){
                                Map<String,String> accountNumberMap = new HashMap<>();
                                accountNumberMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, accountNumber);
                                List<Account> accountNumberList = (List) getBusinessObjectService().findMatching(Account.class, accountNumberMap);
                                if(accountNumberList.size() == 0){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_ACCOUNT_NUMBER + "  " +dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField +"  " + accountNumber);
                                    accountNumber = null;
                                }
                                oleInvoiceRecord.setAccountNumber(accountNumber);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_ACCOUNT_NUMBER +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String objectCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                            if(!StringUtils.isBlank(objectCode)){
                                Map<String,String> objectCodeMap = new HashMap<>();
                                objectCodeMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.OBJECT_CODE, objectCode);
                                List<ObjectCode> objectCodeList = (List) getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
                                if(objectCodeList.size() == 0){
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_OBJECT_CODE + "  " +dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField +"  " + objectCode);
                                    objectCode = null;
                                }
                                oleInvoiceRecord.setObjectCode(objectCode);
                            }
                            else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_OBJECT_CODE +  " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                        else if (org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String fundCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                            if (StringUtils.isNotBlank(fundCode)) {
                                Map<String, String> fundCodeMap = new HashMap<>();
                                fundCodeMap.put(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
                                List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
                                if (CollectionUtils.isEmpty(fundCodeList)) {
                                    failureRecords.add(org.kuali.ole.OLEConstants.INVALID_FUND_CD + "  " + dataField + " " + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + "  " + fundCode);
                                    fundCode = null;
                                }
                                oleInvoiceRecord.setFundCode(fundCode);
                            } else {
                                failureRecords.add(org.kuali.ole.OLEConstants.REQUIRED_FUND_CODE + " " + dataField + org.kuali.ole.OLEConstants.DELIMITER_DOLLAR + tagField + " " + org.kuali.ole.OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                    }
                }
            }
            if(failureRecords != null && failureRecords.size() > 0){
                List reasonForFailure = (List) dataCarrierService.getData("invoiceIngestFailureReason");
                if(reasonForFailure != null){
                    reasonForFailure.addAll(failureRecords);
                    reasonForFailure.add("==================================================================================");
                    dataCarrierService.addData("invoiceIngestFailureReason",reasonForFailure);
                }
            }
        }
    }

    public void setDefaultAndConstantValuesToInvoiceRecord(OleInvoiceRecord oleInvoiceRecord) {
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            if (StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getDataType()) && org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_IMPORT.equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getDataType())
                    && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeValue()) && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                if (org.kuali.ole.OLEConstants.OLEBatchProcess.CONSTANT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {

                    if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_ITEM_IDENTIFIER.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setVendorItemIdentifier(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setVendorNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setListPrice(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setForeignListPrice(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setQuantity(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_TYPES.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setItemType(org.kuali.ole.OLEConstants.ITEM_TYP);
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setInvoiceNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_DATE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setInvoiceDate(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_DESCRIPTION.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setItemDescription(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setAccountNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setObjectCode(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setFundCode(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.CURRENCY_TYPE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setCurrencyType(oleBatchProcessProfileConstantsBo.getAttributeValue());
                        oleInvoiceRecord.setCurrencyTypeId(getCurrencyTypeIdFromCurrencyType(oleInvoiceRecord.getCurrencyType()));
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.EXCHANGE_RATE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        oleInvoiceRecord.setInvoiceCurrencyExchangeRate(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                }
                else if (org.kuali.ole.OLEConstants.OLEBatchProcess.DEFAULT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {

                    if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_ITEM_IDENTIFIER.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getVendorItemIdentifier())) {
                        oleInvoiceRecord.setVendorItemIdentifier(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    if (org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getVendorNumber())) {
                        oleInvoiceRecord.setVendorNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getListPrice())) {
                        oleInvoiceRecord.setListPrice(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getForeignListPrice())) {
                        oleInvoiceRecord.setForeignListPrice(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if (org.kuali.ole.OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getQuantity())) {
                        oleInvoiceRecord.setQuantity(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_TYPES.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getItemType())) {
                        oleInvoiceRecord.setItemType(org.kuali.ole.OLEConstants.ITEM_TYP);
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getInvoiceNumber())) {
                        oleInvoiceRecord.setInvoiceNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.INVOICE_DATE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getInvoiceDate())) {
                        oleInvoiceRecord.setInvoiceDate(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.ITEM_DESCRIPTION.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getItemDescription())) {
                        oleInvoiceRecord.setItemDescription(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getAccountNumber())) {
                        oleInvoiceRecord.setAccountNumber(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getObjectCode())) {
                        oleInvoiceRecord.setObjectCode(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getFundCode())) {
                        oleInvoiceRecord.setFundCode(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.CURRENCY_TYPE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getCurrencyType())) {
                        oleInvoiceRecord.setCurrencyType(oleBatchProcessProfileConstantsBo.getAttributeValue());
                        oleInvoiceRecord.setCurrencyTypeId(getCurrencyTypeIdFromCurrencyType(oleInvoiceRecord.getCurrencyType()));
                    }
                    else if(org.kuali.ole.OLEConstants.OLEBatchProcess.EXCHANGE_RATE.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleInvoiceRecord.getInvoiceCurrencyExchangeRate())) {
                        oleInvoiceRecord.setInvoiceCurrencyExchangeRate(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                }
            }
        }
    }

    public String getCurrencyTypeIdFromCurrencyType(String currencyType){
        Map<String,String> currencyTypeMap = new HashMap<>();
        currencyTypeMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.CURRENCY_TYPE,currencyType);
        List<OleCurrencyType> currencyTypeList = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyTypeMap);
        return currencyTypeList.get(0).getCurrencyTypeId().toString();
    }

    private String getSubFieldValueFor(BibMarcRecord bibMarcRecord, String dataField, String tag) {
        String subFieldValue = null;
        org.kuali.ole.docstore.common.document.content.bib.marc.DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, dataField);
        if (null != dataFieldForTag) {
            List<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> subfields = dataFieldForTag.getSubFields();
            for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> iterator = subfields.iterator(); iterator.hasNext(); ) {
                org.kuali.ole.docstore.common.document.content.bib.marc.SubField marcSubField = iterator.next();
                if (marcSubField.getCode().equals(tag)) {
                    return marcSubField.getValue();
                }
            }
        }
        return subFieldValue;
    }

    public org.kuali.ole.docstore.common.document.content.bib.marc.DataField getDataFieldForTag(BibMarcRecord bibMarcRecord, String tag) {
        for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> iterator = bibMarcRecord.getDataFields().iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.common.document.content.bib.marc.DataField marcDataField = iterator.next();
            if (marcDataField.getTag().equalsIgnoreCase(tag)) {
                return marcDataField;
            }
        }
        return null;
    }


    private String setDataMappingValues(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList,int dataMapCount,BibMarcRecord bibMarcRecord,String dataField,String tagField){
        String subFieldValue = getSubFieldValueFor(bibMarcRecord, dataField, tagField);
        if (StringUtils.isBlank(subFieldValue)) {
            OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount);
            if (dataMapCount+1 <= oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                if(dataMapCount+1 == oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                }
                else if(!oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount+1).getDestinationField().equalsIgnoreCase(oleBatchProcessProfileDataMappingOptionsBo.getDestinationField())){
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                }
            }
        }
        return subFieldValue;
    }

    private boolean validateInvoiceDate(String invoiceDate){
        SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
        try {
            dateFromRawFile.parse(invoiceDate);
            return true;
        }
        catch (ParseException e) {
            return false;
        }
    }

    private void checkForForeignCurrency(OleInvoiceRecord oleInvoiceRecord){
        if(!StringUtils.isBlank(oleInvoiceRecord.getCurrencyType())){
            if(!oleInvoiceRecord.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                if (oleInvoiceRecord.getForeignListPrice() != null && !oleInvoiceRecord.getForeignListPrice().isEmpty() &&
                        oleInvoiceRecord.getInvoiceCurrencyExchangeRate()!= null &&  !oleInvoiceRecord.getInvoiceCurrencyExchangeRate().isEmpty()) {
                    oleInvoiceRecord.setListPrice((new BigDecimal(oleInvoiceRecord.getForeignListPrice()).
                            divide(new BigDecimal(oleInvoiceRecord.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)).toString());
                }
            }
        }
    }


    @Override
    public String getCurrencyType(String currencyTypeId) {
        if(StringUtils.isNotBlank(currencyTypeId)){
            Map currencyMap = new HashMap();
            currencyMap.put(OleSelectConstant.CURRENCY_TYPE_ID, new Long(currencyTypeId));
            OleCurrencyType oleCurrencyType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleCurrencyType.class,currencyMap);
            if(oleCurrencyType != null){
                return oleCurrencyType.getCurrencyType();
            }
        }
        return "";
    }


    @Override
    public OleExchangeRate getExchangeRate(String currencyTypeId) {
        Map documentNumberMap = new HashMap();
        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, new Long(currencyTypeId));
        List<OleExchangeRate> exchangeRateList = (List) getBusinessObjectService().findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
        Iterator iterator = exchangeRateList.iterator();
        if (iterator.hasNext()) {
            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
            return tempOleExchangeRate;
        }
        return null;
    }

    @Override
    public void deleteInvoiceItem(OleInvoiceDocument oleInvoiceDocument) {
        List<OleInvoiceItem> oleDeletedInvoiceItemList =oleInvoiceDocument.getDeletedInvoiceItems();
        for(OleInvoiceItem oleInvoiceItem:oleDeletedInvoiceItemList){
            List<OLEPaidCopy> olePaidCopies = oleInvoiceItem.getPaidCopies();
            for(OLEPaidCopy olePaidCopy : olePaidCopies){
                Map<String,Integer> olePaidCopyMap=new HashMap<String,Integer>();
                olePaidCopyMap.put("olePaidCopyId",olePaidCopy.getOlePaidCopyId());
                KRADServiceLocator.getBusinessObjectService().deleteMatching(OLEPaidCopy.class,olePaidCopyMap);
            }
            Map<String,Integer> invoiceAccountMap=new HashMap<String,Integer>();
            invoiceAccountMap.put("itemIdentifier",oleInvoiceItem.getItemIdentifier());
            KRADServiceLocator.getBusinessObjectService().deleteMatching(InvoiceAccount.class,invoiceAccountMap);
            List<OleInvoiceNote> oleDeletedNotesList = oleInvoiceItem.getNotes();
            for(OleInvoiceNote oleInvoiceNote:oleDeletedNotesList) {
                Map<String,Integer> invoiceNoteMap=new HashMap<String,Integer>();
                invoiceNoteMap.put("itemNoteIdentifier",oleInvoiceNote.getItemNoteIdentifier());
                KRADServiceLocator.getBusinessObjectService().deleteMatching(OleInvoiceNote.class,invoiceNoteMap);
            }
            Map<String,Integer> invoiceItemMap=new HashMap<String,Integer>();
            invoiceItemMap.put("itemIdentifier",oleInvoiceItem.getItemIdentifier());
            KRADServiceLocator.getBusinessObjectService().deleteMatching(OleInvoiceItem.class,invoiceItemMap);
        }
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

    public String getPurchaseOrderVendor(String poId) {
        Map poMap = new HashMap();
        poMap.put(OLEConstants.PUR_DOC_IDENTIFIER,poId);
        OlePurchaseOrderDocument document = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class,poMap);
        if(document != null) {
            return document.getVendorName();
        }
        return null;

    }

    public void  calculateProrateForPOLevel(OleInvoiceDocument inv) {
        List<Integer> poIdList = new ArrayList<>();
        BigDecimal totalUnitPrice = BigDecimal.ZERO;
        List<OleInvoiceItem> oleInvoiceItemList = new ArrayList<OleInvoiceItem>();
        int noOfCopies = 0;
        for(OleInvoiceItem item : (List<OleInvoiceItem>)inv.getItems()){
            if(item.getItemTypeCode().equals(OLEConstants.ITEM)){
                poIdList.add(item.getPurchaseOrderIdentifier());
                noOfCopies += Integer.parseInt(item.getOleCopiesOrdered());
            }
            else {
                oleInvoiceItemList.add(item);
            }
        }
        List<OleInvoiceItem> removeFromInvoiceItemList = new ArrayList<OleInvoiceItem>();
        List<OleInvoiceItem> addToInvoiceItemList = new ArrayList<OleInvoiceItem>();
        int invoiceItemsize = poIdList.size();
        for(OleInvoiceItem item : oleInvoiceItemList){
            if(!item.getItemTypeCode().equals(OLEConstants.ITEM) && item.getItemUnitPrice() != null) {
                accountingAmount = KualiDecimal.ZERO;
                percentage = BigDecimal.ZERO;
                BigDecimal itemUnitPrice = item.getItemUnitPrice();
                BigDecimal totalItemUnitPrice = item.getItemUnitPrice();
                if(inv.getProrateBy().equalsIgnoreCase(OLEConstants.PRORATE_BY_QTY)) {
                    itemUnitPrice = (itemUnitPrice.divide(new BigDecimal(noOfCopies), 2, RoundingMode.HALF_UP));
                    for (int i = 0; i < invoiceItemsize; i++) {
                        OleInvoiceItem oleInvoiceItem = (OleInvoiceItem) ObjectUtils.deepCopy(item);
                        int copies = Integer.parseInt((String) ((OleInvoiceItem) inv.getItems().get(i)).getOleCopiesOrdered());
                        if((invoiceItemsize-1) == i) {
                            oleInvoiceItem.setItemUnitPrice(totalItemUnitPrice.subtract(totalUnitPrice).multiply(new BigDecimal(copies)));

                        }else {
                            oleInvoiceItem.setItemUnitPrice(itemUnitPrice.multiply(new BigDecimal(copies)));
                        }
                        totalUnitPrice = totalUnitPrice.add(oleInvoiceItem.getItemUnitPrice());
                        oleInvoiceItem.setPurchaseOrderIdentifier(poIdList.get(i));
                        List<SourceAccountingLine> sourceAccountingLines = (List) ((OleInvoiceItem) inv.getItems().get(i)).getSourceAccountingLines();
                        List<PurApAccountingLine> sourceAccountingLineList = updateAccountingLinesForProrate(sourceAccountingLines, itemUnitPrice.multiply(new BigDecimal(copies)), InvoiceAccount.class,i,invoiceItemsize,totalItemUnitPrice);
                        oleInvoiceItem.setSourceAccountingLines(sourceAccountingLineList);
                        addToInvoiceItemList.add(oleInvoiceItem);
                    }
                    removeFromInvoiceItemList.add(item);
                } else if(inv.getProrateBy().equalsIgnoreCase(OLEConstants.PRORATE_BY_DOLLAR)) {
                    for (int i = 0; i < invoiceItemsize; i++) {
                        OleInvoiceItem oleInvoiceItem = (OleInvoiceItem) ObjectUtils.deepCopy(item);
                        KualiDecimal totalAmount = ((OleInvoiceItem) inv.getItems().get(i)).getTotalAmount();
                        String listPrice = (String)((OleInvoiceItem) inv.getItems().get(i)).getListPrice();
                        if (inv.getItemTotal().contains("(")) {
                            itemUnitPrice = (totalAmount.bigDecimalValue().multiply(item.getItemUnitPrice())).divide(new BigDecimal(inv.getInvoicedItemTotal()), 2, RoundingMode.HALF_UP).abs();
                        }
                        else {
                            itemUnitPrice = (totalAmount.bigDecimalValue().multiply(item.getItemUnitPrice())).divide(new BigDecimal(inv.getItemTotal()), 2, RoundingMode.HALF_UP);
                        }
                        if((invoiceItemsize-1) == i) {
                            oleInvoiceItem.setItemUnitPrice(totalItemUnitPrice.subtract(totalUnitPrice));
                        } else {
                            oleInvoiceItem.setItemUnitPrice(itemUnitPrice);
                        }
                        totalUnitPrice = totalUnitPrice.add(oleInvoiceItem.getItemUnitPrice());
                        oleInvoiceItem.setPurchaseOrderIdentifier(poIdList.get(i));
                        List<SourceAccountingLine> sourceAccountingLines = (List) ((OleInvoiceItem) inv.getItems().get(i)).getSourceAccountingLines();
                        List<PurApAccountingLine> sourceAccountingLineList = updateAccountingLinesForProrate(sourceAccountingLines, itemUnitPrice, InvoiceAccount.class,i,invoiceItemsize,totalItemUnitPrice);
                        oleInvoiceItem.setSourceAccountingLines(sourceAccountingLineList);
                        addToInvoiceItemList.add(oleInvoiceItem);
                    }
                    removeFromInvoiceItemList.add(item);

                } else if(inv.getProrateBy().equalsIgnoreCase(OLEConstants.MANUAL_PRORATE)) {
                    itemUnitPrice = itemUnitPrice.divide(new BigDecimal(poIdList.size()), 2, RoundingMode.HALF_UP);
                    for (int i = 0; i < invoiceItemsize; i++) {
                        OleInvoiceItem oleInvoiceItem = (OleInvoiceItem) ObjectUtils.deepCopy(item);
                        oleInvoiceItem.setItemUnitPrice(itemUnitPrice);
                        oleInvoiceItem.setPurchaseOrderIdentifier(poIdList.get(i));
                        List<PurApAccountingLine> sourceAccountingLines = oleInvoiceItem.getSourceAccountingLines();
                        List<PurApAccountingLine> soucrceAccountingLineList = updateAccountingLinesForManualProrate(sourceAccountingLines, itemUnitPrice, InvoiceAccount.class);
                        oleInvoiceItem.setSourceAccountingLines(soucrceAccountingLineList);
                        addToInvoiceItemList.add(oleInvoiceItem);
                    }
                    removeFromInvoiceItemList.add(item);
                }
                totalUnitPrice = BigDecimal.ZERO;
            }
        }
        inv.getItems().removeAll(removeFromInvoiceItemList);
        inv.getItems().addAll(addToInvoiceItemList);
    }

    public List<PurApAccountingLine> updateAccountingLinesForProrate(List<SourceAccountingLine> accounts, BigDecimal itemUnitPrice, Class clazz, int currentItem,int invoiceItemSize, BigDecimal totalItemUnitPrice) {

        List<PurApAccountingLine> newAccounts = new ArrayList();
        BigDecimal percent = new BigDecimal(0);
        for (int i=0;i<accounts.size();i++) {
            PurApAccountingLine newAccountingLine;
            newAccountingLine = null;
            try {
                newAccountingLine = (PurApAccountingLine) clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, accounts.get(i), newAccountingLine);
            if(currentItem == (invoiceItemSize-1)) {
                if(accounts.size()-1 == i) {
                    percentage = new BigDecimal(100).subtract(percentage);
                    KualiDecimal amount = (new KualiDecimal(itemUnitPrice.multiply(percentage.divide(new BigDecimal(100)))));
                    newAccountingLine.setAmount(amount);
                    newAccountingLine.setAccountLinePercent(percentage);


                }else {
                    BigDecimal remainingAmount = totalItemUnitPrice.subtract(accountingAmount.bigDecimalValue());
                    BigDecimal accountPercent = ((InvoiceAccount) accounts.get(i)).getAccountLinePercent();
                    KualiDecimal amount = (new KualiDecimal(remainingAmount.multiply(accountPercent.divide(new BigDecimal(100)))));
                    accountPercent = (amount.bigDecimalValue().multiply(new BigDecimal(100))).divide(remainingAmount,2,RoundingMode.HALF_UP);
                    percentage = accountPercent;
                    newAccountingLine.setAccountLinePercent(accountPercent);
                    newAccountingLine.setAmount(amount);
                }

            } else {
                BigDecimal accountPercent = ((InvoiceAccount) accounts.get(i)).getAccountLinePercent();
                newAccountingLine.setAccountLinePercent(accountPercent);
                newAccountingLine.setAmount(new KualiDecimal(itemUnitPrice.multiply(accountPercent.divide(new BigDecimal(100)))));
            }

            accountingAmount = accountingAmount.add(newAccountingLine.getAmount());
            newAccounts.add(newAccountingLine);


        }

        return newAccounts;

    }


    public List<PurApAccountingLine> updateAccountingLinesForManualProrate(List<PurApAccountingLine> accounts, BigDecimal itemUnitPrice, Class clazz) {

        List<PurApAccountingLine> newAccounts = new ArrayList();
        BigDecimal percent = new BigDecimal(0);
        if (accounts.size() > 0) {
            percent = new BigDecimal(100).divide(new BigDecimal(accounts.size()), 2, RoundingMode.HALF_UP);
            itemUnitPrice = itemUnitPrice.multiply(percent.divide(new BigDecimal(100)));
        }
        for (PurApAccountingLine accountingLine : accounts) {
            PurApAccountingLine newAccountingLine;
            newAccountingLine = null;
            try {
                newAccountingLine = (PurApAccountingLine) clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, accountingLine, newAccountingLine);
            newAccountingLine.setAccountLinePercent(percent);
            newAccountingLine.setAmount(new KualiDecimal(itemUnitPrice));
            newAccounts.add(newAccountingLine);


        }

        return newAccounts;

    }

    public List<String> getRecurringOrderTypes(){
        List<String> continuingOrderType=new ArrayList<>();
        continuingOrderType.add(PurapConstants.ORDER_TYPE_STANDING);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_SUBSCRIPTION);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_MEMBERSHIP);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_BLANKET);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_INTEGRATING_RESOURCE);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_CONTINUING);
        return continuingOrderType;
    }

}