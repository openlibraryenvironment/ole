/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.document.service;

import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.vnd.businessobject.VendorDetail;

import java.math.BigDecimal;
import java.util.Map;

public interface OlePurapService {


    public BigDecimal calculateDiscount(OleRequisitionItem oleRequisition);

    public BigDecimal calculateDiscount(OlePurchaseOrderItem olePurchaseOrder);

    public OleRequisitionItem calculateForeignCurrency(OleRequisitionItem item);

    public OlePurchaseOrderItem calculateForeignCurrency(OlePurchaseOrderItem item);

    public BigDecimal calculateDiscount(OlePaymentRequestItem olePaymentRequestOrder);

    public OlePaymentRequestItem calculateForeignCurrency(OlePaymentRequestItem item);

    public BigDecimal calculateDiscount(OleInvoiceItem oleInvoiceOrder);

    public OleInvoiceItem calculateForeignCurrency(OleInvoiceItem item);

    public String getOperatorInitials();

    public OleCreditMemoItem calculateForeignCurrency(OleCreditMemoItem item);

    public void getInitialCollapseSections(PurchasingAccountsPayableDocument document);

    public String getPatronName(String patronId);

    public void setBibMarcRecord(BibMarcRecord bibMarcRecord,BibInfoBean bibInfoBean);

    public void setInvoiceDocumentsForRequisition(PurApItem purApItem);

    public void setInvoiceDocumentsForPO(OlePurchaseOrderItem singleItem);

    public void setInvoiceDocumentsForEResourcePO(PurApItem purApItem);

    public void setInvoiceDocumentsForPO(PurchaseOrderDocument purchaseOrderDocument,OlePurchaseOrderItem singleItem);

    public Integer getRequestorTypeId(String requestorType);

    public void setClaimDateForReq(OleRequisitionItem oleRequisitionItem,VendorDetail vendorDetail);

    public void setClaimDateForPO(OlePurchaseOrderItem olePurchaseOrderItem,VendorDetail vendorDetail);

    public String getItemDescription(Bib bib);

    public PurchaseOrderType getPurchaseOrderType(BigDecimal purchaseOrderTypeId);

    public String getParameter(String name);

    public String getCurrentDateTime();

    public String setDocumentDescription(String description, Map descMap);

    public String getItemDescription(OlePurchaseOrderItem olePurchaseOrderItem);

}
