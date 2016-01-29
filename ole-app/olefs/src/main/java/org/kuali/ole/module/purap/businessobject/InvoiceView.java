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
package org.kuali.ole.module.purap.businessobject;

import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.identity.PurapKimAttributes;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.core.web.format.DateFormatter;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Invoice View Business Object.
 */
public class InvoiceView extends AbstractRelatedView {

    private String invoiceNumber;
    private Integer purchaseOrderIdentifier;
    private boolean paymentHoldIndicator;
    private boolean invoiceCancelIndicator;
    private String vendorName;
    private String vendorCustomerNumber;
    private Date invoicePayDate;
    private Timestamp paymentExtractedTimestamp;
    private Timestamp paymentPaidTimestamp;
    private Integer accountsPayablePurchasingDocumentLinkIdentifier;
    private Integer invoiceIdentifier;
    protected Date invoiceDate;
    private List<PaymentRequestView> paymentRequestViews = new ArrayList<PaymentRequestView>();

    // REFERENCE OBJECTS
    private FinancialSystemDocumentHeader documentHeader;

    public Integer getAccountsPayablePurchasingDocumentLinkIdentifier() {
        return accountsPayablePurchasingDocumentLinkIdentifier;
    }

    public void setAccountsPayablePurchasingDocumentLinkIdentifier(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        this.accountsPayablePurchasingDocumentLinkIdentifier = accountsPayablePurchasingDocumentLinkIdentifier;
    }

    public Integer getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    public void setInvoiceIdentifier(Integer invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
    }

    // GETTERS & SETTERS
    public Object getTotalAmount() {
        return (new CurrencyFormatter()).format(documentHeader.getFinancialDocumentTotalAmount());
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Timestamp getPaymentExtractedTimestamp() {
        return paymentExtractedTimestamp;
    }

    public void setPaymentExtractedTimestamp(Timestamp paymentExtractedTimestamp) {
        this.paymentExtractedTimestamp = paymentExtractedTimestamp;
    }

    public boolean isPaymentHoldIndicator() {
        return paymentHoldIndicator;
    }

    public void setPaymentHoldIndicator(boolean paymentHoldIndicator) {
        this.paymentHoldIndicator = paymentHoldIndicator;
    }

    public Timestamp getPaymentPaidTimestamp() {
        return paymentPaidTimestamp;
    }

    public void setPaymentPaidTimestamp(Timestamp paymentPaidTimestamp) {
        this.paymentPaidTimestamp = paymentPaidTimestamp;
    }

    public boolean isInvoiceCancelIndicator() {
        return invoiceCancelIndicator;
    }

    public void setInvoiceCancelIndicator(boolean invoiceCancelIndicator) {
        this.invoiceCancelIndicator = invoiceCancelIndicator;
    }

    public Object getInvoicePayDate() {
        return new DateFormatter().format(invoicePayDate);
    }

    public void setInvoicePayDate(Date invoicePayDate) {
        this.invoicePayDate = invoicePayDate;
    }

    public Integer getPurchaseOrderIdentifier() {
        return purchaseOrderIdentifier;
    }

    public void setPurchaseOrderIdentifier(Integer purchaseOrderIdentifier) {
        this.purchaseOrderIdentifier = purchaseOrderIdentifier;
    }

    public String getVendorCustomerNumber() {
        return vendorCustomerNumber;
    }

    public void setVendorCustomerNumber(String vendorCustomerNumber) {
        this.vendorCustomerNumber = vendorCustomerNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * @return workflow document type for the InvoiceDocument
     */
    public String getDocumentType() {
        return OLEConstants.FinancialDocumentTypeCodes.INVOICE;
    }

    /**
     * The next three methods are overridden but shouldnt be! If they arent overridden, they dont show up in the tag, not sure why
     * at this point! (AAP)
     */
    @Override
    public Integer getPurapDocumentIdentifier() {
        return super.getPurapDocumentIdentifier();
    }

    @Override
    public String getDocumentIdentifierString() {

        if(getInvoiceNumber() != null && getInvoiceNumber().trim().length() > 0)   {
            return getInvoiceNumber();
        }
        else {
            return getDocumentNumber();
        }


        // return super.getDocumentIdentifierString();
    }

    /**
     * @see AbstractRelatedView#getNotes()
     */
    @Override
    public List<Note> getNotes() {
        return super.getNotes();
    }

    /**
     * @see AbstractRelatedView#getUrl()
     */
    @Override
    public String getUrl() {
        return super.getUrl();
    }

    /**
     * @see AbstractRelatedView#getDocumentTypeName()
     */
    @Override
    public String getDocumentTypeName() {
        return OLEConstants.FinancialDocumentTypeCodes.INVOICE;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<PaymentRequestView> getPaymentRequestViews() {
        if (this.getAccountsPayablePurchasingDocumentLinkIdentifier() != null) {
            Map criteria = new HashMap();
            criteria.put(OLEConstants.PUR_DOC_IDENTIFIER,this.getPurapDocumentIdentifier());
            List<OleInvoiceItem> invoiceItems = (List<OleInvoiceItem>)SpringContext.getBean(BusinessObjectService.class).findMatching(OleInvoiceItem.class,criteria);
            List<PaymentRequestView> relatedList = new ArrayList<PaymentRequestView>();
            if (invoiceItems.size() > 0) {
                for (OleInvoiceItem item : invoiceItems) {
                    Map paymentCriteria = new HashMap();
                    paymentCriteria.put(PurapKimAttributes.ACCOUNTS_PAYABLE_PURCHASING_DOCUMENT_LINK_IDENTIFIER, item.getAccountsPayablePurchasingDocumentLinkIdentifier());
                    paymentCriteria.put(OLEConstants.InvoiceDocument.INVOICE_IDENTIFIER,this.getInvoiceIdentifier());
                    List<PaymentRequestView> paymentViewList = (List<PaymentRequestView>)SpringContext.getBean(BusinessObjectService.class).findMatching(PaymentRequestView.class,paymentCriteria);
                    relatedList.addAll(paymentViewList);
                }
            }
            this.setPaymentRequestViews(relatedList);
        }
        return paymentRequestViews;
    }

    public void setPaymentRequestViews(List<PaymentRequestView> paymentRequestViews) {
        this.paymentRequestViews = paymentRequestViews;
    }

    public FinancialSystemDocumentHeader getDocumentHeader() {
        return documentHeader;
    }

    public void setDocumentHeader(FinancialSystemDocumentHeader documentHeader) {
        this.documentHeader = documentHeader;
    }


}
