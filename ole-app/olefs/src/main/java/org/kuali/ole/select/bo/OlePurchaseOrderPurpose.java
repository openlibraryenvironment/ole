package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by jating on 22/9/14.
 * Table for Ole Purchase Order Purpose
 */
public class OlePurchaseOrderPurpose extends PersistableBusinessObjectBase {

    private String purchaseOrderPurposeId;

    private String purchaseOrderPurposeCode;

    private String purchaseOrderPurposeName;

    private String purchaseOrderPurposeDescription;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPurchaseOrderPurposeId() {
        return purchaseOrderPurposeId;
    }

    public void setPurchaseOrderPurposeId(String purchaseOrderPurposeId) {
        this.purchaseOrderPurposeId = purchaseOrderPurposeId;
    }

    public String getPurchaseOrderPurposeCode() {
        return purchaseOrderPurposeCode;
    }

    public void setPurchaseOrderPurposeCode(String purchaseOrderPurposeCode) {
        this.purchaseOrderPurposeCode = purchaseOrderPurposeCode;
    }

    public String getPurchaseOrderPurposeName() {
        return purchaseOrderPurposeName;
    }

    public void setPurchaseOrderPurposeName(String purchaseOrderPurposeName) {
        this.purchaseOrderPurposeName = purchaseOrderPurposeName;
    }

    public String getPurchaseOrderPurposeDescription() {
        return purchaseOrderPurposeDescription;
    }

    public void setPurchaseOrderPurposeDescription(String purchaseOrderPurposeDescription) {
        this.purchaseOrderPurposeDescription = purchaseOrderPurposeDescription;
    }
}

