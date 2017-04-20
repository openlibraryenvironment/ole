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
package org.kuali.ole.select.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurApItemUseTax;
import org.kuali.ole.module.purap.businessobject.PurchasingCapitalAssetItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.*;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccount;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AttributedBlanketApproveDocumentEvent;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OLEAddTitlesToInvoiceService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEAddTitlesToInvoiceService.class);
    DocumentService documentService;
    List<String> receivingDocumentsList = new ArrayList<String>();
    boolean isAllowedPoForReceiving = false;
    private static transient OlePurapService olePurapService;
    private static transient OleInvoiceService invoiceService;

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public List<String> getReceivingDocumentsList() {
        return receivingDocumentsList;
    }

    OleInvoiceDocument oleInvoiceDocument;

    public OleInvoiceDocument getOleInvoiceDocument() {
        return oleInvoiceDocument;
    }

    public void setOleInvoiceDocument(OleInvoiceDocument oleInvoiceDocument) {
        this.oleInvoiceDocument = oleInvoiceDocument;
    }

    public boolean isAllowedPoForReceiving() {
        return isAllowedPoForReceiving;
    }

    public void setAllowedPoForReceiving(boolean allowedPoForReceiving) {
        isAllowedPoForReceiving = allowedPoForReceiving;
    }

    public static OleInvoiceService getInvoiceService() {
        if (invoiceService == null) {
            invoiceService = SpringContext.getBean(OleInvoiceService.class);
        }
        return invoiceService;
    }

    public List<OlePurchaseOrderItem> populateOlePurchaseOrderItem() {
        List<OlePurchaseOrderItem> result = new ArrayList<OlePurchaseOrderItem>();
        List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePurchaseOrderDocument.class);
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
            String olePurchaseOrderDocumentNumber = olePurchaseOrderDocument.getDocumentNumber();
            List<OlePurchaseOrderItem> olePurchaseOrderItems = olePurchaseOrderDocument.getItems();
            for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                if (olePurchaseOrderItem.getItemTypeCode().toString().equals("ITEM") && olePurchaseOrderItem.getDocumentNumber().equals(olePurchaseOrderDocumentNumber)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("poItemIdentifier", olePurchaseOrderItem.getItemIdentifier().toString());
                            /*List<OleInvoiceItem> oleInvoiceItems=(List<OleInvoiceItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceItem.class,map);*/
                            /* olePurchaseOrderItem.setSelected(true);*/
                           /* if(!(oleInvoiceItems.size()>0)){*/
                    if (validateRecords(olePurchaseOrderDocument.getPurapDocumentIdentifier(), olePurchaseOrderDocument.getDocumentNumber())) {
                        olePurchaseOrderItem.setOlePurchaseOrderDocument(olePurchaseOrderDocument);
                        result.add(olePurchaseOrderItem);
                    }
                            /*}*/
                }
            }
        }
        return result;
    }

    public List<OlePurchaseOrderItem> populateOlePurchaseOrderItemByPoItemList(List<String> purchaseOrderItemIds, List<String> invoicePrice, List<String> foreignInvoicedPrice) {
        List<OlePurchaseOrderItem> result = new ArrayList<OlePurchaseOrderItem>();
      //  List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePurchaseOrderDocument.class);


        int index = 0;
            for (String purchaseOrderItemId : purchaseOrderItemIds) {
                HashMap itemMap = new HashMap<>();
                itemMap.put("itemIdentifier", purchaseOrderItemId.toString());
                List<OlePurchaseOrderItem> olePurchaseOrderItemList = (List<OlePurchaseOrderItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, itemMap);
                for (OlePurchaseOrderItem olePOItem : olePurchaseOrderItemList) {
                    HashMap poMap = new HashMap<>();
                    poMap.put("documentNumber", olePOItem.getDocumentNumber());
                    List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poMap);
                    for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                        List<OlePurchaseOrderItem> olePurchaseOrderItems = olePurchaseOrderDocument.getItems();
                        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                            if (olePurchaseOrderItem.getItemIdentifier().toString().equalsIgnoreCase(purchaseOrderItemId.toString())) {
                                if (validateRecords(olePurchaseOrderDocument.getPurapDocumentIdentifier(), olePurchaseOrderDocument.getDocumentNumber())) {
                                    olePurchaseOrderItem.setSelected(true);
                                    olePurchaseOrderItem.setOlePurchaseOrderDocument(olePurchaseOrderDocument);
                                    if (invoicePrice.get(index) != null && (!invoicePrice.get(index).trim().equalsIgnoreCase(""))) {
                                        olePurchaseOrderItem.setInvoiceItemListPrice(invoicePrice.get(index));
                                    } else {
                                        olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getExtendedPrice().toString());
                                    }
                                    if (foreignInvoicedPrice.get(index) != null && (!foreignInvoicedPrice.get(index).trim().equalsIgnoreCase(""))) {
                                        olePurchaseOrderItem.setInvoiceForeignItemListPrice(foreignInvoicedPrice.get(index));
                                    }
                                    result.add(olePurchaseOrderItem);

                                }
                            }
                        }

                    }
                    index++;
                }
            }
        return result;
    }

    public boolean validateSelectOlePurchaseOrderItem(List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        boolean isSuccess = false;

        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                isSuccess = true;
            }
        }

        return isSuccess;
    }

    public boolean validateOlePurchaseOrderItemByPoId(List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        List<OlePurchaseOrderItem> result = new ArrayList<OlePurchaseOrderItem>();
        boolean isSuccess = true;
        String poId = "";
        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
            if (poId.equalsIgnoreCase("")) {
                poId = olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString();
            } else {
                if (!poId.equalsIgnoreCase(olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString())) {
                    isSuccess = false;
                }
            }

        }
        return isSuccess;
    }

    public boolean validateSelectedPurchaseOrderByVendor(List<OlePurchaseOrderItem> olePurchaseOrderItems, String vendorId) {
        boolean isSuccess = true;
        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
            if (!olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase(vendorId.toString())) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public boolean validateInvoiceDocumentVendor(String invoiceDocumentNumber, List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean isValid = true;
        Map<String, String> map = new HashMap<String, String>();
        map.put("documentNumber", invoiceDocumentNumber);
        List<OleInvoiceDocument> oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
        OleInvoiceDocument oleInvoiceDocument = oleInvoiceDocuments.get(0);
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                if (oleInvoiceDocument.getVendorAddressGeneratedIdentifier() != null) {
                    if (!(olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase(oleInvoiceDocument.getVendorHeaderGeneratedIdentifier().toString()))) {
                        if (oleInvoiceDocument.getVendorAddressGeneratedIdentifier().toString().equalsIgnoreCase(olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorAddressGeneratedIdentifier().toString())) {
                            isValid = false;
                        }
                    }
                } else {

                    if (!(olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorHeaderGeneratedIdentifier().toString().equalsIgnoreCase(oleInvoiceDocument.getVendorHeaderGeneratedIdentifier().toString()))) {
                        isValid = false;
                    }

                }
            }
        }
        return isValid;
    }

    public boolean validateSelectedPurchaseOrderByVendor(List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean isSuccess = true;
        List<String> list = new ArrayList<String>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                if (!list.contains(olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorHeaderGeneratedIdentifier().toString() + "-" + olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorDetailAssignedIdentifier())) {
                    list.add(olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorHeaderGeneratedIdentifier().toString() + "-" + olePurchaseOrderItem.getOlePurchaseOrderDocument().getVendorDetailAssignedIdentifier());
                }
            }
        }
        if (list.size() > 1) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public boolean isSelectedAtleatOneItem(List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean isValid = false;
        List<OlePurchaseOrderItem> olePurchaseOrderItemSelected = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                isValid = true;
            }
        }
        return isValid;
    }

    public List<OlePurchaseOrderItem> getSelectedItems(List<OlePurchaseOrderItem> purchaseOrderItems) {
        List<OlePurchaseOrderItem> olePurchaseOrderItemSelected = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                olePurchaseOrderItemSelected.add(olePurchaseOrderItem);
            }
        }
        return olePurchaseOrderItemSelected;
    }

    public boolean createNewInvoiceDocument(List<OlePurchaseOrderDocument> olePurchaseOrderDocuments, List<OlePurchaseOrderItem> olePurchaseOrderItems, String paymentMethodId, Date invoiceDate, String invoiceNumber, String vendorInvoiceAmt, String vendorForeignInvoiceAmt,String principalId) throws Exception {
        boolean isSuccess = false;
        try {

            OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_PRQS");
            java.util.Date date1 = new java.util.Date();
            if(invoiceDate == null){
                oleInvoiceDocument.setInvoiceDate(new Date(SpringContext.getBean(DateTimeService.class).getCurrentDate().getTime()));
            } else {
                oleInvoiceDocument.setInvoiceDate(invoiceDate);
            }
            oleInvoiceDocument.setInvoiceNumber(invoiceNumber);
            oleInvoiceDocument.setPaymentMethodId(Integer.parseInt(paymentMethodId));
            oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(vendorInvoiceAmt));
            oleInvoiceDocument.setInvoiceAmount(vendorInvoiceAmt);
            oleInvoiceDocument.setForeignInvoiceAmount(vendorForeignInvoiceAmt);
            oleInvoiceDocument.setForeignVendorAmount(vendorForeignInvoiceAmt);
            oleInvoiceDocument.initiateDocument();
            /*oleInvoiceDocument.setInvoiceNumber("11112");*/

            /*for(OlePurchaseOrderDocument purchaseOrderDocument:olePurchaseOrderDocuments){
                List<OlePurchaseOrderItem> olePurchaseOrderItems1=purchaseOrderDocument.getItems();
                for(OlePurchaseOrderItem olePurchaseOrderItem1:olePurchaseOrderItems){
                    for(OlePurchaseOrderItem olePurchaseOrderItem2:olePurchaseOrderItems1){
                        if(olePurchaseOrderItem1.getItemIdentifier().toString().equalsIgnoreCase(olePurchaseOrderItem2.getItemIdentifier().toString())){
                            olePurchaseOrderItem1.setItemForInvoice(true);
                        }
                    }
                    purchaseOrderDocument.setPendingActionIndicator(false);

                }

            }*/
            Map<String, ExpiredOrClosedAccount> map1 = new HashMap<String, ExpiredOrClosedAccount>();
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                if (olePurchaseOrderDocument != null) {
                    this.populateInvoiceFromPurchaseOrder(olePurchaseOrderDocument, oleInvoiceDocument, null, true);
                    oleInvoiceDocument.getPurchaseOrderDocuments().add(olePurchaseOrderDocument);
                }
            }
            String description = getOlePurapService().getParameter(OLEConstants.INV_DESC);
            description = getOlePurapService().setDocumentDescription(description,null);
            oleInvoiceDocument.getDocumentHeader().setDocumentDescription(description);
           // updateForeignCurrencyDetails(oleInvoiceDocument);
            List<OleInvoiceItem> oleInvItems = oleInvoiceDocument.getItems();
            for (OleInvoiceItem oleInvoiceItem : oleInvItems) {
                if(oleInvoiceItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE) &&
                        StringUtils.isNotEmpty(oleInvoiceItem.getForeignUnitCost()) &&
                        !oleInvoiceItem.getForeignUnitCost().equals("0.00")) {
                        BigDecimal foreignUnitCost = new BigDecimal(oleInvoiceItem.getForeignUnitCost());
                        BigDecimal exchangeRate = new BigDecimal(oleInvoiceItem.getExchangeRate());
                        oleInvoiceItem.setItemListPrice(new KualiDecimal(foreignUnitCost.divide(exchangeRate, 2, RoundingMode.HALF_UP)));
                        oleInvoiceItem.setItemUnitPrice(foreignUnitCost.divide(exchangeRate, 2, RoundingMode.HALF_UP));
                }
                getInvoiceService().calculateAccount(oleInvoiceItem);
            }

            boolean isRulePassed = (KRADServiceLocatorWeb.getKualiRuleService()).applyRules(new AttributedBlanketApproveDocumentEvent("", oleInvoiceDocument));
            if (isRulePassed) {
                try {
                    getOleInvoiceServiceImpl().saveInvoiceDocument(oleInvoiceDocument);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return false;
            }
            isSuccess = true;
            if (isSuccess) {
                this.setOleInvoiceDocument(oleInvoiceDocument);
            }

        } catch (WorkflowException e) {
            LOG.error("Exception Occurred during creating Invoice Document" + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return isSuccess;
    }

    public List<OlePurchaseOrderDocument> populatePurchaseOrderDocuments(List<OlePurchaseOrderItem> purchaseOrderItems) {
        List<OlePurchaseOrderDocument> result = new ArrayList<OlePurchaseOrderDocument>();
        List<String> poId = new ArrayList<String>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (!poId.contains(olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString())) {
                if (olePurchaseOrderItem.isSelected()) {
                    poId.add(olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString());
                }
            }

        }
        for (String id : poId) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("purapDocumentIdentifier", id);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, map);
            if (olePurchaseOrderDocuments.size() > 0) {
                for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
                    if (olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching()) {
                        result.add(olePurchaseOrderDocument);
                    }
                }
            }

        }

        for (OlePurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
            if (purchaseOrderItem.isSelected()) {
                purchaseOrderItem.setItemForInvoice(false);
                for (OlePurchaseOrderDocument olePurchaseOrderDocument : result) {
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = olePurchaseOrderDocument.getItems();
                    for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                        if (purchaseOrderItem.getItemIdentifier().toString().equalsIgnoreCase(olePurchaseOrderItem.getItemIdentifier().toString()) && olePurchaseOrderItem.getItemTypeCode().equals("ITEM")) {
                            olePurchaseOrderItem.setSelected(true);
                            olePurchaseOrderItem.setItemForInvoice(true);
                            olePurchaseOrderItem.setInvoiceItemListPrice(purchaseOrderItem.getInvoiceItemListPrice());

                        }
                    }
                }
            }
        }
        return result;
    }


    public boolean addOlePurchaseOrderItemsToInvoiceDocument(List<OlePurchaseOrderDocument> olePurchaseOrderDocuments, List<OlePurchaseOrderItem> olePurchaseOrderItems, String documentNumber, String principalId, String invoiceAmt, String foreignInvoiceAmount) throws Exception {
        boolean isSuccess = false;
        //getting invoice document
        Map<String, String> map = new HashMap<String, String>();
        map.put("documentNumber", documentNumber);
        List<OleInvoiceDocument> oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
        OleInvoiceDocument oleInvoiceDocument = oleInvoiceDocuments.get(0);

        if (invoiceAmt != null) {
            //oleInvoiceDocument.setInvoiceAmount(invoiceAmt);
            oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceAmt));
        }

        if (foreignInvoiceAmount != null) {
            //oleInvoiceDocument.setInvoiceAmount(invoiceAmt);
            oleInvoiceDocument.setForeignVendorAmount(foreignInvoiceAmount);
        }
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocuments) {
            if (olePurchaseOrderDocument != null) {
                this.populateInvoiceFromPurchaseOrder(olePurchaseOrderDocument, oleInvoiceDocument, null, false);
                oleInvoiceDocument.getPurchaseOrderDocuments().add(olePurchaseOrderDocument);
            }
        }
        String description = getOlePurapService().getParameter(OLEConstants.INV_DESC);
        description = getOlePurapService().setDocumentDescription(description,null);
        oleInvoiceDocument.getDocumentHeader().setDocumentDescription(description);
        boolean isRulePassed = (KRADServiceLocatorWeb.getKualiRuleService()).applyRules(new AttributedBlanketApproveDocumentEvent("", oleInvoiceDocument));
        if (isRulePassed) {
            try {
                getOleInvoiceServiceImpl().saveInvoiceDocument(oleInvoiceDocument);
            } catch (Exception e) {
                LOG.error("Exception Occurred during updating Invoice Document" + e);
                e.printStackTrace();
            }
        } else {
            return false;
        }
        isSuccess = true;
        if (isSuccess) {
            this.setOleInvoiceDocument(oleInvoiceDocument);
        }
        return isSuccess;


    }

    public boolean validateReceivingForProcess(List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean isValid = false;
        int selectedItem = 0;
        int receivingExistForItem = 0;
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                selectedItem++;
                Map<String, String> map = new HashMap<String, String>();
                map.put("purchaseOrderIdentifier", olePurchaseOrderItem
                        .getItemIdentifier().toString());
                List<OleLineItemReceivingItem> oleLineItemReceivingItems = (List<OleLineItemReceivingItem>) KRADServiceLocator
                        .getBusinessObjectService().findMatching(
                                OleLineItemReceivingItem.class, map);
                if (oleLineItemReceivingItems.size() > 0) {
                    OleLineItemReceivingItem oleLineItemReceivingItem = oleLineItemReceivingItems.get(0);
                    /*if (oleLineItemReceivingItem.getReceiptStatusId().toString().equalsIgnoreCase(
                            getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED) + "")) {
                        */
                    receivingExistForItem++;
                    isValid = true;
                    olePurchaseOrderItem.setSelected(false);
                    /*}*/
                }
            }
        }
        if (selectedItem != receivingExistForItem) {
            this.isAllowedPoForReceiving = true;
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

    public OleInvoiceService getOleInvoiceServiceImpl() {

        OleInvoiceService oleInvoiceService = (OleInvoiceService) SpringContext.getBean(OleInvoiceService.class);
        return oleInvoiceService;
    }

    public boolean validateForReceiving(List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean isValid = true;
        String poid = "";
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                if (!poid.equalsIgnoreCase("")) {
                    if (!olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString().equalsIgnoreCase(poid)) {
                        isValid = false;
                        break;
                    }
                } else {
                    poid = olePurchaseOrderItem.getOlePurchaseOrderDocument().getPurapDocumentIdentifier().toString();
                }

            }
        }
        return isValid;
    }


    public void populateInvoiceFromPurchaseOrder(PurchaseOrderDocument po, OleInvoiceDocument oleInvoiceDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList, boolean isNew) {
        AccountsPayableDocumentSpecificService accountsPayableDocumentSpecificService = SpringContext.getBean(InvoiceService.class);
        if (isNew) {
            VendorService vendorService = SpringContext.getBean(VendorService.class);
            PurapService purapService = SpringContext.getBean(PurapService.class);
            //oleInvoiceDocument.setPaymentMethodId(po.getVendorDetail().getPaymentMethodId());
            oleInvoiceDocument.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
            oleInvoiceDocument.getDocumentHeader().setOrganizationDocumentNumber(po.getDocumentHeader().getOrganizationDocumentNumber());
            oleInvoiceDocument.setPostingYear(po.getPostingYear());
            oleInvoiceDocument.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());
            oleInvoiceDocument.setUseTaxIndicator(po.isUseTaxIndicator());
            oleInvoiceDocument.setInvoicePositiveApprovalIndicator(po.isPaymentRequestPositiveApprovalIndicator());
            oleInvoiceDocument.setVendorCustomerNumber(po.getVendorCustomerNumber());
            oleInvoiceDocument.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());

            if (po.getPurchaseOrderCostSource() != null) {
                oleInvoiceDocument.setInvoiceCostSource(po.getPurchaseOrderCostSource());
                oleInvoiceDocument.setInvoiceCostSourceCode(po.getPurchaseOrderCostSourceCode());
            }

            if (po.getVendorShippingPaymentTerms() != null) {
                oleInvoiceDocument.setVendorShippingPaymentTerms(po.getVendorShippingPaymentTerms());
                oleInvoiceDocument.setVendorShippingPaymentTermsCode(po.getVendorShippingPaymentTermsCode());
            }

            if (po.getVendorPaymentTerms() != null) {
                oleInvoiceDocument.setVendorPaymentTermsCode(po.getVendorPaymentTermsCode());
                oleInvoiceDocument.setVendorPaymentTerms(po.getVendorPaymentTerms());
            }

            if (po.getRecurringPaymentType() != null) {
                oleInvoiceDocument.setRecurringPaymentType(po.getRecurringPaymentType());
                oleInvoiceDocument.setRecurringPaymentTypeCode(po.getRecurringPaymentTypeCode());
            }

            Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(oleInvoiceDocument.getClass());
            if (defaultBank != null) {
                oleInvoiceDocument.setBankCode(defaultBank.getBankCode());
                oleInvoiceDocument.setBank(defaultBank);
            }
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorCustomerNumber(po.getVendorCustomerNumber());
            oleInvoiceDocument.setVendorName(po.getVendorName());

            // set original vendor
            oleInvoiceDocument.setOriginalVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setOriginalVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());

            // set alternate vendor info as well
            oleInvoiceDocument.setAlternateVendorHeaderGeneratedIdentifier(po.getAlternateVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setAlternateVendorDetailAssignedIdentifier(po.getAlternateVendorDetailAssignedIdentifier());

            // populate preq vendor address with the default remit address type for the vendor if found
            String userCampus = GlobalVariables.getUserSession().getPerson().getCampusCode();
            VendorAddress vendorAddress = vendorService.getVendorDefaultAddress(po.getVendorHeaderGeneratedIdentifier(), po.getVendorDetailAssignedIdentifier(), VendorConstants.AddressTypes.REMIT, userCampus);
            if (vendorAddress != null) {
                oleInvoiceDocument.templateVendorAddress(vendorAddress);
                oleInvoiceDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
                oleInvoiceDocument.setVendorAttentionName(StringUtils.defaultString(vendorAddress.getVendorAttentionName()));
            } else {
                // set address from PO
                oleInvoiceDocument.setVendorAddressGeneratedIdentifier(po.getVendorAddressGeneratedIdentifier());
                oleInvoiceDocument.setVendorLine1Address(po.getVendorLine1Address());
                oleInvoiceDocument.setVendorLine2Address(po.getVendorLine2Address());
                oleInvoiceDocument.setVendorCityName(po.getVendorCityName());
                oleInvoiceDocument.setVendorAddressInternationalProvinceName(po.getVendorAddressInternationalProvinceName());
                oleInvoiceDocument.setVendorStateCode(po.getVendorStateCode());
                oleInvoiceDocument.setVendorPostalCode(po.getVendorPostalCode());
                oleInvoiceDocument.setVendorCountryCode(po.getVendorCountryCode());

                boolean blankAttentionLine = StringUtils.equalsIgnoreCase(
                        "Y",
                        SpringContext.getBean(ParameterService.class).getParameterValueAsString(
                                PurapConstants.PURAP_NAMESPACE, "Document",
                                PurapParameterConstants.BLANK_ATTENTION_LINE_FOR_PO_TYPE_ADDRESS));

                if (blankAttentionLine) {
                    oleInvoiceDocument.setVendorAttentionName(StringUtils.EMPTY);
                } else {
                    oleInvoiceDocument.setVendorAttentionName(StringUtils.defaultString(po.getVendorAttentionName()));
                }
            }
        }
        oleInvoiceDocument.setInvoicePayDate(getOleInvoiceServiceImpl().calculatePayDate(oleInvoiceDocument.getInvoiceDate(), oleInvoiceDocument.getVendorPaymentTerms()));

        String currencyType = null;
        BigDecimal exchangeRate = null;

        oleInvoiceDocument.setInvoiceCurrencyType(po.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
            currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                // local vendor
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    oleInvoiceDocument.setForeignCurrencyFlag(true);
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                    exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceAmount())) {
                        oleInvoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(oleInvoiceDocument.getInvoiceAmount()).multiply(exchangeRate));
                        oleInvoiceDocument.setForeignInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                        oleInvoiceDocument.setForeignVendorAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                    }
                    if (StringUtils.isNotBlank(oleInvoiceDocument.getForeignInvoiceAmount())) {
                        oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getInvoiceAmount()).divide(new KualiDecimal(exchangeRate)));
                        oleInvoiceDocument.setInvoiceAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
                        oleInvoiceDocument.setVendorAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
                    }
                }
            }
        }

        //   if (getOleInvoiceServiceImpl().encumberedItemExistsForInvoicing(po)) {
        for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) po.getItems()) {
            // check to make sure it's eligible for payment (i.e. active and has encumbrance available
            if (poi.isSelected()) {
                if (poi.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                    poi.setPoOutstandingQuantity(poi.getItemQuantity().subtract(poi.getItemInvoicedTotalQuantity()));
                    poi.setNoOfCopiesInvoiced(new KualiInteger(poi.getItemQuantity().bigDecimalValue()));
                    poi.setNoOfPartsInvoiced(poi.getItemNoOfParts());
                    poi.setInvoiceItemListPrice(poi.getItemListPrice().toString());
                    if(poi.getItemForeignListPrice() != null) {
                        if (!poi.getItemForeignListPrice().equals("0.00")) {
                            poi.setInvoiceForeignItemListPrice(poi.getItemForeignListPrice().toString());
                            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                                oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                                //   String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                                if (StringUtils.isNotBlank(currencyType)) {
                                    if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                        poi.setInvoiceForeignCurrency(currencyType);
                                        oleInvoiceDocument.setForeignCurrencyFlag(true);
                                        poi.setItemDiscount(new KualiDecimal(0.0));
                                        if (StringUtils.isBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                                /*BigDecimal exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                                                oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                                poi.setInvoiceExchangeRate(exchangeRate.toString());*/
                                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                                            //     return getUIFModelAndView(oleInvoiceForm);
                                        } else {
                                            try {
                                                Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                                //   BigDecimal exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                                if (new KualiDecimal(exchangeRate).isZero()) {
                                                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                                    //       return getUIFModelAndView(oleInvoiceForm);
                                                }
                                                poi.setInvoiceExchangeRate(exchangeRate.toString());
                                            } catch (NumberFormatException nfe) {
                                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                                //      return getUIFModelAndView(oleInvoiceForm);
                                            }
                                        }

                                        // if the PO has Foreign Currency
                                        if (poi.getItemForeignListPrice() != null) {
                                            poi.setInvoiceForeignItemListPrice(poi.getItemForeignListPrice().toString());
                                            poi.setInvoiceForeignDiscount(poi.getItemForeignDiscount() != null ? poi.getItemForeignDiscount().toString() : new KualiDecimal("0.0").toString());
                                            poi.setInvoiceForeignUnitCost(poi.getItemForeignUnitCost().toString());
                                            poi.setInvoiceForeignCurrency(currencyType);

                                            if (poi.getInvoiceExchangeRate() != null && poi.getInvoiceForeignUnitCost() != null) {
                                                poi.setItemUnitCostUSD(new KualiDecimal(new BigDecimal(poi.getInvoiceForeignUnitCost()).divide(new BigDecimal(poi.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP)));
                                                poi.setItemUnitPrice(new BigDecimal(poi.getInvoiceForeignUnitCost()).divide(new BigDecimal(poi.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP));
                                                poi.setItemListPrice(poi.getItemUnitCostUSD());
                                                poi.setInvoiceItemListPrice(poi.getItemListPrice().toString());
                                            }
                                        } else {
                                            poi.setItemForeignUnitCost(new KualiDecimal(poi.getItemUnitPrice().multiply(new BigDecimal(poi.getInvoiceExchangeRate()))));
                                            poi.setItemForeignListPrice(poi.getItemForeignUnitCost());
                                            poi.setInvoiceForeignItemListPrice(poi.getItemForeignListPrice().toString());
                                            poi.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                                            poi.setInvoiceForeignUnitCost(poi.getItemForeignUnitCost().toString());
                                        }
                                        getInvoiceService().calculateAccount(poi);

                                    } else {
                                        oleInvoiceDocument.setForeignCurrencyFlag(false);
                                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                                        poi.setItemDiscount(poi.getItemDiscount() != null ? poi.getItemDiscount() : new KualiDecimal(0.0));
                                    }
                                }
                            }
                        }
                    } else {
                        oleInvoiceDocument.setForeignCurrencyFlag(false);
                        if(oleInvoiceDocument.getVendorDetail() != null) {
                            if (oleInvoiceDocument.getVendorDetail().getCurrencyType() != null) {
                                oleInvoiceDocument.setInvoiceCurrencyType(oleInvoiceDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
                                oleInvoiceDocument.setInvoiceCurrencyTypeId(oleInvoiceDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId());
                            }
                            else if((StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())))
                            {
                                oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                            }
                        }
                        else if((StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())))
                        {
                            oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                        }
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                        poi.setInvoiceExchangeRate(null);
                        poi.setItemExchangeRate(null);
                    }
                    if (poi.getItemTitleId() != null) {
                        poi.setItemDescription(SpringContext.getBean(OlePurapService.class).getItemDescription(poi));
                        // poi.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(poi.getItemTitleId()));
                    }

                }




                poi.setNoOfCopiesInvoiced(poi.getOleItemQuantity());
                OleInvoiceItem invoiceItem = new OleInvoiceItem(poi, oleInvoiceDocument);
                invoiceItem.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
                invoiceItem.setItemQuantity(poi.getItemQuantity());
                invoiceItem.setOlePoOutstandingQuantity(new KualiInteger(poi.getOutstandingQuantity().bigDecimalValue()));
                    /*invoiceItem.setExtendedPrice(poi.getExtendedPrice());*/
                invoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());
                invoiceItem.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());
                    /*if (new KualiDecimal(poi.getInvoiceItemListPrice()).isLessThan(AbstractKualiDecimal.ZERO)) {
                        invoiceItem.setInvoiceListPrice(poi.getInvoiceItemListPrice());
                    }
                    else {
                        invoiceItem.setItemListPrice(new KualiDecimal(poi.getInvoiceItemListPrice()));
                    }*/
                    /*List<OleInvoiceItem> oleInvoiceItems=(List<OleInvoiceItem>)oleInvoiceDocument.getItems();
                    int pos=1;
                    if(oleInvoiceItems.size()>0){
                        for(OleInvoiceItem oleInvoiceItem:oleInvoiceItems){
                            if(oleInvoiceItem.getItemTypeCode().equalsIgnoreCase("ITEM")){
                                pos=oleInvoiceItem.getItemLineNumber();
                            }
                        }
                        invoiceItem.setItemLineNumber(new Integer(pos+1));
                    }  else{
                        invoiceItem.setItemLineNumber(new Integer(pos));
                    }*/
                oleInvoiceDocument.getItems().add(invoiceItem);
                PurchasingCapitalAssetItem purchasingCAMSItem = po.getPurchasingCapitalAssetItemByItemIdentifier(poi.getItemIdentifier());
                if (purchasingCAMSItem != null) {
                    invoiceItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                }


                // copy use tax items over
                invoiceItem.getUseTaxItems().clear();
                for (PurApItemUseTax useTax : poi.getUseTaxItems()) {
                    invoiceItem.getUseTaxItems().add(useTax);
                }

            }
        }
        // }

        // add missing below the line
        /*purapService.addBelowLineItems(oleInvoiceDocument);*/
        /*oleInvoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());*/

        //fix up below the line items
        getOleInvoiceServiceImpl().removeIneligibleAdditionalCharges(oleInvoiceDocument);

        oleInvoiceDocument.fixItemReferences();
        oleInvoiceDocument.refreshNonUpdateableReferences();
    }

    public boolean receivePO(OleLineItemReceivingDocument oleLineItemReceivingDocument, OlePurchaseOrderDocument olePurchaseOrderDocument, boolean isCreateRCV, List<OlePurchaseOrderItem> purchaseOrderItems) {
        boolean receivePOSuccess = false;

        // Setting defaults
        oleLineItemReceivingDocument.setPurchaseOrderIdentifier(olePurchaseOrderDocument.getPurapDocumentIdentifier());
        DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
        oleLineItemReceivingDocument.setShipmentReceivedDate(dateTimeService.getCurrentSqlDate());
        // Validations Start
        boolean isValid = true;

      //  PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purchaseOrderIdentifier);

        if (ObjectUtils.isNotNull(olePurchaseOrderDocument)) {
            oleLineItemReceivingDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(olePurchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
            if (!SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(oleLineItemReceivingDocument).isAuthorizedByTemplate(oleLineItemReceivingDocument, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.OPEN_DOCUMENT, GlobalVariables.getUserSession().getPrincipalId())) {
                throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), "initiate document", oleLineItemReceivingDocument.getDocumentNumber());
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_PO_NOT_EXIST, oleLineItemReceivingDocument.getPurchaseOrderIdentifier().toString());
        }


        if (!SpringContext.getBean(ReceivingService.class).isPurchaseOrderActiveForLineItemReceivingDocumentCreation(oleLineItemReceivingDocument.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_PONOTACTIVE, oleLineItemReceivingDocument.getPurchaseOrderIdentifier().toString());

            isValid = false;
            return isValid;
        }

        if (SpringContext.getBean(ReceivingService.class).canCreateLineItemReceivingDocument(oleLineItemReceivingDocument.getPurchaseOrderIdentifier(), oleLineItemReceivingDocument.getDocumentNumber()) == false) {
            String inProcessDocNum = "";
            List<String> inProcessDocNumbers = SpringContext.getBean(ReceivingService.class).getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(oleLineItemReceivingDocument.getPurchaseOrderIdentifier(), oleLineItemReceivingDocument.getDocumentNumber());
            if (!inProcessDocNumbers.isEmpty()) {    // should not be empty if we reach this point
                inProcessDocNum = inProcessDocNumbers.get(0);
            }
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_DOCUMENT_ACTIVE_FOR_PO, oleLineItemReceivingDocument.getPurchaseOrderIdentifier().toString(), inProcessDocNum);
            isValid = false;
            return isValid;
        }
        try {
            if (isValid) {

                SpringContext.getBean(ReceivingService.class).populateAndSaveLineItemReceivingDocument(oleLineItemReceivingDocument);

                List<OleLineItemReceivingItem> itemList = new ArrayList<OleLineItemReceivingItem>();
                for (Object item : oleLineItemReceivingDocument.getItems()) {
                    OleLineItemReceivingItem rlItem = (OleLineItemReceivingItem) item;
                    // Receiving 100pc
                    boolean isPOItemPresent = false;
                    for (OlePurchaseOrderItem poItem : purchaseOrderItems) {
                        if (poItem.isSelected()) {
                            if (!isPOItemPresent
                                    && poItem.getItemIdentifier().equals(rlItem.getPurchaseOrderIdentifier())) {
                                rlItem.setOleItemReceivedTotalQuantity(rlItem.getOleItemReceivedToBeQuantity());

                                rlItem.setOleItemReceivedTotalParts(rlItem.getOleItemReceivedToBeParts());
                                rlItem.setPoSelected(true);
                                /*
                                 * rlItem.setItemReceivedTotalQuantity(rlItem.getItemOrderedQuantity());
                                 * rlItem.setItemReceivedTotalParts(rlItem.getItemOrderedParts());
                                 */
                                isPOItemPresent = true;
                            } else if (!isPOItemPresent) {
                                rlItem.setItemReceivedTotalQuantity(KualiDecimal.ZERO);
                                rlItem.setItemReceivedTotalParts(KualiDecimal.ZERO);
                            }
                        }
                    }

                    itemList.add(rlItem);
                }
                if (ObjectUtils.isNotNull(itemList) && itemList.size() > 0) {
                    oleLineItemReceivingDocument.setItems(itemList);

                    Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                    //oleLineItemReceivingDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().createWorkflowDocument(oleLineItemReceivingDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), principalPerson));
                    oleLineItemReceivingDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(oleLineItemReceivingDocument.getDocumentNumber(), principalPerson));
                    //oleLineItemReceivingDocument.setAdHocRoutePersons(buildFyiRecipient());
                    if (isCreateRCV) {
                        SpringContext.getBean(DocumentService.class).saveDocument(oleLineItemReceivingDocument);
                    } else {
                        List<OleLineItemReceivingItem> items = oleLineItemReceivingDocument.getItems();
                        for (OleLineItemReceivingItem item : items) {
                            OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
                            OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(item.getPurchaseOrderIdentifier());
                            if (olePurchaseOrderItem != null) {
                                if (item.isPoSelected()) {
                                    for (OleCopy oleCopy : item.getCopyList()) {
                                        oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS);
                                    }
                                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                                    oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, item, null, oleLineItemReceivingDocument.getIsATypeOfRCVGDoc());
                                }
                            }
                        }
                        try {
                            SpringContext.getBean(DocumentService.class).routeDocument(oleLineItemReceivingDocument, "Line Item Receiving from Receiving Queue Search page", null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    receivePOSuccess = true;
                }
            }
        } catch (WorkflowException wfe) {
            String rcvDocNum = oleLineItemReceivingDocument.getDocumentNumber();
            String poId = oleLineItemReceivingDocument.getPurchaseOrderIdentifier().toString();
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_SAVE_OR_SUBMIT, new String[]{poId, rcvDocNum, wfe.getMessage()});

          /*  wfe.printStackTrace();*/
            isValid = false;
            return isValid;
        }

        return receivePOSuccess;
    }


    public boolean receiveAndPay(List<OlePurchaseOrderItem> purchaseOrderItems, OlePurchaseOrderDocument olePurchaseOrderDocument) throws Exception {
        boolean isSuccess = false;

        OleLineItemReceivingDocument oleLineItemReceivingDocument = (OleLineItemReceivingDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("OLE_RCVL");
        // Elimiate duplicate POs from the list
        HashMap<Integer, OlePurchaseOrderItem> selectedPOs = getSelectedPurchaseOrders(purchaseOrderItems);
        // Map containing PO ID and receive PO status
        HashMap<Integer, Boolean> receivePOStatus = new HashMap<Integer, Boolean>();
        boolean isInfoMsg = false;
        for (Map.Entry<Integer, OlePurchaseOrderItem> entry : selectedPOs.entrySet()) {
            boolean receivePOSuccess = this.receivePO(oleLineItemReceivingDocument, olePurchaseOrderDocument, false, purchaseOrderItems);
            if (receivePOSuccess) {
                receivingDocumentsList.add(oleLineItemReceivingDocument.getDocumentNumber());
                isInfoMsg = true;
                isSuccess = true;
            }
            receivePOStatus.put(entry.getKey(), receivePOSuccess);

        }
        List<OlePurchaseOrderItem> refreshedPOList = new ArrayList<OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : purchaseOrderItems) {
            Integer poId = poItem.getPurchaseOrder().getPurapDocumentIdentifier();
            if (ObjectUtils.isNull(receivePOStatus.get(poId))) {
                refreshedPOList.add(poItem);
            } else {
                if (receivePOStatus.get(poId) == false) {
                    refreshedPOList.add(poItem);
                }
            }
        }
        return isSuccess;
    }


    private HashMap<Integer, OlePurchaseOrderItem> getSelectedPurchaseOrders(List<OlePurchaseOrderItem> purchaseOrders) {
        HashMap<Integer, OlePurchaseOrderItem> poItemMap = new HashMap<Integer, OlePurchaseOrderItem>();
        for (OlePurchaseOrderItem poItem : purchaseOrders) {
            if (poItem.isSelected()) {
                if (ObjectUtils.isNull(poItemMap.get(poItem.getPurchaseOrder().getPurapDocumentIdentifier()))) {
                    poItemMap.put(poItem.getPurchaseOrder().getPurapDocumentIdentifier(), poItem);
                }
            }
        }


        return poItemMap;
    }

    public boolean validateRecords(Integer purAppNum, String docNumber) {
        boolean valid = true;

        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum);
        valid &= SpringContext.getBean(ReceivingService.class).canCreateLineItemReceivingDocument(purAppNum, null);
        return valid;
    }

    public boolean validateStatusOfPurchaseOrderDocument(OlePurchaseOrderDocument olePurchaseOrderDocument) {
        boolean isValid = true;
        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(olePurchaseOrderDocument.getPurapDocumentIdentifier());
        if (po.getApplicationDocumentStatus().equalsIgnoreCase("Closed")) {
            isValid = false;
        }
        return isValid;
    }

    public boolean validateInvoiceDocumentNumber(String documentNumber) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("documentNumber", documentNumber);
        List<OleInvoiceDocument> oleInvoiceDocuments = (List<OleInvoiceDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInvoiceDocument.class, map);
        if (oleInvoiceDocuments.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean validateNumber(final String hex) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public boolean isPoAlreadyPaid(List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        boolean isPaid = false;
        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
            if (olePurchaseOrderItem.isSelected()) {
                if (olePurchaseOrderItem.getCopyList() != null && olePurchaseOrderItem.getCopyList().size() > 0) {
                    for (OleCopy oleCopy : olePurchaseOrderItem.getCopyList()) {
                        if (oleCopy != null && oleCopy.getOlePaidCopies() != null && oleCopy.getOlePaidCopies().size() > 0) {
                            for (OLEPaidCopy olePaidCopy : oleCopy.getOlePaidCopies()) {
                                if (olePaidCopy != null && olePaidCopy.getPaymentRequestIdentifier() != null) {
                                    isPaid = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isPaid;
    }

    /*
        This method updates the Foreign Details on the Invoice Document if it has foreign currency.
     */
  /*  private void updateForeignCurrencyDetails(OleInvoiceDocument oleInvoiceDocument) {
        String currencyType = null;
        BigDecimal exchangeRate = null;
        oleInvoiceDocument.setInvoiceCurrencyType(oleInvoiceDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
            currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                // local vendor
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    oleInvoiceDocument.setForeignCurrencyFlag(true);
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                    exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceAmount())) {
                        oleInvoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(oleInvoiceDocument.getInvoiceAmount()).multiply(exchangeRate));
                        oleInvoiceDocument.setForeignInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                        oleInvoiceDocument.setForeignVendorAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                    }
                }
            }
        }

    }*/



}
