package org.kuali.ole.docstore.common.response;

/**
 * Created by angelind on 2/16/16.
 */
public class OrderData {

    private String recordNumber;
    private String title;
    private String successfulMatchPoints;
    private String requisitionNumber;

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuccessfulMatchPoints() {
        return successfulMatchPoints;
    }

    public void setSuccessfulMatchPoints(String successfulMatchPoints) {
        this.successfulMatchPoints = successfulMatchPoints;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }
}
