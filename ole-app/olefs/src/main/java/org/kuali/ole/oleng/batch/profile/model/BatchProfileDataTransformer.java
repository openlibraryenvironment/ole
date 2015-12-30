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

    @JsonProperty("dataTransformationSourceField")
    private String sourceField;

    @JsonProperty("dataTransformationOperation")
    private String operation;

    @JsonProperty("dataTransformationDestinationField")
    private String destinationField;

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

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDestinationField() {
        return destinationField;
    }

    public void setDestinationField(String destinationField) {
        this.destinationField = destinationField;
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
}
