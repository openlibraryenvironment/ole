package org.kuali.ole.oleng.batch.process.model;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by rajeshbabuk on 2/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProcessJob extends PersistableBusinessObjectBase {

    @JsonProperty(OleNGConstants.JOB_ID)
    private long jobId;

    @JsonProperty(OleNGConstants.JOB_NAME)
    private String jobName;

    @JsonProperty(OleNGConstants.PROFILE_TYPE)
    private String profileType;

    @JsonProperty(OleNGConstants.PROFILE_ID)
    private long batchProfileId;

    @JsonProperty(OleNGConstants.PROFILE_NAME)
    private String batchProfileName;

    @JsonProperty(OleNGConstants.JOB_TYPE)
    private String jobType;

    @JsonProperty(OleNGConstants.CRON_EXPRESSION)
    private String cronExpression;

    @JsonProperty(OleNGConstants.CREATED_BY)
    private String createdBy;

    @JsonProperty(OleNGConstants.CREATED_ON)
    private Timestamp createdOn;

    @JsonProperty(OleNGConstants.NEXT_RUN_TIME)
    private Timestamp nextRunTime;

    @JsonProperty(OleNGConstants.SCHEDULE)
    private BatchScheduleJob batchScheduleJob;

    @JsonIgnore
    private BatchProcessProfile batchProcessProfile;

    @JsonIgnore
    private boolean uploadExportInputFile = false;

    @JsonIgnore
    private String status;

    @JsonIgnore
    private List<BatchJobDetails> batchJobDetailsList;

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

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public long getBatchProfileId() {
        return batchProfileId;
    }

    public void setBatchProfileId(long batchProfileId) {
        this.batchProfileId = batchProfileId;
    }

    public String getBatchProfileName() {
        if (null != this.batchProcessProfile) {
            this.batchProfileName = this.batchProcessProfile.getBatchProcessProfileName();
        }
        return batchProfileName;
    }

    public void setBatchProfileName(String batchProfileName) {
        this.batchProfileName = batchProfileName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCronExpression() {
        if (null != this.batchScheduleJob) {
            this.cronExpression = this.batchScheduleJob.getCronExpression();
        }
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Timestamp nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public BatchScheduleJob getBatchScheduleJob() {
        return batchScheduleJob;
    }

    public void setBatchScheduleJob(BatchScheduleJob batchScheduleJob) {
        this.batchScheduleJob = batchScheduleJob;
    }

    public BatchProcessProfile getBatchProcessProfile() {
        return batchProcessProfile;
    }

    public void setBatchProcessProfile(BatchProcessProfile batchProcessProfile) {
        this.batchProcessProfile = batchProcessProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BatchJobDetails> getBatchJobDetailsList() {
        return batchJobDetailsList;
    }

    public void setBatchJobDetailsList(List<BatchJobDetails> batchJobDetailsList) {
        this.batchJobDetailsList = batchJobDetailsList;
    }

    public boolean isUploadExportInputFile() {
        if (null != batchProcessProfile) {
            uploadExportInputFile = batchProcessProfile.isUploadExportInputFile();
        }
        return uploadExportInputFile;
    }

    public void setUploadExportInputFile(boolean uploadExportInputFile) {
        this.uploadExportInputFile = uploadExportInputFile;
    }

    public int getNumOfRecordsInFile() {
        return numOfRecordsInFile;
    }

    public void setNumOfRecordsInFile(int numOfRecordsInFile) {
        this.numOfRecordsInFile = numOfRecordsInFile;
    }

    public String getOutputFileFormat() {
        return outputFileFormat;
    }

    public void setOutputFileFormat(String outputFileFormat) {
        this.outputFileFormat = outputFileFormat;
    }
}
