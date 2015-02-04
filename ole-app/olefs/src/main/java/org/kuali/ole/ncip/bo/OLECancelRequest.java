package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancelRequest {
    private String code;
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}