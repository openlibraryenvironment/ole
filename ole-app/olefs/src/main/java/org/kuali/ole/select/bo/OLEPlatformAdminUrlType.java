package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * Created by chenchulakshmig on 9/12/14.
 * OLEPlatformAdminUrlType is business object class for Platform Admin Url Type Maintenance Document
 */

public class OLEPlatformAdminUrlType extends PersistableBusinessObjectBase {

    private String platformAdminUrlTypeId;

    private String platformAdminUrlTypeName;

    private String platformAdminUrlTypeDesc;

    private boolean active;

    public String getPlatformAdminUrlTypeId() {
        return platformAdminUrlTypeId;
    }

    public void setPlatformAdminUrlTypeId(String platformAdminUrlTypeId) {
        this.platformAdminUrlTypeId = platformAdminUrlTypeId;
    }

    public String getPlatformAdminUrlTypeName() {
        return platformAdminUrlTypeName;
    }

    public void setPlatformAdminUrlTypeName(String platformAdminUrlTypeName) {
        this.platformAdminUrlTypeName = platformAdminUrlTypeName;
    }

    public String getPlatformAdminUrlTypeDesc() {
        return platformAdminUrlTypeDesc;
    }

    public void setPlatformAdminUrlTypeDesc(String platformAdminUrlTypeDesc) {
        this.platformAdminUrlTypeDesc = platformAdminUrlTypeDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
