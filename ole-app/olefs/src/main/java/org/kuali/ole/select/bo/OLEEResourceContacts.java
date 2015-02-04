package org.kuali.ole.select.bo;


        import org.kuali.ole.sys.OLEConstants;
        import org.kuali.ole.vnd.businessobject.VendorContact;
        import org.kuali.ole.vnd.businessobject.VendorDetail;
        import org.kuali.rice.core.api.config.property.ConfigContext;

        import java.util.ArrayList;
        import java.util.List;
/**
 * Created by hemalathas on 12/5/14.
 */
public class OLEEResourceContacts {
    private String organization;

    private String contact;

    private String role;

    private String phone;

    private String email;

    private String format;

    private String note;

    private String vendorLink;
    private int vendorHeaderGeneratedIdentifier;
    private int vendorDetailAssignedIdentifier;
    private boolean hasMorePhoneNo = false;

    private List<OLEPhoneNumber> olePhoneNumbers = new ArrayList<>();

    private String oleERSIdentifier;
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(int vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public int getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(int vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public boolean isHasMorePhoneNo() {
        return hasMorePhoneNo;
    }

    public void setHasMorePhoneNo(boolean hasMorePhoneNo) {
        this.hasMorePhoneNo = hasMorePhoneNo;
    }

    public List<OLEPhoneNumber> getOlePhoneNumbers() {
        return olePhoneNumbers;
    }

    public void setOlePhoneNumbers(List<OLEPhoneNumber> olePhoneNumbers) {
        this.olePhoneNumbers = olePhoneNumbers;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getVendorLink() {
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        String url = oleurl+ "/ole-kr-krad/inquiry?methodToCall=start&amp;dataObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&amp;vendorHeaderGeneratedIdentifier=" +vendorHeaderGeneratedIdentifier + "&amp;vendorDetailAssignedIdentifier="
                +vendorDetailAssignedIdentifier;
        return url;
    }

    public void setVendorLink(String vendorLink) {
        this.vendorLink = vendorLink;
    }
}
