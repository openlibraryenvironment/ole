package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleStatisticalSearchingCodes is business object class for Statistical Searching Codes Maintenance Document
 */
public class OleStatisticalSearchingCodes extends PersistableBusinessObjectBase {

    private Integer statisticalSearchingCodeId;
    private String statisticalSearchingCode;
    private String statisticalSearchingName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the statisticalSearchingCodeId attribute.
     *
     * @return Returns the statisticalSearchingCodeId
     */
    public Integer getStatisticalSearchingCodeId() {
        return statisticalSearchingCodeId;
    }

    /**
     * Sets the statisticalSearchingCodeId attribute value.
     *
     * @param statisticalSearchingCodeId The statisticalSearchingCodeId to set.
     */
    public void setStatisticalSearchingCodeId(Integer statisticalSearchingCodeId) {
        this.statisticalSearchingCodeId = statisticalSearchingCodeId;
    }

    /**
     * Gets the statisticalSearchingCode attribute.
     *
     * @return Returns the statisticalSearchingCode
     */
    public String getStatisticalSearchingCode() {
        return statisticalSearchingCode;
    }

    /**
     * Sets the statisticalSearchingCode attribute value.
     *
     * @param statisticalSearchingCode The statisticalSearchingCode to set.
     */
    public void setStatisticalSearchingCode(String statisticalSearchingCode) {
        this.statisticalSearchingCode = statisticalSearchingCode;
    }

    /**
     * Gets the statisticalSearchingName attribute.
     *
     * @return Returns the statisticalSearchingName
     */
    public String getStatisticalSearchingName() {
        return statisticalSearchingName;
    }

    /**
     * Sets the statisticalSearchingName attribute value.
     *
     * @param statisticalSearchingName The statisticalSearchingName to set.
     */
    public void setStatisticalSearchingName(String statisticalSearchingName) {
        this.statisticalSearchingName = statisticalSearchingName;
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
        toStringMap.put("statisticalSearchingCodeId", statisticalSearchingCodeId);
        return toStringMap;
    }
}
