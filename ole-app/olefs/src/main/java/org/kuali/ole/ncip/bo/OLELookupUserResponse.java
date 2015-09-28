package org.kuali.ole.ncip.bo;

import org.kuali.ole.bo.OLEUserPrivilege;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/4/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLELookupUserResponse {
    private String patronId;
    private OLELookupUserEntityNameBo patronName;
    private OLELookupUserEntityEmailBo patronEmail;
    private OLELookupUserEntityAddressBo patronAddress;
    private OLELookupUserEntityPhoneBo patronPhone;
    private List<OLEUserPrivilege> oleUserPrivileges;

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public OLELookupUserEntityNameBo getPatronName() {
        return patronName;
    }

    public void setPatronName(OLELookupUserEntityNameBo patronName) {
        this.patronName = patronName;
    }

    public OLELookupUserEntityEmailBo getPatronEmail() {
        return patronEmail;
    }

    public void setPatronEmail(OLELookupUserEntityEmailBo patronEmail) {
        this.patronEmail = patronEmail;
    }

    public OLELookupUserEntityAddressBo getPatronAddress() {
        return patronAddress;
    }

    public void setPatronAddress(OLELookupUserEntityAddressBo patronAddress) {
        this.patronAddress = patronAddress;
    }

    public List<OLEUserPrivilege> getOleUserPrivileges() {
        return oleUserPrivileges;
    }

    public void setOleUserPrivileges(List<OLEUserPrivilege> oleUserPrivileges) {
        this.oleUserPrivileges = oleUserPrivileges;
    }

    public OLELookupUserEntityPhoneBo getPatronPhone() {
        return patronPhone;
    }

    public void setPatronPhone(OLELookupUserEntityPhoneBo patronPhone) {
        this.patronPhone = patronPhone;
    }
}
