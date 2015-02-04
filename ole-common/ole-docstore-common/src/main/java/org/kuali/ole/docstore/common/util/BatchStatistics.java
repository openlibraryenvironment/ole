package org.kuali.ole.docstore.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/3/14
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchStatistics {
    private static final Logger LOG = LoggerFactory.getLogger(BatchStatistics.class);
    private int bibCount;
    private int holdingsCount;
    private int itemCount;
    private long timeTaken;
    private Date startTime;
    private Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public int getBibCount() {
        return bibCount;
    }

    public void setBibCount(int bibCount) {
        this.bibCount = bibCount;
    }

    public void addBibCount(int bibCount) {
        this.bibCount = this.bibCount + bibCount;
    }

    public void setHoldingsCount(int holdingsCount) {
        this.holdingsCount = holdingsCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getHoldingsCount() {
        return holdingsCount;
    }

    public void addHoldingsCount(int holdingsCount) {
        this.holdingsCount = this.holdingsCount + holdingsCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void addItemCount(int itemCount) {
        this.itemCount = this.itemCount + itemCount;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void addTimeTaken(Long timeTaken) {
        this.timeTaken = this.timeTaken + timeTaken;
    }

    public void printStatistics(){
        LOG.info(toString());

    }

    @Override
    public String toString() {
        return "BatchStatistics{" +
                "Bibs=" + bibCount +
                ", Holdings=" + holdingsCount +
                ", Items=" + itemCount +
                ", Time Taken=" + timeTaken +
                '}';
    }
}
