package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

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
    private List<InvoiceFailureResponse> invoiceFailureResponses;

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
}
