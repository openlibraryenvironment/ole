package org.kuali.ole.docstore.model.xmlpojo.work.bib.marc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an xml record of category:Work, Type:Bibliographic, Format:MARC.
 * User: tirumalesh.b
 * Date: 4/12/11
 * Time: 4:46 PM
 */
public class WorkBibMarcRecord {
    private String leader = null;
    private List<ControlField> controlFields = new ArrayList<ControlField>();
    private List<DataField> dataFields = new ArrayList<DataField>();

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<ControlField> getControlFields() {
        if (controlFields == null)
            controlFields = new ArrayList<ControlField>();
        return controlFields;
    }

    public void setControlFields(List<ControlField> controlFields) {
        this.controlFields = controlFields;
    }

    public List<DataField> getDataFields() {
        if (dataFields == null)
            dataFields = new ArrayList<DataField>();
        return dataFields;
    }

    public void setDataFields(List<DataField> dataFields) {
        this.dataFields = dataFields;
    }

    public void addMarcDataField(DataField marcDataField) {
        if (!this.dataFields.contains(marcDataField)) {
            this.dataFields.add(marcDataField);
        }
    }

    public DataField getDataFieldForTag(String tag) {
        for (DataField marcDataField : dataFields) {
            if (marcDataField.getTag().equalsIgnoreCase(tag)) {
                return marcDataField;
            }
        }
        return null;
    }


    public void addDataFields(DataField marcDataField) {
        if (!this.dataFields.contains(marcDataField)) {
            this.dataFields.add(marcDataField);
        }
    }

    public void addControlFields(ControlField controlField) {
        if (!this.controlFields.contains(controlField)) {
            this.controlFields.add(controlField);
        }
    }

}
