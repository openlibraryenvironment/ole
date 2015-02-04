package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleAgreementStatus is business object class for Agreement Status Maintenance Document
 */
public class OleAgreementStatus extends PersistableBusinessObjectBase {

    private String agreementStatusId;
    private String agreementStatusName;
    private String agreementStatusDesc;
    private boolean active;

    /**
     * Gets the agreementStatusId attribute.
     *
     * @return Returns the agreementStatusId
     */
    public String getAgreementStatusId() {
        return agreementStatusId;
    }

    /**
     * Sets the agreementStatusId attribute value.
     *
     * @param agreementStatusId The agreementStatusId to set.
     */
    public void setAgreementStatusId(String agreementStatusId) {
        this.agreementStatusId = agreementStatusId;
    }

    /**
     * Gets the agreementStatusName attribute.
     *
     * @return Returns the agreementStatusName
     */
    public String getAgreementStatusName() {
        return agreementStatusName;
    }

    /**
     * Sets the agreementStatusName attribute value.
     *
     * @param agreementStatusName The agreementStatusName to set.
     */
    public void setAgreementStatusName(String agreementStatusName) {
        this.agreementStatusName = agreementStatusName;
    }

    /**
     * Gets the agreementStatusDesc attribute
     *
     * @return Returns the agreementStatusDesc
     */
    public String getAgreementStatusDesc() {
        return agreementStatusDesc;
    }

    /**
     * Sets the agreementStatusDesc attribute value
     *
     * @param agreementStatusDesc The agreementStatusDesc to set
     */
    public void setAgreementStatusDesc(String agreementStatusDesc) {
        this.agreementStatusDesc = agreementStatusDesc;
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
        toStringMap.put("agreementStatusId", agreementStatusId);
        toStringMap.put("agreementStatusName", agreementStatusName);
        toStringMap.put("agreementStatusDesc", agreementStatusDesc);
        toStringMap.put("active", active);
        return toStringMap;
    }

}
