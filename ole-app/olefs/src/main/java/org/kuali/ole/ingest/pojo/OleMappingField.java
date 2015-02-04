package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * OleMappingField is business object class for Overlay Action Maintenance Document
 */
public class OleMappingField extends PersistableBusinessObjectBase {

    private String id;
    private String fileFormat;
    private String incomingField;
    private String incomingFieldValue;

    private String overlayActionId;
    private OleOverlayAction oleOverlayAction = new OleOverlayAction();

    public OleOverlayAction getOleOverlayAction() {
        return oleOverlayAction;
    }

    public void setOleOverlayAction(OleOverlayAction oleOverlayAction) {
        this.oleOverlayAction = oleOverlayAction;
    }

    public String getOverlayActionId() {
        return overlayActionId;
    }

    public void setOverlayActionId(String overlayActionId) {
        this.overlayActionId = overlayActionId;
    }

    public String getIncomingField() {
        return incomingField;
    }

    public void setIncomingField(String incomingField) {
        this.incomingField = incomingField;
    }

    public String getIncomingFieldValue() {
        return incomingFieldValue;
    }

    public void setIncomingFieldValue(String incomingFieldValue) {
        this.incomingFieldValue = incomingFieldValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }


}
