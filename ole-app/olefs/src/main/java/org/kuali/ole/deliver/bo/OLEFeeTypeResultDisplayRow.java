package org.kuali.ole.deliver.bo;

import org.kuali.rice.core.api.util.type.KualiDecimal;

/**
 * Created by chenchulakshmig on 1/20/15.
 */
public class OLEFeeTypeResultDisplayRow {

    private String borrowerFirstName;
    private String borrowerLastName;
    private String borrowerBarcode;
    private String borrowerId;
    private String feeType;
    private KualiDecimal feeAmount;

    public String getBorrowerFirstName() {
        return borrowerFirstName;
    }

    public void setBorrowerFirstName(String borrowerFirstName) {
        this.borrowerFirstName = borrowerFirstName;
    }

    public String getBorrowerLastName() {
        return borrowerLastName;
    }

    public void setBorrowerLastName(String borrowerLastName) {
        this.borrowerLastName = borrowerLastName;
    }

    public String getBorrowerBarcode() {
        return borrowerBarcode;
    }

    public void setBorrowerBarcode(String borrowerBarcode) {
        this.borrowerBarcode = borrowerBarcode;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public KualiDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(KualiDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }
}
