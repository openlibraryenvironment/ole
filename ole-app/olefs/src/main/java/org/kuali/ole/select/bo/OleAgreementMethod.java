package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleAgreementMethod is business object class for Agreement Method Maintenance Document
 */
public class OleAgreementMethod extends PersistableBusinessObjectBase {

    private String agreementMethodId;
    private String agreementMethodName;
    private String agreementMethodDesc;
    private boolean active;

    /**
     * Gets the agreementMethodId attribute.
     *
     * @return Returns the agreementMethodId
     */
    public String getAgreementMethodId() {
        return agreementMethodId;
    }

    /**
     * Sets the agreementMethodId attribute value.
     *
     * @param agreementMethodId The agreementMethodId to set.
     */
    public void setAgreementMethodId(String agreementMethodId) {
        this.agreementMethodId = agreementMethodId;
    }

    /**
     * Gets the agreementMethodName attribute.
     *
     * @return Returns the agreementMethodName
     */
    public String getAgreementMethodName() {
        return agreementMethodName;
    }

    /**
     * Sets the agreementMethodName attribute value.
     *
     * @param agreementMethodName The agreementMethodName to set.
     */
    public void setAgreementMethodName(String agreementMethodName) {
        this.agreementMethodName = agreementMethodName;
    }

    /**
     * Gets the agreementMethodDesc attribute value.
     *
     * @return agreementMethodDesc
     */
    public String getAgreementMethodDesc() {
        return agreementMethodDesc;
    }

    /**
     * Sets the agreementMethodDesc attribute value.
     *
     * @param agreementMethodDesc
     */
    public void setAgreementMethodDesc(String agreementMethodDesc) {
        this.agreementMethodDesc = agreementMethodDesc;
    }

    /**
     * Gets the active attribute.
     *
     * @return Returns the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active attribute value.
     *
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("agreementMethodId", agreementMethodId);
        toStringMap.put("agreementMethodName", agreementMethodName);
        toStringMap.put("agreementMethodDesc", agreementMethodDesc);
        toStringMap.put("active", active);
        return toStringMap;
    }

}
