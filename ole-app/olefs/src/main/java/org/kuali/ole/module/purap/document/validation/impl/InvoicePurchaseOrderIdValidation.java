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
package org.kuali.ole.module.purap.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

public class InvoicePurchaseOrderIdValidation extends GenericValidation {

    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        boolean lineItemtypeIndicator = false;
        OleInvoiceDocument document = (OleInvoiceDocument) event.getDocument();
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        Integer POID = document.getPurchaseOrderIdentifier();
        if (document.getItems().size() > 0) {
            Set closedVendorIds = new TreeSet();
            Set pendingActionPoIds = new TreeSet();
            for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) document.getItems()) {
                if (invoiceItem.getItemType().isLineItemIndicator()) {
                    lineItemtypeIndicator = true;
                }
                //PurchaseOrderDocument purchaseOrderDocument =document.getPurchaseOrderDocument(invoiceItem.getPurchaseOrderIdentifier());
                PurchaseOrderDocument purchaseOrderDocument = null ;
                Map map = new HashMap();
                map.put("purapDocumentIdentifier",invoiceItem.getPurchaseOrderIdentifier());
                List<OlePurchaseOrderDocument> purchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, map);
                if(purchaseOrderDocumentList!=null && purchaseOrderDocumentList.size()>0){
                    for (OlePurchaseOrderDocument poDoc : purchaseOrderDocumentList) {
                        if(poDoc.getPurchaseOrderCurrentIndicatorForSearching()){
                            purchaseOrderDocument = poDoc;
                        }
                    }
                }
                if (purchaseOrderDocument != null && purchaseOrderDocument.getDocumentHeader() == null) {
                    purchaseOrderDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(purchaseOrderDocument.getDocumentNumber()));

                }
                //OlePurchaseOrderDocument purchaseOrderDocument = (OlePurchaseOrderDocument) purchaseOrderDocument1;
                if (purchaseOrderDocument != null && purchaseOrderDocument.isPendingActionIndicator()) {
                    pendingActionPoIds.add(purchaseOrderDocument.getPurapDocumentIdentifier());
                } else if (purchaseOrderDocument != null && !StringUtils.equals(purchaseOrderDocument.getApplicationDocumentStatus(), PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN)) {
                    closedVendorIds.add(purchaseOrderDocument.getPurapDocumentIdentifier());
                    // if the PO is pending and it is not a Retransmit, we cannot generate a Invoice for it
                }
            }
            if(pendingActionPoIds.size() > 0) {
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_PENDING_ACTION,pendingActionPoIds.toString().replace("[","").replace("]",""));
                valid &= false;
            }
            if(closedVendorIds.size() > 0){
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_POS_NOT_OPEN,closedVendorIds.toString().replace("[" ,"").replace("]",""));
                valid &= false;
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_EXIST);
            valid &= false;
        }


        //for (OlePurchaseOrderDocument purchaseOrderDocument : document.getPurchaseOrderDocuments()) {
        //PurchaseOrderDocument purchaseOrderDocument = document.getPurchaseOrderDocument();
            /*if (!lineItemtypeIndicator) {
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_EXIST);
                valid &= false;
            }*/
        return valid;
    }

    /**
     * Determines if there are items with encumbrances to be invoiced on passed in
     * purchase order document.
     *
     * @param document - purchase order document
     * @return
     */
    protected boolean encumberedItemExistsForInvoicing(PurchaseOrderDocument document) {
        boolean zeroDollar = true;
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);
        for (PurchaseOrderItem poi : (List<PurchaseOrderItem>) document.getItems()) {
            // Quantity-based items
            if (poi.getItemType().isLineItemIndicator() && poi.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                KualiDecimal encumberedQuantity = poi.getItemOutstandingEncumberedQuantity() == null ? KualiDecimal.ZERO : poi.getItemOutstandingEncumberedQuantity();
                if (encumberedQuantity.compareTo(KualiDecimal.ZERO) == 1) {
                    zeroDollar = false;
                    break;
                }
            }
            // Service Items or Below-the-line Items
            else if (poi.getItemType().isAmountBasedGeneralLedgerIndicator() || poi.getItemType().isAdditionalChargeIndicator()) {
                KualiDecimal encumberedAmount = poi.getItemOutstandingEncumberedAmount() == null ? KualiDecimal.ZERO : poi.getItemOutstandingEncumberedAmount();
                if (encumberedAmount.compareTo(KualiDecimal.ZERO) == 1) {
                    zeroDollar = false;
                    break;
                }
            }
        }
        if (zeroDollar) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_NO_ITEMS_TO_INVOICE);
        }
        //GlobalVariables.getMessageMap().clearErrorPath();
        return !zeroDollar;
    }

}
