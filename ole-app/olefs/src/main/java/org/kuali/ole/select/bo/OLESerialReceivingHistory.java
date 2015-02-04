package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 6/29/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingHistory extends PersistableBusinessObjectBase {

    private String serialReceivingRecordHistoryId;
    private String receivingRecordType;
    private String chronologyCaptionLevel1;
    private String chronologyCaptionLevel2;
    private String chronologyCaptionLevel3;
    private String chronologyCaptionLevel4;
    private String claimCount;
    private Date claimDate;
    private String claimNote;
    private String claimType;
    private String claimTypeName;
    private String claimResponse;
    private String enumerationCaptionLevel1;
    private String enumerationCaptionLevel2;
    private String enumerationCaptionLevel3;
    private String enumerationCaptionLevel4;
    private String enumerationCaptionLevel5;
    private String enumerationCaptionLevel6;
    private boolean publicDisplay;
    private String serialReceiptNote;
    private String operatorId;
    private String machineId;
    private String receiptStatus;
    private Date receiptDate;
    private String publicReceipt;
    private String staffOnlyReceipt;
    private OLESerialReceivingDocument oleSerialReceivingDocument;
    private String serialReceivingRecordId;
    private String documentNumber;
    private String publicDisplayHistory;
    private String enumerationCaption;
    private String chronologyCaption;

    private boolean documentExist=false;
    private boolean validRecordType = true;
    private OLEClaimType oleClaimType;

    public String getClaimTypeName() {
        if(oleClaimType!=null){
            claimTypeName = oleClaimType.getOleClaimTypeName();
        }
        return claimTypeName;
    }

    public OLEClaimType getOleClaimType() {
        return oleClaimType;
    }

    public void setOleClaimType(OLEClaimType oleClaimType) {
        this.oleClaimType = oleClaimType;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getSerialReceivingRecordHistoryId() {
        return serialReceivingRecordHistoryId;
    }

    public void setSerialReceivingRecordHistoryId(String serialReceivingRecordHistoryId) {
        this.serialReceivingRecordHistoryId = serialReceivingRecordHistoryId;
    }

    public String getReceivingRecordType() {
        return receivingRecordType;
    }

    public void setReceivingRecordType(String receivingRecordType) {
        this.receivingRecordType = receivingRecordType;
    }

    public String getChronologyCaptionLevel3() {
        return chronologyCaptionLevel3;
    }

    public void setChronologyCaptionLevel3(String chronologyCaptionLevel3) {
        this.chronologyCaptionLevel3 = chronologyCaptionLevel3;
    }

    public String getChronologyCaptionLevel4() {
        return chronologyCaptionLevel4;
    }

    public void setChronologyCaptionLevel4(String chronologyCaptionLevel4) {
        this.chronologyCaptionLevel4 = chronologyCaptionLevel4;
    }

    public String getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(String claimCount) {
        this.claimCount = claimCount;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimNote() {
        return claimNote;
    }

    public void setClaimNote(String claimNote) {
        this.claimNote = claimNote;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimResponse() {
        return claimResponse;
    }

    public void setClaimResponse(String claimResponse) {
        this.claimResponse = claimResponse;
    }

    public String getEnumerationCaptionLevel3() {
        return enumerationCaptionLevel3;
    }

    public void setEnumerationCaptionLevel3(String enumerationCaptionLevel3) {
        this.enumerationCaptionLevel3 = enumerationCaptionLevel3;
    }

    public String getEnumerationCaptionLevel4() {
        return enumerationCaptionLevel4;
    }

    public void setEnumerationCaptionLevel4(String enumerationCaptionLevel4) {
        this.enumerationCaptionLevel4 = enumerationCaptionLevel4;
    }

    public String getEnumerationCaptionLevel5() {
        return enumerationCaptionLevel5;
    }

    public void setEnumerationCaptionLevel5(String enumerationCaptionLevel5) {
        this.enumerationCaptionLevel5 = enumerationCaptionLevel5;
    }

    public String getEnumerationCaptionLevel6() {
        return enumerationCaptionLevel6;
    }

    public void setEnumerationCaptionLevel6(String enumerationCaptionLevel6) {
        this.enumerationCaptionLevel6 = enumerationCaptionLevel6;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public String getSerialReceiptNote() {
        return serialReceiptNote;
    }

    public void setSerialReceiptNote(String serialReceiptNote) {
        this.serialReceiptNote = serialReceiptNote;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getEnumerationCaptionLevel1() {
        return enumerationCaptionLevel1;
    }

    public void setEnumerationCaptionLevel1(String enumerationCaptionLevel1) {
        this.enumerationCaptionLevel1 = enumerationCaptionLevel1;
    }

    public String getEnumerationCaptionLevel2() {
        return enumerationCaptionLevel2;
    }

    public void setEnumerationCaptionLevel2(String enumerationCaptionLevel2) {
        this.enumerationCaptionLevel2 = enumerationCaptionLevel2;
    }

    public String getChronologyCaptionLevel1() {
        return chronologyCaptionLevel1;
    }

    public void setChronologyCaptionLevel1(String chronologyCaptionLevel1) {
        this.chronologyCaptionLevel1 = chronologyCaptionLevel1;
    }

    public String getChronologyCaptionLevel2() {
        return chronologyCaptionLevel2;
    }

    public void setChronologyCaptionLevel2(String chronologyCaptionLevel2) {
        this.chronologyCaptionLevel2 = chronologyCaptionLevel2;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPublicReceipt() {
        return publicReceipt;
    }

    public void setPublicReceipt(String publicReceipt) {
        this.publicReceipt = publicReceipt;
    }

    public String getStaffOnlyReceipt() {
        return staffOnlyReceipt;
    }

    public void setStaffOnlyReceipt(String staffOnlyReceipt) {
        this.staffOnlyReceipt = staffOnlyReceipt;
    }

    public OLESerialReceivingDocument getOleSerialReceivingDocument() {
        return oleSerialReceivingDocument;
    }

    public void setOleSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        this.oleSerialReceivingDocument = oleSerialReceivingDocument;
    }

    public String getSerialReceivingRecordId() {
        return serialReceivingRecordId;
    }

    public void setSerialReceivingRecordId(String serialReceivingRecordId) {
        this.serialReceivingRecordId = serialReceivingRecordId;
    }

    public String getPublicDisplayHistory() {
        return publicDisplayHistory;
    }

    public void setPublicDisplayHistory(String publicDisplayHistory) {
        this.publicDisplayHistory = publicDisplayHistory;
    }

    public String getEnumerationCaption() {
        return enumerationCaption;
    }

    public void setEnumerationCaption(String enumerationCaption) {
        this.enumerationCaption = enumerationCaption;
    }

    public String getChronologyCaption() {
        return chronologyCaption;
    }

    public void setChronologyCaption(String chronologyCaption) {
        this.chronologyCaption = chronologyCaption;
    }

    public boolean isDocumentExist() {
        return documentExist;
    }

    public void setDocumentExist(boolean documentExist) {
        this.documentExist = documentExist;
    }

    public boolean isValidRecordType() {
        return validRecordType;
    }

    public void setValidRecordType(boolean validRecordType) {
        this.validRecordType = validRecordType;
    }
}

