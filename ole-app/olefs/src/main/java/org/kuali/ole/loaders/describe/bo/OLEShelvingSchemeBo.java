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
 * Created by sheiksalahudeenm on 2/16/15.
 */
@XmlRootElement(name = "oleShelvingScheme")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEShelvingSchemeBo {

    @XmlElement
    @JsonProperty
    private Integer shelvingSchemeId;

    @XmlElement
    @JsonProperty
    private String shelvingSchemeCode;

    @XmlElement
    @JsonProperty
    private String shelvingSchemeName;

    @XmlElement
    @JsonProperty
    private String source;

    @XmlElement
    @JsonProperty
    private Date sourceDate;

    @XmlElement
    @JsonProperty
    private boolean active;

    public Integer getShelvingSchemeId() {
        return shelvingSchemeId;
    }

    public void setShelvingSchemeId(Integer shelvingSchemeId) {
        this.shelvingSchemeId = shelvingSchemeId;
    }

    public String getShelvingSchemeCode() {
        return shelvingSchemeCode;
    }

    public void setShelvingSchemeCode(String shelvingSchemeCode) {
        this.shelvingSchemeCode = shelvingSchemeCode;
    }

    public String getShelvingSchemeName() {
        return shelvingSchemeName;
    }

    public void setShelvingSchemeName(String shelvingSchemeName) {
        this.shelvingSchemeName = shelvingSchemeName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(Date sourceDate) {
        this.sourceDate = sourceDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
