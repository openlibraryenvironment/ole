package org.kuali.ole.describe.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.LinkedHashMap;


/**
 * OleInstanceItemType is business object class for Instance Item Type Maintenance Document
 */
public class OleInstanceItemType extends PersistableBusinessObjectBase {

    private String instanceItemTypeId;
    private String instanceItemTypeCode;
    private String instanceItemTypeName;
    private String instanceItemTypeDesc;
    private String source;
    private Date sourceDate;
    private boolean active;

    /**
     * Gets the instanceItemTypeId attribute.
     *
     * @return Returns the instanceItemTypeId
     */
    public String getInstanceItemTypeId() {
        return instanceItemTypeId;
    }

    /**
     * Sets the instanceItemTypeId attribute value.
     *
     * @param instanceItemTypeId The instanceItemTypeId to set.
     */
    public void setInstanceItemTypeId(String instanceItemTypeId) {
        this.instanceItemTypeId = instanceItemTypeId;
    }

    /**
     * Gets the instanceItemTypeCode attribute.
     *
     * @return Returns the instanceItemTypeCode
     */
    public String getInstanceItemTypeCode() {
        return instanceItemTypeCode;
    }

    /**
     * Sets the instanceItemTypeCode attribute value.
     *
     * @param instanceItemTypeCode The instanceItemTypeCode to set.
     */
    public void setInstanceItemTypeCode(String instanceItemTypeCode) {
        this.instanceItemTypeCode = instanceItemTypeCode;
    }

    /**
     * Gets the instanceItemTypeName attribute.
     *
     * @return Returns the instanceItemTypeName
     */
    public String getInstanceItemTypeName() {
        return instanceItemTypeName;
    }

    /**
     * Sets the instanceItemTypeName attribute value.
     *
     * @param instanceItemTypeName The instanceItemTypeName to set.
     */
    public void setInstanceItemTypeName(String instanceItemTypeName) {
        this.instanceItemTypeName = instanceItemTypeName;
    }

    /**
     * Gets the instanceItemTypeDesc attribute.
     *
     * @return Returns the instanceItemTypeDesc
     */
    public String getInstanceItemTypeDesc() {
        return instanceItemTypeDesc;
    }

    /**
     * Sets the instanceItemTypeDesc attribute value.
     *
     * @param instanceItemTypeDesc The instanceItemTypeDesc to set.
     */
    public void setInstanceItemTypeDesc(String instanceItemTypeDesc) {
        this.instanceItemTypeDesc = instanceItemTypeDesc;
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
     * Gets the toStringMap attribute.
     *
     * @return Returns the toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("instanceItemTypeId", instanceItemTypeId);
        toStringMap.put("instanceItemTypeCode", instanceItemTypeCode);
        toStringMap.put("instanceItemTypeName", instanceItemTypeName);
        toStringMap.put("instanceItemTypeDesc", instanceItemTypeDesc);
        toStringMap.put("source", source);
        toStringMap.put("sourceDate", sourceDate);
        toStringMap.put("active", active);
        return toStringMap;
    }

}














