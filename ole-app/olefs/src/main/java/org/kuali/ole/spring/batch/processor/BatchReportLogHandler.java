package org.kuali.ole.spring.batch.processor;

import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class BatchReportLogHandler {
    private static BatchReportLogHandler batchReportLogHandler;
    private static ProducerTemplate sedaProducer;
    private static List<Processor> processors;

    private BatchReportLogHandler() {
    }

    public static BatchReportLogHandler getInstance() {
        if (null == batchReportLogHandler) {
            batchReportLogHandler = new BatchReportLogHandler();
            String endPoint1 = "seda:batchResponseQ";
            String endPoint2 = "file:batchReports.txt";
            try {
                OleCamelContext.getInstance().addRoutes(endPoint1, endPoint2, getProcessors());
                if (null == sedaProducer) {
                    sedaProducer = OleCamelContext.getInstance().createProducerTemplate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return batchReportLogHandler;
    }

    /**
     *
     * The order of adding the processors matters.
     */
    public static List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new MatchedRecordsReportProcessor());
            //TODO: Add SumaryReportProcessor
            //TODO: Add MatchedRecordsProcessor
            //TODO: Add UnMatchedRecordsProcessor
        }
        return processors;
    }

    public void logMessage(OleNGBibImportResponse oleNGBibImportResponse) {
        sedaProducer.sendBody("seda:batchResponseQ", oleNGBibImportResponse);
    }
}
