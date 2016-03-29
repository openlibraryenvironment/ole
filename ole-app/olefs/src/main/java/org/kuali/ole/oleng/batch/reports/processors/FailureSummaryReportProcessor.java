package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by angelind on 2/11/16.
 */
public class FailureSummaryReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(object);
        logMessage(directoryToWrite, "FailureMessages","txt", message);
    }
}
