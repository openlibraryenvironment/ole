package org.kuali.ole.systemintegration.rest.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsTree", propOrder = {
        "unboundLocation",
        "callNumber",
        "serialReceivingHistory"
})

@XStreamAlias("SerialReceiving")
@XmlRootElement(name = "SerialReceiving")
public class SerialReceiving {

    @XmlElement(name = "unboundLocation")
    private UnboundLocation unboundLocation;

    @XmlElement(name = "callNumber")
    private String callNumber;

    @XmlElement(name = "serialReceivingHistory")
    private HoldingsSerialHistory serialReceivingHistory;
    //private String serialReceivingRecordId;
    /*private String vendorId;
    private String createDate;
    private String operatorId;
    private String machineId;
    private String boundLocation;
    private String receivingRecordType;
    private boolean claim;
    private String claimIntervalInformation;
    private boolean createItem;
    private String generalReceivingNote;
    private String poId;
    private boolean printLabel;
    private boolean publicDisplay;
    private String subscriptionStatus;
    private String serialReceiptLocation;
    private String serialReceivingRecord;
    private String treatmentInstructionNote;
    private String urgentNote;
    private String subscriptionStatusDate;*/

    public UnboundLocation getUnboundLocation() {
        return unboundLocation;
    }

    public void setUnboundLocation(UnboundLocation unboundLocation) {
        this.unboundLocation = unboundLocation;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public HoldingsSerialHistory getSerialReceivingHistory() {
        return serialReceivingHistory;
    }

    public void setSerialReceivingHistory(HoldingsSerialHistory serialReceivingHistory) {
        this.serialReceivingHistory = serialReceivingHistory;
    }

    /*public String getBoundLocation() {
        return boundLocation;
    }

    public void setBoundLocation(String boundLocation) {
        this.boundLocation = boundLocation;
    }

    public String getReceivingRecordType() {
        return receivingRecordType;
    }

    public void setReceivingRecordType(String receivingRecordType) {
        this.receivingRecordType = receivingRecordType;
    }

    public boolean isClaim() {
        return claim;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }

    public String getClaimIntervalInformation() {
        return claimIntervalInformation;
    }

    public void setClaimIntervalInformation(String claimIntervalInformation) {
        this.claimIntervalInformation = claimIntervalInformation;
    }

    public boolean isCreateItem() {
        return createItem;
    }

    public void setCreateItem(boolean createItem) {
        this.createItem = createItem;
    }

    public String getGeneralReceivingNote() {
        return generalReceivingNote;
    }

    public void setGeneralReceivingNote(String generalReceivingNote) {
        this.generalReceivingNote = generalReceivingNote;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public boolean isPrintLabel() {
        return printLabel;
    }

    public void setPrintLabel(boolean printLabel) {
        this.printLabel = printLabel;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getSerialReceiptLocation() {
        return serialReceiptLocation;
    }

    public void setSerialReceiptLocation(String serialReceiptLocation) {
        this.serialReceiptLocation = serialReceiptLocation;
    }

    public String getSerialReceivingRecord() {
        return serialReceivingRecord;
    }

    public void setSerialReceivingRecord(String serialReceivingRecord) {
        this.serialReceivingRecord = serialReceivingRecord;
    }

    public String getTreatmentInstructionNote() {
        return treatmentInstructionNote;
    }

    public void setTreatmentInstructionNote(String treatmentInstructionNote) {
        this.treatmentInstructionNote = treatmentInstructionNote;
    }

    public String getUrgentNote() {
        return urgentNote;
    }

    public void setUrgentNote(String urgentNote) {
        this.urgentNote = urgentNote;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getSubscriptionStatusDate() {
        return subscriptionStatusDate;
    }

    public void setSubscriptionStatusDate(String subscriptionStatusDate) {
        this.subscriptionStatusDate = subscriptionStatusDate;
    }

    public String getSerialReceivingRecordId() {
        return serialReceivingRecordId;
    }

    public void setSerialReceivingRecordId(String serialReceivingRecordId) {
        this.serialReceivingRecordId = serialReceivingRecordId;
    }*/
}
