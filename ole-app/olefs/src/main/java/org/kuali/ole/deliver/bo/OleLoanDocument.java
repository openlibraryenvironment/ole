package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.util.ItemFineRate;
import org.kuali.ole.deliver.util.LoanDateTimeUtil;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * The OleLoanDocument is a BO class that defines the loan document fields with getters and setters which
 * is used for interacting the loan data with the persistence layer in OLE.
 */
public class OleLoanDocument extends PersistableBusinessObjectBase implements Comparable<OleLoanDocument> {
    private String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
    private String loanId;
    private String loanStatusId;
    private String patronBarcode;
    private String patronId;
    private String itemId;
    private boolean blockLoan;
    private String requestFlag;
    private boolean vuFindFlag = false;
    private Date loanPeriodDate;
    private Integer loanPeriod;
    private String loanTermUnitId;
    private Timestamp loanDueDate;
    private String loanOperatorId;
    private String loanApproverId;
    private String borrowerTypeId;
    private String borrowerTypeName;
    private String itemInstitution;
    private String itemCampus;
    private String itemLibrary;
    private String itemCollection;
    private String itemLocation;
    private String itemType;
    private String itemTypeName;
    private String itemTypeDesc;
    private String itemLoanStatus;
    private String errorMessage;
    private String successMessage;
    private String patronUserNotes;
    private String patronNoteTypeId;
    private String itemUuid;
    private String bibUuid;
    private transient Item oleItem;
    private String instanceUuid;
    private String claimsReturnNote;
    private String location;
    private String locationCode;
    private boolean checkNo;
    private boolean isValidPatron;
    private String title;
    private String author;
    private OleLocation oleLocation;
    private EntityBo entity;
    private OleBorrowerType oleBorrowerType;
    private OlePatronDocument olePatron;
    private OleLoanStatus oleLoanStatus;
    private OleLoanTermUnit oleLoanTermUnit;
    private OleInstanceItemType oleInstanceItemType;
    private String patronName;
    private java.util.Date expirationDate;
    private boolean dueDateEmpty;
    private String itemCallNumber;
    private String itemCallNumberPrefix;
    private String itemCopyNumber;
    private List<OlePatronDocument> realPatron;
    private String realPatronBarcode;
    private String proxyPatronBarcode;
    private String proxyPatronBarcodeUrl;
    private String realPatronType;
    private String realPatronName;
    private String itemVolumeNumber;
    private boolean inDefinite = false;
    private String dueDateType = "Indefinite";
    private boolean nonCirculatingItem = false;
    private Timestamp manualRenewalDueDate;
    private Date renewalDateMap;
    private String renewalDateTime;
    private boolean renewNotFlag=false;
    /**
     * New Fields added
     */
    private boolean addressVerified;
    private boolean generalBlock;
    private String preferredAddress;
    private String email;
    private String phoneNumber;
    private String proxyPatronId;
    private boolean validateProxyPatron = false;
    private String circulationLocationId;
    private String machineId;
    private String checkInMachineId;
    private String numberOfRenewals;
    private String numberOfOverdueNoticesSent;
    private String oleRequestId;
    private String repaymentFeePatronBillId;
    private boolean claimsReturnedIndicator;
    private Date createDate;
    private Date pastDueDate;
    private Date dummyPastDueDate;
    private Date overDueNoticeDate;
    private String circulationPolicyId;
    private OleCirculationDesk oleCirculationDesk;
    private String noOfOverdueNoticesSentForBorrower;
    private boolean renewalItemFlag = false;
    private BigDecimal fineRate;
    private boolean isCheckOut = false;
    private boolean isBackGroundCheckOut = false;
    private boolean numberOfPieces = false;
    private boolean continueCheckIn = false;
    private String description;
    private String matchCheck;
    private String billName;
    private boolean damagedCheckInOption;
    private OlePatronDocument oleRequestPatron;
    private OleDeliverRequestBo oleDeliverRequestBo;
    private Integer itemNumberOfPieces;
    private boolean copyRequest = false;
    private boolean requestPatron = false;
    private BigDecimal itemPrice;
    private BigDecimal replacementBill;
    private boolean noticeForClaimsReturned = false;
    private String circulationPolicySetId;
    private String operatorsCirculationLocation;
    private String itemFullPathLocation;
    private String routeToLocation;
    private String routeToLocationName;
    private Date loanDueDateToAlter;
    private String itemStatusCode;
    private Timestamp claimsReturnedDate;
    private String loanDueDateTimeToAlter;
    private String locationId;

    private String borrowerTypeCode;
    private boolean blockCheckinItem;
    private String roleName;
    private String overrideErrorMessage;
    private HashMap<String,String> errorsAndPermission = new HashMap<>();
    private boolean renewPermission = true;
    private boolean renewPermissionForRequestedItem = false;
    private boolean  replacementFeeExist ;
    private boolean overdueFineExist;
    private boolean differentPatron;
    private boolean itemDamagedStatus;
    private String  itemDamagedNote;
    private boolean skipDamagedCheckIn;
    private String missingPiecesCount;
    private boolean missingPieceFlag;
    private String missingPieceNote;
    private String noOfMissingPiece;
    private boolean backgroundCheckInMissingPiece;
    private boolean lostPatron;
    private boolean noOfPiecesEditable;
    private boolean noOfMissingPiecesEditable;
    private String backUpNoOfPieces;
    private String enumeration;
    private String chronology;
    public String itemFullLocation;
    public String itemStatusEffectiveDate;
    private boolean loanModified;
    private String noticeType;
    private String noticeSendType;
    private OLEDeliverNotice oleDeliverNotice;
    private String oleLocationCode;
    private boolean indefiniteCheckFlag= false;
    private boolean invalidBarcodeFlag = false;
    private boolean fastAddItemIndicator = false;
    private String holdingsLocation;
    private boolean itemLevelLocationExist;
    private boolean damagedItemIndicator;
    private Timestamp damagedItemDate;
    private Timestamp missingPieceItemDate;
    private List<FeeType> feeType;
    private List<OleDeliverRequestBo> holdRequestForPatron = new ArrayList<>();
    private String sentNoticesUrl;
    private ItemFineRate itemFineRate = new ItemFineRate();
    private boolean overrideCheckInTime;
    private String  itemLostNote;
    private String  itemReplaceNote;
    private boolean isManualBill = false;
    private int noOfClaimsReturnedNoticesSent;
    private int claimsSearchCount;
    private Timestamp lastClaimsReturnedSearchedDate;
    private String feeTypeName;
    private String fineBillNumber;
    private Double fineAmount;
    private Timestamp fineItemDue;

    public Date getDummyPastDueDate() {
        return dummyPastDueDate;
    }

    public void setDummyPastDueDate(Date dummyPastDueDate) {
        this.dummyPastDueDate = dummyPastDueDate;
    }

    public boolean isIndefiniteCheckFlag() {
        return indefiniteCheckFlag;
    }

