package org.kuali.ole.select.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 12/27/13
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimNoticeBo extends PersistableBusinessObjectBase{
    private String id;
    private String nameOfTheSender;
    private String nameOfTheVendor;
    private String claimDate;
    private String claimCount;
    private String claimType;
    private String title;
    private String placeOfPublication;
    private String publication;
    private String publicationDate;
    private String enumeration;
    private String chronology;
    private String vendorsLibraryAcctNum;
    private String vendorOrderNumber;
    private String vendorTitleNumber;
    private String libraryPurchaseOrderNumber;
    private String unboundLocation;
    private String mailAddress;
    private boolean active;
    private String claimTypeName;


    public String getClaimTypeName() {
        if(claimType!=null){
            Map<String,String> map = new HashMap<>();
            map.put("oleClaimTypeCode",claimType);
            List<OLEClaimType> oleClaimTypeList = (List<OLEClaimType>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEClaimType.class, map);
            claimTypeName = oleClaimTypeList!=null&& oleClaimTypeList.size()>0 ? oleClaimTypeList.get(0).getOleClaimTypeName() : "";
        }
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOfTheSender() {
        return nameOfTheSender;
    }

    public void setNameOfTheSender(String nameOfTheSender) {
        this.nameOfTheSender = nameOfTheSender;
    }

    public String getNameOfTheVendor() {
        return nameOfTheVendor;
    }

    public void setNameOfTheVendor(String nameOfTheVendor) {
        this.nameOfTheVendor = nameOfTheVendor;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(String claimCount) {
        this.claimCount = claimCount;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getVendorsLibraryAcctNum() {
        return vendorsLibraryAcctNum;
    }

    public void setVendorsLibraryAcctNum(String vendorsLibraryAcctNum) {
        this.vendorsLibraryAcctNum = vendorsLibraryAcctNum;
    }

    public String getVendorOrderNumber() {
        return vendorOrderNumber;
    }

    public void setVendorOrderNumber(String vendorOrderNumber) {
        this.vendorOrderNumber = vendorOrderNumber;
    }

    public String getVendorTitleNumber() {
        return vendorTitleNumber;
    }

    public void setVendorTitleNumber(String vendorTitleNumber) {
        this.vendorTitleNumber = vendorTitleNumber;
    }

    public String getLibraryPurchaseOrderNumber() {
        return libraryPurchaseOrderNumber;
    }

    public void setLibraryPurchaseOrderNumber(String libraryPurchaseOrderNumber) {
        this.libraryPurchaseOrderNumber = libraryPurchaseOrderNumber;
    }

    public String getUnboundLocation() {
        return unboundLocation;
    }

    public void setUnboundLocation(String unboundLocation) {
        this.unboundLocation = unboundLocation;
    }
}
