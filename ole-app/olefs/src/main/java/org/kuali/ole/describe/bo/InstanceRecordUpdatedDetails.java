package org.kuali.ole.describe.bo;

/**
 * InstanceRecordUpdatedDetails provides the audit's update details for Instance Editor
 */
public class InstanceRecordUpdatedDetails {
    private String lastUpdatedBy;
    private String lastUpdated;

    /**
     * Gets the lastUpdatedBy attribute.
     *
     * @return Returns the lastUpdatedBy.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the lastUpdatedBy attribute value.
     *
     * @param lastUpdatedBy The lastUpdatedBy to set.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the lastUpdated attribute.
     *
     * @return Returns the lastUpdated.
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the lastUpdated attribute value.
     *
     * @param lastUpdated The lastUpdated to set.
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
