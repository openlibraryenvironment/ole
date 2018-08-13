package org.kuali.ole.model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by sheiks 10/11/16.
 */
@Entity
@Table(name="ole_ds_item_t")
@NamedQuery(name="ItemRecord.findAll", query="SELECT o FROM ItemRecord o")
public class ItemRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_ID")
	private Integer itemId;

	private String barcode;

	@Column(name="BARCODE_ARSL")
	private String barcodeArsl;

	@Column(name="CALL_NUMBER")
	private String callNumber;

	@Column(name="CALL_NUMBER_PREFIX")
	private String callNumberPrefix;

	@Column(name="CALL_NUMBER_TYPE_ID")
	private Integer callNumberTypeId;

	@Column(name="CHECK_IN_NOTE")
	private String checkInNote;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECK_OUT_DATE_TIME")
	private Date checkOutDateTime;

	private String chronology;

	@Column(name="CLAIMS_RETURNED")
	private String claimsReturned;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLAIMS_RETURNED_DATE_CREATED")
	private Date claimsReturnedDateCreated;

	@Column(name="CLAIMS_RETURNED_NOTE")
	private String claimsReturnedNote;

	@Column(name="COPY_NUMBER")
	private String copyNumber;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CURRENT_BORROWER")
	private String currentBorrower;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_CREATED")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPDATED")
	private Date dateUpdated;

	@Column(name="DESC_OF_PIECES")
	private String descOfPieces;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DUE_DATE_TIME")
	private Date dueDateTime;

	private String enumeration;

	@Column(name="FAST_ADD")
	private String fastAdd;

	private String fund;

	@Column(name="ITEM_DAMAGED_NOTE")
	private String itemDamagedNote;

	@Column(name="ITEM_DAMAGED_STATUS")
	private String itemDamagedStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ITEM_STATUS_DATE_UPDATED")
	private Date itemStatusDateUpdated;

	@Column(name="ITEM_STATUS_ID")
	private Integer itemStatusId;

	@Column(name="ITEM_TYPE_ID")
	private Integer itemTypeId;

	private String location;

	@Column(name="LOCATION_ID")
	private Integer locationId;

	@Column(name="LOCATION_LEVEL")
	private String locationLevel;

	@Column(name="MISSING_PIECES")
	private String missingPieces;

	@Column(name="MISSING_PIECES_COUNT")
	private Integer missingPiecesCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MISSING_PIECES_EFFECTIVE_DATE")
	private Date missingPiecesEffectiveDate;

	@Column(name="MISSING_PIECES_NOTE")
	private String missingPiecesNote;

	@Column(name="NUM_OF_RENEW")
	private Integer numOfRenew;

	@Column(name="NUM_PIECES")
	private String numPieces;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ORG_DUE_DATE_TIME")
	private Date orgDueDateTime;

	private BigDecimal price;

	@Column(name="PROXY_BORROWER")
	private String proxyBorrower;

	@Column(name="PURCHASE_ORDER_LINE_ITEM_ID")
	private String purchaseOrderLineItemId;

	@Column(name="SHELVING_ORDER")
	private String shelvingOrder;

	@Column(name="STAFF_ONLY")
	private String staffOnly;

	@Column(name="TEMP_ITEM_TYPE_ID")
	private Integer tempItemTypeId;

	@Column(name="UNIQUE_ID_PREFIX")
	private String uniqueIdPrefix;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	private String uri;

	@Column(name="VENDOR_LINE_ITEM_ID")
	private String vendorLineItemId;

	@Column(name="VOLUME_NUMBER")
	private String volumeNumber;

	//bi-directional many-to-one association to ItemDamagedRecord
	@OneToMany(mappedBy="itemRecord")
	private List<ItemDamagedRecord> itemDamagedRecords;

	//bi-directional many-to-one association to OLEItemDonorRecord
	@OneToMany(mappedBy="itemRecord")
	private List<OLEItemDonorRecord> oleItemDonorRecords;

	//bi-directional many-to-one association to ItemNoteRecord
	@OneToMany(mappedBy="itemRecord")
	private List<ItemNoteRecord> itemNoteRecords;

	//bi-directional many-to-one association to ItemStatisticalSearchRecord
	@OneToMany(mappedBy="itemRecord")
	private List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords;

	//bi-directional many-to-one association to HoldingsRecord
	@ManyToOne
	@JoinColumn(name="HOLDINGS_ID")
	private HoldingsRecord holdingsRecord;

	//bi-directional many-to-one association to FormerIdentifierRecord
	@OneToMany(mappedBy="itemRecord")
	private List<FormerIdentifierRecord> formerIdentifierRecords;

	//bi-directional many-to-one association to LocationsCheckinCountRecord
	@OneToMany(mappedBy="itemRecord")
	private List<LocationsCheckinCountRecord> locationsCheckinCountRecords;

	//bi-directional many-to-one association to ItemClaimsReturnedRecord
	@OneToMany(mappedBy="itemRecord")
	private List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords;

	//bi-directional many-to-one association to MissingPieceItemRecord
	@OneToMany(mappedBy="itemRecord")
	private List<MissingPieceItemRecord> missingPieceItemRecords;

	public ItemRecord() {
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBarcodeArsl() {
		return this.barcodeArsl;
	}

	public void setBarcodeArsl(String barcodeArsl) {
		this.barcodeArsl = barcodeArsl;
	}

	public String getCallNumber() {
		return this.callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getCallNumberPrefix() {
		return this.callNumberPrefix;
	}

	public void setCallNumberPrefix(String callNumberPrefix) {
		this.callNumberPrefix = callNumberPrefix;
	}

	public Integer getCallNumberTypeId() {
		return this.callNumberTypeId;
	}

	public void setCallNumberTypeId(Integer callNumberTypeId) {
		this.callNumberTypeId = callNumberTypeId;
	}

	public String getCheckInNote() {
		return this.checkInNote;
	}

	public void setCheckInNote(String checkInNote) {
		this.checkInNote = checkInNote;
	}

	public Date getCheckOutDateTime() {
		return this.checkOutDateTime;
	}

	public void setCheckOutDateTime(Date checkOutDateTime) {
		this.checkOutDateTime = checkOutDateTime;
	}

	public String getChronology() {
		return this.chronology;
	}

	public void setChronology(String chronology) {
		this.chronology = chronology;
	}

	public String getClaimsReturned() {
		return this.claimsReturned;
	}

	public void setClaimsReturned(String claimsReturned) {
		this.claimsReturned = claimsReturned;
	}

	public Date getClaimsReturnedDateCreated() {
		return this.claimsReturnedDateCreated;
	}

	public void setClaimsReturnedDateCreated(Date claimsReturnedDateCreated) {
		this.claimsReturnedDateCreated = claimsReturnedDateCreated;
	}

	public String getClaimsReturnedNote() {
		return this.claimsReturnedNote;
	}

	public void setClaimsReturnedNote(String claimsReturnedNote) {
		this.claimsReturnedNote = claimsReturnedNote;
	}

	public String getCopyNumber() {
		return this.copyNumber;
	}

	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCurrentBorrower() {
		return this.currentBorrower;
	}

	public void setCurrentBorrower(String currentBorrower) {
		this.currentBorrower = currentBorrower;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getDescOfPieces() {
		return this.descOfPieces;
	}

	public void setDescOfPieces(String descOfPieces) {
		this.descOfPieces = descOfPieces;
	}

	public Date getDueDateTime() {
		return this.dueDateTime;
	}

	public void setDueDateTime(Date dueDateTime) {
		this.dueDateTime = dueDateTime;
	}

	public String getEnumeration() {
		return this.enumeration;
	}

	public void setEnumeration(String enumeration) {
		this.enumeration = enumeration;
	}

	public String getFastAdd() {
		return this.fastAdd;
	}

	public void setFastAdd(String fastAdd) {
		this.fastAdd = fastAdd;
	}

	public String getFund() {
		return this.fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getItemDamagedNote() {
		return this.itemDamagedNote;
	}

	public void setItemDamagedNote(String itemDamagedNote) {
		this.itemDamagedNote = itemDamagedNote;
	}

	public String getItemDamagedStatus() {
		return this.itemDamagedStatus;
	}

	public void setItemDamagedStatus(String itemDamagedStatus) {
		this.itemDamagedStatus = itemDamagedStatus;
	}

	public Date getItemStatusDateUpdated() {
		return this.itemStatusDateUpdated;
	}

	public void setItemStatusDateUpdated(Date itemStatusDateUpdated) {
		this.itemStatusDateUpdated = itemStatusDateUpdated;
	}

	public Integer getItemStatusId() {
		return this.itemStatusId;
	}

	public void setItemStatusId(Integer itemStatusId) {
		this.itemStatusId = itemStatusId;
	}

	public Integer getItemTypeId() {
		return this.itemTypeId;
	}

	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationLevel() {
		return this.locationLevel;
	}

	public void setLocationLevel(String locationLevel) {
		this.locationLevel = locationLevel;
	}

	public String getMissingPieces() {
		return this.missingPieces;
	}

	public void setMissingPieces(String missingPieces) {
		this.missingPieces = missingPieces;
	}

	public Integer getMissingPiecesCount() {
		return this.missingPiecesCount;
	}

	public void setMissingPiecesCount(Integer missingPiecesCount) {
		this.missingPiecesCount = missingPiecesCount;
	}

	public Date getMissingPiecesEffectiveDate() {
		return this.missingPiecesEffectiveDate;
	}

	public void setMissingPiecesEffectiveDate(Date missingPiecesEffectiveDate) {
		this.missingPiecesEffectiveDate = missingPiecesEffectiveDate;
	}

	public String getMissingPiecesNote() {
		return this.missingPiecesNote;
	}

	public void setMissingPiecesNote(String missingPiecesNote) {
		this.missingPiecesNote = missingPiecesNote;
	}

	public Integer getNumOfRenew() {
		return this.numOfRenew;
	}

	public void setNumOfRenew(Integer numOfRenew) {
		this.numOfRenew = numOfRenew;
	}

	public String getNumPieces() {
		return this.numPieces;
	}

	public void setNumPieces(String numPieces) {
		this.numPieces = numPieces;
	}

	public Date getOrgDueDateTime() {
		return this.orgDueDateTime;
	}

	public void setOrgDueDateTime(Date orgDueDateTime) {
		this.orgDueDateTime = orgDueDateTime;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getProxyBorrower() {
		return this.proxyBorrower;
	}

	public void setProxyBorrower(String proxyBorrower) {
		this.proxyBorrower = proxyBorrower;
	}

	public String getPurchaseOrderLineItemId() {
		return this.purchaseOrderLineItemId;
	}

	public void setPurchaseOrderLineItemId(String purchaseOrderLineItemId) {
		this.purchaseOrderLineItemId = purchaseOrderLineItemId;
	}

	public String getShelvingOrder() {
		return this.shelvingOrder;
	}

	public void setShelvingOrder(String shelvingOrder) {
		this.shelvingOrder = shelvingOrder;
	}

	public String getStaffOnly() {
		return this.staffOnly;
	}

	public void setStaffOnly(String staffOnly) {
		this.staffOnly = staffOnly;
	}

	public Integer getTempItemTypeId() {
		return this.tempItemTypeId;
	}

	public void setTempItemTypeId(Integer tempItemTypeId) {
		this.tempItemTypeId = tempItemTypeId;
	}

	public String getUniqueIdPrefix() {
		return this.uniqueIdPrefix;
	}

	public void setUniqueIdPrefix(String uniqueIdPrefix) {
		this.uniqueIdPrefix = uniqueIdPrefix;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVendorLineItemId() {
		return this.vendorLineItemId;
	}

	public void setVendorLineItemId(String vendorLineItemId) {
		this.vendorLineItemId = vendorLineItemId;
	}

	public String getVolumeNumber() {
		return this.volumeNumber;
	}

	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	public List<ItemDamagedRecord> getItemDamagedRecords() {
		return this.itemDamagedRecords;
	}

	public void setItemDamagedRecords(List<ItemDamagedRecord> itemDamagedRecords) {
		this.itemDamagedRecords = itemDamagedRecords;
	}

	public ItemDamagedRecord addOleDsDmgdItmHstryT(ItemDamagedRecord oleDsDmgdItmHstryT) {
		getItemDamagedRecords().add(oleDsDmgdItmHstryT);
		oleDsDmgdItmHstryT.setItemRecord(this);

		return oleDsDmgdItmHstryT;
	}

	public ItemDamagedRecord removeOleDsDmgdItmHstryT(ItemDamagedRecord oleDsDmgdItmHstryT) {
		getItemDamagedRecords().remove(oleDsDmgdItmHstryT);
		oleDsDmgdItmHstryT.setItemRecord(null);

		return oleDsDmgdItmHstryT;
	}

	public List<OLEItemDonorRecord> getOleItemDonorRecords() {
		return this.oleItemDonorRecords;
	}

	public void setOleItemDonorRecords(List<OLEItemDonorRecord> oleItemDonorRecords) {
		this.oleItemDonorRecords = oleItemDonorRecords;
	}

	public OLEItemDonorRecord addOleDsItemDonorT(OLEItemDonorRecord oleDsItemDonorT) {
		getOleItemDonorRecords().add(oleDsItemDonorT);
		oleDsItemDonorT.setItemRecord(this);

		return oleDsItemDonorT;
	}

	public OLEItemDonorRecord removeOleDsItemDonorT(OLEItemDonorRecord oleDsItemDonorT) {
		getOleItemDonorRecords().remove(oleDsItemDonorT);
		oleDsItemDonorT.setItemRecord(null);

		return oleDsItemDonorT;
	}

	public List<ItemNoteRecord> getItemNoteRecords() {
		return this.itemNoteRecords;
	}

	public void setItemNoteRecords(List<ItemNoteRecord> itemNoteRecords) {
		this.itemNoteRecords = itemNoteRecords;
	}

	public ItemNoteRecord addOleDsItemNoteT(ItemNoteRecord oleDsItemNoteT) {
		getItemNoteRecords().add(oleDsItemNoteT);
		oleDsItemNoteT.setItemRecord(this);

		return oleDsItemNoteT;
	}

	public ItemNoteRecord removeOleDsItemNoteT(ItemNoteRecord oleDsItemNoteT) {
		getItemNoteRecords().remove(oleDsItemNoteT);
		oleDsItemNoteT.setItemRecord(null);

		return oleDsItemNoteT;
	}

	public List<ItemStatisticalSearchRecord> getItemStatisticalSearchRecords() {
		return this.itemStatisticalSearchRecords;
	}

	public void setItemStatisticalSearchRecords(List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords) {
		this.itemStatisticalSearchRecords = itemStatisticalSearchRecords;
	}

	public ItemStatisticalSearchRecord addOleDsItemStatSearchT(ItemStatisticalSearchRecord oleDsItemStatSearchT) {
		getItemStatisticalSearchRecords().add(oleDsItemStatSearchT);
		oleDsItemStatSearchT.setItemRecord(this);

		return oleDsItemStatSearchT;
	}

	public ItemStatisticalSearchRecord removeOleDsItemStatSearchT(ItemStatisticalSearchRecord oleDsItemStatSearchT) {
		getItemStatisticalSearchRecords().remove(oleDsItemStatSearchT);
		oleDsItemStatSearchT.setItemRecord(null);

		return oleDsItemStatSearchT;
	}

	public HoldingsRecord getHoldingsRecord() {
		return this.holdingsRecord;
	}

	public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
		this.holdingsRecord = holdingsRecord;
	}

	public List<FormerIdentifierRecord> getFormerIdentifierRecords() {
		return this.formerIdentifierRecords;
	}

	public void setFormerIdentifierRecords(List<FormerIdentifierRecord> formerIdentifierRecords) {
		this.formerIdentifierRecords = formerIdentifierRecords;
	}

	public FormerIdentifierRecord addOleDsItmFormerIdentifierT(FormerIdentifierRecord oleDsItmFormerIdentifierT) {
		getFormerIdentifierRecords().add(oleDsItmFormerIdentifierT);
		oleDsItmFormerIdentifierT.setItemRecord(this);

		return oleDsItmFormerIdentifierT;
	}

	public FormerIdentifierRecord removeOleDsItmFormerIdentifierT(FormerIdentifierRecord oleDsItmFormerIdentifierT) {
		getFormerIdentifierRecords().remove(oleDsItmFormerIdentifierT);
		oleDsItmFormerIdentifierT.setItemRecord(null);

		return oleDsItmFormerIdentifierT;
	}

	public List<LocationsCheckinCountRecord> getLocationsCheckinCountRecords() {
		return this.locationsCheckinCountRecords;
	}

	public void setLocationsCheckinCountRecords(List<LocationsCheckinCountRecord> locationsCheckinCountRecords) {
		this.locationsCheckinCountRecords = locationsCheckinCountRecords;
	}

	public LocationsCheckinCountRecord addOleDsLocCheckinCountT(LocationsCheckinCountRecord oleDsLocCheckinCountT) {
		getLocationsCheckinCountRecords().add(oleDsLocCheckinCountT);
		oleDsLocCheckinCountT.setItemRecord(this);

		return oleDsLocCheckinCountT;
	}

	public LocationsCheckinCountRecord removeOleDsLocCheckinCountT(LocationsCheckinCountRecord oleDsLocCheckinCountT) {
		getLocationsCheckinCountRecords().remove(oleDsLocCheckinCountT);
		oleDsLocCheckinCountT.setItemRecord(null);

		return oleDsLocCheckinCountT;
	}

	public List<ItemClaimsReturnedRecord> getItemClaimsReturnedRecords() {
		return this.itemClaimsReturnedRecords;
	}

	public void setItemClaimsReturnedRecords(List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords) {
		this.itemClaimsReturnedRecords = itemClaimsReturnedRecords;
	}

	public ItemClaimsReturnedRecord addOleItmClmRtndHstryT(ItemClaimsReturnedRecord oleItmClmRtndHstryT) {
		getItemClaimsReturnedRecords().add(oleItmClmRtndHstryT);
		oleItmClmRtndHstryT.setItemRecord(this);

		return oleItmClmRtndHstryT;
	}

	public ItemClaimsReturnedRecord removeOleItmClmRtndHstryT(ItemClaimsReturnedRecord oleItmClmRtndHstryT) {
		getItemClaimsReturnedRecords().remove(oleItmClmRtndHstryT);
		oleItmClmRtndHstryT.setItemRecord(null);

		return oleItmClmRtndHstryT;
	}

	public List<MissingPieceItemRecord> getMissingPieceItemRecords() {
		return this.missingPieceItemRecords;
	}

	public void setMissingPieceItemRecords(List<MissingPieceItemRecord> missingPieceItemRecords) {
		this.missingPieceItemRecords = missingPieceItemRecords;
	}

	public MissingPieceItemRecord addOleMissPceItmHstryT(MissingPieceItemRecord oleMissPceItmHstryT) {
		getMissingPieceItemRecords().add(oleMissPceItmHstryT);
		oleMissPceItmHstryT.setItemRecord(this);

		return oleMissPceItmHstryT;
	}

	public MissingPieceItemRecord removeOleMissPceItmHstryT(MissingPieceItemRecord oleMissPceItmHstryT) {
		getMissingPieceItemRecords().remove(oleMissPceItmHstryT);
		oleMissPceItmHstryT.setItemRecord(null);

		return oleMissPceItmHstryT;
	}

}