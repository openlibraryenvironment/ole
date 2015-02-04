package org.kuali.ole.alert.bo;


import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by maheswarang on 8/12/14.
 */
public class AlertFieldValueMapping extends PersistableBusinessObjectBase {

    private String alertFieldValueMappingId;

    private String fieldName;

    private String fieldType;

    private String fieldValue;

    private boolean booFieldValue;

    private String alertDocumentId;

    private AlertDocument alertDocument;



    public String getAlertFieldValueMappingId() {
        return alertFieldValueMappingId;
    }

    public void setAlertFieldValueMappingId(String alertFieldValueMappingId) {
        this.alertFieldValueMappingId = alertFieldValueMappingId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getAlertDocumentId() {
        return alertDocumentId;
    }

    public void setAlertDocumentId(String alertDocumentId) {
        this.alertDocumentId = alertDocumentId;
    }

    public AlertDocument getAlertDocument() {
        return alertDocument;
    }

    public void setAlertDocument(AlertDocument alertDocument) {
        this.alertDocument = alertDocument;
    }

    public boolean isBooFieldValue() {
        return booFieldValue;
    }

    public void setBooFieldValue(boolean booFieldValue) {
        this.booFieldValue = booFieldValue;
    }
}
