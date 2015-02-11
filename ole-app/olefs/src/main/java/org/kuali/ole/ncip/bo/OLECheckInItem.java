package org.kuali.ole.ncip.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/29/13
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OLECheckInItem {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("title")
    private  String title;

    @JsonProperty("author")
    private  String author;

    @JsonProperty("description")
    private  String description;

    @JsonProperty("callNumber")
    private  String callNumber;

    @JsonProperty("userId")
    private  String userId;

    @JsonProperty("userType")
    private String userType;

    @JsonProperty("itemType")
    private String itemType;

    @JsonProperty("barcode")
    private String barcode;

    /*This following fields are only for SIP2*/
    @JsonIgnore
    private String patronBarcode;

    @JsonIgnore
    private String itemLocation;
    /*This above fields are only for SIP2*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }
}
