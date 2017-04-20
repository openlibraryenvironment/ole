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
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderDocTypes;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderAmendmentDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.LogicContainer;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.service.impl.PaymentRequestServiceImpl;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePaymentMethod;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OlePaymentRequestService;
import org.kuali.ole.select.document.service.OlePurapAccountingService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * This class has implementation for PaymentRequesr with respect to OLE.
 */
public class OlePaymentRequestServiceImpl extends PaymentRequestServiceImpl implements OlePaymentRequestService {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePaymentRequestServiceImpl.class);

    protected PurchaseOrderService purchaseOrderService;
    protected PurapService purapService;
    protected DocumentService documentService;
    protected NoteService noteService;
    private OlePurapAccountingService olePurapAccountingService;
    private PurapAccountingService purapAccountingService;
    private OleSelectDocumentService oleSelectDocumentService;

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
     * @see org.kuali.ole.select.document.service.OlePaymentRequestService#completePaymentDocument(org.kuali.ole.select.document.OlePaymentRequestDocument)
     */
    @Override
    public void completePaymentDocument(OlePaymentRequestDocument paymentRequestDocument) {
        LOG.debug("Inside CompletePaymentDocument");

        PurchaseOrderDocument poDoc = null;

        if (paymentRequestDocument instanceof PaymentRequestDocument) {
            // delete unentered items
            purapService.deleteUnenteredItems(paymentRequestDocument);

            poDoc = paymentRequestDocument.getPurchaseOrderDocument();
        }
        updatePaymentTotalsOnPurchaseOrder(paymentRequestDocument, poDoc);

        purapService.saveDocumentNoValidation(poDoc);

        poDoc.setVendorName(paymentRequestDocument.getVendorDetail().getVendorName());

        spawnPoAmendmentForUnorderedItems(paymentRequestDocument, poDoc);

        purapService.saveDocumentNoValidation(paymentRequestDocument);

        LOG.debug("Leaving CompletePaymentDocument");
    }

    protected void updatePaymentTotalsOnPurchaseOrder(OlePaymentRequestDocument paymentDocument, PurchaseOrderDocument poDoc) {
       /* LOG.debug("Inside updatePaymentTotalsOnPurchaseOrder");
        for (OlePaymentRequestItem paymentItem : (List<OlePaymentRequestItem>) paymentDocument.getItems()) {
            ItemType itemType = paymentItem.getItemType();
            if (StringUtils.equalsIgnoreCase(itemType.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) poDoc.getItemByLineNumber(paymentItem.getItemLineNumber());

                if (ObjectUtils.isNotNull(poItem)) {

                    KualiDecimal poItemReceivedTotal = poItem.getOutstandingQuantity();

                    KualiDecimal itemQuantity = paymentItem.getItemQuantity();

                    if (ObjectUtils.isNull(itemQuantity)) {
                        itemQuantity = KualiDecimal.ZERO;
                    }
                    if (ObjectUtils.isNull(poItemReceivedTotal)) {
                        poItemReceivedTotal = KualiDecimal.ZERO;
                    }   */
                    /* Modified for OLE - 2516
                      poItem.setItemQuantity(itemQuantity);
                    */
               /*     poItem.setItemUnitPrice(paymentItem.getItemUnitPrice());
                }
            }
        }
        LOG.debug("Leaving updatePaymentTotalsOnPurchaseOrder");  */

        LOG.debug("Inside updatePaymentTotalsOnPurchaseOrder");
       // List<Integer> poList = new ArrayList();
       // Integer invPoId = 0;
      /*  for (PaymentRequestItem payItem : (List<PaymentRequestItem>) paymentDocument.getItems()) {
            if (!(poList.contains(payItem.getPurchaseOrderIdentifier()))) {
                poList.add(payItem.getPurchaseOrderIdentifier());
            }
        }*/
        for (PaymentRequestItem payItem : (List<PaymentRequestItem>) paymentDocument.getItems()) {
         //   for (Integer purchaseOrderId : poList) {
                if (payItem.getItemType() != null && payItem.getItemType().getItemTypeCode() != null && StringUtils.equalsIgnoreCase(payItem.getItemType().getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE))
                {
                    PurchaseOrderDocument purDoc = paymentDocument.getPurchaseOrderDocument();
                    OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) purDoc.getItemByLineNumber(payItem.getItemLineNumber());

                    if (ObjectUtils.isNotNull(poItem)) {

                        KualiDecimal poItemReceivedTotal = poItem.getOutstandingQuantity();

                        KualiDecimal itemQuantity = payItem.getItemQuantity();

                        if (ObjectUtils.isNull(itemQuantity)) {
                            itemQuantity = KualiDecimal.ZERO;
                        }
                        if (ObjectUtils.isNull(poItemReceivedTotal)) {
                            poItemReceivedTotal = KualiDecimal.ZERO;
                        }
                        /* Modified for OLE - 2516
                          poItem.setItemQuantity(itemQuantity);
                        */
                         //poItem.setItemUnitPrice(payItem.getItemUnitPrice());/*Modified for the jira 5458*/
                    }
                 //   purapService.saveDocumentNoValidation(poDoc);
                }

         }
         LOG.debug("Leaving updatePaymentTotalsOnPurchaseOrder");
    }
    /**
     * Spawns PO amendments for new unordered items on a PaymentRequest document.
     *
     * @param paymentDocument
     * @param po
     */
    protected void spawnPoAmendmentForUnorderedItems(OlePaymentRequestDocument paymentDocument, PurchaseOrderDocument po) {

        LOG.debug("Inside spawnPoAmendmentForUnorderedItems");
        if (paymentDocument instanceof OlePaymentRequestDocument) {
            OlePaymentRequestDocument rlDoc = paymentDocument;
            final PurchaseOrderDocument currentDocument = po;

            //if a new item has been added spawn a purchase order amendment
            if (hasNewUnorderedItem(paymentDocument)) {
                String newSessionUserId = getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER);
                try {

                    LogicContainer logicToRun = new LogicContainer() {
                        @Override
                        public Object runLogic(Object[] objects) throws Exception {
                            OlePaymentRequestDocument rlDoc = (OlePaymentRequestDocument) objects[0];
                            String poDocNumber = (String) objects[1];

                            //create a PO amendment
                            PurchaseOrderAmendmentDocument amendmentPo = (PurchaseOrderAmendmentDocument) purchaseOrderService.createAndSavePotentialChangeDocument(currentDocument, PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT, PurchaseOrderStatuses.APPDOC_AMENDMENT);

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

                    purapService.performLogicWithFakedUserSession(newSessionUserId, logicToRun, new Object[]{rlDoc, po.getDocumentNumber()});
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
    protected boolean hasNewUnorderedItem(OlePaymentRequestDocument paymentDoc) {
        LOG.debug("Inside hasNewUnorderedItem");
        boolean itemAdded = false;

        for (OlePaymentRequestItem prItem : (List<OlePaymentRequestItem>) paymentDoc.getItems()) {
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
    protected void addUnorderedItemsToAmendment(PurchaseOrderAmendmentDocument amendment, OlePaymentRequestDocument rlDoc) {

        LOG.debug("Inside addUnorderedItemsToAmendment");
        OlePurchaseOrderItem poi = null;

        for (OlePaymentRequestItem rlItem : (List<OlePaymentRequestItem>) rlDoc.getItems()) {
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
     * Creates a PO item from paymentRequest Line item.
     *
     * @param rlItem
     * @return
     */
    protected OlePurchaseOrderItem createPoItemFromPaymentLine(OlePaymentRequestItem rlItem) {
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

    public void setAccountingLinesFromPayment(OlePaymentRequestItem payItem, OlePurchaseOrderItem purItem) {
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
     * @see org.kuali.ole.select.document.service.OlePaymentRequestService#calculateProrateItemSurcharge(org.kuali.ole.select.document.OlePaymentRequestDocument)
     */
    @Override
    public void calculateProrateItemSurcharge(OlePaymentRequestDocument paymentRequestDocument) {
        LOG.debug("Inside Calculation for ProrateItemSurcharge");
        //  KualiDecimal addChargeItem = paymentRequestDocument.getGrandPreTaxTotalExcludingDiscount().subtract(paymentRequestDocument.getLineItemPreTaxTotal());
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OlePaymentRequestItem> item = paymentRequestDocument.getItems();

        for (OlePaymentRequestItem items : item) {
            if (!items.getItemType().isQuantityBasedGeneralLedgerIndicator() && !items.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE) && items.getItemUnitPrice() != null) {
                addChargeItem = addChargeItem.add(items.getItemUnitPrice());
            }
        }
        List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
        BigDecimal totalExtPrice = new BigDecimal(0);
        BigDecimal newUnitPrice = new BigDecimal(0);
        BigDecimal extPrice = new BigDecimal(0);
        BigDecimal unitPricePercent = new BigDecimal(0);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal one = new BigDecimal(1);
        BigDecimal totalSurCharge = new BigDecimal(0);
        BigDecimal totalItemQuantity = new BigDecimal(0);
        BigDecimal itemSurchargeCons = new BigDecimal(0);
        for (int i = 0; item.size() > i; i++) {
            OlePaymentRequestItem items = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
            if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator()) && !ObjectUtils.isNull(items.getItemQuantity())) {
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    totalItemQuantity = totalItemQuantity.add(items.getItemQuantity().bigDecimalValue());
                }
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || paymentRequestDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) || paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    if (items.getItemDiscount() == null) {
                        items.setItemDiscount(KualiDecimal.ZERO);
                    }
                    if (items.getItemDiscountType() != null && items.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                        newUnitPrice = (hundred.subtract(items.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(items.getItemListPrice().bigDecimalValue());
                    } else {
                        newUnitPrice = items.getItemListPrice().bigDecimalValue().subtract(items.getItemDiscount().bigDecimalValue());
                    }
                    newUnitPriceList.add(newUnitPrice);
                    extPrice = newUnitPrice.multiply(items.getItemQuantity().bigDecimalValue());
                    totalExtPrice = totalExtPrice.add(extPrice);
                }
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                    if (items.getItemSurcharge() == null) {
                        items.setItemSurcharge(BigDecimal.ZERO);
                    }
                    totalSurCharge = totalSurCharge.add(items.getItemQuantity().bigDecimalValue().multiply(items.getItemSurcharge()));
                }
            }

        }
        if (paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
            if (totalItemQuantity.compareTo(BigDecimal.ZERO) != 0) {
                itemSurchargeCons = one.divide(totalItemQuantity, 8, RoundingMode.HALF_UP);
            }
        }
        for (int i = 0, j = 0; item.size() > i; i++) {
            OlePaymentRequestItem items = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
            if (items.getItemType().isQuantityBasedGeneralLedgerIndicator() && newUnitPriceList.size() > j && !ObjectUtils.isNull(items.getItemQuantity())) {
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR)) {
                    if (totalExtPrice.compareTo(BigDecimal.ZERO) != 0) {
                        unitPricePercent = newUnitPriceList.get(j).divide(totalExtPrice, 8, RoundingMode.HALF_UP);
                    }
                    items.setItemSurcharge(unitPricePercent.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                    items.setItemUnitPrice(newUnitPriceList.get(j).add(items.getItemSurcharge()));
                }
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    items.setItemSurcharge(itemSurchargeCons.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                    items.setItemUnitPrice(newUnitPriceList.get(j).add(items.getItemSurcharge()));
                }
                if (paymentRequestDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) && items.getItemSurcharge() != null) {
                    items.setItemUnitPrice(newUnitPriceList.get(j).add(items.getItemSurcharge()));
                }
                j++;
            }
        }
        if (paymentRequestDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
            if (totalSurCharge.compareTo(addChargeItem) != 0) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_PAYMENT_REQUEST_TOTAL_MISMATCH);
            }
        }
        LOG.debug("Leaving Calculation for ProrateItemSurcharge");
    }

    @Override
    public void calculatePaymentRequest(PaymentRequestDocument paymentRequest, boolean updateDiscount) {
        LOG.debug("calculatePaymentRequest() started");

        // general calculation, i.e. for the whole preq document
        if (ObjectUtils.isNull(paymentRequest.getPaymentRequestPayDate())) {
            paymentRequest.setPaymentRequestPayDate(calculatePayDate(paymentRequest.getInvoiceDate(), paymentRequest.getVendorPaymentTerms()));
        }

        distributeAccounting(paymentRequest);

        calculateTax(paymentRequest);

        // do proration for full order and trade in
        purapService.prorateForTradeInAndFullOrderDiscount(paymentRequest);

        // do proration for payment terms discount
        if (updateDiscount) {
            calculateDiscount(paymentRequest);
        }

        distributeAccounting(paymentRequest);
    }

    public void calculateTax(PaymentRequestDocument purapDocument) {
        PurchaseOrderDocument pDoc = purapDocument.getPurchaseOrderDocument();
        String deliveryState = pDoc.getDeliveryStateCode();
        String deliveryPostalCode = pDoc.getBillingPostalCode();
        purapService.calculateTaxForPREQ(purapDocument,deliveryState,deliveryPostalCode);
    }

    @Override
    protected void distributeAccounting(PaymentRequestDocument paymentRequestDocument) {
        // update the account amounts before doing any distribution
        purapAccountingService.updateAccountAmounts(paymentRequestDocument);

        for (PaymentRequestItem item : (List<PaymentRequestItem>) paymentRequestDocument.getItems()) {
            KualiDecimal totalAmount = KualiDecimal.ZERO;
            List<PurApAccountingLine> distributedAccounts = null;
            List<SourceAccountingLine> summaryAccounts = null;
            Set excludedItemTypeCodes = new HashSet();
            excludedItemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE);

            // skip above the line
            if (item.getItemType().isLineItemIndicator()) {
                continue;
            }

            if ((ObjectUtils.isNotNull(item.getExtendedPrice())) && (KualiDecimal.ZERO.compareTo(item.getExtendedPrice()) != 0) && !"DISC".equals(item.getItemTypeCode())) {
                if ((StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE, item.getItemType().getItemTypeCode())) && (paymentRequestDocument.getGrandTotal() != null) && ((KualiDecimal.ZERO.compareTo(paymentRequestDocument.getGrandTotal()) != 0))) {

                    // No discount is applied to other item types other than item line
                    // See KFSMI-5210 for details

                    // total amount should be the line item total, not the grand total
                    totalAmount = paymentRequestDocument.getLineItemTotal();

                    // prorate item line accounts only
                    Set includedItemTypeCodes = new HashSet();
                    includedItemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
                    summaryAccounts = olePurapAccountingService.generateSummaryIncludeItemTypesAndNoZeroTotals(paymentRequestDocument.getItems(), includedItemTypeCodes);
                    distributedAccounts = olePurapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, PaymentRequestAccount.class);

                    // update amounts on distributed accounts
                    purapAccountingService.updateAccountAmountsWithTotal(distributedAccounts, item.getTotalAmount());
                } else {
                    PurchaseOrderItem poi = item.getPurchaseOrderItem();
                    if ((poi != null) && (poi.getSourceAccountingLines() != null) && (!(poi.getSourceAccountingLines().isEmpty())) && (poi.getExtendedPrice() != null) && ((KualiDecimal.ZERO.compareTo(poi.getExtendedPrice())) != 0)) {
                        // use accounts from purchase order item matching this item
                        // account list of current item is already empty
                        item.generateAccountListFromPoItemAccounts(poi.getSourceAccountingLines());
                    } else {
                        totalAmount = paymentRequestDocument.getPurchaseOrderDocument().getTotalDollarAmountAboveLineItems();
                        purapAccountingService.updateAccountAmounts(paymentRequestDocument.getPurchaseOrderDocument());
                        summaryAccounts = olePurapAccountingService.generateSummary(paymentRequestDocument.getItems());
                        distributedAccounts = olePurapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, PaymentRequestAccount.class);
                    }

                }
                if (CollectionUtils.isNotEmpty(distributedAccounts)) {
                    item.setSourceAccountingLines(distributedAccounts);
                }
            }
            // update the item
            purapAccountingService.updateItemAccountAmounts(item);
        }
        // update again now that distribute is finished. (Note: we may not need this anymore now that I added updateItem line above
        purapAccountingService.updateAccountAmounts(paymentRequestDocument);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.PaymentRequestService#getPaymentRequestByDocumentNumber(java.lang.String)
     */
    @Override
    public PaymentRequestDocument getPaymentRequestByDocumentNumber(String documentNumber) {
        LOG.debug("getPaymentRequestByDocumentNumber() started");

        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                OlePaymentRequestDocument doc = (OlePaymentRequestDocument) documentService.getByDocumentHeaderId(documentNumber);
                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting payment request document from document service";
                LOG.error("Exception in getPaymentRequestByDocumentNumber() " + errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return null;
    }

    /**
     * This method is validates the prorate surchanges if prorate by manual
     *
     * @see org.kuali.ole.select.document.service.OlePaymentRequestService#validateProratedSurcharge(org.kuali.ole.select.document.OlePaymentRequestDocument)
     */
    @Override
    public boolean validateProratedSurcharge(OlePaymentRequestDocument paymentRequestDocument) {

        List<OlePaymentRequestItem> items = paymentRequestDocument.getItems();
        boolean manuvalProrateValidFlag = false;
        BigDecimal proratedSurchargeAmount = new BigDecimal(0);
        for (int i = 0; items.size() > i; i++) {
            OlePaymentRequestItem item = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
            if ("".equals(item.getItemTitleId()) || item.getItemTitleId() == null) {
                if (item.getItemUnitPrice() != null && !"".equals(item.getItemUnitPrice())) {
                    manuvalProrateValidFlag = true;
                }
            }
        }
        if (manuvalProrateValidFlag) {
            for (int i = 0; items.size() > i; i++) {
                OlePaymentRequestItem item = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
                if (!"".equals(item.getItemTitleId()) && item.getItemTitleId() != null) {
                    if (item.getItemSurcharge() != null && item.getItemSurcharge().compareTo(new BigDecimal(0)) != 0) {
                        proratedSurchargeAmount = proratedSurchargeAmount.add(item.getItemSurcharge());
                    }
                }
            }
            if (proratedSurchargeAmount.compareTo(new BigDecimal(0)) == 0) {
                manuvalProrateValidFlag = false;
                paymentRequestDocument.setProrateBy(null);
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_PAYMENT_REQUEST_TOTAL_MISMATCH);
            }
        }
        return manuvalProrateValidFlag;
    }

    /**
     * This method is for caluclate the total amount without select proprate by Quantity,doller and manual
     *
     * @see org.kuali.ole.select.document.service.OlePaymentRequestService#calculateWithoutProrates(org.kuali.ole.select.document.OlePaymentRequestDocument)
     */
    @Override
    public void calculateWithoutProrates(OlePaymentRequestDocument paymentRequestDocument) {
        LOG.debug("Inside Calculation for with out  prorate");
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OlePaymentRequestItem> items = paymentRequestDocument.getItems();

        for (OlePaymentRequestItem item : items) {
            if (item.getItemTitleId() != null && !"".equals(item.getItemTitleId())) {
                if (!item.getItemListPrice().equals(item.getExtendedPrice())) {
                    item.setItemUnitPrice(item.getItemListPrice().bigDecimalValue());
                    item.setExtendedPrice(item.getItemListPrice());
                    item.setItemSurcharge(BigDecimal.ZERO);
                }
            }
        }

        for (OlePaymentRequestItem item : items) {
            if (!item.getItemType().isQuantityBasedGeneralLedgerIndicator()
                    && !item.getItemTypeCode().equalsIgnoreCase(
                    PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE)
                    && item.getItemUnitPrice() != null) {
                addChargeItem = addChargeItem.add(item.getItemUnitPrice());
            }
        }
        List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
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
            OlePaymentRequestItem item = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
            if ((item.getItemType().isQuantityBasedGeneralLedgerIndicator())
                    && !ObjectUtils.isNull(item.getItemQuantity())) {
                if (item.getItemSurcharge() == null) {
                    item.setItemSurcharge(BigDecimal.ZERO);
                }
                if (paymentRequestDocument.getProrateBy() == null) {
                    if (item.getItemDiscount() == null) {
                        item.setItemDiscount(KualiDecimal.ZERO);
                    }
                    if (item.getItemDiscountType() != null
                            && item.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                        newUnitPrice = SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP);
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
            OlePaymentRequestItem item = (OlePaymentRequestItem) paymentRequestDocument.getItem(i);
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

    public String getPaymentMethod(Integer paymentId){
        Map payMap = new HashMap();
        payMap.put("paymentMethodId", paymentId);
        OlePaymentMethod olePaymentMethod = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePaymentMethod.class,payMap);
        if(olePaymentMethod != null){
            return olePaymentMethod.getPaymentMethod();
        }
        return "";
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

}
