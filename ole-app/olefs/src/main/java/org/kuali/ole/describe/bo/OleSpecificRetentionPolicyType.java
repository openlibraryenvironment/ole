package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleSpecificRetentionPolicyType is business object class for Specific Retention Policy Type Maintenance Document
 */
public class OleSpecificRetentionPolicyType extends PersistableBusinessObjectBase {

    private Integer specificRetentionPolicyTypeId;
    private String specificRetentionPolicyTypeCode;
    private String specificRetentionPolicyTypeName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the specificRetentionPolicyTypeId attribute.
     *
     * @return Returns the specificRetentionPolicyTypeId
     */
    public Integer getSpecificRetentionPolicyTypeId() {
        return specificRetentionPolicyTypeId;
    }

    /**
     * Sets the specificRetentionPolicyTypeId attribute value.
     *
     * @param specificRetentionPolicyTypeId The specificRetentionPolicyTypeId to set.
     */
    public void setSpecificRetentionPolicyTypeId(Integer specificRetentionPolicyTypeId) {
        this.specificRetentionPolicyTypeId = specificRetentionPolicyTypeId;
    }

    /**
     * Gets the specificRetentionPolicyTypeCode attribute.
     *
     * @return Returns the specificRetentionPolicyTypeCode
     */
    public String getSpecificRetentionPolicyTypeCode() {
        return specificRetentionPolicyTypeCode;
    }

    /**
     * Sets the specificRetentionPolicyTypeCode attribute value.
     *
     * @param specificRetentionPolicyTypeCode
     *         The specificRetentionPolicyTypeCode to set.
     */
    public void setSpecificRetentionPolicyTypeCode(String specificRetentionPolicyTypeCode) {
        this.specificRetentionPolicyTypeCode = specificRetentionPolicyTypeCode;
    }

    /**
     * Gets the specificRetentionPolicyTypeName attribute.
     *
     * @return Returns the specificRetentionPolicyTypeName
     */
    public String getSpecificRetentionPolicyTypeName() {
        return specificRetentionPolicyTypeName;
    }

    /**
     * Sets the specificRetentionPolicyTypeName attribute value.
     *
     * @param specificRetentionPolicyTypeName
     *         The specificRetentionPolicyTypeName to set.
     */
    public void setSpecificRetentionPolicyTypeName(String specificRetentionPolicyTypeName) {
        this.specificRetentionPolicyTypeName = specificRetentionPolicyTypeName;
    }

    /**
     * Gets the source attribute.
     *
     * @return Returns the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source attribute value.
     *
     * @param source The source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the sourceDate attribute.
     *
     * @return Returns the sourceDate
     */
    public Date getSourceDate() {
        return sourceDate;
    }

    /**
     * Sets the sourceDate attribute value.
     *
     * @param sourceDate The sourceDate to set.
     */
    public void setSourceDate(Date sourceDate) {
        this.sourceDate = sourceDate;
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
        toStringMap.put("specificRetentionPolicyTypeId", specificRetentionPolicyTypeId);
        return toStringMap;
    }

}
