package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleReproductionPolicy is business object class for Reproduction Policy Maintenance Document
 */
public class OleReproductionPolicy extends PersistableBusinessObjectBase {

    private Integer reproductionPolicyId;
    private String reproductionPolicyCode;
    private String reproductionPolicyName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the reproductionPolicyId attribute.
     *
     * @return Returns the reproductionPolicyId
     */
    public Integer getReproductionPolicyId() {
        return reproductionPolicyId;
    }

    /**
     * Sets the reproductionPolicyId attribute value.
     *
     * @param reproductionPolicyId The reproductionPolicyId to set.
     */
    public void setReproductionPolicyId(Integer reproductionPolicyId) {
        this.reproductionPolicyId = reproductionPolicyId;
    }

    /**
     * Gets the reproductionPolicyCode attribute.
     *
     * @return Returns the reproductionPolicyCode
     */
    public String getReproductionPolicyCode() {
        return reproductionPolicyCode;
    }

    /**
     * Sets the reproductionPolicyCode attribute value.
     *
     * @param reproductionPolicyCode The reproductionPolicyCode to set.
     */
    public void setReproductionPolicyCode(String reproductionPolicyCode) {
        this.reproductionPolicyCode = reproductionPolicyCode;
    }

    /**
     * Gets the reproductionPolicyName attribute.
     *
     * @return Returns the reproductionPolicyName
     */
    public String getReproductionPolicyName() {
        return reproductionPolicyName;
    }

    /**
     * Sets the reproductionPolicyName attribute value.
     *
     * @param reproductionPolicyName The reproductionPolicyName to set.
     */
    public void setReproductionPolicyName(String reproductionPolicyName) {
        this.reproductionPolicyName = reproductionPolicyName;
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
        toStringMap.put("reproductionPolicyId", reproductionPolicyId);
        return toStringMap;
    }
}
