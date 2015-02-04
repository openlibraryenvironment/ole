package org.kuali.ole.select.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 3/15/14
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingFailureType {
    private String errorMessage;
    private OLESerialReceivingType oleSerialReceivingType;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OLESerialReceivingType getOleSerialReceivingType() {
        return oleSerialReceivingType;
    }

    public void setOleSerialReceivingType(OLESerialReceivingType oleSerialReceivingType) {
        this.oleSerialReceivingType = oleSerialReceivingType;
    }
}
