package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleElectronicLocationAndAccessRelationship is  business object class for Electronic Location And Access Relationship Maintenance Document
 */
public class OleElectronicLocationAndAccessRelationship extends PersistableBusinessObjectBase {
    private Integer elaRelationshipId;
    private String elaRelationshipCode;
    private String elaRelationshipName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the elaRelationshipId attribute.
     *
     * @return Returns the elaRelationshipId
     */
    public Integer getElaRelationshipId() {
        return elaRelationshipId;
    }

    /**
     * Sets the elaRelationshipId attribute value.
     *
     * @param elaRelationshipId The elaRelationshipId to set.
     */
    public void setElaRelationshipId(Integer elaRelationshipId) {
        this.elaRelationshipId = elaRelationshipId;
    }

    /**
     * Gets the elaRelationshipCode attribute.
     *
     * @return Returns the elaRelationshipCode
     */
    public String getElaRelationshipCode() {
        return elaRelationshipCode;
    }

    /**
     * Sets the elaRelationshipCode attribute value.
     *
     * @param elaRelationshipCode The elaRelationshipCode to set.
     */
    public void setElaRelationshipCode(String elaRelationshipCode) {
        this.elaRelationshipCode = elaRelationshipCode;
    }

    /**
     * Gets the elaRelationshipName attribute.
     *
     * @return Returns the elaRelationshipName
     */
    public String getElaRelationshipName() {
        return elaRelationshipName;
    }

    /**
     * Sets the elaRelationshipName attribute value.
     *
     * @param elaRelationshipName The elaRelationshipName to set.
     */
    public void setElaRelationshipName(String elaRelationshipName) {
        this.elaRelationshipName = elaRelationshipName;
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
        toStringMap.put("elaRelationshipId", elaRelationshipId);
        return toStringMap;
    }
}
