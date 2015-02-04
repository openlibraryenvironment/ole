package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPlaceRequest {
    private String code;
    private String message;
    private String requestId;
    private String queuePosition;
    private String availableDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(String queuePosition) {
        this.queuePosition = queuePosition;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }
}
