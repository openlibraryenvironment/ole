package org.kuali.ole.gobi;

import org.kuali.ole.gobi.datobjects.PurchaseOrder;

public class GobiRequest {
    private PurchaseOrder purchaseOrder;
    private String profileIdForDefaultMapping;

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getProfileIdForDefaultMapping() {
        return profileIdForDefaultMapping;
    }

    public void setProfileIdForDefaultMapping(String profileIdForDefaultMapping) {
        this.profileIdForDefaultMapping = profileIdForDefaultMapping;
    }
}
