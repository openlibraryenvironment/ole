package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 4/7/16.
 */
public class BatchDeleteReportLogHandler extends ReportLogHandler {
    public static BatchDeleteReportLogHandler batchDeleteReportLogHandler;

    private BatchDeleteReportLogHandler() {
    }

    public static BatchDeleteReportLogHandler getInstance() {
        if (null == batchDeleteReportLogHandler) {
            batchDeleteReportLogHandler = new BatchDeleteReportLogHandler();
        }
        return batchDeleteReportLogHandler;
    }

    /**
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchDeleteSummaryReportProcessor());
            processors.add(new BatchDeleteSuccessMarcRecordsProcessor());
            processors.add(new BatchDeleteFailureMarcRecordsProcessor());
            processors.add(new BatchDeleteFailureReportProcessor());
        }
        return processors;
    }
}
