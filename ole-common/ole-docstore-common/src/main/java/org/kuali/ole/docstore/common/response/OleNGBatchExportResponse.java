package org.kuali.ole.docstore.common.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by rajeshbabuk on 4/20/16.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OleNGBatchExportResponse {

    @JsonProperty("jobDetailId")
    private String jobDetailId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("profileName")
    private String profileName;

    @JsonProperty("totalRecordsCount")
    private int totalNumberOfRecords;

    @JsonProperty("successBibsCount")
    private int noOfSuccessRecords;

    @JsonProperty("failedBibsCount")
    private int noOfFailureRecords;

    @JsonProperty("exportSuccessResponses")
    private List<BatchExportSuccessResponse> batchExportSuccessResponseList;

    @JsonProperty("exportFailureResponses")
    private List<BatchExportFailureResponse> batchExportFailureResponseList;

    @JsonIgnore
    private List<ExportFailureResponse> exportFailureResponses;
    
    @JsonIgnore
    private List<Record> failureMarcRecords;

    private SortedSet<String> deletedBibIds;

    @JsonIgnore
    private String directoryName;

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

    public int getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    public void setTotalNumberOfRecords(int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }

    public int getNoOfSuccessRecords() {
        return noOfSuccessRecords;
    }

    public void setNoOfSuccessRecords(int noOfSuccessRecords) {
        this.noOfSuccessRecords = noOfSuccessRecords;
    }

    public int getNoOfFailureRecords() {
        return noOfFailureRecords;
    }

    public void setNoOfFailureRecords(int noOfFailureRecords) {
        this.noOfFailureRecords = noOfFailureRecords;
    }

    public List<BatchExportSuccessResponse> getBatchExportSuccessResponseList() {
        if (null == batchExportSuccessResponseList) {
            batchExportSuccessResponseList = new ArrayList<>();
        }
        return batchExportSuccessResponseList;
    }

    public void setBatchExportSuccessResponseList(List<BatchExportSuccessResponse> batchExportSuccessResponseList) {
        this.batchExportSuccessResponseList = batchExportSuccessResponseList;
    }

    public List<BatchExportFailureResponse> getBatchExportFailureResponseList() {
        if (null == batchExportFailureResponseList) {
            batchExportFailureResponseList = new ArrayList<>();
        }
        return batchExportFailureResponseList;
    }

    public void setBatchExportFailureResponseList(List<BatchExportFailureResponse> batchExportFailureResponseList) {
        this.batchExportFailureResponseList = batchExportFailureResponseList;
    }

    public List<ExportFailureResponse> getExportFailureResponses() {
        if (null == exportFailureResponses) {
            exportFailureResponses = new ArrayList<>();
        }
        return exportFailureResponses;
    }

    public void setExportFailureResponses(List<ExportFailureResponse> exportFailureResponses) {
        this.exportFailureResponses = exportFailureResponses;
    }

    public List<Record> getFailureMarcRecords() {
        return failureMarcRecords;
    }

    public void setFailureMarcRecords(List<Record> failureMarcRecords) {
        this.failureMarcRecords = failureMarcRecords;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public SortedSet<String> getDeletedBibIds() {
        if (null == deletedBibIds) {
            deletedBibIds = new TreeSet<>();
        }
        return deletedBibIds;
    }

    public void setDeletedBibIds(SortedSet<String> deletedBibIds) {
        this.deletedBibIds = deletedBibIds;
    }

    private BatchExportSuccessResponse buildBatchExportSuccessResponse(String bibLocalId, String bibId, String message) {
        BatchExportSuccessResponse batchExportSuccessResponse = new BatchExportSuccessResponse();
        batchExportSuccessResponse.setBibLocalId(bibLocalId);
        batchExportSuccessResponse.setBibId(bibId);
        batchExportSuccessResponse.setMessage(message);
        return batchExportSuccessResponse;
    }

    private BatchExportFailureResponse buildBatchExportFailureResponse(String bibLocalId, String bibId, String message) {
        BatchExportFailureResponse batchExportFailureResponse = new BatchExportFailureResponse();
        batchExportFailureResponse.setBibLocalId(bibLocalId);
        batchExportFailureResponse.setBibId(bibId);
        batchExportFailureResponse.setMessage(message);
        return batchExportFailureResponse;
    }

    public void addSuccessRecord(String bibLocalId, String bibId, String message) {
        getBatchExportSuccessResponseList().add(buildBatchExportSuccessResponse(bibLocalId, bibId, message));
    }

    public void addFailureRecord(String bibLocalId, String bibId, String message) {
        getBatchExportFailureResponseList().add(buildBatchExportFailureResponse(bibLocalId, bibId, message));
    }

    public void addNoOfSuccessRecords(int count) {
        setNoOfSuccessRecords(getNoOfSuccessRecords() + count);
    }

    public void addNoOfFailureRecords(int count) {
        setNoOfFailureRecords(getNoOfFailureRecords() + count);
    }

}
