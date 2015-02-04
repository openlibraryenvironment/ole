package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by chenchulakshmig on 11/10/14.
 */
public class OLEMobileAccess extends PersistableBusinessObjectBase {

    private String mobileAccessId;

    private String mobileAccessName;

    private String mobileAccessDesc;

    private boolean active;

    public String getMobileAccessId() {
        return mobileAccessId;
    }

    public void setMobileAccessId(String mobileAccessId) {
        this.mobileAccessId = mobileAccessId;
    }

    public String getMobileAccessName() {
        return mobileAccessName;
    }

    public void setMobileAccessName(String mobileAccessName) {
        this.mobileAccessName = mobileAccessName;
    }

    public String getMobileAccessDesc() {
        return mobileAccessDesc;
    }

    public void setMobileAccessDesc(String mobileAccessDesc) {
        this.mobileAccessDesc = mobileAccessDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
