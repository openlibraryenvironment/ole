package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/19/13
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPaymentType extends PersistableBusinessObjectBase {
    private String olePaymentTypeId;
    private String olePaymentTypeName;
    private String olePaymentTypeDescription;
    private boolean active;

    public String getOlePaymentTypeId() {
        return olePaymentTypeId;
    }

    public void setOlePaymentTypeId(String olePaymentTypeId) {
        this.olePaymentTypeId = olePaymentTypeId;
    }

    public String getOlePaymentTypeName() {
        return olePaymentTypeName;
    }

    public void setOlePaymentTypeName(String olePaymentTypeName) {
        this.olePaymentTypeName = olePaymentTypeName;
    }

    public String getOlePaymentTypeDescription() {
        return olePaymentTypeDescription;
    }

    public void setOlePaymentTypeDescription(String olePaymentTypeDescription) {
        this.olePaymentTypeDescription = olePaymentTypeDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
