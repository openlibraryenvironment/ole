package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * OleOutputFieldMapping is business object class for Overlay Action Maintenance Document
 */
public class OleOutputFieldMapping extends PersistableBusinessObjectBase {

    private String id;
    private String fieldName;
    private String fieldValue;
    private String targetField;
    private String overlayActionId;
    private boolean lookUp;

    public boolean isLookUp() {
        return lookUp;
    }

    public void setLookUp(boolean lookUp) {
        this.lookUp = lookUp;
    }

    private OleOverlayAction oleOverlayAction = new OleOverlayAction();

    public OleOverlayAction getOleOverlayAction() {
        return oleOverlayAction;
    }

    public void setOleOverlayAction(OleOverlayAction oleOverlayAction) {
        this.oleOverlayAction = oleOverlayAction;
    }

    private String targetObject;
    private String detailedTargetObject;

    public String getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public String getDetailedTargetObject() {
        return detailedTargetObject;
    }

    public void setDetailedTargetObject(String detailedTargetObject) {
        this.detailedTargetObject = detailedTargetObject;
    }

    public String getOverlayActionId() {
        return overlayActionId;
    }

    public void setOverlayActionId(String overlayActionId) {
        this.overlayActionId = overlayActionId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
