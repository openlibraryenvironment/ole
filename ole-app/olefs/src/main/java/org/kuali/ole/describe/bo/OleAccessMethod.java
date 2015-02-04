package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleAccessMethod is business object class for Access Method Maintenance Document
 */
public class OleAccessMethod extends PersistableBusinessObjectBase {

    private Integer accessMethodId;
    private String accessMethodCode;
    private String accessMethodName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the accessMethodId attribute.
     *
     * @return Returns the accessMethodId
     */
    public Integer getAccessMethodId() {
        return accessMethodId;
    }

    /**
     * Sets the accessMethodId attribute value.
     *
     * @param accessMethodId The accessMethodId to set.
     */
    public void setAccessMethodId(Integer accessMethodId) {
        this.accessMethodId = accessMethodId;
    }

    /**
     * Gets the accessMethodCode attribute.
     *
     * @return Returns the accessMethodCode
     */
    public String getAccessMethodCode() {
        return accessMethodCode;
    }

    /**
     * Sets the accessMethodCode attribute value.
     *
     * @param accessMethodCode The accessMethodCode to set.
     */
    public void setAccessMethodCode(String accessMethodCode) {
        this.accessMethodCode = accessMethodCode;
    }

    /**
     * Gets the accessMethodName attribute.
     *
     * @return Returns the accessMethodName
     */
    public String getAccessMethodName() {
        return accessMethodName;
    }

    /**
     * Sets the accessMethodName attribute value.
     *
     * @param accessMethodName The accessMethodName to set.
     */
    public void setAccessMethodName(String accessMethodName) {
        this.accessMethodName = accessMethodName;
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
        toStringMap.put("accessMethodId", accessMethodId);
        return toStringMap;
    }
}
