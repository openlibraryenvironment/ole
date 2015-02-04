package org.kuali.asr.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 1/6/14
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRRequestDetailsBo {
    @XmlElement
    @XmlElementWrapper
    @JsonProperty
    private List<ASRRequestDetailBo> asrRequestDetailBos = new ArrayList<ASRRequestDetailBo>();

    public List<ASRRequestDetailBo> getAsrRequestDetailBos() {
        return asrRequestDetailBos;
    }

    public void setAsrRequestDetailBos(List<ASRRequestDetailBo> asrRequestDetailBos) {
        this.asrRequestDetailBos = asrRequestDetailBos;
    }
}
