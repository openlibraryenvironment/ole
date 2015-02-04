package org.kuali.ole.ingest.pojo;

import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * OleOverlayAction is business object class for Overlay Action Maintenance Document
 */
public class OleOverlayAction extends PersistableBusinessObjectBase {

    private String overlayActionId;
    private String profileName;
    private String description;
    List<OleMappingField> oleMappingFields = new ArrayList<OleMappingField>();
    List<OleOutputFieldMapping> oleOutputFieldMappings =  new ArrayList<OleOutputFieldMapping>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOverlayActionId() {
        return overlayActionId;
    }

    public void setOverlayActionId(String overlayActionId) {
        this.overlayActionId = overlayActionId;
    }

    public List<OleMappingField> getOleMappingFields() {
        return oleMappingFields;
    }

    public void setOleMappingFields(List<OleMappingField> oleMappingFields) {
        this.oleMappingFields = oleMappingFields;
    }

    public List<OleOutputFieldMapping> getOleOutputFieldMappings() {
        return oleOutputFieldMappings;
    }

    public void setOleOutputFieldMappings(List<OleOutputFieldMapping> oleOutputFieldMappings) {
        this.oleOutputFieldMappings = oleOutputFieldMappings;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
