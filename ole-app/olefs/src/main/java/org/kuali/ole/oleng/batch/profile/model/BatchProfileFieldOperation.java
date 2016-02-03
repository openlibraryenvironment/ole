package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileFieldOperation extends MarcDataField {

    private long fieldOperationId;

    @JsonProperty("fieldOperationType")
    private String fieldOperationType;

    @JsonProperty("value")
    private String value;

    @JsonProperty("ignoreGPF")
    private Boolean ignoreGPF;

    public String getFieldOperationType() {
        return fieldOperationType;
    }

    public void setFieldOperationType(String fieldOperationType) {
        this.fieldOperationType = fieldOperationType;
    }

    public long getFieldOperationId() {
        return fieldOperationId;
    }

    public void setFieldOperationId(long fieldOperationId) {
        this.fieldOperationId = fieldOperationId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getIgnoreGPF() {
        return ignoreGPF;
    }

    public void setIgnoreGPF(Boolean ignoreGPF) {
        this.ignoreGPF = ignoreGPF;
    }
}
