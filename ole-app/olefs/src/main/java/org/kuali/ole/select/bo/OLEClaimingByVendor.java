package org.kuali.ole.select.bo;

import org.kuali.ole.vnd.businessobject.VendorTransmissionFormatDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/18/14
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimingByVendor {
    private String vendorName;
    private OLEClaimingAddress toAddress;
    private OLEClaimingAddress fromAddress;
    private List<OLEClaimingByTitle> oleClaimingByTitles;

    private VendorTransmissionFormatDetail vendorTransmissionFormatDetail;
    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public VendorTransmissionFormatDetail getVendorTransmissionFormatDetail() {
        return vendorTransmissionFormatDetail;
    }

    public void setVendorTransmissionFormatDetail(VendorTransmissionFormatDetail vendorTransmissionFormatDetail) {
        this.vendorTransmissionFormatDetail = vendorTransmissionFormatDetail;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public OLEClaimingAddress getToAddress() {
        return toAddress;
    }

    public void setToAddress(OLEClaimingAddress toAddress) {
        this.toAddress = toAddress;
    }

    public OLEClaimingAddress getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(OLEClaimingAddress fromAddress) {
        this.fromAddress = fromAddress;
    }

    public List<OLEClaimingByTitle> getOleClaimingByTitles() {
        return oleClaimingByTitles;
    }

    public void setOleClaimingByTitles(List<OLEClaimingByTitle> oleClaimingByTitles) {
        this.oleClaimingByTitles = oleClaimingByTitles;
    }
}
