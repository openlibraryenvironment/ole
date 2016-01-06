package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 12/14/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileAddOrOverlay {

    private String matchOption;

    @JsonProperty("addOrOverlayDocType")
    private String dataType;
    private String operation;
    private String bibStatus;
    private String addOperation;
    @JsonProperty("addOrOverlayField")
    private String field;
    @JsonProperty("addOrOverlayFieldOperation")
    private String fieldOperation;
    @JsonProperty("addOrOverlayFieldValue")
    private String fieldValue;
    private boolean addItems;

    public String getMatchOption() {
        return matchOption;
    }

    public void setMatchOption(String matchOption) {
        this.matchOption = matchOption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBibStatus() {
        return bibStatus;
    }

    public void setBibStatus(String bibStatus) {
        this.bibStatus = bibStatus;
    }

    public String getAddOperation() {
        return addOperation;
    }

    public void setAddOperation(String addOperation) {
        this.addOperation = addOperation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldOperation() {
        return fieldOperation;
    }

    public void setFieldOperation(String fieldOperation) {
        this.fieldOperation = fieldOperation;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isAddItems() {
        return addItems;
    }

    public void setAddItems(boolean addItems) {
        this.addItems = addItems;
    }
}
