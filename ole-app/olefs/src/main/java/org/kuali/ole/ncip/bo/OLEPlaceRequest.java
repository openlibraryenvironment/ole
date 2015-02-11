package org.kuali.ole.ncip.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEPlaceRequest {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("queuePosition")
    private String queuePosition;

    @JsonProperty("availableDate")
    private String availableDate;

    /*This following fields are only for SIP2*/
    @JsonIgnore
    private String expirationDate;
    /*This above fields are only for SIP2*/

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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
