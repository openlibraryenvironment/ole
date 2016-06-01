package org.kuali.ole.deliver.bo;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 1/13/15.
 */
public class OLESingleItemResultDisplayRow {

    private String id;
    private String location;
    private String itemType;
    private String itemStatus;
    private String itemBarCode;
    private String enumeration;
    private String chronology;
    private String bibIdentifier;
    private String holdingsIdentifier;
    private String callNumber;
    private String title;
    private String author;
    private String publication;
    private String itemStatusDate;
    private Timestamp lastBorrowerItemStatusDate;
    private String dueDate;
    private String noOfPieces;
    private String holdingsLocation;
    private boolean placeRequest;
    private String oclcNumber;
    private String isbn;
    private String itemNoteValue;
    private String itemNoteType;

    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;

    private String currentBorrowerId;
    private String patronBarcode;
    private String lastBorrowerType;
    private String lastBorrowerExpDate;
    private String patronType;
    private String patronExpDate;
    private String patronFirstName;
    private String patronMiddleName;
    private String patronLastName;
    private String lastBorrowerFirstName;
    private String lastBorrowerMiddleName;
    private String lastBorrowerLastName;
    private String patronURL;
    private String lastBorrowerURL;
    private String proxyBorrowerId;
    private String proxyPatronBarcode;
    private String proxyPatronName;
    private String proxyPatronURL;

    private String claimsReturnedFlag;
    private String claimsReturnedNote;
    private String claimsReturnedDate;

    private String missingPieceFlag;
    private String missingPieceNote;
    private String missingPieceCount;

    private String itemDamagedStatusFlag;
    private String itemDamagedNote;

    private String requestTypeCode;
    private String pickUpLocationCode;
    private String finalStatus;
    private Date finalStatusDate;
    private String originalDueDate;
    private String lastBorrower;
    private Timestamp lastCheckinDate;
    private Timestamp createDate;

    private List<OLEDeliverRequestResultDisplayRow> oleDeliverRequestResultDisplayRowList = new ArrayList<>();
    private List<OLEFeeTypeResultDisplayRow> oleFeeTypeResultDisplayRowList = new ArrayList<>();
    private List<OLEItemNoteResultDisplayRow> oleItemNoteResultDisplayRowList = new ArrayList<>();
    private List<OLEHoldingsSearchResultDisplayRow> oleHoldingsSearchResultDisplayRowList = new ArrayList<>();
    private List<OLEReturnHistoryRecord> oleReturnHistoryRecords = new ArrayList<>();
    private List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = new ArrayList<>();
    private List<ItemDamagedRecord> itemDamagedRecords = new ArrayList<>();
    private List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList = new ArrayList<>();

    public List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> getMissingPieceItemRecordList() {
        return missingPieceItemRecordList;
    }

