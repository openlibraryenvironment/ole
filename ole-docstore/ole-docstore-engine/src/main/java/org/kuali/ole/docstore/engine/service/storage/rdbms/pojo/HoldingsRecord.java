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
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsRecord extends PersistableBusinessObjectBase
        implements Serializable {

    private String holdingsId;
    private String bibId;
    @AuditField
    private String holdingsType;
    private String formerHoldingsId;
    @AuditField
    private Integer gokbIdentifier;
    private String uri;
    @AuditField
    private String location;
    private String content;
    @AuditField
    private String locationLevel;
    @AuditField
    private String callNumberPrefix;
    @AuditField
    private String callNumber;
    @AuditField
    private String shelvingOrder;
    @AuditField
    private String callNumberTypeId;
    private String receiptStatusId;
    @AuditField
    private String copyNumber;

    private String publisherId;
    private String accessStatus;
    private String platform;
    private String imprint;
    private String localPersistentUri;
    private boolean allowIll;
    private String proxiedResource;
    private Timestamp statusDate;
    private String eResourceId;
    private String adminUrl;
    private String adminUserName;
    private String adminPassword;
    private String accessUserName;
    private String accessPassword;
    protected String materialsSpecified;
    protected String firstIndicator;
    protected String secondIndicator;

    @AuditField
    private String subscriptionStatus;
    private Timestamp currentSubscriptionStartDate;
    private Timestamp currentSubscriptionEndDate;
    private Timestamp initialSubscriptionStartDate;
    private boolean cancellationCandidate;
    private Timestamp cancellationDecisionDate;
    private Timestamp cancellationEffectiveDate;
    private String cancellationReason;
//    private String link;
//    private String linkText;
    private String numberSimultaneousUsers;
    private String authenticationTypeId;

    private String uniqueIdPrefix;
    @AuditField
    private Boolean staffOnlyFlag;
    private Timestamp createdDate;
    @AuditField
    private Timestamp updatedDate;
    private String createdBy;
    @AuditField
    private String updatedBy;

    private ReceiptStatusRecord receiptStatusRecord;
    private CallNumberTypeRecord callNumberTypeRecord;
    private List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords;
    private List<HoldingsNoteRecord> holdingsNoteRecords;
    private List<HoldingsUriRecord> holdingsUriRecords;

    private List<HoldingsAccessLocation> holdingsAccessLocations;
    private AuthenticationTypeRecord authenticationType;
    private List<EInstanceCoverageRecord> eInstanceCoverageRecordList;
    private List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecordList;
    private List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords;

    private List<ItemRecord> itemRecords;
    private List<BibRecord> bibRecords;
    private List<OLEHoldingsDonorRecord> donorList;
    private List<HoldingsUriRecord> accessUriRecords;
    private String operationType;

    public List<HoldingsUriRecord> getAccessUriRecords() {
        return accessUriRecords;
    }

    public void setAccessUriRecords(List<HoldingsUriRecord> accessUriRecords) {
        this.accessUriRecords = accessUriRecords;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getHoldingsType() {
        return holdingsType;
    }

    public void setHoldingsType(String holdingsType) {
        this.holdingsType = holdingsType;
    }

    public String getFormerHoldingsId() {
        return formerHoldingsId;
    }

    public void setFormerHoldingsId(String formerHoldingsId) {
        this.formerHoldingsId = formerHoldingsId;
    }

    public Integer getGokbIdentifier() {
        return gokbIdentifier;
    }

    public void setGokbIdentifier(Integer gokbIdentifier) {
        this.gokbIdentifier = gokbIdentifier;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReceiptStatusId() {
        return receiptStatusId;
    }

    public void setReceiptStatusId(String receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getLocalPersistentUri() {
        return localPersistentUri;
    }

    public void setLocalPersistentUri(String localPersistentUri) {
        this.localPersistentUri = localPersistentUri;
    }

    public boolean getAllowIll() {
        return allowIll;
    }

    public void setAllowIll(boolean allowIll) {
        this.allowIll = allowIll;
    }

    public String getProxiedResource() {
        return proxiedResource;
    }

    public void setProxiedResource(String proxiedResource) {
        this.proxiedResource = proxiedResource;
    }

    public Timestamp getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Timestamp statusDate) {
        this.statusDate = statusDate;
    }

    public String geteResourceId() {
        return eResourceId;
    }

    public void seteResourceId(String eResourceId) {
        this.eResourceId = eResourceId;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAccessUserName() {
        return accessUserName;
    }

    public void setAccessUserName(String accessUserName) {
        this.accessUserName = accessUserName;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Timestamp getCurrentSubscriptionStartDate() {
        return currentSubscriptionStartDate;
    }

    public void setCurrentSubscriptionStartDate(Timestamp currentSubscriptionStartDate) {
        this.currentSubscriptionStartDate = currentSubscriptionStartDate;
    }

    public Timestamp getCurrentSubscriptionEndDate() {
        return currentSubscriptionEndDate;
    }

    public void setCurrentSubscriptionEndDate(Timestamp currentSubscriptionEndDate) {
        this.currentSubscriptionEndDate = currentSubscriptionEndDate;
    }

    public Timestamp getInitialSubscriptionStartDate() {
        return initialSubscriptionStartDate;
    }

    public void setInitialSubscriptionStartDate(Timestamp initialSubscriptionStartDate) {
        this.initialSubscriptionStartDate = initialSubscriptionStartDate;
    }

    public boolean isCancellationCandidate() {
        return cancellationCandidate;
    }

    public void setCancellationCandidate(boolean cancellationCandidate) {
        this.cancellationCandidate = cancellationCandidate;
    }

    public Timestamp getCancellationDecisionDate() {
        return cancellationDecisionDate;
    }

    public void setCancellationDecisionDate(Timestamp cancellationDecisionDate) {
        this.cancellationDecisionDate = cancellationDecisionDate;
    }

    public Timestamp getCancellationEffectiveDate() {
        return cancellationEffectiveDate;
    }

    public void setCancellationEffectiveDate(Timestamp cancellationEffectiveDate) {
        this.cancellationEffectiveDate = cancellationEffectiveDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

//    public String getLink() {
//        return link;
//    }

//    public void setLink(String link) {
//        this.link = link;
//    }

//    public String getLinkText() {
//        return linkText;
//    }

//    public void setLinkText(String linkText) {
//        this.linkText = linkText;
//    }

    public String getNumberSimultaneousUsers() {
        return numberSimultaneousUsers;
    }

    public void setNumberSimultaneousUsers(String numberSimultaneousUsers) {
        this.numberSimultaneousUsers = numberSimultaneousUsers;
    }

    public String getAuthenticationTypeId() {
        return authenticationTypeId;
    }

    public void setAuthenticationTypeId(String authenticationTypeId) {
        this.authenticationTypeId = authenticationTypeId;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
    }

    public Boolean getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(Boolean staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
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

    public ReceiptStatusRecord getReceiptStatusRecord() {
        return receiptStatusRecord;
    }

    public void setReceiptStatusRecord(ReceiptStatusRecord receiptStatusRecord) {
        this.receiptStatusRecord = receiptStatusRecord;
    }

    public CallNumberTypeRecord getCallNumberTypeRecord() {
        return callNumberTypeRecord;
    }

    public void setCallNumberTypeRecord(CallNumberTypeRecord callNumberTypeRecord) {
        this.callNumberTypeRecord = callNumberTypeRecord;
    }

    public List<ExtentOfOwnerShipRecord> getExtentOfOwnerShipRecords() {
        return extentOfOwnerShipRecords;
    }

    public void setExtentOfOwnerShipRecords(List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords) {
        this.extentOfOwnerShipRecords = extentOfOwnerShipRecords;
    }

    public List<HoldingsNoteRecord> getHoldingsNoteRecords() {
        return holdingsNoteRecords;
    }

    public void setHoldingsNoteRecords(List<HoldingsNoteRecord> holdingsNoteRecords) {
        this.holdingsNoteRecords = holdingsNoteRecords;
    }

    public List<HoldingsUriRecord> getHoldingsUriRecords() {
        return holdingsUriRecords;
    }

    public void setHoldingsUriRecords(List<HoldingsUriRecord> holdingsUriRecords) {
        this.holdingsUriRecords = holdingsUriRecords;
    }

    public List<HoldingsAccessLocation> getHoldingsAccessLocations() {
        return holdingsAccessLocations;
    }

    public void setHoldingsAccessLocations(List<HoldingsAccessLocation> holdingsAccessLocations) {
        this.holdingsAccessLocations = holdingsAccessLocations;
    }

    public AuthenticationTypeRecord getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationTypeRecord authenticationType) {
        this.authenticationType = authenticationType;
    }

    public List<EInstanceCoverageRecord> geteInstanceCoverageRecordList() {
        return eInstanceCoverageRecordList;
    }

    public void seteInstanceCoverageRecordList(List<EInstanceCoverageRecord> eInstanceCoverageRecordList) {
        this.eInstanceCoverageRecordList = eInstanceCoverageRecordList;
    }

    public List<EInstancePerpetualAccessRecord> geteInstancePerpetualAccessRecordList() {
        return eInstancePerpetualAccessRecordList;
    }

    public void seteInstancePerpetualAccessRecordList(List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecordList) {
        this.eInstancePerpetualAccessRecordList = eInstancePerpetualAccessRecordList;
    }

    public List<HoldingsStatisticalSearchRecord> getHoldingsStatisticalSearchRecords() {
        return holdingsStatisticalSearchRecords;
    }

    public void setHoldingsStatisticalSearchRecords(List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords) {
        this.holdingsStatisticalSearchRecords = holdingsStatisticalSearchRecords;
    }

    public List<ItemRecord> getItemRecords() {
        return itemRecords;
    }

    public void setItemRecords(List<ItemRecord> itemRecords) {
        this.itemRecords = itemRecords;
    }

    public List<BibRecord> getBibRecords() {
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }

    public List<OLEHoldingsDonorRecord> getDonorList() {
        return donorList;
    }

    public void setDonorList(List<OLEHoldingsDonorRecord> donorList) {
        this.donorList = donorList;
    }

    public String getMaterialsSpecified() {
        return materialsSpecified;
    }

    public void setMaterialsSpecified(String materialsSpecified) {
        this.materialsSpecified = materialsSpecified;
    }

    public String getFirstIndicator() {
        return firstIndicator;
    }

    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    public String getSecondIndicator() {
        return secondIndicator;
    }

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
