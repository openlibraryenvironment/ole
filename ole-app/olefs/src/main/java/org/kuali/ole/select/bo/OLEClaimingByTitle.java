package org.kuali.ole.select.bo;

import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.vnd.businessobject.VendorTransmissionFormatDetail;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/18/14
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimingByTitle {
    private String author;
    private String title;
    private String placeOfPublication;
    private String publisherName;
    private String publicationDate;
    private String isxn;
    private String vendorItemIdentifier;
    private String poOrderedNum;
    private String poOrderedDate;
    private String claimNumber;
    private String claimNote;
    private OLEClaimingAddress toAddress;
    private OLEClaimingAddress fromAddress;
    private String poItemId;

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

    public OLEClaimingByTitle() {
        super();
    }

    public String getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(String poItemId) {
        this.poItemId = poItemId;
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

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIsxn() {
        return isxn;
    }

    public void setIsxn(String isxn) {
        this.isxn = isxn;
    }

    public String getVendorItemIdentifier() {
        return vendorItemIdentifier;
    }

    public void setVendorItemIdentifier(String vendorItemIdentifier) {
        this.vendorItemIdentifier = vendorItemIdentifier;
    }

    public String getPoOrderedNum() {
        return poOrderedNum;
    }

    public void setPoOrderedNum(String poOrderedNum) {
        this.poOrderedNum = poOrderedNum;
    }

    public String getPoOrderedDate() {
        return poOrderedDate;
    }

    public void setPoOrderedDate(String poOrderedDate) {
        this.poOrderedDate = poOrderedDate;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getClaimNote() {
        return claimNote;
    }

    public void setClaimNote(String claimNote) {
        this.claimNote = claimNote;
    }
}
