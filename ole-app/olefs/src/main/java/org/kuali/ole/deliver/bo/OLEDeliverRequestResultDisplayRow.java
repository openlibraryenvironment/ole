package org.kuali.ole.deliver.bo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by chenchulakshmig on 1/20/15.
 */
public class OLEDeliverRequestResultDisplayRow {

    private Integer borrowerQueuePosition;
    private String requestId;
    private String requestTypeCode;
    private String borrowerFirstName;
    private String borrowerLastName;
    private String itemUuid;
    private String borrowerBarcode;
    private Timestamp createDate;
    private Date expiryDate;
    private Date onHoldExpirationDate;
    private String pickUpLocation;

    public Integer getBorrowerQueuePosition() {
        return borrowerQueuePosition;
    }

    public void setBorrowerQueuePosition(Integer borrowerQueuePosition) {
        this.borrowerQueuePosition = borrowerQueuePosition;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

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

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public void setBorrowerBarcode(String borrowerBarcode) {
        this.borrowerBarcode = borrowerBarcode;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getOnHoldExpirationDate() {
        return onHoldExpirationDate;
    }

    public void setOnHoldExpirationDate(Date onHoldExpirationDate) {
        this.onHoldExpirationDate = onHoldExpirationDate;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
