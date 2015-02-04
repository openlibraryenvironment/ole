package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/8/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessFilterCriteriaBo extends PersistableBusinessObjectBase {

    private String fieldId;
    private String fieldDisplayName;
    private String fieldName;
    private String fieldType;

    private boolean activeIndicator;
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
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

    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }
}
