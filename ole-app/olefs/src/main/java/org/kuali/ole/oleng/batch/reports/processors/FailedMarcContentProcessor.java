package org.kuali.ole.oleng.batch.reports.processors;

import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;

/**
 * Created by angelind on 2/11/16.
 */
public class FailedMarcContentProcessor extends OleNGReportProcessor {
    public void process(Object object, String directoryToWrite) throws Exception {
        BatchProcessFailureResponse batchProcessFailureResponse = (BatchProcessFailureResponse) object;
        String failedRawMarcContent = batchProcessFailureResponse.getFailedRawMarcContent();
        logMessage(directoryToWrite, batchProcessFailureResponse.getBatchProcessProfileName() + "-FailedInputData","mrc", failedRawMarcContent, true);
    }
}
