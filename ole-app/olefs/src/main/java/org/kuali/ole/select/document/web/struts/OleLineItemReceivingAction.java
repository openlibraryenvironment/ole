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
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.CorrectionReceivingDocumentStrings;
import org.kuali.ole.module.purap.PurapConstants.PREQDocumentsStrings;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.module.purap.document.validation.event.AddReceivingItemEvent;
import org.kuali.ole.module.purap.document.web.struts.LineItemReceivingAction;
import org.kuali.ole.module.purap.document.web.struts.LineItemReceivingForm;
import org.kuali.ole.module.purap.util.ReceivingQuestionCallback;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.OleNoteTypeService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.select.document.validation.event.OleLineItemReceivingDescEvent;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * This class performs actions for Line Item Receiving with respect to OLE
 */
public class OleLineItemReceivingAction extends LineItemReceivingAction {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLineItemReceivingAction.class);
    private static transient ConfigurationService kualiConfigurationService;
    private static transient OleLineItemReceivingService oleLineItemReceivingService;

    /**
     * This method sets parts received value for each item to zero.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ActionForward clearQty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside clearQty of OleLineItemReceivingAction");
        OleLineItemReceivingForm lineItemReceivingForm = (OleLineItemReceivingForm) form;

        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) lineItemReceivingForm.getDocument();

        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) lineItemReceivingDocument.getItems()) {
            item.setItemReceivedTotalParts(KualiDecimal.ZERO);
        }
        LOG.debug("Leaving clearQty of OleLineItemReceivingAction");
        return super.clearQty(mapping, lineItemReceivingForm, request, response);
    }

    /**
     * This method loads total order parts minus prior received parts into total received parts for each item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ActionForward loadQty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside loadQty of OleLineItemReceivingAction");
        OleLineItemReceivingForm lineItemReceivingForm = (OleLineItemReceivingForm) form;

        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) lineItemReceivingForm.getDocument();

        for (OleLineItemReceivingItem item : (List<OleLineItemReceivingItem>) lineItemReceivingDocument.getItems()) {
            if (item.isOrderedItem()) {
                if (item.getItemOrderedParts().subtract(item.getItemReceivedPriorParts()).isGreaterEqual(KualiDecimal.ZERO)) {
                    item.setItemReceivedTotalParts(item.getItemOrderedQuantity().subtract(item.getItemReceivedPriorParts()));
                } else {
                    item.setItemReceivedTotalParts(KualiDecimal.ZERO);
                }
            }
        }
        LOG.debug("Leaving loadQty of OleLineItemReceivingAction");
        return super.loadQty(mapping, form, request, response);
    }

    /**
     * This method is used to add an exception note to the specified item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addExceptionNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addExceptionNote of OleLineItemReceivingAction");
        OleLineItemReceivingForm receiveForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) receiveForm.getDocument();
        OleLineItemReceivingItem selectedItem = (OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
        OleReceivingLineExceptionNotes exceptionNotes = new OleReceivingLineExceptionNotes();
        exceptionNotes.setExceptionTypeId(selectedItem.getExceptionTypeId());
        exceptionNotes.setExceptionNotes(selectedItem.getExceptionNotes());
        exceptionNotes.setReceivingLineItemIdentifier(selectedItem.getReceivingItemIdentifier());
        selectedItem.addExceptionNote(exceptionNotes);
        ((OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setExceptionTypeId(null);
        ((OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setExceptionNotes(null);
        LOG.debug("Leaving addExceptionNote of OleLineItemReceivingAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * This method is used to delete an exception note from the specified item
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteExceptionNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteExceptionNote of OleLineItemReceivingAction");
        OleLineItemReceivingForm receiveForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) receiveForm.getDocument();
        String[] indexes = getSelectedLineForDelete(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) receiveDocument.getItem((itemIndex));
        item.getExceptionNoteList().remove(noteIndex);
        LOG.debug("Leaving deleteExceptionNote of OleLineItemReceivingAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * This method is used to add a receipt note to the specified item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addReceiptNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addReceiptNote of OleLineItemReceivingAction");
        OleLineItemReceivingForm receiveForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) receiveForm.getDocument();
        OleLineItemReceivingItem selectedItem = (OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
        OleLineItemReceivingReceiptNotes receiptNotes = new OleLineItemReceivingReceiptNotes();
        OleNoteType oleNoteType = SpringContext.getBean(OleNoteTypeService.class).getNoteTypeDetails(selectedItem.getNoteTypeId());
        receiptNotes.setNoteTypeId(selectedItem.getNoteTypeId());
        receiptNotes.setNotes(selectedItem.getReceiptNotes());
        receiptNotes.setReceivingLineItemIdentifier(selectedItem.getReceivingItemIdentifier());
        receiptNotes.setNoteType(oleNoteType);
        receiptNotes.setNotesAck(selectedItem.isNotesAck());
        selectedItem.addReceiptNote(receiptNotes);
        selectedItem.addNote(receiptNotes);
        ((OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setNoteTypeId(null);
        ((OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setReceiptNotes(null);
        LOG.debug("Leaving addReceiptNote of OleLineItemReceivingAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * This method is used to delete a receipt note from the specified item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteReceiptNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteReceiptNote of OleLineItemReceivingAction");
        OleLineItemReceivingForm receiveForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) receiveForm.getDocument();
        String[] indexes = getSelectedLineForDelete(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) receiveDocument.getItem((itemIndex));
        item.getReceiptNoteList().remove(noteIndex);
        int index = ObjectUtils.isNotNull(item.getReceiptNoteListSize()) ? item.getReceiptNoteListSize() : 0;
        index = index + (ObjectUtils.isNotNull(item.getSpecialHandlingNoteList()) ? item.getSpecialHandlingNoteList().size() : 0);
        if (ObjectUtils.isNotNull(item.getReceiptNoteListSize()) || (ObjectUtils.isNotNull(item.getSpecialHandlingNoteList()) && item.getSpecialHandlingNoteList().size() > 0)) {
            index = index - 1;
        }
        item.getNoteList().remove(index + noteIndex);
        LOG.debug("leaving deleteReceiptNote of OleLineItemReceivingAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Will return an array of Strings containing 2 indexes, the first String is the item index and the second String is the account
     * index. These are obtained by parsing the method to call parameter from the request, between the word ".line" and "." The
     * indexes are separated by a semicolon (:)
     *
     * @param request The HttpServletRequest
     * @return An array of Strings containing pairs of two indices, an item index and a account index
     */
    protected String[] getSelectedLineForDelete(HttpServletRequest request) {
        LOG.debug("Inside getSelectedLineForDelete of OleLineItemReceivingAction");
        String accountString = new String();
        String parameterName = (String) request.getAttribute(OLEConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            accountString = StringUtils.substringBetween(parameterName, ".line", ".");
        }
        String[] result = StringUtils.split(accountString, ":");
        LOG.debug("Leaving getSelectedLineForDelete of OleLineItemReceivingAction");
        return result;
    }


    /**
     * This method is overridden to set doctype name as OLE_RCVC and to change receivingUrl to ole receiving url
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see org.kuali.ole.module.purap.document.web.struts.LineItemReceivingAction#createReceivingCorrection(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward createReceivingCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside createReceivingCorrection of OleLineItemReceivingAction");
        LineItemReceivingForm rlForm = (LineItemReceivingForm) form;
        LineItemReceivingDocument document = (LineItemReceivingDocument) rlForm.getDocument();

        String operation = "AddCorrectionNote ";

        ReceivingQuestionCallback callback = new ReceivingQuestionCallback() {
            public boolean questionComplete = false;
            protected String correctionDocumentnoteText;

            @Override
            public ReceivingDocument doPostQuestion(ReceivingDocument document, String noteText) throws Exception {
                //mark question completed
                this.setQuestionComplete(true);
                this.setCorrectionDocumentCreationNoteText(noteText);
                return document;
            }

            @Override
            public boolean isQuestionComplete() {
                return this.questionComplete;
            }

            @Override
            public void setQuestionComplete(boolean questionComplete) {
                this.questionComplete = questionComplete;
            }

            @Override
            public String getCorrectionDocumentCreationNoteText() {
                return correctionDocumentnoteText;
            }

            @Override
            public void setCorrectionDocumentCreationNoteText(String noteText) {
                correctionDocumentnoteText = noteText;
            }
        };

        //ask question
        ActionForward forward = askQuestionWithInput(mapping, form, request, response, CorrectionReceivingDocumentStrings.NOTE_QUESTION, CorrectionReceivingDocumentStrings.NOTE_PREFIX, operation, PurapKeyConstants.MESSAGE_RECEIVING_CORRECTION_NOTE, callback);

        //if question asked is complete, then route
        if (callback.isQuestionComplete()) {

            //set parameters
            String basePath = getApplicationBaseUrl();
            String methodToCallDocHandler = "docHandler";
            String methodToCallReceivingCorrection = "initiate";

            Properties parameters = new Properties();
            parameters.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, methodToCallDocHandler);
            parameters.put(OLEConstants.PARAMETER_COMMAND, methodToCallReceivingCorrection);
            parameters.put(OLEConstants.DOCUMENT_TYPE_NAME, "OLE_RCVC");
            parameters.put("receivingLineDocId", document.getDocumentHeader().getDocumentNumber());
            parameters.put(PurapConstants.CorrectionReceivingDocumentStrings.CORRECTION_RECEIVING_CREATION_NOTE_PARAMETER, callback.getCorrectionDocumentCreationNoteText());

            //create url
            String receivingCorrectionUrl = UrlFactory.parameterizeUrl(basePath + "/" + "selectOleCorrectionReceiving.do", parameters);
            //create forward
            forward = new ActionForward(receivingCorrectionUrl, true);
        }

        LOG.debug("Leaving createReceivingCorrection of OleLineItemReceivingAction");

        return forward;

    }

    /**
     * This method is overridden to skip intermediate page when creating OleLineItemReceivingDocument from Purchase Order page
     *
     * @see org.kuali.ole.module.purap.document.web.struts.LineItemReceivingAction#continueReceivingLine(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward continueReceivingLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside continueReceivingLine of OleLineItemReceivingAction");
        LineItemReceivingForm rlf = (LineItemReceivingForm) form;
        LineItemReceivingDocument rlDoc = (LineItemReceivingDocument) rlf.getDocument();

        // Added for OLE-2060 to skip intermediate page when creating Receiving Document from Purchase Order page
        if (ObjectUtils.isNull(rlDoc.getDocumentHeader()) || ObjectUtils.isNull(rlDoc.getDocumentHeader().getDocumentNumber())) {
            KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
            createDocument(kualiDocumentFormBase);
            rlDoc = (LineItemReceivingDocument) kualiDocumentFormBase.getDocument();
            rlDoc.setPurchaseOrderIdentifier(rlf.getPurchaseOrderId());

            DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
            rlDoc.setShipmentReceivedDate(dateTimeService.getCurrentSqlDate());
        }
        // Added for OLE-2060 to skip intermediate page when creating Receiving Document from Purchase Order page Ends
        getOleLineItemReceivingService().getInitialCollapseSections(rlDoc);
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        boolean valid = true;
        boolean poNotNull = true;

        //check for a po id
        if (ObjectUtils.isNull(rlDoc.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_REQUIRED, PREQDocumentsStrings.PURCHASE_ORDER_ID);
            poNotNull = false;
        }

        if (ObjectUtils.isNull(rlDoc.getShipmentReceivedDate())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.SHIPMENT_RECEIVED_DATE, OLEKeyConstants.ERROR_REQUIRED, PurapConstants.LineItemReceivingDocumentStrings.VENDOR_DATE);
        }

        //exit early as the po is null, no need to proceed further until this is taken care of
        if (poNotNull == false) {
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(rlDoc.getPurchaseOrderIdentifier());
        if (ObjectUtils.isNotNull(po)) {
            // TODO figure out a more straightforward way to do this.  ailish put this in so the link id would be set and the perm check would work
            rlDoc.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

            //TODO hjs-check to see if user is allowed to initiate doc based on PO sensitive data (add this to all other docs except acm doc)
            if (!SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(rlDoc).isAuthorizedByTemplate(rlDoc, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.OPEN_DOCUMENT, GlobalVariables.getUserSession().getPrincipalId())) {
                throw buildAuthorizationException("initiate document", rlDoc);
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_EXIST);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }

        //perform duplicate check
        ActionForward forward = performDuplicateReceivingLineCheck(mapping, form, request, response, rlDoc);
        if (forward != null) {
            return forward;
        }

        if (!SpringContext.getBean(ReceivingService.class).isPurchaseOrderActiveForLineItemReceivingDocumentCreation(rlDoc.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_RECEIVING_LINE_DOCUMENT_PO_NOT_ACTIVE, rlDoc.getPurchaseOrderIdentifier().toString());
            valid &= false;
        }

        if (SpringContext.getBean(ReceivingService.class).canCreateLineItemReceivingDocument(rlDoc.getPurchaseOrderIdentifier(), rlDoc.getDocumentNumber()) == false) {
            String inProcessDocNum = "";
            List<String> inProcessDocNumbers = SpringContext.getBean(ReceivingService.class).getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(rlDoc.getPurchaseOrderIdentifier(), rlDoc.getDocumentNumber());
            if (!inProcessDocNumbers.isEmpty()) {    // should not be empty if we reach this point
                inProcessDocNum = inProcessDocNumbers.get(0);
            }
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_RECEIVING_LINE_DOCUMENT_ACTIVE_FOR_PO, inProcessDocNum, rlDoc.getPurchaseOrderIdentifier().toString());
            valid &= false;
        }

        //populate and save Receiving Line Document from Purchase Order, only if we passed all the rules
        if (valid) {
            SpringContext.getBean(ReceivingService.class).populateAndSaveLineItemReceivingDocument(rlDoc);
        }

        LOG.debug("Leaving continueReceivingLine of OleLineItemReceivingAction");

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
    @Override
    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) oleLineItemReceivingForm.getNewLineItemReceivingItemLine();
        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm.getDocument();

        LOG.debug("Inside addItem >>>>>>>>>>>>>>>>>");

        String documentTypeName = OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING;
        String nameSpaceCode = OLEConstants.OleLineItemReceiving.LINE_ITEM_RECEIVING_NAMESPACE;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside addItem documentTypeName   >>>>>>>>>>>>>>>>>" + documentTypeName);
        }
        LOG.debug("Inside addItem nameSpaceCode  >>>>>>>>>>>>>>>>>" + nameSpaceCode);
        // LOG.info("Inside addItem permissionDetails >>>>>>>>>>>>>>>>>" + permissionDetails.get(documentTypeName));

        boolean hasPermission = SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleLineItemReceiving.ADD_NEW_LINE_ITEM);

        if (!hasPermission) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Inside addItem hasPermission   if>>>>>>>>>>>>>>>>>" + hasPermission);
            }
            throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), "add New Line Items", lineItemReceivingDocument.getDocumentNumber());

            //  throw new RuntimeException("User " + new String[]{GlobalVariables.getUserSession().getPerson().getPrincipalName() + "is not authorized to add New Line Items" + true});
        }

        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddReceivingItemEvent(PurapPropertyConstants.NEW_LINE_ITEM_RECEIVING_ITEM_LINE, lineItemReceivingDocument, item));
        if (rulePassed) {
            item = (OleLineItemReceivingItem) oleLineItemReceivingForm.getAndResetNewReceivingItemLine();
            //TODO: we need to set the line number correctly to match up to PO

            BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
            FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
            String titleId = null;
            boolean isBibFileExist = false;
            Iterator itemIterator = lineItemReceivingDocument.getItems().iterator();
            int itemCounter = 0;
            while (itemIterator.hasNext()) {
                OleLineItemReceivingItem tempItem = (OleLineItemReceivingItem) itemIterator.next();
                if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) || tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                    itemCounter++;
                }
            }
            String itemNo = String.valueOf(itemCounter);
            //String itemNo = String.valueOf(lineItemReceivingDocument.getItems().size() - 1);
            HashMap<String, String> dataMap = new HashMap<String, String>();
            item.setBibInfoBean(new BibInfoBean());
            if (item.getBibInfoBean().getDocStoreOperation() == null) {
                item.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
            }
            String fileName = lineItemReceivingDocument.getDocumentNumber() + "_" + itemNo;
            // Modified for jira OLE - 2437 starts

            setItemDescription(item, fileName);
            item.setStartingCopyNumber(new KualiInteger(1));

            // Modified for jira OLE - 2437 ends

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
                item.setItemDescription((item.getBibInfoBean().getTitle() != null ? item.getBibInfoBean().getTitle() : "") + (item.getBibInfoBean().getAuthor() != null ? "," + item.getBibInfoBean().getAuthor() : "") + (item.getBibInfoBean().getPublisher() != null ? "," + item.getBibInfoBean().getPublisher() : "") + (item.getBibInfoBean().getIsbn() != null ? "," + item.getBibInfoBean().getIsbn() : ""));

                HashMap<String,String> queryMap = new HashMap<String,String>();
                queryMap.put(OleSelectConstant.DocStoreDetails.ITEMLINKS_KEY, item.getItemTitleId());
                List<DocInfoBean> docStoreResult = docStore.searchBibInfo(queryMap);
                Iterator bibIdIterator = docStoreResult.iterator();

                if(bibIdIterator.hasNext()){
                    DocInfoBean docInfoBean = (DocInfoBean)bibIdIterator.next();
                    item.setBibUUID(docInfoBean.getUniqueId());
                }
            }*/
            boolean ruleFlag = getKualiRuleService().applyRules(new OleLineItemReceivingDescEvent(lineItemReceivingDocument, item));
            if (ruleFlag) {
                lineItemReceivingDocument.addItem(item);
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        // ActionForward forward=super.route(mapping, form, request, response);
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm.getDocument();
        List<OleLineItemReceivingItem> items = lineItemReceivingDocument.getItems();
        for (OleLineItemReceivingItem item : items) {
            OleLineItemReceivingService oleLineItemReceivingService = SpringContext
                    .getBean(OleLineItemReceivingServiceImpl.class);
            OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(item.getPurchaseOrderIdentifier());
            OleLineItemReceivingDoc oleLineItemReceivingItemDoc = new OleLineItemReceivingDoc();
            oleLineItemReceivingItemDoc.setReceivingLineItemIdentifier(item.getReceivingItemIdentifier());
            if (item.getItemTitleId() != null) {
                oleLineItemReceivingItemDoc.setItemTitleId(item.getItemTitleId());
            } else {

                oleLineItemReceivingItemDoc.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
            }
            if (olePurchaseOrderItem != null) {
                /*
                 * if(item.getItemReturnedTotalQuantity().isNonZero() && item.getItemReturnedTotalParts().isNonZero()){ }
                 */
                OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, item, null, lineItemReceivingDocument.getIsATypeOfRCVGDoc());
                //  updateReceivingItemReceiptStatus(item);
            }
            // oleLineItemReceivingService.saveOleLineItemReceivingItemDoc(oleLineItemReceivingItemDoc);
        }
        return super.route(mapping, form, request, response);
    }

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = super.save(mapping, form, request, response);
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm.getDocument();
        List<OleLineItemReceivingItem> items = lineItemReceivingDocument.getItems();
        for (OleLineItemReceivingItem item : items) {
            OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
            ;
            OleLineItemReceivingDoc oleLineItemReceivingItemDoc = new OleLineItemReceivingDoc();
            oleLineItemReceivingItemDoc.setReceivingLineItemIdentifier(item.getReceivingItemIdentifier());
            if (item.getItemTitleId() != null) {
                oleLineItemReceivingItemDoc.setItemTitleId(item.getItemTitleId());
            } else {
                OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(item.getPurchaseOrderIdentifier());
                oleLineItemReceivingItemDoc.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
            }
            oleLineItemReceivingService.saveOleLineItemReceivingItemDoc(oleLineItemReceivingItemDoc);
        }
        return forward;
    }

    private void setItemDescription(OleLineItemReceivingItem item, String fileName) {
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
                OleLineItemReceivingDoc oleLineItemReceivingDoc = new OleLineItemReceivingDoc();
                oleLineItemReceivingDoc.setItemTitleId(oleEditorResponse.getBib().getId());
                item.getOleLineItemReceivingItemDocList().add(oleLineItemReceivingDoc);
            }
            OleDocstoreResponse.getInstance().getEditorResponse().remove(oleEditorResponse);
        }
    }

    public boolean checkForCopiesAndLocation(OleLineItemReceivingItem item) {
        boolean isValid = true;
        if (null == item.getItemCopies() || null == item.getLocationCopies()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_ITEMCOPIES_OR_LOCATIONCOPIES_SHOULDNOT_BE_NULL, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForItemCopiesGreaterThanQuantity(OleLineItemReceivingItem item) {
        boolean isValid = true;
        if (item.getItemCopies().isGreaterThan(item.getItemReceivedTotalQuantity())) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForTotalCopiesGreaterThanQuantity(OleLineItemReceivingItem item) {
        boolean isValid = true;
        int copies = 0;
        if (item.getCopies().size() > 0) {
            for (int itemCopies = 0; itemCopies < item.getCopies().size(); itemCopies++) {
                copies = copies + item.getCopies().get(itemCopies).getItemCopies().intValue();
            }
            if (item.getItemReceivedTotalQuantity().isLessThan(item.getItemCopies().add(new KualiDecimal(copies)))) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * This method takes RequisitionItem as parameter, it will calculate and set partEnumerations and startingCopyNumber for each
     * lineItem
     *
     * @param item
     * @return OleRequisitionCopies
     */
    public OleRequisitionCopies setCopyValues(OleLineItemReceivingItem item) {
        OleRequisitionCopies itemCopy = new OleRequisitionCopies();
        int parts = 0;
        if (null != item.getItemReceivedTotalParts()) {
            parts = item.getItemReceivedTotalParts().intValue();
            itemCopy.setParts(new KualiInteger(parts));
        } else if (null != item.getItemOrderedParts()) {
            parts = item.getItemOrderedParts().intValue();
            itemCopy.setParts(new KualiInteger(parts));

        }
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
            for (int noOfParts = 0; noOfParts < parts; noOfParts++) {
                int newNoOfCopies = startingCopyNumber + noOfCopies;
                int newNoOfParts = noOfParts + 1;
                if (noOfCopies + 1 == item.getItemCopies().intValue() && newNoOfParts == parts) {
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
     * Receive a Copy for the selected Item .
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward receiveCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        LOG.debug("Inside receiveCopy Method of OLELineItemReceivingAction");
        OleLineItemReceivingForm lineItemReceivingForm = (OleLineItemReceivingForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) ((LineItemReceivingDocument) lineItemReceivingForm
                .getDocument()).getItem((itemIndex));
        OleCopy oleCopy = null;
        if(item.getCopyList().size()==1){
            oleCopy =  item.getCopyList().get(0);
            oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS);
        }else {
            for(OleCopy oleCopy1 : item.getCopyList()) {
                oleCopy1.setReceiptStatus(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS);
            }
        }
        LOG.debug("Selected Copy is Received");
        LOG.debug("Leaving receiveCopy Method of OLELineItemReceivingAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    /**
     * Will return an array of Strings containing 2 indexes, the first String is the item index and the second String is the account
     * index. These are obtained by parsing the method to call parameter from the request, between the word ".line" and "." The
     * indexes are separated by a semicolon (:)
     *
     * @param request The HttpServletRequest
     * @return An array of Strings containing pairs of two indices, an item index and a account index
     */
    protected String[] getSelectedLineForAccounts(HttpServletRequest request) {
        String accountString = new String();
        String parameterName = (String) request.getAttribute(OLEConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            accountString = StringUtils.substringBetween(parameterName, ".line", ".");
        }
        String[] result = StringUtils.split(accountString, ":");

        return result;
    }

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument lineItemReceivingDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm
                .getDocument();
        List<OleLineItemReceivingItem> items = lineItemReceivingDocument.getItems();
        for (OleLineItemReceivingItem item : items) {
            OleLineItemReceivingService oleLineItemReceivingService = SpringContext
                    .getBean(OleLineItemReceivingServiceImpl.class);
            OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(item
                    .getPurchaseOrderIdentifier());
            OleLineItemReceivingDoc oleLineItemReceivingItemDoc = new OleLineItemReceivingDoc();
            oleLineItemReceivingItemDoc.setReceivingLineItemIdentifier(item.getReceivingItemIdentifier());
            if (item.getItemTitleId() != null) {
                oleLineItemReceivingItemDoc.setItemTitleId(item.getItemTitleId());
            } else {

                oleLineItemReceivingItemDoc.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
            }
            if (olePurchaseOrderItem != null) {

                OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, item, null, lineItemReceivingDocument.getIsATypeOfRCVGDoc());
                //   updateReceivingItemReceiptStatus(item);
            }
            // oleLineItemReceivingService.saveOleLineItemReceivingItemDoc(oleLineItemReceivingItemDoc);
        }
        return super.blanketApprove(mapping, form, request, response);
    }

    public static OleLineItemReceivingService getOleLineItemReceivingService() {
        if (oleLineItemReceivingService == null) {
            oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingService.class);
        }
        return oleLineItemReceivingService;
    }

    public ActionForward addDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = true;
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm.getDocument();
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
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

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    public ActionForward deleteDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        OleLineItemReceivingForm oleLineItemReceivingForm = (OleLineItemReceivingForm) form;
        OleLineItemReceivingDocument receiveDocument = (OleLineItemReceivingDocument) oleLineItemReceivingForm.getDocument();
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int donorIndex = Integer.parseInt(indexes[1]);
        OleLineItemReceivingItem item = (OleLineItemReceivingItem) receiveDocument.getItem((itemIndex));
        item.getOleDonors().remove(donorIndex);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }
}