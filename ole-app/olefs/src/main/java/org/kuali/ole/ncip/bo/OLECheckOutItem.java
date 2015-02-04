package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/29/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckOutItem {
    public String code;
    public String message;
    public String dueDate;
    public String renewalCount;
    public String userType;
    public String itemType;
    public String barcode;
    public String patronId;
    public String patronBarcode;



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
}
