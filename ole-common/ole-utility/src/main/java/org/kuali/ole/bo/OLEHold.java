package org.kuali.ole.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/20/13
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHold {
    private String itemId;
    private String itemType;
    private String catalogueId;
    private String title;
    private String author;
    private String requestId;
    private String availableStatus;
    private String recallStatus;
    private String requestExpiryDate;
    private String createDate;
    private String holdExpiryDate;
    private String priority;
    private String type;
    private String pickupLocation;
    private String dateRecalled;
    private String requestType;
    private String availableDate;
    private String copyNumber;
    private String callNumber;
    private String volumeNumber;
    private String dateAvailableExpires;
    private boolean reserve;

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogId) {
        this.catalogueId = catalogId;
    }

    public String getRequestExpiryDate() {
        return requestExpiryDate;
    }

    public void setRequestExpiryDate(String requestExpiryDate) {
        this.requestExpiryDate = requestExpiryDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDateRecalled() {
        return dateRecalled;
    }

    public void setDateRecalled(String dateRecalled) {
        this.dateRecalled = dateRecalled;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getRecallStatus() {
        return recallStatus;
    }

    public void setRecallStatus(String recallStatus) {
        this.recallStatus = recallStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDateAvailableExpires() {
        return dateAvailableExpires;
    }

    public void setDateAvailableExpires(String dateAvailableExpires) {
        this.dateAvailableExpires = dateAvailableExpires;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public boolean isReserve() {
        return reserve;
    }

    public void setReserve(boolean reserve) {
        this.reserve = reserve;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHoldExpiryDate() {
        return holdExpiryDate;
    }

    public void setHoldExpiryDate(String holdExpiryDate) {
        this.holdExpiryDate = holdExpiryDate;
    }
}
