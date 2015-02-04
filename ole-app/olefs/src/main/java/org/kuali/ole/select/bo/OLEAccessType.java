package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/19/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAccessType extends PersistableBusinessObjectBase {
    private String oleAccessTypeId;
    private String oleAccessTypeName;
    private String oleAccessTypeDescription;
    private boolean active;

    public String getOleAccessTypeId() {
        return oleAccessTypeId;
    }

    public void setOleAccessTypeId(String oleAccessTypeId) {
        this.oleAccessTypeId = oleAccessTypeId;
    }

    public String getOleAccessTypeName() {
        return oleAccessTypeName;
    }

    public void setOleAccessTypeName(String oleAccessTypeName) {
        this.oleAccessTypeName = oleAccessTypeName;
    }

    public String getOleAccessTypeDescription() {
        return oleAccessTypeDescription;
    }

    public void setOleAccessTypeDescription(String oleAccessTypeDescription) {
        this.oleAccessTypeDescription = oleAccessTypeDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
