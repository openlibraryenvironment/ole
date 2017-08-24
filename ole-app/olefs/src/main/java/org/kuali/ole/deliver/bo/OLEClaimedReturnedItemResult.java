package org.kuali.ole.deliver.bo;

/**
 * Created by chenchulakshmig on 3/16/16.
 */
public class OLEClaimedReturnedItemResult {

    private boolean select;
    private String itemId;
    private String itemBarcode;
    private String patronBarcode;
    private String callNumber;
    private String callNumberPrefix;
    private String title;
    private String author;
    private String copyNumber;
    private String enumeration;
    private String chronology;
    private String location;
    private String dateOfClaim;
    private String claimReturnNote;
    private String itemType;
    private OleLoanDocument oleLoanDocument;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
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

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfClaim() {
        return dateOfClaim;
    }

    public void setDateOfClaim(String dateOfClaim) {
        this.dateOfClaim = dateOfClaim;
    }

    public String getClaimReturnNote() {
        return claimReturnNote;
    }

    public void setClaimReturnNote(String claimReturnNote) {
        this.claimReturnNote = claimReturnNote;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public OleLoanDocument getOleLoanDocument() {
        return oleLoanDocument;
    }

    public void setOleLoanDocument(OleLoanDocument oleLoanDocument) {
        this.oleLoanDocument = oleLoanDocument;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }
}
