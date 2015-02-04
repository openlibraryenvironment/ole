package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleGeneralRetentionPolicy is business object class for General Retention Policy Maintenance Document
 */
public class OleGeneralRetentionPolicy extends PersistableBusinessObjectBase {

    private Integer generalRetentionPolicyId;
    private String generalRetentionPolicyCode;
    private String generalRetentionPolicyName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the generalRetentionPolicyId attribute.
     *
     * @return Returns the generalRetentionPolicyId
     */
    public Integer getGeneralRetentionPolicyId() {
        return generalRetentionPolicyId;
    }

    /**
     * Sets the generalRetentionPolicyId attribute value.
     *
     * @param generalRetentionPolicyId The generalRetentionPolicyId to set.
     */
    public void setGeneralRetentionPolicyId(Integer generalRetentionPolicyId) {
        this.generalRetentionPolicyId = generalRetentionPolicyId;
    }

    /**
     * Gets the generalRetentionPolicyCode attribute.
     *
     * @return Returns the generalRetentionPolicyCode
     */
    public String getGeneralRetentionPolicyCode() {
        return generalRetentionPolicyCode;
    }

    /**
     * Sets the generalRetentionPolicyCode attribute value.
     *
     * @param generalRetentionPolicyCode The generalRetentionPolicyCode to set.
     */
    public void setGeneralRetentionPolicyCode(String generalRetentionPolicyCode) {
        this.generalRetentionPolicyCode = generalRetentionPolicyCode;
    }

    /**
     * Gets the generalRetentionPolicyName attribute.
     *
     * @return Returns the generalRetentionPolicyName
     */
    public String getGeneralRetentionPolicyName() {
        return generalRetentionPolicyName;
    }

    /**
     * Sets the generalRetentionPolicyName attribute value.
     *
     * @param generalRetentionPolicyName The generalRetentionPolicyName to set.
     */
    public void setGeneralRetentionPolicyName(String generalRetentionPolicyName) {
        this.generalRetentionPolicyName = generalRetentionPolicyName;
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
        toStringMap.put("generalRetentionPolicyId", generalRetentionPolicyId);
        return toStringMap;
    }
}
