package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SheikS on 11/25/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileDataTransformer extends MarcDataField {

    private long dataTransformerId;

    @JsonProperty("dataTransformationDocType")
    private String dataType;

    @JsonProperty("dataTransformationActionType")
    private String actionType;

    @JsonProperty("dataTransformationOperation")
    private String operation;

    @JsonProperty("destinationDataField")
    private String destDataField;

    @JsonProperty("destinationInd1")
    private String destInd1;

    @JsonProperty("destinationInd2")
    private String destInd2;

    @JsonProperty("destinationSubField")
    private String destSubField;

    @JsonProperty("dataTransformationConstant")
    private String constant;

    @JsonProperty("dataTransformationStep")
    private Integer step;

    public long getDataTransformerId() {
        return dataTransformerId;
    }

    public void setDataTransformerId(long dataTransformerId) {
        this.dataTransformerId = dataTransformerId;
    }

    public String getDataType() {
        return dataType;
}

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
}
