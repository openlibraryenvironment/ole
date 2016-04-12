package org.kuali.ole.deliver.bo;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * BarcodeStatus provides barcode status information through getter and setter.
 */

public class FeeType extends PersistableBusinessObjectBase {

    private String id;
    private String feeType;
    private KualiDecimal feeAmount;
    private String billNumber;
    private Integer viewBillNumber;
    private OleFeeType oleFeeType;
    private PatronBillPayment patronBillPayment;
    private String itemBarcode;
    private String itemTitle;
    private String itemType;
    private String feeTypeName;
    private OleItemSearch oleItemSearch;
    private List<OleItemLevelBillPayment> itemLevelBillPaymentList;
    private boolean activeItem;
    private String paymentStatus;
    private String itemUuid;
    private String paymentStatusCode;
    private String forgiveErrorNote;

    private String itemAuthor;
    private String itemCallNumber;
    private String itemEnumeration;
    private String itemChronology;
    private String itemOwnLocation;
    private String itemCopyNumber;
    private Timestamp billDate = new Timestamp(new Date().getTime());
    private KualiDecimal balFeeAmount = new KualiDecimal(0);
    private KualiDecimal paidAmount = new KualiDecimal(0);
    private org.kuali.ole.deliver.bo.OlePaymentStatus olePaymentStatus = new org.kuali.ole.deliver.bo.OlePaymentStatus();
    private String forgiveNote;
    private String errorNote;
    private String cancellationNote;
    private String generalNote;
    private String feeSource;
    private List<FeeType> feeTypes=new ArrayList<FeeType>();
    private Timestamp dueDate;
    private Date checkOutDate;
    private Timestamp checkInDate;
    private Timestamp lastTransactionDate;
    private Timestamp overrideCheckInDate;
    private KualiDecimal creditIssued = new KualiDecimal(0);
    private KualiDecimal creditRemaining = new KualiDecimal(0);
    private String creditNote;
    private String transferNote;
    private String refundNote;
    private String cancelCreditNote;
    private boolean manualProcessBill = false;
    private Timestamp renewalDate;

    public String getForgiveNote() {
        return forgiveNote;
    }

