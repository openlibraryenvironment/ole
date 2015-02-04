package org.kuali.asr.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 1/6/14
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class LookupASRRequestResponseBO {
    @XmlElement
    @JsonProperty
    private String code;
    @XmlElement
    @JsonProperty
    private String message;
    @XmlTransient
    @JsonIgnore
    private int statusCode;
    @XmlElement
    @JsonProperty
    private ASRRequestDetailsBo asrRequestDetailsBo;

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

    public ASRRequestDetailsBo getAsrRequestDetailsBo() {
        return asrRequestDetailsBo;
    }

    public void setAsrRequestDetailsBo(ASRRequestDetailsBo asrRequestDetailsBo) {
        this.asrRequestDetailsBo = asrRequestDetailsBo;
    }
}
