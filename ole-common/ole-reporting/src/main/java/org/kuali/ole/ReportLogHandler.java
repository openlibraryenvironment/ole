package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

/**
 * Created by pvsubrah on 1/10/15.
 */
public class ReportLogHandler {

    private ProducerTemplate sedaProducer;
    static ReportLogHandler reportLogHandler;

    private ReportLogHandler() {
    }

    public static ReportLogHandler getInstance() {
        if(null == reportLogHandler){
            reportLogHandler = new ReportLogHandler();
        }
        return reportLogHandler;
    }

    public void logMessage(Object message) throws Exception {
        CamelContext context = OleCamelContext.getInstance().getContext();
        if (null == sedaProducer) {
            sedaProducer = context.createProducerTemplate();
        }
        sedaProducer.sendBody("seda:messages", message);
    }

}
