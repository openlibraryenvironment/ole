package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * OleStatisticalCategoryBo provides OleStatisticalCategoryBo information through getter and setter.
 */
public class OleStatisticalCategoryBo extends PersistableBusinessObjectBase {

    private String oleStatisticalCategoryId;
    private String oleStatisticalCategoryCode;
    private String oleStatisticalCategoryName;
    private String oleStatisticalCategoryDesc;
    private boolean active;

    /**
     * Gets the value of oleStatisticalCategoryId property
     *
     * @return oleStatisticalCategoryId
     */
    public String getOleStatisticalCategoryId() {
        return oleStatisticalCategoryId;
    }

    /**
     * Sets the value for oleStatisticalCategoryId property
     *
     * @param oleStatisticalCategoryId
     */
    public void setOleStatisticalCategoryId(String oleStatisticalCategoryId) {
        this.oleStatisticalCategoryId = oleStatisticalCategoryId;
    }

    /**
     * Gets the value of oleStatisticalCategoryCode property
     *
     * @return oleStatisticalCategoryCode
     */
    public String getOleStatisticalCategoryCode() {
        return oleStatisticalCategoryCode;
    }

    /**
     * Sets the value for oleStatisticalCategoryCode property
     *
     * @param oleStatisticalCategoryCode
     */
    public void setOleStatisticalCategoryCode(String oleStatisticalCategoryCode) {
        this.oleStatisticalCategoryCode = oleStatisticalCategoryCode;
    }

    /**
     * Gets the value of oleStatisticalCategoryName property
     *
     * @return oleStatisticalCategoryName
     */
    public String getOleStatisticalCategoryName() {
        return oleStatisticalCategoryName;
    }

    /**
     * Sets the value for oleStatisticalCategoryName property
     *
     * @param oleStatisticalCategoryName
     */
    public void setOleStatisticalCategoryName(String oleStatisticalCategoryName) {
        this.oleStatisticalCategoryName = oleStatisticalCategoryName;
    }

    /**
     * Gets the value of oleStatisticalCategoryDesc property
     *
     * @return oleStatisticalCategoryDesc
     */
    public String getOleStatisticalCategoryDesc() {
        return oleStatisticalCategoryDesc;
    }

    /**
     * Sets the value for oleStatisticalCategoryDesc property
     *
     * @param oleStatisticalCategoryDesc
     */
    public void setOleStatisticalCategoryDesc(String oleStatisticalCategoryDesc) {
        this.oleStatisticalCategoryDesc = oleStatisticalCategoryDesc;
    }

    /**
     * Gets the value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
