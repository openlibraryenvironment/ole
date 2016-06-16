package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class BibImportReportLogHandler extends ReportLogHandler {
    public static BibImportReportLogHandler bibImportReportLogHandler;

    private BibImportReportLogHandler() {
    }
    public static BibImportReportLogHandler getInstance(){
        if(null == bibImportReportLogHandler) {
            bibImportReportLogHandler = new BibImportReportLogHandler();
        }
        return bibImportReportLogHandler;
    }
    /**
     *
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new UnMatchedRecordsReportProcessor());
            processors.add(new MatchedRecordsReportProcessor());
            processors.add(new MultipleMatchedRecordsReportProcessor());
            processors.add(new BibImportFailureReportProcessor());
            processors.add(new BibImportValidationErrorReportProcessor());
            processors.add(new SummaryReportProcessor());
        }
        return processors;
    }

}
