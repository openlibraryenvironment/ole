package org.kuali.ole.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sundarr
 * Date: 7/3/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingRecord {
    private String serialReceivingRecordId;
    private String boundLocation;
    private String callNumber;
    private String issn;
    private String title;
    private String unboundLocation;
    private String subscriptionStatus;
    private String checkInWorkUnit;
    private String action;
    private String bibId;
    private String href;
    private String instanceId;
    private String staffOnlyFlag;
    private String staffOnlyFlagStyle;

    public String getStaffOnlyFlagStyle() {
        return staffOnlyFlagStyle;
    }

    public void setStaffOnlyFlagStyle(String staffOnlyFlagStyle) {
        this.staffOnlyFlagStyle = staffOnlyFlagStyle;
    }

    public String getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(String staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSerialReceivingRecordId() {
        return serialReceivingRecordId;
    }

    public void setSerialReceivingRecordId(String serialReceivingRecordId) {
        this.serialReceivingRecordId = serialReceivingRecordId;
    }

    public String getBoundLocation() {
        return boundLocation;
    }

    public void setBoundLocation(String boundLocation) {
        this.boundLocation = boundLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getUnboundLocation() {
        return unboundLocation;
    }

    public void setUnboundLocation(String unboundLocation) {
        this.unboundLocation = unboundLocation;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getCheckInWorkUnit() {
        return checkInWorkUnit;
    }

    public void setCheckInWorkUnit(String checkInWorkUnit) {
        this.checkInWorkUnit = checkInWorkUnit;
    }
}
