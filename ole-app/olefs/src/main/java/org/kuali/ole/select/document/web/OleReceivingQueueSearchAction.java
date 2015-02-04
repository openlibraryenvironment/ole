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
package org.kuali.ole.select.document.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.batch.service.OLEClaimNoticeService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleLineItemReceivingItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleReceiptStatus;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.OleReceivingQueueSearchDocument;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OleReceivingQueueSearchAction extends KualiTransactionalDocumentActionBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleReceivingQueueSearchAction.class);
    protected Document document;
    private static transient OleLineItemReceivingService oleLineItemReceivingService;

    /**
     * Constructs a OleReceivingQueueSearchAction.java.
     */
    public OleReceivingQueueSearchAction() {
        super();
    }

    /**
     * This method Searches and retrieves the purchase order result
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward valueSearch(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument();
        oleReceivingQueueSearchDocument.setReceivingDocumentsList(new ArrayList<String>());
        String purchaseOrderString = oleReceivingQueueSearchDocument.getPurchaseOrderNumber();
        String purchaseOrderStatus=oleReceivingQueueSearchDocument.getPurchaseOrderStatusDescription()!=null?oleReceivingQueueSearchDocument.getPurchaseOrderStatusDescription():"";
        String datePattern = "\\d{2}/\\d{2}/\\d{4}";
        boolean isValid = true;
        if ((oleReceivingQueueSearchDocument.getEndDate() != null && !oleReceivingQueueSearchDocument.getEndDate().equalsIgnoreCase(""))) {
            if (!oleReceivingQueueSearchDocument.getEndDate().matches(datePattern)) {
                oleReceivingQueueSearchDocument.getPurchaseOrders().clear();
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO To Date"});
                isValid = false;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setLenient(false);
                Date date = sdf.parse(oleReceivingQueueSearchDocument.getEndDate());

            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO To Date"});
                isValid = false;
            }
        }
        if ((oleReceivingQueueSearchDocument.getBeginDate() != null && !oleReceivingQueueSearchDocument.getBeginDate().equalsIgnoreCase(""))) {
            if (!oleReceivingQueueSearchDocument.getBeginDate().matches(datePattern)) {
                oleReceivingQueueSearchDocument.getPurchaseOrders().clear();
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO From Date"});
                isValid = false;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setLenient(false);
                Date date = sdf.parse(oleReceivingQueueSearchDocument.getBeginDate());
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO From Date"});
                isValid = false;
            }
        }
        if (!isValid) {
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        if (purchaseOrderString != null) {
            boolean isValidSting = true;
            String[] purchaseOrderNumbers = purchaseOrderString.split(",");
            for (String purchaseOrderNumber : purchaseOrderNumbers) {
                if (!validate(purchaseOrderNumber)) {
                    isValidSting = false;
                    break;
                }
            }
            if (!isValidSting) {
                oleReceivingQueueSearchDocument.getPurchaseOrders().clear();
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_PO_ITM_FORMAT);
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        }
        if(purchaseOrderStatus.equalsIgnoreCase(OleSelectConstant.CLOSED)){
            oleReceivingQueueSearchDocument.getPurchaseOrders().clear();
            GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                    OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_CLOSED);
            //return mapping.findForward(RiceConstants.MAPPING_BASIC);

        }else{
        oleReceivingQueueSearchDocument.valueSearch();
        }
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, (KewApiConstants.class));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public boolean validate(final String hex) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        if(((OleReceivingQueueSearchForm) form).getMethodToCall() != null) {
            if(((OleReceivingQueueSearchForm) form).getMethodToCall().equalsIgnoreCase("docHandler")){
                ((OleReceivingQueueSearchForm) form).setDocId(null);
            }
        }
        return super.execute(mapping, form, request, response);
    }

    /**
     * This method clears the tabs in the UI
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward valueClear(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument();
        oleReceivingQueueSearchDocument.setPurchaseOrders(null);
        oleReceivingQueueSearchDocument.setPurchaseOrderNumber(null);
        oleReceivingQueueSearchDocument.setVendorName(null);
        //oleReceivingQueueSearchDocument.setVendor(false);
        //oleReceivingQueueSearchDocument.setPurchaseOrderDate(false);
        oleReceivingQueueSearchDocument.setPurchaseOrderStatusDescription(null);
        oleReceivingQueueSearchDocument.setMonograph(false);
        oleReceivingQueueSearchDocument.setPurchaseOrderType(null);
        oleReceivingQueueSearchDocument.setBeginDate(null);
        oleReceivingQueueSearchDocument.setEndDate(null);
        oleReceivingQueueSearchDocument.setStandardNumber(null);
        oleReceivingQueueSearchDocument.setTitle(null);
        oleReceivingQueueSearchDocument.setClaimFilter(false);
        oleReceivingQueueSearchDocument.setReceivingDocumentsList(new ArrayList<String>());
        //oleReceivingQueueSearchDocument.setJournal(null);
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, KewApiConstants.class.newInstance());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }


    /**
     * Gets the document attribute.
     *
     * @return Returns the document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Sets the document attribute value.
     *
     * @param document The document to set.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * This method selects all Purchase Order Items in Receiving Queue search page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward selectAll(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        List<OlePurchaseOrderItem> items = ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).getPurchaseOrders();
        List<OlePurchaseOrderItem> refreshItems = new ArrayList<OlePurchaseOrderItem>(0);

        for (OlePurchaseOrderItem item : items) {
            if(item.isClaimFilter()){
                item.setClaimPoAdded(true);
            }
            item.setPoAdded(true);
            refreshItems.add(item);
        }

        ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).setPurchaseOrders(refreshItems);
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, KewApiConstants.class.newInstance());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method deselects all Purchase Order Items in Receiving Queue search page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward unselectAll(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        List<OlePurchaseOrderItem> items = ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).getPurchaseOrders();
        List<OlePurchaseOrderItem> refreshItems = new ArrayList<OlePurchaseOrderItem>(0);

        for (OlePurchaseOrderItem item : items) {
            if(item.isClaimFilter()){
                item.setClaimPoAdded(false);
            }
            item.setPoAdded(false);
            refreshItems.add(item);
        }

        ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).setPurchaseOrders(refreshItems);
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, KewApiConstants.class.newInstance());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method returns a new OleLineItemReceivingDocument
     *
     * @return
     * @throws WorkflowException
     */
    protected OleLineItemReceivingDocument createLineItemReceivingDocument() throws WorkflowException {
        Document doc = getDocumentService().getNewDocument("OLE_RCVL");
        return (OleLineItemReceivingDocument) doc;
    }

    /**
     * This method validates and performs complete receiving of PO IDs selected.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward completeReceiving(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside completeReceiving of OleReceivingQueueSearchDocument");
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument();
        OleLineItemReceivingDocument rlDoc = null;
        // Elimiate duplicate POs from the list
        List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();
        HashMap<Integer, OlePurchaseOrderItem> selectedPOs = getSelectedPurchaseOrders(oleReceivingQueueSearchDocument.getPurchaseOrders());
        if (selectedPOs.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_PO_ITM);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        boolean isReceivingExist = false;
        List<String> errorCreatingReceivingForPoIdList = new ArrayList<String>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            if (olePurchaseOrderItem.isPoAdded()) {
                /*boolean isReceivingExisted=false;*/
                if (validateReceivingForProcess(olePurchaseOrderItem)) {
                    isReceivingExist = true;
                    olePurchaseOrderItem.setPoAdded(false);
                    if (!errorCreatingReceivingForPoIdList.contains(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier().toString())) {
                        errorCreatingReceivingForPoIdList.add(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier().toString());
                    }
                }
                /*if (!isReceivingExisted) {
                if (olePurchaseOrderItem.getItemQuantity() != null) {
                    KualiDecimal kualiDecimal = olePurchaseOrderItem.getItemQuantity();
                    int itemQty = kualiDecimal.intValue();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("itemIdentifier", olePurchaseOrderItem.getItemIdentifier().toString());
                    List<OlePurchaseOrderItem> orderItems = (List<OlePurchaseOrderItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, map);
                    if (orderItems.size() > 0) {
                        OlePurchaseOrderItem purchaseOrderItem = orderItems.get(0);
                        if (!(purchaseOrderItem.getNoOfCopiesReceived() != null && purchaseOrderItem.getNoOfCopiesReceived().equalsIgnoreCase(""))) {
                            if (itemQty == Integer.parseInt(purchaseOrderItem.getNoOfCopiesReceived())) {
                                olePurchaseOrderItem.setPoAdded(false);

                            }
                        }
                        if (!(purchaseOrderItem.getNoOfPartsReceived() != null && purchaseOrderItem.getNoOfPartsReceived().equalsIgnoreCase(""))) {
                            if (purchaseOrderItem.getNoOfPartsReceived().equals(purchaseOrderItem.getItemNoOfParts())) {
                                olePurchaseOrderItem.setPoAdded(false);

                            }
                        }
                    }
                }

                }*/
            }
        }
        selectedPOs = getSelectedPurchaseOrders(oleReceivingQueueSearchDocument.getPurchaseOrders());
        // Map containing PO ID and receive PO status
        HashMap<Integer, Boolean> receivePOStatus = new HashMap<Integer, Boolean>();
        oleReceivingQueueSearchDocument.getReceivingDocumentsList().clear();
        boolean isInfoMsg = false;
        for (Map.Entry<Integer, OlePurchaseOrderItem> entry : selectedPOs.entrySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey());
            }
            rlDoc = createLineItemReceivingDocument();
            boolean receivePOSuccess = oleReceivingQueueSearchDocument.receivePO(rlDoc, entry.getKey(), false);
            if (receivePOSuccess) {
                oleReceivingQueueSearchDocument.getReceivingDocumentsList().add(rlDoc.getDocumentNumber());
                // assignActionRequests(rlDoc.getDocumentHeader().getWorkflowDocument().getDocumentId().toString());
                isInfoMsg = true;
            }
            receivePOStatus.put(entry.getKey(), receivePOSuccess);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey() + " completed successfully");
            }
        }
        List<OlePurchaseOrderItem> refreshedPOList = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            Integer poId = poItem.getPurchaseOrder().getPurapDocumentIdentifier();
            refreshedPOList.add(poItem);
            /*if(ObjectUtils.isNull(receivePOStatus.get(poId))){
                refreshedPOList.add(poItem);
            }else{
                if(receivePOStatus.get(poId) == false) {
                    refreshedPOList.add(poItem);
                }
            }*/
        }

        ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).setPurchaseOrders(refreshedPOList);

        LOG.debug("Leaving completeReceiving of OleReceivingQueueSearchDocument");
        String errorCreatingReceivingForPoIds = "";
        for (String string : errorCreatingReceivingForPoIdList) {
            errorCreatingReceivingForPoIds = errorCreatingReceivingForPoIds + string + ",";
        }
        if (isReceivingExist) {
            errorCreatingReceivingForPoIds = errorCreatingReceivingForPoIds.substring(0, errorCreatingReceivingForPoIds.length() - 1);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_RECEIVING_EXIST, errorCreatingReceivingForPoIds);
        }

        return unselectAll(mapping, oleReceivingQueueSearchForm, request, response);
    }


    public ActionForward claiming(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside completeReceiving of OleReceivingQueueSearchDocument");
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument();
        // Elimiate duplicate POs from the list
        List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            if (poItem.isPoAdded()) {
                olePurchaseOrderItems.add(poItem);
            }
        }
        if (olePurchaseOrderItems.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_PO_ITM);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        OLEClaimNoticeService oleClaimNoticeService  = GlobalResourceLoader.getService("oleClaimNoticeService");
        oleClaimNoticeService.generateClaimReportFromPO(olePurchaseOrderItems,false);
        return unselectAll(mapping, oleReceivingQueueSearchForm, request, response);
    }


    /**
     * This method validates and creates receiving document from the selected PO Items.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createReceiving(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        LOG.debug("Inside completereceiving of OleReceivingQueueSearchDocument");
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm
                .getDocument();
        OleLineItemReceivingDocument rlDoc = null;
        HashMap<Integer, OlePurchaseOrderItem> selectedPOs = getSelectedPurchaseOrders(oleReceivingQueueSearchDocument
                .getPurchaseOrders());
        List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();
        if (selectedPOs.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_PO_ITM);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        boolean isReceivingExist = false;
        List<String> errorCreatingReceivingForPoIdList = new ArrayList<String>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            if (olePurchaseOrderItem.isPoAdded()) {
                if (validateReceivingForProcess(olePurchaseOrderItem)) {
                    isReceivingExist = true;
                    olePurchaseOrderItem.setPoAdded(false);
                    if (!errorCreatingReceivingForPoIdList.contains(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier().toString())) {
                        errorCreatingReceivingForPoIdList.add(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier().toString());
                    }
                }
                /*if (olePurchaseOrderItem.getItemQuantity() != null) {
                    KualiDecimal kualiDecimal = olePurchaseOrderItem.getItemQuantity();
                    int itemQty = kualiDecimal.intValue();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("itemIdentifier", olePurchaseOrderItem.getItemIdentifier().toString());
                    List<OlePurchaseOrderItem> orderItems = (List<OlePurchaseOrderItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, map);
                    if (orderItems.size() > 0) {
                        OlePurchaseOrderItem purchaseOrderItem = orderItems.get(0);
                        if (!(purchaseOrderItem.getNoOfCopiesReceived() != null && purchaseOrderItem.getNoOfCopiesReceived().equalsIgnoreCase(""))) {
                            if (itemQty == Integer.parseInt(purchaseOrderItem.getNoOfCopiesReceived())) {
                                olePurchaseOrderItem.setPoAdded(false);

                            }
                        }
                        if (!(purchaseOrderItem.getNoOfPartsReceived() != null && purchaseOrderItem.getNoOfPartsReceived().equalsIgnoreCase(""))) {
                            if (purchaseOrderItem.getNoOfPartsReceived().equals(purchaseOrderItem.getItemNoOfParts())) {
                                olePurchaseOrderItem.setPoAdded(false);

                            }
                        }
                    }
                }*/
            }
        }
        selectedPOs = getSelectedPurchaseOrders(oleReceivingQueueSearchDocument.getPurchaseOrders());
        // Map containing PO ID and receive PO status
        HashMap<Integer, Boolean> receivePOStatus = new HashMap<Integer, Boolean>();
        boolean isInfoMsg = false;
        for (Map.Entry<Integer, OlePurchaseOrderItem> entry : selectedPOs.entrySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey());
            }
            rlDoc = createLineItemReceivingDocument();
            getOleLineItemReceivingService().getInitialCollapseSections(rlDoc);
            boolean receivePOSuccess = oleReceivingQueueSearchDocument.receivePO(rlDoc, entry.getKey(), true);
            if (receivePOSuccess) {
                oleReceivingQueueSearchDocument.getReceivingDocumentsList().add(rlDoc.getDocumentNumber());
                isInfoMsg = true;
            }
            receivePOStatus.put(entry.getKey(), receivePOSuccess);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey()
                        + " completed successfully");
            }
        }
        List<OlePurchaseOrderItem> refreshedPOList = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            Integer poId = poItem.getPurchaseOrder().getPurapDocumentIdentifier();
            refreshedPOList.add(poItem);
            /*if (ObjectUtils.isNull(receivePOStatus.get(poId))) {
                refreshedPOList.add(poItem);
            }
            else {
                if (receivePOStatus.get(poId) == false) {
                    refreshedPOList.add(poItem);
                }
            }*/
        }
        if (oleReceivingQueueSearchDocument.getReceivingDocumentsList().size() == 1) {
            ActionForward dest = returnToReceiving(oleReceivingQueueSearchDocument.getReceivingDocumentsList().get(0));
            return dest;
        }
        ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument())
                .setPurchaseOrders(refreshedPOList);

        LOG.debug("Leaving completeReceiving of OleReceivingQueueSearchDocument");
        String errorCreatingReceivingForPoIds = "";
        for (String string : errorCreatingReceivingForPoIdList) {
            errorCreatingReceivingForPoIds = errorCreatingReceivingForPoIds + string + ",";
        }
        if (isReceivingExist) {
            errorCreatingReceivingForPoIds = errorCreatingReceivingForPoIds.substring(0, errorCreatingReceivingForPoIds.length() - 1);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_RECEIVING_EXIST, errorCreatingReceivingForPoIds);
        }

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method populates a HashMap with POID and Purchase Order Item. The display is at line item level, and users can select
     * multiple line items under same PO and click on Receive. This method removes duplicate PO selections in such cases.
     *
     * @param purchaseOrders Selected PurchaseOrders from UI
     * @return
     */
    private HashMap<Integer, OlePurchaseOrderItem> getSelectedPurchaseOrders(List<OlePurchaseOrderItem> purchaseOrders) {
        LOG.debug("Inside getSelectedPurchaseOrders of OleReceivingQueueSearchDocument");
        HashMap<Integer, OlePurchaseOrderItem> poItemMap = new HashMap<Integer, OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : purchaseOrders) {
            if (poItem.isPoAdded()) {
                if (ObjectUtils.isNull(poItemMap.get(poItem.getPurchaseOrder().getPurapDocumentIdentifier()))) {
                    poItemMap.put(poItem.getPurchaseOrder().getPurapDocumentIdentifier(), poItem);
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Number of actual PO selected in OleReceivingQueueSearchDocument.getSelectedPurchaseOrders - " + poItemMap.size());
            LOG.debug("Leaving getSelectedPurchaseOrders of OleReceivingQueueSearchDocument");
        }

        return poItemMap;
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#cancel(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        return returnToSender(request, mapping, kualiDocumentFormBase);
    }

    /**
     * This method redirects the receiving queue to Line Item Receiving Document.
     *
     * @param rcvDocId
     * @return ActionForward which will redirect the user to the docSearchDisplay for the Receiving Document with the given
     *         documentId
     */
    private ActionForward returnToReceiving(String rcvDocId) {
        String receivingUrl = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OLEConstants.WORKFLOW_URL_KEY)
                + "/DocHandler.do?docId=" + rcvDocId + "&command=displayDocSearchView";
        return new ActionForward(receivingUrl, true);
    }


    /**
     * This method redirects the receiving queue to Payment Request Document.
     *
     * @param purchaseOrderId
     * @return ActionForward which will redirect the user to the docSearchDisplay for the Receiving Document with the given
     *         documentId
     */
    private ActionForward returnToPayment(String purchaseOrderId) {
        String paymentUrl = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OLEConstants.WORKFLOW_URL_KEY)
                + "/selectOlePaymentRequest.do?purchaseOrderIdentifier=" + purchaseOrderId
                + "&methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ";
        return new ActionForward(paymentUrl, true);
    }

    /**
     * This method validates and creates receiving document and the Payment Request Document from the selected PO Items.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createPaymentRequestDocument(ActionMapping mapping, ActionForm form,
                                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method validates and creates receiving document and the Payment Request Document from the selected PO Items.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward receiveAndPay(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        LOG.debug("Inside receiveAndPay of OleReceivingQueueSearchDocument");
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) form;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument();
        OleLineItemReceivingDocument rlDoc = null;
        // Elimiate duplicate POs from the list
        HashMap<Integer, OlePurchaseOrderItem> selectedPOs = getSelectedPurchaseOrders(oleReceivingQueueSearchDocument.getPurchaseOrders());
        // Map containing PO ID and receive PO status
        HashMap<Integer, Boolean> receivePOStatus = new HashMap<Integer, Boolean>();
        boolean isInfoMsg = false;
        for (Map.Entry<Integer, OlePurchaseOrderItem> entry : selectedPOs.entrySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey());
            }
            rlDoc = createLineItemReceivingDocument();
            boolean receivePOSuccess = oleReceivingQueueSearchDocument.receivePO(rlDoc, entry.getKey(), false);
            if (receivePOSuccess) {
                oleReceivingQueueSearchDocument.getReceivingDocumentsList().add(rlDoc.getDocumentNumber());
                // assignActionRequests(rlDoc.getDocumentHeader().getWorkflowDocument().getDocumentId().toString());
                isInfoMsg = true;
            }
            receivePOStatus.put(entry.getKey(), receivePOSuccess);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Line Item Receiving Document for PO ID - " + entry.getKey() + " completed successfully");
            }
        }
        List<OlePurchaseOrderItem> refreshedPOList = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : oleReceivingQueueSearchDocument.getPurchaseOrders()) {
            Integer poId = poItem.getPurchaseOrder().getPurapDocumentIdentifier();
            if (ObjectUtils.isNull(receivePOStatus.get(poId))) {
                refreshedPOList.add(poItem);
            } else {
                if (receivePOStatus.get(poId) == false) {
                    refreshedPOList.add(poItem);
                }
            }
        }

        ((OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm.getDocument()).setPurchaseOrders(refreshedPOList);

        LOG.debug("Leaving receiveAndPay of OleReceivingQueueSearchDocument");

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    @Override
    protected void loadDocument(KualiDocumentFormBase kualiDocumentFormBase) throws WorkflowException {
        super.loadDocument(kualiDocumentFormBase);
        OleReceivingQueueSearchForm oleReceivingQueueSearchForm = (OleReceivingQueueSearchForm) kualiDocumentFormBase;
        OleReceivingQueueSearchDocument oleReceivingQueueSearchDocument = (OleReceivingQueueSearchDocument) oleReceivingQueueSearchForm
                .getDocument();
        oleReceivingQueueSearchDocument.setReceivingDocumentsList(new ArrayList<String>());
    }

    /**
     * This method assigns specified document to the selector.
     */
    private void assignActionRequests(String routeHeaderId) {
        LOG.debug("Inside assignActionRequests of OleOrderQueueDocument");
        Timestamp currentTime = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();
        ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
        ActionRequestValue actionRequest = new ActionRequestValue();
        actionRequest.setActionRequested(KewApiConstants.ACTION_REQUEST_FYI_REQ);
        actionRequest.setPrincipalId("OLE10003");
        actionRequest.setCreateDate(currentTime);
        actionRequest.setForceAction(true);
        actionReqSrv.saveActionRequest(actionRequest);
        /*
         * List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId); for (ActionRequestValue
         * actionRequest : actionReqValues) { List<ActionItem> actionItems = actionRequest.getActionItems(); for (ActionItem
         * actionItem : actionItems) { actionItem.setPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
         * actionItem.setDateAssigned(currentTime); actionListSrv.saveActionItem(actionItem); }
         * actionRequest.setActionRequested(KewApiConstants.ACTION_REQUEST_FYI_REQ);
         * actionRequest.setPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
         * actionRequest.setCreateDate(currentTime); actionReqSrv.saveActionRequest(actionRequest); }
         */
        LOG.debug("Leaving assignActionRequests of OleOrderQueueDocument");
    }

    public boolean validateReceivingForProcess(OlePurchaseOrderItem purchaseOrderItem) {
        boolean isValid = false;
        Map<String, String> map = new HashMap<String, String>();
        map.put("purchaseOrderIdentifier", purchaseOrderItem.getItemIdentifier().toString());
        List<OleLineItemReceivingItem> oleLineItemReceivingItems = (List<OleLineItemReceivingItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLineItemReceivingItem.class, map);

        if (oleLineItemReceivingItems.size() > 0) {
            OleLineItemReceivingItem oleLineItemReceivingItem = oleLineItemReceivingItems.get(0);
            if (oleLineItemReceivingItem.getReceiptStatusId() != null) {
                if (oleLineItemReceivingItem.getReceiptStatusId().toString().equalsIgnoreCase(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED) + "")) {
                    isValid = true;
                }
            }
        }
        return isValid;
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

    public static OleLineItemReceivingService getOleLineItemReceivingService() {
        if (oleLineItemReceivingService == null) {
            oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingService.class);
        }
        return oleLineItemReceivingService;
    }

}
