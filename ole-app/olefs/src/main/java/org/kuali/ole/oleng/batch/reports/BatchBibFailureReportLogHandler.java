package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.FailedMarcContentProcessor;
import org.kuali.ole.oleng.batch.reports.processors.FailureSummaryReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/10/16.
 */
public class BatchBibFailureReportLogHandler extends ReportLogHandler {
    public static BatchBibFailureReportLogHandler batchBibFailureReportLogHandler;

    private BatchBibFailureReportLogHandler() {
    }
    public static BatchBibFailureReportLogHandler getInstance(){
        if(null == batchBibFailureReportLogHandler) {
            batchBibFailureReportLogHandler = new BatchBibFailureReportLogHandler();
        }
        return batchBibFailureReportLogHandler;
    }

    /**
     *
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new FailedMarcContentProcessor());
            processors.add(new FailureSummaryReportProcessor());
        }
        return processors;
    }
}
