/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.service;

import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.util.VendorGroupingHelper;
import org.kuali.ole.vnd.businessobject.PaymentTermType;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Defines methods that must be implemented by a InvoiceService implementation.
 */
public interface InvoiceService extends AccountsPayableDocumentSpecificService {

    /**
     * Obtains a list of invoice documents given the purchase order id.
     *
     * @param poDocId The purchase order id to be used to obtain a list of invoice documents.
     * @return The List of invoice documents given the purchase order id.
     */
  //  public List<InvoiceDocument> getInvoicesByPurchaseOrderId(Integer poDocId);

    /**
     * Obtains a list of invoice documents given the purchase order id, the invoice amount
     * and the invoice date.
     *
     * @param poId          The purchase order id used to obtain the invoice documents.
     * @param invoiceAmount The invoice amount used to obtain the invoice documents.
     * @param invoiceDate   The invoice date used to obtain the invoice documents.
     * @return The List of invoice documents that match the given criterias (purchase order id, invoice amount and invoice date).
     */
    public List getInvoicesByPOIdInvoiceAmountInvoiceDate(Integer poId, KualiDecimal invoiceAmount, Date invoiceDate);

    /**
     * Obtains a list of invoice documents given the vendorHeaderGeneratedIdentifier, vendorDetailAssignedIdentifier and the invoice number.
     *
     * @param vendorHeaderGeneratedIdentifier
     *                                       The vendorHeaderGeneratedIdentifier used to obtain the invoice documents.
     * @param vendorDetailAssignedIdentifier The vendorDetailAssignedIdentifier used to obtain the invoice documents.
     * @return The List of invoice documents that match the given criterias.
     */
    public List getInvoicesByVendorNumber(Integer vendorHeaderGeneratedIdentifier, Integer vendorDetailAssignedIdentifier);

    /**
     * Obtains a list of invoice documents given the vendorHeaderGeneratedIdentifier, vendorDetailAssignedIdentifier and the invoice number.
     *
     * @param vendorHeaderGeneratedIdentifier
     *                                       The vendorHeaderGeneratedIdentifier used to obtain the invoice documents.
     * @param vendorDetailAssignedIdentifier The vendorDetailAssignedIdentifier used to obtain the invoice documents.
     * @param invoiceNumber                  The invoice number used to obtain the invoice documents.
     * @return The List of invoice documents that match the given criterias.
     */
    public List getInvoicesByVendorNumberInvoiceNumber(Integer vendorHeaderGeneratedIdentifier, Integer vendorDetailAssignedIdentifier, String invoiceNumber);


    /**
     * Determines whether the invoice date is after today.
     *
     * @param invoiceDate The invoice date to be determined whether it's after today.
     * @return boolean true if the given invoice date is after today.
     */
    public boolean isInvoiceDateAfterToday(Date invoiceDate);

    /**
     * Performs the processing to check whether the invoice is a duplicate and if so, adds
     * the information about the type of duplication into the resulting HashMap to be returned by this method.
     *
     * @param document The invoice document to be processed/checked for duplicates.
     * @return The HashMap containing "PREQDuplicateInvoice" as the key and the string
     *         describing the types of duplication as the value.
     */
    public HashMap<String, String> invoiceDuplicateMessages(InvoiceDocument document);

    /**
     * Calculate based on the terms and calculate a date 10 days from today. Pick the one that is the farthest out. We always
     * calculate the discount date, if there is one.
     *
     * @param invoiceDate The invoice date to be used in the pay date calculation.
     * @param terms       The payment term type to be used in the pay date calculation.
     * @return The resulting pay date given the invoice date and the terms.
     */
    public Date calculatePayDate(Date invoiceDate, PaymentTermType terms);

    /**
     * Marks a invoice on hold.
     *
     * @param document The invoice document to be marked as on hold.
     * @param note     The note to be added to the invoice document while being marked as on hold.
     * @return The InvoiceDocument with updated information.
     * @throws Exception
     */
    public InvoiceDocument addHoldOnInvoice(InvoiceDocument document, String note) throws Exception;

    /**
     * Removes a hold on a invoice.
     *
     * @param document The invoice document whose hold is to be removed.
     * @param note     The note to be added to the invoice document while its hold is being removed.
     * @return The InvoiceDocument with updated information.
     * @throws Exception
     */
    public InvoiceDocument removeHoldOnInvoice(InvoiceDocument document, String note) throws Exception;

    /**
     * Obtains the invoice document given the purapDocumentIdentifier.
     *
     * @param poDocId The purapDocumentIdentifier of the invoice document to be obtained.
     * @return The invoice document whose purapDocumentIdentifier matches with the input parameter.
     */
    public InvoiceDocument getInvoiceById(Integer poDocId);

