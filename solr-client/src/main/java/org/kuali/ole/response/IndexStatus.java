package org.kuali.ole.response;

import java.util.Date;

/**
 * Created by sheiks on 27/03/17.
 */
public class IndexStatus {
    private String status;
    private Date startTime;
    private Date endTime;
    private int numberOfBibFetched;
    private int numberOfBibProcessed;
    private int numberOfHoldingsFetched;
    private int numberOfHoldingsProcessed;
    private int numberOfItemsFetched;
    private int numberOfItemsProcessed;
    private boolean running;
    private Integer noOfDbThreads;
    private Integer docsPerThread;

    public String getStatus() {
        if(running) {
            return "Running";
        } else {
            if(null != startTime) {
                return "Finished";
            } else {
                return "";
            }
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getNumberOfBibFetched() {
        return numberOfBibFetched;
    }

    public void setNumberOfBibFetched(int numberOfBibFetched) {
        this.numberOfBibFetched = numberOfBibFetched;
    }

    public int getNumberOfBibProcessed() {
        return numberOfBibProcessed;
    }

    public void setNumberOfBibProcessed(int numberOfBibProcessed) {
        this.numberOfBibProcessed = numberOfBibProcessed;
    }

    public int getNumberOfHoldingsFetched() {
        return numberOfHoldingsFetched;
    }

    public void setNumberOfHoldingsFetched(int numberOfHoldingsFetched) {
        this.numberOfHoldingsFetched = numberOfHoldingsFetched;
    }

    public int getNumberOfHoldingsProcessed() {
        return numberOfHoldingsProcessed;
    }

    public void setNumberOfHoldingsProcessed(int numberOfHoldingsProcessed) {
        this.numberOfHoldingsProcessed = numberOfHoldingsProcessed;
    }

    public int getNumberOfItemsFetched() {
        return numberOfItemsFetched;
    }

    public void setNumberOfItemsFetched(int numberOfItemsFetched) {
        this.numberOfItemsFetched = numberOfItemsFetched;
    }

    public int getNumberOfItemsProcessed() {
        return numberOfItemsProcessed;
    }

    public void setNumberOfItemsProcessed(int numberOfItemsProcessed) {
        this.numberOfItemsProcessed = numberOfItemsProcessed;
    }

    public Integer getNoOfDbThreads() {
        return noOfDbThreads;
    }

    public void setNoOfDbThreads(Integer noOfDbThreads) {
        this.noOfDbThreads = noOfDbThreads;
    }

    public Integer getDocsPerThread() {
        return docsPerThread;
    }

    public void setDocsPerThread(Integer docsPerThread) {
        this.docsPerThread = docsPerThread;
    }

    public void addBibFetched(int size) {
        synchronized (this) {
            this.numberOfBibFetched += size;
        }
    }

    public void addBibProcessed(int count) {
        synchronized (this) {
            this.numberOfBibProcessed += count;
        }
    }

    public void addHoldingsFetched(int size) {
        synchronized (this) {
            this.numberOfHoldingsFetched += size;
        }
    }

    public void addHoldingsProcessed(int count) {
        synchronized (this) {
            this.numberOfHoldingsProcessed += count;
        }
    }

    public void addItemsFetched(int size) {
        synchronized (this) {
            this.numberOfItemsFetched += size;
        }
    }

    public void addItemsProcessed(int count) {
        synchronized (this) {
            this.numberOfItemsProcessed += count;
        }
    }

    public void resetStatus() {
        this.running = true;
        startTime = new Date();
        endTime = null;
        numberOfBibFetched = 0;
        numberOfBibProcessed = 0;
        numberOfHoldingsFetched = 0;
        numberOfHoldingsProcessed = 0;
        numberOfItemsFetched = 0;
        numberOfItemsProcessed = 0;
    }
}
