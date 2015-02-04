package org.kuali.ole.docstore.metrics.reindex;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/27/12
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReIndexingBatchStatus {
    private String batchStartTime;
    private String batchEndTime;
    private String batchIndexingTime;
    private Long recordsProcessed;
    private Long recordsRemaining;
    private String status;
    private String batchLoadTime;
    private String batchTotalTime;

    public String getBatchStartTime() {
        return batchStartTime;
    }

    public void setBatchStartTime(String batchStartTime) {
        this.batchStartTime = batchStartTime;
    }

    public String getBatchEndTime() {
        return batchEndTime;
    }

    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

    public Long getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Long recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public Long getRecordsRemaining() {
        return recordsRemaining;
    }

    public void setRecordsRemaining(Long recordsRemaining) {
        this.recordsRemaining = recordsRemaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchIndexingTime() {
        return batchIndexingTime;
    }

    public void setBatchIndexingTime(String batchIndexingTime) {
        this.batchIndexingTime = batchIndexingTime;
    }

    public String getBatchLoadTime() {
        return batchLoadTime;
    }

    public void setBatchLoadTime(String batchLoadTime) {
        this.batchLoadTime = batchLoadTime;
    }

    public String getBatchTotalTime() {
        return batchTotalTime;
    }

    public void setBatchTotalTime(String batchTotalTime) {
        this.batchTotalTime = batchTotalTime;
    }
}
