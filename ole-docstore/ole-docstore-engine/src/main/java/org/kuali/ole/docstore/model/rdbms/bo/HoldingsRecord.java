package org.kuali.ole.docstore.model.rdbms.bo;

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
    private String formerHoldingsId;
    private Boolean staffOnlyFlag;
    private String uri;
    private String location;
    private String content;
    private String instanceId;
    private String uniqueIdPrefix;
    private String locationLevel;
    private String callNumberPrefix;
    private String callNumber;
    private String shelvingOrder;
    private String callNumberTypeId;
    private String receiptStatusId;
    private String copyNumber;

    private ReceiptStatusRecord receiptStatusRecord;
    private CallNumberTypeRecord callNumberTypeRecord;
    private List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords;
    private List<HoldingsNoteRecord> holdingsNoteRecords;
    private List<AccessUriRecord> accessUriRecords;

    private Timestamp createdDate;
        private Timestamp updatedDate;
        private String createdBy;
        private String updatedBy;

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

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getFormerHoldingsId() {
        return formerHoldingsId;
    }

    public void setFormerHoldingsId(String formerHoldingsId) {
        this.formerHoldingsId = formerHoldingsId;
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

    public Boolean getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(Boolean staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public ReceiptStatusRecord getReceiptStatusRecord() {
        return receiptStatusRecord;
    }

    public void setReceiptStatusRecord(ReceiptStatusRecord receiptStatusRecord) {
        this.receiptStatusRecord = receiptStatusRecord;
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

    public String getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(String locationLevel) {
        this.locationLevel = locationLevel;
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

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public String getReceiptStatusId() {
        return receiptStatusId;
    }

    public void setReceiptStatusId(String receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    public List<AccessUriRecord> getAccessUriRecords() {
        return accessUriRecords;
    }

    public void setAccessUriRecords(List<AccessUriRecord> accessUriRecords) {
        this.accessUriRecords = accessUriRecords;
    }
}
