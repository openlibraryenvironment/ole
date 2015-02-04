package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.PHoldings;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 8/1/13
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWorkHoldingsDocument extends PHoldings {

    private boolean select;
    private String bibTitle;
    private String bibIdentifier;
    private String staffOnly;
    private String holdingsIdentifier;
    private String localId;
    private String instanceIdentifier;
    private String locationName;
    private String callNumber;
    private String callNumberPrefix;
    private String callNumberType;
    private String copyNumber;
    private String linkedBibCount;
    private List<String> bibUUIDList;
    private String staffOnlyFlag;
    protected String dateEntered;
    protected String dateUpdated;
    protected String createdBy;
    protected String updatedBy;

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallNumberType() {
        return callNumberType;
    }

    public void setCallNumberType(String callNumberType) {
        this.callNumberType = callNumberType;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
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

    public String getBibTitle() {
        return bibTitle;
    }

    public void setBibTitle(String bibTitle) {
        this.bibTitle = bibTitle;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


}
