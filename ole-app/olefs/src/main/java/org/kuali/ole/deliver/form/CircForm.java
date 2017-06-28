package org.kuali.ole.deliver.form;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 6/18/15.
 */
public class CircForm extends OLEForm {
    /*
    Need loggedInUser is the principalId used for checking for permissions etc.
    overridingPrincipalName is the operator who has overriding privs. Needed for the loan.
     */
    private String loggedInUser = null;
    private String overridingPrincipalName;

    /*
    selectedCirculationDesk selected circ desk.
    previouslySelectedCirculationDesk is to maintain the state
    in case we need to revert back to the previously selected desk.
     */
    private String selectedCirculationDesk;
    private String previouslySelectedCirculationDesk;

    private ErrorMessage errorMessage = new ErrorMessage();

    private boolean proxyCheckDone;
    private boolean itemValidationDone;
    private boolean itemOverride;
    private boolean requestExistOrLoanedCheck;
    private String cancelRequest;

    /*
    patronBarcode: Current Borrower
     */
    private String patronBarcode;
    private OlePatronDocument patronDocument;


    private String itemBarcode;
    private ItemRecord itemRecord;

    private boolean proceedWithCheckout;
    private boolean showExistingLoan;

    private boolean autoCheckout;


    private OleLoanFastAdd oleLoanFastAdd;

    private Date customDueDateMap;
    private String customDueDateTime;
    private String customDueDateTimeMessage;

    private List<OleLoanDocument> loanDocumentListForCurrentSession;
    private List<OleLoanDocument> existingLoanList;
    private List<OleLoanDocument> loanDocumentsForAlterDueDate;
    private List<OleLoanDocument> loanDocumentsForRenew;

    private String claimsReturnNote;

    private boolean claimsReturnFlag;

    private String damagedItemNote;

    private boolean damagedItemFlag;

    private String missingPieceNote;

    private String numberOfPiece;

    private String missingPieceCount;

    private boolean missingPieceFlag;

    private String pageNumber;

    private String pageSize;

    private String maxSessionTime;


    private Date customDueDateMapForRenew;
    private String customDueDateTimeForRenew;
    private String customDueDateTimeMessageForRenew;

    private boolean recordNoteForClaimsReturn;
    private boolean itemFoundInLibrary;
    private boolean recordNoteForDamagedItem;
    private boolean recordNoteForMissingPiece;

    private String missingPieceMatchCheck;
    private String mismatchedMissingPieceNote;
    private String missingPieces;

    private String urlBase;
    private String viewBillUrl;
    private String createBillUrl;

    private Date loanDueDateToAllEntries;
    private String loanTimeToAllEntries;

    private String itemLostNote;
    private String itemReplaceNote;

    private String createNewPatronLink;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getSelectedCirculationDesk() {
        return selectedCirculationDesk;
    }

    public void setSelectedCirculationDesk(String selectedCirculationDesk) {
        this.selectedCirculationDesk = selectedCirculationDesk;
    }

    public String getPreviouslySelectedCirculationDesk() {
        return previouslySelectedCirculationDesk;
    }

    public void setPreviouslySelectedCirculationDesk(String previouslySelectedCirculationDesk) {
        this.previouslySelectedCirculationDesk = previouslySelectedCirculationDesk;
    }

    public void reset() {
        this.itemBarcode = null;
        this.itemRecord = null;
        this.showExistingLoan = false;
        this.proxyCheckDone = false;
        this.itemValidationDone = false;
        this.itemOverride = false;
        if (null != getExistingLoanList()) {
            this.getExistingLoanList().clear();
        }
        if (null != getLoanDocumentListForCurrentSession()) {
            this.getLoanDocumentListForCurrentSession().clear();
        }
        this.patronDocument = new OlePatronDocument();
        this.proceedWithCheckout = false;
        this.claimsReturnNote = null;
        this.damagedItemNote = null;
        this.missingPieceNote = null;
        this.missingPieceCount = null;
        this.itemLostNote = null;
        this.numberOfPiece=null;
    }

    public void resetForAutoCheckout() {
        this.itemRecord = null;
        this.showExistingLoan = false;
        this.proxyCheckDone = false;
        this.itemValidationDone = false;
        this.itemOverride = false;
        if (null != getExistingLoanList()) {
            this.getExistingLoanList().clear();
        }
        if (null != getLoanDocumentListForCurrentSession()) {
            this.getLoanDocumentListForCurrentSession().clear();
        }
        this.patronDocument = new OlePatronDocument();
        this.proceedWithCheckout = false;
        this.claimsReturnNote = null;
        this.damagedItemNote = null;
        this.missingPieceNote = null;
        this.itemLostNote = null;
        this.missingPieceCount = null;
        this.numberOfPiece=null;
    }

