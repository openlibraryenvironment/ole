package org.kuali.ole.oleng.batch.reports.processors;

import org.kuali.ole.oleng.batch.reports.ReportLogHandler;

import java.util.ArrayList;
import java.util.List;

public class BatchNoticeReportLogHandler extends ReportLogHandler {

    public static BatchNoticeReportLogHandler batchNoticeReportLogHandler;

    private BatchNoticeReportLogHandler(){}

    public static BatchNoticeReportLogHandler getInstance() {
        if (null == batchNoticeReportLogHandler) {
            batchNoticeReportLogHandler = new BatchNoticeReportLogHandler();
        }
        return batchNoticeReportLogHandler;
    }

    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchDeliverNoticeReportProcessor());
        }
        return processors;
    }
}
