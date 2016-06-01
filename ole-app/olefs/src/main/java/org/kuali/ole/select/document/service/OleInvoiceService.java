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
package org.kuali.ole.select.document.service;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;

import java.util.HashMap;
import java.util.List;

public interface OleInvoiceService extends InvoiceService {

    public void completePaymentDocument(OleInvoiceDocument invoiceDocument);

    public void calculateProrateItemSurcharge(OleInvoiceDocument invoiceDocument);

    public boolean validateProratedSurcharge(OleInvoiceDocument invoiceDocument);

    public void calculateWithoutProrates(OleInvoiceDocument invoiceDocument);

    /**
     * Checks whether the invoice document is eligible for auto approval. If so, then updates
     * the status of the document to auto approved and calls the documentService to blanket approve
     * the document, then returns false.
     * If the document is not eligible for auto approval then returns true.
     *
     * @param doc The invoice document to be auto approved.
     * @return boolean true if the invoice document is not eligible for auto approval.
     * @throws RuntimeException To indicate to Spring transactional management that the transaction for this document should be rolled back
     */
    public boolean autoApprovePaymentRequest(PaymentRequestDocument doc);

    public void createPaymentRequestOrCreditMemoDocument(OleInvoiceDocument inv);

    public OleInvoiceDocument populateInvoiceFromPurchaseOrders(OleInvoiceDocument invoiceDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList);

    public void populateInvoice(OleInvoiceDocument invoiceDocument);

    public OleInvoiceDocument populateVendorDetail(String vendorNumber, OleInvoiceDocument oleInvoiceDocument);

    public void createCreditMemoDocument(OleInvoiceDocument invoiceDocument, List<OleInvoiceItem> item,boolean flag);

    public OleInvoiceDocument getInvoiceDocumentById(Integer invoiceIdentifier);

    public String saveInvoiceDocument(OleInvoiceDocument invoiceDocument) throws WorkflowException;

    public String routeInvoiceDocument(OleInvoiceDocument invoiceDocument) throws WorkflowException;

    public boolean autoApprovePaymentRequest(OleInvoiceDocument doc);

    public OleInvoiceDocument populateInvoiceItems (OleInvoiceDocument invoiceDocument, List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList);

    public OleInvoiceDocument populateInvoiceDocument (OleInvoiceDocument invoiceDocument);

    public void calculateAccount(PurApItem purapItem);

    public void convertPOItemToInvoiceItem(OleInvoiceDocument oleInvoiceDocument, OlePurchaseOrderDocument olePurchaseOrderDocument);

    public String createInvoiceNoMatchQuestionText(OleInvoiceDocument invoiceDocument);

    public String createSubscriptionDateOverlapQuestionText(OleInvoiceDocument invoiceDocument);

    public String getParameter(String name);

    public KualiDecimal getTotalDollarAmountWithExclusionsSubsetItems(String[] excludedTypes, boolean includeBelowTheLine, List<PurApItem> itemsForTotal);

    public String[] getCollapseSections();

    public String[] getDefaultCollapseSections();

    public boolean canCollapse(String sectionName,String[] collapseSections);

    public boolean isDuplicationExists(OleInvoiceDocument invoiceDocument, OLEInvoiceForm invoiceForm, String actionName);

    public String getPaymentMethodType(String paymentId);

    public boolean validateDepositAccount(OleInvoiceDocument oleInvoiceDocument);

    public boolean isNotificationRequired(OleInvoiceDocument invoiceDocument);

    public String createInvoiceAmountExceedsThresholdText(OleInvoiceDocument oleInvoiceDocument);

    public OleInvoiceRecord populateValuesFromProfile(BibMarcRecord bibMarcRecord);

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo);

    public void setDefaultAndConstantValuesToInvoiceRecord(OleInvoiceRecord oleInvoiceRecord);

    public String getCurrencyType(String currencyTypeId);

    public OleExchangeRate getExchangeRate(String currencyTypeId);

    public String getCurrencyTypeIdFromCurrencyType(String currencyType);

    public void deleteInvoiceItem(OleInvoiceDocument oleInvoiceDocument);

    public String getPurchaseOrderVendor(String poId);

    public List<String> getRecurringOrderTypes();
}
