package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
