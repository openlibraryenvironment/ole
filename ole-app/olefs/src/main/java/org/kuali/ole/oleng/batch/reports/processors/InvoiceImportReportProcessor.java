package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.OleNGInvoiceImportResponse;

/**
 * Created by SheikS on 2/18/2016.
 */
public class InvoiceImportReportProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = (OleNGInvoiceImportResponse) exchange.getIn().getBody();
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGInvoiceImportResponse);
        exchange.getOut().setBody(message);
    }
}
