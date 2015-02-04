package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbTipp extends PersistableBusinessObjectBase {

    private Integer gokbTippId;
    private Integer gokbPackageId;
    private Integer gokbTitleId;
    private Integer gokbPlatformId;
    private String status;
    private String statusReason;
    private Timestamp startdate;
    private String startVolume;
    private String startIssue;
    private Timestamp endDate;
    private String endVolume;
    private String endIssue;
    private String embarco;
    private String platformHostUrl;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;


    public Integer getGokbTippId() {
        return gokbTippId;
    }

    public void setGokbTippId(Integer gokbTippId) {
        this.gokbTippId = gokbTippId;
    }

    public Integer getGokbPackageId() {
        return gokbPackageId;
    }

    public void setGokbPackageId(Integer gokbPackageId) {
        this.gokbPackageId = gokbPackageId;
    }

    public Integer getGokbTitleId() {
        return gokbTitleId;
    }

    public void setGokbTitleId(Integer gokbTitleId) {
        this.gokbTitleId = gokbTitleId;
    }

    public Integer getGokbPlatformId() {
        return gokbPlatformId;
    }

    public void setGokbPlatformId(Integer gokbPlatformId) {
        this.gokbPlatformId = gokbPlatformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public String getStartVolume() {
        return startVolume;
    }

    public void setStartVolume(String startVolume) {
        this.startVolume = startVolume;
    }

    public String getStartIssue() {
        return startIssue;
    }

    public void setStartIssue(String startIssue) {
        this.startIssue = startIssue;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getEndVolume() {
        return endVolume;
    }

    public void setEndVolume(String endVolume) {
        this.endVolume = endVolume;
    }

    public String getEndIssue() {
        return endIssue;
    }

    public void setEndIssue(String endIssue) {
        this.endIssue = endIssue;
    }

    public String getEmbarco() {
        return embarco;
    }

    public void setEmbarco(String embarco) {
        this.embarco = embarco;
    }

    public String getPlatformHostUrl() {
        return platformHostUrl;
    }

    public void setPlatformHostUrl(String platformHostUrl) {
        this.platformHostUrl = platformHostUrl;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

}
