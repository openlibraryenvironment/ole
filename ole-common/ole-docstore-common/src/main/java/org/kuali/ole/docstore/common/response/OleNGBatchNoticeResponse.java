package org.kuali.ole.docstore.common.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;


/**
 * Created by govindarajank on 4/20/16.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OleNGBatchNoticeResponse {

    @JsonProperty("jobDetailId")
    private String jobDetailId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("profileName")
    private String profileName;

    @JsonProperty("noticeType")
    private String noticeType;

    @JsonProperty("totalNoticeCount")
    private int totalNoticeCount;

    @JsonProperty("failedNoticeCount")
    private int noOfFailureNotice;

    @JsonProperty("successNoticeCount")
    private int noOfSuccessNotice;

    @JsonProperty("reportDirectoryName")
    private String reportDirectoryName;

    @JsonProperty("failureLoanAndNoticeResponses")
    private List<FailureLoanAndNoticeResponse> failureLoanAndNoticeResponses;

    @JsonProperty("failureRequestAndNoticeResponses")
    private List<FailureRequestAndNoticeResponse> failureRequestAndNoticeResponses;


    public String getJobDetailId() {
        return jobDetailId;
    }

    public void setJobDetailId(String jobDetailId) {
        this.jobDetailId = jobDetailId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public int getTotalNoticeCount() {
        return totalNoticeCount;
    }

    public void setTotalNoticeCount(int totalNoticeCount) {
        this.totalNoticeCount = totalNoticeCount;
    }

    public int getNoOfSuccessNotice() {
        return noOfSuccessNotice;
    }

    public void setNoOfSuccessNotice(int noOfSuccessNotice) {
        this.noOfSuccessNotice = noOfSuccessNotice;
    }

    public int getNoOfFailureNotice() {
        return noOfFailureNotice;
    }

    public void setNoOfFailureNotice(int noOfFailureNotice) {
        this.noOfFailureNotice = noOfFailureNotice;
    }

    public String getReportDirectoryName() {
        return reportDirectoryName;
    }

    public void setReportDirectoryName(String reportDirectoryName) {
        this.reportDirectoryName = reportDirectoryName;
    }

    public List<FailureLoanAndNoticeResponse> getFailureLoanAndNoticeResponses() {
        return failureLoanAndNoticeResponses;
    }

    public void setFailureLoanAndNoticeResponses(List<FailureLoanAndNoticeResponse> failureLoanAndNoticeResponses) {
        this.failureLoanAndNoticeResponses = failureLoanAndNoticeResponses;
    }

    public List<FailureRequestAndNoticeResponse> getFailureRequestAndNoticeResponses() {
        return failureRequestAndNoticeResponses;
    }

    public void setFailureRequestAndNoticeResponses(List<FailureRequestAndNoticeResponse> failureRequestAndNoticeResponses) {
        this.failureRequestAndNoticeResponses = failureRequestAndNoticeResponses;
    }
}
