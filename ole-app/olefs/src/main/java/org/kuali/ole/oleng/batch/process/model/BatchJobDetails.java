package org.kuali.ole.oleng.batch.process.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by rajeshbabuk on 2/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchJobDetails extends PersistableBusinessObjectBase {

    @JsonProperty(OleNGConstants.JOB_DETAIL_ID)
    private long jobDetailId;

    @JsonProperty(OleNGConstants.JOB_NAME)
    private String jobName;

    @JsonProperty(OleNGConstants.JOB_ID)
    private long jobId;

    @JsonProperty(OleNGConstants.PROFILE_TYPE)
    private String profileType;

    @JsonProperty(OleNGConstants.PROFILE_NAME)
    private String profileName;

    @JsonProperty(OleNGConstants.FILE_NAME)
    private String fileName;

    @JsonProperty(OleNGConstants.PROFILE_ID)
    private long profileId;

    @JsonProperty(OleNGConstants.CREATED_BY)
    private String createdBy;

    @JsonProperty(OleNGConstants.START_TIME)
    private Timestamp startTime;

    @JsonProperty(OleNGConstants.END_TIME)
    private Timestamp endTime;

    @JsonProperty(OleNGConstants.PER_COMPLETED)
    private String perCompleted;

    @JsonProperty(OleNGConstants.TIME_SPENT)
    private String timeSpent;

    @JsonProperty(OleNGConstants.TOTAL_RECORDS)
    private String totalRecords;

    @JsonProperty(OleNGConstants.TOTAL_RECORDS_PROCESSED)
    private String totalRecordsProcessed;

    @JsonProperty(OleNGConstants.TOTAL_FAILURE_RECORDS)
    private String totalFailureRecords;

    @JsonProperty(OleNGConstants.JOB_STATUS)
    private String status;

    @JsonIgnore
    private String outputFileFormat;

    @JsonIgnore
    private int numOfRecordsInFile;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getJobDetailId() {
        return jobDetailId;
    }

    public void setJobDetailId(long jobDetailId) {
        this.jobDetailId = jobDetailId;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getPerCompleted() {
        return perCompleted;
    }

    public void setPerCompleted(String perCompleted) {
        this.perCompleted = perCompleted;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getTotalRecordsProcessed() {
        return totalRecordsProcessed;
    }

    public void setTotalRecordsProcessed(String totalRecordsProcessed) {
        this.totalRecordsProcessed = totalRecordsProcessed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTotalFailureRecords() {
        return totalFailureRecords;
    }

    public void setTotalFailureRecords(String totalFailureRecords) {
        this.totalFailureRecords = totalFailureRecords;
    }

    public String getOutputFileFormat() {
        return outputFileFormat;
    }

    public void setOutputFileFormat(String outputFileFormat) {
        this.outputFileFormat = outputFileFormat;
    }

    public int getNumOfRecordsInFile() {
        return numOfRecordsInFile;
    }

    public void setNumOfRecordsInFile(int numOfRecordsInFile) {
        this.numOfRecordsInFile = numOfRecordsInFile;
    }
}

