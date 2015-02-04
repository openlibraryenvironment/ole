package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/18/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMaterialType extends PersistableBusinessObjectBase {
    private String oleMaterialTypeId;
    private String oleMaterialTypeName;
    private String oleMaterialTypeDescription;
    private boolean active;

    public String getOleMaterialTypeId() {
        return oleMaterialTypeId;
    }

    public void setOleMaterialTypeId(String oleMaterialTypeId) {
        this.oleMaterialTypeId = oleMaterialTypeId;
    }

    public String getOleMaterialTypeName() {
        return oleMaterialTypeName;
    }

    public void setOleMaterialTypeName(String oleMaterialTypeName) {
        this.oleMaterialTypeName = oleMaterialTypeName;
    }

    public String getOleMaterialTypeDescription() {
        return oleMaterialTypeDescription;
    }

    public void setOleMaterialTypeDescription(String oleMaterialTypeDescription) {
        this.oleMaterialTypeDescription = oleMaterialTypeDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
