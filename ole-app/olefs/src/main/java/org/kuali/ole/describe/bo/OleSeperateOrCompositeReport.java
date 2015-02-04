package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleSeperateOrCompositeReport is business object class for Seperate Or Composite Report Maintenance Document
 */
public class OleSeperateOrCompositeReport extends PersistableBusinessObjectBase {

    private Integer seperateOrCompositeReportId;
    private String seperateOrCompositeReportCode;
    private String seperateOrCompositeReportName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the seperateOrCompositeReportId attribute.
     *
     * @return Returns the seperateOrCompositeReportId
     */
    public Integer getSeperateOrCompositeReportId() {
        return seperateOrCompositeReportId;
    }

    /**
     * Sets the seperateOrCompositeReportId attribute value.
     *
     * @param seperateOrCompositeReportId The seperateOrCompositeReportId to set.
     */
    public void setSeperateOrCompositeReportId(Integer seperateOrCompositeReportId) {
        this.seperateOrCompositeReportId = seperateOrCompositeReportId;
    }

    /**
     * Gets the seperateOrCompositeReportCode attribute.
     *
     * @return Returns the seperateOrCompositeReportCode
     */
    public String getSeperateOrCompositeReportCode() {
        return seperateOrCompositeReportCode;
    }

    /**
     * Sets the seperateOrCompositeReportCode attribute value.
     *
     * @param seperateOrCompositeReportCode The seperateOrCompositeReportCode to set.
     */
    public void setSeperateOrCompositeReportCode(String seperateOrCompositeReportCode) {
        this.seperateOrCompositeReportCode = seperateOrCompositeReportCode;
    }

    /**
     * Gets the seperateOrCompositeReportName attribute.
     *
     * @return Returns the seperateOrCompositeReportName
     */
    public String getSeperateOrCompositeReportName() {
        return seperateOrCompositeReportName;
    }

    /**
     * Sets the seperateOrCompositeReportName attribute value.
     *
     * @param seperateOrCompositeReportName The seperateOrCompositeReportName to set.
     */
    public void setSeperateOrCompositeReportName(String seperateOrCompositeReportName) {
        this.seperateOrCompositeReportName = seperateOrCompositeReportName;
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
        toStringMap.put("seperateOrCompositeReportId", seperateOrCompositeReportId);
        return toStringMap;
    }
}
