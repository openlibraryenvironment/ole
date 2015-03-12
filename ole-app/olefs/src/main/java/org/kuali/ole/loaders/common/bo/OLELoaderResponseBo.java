package org.kuali.ole.loaders.common.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;

/**
 * Created by sheiksalahudeenm on 2/11/15.
 */
@XmlRootElement(name = "oleLoader")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLELoaderResponseBo {

    @XmlElement(name = "code")
    @JsonProperty("code")
    private String code;

    @XmlElement(name = "message")
    @JsonProperty("message")
    private String message;

    @XmlTransient
    @JsonIgnore
    private int statusCode;

    @XmlElement(name = "details")
    @JsonProperty("details")
    private String details;

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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

