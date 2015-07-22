package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/3/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLoanFastAdd extends PersistableBusinessObjectBase {

    private String title;
    private String locationName;
    private String circulationLocation;
    private String itemType;
    private String callNumber;
    private String callNumberPrefix;
    private String labelForCopyNumber;
    private String enumeration;
    private String checkinNote;
    private String note;
    private String shelvingLocation;
    private String barcode;
    private String itemStatus;
    private String callNumberType;
    private String copyNumber;
    private String chronology;
    private String numberOfPieces;
    private String author;
    private String callNumberRequired = "false";


    public String getCallNumberRequired() {
        return callNumberRequired;
    }

    public void setCallNumberRequired(String callNumberRequired) {
        this.callNumberRequired = callNumberRequired;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(String numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCirculationLocation() {
        return circulationLocation;
    }

    public void setCirculationLocation(String circulationLocation) {
        this.circulationLocation = circulationLocation;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getLabelForCopyNumber() {
        return labelForCopyNumber;
    }

    public void setLabelForCopyNumber(String labelForCopyNumber) {
        this.labelForCopyNumber = labelForCopyNumber;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getCheckinNote() {
        return checkinNote;
    }

    public void setCheckinNote(String checkinNote) {
        this.checkinNote = checkinNote;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getCallNumberType() {
        return callNumberType;
    }

    public void setCallNumberType(String callNumberType) {
        this.callNumberType = callNumberType;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }
}
