package org.kuali.ole.describe.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * The OleLocationIngestSumminquiryaryRecord is a BO class that defines the ingest summary fields with getters and setters which
 * is used for interacting the summary data with the persistence layer in OLE.
 */
public class OleLocationIngestSummaryRecord extends PersistableBusinessObjectBase {

    private String oleLocationSummaryId;
    private Integer oleLocationTotCount;
    private Integer oleLocationCreateCount;
    private Integer oleLocationUpdateCount;
    private Integer oleLocationRejectCount;
    private Integer oleLocationFailedCount;
    private String fileName;
    private String olePrincipalName;
    private Timestamp date;


    /**
     * Gets the oleLocationSummaryId string value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationSummaryId
     */
    public String getOleLocationSummaryId() {
        return oleLocationSummaryId;
    }

    /**
     * Sets the oleLocationSummaryId string value inside this OleLocationIngestSummaryRecord class
     *
     * @param oleLocationSummaryId
     */
    public void setOleLocationSummaryId(String oleLocationSummaryId) {
        this.oleLocationSummaryId = oleLocationSummaryId;
    }

    /**
     * Gets the oleLocationRejectCount integer value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationRejectCount
     */
    public Integer getOleLocationRejectCount() {
        if (this.oleLocationRejectCount == null)
            return 0;
        return oleLocationRejectCount;
    }

    /**
     * Sets the oleLocationRejectCount integer value inside this OleLocationIngestSummaryRecord class
     *
     * @param LocationRejectCount
     */
    public void setOleLocationRejectCount(Integer LocationRejectCount) {
        this.oleLocationRejectCount = LocationRejectCount;
    }

    /**
     * Gets the oleLocationUpdateCount integer value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationUpdateCount
     */
    public Integer getOleLocationUpdateCount() {
        if (this.oleLocationUpdateCount == null)
            return 0;
        return oleLocationUpdateCount;
    }

    /**
     * Sets the oleLocationUpdateCount integer value inside this OleLocationIngestSummaryRecord class
     *
     * @param LocationUpdateCount
     */
    public void setOleLocationUpdateCount(Integer LocationUpdateCount) {
        this.oleLocationUpdateCount = LocationUpdateCount;
    }

    /**
     * Gets the oleLocationCreateCount integer value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationCreateCount
     */
    public Integer getOleLocationCreateCount() {
        if (this.oleLocationCreateCount == null)
            return 0;
        return oleLocationCreateCount;
    }

    /*
     * Sets the oleLocationCreateCount integer value inside this OleLocationIngestSummaryRecord class
     * @param LocationCreateCount
     */
    public void setOleLocationCreateCount(Integer LocationCreateCount) {
        this.oleLocationCreateCount = LocationCreateCount;
    }

    /**
     * Gets the oleLocationTotCount integer value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationTotCount
     */
    public Integer getOleLocationTotCount() {
        if (this.oleLocationTotCount == null)
            return 0;
        return oleLocationTotCount;
    }

    /**
     * Sets the oleLocationTotCount integer value inside this OleLocationIngestSummaryRecord class
     *
     * @param LocationTotCount
     */
    public void setOleLocationTotCount(Integer LocationTotCount) {
        this.oleLocationTotCount = LocationTotCount;
    }

    /**
     * Gets the fileName string value from this OleLocationIngestSummaryRecord class
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the fileName string value inside this OleLocationIngestSummaryRecord class
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the oleLocationFailedCount integer value from this OleLocationIngestSummaryRecord class
     *
     * @return oleLocationFailedCount
     */
    public Integer getOleLocationFailedCount() {
        if (this.oleLocationFailedCount == null)
            return 0;
        return oleLocationFailedCount;
    }

    /**
     * Sets the oleLocationFailedCount integer value inside this OleLocationIngestSummaryRecord class
     *
     * @param LocationFailedCount
     */
    public void setOleLocationFailedCount(Integer LocationFailedCount) {
        this.oleLocationFailedCount = LocationFailedCount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getOlePrincipalName() {
        return olePrincipalName;
    }

    public void setOlePrincipalName(String olePrincipalName) {
        this.olePrincipalName = olePrincipalName;
    }
}
