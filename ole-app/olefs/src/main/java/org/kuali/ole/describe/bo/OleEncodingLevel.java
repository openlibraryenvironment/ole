package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleEncodingLevel is business object class for Encoding Level Maintenance Document
 */
public class OleEncodingLevel extends PersistableBusinessObjectBase {
    private Integer encodingLevelId;
    private String encodingLevelCode;
    private String encodingLevelName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the encodingLevelId attribute.
     *
     * @return Returns the encodingLevelId
     */
    public Integer getEncodingLevelId() {
        return encodingLevelId;
    }

    /**
     * Sets the encodingLevelId attribute value.
     *
     * @param encodingLevelId The encodingLevelId to set.
     */
    public void setEncodingLevelId(Integer encodingLevelId) {
        this.encodingLevelId = encodingLevelId;
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
     * Gets the encodingLevelName attribute.
     *
     * @return Returns the encodingLevelName
     */
    public String getEncodingLevelName() {
        return encodingLevelName;
    }

    /**
     * Sets the encodingLevelName attribute value.
     *
     * @param encodingLevelName The encodingLevelName to set.
     */
    public void setEncodingLevelName(String encodingLevelName) {
        this.encodingLevelName = encodingLevelName;
    }

    /**
     * Gets the encodingLevelCode attribute.
     *
     * @return Returns the encodingLevelCode
     */
    public String getEncodingLevelCode() {
        return encodingLevelCode;
    }

    /**
     * Sets the encodingLevelCode attribute value.
     *
     * @param encodingLevelCode The encodingLevelCode to set.
     */
    public void setEncodingLevelCode(String encodingLevelCode) {
        this.encodingLevelCode = encodingLevelCode;
    }

    /**
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("encodingLevelId", encodingLevelId);
        return toStringMap;
    }
}
