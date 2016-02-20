package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.kuali.ole.oleng.batch.reports.processors.FailedMarcContentProcessor;
import org.kuali.ole.oleng.batch.reports.processors.FailureSummaryReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/10/16.
 */
public class BatchBibFailureReportLogHandler extends ReportLogHandler {

    public BatchBibFailureReportLogHandler(String directoryName, String profileName) {
        initiateHander(directoryName,profileName,"FailureReport");
    }

    /**
     *
     * The order of adding the processors matters.
     */
    @Override
    public List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new FailedMarcContentProcessor());
            processors.add(new FailureSummaryReportProcessor());
        }
        return processors;
    }
}
