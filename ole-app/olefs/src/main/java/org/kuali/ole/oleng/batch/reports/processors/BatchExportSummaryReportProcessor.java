package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by rajeshbabuk on 4/27/16.
 */
public class BatchExportSummaryReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(object);
        logMessage(directoryToWrite,  "BatchExport","txt", message, false);
    }
}