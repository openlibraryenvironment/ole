package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;

/**
 * OleAcquisitionMethod is business object class for Acquisition Method Maintenance Document
 */
public class OleAcquisitionMethod extends PersistableBusinessObjectBase {

    private Integer acquisitionMethodId;
    private String acquisitionMethodCode;
    private String acquisitionMethodName;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the acquisitionMethodId attribute.
     *
     * @return Returns the acquisitionMethodId
     */
    public Integer getAcquisitionMethodId() {
        return acquisitionMethodId;
    }

    /**
     * Sets the acquisitionMethodId attribute value.
     *
     * @param acquisitionMethodId The acquisitionMethodId to set.
     */
    public void setAcquisitionMethodId(Integer acquisitionMethodId) {
        this.acquisitionMethodId = acquisitionMethodId;
    }

    /**
     * Gets the acquisitionMethodCode attribute.
     *
     * @return Returns the acquisitionMethodCode
     */
    public String getAcquisitionMethodCode() {
        return acquisitionMethodCode;
    }

    /**
     * Sets the acquisitionMethodCode attribute value.
     *
     * @param acquisitionMethodCode The acquisitionMethodCode to set.
     */
    public void setAcquisitionMethodCode(String acquisitionMethodCode) {
        this.acquisitionMethodCode = acquisitionMethodCode;
    }

    /**
     * Gets the acquisitionMethodName attribute.
     *
     * @return Returns the acquisitionMethodName
     */
    public String getAcquisitionMethodName() {
        return acquisitionMethodName;
    }

    /**
     * Sets the acquisitionMethodName attribute value.
     *
     * @param acquisitionMethodName The acquisitionMethodName to set.
     */
    public void setAcquisitionMethodName(String acquisitionMethodName) {
        this.acquisitionMethodName = acquisitionMethodName;
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
     * Gets the source attribute.
     *
     * @return Returns the source
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
        toStringMap.put("acquisitionMethodId", acquisitionMethodId);
        return toStringMap;
    }
}
