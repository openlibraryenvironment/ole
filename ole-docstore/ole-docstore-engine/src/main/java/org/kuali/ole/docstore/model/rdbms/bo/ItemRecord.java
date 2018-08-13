package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/28/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemRecord extends PersistableBusinessObjectBase
        implements Serializable {


    private String itemId;
    private Boolean staffOnlyFlag;
    private String barCodeArsl;
    private String barCode;
    private String statisticalSearchId;
    private String itemTypeId;
    private String tempItemTypeId;
    private String enumeration;
    private String chronology;
    private String copyNumber;
    private String uri;
    private String numberOfPieces;
    private String descriptionOfPieces;
    private String instanceId;
    private String uniqueIdPrefix;
    private String purchaseOrderItemLineId;
    private String donorCode;
    private String donorPublicDisplay;
    private String donorNote;
    private String vendorLineItemId;
    private String price;
    private String fund;
    private String itemStatusId;
    private String checkInNote;
    private Timestamp effectiveDate;
    private String staffOnly;
    private Boolean fastAddFlag;
    private String location;
    private String locationLevel;
    private String callNumberPrefix;
    private String callNumber;
    private String shelvingOrder;
    private String callNumberTypeId;
    private String currentBorrower;
    private String proxyBorrower;
    private Timestamp dueDateTime;
    private Boolean claimsReturnedFlag;
    private Timestamp claimsReturnedFlagCreateDate;
    private String claimsReturnedNote;


    private ItemStatusRecord itemStatusRecord;
    private CallNumberTypeRecord callNumberTypeRecord;
    private ItemTypeRecord itemTypeRecord;
    private ItemTypeRecord itemTempTypeRecord;
    private StatisticalSearchRecord statisticalSearchRecord;
    private List<FormerIdentifierRecord> formerIdentifierRecords;
    private List<ItemNoteRecord> itemNoteRecords;
    private List<LocationsCheckinCountRecord> locationsCheckinCountRecords;
    private List<MissingPieceItemRecord> missingPieceItemRecordList;

    public List<MissingPieceItemRecord> getMissingPieceItemRecordList() {
        return missingPieceItemRecordList;
    }

    public void setMissingPieceItemRecordList(List<MissingPieceItemRecord> missingPieceItemRecordList) {
        this.missingPieceItemRecordList = missingPieceItemRecordList;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Boolean getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(Boolean staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public String getBarCodeArsl() {
        return barCodeArsl;
    }

    public void setBarCodeArsl(String barCodeArsl) {
        this.barCodeArsl = barCodeArsl;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getStatisticalSearchId() {
        return statisticalSearchId;
    }

    public void setStatisticalSearchId(String statisticalSearchId) {
        this.statisticalSearchId = statisticalSearchId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getTempItemTypeId() {
        return tempItemTypeId;
    }

    public void setTempItemTypeId(String tempItemTypeId) {
        this.tempItemTypeId = tempItemTypeId;
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

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(String numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public String getDescriptionOfPieces() {
        return descriptionOfPieces;
    }

    public void setDescriptionOfPieces(String descriptionOfPieces) {
        this.descriptionOfPieces = descriptionOfPieces;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }

    public String getPurchaseOrderItemLineId() {
        return purchaseOrderItemLineId;
    }

    public void setPurchaseOrderItemLineId(String purchaseOrderItemLineId) {
        this.purchaseOrderItemLineId = purchaseOrderItemLineId;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getDonorPublicDisplay() {
        return donorPublicDisplay;
    }

    public void setDonorPublicDisplay(String donorPublicDisplay) {
        this.donorPublicDisplay = donorPublicDisplay;
    }

    public String getDonorNote() {
        return donorNote;
    }

    public void setDonorNote(String donorNote) {
        this.donorNote = donorNote;
    }

    public String getVendorLineItemId() {
        return vendorLineItemId;
    }

    public void setVendorLineItemId(String vendorLineItemId) {
        this.vendorLineItemId = vendorLineItemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getItemStatusId() {
        return itemStatusId;
    }

    public void setItemStatusId(String itemStatusId) {
        this.itemStatusId = itemStatusId;
    }

    public ItemStatusRecord getItemStatusRecord() {
        return itemStatusRecord;
    }

    public void setItemStatusRecord(ItemStatusRecord itemStatusRecord) {
        this.itemStatusRecord = itemStatusRecord;
    }

    public String getCheckInNote() {
        return checkInNote;
    }

    public void setCheckInNote(String checkInNote) {
        this.checkInNote = checkInNote;
    }

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

/*    public String getFastAddFlag() {
        return fastAddFlag;
    }

    public void setFastAddFlag(String fastAddFlag) {
        this.fastAddFlag = fastAddFlag;
    }*/

    public Boolean getFastAddFlag() {
        return fastAddFlag;
    }

    public void setFastAddFlag(Boolean fastAddFlag) {
        this.fastAddFlag = fastAddFlag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(String locationLevel) {
        this.locationLevel = locationLevel;
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

    public String getShelvingOrder() {
        return shelvingOrder;
    }

    public void setShelvingOrder(String shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }

    public String getCallNumberTypeId() {
        return callNumberTypeId;
    }

    public void setCallNumberTypeId(String callNumberTypeId) {
        this.callNumberTypeId = callNumberTypeId;
    }

    public CallNumberTypeRecord getCallNumberTypeRecord() {
        return callNumberTypeRecord;
    }

    public void setCallNumberTypeRecord(CallNumberTypeRecord callNumberTypeRecord) {
        this.callNumberTypeRecord = callNumberTypeRecord;
    }

    public ItemTypeRecord getItemTypeRecord() {
        return itemTypeRecord;
    }

    public void setItemTypeRecord(ItemTypeRecord itemTypeRecord) {
        this.itemTypeRecord = itemTypeRecord;
    }

    public ItemTypeRecord getItemTempTypeRecord() {
        return itemTempTypeRecord;
    }

    public void setItemTempTypeRecord(ItemTypeRecord itemTempTypeRecord) {
        this.itemTempTypeRecord = itemTempTypeRecord;
    }

    public StatisticalSearchRecord getStatisticalSearchRecord() {
        return statisticalSearchRecord;
    }

    public void setStatisticalSearchRecord(StatisticalSearchRecord statisticalSearchRecord) {
        this.statisticalSearchRecord = statisticalSearchRecord;
    }

    public List<ItemNoteRecord> getItemNoteRecords() {
        return itemNoteRecords;
    }

    public void setItemNoteRecords(List<ItemNoteRecord> itemNoteRecords) {
        this.itemNoteRecords = itemNoteRecords;
    }

    public List<LocationsCheckinCountRecord> getLocationsCheckinCountRecords() {
        return locationsCheckinCountRecords;
    }

    public void setLocationsCheckinCountRecords(List<LocationsCheckinCountRecord> locationsCheckinCountRecords) {
        this.locationsCheckinCountRecords = locationsCheckinCountRecords;
    }

    public List<FormerIdentifierRecord> getFormerIdentifierRecords() {
        return formerIdentifierRecords;
    }

    public void setFormerIdentifierRecords(List<FormerIdentifierRecord> formerIdentifierRecords) {
        this.formerIdentifierRecords = formerIdentifierRecords;
    }


    public String getCurrentBorrower() {
        return currentBorrower;
    }

    public void setCurrentBorrower(String currentBorrower) {
        this.currentBorrower = currentBorrower;
    }

    public String getProxyBorrower() {
        return proxyBorrower;
    }

    public void setProxyBorrower(String proxyBorrower) {
        this.proxyBorrower = proxyBorrower;
    }


    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    public void setClaimsReturnedNote(String claimsReturnedNote) {
        this.claimsReturnedNote = claimsReturnedNote;
    }

    public Timestamp getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Timestamp dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public Timestamp getClaimsReturnedFlagCreateDate() {
        return claimsReturnedFlagCreateDate;
    }

    public void setClaimsReturnedFlagCreateDate(Timestamp claimsReturnedFlagCreateDate) {
        this.claimsReturnedFlagCreateDate = claimsReturnedFlagCreateDate;
    }

    public Boolean getClaimsReturnedFlag() {
        return claimsReturnedFlag;
    }

    public void setClaimsReturnedFlag(Boolean claimsReturnedFlag) {
        this.claimsReturnedFlag = claimsReturnedFlag;
    }



}