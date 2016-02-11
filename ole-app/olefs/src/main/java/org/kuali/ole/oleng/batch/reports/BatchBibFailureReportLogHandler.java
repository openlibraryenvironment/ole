package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.oleng.batch.reports.processors.FailedMarcContentProcessor;
import org.kuali.ole.oleng.batch.reports.processors.FailureSummaryReportProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/10/16.
 */
public class BatchBibFailureReportLogHandler {

    private static BatchBibFailureReportLogHandler batchBibFailureReportLogHandler;
    private static ProducerTemplate sedaProducer;
    private static List<Processor> processors;

    private BatchBibFailureReportLogHandler() {
    }

    public static BatchBibFailureReportLogHandler getInstance() {
        if (null == batchBibFailureReportLogHandler) {
            batchBibFailureReportLogHandler = new BatchBibFailureReportLogHandler();
            String fileName = "BibImportReport" + OleNGConstants.TIMESTAMP_FOR_CAMEL + ".txt";
            String endPoint1 = "seda:batchProcessFailureResponseQ";
            String endPoint2 = "file:"+ ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports/failureReports?fileName=" + fileName + "&fileExist=Append";
            try {
                OleCamelContext.getInstance().addRoutes(endPoint1, endPoint2, getProcessors());
                if (null == sedaProducer) {
                    sedaProducer = OleCamelContext.getInstance().createProducerTemplate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return batchBibFailureReportLogHandler;
    }

    /**
     *
     * The order of adding the processors matters.
     */
    public static List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new FailedMarcContentProcessor());
            processors.add(new FailureSummaryReportProcessor());
        }
        return processors;
    }

    public void logMessage(BatchProcessFailureResponse batchProcessFailureResponse) {
        sedaProducer.sendBody("seda:batchProcessFailureResponseQ", batchProcessFailureResponse);
    }
}
