package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 4/27/16.
 */
public class BatchExportReportLogHandler extends ReportLogHandler {
    public static BatchExportReportLogHandler batchExportReportLogHandler;

    private BatchExportReportLogHandler() {
    }

    public static BatchExportReportLogHandler getInstance() {
        if (null == batchExportReportLogHandler) {
            batchExportReportLogHandler = new BatchExportReportLogHandler();
        }
        return batchExportReportLogHandler;
    }

    /**
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchExportSummaryReportProcessor());
            processors.add(new BatchExportFailureReportProcessor());
        }
        return processors;
    }
}
