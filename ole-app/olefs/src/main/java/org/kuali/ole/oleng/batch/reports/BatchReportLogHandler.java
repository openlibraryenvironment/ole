package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.rice.core.api.config.property.ConfigContext;

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
            String fileName = "BibImportReport" + OleNGConstants.TIMESTAMP_FOR_CAMEL + ".txt";
            String endPoint1 = "seda:batchResponseQ";
            String endPoint2 = "file:"+ ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports?fileName=" + fileName;
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
            processors.add(new UnMatchedRecordsReportProcessor());
            processors.add(new MatchedRecordsReportProcessor());
            processors.add(new MultipleMatchedRecordsReportProcessor());
            processors.add(new SummaryReportProcessor());
        }
        return processors;
    }

    public void logMessage(OleNGBibImportResponse oleNGBibImportResponse) {
        sedaProducer.sendBody("seda:batchResponseQ", oleNGBibImportResponse);
    }
}
