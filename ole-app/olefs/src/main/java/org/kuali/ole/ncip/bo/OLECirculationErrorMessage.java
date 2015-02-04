package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/4/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECirculationErrorMessage {
    private String code;
    private String message;
    private String requiredParameters;
    private String service;

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

    public String getRequiredParameters() {
        return requiredParameters;
    }

    public void setRequiredParameters(String requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
