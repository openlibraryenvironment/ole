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
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "asrItems")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRItems {
    @XmlElementWrapper
    @XmlElement(name="asrItem")
    @JsonProperty("asrItems")
    private List<ASRItem> asrItems;

    public List<ASRItem> getAsrItems() {
        return asrItems;
    }

    public void setAsrItems(List<ASRItem> asrItems) {
        this.asrItems = asrItems;
    }
}
