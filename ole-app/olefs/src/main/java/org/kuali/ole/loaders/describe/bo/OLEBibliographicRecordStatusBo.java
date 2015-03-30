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
@XmlRootElement(name = "oleBibliographicRecordStatus")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEBibliographicRecordStatusBo {

    @XmlElement
    @JsonProperty
    private String bibliographicRecordStatusId;

    @XmlElement
    @JsonProperty
    private String bibliographicRecordStatusCode;

    @XmlElement
    @JsonProperty
    private String bibliographicRecordStatusName;

    @XmlElement
    @JsonProperty
    private String source;

    @XmlElement
    @JsonProperty
    private Date sourceDate;

    @XmlElement
    @JsonProperty
    private boolean active;

    public String getBibliographicRecordStatusId() {
        return bibliographicRecordStatusId;
    }

    public void setBibliographicRecordStatusId(String bibliographicRecordStatusId) {
        this.bibliographicRecordStatusId = bibliographicRecordStatusId;
    }

    public String getBibliographicRecordStatusCode() {
        return bibliographicRecordStatusCode;
    }

    public void setBibliographicRecordStatusCode(String bibliographicRecordStatusCode) {
        this.bibliographicRecordStatusCode = bibliographicRecordStatusCode;
    }

    public String getBibliographicRecordStatusName() {
        return bibliographicRecordStatusName;
    }

    public void setBibliographicRecordStatusName(String bibliographicRecordStatusName) {
        this.bibliographicRecordStatusName = bibliographicRecordStatusName;
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