    public void setIndefiniteCheckFlag(boolean indefiniteCheckFlag) {
        this.indefiniteCheckFlag = indefiniteCheckFlag;
    }

    public boolean isRenewNotFlag() {
        return renewNotFlag;
    }

    public void setRenewNotFlag(boolean renewNotFlag) {
        this.renewNotFlag = renewNotFlag;
    }

    public String getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(String requestFlag) {
        this.requestFlag = requestFlag;
    }

    public Date getRenewalDateMap() {
        return renewalDateMap;
    }

    public void setRenewalDateMap(Date renewalDateMap) {
        this.renewalDateMap = renewalDateMap;
    }

    public String getRenewalDateTime() {
        return renewalDateTime;
    }

    public void setRenewalDateTime(String renewalDateTime) {
        this.renewalDateTime = renewalDateTime;
    }

    /*private boolean statusLost;

    public boolean isStatusLost() {
        return statusLost;
    }


    public void setStatusLost(boolean statusLost) {
        this.statusLost = statusLost;
    }*/

    public String getItemCallNumberPrefix() {
        return itemCallNumberPrefix;
    }

    public void setItemCallNumberPrefix(String itemCallNumberPrefix) {
        this.itemCallNumberPrefix = itemCallNumberPrefix;
    }

    public Timestamp getManualRenewalDueDate() {
        return manualRenewalDueDate;
    }

    public void setManualRenewalDueDate(Timestamp manualRenewalDueDate) {
        this.manualRenewalDueDate = manualRenewalDueDate;
    }

    public boolean isVuFindFlag() {
        return vuFindFlag;
    }

    public void setVuFindFlag(boolean vuFindFlag) {
        this.vuFindFlag = vuFindFlag;
    }

    public String getRouteToLocationName() {
        return routeToLocationName;
    }

    public void setRouteToLocationName(String routeToLocationName) {
        this.routeToLocationName = routeToLocationName;
    }

    //added for OLE-5119
    private boolean courtesyNoticeFlag;