    public void resetAll() {
        this.previouslySelectedCirculationDesk = this.selectedCirculationDesk;
        this.patronBarcode = null;
        this.itemBarcode = null;
        this.itemRecord = null;
        this.showExistingLoan = false;
        this.proxyCheckDone = false;
        this.itemValidationDone = false;
        this.itemOverride = false;
        if (null != getExistingLoanList()) {
            this.getExistingLoanList().clear();
        }
        if (null != getLoanDocumentListForCurrentSession()) {
            this.getLoanDocumentListForCurrentSession().clear();
        }
        this.patronDocument = new OlePatronDocument();
        this.proceedWithCheckout = false;
        this.claimsReturnNote = null;
        this.damagedItemNote = null;
        this.missingPieceNote = null;
        this.itemLostNote = null;
        this.missingPieceCount = null;
        this.numberOfPiece=null;
        this.createNewPatronLink = null;
    }

    public OlePatronDocument getPatronDocument() {
        return patronDocument;
    }

    public void setPatronDocument(OlePatronDocument patronDocument) {
        this.patronDocument = patronDocument;
        if (null != patronDocument) {
            this.patronBarcode = patronDocument.getBarcode();
        }
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOverridingPrincipalName() {
        return overridingPrincipalName;
    }

    public void setOverridingPrincipalName(String overridingPrincipalName) {
        this.overridingPrincipalName = overridingPrincipalName;
    }

    public boolean isProceedWithCheckout() {
        return proceedWithCheckout;
    }

    public void setProceedWithCheckout(boolean proceedWithCheckout) {
        this.proceedWithCheckout = proceedWithCheckout;
    }

    public boolean isProxyCheckDone() {
        return proxyCheckDone;
    }

    public boolean isItemValidationDone() {
        return itemValidationDone;
    }

    public void setItemValidationDone(boolean itemValidationDone) {
        this.itemValidationDone = itemValidationDone;
    }

    public void setProxyCheckDone(boolean proxyCheckDone) {
        this.proxyCheckDone = proxyCheckDone;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public boolean isShowExistingLoan() {
        return showExistingLoan;
    }

    public void setShowExistingLoan(boolean showExistingLoan) {
        this.showExistingLoan = showExistingLoan;
    }

    public List<OleLoanDocument> getLoanDocumentListForCurrentSession() {
        if (null == loanDocumentListForCurrentSession) {
            loanDocumentListForCurrentSession = new ArrayList<>();
        }
        return loanDocumentListForCurrentSession;
    }

    public void setLoanDocumentListForCurrentSession(List<OleLoanDocument> loanDocumentListForCurrentSession) {
        this.loanDocumentListForCurrentSession = loanDocumentListForCurrentSession;
    }

    public void setItemRecord(ItemRecord itemRecord) {
        this.itemRecord = itemRecord;
    }

    public ItemRecord getItemRecord() {
        return itemRecord;
    }

    public String getCustomDueDateTimeMessage() {
        return customDueDateTimeMessage;
    }

    public void setCustomDueDateTimeMessage(String customDueDateTimeMessage) {
        this.customDueDateTimeMessage = customDueDateTimeMessage;
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

    public void setExistingLoanList(List<OleLoanDocument> existingLoanList) {
        this.existingLoanList = existingLoanList;
    }

    public List<OleLoanDocument> getExistingLoanList() {
        return existingLoanList;
    }

    public OleLoanFastAdd getOleLoanFastAdd() {
        return oleLoanFastAdd;
    }

    public void setOleLoanFastAdd(OleLoanFastAdd oleLoanFastAdd) {
        this.oleLoanFastAdd = oleLoanFastAdd;
    }

    public boolean isItemOverride() {
        return itemOverride;
    }

    public void setItemOverride(boolean itemOverride) {
        this.itemOverride = itemOverride;
    }

    public boolean isAutoCheckout() {
        return autoCheckout;
    }

    public void setAutoCheckout(boolean autoCheckout) {
        this.autoCheckout = autoCheckout;
    }

    public String getClaimsReturnNote() {
        return claimsReturnNote;
    }

    public void setClaimsReturnNote(String claimsReturnNote) {
        this.claimsReturnNote = claimsReturnNote;
    }

    public List<OleLoanDocument> getLoanDocumentsForAlterDueDate() {
        return loanDocumentsForAlterDueDate;
    }

    public void setLoanDocumentsForAlterDueDate(List<OleLoanDocument> loanDocumentsForAlterDueDate) {
        this.loanDocumentsForAlterDueDate = loanDocumentsForAlterDueDate;
    }

    public String getDamagedItemNote() {
        return damagedItemNote;
    }

    public void setDamagedItemNote(String damagedItemNote) {
        this.damagedItemNote = damagedItemNote;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public String getNumberOfPiece() {
        return numberOfPiece;
    }

    public void setNumberOfPiece(String numberOfPiece) {
        this.numberOfPiece = numberOfPiece;
    }

    public String getMissingPieceCount() {
        return missingPieceCount;
    }

    public void setMissingPieceCount(String missingPieceCount) {
        this.missingPieceCount = missingPieceCount;
    }

    public List<OleLoanDocument> getLoanDocumentsForRenew() {
        return loanDocumentsForRenew;
    }

    public void setLoanDocumentsForRenew(List<OleLoanDocument> loanDocumentsForRenew) {
        this.loanDocumentsForRenew = loanDocumentsForRenew;
    }

    public Date getCustomDueDateMapForRenew() {
        return customDueDateMapForRenew;
    }

    public void setCustomDueDateMapForRenew(Date customDueDateMapForRenew) {
        this.customDueDateMapForRenew = customDueDateMapForRenew;
    }

    public String getCustomDueDateTimeForRenew() {
        return customDueDateTimeForRenew;
    }

    public void setCustomDueDateTimeForRenew(String customDueDateTimeForRenew) {
        this.customDueDateTimeForRenew = customDueDateTimeForRenew;
    }

    public String getCustomDueDateTimeMessageForRenew() {
        return customDueDateTimeMessageForRenew;
    }

    public void setCustomDueDateTimeMessageForRenew(String customDueDateTimeMessageForRenew) {
        this.customDueDateTimeMessageForRenew = customDueDateTimeMessageForRenew;
    }

    public boolean isClaimsReturnFlag() {
        return claimsReturnFlag;
    }

    public void setClaimsReturnFlag(boolean claimsReturnFlag) {
        this.claimsReturnFlag = claimsReturnFlag;
    }

    public boolean isMissingPieceFlag() {
        return missingPieceFlag;
    }

    public void setMissingPieceFlag(boolean missingPieceFlag) {
        this.missingPieceFlag = missingPieceFlag;
    }

    public boolean isDamagedItemFlag() {
        return damagedItemFlag;
    }

    public void setDamagedItemFlag(boolean damagedItemFlag) {
        this.damagedItemFlag = damagedItemFlag;
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

    public String getMismatchedMissingPieceNote() {
        return mismatchedMissingPieceNote;
    }

    public void setMismatchedMissingPieceNote(String mismatchedMissingPieceNote) {
        this.mismatchedMissingPieceNote = mismatchedMissingPieceNote;
    }

    public String getMissingPieces() {
        return missingPieces;
    }

    public void setMissingPieces(String missingPieces) {
        this.missingPieces = missingPieces;
    }

    public void setMissingPieceMatchCheck(String missingPieceMatchCheck) {
        this.missingPieceMatchCheck = missingPieceMatchCheck;
    }

    public boolean isRequestExistOrLoanedCheck() {
        return requestExistOrLoanedCheck;
    }

    public void setRequestExistOrLoanedCheck(boolean requestExistOrLoanedCheck) {
        this.requestExistOrLoanedCheck = requestExistOrLoanedCheck;
    }

    public String getMaxSessionTime() {
        return maxSessionTime;
    }

    public void setMaxSessionTime(String maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getViewBillUrl() {
        return viewBillUrl;
    }

    public void setViewBillUrl(String viewBillUrl) {
        this.viewBillUrl = viewBillUrl;
    }

    public String getCreateBillUrl() {
        return createBillUrl;
    }

    public void setCreateBillUrl(String createBillUrl) {
        this.createBillUrl = createBillUrl;
    }
    
    public String getCreateNewPatronLink() {
        return createNewPatronLink;
    }

    public void setCreateNewPatronLink(String createNewPatronLink) {
        this.createNewPatronLink = createNewPatronLink;
    }

    public String getItemLostNote() {
        return itemLostNote;
    }

    public String getItemReplaceNote() {
        return itemReplaceNote;
    }

    public void setItemReplaceNote(String itemReplaceNote) {
        this.itemReplaceNote = itemReplaceNote;
    }

    public void setItemLostNote(String itemLostNote) {
        this.itemLostNote = itemLostNote;
    }

    public Date getLoanDueDateToAllEntries() {
        return loanDueDateToAllEntries;
    }

    public void setLoanDueDateToAllEntries(Date loanDueDateToAllEntries) {
        this.loanDueDateToAllEntries = loanDueDateToAllEntries;
    }

    public String getLoanTimeToAllEntries() {
        return loanTimeToAllEntries;
    }

    public void setLoanTimeToAllEntries(String loanTimeToAllEntries) {
        this.loanTimeToAllEntries = loanTimeToAllEntries;
    }

    public boolean isItemFoundInLibrary() {
        return itemFoundInLibrary;
    }

    public void setItemFoundInLibrary(boolean itemFoundInLibrary) {
        this.itemFoundInLibrary = itemFoundInLibrary;
    }

    public String getCancelRequest() {
        return cancelRequest;
    }

    public void setCancelRequest(String cancelRequest) {
        this.cancelRequest = cancelRequest;
    }
}
