package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/19/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EHoldingsRecord extends PersistableBusinessObjectBase implements Serializable {

    private String eHoldingsIdentifier;
    private String formerHoldingsId;
    private String eInstanceIdentifier;
    private String uniqueIdPrefix;
    private String accessStatus;
    private String platform;
    //private String statusDate;
    private Timestamp statusDate;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String publisher;
    private boolean staffOnly;
    private String imprint;
    private String statisticalSearchCode;
    private String subscriptionStatus;
    private String fundCode;
    private String currentFYCost;
    private String paymentStatus;
    private String adminUsername;
    private String adminPassword;
    private String adminURL;
    private String vendor;
    private String purchaseOrderId;
    private String orderType;
    private String orderFormat;
    private String relatedInstanceId;
    private String donorPublicDisplayNote;
    private String donorNote;
    private String link;
    private String linkText;
    private String localPersistentLink;
    private Boolean iLLAllowed;
    private String authenticationType;
    private String proxiedResource;
    private String numberSimultaneousUsers;
    private String accessLocation;
    private String accessUsername;
    private String accessPassword;
    private String location;
    private String locationLevel;
    private String callNumberPrefix;
    private String callNumber;
    private String shelvingOrder;
    private String callNumberTypeId;
    private String eResourceTitle;
    private String eResourceId;
    private EInstanceRecord eInstanceRecord;
    private CallNumberTypeRecord callNumberTypeRecord;

    private List<EInstanceCoverageRecord> eInstanceCoverageRecordList;
    private List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecordList;
    private List<EHoldingsNote> eHoldingsNoteList;

    public EHoldingsRecord() {
        eInstanceCoverageRecordList = new ArrayList<EInstanceCoverageRecord>();
        eInstancePerpetualAccessRecordList = new ArrayList<EInstancePerpetualAccessRecord>();
        eHoldingsNoteList = new ArrayList<EHoldingsNote>();
    }

    public String geteHoldingsIdentifier() {
        return eHoldingsIdentifier;
    }

    public void seteHoldingsIdentifier(String eHoldingsIdentifier) {
        this.eHoldingsIdentifier = eHoldingsIdentifier;
    }

    public String getFormerHoldingsId() {
        return formerHoldingsId;
    }

    public void setFormerHoldingsId(String formerHoldingsId) {
        this.formerHoldingsId = formerHoldingsId;
    }

    public String geteInstanceIdentifier() {
        return eInstanceIdentifier;
    }

    public void seteInstanceIdentifier(String eInstanceIdentifier) {
        this.eInstanceIdentifier = eInstanceIdentifier;
    }

    public String getUniqueIdPrefix() {
        return uniqueIdPrefix;
    }

    public void setUniqueIdPrefix(String uniqueIdPrefix) {
        this.uniqueIdPrefix = uniqueIdPrefix;
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

    public Timestamp getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Timestamp statusDate) {
        this.statusDate = statusDate;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(boolean staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getStatisticalSearchCode() {
        return statisticalSearchCode;
    }

    public void setStatisticalSearchCode(String statisticalSearchCode) {
        this.statisticalSearchCode = statisticalSearchCode;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getCurrentFYCost() {
        return currentFYCost;
    }

    public void setCurrentFYCost(String currentFYCost) {
        this.currentFYCost = currentFYCost;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminURL() {
        return adminURL;
    }

    public void setAdminURL(String adminURL) {
        this.adminURL = adminURL;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderFormat() {
        return orderFormat;
    }

    public void setOrderFormat(String orderFormat) {
        this.orderFormat = orderFormat;
    }

    public String getRelatedInstanceId() {
        return relatedInstanceId;
    }

    public void setRelatedInstanceId(String relatedInstanceId) {
        this.relatedInstanceId = relatedInstanceId;
    }

    public String getDonorPublicDisplayNote() {
        return donorPublicDisplayNote;
    }

    public void setDonorPublicDisplayNote(String donorPublicDisplayNote) {
        this.donorPublicDisplayNote = donorPublicDisplayNote;
    }

    public String getDonorNote() {
        return donorNote;
    }

    public void setDonorNote(String donorNote) {
        this.donorNote = donorNote;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLocalPersistentLink() {
        return localPersistentLink;
    }

    public void setLocalPersistentLink(String localPersistentLink) {
        this.localPersistentLink = localPersistentLink;
    }

    public Boolean getiLLAllowed() {
        return iLLAllowed;
    }

    public void setiLLAllowed(Boolean iLLAllowed) {
        this.iLLAllowed = iLLAllowed;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getProxiedResource() {
        return proxiedResource;
    }

    public void setProxiedResource(String proxiedResource) {
        this.proxiedResource = proxiedResource;
    }

    public String getNumberSimultaneousUsers() {
        return numberSimultaneousUsers;
    }

    public void setNumberSimultaneousUsers(String numberSimultaneousUsers) {
        this.numberSimultaneousUsers = numberSimultaneousUsers;
    }

    public String getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(String accessLocation) {
        this.accessLocation = accessLocation;
    }

    public String getAccessUsername() {
        return accessUsername;
    }

    public void setAccessUsername(String accessUsername) {
        this.accessUsername = accessUsername;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
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

    public String geteResourceTitle() {
        return eResourceTitle;
    }

    public void seteResourceTitle(String eResourceTitle) {
        this.eResourceTitle = eResourceTitle;
    }

    public String geteResourceId() {
        return eResourceId;
    }

    public void seteResourceId(String eResourceId) {
        this.eResourceId = eResourceId;
    }

    public EInstanceRecord geteInstanceRecord() {
        return eInstanceRecord;
    }

    public void seteInstanceRecord(EInstanceRecord eInstanceRecord) {
        this.eInstanceRecord = eInstanceRecord;
    }

    public CallNumberTypeRecord getCallNumberTypeRecord() {
        return callNumberTypeRecord;
    }

    public void setCallNumberTypeRecord(CallNumberTypeRecord callNumberTypeRecord) {
        this.callNumberTypeRecord = callNumberTypeRecord;
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

    public List<EHoldingsNote> geteHoldingsNoteList() {
        return eHoldingsNoteList;
    }

    public void seteHoldingsNoteList(List<EHoldingsNote> eHoldingsNoteList) {
        this.eHoldingsNoteList = eHoldingsNoteList;
    }
}
