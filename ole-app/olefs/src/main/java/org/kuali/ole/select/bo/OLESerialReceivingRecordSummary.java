package org.kuali.ole.select.bo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 2/11/14
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingRecordSummary {
    private int totalRecordSize;
    private int successRecordSize;
    private int failureRecordSize;
    private int docSuccessCount;
    private int docFailureCount;
    private int hstrySucceesCount;
    private int hstryFailureCount;
    private int typeSuccessCount;
    private int typeFailureCount;
    private List<OLESerialReceivingDocument> failureDocuments;
    private List<OLESerialReceivingDocument> docFailureList;
    private List<OLESerialReceivingHistory> hstryFailureList;
    private List<OLESerialReceivingType> typeFailureList;

    public OLESerialReceivingRecordSummary(int totalRecordSize, int successRecordSize, int failureRecordSize) {
        this.totalRecordSize = totalRecordSize;
        this.successRecordSize = successRecordSize;
        this.failureRecordSize = failureRecordSize;
    }

    public OLESerialReceivingRecordSummary(int totalRecordSize, int successRecordSize, int failureRecordSize, List<OLESerialReceivingDocument> failureDocuments) {
        this.totalRecordSize = totalRecordSize;
        this.successRecordSize = successRecordSize;
        this.failureRecordSize = failureRecordSize;
        this.failureDocuments = failureDocuments;
    }

    public OLESerialReceivingRecordSummary(int totalRecordSize, int docSuccessCount, int docFailureCount, List<OLESerialReceivingDocument> docFailureList,int hstrySucceesCount,int hstryFailureCount,List<OLESerialReceivingHistory> hstryFailureList,int typeSuccessCount,int typeFailureCount,List<OLESerialReceivingType> typeFailureList) {
        this.totalRecordSize = totalRecordSize;
        this.docSuccessCount = docSuccessCount;
        this.docFailureCount = docFailureCount;
        this.docFailureList = docFailureList;
        this.hstrySucceesCount = hstrySucceesCount;
        this.hstryFailureCount = hstryFailureCount;
        this.hstryFailureList = hstryFailureList;
        this.typeSuccessCount = typeSuccessCount;
        this.typeFailureCount = typeFailureCount;
        this.typeFailureList = typeFailureList;
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

    public List<OLESerialReceivingDocument> getFailureDocuments() {
        return failureDocuments;
    }

    public void setFailureDocuments(List<OLESerialReceivingDocument> failureDocuments) {
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

    public int getHstrySucceesCount() {
        return hstrySucceesCount;
    }

    public void setHstrySucceesCount(int hstrySucceesCount) {
        this.hstrySucceesCount = hstrySucceesCount;
    }

    public int getHstryFailureCount() {
        return hstryFailureCount;
    }

    public void setHstryFailureCount(int hstryFailureCount) {
        this.hstryFailureCount = hstryFailureCount;
    }

    public int getTypeSuccessCount() {
        return typeSuccessCount;
    }

    public void setTypeSuccessCount(int typeSuccessCount) {
        this.typeSuccessCount = typeSuccessCount;
    }

    public int getTypeFailureCount() {
        return typeFailureCount;
    }

    public void setTypeFailureCount(int typeFailureCount) {
        this.typeFailureCount = typeFailureCount;
    }

    public List<OLESerialReceivingDocument> getDocFailureList() {
        return docFailureList;
    }

    public void setDocFailureList(List<OLESerialReceivingDocument> docFailureList) {
        this.docFailureList = docFailureList;
    }

    public List<OLESerialReceivingHistory> getHstryFailureList() {
        return hstryFailureList;
    }

    public void setHstryFailureList(List<OLESerialReceivingHistory> hstryFailureList) {
        this.hstryFailureList = hstryFailureList;
    }

    public List<OLESerialReceivingType> getTypeFailureList() {
        return typeFailureList;
    }

    public void setTypeFailureList(List<OLESerialReceivingType> typeFailureList) {
        this.typeFailureList = typeFailureList;
    }
}
