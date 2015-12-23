package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SheikS on 11/25/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileMatchPoint extends MarcDataField {

    private long matchPointId;

    @JsonProperty("matchPointDocType")
    private String dataType;

    private String matchPointType;

    private String matchPointValue;

    private String constant;

    public long getMatchPointId() {
        return matchPointId;
    }

    public void setMatchPointId(long matchPointId) {
        this.matchPointId = matchPointId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMatchPointType() {
        return matchPointType;
    }

    public void setMatchPointType(String matchPointType) {
        this.matchPointType = matchPointType;
    }

    public String getMatchPointValue() {
        return matchPointValue;
    }

    public void setMatchPointValue(String matchPointValue) {
        this.matchPointValue = matchPointValue;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }
}
