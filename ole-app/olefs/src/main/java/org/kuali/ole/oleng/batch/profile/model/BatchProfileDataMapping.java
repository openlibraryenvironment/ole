package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SheikS on 11/25/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileDataMapping extends MarcDataField {

    @JsonProperty("dataMappingId")
    private long dataMappingId;

    @JsonProperty("dataMappingDocType")
    private String dataType;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("field")
    private String field;

    @JsonProperty("constant")
    private String constant;

    @JsonProperty("transferOption")
    private String transferOption;

    @JsonProperty("isMultiValue")
    private boolean isMultiValue;


    @JsonProperty("priority")
    private int priority;

    public long getDataMappingId() {
        return dataMappingId;
    }

    public void setDataMappingId(long dataMappingId) {
        this.dataMappingId = dataMappingId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getTransferOption() {
        return transferOption;
    }

    public void setTransferOption(String transferOption) {
        this.transferOption = transferOption;
    }

    public boolean isMultiValue() {
        return isMultiValue;
    }

    public void setMultiValue(boolean multiValue) {
        isMultiValue = multiValue;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
