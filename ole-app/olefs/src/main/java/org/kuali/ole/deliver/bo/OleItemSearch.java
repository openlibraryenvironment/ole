package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/3/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleItemSearch extends PersistableBusinessObjectBase {

    private String title;
    private String author;
    private String itemBarCode;
    private String publisher;
    private String itemType;
    private String callNumber;
    private String callNumberPrefix;
    private String shelvingLocation;
    private String copyNumber;
    private String itemStatus;
    private String volumeNumber;
    private String itemUUID;
    private String instanceUUID;
    private String bibUUID;
    private String holdingUUID;
    private String pageDisplay;
    private String totalRec;
    private String pageSize;
    private String nextFlag;
    private String previousFlag;
    private String startIndex;
    private String chronology;
    private String enumeration;


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

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public String getPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(String previousFlag) {
        this.previousFlag = previousFlag;
    }

    public String getTotalRec() {
        return totalRec;
    }

    public void setTotalRec(String totalRec) {
        this.totalRec = totalRec;
    }

    public String getPageDisplay() {
        return pageDisplay;
    }

    public void setPageDisplay(String pageDisplay) {
        this.pageDisplay = pageDisplay;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public String getInstanceUUID() {
        return instanceUUID;
    }

    public void setInstanceUUID(String instanceUUID) {
        this.instanceUUID = instanceUUID;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public String getHoldingUUID() {
        return holdingUUID;
    }

    public void setHoldingUUID(String holdingUUID) {
        this.holdingUUID = holdingUUID;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getItemBarCode() {
        return itemBarCode;
    }

    public void setItemBarCode(String itemBarCode) {
        this.itemBarCode = itemBarCode;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
