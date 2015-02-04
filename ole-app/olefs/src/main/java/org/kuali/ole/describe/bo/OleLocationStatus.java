package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;


/**
 * OleLocationStatus is business object class for Location Status Maintenance Document
 */
public class OleLocationStatus extends PersistableBusinessObjectBase {

    private String locationStatusId;
    private String locationStatusCode;
    private String locationStatusName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the locationStatusId attribute.
     *
     * @return Returns the locationStatusId
     */
    public String getLocationStatusId() {
        return locationStatusId;
    }

    /**
     * Sets the locationStatusId attribute value.
     *
     * @param locationStatusId The locationStatusId to set.
     */
    public void setLocationStatusId(String locationStatusId) {
        this.locationStatusId = locationStatusId;
    }

    /**
     * Gets the locationStatusCode attribute.
     *
     * @return Returns the locationStatusCode
     */
    public String getLocationStatusCode() {
        return locationStatusCode;
    }

    /**
     * Sets the locationStatusCode attribute value.
     *
     * @param locationStatusCode The locationStatusCode to set.
     */
    public void setLocationStatusCode(String locationStatusCode) {
        this.locationStatusCode = locationStatusCode;
    }

    /**
     * Gets the locationStatusName attribute.
     *
     * @return Returns the locationStatusName
     */
    public String getLocationStatusName() {
        return locationStatusName;
    }

    /**
     * Sets the locationStatusName attribute value.
     *
     * @param locationStatusName The locationStatusName to set.
     */
    public void setLocationStatusName(String locationStatusName) {
        this.locationStatusName = locationStatusName;
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
        toStringMap.put("locationStatusId", locationStatusId);
        return toStringMap;
    }


}
