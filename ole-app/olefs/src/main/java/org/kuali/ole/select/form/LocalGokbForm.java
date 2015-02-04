package org.kuali.ole.select.form;

import org.kuali.ole.select.gokb.OleGokbUpdateLog;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by premkumarv on 12/18/14.
 */
public class LocalGokbForm  extends UifFormBase {
    private String id;
    private String noOfPackages;
    private String noOfTipps;
    private String noOfTitles;
    private String noOfOrganization;
    private String noOfPlatforms;
    private String status;
    private Timestamp startTime;
    private Timestamp endTime;

    private List<OleGokbUpdateLog> oleGokbUpdateLogList = new ArrayList<OleGokbUpdateLog>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoOfPackages() {
        return noOfPackages;
    }

    public void setNoOfPackages(String noOfPackages) {
        this.noOfPackages = noOfPackages;
    }

    public String getNoOfTipps() {
        return noOfTipps;
    }

    public void setNoOfTipps(String noOfTipps) {
        this.noOfTipps = noOfTipps;
    }

    public String getNoOfTitles() {
        return noOfTitles;
    }

    public void setNoOfTitles(String noOfTitles) {
        this.noOfTitles = noOfTitles;
    }

    public String getNoOfOrganization() {
        return noOfOrganization;
    }

    public void setNoOfOrganization(String noOfOrganization) {
        this.noOfOrganization = noOfOrganization;
    }

    public String getNoOfPlatforms() {
        return noOfPlatforms;
    }

    public void setNoOfPlatforms(String noOfPlatforms) {
        this.noOfPlatforms = noOfPlatforms;
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

    public List<OleGokbUpdateLog> getOleGokbUpdateLogList() {
        return oleGokbUpdateLogList;
    }

    public void setOleGokbUpdateLogList(List<OleGokbUpdateLog> oleGokbUpdateLogList) {

        this.oleGokbUpdateLogList = oleGokbUpdateLogList;
    }
}
