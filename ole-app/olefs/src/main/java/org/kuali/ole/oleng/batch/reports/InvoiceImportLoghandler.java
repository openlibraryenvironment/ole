package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.InvoiceImportFailureReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.InvoiceImportReportProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class InvoiceImportLoghandler  extends ReportLogHandler {
    public static InvoiceImportLoghandler invoiceImportLoghandler;

    private InvoiceImportLoghandler() {
    }
    public static InvoiceImportLoghandler getInstance(){
        if(null == invoiceImportLoghandler) {
            invoiceImportLoghandler = new InvoiceImportLoghandler();
        }
        return invoiceImportLoghandler;
    }
    /**
     *
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new InvoiceImportFailureReportProcessor());
            processors.add(new InvoiceImportReportProcessor());
        }
        return processors;
    }
}