    public void setMissingPieceItemRecordList(List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList) {
        this.missingPieceItemRecordList = missingPieceItemRecordList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemBarCode() {
        return itemBarCode;
    }

    public void setItemBarCode(String itemBarCode) {
        this.itemBarCode = itemBarCode;
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

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
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

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getItemStatusDate() {
        return itemStatusDate;
    }

    public void setItemStatusDate(String itemStatusDate) {
        this.itemStatusDate = itemStatusDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNoOfPieces() {
        return noOfPieces;
    }

    public void setNoOfPieces(String noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public String getHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(String holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public boolean isPlaceRequest() {
        return placeRequest;
    }

    public void setPlaceRequest(boolean placeRequest) {
        this.placeRequest = placeRequest;
    }

    public String getOclcNumber() {
        return oclcNumber;
    }

    public void setOclcNumber(String oclcNumber) {
        this.oclcNumber = oclcNumber;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getItemNoteValue() {
        return itemNoteValue;
    }

    public void setItemNoteValue(String itemNoteValue) {
        this.itemNoteValue = itemNoteValue;
    }

    public String getItemNoteType() {
        return itemNoteType;
    }

    public void setItemNoteType(String itemNoteType) {
        this.itemNoteType = itemNoteType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCurrentBorrowerId() {
        return currentBorrowerId;
    }

    public void setCurrentBorrowerId(String currentBorrowerId) {
        this.currentBorrowerId = currentBorrowerId;
    }

    public String getProxyBorrowerId() {
        return proxyBorrowerId;
    }

    public void setProxyBorrowerId(String proxyBorrowerId) {
        this.proxyBorrowerId = proxyBorrowerId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getPatronType() {
        return patronType;
    }

    public void setPatronType(String patronType) {
        this.patronType = patronType;
    }

    public String getPatronExpDate() {
        return patronExpDate;
    }

    public void setPatronExpDate(String patronExpDate) {
        this.patronExpDate = patronExpDate;
    }

    public String getPatronFirstName() {
        return patronFirstName;
    }

    public void setPatronFirstName(String patronFirstName) {
        this.patronFirstName = patronFirstName;
    }

    public String getPatronMiddleName() {
        return patronMiddleName;
    }

    public void setPatronMiddleName(String patronMiddleName) {
        this.patronMiddleName = patronMiddleName;
    }

    public String getPatronLastName() {
        return patronLastName;
    }

    public void setPatronLastName(String patronLastName) {
        this.patronLastName = patronLastName;
    }

    public String getProxyPatronBarcode() {
        return proxyPatronBarcode;
    }

    public void setProxyPatronBarcode(String proxyPatronBarcode) {
        this.proxyPatronBarcode = proxyPatronBarcode;
    }

    public String getProxyPatronName() {
        return proxyPatronName;
    }

    public void setProxyPatronName(String proxyPatronName) {
        this.proxyPatronName = proxyPatronName;
    }

    public String getPatronURL() {
        return patronURL;
    }

    public void setPatronURL(String patronURL) {
        this.patronURL = patronURL;
    }

    public String getProxyPatronURL() {
        return proxyPatronURL;
    }

    public void setProxyPatronURL(String proxyPatronURL) {
        this.proxyPatronURL = proxyPatronURL;
    }

    public List<OLEDeliverRequestResultDisplayRow> getOleDeliverRequestResultDisplayRowList() {
        return oleDeliverRequestResultDisplayRowList;
    }

    public void setOleDeliverRequestResultDisplayRowList(List<OLEDeliverRequestResultDisplayRow> oleDeliverRequestResultDisplayRowList) {
        this.oleDeliverRequestResultDisplayRowList = oleDeliverRequestResultDisplayRowList;
    }

    public List<OLEFeeTypeResultDisplayRow> getOleFeeTypeResultDisplayRowList() {
        return oleFeeTypeResultDisplayRowList;
    }

    public void setOleFeeTypeResultDisplayRowList(List<OLEFeeTypeResultDisplayRow> oleFeeTypeResultDisplayRowList) {
        this.oleFeeTypeResultDisplayRowList = oleFeeTypeResultDisplayRowList;
    }

    public List<OLEItemNoteResultDisplayRow> getOleItemNoteResultDisplayRowList() {
        return oleItemNoteResultDisplayRowList;
    }

    public void setOleItemNoteResultDisplayRowList(List<OLEItemNoteResultDisplayRow> oleItemNoteResultDisplayRowList) {
        this.oleItemNoteResultDisplayRowList = oleItemNoteResultDisplayRowList;
    }

    public String getClaimsReturnedFlag() {
        return claimsReturnedFlag;
    }

    public void setClaimsReturnedFlag(String claimsReturnedFlag) {
        this.claimsReturnedFlag = claimsReturnedFlag;
    }

    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    public void setClaimsReturnedNote(String claimsReturnedNote) {
        this.claimsReturnedNote = claimsReturnedNote;
    }

    public String getClaimsReturnedDate() {
        return claimsReturnedDate;
    }

    public void setClaimsReturnedDate(String claimsReturnedDate) {
        this.claimsReturnedDate = claimsReturnedDate;
    }

    public String getMissingPieceFlag() {
        return missingPieceFlag;
    }

    public void setMissingPieceFlag(String missingPieceFlag) {
        this.missingPieceFlag = missingPieceFlag;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public String getMissingPieceCount() {
        return missingPieceCount;
    }

    public void setMissingPieceCount(String missingPieceCount) {
        this.missingPieceCount = missingPieceCount;
    }

    public String getItemDamagedStatusFlag() {
        return itemDamagedStatusFlag;
    }

    public void setItemDamagedStatusFlag(String itemDamagedStatusFlag) {
        this.itemDamagedStatusFlag = itemDamagedStatusFlag;
    }

    public String getItemDamagedNote() {
        return itemDamagedNote;
    }

    public void setItemDamagedNote(String itemDamagedNote) {
        this.itemDamagedNote = itemDamagedNote;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getPickUpLocationCode() {
        return pickUpLocationCode;
    }

    public void setPickUpLocationCode(String pickUpLocationCode) {
        this.pickUpLocationCode = pickUpLocationCode;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Date getFinalStatusDate() {
        return finalStatusDate;
    }

    public void setFinalStatusDate(Date finalStatusDate) {
        this.finalStatusDate = finalStatusDate;
    }

    public String getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(String originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public List<OLEHoldingsSearchResultDisplayRow> getOleHoldingsSearchResultDisplayRowList() {
        return oleHoldingsSearchResultDisplayRowList;
    }

    public void setOleHoldingsSearchResultDisplayRowList(List<OLEHoldingsSearchResultDisplayRow> oleHoldingsSearchResultDisplayRowList) {
        this.oleHoldingsSearchResultDisplayRowList = oleHoldingsSearchResultDisplayRowList;
    }

    public List<OLEReturnHistoryRecord> getOleReturnHistoryRecords() {
        return oleReturnHistoryRecords;
    }

    public void setOleReturnHistoryRecords(List<OLEReturnHistoryRecord> oleReturnHistoryRecords) {
        this.oleReturnHistoryRecords = oleReturnHistoryRecords;
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

    public String getLastBorrower() {
        return lastBorrower;
    }

    public void setLastBorrower(String lastBorrower) {
        this.lastBorrower = lastBorrower;
    }

    public Timestamp getLastCheckinDate() {
        return lastCheckinDate;
    }

    public void setLastCheckinDate(Timestamp lastCheckinDate) {
        this.lastCheckinDate = lastCheckinDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getLastBorrowerFirstName() {
        return lastBorrowerFirstName;
    }

    public void setLastBorrowerFirstName(String lastBorrowerFirstName) {
        this.lastBorrowerFirstName = lastBorrowerFirstName;
    }

    public String getLastBorrowerMiddleName() {
        return lastBorrowerMiddleName;
    }

    public void setLastBorrowerMiddleName(String lastBorrowerMiddleName) {
        this.lastBorrowerMiddleName = lastBorrowerMiddleName;
    }

    public String getLastBorrowerLastName() {
        return lastBorrowerLastName;
    }

    public void setLastBorrowerLastName(String lastBorrowerLastName) {
        this.lastBorrowerLastName = lastBorrowerLastName;
    }

    public String getLastBorrowerType() {
        return lastBorrowerType;
    }

    public void setLastBorrowerType(String lastBorrowerType) {
        this.lastBorrowerType = lastBorrowerType;
    }

    public String getLastBorrowerExpDate() {
        return lastBorrowerExpDate;
    }

    public void setLastBorrowerExpDate(String lastBorrowerExpDate) {
        this.lastBorrowerExpDate = lastBorrowerExpDate;
    }

    public String getLastBorrowerURL() {
        return lastBorrowerURL;
    }

    public void setLastBorrowerURL(String lastBorrowerURL) {
        this.lastBorrowerURL = lastBorrowerURL;
    }

    public Timestamp getLastBorrowerItemStatusDate() {
        return lastBorrowerItemStatusDate;
    }

    public void setLastBorrowerItemStatusDate(Timestamp lastBorrowerItemStatusDate) {
        this.lastBorrowerItemStatusDate = lastBorrowerItemStatusDate;
    }
}
