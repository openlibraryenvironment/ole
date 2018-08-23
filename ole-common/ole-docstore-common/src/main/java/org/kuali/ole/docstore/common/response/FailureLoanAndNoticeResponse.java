package org.kuali.ole.docstore.common.response;

public class FailureLoanAndNoticeResponse {

    private String failedLoanId;
    private String failedNoticeId;

    public String getFailedLoanId() {
        return failedLoanId;
    }

    public void setFailedLoanId(String failedLoanId) {
        this.failedLoanId = failedLoanId;
    }

    public String getFailedNoticeId() {
        return failedNoticeId;
    }

    public void setFailedNoticeId(String failedNoticeId) {
        this.failedNoticeId = failedNoticeId;
    }
}

