package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/19/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestPriority extends PersistableBusinessObjectBase {
    private String oleRequestPriorityId;
    private String oleRequestPriorityName;
    private String oleRequestPriorityDescription;
    private boolean active;

    public String getOleRequestPriorityId() {
        return oleRequestPriorityId;
    }

    public void setOleRequestPriorityId(String oleRequestPriorityId) {
        this.oleRequestPriorityId = oleRequestPriorityId;
    }

    public String getOleRequestPriorityName() {
        return oleRequestPriorityName;
    }

    public void setOleRequestPriorityName(String oleRequestPriorityName) {
        this.oleRequestPriorityName = oleRequestPriorityName;
    }

    public String getOleRequestPriorityDescription() {
        return oleRequestPriorityDescription;
    }

    public void setOleRequestPriorityDescription(String oleRequestPriorityDescription) {
        this.oleRequestPriorityDescription = oleRequestPriorityDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

