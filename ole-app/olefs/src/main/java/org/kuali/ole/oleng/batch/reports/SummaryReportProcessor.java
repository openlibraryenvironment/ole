package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;

/**
 * Created by angelind on 2/10/16.
 */
public class SummaryReportProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) exchange.getIn().getBody();
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGBibImportResponse);
        exchange.getOut().setBody(message);
    }
}
