package org.kuali.ole.oleng.batch.process.model;

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

    @JsonProperty(OleNGConstants.PROCESS_ID)
    private long batchProcessId;

    @JsonProperty(OleNGConstants.PROCESS_NAME)
    private String batchProcessName;

    @JsonProperty(OleNGConstants.PROCESS_TYPE)
    private String batchProcessType;

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

    @JsonIgnore
    private BatchProcessProfile batchProcessProfile;

    @JsonIgnore
    private String status;

    @JsonIgnore
    private List<BatchJobDetails> batchJobDetailsList;

    public long getBatchProcessId() {
        return batchProcessId;
    }

    public void setBatchProcessId(long batchProcessId) {
        this.batchProcessId = batchProcessId;
    }

    public String getBatchProcessName() {
        return batchProcessName;
    }

    public void setBatchProcessName(String batchProcessName) {
        this.batchProcessName = batchProcessName;
    }

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public long getBatchProfileId() {
        return batchProfileId;
    }

    public void setBatchProfileId(long batchProfileId) {
        this.batchProfileId = batchProfileId;
    }

    public String getBatchProfileName() {
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
}
