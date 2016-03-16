package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/19/2016.
 */
public class OleNGInvoiceImportResponse {

    private List<InvoiceResponse> invoiceResponses;
    private List<InvoiceFailureResponse> invoiceFailureResponses;

    public List<InvoiceResponse> getInvoiceResponses() {
        if(null == invoiceResponses) {
            invoiceResponses = new ArrayList<>();
        }
        return invoiceResponses;
    }

    public void setInvoiceResponses(List<InvoiceResponse> invoiceResponses) {
        this.invoiceResponses = invoiceResponses;
    }

    public List<InvoiceFailureResponse> getInvoiceFailureResponses() {
        if(null == invoiceFailureResponses) {
            invoiceFailureResponses = new ArrayList<>();
        }
        return invoiceFailureResponses;
    }

    public void setInvoiceFailureResponses(List<InvoiceFailureResponse> invoiceFailureResponses) {
        this.invoiceFailureResponses = invoiceFailureResponses;
    }

    public void addInvoiceFailureResponse(InvoiceFailureResponse invoiceFailureResponse) {
        getInvoiceFailureResponses().add(invoiceFailureResponse);
    }
}
