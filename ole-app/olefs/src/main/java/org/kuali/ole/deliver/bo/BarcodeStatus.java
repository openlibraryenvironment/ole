package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * BarcodeStatus provides barcode status information through getter and setter.
 */

public class BarcodeStatus extends PersistableBusinessObjectBase {

    private String barcodeStatusId;
    private String barcodeStatusCode;
    private String barcodeStatusName;
    private boolean active;
    private boolean delete;

    /**
     * Gets the value of barcodeStatusId property
     *
     * @return barcodeStatusId
     */
    public String getBarcodeStatusId() {
        return barcodeStatusId;
    }

    /**
     * Sets the value for barcodeStatusId property
     *
     * @param barcodeStatusId
     */
    public void setBarcodeStatusId(String barcodeStatusId) {
        this.barcodeStatusId = barcodeStatusId;
    }

    /**
     * default constructor
     */
    public BarcodeStatus() {

    }

    /**
     * Gets the value of barcodeStatusCode property
     *
     * @return barcodeStatusCode
     */
    public String getBarcodeStatusCode() {
        return barcodeStatusCode;
    }

    /**
     * Sets the value for barcodeStatusCode property
     *
     * @param barcodeStatusCode
     */
    public void setBarcodeStatusCode(String barcodeStatusCode) {
        this.barcodeStatusCode = barcodeStatusCode;
    }

    /**
     * Gets the value of barcodeStatusName property
     *
     * @return barcodeStatusName
     */
    public String getBarcodeStatusName() {
        return barcodeStatusName;
    }

    /**
     * Sets the value for barcodeStatusName property
     *
     * @param barcodeStatusName
     */
    public void setBarcodeStatusName(String barcodeStatusName) {
        this.barcodeStatusName = barcodeStatusName;
    }

    /**
     * Gets the boolean value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the boolean value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the boolean value of delete property
     *
     * @return delete
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Sets the boolean value for delete property
     *
     * @param delete
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("barcodeStatusId", barcodeStatusId);
        return toStringMap;
    }
}
