package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;

/**
 * Created by maheswarang on 2/25/15.
 */
public class OleRenewalHistory extends PersistableBusinessObjectBase {
    private String oleRenewalHistoryId;
    private String loanId;
    private String patronBarcode;
    private String itemBarcode;
    private String itemId;
    private Timestamp renewedDate;
    private Timestamp renewalDueDate;
    private String operatorId;

    public String getOleRenewalHistoryId() {
        return oleRenewalHistoryId;
    }

    public void setOleRenewalHistoryId(String oleRenewalHistoryId) {
        this.oleRenewalHistoryId = oleRenewalHistoryId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Timestamp getRenewedDate() {
        return renewedDate;
    }

    public void setRenewedDate(Timestamp renewedDate) {
        this.renewedDate = renewedDate;
    }

    public Timestamp getRenewalDueDate() {
        return renewalDueDate;
    }

    public void setRenewalDueDate(Timestamp renewalDueDate) {
        this.renewalDueDate = renewalDueDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
