package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleSpecificRetentionPolicyTypeUnit is business object class for Specific Retention Policy Type Unit Maintenance Document
 */
public class OleSpecificRetentionPolicyTypeUnit extends PersistableBusinessObjectBase {

    private String specificPolicyUnitTypeId;
    private String specificPolicyUnitTypeCode;
    private String specificPolicyUnitTypeName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the specificPolicyUnitTypeId attribute.
     *
     * @return Returns the specificPolicyUnitTypeId
     */
    public String getSpecificPolicyUnitTypeId() {
        return specificPolicyUnitTypeId;
    }

    /**
     * Sets the specificPolicyUnitTypeId attribute value.
     *
     * @param specificPolicyUnitTypeId The specificPolicyUnitTypeId to set.
     */
    public void setSpecificPolicyUnitTypeId(String specificPolicyUnitTypeId) {
        this.specificPolicyUnitTypeId = specificPolicyUnitTypeId;
    }

    /**
     * Gets the specificPolicyUnitTypeCode attribute.
     *
     * @return Returns the specificPolicyUnitTypeCode
     */
    public String getSpecificPolicyUnitTypeCode() {
        return specificPolicyUnitTypeCode;
    }

    /**
     * Sets the specificPolicyUnitTypeCode attribute value.
     *
     * @param specificPolicyUnitTypeCode The specificPolicyUnitTypeCode to set.
     */
    public void setSpecificPolicyUnitTypeCode(String specificPolicyUnitTypeCode) {
        this.specificPolicyUnitTypeCode = specificPolicyUnitTypeCode;
    }

    /**
     * Gets the specificPolicyUnitTypeName attribute.
     *
     * @return Returns the specificPolicyUnitTypeName
     */
    public String getSpecificPolicyUnitTypeName() {
        return specificPolicyUnitTypeName;
    }

    /**
     * Sets the specificPolicyUnitTypeName attribute value.
     *
     * @param specificPolicyUnitTypeName The specificPolicyUnitTypeName to set.
     */
    public void setSpecificPolicyUnitTypeName(String specificPolicyUnitTypeName) {
        this.specificPolicyUnitTypeName = specificPolicyUnitTypeName;
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
        toStringMap.put("specificPolicyUnitTypeId", specificPolicyUnitTypeId);
        toStringMap.put("specificPolicyUnitTypeCode", specificPolicyUnitTypeCode);
        toStringMap.put("specificPolicyUnitTypeName", specificPolicyUnitTypeName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }

}















