package org.kuali.ole.select.gokb;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by premkumarv on 12/16/14.
 */
public class OleGokbUpdateLog extends PersistableBusinessObjectBase {
    private Integer id;
    private Integer noOfPackages;
    private Integer noOfTipps;
    private Integer noOfTitles;
    private Integer noOfOrganization;
    private Integer noOfPlatforms;
    private String user;
    private String status;
    private Timestamp startTime;
    private Timestamp endTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNoOfPackages() {
        return noOfPackages;
    }

    public void setNoOfPackages(Integer noOfPackages) {
        this.noOfPackages = noOfPackages;
    }

    public Integer getNoOfTipps() {
        return noOfTipps;
    }

    public void setNoOfTipps(Integer noOfTipps) {
        this.noOfTipps = noOfTipps;
    }

    public Integer getNoOfTitles() {
        return noOfTitles;
    }

    public void setNoOfTitles(Integer noOfTitles) {
        this.noOfTitles = noOfTitles;
    }

    public Integer getNoOfOrganization() {
        return noOfOrganization;
    }

    public void setNoOfOrganization(Integer noOfOrganization) {
        this.noOfOrganization = noOfOrganization;
    }

    public Integer getNoOfPlatforms() {
        return noOfPlatforms;
    }

    public void setNoOfPlatforms(Integer noOfPlatforms) {
        this.noOfPlatforms = noOfPlatforms;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
