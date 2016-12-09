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
package org.kuali.ole.select.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.form.OLEAddTitlesToInvoiceForm;
import org.kuali.ole.select.service.OLEAddTitlesToInvoiceService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Controller
@RequestMapping(value = "/oleTitlesToInvoiceController")
public class OLEAddTitlesToInvoiceController extends UifControllerBase {


    OLEAddTitlesToInvoiceService oleAddTitlesToInvoiceService;

    private OleInvoiceService oleInvoiceService;

    public OLEAddTitlesToInvoiceService getOLEAddTitlesToInvoiceService() {

        return this.oleAddTitlesToInvoiceService;
    }

    public OLEAddTitlesToInvoiceService getNewOleAddTitlesToInvoiceService() {
        this.oleAddTitlesToInvoiceService = new OLEAddTitlesToInvoiceService();
        return this.oleAddTitlesToInvoiceService;
    }

    public OleInvoiceService getOleInvoiceService() {
        if(oleInvoiceService == null) {
            oleInvoiceService = SpringContext.getBean(OleInvoiceService.class);
        }
        return oleInvoiceService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEAddTitlesToInvoiceForm();
    }

    @RequestMapping(params = "methodToCall=pay")
    public ModelAndView pay(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setErrorMsg("");
        oleAddTitlesToInvoiceForm.setPayAndReceive(false);
        double invoiceAmount = 0;
        double foreignInvoiceAmount = 0;
        //preparing po item list from view page
        String poItemList = request.getParameter("poItemList");
        String poItemListSelection = request.getParameter("poItemListSelection");
        String invoicePriceList = request.getParameter("invoicePriceList");
        String foreignInvoicePriceList = request.getParameter("foreignInvoicePriceList");
        if((!StringUtils.isNotBlank(poItemList)) || (!StringUtils.isNotBlank(poItemListSelection)) || (!(StringUtils.isNotBlank(invoicePriceList)))){
            return getUIFModelAndView(oleAddTitlesToInvoiceForm)  ;
        }
        if((!StringUtils.isNotBlank(poItemList)) || (!StringUtils.isNotBlank(poItemListSelection)) || (!(StringUtils.isNotBlank(foreignInvoicePriceList)))){
            return getUIFModelAndView(oleAddTitlesToInvoiceForm)  ;
        }
        List<String> poItemListResult = new ArrayList<String>();
        List<String> invoicedPrice = new ArrayList<String>();
        List<String> foreignInvoicedPrice = new ArrayList<String>();
        String[] poListArray = poItemList.split(":");
        String[] poListSelectionArray = poItemListSelection.split(":");
        String[] invoicePriceSectionArray = invoicePriceList.split(":");
        String[] foreignInvoicePriceSectionArray = foreignInvoicePriceList.split(":");
        for (int i = 1; i < poListArray.length; i++) {
            boolean isNotNullValue=true;
            boolean isNotNullForeignValue=true;
            if (poListArray[i].equals("true")) {
                poItemListResult.add(poListSelectionArray[i]);
                if (invoicePriceSectionArray[i] != null && (!invoicePriceSectionArray[i].trim().equalsIgnoreCase(""))) {
                    if(invoicePriceSectionArray[i].toString().equalsIgnoreCase("null")){
                        isNotNullValue=false;
                    }else {
                       invoiceAmount = invoiceAmount + Double.parseDouble(invoicePriceSectionArray[i].toString());
                    }
                }
                if(isNotNullValue){
                    invoicedPrice.add(String.valueOf(invoicePriceSectionArray[i]));
                }else {
                    invoicedPrice.add(String.valueOf(""));
                }
                if (foreignInvoicePriceSectionArray[i] != null && (!foreignInvoicePriceSectionArray[i].trim().equalsIgnoreCase(""))) {
                    if(foreignInvoicePriceSectionArray[i].toString().equalsIgnoreCase("null")){
                        isNotNullForeignValue=false;
                    }else {
                        foreignInvoiceAmount = foreignInvoiceAmount + Double.parseDouble(foreignInvoicePriceSectionArray[i].toString());
                    }
                }

                if(isNotNullForeignValue){
                    foreignInvoicedPrice.add(String.valueOf(foreignInvoicePriceSectionArray[i]));
                }else {
                    foreignInvoicedPrice.add(String.valueOf(""));
                }
            }

        }
        oleAddTitlesToInvoiceForm.setInvoiceAmount(invoiceAmount + "");
        oleAddTitlesToInvoiceForm.setForeignInvoiceAmount(foreignInvoiceAmount + "");
        oleAddTitlesToInvoiceForm.setOlePurchaseOrderItems(getNewOleAddTitlesToInvoiceService().populateOlePurchaseOrderItemByPoItemList(poItemListResult,invoicedPrice,foreignInvoicedPrice));

        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=receiveAndPay")
    public ModelAndView receiveAndPay(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setErrorMsg("");
        oleAddTitlesToInvoiceForm.setPayAndReceive(true);
        double invoiceAmount = 0;
        double foreignInvoiceAmount = 0;
        //preparing po item list from view page
        String poItemList = request.getParameter("poItemList");
        String poItemListSelection = request.getParameter("poItemListSelection");
        String invoicePriceList = request.getParameter("invoicePriceList");
        String foreignInvoicePriceList = request.getParameter("foreignInvoicePriceList");
        if((!StringUtils.isNotBlank(poItemList)) || (!StringUtils.isNotBlank(poItemListSelection)) || (!(StringUtils.isNotBlank(invoicePriceList)))){
            return getUIFModelAndView(oleAddTitlesToInvoiceForm)  ;
        }
        if((!StringUtils.isNotBlank(poItemList)) || (!StringUtils.isNotBlank(poItemListSelection)) || (!(StringUtils.isNotBlank(foreignInvoicePriceList)))){
            return getUIFModelAndView(oleAddTitlesToInvoiceForm)  ;
        }
        List<String> poItemListResult = new ArrayList<String>();
        List<String> invoicedPrice = new ArrayList<String>();
        List<String> foreignInvoicedPrice = new ArrayList<String>();
        String[] poListArray = poItemList.split(":");
        String[] poListSelectionArray = poItemListSelection.split(":");
        String[] invoicePriceSectionArray = invoicePriceList.split(":");
        String[] foreignInvoicePriceSectionArray = foreignInvoicePriceList.split(":");
        for (int i = 1; i < poListArray.length; i++) {
            boolean isNotNullValue=true;
            boolean isNotNullForeignValue=true;
            if (poListArray[i].equals("true")) {
                poItemListResult.add(poListSelectionArray[i]);
                if (invoicePriceSectionArray[i] != null && (!invoicePriceSectionArray[i].trim().equalsIgnoreCase(""))) {
                    if(invoicePriceSectionArray[i].toString().equalsIgnoreCase("null")){
                        isNotNullValue=false;
                    }else {
                        invoiceAmount = invoiceAmount + Double.parseDouble(invoicePriceSectionArray[i].toString());
                    }
                }
                if(isNotNullValue){
                    invoicedPrice.add(String.valueOf(invoicePriceSectionArray[i]));
                }else {
                    invoicedPrice.add(String.valueOf(""));
                }
                if (foreignInvoicePriceSectionArray[i] != null && (!foreignInvoicePriceSectionArray[i].trim().equalsIgnoreCase(""))) {
                    if(foreignInvoicePriceSectionArray[i].toString().equalsIgnoreCase("null")){
                        isNotNullForeignValue=false;
                    }else {
                        foreignInvoiceAmount = foreignInvoiceAmount + Double.parseDouble(foreignInvoicePriceSectionArray[i].toString());
                    }
                }

                if(isNotNullForeignValue){
                    foreignInvoicedPrice.add(String.valueOf(foreignInvoicePriceSectionArray[i]));
                }else {
                    foreignInvoicedPrice.add(String.valueOf(""));
                }

            }

        }
        oleAddTitlesToInvoiceForm.setInvoiceAmount(invoiceAmount + "");
        oleAddTitlesToInvoiceForm.setForeignInvoiceAmount(foreignInvoiceAmount + "");
        oleAddTitlesToInvoiceForm.setOlePurchaseOrderItems(getNewOleAddTitlesToInvoiceService().populateOlePurchaseOrderItemByPoItemList(poItemListResult,invoicedPrice,foreignInvoicedPrice));

        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }


    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {

        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setOlePurchaseOrderItems(getNewOleAddTitlesToInvoiceService().populateOlePurchaseOrderItem());
        oleAddTitlesToInvoiceForm.setPayAndReceive(false);
        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }


    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setErrorMsg("");
        oleAddTitlesToInvoiceForm.setSuccessMsg("");
        boolean isAllowedForInvoice = true;
        /*List<OlePurchaseOrderItem> olePurchaseOrderItems=getOLEAddTitlesToInvoiceService().getSelectedItems(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems());*/
        if(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems()==null){
            oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_PO_ITEM_NULL);
            return getUIFModelAndView(oleAddTitlesToInvoiceForm);
        }
        if ((oleAddTitlesToInvoiceForm.getDocumentNumber().equals("")) && (oleAddTitlesToInvoiceForm.getDocumentNumber() != null)) {
            if (oleAddTitlesToInvoiceForm.getInvoiceDate() == null || oleAddTitlesToInvoiceForm.getInvoiceDate().equals("")) {
                oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_DATE);
                return getUIFModelAndView(oleAddTitlesToInvoiceForm);
            }
            if (oleAddTitlesToInvoiceForm.getInvoiceDate() == null || oleAddTitlesToInvoiceForm.getInvoiceAmount().equals("")) {
                oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_VND_AMT);
                return getUIFModelAndView(oleAddTitlesToInvoiceForm);
            }
            if (oleAddTitlesToInvoiceForm.getPaymentMethod() == null || oleAddTitlesToInvoiceForm.getPaymentMethod().equals("")) {
                oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_SELECT_PAYMENT_METHOD);
                return getUIFModelAndView(oleAddTitlesToInvoiceForm);
            }
        }
        if (!oleAddTitlesToInvoiceForm.isSkipValidation()) {
            if (getNewOleAddTitlesToInvoiceService().isPoAlreadyPaid(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems())) {
                oleAddTitlesToInvoiceForm.setContinueReceiveAndPay(true);
                oleAddTitlesToInvoiceForm.setSkipValidation(true);
                return getUIFModelAndView(form);
            }
        }
        if(!oleAddTitlesToInvoiceForm.isSkipValidation()){
            oleAddTitlesToInvoiceForm.setContinueReceiveAndPay(false);
        }
        if (!getNewOleAddTitlesToInvoiceService().isSelectedAtleatOneItem(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems())) {
            oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_SELECT_ATLEAST_ONE_TITLE);
            return getUIFModelAndView(oleAddTitlesToInvoiceForm);
        }
        List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = getNewOleAddTitlesToInvoiceService().populatePurchaseOrderDocuments(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems());
        if (!getNewOleAddTitlesToInvoiceService().validateSelectedPurchaseOrderByVendor(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems())) {
            oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_SELECT_TITLE_SAME_VENDOR);
            isAllowedForInvoice = false;
        }
        String poId = "";
        boolean isClosedPosExist = false;
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
            if (StringUtils.isNotEmpty(oleAddTitlesToInvoiceForm.getForeignInvoiceAmount())) {
                if (!new KualiDecimal(oleAddTitlesToInvoiceForm.getForeignInvoiceAmount()).equals(new KualiDecimal("0.00"))) {
                    OleExchangeRate oleExchangeRate = getOleInvoiceService().getExchangeRate(olePurchaseOrderDocuments.get(0).getVendorDetail().getCurrencyTypeId().toString());
                    BigDecimal foreignInvoiceAmount = new BigDecimal(oleAddTitlesToInvoiceForm.getForeignInvoiceAmount());
                    for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                        if (olePurchaseOrderItem.getItemTypeCode().equalsIgnoreCase("ITEM")) {
                            olePurchaseOrderItem.setItemForeignListPrice(new KualiDecimal(foreignInvoiceAmount));
                        }
                    }
                    BigDecimal invoiceAmont = foreignInvoiceAmount.divide(oleExchangeRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
                    oleAddTitlesToInvoiceForm.setInvoiceAmount(invoiceAmont.toString());
                }
            }
            if (!getNewOleAddTitlesToInvoiceService().validateStatusOfPurchaseOrderDocument(olePurchaseOrderDocument)) {
                poId = poId + olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() + ",";
                isClosedPosExist = true;
            }
        }
        if (isClosedPosExist) {

            poId = poId.substring(0, poId.length() - 1);
            oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_PO_CLOSED + poId);
            return getUIFModelAndView(oleAddTitlesToInvoiceForm);
        }
        Person currentUser = GlobalVariables.getUserSession().getPerson();
        if (isAllowedForInvoice) {
            if ((oleAddTitlesToInvoiceForm.getDocumentNumber().equals("")) && (oleAddTitlesToInvoiceForm.getDocumentNumber() != null)) {
                try {
                    if (!getNewOleAddTitlesToInvoiceService().createNewInvoiceDocument(olePurchaseOrderDocuments, oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems(), oleAddTitlesToInvoiceForm.getPaymentMethod(), oleAddTitlesToInvoiceForm.getInvoiceDate(), oleAddTitlesToInvoiceForm.getInvoiceNumber(), oleAddTitlesToInvoiceForm.getInvoiceAmount(), oleAddTitlesToInvoiceForm.getForeignInvoiceAmount(),currentUser.getPrincipalId())) {
                        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Exception occured while creating invoice", e);
                }
                oleAddTitlesToInvoiceForm.setSuccessMsg(OLEConstants.OLEAddTitlesToInvoice.SUCCESS_CREATE_INVOICE + OLEConstants.OLEAddTitlesToInvoice.DOC_ID + createLinkForDocHandler(getOLEAddTitlesToInvoiceService().getOleInvoiceDocument().getDocumentNumber()));

            } else {
                if(!getNewOleAddTitlesToInvoiceService().validateNumber(oleAddTitlesToInvoiceForm.getDocumentNumber())){
                    oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.INVALID_FORMAT_DOC);
                    return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                }
                if(!getNewOleAddTitlesToInvoiceService().validateInvoiceDocumentNumber(oleAddTitlesToInvoiceForm.getDocumentNumber())){
                    oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.INV_OR_NOT_EXIST_DOC_NUM);
                    return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                }
                if (!getNewOleAddTitlesToInvoiceService().validateInvoiceDocumentVendor(oleAddTitlesToInvoiceForm.getDocumentNumber(), oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems())) {
                    oleAddTitlesToInvoiceForm.setErrorMsg(oleAddTitlesToInvoiceForm.getErrorMsg() == null ? "" : (oleAddTitlesToInvoiceForm.getErrorMsg() + "</br>") + OLEConstants.OLEAddTitlesToInvoice.ERROR_SELECT_POITEM_INVOICE_SAME_VND);
                    return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                }
                if (!getNewOleAddTitlesToInvoiceService().addOlePurchaseOrderItemsToInvoiceDocument(olePurchaseOrderDocuments, oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems(), oleAddTitlesToInvoiceForm.getDocumentNumber(), currentUser.getPrincipalId(), oleAddTitlesToInvoiceForm.getInvoiceAmount(), oleAddTitlesToInvoiceForm.getForeignInvoiceAmount())) {
                    return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                }
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Exception occured while updating invoice ", e);
                }
                oleAddTitlesToInvoiceForm.setSuccessMsg(OLEConstants.OLEAddTitlesToInvoice.UPDATE_INVOICE + OLEConstants.OLEAddTitlesToInvoice.DOC_ID + createLinkForDocHandler(getOLEAddTitlesToInvoiceService().getOleInvoiceDocument().getDocumentNumber()));

            }
        }
        OLEAddTitlesToInvoiceService titlesToInvoiceService = getOLEAddTitlesToInvoiceService();
        boolean isReceiveSuccess = false;
        if (oleAddTitlesToInvoiceForm.isPayAndReceive()) {
            /*if (oleAddTitlesToInvoiceForm.isPayAndReceive()) {
                if (!getNewOleAddTitlesToInvoiceService().validateForReceiving(oleAddTitlesToInvoiceForm.getOlePurchaseOrderItems())) {
                    oleAddTitlesToInvoiceForm.setErrorMsg(OLEConstants.OLEAddTitlesToInvoice.ERROR_SELECT_RECEIVE_SAME_PO);
                    return getUIFModelAndView(oleAddTitlesToInvoiceForm);
                }
            }*/
            try {

                Set<String> hashSet = new HashSet<String>();
                for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                    if (!getNewOleAddTitlesToInvoiceService().validateReceivingForProcess(olePurchaseOrderDocument.getItems()) || getOLEAddTitlesToInvoiceService().isAllowedPoForReceiving()) {
                        if (titlesToInvoiceService.receiveAndPay(olePurchaseOrderDocument.getItems(), olePurchaseOrderDocument)) {
                            isReceiveSuccess = true;
                        }
                    } else {
                        hashSet.add(olePurchaseOrderDocument.getPurapDocumentIdentifier().toString());
                    }

                }
                if (hashSet.size() > 0) {
                    String failedPosForReceiving = "";
                    Iterator<String> itr = hashSet.iterator();
                    while (itr.hasNext()) {
                        if (failedPosForReceiving.equalsIgnoreCase("")) {
                            failedPosForReceiving = itr.next().toString();
                        } else {
                            failedPosForReceiving = itr.next().toString() + "," + failedPosForReceiving;
                        }
                    }
                    oleAddTitlesToInvoiceForm.setErrorMsg(oleAddTitlesToInvoiceForm.getErrorMsg() == null ? "" : (oleAddTitlesToInvoiceForm.getErrorMsg() + "</br>") + OLEConstants.OLEAddTitlesToInvoice.ERROR_CREATE_RCV_PO_ITM_EXIST + (failedPosForReceiving.equalsIgnoreCase("") ? "".toString() : "-" + failedPosForReceiving.toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Exception occured while creating receiving", e);
            }
            if (isReceiveSuccess) {
                if (titlesToInvoiceService.getReceivingDocumentsList().size() > 0) {
                    String poStringDocNumForRCV = createLinkForDocHandler(titlesToInvoiceService.getReceivingDocumentsList());
                    oleAddTitlesToInvoiceForm.setSuccessMsg(oleAddTitlesToInvoiceForm.getSuccessMsg() + "<br/>" + OLEConstants.OLEAddTitlesToInvoice.SUCCESS_RECEIVE + (poStringDocNumForRCV.equalsIgnoreCase("") ? "".toString() : " DOC ID -" + poStringDocNumForRCV.toString()));
                } else {
                    oleAddTitlesToInvoiceForm.setSuccessMsg(oleAddTitlesToInvoiceForm.getSuccessMsg());
                }
            }
        }
        oleAddTitlesToInvoiceForm.setSkipValidation(false);
        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }
    public String createLinkForDocHandler(List<String> docIdList){
        String docIdString="";
        if(docIdList.size()>0){
            for(String docId:docIdList){
                String linkUrl=OLEConstants.OLEAddTitlesToInvoice.LINK_START_TAG_HREF+ "\" "+OLEConstants.OLEAddTitlesToInvoice.LINK_DOC_HANDLER+docId+"\" "+"  target=\"_blank\" "+OLEConstants.OLEAddTitlesToInvoice.ANCHOR_END+docId+OLEConstants.OLEAddTitlesToInvoice.LINK_END_TAG;
                if(docIdString.equalsIgnoreCase("")){
                    docIdString=linkUrl;
                }   else {
                    docIdString=linkUrl+","+docIdString;
                }
            }
        }
        return docIdString;
    }
    public String createLinkForDocHandler(String docId){

        String linkUrl=OLEConstants.OLEAddTitlesToInvoice.LINK_START_TAG_HREF+"\" "+OLEConstants.OLEAddTitlesToInvoice.LINK_DOC_HANDLER+docId+"\" "+"  target=\"_blank\" "+OLEConstants.OLEAddTitlesToInvoice.ANCHOR_END+docId+OLEConstants.OLEAddTitlesToInvoice.LINK_END_TAG;
       return linkUrl;
    }

    @RequestMapping(params = "methodToCall=cancelProcess")
    public ModelAndView cancelProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setCancelBox(true);
        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=redirectToRQ")
    public ModelAndView redirectToReceivingQueue(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEAddTitlesToInvoiceForm oleAddTitlesToInvoiceForm = (OLEAddTitlesToInvoiceForm) form;
        oleAddTitlesToInvoiceForm.setCancelBox(true);
        return getUIFModelAndView(oleAddTitlesToInvoiceForm);
    }


}
