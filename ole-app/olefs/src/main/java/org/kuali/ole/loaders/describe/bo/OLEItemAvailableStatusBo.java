package org.kuali.ole.loaders.describe.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by sheiksalahudeenm on 2/3/15.
 */
@XmlRootElement(name = "oleItemAvailableStatus")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEItemAvailableStatusBo {

    @XmlElement
    @JsonProperty
    private String itemAvailableStatusId;

    @XmlElement
    @JsonProperty
    private String itemAvailableStatusCode;

    @XmlElement
    @JsonProperty
    private String itemAvailableStatusName;

    @XmlElement
    @JsonProperty
    private boolean active;

    public String getItemAvailableStatusId() {
        return itemAvailableStatusId;
    }

    public void setItemAvailableStatusId(String itemAvailableStatusId) {
        this.itemAvailableStatusId = itemAvailableStatusId;
    }

    public String getItemAvailableStatusCode() {
        return itemAvailableStatusCode;
    }

    public void setItemAvailableStatusCode(String itemAvailableStatusCode) {
        this.itemAvailableStatusCode = itemAvailableStatusCode;
    }

    public String getItemAvailableStatusName() {
        return itemAvailableStatusName;
    }

    public void setItemAvailableStatusName(String itemAvailableStatusName) {
        this.itemAvailableStatusName = itemAvailableStatusName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

