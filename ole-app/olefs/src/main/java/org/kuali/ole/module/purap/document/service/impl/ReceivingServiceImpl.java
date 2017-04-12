/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderDocTypes;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.*;
import org.kuali.ole.module.purap.document.dataaccess.ReceivingDao;
import org.kuali.ole.module.purap.document.service.LogicContainer;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class ReceivingServiceImpl implements ReceivingService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReceivingServiceImpl.class);

    protected PurchaseOrderService purchaseOrderService;
    protected ReceivingDao receivingDao;
    protected DocumentService documentService;
    protected WorkflowDocumentService workflowDocumentService;
    protected ConfigurationService configurationService;
    protected PurapService purapService;
    protected NoteService noteService;
    private OleSelectDocumentService oleSelectDocumentService;

    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public void setReceivingDao(ReceivingDao receivingDao) {
        this.receivingDao = receivingDao;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        this.workflowDocumentService = workflowDocumentService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#populateReceivingLineFromPurchaseOrder(org.kuali.ole.module.purap.document.LineItemReceivingDocument)
     */
    @Override
    public void populateReceivingLineFromPurchaseOrder(LineItemReceivingDocument rlDoc) {


        //retrieve po by doc id
        PurchaseOrderDocument poDoc = null;
        poDoc = purchaseOrderService.getCurrentPurchaseOrder(rlDoc.getPurchaseOrderIdentifier());
        Integer activeItems = 0;
        for (PurchaseOrderItem poi : (List<PurchaseOrderItem>) poDoc.getItems()) {
            //TODO: Refactor this check into a service call. route FYI during submit
            if (poi.isItemActiveIndicator() &&
                    poi.getItemType().isQuantityBasedGeneralLedgerIndicator() &&
                    poi.getItemType().isLineItemIndicator()) {
                activeItems = activeItems + 1;
            }
        }

        if (rlDoc == null && activeItems > 0) {
            rlDoc = new LineItemReceivingDocument();
        }


        if (poDoc != null && activeItems > 0) {
            rlDoc.populateReceivingLineFromPurchaseOrder(poDoc);
        }

    }

    @Override
    public void populateCorrectionReceivingFromReceivingLine(CorrectionReceivingDocument rcDoc) {

        if (rcDoc == null) {
            rcDoc = new CorrectionReceivingDocument();
        }

        //retrieve receiving line by doc id
        LineItemReceivingDocument rlDoc = rcDoc.getLineItemReceivingDocument();

        if (rlDoc != null) {
            rcDoc.populateCorrectionReceivingFromReceivingLine(rlDoc);
        }

    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#populateAndSaveLineItemReceivingDocument(org.kuali.ole.module.purap.document.LineItemReceivingDocument)
     */
    @Override
    public void populateAndSaveLineItemReceivingDocument(LineItemReceivingDocument rlDoc) throws WorkflowException {
        try {
            documentService.saveDocument(rlDoc, AttributedContinuePurapEvent.class);
        } catch (WorkflowException we) {
            String errorMsg = "Error saving document # " + rlDoc.getDocumentHeader().getDocumentNumber() + " " + we.getMessage();
            //LOG.error(errorMsg, we);
            throw new RuntimeException(errorMsg, we);
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#populateCorrectionReceivingDocument(org.kuali.ole.module.purap.document.CorrectionReceivingDocument)
     */
    @Override
    public void populateCorrectionReceivingDocument(CorrectionReceivingDocument rcDoc) {
        populateCorrectionReceivingFromReceivingLine(rcDoc);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#canCreateLineItemReceivingDocument(java.lang.Integer, java.lang.String)
     */
    @Override
    public boolean canCreateLineItemReceivingDocument(Integer poId, String receivingDocumentNumber) throws RuntimeException {

        PurchaseOrderDocument po = purchaseOrderService.getCurrentPurchaseOrder(poId);

        return canCreateLineItemReceivingDocument(po, receivingDocumentNumber);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#canCreateLineItemReceivingDocument(org.kuali.ole.module.purap.document.PurchaseOrderDocument)
     */
    @Override
    public boolean canCreateLineItemReceivingDocument(PurchaseOrderDocument po) throws RuntimeException {
        return canCreateLineItemReceivingDocument(po, null);
    }

    public boolean canCreateLineItemReceivingDocument(PurchaseOrderDocument po, String receivingDocumentNumber) {
        boolean canCreate = false;

        if (isPurchaseOrderValidForLineItemReceivingDocumentCreation(po) &&
                !isLineItemReceivingDocumentInProcessForPurchaseOrder(po.getPurapDocumentIdentifier(), receivingDocumentNumber) &&
                !isCorrectionReceivingDocumentInProcessForPurchaseOrder(po.getPurapDocumentIdentifier(), null)) {
            canCreate = true;
        }

        return canCreate;
    }

    @Override
    public boolean isPurchaseOrderActiveForLineItemReceivingDocumentCreation(Integer poId) {
        PurchaseOrderDocument po = purchaseOrderService.getCurrentPurchaseOrder(poId);
        return isPurchaseOrderValidForLineItemReceivingDocumentCreation(po);
    }

    protected boolean isPurchaseOrderValidForLineItemReceivingDocumentCreation(PurchaseOrderDocument po) {
        return po != null &&
                ObjectUtils.isNotNull(po.getPurapDocumentIdentifier()) &&
                po.isPurchaseOrderCurrentIndicator() &&
                (PurchaseOrderStatuses.APPDOC_OPEN.equals(po.getApplicationDocumentStatus()) ||
                        PurchaseOrderStatuses.APPDOC_CLOSED.equals(po.getApplicationDocumentStatus()) ||
                        PurchaseOrderStatuses.APPDOC_PAYMENT_HOLD.equals(po.getApplicationDocumentStatus()));
    }

    @Override
    public boolean canCreateCorrectionReceivingDocument(LineItemReceivingDocument rl) throws RuntimeException {
        return canCreateCorrectionReceivingDocument(rl, null);
    }

    @Override
    public boolean canCreateCorrectionReceivingDocument(LineItemReceivingDocument rl, String receivingCorrectionDocNumber) throws RuntimeException {

        boolean canCreate = false;
        WorkflowDocument workflowDocument = null;

        try {
            workflowDocument = workflowDocumentService.loadWorkflowDocument(rl.getDocumentNumber(), GlobalVariables.getUserSession().getPerson());
        } catch (WorkflowException we) {
            throw new RuntimeException(we);
        }

        if (workflowDocument.isFinal() &&
                !isCorrectionReceivingDocumentInProcessForReceivingLine(rl.getDocumentNumber(), receivingCorrectionDocNumber)) {
            canCreate = true;
        }

        return canCreate;
    }

    protected boolean isLineItemReceivingDocumentInProcessForPurchaseOrder(Integer poId, String receivingDocumentNumber) throws RuntimeException {
        return !getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(poId, receivingDocumentNumber).isEmpty();
    }

    @Override
    public List<String> getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(Integer poId,
                                                                                     String receivingDocumentNumber) {

        List<String> inProcessDocNumbers = new ArrayList<String>();
        List<String> docNumbers = receivingDao.getDocumentNumbersByPurchaseOrderId(poId);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (!(workflowDocument.isCanceled() ||
                    workflowDocument.isException() ||
                    workflowDocument.isFinal()) &&
                    docNumber.equals(receivingDocumentNumber) == false) {
                inProcessDocNumbers.add(docNumber);
            }
        }

        return inProcessDocNumbers;
    }

    @Override
    public List<LineItemReceivingDocument> getLineItemReceivingDocumentsInFinalForPurchaseOrder(Integer poId) {

        List<String> finalDocNumbers = new ArrayList<String>();
        List<String> docNumbers = receivingDao.getDocumentNumbersByPurchaseOrderId(poId);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (workflowDocument.isFinal()) {
                finalDocNumbers.add(docNumber);
            }
        }

        if (finalDocNumbers.size() > 0) {
            try {
                List<LineItemReceivingDocument> docs = new ArrayList<LineItemReceivingDocument>();
                for (Document doc : documentService.getDocumentsByListOfDocumentHeaderIds(LineItemReceivingDocument.class, finalDocNumbers)) {
                    docs.add((LineItemReceivingDocument) doc);
                }
                return docs;
            } catch (WorkflowException e) {
                throw new IllegalArgumentException("unable to retrieve LineItemReceivingDocuments", e);
            }
        } else {
            return null;
        }

    }

    protected boolean isCorrectionReceivingDocumentInProcessForPurchaseOrder(Integer poId, String receivingDocumentNumber) throws RuntimeException {
        return !getCorrectionReceivingDocumentNumbersInProcessForPurchaseOrder(poId, receivingDocumentNumber).isEmpty();
    }

    @Override
    public List<String> getCorrectionReceivingDocumentNumbersInProcessForPurchaseOrder(Integer poId,
                                                                                       String receivingDocumentNumber) {

        boolean isInProcess = false;

        List<String> inProcessDocNumbers = new ArrayList<String>();
        List<String> docNumbers = receivingDao.getCorrectionReceivingDocumentNumbersByPurchaseOrderId(poId);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (!(workflowDocument.isCanceled() ||
                    workflowDocument.isException() ||
                    workflowDocument.isFinal()) &&
                    docNumber.equals(receivingDocumentNumber) == false) {
                inProcessDocNumbers.add(docNumber);
            }
        }

        return inProcessDocNumbers;
    }


    protected boolean isCorrectionReceivingDocumentInProcessForReceivingLine(String receivingDocumentNumber, String receivingCorrectionDocNumber) throws RuntimeException {

        boolean isInProcess = false;

        List<String> docNumbers = receivingDao.getCorrectionReceivingDocumentNumbersByReceivingLineNumber(receivingDocumentNumber);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (!(workflowDocument.isCanceled() ||
                    workflowDocument.isException() ||
                    workflowDocument.isFinal()) &&
                    docNumber.equals(receivingCorrectionDocNumber) == false) {

                isInProcess = true;
                break;
            }
        }

        return isInProcess;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#receivingLineDuplicateMessages(org.kuali.ole.module.purap.document.LineItemReceivingDocument)
     */
    @Override
    public HashMap<String, String> receivingLineDuplicateMessages(LineItemReceivingDocument rlDoc) {
        HashMap<String, String> msgs;
        msgs = new HashMap<String, String>();
        Integer poId = rlDoc.getPurchaseOrderIdentifier();
        StringBuffer currentMessage = new StringBuffer("");
        List<String> docNumbers = null;

        //check vendor date for duplicates
        if (rlDoc.getShipmentReceivedDate() != null) {
            docNumbers = receivingDao.duplicateVendorDate(poId, rlDoc.getShipmentReceivedDate());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_VENDOR_DATE, rlDoc.getPurchaseOrderIdentifier());
            }
        }

        //check packing slip number for duplicates
        if (!StringUtils.isEmpty(rlDoc.getShipmentPackingSlipNumber())) {
            docNumbers = receivingDao.duplicatePackingSlipNumber(poId, rlDoc.getShipmentPackingSlipNumber());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_PACKING_SLIP_NUMBER, rlDoc.getPurchaseOrderIdentifier());
            }
        }

        //check bill of lading number for duplicates
        if (!StringUtils.isEmpty(rlDoc.getShipmentBillOfLadingNumber())) {
            docNumbers = receivingDao.duplicateBillOfLadingNumber(poId, rlDoc.getShipmentBillOfLadingNumber());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_BILL_OF_LADING_NUMBER, rlDoc.getPurchaseOrderIdentifier());
            }
        }

        //add message if one exists
        if (currentMessage.length() > 0) {
            //add suffix
            appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_SUFFIX, rlDoc.getPurchaseOrderIdentifier());

            //add msg to map
            msgs.put(PurapConstants.LineItemReceivingDocumentStrings.DUPLICATE_RECEIVING_LINE_QUESTION, currentMessage.toString());
        }

        return msgs;
    }

    /**
     * Looks at a list of doc numbers, but only considers an entry duplicate
     * if the document is in a Final status.
     *
     * @param docNumbers
     * @return
     */
    protected boolean hasDuplicateEntry(List<String> docNumbers) {

        boolean isDuplicate = false;
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            //if the doc number exists, and is in final status, consider this a dupe and return
            if (workflowDocument.isFinal()) {
                isDuplicate = true;
                break;
            }
        }

        return isDuplicate;

    }

    protected void appendDuplicateMessage(StringBuffer currentMessage, String duplicateMessageKey, Integer poId) {

        //append prefix if this is first call
        if (currentMessage.length() == 0) {
            String messageText = configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_PREFIX);
            String prefix = MessageFormat.format(messageText, poId.toString());

            currentMessage.append(prefix);
        }

        //append message
        currentMessage.append(configurationService.getPropertyValueAsString(duplicateMessageKey));
    }

    @Override
    public void completeCorrectionReceivingDocument(ReceivingDocument correctionDocument) {

        ReceivingDocument receivingDoc = ((CorrectionReceivingDocument) correctionDocument).getLineItemReceivingDocument();

        for (CorrectionReceivingItem correctionItem : (List<CorrectionReceivingItem>) correctionDocument.getItems()) {
            if (!StringUtils.equalsIgnoreCase(correctionItem.getItemType().getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {

                LineItemReceivingItem recItem = (LineItemReceivingItem) receivingDoc.getItem(correctionItem.getItemLineNumber().intValue() - 1);
                List<PurchaseOrderItem> purchaseOrderItems = receivingDoc.getPurchaseOrderDocument().getItems();
                PurchaseOrderItem poItem = purchaseOrderItems.get(correctionItem.getItemLineNumber().intValue() - 1);

                if (ObjectUtils.isNotNull(recItem)) {
                    recItem.setItemReceivedTotalQuantity(correctionItem.getItemReceivedTotalQuantity());
                    recItem.setItemReturnedTotalQuantity(correctionItem.getItemReturnedTotalQuantity());
                    recItem.setItemDamagedTotalQuantity(correctionItem.getItemDamagedTotalQuantity());
                }
            }
        }

    }

    /**
     * This method deletes unneeded items and updates the totals on the po and does any additional processing based on items i.e. FYI etc
     *
     * @param receivingDocument receiving document
     */
    @Override
    public void completeReceivingDocument(ReceivingDocument receivingDocument) {

        PurchaseOrderDocument poDoc = null;

        if (receivingDocument instanceof LineItemReceivingDocument) {
            // delete unentered items
            purapService.deleteUnenteredItems(receivingDocument);
            poDoc = receivingDocument.getPurchaseOrderDocument();
       //     SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(poDoc);
        } else if (receivingDocument instanceof CorrectionReceivingDocument) {
            CorrectionReceivingDocument correctionDocument = (CorrectionReceivingDocument) receivingDocument;
            poDoc = purchaseOrderService.getCurrentPurchaseOrder(correctionDocument.getLineItemReceivingDocument().getPurchaseOrderIdentifier());
        }

        updateReceivingTotalsOnPurchaseOrder(receivingDocument, poDoc);

        //TODO: custom doc specific service hook here for correction to do it's receiving doc update

     //   purapService.saveDocumentNoValidation(poDoc);

        // sendFyiForItems(receivingDocument);

        spawnPoAmendmentForUnorderedItems(receivingDocument, poDoc);

        purapService.saveDocumentNoValidation(receivingDocument);
    }

    @Override
    public void createNoteForReturnedAndDamagedItems(ReceivingDocument recDoc) {

        for (ReceivingItem item : (List<ReceivingItem>) recDoc.getItems()) {
            if (!StringUtils.equalsIgnoreCase(item.getItemType().getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                if (item.getItemReturnedTotalQuantity() != null && item.getItemReturnedTotalQuantity().isGreaterThan(KualiDecimal.ZERO)) {
                    try {
                        String noteString = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.MESSAGE_RECEIVING_LINEITEM_RETURN_NOTE_TEXT);
                        noteString = item.getItemReturnedTotalQuantity().intValue() + " " + noteString + " " + item.getItemLineNumber();
                        addNoteToReceivingDocument(recDoc, noteString);
                    } catch (Exception e) {
                        String errorMsg = "Note Service Exception caught: " + e.getLocalizedMessage();
                        throw new RuntimeException(errorMsg, e);
                    }
                }

                if (item.getItemDamagedTotalQuantity() != null && item.getItemDamagedTotalQuantity().isGreaterThan(KualiDecimal.ZERO)) {
                    try {
                        String noteString = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.MESSAGE_RECEIVING_LINEITEM_DAMAGE_NOTE_TEXT);
                        noteString = item.getItemDamagedTotalQuantity().intValue() + " " + noteString + " " + item.getItemLineNumber();
                        addNoteToReceivingDocument(recDoc, noteString);
                    } catch (Exception e) {
                        String errorMsg = "Note Service Exception caught: " + e.getLocalizedMessage();
                        throw new RuntimeException(errorMsg, e);
                    }
                }
            }
        }
    }

    protected void updateReceivingTotalsOnPurchaseOrder(ReceivingDocument receivingDocument, PurchaseOrderDocument poDoc) {
        for (OleReceivingItem receivingItem : (List<OleReceivingItem>) receivingDocument.getItems()) {
            ItemType itemType = receivingItem.getItemType();
            if (!StringUtils.equalsIgnoreCase(itemType.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                //TODO: Chris - this method of getting the line out of po should be turned into a method that can get an item based on a combo or itemType and line
                OlePurchaseOrderItem poItem = (OlePurchaseOrderItem) poDoc.getItemByLineNumber(receivingItem
                        .getItemLineNumber());

                if (ObjectUtils.isNotNull(poItem)) {

                    KualiDecimal poItemReceivedTotal = poItem.getItemReceivedTotalQuantity();

                    KualiDecimal receivingItemReceivedOriginal = receivingItem.getItemOriginalReceivedTotalQuantity();
                    /**
                     * FIXME: It's coming as null although we set the default value in the LineItemReceivingItem constructor - mpv
                     */
                    if (ObjectUtils.isNull(receivingItemReceivedOriginal)) {
                        receivingItemReceivedOriginal = KualiDecimal.ZERO;
                    }
                    KualiDecimal receivingItemReceived = receivingItem.getItemReceivedTotalQuantity();
                    KualiDecimal receivingItemTotalReceivedAdjested = receivingItemReceived.subtract(receivingItemReceivedOriginal);

                    if (ObjectUtils.isNull(poItemReceivedTotal)) {
                        poItemReceivedTotal = KualiDecimal.ZERO;
                    }
                    KualiDecimal poItemReceivedTotalAdjusted = poItemReceivedTotal.add(receivingItemTotalReceivedAdjested);

                    KualiDecimal receivingItemReturnedOriginal = receivingItem.getItemOriginalReturnedTotalQuantity();
                    if (ObjectUtils.isNull(receivingItemReturnedOriginal)) {
                        receivingItemReturnedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemReturned = receivingItem.getItemReturnedTotalQuantity();
                    if (ObjectUtils.isNull(receivingItemReturned)) {
                        receivingItemReturned = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemTotalReturnedAdjusted = receivingItemReturned.subtract(receivingItemReturnedOriginal);

                    poItemReceivedTotalAdjusted = poItemReceivedTotalAdjusted.subtract(receivingItemTotalReturnedAdjusted);

                    poItem.setItemReceivedTotalQuantity(poItemReceivedTotalAdjusted);

                    KualiDecimal poTotalDamaged = poItem.getItemDamagedTotalQuantity();
                    if (ObjectUtils.isNull(poTotalDamaged)) {
                        poTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemTotalDamagedOriginal = receivingItem.getItemOriginalDamagedTotalQuantity();
                    if (ObjectUtils.isNull(receivingItemTotalDamagedOriginal)) {
                        receivingItemTotalDamagedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemTotalDamaged = receivingItem.getItemDamagedTotalQuantity();
                    if (ObjectUtils.isNull(receivingItemTotalDamaged)) {
                        receivingItemTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemTotalDamagedAdjusted = receivingItemTotalDamaged.subtract(receivingItemTotalDamagedOriginal);

                    poItem.setItemDamagedTotalQuantity(poTotalDamaged.add(receivingItemTotalDamagedAdjusted));
                    // Updating the Total Parts
                    KualiDecimal poItemPartsReceivedTotal = poItem.getItemReceivedTotalParts();

                    KualiDecimal receivingItemPartsReceivedOriginal = receivingItem.getItemOriginalReceivedTotalParts();

                    if (ObjectUtils.isNull(receivingItemPartsReceivedOriginal)) {
                        receivingItemPartsReceivedOriginal = KualiDecimal.ZERO;
                    }
                    KualiDecimal receivingItemPartsReceived = receivingItem.getItemReceivedTotalParts();
                    KualiDecimal receivingItemPartsTotalReceivedAdjusted = receivingItemPartsReceived
                            .subtract(receivingItemPartsReceivedOriginal);

                    if (ObjectUtils.isNull(poItemPartsReceivedTotal)) {
                        poItemPartsReceivedTotal = KualiDecimal.ZERO;
                    }
                    KualiDecimal poItemPartsReceivedTotalAdjusted = poItemPartsReceivedTotal
                            .add(receivingItemPartsTotalReceivedAdjusted);

                    KualiDecimal receivingItemPartsReturnedOriginal = receivingItem.getItemOriginalReturnedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsReturnedOriginal)) {
                        receivingItemPartsReturnedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsReturned = receivingItem.getItemReturnedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsReturned)) {
                        receivingItemPartsReturned = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalReturnedAdjusted = receivingItemPartsReturned
                            .subtract(receivingItemPartsReturnedOriginal);

                    poItemPartsReceivedTotalAdjusted = poItemPartsReceivedTotalAdjusted
                            .subtract(receivingItemPartsTotalReturnedAdjusted);

                    poItem.setItemReceivedTotalParts(poItemPartsReceivedTotalAdjusted);

                    KualiDecimal poPartsTotalDamaged = poItem.getItemDamagedTotalParts();
                    if (ObjectUtils.isNull(poPartsTotalDamaged)) {
                        poPartsTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamagedOriginal = receivingItem
                            .getItemOriginalDamagedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsTotalDamagedOriginal)) {
                        receivingItemPartsTotalDamagedOriginal = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamaged = receivingItem.getItemDamagedTotalParts();
                    if (ObjectUtils.isNull(receivingItemPartsTotalDamaged)) {
                        receivingItemPartsTotalDamaged = KualiDecimal.ZERO;
                    }

                    KualiDecimal receivingItemPartsTotalDamagedAdjusted = receivingItemPartsTotalDamaged
                            .subtract(receivingItemPartsTotalDamagedOriginal);

                    poItem.setItemDamagedTotalParts(poPartsTotalDamaged.add(receivingItemPartsTotalDamagedAdjusted));

                }
            }
        }
    }

    /**
     * Spawns PO amendments for new unordered items on a receiving document.
     *
     * @param receivingDocument
     * @param po
     */
    protected void spawnPoAmendmentForUnorderedItems(ReceivingDocument receivingDocument, PurchaseOrderDocument po) {

        //if receiving line document
        if (receivingDocument instanceof LineItemReceivingDocument) {
            LineItemReceivingDocument rlDoc = (LineItemReceivingDocument) receivingDocument;
            final PurchaseOrderDocument currentDocument = po;

            //if a new item has been added spawn a purchase order amendment
            if (hasNewUnorderedItem((LineItemReceivingDocument) receivingDocument)) {
                String newSessionUserId = getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER);
                try {

                    LogicContainer logicToRun = new LogicContainer() {
                        @Override
                        public Object runLogic(Object[] objects) throws Exception {
                            LineItemReceivingDocument rlDoc = (LineItemReceivingDocument) objects[0];
                            String poDocNumber = (String) objects[1];

                            //create a PO amendment
                            PurchaseOrderAmendmentDocument amendmentPo = (PurchaseOrderAmendmentDocument) purchaseOrderService.createAndSavePotentialChangeDocument(currentDocument, PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT, PurchaseOrderStatuses.APPDOC_AMENDMENT);

                            //add new lines to amendement
                            addUnorderedItemsToAmendment(amendmentPo, rlDoc);

                            //route amendment
                            //documentService.routeDocument(amendmentPo, null, null);
                            //save document
                            documentService.saveDocument(amendmentPo);
                            //add note to amendment po document
                            String note = "Purchase Order Amendment " + amendmentPo.getPurapDocumentIdentifier() + " (document id " + amendmentPo.getDocumentNumber() + ") created for new unordered line items due to Receiving (document id " + rlDoc.getDocumentNumber() + ")";

                            Note noteObj = documentService.createNoteFromDocument(amendmentPo, note);
                            amendmentPo.addNote(noteObj);
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
    }

    /**
     * Checks the item list for newly added items.
     *
     * @param rlDoc
     * @return
     */
    @Override
    public boolean hasNewUnorderedItem(LineItemReceivingDocument rlDoc) {

        boolean itemAdded = false;

        for (LineItemReceivingItem rlItem : (List<LineItemReceivingItem>) rlDoc.getItems()) {
            if (PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE.equals(rlItem.getItemTypeCode()) &&
                    !StringUtils.isEmpty(rlItem.getItemReasonAddedCode())) {
                itemAdded = true;
                break;
            }
        }

        return itemAdded;
    }

    /**
     * Adds an unordered item to a po amendment document.
     *
     * @param amendment
     * @param rlDoc
     */
    protected void addUnorderedItemsToAmendment(PurchaseOrderAmendmentDocument amendment, LineItemReceivingDocument rlDoc) {

        PurchaseOrderItem poi = null;
        // Added for jira OLE-2515
//        OlePurchaseOrderItem  opoi = null;

        for (OleLineItemReceivingItem rlItem : (List<OleLineItemReceivingItem>) rlDoc.getItems()) {

            if (PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE.equals(rlItem.getItemTypeCode()) &&
                    !StringUtils.isEmpty(rlItem.getItemReasonAddedCode())) {

                poi = createPoItemFromReceivingLine(rlItem);
                poi.setDocumentNumber(amendment.getDocumentNumber());
                // add default commodity code from parameter, if commodity code is required on PO and not specified in the unordered item
                // Note: if we don't add logic to populate commodity code in LineItemReceivingItem, then at this point this field is always empty for unordered item
                if (purchaseOrderService.isCommodityCodeRequiredOnPurchaseOrder() && StringUtils.isEmpty(poi.getPurchasingCommodityCode())) {
                    String defaultCommodityCode = SpringContext.getBean(ParameterService.class).getParameterValueAsString(PurchaseOrderAmendmentDocument.class, PurapParameterConstants.UNORDERED_ITEM_DEFAULT_COMMODITY_CODE);
                    poi.setPurchasingCommodityCode(defaultCommodityCode);
                }
                poi.refreshNonUpdateableReferences();
                amendment.addItem(poi);
            }
        }

    }

    /**
     * Creates a PO item from a receiving line item.
     *
     * @param rlItem
     * @return
     */
    protected PurchaseOrderItem createPoItemFromReceivingLine(LineItemReceivingItem rlItem) {

        OlePurchaseOrderItem opoi = new OlePurchaseOrderItem();
        PurchaseOrderItem poi = new OlePurchaseOrderItem();

        poi.setItemActiveIndicator(true);
        poi.setItemTypeCode(rlItem.getItemTypeCode());
        poi.setItemLineNumber(rlItem.getItemLineNumber());
        poi.setItemCatalogNumber(rlItem.getItemCatalogNumber());
        poi.setItemDescription(rlItem.getItemDescription());

        if (rlItem.getItemReturnedTotalQuantity() == null) {
            poi.setItemQuantity(rlItem.getItemReceivedTotalQuantity());
        } else {
            poi.setItemQuantity(rlItem.getItemReceivedTotalQuantity().subtract(rlItem.getItemReturnedTotalQuantity()));
        }

        poi.setItemUnitOfMeasureCode(rlItem.getItemUnitOfMeasureCode());
        poi.setItemUnitPrice(new BigDecimal(0));

        poi.setItemDamagedTotalQuantity(rlItem.getItemDamagedTotalQuantity());
        poi.setItemReceivedTotalQuantity(rlItem.getItemReceivedTotalQuantity());

        return poi;
    }


    /**
     * Creates a PO item from a receiving line item.
     *
     * @param rlItem
     * @return
     */
    protected OlePurchaseOrderItem createPoItemFromReceivingLine(OleLineItemReceivingItem rlItem) {

        //  OlePurchaseOrderItem opoi = new OlePurchaseOrderItem();
        //  (OleLineItemReceivingItem) rlItem.getItemT
        //opoi.setItemTitleId(rlItem.getitem);
        OlePurchaseOrderItem poi = new OlePurchaseOrderItem();

        poi.setItemTitleId(rlItem.getItemTitleId());
        poi.setItemActiveIndicator(true);
        poi.setItemTypeCode(rlItem.getItemTypeCode());
        poi.setItemLineNumber(rlItem.getItemLineNumber());
        poi.setItemCatalogNumber(rlItem.getItemCatalogNumber());
        poi.setItemDescription(rlItem.getItemDescription());

        if (rlItem.getItemReturnedTotalQuantity() == null) {
            poi.setItemQuantity(rlItem.getItemReceivedTotalQuantity());
        } else {
            poi.setItemQuantity(rlItem.getItemReceivedTotalQuantity().subtract(rlItem.getItemReturnedTotalQuantity()));
        }

        poi.setItemUnitOfMeasureCode(rlItem.getItemUnitOfMeasureCode());
        poi.setItemUnitPrice(new BigDecimal(0));

        poi.setItemDamagedTotalQuantity(rlItem.getItemDamagedTotalQuantity());
        poi.setItemReceivedTotalQuantity(rlItem.getItemReceivedTotalQuantity());
        poi.setItemNoOfParts(new KualiInteger(rlItem.getItemReceivedTotalParts().intValue()));

        List<OleCopy> copyList = rlItem.getCopyList() != null ? rlItem.getCopyList() : new ArrayList<OleCopy>();
        Integer receivedCount = 0;
        for (OleCopy oleCopy : copyList) {
            if (oleCopy.getReceiptStatus().equalsIgnoreCase("Received")) {
                receivedCount++;
            }
        }
        if (receivedCount == 0) {
            rlItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_NOT_RECEIVED));
        } else if (receivedCount == copyList.size()) {
            rlItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED));
        } else {
            rlItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_PARTIALLY_RECEIVED));
        }
        poi.setReceiptStatusId(rlItem.getReceiptStatusId());
        if (poi.getItemQuantity().equals(new KualiDecimal(1)) && poi.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            poi.setNoOfCopiesReceived("N/A");
            poi.setNoOfPartsReceived(receivedCount.toString());
        } else if (poi.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && poi.getItemNoOfParts().equals(new KualiDecimal(1))) {
            poi.setNoOfCopiesReceived(receivedCount.toString());
            poi.setNoOfPartsReceived("N/A");
        } else if (poi.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && poi.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            poi.setNoOfCopiesReceived("See Copies Section");
            poi.setNoOfPartsReceived("See Copies Section");
        }
       /* poi.setNoOfCopiesReceived((poi.getNoOfCopiesReceived().add(new KualiInteger(rlItem
                .getItemReceivedTotalQuantity().bigDecimalValue()))).subtract(new KualiInteger(rlItem
                .getItemReturnedTotalQuantity().bigDecimalValue())));
        poi.setNoOfPartsReceived((poi.getNoOfPartsReceived().add(new KualiInteger(rlItem.getItemReceivedTotalParts()
                .bigDecimalValue()))).subtract(new KualiInteger(rlItem.getItemReturnedTotalParts().bigDecimalValue())));
        if (poi.getItemQuantity().intValue() == poi.getNoOfCopiesReceived().intValue()
                && poi.getItemNoOfParts().intValue() == poi.getNoOfPartsReceived().intValue()) {
            poi.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED));
        }
        else {
            if (poi.getNoOfPartsReceived().isZero() && poi.getNoOfCopiesReceived().isZero()) {
                poi.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_NOT_RECEIVED));
            }
            else {
                poi.setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_PARTIALLY_RECEIVED));
            }
        }
        List<OleLineItemReceivingDoc> oleLineItemReceivingItemDocList = new ArrayList<OleLineItemReceivingDoc>();
        oleLineItemReceivingItemDocList = rlItem.getOleLineItemReceivingItemDocList();
        Iterator iterator = oleLineItemReceivingItemDocList.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof OleLineItemReceivingDoc) {
                OleLineItemReceivingDoc oleLineItemReceivingDoc = (OleLineItemReceivingDoc) object;
                if (rlItem.getReceivingItemIdentifier().intValue() == oleLineItemReceivingDoc.getReceivingLineItem()
                        .getReceivingItemIdentifier()) {
                    if (null != oleLineItemReceivingDoc.getItemTitleId()) {
                        poi.setItemTitleId(oleLineItemReceivingDoc.getItemTitleId());
                        List<String> itemTitleIdsList = new ArrayList<String>();

                        itemTitleIdsList.add(rlItem.getItemTitleId());

                    }
                    if (null != oleLineItemReceivingDoc.getItemTitleId()) {
                        if (rlItem.getItemReceivedTotalQuantity().isGreaterThan(new KualiDecimal(1))
                                || rlItem.getItemReceivedTotalParts().isGreaterThan(new KualiDecimal(1))) {
                            List<OleCopies> copies = rlItem.getCopies();
                                poi.setCopies(copies);

                        }

                    }
                }
            }
        }    */

        return poi;
    }

    /**
     * Creates a list of fiscal officers for new unordered items added to a purchase order.
     *
     * @param po
     * @return
     */
    protected List<AdHocRoutePerson> createFyiFiscalOfficerList(ReceivingDocument recDoc) {

        PurchaseOrderDocument po = recDoc.getPurchaseOrderDocument();
        List<AdHocRoutePerson> adHocRoutePersons = new ArrayList<AdHocRoutePerson>();
        Map fiscalOfficers = new HashMap();
        AdHocRoutePerson adHocRoutePerson = null;

        for (ReceivingItem recItem : (List<ReceivingItem>) recDoc.getItems()) {
            //if this item has an item line number then it is coming from the po
            if (ObjectUtils.isNotNull(recItem.getItemLineNumber())) {
                PurchaseOrderItem poItem = (PurchaseOrderItem) po.getItemByLineNumber(recItem.getItemLineNumber());

                if (poItem.getItemQuantity().isLessThan(poItem.getItemReceivedTotalQuantity()) ||
                        recItem.getItemDamagedTotalQuantity().isGreaterThan(KualiDecimal.ZERO)) {

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

        return adHocRoutePersons;
    }

    /**
     * Sends an FYI to fiscal officers for new unordered items.
     *
     * @param po
     */
    protected void sendFyiForItems(ReceivingDocument recDoc) {

        List<AdHocRoutePerson> fyiList = createFyiFiscalOfficerList(recDoc);
        String annotation = "Notification of Item exceeded Quantity or Damaged" + "(document id " + recDoc.getDocumentNumber() + ")";
        String responsibilityNote = "Please Review";

        for (AdHocRoutePerson adHocPerson : fyiList) {
            try {
                recDoc.appSpecificRouteDocumentToUser(
                        recDoc.getDocumentHeader().getWorkflowDocument(),
                        adHocPerson.getPerson().getPrincipalId(),
                        annotation,
                        responsibilityNote);
            } catch (WorkflowException e) {
                throw new RuntimeException("Error routing fyi for document with id " + recDoc.getDocumentNumber(), e);
            }

        }
    }

    @Override
    public void addNoteToReceivingDocument(ReceivingDocument receivingDocument, String note) throws Exception {
        Note noteObj = documentService.createNoteFromDocument(receivingDocument, note);
        receivingDocument.addNote(noteObj);
        noteService.save(noteObj);
    }

    @Override
    public String getReceivingDeliveryCampusCode(PurchaseOrderDocument po) {
        String deliveryCampusCode = "";
        String latestDocumentNumber = "";

        List<LineItemReceivingView> rViews = null;
        WorkflowDocument workflowDocument = null;
        DateTime latestCreateDate = null;

        //get related views
        if (ObjectUtils.isNotNull(po.getRelatedViews())) {
            rViews = po.getRelatedViews().getRelatedLineItemReceivingViews();
        }

        //if not empty, then grab the latest receiving view
        if (ObjectUtils.isNotNull(rViews) && rViews.isEmpty() == false) {

            for (LineItemReceivingView rView : rViews) {
                try {
                    workflowDocument = workflowDocumentService.loadWorkflowDocument(rView.getDocumentNumber(), GlobalVariables.getUserSession().getPerson());

                    //if latest create date is null or the latest is before the current, current is newer
                    if (ObjectUtils.isNull(latestCreateDate) || latestCreateDate.isBefore(workflowDocument.getDateCreated())) {
                        latestCreateDate = workflowDocument.getDateCreated();
                        latestDocumentNumber = workflowDocument.getDocumentId().toString();
                    }
                } catch (WorkflowException we) {
                    throw new RuntimeException(we);
                }
            }

            //if there is a create date, a latest workflow doc was found
            if (ObjectUtils.isNotNull(latestCreateDate)) {
                try {
                    LineItemReceivingDocument rlDoc = (LineItemReceivingDocument) documentService.getByDocumentHeaderId(latestDocumentNumber);
                    deliveryCampusCode = rlDoc.getDeliveryCampusCode();
                } catch (WorkflowException we) {
                    throw new RuntimeException(we);
                }
            }
        }

        return deliveryCampusCode;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.ReceivingService#isLineItemReceivingDocumentGeneratedForPurchaseOrder(java.lang.Integer)
     */
    @Override
    public boolean isLineItemReceivingDocumentGeneratedForPurchaseOrder(Integer poId) throws RuntimeException {

        boolean isGenerated = false;

        List<String> docNumbers = receivingDao.getDocumentNumbersByPurchaseOrderId(poId);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber, GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (workflowDocument.isFinal()) {
                isGenerated = true;
                break;
            }
        }

        return isGenerated;
    }

    @Override
    public void approveReceivingDocsForPOAmendment() {
        List<String> docNumbers = getDocumentsNumbersAwaitingPurchaseOrderOpenStatus();
        List<LineItemReceivingDocument> docs = new ArrayList<LineItemReceivingDocument>();
        for (String docNumber : docNumbers) {
            LineItemReceivingDocument lreq = getLineItemReceivingByDocumentNumber(docNumber);
            if (ObjectUtils.isNotNull(lreq)) {
                docs.add(lreq);
            }
        }
        if (docs != null) {
            for (LineItemReceivingDocument receivingDoc : docs) {
                if (receivingDoc.getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().contains(PurapConstants.LineItemReceivingDocumentStrings.AWAITING_PO_OPEN_STATUS)) {
                    approveReceivingDoc(receivingDoc);
                }
            }
        }

    }

    /**
     * @see org.kuali.ole.module.purap.document.service.PaymentRequestService#getPaymentRequestByDocumentNumber(java.lang.String)
     */
    public LineItemReceivingDocument getLineItemReceivingByDocumentNumber(String documentNumber) {
        LOG.debug("getLineItemReceivingByDocumentNumber() started");

        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                LineItemReceivingDocument doc = (LineItemReceivingDocument) documentService.getByDocumentHeaderId(documentNumber);
                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting LineItemReceiving document from document service";
                LOG.error("getLineItemReceivingByDocumentNumber() " + errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return null;
    }

    protected void approveReceivingDoc(LineItemReceivingDocument receivingDoc) {
        PurchaseOrderDocument poDoc = receivingDoc.getPurchaseOrderDocument();
        if (purchaseOrderService.isPurchaseOrderOpenForProcessing(poDoc)) {
            try {
                SpringContext.getBean(DocumentService.class).approveDocument(receivingDoc, "Approved by the batch job", null);
            } catch (WorkflowException e) {
                LOG.error("approveReceivingDoc() Error approving receiving document from awaiting PO open", e);
                throw new RuntimeException("Error approving receiving document from awaiting PO open", e);
            }
        }
    }

    /**
     * Gets a list of strings of receiving line item document numbers from
     * workflow documents where  applicationdocumentstatus = 'Awaiting Purchase Order Open Status'
     * If there are documents then the document number is added to the list
     * <p/>
     * NOTE: simplify using DocSearch lookup with AppDocStatus
     *
     * @return list of documentNumbers to retrieve line item receiving documents.
     */
    protected List<String> getDocumentsNumbersAwaitingPurchaseOrderOpenStatus() {
        List<String> receivingDocumentNumbers = new ArrayList<String>();

        DocumentSearchCriteria.Builder documentSearchCriteriaDTO = DocumentSearchCriteria.Builder.create();
        documentSearchCriteriaDTO.setDocumentTypeName(PurapConstants.RECEIVING_LINE_ITEM_DOCUMENT_TYPE);
        documentSearchCriteriaDTO.setApplicationDocumentStatus(PurapConstants.LineItemReceivingStatuses.APPDOC_AWAITING_PO_OPEN_STATUS);
        documentSearchCriteriaDTO.setSaveName(null);

        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(null, documentSearchCriteriaDTO.build());

        String documentHeaderId = null;

        for (DocumentSearchResult result : results.getSearchResults()) {
            receivingDocumentNumbers.add(result.getDocument().getDocumentId());
        }

        return receivingDocumentNumbers;
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

