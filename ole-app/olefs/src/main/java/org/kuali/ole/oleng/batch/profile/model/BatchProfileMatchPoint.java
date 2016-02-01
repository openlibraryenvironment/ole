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
    private String destDataField;
    private String destInd1;
    private String destInd2;
    private String destSubField;
    private boolean isMultiValue;

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

    public String getDestDataField() {
        return destDataField;
    }

    public void setDestDataField(String destDataField) {
        this.destDataField = destDataField;
    }

    public String getDestInd1() {
        return destInd1;
    }

    public void setDestInd1(String destInd1) {
        this.destInd1 = destInd1;
    }

    public String getDestInd2() {
        return destInd2;
    }

    public void setDestInd2(String destInd2) {
        this.destInd2 = destInd2;
    }

    public String getDestSubField() {
        return destSubField;
    }

    public void setDestSubField(String destSubField) {
        this.destSubField = destSubField;
    }

    public boolean isMultiValue() {
        return isMultiValue;
    }

    public void setMultiValue(boolean multiValue) {
        isMultiValue = multiValue;
    }
}
