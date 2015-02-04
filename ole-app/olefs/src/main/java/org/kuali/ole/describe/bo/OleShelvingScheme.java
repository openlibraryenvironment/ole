package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleShelvingScheme is business object class for Shelving Scheme Maintenance Document
 */
public class OleShelvingScheme extends PersistableBusinessObjectBase {

    private Integer shelvingSchemeId;
    private String shelvingSchemeCode;
    private String shelvingSchemeName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the shelvingSchemeId attribute.
     *
     * @return Returns the shelvingSchemeId
     */
    public Integer getShelvingSchemeId() {
        return shelvingSchemeId;
    }

    /**
     * Sets the shelvingSchemeId attribute value.
     *
     * @param shelvingSchemeId The shelvingSchemeId to set.
     */
    public void setShelvingSchemeId(Integer shelvingSchemeId) {
        this.shelvingSchemeId = shelvingSchemeId;
    }

    /**
     * Gets the shelvingSchemeCode attribute.
     *
     * @return Returns the shelvingSchemeCode
     */
    public String getShelvingSchemeCode() {
        return shelvingSchemeCode;
    }

    /**
     * Sets the shelvingSchemeCode attribute value.
     *
     * @param shelvingSchemeCode The shelvingSchemeCode to set.
     */
    public void setShelvingSchemeCode(String shelvingSchemeCode) {
        this.shelvingSchemeCode = shelvingSchemeCode;
    }

    /**
     * Gets the shelvingSchemeName attribute.
     *
     * @return Returns the shelvingSchemeName
     */
    public String getShelvingSchemeName() {
        return shelvingSchemeName;
    }

    /**
     * Sets the shelvingSchemeName attribute value.
     *
     * @param shelvingSchemeName The shelvingSchemeName to set.
     */
    public void setShelvingSchemeName(String shelvingSchemeName) {
        this.shelvingSchemeName = shelvingSchemeName;
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
        toStringMap.put("shelvingSchemeId", shelvingSchemeId);
        return toStringMap;
    }

}
