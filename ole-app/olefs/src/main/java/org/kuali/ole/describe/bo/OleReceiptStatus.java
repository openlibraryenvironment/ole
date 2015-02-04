package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleReceiptStatus is business object class for Receipt Status Maintenance Document
 */
public class OleReceiptStatus extends PersistableBusinessObjectBase {

    private String receiptStatusId;
    private String receiptStatusCode;
    private String receiptStatusName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the receiptStatusId attribute.
     *
     * @return Returns the receiptStatusId
     */
    public String getReceiptStatusId() {
        return receiptStatusId;
    }

    /**
     * Sets the receiptStatusId attribute value.
     *
     * @param receiptStatusId The receiptStatusId to set.
     */
    public void setReceiptStatusId(String receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    /**
     * Gets the receiptStatusCode attribute.
     *
     * @return Returns the receiptStatusCode
     */
    public String getReceiptStatusCode() {
        return receiptStatusCode;
    }

    /**
     * Sets the receiptStatusCode attribute value.
     *
     * @param receiptStatusCode The receiptStatusCode to set.
     */
    public void setReceiptStatusCode(String receiptStatusCode) {
        this.receiptStatusCode = receiptStatusCode;
    }

    /**
     * Gets the receiptStatusName attribute.
     *
     * @return Returns the receiptStatusName
     */
    public String getReceiptStatusName() {
        return receiptStatusName;
    }

    /**
     * Sets the receiptStatusName attribute value.
     *
     * @param receiptStatusName The receiptStatusName to set.
     */
    public void setReceiptStatusName(String receiptStatusName) {
        this.receiptStatusName = receiptStatusName;
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
        toStringMap.put("receiptStatusId", receiptStatusId);
        toStringMap.put("receiptStatusCode", receiptStatusCode);
        toStringMap.put("receiptStatusName", receiptStatusName);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
