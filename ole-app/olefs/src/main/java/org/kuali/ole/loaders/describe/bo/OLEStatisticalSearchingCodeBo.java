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
@XmlRootElement(name = "oleStatisticalSearchingCode")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class OLEStatisticalSearchingCodeBo {

    @XmlElement
    @JsonProperty
    private String statisticalSearchingCodeId;

    @XmlElement
    @JsonProperty
    private String statisticalSearchingCode;

    @XmlElement
    @JsonProperty
    private String statisticalSearchingName;

    @XmlElement
    @JsonProperty
    private String source;

    @XmlElement
    @JsonProperty
    private Date sourceDate;

    @XmlElement
    @JsonProperty
    private boolean active;

    public String getStatisticalSearchingCodeId() {
        return statisticalSearchingCodeId;
    }

    public void setStatisticalSearchingCodeId(String statisticalSearchingCodeId) {
        this.statisticalSearchingCodeId = statisticalSearchingCodeId;
    }

    public String getStatisticalSearchingCode() {
        return statisticalSearchingCode;
    }

    public void setStatisticalSearchingCode(String statisticalSearchingCode) {
        this.statisticalSearchingCode = statisticalSearchingCode;
    }

    public String getStatisticalSearchingName() {
        return statisticalSearchingName;
    }

    public void setStatisticalSearchingName(String statisticalSearchingName) {
        this.statisticalSearchingName = statisticalSearchingName;
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

