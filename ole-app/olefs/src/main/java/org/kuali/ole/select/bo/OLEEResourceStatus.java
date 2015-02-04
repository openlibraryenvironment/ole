package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/21/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceStatus extends PersistableBusinessObjectBase {

    private String oleEResourceStatusId;
    private String oleEResourceStatusName;
    private String oleEResourceStatusDesc;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOleEResourceStatusDesc() {
        return oleEResourceStatusDesc;
    }

    public void setOleEResourceStatusDesc(String oleEResourceStatusDesc) {
        this.oleEResourceStatusDesc = oleEResourceStatusDesc;
    }

    public String getOleEResourceStatusId() {
        return oleEResourceStatusId;
    }

    public void setOleEResourceStatusId(String oleEResourceStatusId) {
        this.oleEResourceStatusId = oleEResourceStatusId;
    }

    public String getOleEResourceStatusName() {
        return oleEResourceStatusName;
    }

    public void setOleEResourceStatusName(String oleEResourceStatusName) {
        this.oleEResourceStatusName = oleEResourceStatusName;
    }
}
