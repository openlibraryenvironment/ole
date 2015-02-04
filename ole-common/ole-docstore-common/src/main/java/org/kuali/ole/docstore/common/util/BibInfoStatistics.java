package org.kuali.ole.docstore.common.util;

import java.util.Date;

/**
 * Created by sambasivam on 20/10/14.
 */
public class BibInfoStatistics {

    private Date startDateTime;
    private Date endDateTime;
    private long bibCount;
    private Date batchStartDateTime;
    private Date batchEndDateTime;
    private long batchTotalTime;
    private long totalTime;


    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public long getBibCount() {
        return bibCount;
    }

    public void setBibCount(long bibCount) {
        this.bibCount = bibCount;
    }

    public Date getBatchStartDateTime() {
        return batchStartDateTime;
    }

    public void setBatchStartDateTime(Date batchStartDateTime) {
        this.batchStartDateTime = batchStartDateTime;
    }

    public Date getBatchEndDateTime() {
        return batchEndDateTime;
    }

    public void setBatchEndDateTime(Date batchEndDateTime) {
        this.batchEndDateTime = batchEndDateTime;
    }

    public long getBatchTotalTime() {
        return batchTotalTime;
    }

    public void setBatchTotalTime(long batchTotalTime) {
        this.batchTotalTime = batchTotalTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return  "Start Time : " + startDateTime +
                "\nBatch start time : " + batchStartDateTime +
                "\nBatch end time : " + batchEndDateTime +
                "\nBatch total time : " + batchTotalTime +
                "\nTotal bib records inserted : " + bibCount +
                "\nEnd Time : " + endDateTime +
                "\nTotal Time : " + totalTime  + "ms";
    }
}
