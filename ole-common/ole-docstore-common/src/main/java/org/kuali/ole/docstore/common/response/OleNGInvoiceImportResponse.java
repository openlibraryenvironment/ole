package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/19/2016.
 */
public class OleNGInvoiceImportResponse {
    private List<InvoiceResponse> invoiceResponses;

    public List<InvoiceResponse> getInvoiceResponses() {
        if(null == invoiceResponses) {
            invoiceResponses = new ArrayList<>();
        }
        return invoiceResponses;
    }

    public void setInvoiceResponses(List<InvoiceResponse> invoiceResponses) {
        this.invoiceResponses = invoiceResponses;
    }
}
