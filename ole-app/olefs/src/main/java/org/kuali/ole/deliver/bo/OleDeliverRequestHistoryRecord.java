package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/7/12
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestHistoryRecord extends PersistableBusinessObjectBase {
    private String requestHistoryId;
    private String requestId;
    private String itemId;
    private String itemBarcode;
    private String patronId;
    private String loanTransactionId;
    private String poLineItemNumber;
    private String deliverRequestTypeCode;
    private String pickUpLocationCode;
    private String operatorId;
    private String machineId;
    private Date archiveDate;
    private Timestamp createDate;
    private String requestStatus;
	
    private String requestOutComeStatus;

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getRequestHistoryId() {
        return requestHistoryId;
    }

    public void setRequestHistoryId(String requestHistoryId) {
        this.requestHistoryId = requestHistoryId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLoanTransactionId() {
        return loanTransactionId;
    }

    public void setLoanTransactionId(String loanTransactionId) {
        this.loanTransactionId = loanTransactionId;
    }

    public String getPoLineItemNumber() {
        return poLineItemNumber;
    }

    public void setPoLineItemNumber(String poLineItemNumber) {
        this.poLineItemNumber = poLineItemNumber;
    }

    public String getDeliverRequestTypeCode() {
        return deliverRequestTypeCode;
    }

    public void setDeliverRequestTypeCode(String deliverRequestTypeCode) {
        this.deliverRequestTypeCode = deliverRequestTypeCode;
    }

    public String getPickUpLocationCode() {
        return pickUpLocationCode;
    }

    public void setPickUpLocationCode(String pickUpLocationCode) {
        this.pickUpLocationCode = pickUpLocationCode;
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

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestOutComeStatus() {
        return requestOutComeStatus;
    }

    public void setRequestOutComeStatus(String requestOutComeStatus) {
        this.requestOutComeStatus = requestOutComeStatus;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}
