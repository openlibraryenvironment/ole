package org.kuali.ole.docstore.common.response;

/**
 * Created by angelind on 2/16/16.
 */
public class OrderData {

    private Integer recordNumber;
    private String title;
    private Integer reqDocumentNumber;
    private String successfulMatchPoints;

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReqDocumentNumber() {
        return reqDocumentNumber;
    }

    public void setReqDocumentNumber(Integer reqDocumentNumber) {
        this.reqDocumentNumber = reqDocumentNumber;
    }

    public String getSuccessfulMatchPoints() {
        return successfulMatchPoints;
    }

    public void setSuccessfulMatchPoints(String successfulMatchPoints) {
        this.successfulMatchPoints = successfulMatchPoints;
    }
}
