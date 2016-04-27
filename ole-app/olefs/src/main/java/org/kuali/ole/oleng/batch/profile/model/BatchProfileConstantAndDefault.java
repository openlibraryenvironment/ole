package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 12/24/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileConstantAndDefault {

    private long constantAndDefaultId;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldValue")
    private String fieldValue;

    @JsonProperty("constantOrDefault")
    private String constantOrDefault;

    public String getConstantOrDefault() {
        return constantOrDefault;
    }

    public void setConstantOrDefault(String constantOrDefault) {
        this.constantOrDefault = constantOrDefault;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public long getConstantAndDefaultId() {
        return constantAndDefaultId;
    }

    public void setConstantAndDefaultId(long constantAndDefaultId) {
        this.constantAndDefaultId = constantAndDefaultId;
    }
}
