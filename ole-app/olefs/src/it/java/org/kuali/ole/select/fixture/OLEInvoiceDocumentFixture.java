package org.kuali.ole.select.fixture;

import org.kuali.ole.DocumentTestUtils;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.select.businessobject.OlePaymentMethod;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.DocumentService;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 5/29/14
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This fixture provides necessary values to create an Invoice document.
 */

public enum OLEInvoiceDocumentFixture {

    BASIC_ONLY_REQ_FIELDS_FOR_INVOICE (
         "1000-0", //vendorId
        "ALEPH-BET BOOKS", //vendorName
         "30", //invoiceVendorAmount
         "1"//paymentMethodIdentifier
    ),;

    public final String vendorId;
    public final String vendorName;
    public final String invoiceVendorAmount;
    public final String paymentMethodIdentifier;

     private OLEInvoiceDocumentFixture(String vendorId, String vendorName, String invoiceVendorAmount, String paymentMethodIdentifier) {
         this.vendorId = vendorId;
         this.vendorName = vendorName;
         this.invoiceVendorAmount = invoiceVendorAmount;
         this.paymentMethodIdentifier = paymentMethodIdentifier;
    }

    public OleInvoiceDocument createInvoiceDocument(String poId) {
        OleInvoiceDocument oleInvoiceDocument = null;
        try {
            oleInvoiceDocument = DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), OleInvoiceDocument.class);
        }
        catch (WorkflowException e) {
            throw new RuntimeException("Document creation failed.");
        }
        oleInvoiceDocument.setVendorId(vendorId);
        oleInvoiceDocument.setVendorName(vendorName);
        oleInvoiceDocument.setInvoiceVendorAmount(invoiceVendorAmount);
        oleInvoiceDocument.setInvoiceAmount(invoiceVendorAmount);
        oleInvoiceDocument.setAccountsPayableProcessorIdentifier("khuntley");
        oleInvoiceDocument.setProcessingCampusCode("BL");
        oleInvoiceDocument.setPaymentMethodIdentifier(paymentMethodIdentifier);
        oleInvoiceDocument.setPoId(poId);
        java.util.Date date = new java.util.Date();
        oleInvoiceDocument.setInvoiceDate(new java.sql.Date(date.getTime()));
        return oleInvoiceDocument;
    }
}
