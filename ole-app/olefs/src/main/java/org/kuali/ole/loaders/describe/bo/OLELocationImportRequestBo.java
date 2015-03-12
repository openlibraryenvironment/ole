package org.kuali.ole.loaders.describe.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
@XmlRootElement(name = "oleLocationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLELocationImportRequestBo {

    @XmlElement(name = "locations")
    @JsonProperty("locations")
    List<OLELocationBo> oleLocationBoList = new ArrayList<>();

    public List<OLELocationBo> getOleLocationBoList() {
        return oleLocationBoList;
    }

    public void setOleLocationBoList(List<OLELocationBo> oleLocationBoList) {
        this.oleLocationBoList = oleLocationBoList;
    }
}