    /**
     * Obtains the invoice document given the document number.
     *
     * @param documentNumber The document number to be used to obtain the invoice document.
     * @return The invoice document whose document number matches with the given input parameter.
     */
    public InvoiceDocument getInvoiceByDocumentNumber(String documentNumber);

    /**
     * Marks a invoice as requested to be canceled.
     *
     * @param document The invoice document to be marked as requested to be canceled.
     * @param note     The note to be added to the invoice document while being marked as requested to be canceled.
     * @throws Exception
     */
    public void requestCancelOnInvoice(InvoiceDocument document, String note) throws Exception;

    /**
     * Returns true if the invoice has been extracted
     *
     * @param document The invoice document to be determined whether it is extracted.
     * @return boolean true if the invoice document is extracted.
     */
    public boolean isExtracted(InvoiceDocument document);

    /**
     * Removes a request cancel on invoice.
     *
     * @param document The invoice document to be used for request cancel.
     * @param note     The note to be added to the invoice document upon the removal of the request cancel.
     * @throws Exception
     */
    public void removeRequestCancelOnInvoice(InvoiceDocument document, String note) throws Exception;

    /**
     * Resets a Invoice that had an associated Invoice or Credit Memo cancelled externally.
     *
     * @param invoice The extracted invoice document to be resetted.
     * @param note    The note to be added to the invoice document upon its reset.
     */
    public void resetExtractedInvoice(InvoiceDocument invoice, String note);

    /**
     * Cancels a PREQ that has already been extracted if allowed.
     *
     * @param invoice The extracted invoice document to be canceled.
     * @param note    The note to be added to the invoice document.
     */
    public void cancelExtractedInvoice(InvoiceDocument invoice, String note);

    /**
     * Get all the invoices that are immediate and need to be extracted to PDP.
     *
     * @param chartCode The chart code to be used as one of the criterias to retrieve the invoice documents.
     * @return The iterator of the list of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getImmediateInvoicesToExtract(String chartCode);

    /**
     * Get all the invoices that match a credit memo.
     *
     * @param cmd The credit memo document to be used to obtain the invoices.
     * @return The iterator of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getInvoicesToExtractByCM(String campusCode, VendorCreditMemoDocument cmd);

    /**
     * Get all the invoices that match a vendor.
     *
     * @param vendor
     * @param onOrBeforeInvoicePayDate only invoices with a pay date on or before this date will be extracted
     * @return Collection of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getInvoicesToExtractByVendor(String campusCode, VendorGroupingHelper vendor, Date onOrBeforeInvoicePayDate);

    /**
     * Get all the invoices that need to be extracted.
     *
     * @return The Collection of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getInvoicesToExtract(Date onOrBeforeInvoicePayDate);

    /**
     * Get all the special invoices for a single chart that need to be extracted.
     *
     * @param chartCode The chart code to be used as one of the criterias to retrieve the invoice documents.
     * @return The Collection of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getInvoicesToExtractSpecialPayments(String chartCode, Date onOrBeforeInvoicePayDate);

    /**
     * Get all the regular invoices for a single campus.
     *
     * @param chartCode The chart code to be used as one of the criterias to retrieve the invoice documents.
     * @return The collection of the resulting invoice documents returned by the invoiceDao.
     */
    public Collection<InvoiceDocument> getInvoiceToExtractByChart(String chartCode, Date onOrBeforeInvoicePayDate);

    /**
     * Recalculate the invoice.
     *
     * @param pr             The invoice document to be calculated.
     * @param updateDiscount boolean true if we also want to calculate the discount items for the invoice.
     */
    public void calculateInvoice(InvoiceDocument pr, boolean updateDiscount);

    /**
     * Performs calculations on the tax edit area, generates and adds NRA tax charge items as below the line items, with their accounting lines;
     * the calculation will activate updates on the account summary tab and the general ledger entries as well.
     * <p/>
     * The non-resident alien (NRA) tax lines consist of four possible sets of tax lines:
     * - Federal tax lines
     * - Federal Gross up tax lines
     * - State tax lines
     * - State Gross up tax lines
     * <p/>
     * Federal tax lines are generated if the federal tax rate in the invoice is not zero.
     * State tax lines are generated if the state tax rate in the invoice is not zero.
     * Gross up tax lines are generated if the tax gross up indicator is set on the invoice and the tax rate is not zero.
     *
     * @param preq The invoice the NRA tax lines will be added to.
     */
    public void calculateTaxArea(InvoiceDocument preq);

    /**
     * Populate invoice.
     *
     * @param preq  The invoice document to be populated.
     */
    // public void populateInvoice(InvoiceDocument preq);

