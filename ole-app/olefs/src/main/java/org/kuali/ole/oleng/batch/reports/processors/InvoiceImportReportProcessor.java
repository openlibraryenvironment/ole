package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by SheikS on 2/18/2016.
 */
public class InvoiceImportReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(object);
        logMessage(directoryToWrite, "InvoiceImport","txt", message, false);
    }
}
