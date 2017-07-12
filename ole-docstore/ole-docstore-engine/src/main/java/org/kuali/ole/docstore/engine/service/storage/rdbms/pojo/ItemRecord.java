package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.ole.audit.AuditField;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
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
    @AuditField
    private Boolean staffOnlyFlag;

    private String barCodeArsl;
    @AuditField
    private String barCode;
    private String statisticalSearchId;
    @AuditField
    private String itemTypeId;
    private String tempItemTypeId;
    @AuditField
    private String enumeration;
    private String volumeNumber;
    @AuditField
    private String chronology;
    @AuditField
    private String copyNumber;
    private String uri;
    private String numberOfPieces;
    private String descriptionOfPieces;
    private String holdingsId;
    private String uniqueIdPrefix;
    private String purchaseOrderItemLineId;
    private String vendorLineItemId;
    private String price;
    private String fund;
    private String itemStatusId;
    private String checkInNote;
    private Timestamp effectiveDate;
    private String staffOnly;
    @AuditField
    private Boolean fastAddFlag;
    @AuditField
    private String location;
    @AuditField
    private String locationLevel;
    private String callNumberPrefix;
    @AuditField
    private String callNumber;
    private String shelvingOrder;
    private String callNumberTypeId;
    private String highDensityStorageId;
    @AuditField
    private String currentBorrower;
    @AuditField
    private String proxyBorrower;
    @AuditField
    private Timestamp dueDateTime;
    @AuditField
    private Timestamp checkOutDateTime;
    private Boolean claimsReturnedFlag;
    private Timestamp claimsReturnedFlagCreateDate;
    private String claimsReturnedNote;
    private ItemStatusRecord itemStatusRecord;
    private CallNumberTypeRecord callNumberTypeRecord;
    private ItemTypeRecord itemTypeRecord;
    private ItemTypeRecord itemTempTypeRecord;
    private HighDensityStorageRecord highDensityStorageRecord;
    private List<FormerIdentifierRecord> formerIdentifierRecords;
    private List<ItemNoteRecord> itemNoteRecords;
    private List<LocationsCheckinCountRecord> locationsCheckinCountRecords;
    private String damagedItemNote;
    private String itemLostNote;
    private String itemReplaceNote;
    private boolean itemDamagedStatus;
    private boolean missingPieceFlag;
    private String missingPiecesCount;
    private Timestamp missingPieceEffectiveDate;
    private String missingPieceFlagNote;
    private List<OLEItemDonorRecord> donorList;
    private Timestamp createdDate;
    @AuditField
    private Timestamp updatedDate;
    private String createdBy;
    @AuditField
    private String updatedBy;
    private List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords;
    private List<HoldingsItemRecord> holdingsItemRecords;
    private int numberOfRenew;
    private List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords;
    private List<ItemDamagedRecord> itemDamagedRecords;
    private List<MissingPieceItemRecord> missingPieceItemRecordList;
    @AuditField
    private Timestamp originalDueDate;
    private HoldingsRecord holdingsRecord;
    private String operationType;

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

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
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

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
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

    public String getHighDensityStorageId() {
        return highDensityStorageId;
    }

    public void setHighDensityStorageId(String highDensityStorageId) {
        this.highDensityStorageId = highDensityStorageId;
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

    public HighDensityStorageRecord getHighDensityStorageRecord() {
        return highDensityStorageRecord;
    }

    public void setHighDensityStorageRecord(HighDensityStorageRecord highDensityStorageRecord) {
        this.highDensityStorageRecord = highDensityStorageRecord;
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

    public Timestamp getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(Timestamp checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
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

    public boolean isItemDamagedStatus() {
        return itemDamagedStatus;
    }

    public void setItemDamagedStatus(boolean itemDamagedStatus) {
        this.itemDamagedStatus = itemDamagedStatus;
    }

    public boolean isMissingPieceFlag() {
        return missingPieceFlag;
    }

    public void setMissingPieceFlag(boolean missingPieceFlag) {
        this.missingPieceFlag = missingPieceFlag;
    }

    public String getMissingPiecesCount() {
        return missingPiecesCount;
    }

    public void setMissingPiecesCount(String missingPiecesCount) {
        this.missingPiecesCount = missingPiecesCount;
    }

    public Timestamp getMissingPieceEffectiveDate() {
        return missingPieceEffectiveDate;
    }

    public void setMissingPieceEffectiveDate(Timestamp missingPieceEffectiveDate) {
        this.missingPieceEffectiveDate = missingPieceEffectiveDate;
    }

    public String getDamagedItemNote() {
        return damagedItemNote;
    }

    public void setDamagedItemNote(String damagedItemNote) {
        this.damagedItemNote = damagedItemNote;
    }

    public String getMissingPieceFlagNote() {
        return missingPieceFlagNote;
    }

    public void setMissingPieceFlagNote(String missingPieceFlagNote) {
        this.missingPieceFlagNote = missingPieceFlagNote;
    }

    public List<OLEItemDonorRecord> getDonorList() {
        return donorList;
    }

    public void setDonorList(List<OLEItemDonorRecord> donorList) {
        this.donorList = donorList;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
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

    public List<ItemStatisticalSearchRecord> getItemStatisticalSearchRecords() {
        return itemStatisticalSearchRecords;
    }

    public void setItemStatisticalSearchRecords(List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords) {
        this.itemStatisticalSearchRecords = itemStatisticalSearchRecords;
    }

    public List<HoldingsItemRecord> getHoldingsItemRecords() {
        return holdingsItemRecords;
    }

    public void setHoldingsItemRecords(List<HoldingsItemRecord> holdingsItemRecords) {
        this.holdingsItemRecords = holdingsItemRecords;
    }

    public int getNumberOfRenew() {
        return numberOfRenew;
    }

    public void setNumberOfRenew(int numberOfRenew) {
        this.numberOfRenew = numberOfRenew;
    }

    public List<ItemClaimsReturnedRecord> getItemClaimsReturnedRecords() {
        return itemClaimsReturnedRecords;
    }

    public void setItemClaimsReturnedRecords(List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords) {
        this.itemClaimsReturnedRecords = itemClaimsReturnedRecords;
    }

    public List<ItemDamagedRecord> getItemDamagedRecords() {
        return itemDamagedRecords;
    }

    public void setItemDamagedRecords(List<ItemDamagedRecord> itemDamagedRecords) {
        this.itemDamagedRecords = itemDamagedRecords;
    }

    public Timestamp getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(Timestamp originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public String getItemLostNote() {
        return itemLostNote;
    }

    public void setItemLostNote(String itemLostNote) {
        this.itemLostNote = itemLostNote;
    }

    public String getItemReplaceNote() {
        return itemReplaceNote;
    }

    public void setItemReplaceNote(String itemReplaceNote) {
        this.itemReplaceNote = itemReplaceNote;
    }

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}