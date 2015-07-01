package org.kuali.ole.utility;

/**
 * Created by pvsubrah on 4/7/15.
 */
public class OleStopWatch {

    private long startTime = 0;
    private long endTime   = 0;

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime   = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public long getTotalTime() {
        return this.endTime - this.startTime;
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
    }
}
