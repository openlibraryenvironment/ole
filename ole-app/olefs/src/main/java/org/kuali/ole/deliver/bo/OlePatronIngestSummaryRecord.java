package org.kuali.ole.deliver.bo;


import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * OlePatronIngestSummaryRecord provides OlePatronIngestSummaryRecord information through getter and setter.
 */
public class OlePatronIngestSummaryRecord extends PersistableBusinessObjectBase {

    private String olePatronSummaryId;
    private Integer patronTotCount;
    private Integer patronCreateCount;
    private Integer patronUpdateCount;
    private Integer patronRejectCount;
    private Integer patronFailedCount;
    private String fileName;
    private String principalName;
    private Timestamp createdDate;
    private String failureRecords;
    private List<OlePatron> failurePatronRecords = new ArrayList<>();


    public String getFailureRecords() {
        return failureRecords;
    }

    public void setFailureRecords(String failureRecords) {
        this.failureRecords = failureRecords;
    }

    /**
     * Gets the value of olePatronSummaryId property
     *
     * @return olePatronSummaryId
     */
    public String getOlePatronSummaryId() {
        return olePatronSummaryId;
    }

    /**
     * Sets the value for olePatronSummaryId property
     *
     * @param olePatronSummaryId
     */
    public void setOlePatronSummaryId(String olePatronSummaryId) {
        this.olePatronSummaryId = olePatronSummaryId;
    }

    /**
     * Gets the value of patronRejectCount property
     *
     * @return patronRejectCount
     */
    public Integer getPatronRejectCount() {
        if (this.patronRejectCount == null)
            return 0;
        return patronRejectCount;
    }

    /**
     * Sets the value for patronRejectCount property
     *
     * @param patronRejectCount
     */
    public void setPatronRejectCount(Integer patronRejectCount) {
        this.patronRejectCount = patronRejectCount;
    }

    /**
     * Gets the value of patronUpdateCount property
     *
     * @return patronUpdateCount
     */
    public Integer getPatronUpdateCount() {
        if (this.patronUpdateCount == null)
            return 0;
        return patronUpdateCount;
    }

    /**
     * Sets the value for patronUpdateCount property
     *
     * @param patronUpdateCount
     */
    public void setPatronUpdateCount(Integer patronUpdateCount) {
        this.patronUpdateCount = patronUpdateCount;
    }

    /**
     * Gets the value of patronCreateCount property
     *
     * @return patronCreateCount
     */
    public Integer getPatronCreateCount() {
        if (this.patronCreateCount == null)
            return 0;
        return patronCreateCount;
    }

    /**
     * Sets the value for patronCreateCount property
     *
     * @param patronCreateCount
     */
    public void setPatronCreateCount(Integer patronCreateCount) {
        this.patronCreateCount = patronCreateCount;
    }

    /**
     * Gets the value of patronTotCount property
     *
     * @return patronTotCount
     */
    public Integer getPatronTotCount() {
        if (this.patronTotCount == null)
            return 0;
        return patronTotCount;
    }

    /**
     * Sets the value for patronTotCount property
     *
     * @param patronTotCount
     */
    public void setPatronTotCount(Integer patronTotCount) {
        this.patronTotCount = patronTotCount;
    }

    /**
     * Gets the value of fileName property
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value for fileName property
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the value of patronFailedCount property
     *
     * @return patronFailedCount
     */
    public Integer getPatronFailedCount() {
        if (this.patronFailedCount == null)
            return 0;
        return patronFailedCount;
    }

    /**
     * Sets the value for patronFailedCount property
     *
     * @param patronFailedCount
     */
    public void setPatronFailedCount(Integer patronFailedCount) {
        this.patronFailedCount = patronFailedCount;
    }

    /**
     * Gets the value of principalName property
     *
     * @return principalName
     */
    public String getPrincipalName() {
        return principalName;
    }

    /**
     * Sets the value for principalName property
     *
     * @param principalName
     */
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    /**
     * Gets the value of createdDate property
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the value for createdDate property
     *
     * @param createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public List<OlePatron> getFailurePatronRecords() {
        return failurePatronRecords;
    }

    public void setFailurePatronRecords(List<OlePatron> failurePatronRecords) {
        this.failurePatronRecords = failurePatronRecords;
    }
}
