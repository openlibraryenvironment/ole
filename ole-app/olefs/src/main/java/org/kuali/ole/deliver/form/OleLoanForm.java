package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The OleLoanForm is the form class that defines all the loan fields required for a loan processing using getters and setters
 * and is involved in passing the data to the UI layer
 */
public class OleLoanForm extends UifFormBase {

    private String patronBarcode;
    private String patronFirstName;
    private String patronName;
    private String patronId;
    private String patronNameURL;
    private String borrowerType;
    private String borrowerTypeId;
    private String item;
    private String itemUuid;
    private String instanceUuid;
    private String message;
    private String returnMessage;
    private String information;
    private String successInfo;
    private String returnInformation;
    private Item oleItem;
    private String author;
    private boolean blockLoan;
    private String returnItemUuid;
    private String itemFlag;
    private String returnInstanceUuid;
    private boolean isClearUI = false;
    private String fastAddUrl;
    private String routeToLocation;
    private boolean checkOut = false;
    private boolean nonCirculatingFlag = false;
    private String proxyPatronName;

    private String checkInTime;
    private String alterDueDateTimeInfo;
    private String loanLoginUserInfo;
    private boolean loanLoginMessage;
    private boolean audioEnable = false;
    private boolean audioForPastDate = false;
    private String returnViewUrl;
    private boolean returnViewUrlFlag;
    private boolean okOrRemoveNote = false;
    private boolean damagedCheckIn = false;
    private String borrowerCode;
    private String loanLoginName;
    private boolean validLogin;
    private boolean removeClaimsReturnFlag;
    private boolean itemClaimsReturnFlag;
    private boolean recordNote=false;
    private String overrideErrorMessage;
    private boolean claimsFlag=false;
    private HashMap<String,String> errorsAndPermission = new HashMap<>();
    private boolean blockUser;
    private boolean itemDamagedStatus;
    private boolean skipDamagedCheckIn;
    private String missingPieceCount;
    private String errorMessage;
    private boolean missingPieceValidationSuccess;
    private boolean inTransit;
    private String missingPieceMessage;
    private boolean missingPieceDialog;
    private boolean damagedItemDialog;
    private String  dialogText;
    private boolean dialogFlag;
    private String  dialogMissingPieceCount;
    private String  dialogItemNoOfPieces;
    private boolean dialogMissingPieceCountReadOnly;
    private boolean dialogItemNoOfPiecesReadOnly;
    private boolean removeMissingPieceButton;
    private String  dialogErrorMessage;
    private String  realPatronId;
    private String  missingPieceNote;
    private OleLoanDocument missingPieceLoanDocument;
    private boolean removeMissingPieceFlag;
    private boolean recordDamagedItemNote;
    private boolean recordMissingPieceNote;
    private boolean recordCheckoutMissingPieceNote;
    private boolean displayRecordNotePopup;
    private boolean checkoutRecordFlag;
    private String checkoutRecordMessage;
    private boolean skipMissingPieceRecordPopup;
    private boolean skipDamagedRecordPopup;
    private boolean displayMissingPieceNotePopup;
    private boolean checkoutMissingPieceRecordFlag;
    private boolean displayDamagedRecordNotePopup;
    private boolean checkoutDamagedRecordFlag;
    private boolean tempClaimsFlag;
    private boolean sendMissingPieceMail;
    private boolean patronbill=false;
    private String successMessage;
    private boolean removeItemDamagedButton;
    private boolean showExistingLoan = false;
    private OleLoanDocument renewalLoan;


    /*
    private boolean itemStatusLost = false;

    public boolean isItemStatusLost() {
        return itemStatusLost;
    }

    public void setItemStatusLost(boolean itemStatusLost) {
        this.itemStatusLost = itemStatusLost;
    }
*/

    public boolean isPatronbill() {
        return patronbill;
    }

    public void setPatronbill(boolean patronbill) {
        this.patronbill = patronbill;
    }

