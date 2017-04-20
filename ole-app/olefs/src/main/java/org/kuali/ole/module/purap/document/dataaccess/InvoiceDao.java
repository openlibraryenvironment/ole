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
package org.kuali.ole.module.purap.document.dataaccess;

import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.util.VendorGroupingHelper;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

/**
 * Invoice DAO Interface.
 */
public interface InvoiceDao {

    /**
     * Get all the invoices that need to be extracted that match a credit memo.
     *
     * @param campusCode                     - limit results to a single chart
     * @param invoiceIdentifier              - Invoice Identifier (can be null)
     * @param purchaseOrderIdentifier        - PO Identifier (can be null)
     * @param vendorHeaderGeneratedIdentifier
     *                                       - Vendor Header ID
     * @param vendorDetailAssignedIdentifier - Vendor Detail ID
     * @param currentSqlDateMidnight         current SQL date midnight
     * @return - list of invoices that need to be extracted
     */
    public List<InvoiceDocument> getInvoicesToExtract(String campusCode, Integer invoiceIdentifier, Integer purchaseOrderIdentifier, Integer vendorHeaderGeneratedIdentifier, Integer vendorDetailAssignedIdentifier, Date currentSqlDateMidnight);

    /**
     * Get all the invoices that need to be extracted that match a credit memo.
     *
     * @param campusCode               - limit results to a single chart
     * @param vendor                   - Vendor Header ID, Vendor Detail ID, Country, Zip Code
     * @param onOrBeforeInvoicePayDate only invoices with a pay date on or before this value will be returned in the
     *                                 iterator
     * @return - list of invoices that need to be extracted
     */
    public Collection<InvoiceDocument> getInvoicesToExtractForVendor(String campusCode, VendorGroupingHelper vendor, Date onOrBeforeInvoicePayDate);

    /**
     * Get all the invoices that need to be extracted to PDP.
     *
     * @param onlySpecialPayments - true only include special payments, False - include all
     * @param chartCode           - if not null, limit results to a single chart
     * @return - Collection of invoices
     */
    public List<InvoiceDocument> getInvoicesToExtract(boolean onlySpecialPayments, String chartCode, Date onOrBeforeInvoicePayDate);

    /**
     * Get all the invoices that are marked immediate that need to be extracted to PDP.
     *
     * @param chartCode - chart of accounts code
     * @return - Collection of invoices
     */
    public List<InvoiceDocument> getImmediateInvoicesToExtract(String chartCode);

    /**
     * Get all invoice documents that are eligible for auto-approval. Whether or not a document is eligible for
     * auto-approval is determined according to whether or not the document total is below a pre-determined minimum amount. This
     * amount is derived from the accounts, charts and/or organizations associated with a given document. If no minimum amount can
     * be determined from chart associations a default minimum specified as a system parameter is used to determine the minimum
     * amount threshold.
     *
     * @param todayAtMidnight
     * @return - an Iterator over all invoice documents eligible for automatic approval
     */
    public List<String> getEligibleForAutoApproval(Date todayAtMidnight);

    /**
     * Get a invoice document number by id.
     *
     * @param id - Invoice Id
     * @return - Invoice or null if not found
     */
    public String getDocumentNumberByInvoiceId(Integer id);

    public OleInvoiceDocument getDocumentByInvoiceId(Integer id);


    /**
     * Retrieves a list of document numbers by purchase order id.
     *
     * @param id - purchase order id
     * @return - list of document numbers
     */
    public List<String> getDocumentNumbersByPurchaseOrderId(Integer id);


    /**
     * Retrieves a list of Invoices with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId - header id of the vendor id
     * @param vendorDetailAssignedId  - detail id of the vendor id
     * @param invoiceNumber           - invoice number as entered by AP
     * @return - List of Invoices.
     */
    public List getActiveInvoicesByVendorNumberInvoiceNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId, String invoiceNumber);

    /**
     * Retrieves a list of Invoices with the given vendor id and invoice number.
     *
     * @param vendorHeaderGeneratedId - header id of the vendor id
     * @param vendorDetailAssignedId  - detail id of the vendor id
     * @return - List of Invoices.
     */
    public List getActiveInvoicesByVendorNumber(Integer vendorHeaderGeneratedId, Integer vendorDetailAssignedId);

    /**
     * Retrieves a list of Invoices with the given PO Id, invoice amount, and invoice date.
     *
     * @param poId          - purchase order ID
     * @param invoiceAmount - amount of the invoice as entered by AP
     * @param invoiceDate   - date of the invoice as entered by AP
     * @return - List of Pay Reqs.
     */
    public List getActiveInvoicesByPOIdInvoiceAmountInvoiceDate(Integer poId, KualiDecimal invoiceAmount, Date invoiceDate);

    /**
     * Retrieves a list of potentially active invoices for a purchase order by status code. Active being defined as being
     * enroute and before final. The issue is that a status of vendor_tax_review may not mean that it's in review, but could be in
     * final (as there isn't a final status code for invoice). Workflow status must be checked further after retrieval.
     *
     * @param purchaseOrderId
     * @return
     */
    public List<String> getActiveInvoiceDocumentNumbersForPurchaseOrder(Integer purchaseOrderId);

    /**
     * Get all invoice which are waiting in receiving status queue
     *
     * @return
     */
    public List<OleInvoiceDocument> getInvoiceInReceivingStatus();

}
