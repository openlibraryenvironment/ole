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
import org.apache.log4j.Logger;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.document.BulkReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.dataaccess.BulkReceivingDao;
import org.kuali.ole.module.purap.document.service.BulkReceivingService;
import org.kuali.ole.module.purap.document.service.PrintService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Transactional
public class BulkReceivingServiceImpl implements BulkReceivingService {

    private static final Logger LOG = Logger.getLogger(BulkReceivingServiceImpl.class);

    protected PurchaseOrderService purchaseOrderService;
    protected BulkReceivingDao bulkReceivingDao;
    protected DocumentService documentService;
    protected WorkflowDocumentService workflowDocumentService;
    protected ConfigurationService configurationService;
    protected PrintService printService;

    @Override
    public boolean canPrintReceivingTicket(BulkReceivingDocument blkRecDoc) {

        boolean canCreate = false;
        WorkflowDocument workflowDocument = null;

        try {
            workflowDocument = workflowDocumentService.createWorkflowDocument(blkRecDoc.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), GlobalVariables.getUserSession().getPerson());
        } catch (WorkflowException we) {
            throw new RuntimeException(we);
        }

        if (workflowDocument.isFinal()) {
            canCreate = true;
        }

        return canCreate;
    }

    @Override
    public void populateAndSaveBulkReceivingDocument(BulkReceivingDocument blkRecDoc)
            throws WorkflowException {
        try {
            documentService.saveDocument(blkRecDoc, AttributedContinuePurapEvent.class);
        } catch (WorkflowException we) {
            String errorMsg = "Error saving document # " + blkRecDoc.getDocumentHeader().getDocumentNumber() + " " + we.getMessage();
            throw new RuntimeException(errorMsg, we);
        }
    }

    @Override
    public HashMap<String, String> bulkReceivingDuplicateMessages(BulkReceivingDocument blkRecDoc) {
        HashMap<String, String> msgs;
        msgs = new HashMap<String, String>();
        Integer poId = blkRecDoc.getPurchaseOrderIdentifier();
        StringBuffer currentMessage = new StringBuffer("");
        List<String> docNumbers = null;

        //check vendor date for duplicates
        if (blkRecDoc.getShipmentReceivedDate() != null) {
            docNumbers = bulkReceivingDao.duplicateVendorDate(poId, blkRecDoc.getShipmentReceivedDate());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_VENDOR_DATE, blkRecDoc.getPurchaseOrderIdentifier());
            }
        }

        //check packing slip number for duplicates
        if (!StringUtils.isEmpty(blkRecDoc.getShipmentPackingSlipNumber())) {
            docNumbers = bulkReceivingDao.duplicatePackingSlipNumber(poId, blkRecDoc.getShipmentPackingSlipNumber());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_PACKING_SLIP_NUMBER, blkRecDoc.getPurchaseOrderIdentifier());
            }
        }

        //check bill of lading number for duplicates
        if (!StringUtils.isEmpty(blkRecDoc.getShipmentBillOfLadingNumber())) {
            docNumbers = bulkReceivingDao.duplicateBillOfLadingNumber(poId, blkRecDoc.getShipmentBillOfLadingNumber());
            if (hasDuplicateEntry(docNumbers)) {
                appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_BILL_OF_LADING_NUMBER, blkRecDoc.getPurchaseOrderIdentifier());
            }
        }

        //add message if one exists
        if (currentMessage.length() > 0) {
            //add suffix
            appendDuplicateMessage(currentMessage, PurapKeyConstants.MESSAGE_DUPLICATE_RECEIVING_LINE_SUFFIX, blkRecDoc.getPurchaseOrderIdentifier());

            //add msg to map
            msgs.put(PurapConstants.BulkReceivingDocumentStrings.DUPLICATE_BULK_RECEIVING_DOCUMENT_QUESTION, currentMessage.toString());
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

    protected void appendDuplicateMessage(StringBuffer currentMessage,
                                          String duplicateMessageKey,
                                          Integer poId) {

        //append prefix if this is first call
        if (currentMessage.length() == 0) {
            String messageText = configurationService.getPropertyValueAsString(PurapKeyConstants.MESSAGE_BULK_RECEIVING_DUPLICATE_PREFIX);
            String prefix = MessageFormat.format(messageText, poId.toString());

            currentMessage.append(prefix);
        }

        //append message
        currentMessage.append(configurationService.getPropertyValueAsString(duplicateMessageKey));
    }

    @Override
    public String getBulkReceivingDocumentNumberInProcessForPurchaseOrder(Integer poId,
                                                                          String bulkReceivingDocumentNumber) {

        String docNumberInProcess = StringUtils.EMPTY;

        List<String> docNumbers = bulkReceivingDao.getDocumentNumbersByPurchaseOrderId(poId);
        WorkflowDocument workflowDocument = null;

        for (String docNumber : docNumbers) {

            try {
                workflowDocument = workflowDocumentService.loadWorkflowDocument(docNumber,
                        GlobalVariables.getUserSession().getPerson());
            } catch (WorkflowException we) {
                throw new RuntimeException(we);
            }

            if (!(workflowDocument.isCanceled() ||
                    workflowDocument.isException() ||
                    workflowDocument.isFinal()) &&
                    !docNumber.equals(bulkReceivingDocumentNumber)) {

                docNumberInProcess = docNumber;
                break;
            }
        }

        return docNumberInProcess;
    }

    @Override
    public void populateBulkReceivingFromPurchaseOrder(BulkReceivingDocument blkRecDoc) {

        if (blkRecDoc != null) {
            PurchaseOrderDocument poDoc = purchaseOrderService.getCurrentPurchaseOrder(blkRecDoc.getPurchaseOrderIdentifier());
            if (poDoc != null) {
                blkRecDoc.populateBulkReceivingFromPurchaseOrder(poDoc);
            }
        }

    }

    public BulkReceivingDocument getBulkReceivingByDocumentNumber(String documentNumber) {

        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                BulkReceivingDocument doc = (BulkReceivingDocument) documentService.getByDocumentHeaderId(documentNumber);
                if (ObjectUtils.isNotNull(doc)) {
                    WorkflowDocument workflowDocument = doc.getDocumentHeader().getWorkflowDocument();
                    doc.refreshReferenceObject(OLEPropertyConstants.DOCUMENT_HEADER);
                    doc.getDocumentHeader().setWorkflowDocument(workflowDocument);
                }
                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting bulk receiving document from document service";
                throw new RuntimeException(errorMessage, e);
            }
        }
        return null;
    }

    @Override
    public void performPrintReceivingTicketPDF(String blkDocId,
                                               ByteArrayOutputStream baosPDF) {

        BulkReceivingDocument blkRecDoc = getBulkReceivingByDocumentNumber(blkDocId);
        Collection<String> generatePDFErrors = printService.generateBulkReceivingPDF(blkRecDoc, baosPDF);

        if (!generatePDFErrors.isEmpty()) {
            addStringErrorMessagesToMessageMap(PurapKeyConstants.ERROR_BULK_RECEIVING_PDF, generatePDFErrors);
            throw new ValidationException("printing bulk receiving ticket failed");
        }

    }

    protected void addStringErrorMessagesToMessageMap(String errorKey,
                                                      Collection<String> errors) {

        if (ObjectUtils.isNotNull(errors)) {
            for (String error : errors) {
                LOG.error("Adding error message using error key '" + errorKey + "' with text '" + error + "'");
                GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, errorKey, error);
            }
        }

    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }

    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public void setBulkReceivingDao(BulkReceivingDao bulkReceivingDao) {
        this.bulkReceivingDao = bulkReceivingDao;
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
}

