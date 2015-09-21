package org.kuali.ole.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/29/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OLECheckOutItem {

    @JsonProperty("code")
    public String code;

    @JsonProperty("message")
    public String message;

    @JsonProperty("dueDate")
    public String dueDate;

    @JsonProperty("renewalCount")
    public String renewalCount;

    @JsonProperty("userType")
    public String userType;

    @JsonProperty("itemType")
    public String itemType;

    @JsonProperty("barcode")
    public String barcode;

    @JsonProperty("patronId")
    public String patronId;

    @JsonProperty("patronBarcode")
    public String patronBarcode;

    /*This following fields are only for SIP2*/
    @JsonIgnore
    private String titleIdentifier;

    @JsonIgnore
    private String feeType;

    @JsonIgnore
    private String feeAmount;

    @JsonIgnore
    private String itemProperties;

    @JsonIgnore
    private String transactionId;
    /*This above fields are only for SIP2*/



    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(String renewalCount) {
        this.renewalCount = renewalCount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }


    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    public void setTitleIdentifier(String titleIdentifier) {
        this.titleIdentifier = titleIdentifier;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getItemProperties() {
        return itemProperties;
    }

    public void setItemProperties(String itemProperties) {
        this.itemProperties = itemProperties;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
