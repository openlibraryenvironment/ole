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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.web.struts.CorrectionReceivingAction;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleCorrectionReceivingDocument;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.OleNoteTypeService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleCorrectionReceivingAction extends CorrectionReceivingAction {

    private static transient ConfigurationService kualiConfigurationService;

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCorrectionReceivingAction.class);

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
    public ActionForward addExceptionNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        LOG.debug("Inside addExceptionNote of OleCorrectionReceivingAction");
        OleCorrectionReceivingForm receiveForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) receiveForm.getDocument();
        OleCorrectionReceivingItem selectedItem = (OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
        OleCorrectionReceivingItemExceptionNotes exceptionNotes = new OleCorrectionReceivingItemExceptionNotes();
        exceptionNotes.setExceptionTypeId(selectedItem.getExceptionTypeId());
        exceptionNotes.setExceptionNotes(selectedItem.getExceptionNotes());
        exceptionNotes.setReceivingItemIdentifier(selectedItem.getReceivingItemIdentifier());
        selectedItem.addExceptionNote(exceptionNotes);
        ((OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setExceptionTypeId(null);
        ((OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setExceptionNotes(null);
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
        LOG.debug("Inside deleteExceptionNote of OleCorrectionReceivingAction");
        OleCorrectionReceivingForm receiveForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) receiveForm.getDocument();
        String[] indexes = getSelectedLineForDelete(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleCorrectionReceivingItem item = (OleCorrectionReceivingItem) receiveDocument.getItem((itemIndex));
        item.getCorrectionExceptionNoteList().remove(noteIndex);
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
    public ActionForward addReceiptNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        LOG.debug("Inside addReceiptNote of OleCorrectionReceivingAction");
        OleCorrectionReceivingForm receiveForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) receiveForm.getDocument();
        OleCorrectionReceivingItem selectedItem = (OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
        OleCorrectionReceivingItemReceiptNotes receiptNotes = new OleCorrectionReceivingItemReceiptNotes();
        OleNoteType oleNoteType = SpringContext.getBean(OleNoteTypeService.class).getNoteTypeDetails(selectedItem.getNoteTypeId());
        receiptNotes.setNoteTypeId(selectedItem.getNoteTypeId());
        receiptNotes.setNotes(selectedItem.getReceiptNotes());
        receiptNotes.setReceivingItemIdentifier(selectedItem.getReceivingItemIdentifier());
        receiptNotes.setNoteType(oleNoteType);
        selectedItem.addReceiptNote(receiptNotes);
        selectedItem.addNote(receiptNotes);
        ((OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setNoteTypeId(null);
        ((OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request))).setReceiptNotes(null);
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
        LOG.debug("Inside deleteReceiptNote of OleCorrectionReceivingAction");
        OleCorrectionReceivingForm receiveForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) receiveForm.getDocument();
        String[] indexes = getSelectedLineForDelete(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleCorrectionReceivingItem item = (OleCorrectionReceivingItem) receiveDocument.getItem((itemIndex));
        item.getCorrectionReceiptNoteList().remove(noteIndex);
        int index = ObjectUtils.isNotNull(item.getCorrectionReceiptNoteListSize()) ? item.getCorrectionReceiptNoteListSize() : 0;
        index = index + (ObjectUtils.isNotNull(item.getCorrectionSpecialHandlingNoteList()) ? item.getCorrectionSpecialHandlingNoteList().size() : 0);
        if (ObjectUtils.isNotNull(item.getCorrectionReceiptNoteListSize())
                || (ObjectUtils.isNotNull(item.getCorrectionSpecialHandlingNoteList()) && item
                .getCorrectionSpecialHandlingNoteList().size() > 0)) {
            index = index - 1;
        }
        item.getCorrectionNoteList().remove(index + noteIndex);
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
        LOG.debug("Inside getSelectedLineForDelete of OleCorrectionReceivingAction");
        String accountString = new String();
        String parameterName = (String) request.getAttribute(OLEConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            accountString = StringUtils.substringBetween(parameterName, ".line", ".");
        }
        String[] result = StringUtils.split(accountString, ":");
        return result;
    }


    /*
     * public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * throws Exception { ActionForward forward=super.route(mapping, form, request, response); OleCorrectionReceivingForm
     * oleCorrectionReceivingForm = (OleCorrectionReceivingForm) form; OleCorrectionReceivingDocument correctionReceivingDocument =
     * (OleCorrectionReceivingDocument)oleCorrectionReceivingForm.getDocument(); List<OleCorrectionReceivingItem> items =
     * (List<OleCorrectionReceivingItem>)correctionReceivingDocument.getItems(); for(OleCorrectionReceivingItem item : items){
     * OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
     * OleLineItemCorrectionReceivingDoc oleLineItemReceivingCorrection = new OleLineItemCorrectionReceivingDoc();
     * oleLineItemReceivingCorrection.setReceivingItemIdentifier(item.getReceivingItemIdentifier());
     * if(item.getItemTitleId()!=null){ oleLineItemReceivingCorrection.setItemTitleId(item.getItemTitleId()); }else{
     * OlePurchaseOrderItem olePurchaseOrderItem=
     * oleLineItemReceivingService.getOlePurchaseOrderItem(item.getPurchaseOrderIdentifier());
     * oleLineItemReceivingCorrection.setItemTitleId(olePurchaseOrderItem.getItemTitleId()); }
     * oleLineItemReceivingService.saveOleLineItemReceivingCorrection(oleLineItemReceivingCorrection); } return forward; }
     */

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        OleCorrectionReceivingForm oleCorrectionReceivingForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument correctionReceivingDocument = (OleCorrectionReceivingDocument) oleCorrectionReceivingForm
                .getDocument();
        OleLineItemReceivingDocument oleLineItemReceivingDocument = (OleLineItemReceivingDocument) correctionReceivingDocument.getLineItemReceivingDocument();
        List<OleCorrectionReceivingItem> items = correctionReceivingDocument.getItems();
        OleLineItemReceivingService oleLineItemReceivingService = SpringContext
                .getBean(OleLineItemReceivingServiceImpl.class);
        List<OleLineItemReceivingItem> lineItemRcvngItemList = oleLineItemReceivingDocument.getItems();
        if (lineItemRcvngItemList.size() == items.size()) {
            for (int i = 0; i < items.size(); i++) {
                OleLineItemReceivingItem oleLineItemReceivingItem = lineItemRcvngItemList.get(i);
                OleCorrectionReceivingItem oleCorrectionReceivingItem = items.get(i);
                OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService
                        .getOlePurchaseOrderItem(oleLineItemReceivingItem.getPurchaseOrderIdentifier());
                if (olePurchaseOrderItem != null) {
                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                    oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, oleLineItemReceivingItem, oleCorrectionReceivingItem, correctionReceivingDocument.getIsATypeOfRCVGDoc());
                }
            }

        }
        return super.route(mapping, form, request, response);
    }

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        OleCorrectionReceivingForm oleCorrectionReceivingForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument correctionReceivingDocument = (OleCorrectionReceivingDocument) oleCorrectionReceivingForm
                .getDocument();
        OleLineItemReceivingDocument oleLineItemReceivingDocument = (OleLineItemReceivingDocument) correctionReceivingDocument.getLineItemReceivingDocument();
        List<OleCorrectionReceivingItem> items = correctionReceivingDocument.getItems();
        OleLineItemReceivingService oleLineItemReceivingService = SpringContext
                .getBean(OleLineItemReceivingServiceImpl.class);
        List<OleLineItemReceivingItem> lineItemRcvngItemList = oleLineItemReceivingDocument.getItems();
        if (lineItemRcvngItemList.size() == items.size()) {
            for (int i = 0; i < items.size(); i++) {
                OleLineItemReceivingItem oleLineItemReceivingItem = lineItemRcvngItemList.get(i);
                OleCorrectionReceivingItem oleCorrectionReceivingItem = items.get(i);
                OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService
                        .getOlePurchaseOrderItem(oleLineItemReceivingItem.getPurchaseOrderIdentifier());
                if (olePurchaseOrderItem != null) {
                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                    oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, oleLineItemReceivingItem, oleCorrectionReceivingItem, correctionReceivingDocument.getIsATypeOfRCVGDoc());
                }
            }

        }
        return super.blanketApprove(mapping, form, request, response);
    }


    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }


    /**
     * Un Receive a Copy for the selected Item .
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward unReceiveCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        LOG.debug("Inside unReceiveCopy Method of OleCorrectionReceivingAction");
        OleCorrectionReceivingForm correctionForm = (OleCorrectionReceivingForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        OleCorrectionReceivingItem item = (OleCorrectionReceivingItem) ((OleCorrectionReceivingDocument) correctionForm
                .getDocument()).getItem(itemIndex);
        OleCopy oleCopy = null;
        if(item.getCopyList().size()==1){
            oleCopy =  item.getCopyList().get(0);
        }else {
            int copyIndex = Integer.parseInt(indexes[1]);
            oleCopy = item.getCopyList().get(copyIndex);
        }
        oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
        LOG.debug("Selected Copy is Un Received");
        LOG.debug("Leaving unReceiveCopy Method of OleCorrectionReceivingAction");
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
    protected String[] getSelectedLineForAccounts(HttpServletRequest request) {
        String accountString = new String();
        String parameterName = (String) request.getAttribute(OLEConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            accountString = StringUtils.substringBetween(parameterName, ".line", ".");
        }
        String[] result = StringUtils.split(accountString, ":");

        return result;
    }
    public ActionForward addDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = true;
        OleCorrectionReceivingForm correctionForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) correctionForm.getDocument();
        OleCorrectionReceivingItem item = (OleCorrectionReceivingItem) receiveDocument.getItem(this.getSelectedLine(request));
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
        OleCorrectionReceivingForm correctionForm = (OleCorrectionReceivingForm) form;
        OleCorrectionReceivingDocument receiveDocument = (OleCorrectionReceivingDocument) correctionForm.getDocument();
        String[] indexes = getSelectedLineForDelete(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int donorIndex = Integer.parseInt(indexes[1]);
        OleCorrectionReceivingItem item = (OleCorrectionReceivingItem) receiveDocument.getItem((itemIndex));
        item.getOleDonors().remove(donorIndex);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

}