package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 12/14/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileAddOrOverlay extends MarcDataField {

    private String matchOption;

    @JsonProperty("addOrOverlayDocType")
    private String dataType;
    private String operation;
    private String addOperation;
    @JsonProperty("addOrOverlayField")
    private String field;
    @JsonProperty("addOrOverlayFieldOperation")
    private String fieldOperation;
    @JsonProperty("addOrOverlayFieldValue")
    private String fieldValue;
    private boolean addItems;

    private String addOrOverlayField;
    private String addOrOverlayFieldOperation;
    private String addOrOverlayFieldValue;

    public String getAddOrOverlayField() {
        return addOrOverlayField;
    }

    public void setAddOrOverlayField(String addOrOverlayField) {
        this.addOrOverlayField = addOrOverlayField;
    }

    public String getAddOrOverlayFieldOperation() {
        return addOrOverlayFieldOperation;
    }

    public void setAddOrOverlayFieldOperation(String addOrOverlayFieldOperation) {
        this.addOrOverlayFieldOperation = addOrOverlayFieldOperation;
    }

    public String getAddOrOverlayFieldValue() {
        return addOrOverlayFieldValue;
    }

    public void setAddOrOverlayFieldValue(String addOrOverlayFieldValue) {
        this.addOrOverlayFieldValue = addOrOverlayFieldValue;
    }

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
