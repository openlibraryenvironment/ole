package org.kuali.ole.docstore.model.rdbms.bo;

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
    private String eHoldingsIdentifier;
    private Date coverageStartDate;
    private String coverageStartVolume;
    private String coverageStartIssue;
    private Date coverageEndDate;
    private String coverageEndVolume;
    private String coverageEndIssue;
    private EHoldingsRecord eHoldingsRecord;

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

    public Date getCoverageEndDate() {
        return coverageEndDate;
    }

    public void setCoverageEndDate(Date coverageEndDate) {
        this.coverageEndDate = coverageEndDate;
    }

    public Date getCoverageStartDate() {
        return coverageStartDate;
    }

    public void setCoverageStartDate(Date coverageStartDate) {
        this.coverageStartDate = coverageStartDate;
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

    public EHoldingsRecord getEHoldingsRecord() {
        return eHoldingsRecord;
    }

    public void setEHoldingsRecord(EHoldingsRecord eHoldingsRecord) {
        this.eHoldingsRecord = eHoldingsRecord;
    }

    public String getEHoldingsIdentifier() {
        return eHoldingsIdentifier;
    }

    public void setEHoldingsIdentifier(String eHoldingsIdentifier) {
        this.eHoldingsIdentifier = eHoldingsIdentifier;
    }
}

