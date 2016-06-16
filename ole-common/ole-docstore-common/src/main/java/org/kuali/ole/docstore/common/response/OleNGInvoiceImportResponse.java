package org.kuali.ole.docstore.common.response;

import org.kuali.ole.docstore.common.pojo.RecordDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 2/19/2016.
 */
public class OleNGInvoiceImportResponse {
    private List<InvoiceResponse> invoiceResponses;
    private int matchedCount;
    private int unmatchedCount;
    private int multiMatchedCount;
    private String jobDetailId;
    private String jobName;
    private String fileExtension;
    private List<InvoiceFailureResponse> invoiceFailureResponses;
    private Map<Integer, RecordDetails> recordsMap;

    public List<InvoiceResponse> getInvoiceResponses() {
        if(null == invoiceResponses) {
            invoiceResponses = new ArrayList<>();
        }
        return invoiceResponses;
    }

    public void setInvoiceResponses(List<InvoiceResponse> invoiceResponses) {
        this.invoiceResponses = invoiceResponses;
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

    public List<InvoiceFailureResponse> getInvoiceFailureResponses() {
        if(null == invoiceFailureResponses) {
            invoiceFailureResponses = new ArrayList<>();
        }
        return invoiceFailureResponses;
    }

    public void setInvoiceFailureResponses(List<InvoiceFailureResponse> invoiceFailureResponses) {
        this.invoiceFailureResponses = invoiceFailureResponses;
    }

    public void addInvoiceFailureResponse(InvoiceFailureResponse invoiceFailureResponse) {
        getInvoiceFailureResponses().add(invoiceFailureResponse);
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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
