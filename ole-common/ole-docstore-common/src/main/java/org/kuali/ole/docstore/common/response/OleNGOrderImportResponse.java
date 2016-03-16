package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/16/16.
 */
public class OleNGOrderImportResponse {

    private List<Integer> requisitionIds;
    private int matchedCount;
    private int unmatchedCount;
    private int multiMatchedCount;
    private String jobDetailId;
    private String jobName;
    private List<OrderResponse> reqOnlyResponses;
    private List<OrderResponse> reqAndPOResponses;
    private List<OrderResponse> noReqNorPOResponses;

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

}
