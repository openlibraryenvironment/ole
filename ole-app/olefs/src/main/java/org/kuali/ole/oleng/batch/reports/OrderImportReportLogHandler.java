package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.kuali.ole.oleng.batch.reports.processors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class OrderImportReportLogHandler extends ReportLogHandler {

    public OrderImportReportLogHandler(String directoryName, String profileName) {
        initiateHander(directoryName,profileName,"OrderImport");
    }

    /**
     *
     * The order of adding the processors matters.
     */
    @Override
    public List<Processor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchOrderImportProcessor());
        }
        return processors;
    }
}
