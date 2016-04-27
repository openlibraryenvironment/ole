package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.InvoiceFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGInvoiceImportResponse;

import java.util.List;

/**
 * Created by angelind on 3/16/16.
 */
public class InvoiceImportFailureReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = (OleNGInvoiceImportResponse) object;
        List<InvoiceFailureResponse> invoiceFailureResponses = oleNGInvoiceImportResponse.getInvoiceFailureResponses();
        if(CollectionUtils.isNotEmpty(invoiceFailureResponses)) {
            String invoiceFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(invoiceFailureResponses);
            logMessage(directoryToWrite, "Invoice-FailureMessages", "txt", invoiceFailureMessage, false);
        }
    }
}
