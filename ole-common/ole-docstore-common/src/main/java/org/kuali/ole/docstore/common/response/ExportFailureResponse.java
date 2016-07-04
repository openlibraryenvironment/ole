package org.kuali.ole.docstore.common.response;

/**
 * Created by rajeshbabuk on 4/27/16.
 */
public class ExportFailureResponse {

    private String failureMessage;
    private String detailedMessage;
    private String failedBibId;

    public ExportFailureResponse() {}

    public ExportFailureResponse(String failureMessage, String detailedMessage, String failedBibId) {
        this.failureMessage = failureMessage;
        this.detailedMessage = detailedMessage;
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

    public String getFailedBibId() {
        return failedBibId;
    }

    public void setFailedBibId(String failedBibId) {
        this.failedBibId = failedBibId;
    }
}
