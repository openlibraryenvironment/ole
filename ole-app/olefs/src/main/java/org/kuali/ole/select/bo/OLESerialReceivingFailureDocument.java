package org.kuali.ole.select.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 3/14/14
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingFailureDocument {
    private String errorMessage;
    private OLESerialReceivingDocument oleSerialReceivingDocument;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OLESerialReceivingDocument getOleSerialReceivingDocument() {
        return oleSerialReceivingDocument;
    }

    public void setOleSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        this.oleSerialReceivingDocument = oleSerialReceivingDocument;
    }
}
