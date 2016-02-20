package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.kuali.ole.oleng.batch.reports.processors.MatchedRecordsReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.MultipleMatchedRecordsReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.SummaryReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.UnMatchedRecordsReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class BibImportReportLogHandler extends ReportLogHandler {

    public BibImportReportLogHandler(String directoryName, String profileName) {
        initiateHander(directoryName,profileName,"BibImport");
    }

    /**
     *
     * The order of adding the processors matters.
     */
    @Override
    public List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new UnMatchedRecordsReportProcessor());
            processors.add(new MatchedRecordsReportProcessor());
            processors.add(new MultipleMatchedRecordsReportProcessor());
            processors.add(new SummaryReportProcessor());
        }
        return processors;
    }
}
