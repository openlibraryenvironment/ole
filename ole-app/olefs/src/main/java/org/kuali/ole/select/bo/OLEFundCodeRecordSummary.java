package org.kuali.ole.select.bo;

import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;

import java.util.List;

public class OLEFundCodeRecordSummary {
    private int totalRecordSize;
    private int successRecordSize;
    private int failureRecordSize;
    private int docSuccessCount;
    private int docFailureCount;
    private int fndAcclnSucceesCount;
    private int fndAcclnFailureCount;
    private List<OleFundCode> failureDocuments;
    private List<OleFundCode> docFailureList;
    private List<OleFundCodeAccountingLine> fndAcclnFailureList;

    public OLEFundCodeRecordSummary(int totalRecordSize, int successRecordSize, int failureRecordSize) {
        this.totalRecordSize = totalRecordSize;
        this.successRecordSize = successRecordSize;
        this.failureRecordSize = failureRecordSize;
    }

    public OLEFundCodeRecordSummary(int totalRecordSize, int successRecordSize, int failureRecordSize, List<OleFundCode> failureDocuments) {
        this.totalRecordSize = totalRecordSize;
        this.successRecordSize = successRecordSize;
        this.failureRecordSize = failureRecordSize;
        this.failureDocuments = failureDocuments;
    }

    public OLEFundCodeRecordSummary(int totalRecordSize, int docSuccessCount, int docFailureCount, List<OleFundCode> docFailureList, int fndAcclnSucceesCount, int fndAcclnFailureCount, List<OleFundCodeAccountingLine> fndAcclnFailureList) {
        this.totalRecordSize = totalRecordSize;
        this.docSuccessCount = docSuccessCount;
        this.docFailureCount = docFailureCount;
        this.docFailureList = docFailureList;
        this.fndAcclnSucceesCount = fndAcclnSucceesCount;
        this.fndAcclnFailureCount = fndAcclnFailureCount;
        this.fndAcclnFailureList = fndAcclnFailureList;
     }

    public int getTotalRecordSize() {
        return totalRecordSize;
    }

    public void setTotalRecordSize(int totalRecordSize) {
        this.totalRecordSize = totalRecordSize;
    }

    public int getSuccessRecordSize() {
        return successRecordSize;
    }

    public void setSuccessRecordSize(int successRecordSize) {
        this.successRecordSize = successRecordSize;
    }

    public int getFailureRecordSize() {
        return failureRecordSize;
    }

    public void setFailureRecordSize(int failureRecordSize) {
        this.failureRecordSize = failureRecordSize;
    }

    public List<OleFundCode> getFailureDocuments() {
        return failureDocuments;
    }

    public void setFailureDocuments(List<OleFundCode> failureDocuments) {
        this.failureDocuments = failureDocuments;
    }

    public int getDocSuccessCount() {
        return docSuccessCount;
    }

    public void setDocSuccessCount(int docSuccessCount) {
        this.docSuccessCount = docSuccessCount;
    }

    public int getDocFailureCount() {
        return docFailureCount;
    }

    public void setDocFailureCount(int docFailureCount) {
        this.docFailureCount = docFailureCount;
    }

    public int getFndAcclnSucceesCount() {
        return fndAcclnSucceesCount;
    }

    public void setFndAcclnSucceesCount(int fndAcclnSucceesCount) {
        this.fndAcclnSucceesCount = fndAcclnSucceesCount;
    }

    public int getFndAcclnFailureCount() {
        return fndAcclnFailureCount;
    }

    public void setFndAcclnFailureCount(int fndAcclnFailureCount) {
        this.fndAcclnFailureCount = fndAcclnFailureCount;
    }

    public List<OleFundCode> getDocFailureList() {
        return docFailureList;
    }

    public void setDocFailureList(List<OleFundCode> docFailureList) {
        this.docFailureList = docFailureList;
    }

    public List<OleFundCodeAccountingLine> getFndAcclnFailureList() {
        return fndAcclnFailureList;
    }

    public void setFndAcclnFailureList(List<OleFundCodeAccountingLine> fndAcclnFailureList) {
        this.fndAcclnFailureList = fndAcclnFailureList;
    }

}
