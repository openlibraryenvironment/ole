package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.BatchOrderImportReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OrderImportFailureReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class OrderImportReportLogHandler  extends ReportLogHandler {
    public static OrderImportReportLogHandler orderImportReportLogHandler;

    private OrderImportReportLogHandler() {
    }
    public static OrderImportReportLogHandler getInstance(){
        if(null == orderImportReportLogHandler) {
            orderImportReportLogHandler = new OrderImportReportLogHandler();
        }
        return orderImportReportLogHandler;
    }

    /**
     *
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new OrderImportFailureReportProcessor());
            processors.add(new BatchOrderImportReportProcessor());
        }
        return processors;
    }
}
