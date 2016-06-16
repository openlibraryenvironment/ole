package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by rajeshbabuk on 4/12/16.
 */
public class BatchDeleteSummaryReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(object);
        logMessage(directoryToWrite,  "BatchDelete","txt", message, false);
    }
}
