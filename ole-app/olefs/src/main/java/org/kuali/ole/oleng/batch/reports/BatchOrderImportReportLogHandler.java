package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;
import org.kuali.ole.oleng.batch.reports.processors.BatchOrderImportProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/16/16.
 */
public class BatchOrderImportReportLogHandler {

    private static BatchOrderImportReportLogHandler batchOrderImportReportLogHandler;
    private static ProducerTemplate sedaProducer;
    private static List<Processor> processors;

    private BatchOrderImportReportLogHandler() {
    }

    public static BatchOrderImportReportLogHandler getInstance() {
        if(null == batchOrderImportReportLogHandler) {
            batchOrderImportReportLogHandler = new BatchOrderImportReportLogHandler();
            String fileName = "OrderImportReport" + OleNGConstants.TIMESTAMP_FOR_CAMEL + ".txt";
            String endPoint1 = "seda:batchOrderResponseQ";
            String endPoint2 = "file:" + ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports?fileName=" + fileName;
            try {
                OleCamelContext.getInstance().addRoutes(endPoint1, endPoint2, getProcessors());
                if(null == sedaProducer) {
                    sedaProducer = OleCamelContext.getInstance().createProducerTemplate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return batchOrderImportReportLogHandler;
    }

    public static List<Processor> getProcessors() {
        if(null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchOrderImportProcessor());
        }
        return processors;
    }

    public void logMessage(OleNGOrderImportResponse oleNGOrderImportResponse) {
        sedaProducer.sendBody("seda:batchOrderResponseQ", oleNGOrderImportResponse);
    }

}
