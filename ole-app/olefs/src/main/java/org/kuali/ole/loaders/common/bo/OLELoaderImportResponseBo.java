package org.kuali.ole.loaders.common.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
@XmlRootElement(name = "importResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLELoaderImportResponseBo {

    @XmlElement
    @JsonProperty
    private List<JSONObject> oleCreatedBos;

    @XmlTransient
    @JsonIgnore
    private List<Integer> oleRejectedBos;

    public List<JSONObject> getOleCreatedBos() {
        return oleCreatedBos;
    }

    public void setOleCreatedBos(List<JSONObject> oleCreatedBos) {
        this.oleCreatedBos = oleCreatedBos;
    }

    public List<Integer> getOleRejectedBos() {
        return oleRejectedBos;
    }

    public void setOleRejectedBos(List<Integer> oleRejectedBos) {
        this.oleRejectedBos = oleRejectedBos;
    }
}
