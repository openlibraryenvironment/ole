package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;


public class OLEClaimType extends PersistableBusinessObjectBase {

    private String oleClaimTypeId;
    private String oleClaimTypeCode;
    private String oleClaimTypeName;
    private boolean active;


    public String getOleClaimTypeId() {
        return oleClaimTypeId;
    }

    public void setOleClaimTypeId(String oleClaimTypeId) {
        this.oleClaimTypeId = oleClaimTypeId;
    }

    public String getOleClaimTypeCode() {
        return oleClaimTypeCode;
    }

    public void setOleClaimTypeCode(String oleClaimTypeCode) {
        this.oleClaimTypeCode = oleClaimTypeCode;
    }

    public String getOleClaimTypeName() {
        return oleClaimTypeName;
    }

    public void setOleClaimTypeName(String oleClaimTypeName) {
        this.oleClaimTypeName = oleClaimTypeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("oleClaimTypeId", oleClaimTypeId);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
