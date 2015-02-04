package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleItemAvailableStatus is business object class for Item Available Status Maintenance Document
 */
public class OleItemAvailableStatus extends PersistableBusinessObjectBase {

    private String itemAvailableStatusId;
    private String itemAvailableStatusCode;
    private String itemAvailableStatusName;
    private boolean active;


    public String getItemAvailableStatusId() {
        return itemAvailableStatusId;
    }

    public void setItemAvailableStatusId(String itemAvailableStatusId) {
        this.itemAvailableStatusId = itemAvailableStatusId;
    }

    public String getItemAvailableStatusCode() {
        return itemAvailableStatusCode;
    }

    public void setItemAvailableStatusCode(String itemAvailableStatusCode) {
        this.itemAvailableStatusCode = itemAvailableStatusCode;
    }

    public String getItemAvailableStatusName() {
        return itemAvailableStatusName;
    }

    public void setItemAvailableStatusName(String itemAvailableStatusName) {
        this.itemAvailableStatusName = itemAvailableStatusName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("itemAvailableStatusId", itemAvailableStatusId);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
