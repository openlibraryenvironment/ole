package org.kuali.ole.model.jpa;

import org.kuali.ole.model.jpa.*;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by sheiks on 27/10/16.
 */
@Entity
@Table(name="ole_ds_holdings_t")
@NamedQuery(name="HoldingsRecord.findAll", query="SELECT o FROM HoldingsRecord o")
public class HoldingsRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="HOLDINGS_ID")
    private Integer holdingsId;

    @Column(name="ACCESS_PASSWORD")
    private String accessPassword;

    @Column(name="ACCESS_STATUS")
    private String accessStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ACCESS_STATUS_DATE_UPDATED")
    private Date accessStatusDateUpdated;

    @Column(name="ACCESS_USERNAME")
    private String accessUsername;

    @Column(name="ADMIN_PASSWORD")
    private String adminPassword;

    @Column(name="ADMIN_URL")
    private String adminUrl;

    @Column(name="ADMIN_USERNAME")
    private String adminUsername;

    @Column(name="ALLOW_ILL")
    private String allowIll;

    @Column(name="AUTHENTICATION_TYPE_ID")
    private Integer authenticationTypeId;

    @Column(name="CALL_NUMBER")
    private String callNumber;

    @Column(name="CALL_NUMBER_PREFIX")
    private String callNumberPrefix;

    @Column(name="CALL_NUMBER_TYPE_ID")
    private Integer callNumberTypeId;

    @Column(name="CANCELLATION_CANDIDATE")
    private String cancellationCandidate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CANCELLATION_DECISION_DT")
    private Date cancellationDecisionDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CANCELLATION_EFFECTIVE_DT")
    private Date cancellationEffectiveDt;

    @Column(name="CANCELLATION_REASON")
    private String cancellationReason;

    @Column(name="COPY_NUMBER")
    private String copyNumber;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CURRENT_SBRCPTN_END_DT")
    private Date currentSbrcptnEndDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CURRENT_SBRCPTN_START_DT")
    private Date currentSbrcptnStartDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_CREATED")
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_UPDATED")
    private Date dateUpdated;

    @Column(name="E_RESOURCE_ID")
    private String eResourceId;

    @Column(name="FIRST_INDICATOR")
    private String firstIndicator;

    @Column(name="FORMER_HOLDINGS_ID")
    private String formerHoldingsId;

    @Column(name="GOKB_IDENTIFIER")
    private Integer gokbIdentifier;

    @Column(name="HOLDINGS_TYPE")
    private String holdingsType;

    private String imprint;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INITIAL_SBRCPTN_START_DT")
    private Date initialSbrcptnStartDt;

    @Column(name="LOCAL_PERSISTENT_URI")
    private String localPersistentUri;

    private String location;

    @Column(name="LOCATION_ID")
    private Integer locationId;

    @Column(name="LOCATION_LEVEL")
    private String locationLevel;

    @Column(name="MATERIALS_SPECIFIED")
    private String materialsSpecified;

    @Column(name="NUMBER_SIMULT_USERS")
    private Integer numberSimultUsers;

    private String platform;

    @Column(name="PROXIED_RESOURCE")
    private String proxiedResource;

    private String publisher;

    @Column(name="RECEIPT_STATUS_ID")
    private Integer receiptStatusId;

    @Column(name="SECOND_INDICATOR")
    private String secondIndicator;

    @Column(name="SHELVING_ORDER")
    private String shelvingOrder;

    @Lob
    @Column(name="SOURCE_HOLDINGS_CONTENT")
    private String sourceHoldingsContent;

    @Column(name="STAFF_ONLY")
    private String staffOnly;

    @Column(name="SUBSCRIPTION_STATUS")
    private String subscriptionStatus;

    @Column(name="UNIQUE_ID_PREFIX")
    private String uniqueIdPrefix;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    //bi-directional many-to-one association to HoldingsAccessLocation
    @OneToMany(mappedBy="holdingsRecord")
    private List<HoldingsAccessLocation> holdingsAccessLocations;

    //bi-directional many-to-one association to ExtentOfOwnerShipRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords;

    //bi-directional many-to-one association to EInstanceCoverageRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<EInstanceCoverageRecord> EInstanceCoverageRecords;

    //bi-directional many-to-one association to OLEHoldingsDonorRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<OLEHoldingsDonorRecord> OLEHoldingsDonorRecords;

    //bi-directional many-to-one association to HoldingsNoteRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<HoldingsNoteRecord> holdingsNoteRecords;

    //bi-directional many-to-one association to HoldingsStatisticalSearchRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords;

    //bi-directional many-to-one association to BibRecord
    @ManyToOne
    @JoinColumn(name="BIB_ID")
    private BibRecord bibRecord;

    //bi-directional many-to-one association to HoldingsUriRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<HoldingsUriRecord> holdingsUriRecords;

    //bi-directional many-to-one association to ItemRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<ItemRecord> itemRecords;

    //bi-directional many-to-one association to EInstancePerpetualAccessRecord
    @OneToMany(mappedBy="holdingsRecord")
    private List<EInstancePerpetualAccessRecord> EInstancePerpetualAccessRecords;

    public HoldingsRecord() {
    }

    public Integer getHoldingsId() {
        return this.holdingsId;
    }

    public void setHoldingsId(Integer holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getAccessPassword() {
        return this.accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public String getAccessStatus() {
        return this.accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public Date getAccessStatusDateUpdated() {
        return this.accessStatusDateUpdated;
    }

    public void setAccessStatusDateUpdated(Date accessStatusDateUpdated) {
        this.accessStatusDateUpdated = accessStatusDateUpdated;
    }

    public String getAccessUsername() {
        return this.accessUsername;
    }

    public void setAccessUsername(String accessUsername) {
        this.accessUsername = accessUsername;
    }

    public String getAdminPassword() {
        return this.adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminUrl() {
        return this.adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getAdminUsername() {
        return this.adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAllowIll() {
        return this.allowIll;
    }

    public void setAllowIll(String allowIll) {
        this.allowIll = allowIll;
    }

    public Integer getAuthenticationTypeId() {
        return this.authenticationTypeId;
    }

    public void setAuthenticationTypeId(Integer authenticationTypeId) {
        this.authenticationTypeId = authenticationTypeId;
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

    public String getCancellationCandidate() {
        return this.cancellationCandidate;
    }

    public void setCancellationCandidate(String cancellationCandidate) {
        this.cancellationCandidate = cancellationCandidate;
    }

    public Date getCancellationDecisionDt() {
        return this.cancellationDecisionDt;
    }

    public void setCancellationDecisionDt(Date cancellationDecisionDt) {
        this.cancellationDecisionDt = cancellationDecisionDt;
    }

    public Date getCancellationEffectiveDt() {
        return this.cancellationEffectiveDt;
    }

    public void setCancellationEffectiveDt(Date cancellationEffectiveDt) {
        this.cancellationEffectiveDt = cancellationEffectiveDt;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
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

    public Date getCurrentSbrcptnEndDt() {
        return this.currentSbrcptnEndDt;
    }

    public void setCurrentSbrcptnEndDt(Date currentSbrcptnEndDt) {
        this.currentSbrcptnEndDt = currentSbrcptnEndDt;
    }

    public Date getCurrentSbrcptnStartDt() {
        return this.currentSbrcptnStartDt;
    }

    public void setCurrentSbrcptnStartDt(Date currentSbrcptnStartDt) {
        this.currentSbrcptnStartDt = currentSbrcptnStartDt;
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

    public String getEResourceId() {
        return this.eResourceId;
    }

    public void setEResourceId(String eResourceId) {
        this.eResourceId = eResourceId;
    }

    public String getFirstIndicator() {
        return this.firstIndicator;
    }

    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    public String getFormerHoldingsId() {
        return this.formerHoldingsId;
    }

    public void setFormerHoldingsId(String formerHoldingsId) {
        this.formerHoldingsId = formerHoldingsId;
    }

    public Integer getGokbIdentifier() {
        return this.gokbIdentifier;
    }

    public void setGokbIdentifier(Integer gokbIdentifier) {
        this.gokbIdentifier = gokbIdentifier;
    }

    public String getHoldingsType() {
        return this.holdingsType;
    }

    public void setHoldingsType(String holdingsType) {
        this.holdingsType = holdingsType;
    }

    public String getImprint() {
        return this.imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public Date getInitialSbrcptnStartDt() {
        return this.initialSbrcptnStartDt;
    }

    public void setInitialSbrcptnStartDt(Date initialSbrcptnStartDt) {
        this.initialSbrcptnStartDt = initialSbrcptnStartDt;
    }

    public String getLocalPersistentUri() {
        return this.localPersistentUri;
    }

    public void setLocalPersistentUri(String localPersistentUri) {
        this.localPersistentUri = localPersistentUri;
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

    public String getMaterialsSpecified() {
        return this.materialsSpecified;
    }

    public void setMaterialsSpecified(String materialsSpecified) {
        this.materialsSpecified = materialsSpecified;
    }

    public Integer getNumberSimultUsers() {
        return this.numberSimultUsers;
    }

    public void setNumberSimultUsers(Integer numberSimultUsers) {
        this.numberSimultUsers = numberSimultUsers;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProxiedResource() {
        return this.proxiedResource;
    }

    public void setProxiedResource(String proxiedResource) {
        this.proxiedResource = proxiedResource;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getReceiptStatusId() {
        return this.receiptStatusId;
    }

    public void setReceiptStatusId(Integer receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    public String getSecondIndicator() {
        return this.secondIndicator;
    }

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    public String getShelvingOrder() {
        return this.shelvingOrder;
    }

    public void setShelvingOrder(String shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }

    public String getSourceHoldingsContent() {
        return this.sourceHoldingsContent;
    }

    public void setSourceHoldingsContent(String sourceHoldingsContent) {
        this.sourceHoldingsContent = sourceHoldingsContent;
    }

    public String getStaffOnly() {
        return this.staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getSubscriptionStatus() {
        return this.subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
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

    public List<HoldingsAccessLocation> getHoldingsAccessLocations() {
        return this.holdingsAccessLocations;
    }

    public void setHoldingsAccessLocations(List<HoldingsAccessLocation> holdingsAccessLocations) {
        this.holdingsAccessLocations = holdingsAccessLocations;
    }

    public HoldingsAccessLocation addHoldingsAccessLocation(HoldingsAccessLocation holdingsAccessLocation) {
        getHoldingsAccessLocations().add(holdingsAccessLocation);
        holdingsAccessLocation.setHoldingsRecord(this);

        return holdingsAccessLocation;
    }

    public HoldingsAccessLocation removeHoldingsAccessLocation(HoldingsAccessLocation holdingsAccessLocation) {
        getHoldingsAccessLocations().remove(holdingsAccessLocation);
        holdingsAccessLocation.setHoldingsRecord(null);

        return holdingsAccessLocation;
    }

    public List<ExtentOfOwnerShipRecord> getExtentOfOwnerShipRecords() {
        return this.extentOfOwnerShipRecords;
    }

    public void setExtentOfOwnerShipRecords(List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords) {
        this.extentOfOwnerShipRecords = extentOfOwnerShipRecords;
    }

    public ExtentOfOwnerShipRecord addExtentOfOwnerShipRecord(ExtentOfOwnerShipRecord extentOfOwnerShipRecord) {
        getExtentOfOwnerShipRecords().add(extentOfOwnerShipRecord);
        extentOfOwnerShipRecord.setHoldingsRecord(this);

        return extentOfOwnerShipRecord;
    }

    public ExtentOfOwnerShipRecord removeExtentOfOwnerShipRecord(ExtentOfOwnerShipRecord extentOfOwnerShipRecord) {
        getExtentOfOwnerShipRecords().remove(extentOfOwnerShipRecord);
        extentOfOwnerShipRecord.setHoldingsRecord(null);

        return extentOfOwnerShipRecord;
    }

    public List<EInstanceCoverageRecord> getEInstanceCoverageRecords() {
        return this.EInstanceCoverageRecords;
    }

    public void setEInstanceCoverageRecords(List<EInstanceCoverageRecord> EInstanceCoverageRecords) {
        this.EInstanceCoverageRecords = EInstanceCoverageRecords;
    }

    public EInstanceCoverageRecord addEInstanceCoverageRecord(EInstanceCoverageRecord EInstanceCoverageRecord) {
        getEInstanceCoverageRecords().add(EInstanceCoverageRecord);
        EInstanceCoverageRecord.setHoldingsRecord(this);

        return EInstanceCoverageRecord;
    }

    public EInstanceCoverageRecord removeEInstanceCoverageRecord(EInstanceCoverageRecord EInstanceCoverageRecord) {
        getEInstanceCoverageRecords().remove(EInstanceCoverageRecord);
        EInstanceCoverageRecord.setHoldingsRecord(null);

        return EInstanceCoverageRecord;
    }

    public List<OLEHoldingsDonorRecord> getOLEHoldingsDonorRecords() {
        return this.OLEHoldingsDonorRecords;
    }

    public void setOLEHoldingsDonorRecords(List<OLEHoldingsDonorRecord> OLEHoldingsDonorRecords) {
        this.OLEHoldingsDonorRecords = OLEHoldingsDonorRecords;
    }

    public OLEHoldingsDonorRecord addOLEHoldingsDonorRecord(OLEHoldingsDonorRecord OLEHoldingsDonorRecord) {
        getOLEHoldingsDonorRecords().add(OLEHoldingsDonorRecord);
        OLEHoldingsDonorRecord.setHoldingsRecord(this);

        return OLEHoldingsDonorRecord;
    }

    public OLEHoldingsDonorRecord removeOLEHoldingsDonorRecord(OLEHoldingsDonorRecord OLEHoldingsDonorRecord) {
        getOLEHoldingsDonorRecords().remove(OLEHoldingsDonorRecord);
        OLEHoldingsDonorRecord.setHoldingsRecord(null);

        return OLEHoldingsDonorRecord;
    }

    public List<HoldingsNoteRecord> getHoldingsNoteRecords() {
        return this.holdingsNoteRecords;
    }

    public void setHoldingsNoteRecords(List<HoldingsNoteRecord> holdingsNoteRecords) {
        this.holdingsNoteRecords = holdingsNoteRecords;
    }

    public HoldingsNoteRecord addHoldingsNoteRecord(HoldingsNoteRecord holdingsNoteRecord) {
        getHoldingsNoteRecords().add(holdingsNoteRecord);
        holdingsNoteRecord.setHoldingsRecord(this);

        return holdingsNoteRecord;
    }

    public HoldingsNoteRecord removeHoldingsNoteRecord(HoldingsNoteRecord holdingsNoteRecord) {
        getHoldingsNoteRecords().remove(holdingsNoteRecord);
        holdingsNoteRecord.setHoldingsRecord(null);

        return holdingsNoteRecord;
    }

    public List<HoldingsStatisticalSearchRecord> getHoldingsStatisticalSearchRecords() {
        return this.holdingsStatisticalSearchRecords;
    }

    public void setHoldingsStatisticalSearchRecords(List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords) {
        this.holdingsStatisticalSearchRecords = holdingsStatisticalSearchRecords;
    }

    public HoldingsStatisticalSearchRecord addHoldingsStatisticalSearchRecord(HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord) {
        getHoldingsStatisticalSearchRecords().add(holdingsStatisticalSearchRecord);
        holdingsStatisticalSearchRecord.setHoldingsRecord(this);

        return holdingsStatisticalSearchRecord;
    }

    public HoldingsStatisticalSearchRecord removeHoldingsStatisticalSearchRecord(HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord) {
        getHoldingsStatisticalSearchRecords().remove(holdingsStatisticalSearchRecord);
        holdingsStatisticalSearchRecord.setHoldingsRecord(null);

        return holdingsStatisticalSearchRecord;
    }

    public BibRecord getBibRecord() {
        return this.bibRecord;
    }

    public void setBibRecord(BibRecord bibRecord) {
        this.bibRecord = bibRecord;
    }

    public List<HoldingsUriRecord> getHoldingsUriRecords() {
        return this.holdingsUriRecords;
    }

    public void setHoldingsUriRecords(List<HoldingsUriRecord> holdingsUriRecords) {
        this.holdingsUriRecords = holdingsUriRecords;
    }

    public HoldingsUriRecord addHoldingsUriRecord(HoldingsUriRecord holdingsUriRecord) {
        getHoldingsUriRecords().add(holdingsUriRecord);
        holdingsUriRecord.setHoldingsRecord(this);

        return holdingsUriRecord;
    }

    public HoldingsUriRecord removeHoldingsUriRecord(HoldingsUriRecord holdingsUriRecord) {
        getHoldingsUriRecords().remove(holdingsUriRecord);
        holdingsUriRecord.setHoldingsRecord(null);

        return holdingsUriRecord;
    }

    public List<ItemRecord> getItemRecords() {
        return this.itemRecords;
    }

    public void setItemRecords(List<ItemRecord> itemRecords) {
        this.itemRecords = itemRecords;
    }

    public ItemRecord addItemRecord(ItemRecord itemRecord) {
        getItemRecords().add(itemRecord);
        itemRecord.setHoldingsRecord(this);

        return itemRecord;
    }

    public ItemRecord removeItemRecord(ItemRecord itemRecord) {
        getItemRecords().remove(itemRecord);
        itemRecord.setHoldingsRecord(null);

        return itemRecord;
    }

    public List<EInstancePerpetualAccessRecord> getEInstancePerpetualAccessRecords() {
        return this.EInstancePerpetualAccessRecords;
    }

    public void setEInstancePerpetualAccessRecords(List<EInstancePerpetualAccessRecord> EInstancePerpetualAccessRecords) {
        this.EInstancePerpetualAccessRecords = EInstancePerpetualAccessRecords;
    }

    public EInstancePerpetualAccessRecord addEInstancePerpetualAccessRecord(EInstancePerpetualAccessRecord EInstancePerpetualAccessRecord) {
        getEInstancePerpetualAccessRecords().add(EInstancePerpetualAccessRecord);
        EInstancePerpetualAccessRecord.setHoldingsRecord(this);

        return EInstancePerpetualAccessRecord;
    }

    public EInstancePerpetualAccessRecord removeEInstancePerpetualAccessRecord(EInstancePerpetualAccessRecord EInstancePerpetualAccessRecord) {
        getEInstancePerpetualAccessRecords().remove(EInstancePerpetualAccessRecord);
        EInstancePerpetualAccessRecord.setHoldingsRecord(null);

        return EInstancePerpetualAccessRecord;
    }

}