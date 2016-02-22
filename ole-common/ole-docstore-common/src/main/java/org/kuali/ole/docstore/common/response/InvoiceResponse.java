package org.kuali.ole.docstore.common.response;

/**
 * Created by SheikS on 2/18/2016.
 */
public class InvoiceResponse {
    private String documentNumber;
    private int noOfRecordLinked;
    private int noOfRecordUnlinked;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getNoOfRecordLinked() {
        return noOfRecordLinked;
    }

    public void setNoOfRecordLinked(int noOfRecordLinked) {
        this.noOfRecordLinked = noOfRecordLinked;
    }

    public int getNoOfRecordUnlinked() {
        return noOfRecordUnlinked;
    }

    public void setNoOfRecordUnlinked(int noOfRecordUnlinked) {
        this.noOfRecordUnlinked = noOfRecordUnlinked;
    }

    public void addLinkCount() {
        noOfRecordLinked++;
    }

    public void addUnLinkCount() {
        noOfRecordUnlinked++;
    }
}
