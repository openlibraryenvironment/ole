package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.ReportLogHandler;

/**
 * Created by pvsubrah on 1/27/16.
 */
public class BatchReportLogHandler extends ReportLogHandler {
    private BatchReportLogHandler() {
        super();
        setFileName("batch-report.txt");
//        injectProcessor(new BatchReportProcessor());
    }

}
