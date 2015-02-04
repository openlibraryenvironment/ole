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


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.CorrectionReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class has implementation for ReceivingService with respect to OLE.
 */

@Transactional
public class OleReceivingServiceImpl extends ReceivingServiceImpl implements ReceivingService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleReceivingServiceImpl.class);

    /**
     * This method is overridden to set total parts received and total parts damaged for
     * a PurchaseOrderItem
     *
     * @param receivingDocument ReceivingDocument from which total parts received and damaged are to be set in PO
     * @param poDoc             Purchase order document to set total parts received and damaged
     * @return
     * @see org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl#updateReceivingTotalsOnPurchaseOrder(org.kuali.ole.module.purap.document.ReceivingDocument, org.kuali.ole.module.purap.document.PurchaseOrderDocument)
     */
    @Override
    protected void updateReceivingTotalsOnPurchaseOrder(ReceivingDocument receivingDocument, PurchaseOrderDocument poDoc) {
        LOG.debug("Inside updateReceivingTotalsOnPurchaseOrder of OleReceivingServiceImpl");
        super.updateReceivingTotalsOnPurchaseOrder(receivingDocument, poDoc);
        for (OleReceivingItem receivingItem : (List<OleReceivingItem>) receivingDocument.getItems()) {
            ItemType itemType = receivingItem.getItemType();
            if (!StringUtils.equalsIgnoreCase(itemType.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                //TODO: Chris - this method of getting the line out of po should be turned into a method that can get an item based on a combo or itemType and line
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) poDoc.getItemByLineNumber(receivingItem.getItemLineNumber());

                if (ObjectUtils.isNotNull(poItem)) {
                    KualiDecimal poItemPartsReceivedTotal = poItem.getItemReceivedTotalParts();

                    KualiDecimal receivingItemPartsReceivedOriginal = receivingItem.getItemOriginalReceivedTotalParts();

                    if (ObjectUtils.isNull(receivingItemPartsReceivedOriginal)) {
                        receivingItemPartsReceivedOriginal = KualiDecimal.ZERO;
                    }
                    KualiDecimal receivingItemPartsReceived = receivingItem.getItemReceivedTotalParts();
                    KualiDecimal receivingItemPartsTotalReceivedAdjusted = receivingItemPartsReceived.subtract(receivingItemPartsReceivedOriginal);

                    if (ObjectUtils.isNull(poItemPartsReceivedTotal)) {
                        poItemPartsReceivedTotal = KualiDecimal.ZERO;
                    }
                    KualiDecimal poItemPartsReceivedTotalAdjusted = poItemPartsReceivedTotal.add(receivingItemPartsTotalReceivedAdjusted);

                    KualiDecimal receivingItemPartsReturnedOriginal = receivingItem.getItemOriginalReturnedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsReturnedOriginal)) {
                        receivingItemPartsReturnedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsReturned = receivingItem.getItemReturnedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsReturned)) {
                        receivingItemPartsReturned = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalReturnedAdjusted = receivingItemPartsReturned.subtract(receivingItemPartsReturnedOriginal);

                    poItemPartsReceivedTotalAdjusted = poItemPartsReceivedTotalAdjusted.subtract(receivingItemPartsTotalReturnedAdjusted);

                    poItem.setItemReceivedTotalParts(poItemPartsReceivedTotalAdjusted);

                    KualiDecimal poPartsTotalDamaged = poItem.getItemDamagedTotalParts();
                    if (ObjectUtils.isNull(poPartsTotalDamaged)) {
                        poPartsTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamagedOriginal = receivingItem.getItemOriginalDamagedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsTotalDamagedOriginal)) {
                        receivingItemPartsTotalDamagedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamaged = receivingItem.getItemDamagedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsTotalDamaged)) {
                        receivingItemPartsTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamagedAdjusted = receivingItemPartsTotalDamaged.subtract(receivingItemPartsTotalDamagedOriginal);

                    poItem.setItemDamagedTotalParts(poPartsTotalDamaged.add(receivingItemPartsTotalDamagedAdjusted));
                }
            }
        }
        LOG.debug("Leaving updateReceivingTotalsOnPurchaseOrder of OleReceivingServiceImpl");
    }

    /**
     * This method is overridden to set total parts receiving and damaged in OlePurchaseOrderItem from OleLineItemReceivingItem
     *
     * @param rlItem Receiving line item from which total damaged and received parts are to be set in PO
     * @return
     * @see org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl#createPoItemFromReceivingLine(org.kuali.ole.module.purap.businessobject.LineItemReceivingItem)
     */
    @Override
    protected PurchaseOrderItem createPoItemFromReceivingLine(LineItemReceivingItem rlItem) {
        LOG.debug("Inside createPoItemFromReceivingLine of OleReceivingServiceImpl");
        OlePurchaseOrderItem poi = (OlePurchaseOrderItem) super.createPoItemFromReceivingLine(rlItem);
        OleLineItemReceivingItem oleRlItem = (OleLineItemReceivingItem) rlItem;
        poi.setItemDamagedTotalParts(oleRlItem.getItemDamagedTotalParts());
        poi.setItemReceivedTotalParts(oleRlItem.getItemReceivedTotalParts());
        poi.setBibInfoBean(oleRlItem.getBibInfoBean());
        poi.setItemTitleId(oleRlItem.getItemTitleId());
        LOG.debug("Leaving createPoItemFromReceivingLine of OleReceivingServiceImpl");
        return poi;
    }


    /**
     * This method is overridden to disable notes on a document when quantity returned or damaged is entered.
     *
     * @param recDoc Receiving Document
     * @return
     * @see org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl#createNoteForReturnedAndDamagedItems(org.kuali.ole.module.purap.document.ReceivingDocument)
     */
    @Override
    public void createNoteForReturnedAndDamagedItems(ReceivingDocument recDoc) {

    }

    /**
     * This method is overridden to include parts values in creating a list of fiscal officers for new unordered items added to a purchase order.
     *
     * @param recDoc ReceivingDocument
     * @return Fiscal officer list to send notification
     * @see org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl#createFyiFiscalOfficerList(org.kuali.ole.module.purap.document.ReceivingDocument)
     */
    @Override
    protected List<AdHocRoutePerson> createFyiFiscalOfficerList(ReceivingDocument recDoc) {
        LOG.debug("Inside createFyiFiscalOfficerList of OleReceivingServiceImpl");
        PurchaseOrderDocument po = recDoc.getPurchaseOrderDocument();
        List<AdHocRoutePerson> adHocRoutePersons = new ArrayList<AdHocRoutePerson>();
        Map fiscalOfficers = new HashMap();
        AdHocRoutePerson adHocRoutePerson = null;

        for (OleReceivingItem recItem : (List<OleReceivingItem>) recDoc.getItems()) {
            //if this item has an item line number then it is coming from the po
            if (ObjectUtils.isNotNull(recItem.getItemLineNumber())) {
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) po.getItemByLineNumber(recItem.getItemLineNumber());

                if ((poItem.getItemQuantity().isLessThan(poItem.getItemReceivedTotalQuantity()) ||
                        recItem.getItemDamagedTotalQuantity().isGreaterThan(KualiDecimal.ZERO))
                        && (((poItem.getItemNoOfParts().kualiDecimalValue()).isLessThan(poItem.getItemReceivedTotalParts()))
                        || recItem.getItemDamagedTotalParts().isGreaterThan(KualiDecimal.ZERO))) {

                    // loop through accounts and pull off fiscal officer
                    for (PurApAccountingLine account : poItem.getSourceAccountingLines()) {

                        //check for dupes of fiscal officer
                        if (fiscalOfficers.containsKey(account.getAccount().getAccountFiscalOfficerUser().getPrincipalName()) == false) {

                            //add fiscal officer to list
                            fiscalOfficers.put(account.getAccount().getAccountFiscalOfficerUser().getPrincipalName(), account.getAccount().getAccountFiscalOfficerUser().getPrincipalName());

                            //create AdHocRoutePerson object and add to list
                            adHocRoutePerson = new AdHocRoutePerson();
                            adHocRoutePerson.setId(account.getAccount().getAccountFiscalOfficerUser().getPrincipalName());
                            adHocRoutePerson.setActionRequested(OLEConstants.WORKFLOW_FYI_REQUEST);
                            adHocRoutePersons.add(adHocRoutePerson);
                        }
                    }

                }

            }
        }
        LOG.debug("Leaving createFyiFiscalOfficerList of OleReceivingServiceImpl");
        return adHocRoutePersons;
    }

    /**
     * This method is overridden to change annotation to include Parts
     *
     * @see org.kuali.ole.module.purap.document.service.impl.ReceivingServiceImpl#sendFyiForItems(org.kuali.ole.module.purap.document.ReceivingDocument)
     */
    @Override
    protected void sendFyiForItems(ReceivingDocument recDoc) {
        LOG.debug("Inside sendFyiForItems of OleReceivingServiceImpl");
        List<AdHocRoutePerson> fyiList = createFyiFiscalOfficerList(recDoc);
        String annotation = "Notification of Item exceeded Quantity/Parts or Damaged" + "(document id " + recDoc.getDocumentNumber() + ")";
        String responsibilityNote = "Please Review";

        for (AdHocRoutePerson adHocPerson : fyiList) {
            try {
                recDoc.appSpecificRouteDocumentToUser(
                        recDoc.getDocumentHeader().getWorkflowDocument(),
                        adHocPerson.getId(),
                        annotation,
                        responsibilityNote);
            } catch (WorkflowException e) {
                throw new RuntimeException("Error routing fyi for document with id " + recDoc.getDocumentNumber(), e);
            }

        }
        LOG.debug("Leaving sendFyiForItems of OleReceivingServiceImpl");
    }

    /**
     * This method deletes unneeded items and updates the totals on the po and does any additional processing based on items i.e.
     * FYI etc
     *
     * @param receivingDocument receiving document
     */
    @Override
    public void completeCorrectionReceivingDocument(ReceivingDocument correctionDocument) {
        ReceivingDocument receivingDoc = ((CorrectionReceivingDocument) correctionDocument)
                .getLineItemReceivingDocument();

        if (correctionDocument.getItems().size() >= 1) {
            for (OleCorrectionReceivingItem correctionItem : (List<OleCorrectionReceivingItem>) correctionDocument.getItems()) {
                if (!StringUtils.equalsIgnoreCase(correctionItem.getItemType().getItemTypeCode(),
                        PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                    int itemNumber = correctionItem.getItemLineNumber().intValue();
                    OleLineItemReceivingItem recItem = (OleLineItemReceivingItem) receivingDoc.getItem(correctionItem
                            .getItemLineNumber().intValue() - itemNumber);
                    List<PurchaseOrderItem> purchaseOrderItems = receivingDoc.getPurchaseOrderDocument().getItems();
                    PurchaseOrderItem poItem = purchaseOrderItems.get(correctionItem.getItemLineNumber().intValue()
                            - itemNumber);

                    if (ObjectUtils.isNotNull(recItem)) {
                        recItem.setItemReceivedTotalQuantity(correctionItem.getItemReceivedTotalQuantity());
                        recItem.setItemReceivedTotalParts(correctionItem.getItemReceivedTotalParts());
                        recItem.setItemReturnedTotalQuantity(correctionItem.getItemReturnedTotalQuantity());
                        recItem.setItemReturnedTotalParts(correctionItem.getItemReturnedTotalParts());
                        recItem.setItemDamagedTotalQuantity(correctionItem.getItemDamagedTotalQuantity());
                        recItem.setItemDamagedTotalParts(correctionItem.getItemDamagedTotalParts());
                        updateReceivingItemReceiptStatus(recItem, correctionItem);
                    }
                }
            }


        } else {
            for (CorrectionReceivingItem correctionItem : (List<CorrectionReceivingItem>) correctionDocument.getItems()) {
                if (!StringUtils.equalsIgnoreCase(correctionItem.getItemType().getItemTypeCode(),
                        PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                    LineItemReceivingItem recItem = (LineItemReceivingItem) receivingDoc.getItem(correctionItem
                            .getItemLineNumber().intValue() - 1);
                    List<PurchaseOrderItem> purchaseOrderItems = receivingDoc.getPurchaseOrderDocument().getItems();
                    PurchaseOrderItem poItem = purchaseOrderItems
                            .get(correctionItem.getItemLineNumber().intValue() - 1);

                    if (ObjectUtils.isNotNull(recItem)) {
                        recItem.setItemReceivedTotalQuantity(correctionItem.getItemReceivedTotalQuantity());
                        recItem.setItemReturnedTotalQuantity(correctionItem.getItemReturnedTotalQuantity());
                        recItem.setItemDamagedTotalQuantity(correctionItem.getItemDamagedTotalQuantity());
                    }
                }
            }
        }
    }

    public void updateReceivingItemReceiptStatus(OleLineItemReceivingItem recItem,
                                                 OleCorrectionReceivingItem correctionItem) {

        int finalCopiesReceived = (correctionItem.getItemReceivedTotalQuantity().subtract(
                correctionItem.getItemReturnedTotalQuantity()).intValue());
        int finalPartsReceived = (correctionItem.getItemReceivedTotalParts().subtract(
                correctionItem.getItemReturnedTotalParts()).intValue());
        if (correctionItem.getItemOriginalReceivedTotalQuantity().intValue() == finalCopiesReceived
                && correctionItem.getItemOriginalReceivedTotalParts().intValue() == finalPartsReceived) {
            recItem.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.RCV_RECEIPT_STATUS_RCVD));
        } else {
            if (finalCopiesReceived == 0 && finalCopiesReceived == 0) {
                recItem.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.RCV_RECEIPT_STATUS_NONE));
            } else {
                recItem.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.RCV_RECEIPT_STATUS_EXPTD));
            }
        }
    }

    public int getReceiptStatusDetails(String receiptStatusCd) {
        int receiptStatusId = 0;
        Map<String, String> receiptStatusCdMap = new HashMap<String, String>();
        receiptStatusCdMap.put(OLEConstants.RCPT_STATUS_CD, receiptStatusCd);
        List<OleReceiptStatus> oleReceiptStatusList = (List) SpringContext.getBean(BusinessObjectService.class)
                .findMatching(OleReceiptStatus.class, receiptStatusCdMap);
        for (OleReceiptStatus oleReceiptStatus : oleReceiptStatusList) {
            receiptStatusId = oleReceiptStatus.getReceiptStatusId().intValue();
        }
        return receiptStatusId;
    }

}

