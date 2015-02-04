package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleSourceOfTerm is business object class for Source Of Term Maintenance Document
 */
public class OleSourceOfTerm extends PersistableBusinessObjectBase {

    private String sourceOfTermId;
    private String sourceOfTermCode;
    private String sourceOfTermName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the sourceOfTermId attribute.
     *
     * @return Returns the sourceOfTermId
     */
    public String getSourceOfTermId() {
        return sourceOfTermId;
    }

    /**
     * Sets the sourceOfTermId attribute value.
     *
     * @param sourceOfTermId The sourceOfTermId to set.
     */
    public void setSourceOfTermId(String sourceOfTermId) {
        this.sourceOfTermId = sourceOfTermId;
    }

    /**
     * Gets the sourceOfTermCode attribute.
     *
     * @return Returns the sourceOfTermCode
     */
    public String getSourceOfTermCode() {
        return sourceOfTermCode;
    }

    /**
     * Sets the sourceOfTermCode attribute value.
     *
     * @param sourceOfTermCode The sourceOfTermCode to set.
     */
    public void setSourceOfTermCode(String sourceOfTermCode) {
        this.sourceOfTermCode = sourceOfTermCode;
    }

    /**
     * Gets the sourceOfTermName attribute.
     *
     * @return Returns the sourceOfTermName
     */
    public String getSourceOfTermName() {
        return sourceOfTermName;
    }

    /**
     * Sets the sourceOfTermName attribute value.
     *
     * @param sourceOfTermName The sourceOfTermName to set.
     */
    public void setSourceOfTermName(String sourceOfTermName) {
        this.sourceOfTermName = sourceOfTermName;
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
        toStringMap.put("sourceOfTermId", sourceOfTermId);
        toStringMap.put("sourceOfTermCode", sourceOfTermCode);
        toStringMap.put("sourceOfTermName", sourceOfTermName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }

}





















