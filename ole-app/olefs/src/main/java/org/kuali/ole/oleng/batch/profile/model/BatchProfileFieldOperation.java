package org.kuali.ole.oleng.batch.profile.model;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
public class BatchProfileFieldOperation extends MarcDataField {

    private long fieldOperationId;
    private String fieldOperationType;

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

}
