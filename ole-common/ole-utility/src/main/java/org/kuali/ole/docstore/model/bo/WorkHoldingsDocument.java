package org.kuali.ole.docstore.model.bo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 3/30/12
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkHoldingsDocument {
    private String holdingsIdentifier;
    private String bibIdentifier;
    private String localId;
    private String instanceIdentifier;
    private String locationName;
    private String callNumber;
    private String callNumberPrefix;
    private String callNumberType;
    private String copyNumber;
    private String bibTitle;
    private String linkedBibCount;
    private List<String> bibUUIDList;
    private String staffOnlyFlag;
    protected String dateEntered;
    protected String dateUpdated;
    protected String createdBy;
    protected String updatedBy;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(String staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public String getCallNumberType() {
        return callNumberType;
    }

    public void setCallNumberType(String callNumberType) {
        this.callNumberType = callNumberType;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getBibTitle() {
        return bibTitle;
    }

    public void setBibTitle(String bibTitle) {
        this.bibTitle = bibTitle;
    }

    public String getLinkedBibCount() {
        return linkedBibCount;
    }

    public void setLinkedBibCount(String linkedBibCount) {
        this.linkedBibCount = linkedBibCount;
    }

    public List<String> getBibUUIDList() {
        return bibUUIDList;
    }

    public void setBibUUIDList(List<String> bibUUIDList) {
        this.bibUUIDList = bibUUIDList;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "WorkHoldingsDocument{" +
                "localId='" + localId + '\'' +
                ", dateEntered=" + dateEntered +
                ", dateUpdated=" + dateUpdated +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", holdingsIdentifier='" + holdingsIdentifier + '\'' +
                ", bibIdentifier='" + bibIdentifier + '\'' +
                ", instanceIdentifier='" + instanceIdentifier + '\'' +
                ", locationName='" + locationName + '\'' +
                ", copyNumber='" + copyNumber + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", callNumberPrefix='" + callNumberPrefix + '\'' +
                ", callNumberType='" + callNumberType + '\'' +
                ", bibTitle='" + bibTitle + '\'' +
                ", linkedBibCount='" + linkedBibCount + '\'' +
                ", bibUUIDList=" + bibUUIDList +
                '}';
    }
}
