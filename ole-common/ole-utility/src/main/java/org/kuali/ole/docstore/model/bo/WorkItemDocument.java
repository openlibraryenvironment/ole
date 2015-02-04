package org.kuali.ole.docstore.model.bo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 3/30/12
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkItemDocument extends OleDocument {
    private String localId;
    private String itemIdentifier;
    private String bibIdentifier;
    private String callNumber;
    private String location;
    private String shelvingOrder;
    private String barcode;
    private String copyNumber;
    private String enumeration;
    private String chronology;
    private String locationName;
    private String itemType;
    private String instanceIdentifier;
    private String volumeNumber;
    private String itemStatus;
    private String callNumberPrefix;
    private String callNumberType;
    private String bibTitle;
    private String imprint;
    private String prefixAndCallnumber;
    private String PoLineItemId;
    private String linkedBibCount;
    private String donorCode;
    private String bibAuthor;
    private List<String> bibUUIDList;
    private String staffOnlyFlag;
    protected String dateEntered;
    protected String dateUpdated;
    protected String createdBy;
    protected String updatedBy;

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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getPoLineItemId() {
        return PoLineItemId;
    }

    public void setPoLineItemId(String poLineItemId) {
        PoLineItemId = poLineItemId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getPrefixAndCallnumber() {
        return prefixAndCallnumber;
    }

    public void setPrefixAndCallnumber(String prefixAndCallnumber) {
        this.prefixAndCallnumber = prefixAndCallnumber;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getBibAuthor() {
        return bibAuthor;
    }

    public void setBibAuthor(String bibAuthor) {
        this.bibAuthor = bibAuthor;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
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

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getShelvingOrder() {
        return shelvingOrder;
    }

    public void setShelvingOrder(String shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
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

    @Override
    public String toString() {
        return "WorkItemDocument{" +
                "localId='" + localId + '\'' +
                ", dateEntered=" + dateEntered +
                ", dateUpdated=" + dateUpdated +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", itemIdentifier='" + itemIdentifier + '\'' +
                ", bibIdentifier='" + bibIdentifier + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", location='" + location + '\'' +
                ", shelvingOrder='" + shelvingOrder + '\'' +
                ", barcode='" + barcode + '\'' +
                ", copyNumber='" + copyNumber + '\'' +
                ", enumeration='" + enumeration + '\'' +
                ", chronology='" + chronology + '\'' +
                ", locationName='" + locationName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", instanceIdentifier='" + instanceIdentifier + '\'' +
                ", volumeNumber='" + volumeNumber + '\'' +
                ", itemStatus='" + itemStatus + '\'' +
                ", callNumberPrefix='" + callNumberPrefix + '\'' +
                ", callNumberType='" + callNumberType + '\'' +
                ", bibTitle='" + bibTitle + '\'' +
                ", linkedBibCount='" + linkedBibCount + '\'' +
                ", bibUUIDList=" + bibUUIDList +
                '}';
    }
}
