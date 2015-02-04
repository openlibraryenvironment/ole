package org.kuali.ole.ncip.bo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLELookupUser {
    private String code;
    private String message;
    private String patronId;
    private OlePatronNameBo patronName;
    private OlePatronEmailBo patronEmail;
    private OlePatronAddressBo patronAddress;
    private OlePatronPhoneBo patronPhone;
    private List<OLEUserPrivilege> oleUserPrivileges;
    private OLEHolds oleHolds;
    private OLECheckedOutItems oleCheckedOutItems;
    private OLEItemFines oleItemFines;

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

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public OlePatronNameBo getPatronName() {
        return patronName;
    }

    public void setPatronName(OlePatronNameBo patronName) {
        this.patronName = patronName;
    }

    public OlePatronEmailBo getPatronEmail() {
        return patronEmail;
    }

    public void setPatronEmail(OlePatronEmailBo patronEmail) {
        this.patronEmail = patronEmail;
    }

    public OlePatronAddressBo getPatronAddress() {
        return patronAddress;
    }

    public void setPatronAddress(OlePatronAddressBo patronAddress) {
        this.patronAddress = patronAddress;
    }

    public OlePatronPhoneBo getPatronPhone() {
        return patronPhone;
    }

    public void setPatronPhone(OlePatronPhoneBo patronPhone) {
        this.patronPhone = patronPhone;
    }

    public List<OLEUserPrivilege> getOleUserPrivileges() {
        return oleUserPrivileges;
    }

    public void setOleUserPrivileges(List<OLEUserPrivilege> oleUserPrivileges) {
        this.oleUserPrivileges = oleUserPrivileges;
    }

    public OLEHolds getOleHolds() {
        return oleHolds;
    }

    public void setOleHolds(OLEHolds oleHolds) {
        this.oleHolds = oleHolds;
    }

    public OLECheckedOutItems getOleCheckedOutItems() {
        return oleCheckedOutItems;
    }

    public void setOleCheckedOutItems(OLECheckedOutItems oleCheckedOutItems) {
        this.oleCheckedOutItems = oleCheckedOutItems;
    }

    public OLEItemFines getOleItemFines() {
        return oleItemFines;
    }

    public void setOleItemFines(OLEItemFines oleItemFines) {
        this.oleItemFines = oleItemFines;
    }
}
