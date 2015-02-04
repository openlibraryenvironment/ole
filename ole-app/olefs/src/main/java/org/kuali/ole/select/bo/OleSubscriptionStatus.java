package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by jating on 22/9/14.
 * Table for Ole Subscription Status
 */

public class OleSubscriptionStatus extends PersistableBusinessObjectBase {

    private String subscriptionStatusId;

    private String subscriptionStatusCode;

    private String subscriptionStatusName;

    private String subscriptionStatusDescription;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSubscriptionStatusId() {
        return subscriptionStatusId;
    }

    public void setSubscriptionStatusId(String subscriptionStatusId) {
        this.subscriptionStatusId = subscriptionStatusId;
    }

    public String getSubscriptionStatusCode() {
        return subscriptionStatusCode;
    }

    public void setSubscriptionStatusCode(String subscriptionStatusCode) {
        this.subscriptionStatusCode = subscriptionStatusCode;
    }

    public String getSubscriptionStatusName() {
        return subscriptionStatusName;
    }

    public void setSubscriptionStatusName(String subscriptionStatusName) {
        this.subscriptionStatusName = subscriptionStatusName;
    }

    public String getSubscriptionStatusDescription() {
        return subscriptionStatusDescription;
    }

    public void setSubscriptionStatusDescription(String subscriptionStatusDescription) {
        this.subscriptionStatusDescription = subscriptionStatusDescription;
    }
}
