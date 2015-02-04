package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by chenchulakshmig on 9/12/14.
 * OLEPlatformStatus is business object class for Platform Status Maintenance Document
 */

public class OLEPlatformStatus extends PersistableBusinessObjectBase {

    private String platformStatusId;

    private String platformStatusName;

    private String platformStatusDesc;

    private boolean active;

    public String getPlatformStatusId() {
        return platformStatusId;
    }

    public void setPlatformStatusId(String platformStatusId) {
        this.platformStatusId = platformStatusId;
    }

    public String getPlatformStatusName() {
        return platformStatusName;
    }

    public void setPlatformStatusName(String platformStatusName) {
        this.platformStatusName = platformStatusName;
    }

    public String getPlatformStatusDesc() {
        return platformStatusDesc;
    }

    public void setPlatformStatusDesc(String platformStatusDesc) {
        this.platformStatusDesc = platformStatusDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
