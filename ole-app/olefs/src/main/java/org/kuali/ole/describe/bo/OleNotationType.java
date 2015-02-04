package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleNotationType is business object class for Notation Type Maintenance Document
 */
public class OleNotationType extends PersistableBusinessObjectBase {

    private String notationTypeId;
    private String notationTypeCode;
    private String notationTypeName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the notationTypeId attribute.
     *
     * @return Returns the notationTypeId
     */
    public String getNotationTypeId() {
        return notationTypeId;
    }

    /**
     * Sets the notationTypeId attribute value.
     *
     * @param notationTypeId The notationTypeId to set.
     */
    public void setNotationTypeId(String notationTypeId) {
        this.notationTypeId = notationTypeId;
    }

    /**
     * Gets the notationTypeCode attribute.
     *
     * @return Returns the notationTypeCode
     */
    public String getNotationTypeCode() {
        return notationTypeCode;
    }

    /**
     * Sets the notationTypeCode attribute value.
     *
     * @param notationTypeCode The notationTypeCode to set.
     */
    public void setNotationTypeCode(String notationTypeCode) {
        this.notationTypeCode = notationTypeCode;
    }

    /**
     * Gets the notationTypeName attribute.
     *
     * @return Returns the notationTypeName
     */
    public String getNotationTypeName() {
        return notationTypeName;
    }

    /**
     * Sets the notationTypeName attribute value.
     *
     * @param notationTypeName The notationTypeName to set.
     */
    public void setNotationTypeName(String notationTypeName) {
        this.notationTypeName = notationTypeName;
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
        toStringMap.put("notationTypeId", notationTypeId);
        toStringMap.put("notationTypeCode", notationTypeCode);
        toStringMap.put("notationTypeName", notationTypeName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }

}

















