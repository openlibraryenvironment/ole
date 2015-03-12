package org.kuali.ole.loaders.describe.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sheiksalahudeenm on 2/3/15.
 */
@XmlRootElement(name = "oleLocation")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLELocationBo {

    @XmlElement
    @JsonProperty
    private String locationCode;

    @XmlElement
    @JsonProperty
    private String locationId;

    @XmlElement
    @JsonProperty
    private String locationName;

    @XmlElement
    @JsonProperty
    private String parentLocationId;

    @XmlElement
    @JsonProperty
    private String locationLevelId;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(String parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public String getLocationLevelId() {
        return locationLevelId;
    }

    public void setLocationLevelId(String locationLevelId) {
        this.locationLevelId = locationLevelId;
    }
}

