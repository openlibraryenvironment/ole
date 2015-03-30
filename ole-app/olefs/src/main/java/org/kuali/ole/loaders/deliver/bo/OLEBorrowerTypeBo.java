package org.kuali.ole.loaders.deliver.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by maheswarang on 2/18/15.
 */
@XmlRootElement(name = "oleBorrowerType")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEBorrowerTypeBo {

    @XmlElement
    @JsonProperty
    private String borrowerTypeId;

    @XmlElement
    @JsonProperty
    private String borrowerTypeCode;

    @XmlElement
    @JsonProperty
    private String borrowerTypeDescription;

    @XmlElement
    @JsonProperty
    private String borrowerTypeName;

    @XmlElement
    @JsonProperty
    private boolean active;


    public String getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(String borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public String getBorrowerTypeCode() {
        return borrowerTypeCode;
    }

    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    public String getBorrowerTypeDescription() {
        return borrowerTypeDescription;
    }

    public void setBorrowerTypeDescription(String borrowerTypeDescription) {
        this.borrowerTypeDescription = borrowerTypeDescription;
    }

    public String getBorrowerTypeName() {
        return borrowerTypeName;
    }

    public void setBorrowerTypeName(String borrowerTypeName) {
        this.borrowerTypeName = borrowerTypeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
