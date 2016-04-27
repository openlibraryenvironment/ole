package org.kuali.ole.docstore.common.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.docstore.common.pojo.RecordDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 2/16/16.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OleNGOrderImportResponse {

    @JsonProperty("requisitionIds")
    private List<Integer> requisitionIds;

    @JsonProperty("matchedCount")
    private int matchedCount;

    @JsonProperty("unmatchedCount")
    private int unmatchedCount;

    @JsonProperty("multiMatchedCount")
    private int multiMatchedCount;

    @JsonProperty("jobDetailId")
    private String jobDetailId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("reqOnlyResponses")
    private List<OrderResponse> reqOnlyResponses;

    @JsonProperty("reqAndPOResponses")
    private List<OrderResponse> reqAndPOResponses;

    @JsonProperty("noReqNorPOResponses")
    private List<OrderResponse> noReqNorPOResponses;

    @JsonProperty("orderFailureResponses")
    private List<OrderFailureResponse> orderFailureResponses;

    @JsonIgnore
    private Map<Integer, RecordDetails> recordsMap;

    public List<Integer> getRequisitionIds() {
        return requisitionIds;
    }

    public void setRequisitionIds(List<Integer> requisitionIds) {
        this.requisitionIds = requisitionIds;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public int getUnmatchedCount() {
        return unmatchedCount;
    }

    public void setUnmatchedCount(int unmatchedCount) {
        this.unmatchedCount = unmatchedCount;
    }

    public int getMultiMatchedCount() {
        return multiMatchedCount;
    }

    public void setMultiMatchedCount(int multiMatchedCount) {
        this.multiMatchedCount = multiMatchedCount;
    }

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

    public List<OrderResponse> getReqOnlyResponses() {
        if(null == reqOnlyResponses) {
            reqOnlyResponses = new ArrayList<>();
        }
        return reqOnlyResponses;
    }

    public void setReqOnlyResponses(List<OrderResponse> reqOnlyResponses) {
        this.reqOnlyResponses = reqOnlyResponses;
    }

    public List<OrderResponse> getReqAndPOResponses() {
        if(null == reqAndPOResponses) {
            reqAndPOResponses = new ArrayList<>();
        }
        return reqAndPOResponses;
    }

    public void setReqAndPOResponses(List<OrderResponse> reqAndPOResponses) {
        this.reqAndPOResponses = reqAndPOResponses;
    }

    public List<OrderResponse> getNoReqNorPOResponses() {
        if(null == noReqNorPOResponses) {
            noReqNorPOResponses = new ArrayList<>();
        }
        return noReqNorPOResponses;
    }

    public void setNoReqNorPOResponses(List<OrderResponse> noReqNorPOResponses) {
        this.noReqNorPOResponses = noReqNorPOResponses;
    }

    public void addReqOnlyResponse(OrderResponse orderResponse) {
        getReqOnlyResponses().add(orderResponse);
    }

    public void addReqAndPOResponse(OrderResponse orderResponse) {
        getReqAndPOResponses().add(orderResponse);
    }

    public void addNoReqNorPOResponse(OrderResponse orderResponse) {
        getNoReqNorPOResponses().add(orderResponse);
    }

    public List<OrderFailureResponse> getOrderFailureResponses() {
        if(null == orderFailureResponses) {
            orderFailureResponses = new ArrayList<>();
        }
        return orderFailureResponses;
    }

    public void setOrderFailureResponses(List<OrderFailureResponse> orderFailureResponses) {
        this.orderFailureResponses = orderFailureResponses;
    }

    public void addOrderFailureResponse(OrderFailureResponse orderFailureResponse) {
        getOrderFailureResponses().add(orderFailureResponse);
    }

    public Map<Integer, RecordDetails> getRecordsMap() {
        if(null == recordsMap) {
            recordsMap = new HashMap<>();
        }
        return recordsMap;
    }

    public void setRecordsMap(Map<Integer, RecordDetails> recordsMap) {
        this.recordsMap = recordsMap;
    }
}
