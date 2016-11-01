package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 4/20/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileFilterCriteria extends MarcDataField {

    private long filterCriteriaId;

    @JsonProperty("filterFieldName")
    private String fieldName;

    @JsonProperty("filterFieldNameText")
    private String fieldNameText;

    @JsonProperty("filterFieldValue")
    private String fieldValue;

    @JsonProperty("filterFieldRangeFrom")
    private String rangeFrom;

    @JsonProperty("filterFieldRangeTo")
    private String rangeTo;

    public long getFilterCriteriaId() {
        return filterCriteriaId;
    }

    public void setFilterCriteriaId(long filterCriteriaId) {
        this.filterCriteriaId = filterCriteriaId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameText() {
        return fieldNameText;
    }

    public void setFieldNameText(String fieldNameText) {
        this.fieldNameText = fieldNameText;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(String rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public String getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(String rangeTo) {
        this.rangeTo = rangeTo;
    }
}
