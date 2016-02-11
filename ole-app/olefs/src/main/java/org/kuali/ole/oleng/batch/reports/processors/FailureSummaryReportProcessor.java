package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;

/**
 * Created by angelind on 2/11/16.
 */
public class FailureSummaryReportProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        BatchProcessFailureResponse batchProcessFailureResponse = (BatchProcessFailureResponse) exchange.getIn().getBody();
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(batchProcessFailureResponse);
        exchange.getOut().setBody(message);
    }
}
