package org.kuali.ole.ncip.bo;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class OLELookupUser {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("patronId")
    private String patronId;

    @JsonProperty("patronName")
    private OlePatronNameBo patronName;

    @JsonProperty("patronEmail")
    private OlePatronEmailBo patronEmail;

    @JsonProperty("patronAddress")
    private OlePatronAddressBo patronAddress;

    @JsonProperty("patronPhone")
    private OlePatronPhoneBo patronPhone;

    @JsonProperty("oleUserPrivileges")
    private List<OLEUserPrivilege> oleUserPrivileges;

    @JsonProperty("oleHolds")
    private OLEHolds oleHolds;

    @JsonProperty("oleCheckedOutItems")
    private OLECheckedOutItems oleCheckedOutItems;

    @JsonProperty("oleItemFines")
    private OLEItemFines oleItemFines;

    /*This following fields are only for SIP2*/
    @JsonIgnore
    private boolean validPatron;
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

    public boolean isValidPatron() {
        return validPatron;
    }

    public void setValidPatron(boolean isValidPatron) {
        this.validPatron = isValidPatron;
    }
}
