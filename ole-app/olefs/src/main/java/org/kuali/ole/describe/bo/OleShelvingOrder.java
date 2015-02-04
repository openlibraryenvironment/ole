package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleShelvingOrder is business object class for Shelving Order Maintenance Document
 */
public class OleShelvingOrder extends PersistableBusinessObjectBase {
    private Integer shelvingOrderId;
    private String shelvingOrderCode;
    private String shelvingOrderName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the shelvingOrderId attribute.
     *
     * @return Returns the shelvingOrderId
     */
    public Integer getShelvingOrderId() {
        return shelvingOrderId;
    }

    /**
     * Sets the shelvingOrderId attribute value.
     *
     * @param shelvingOrderId The shelvingOrderId to set.
     */
    public void setShelvingOrderId(Integer shelvingOrderId) {
        this.shelvingOrderId = shelvingOrderId;
    }

    /**
     * Gets the shelvingOrderCode attribute.
     *
     * @return Returns the shelvingOrderCode
     */
    public String getShelvingOrderCode() {
        return shelvingOrderCode;
    }

    /**
     * Sets the shelvingOrderCode attribute value.
     *
     * @param shelvingOrderCode The shelvingOrderCode to set.
     */
    public void setShelvingOrderCode(String shelvingOrderCode) {
        this.shelvingOrderCode = shelvingOrderCode;
    }

    /**
     * Gets the shelvingOrderName attribute.
     *
     * @return Returns the shelvingOrderName
     */
    public String getShelvingOrderName() {
        return shelvingOrderName;
    }

    /**
     * Sets the shelvingOrderName attribute value.
     *
     * @param shelvingOrderName The shelvingOrderName to set.
     */
    public void setShelvingOrderName(String shelvingOrderName) {
        this.shelvingOrderName = shelvingOrderName;
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
        toStringMap.put("shelvingOrderId", shelvingOrderId);
        return toStringMap;
    }
}
