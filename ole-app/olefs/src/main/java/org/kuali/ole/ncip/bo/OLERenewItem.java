package org.kuali.ole.ncip.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OLERenewItem {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("pastDueDate")
    private String pastDueDate;

    @JsonProperty("newDueDate")
    private String newDueDate;

    @JsonProperty("renewalCount")
    private String renewalCount;

    @JsonProperty("itemBarcode")
    private String itemBarcode;

    /*This following fields are only for SIP2*/
    @JsonIgnore
    private String patronBarcode;

    @JsonIgnore
    private String titleIdentifier;

    @JsonIgnore
    private String feeType;

    @JsonIgnore
    private String feeAmount;

    @JsonIgnore
    private String mediaType;

    @JsonIgnore
    private String itemProperties;

    @JsonIgnore
    private String transactionId;
    /*This above fields are only for SIP2*/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPastDueDate() {
        return pastDueDate;
    }

    public void setPastDueDate(String pastDueDate) {
        this.pastDueDate = pastDueDate;
    }

    public String getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(String newDueDate) {
        this.newDueDate = newDueDate;
    }

    public String getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(String renewalCount) {
        this.renewalCount = renewalCount;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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
