package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

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
    private String loanTransactionId;
    private String poLineItemNumber;
    private String deliverRequestTypeCode;
    private String pickUpLocationCode;
    private String operatorId;
    private String machineId;
    private Date archiveDate;

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


}
