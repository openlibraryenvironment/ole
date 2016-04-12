package org.kuali.ole.docstore.common.response;

/**
 * Created by SheikS on 3/16/2016.
 */
public class OleNgBatchResponse {
    private String response;
    private int totalNoOfRecord;
    private int noOfFailureRecord;
    private Object batchResponse;
    private OleNGBibImportResponse bibImportResponse;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getTotalNoOfRecord() {
        return totalNoOfRecord;
    }

    public void setTotalNoOfRecord(int totalNoOfRecord) {
        this.totalNoOfRecord = totalNoOfRecord;
    }

    public int getNoOfFailureRecord() {
        return noOfFailureRecord;
    }

    public void setNoOfFailureRecord(int noOfFailureRecord) {
        this.noOfFailureRecord = noOfFailureRecord;
    }

    public Object getBatchResponse() {
        return batchResponse;
    }

    public void setBatchResponse(Object batchResponse) {
        this.batchResponse = batchResponse;
    }

    public OleNGBibImportResponse getBibImportResponse() {
        return bibImportResponse;
    }

    public void setBibImportResponse(OleNGBibImportResponse bibImportResponse) {
        this.bibImportResponse = bibImportResponse;
    }
}
