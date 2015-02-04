package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleRecordType is business object class for Record Type Maintenance Document
 */
public class OleRecordType extends PersistableBusinessObjectBase {

    private Integer recordTypeId;
    private String recordTypeCode;
    private String recordTypeName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the recordTypeId attribute.
     *
     * @return Returns the recordTypeId
     */
    public Integer getRecordTypeId() {
        return recordTypeId;
    }

    /**
     * Sets the recordTypeId attribute value.
     *
     * @param recordTypeId The recordTypeId to set.
     */
    public void setRecordTypeId(Integer recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    /**
     * Gets the recordTypeCode attribute.
     *
     * @return Returns the recordTypeCode
     */
    public String getRecordTypeCode() {
        return recordTypeCode;
    }

    /**
     * Sets the recordTypeCode attribute value.
     *
     * @param recordTypeCode The recordTypeCode to set.
     */
    public void setRecordTypeCode(String recordTypeCode) {
        this.recordTypeCode = recordTypeCode;
    }

    /**
     * Gets the recordTypeName attribute.
     *
     * @return Returns the recordTypeName
     */
    public String getRecordTypeName() {
        return recordTypeName;
    }

    /**
     * Sets the recordTypeName attribute value.
     *
     * @param recordTypeName The recordTypeName to set.
     */
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
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
        toStringMap.put("recordTypeId", recordTypeId);
        return toStringMap;
    }

}
