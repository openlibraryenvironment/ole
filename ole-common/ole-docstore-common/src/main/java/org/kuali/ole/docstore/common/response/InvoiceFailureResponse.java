package org.kuali.ole.docstore.common.response;

/**
 * Created by angelind on 3/15/16.
 */
public class InvoiceFailureResponse extends FailureResponse {

    private String invoiceNumber;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
