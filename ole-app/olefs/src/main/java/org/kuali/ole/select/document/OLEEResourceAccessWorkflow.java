package org.kuali.ole.select.document;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by chenchulakshmig on 11/10/14.
 */
public class OLEEResourceAccessWorkflow extends PersistableBusinessObjectBase {

    private String description;
    private String status;
    private Timestamp lastApproved;
    private String currentOwner;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastApproved() {
        return lastApproved;
    }

    public void setLastApproved(Timestamp lastApproved) {
        this.lastApproved = lastApproved;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }
}
