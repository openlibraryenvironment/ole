/*
 * Copyright 2008-2009 The Kuali Foundation
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
import org.kuali.ole.module.purap.document.BulkReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.BulkReceivingService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

public class BulkReceivingPurchaseOrderIdValidation extends GenericValidation {

    private PurchaseOrderService purchaseOrderService;

    public boolean validate(AttributedDocumentEvent event) {

        boolean valid = true;

        BulkReceivingDocument bulkReceivingDocument = (BulkReceivingDocument) event.getDocument();

        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);

        return canCreateBulkReceivingDocument(bulkReceivingDocument);

    }

    /**
     * Determines if it is valid to create a bulk receiving document.
     *
     * @param bulkReceivingDocument
     * @return
     */
    protected boolean canCreateBulkReceivingDocument(BulkReceivingDocument bulkReceivingDocument) {

        boolean valid = true;

        if (bulkReceivingDocument.getPurchaseOrderIdentifier() != null) {
            PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(bulkReceivingDocument.getPurchaseOrderIdentifier());

            if (ObjectUtils.isNull(po)) {
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_BULK_RECEIVING_DOCUMENT_INVALID_PO, bulkReceivingDocument.getDocumentNumber(), bulkReceivingDocument.getPurchaseOrderIdentifier().toString());
                valid = false;
            } else {
                if (!(po.getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN) ||
                        po.getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_CLOSED))) {
                    valid &= false;
                    GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_BULK_RECEIVING_PO_NOT_OPEN, bulkReceivingDocument.getDocumentNumber(), bulkReceivingDocument.getPurchaseOrderIdentifier().toString());
                } else {
                    String docNumberInProcess = SpringContext.getBean(BulkReceivingService.class).getBulkReceivingDocumentNumberInProcessForPurchaseOrder(po.getPurapDocumentIdentifier(), bulkReceivingDocument.getDocumentNumber());
                    if (StringUtils.isNotEmpty(docNumberInProcess)) {
                        valid &= false;
                        GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, PurapKeyConstants.ERROR_BULK_RECEIVING_DOCUMENT_ACTIVE_FOR_PO, docNumberInProcess, bulkReceivingDocument.getPurchaseOrderIdentifier().toString());
                    }
                }
            }
        }

        return valid;
    }

    public PurchaseOrderService getPurchaseOrderService() {
        return purchaseOrderService;
    }

    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

}
