package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleAgreementType is business object class for Agreement Type Maintenance Document
 */
public class OleAgreementType extends PersistableBusinessObjectBase {

    private String agreementTypeId;
    private String agreementTypeName;
    private String agreementTypeDesc;
    private boolean active;

    /**
     * Gets the agreementTypeId attribute.
     *
     * @return Returns the agreementTypeId
     */
    public String getAgreementTypeId() {
        return agreementTypeId;
    }

    /**
     * Sets the agreementTypeId attribute value.
     *
     * @param agreementTypeId The agreementTypeId to set.
     */
    public void setAgreementTypeId(String agreementTypeId) {
        this.agreementTypeId = agreementTypeId;
    }

    /**
     * Gets the agreementTypeName attribute.
     *
     * @return Returns the agreementTypeName
     */
    public String getAgreementTypeName() {
        return agreementTypeName;
    }

    /**
     * Sets the agreementTypeName attribute value.
     *
     * @param agreementTypeName The agreementTypeName to set.
     */
    public void setAgreementTypeName(String agreementTypeName) {
        this.agreementTypeName = agreementTypeName;
    }

    /**
     * Gets the attribute agreementTypeDesc
     *
     * @return Return the agreementTypeDesc
     */
    public String getAgreementTypeDesc() {
        return agreementTypeDesc;
    }

    /**
     * Sets the agreementTypeDesc attribute value
     *
     * @param agreementTypeDesc The agreementTypeDesc to set
     */
    public void setAgreementTypeDesc(String agreementTypeDesc) {
        this.agreementTypeDesc = agreementTypeDesc;
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
        toStringMap.put("agreementTypeId", agreementTypeId);
        toStringMap.put("agreementTypeName", agreementTypeName);
        toStringMap.put("agreementTypeDesc", agreementTypeDesc);
        toStringMap.put("active", active);
        return toStringMap;
    }

}
