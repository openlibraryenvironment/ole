package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleFieldEncodingLevel is business object class for Field Encoding Level Maintenance Document
 */
public class OleFieldEncodingLevel extends PersistableBusinessObjectBase {

    private Integer fieldEncodingLevelId;
    private String fieldEncodingLevelCode;
    private String fieldEncodingLevelName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the fieldEncodingLevelId attribute.
     *
     * @return Returns the fieldEncodingLevelId
     */
    public Integer getFieldEncodingLevelId() {
        return fieldEncodingLevelId;
    }

    /**
     * Sets the fieldEncodingLevelId attribute value.
     *
     * @param fieldEncodingLevelId The fieldEncodingLevelId to set.
     */
    public void setFieldEncodingLevelId(Integer fieldEncodingLevelId) {
        this.fieldEncodingLevelId = fieldEncodingLevelId;
    }

    /**
     * Gets the fieldEncodingLevelCode attribute.
     *
     * @return Returns the fieldEncodingLevelCode
     */
    public String getFieldEncodingLevelCode() {
        return fieldEncodingLevelCode;
    }

    /**
     * Sets the fieldEncodingLevelCode attribute value.
     *
     * @param fieldEncodingLevelCode The fieldEncodingLevelCode to set.
     */
    public void setFieldEncodingLevelCode(String fieldEncodingLevelCode) {
        this.fieldEncodingLevelCode = fieldEncodingLevelCode;
    }

    /**
     * Gets the fieldEncodingLevelName attribute.
     *
     * @return Returns the fieldEncodingLevelName
     */
    public String getFieldEncodingLevelName() {
        return fieldEncodingLevelName;
    }

    /**
     * Sets the fieldEncodingLevelName attribute value.
     *
     * @param fieldEncodingLevelName The fieldEncodingLevelName to set.
     */
    public void setFieldEncodingLevelName(String fieldEncodingLevelName) {
        this.fieldEncodingLevelName = fieldEncodingLevelName;
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
        toStringMap.put("fieldEncodingLevelId", fieldEncodingLevelId);
        return toStringMap;
    }
}
