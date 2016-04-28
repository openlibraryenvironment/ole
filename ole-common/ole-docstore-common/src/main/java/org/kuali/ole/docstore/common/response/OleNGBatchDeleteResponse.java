package org.kuali.ole.docstore.common.response;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.docstore.common.pojo.DeleteRecordDetails;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajeshbabuk on 4/5/16.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OleNGBatchDeleteResponse {

    @JsonProperty("jobDetailId")
    private String jobDetailId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("profileName")
    private String profileName;

    @JsonProperty("totalRecordsCount")
    private int totalNumberOfRecords;

    @JsonProperty("deletedBibsCount")
    private int noOfSuccessRecords;

    @JsonProperty("failedBibsCount")
    private int noOfFailureRecords;

    @JsonProperty("deleteSuccessResponses")
    private List<BatchDeleteSuccessResponse> batchDeleteSuccessResponseList;

    @JsonProperty("deleteFailureResponses")
    private List<BatchDeleteFailureResponse> batchDeleteFailureResponseList;

    @JsonProperty("deleteFailedResponses")
    private List<DeleteFailureResponse> deleteFailureResponseList;

    @JsonIgnore
    private Map<String, DeleteRecordDetails> bibRecordsMap;

    @JsonIgnore
    private List<Record> successMarcRecords;

    @JsonIgnore
    private List<Record> failureMarcRecords;

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

    public List<BatchDeleteSuccessResponse> getBatchDeleteSuccessResponseList() {
        if (null == batchDeleteSuccessResponseList) {
            batchDeleteSuccessResponseList = new ArrayList<>();
        }
        return batchDeleteSuccessResponseList;
    }

    public void setBatchDeleteSuccessResponseList(List<BatchDeleteSuccessResponse> batchDeleteSuccessResponseList) {
        this.batchDeleteSuccessResponseList = batchDeleteSuccessResponseList;
    }

    public List<BatchDeleteFailureResponse> getBatchDeleteFailureResponseList() {
        if (null == batchDeleteFailureResponseList) {
            batchDeleteFailureResponseList = new ArrayList<>();
        }
        return batchDeleteFailureResponseList;
    }

    public void setBatchDeleteFailureResponseList(List<BatchDeleteFailureResponse> batchDeleteFailureResponseList) {
        this.batchDeleteFailureResponseList = batchDeleteFailureResponseList;
    }

    public List<DeleteFailureResponse> getDeleteFailureResponseList() {
        if (null == deleteFailureResponseList) {
            deleteFailureResponseList = new ArrayList<>();
        }
        return deleteFailureResponseList;
    }

    public void setDeleteFailureResponseList(List<DeleteFailureResponse> deleteFailureResponseList) {
        this.deleteFailureResponseList = deleteFailureResponseList;
    }

    public Map<String, DeleteRecordDetails> getBibRecordsMap() {
        if (null == bibRecordsMap) {
            bibRecordsMap = new HashMap<>();
        }
        return bibRecordsMap;
    }

    public void setBibRecordsMap(Map<String, DeleteRecordDetails> bibRecordsMap) {
        this.bibRecordsMap = bibRecordsMap;
    }

    public List<Record> getSuccessMarcRecords() {
        if (null == successMarcRecords) {
            successMarcRecords = new ArrayList<>();
        }
        return successMarcRecords;
    }

    public void setSuccessMarcRecords(List<Record> successMarcRecords) {
        this.successMarcRecords = successMarcRecords;
    }

    public List<Record> getFailureMarcRecords() {
        if (null == failureMarcRecords) {
            failureMarcRecords = new ArrayList<>();
        }
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

    public BatchDeleteSuccessResponse buildBatchDeleteSuccessResponse(String matchPoint, String matchPointValue, String bibId, String message) {
        BatchDeleteSuccessResponse batchDeleteSuccessResponse = new BatchDeleteSuccessResponse();
        batchDeleteSuccessResponse.setMatchPoint(matchPoint);
        batchDeleteSuccessResponse.setMatchPointValue(matchPointValue);
        batchDeleteSuccessResponse.setBibId(bibId);
        batchDeleteSuccessResponse.setMessage(message);
        return batchDeleteSuccessResponse;
    }

    public BatchDeleteFailureResponse buildBatchDeleteFailureResponse(String matchPoint, String matchPointValue, String bibId, String message) {
        BatchDeleteFailureResponse batchDeleteFailureResponse = new BatchDeleteFailureResponse();
        batchDeleteFailureResponse.setMatchPoint(matchPoint);
        batchDeleteFailureResponse.setMatchPointValue(matchPointValue);
        batchDeleteFailureResponse.setBibId(StringUtils.isNotBlank(bibId) ? bibId : "No Match");
        batchDeleteFailureResponse.setMessage(message);
        return batchDeleteFailureResponse;
    }

    public DeleteRecordDetails buildDeleteRecordDetails(Record record, String matchPoint, String matchPointValue, String bibId) {
        DeleteRecordDetails deleteRecordDetails = new DeleteRecordDetails();
        deleteRecordDetails.setRecord(record);
        deleteRecordDetails.setMatchPoint(matchPoint);
        deleteRecordDetails.setMatchPointValue(matchPointValue);
        deleteRecordDetails.setBibId(bibId);
        return deleteRecordDetails;
    }

    public void addSuccessRecord(String matchPoint, String matchPointValue, String bibId, String message) {
        getBatchDeleteSuccessResponseList().add(buildBatchDeleteSuccessResponse(matchPoint, matchPointValue, bibId, message));
    }

    public void addFailureRecord(String matchPoint, String matchPointValue, String bibId, String message) {
        getBatchDeleteFailureResponseList().add(buildBatchDeleteFailureResponse(matchPoint, matchPointValue, bibId, message));
    }

    public void addBibRecordToMap(Record record, String matchPoint, String matchPointValue, String bibId) {
        getBibRecordsMap().put(bibId, buildDeleteRecordDetails(record, matchPoint, matchPointValue, bibId));
    }
}
