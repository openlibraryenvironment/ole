package org.kuali.ole.docstore.common.response;

public class FailureRequestAndNoticeResponse {

    private String failedRequestId;
    private String failedNoticeId;

    public String getFailedRequestId() {
        return failedRequestId;
    }

    public void setFailedRequestId(String failedRequestId) {
        this.failedRequestId = failedRequestId;
    }

    public String getFailedNoticeId() {
        return failedNoticeId;
    }

    public void setFailedNoticeId(String failedNoticeId) {
        this.failedNoticeId = failedNoticeId;
    }
}
