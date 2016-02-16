package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;

/**
 * Created by angelind on 2/16/16.
 */
public class BatchOrderImportProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGOrderImportResponse oleNGOrderImportResponse = (OleNGOrderImportResponse) exchange.getIn().getBody();
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGOrderImportResponse);
        exchange.getOut().setBody(message);
    }
}
