package org.kuali.ole.select.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 3/15/14
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingFailureHistory {
    private String errorMessage;
    private OLESerialReceivingHistory oleSerialReceivingHistory;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OLESerialReceivingHistory getOleSerialReceivingHistory() {
        return oleSerialReceivingHistory;
    }

    public void setOleSerialReceivingHistory(OLESerialReceivingHistory oleSerialReceivingHistory) {
        this.oleSerialReceivingHistory = oleSerialReceivingHistory;
    }
}
