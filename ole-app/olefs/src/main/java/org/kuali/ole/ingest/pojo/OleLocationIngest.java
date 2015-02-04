package org.kuali.ole.ingest.pojo;

/**
 * OleLocationIngest is a business object class for Location Document
 */
public class OleLocationIngest {

    private String locationCode;
    private String locationName;
    private String locationLevelCode;
    private String parentLocationCode;
    private String locationId;
    /**
     * Gets the locationId attribute.
     * @return  Returns the locationId.
     */
    public String getLocationId() {
        return locationId;
    }
    /**
     * Sets the locationId attribute value.
     * @param locationId The locationId to set.
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    /**
     * Gets the locationCode attribute.
     * @return  Returns the locationCode.
     */
    public String getLocationCode() {

        return locationCode;
    }
    /**
     * Sets the locationCode attribute value.
     * @param locationCode The locationCode to set.
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    /**
     * Gets the locationName attribute.
     * @return  Returns the locationName.
     */
    public String getLocationName() {
        return locationName;
    }
    /**
     * Sets the locationName attribute value.
     * @param locationName The locationName to set.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    /**
     * Gets the locationLevelCode attribute.
     * @return  Returns the locationLevelCode.
     */
    public String getLocationLevelCode() {
        return locationLevelCode;
    }
    /**
     * Sets the locationLevelCode attribute value.
     * @param locationLevelCode The locationLevelCode to set.
     */
    public void setLocationLevelCode(String locationLevelCode) {
        this.locationLevelCode = locationLevelCode;
    }
    /**
     * Gets the parentLocationCode attribute.
     * @return  Returns the parentLocationCode.
     */
    public String getParentLocationCode() {
        return parentLocationCode;
    }
    /**
     * Sets the parentLocationCode attribute value.
     * @param parentLocationCode The parentLocationCode to set.
     */
    public void setParentLocationCode(String parentLocationCode) {
        this.parentLocationCode = parentLocationCode;
    }
}
