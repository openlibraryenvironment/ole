package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleCompleteness is business object class for Completeness Maintenance Document
 */
public class OleCompleteness extends PersistableBusinessObjectBase {
    private Integer completenessId;
    private String completenessCode;
    private String completenessName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Default constructor.
     */
    public OleCompleteness() {
    }

    /**
     * Gets the completenessId attribute.
     *
     * @return Returns the completenessId
     */
    public Integer getCompletenessId() {
        return completenessId;
    }

    /**
     * Sets the completenessId attribute value.
     *
     * @param completenessId The completenessId to set.
     */
    public void setCompletenessId(Integer completenessId) {
        this.completenessId = completenessId;
    }

    /**
     * Gets the completenessCode attribute.
     *
     * @return Returns the completenessCode
     */
    public String getCompletenessCode() {
        return completenessCode;
    }

    /**
     * Sets the completenessCode attribute value.
     *
     * @param completenessCode The completenessCode to set.
     */
    public void setCompletenessCode(String completenessCode) {
        this.completenessCode = completenessCode;
    }

    /**
     * Gets the completenessName attribute.
     *
     * @return Returns the completenessName
     */
    public String getCompletenessName() {
        return completenessName;
    }

    /**
     * Sets the completenessName attribute value.
     *
     * @param completenessName The completenessName to set.
     */
    public void setCompletenessName(String completenessName) {
        this.completenessName = completenessName;
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
        toStringMap.put("completenessId", completenessId);
        return toStringMap;
    }
}
