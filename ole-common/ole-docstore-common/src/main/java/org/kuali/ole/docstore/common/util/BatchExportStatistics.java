package org.kuali.ole.docstore.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/3/14
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchExportStatistics extends BatchStatistics {

    private static final Logger LOG = LoggerFactory.getLogger(BatchExportStatistics.class);

    private String fileName;

    private long timeTakenForBibMarcRecords;

    private long timeTakenForProcessing;

    private long timeTakenForWritingRecords;

    private long totalTimeTaken;

    public long getTotalTimeTaken() {
        return totalTimeTaken;
    }

    public void setTotalTimeTaken(Long totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public long getTimeTakenForBibMarcRecords() {
        return timeTakenForBibMarcRecords;
    }

    public void addTimeTakenForBibMarcRecords(long timeTakenForBibMarcRecords) {
        this.timeTakenForBibMarcRecords =   this.timeTakenForBibMarcRecords + timeTakenForBibMarcRecords;
    }

    public long getTimeTakenForProcessing() {
        return timeTakenForProcessing;
    }

    public void addTimeTakenForProcessing(long timeTakenForProcessing) {
        this.timeTakenForProcessing =  this.timeTakenForProcessing + timeTakenForProcessing;
    }

    public long getTimeTakenForWritingRecords() {
        return timeTakenForWritingRecords;
    }

    public void addTimeTakenForWritingRecords(long timeTakenForWritingRecords) {
        this.timeTakenForWritingRecords = this.timeTakenForWritingRecords+ timeTakenForWritingRecords;
    }

    @Override
    public String toString() {
        return "BatchExportStatistics{" +
                "  fileName='" + fileName + '\'' +
                ", Exported " + getBibCount() + " Bibs ," + getHoldingsCount() + " Holdings ," + getItemCount() + " Items." +
                "  Time Taken - fetch :" + getTimeTaken() +
                ", parse=" + timeTakenForBibMarcRecords +
                ", Process=" + timeTakenForProcessing +
                ", Writing=" + timeTakenForWritingRecords +
                ", Total =" + totalTimeTaken +
                '}';
    }


    public void printExportStatistics(){
        LOG.info(toString());
    }
}
