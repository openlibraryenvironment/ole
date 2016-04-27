package org.kuali.ole.docstore.common.response;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 1/11/2016.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OleNGBibImportResponse {

    @JsonProperty("bibImportProfileName")
    private String bibImportProfileName;
    @JsonProperty("jobDetailId")
    private String jobDetailId;
    @JsonProperty("jobName")
    private String jobName;
    @JsonProperty("matchedBibsCount")
    private int matchedBibsCount;
    @JsonProperty("unmatchedBibsCount")
    private int unmatchedBibsCount;
    @JsonProperty("multipleMatchedBibsCount")
    private int multipleMatchedBibsCount;

    @JsonProperty("matchedHoldingsCount")
    private int matchedHoldingsCount;
    @JsonProperty("unmatchedHoldingsCount")
    private int unmatchedHoldingsCount;
    @JsonProperty("multipleMatchedHoldingsCount")
    private int multipleMatchedHoldingsCount;

    @JsonProperty("matchedItemsCount")
    private int matchedItemsCount;
    @JsonProperty("unmatchedItemsCount")
    private int unmatchedItemsCount;
    @JsonProperty("multipleMatchedItemsCount")
    private int multipleMatchedItemsCount;

    @JsonProperty("matchedEHoldingsCount")
    private int matchedEHoldingsCount;
    @JsonProperty("unmatchedEHoldingsCount")
    private int unmatchedEHoldingsCount;
    @JsonProperty("multipleMatchedEHoldingsCount")
    private int multipleMatchedEHoldingsCount;

    @JsonProperty("failureResponses")
    private List<BibFailureResponse> failureResponses;

    @JsonIgnore
    private List<Record> matchedRecords = new ArrayList<>();
    @JsonIgnore
    private List<Record> unmatchedRecords = new ArrayList<>();
    @JsonIgnore
    private List<Record> multipleMatchedRecords = new ArrayList<>();
    @JsonIgnore
    private String directoryName;
    @JsonIgnore
    private Map<Integer, RecordDetails> recordsMap;

    @JsonProperty("bibResponses")
    private List<BibResponse> bibResponses;

    public List<BibResponse> getBibResponses() {
        if(null == bibResponses) {
            bibResponses = new ArrayList<>();
        }
        return bibResponses;
    }

    public void setBibResponses(List<BibResponse> bibResponses) {
        this.bibResponses = bibResponses;
    }

    public int getMatchedBibsCount() {
        return matchedBibsCount;
    }

    public void setMatchedBibsCount(int matchedBibsCount) {
        this.matchedBibsCount = matchedBibsCount;
    }

    public int getUnmatchedBibsCount() {
        return unmatchedBibsCount;
    }

    public void setUnmatchedBibsCount(int unmatchedBibsCount) {
        this.unmatchedBibsCount = unmatchedBibsCount;
    }

    public int getMultipleMatchedBibsCount() {
        return multipleMatchedBibsCount;
    }

    public void setMultipleMatchedBibsCount(int multipleMatchedBibsCount) {
        this.multipleMatchedBibsCount = multipleMatchedBibsCount;
    }

    public int getMatchedHoldingsCount() {
        return matchedHoldingsCount;
    }

    public void setMatchedHoldingsCount(int matchedHoldingsCount) {
        this.matchedHoldingsCount = matchedHoldingsCount;
    }

    public int getUnmatchedHoldingsCount() {
        return unmatchedHoldingsCount;
    }

    public void setUnmatchedHoldingsCount(int unmatchedHoldingsCount) {
        this.unmatchedHoldingsCount = unmatchedHoldingsCount;
    }

    public int getMultipleMatchedHoldingsCount() {
        return multipleMatchedHoldingsCount;
    }

    public void setMultipleMatchedHoldingsCount(int multipleMatchedHoldingsCount) {
        this.multipleMatchedHoldingsCount = multipleMatchedHoldingsCount;
    }

    public int getMatchedItemsCount() {
        return matchedItemsCount;
    }

    public void setMatchedItemsCount(int matchedItemsCount) {
        this.matchedItemsCount = matchedItemsCount;
    }

    public int getUnmatchedItemsCount() {
        return unmatchedItemsCount;
    }

    public void setUnmatchedItemsCount(int unmatchedItemsCount) {
        this.unmatchedItemsCount = unmatchedItemsCount;
    }

    public int getMultipleMatchedItemsCount() {
        return multipleMatchedItemsCount;
    }

    public void setMultipleMatchedItemsCount(int multipleMatchedItemsCount) {
        this.multipleMatchedItemsCount = multipleMatchedItemsCount;
    }

    public int getMatchedEHoldingsCount() {
        return matchedEHoldingsCount;
    }

    public void setMatchedEHoldingsCount(int matchedEHoldingsCount) {
        this.matchedEHoldingsCount = matchedEHoldingsCount;
    }

    public int getUnmatchedEHoldingsCount() {
        return unmatchedEHoldingsCount;
    }

    public void setUnmatchedEHoldingsCount(int unmatchedEHoldingsCount) {
        this.unmatchedEHoldingsCount = unmatchedEHoldingsCount;
    }

    public int getMultipleMatchedEHoldingsCount() {
        return multipleMatchedEHoldingsCount;
    }

    public void setMultipleMatchedEHoldingsCount(int multipleMatchedEHoldingsCount) {
        this.multipleMatchedEHoldingsCount = multipleMatchedEHoldingsCount;
    }

    public List<Record> getMatchedRecords() {
        return matchedRecords;
    }

    public void setMatchedRecords(List<Record> matchedRecords) {
        this.matchedRecords = matchedRecords;
    }

    public List<Record> getUnmatchedRecords() {
        return unmatchedRecords;
    }

    public void setUnmatchedRecords(List<Record> unmatchedRecords) {
        this.unmatchedRecords = unmatchedRecords;
    }

    public List<Record> getMultipleMatchedRecords() {
        return multipleMatchedRecords;
    }

    public void setMultipleMatchedRecords(List<Record> multipleMatchedRecords) {
        this.multipleMatchedRecords = multipleMatchedRecords;
    }

    public String getBibImportProfileName() {
        return bibImportProfileName;
    }

    public void setBibImportProfileName(String bibImportProfileName) {
        this.bibImportProfileName = bibImportProfileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public List<BibFailureResponse> getFailureResponses() {
        if(null == failureResponses) {
            failureResponses = new ArrayList<>();
        }
        return failureResponses;
    }

    public void setFailureResponses(List<BibFailureResponse> failureResponses) {
        this.failureResponses = failureResponses;
    }

    public void addFailureResponse(BibFailureResponse failureResponse) {
        getFailureResponses().add(failureResponse);
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
}
