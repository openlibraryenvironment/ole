package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 1/13/14
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEFlaggedItems extends PersistableBusinessObjectBase {
    private String title;
    private String barcode;
    private String callNumber;
    private String copyNumber;
    private String flagType;
    private String flagNote;
    private String location;

    private boolean claimsReturned;
    private boolean damaged;
    private boolean missingPiece;

    private String  claimsReturnedNote;
    private String  damagedNote;
    private String  missingPieceNote;

    private String itemUuid;
    private String instanceUuid;
    private String bibUuid;

    private String patronId;
    private String patronNote;
    private String PatronNoteId;
    private String patronFlagType;
    private String patronBarcode;
    private String patronName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getFlagType() {
        return flagType;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public String getFlagNote() {
        return flagNote;
    }

    public void setFlagNote(String flagNote) {
        this.flagNote = flagNote;
    }

    public boolean isClaimsReturned() {
        return claimsReturned;
    }

    public void setClaimsReturned(boolean claimsReturned) {
        this.claimsReturned = claimsReturned;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isMissingPiece() {
        return missingPiece;
    }

    public void setMissingPiece(boolean missingPiece) {
        this.missingPiece = missingPiece;
    }

    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    public void setClaimsReturnedNote(String claimsReturnedNote) {
        this.claimsReturnedNote = claimsReturnedNote;
    }

    public String getDamagedNote() {
        return damagedNote;
    }

    public void setDamagedNote(String damagedNote) {
        this.damagedNote = damagedNote;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public String getBibUuid() {
        return bibUuid;
    }

    public void setBibUuid(String bibUuid) {
        this.bibUuid = bibUuid;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronNote() {
        return patronNote;
    }

    public void setPatronNote(String patronNote) {
        this.patronNote = patronNote;
    }

    public String getPatronNoteId() {
        return PatronNoteId;
    }

    public void setPatronNoteId(String patronNoteId) {
        PatronNoteId = patronNoteId;
    }

    public String getPatronFlagType() {
        return patronFlagType;
    }

    public void setPatronFlagType(String patronFlagType) {
        this.patronFlagType = patronFlagType;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }
}
