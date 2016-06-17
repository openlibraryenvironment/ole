package org.kuali.ole.docstore.common.document.content.instance;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * The OleLocationLevel is a BO class that defines the location level fields with getters and setters which
 * is used for interacting the location level data with the persistence layer in OLE.
 */
public class OleLocationLevel extends PersistableBusinessObjectBase {

    private String levelId;
    private String levelCode;
    private String levelName;
    private String parentLevelId;
    private OleLocationLevel oleLocationLevel;
    private OleLocationLevel oleLocation;

    /**
     * Gets the levelId string value from this OleLocationLevel class
     *
     * @return levelId
     */
    public String getLevelId() {
        return levelId;
    }

    /**
     * Sets the levelId string value inside this OleLocationLevel class
     *
     * @param levelId
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    /**
     * Gets the levelCode string value from this OleLocationLevel class
     *
     * @return levelCode
     */
    public String getLevelCode() {
        return levelCode;
    }

    /**
     * Sets the levelCode string value inside this OleLocationLevel class
     *
     * @param levelCode
     */
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * Gets the levelName string value from this OleLocationLevel class
     *
     * @return levelName
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Sets the levelName string value inside this OleLocationLevel class
     *
     * @param levelName
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Gets the parentLevelId string value from this OleLocationLevel class
     *
     * @return parentLevelId
     */
    public String getParentLevelId() {
        return parentLevelId;
    }

    /**
     * Sets the parentLevelId string value inside this OleLocationLevel class
     *
     * @param parentLevelId
     */
    public void setParentLevelId(String parentLevelId) {
        if (!parentLevelId.equals(""))
            this.parentLevelId = parentLevelId;
    }

    /**
     * Gets the oleLocationLevel object value from this OleLocationLevel class
     *
     * @return oleLocationLevel
     */
    public OleLocationLevel getOleLocationLevel() {
        return oleLocationLevel;
    }

    /**
     * ets the oleLocationLevel value inside this OleLocationLevel class
     *
     * @param oleLocationLevel
     */
    public void setOleLocationLevel(OleLocationLevel oleLocationLevel) {
        this.oleLocationLevel = oleLocationLevel;
    }

    /**
     * Gets the oleLocation object value from this OleLocationLevel class
     *
     * @return oleLocation
     */
    public OleLocationLevel getOleLocation() {
        return oleLocation;
    }

    /**
     * Sets the oleLocation value inside this OleLocationLevel class
     *
     * @param oleLocation
     */
    public void setOleLocation(OleLocationLevel oleLocation) {
        this.oleLocation = oleLocation;
    }

    /**
     * This method returns a map that contains the Primary Key value of this OleLocationLevel class
     *
     * @return toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("levelId", levelId);
        return toStringMap;
    }
}
