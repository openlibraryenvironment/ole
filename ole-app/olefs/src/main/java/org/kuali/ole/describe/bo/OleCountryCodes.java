package org.kuali.ole.describe.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleCountryCodes is business object class for Country Codes Maintenance Document
 */
public class OleCountryCodes extends PersistableBusinessObjectBase {
    private Integer countryCodeId;
    private String countryCode;
    private String countryName;
    private String countryRegionName;
    private String countryNameSequence;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the countryCodeId attribute.
     *
     * @return Returns the countryCodeId
     */
    public Integer getCountryCodeId() {
        return countryCodeId;
    }

    /**
     * Sets the countryCodeId attribute value.
     *
     * @param countryCodeId The countryCodeId to set.
     */
    public void setCountryCodeId(Integer countryCodeId) {
        this.countryCodeId = countryCodeId;
    }

    /**
     * Gets the countryCode attribute.
     *
     * @return Returns the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the countryCode attribute value.
     *
     * @param countryCode The countryCode to set.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the countryName attribute.
     *
     * @return Returns the countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the countryName attribute value.
     *
     * @param countryName The countryName to set.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Gets the countryRegionName attribute.
     *
     * @return Returns the countryRegionName
     */
    public String getCountryRegionName() {
        return countryRegionName;
    }

    /**
     * Sets the countryRegionName attribute value.
     *
     * @param countryRegionName The countryRegionName to set.
     */
    public void setCountryRegionName(String countryRegionName) {
        this.countryRegionName = countryRegionName;
    }

    /**
     * Gets the countryNameSequence attribute.
     *
     * @return Returns the countryNameSequence
     */
    public String getCountryNameSequence() {
        return countryNameSequence;
    }

    /**
     * Sets the countryNameSequence attribute value.
     *
     * @param countryNameSequence The countryNameSequence to set.
     */
    public void setCountryNameSequence(String countryNameSequence) {
        this.countryNameSequence = countryNameSequence;
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
        toStringMap.put("countryCodeId", countryCodeId);
        return toStringMap;
    }
}
