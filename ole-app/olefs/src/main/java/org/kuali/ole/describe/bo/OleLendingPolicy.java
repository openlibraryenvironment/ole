package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * OleLendingPolicy is business object class for Lending Policy Maintenance Document
 */
public class OleLendingPolicy extends PersistableBusinessObjectBase {
    private Integer lendingPolicyId;
    private String lendingPolicyCode;
    private String lendingPolicyName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the lendingPolicyId attribute.
     *
     * @return Returns the lendingPolicyId
     */
    public Integer getLendingPolicyId() {
        return lendingPolicyId;
    }

    /**
     * Sets the lendingPolicyId attribute value.
     *
     * @param lendingPolicyId The lendingPolicyId to set.
     */
    public void setLendingPolicyId(Integer lendingPolicyId) {
        this.lendingPolicyId = lendingPolicyId;
    }

    /**
     * Gets the lendingPolicyCode attribute.
     *
     * @return Returns the lendingPolicyCode
     */
    public String getLendingPolicyCode() {
        return lendingPolicyCode;
    }

    /**
     * Sets the lendingPolicyCode attribute value.
     *
     * @param lendingPolicyCode The lendingPolicyCode to set.
     */
    public void setLendingPolicyCode(String lendingPolicyCode) {
        this.lendingPolicyCode = lendingPolicyCode;
    }

    /**
     * Gets the lendingPolicyName attribute.
     *
     * @return Returns the lendingPolicyName
     */
    public String getLendingPolicyName() {
        return lendingPolicyName;
    }

    /**
     * Sets the lendingPolicyName attribute value.
     *
     * @param lendingPolicyName The lendingPolicyName to set.
     */
    public void setLendingPolicyName(String lendingPolicyName) {
        this.lendingPolicyName = lendingPolicyName;
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
        toStringMap.put("lendingPolicyId", lendingPolicyId);
        return toStringMap;
    }

}
