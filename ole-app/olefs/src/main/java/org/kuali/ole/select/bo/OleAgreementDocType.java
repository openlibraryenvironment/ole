package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleAgreementDocType is  business object class for Agreement DocType Maintenance Document
 */
public class OleAgreementDocType extends PersistableBusinessObjectBase {

    private String agreementDocTypeId;
    private String agreementDocTypeName;
    private String agreementDocTypeDesc;
    private boolean active;

    /**
     * Gets the agreementDocTypeId attribute.
     *
     * @return Returns the agreementDlocTypeId
     */
    public String getAgreementDocTypeId() {
        return agreementDocTypeId;
    }

    /**
     * Sets the agreementDocTypeId attribute value.
     *
     * @param agreementDocTypeId The agreementDocTypeId to set.
     */
    public void setAgreementDocTypeId(String agreementDocTypeId) {
        this.agreementDocTypeId = agreementDocTypeId;
    }

    /**
     * Gets the agreementDocTypeName attribute.
     *
     * @return Returns the agreementDocTypeName
     */
    public String getAgreementDocTypeName() {
        return agreementDocTypeName;
    }

    /**
     * Sets the agreementDocTypeName attribute value.
     *
     * @param agreementDocTypeName The agreementDocTypeName to set.
     */
    public void setAgreementDocTypeName(String agreementDocTypeName) {
        this.agreementDocTypeName = agreementDocTypeName;
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
     * Gets the Agreement Description attribute
     *
     * @return Returns the agreementDocTypeDesc
     */
    public String getAgreementDocTypeDesc() {
        return agreementDocTypeDesc;
    }

    /**
     * Sets the agreementDesc attribute
     *
     * @param agreementDocTypeDesc
     */
    public void setAgreementDocTypeDesc(String agreementDocTypeDesc) {
        this.agreementDocTypeDesc = agreementDocTypeDesc;
    }


    /**
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("agreementDocTypeId", agreementDocTypeId);
        toStringMap.put("agreementDocTypeName", agreementDocTypeName);
        toStringMap.put("active", active);
        return toStringMap;
    }

}
