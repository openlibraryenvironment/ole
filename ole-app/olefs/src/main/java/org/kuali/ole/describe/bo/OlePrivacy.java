package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OlePrivacy is business object class for Privacy Maintenance Document
 */
public class OlePrivacy extends PersistableBusinessObjectBase {

    private String privacyId;
    private String privacyCode;
    private String privacyName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the privacyId attribute.
     *
     * @return Returns the privacyId
     */
    public String getPrivacyId() {
        return privacyId;
    }

    /**
     * Sets the privacyId attribute value.
     *
     * @param privacyId The privacyId to set.
     */
    public void setPrivacyId(String privacyId) {
        this.privacyId = privacyId;
    }

    /**
     * Gets the privacyCode attribute.
     *
     * @return Returns the privacyCode
     */
    public String getPrivacyCode() {
        return privacyCode;
    }

    /**
     * Sets the privacyCode attribute value.
     *
     * @param privacyCode The privacyCode to set.
     */
    public void setPrivacyCode(String privacyCode) {
        this.privacyCode = privacyCode;
    }

    /**
     * Gets the privacyName attribute.
     *
     * @return Returns the privacyName
     */
    public String getPrivacyName() {
        return privacyName;
    }

    /**
     * Sets the privacyName attribute value.
     *
     * @param privacyName The privacyName to set.
     */
    public void setPrivacyName(String privacyName) {
        this.privacyName = privacyName;
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
        toStringMap.put("privacyId", privacyId);
        toStringMap.put("privacyCode", privacyCode);
        toStringMap.put("privacyName", privacyName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }

}











