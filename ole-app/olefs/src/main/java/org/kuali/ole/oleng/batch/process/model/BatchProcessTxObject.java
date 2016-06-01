package org.kuali.ole.oleng.batch.process.model;

import org.kuali.ole.Exchange;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.spring.batch.processor.BatchFileProcessor;
import org.kuali.ole.utility.OleStopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 4/5/2016.
 */
public class BatchProcessTxObject {
    private String fileExtension;
    private BatchProcessProfile batchProcessProfile;
    private String reportDirectoryName;
    private String incomingFileDirectoryPath;
    private BatchJobDetails batchJobDetails;
    private BatchFileProcessor batchFileProcessor;
    private Exchange exchangeForOrderOrInvoiceImport;
    private Exchange exchangeObjectForBibImport;
    private Exchange exchangeObjectForBatchExport;
    private OleStopWatch oleStopWatch;
    private int totalNumberOfRecords;
    private int numberOfFailurRecords;
    private boolean exceptionCaught;
    private List<BatchProcessFailureResponse> batchProcessFailureResponses;
    private boolean stopped;

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public BatchProcessProfile getBatchProcessProfile() {
        return batchProcessProfile;
    }

    public void setBatchProcessProfile(BatchProcessProfile batchProcessProfile) {
        this.batchProcessProfile = batchProcessProfile;
    }

    public String getReportDirectoryName() {
        return reportDirectoryName;
    }

    public void setReportDirectoryName(String reportDirectoryName) {
        this.reportDirectoryName = reportDirectoryName;
    }

    public String getIncomingFileDirectoryPath() {
        return incomingFileDirectoryPath;
    }

    public void setIncomingFileDirectoryPath(String incomingFileDirectoryPath) {
        this.incomingFileDirectoryPath = incomingFileDirectoryPath;
    }

    public BatchJobDetails getBatchJobDetails() {
        return batchJobDetails;
    }

    public BatchFileProcessor getBatchFileProcessor() {
        return batchFileProcessor;
    }

    public void setBatchFileProcessor(BatchFileProcessor batchFileProcessor) {
        this.batchFileProcessor = batchFileProcessor;
    }

    public void setBatchJobDetails(BatchJobDetails batchJobDetails) {
        this.batchJobDetails = batchJobDetails;
    }

    public Exchange getExchangeForOrderOrInvoiceImport() {
        if(exchangeForOrderOrInvoiceImport == null) {
            exchangeForOrderOrInvoiceImport = new Exchange();
        }
        return exchangeForOrderOrInvoiceImport;
    }

    public void setExchangeForOrderOrInvoiceImport(Exchange exchangeForOrderOrInvoiceImport) {
        this.exchangeForOrderOrInvoiceImport = exchangeForOrderOrInvoiceImport;
    }

    public Exchange getExchangeObjectForBibImport() {
        if(exchangeObjectForBibImport == null) {
            exchangeObjectForBibImport = new Exchange();
        }
        return exchangeObjectForBibImport;
    }

    public void setExchangeObjectForBibImport(Exchange exchangeObjectForBibImport) {
        this.exchangeObjectForBibImport = exchangeObjectForBibImport;
    }

    public Exchange getExchangeObjectForBatchExport() {
        if (null == exchangeObjectForBatchExport) {
            exchangeObjectForBatchExport = new Exchange();
        }
        return exchangeObjectForBatchExport;
    }

    public void setExchangeObjectForBatchExport(Exchange exchangeObjectForBatchExport) {
        this.exchangeObjectForBatchExport = exchangeObjectForBatchExport;
    }

    public OleStopWatch getOleStopWatch() {
        if(null == oleStopWatch) {
            oleStopWatch = new OleStopWatch();
        }
        return oleStopWatch;
    }

    public void setOleStopWatch(OleStopWatch oleStopWatch) {
        this.oleStopWatch = oleStopWatch;
    }

    public int getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    public void setTotalNumberOfRecords(int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }

    public int getNumberOfFailurRecords() {
        return numberOfFailurRecords;
    }

    public void setNumberOfFailurRecords(int numberOfFailurRecords) {
        this.numberOfFailurRecords = numberOfFailurRecords;
    }

    public boolean isExceptionCaught() {
        return exceptionCaught;
    }

    public void setExceptionCaught(boolean exceptionCaught) {
        this.exceptionCaught = exceptionCaught;
    }

    public List<BatchProcessFailureResponse> getBatchProcessFailureResponses() {
        if(null == batchProcessFailureResponses) {
            batchProcessFailureResponses = new ArrayList<>();
        }
        return batchProcessFailureResponses;
    }

    public void setBatchProcessFailureResponses(List<BatchProcessFailureResponse> batchProcessFailureResponses) {
        this.batchProcessFailureResponses = batchProcessFailureResponses;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
