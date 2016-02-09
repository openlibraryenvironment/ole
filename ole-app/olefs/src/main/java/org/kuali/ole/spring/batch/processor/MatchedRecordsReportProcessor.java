package org.kuali.ole.spring.batch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class MatchedRecordsReportProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) exchange.getIn().getBody();
        //TODO: Extract the info you need and write to a file.
        exchange.setOut((Message) oleNGBibImportResponse);
    }
}