    /**
     * Populate and save invoice.
     *
     * @param preq The invoice document to be populated and saved.
     */
    public void populateAndSaveInvoice(InvoiceDocument preq) throws WorkflowException;

    /**
     * Retrieve a list of PREQs that aren't approved, check to see if they match specific requirements, then auto-approve them if
     * possible.
     *
     * @return boolean true if the auto approval of invoices has at least one error.
     */
    // public boolean autoApproveInvoices();

    /**
     * Checks whether the invoice document is eligible for auto approval. If so, then updates
     * the status of the document to auto approved and calls the documentService to blanket approve
     * the document, then returns false.
     * If the document is not eligible for auto approval then returns true.
     *
     * @param docNumber           The invoice document number (not the invoice ID) to be auto approved.
     * @param defaultMinimumLimit The default minimum limit amount to be used in determining the eligibility of the document to be auto approved.
     * @return boolean true if the invoice document is not eligible for auto approval.
     * @throws RuntimeException To indicate to Spring transactional management that the transaction for this document should be rolled back
     */
   // public boolean autoApproveInvoice(String docNumber, KualiDecimal defaultMinimumLimit);

    /**
     * Checks whether the invoice document is eligible for auto approval. If so, then updates
     * the status of the document to auto approved and calls the documentService to blanket approve
     * the document, then returns false.
     * If the document is not eligible for auto approval then returns true.
     *
     * @param doc                 The invoice document to be auto approved.
     * @param defaultMinimumLimit The default minimum limit amount to be used in determining the eligibility of the document to be auto approved.
     * @return boolean true if the invoice document is not eligible for auto approval.
     * @throws RuntimeException To indicate to Spring transactional management that the transaction for this document should be rolled back
     */
    public boolean autoApproveInvoice(InvoiceDocument doc, KualiDecimal defaultMinimumLimit);

    /**
     * Mark a invoice as being paid and set the invoice's paid date as the processDate.
     *
     * @param pr          The invoice document to be marked as paid and paid date to be set.
     * @param processDate The date to be set as the invoice's paid date.
     */
    public void markPaid(InvoiceDocument pr, Date processDate);

    /**
     * This method specifies whether the invoice document has a discount item.
     *
     * @param preq The invoice document to be verified whether it has a discount item.
     * @return boolean true if the invoice document has at least one discount item.
     */
    public boolean hasDiscountItem(InvoiceDocument preq);

    /**
     * Changes the current vendor to the vendor passed in.
     *
     * @param preq
     * @param headerId
     * @param detailId
     */
    public void changeVendor(InvoiceDocument preq, Integer headerId, Integer detailId);

    /**
     * A method to create the description for the invoice document.
     *
     * @param purchaseOrderIdentifier The purchase order identifier to be used as part of the description.
     * @param vendorName              The vendor name to be used as part of the description.
     * @return The resulting description string for the invoice document.
     */
    public String createPreqDocumentDescription(Integer purchaseOrderIdentifier, String vendorName);

    /**
     * Determines if there are active invoices for a purchase order.
     *
     * @param purchaseOrderIdentifier
     * @return
     */
    public boolean hasActiveInvoicesForPurchaseOrder(Integer purchaseOrderIdentifier);

    public void processInvoiceInReceivingStatus();

    /**
     * Invoices created in the previous fiscal year get backdated if we're at the beginning of the new fiscal year (i.e.
     * prior to first closing)
     *
     * @param invoiceDocument
     * @return
     */
    public boolean allowBackpost(InvoiceDocument invoiceDocument);

    public boolean isPurchaseOrderValidForInvoiceDocumentCreation(InvoiceDocument invoiceDocument, PurchaseOrderDocument po);

    /**
     * Removes additional charge items that are not eligible on the invoice document.
     *
     * @param document
     */
    public void removeIneligibleAdditionalCharges(InvoiceDocument document);

    public boolean encumberedItemExistsForInvoicing(PurchaseOrderDocument document);

    /**
     * Clears tax info.
     *
     * @param document The disbursement voucher document being modified.
     */
    public void clearTax(InvoiceDocument document);

    /**
     * Checks whether the invoice document is eligible for auto approval. If so, then updates
     * the status of the document to auto approved and calls the documentService to blanket approve
     * the document, then returns false.
     * If the document is not eligible for auto approval then returns true.
     *
     * @param docNumber The invoice document number (not the invoice ID) to be auto approved.
     * @return boolean true if the invoice document is not eligible for auto approval.
     * @throws RuntimeException To indicate to Spring transactional management that the transaction for this document should be rolled back
     */
    public boolean autoApprovePaymentRequest(String docNumber);

    public String getParameter(String namespaceCode, String componentCode, String parameterName);

    public boolean getParameterBoolean(String namespaceCode, String componentCode, String parameterName);

}

