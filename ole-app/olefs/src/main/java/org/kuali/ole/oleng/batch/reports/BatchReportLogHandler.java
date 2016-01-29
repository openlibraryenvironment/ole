package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.kuali.ole.ReportLogHandler;

/**
 * Created by pvsubrah on 1/27/16.
 */
public class BatchReportLogHandler  {

    public static BatchReportLogHandler batchReportLogHandler;

    protected BatchReportLogHandler() {

    }

    private static void injectProcessor(Processor batchReportProcessor) {
        ReportLogHandler.getInstance().addProcessor(batchReportProcessor);
    }

    public static BatchReportLogHandler getInstance() {
        if(null == batchReportLogHandler) {
            batchReportLogHandler = new BatchReportLogHandler();
            injectProcessor(new BatchReportProcessor());
            ReportLogHandler.getInstance().setFileNameAndPath(null, getReportFileName());
        }
        return batchReportLogHandler;
    }

    public static String getReportFileName() {
        return "batch-report"+"-${date:now:yyyyMMddHHmmssSSS}"+".txt";
    }

    public void logMessage(Object message) throws Exception {
        ReportLogHandler.getInstance().logMessage(message);
    }

}
