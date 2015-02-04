package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by chenchulakshmig on 11/10/14.
 */
public class OLEMarcRecordSourceType extends PersistableBusinessObjectBase {

    private String marcRecordSourceTypeId;

    private String marcRecordSourceTypeName;

    private String marcRecordSourceTypeDesc;

    private boolean active;

    public String getMarcRecordSourceTypeId() {
        return marcRecordSourceTypeId;
    }

    public void setMarcRecordSourceTypeId(String marcRecordSourceTypeId) {
        this.marcRecordSourceTypeId = marcRecordSourceTypeId;
    }

    public String getMarcRecordSourceTypeName() {
        return marcRecordSourceTypeName;
    }

    public void setMarcRecordSourceTypeName(String marcRecordSourceTypeName) {
        this.marcRecordSourceTypeName = marcRecordSourceTypeName;
    }

    public String getMarcRecordSourceTypeDesc() {
        return marcRecordSourceTypeDesc;
    }

    public void setMarcRecordSourceTypeDesc(String marcRecordSourceTypeDesc) {
        this.marcRecordSourceTypeDesc = marcRecordSourceTypeDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
