package org.kuali.ole.oleng.batch.profile.model;

/**
 * Created by SheikS on 11/25/2015.
 */
public class BatchProfileDataMapping extends MarcDataField {

    private long dataMappingId;
    private String dataType;
    private String destination;
    private String field;

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
}