    public void setForgiveNote(String forgiveNote) {
        this.forgiveNote = forgiveNote;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        this.errorNote = errorNote;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getFeeSource() {
        return feeSource;
    }

    public void setFeeSource(String feeSource) {
        this.feeSource = feeSource;
    }


    public String getPaymentStatusCode() {
        return olePaymentStatus.getPaymentStatusCode();
    }

    public void setPaymentStatusCode(String paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    public org.kuali.ole.deliver.bo.OlePaymentStatus getOlePaymentStatus() {
        return olePaymentStatus;
    }

    public void setOlePaymentStatus(OlePaymentStatus olePaymentStatus) {
        this.olePaymentStatus = olePaymentStatus;
    }

    public KualiDecimal getBalFeeAmount() {
        return balFeeAmount;
    }

    public void setBalFeeAmount(KualiDecimal balFeeAmount) {
        this.balFeeAmount = balFeeAmount;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public String getItemAuthor() {
        return itemAuthor;
    }

    public void setItemAuthor(String itemAuthor) {
        this.itemAuthor = itemAuthor;
    }

    public String getItemCallNumber() {
        return itemCallNumber;
    }

    public void setItemCallNumber(String itemCallNumber) {
        this.itemCallNumber = itemCallNumber;
    }

    public String getItemEnumeration() {
        return itemEnumeration;
    }

    public void setItemEnumeration(String itemEnumeration) {
        this.itemEnumeration = itemEnumeration;
    }

    public String getItemChronology() {
        return itemChronology;
    }

    public void setItemChronology(String itemChronology) {
        this.itemChronology = itemChronology;
    }

    public String getItemCopyNumber() {
        return itemCopyNumber;
    }

    public void setItemCopyNumber(String itemCopyNumber) {
        this.itemCopyNumber = itemCopyNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public boolean isActiveItem() {
        return activeItem;
    }

    public void setActiveItem(boolean activeItem) {
        this.activeItem = activeItem;
    }

    public OleItemSearch getOleItemSearch() {
        return oleItemSearch;
    }

    public void setOleItemSearch(OleItemSearch oleItemSearch) {
        this.oleItemSearch = oleItemSearch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PatronBillPayment getPatronBillPayment() {
        return patronBillPayment;
    }

    public void setPatronBillPayment(PatronBillPayment patronBillPayment) {
        this.patronBillPayment = patronBillPayment;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public KualiDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(KualiDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public OleFeeType getOleFeeType() {
        return oleFeeType;
    }

    public void setOleFeeType(OleFeeType oleFeeType) {
        this.oleFeeType = oleFeeType;
    }

    public String getFeeTypeName() {
        return oleFeeType.getFeeTypeName();
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("id", id);
        return toStringMap;
    }

    public List<OleItemLevelBillPayment> getItemLevelBillPaymentList() {
        return itemLevelBillPaymentList;
    }

    public void setItemLevelBillPaymentList(List<OleItemLevelBillPayment> itemLevelBillPaymentList) {
        this.itemLevelBillPaymentList = itemLevelBillPaymentList;
    }

    public KualiDecimal getPaidAmount() {
        if (feeAmount != null && balFeeAmount != null && feeAmount.compareTo(balFeeAmount) > 0) {
            paidAmount = feeAmount.subtract(balFeeAmount);
        }
        return paidAmount;
    }

    public void setPaidAmount(KualiDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getForgiveErrorNote() {
        return forgiveErrorNote;
    }

    public void setForgiveErrorNote(String forgiveErrorNote) {
        this.forgiveErrorNote = forgiveErrorNote;
    }

    public List<FeeType> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(List<FeeType> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Timestamp checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Timestamp getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Timestamp lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Integer getViewBillNumber() {
        return Integer.parseInt(this.getBillNumber());
    }

    public void setViewBillNumber(Integer viewBillNumber) {
        this.viewBillNumber = viewBillNumber;
    }

    public KualiDecimal getCreditIssued() {
        return creditIssued;
    }

    public void setCreditIssued(KualiDecimal creditIssued) {
        this.creditIssued = creditIssued;
    }

    public KualiDecimal getCreditRemaining() {
        return creditRemaining;
    }

    public void setCreditRemaining(KualiDecimal creditRemaining) {
        this.creditRemaining = creditRemaining;
    }

    public String getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(String creditNote) {
        this.creditNote = creditNote;
    }

    public String getItemOwnLocation() {
        return itemOwnLocation;
    }

    public void setItemOwnLocation(String itemOwnLocation) {
        this.itemOwnLocation = itemOwnLocation;
    }

    public Timestamp getOverrideCheckInDate() {
        return overrideCheckInDate;
    }

    public void setOverrideCheckInDate(Timestamp overrideCheckInDate) {
        this.overrideCheckInDate = overrideCheckInDate;
    }

    public String getTransferNote() {
        return transferNote;
    }

    public void setTransferNote(String transferNote) {
        this.transferNote = transferNote;
    }

    public String getRefundNote() {
        return refundNote;
    }

    public void setRefundNote(String refundNote) {
        this.refundNote = refundNote;
    }

    public String getCancelCreditNote() {
        return cancelCreditNote;
    }

    public void setCancelCreditNote(String cancelCreditNote) {
        this.cancelCreditNote = cancelCreditNote;
    }

    public boolean isManualProcessBill() {
        return manualProcessBill;
    }

    public void setManualProcessBill(boolean manualProcessBill) {
        this.manualProcessBill = manualProcessBill;
    }

    public Timestamp getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Timestamp renewalDate) {
        this.renewalDate = renewalDate;
    }
}
