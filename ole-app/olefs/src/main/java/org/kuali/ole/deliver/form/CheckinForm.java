package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pvsubrah on 7/22/15.
 */
public class CheckinForm extends OLEForm {

    private String selectedCirculationDesk;
    private String previouslySelectedCirculationDesk;
    private String itemBarcode;
    private Date customDueDateMap;
    private String customDueDateTime;
    private String loggedInUser;
    private List<CheckedInItem> checkedInItemList;
    private CheckedInItem checkedInItem;
    private boolean permissionToRemoveNote;
    private boolean showPrintSlip;
    private ErrorMessage errorMessage;

    private boolean recordNoteForClaimsReturn;
    private boolean itemFoundInLibrary;
    private boolean recordNoteForDamagedItem;
    private boolean recordNoteForLostItemWithBill;
    private boolean recordNoteForMissingPiece;

    private String missingPieceMatchCheck;
    private String missingPieceNote;
    private String missingPieceCount;
    private String noOfPieces;

    private String maxSessionTime;

    private String routeToLocation;
    private boolean printOnHoldSlipQueue;
    private String printFormat;

    private String locationPopupMsg;

    public String getSelectedCirculationDesk() {
        return selectedCirculationDesk;
    }

    public void setSelectedCirculationDesk(String selectedCirculationDesk) {
        this.selectedCirculationDesk = selectedCirculationDesk;
    }


    public void setPreviouslySelectedCirculationDesk(String previouslySelectedCirculationDesk) {
        this.previouslySelectedCirculationDesk = previouslySelectedCirculationDesk;
    }

    public String getPreviouslySelectedCirculationDesk() {
        return previouslySelectedCirculationDesk;
    }

    public void resetAll() {
        this.previouslySelectedCirculationDesk = this.selectedCirculationDesk;
        this.itemBarcode = "";
        this.checkedInItem = null;
        setDroolsExchange(null);
        this.checkedInItemList = new ArrayList<>();
        this.permissionToRemoveNote = false;
        this.setShowPrintSlip(false);
        this.errorMessage = new ErrorMessage();
        this.routeToLocation = null;
        this.setPrintOnHoldSlipQueue(false);
        this.customDueDateMap = new Date();
        this.customDueDateTime = null;
        this.printFormat = null;
    }

    public void reset() {
        this.previouslySelectedCirculationDesk = this.selectedCirculationDesk;
        this.itemBarcode = "";
        this.errorMessage = new ErrorMessage();
        this.routeToLocation = null;
        this.missingPieceMatchCheck = "";
        this.missingPieceCount = "";
        this.missingPieceNote = "";
        this.recordNoteForClaimsReturn = false;
        this.itemFoundInLibrary = false;
        this.recordNoteForDamagedItem = false;
        this.recordNoteForMissingPiece = false;
        this.recordNoteForLostItemWithBill = false;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public Date getCustomDueDateMap() {
        return customDueDateMap;
    }

    public void setCustomDueDateMap(Date customDueDateMap) {
        this.customDueDateMap = customDueDateMap;
    }

    public String getCustomDueDateTime() {
        return customDueDateTime;
    }

    public void setCustomDueDateTime(String customDueDateTime) {
        this.customDueDateTime = customDueDateTime;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<CheckedInItem> getCheckedInItemList() {
        if (null == checkedInItemList) {
            checkedInItemList = new ArrayList<>();
        }
        return checkedInItemList;
    }

    public void setCheckedInItemList(List<CheckedInItem> checkedInItemList) {
        this.checkedInItemList = checkedInItemList;
    }

    public CheckedInItem getCheckedInItem() {
        if (null == checkedInItem) {
            return new CheckedInItem();
        }
        return checkedInItem;
    }

    public void setCheckedInItem(CheckedInItem checkedInItem) {
        this.checkedInItem = checkedInItem;
    }

    public boolean isPermissionToRemoveNote() {
        return permissionToRemoveNote;
    }

    public void setPermissionToRemoveNote(boolean permissionToRemoveNote) {
        this.permissionToRemoveNote = permissionToRemoveNote;
    }

    public boolean isShowPrintSlip() {
        return showPrintSlip;
    }

    public void setShowPrintSlip(boolean showPrintSlip) {
        this.showPrintSlip = showPrintSlip;
    }

    public ErrorMessage getErrorMessage() {
        if(null == errorMessage){
            errorMessage = new ErrorMessage();
        }
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isRecordNoteForClaimsReturn() {
        return recordNoteForClaimsReturn;
    }

    public void setRecordNoteForClaimsReturn(boolean recordNoteForClaimsReturn) {
        this.recordNoteForClaimsReturn = recordNoteForClaimsReturn;
    }

    public boolean isRecordNoteForDamagedItem() {
        return recordNoteForDamagedItem;
    }

    public void setRecordNoteForDamagedItem(boolean recordNoteForDamagedItem) {
        this.recordNoteForDamagedItem = recordNoteForDamagedItem;
    }

    public boolean isRecordNoteForMissingPiece() {
        return recordNoteForMissingPiece;
    }

    public void setRecordNoteForMissingPiece(boolean recordNoteForMissingPiece) {
        this.recordNoteForMissingPiece = recordNoteForMissingPiece;
    }

    public String getMissingPieceMatchCheck() {
        return missingPieceMatchCheck;
    }

    public void setMissingPieceMatchCheck(String missingPieceMatchCheck) {
        this.missingPieceMatchCheck = missingPieceMatchCheck;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public String getMissingPieceCount() {
        return missingPieceCount;
    }

    public void setMissingPieceCount(String missingPieceCount) {
        this.missingPieceCount = missingPieceCount;
    }

    public String getMaxSessionTime() {
        return maxSessionTime;
    }

    public void setMaxSessionTime(String maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public String getRouteToLocation() {
        return routeToLocation;
    }

    public void setRouteToLocation(String routeToLocation) {
        this.routeToLocation = routeToLocation;
    }

    public String getNoOfPieces() {
        return noOfPieces;
    }

    public void setNoOfPieces(String noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public boolean isPrintOnHoldSlipQueue() {
        return printOnHoldSlipQueue;
    }

    public void setPrintOnHoldSlipQueue(boolean printOnHoldSlipQueue) {
        this.printOnHoldSlipQueue = printOnHoldSlipQueue;
    }

    public String getPrintFormat() {
        return printFormat;
    }

    public void setPrintFormat(String printFormat) {
        this.printFormat = printFormat;
    }

    public String getLocationPopupMsg() {
        return locationPopupMsg;
    }

    public void setLocationPopupMsg(String locationPopupMsg) {
        this.locationPopupMsg = locationPopupMsg;
    }

    public boolean isItemFoundInLibrary() {
        return itemFoundInLibrary;
    }

    public void setItemFoundInLibrary(boolean itemFoundInLibrary) {
        this.itemFoundInLibrary = itemFoundInLibrary;
    }

    public boolean isRecordNoteForLostItemWithBill() {
        return recordNoteForLostItemWithBill;
    }

    public void setRecordNoteForLostItemWithBill(boolean recordNoteForLostItemWithBill) {
        this.recordNoteForLostItemWithBill = recordNoteForLostItemWithBill;
    }
}
