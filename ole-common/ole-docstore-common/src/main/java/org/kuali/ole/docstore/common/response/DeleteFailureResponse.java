package org.kuali.ole.docstore.common.response;

import org.marc4j.marc.Record;

/**
 * Created by rajeshbabuk on 4/13/16.
 */
public class DeleteFailureResponse extends ExportFailureResponse {

    private String failedMatchPointData;

    public DeleteFailureResponse() {};

    public DeleteFailureResponse(String failureMessage, String detailedMessage, String failedMatchPointData, String failedBibId) {
        setFailureMessage(failureMessage);
        setDetailedMessage(detailedMessage);
        setFailedBibId(failedBibId);
        this.failedMatchPointData = failedMatchPointData;
    }

    public String getFailedMatchPointData() {
        return failedMatchPointData;
    }

    public void setFailedMatchPointData(String failedMatchPointData) {
        this.failedMatchPointData = failedMatchPointData;
    }
}
