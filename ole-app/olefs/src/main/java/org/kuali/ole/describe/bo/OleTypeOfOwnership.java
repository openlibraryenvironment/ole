package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleTypeOfOwnership is business object class for Type Of Ownership Maintenance Document
 */
public class OleTypeOfOwnership extends PersistableBusinessObjectBase {

    private Integer typeOfOwnershipId;
    private String typeOfOwnershipCode;
    private String typeOfOwnershipName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the typeOfOwnershipId attribute.
     *
     * @return Returns the typeOfOwnershipId
     */
    public Integer getTypeOfOwnershipId() {
        return typeOfOwnershipId;
    }

    /**
     * Sets the typeOfOwnershipId attribute value.
     *
     * @param typeOfOwnershipId The typeOfOwnershipId to set.
     */
    public void setTypeOfOwnershipId(Integer typeOfOwnershipId) {
        this.typeOfOwnershipId = typeOfOwnershipId;
    }

    /**
     * Gets the typeOfOwnershipCode attribute.
     *
     * @return Returns the typeOfOwnershipCode
     */
    public String getTypeOfOwnershipCode() {
        return typeOfOwnershipCode;
    }

    /**
     * Sets the typeOfOwnershipCode attribute value.
     *
     * @param typeOfOwnershipCode The typeOfOwnershipCode to set.
     */
    public void setTypeOfOwnershipCode(String typeOfOwnershipCode) {
        this.typeOfOwnershipCode = typeOfOwnershipCode;
    }

    /**
     * Gets the typeOfOwnershipName attribute.
     *
     * @return Returns the typeOfOwnershipName
     */
    public String getTypeOfOwnershipName() {
        return typeOfOwnershipName;
    }

    /**
     * Sets the typeOfOwnershipName attribute value.
     *
     * @param typeOfOwnershipName The typeOfOwnershipName to set.
     */
    public void setTypeOfOwnershipName(String typeOfOwnershipName) {
        this.typeOfOwnershipName = typeOfOwnershipName;
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
        toStringMap.put("typeOfOwnershipId", typeOfOwnershipId);
        return toStringMap;
    }

}
