package org.kuali.ole.describe.bo;

import java.util.List;

/**
 * InstanceRecordMetaData provides the audit details for Instance Editor
 */
public class InstanceRecordMetaData {

    private String status;
    private String suppressFromPublic;
    private String fastAddFlag;
    private String createdBy;
    private String dateEntered;
    private List<InstanceRecordUpdatedDetails> updatedDetailsList;

    /**
     * Gets the status attribute.
     *
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status attribute value.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the suppressFromPublic attribute.
     *
     * @return Returns the suppressFromPublic.
     */
    public String getSuppressFromPublic() {
        return suppressFromPublic;
    }

    /**
     * Sets the suppressFromPublic attribute value.
     *
     * @param suppressFromPublic The suppressFromPublic to set.
     */
    public void setSuppressFromPublic(String suppressFromPublic) {
        this.suppressFromPublic = suppressFromPublic;
    }

    /**
     * Gets the fastAddFlag attribute.
     *
     * @return Returns the fastAddFlag.
     */
    public String getFastAddFlag() {
        return fastAddFlag;
    }

    /**
     * Sets the fastAddFlag attribute value.
     *
     * @param fastAddFlag The fastAddFlag to set.
     */
    public void setFastAddFlag(String fastAddFlag) {
        this.fastAddFlag = fastAddFlag;
    }

    /**
     * Gets the createdBy attribute.
     *
     * @return Returns the createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the createdBy attribute value.
     *
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the dateEntered attribute.
     *
     * @return Returns the dateEntered.
     */
    public String getDateEntered() {
        return dateEntered;
    }

    /**
     * Sets the dateEntered attribute value.
     *
     * @param dateEntered The dateEntered to set.
     */
    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * Gets the updatedDetailsList attribute.
     *
     * @return Returns the updatedDetailsList.
     */
    public List<InstanceRecordUpdatedDetails> getUpdatedDetailsList() {
        return updatedDetailsList;
    }

    /**
     * Sets the updatedDetailsList attribute value.
     *
     * @param updatedDetailsList The updatedDetailsList to set.
     */
    public void setUpdatedDetailsList(List<InstanceRecordUpdatedDetails> updatedDetailsList) {
        this.updatedDetailsList = updatedDetailsList;
    }
}
