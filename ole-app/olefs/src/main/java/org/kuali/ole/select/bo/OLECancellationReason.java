package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 12/9/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancellationReason extends PersistableBusinessObjectBase {

    private String cancelReasonId;
    private String cancelReasonName;
    private String cancelReasonText;
    private boolean active;

    public String getCancelReasonId() {
        return cancelReasonId;
    }

    public void setCancelReasonId(String cancelReasonId) {
        this.cancelReasonId = cancelReasonId;
    }

    public String getCancelReasonName() {
        return cancelReasonName;
    }

    public void setCancelReasonName(String cancelReasonName) {
        this.cancelReasonName = cancelReasonName;
    }

    public String getCancelReasonText() {
        return cancelReasonText;
    }

    public void setCancelReasonText(String cancelReasonText) {
        this.cancelReasonText = cancelReasonText;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("cancelReasonId", cancelReasonId);
        toStringMap.put("cancelReasonName", cancelReasonName);
        toStringMap.put("cancelReasonText", cancelReasonText);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
