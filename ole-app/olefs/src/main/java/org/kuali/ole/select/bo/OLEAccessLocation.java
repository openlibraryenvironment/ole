package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/21/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAccessLocation extends PersistableBusinessObjectBase {

    private String oleAccessLocationId;
    private String oleAccessLocationName;
    private String oleAccessLocationDesc;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOleAccessLocationDesc() {
        return oleAccessLocationDesc;
    }

    public void setOleAccessLocationDesc(String oleAccessLocationDesc) {
        this.oleAccessLocationDesc = oleAccessLocationDesc;
    }

    public String getOleAccessLocationId() {
        return oleAccessLocationId;
    }

    public void setOleAccessLocationId(String oleAccessLocationId) {
        this.oleAccessLocationId = oleAccessLocationId;
    }

    public String getOleAccessLocationName() {
        return oleAccessLocationName;
    }

    public void setOleAccessLocationName(String oleAccessLocationName) {
        this.oleAccessLocationName = oleAccessLocationName;
    }
}
