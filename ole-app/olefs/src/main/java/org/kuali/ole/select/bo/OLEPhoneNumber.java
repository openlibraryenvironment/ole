package org.kuali.ole.select.bo;

/**
 * Created by srirams on 26/9/14.
 *
 *  This class is used to hold the phone number details of vendor.
 *
 */
public class OLEPhoneNumber {

    private String phoneNumber;
    private String phoneNumberType;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(String phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }
}
