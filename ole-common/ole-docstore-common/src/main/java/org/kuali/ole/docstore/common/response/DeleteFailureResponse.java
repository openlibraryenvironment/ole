package org.kuali.ole.docstore.common.response;

import org.marc4j.marc.Record;

/**
 * Created by rajeshbabuk on 4/13/16.
 */
public class DeleteFailureResponse {

    private String failureMessage;
    private String detailedMessage;
    private String failedMatchPointData;
    private String failedBibId;

    public DeleteFailureResponse() {};

    public DeleteFailureResponse(String failureMessage, String detailedMessage, String failedMatchPointData, String failedBibId) {
        this.failureMessage = failureMessage;
        this.detailedMessage = detailedMessage;
        this.failedMatchPointData = failedMatchPointData;
        this.failedBibId = failedBibId;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public String getFailedMatchPointData() {
        return failedMatchPointData;
    }

    public void setFailedMatchPointData(String failedMatchPointData) {
        this.failedMatchPointData = failedMatchPointData;
    }

    public String getFailedBibId() {
        return failedBibId;
    }

    public void setFailedBibId(String failedBibId) {
        this.failedBibId = failedBibId;
    }
}