    public String getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }

    public String getPatronNameURL() {
        return patronNameURL;
    }

    public void setPatronNameURL(String patronNameURL) {
        this.patronNameURL = patronNameURL;
    }

    public boolean isValidLogin() {
        return validLogin;
    }

    public boolean isItemClaimsReturnFlag() {
        return itemClaimsReturnFlag;
    }

    public void setItemClaimsReturnFlag(boolean itemClaimsReturnFlag) {
        this.itemClaimsReturnFlag = itemClaimsReturnFlag;
    }

    public void setValidLogin(boolean validLogin) {
        this.validLogin = validLogin;
    }

    public String getLoanLoginName() {
        return loanLoginName;
    }

    public void setLoanLoginName(String loanLoginName) {
        this.loanLoginName = loanLoginName;
    }

    public String getBorrowerCode() {
        return borrowerCode;
    }

    public boolean isClaimsFlag() {
        return claimsFlag;
    }

    public void setClaimsFlag(boolean claimsFlag) {
        this.claimsFlag = claimsFlag;
    }

    public void setBorrowerCode(String borrowerCode) {
        this.borrowerCode = borrowerCode;
    }

    public boolean isDamagedCheckIn() {
        return damagedCheckIn;
    }

    public boolean isRecordNote() {
        return recordNote;
    }

    public void setRecordNote(boolean recordNote) {
        this.recordNote = recordNote;
    }

    public void setDamagedCheckIn(boolean damagedCheckIn) {
        this.damagedCheckIn = damagedCheckIn;
    }

    public boolean isRemoveClaimsReturnFlag() {
        return removeClaimsReturnFlag;
    }

    public void setRemoveClaimsReturnFlag(boolean removeClaimsReturnFlag) {
        this.removeClaimsReturnFlag = removeClaimsReturnFlag;
    }

    public boolean isOkOrRemoveNote() {
        return okOrRemoveNote;
    }

    public void setOkOrRemoveNote(boolean okOrRemoveNote) {
        this.okOrRemoveNote = okOrRemoveNote;
    }

    public boolean isReturnViewUrlFlag() {
        return returnViewUrlFlag;
    }

    public void setReturnViewUrlFlag(boolean returnViewUrlFlag) {
        this.returnViewUrlFlag = returnViewUrlFlag;
    }

    public String getReturnViewUrl() {
        return returnViewUrl;
    }

    public void setReturnViewUrl(String returnViewUrl) {
        this.returnViewUrl = returnViewUrl;
    }

    public String getProxyPatronName() {
        return proxyPatronName;
    }

    public void setProxyPatronName(String proxyPatronName) {
        this.proxyPatronName = proxyPatronName;
    }

    public boolean isAudioForPastDate() {
        return audioForPastDate;
    }

    public void setAudioForPastDate(boolean audioForPastDate) {
        this.audioForPastDate = audioForPastDate;
    }

    public boolean isAudioEnable() {
        return audioEnable;
    }

    public void setAudioEnable(boolean audioEnable) {
        this.audioEnable = audioEnable;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    public void setCheckOut(boolean checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * item focus after loaning an item.
     */
    private boolean itemFocus = false;

    /**
     * patron focus for new patron
     */
    private boolean patronFocus = false;


    /**
     * Circulation Desk attributes
     */
    private String circulationDesk;
    private String confirmMessage;
    private boolean changeLocationFlag;
    /**
     * Fast-Add item attributes
     */
    private OleLoanFastAdd oleLoanFastAdd;
    private boolean fastAddItemIndicator = false;

    /**
     * Claims Return attributes
     */
    private String claimsReturnNote;
    private boolean claimsReturnFlag = false;
    /**
     * Patron User Note Display attributes
     */
    private String patronUserNote;
    private String patronNoteTypeId;
    private boolean patronNoteFlag = false;
    /**
     * Proxy patron  attributes
     */
    private String realPatronBarcode;
    private String proxyPatronId;
    private String realPatronType;
    private String realPatronName;
    private List<OlePatronDocument> currentPatronList;
    private List<OlePatronDocument> realPatronList;
    private boolean realPatronFlag = false;
    private boolean selfCheckOut = false;
    /**
     * Alter Due Date attributes
     */
    private List<OleLoanDocument> alterDueDateList;
    private boolean alterDueDateFlag = false;

    /**
     * Override attributes
     */
    private String newPrincipalId;
    private String newPrincipalName;
    private String overideMethodCall;
    private boolean overrideFlag = false;
    private String oldPrincipalId;

    /**
     * Edit patron
     */
    private boolean addressVerified = false;

    /**
     * Print due date slip
     */

    private boolean dueDateSlip = false;

    /**
     * Session Time out
     */
    private int maxSessionTime;
    private String maxTimeForCheckOutConstant;

    /**
     * print slip
     */
    private String oleFormKey;

    /**
     * backGroundCheckIn
     */
    private boolean backGroundCheckIn = false;

    private boolean dueDateEmpty = false;
    private Date dueDateMap;
    private Date popDateMap;
    private String popDateTime;
    private String popDateTimeInfo;
    private boolean success = true;
    private boolean returnSuccess = true;
    private OleLoanDocument dummyLoan;
    private OleLoanDocument backUpDummyLoan;
    private List<OleLoanDocument> loanList;
    private List<OleLoanDocument> existingLoanList;

    private List<OleLoanDocument> itemReturnList;


    private String overrideLoginMessage;

    private String preferredAddress;
    private String email;
    private String phoneNumber;

    private boolean numberOfPieces = false;
    private String description;
    private String matchCheck;
    private String billName;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal paymentAmount;
    private boolean billAvailability = false;
    private String billAlertMessage;
    private String dateAlertMessage;
    private boolean claimsReturned = false;
    private boolean copyRequest = false;
    private boolean patronRequest = false;
    private String copyCheck;
    private Integer maxTimeForCheckInDate;
    private Integer checkInDateMaxTime;

    //renewal

    private boolean overrideRenewal;
    private List<OleLoanDocument> renewDueDateList;
    private boolean renewDueDateFlag = false;
    private boolean renewalFlag = false;
    private boolean overrideRenewItemFlag = false;
    private String checkInNote;

    private String loginUser;

    private boolean blockPatron;
    private boolean blockItem;
    private String roleName;
    private boolean renewPermission = true;
    private boolean proxyDisplay;
    private boolean holdSlip = false;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isBlockPatron() {
        return blockPatron;
    }

    public void setBlockPatron(boolean blockPatron) {
        this.blockPatron = blockPatron;
    }

    public boolean isBlockItem() {
        return blockItem;
    }

    public void setBlockItem(boolean blockItem) {
        this.blockItem = blockItem;
    }

    public OleLoanDocument getBackUpDummyLoan() {
        return backUpDummyLoan;
    }

    public void setBackUpDummyLoan(OleLoanDocument backUpDummyLoan) {
        this.backUpDummyLoan = backUpDummyLoan;
    }

    public List<OlePatronDocument> getCurrentPatronList() {
        return currentPatronList;
    }

    public void setCurrentPatronList(List<OlePatronDocument> currentPatronList) {
        this.currentPatronList = currentPatronList;
    }

    public boolean isNonCirculatingFlag() {
        return nonCirculatingFlag;
    }

    public void setNonCirculatingFlag(boolean nonCirculatingFlag) {
        this.nonCirculatingFlag = nonCirculatingFlag;
    }

    public String getPatronFirstName() {
        return patronFirstName;
    }

    public void setPatronFirstName(String patronFirstName) {
        this.patronFirstName = patronFirstName;
    }

    public String getFastAddUrl() {
        return fastAddUrl;
    }

    public void setFastAddUrl(String fastAddUrl) {
        this.fastAddUrl = fastAddUrl;
    }

    public String getCheckInNote() {
        return checkInNote;
    }

    public void setCheckInNote(String checkInNote) {
        this.checkInNote = checkInNote;
    }

    public String getReturnItemUuid() {
        return returnItemUuid;
    }

    public void setReturnItemUuid(String returnItemUuid) {
        this.returnItemUuid = returnItemUuid;
    }

    public String getReturnInstanceUuid() {
        return returnInstanceUuid;
    }

    public void setReturnInstanceUuid(String returnInstanceUuid) {
        this.returnInstanceUuid = returnInstanceUuid;
    }

    public boolean isPatronFocus() {
        return patronFocus;
    }

    public void setPatronFocus(boolean patronFocus) {
        this.patronFocus = patronFocus;
    }

    public boolean isItemFocus() {
        return itemFocus;
    }

    public void setItemFocus(boolean itemFocus) {
        this.itemFocus = itemFocus;
    }

    public boolean isBackGroundCheckIn() {
        return backGroundCheckIn;
    }

    public void setBackGroundCheckIn(boolean backGroundCheckIn) {
        this.backGroundCheckIn = backGroundCheckIn;
    }

    public boolean isRenewalFlag() {
        return renewalFlag;
    }

    public void setRenewalFlag(boolean renewalFlag) {
        this.renewalFlag = renewalFlag;
    }

    public boolean isOverrideRenewItemFlag() {
        return overrideRenewItemFlag;
    }

    public void setOverrideRenewItemFlag(boolean overrideRenewItemFlag) {
        this.overrideRenewItemFlag = overrideRenewItemFlag;
    }

    public boolean isFastAddItemIndicator() {
        return fastAddItemIndicator;
    }

    public void setFastAddItemIndicator(boolean fastAddItemIndicator) {
        this.fastAddItemIndicator = fastAddItemIndicator;
    }

    public List<OleLoanDocument> getRenewDueDateList() {
        return renewDueDateList;
    }

    public void setRenewDueDateList(List<OleLoanDocument> renewDueDateList) {
        this.renewDueDateList = renewDueDateList;
    }

    public boolean isRenewDueDateFlag() {
        return renewDueDateFlag;
    }

    public void setRenewDueDateFlag(boolean renewDueDateFlag) {
        this.renewDueDateFlag = renewDueDateFlag;
    }

    public boolean isOverrideRenewal() {
        return overrideRenewal;
    }

    public void setOverrideRenewal(boolean overrideRenewal) {
        this.overrideRenewal = overrideRenewal;
    }

    public String getMaxTimeForCheckOutConstant() {
        return maxTimeForCheckOutConstant;
    }

    public void setMaxTimeForCheckOutConstant(String maxTimeForCheckOutConstant) {
        this.maxTimeForCheckOutConstant = maxTimeForCheckOutConstant;
    }

    public Integer getCheckInDateMaxTime() {
        return checkInDateMaxTime;
    }

    public void setCheckInDateMaxTime(Integer checkInDateMaxTime) {
        this.checkInDateMaxTime = checkInDateMaxTime;
    }

    public Integer getMaxTimeForCheckInDate() {
        return maxTimeForCheckInDate;
    }

    public void setMaxTimeForCheckInDate(Integer maxTimeForCheckInDate) {
        this.maxTimeForCheckInDate = maxTimeForCheckInDate;
    }

    private String previousCirculationDesk;


    public boolean isSelfCheckOut() {
        return selfCheckOut;
    }

    public void setSelfCheckOut(boolean selfCheckOut) {
        this.selfCheckOut = selfCheckOut;
    }

    public String getPreviousCirculationDesk() {
        return previousCirculationDesk;
    }

    public void setPreviousCirculationDesk(String previousCirculationDesk) {
        this.previousCirculationDesk = previousCirculationDesk;
    }

    public boolean isDueDateSlip() {
        return dueDateSlip;
    }

    public void setDueDateSlip(boolean dueDateSlip) {
        this.dueDateSlip = dueDateSlip;
    }

    private boolean checkInNoteExists = false;

    public String getOleFormKey() {
        return oleFormKey;
    }

    public void setOleFormKey(String oleFormKey) {
        this.oleFormKey = oleFormKey;
    }

    public int getMaxSessionTime() {
        return maxSessionTime;
    }

    public void setMaxSessionTime(int maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public boolean isAddressVerified() {
        return addressVerified;
    }

    public void setAddressVerified(boolean addressVerified) {
        this.addressVerified = addressVerified;
    }

    public String getCopyCheck() {
        return copyCheck;
    }

    public void setCopyCheck(String copyCheck) {
        this.copyCheck = copyCheck;
    }

    public boolean isCopyRequest() {
        return copyRequest;
    }

    public void setCopyRequest(boolean copyRequest) {
        this.copyRequest = copyRequest;
    }

    public boolean isClaimsReturned() {
        return claimsReturned;
    }

    public void setClaimsReturned(boolean claimsReturned) {
        this.claimsReturned = claimsReturned;
    }

    public String getRealPatronName() {
        return realPatronName;
    }

    public void setRealPatronName(String realPatronName) {
        this.realPatronName = realPatronName;
    }

    public boolean isBlockLoan() {
        return blockLoan;
    }

    public void setBlockLoan(boolean blockLoan) {
        this.blockLoan = blockLoan;
    }

    public String getBillAlertMessage() {
        return billAlertMessage;
    }

    public void setBillAlertMessage(String billAlertMessage) {
        this.billAlertMessage = billAlertMessage;
    }

    public String getDateAlertMessage() {
        return dateAlertMessage;
    }

    public void setDateAlertMessage(String dateAlertMessage) {
        this.dateAlertMessage = dateAlertMessage;
    }

    public boolean isBillAvailability() {
        return billAvailability;
    }

    public void setBillAvailability(boolean billAvailability) {
        this.billAvailability = billAvailability;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }


    public String getMatchCheck() {
        return matchCheck;
    }

    public boolean isChangeLocationFlag() {
        return changeLocationFlag;
    }

    public void setChangeLocationFlag(boolean changeLocationFlag) {
        this.changeLocationFlag = changeLocationFlag;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public String getCirculationDesk() {
        return circulationDesk;
    }

    public void setCirculationDesk(String circulationDesk) {
        this.circulationDesk = circulationDesk;
    }

    public void setMatchCheck(String matchCheck) {
        this.matchCheck = matchCheck;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(boolean numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public String getRealPatronType() {
        return realPatronType;
    }

    public void setRealPatronType(String realPatronType) {
        this.realPatronType = realPatronType;
    }

    public String getProxyPatronId() {
        return proxyPatronId;
    }

    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
    }

    public String getRealPatronBarcode() {
        return realPatronBarcode;
    }

    public void setRealPatronBarcode(String realPatronBarcode) {
        this.realPatronBarcode = realPatronBarcode;
    }

    public boolean isRealPatronFlag() {
        return realPatronFlag;
    }

    public void setRealPatronFlag(boolean realPatronFlag) {
        this.realPatronFlag = realPatronFlag;
    }

    public List<OlePatronDocument> getRealPatronList() {
        return realPatronList;
    }

    public void setRealPatronList(List<OlePatronDocument> realPatronList) {
        this.realPatronList = realPatronList;
    }

    public String getPatronNoteTypeId() {
        return patronNoteTypeId;
    }

    public void setPatronNoteTypeId(String patronNoteTypeId) {
        this.patronNoteTypeId = patronNoteTypeId;
    }

    public String getPatronUserNote() {
        return patronUserNote;
    }

    public void setPatronUserNote(String patronUserNote) {
        this.patronUserNote = patronUserNote;
    }

    public boolean isPatronNoteFlag() {
        return patronNoteFlag;
    }

    public void setPatronNoteFlag(boolean patronNoteFlag) {
        this.patronNoteFlag = patronNoteFlag;
    }

    public OleLoanFastAdd getOleLoanFastAdd() {
        return oleLoanFastAdd;
    }

    public void setOleLoanFastAdd(OleLoanFastAdd oleLoanFastAdd) {
        this.oleLoanFastAdd = oleLoanFastAdd;
    }


    //Fields for Check-in
    private boolean billPaymentOption = true;
    private boolean damagedCheckInOption;
    private Date checkInDate;
    private String checkInItem;
    private boolean returnCheck;
    private Date currentDate;


    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckInItem() {
        return checkInItem;
    }

    public void setCheckInItem(String checkInItem) {
        this.checkInItem = checkInItem;
    }

    public boolean isDamagedCheckInOption() {
        return damagedCheckInOption;
    }

    public void setDamagedCheckInOption(boolean damagedCheckInOption) {
        this.damagedCheckInOption = damagedCheckInOption;
    }

    public boolean isBillPaymentOption() {
        return billPaymentOption;
    }

    public void setBillPaymentOption(boolean billPaymentOption) {
        this.billPaymentOption = billPaymentOption;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOverrideLoginMessage() {
        return overrideLoginMessage;
    }

    public void setOverrideLoginMessage(String overrideLoginMessage) {
        this.overrideLoginMessage = overrideLoginMessage;
    }


    public String getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(String preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPrincipalId() {
        if (this.newPrincipalId != null && this.newPrincipalId.contains(",")) {
            return newPrincipalId.replaceAll(",", "");
        }
        return newPrincipalId;
    }

    public void setNewPrincipalId(String newPrincipalId) {
        this.newPrincipalId = newPrincipalId;
    }

    public String getOverideMethodCall() {
        return overideMethodCall;
    }

    public void setOverideMethodCall(String overideMethodCall) {
        this.overideMethodCall = overideMethodCall;
    }

    public boolean isOverrideFlag() {
        return overrideFlag;
    }

    public void setOverrideFlag(boolean overrideFlag) {
        this.overrideFlag = overrideFlag;
    }

    public String getOldPrincipalId() {
        return oldPrincipalId;
    }

    public void setOldPrincipalId(String oldPrincipalId) {
        this.oldPrincipalId = oldPrincipalId;
    }

    public String getClaimsReturnNote() {
        return claimsReturnNote;
    }

    public void setClaimsReturnNote(String claimsReturnNote) {
        this.claimsReturnNote = claimsReturnNote;
    }

    public boolean isClaimsReturnFlag() {
        return claimsReturnFlag;
    }

    public void setClaimsReturnFlag(boolean claimsReturnFlag) {
        this.claimsReturnFlag = claimsReturnFlag;
    }


    public boolean isAlterDueDateFlag() {
        return alterDueDateFlag;
    }

    public void setAlterDueDateFlag(boolean alterDueDateFlag) {
        this.alterDueDateFlag = alterDueDateFlag;
    }

    public List<OleLoanDocument> getAlterDueDateList() {
        return alterDueDateList;
    }

    public void setAlterDueDateList(List<OleLoanDocument> alterDueDateList) {
        this.alterDueDateList = alterDueDateList;
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public String getSuccessInfo() {
        return successInfo;
    }

    public void setSuccessInfo(String successInfo) {
        this.successInfo = successInfo;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getDueDateMap() {
        return dueDateMap;
    }

    public void setDueDateMap(Date dueDateMap) {
        this.dueDateMap = dueDateMap;
    }

    public Date getPopDateMap() {
        return popDateMap;
    }

    public void setPopDateMap(Date popDateMap) {
        this.popDateMap = popDateMap;
    }


    public boolean isDueDateEmpty() {
        return dueDateEmpty;
    }

    public void setDueDateEmpty(boolean dueDateEmpty) {
        this.dueDateEmpty = dueDateEmpty;
    }

    public OleLoanDocument getDummyLoan() {
        return dummyLoan;
    }

    public void setDummyLoan(OleLoanDocument dummyLoan) {
        this.dummyLoan = dummyLoan;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public Item getOleItem() {
        return oleItem;
    }

    public void setOleItem(Item oleItem) {
        this.oleItem = oleItem;
    }

    public String getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(String borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OleLoanForm() {
        loanList = new ArrayList<OleLoanDocument>();
    }


    public List<OleLoanDocument> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<OleLoanDocument> loanList) {
        this.loanList = loanList;
    }

    public void setOleLoanDocumentToLoanList(OleLoanDocument oleLoanDocument) {
        if (this.loanList == null)
            this.loanList = new ArrayList<OleLoanDocument>(0);
        this.loanList.add(oleLoanDocument);
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<OleLoanDocument> getExistingLoanList() {
        return existingLoanList;
    }

    public void setExistingLoanList(List<OleLoanDocument> existingLoanList) {
        this.existingLoanList = existingLoanList;
    }

    public boolean isReturnSuccess() {
        return returnSuccess;
    }

    public void setReturnSuccess(boolean returnSuccess) {
        this.returnSuccess = returnSuccess;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public boolean isReturnCheck() {
        return returnCheck;
    }

    public void setReturnCheck(boolean returnCheck) {
        this.returnCheck = returnCheck;
    }

    public String getReturnInformation() {
        return returnInformation;
    }

    public void setReturnInformation(String returnInformation) {
        this.returnInformation = returnInformation;
    }

    public List<OleLoanDocument> getItemReturnList() {
        return itemReturnList;
    }

    public void setItemReturnList(List<OleLoanDocument> itemReturnList) {
        this.itemReturnList = itemReturnList;
    }

    /**
     * Gets the checkInNoteExists attribute.
     *
     * @return Returns the checkInNoteExists
     */
    public boolean isCheckInNoteExists() {
        return checkInNoteExists;
    }

    /**
     * Sets the checkInNoteExists attribute value.
     *
     * @param checkInNoteExists The checkInNoteExists to set.
     */
    public void setCheckInNoteExists(boolean checkInNoteExists) {
        this.checkInNoteExists = checkInNoteExists;
    }

    public boolean isClearUI() {
        return isClearUI;
    }

    public void setClearUI(boolean clearUI) {
        isClearUI = clearUI;
    }

    public String getRouteToLocation() {
        return routeToLocation;
    }

    public void setRouteToLocation(String routeToLocation) {
        this.routeToLocation = routeToLocation;
    }

    /**
     * Gets the alterDueDateTimeInfo attribute.
     *
     * @return Returns the alterDueDateTimeInfo
     */
    public String getAlterDueDateTimeInfo() {
        return alterDueDateTimeInfo;
    }

    /**
     * Sets the alterDueDateTimeInfo attribute value.
     *
     * @param alterDueDateTimeInfo The alterDueDateTimeInfo to set.
     */
    public void setAlterDueDateTimeInfo(String alterDueDateTimeInfo) {
        this.alterDueDateTimeInfo = alterDueDateTimeInfo;
    }

    /**
     * Gets the loanLoginMessage attribute.
     *
     * @return Returns the loanLoginMessage
     */
    public boolean isLoanLoginMessage() {
        return loanLoginMessage;
    }

    /**
     * Sets the loanLoginMessage attribute value.
     *
     * @param loanLoginMessage The loanLoginMessage to set.
     */
    public void setLoanLoginMessage(boolean loanLoginMessage) {
        this.loanLoginMessage = loanLoginMessage;
    }

    /**
     * Gets the popDateTime attribute.
     *
     * @return Returns the popDateTime
     */
    public String getPopDateTime() {
        return popDateTime;
    }

    /**
     * Sets the popDateTime attribute value.
     *
     * @param popDateTime The popDateTime to set.
     */
    public void setPopDateTime(String popDateTime) {
        this.popDateTime = popDateTime;
    }

    /**
     * Gets the popDateTimeInfo attribute.
     *
     * @return Returns the popDateTimeInfo
     */
    public String getPopDateTimeInfo() {
        return popDateTimeInfo;
    }

    /**
     * Sets the popDateTimeInfo attribute value.
     *
     * @param popDateTimeInfo The popDateTimeInfo to set.
     */
    public void setPopDateTimeInfo(String popDateTimeInfo) {
        this.popDateTimeInfo = popDateTimeInfo;
    }

    /**
     * Gets the loanLoginUserInfo attribute.
     *
     * @return Returns the loanLoginUserInfo
     */
    public String getLoanLoginUserInfo() {
        return loanLoginUserInfo;
    }

    /**
     * Sets the loanLoginUserInfo attribute value.
     *
     * @param loanLoginUserInfo The loanLoginUserInfo to set.
     */
    public void setLoanLoginUserInfo(String loanLoginUserInfo) {
        this.loanLoginUserInfo = loanLoginUserInfo;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public boolean isPatronRequest() {
        return patronRequest;
    }

    public void setPatronRequest(boolean patronRequest) {
        this.patronRequest = patronRequest;
    }

    public String getOverrideErrorMessage() {
        return overrideErrorMessage;
    }

    public void setOverrideErrorMessage(String overrideErrorMessage) {
        this.overrideErrorMessage = overrideErrorMessage;
    }

    public HashMap<String, String> getErrorsAndPermission() {
        return errorsAndPermission;
    }

    public void setErrorsAndPermission(HashMap<String, String> errorsAndPermission) {
        this.errorsAndPermission = errorsAndPermission;
    }

    public boolean isRenewPermission() {
        return renewPermission;
    }

    public void setRenewPermission(boolean renewPermission) {
        this.renewPermission = renewPermission;
    }

    public boolean isBlockUser() {
        return blockUser;
    }

    public void setBlockUser(boolean blockUser) {
        this.blockUser = blockUser;
    }

    public boolean isItemDamagedStatus() {
        return itemDamagedStatus;
    }

    public void setItemDamagedStatus(boolean itemDamagedStatus) {
        this.itemDamagedStatus = itemDamagedStatus;
    }

    public boolean isSkipDamagedCheckIn() {
        return skipDamagedCheckIn;
    }

    public void setSkipDamagedCheckIn(boolean skipDamagedCheckIn) {
        this.skipDamagedCheckIn = skipDamagedCheckIn;
    }

    public String getMissingPieceCount() {
        return missingPieceCount;
    }

    public void setMissingPieceCount(String missingPieceCount) {
        this.missingPieceCount = missingPieceCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isMissingPieceValidationSuccess() {
        return missingPieceValidationSuccess;
    }

    public void setMissingPieceValidationSuccess(boolean missingPieceValidationSuccess) {
        this.missingPieceValidationSuccess = missingPieceValidationSuccess;
    }

    public boolean isInTransit() {
        return inTransit;
    }

    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    public boolean isProxyDisplay() {
        return proxyDisplay;
    }

    public void setProxyDisplay(boolean proxyDisplay) {
        this.proxyDisplay = proxyDisplay;
    }

    public String getMissingPieceMessage() {
        return missingPieceMessage;
    }

    public void setMissingPieceMessage(String missingPieceMessage) {
        this.missingPieceMessage = missingPieceMessage;
    }

    public boolean isMissingPieceDialog() {
        return missingPieceDialog;
    }

    public void setMissingPieceDialog(boolean missingPieceDialog) {
        this.missingPieceDialog = missingPieceDialog;
    }

    public boolean isDamagedItemDialog() {
        return damagedItemDialog;
    }

    public void setDamagedItemDialog(boolean damagedItemDialog) {
        this.damagedItemDialog = damagedItemDialog;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public boolean isDialogFlag() {
        return dialogFlag;
    }

    public void setDialogFlag(boolean dialogFlag) {
        this.dialogFlag = dialogFlag;
    }

    public String getDialogMissingPieceCount() {
        return dialogMissingPieceCount;
    }

    public void setDialogMissingPieceCount(String dialogMissingPieceCount) {
        this.dialogMissingPieceCount = dialogMissingPieceCount;
    }

    public String getDialogItemNoOfPieces() {
        return dialogItemNoOfPieces;
    }

    public void setDialogItemNoOfPieces(String dialogItemNoOfPieces) {
        this.dialogItemNoOfPieces = dialogItemNoOfPieces;
    }

    public OleLoanDocument getMissingPieceLoanDocument() {
        return missingPieceLoanDocument;
    }

    public void setMissingPieceLoanDocument(OleLoanDocument missingPieceLoanDocument) {
        this.missingPieceLoanDocument = missingPieceLoanDocument;
    }

    public boolean isDialogMissingPieceCountReadOnly() {
        return dialogMissingPieceCountReadOnly;
    }

    public void setDialogMissingPieceCountReadOnly(boolean dialogMissingPieceCountReadOnly) {
        this.dialogMissingPieceCountReadOnly = dialogMissingPieceCountReadOnly;
    }

    public boolean isDialogItemNoOfPiecesReadOnly() {
        return dialogItemNoOfPiecesReadOnly;
    }

    public void setDialogItemNoOfPiecesReadOnly(boolean dialogItemNoOfPiecesReadOnly) {
        this.dialogItemNoOfPiecesReadOnly = dialogItemNoOfPiecesReadOnly;
    }

    public String getDialogErrorMessage() {
        return dialogErrorMessage;
    }

    public void setDialogErrorMessage(String dialogErrorMessage) {
        this.dialogErrorMessage = dialogErrorMessage;
    }

    public boolean isRemoveMissingPieceButton() {
        return removeMissingPieceButton;
    }

    public void setRemoveMissingPieceButton(boolean removeMissingPieceButton) {
        this.removeMissingPieceButton = removeMissingPieceButton;
    }

    public String getRealPatronId() {
        return realPatronId;
    }

    public void setRealPatronId(String realPatronId) {
        this.realPatronId = realPatronId;
    }

    public boolean isRemoveMissingPieceFlag() {
        return removeMissingPieceFlag;
    }

    public void setRemoveMissingPieceFlag(boolean removeMissingPieceFlag) {
        this.removeMissingPieceFlag = removeMissingPieceFlag;
    }

    public boolean isRecordDamagedItemNote() {
        return recordDamagedItemNote;
    }

    public void setRecordDamagedItemNote(boolean recordDamagedItemNote) {
        this.recordDamagedItemNote = recordDamagedItemNote;
    }

    public boolean isRecordMissingPieceNote() {
        return recordMissingPieceNote;
    }

    public void setRecordMissingPieceNote(boolean recordMissingPieceNote) {
        this.recordMissingPieceNote = recordMissingPieceNote;
    }

    public boolean isDisplayRecordNotePopup() {
        return displayRecordNotePopup;
    }

    public void setDisplayRecordNotePopup(boolean displayRecordNotePopup) {
        this.displayRecordNotePopup = displayRecordNotePopup;
    }

    public boolean isCheckoutRecordFlag() {
        return checkoutRecordFlag;
    }

    public void setCheckoutRecordFlag(boolean checkoutRecordFlag) {
        this.checkoutRecordFlag = checkoutRecordFlag;
    }

    public String getCheckoutRecordMessage() {
        return checkoutRecordMessage;
    }

    public void setCheckoutRecordMessage(String checkoutRecordMessage) {
        this.checkoutRecordMessage = checkoutRecordMessage;
    }

    public boolean isSkipMissingPieceRecordPopup() {
        return skipMissingPieceRecordPopup;
    }

    public void setSkipMissingPieceRecordPopup(boolean skipMissingPieceRecordPopup) {
        this.skipMissingPieceRecordPopup = skipMissingPieceRecordPopup;
    }

    public boolean isSkipDamagedRecordPopup() {
        return skipDamagedRecordPopup;
    }

    public void setSkipDamagedRecordPopup(boolean skipDamagedRecordPopup) {
        this.skipDamagedRecordPopup = skipDamagedRecordPopup;
    }

    public boolean isRecordCheckoutMissingPieceNote() {
        return recordCheckoutMissingPieceNote;
    }

    public void setRecordCheckoutMissingPieceNote(boolean recordCheckoutMissingPieceNote) {
        this.recordCheckoutMissingPieceNote = recordCheckoutMissingPieceNote;
    }

    public boolean isDisplayMissingPieceNotePopup() {
        return displayMissingPieceNotePopup;
    }

    public void setDisplayMissingPieceNotePopup(boolean displayMissingPieceNotePopup) {
        this.displayMissingPieceNotePopup = displayMissingPieceNotePopup;
    }

    public boolean isCheckoutMissingPieceRecordFlag() {
        return checkoutMissingPieceRecordFlag;
    }

    public void setCheckoutMissingPieceRecordFlag(boolean checkoutMissingPieceRecordFlag) {
        this.checkoutMissingPieceRecordFlag = checkoutMissingPieceRecordFlag;
    }

    public boolean isDisplayDamagedRecordNotePopup() {
        return displayDamagedRecordNotePopup;
    }

    public void setDisplayDamagedRecordNotePopup(boolean displayDamagedRecordNotePopup) {
        this.displayDamagedRecordNotePopup = displayDamagedRecordNotePopup;
    }

    public boolean isCheckoutDamagedRecordFlag() {
        return checkoutDamagedRecordFlag;
    }

    public void setCheckoutDamagedRecordFlag(boolean checkoutDamagedRecordFlag) {
        this.checkoutDamagedRecordFlag = checkoutDamagedRecordFlag;
    }

    public boolean isTempClaimsFlag() {
        return tempClaimsFlag;
    }

    public void setTempClaimsFlag(boolean tempClaimsFlag) {
        this.tempClaimsFlag = tempClaimsFlag;
    }

    public boolean isSendMissingPieceMail() {
        return sendMissingPieceMail;
    }

    public void setSendMissingPieceMail(boolean sendMissingPieceMail) {
        this.sendMissingPieceMail = sendMissingPieceMail;
    }

    public boolean isHoldSlip() {
        return holdSlip;
    }

    public void setHoldSlip(boolean holdSlip) {
        this.holdSlip = holdSlip;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public boolean isRemoveItemDamagedButton() {
        return removeItemDamagedButton;
    }

    public void setRemoveItemDamagedButton(boolean removeItemDamagedButton) {
        this.removeItemDamagedButton = removeItemDamagedButton;
    }

    public boolean isShowExistingLoan() {
        return showExistingLoan;
    }

    public void setShowExistingLoan(boolean showExistingLoan) {
        this.showExistingLoan = showExistingLoan;
    }

    public OleLoanDocument getRenewalLoan() {
        return renewalLoan;
    }

    public void setRenewalLoan(OleLoanDocument renewalLoan) {
        this.renewalLoan = renewalLoan;
    }

    public String getNewPrincipalName() {
        if (this.newPrincipalName != null && this.newPrincipalName.contains(",")) {
            return this.newPrincipalName.replaceAll(",", "");
        }
        return newPrincipalName;
    }

    public void setNewPrincipalName(String newPrincipalName) {
        this.newPrincipalName = newPrincipalName;
    }
}

