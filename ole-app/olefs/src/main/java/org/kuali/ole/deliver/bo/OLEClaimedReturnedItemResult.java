package org.kuali.ole.deliver.bo;

/**
 * Created by chenchulakshmig on 3/16/16.
 */
public class OLEClaimedReturnedItemResult {

    private boolean select;
    private String itemId;
    private String callNumber;
    private String title;
    private String copyNumber;
    private String enumeration;
    private String chronology;
    private String location;
    private String dateOfClaim;
    private String claimReturnNote;
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

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public OleLoanDocument getOleLoanDocument() {
        return oleLoanDocument;
    }

    public void setOleLoanDocument(OleLoanDocument oleLoanDocument) {
        this.oleLoanDocument = oleLoanDocument;
    }
}
