package org.kuali.ole.docstore.common.document.content.instance;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * The OleLocation is a BO class that defines the location fields with getters and setters which
 * is used for interacting the location data with the persistence layer in OLE.
 */
public class OleLocation extends PersistableBusinessObjectBase {

    private String locationId;
    private String locationCode;
    private String locationName;
    private String parentLocationId;
    private String levelId;
    private String levelCode;
    private OleLocation oleLocation;
    private OleLocationLevel oleLocationLevel;
    private String fullLocationPath;

    /**
     * Gets the levelCode string value from this OleLocation class
     *
     * @return levelCode
     */
    public String getLevelCode() {
        return oleLocationLevel.getLevelCode();
    }

    /**
     * Sets the levelCode string value inside this OleLocation class
     *
     * @param levelCode
     */
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * Gets the parentLocationId string value from this OleLocation class
     *
     * @return parentLocationId
     */
    public String getParentLocationId() {
        return parentLocationId;
    }

    /**
     * Sets the parentLocationId string value inside this OleLocation class
     *
     * @param parentLocationId
     */
    public void setParentLocationId(String parentLocationId) {
        if (parentLocationId != null && parentLocationId.equals(""))
            this.parentLocationId = null;
        else
            this.parentLocationId = parentLocationId;
    }

    /**
     * Gets the locationName string value from this OleLocation class
     *
     * @return locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the locationName string value inside this OleLocation class
     *
     * @param locationName
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Gets the locationCode string value from this OleLocation class
     *
     * @return locationCode
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * Sets the locationCode string value inside this OleLocation class
     *
     * @param locationCode
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    /**
     * Gets the locationId string value from this OleLocation class
     *
     * @return locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the locationId string value inside this OleLocation class
     *
     * @param locationId
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Gets the oleLocation object value from this OleLocation class
     *
     * @return oleLocation
     */
    public OleLocation getOleLocation() {
        return oleLocation;
    }

    /**
     * Sets the oleLocation value inside this OleLocation class
     *
     * @param oleLocation
     */
    public void setOleLocation(OleLocation oleLocation) {
        this.oleLocation = oleLocation;
    }

    /**
     * Gets the levelId string value from this OleLocation class
     *
     * @return levelId
     */
    public String getLevelId() {
        return levelId;
    }

    /**
     * Sets the levelId string value inside this OleLocation class
     *
     * @param levelId
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    /**
     * Gets the oleLocationLevel object value from this OleLocation class
     *
     * @return oleLocationLevel
     */
    public OleLocationLevel getOleLocationLevel() {
        return oleLocationLevel;
    }

    /**
     * Sets the oleLocationLevel value inside this OleLocation class
     *
     * @param oleLocationLevel
     */
    public void setOleLocationLevel(OleLocationLevel oleLocationLevel) {
        this.oleLocationLevel = oleLocationLevel;
    }

    /**
     * This method returns a map that contains the Primary Key value of this OleLocation class
     *
     * @return toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("locationId", locationId);
        return toStringMap;
    }


    public String getFullLocationPath() {
        String fullLocationCode = this.getLocationCode();
        OleLocation deskLocation = this.getOleLocation();
        while (deskLocation != null) {
            fullLocationCode = deskLocation.getLocationCode() + "/" + fullLocationCode;
            if (deskLocation.getParentLocationId() != null) {
                deskLocation = deskLocation.getOleLocation();
            } else
                deskLocation = null;
        }
        return fullLocationCode;
    }

    public void setFullLocationPath(String fullLocationPath) {
        this.fullLocationPath = fullLocationPath;
    }


}
