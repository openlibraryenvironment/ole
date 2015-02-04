package org.kuali.asr.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/24/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRRequests {
    @XmlElementWrapper
    @XmlElement(name="asrRequest")
    @JsonProperty
    private List<ASRRequest> asrRequests;

    public List<ASRRequest> getAsrRequests() {
        return asrRequests;
    }

    public void setAsrRequests(List<ASRRequest> asrRequests) {
        this.asrRequests = asrRequests;
    }
}
