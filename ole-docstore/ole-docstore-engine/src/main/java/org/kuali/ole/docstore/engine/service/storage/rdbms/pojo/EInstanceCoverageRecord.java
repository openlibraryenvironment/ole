package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/19/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class EInstanceCoverageRecord extends PersistableBusinessObjectBase implements Serializable {
    private String eInstanceCoverageId;
    private String holdingsId;
    private String coverageStartDate;
    private String coverageStartVolume;
    private String coverageStartIssue;
    private String coverageEndDate;
    private String coverageEndVolume;
    private String coverageEndIssue;
    private HoldingsRecord holdingsRecord;
    private Timestamp updatedDate;

    public String getEInstanceCoverageId() {
        return eInstanceCoverageId;
    }

    public void setEInstanceCoverageId(String eInstanceCoverageId) {
        this.eInstanceCoverageId = eInstanceCoverageId;
    }



    public String getCoverageStartVolume() {
        return coverageStartVolume;
    }

    public void setCoverageStartVolume(String coverageStartVolume) {
        this.coverageStartVolume = coverageStartVolume;
    }

    public String getCoverageStartIssue() {
        return coverageStartIssue;
    }

    public void setCoverageStartIssue(String coverageStartIssue) {
        this.coverageStartIssue = coverageStartIssue;
    }

    public String getCoverageStartDate() {
        return coverageStartDate;
    }

    public void setCoverageStartDate(String coverageStartDate) {
        this.coverageStartDate = coverageStartDate;
    }

    public String getCoverageEndDate() {
        return coverageEndDate;
    }

    public void setCoverageEndDate(String coverageEndDate) {
        this.coverageEndDate = coverageEndDate;
    }

    public String getCoverageEndVolume() {
        return coverageEndVolume;
    }

    public void setCoverageEndVolume(String coverageEndVolume) {
        this.coverageEndVolume = coverageEndVolume;
    }

    public String getCoverageEndIssue() {
        return coverageEndIssue;
    }

    public void setCoverageEndIssue(String coverageEndIssue) {
        this.coverageEndIssue = coverageEndIssue;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}