    public boolean isCourtesyNoticeFlag() {
        return courtesyNoticeFlag;
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

    public void setCourtesyNoticeFlag(boolean courtesyNoticeFlag) {
        this.courtesyNoticeFlag = courtesyNoticeFlag;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isBlockCheckinItem() {
        return blockCheckinItem;
    }

    public void setBlockCheckinItem(boolean blockCheckinItem) {
        this.blockCheckinItem = blockCheckinItem;
    }

    public String getBorrowerTypeCode() {
        return borrowerTypeCode;
    }

    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isNonCirculatingItem() {
        return nonCirculatingItem;
    }

    public void setNonCirculatingItem(boolean nonCirculatingItem) {
        this.nonCirculatingItem = nonCirculatingItem;
    }

    public String getItemStatusCode() {
        return itemStatusCode;
    }

    public void setItemStatusCode(String itemStatusCode) {
        this.itemStatusCode = itemStatusCode;
    }

    public Date getLoanDueDateToAlter() {
        return loanDueDateToAlter;
    }

    public void setLoanDueDateToAlter(Date loanDueDateToAlter) {
        this.loanDueDateToAlter = loanDueDateToAlter;
    }

    public String getRouteToLocation() {
        return routeToLocation;
    }

    public void setRouteToLocation(String routeToLocation) {
        this.routeToLocation = routeToLocation;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getItemFullPathLocation() {
        return itemFullPathLocation;
    }

    public void setItemFullPathLocation(String itemFullPathLocation) {
        this.itemFullPathLocation = itemFullPathLocation;
    }

    public String getBibUuid() {
        return bibUuid;
    }

    public void setBibUuid(String bibUuid) {
        this.bibUuid = bibUuid;
    }

    public String getOperatorsCirculationLocation() {
        return operatorsCirculationLocation;
    }

    public void setOperatorsCirculationLocation(String operatorsCirculationLocation) {
        this.operatorsCirculationLocation = operatorsCirculationLocation;
    }

    public String getCirculationPolicySetId() {
        return circulationPolicySetId;
    }

    public void setCirculationPolicySetId(String circulationPolicySetId) {
        this.circulationPolicySetId = circulationPolicySetId;
    }

    public boolean isNoticeForClaimsReturned() {
        return noticeForClaimsReturned;
    }

    public void setNoticeForClaimsReturned(boolean noticeForClaimsReturned) {
        this.noticeForClaimsReturned = noticeForClaimsReturned;
    }

    public boolean isInDefinite() {
        return inDefinite;
    }

    public void setInDefinite(boolean inDefinite) {
        this.inDefinite = inDefinite;
    }

    //renewal
    private Timestamp renewalLoanDueDate;
    private boolean renewCheckNo;

    public String getDueDateType() {
        return dueDateType;
    }

    public void setDueDateType(String dueDateType) {
        this.dueDateType = dueDateType;
    }

    public boolean isRenewCheckNo() {
        return renewCheckNo;
    }

    public void setRenewCheckNo(boolean renewCheckNo) {
        this.renewCheckNo = renewCheckNo;
    }

    public Timestamp getRenewalLoanDueDate() {
        return renewalLoanDueDate;
    }

    public void setRenewalLoanDueDate(Timestamp renewalLoanDueDate) {
        this.renewalLoanDueDate = renewalLoanDueDate;
    }

    public boolean isAddressVerified() {
        return addressVerified;
    }

    public void setAddressVerified(boolean addressVerified) {
        this.addressVerified = addressVerified;
    }

    public boolean isGeneralBlock() {
        return generalBlock;
    }

    public void setGeneralBlock(boolean generalBlock) {
        this.generalBlock = generalBlock;
    }

    public boolean isRequestPatron() {
        return requestPatron;
    }

    public void setRequestPatron(boolean requestPatron) {
        this.requestPatron = requestPatron;
    }

    public BigDecimal getReplacementBill() {
        return replacementBill;
    }

    public void setReplacementBill(BigDecimal replacementBill) {
        this.replacementBill = replacementBill;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemCopyNumber() {
        return itemCopyNumber;
    }

    public void setItemCopyNumber(String itemCopyNumber) {
        this.itemCopyNumber = itemCopyNumber;
    }

    public boolean isCopyRequest() {
        return copyRequest;
    }

    public void setCopyRequest(boolean copyRequest) {
        this.copyRequest = copyRequest;
    }

    public boolean isValidateProxyPatron() {
        return validateProxyPatron;
    }

    public void setValidateProxyPatron(boolean validateProxyPatron) {
        this.validateProxyPatron = validateProxyPatron;
    }

    public Integer getItemNumberOfPieces() {
        return itemNumberOfPieces;
    }

    public void setItemNumberOfPieces(Integer itemNumberOfPieces) {
        this.itemNumberOfPieces = itemNumberOfPieces;
    }

    public OleDeliverRequestBo getOleDeliverRequestBo() {
        return oleDeliverRequestBo;
    }

    public void setOleDeliverRequestBo(OleDeliverRequestBo oleDeliverRequestBo) {
        this.oleDeliverRequestBo = oleDeliverRequestBo;
    }

    public OlePatronDocument getOleRequestPatron() {
        return oleRequestPatron;
    }

    public void setOleRequestPatron(OlePatronDocument oleRequestPatron) {
        this.oleRequestPatron = oleRequestPatron;
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

    public boolean isDamagedCheckInOption() {
        return damagedCheckInOption;
    }

    public void setDamagedCheckInOption(boolean damagedCheckInOption) {
        this.damagedCheckInOption = damagedCheckInOption;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public boolean isContinueCheckIn() {
        return continueCheckIn;
    }

    public void setContinueCheckIn(boolean continueCheckIn) {
        this.continueCheckIn = continueCheckIn;
    }

    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatchCheck() {
        return matchCheck;
    }

    public void setMatchCheck(String matchCheck) {
        this.matchCheck = matchCheck;
    }

    public boolean isNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(boolean numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public BigDecimal getFineRate() {
        return fineRate;
    }

    public void setFineRate(BigDecimal fineRate) {
        this.fineRate = fineRate;
    }

    public boolean isCheckOut() {
        return isCheckOut;
    }

    public void setCheckOut(boolean checkOut) {
        isCheckOut = checkOut;
    }

    public String getRealPatronType() {
        return realPatronType;
    }

    public void setRealPatronType(String realPatronType) {
        this.realPatronType = realPatronType;
    }

    public String getRealPatronBarcode() {
        return realPatronBarcode;
    }

    public void setRealPatronBarcode(String realPatronBarcode) {
        this.realPatronBarcode = realPatronBarcode;
    }

    public List<OlePatronDocument> getRealPatron() {
        return realPatron;
    }

    public void setRealPatron(List<OlePatronDocument> realPatron) {
        this.realPatron = realPatron;
    }

    public String getPatronNoteTypeId() {
        return patronNoteTypeId;
    }

    public void setPatronNoteTypeId(String patronNoteTypeId) {
        this.patronNoteTypeId = patronNoteTypeId;
    }

    public String getPatronUserNotes() {
        return patronUserNotes;
    }

    public void setPatronUserNotes(String patronUserNotes) {
        this.patronUserNotes = patronUserNotes;
    }

    //For Check-in
    private String itemStatus;
    private Timestamp checkInDate;

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Timestamp checkInDate) {
        this.checkInDate = checkInDate;
    }

    public boolean isRenewalItemFlag() {
        return renewalItemFlag;
    }

    public void setRenewalItemFlag(boolean renewalItemFlag) {
        this.renewalItemFlag = renewalItemFlag;
    }


    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getClaimsReturnNote() {
        return claimsReturnNote;
    }

    public void setClaimsReturnNote(String claimsReturnNote) {
        this.claimsReturnNote = claimsReturnNote;
    }

    public String getProxyPatronId() {
        return proxyPatronId;
    }

    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
    }

    public String getCirculationLocationId() {
        return circulationLocationId;
    }

    public void setCirculationLocationId(String circulationLocationId) {
        this.circulationLocationId = circulationLocationId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getNumberOfRenewals() {
        if (numberOfRenewals == null || "".equals(numberOfRenewals))
            numberOfRenewals = "0";
        return numberOfRenewals;
    }

    public void setNumberOfRenewals(String numberOfRenewals) {
        this.numberOfRenewals = numberOfRenewals;
    }

    public String getNumberOfOverdueNoticesSent() {
        return numberOfOverdueNoticesSent;
    }

    public void setNumberOfOverdueNoticesSent(String numberOfOverdueNoticesSent) {
        this.numberOfOverdueNoticesSent = numberOfOverdueNoticesSent;
    }

    public String getOleRequestId() {
        return oleRequestId;
    }

    public void setOleRequestId(String oleRequestId) {
        this.oleRequestId = oleRequestId;
    }

    public String getRepaymentFeePatronBillId() {
        return repaymentFeePatronBillId;
    }

    public void setRepaymentFeePatronBillId(String repaymentFeePatronBillId) {
        this.repaymentFeePatronBillId = repaymentFeePatronBillId;
    }

    public boolean isClaimsReturnedIndicator() {
        return claimsReturnedIndicator;
    }

    public void setClaimsReturnedIndicator(boolean claimsReturnedIndicator) {
        this.claimsReturnedIndicator = claimsReturnedIndicator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPastDueDate() {
        return pastDueDate;
    }

    public void setPastDueDate(Date pastDueDate) {
        this.pastDueDate = pastDueDate;
    }

    public Date getOverDueNoticeDate() {
        return overDueNoticeDate;
    }

    public void setOverDueNoticeDate(Date overDueNoticeDate) {
        this.overDueNoticeDate = overDueNoticeDate;
    }

    public String getCirculationPolicyId() {
        return circulationPolicyId;
    }

    public void setCirculationPolicyId(String circulationPolicyId) {
        this.circulationPolicyId = circulationPolicyId;
    }

    public String getNoOfOverdueNoticesSentForBorrower() {
        return noOfOverdueNoticesSentForBorrower;
    }

    public void setNoOfOverdueNoticesSentForBorrower(String noOfOverdueNoticesSentForBorrower) {
        this.noOfOverdueNoticesSentForBorrower = noOfOverdueNoticesSentForBorrower;
    }

    public boolean isCheckNo() {
        return checkNo;
    }

    public void setCheckNo(boolean checkNo) {
        this.checkNo = checkNo;
    }

    private List<OLEDeliverNotice> deliverNotices = new ArrayList<>();

    /**
     * Gets the ItemCallNumber attribute.
     *
     * @return returns the String.
     */
    public String getItemCallNumber() {
        return itemCallNumber;
    }

    /**
     * Sets the itemCallNumber attribute value.
     *
     * @param itemCallNumber
     */
    public void setItemCallNumber(String itemCallNumber) {
        this.itemCallNumber = itemCallNumber;
    }

    /**
     * Gets the instanceUuid attribute.
     *
     * @return Returns the instanceUuid
     */
    public String getInstanceUuid() {
        return instanceUuid;
    }

    /**
     * Sets the instanceUuid attribute value.
     *
     * @param instanceUuid The instanceUuid to set.
     */
    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    /**
     * Gets the oleInstanceItemType attribute.
     *
     * @return Returns the oleInstanceItemType
     */
    public OleInstanceItemType getOleInstanceItemType() {
        return oleInstanceItemType;
    }

    /**
     * Sets the oleInstanceItemType attribute value.
     *
     * @param oleInstanceItemType The oleInstanceItemType to set.
     */
    public void setOleInstanceItemType(OleInstanceItemType oleInstanceItemType) {
        this.oleInstanceItemType = oleInstanceItemType;
    }

    /**
     * Gets the dueDateEmpty attribute.
     *
     * @return Returns the dueDateEmpty
     */
    public boolean isDueDateEmpty() {
        return dueDateEmpty;
    }

    /**
     * Sets the dueDateEmpty attribute value.
     *
     * @param dueDateEmpty The dueDateEmpty to set.
     */
    public void setDueDateEmpty(boolean dueDateEmpty) {
        this.dueDateEmpty = dueDateEmpty;
    }

    /**
     * Gets the expirationDate attribute.
     *
     * @return Returns the expirationDate
     */
    public java.util.Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expirationDate attribute value.
     *
     * @param expirationDate The expirationDate to set.
     */
    public void setExpirationDate(java.util.Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the itemTypeName attribute.
     *
     * @return Returns the itemTypeName
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * Sets the itemTypeName attribute value.
     *
     * @param itemTypeName The itemTypeName to set.
     */
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    /**
     * Gets the itemTypeDesc attribute.
     *
     * @return Returns the itemTypeDesc
     */
    public String getItemTypeDesc() {
        return itemTypeDesc;
    }

    /**
     * Sets the itemTypeDesc attribute value.
     *
     * @param itemTypeDesc The itemTypeName to set.
     */
    public void setItemTypeDesc(String itemTypeDesc) {
        this.itemTypeDesc = itemTypeDesc;
    }

    /**
     * Gets the patronBarcode attribute.
     *
     * @return Returns the patronBarcode
     */
    public String getPatronBarcode() {
        return patronBarcode;
    }

    /**
     * Sets the patronBarcode attribute value.
     *
     * @param patronBarcode The patronBarcode to set.
     */
    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    /**
     * Gets the itemUuid attribute.
     *
     * @return Returns the itemUuid
     */
    public String getItemUuid() {
        return itemUuid;
    }

    /**
     * Sets the itemUuid attribute value.
     *
     * @param itemUuid The itemUuid to set.
     */
    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    /**
     * Gets the patronName attribute.
     *
     * @return Returns the patronName
     */
    public String getPatronName() {
        return patronName;
    }

    /**
     * Sets the patronName attribute value.
     *
     * @param patronName The patronName to set.
     */
    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    /**
     * Gets the isValidPatron attribute.
     *
     * @return Returns the isValidPatron
     */
    public boolean isValidPatron() {
        return isValidPatron;
    }

    /**
     * Sets the isValidPatron attribute value.
     *
     * @param validPatron The isValidPatron to set.
     */
    public void setValidPatron(boolean validPatron) {
        isValidPatron = validPatron;
    }

    /**
     * Gets the borrowerTypeName attribute.
     *
     * @return Returns the borrowerTypeName
     */
    public String getBorrowerTypeName() {
        return borrowerTypeName;
    }

    /**
     * Sets the borrowerTypeName attribute value.
     *
     * @param borrowerTypeName The borrowerTypeName to set.
     */
    public void setBorrowerTypeName(String borrowerTypeName) {
        this.borrowerTypeName = borrowerTypeName;
    }


    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String getFineBillNumber() {
        return fineBillNumber;
    }

    public void setFineBillNumber(String fineBillNumber) {
        this.fineBillNumber = fineBillNumber;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Timestamp getFineItemDue() {
        return fineItemDue;
    }

    public void setFineItemDue(Timestamp fineItemDue) {
        this.fineItemDue = fineItemDue;
    }

    /**
     * Gets the errorMessage attribute.
     *
     * @return Returns the errorMessage
     */
    public String getErrorMessage() {
        if (null == errorMessage) {
            return "";
        }
        return errorMessage;
    }

    /**
     * Sets the errorMessage attribute value.
     *
     * @param errorMessage The errorMessage to set.
     */
    public void setErrorMessage(String errorMessage) {
        if (null != errorMessage) {
            StringBuilder stringBuilder = new StringBuilder();
            if (null != this.errorMessage && !org.apache.commons.lang.StringUtils.isBlank(this.errorMessage)) {
                stringBuilder.append(this
                        .errorMessage).append
                        (OLEConstants.BREAK);
            }
            stringBuilder.append(errorMessage);
            this.errorMessage = stringBuilder.toString();
        }else{
            this.errorMessage = errorMessage;
        }
    }

    /**
     * Gets the oleLocation attribute.
     *
     * @return Returns the oleLocation
     */
    public OleLocation getOleLocation() {
        return oleLocation;
    }

    /**
     * Sets the oleLocation attribute value.
     *
     * @param oleLocation The oleLocation to set.
     */
    public void setOleLocation(OleLocation oleLocation) {
        this.oleLocation = oleLocation;
    }

    /**
     * Gets the title attribute.
     *
     * @return Returns the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title attribute value.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the entity attribute.
     *
     * @return Returns the entity
     */
    public EntityBo getEntity() {
        if(getPatronId()!=null){
            EntityBo entityBo = (EntityBo) KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(EntityBo.class, getPatronId());
            return entityBo;
        }else{
            return entity;
        }
    }

    /**
     * Sets the entity attribute value.
     *
     * @param entity The entity to set.
     */
    public void setEntity(EntityBo entity) {
        this.entity = entity;
    }

    /**
     * Gets the oleBorrowerType attribute.
     *
     * @return Returns the oleBorrowerType
     */
    public OleBorrowerType getOleBorrowerType() {
        return oleBorrowerType;
    }

    /**
     * Sets the oleBorrowerType attribute value.
     *
     * @param oleBorrowerType The oleBorrowerType to set.
     */
    public void setOleBorrowerType(OleBorrowerType oleBorrowerType) {
        this.oleBorrowerType = oleBorrowerType;
    }

    /**
     * Gets the olePatron attribute.
     *
     * @return Returns the olePatron
     */
    public OlePatronDocument getOlePatron() {
        if (null == olePatron) {
            String patronId = getPatronId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                olePatron = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
            }
        }
        return olePatron;
    }

    /**
     * Sets the olePatron attribute value.
     *
     * @param olePatron The olePatron to set.
     */
    public void setOlePatron(OlePatronDocument olePatron) {
        this.olePatron = olePatron;
    }

    /**
     * Gets the oleLoanStatus attribute.
     *
     * @return Returns the oleLoanStatus
     */
    public OleLoanStatus getOleLoanStatus() {
        return oleLoanStatus;
    }

    /**
     * Sets the oleLoanStatus attribute value.
     *
     * @param oleLoanStatus The oleLoanStatus to set.
     */
    public void setOleLoanStatus(OleLoanStatus oleLoanStatus) {
        this.oleLoanStatus = oleLoanStatus;
    }

    /**
     * Gets the oleLoanTermUnit attribute.
     *
     * @return Returns the oleLoanTermUnit
     */
    public OleLoanTermUnit getOleLoanTermUnit() {
        return oleLoanTermUnit;
    }

    /**
     * Sets the oleLoanTermUnit attribute value.
     *
     * @param oleLoanTermUnit The oleLoanTermUnit to set.
     */
    public void setOleLoanTermUnit(OleLoanTermUnit oleLoanTermUnit) {
        this.oleLoanTermUnit = oleLoanTermUnit;
    }

    /**
     * Gets the loanId attribute.
     *
     * @return Returns the loanId
     */
    public String getLoanId() {
        return loanId;
    }

    /**
     * Sets the loanId attribute value.
     *
     * @param loanId The loanId to set.
     */
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    /**
     * Gets the loanStatusId attribute.
     *
     * @return Returns the loanStatusId
     */
    public String getLoanStatusId() {
        return loanStatusId;
    }

    /**
     * Sets the loanStatusId attribute value.
     *
     * @param loanStatusId The loanStatusId to set.
     */
    public void setLoanStatusId(String loanStatusId) {
        this.loanStatusId = loanStatusId;
    }

    /**
     * Gets the patronId attribute.
     *
     * @return Returns the patronId
     */
    public String getPatronId() {
        return patronId;
    }

    /**
     * Sets the patronId attribute value.
     *
     * @param patronId The patronId to set.
     */
    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    /**
     * Gets the itemId attribute.
     *
     * @return Returns the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the itemId attribute value.
     *
     * @param itemId The itemId to set.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the loanPeriodDate attribute.
     *
     * @return Returns the loanPeriodDate
     */
    public Date getLoanPeriodDate() {
        return loanPeriodDate;
    }

    /**
     * Sets the loanPeriodDate attribute value.
     *
     * @param loanPeriodDate The loanPeriodDate to set.
     */
    public void setLoanPeriodDate(Date loanPeriodDate) {
        this.loanPeriodDate = loanPeriodDate;
    }

    /**
     * Gets the loanPeriod attribute.
     *
     * @return Returns the loanPeriod
     */
    public Integer getLoanPeriod() {
        return loanPeriod;
    }

    /**
     * Sets the loanPeriod attribute value.
     *
     * @param loanPeriod The loanPeriod to set.
     */
    public void setLoanPeriod(Integer loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    /**
     * Gets the loanTermUnitId attribute.
     *
     * @return Returns the loanTermUnitId
     */
    public String getLoanTermUnitId() {
        return loanTermUnitId;
    }

    /**
     * Sets the loanTermUnitId attribute value.
     *
     * @param loanTermUnitId The loanTermUnitId to set.
     */
    public void setLoanTermUnitId(String loanTermUnitId) {
        this.loanTermUnitId = loanTermUnitId;
    }

    /**
     * Gets the loanDueDate attribute.
     *
     * @return Returns the loanDueDate
     */
    public Timestamp getLoanDueDate() {
        return loanDueDate;
    }

    /**
     * Sets the loanDueDate attribute value.
     *
     * @param loanDueDate The loanDueDate to set.
     */
    public void setLoanDueDate(Timestamp loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    /**
     * Gets the loanOperatorId attribute.
     *
     * @return Returns the loanOperatorId
     */
    public String getLoanOperatorId() {
        return loanOperatorId;
    }

    /**
     * Sets the loanOperatorId attribute value.
     *
     * @param loanOperatorId The loanOperatorId to set.
     */
    public void setLoanOperatorId(String loanOperatorId) {
        this.loanOperatorId = loanOperatorId;
    }

    /**
     * Gets the loanApproverId attribute.
     *
     * @return Returns the loanApproverId
     */
    public String getLoanApproverId() {
        return loanApproverId;
    }

    /**
     * Sets the loanApproverId attribute value.
     *
     * @param loanApproverId The loanApproverId to set.
     */
    public void setLoanApproverId(String loanApproverId) {
        this.loanApproverId = loanApproverId;
    }

    /**
     * Gets the borrowerTypeId attribute.
     *
     * @return Returns the borrowerTypeId
     */
    public String getBorrowerTypeId() {
        return borrowerTypeId;
    }

    /**
     * Sets the borrowerTypeId attribute value.
     *
     * @param borrowerTypeId The borrowerTypeId to set.
     */
    public void setBorrowerTypeId(String borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    /**
     * Gets the itemInstitution attribute.
     *
     * @return Returns the itemInstitution
     */
    public String getItemInstitution() {
        return itemInstitution;
    }

    /**
     * Sets the itemInstitution attribute value.
     *
     * @param itemInstitution The itemInstitution to set.
     */
    public void setItemInstitution(String itemInstitution) {
        this.itemInstitution = itemInstitution;
    }

    /**
     * Gets the itemCampus attribute.
     *
     * @return Returns the itemCampus
     */
    public String getItemCampus() {
        return itemCampus;
    }

    /**
     * Sets the itemCampus attribute value.
     *
     * @param itemCampus The itemCampus to set.
     */
    public void setItemCampus(String itemCampus) {
        this.itemCampus = itemCampus;
    }

    /**
     * Gets the itemLibrary attribute.
     *
     * @return Returns the itemLibrary
     */
    public String getItemLibrary() {
        return itemLibrary;
    }

    /**
     * Sets the itemLibrary attribute value.
     *
     * @param itemLibrary The itemLibrary to set.
     */
    public void setItemLibrary(String itemLibrary) {
        this.itemLibrary = itemLibrary;
    }

    /**
     * Gets the itemCollection attribute.
     *
     * @return Returns the itemCollection
     */
    public String getItemCollection() {
        return itemCollection;
    }

    /**
     * Sets the itemCollection attribute value.
     *
     * @param itemCollection The itemCollection to set.
     */
    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }

    /**
     * Gets the itemLocation attribute.
     *
     * @return Returns the itemLocation
     */
    public String getItemLocation() {
        return itemLocation;
    }

    /**
     * Sets the itemLocation attribute value.
     *
     * @param itemLocation The itemLocation to set.
     */
    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    /**
     * Gets the itemType attribute.
     *
     * @return Returns the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Sets the itemType attribute value.
     *
     * @param itemType The itemType to set.
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }


    /**
     * Gets the itemLoanStatus attribute.
     *
     * @return Returns the itemLoanStatus
     */
    public String getItemLoanStatus() {
        return itemLoanStatus;
    }

    /**
     * Sets the itemLoanStatus attribute value.
     *
     * @param itemLoanStatus The itemLoanStatus to set.
     */
    public void setItemLoanStatus(String itemLoanStatus) {
        this.itemLoanStatus = itemLoanStatus;
    }

    /**
     * Gets the oleItem attribute.
     *
     * @return Returns the oleItem
     */
    public Item getOleItem() {
        return oleItem;
    }

    /**
     * Sets the oleItem attribute value.
     *
     * @param oleItem The oleItem to set.
     */
    public void setOleItem(Item oleItem) {
        this.oleItem = oleItem;
    }

    /**
     * Gets the itemVolumeNumber attribute.
     *
     * @return Returns the itemVolumeNumber
     */
    public String getItemVolumeNumber() {
        return itemVolumeNumber;
    }

    /**
     * Sets the itemVolumeNumber attribute value.
     *
     * @param itemVolumeNumber The oleItem to set.
     */
    public void setItemVolumeNumber(String itemVolumeNumber) {
        this.itemVolumeNumber = itemVolumeNumber;
    }

    /**
     * Gets the claims return date attribute value.
     *
     * @return Returns the claims return date
     */
    public Timestamp getClaimsReturnedDate() {
        return claimsReturnedDate;
    }

    /**
     * Sets the claims return date attribute value.
     *
     * @param claimsReturnedDate
     */
    public void setClaimsReturnedDate(Timestamp claimsReturnedDate) {
        this.claimsReturnedDate = claimsReturnedDate;
    }

    /**
     * Gets the loanDueDateTimeToAlter attribute value.
     *
     * @return Returns the loanDueDateTimeToAlter
     */
    public String getLoanDueDateTimeToAlter() {
        return loanDueDateTimeToAlter;
    }

    /**
     * Sets the loanDueDateTimeToAlter attribute value.
     *
     * @param loanDueDateTimeToAlter
     */
    public void setLoanDueDateTimeToAlter(String loanDueDateTimeToAlter) {
        this.loanDueDateTimeToAlter = loanDueDateTimeToAlter;
    }

    public boolean isBackGroundCheckOut() {
        return isBackGroundCheckOut;
    }

    public void setBackGroundCheckOut(boolean backGroundCheckOut) {
        isBackGroundCheckOut = backGroundCheckOut;
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

    public boolean isReplacementFeeExist() {
        return replacementFeeExist;
    }

    public void setReplacementFeeExist(boolean replacementFeeExist) {
        this.replacementFeeExist = replacementFeeExist;
    }

    public boolean isOverdueFineExist() {
        return overdueFineExist;
    }

    public void setOverdueFineExist(boolean overdueFineExist) {
        this.overdueFineExist = overdueFineExist;
    }

    public boolean isDifferentPatron() {
        return differentPatron;
    }

    public void setDifferentPatron(boolean differentPatron) {
        this.differentPatron = differentPatron;
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

    public String getMissingPiecesCount() {
        return missingPiecesCount;
    }

    public void setMissingPiecesCount(String missingPiecesCount) {
        this.missingPiecesCount = missingPiecesCount;
    }

    public boolean isMissingPieceFlag() {
        return missingPieceFlag;
    }

    public void setMissingPieceFlag(boolean missingPieceFlag) {
        this.missingPieceFlag = missingPieceFlag;
    }

    public boolean isBackgroundCheckInMissingPiece() {
        return backgroundCheckInMissingPiece;
    }

    public void setBackgroundCheckInMissingPiece(boolean backgroundCheckInMissingPiece) {
        this.backgroundCheckInMissingPiece = backgroundCheckInMissingPiece;
    }

    public boolean isLostPatron() {
        return lostPatron;
    }

    public void setLostPatron(boolean lostPatron) {
        this.lostPatron = lostPatron;
    }

    public String getItemDamagedNote() {
        return itemDamagedNote;
    }

    public void setItemDamagedNote(String itemDamagedNote) {
        this.itemDamagedNote = itemDamagedNote;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }

    public boolean isNoOfPiecesEditable() {
        return noOfPiecesEditable;
    }

    public void setNoOfPiecesEditable(boolean noOfPiecesEditable) {
        this.noOfPiecesEditable = noOfPiecesEditable;
    }

    public boolean isNoOfMissingPiecesEditable() {
        return noOfMissingPiecesEditable;
    }

    public void setNoOfMissingPiecesEditable(boolean noOfMissingPiecesEditable) {
        this.noOfMissingPiecesEditable = noOfMissingPiecesEditable;
    }

    public String getBackUpNoOfPieces() {
        return backUpNoOfPieces;
    }

    public void setBackUpNoOfPieces(String backUpNoOfPieces) {
        this.backUpNoOfPieces = backUpNoOfPieces;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public boolean isRenewPermissionForRequestedItem() {
        return renewPermissionForRequestedItem;
    }

    public void setRenewPermissionForRequestedItem(boolean renewPermissionForRequestedItem) {
        this.renewPermissionForRequestedItem = renewPermissionForRequestedItem;
    }

    public boolean isLoanModified() {
        return loanModified;
    }

    public void setLoanModified(boolean loanModified) {
        this.loanModified = loanModified;
    }

    public String getItemFullLocation() {
        return itemFullLocation;
    }

    public void setItemFullLocation(String itemFullLocation) {
        this.itemFullLocation = itemFullLocation;
    }

    @Override
    public int compareTo(OleLoanDocument oleLoanDocument) {
        if(this.itemId!=null && itemId.equals(oleLoanDocument.getItemId())){
            return 0;
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
           return super.equals(obj);
        } else {
             if(obj  instanceof OleLoanDocument){
                 OleLoanDocument oleLoanDocument=(OleLoanDocument)obj;
                 if(this.itemId!=null && itemId.equals(oleLoanDocument.getItemId())){
                     return true;
                 } else {
                     return super.equals(obj);
                 }
             } else {
                 return super.equals(obj);
             }

        }
    }

    public String getItemStatusEffectiveDate() {
        return itemStatusEffectiveDate;
    }

    public void setItemStatusEffectiveDate(String itemStatusEffectiveDate) {
        this.itemStatusEffectiveDate = itemStatusEffectiveDate;
    }

    public List<OLEDeliverNotice> getDeliverNotices() {
        return deliverNotices;
    }

    public void setDeliverNotices(List<OLEDeliverNotice> deliverNotices) {
        this.deliverNotices = deliverNotices;
    }



    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }


    public String getNoticeSendType() {
        return noticeSendType;
    }

    public void setNoticeSendType(String noticeSendType) {
        this.noticeSendType = noticeSendType;
    }

    public OLEDeliverNotice getOleDeliverNotice() {
        return oleDeliverNotice;
    }

    public void setOleDeliverNotice(OLEDeliverNotice oleDeliverNotice) {
        this.oleDeliverNotice = oleDeliverNotice;
    }

    public String getOleLocationCode() {
        return oleLocationCode;
    }

    public void setOleLocationCode(String oleLocationCode) {
        this.oleLocationCode = oleLocationCode;
    }

    public boolean isInvalidBarcodeFlag() {
        return invalidBarcodeFlag;
    }

    public void setInvalidBarcodeFlag(boolean invalidBarcodeFlag) {
        this.invalidBarcodeFlag = invalidBarcodeFlag;
    }

    public boolean isFastAddItemIndicator() {
        return fastAddItemIndicator;
    }

    public void setFastAddItemIndicator(boolean fastAddItemIndicator) {
        this.fastAddItemIndicator = fastAddItemIndicator;
    }

    public String getHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(String holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public boolean isItemLevelLocationExist() {
        return itemLevelLocationExist;
    }

    public void setItemLevelLocationExist(boolean itemLevelLocationExist) {
        this.itemLevelLocationExist = itemLevelLocationExist;
    }

    public String getProxyPatronBarcode() {
        return proxyPatronBarcode;
    }

    public void setProxyPatronBarcode(String proxyPatronBarcode) {
        this.proxyPatronBarcode = proxyPatronBarcode;
    }

    public String getProxyPatronBarcodeUrl() {
        return proxyPatronBarcodeUrl;
    }

    public void setProxyPatronBarcodeUrl(String proxyPatronBarcodeUrl) {
        this.proxyPatronBarcodeUrl = proxyPatronBarcodeUrl;
    }

    public String getCheckInMachineId() {
        return checkInMachineId;
    }

    public void setCheckInMachineId(String checkInMachineId) {
        this.checkInMachineId = checkInMachineId;
    }

    public List<FeeType> getFeeType() {
        return feeType;
    }

    public void setFeeType(List<FeeType> feeType) {
        this.feeType = feeType;
    }

    public void loanPeriod(String defaultLoanPeriod, String recallLoanPeriod) {
        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
        loanDateTimeUtil.setPolicyId(getCirculationPolicyId());
        if(null == oleCirculationDesk){
            OleCirculationDesk oleCirculationDesk = getCirculationLocationId() != null ? new CircDeskLocationResolver().getOleCirculationDesk(getCirculationLocationId()) : null;
            setOleCirculationDesk(oleCirculationDesk);
        }
        Date calculateDateTimeByPeriod = null;
        if (!isRequestPatron()) {
            calculateDateTimeByPeriod = loanDateTimeUtil.calculateDateTimeByPeriod(defaultLoanPeriod, getOleCirculationDesk());
        } else {
            calculateDateTimeByPeriod = loanDateTimeUtil.calculateDateTimeByPeriod(recallLoanPeriod, getOleCirculationDesk());
        }
        setLoanDueDate((calculateDateTimeByPeriod != null ? new Timestamp(calculateDateTimeByPeriod.getTime()) : null));
    }

    public Integer getOverdueFineAmount(OleCirculationPolicyServiceImpl oleCirculationPolicyService) {
        Integer overdueFineAmt = 0;
        if (null != oleDeliverRequestBo) {
            List<FeeType> feeTypeList = oleCirculationPolicyService.getPatronBillPayment(oleDeliverRequestBo.getBorrowerId());
            for (FeeType feeType : feeTypeList) {
                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                overdueFineAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.OVERDUE_FINE) ? fineAmount : 0;
            }
        }
        return overdueFineAmt;
    }

    public Integer getReplacementFineAmount(OleCirculationPolicyServiceImpl oleCirculationPolicyService) {
        Integer replacementFeeAmt = 0;
        if (null != oleDeliverRequestBo) {
            List<FeeType> feeTypeList = oleCirculationPolicyService.getPatronBillPayment(oleDeliverRequestBo.getBorrowerId());
            for (FeeType feeType : feeTypeList) {
                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                replacementFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE) ? fineAmount : 0;
            }
        }
        return replacementFeeAmt;
    }

    public Integer getServiceFeeAmount(OleCirculationPolicyServiceImpl oleCirculationPolicyService) {
        Integer serviceFeeAmt = 0;
        if (null != oleDeliverRequestBo) {
            List<FeeType> feeTypeList = oleCirculationPolicyService.getPatronBillPayment(oleDeliverRequestBo.getBorrowerId());
            for (FeeType feeType : feeTypeList) {
                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                serviceFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.SERVICE_FEE) ? fineAmount : 0;
            }
        }
        return serviceFeeAmt;
    }

    public Integer getAllCharges(OleCirculationPolicyServiceImpl oleCirculationPolicyService) {
        Integer allCharges = 0;

        allCharges = getOverdueFineAmount(oleCirculationPolicyService) + getReplacementFineAmount
                (oleCirculationPolicyService);

        return allCharges;
    }

    public void addErrorsAndPermission(String errorsAndPermissions) {
        getErrorsAndPermission().put("permissionName", errorsAndPermissions);
    }

    public Boolean isCheckinLocationSameAsHomeLocation() {
        String itemFullPathLocation = getItemFullPathLocation();

        String operatorCircLocations = getOperatorsCirculationLocation();

        if (null != itemFullPathLocation && null != operatorCircLocations) {
            StringTokenizer strTokenizer = new StringTokenizer(operatorCircLocations, "#");
            while (strTokenizer.hasMoreTokens()) {
                String nextToken = strTokenizer.nextToken();
                if (nextToken.equals(itemFullPathLocation)) {
                    return true;
                }
            }
        }

        return false;

    }

    public boolean isPastAndRenewDueDateSame() throws Exception {
        Timestamp pastLoanDueDate = getLoanDueDate();
        Date newLoanDueDate = getPastDueDate();
        if (pastLoanDueDate != null) {
            return (new Date(pastLoanDueDate.getTime()).compareTo(newLoanDueDate) == 0 ? true : false);
        } else {
            throw new Exception("No Fixed Due Date found for the renewal policy");
        }
    }

    public boolean IsIndefiniteDueDate() {
        return getLoanDueDate() == null ? true : false;
    }



    public Boolean isItemPickupLocationSameAsOperatorCircLoc() {
        OleCirculationDesk olePickUpLocation = null == getOleDeliverRequestBo() ? null : getOleDeliverRequestBo().getOlePickUpLocation();
        String operatorCircLocations = getOperatorsCirculationLocation();

        if (null != olePickUpLocation && null != operatorCircLocations) {
            StringTokenizer strTokenizer = new StringTokenizer(operatorCircLocations, "#");
            while (strTokenizer.hasMoreTokens()) {
                String nextToken = strTokenizer.nextToken();
                if (nextToken.equals(olePickUpLocation)) {
                    return true;
                }
            }
        }

        return false;
    }


    public boolean isPickupCirculationLocationMatched(Collection<Object> pickUpLocation,OleDeliverRequestBo deliverRequestBo) {

        for (Object oleCirculationDeskLoc : pickUpLocation){
            OleCirculationDeskLocation oleCirculationDeskLocation = (OleCirculationDeskLocation)oleCirculationDeskLoc;
            if (StringUtils.isNotBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()) &&
                    oleCirculationDeskLocation.getOleCirculationDesk() !=null &&
                    oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode()!= null &&
                    oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode().contains(deliverRequestBo.getPickUpLocationCode())) {
                return true;
            }
        }
        return false;
    }

    public List<OleDeliverRequestBo> getHoldRequestForPatron(OlePatronDocument olePatronDocument,OleCirculationDesk oleCirculationDesk) {
        if(olePatronDocument.getOleDeliverRequestBos() != null && olePatronDocument.getOleDeliverRequestBos().size()>0) {
            for(OleDeliverRequestBo deliverRequestBo : olePatronDocument.getOleDeliverRequestBos()) {
                if (deliverRequestBo.getRequestTypeCode() != null && deliverRequestBo.getRequestTypeCode().contains("Hold")) {
                    if (StringUtils.isNotBlank(oleCirculationDesk.getShowItemOnHold()) && oleCirculationDesk.getShowItemOnHold().equals(OLEConstants.CURR_CIR_DESK) && oleCirculationDesk.getOlePickupCirculationDeskLocations() != null){
                        Collection<Object> oleCirculationDeskLocations =  getOleLoanDocumentDaoOjb().getPickUpLocationForCirculationDesk(oleCirculationDesk);
                        if(isPickupCirculationLocationMatched(oleCirculationDeskLocations,deliverRequestBo)) {
                            deliverRequestBo.setOnHoldRequestForPatronMessage(OLEConstants.PTRN_RQST_MSG_CURR_CIR_DESK);
                            holdRequestForPatron.add(deliverRequestBo);
                        }
                    }else if (StringUtils.isNotBlank(oleCirculationDesk.getShowItemOnHold()) && oleCirculationDesk.getShowItemOnHold().equals(OLEConstants.ALL_CIR_DESK)) {
                        deliverRequestBo.setOnHoldRequestForPatronMessage(OLEConstants.PTRN_RQST_MSG_ALL_CIR_DESK);
                        holdRequestForPatron.add(deliverRequestBo);
                    }
                }
            }
        }
        return holdRequestForPatron;
    }

    public String getRealPatronWithLoan() {
        StringBuilder message = new StringBuilder();
        if (getItemLoanStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT) && !isRenewalItemFlag()) {
            Map barMap = new HashMap();
            barMap.put("itemId", getItemId());
            List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService()
                    .findMatching(OleLoanDocument.class, barMap);
            if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
                String url = "<a target=\"_blank\" href=" + OLEConstants.ASSIGN_INQUIRY_PATRON_ID + oleLoanDocuments.get(0).getPatronId() + OLEConstants.ASSIGN_PATRON_INQUIRY + ">" + oleLoanDocuments.get(0).getPatronId() + "</a>";
                message.append(OLEConstants.ITEM_STATUS_LOANED_ANOTHER_PATRON_PERMISSION + "&nbsp;&nbsp;:&nbsp;" + url + OLEConstants.BREAK);
            }
        }
        return message.toString();
    }

    private Timestamp calculateLoanDueDate(String loanPeriod) {
        Calendar calendar = Calendar.getInstance();
        String loanPeriodType[]=null;
        Timestamp dueDate = null;
        if(loanPeriod != null && loanPeriod.trim().length()>0){
            loanPeriodType =  loanPeriod.split("-");
            int loanPeriodValue =  Integer.parseInt(loanPeriodType[0].toString());
            String loanPeriodTypeValue =  loanPeriodType[1].toString();
            if(loanPeriodTypeValue.equalsIgnoreCase("M")){
                calendar.add(Calendar.MINUTE, loanPeriodValue);
            } else if(loanPeriodTypeValue.equalsIgnoreCase("H")) {
                calendar.add(Calendar.HOUR, loanPeriodValue);
            } else if(loanPeriodTypeValue.equalsIgnoreCase("W")) {
                calendar.add(Calendar.WEEK_OF_MONTH, loanPeriodValue);
            } else {
                calendar.add(Calendar.DATE, loanPeriodValue);
            }
            dueDate =  new Timestamp(calendar.getTime().getTime());
        }
        return dueDate;
    }

    public Boolean isItemHasRequest() {
        Map<String, String> requestHistoryMap = new HashMap<>();
        if(StringUtils.isNotBlank(this.itemId)) {
            requestHistoryMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, this.itemId);
            List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestHistoryMap);
            if(CollectionUtils.isNotEmpty(oleDeliverRequestBoList)) {
                return true;
            }
            List<OleDeliverRequestHistoryRecord> oleDeliverRequestHistoryRecords = getOleLoanDocumentDaoOjb().getExpiredRequest(this.itemId, this.createDate);
            if(CollectionUtils.isNotEmpty(oleDeliverRequestHistoryRecords)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDamagedItemIndicator() {
        return damagedItemIndicator;
    }

    public void setDamagedItemIndicator(boolean damagedItemIndicator) {
        this.damagedItemIndicator = damagedItemIndicator;
    }

    public Timestamp getDamagedItemDate() {
        return damagedItemDate;
    }

    public void setDamagedItemDate(Timestamp damagedItemDate) {
        this.damagedItemDate = damagedItemDate;
    }

    public String getNoOfMissingPiece() {
        return noOfMissingPiece;
    }

    public void setNoOfMissingPiece(String noOfMissingPiece) {
        this.noOfMissingPiece = noOfMissingPiece;
    }

    public Timestamp getMissingPieceItemDate() {
        return missingPieceItemDate;
    }

    public void setMissingPieceItemDate(Timestamp missingPieceItemDate) {
        this.missingPieceItemDate = missingPieceItemDate;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getDeliverNotices());
        return managedLists;
    }

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        return (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
    }

    @Override
    public int hashCode() {
        return itemId != null ? itemId.hashCode() : 0;
    }



    public String getSentNoticesUrl() {
        return baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/loanSentNotices?viewId=OleLoanNoticesSentView&amp;methodToCall=viewLoanSentNotices&amp;loanId=" + loanId;
    }

    public void setSentNoticesUrl(String sentNoticesUrl) {
        this.sentNoticesUrl = sentNoticesUrl;
    }

    public ItemFineRate getItemFineRate() {
        return itemFineRate;
    }

    public void setItemFineRate(ItemFineRate itemFineRate) {
        this.itemFineRate = itemFineRate;
    }

    public boolean isOverrideCheckInTime() {
        return overrideCheckInTime;
    }

    public void setOverrideCheckInTime(boolean overrideCheckInTime) {
        this.overrideCheckInTime = overrideCheckInTime;
    }

    public String getItemLostNote() {
        return itemLostNote;
    }

    public void setItemLostNote(String itemLostNote) {
        this.itemLostNote = itemLostNote;
    }

    public String getItemReplaceNote() {
        return itemReplaceNote;
    }

    public void setItemReplaceNote(String itemReplaceNote) {
        this.itemReplaceNote = itemReplaceNote;
    }

    public boolean isManualBill() {
        return isManualBill;
    }

    public void setIsManualBill(boolean isManualBill) {
        this.isManualBill = isManualBill;
    }

    public int getNoOfClaimsReturnedNoticesSent() {
        return noOfClaimsReturnedNoticesSent;
    }

    public void setNoOfClaimsReturnedNoticesSent(int noOfClaimsReturnedNoticesSent) {
        this.noOfClaimsReturnedNoticesSent = noOfClaimsReturnedNoticesSent;
    }

    public int getClaimsSearchCount() {
        return claimsSearchCount;
    }

    public void setClaimsSearchCount(int claimsSearchCount) {
        this.claimsSearchCount = claimsSearchCount;
    }

    public Timestamp getLastClaimsReturnedSearchedDate() {
        return lastClaimsReturnedSearchedDate;
    }

    public void setLastClaimsReturnedSearchedDate(Timestamp lastClaimsReturnedSearchedDate) {
        this.lastClaimsReturnedSearchedDate = lastClaimsReturnedSearchedDate;
    }
}
