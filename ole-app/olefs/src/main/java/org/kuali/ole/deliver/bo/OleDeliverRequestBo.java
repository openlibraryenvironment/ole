package org.kuali.ole.deliver.bo;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.api.OleDeliverRequestContract;
import org.kuali.ole.deliver.api.OleDeliverRequestDefinition;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/1/12
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestBo extends PersistableBusinessObjectBase implements OleDeliverRequestContract {
    private String requestId;
    private String poLineItemNo;
    private String borrowerId;
    private String borrowerBarcode;
    private Integer borrowerQueuePosition;
    private String proxyBorrowerId;
    private String proxyBorrowerBarcode;
    private String loanTransactionRecordNumber;
    private String requestTypeId;
    private String contentDescription;
    private String copyFormat;
    private String pickUpLocationId;
    private String operatorCreateId;
    private String operatorModifiedId;
    private String machineId;
    private String circulationLocationId;
    private Timestamp createDate;
    private Date modifiedDate;
    private Date requestExpiryDate;
    private Date onHoldNoticeSentDate;
    private Date recallNoticeSentDate;
    private String itemId;
    private String itemTypeName;
    private String itemUuid;

    private String selectRequest;
    private String requestCreator;
    private String requestTypeCode;
    private String borrowerName;
    private String firstName;
    private String lastName;
    private String proxyBorrowerName;
    private String proxyBorrowerFirstName;
    private String proxyBorrowerLastName;
    private String operatorCreateName;
    private String operatorModifierName;

    private String courtesyNotice;
    private String title;
    private String author;
    private String shelvingLocation;
    private String callNumber;
    private String copyNumber;
    private String volumeNumber;
    private String patronName;
    private String itemType;
    private String itemStatus;
    private String circulationLocationCode;
    private String pickUpLocationCode;
    private String inTransitCheckInNote;
    private Timestamp inTransitDate;
    private boolean requestFlag;
    private boolean claimsReturnedFlag;
    private String message;
    private String chronology;
    private String enumeration;

    private OleCirculationDesk oleCirculationLocation = new OleCirculationDesk();

    private OleCirculationDesk olePickUpLocation = new OleCirculationDesk();
    private OlePatronDocument olePatron = new OlePatronDocument();
    private OlePatronDocument oleProxyPatron = new OlePatronDocument();

    private PersonImpl operatorCreator = new PersonImpl();
    private PersonImpl operatorModifier = new PersonImpl();
    private OleDeliverRequestType oleDeliverRequestType = new OleDeliverRequestType();

    private String roleName = OLEConstants.OleCirculationDeskDetail.OPERATOR_ROLE_NAME;
    private String roleNamespaceCode = OLEConstants.OleCirculationDeskDetail.OPERATOR_ROLE_NAMESPACE;
    private Date date;
    private OleItemSearch oleItemSearch = new OleItemSearch();
    private transient Item oleItem;

    private String itemInstitution;
    private String itemCampus;
    private String itemLibrary;
    private String itemCollection;
    private String itemLocation;
    private String itemTypeDesc;
    private String noticeType;
    private Date newDueDate;
    private Date originalDueDate;
    private String requestStatus;
    private boolean asrFlag;
    private boolean isASRItem;
    private boolean isValidToProcess;
    private String bibId;
    private String requestLevel;
    private Date holdExpirationDate;
    private int recallRequestCount;
    private int holdRequestCount;
    private int pageRequestCount;
    private int copyRequestCount;
    private int asrRequestCount;
    private OleDroolsHoldResponseBo oleDroolsHoldResponseBo;
    private int fineAmount;
    private List<OLEDeliverNotice> deliverNotices;
    private String onHoldRequestForPatronMessage;
    private boolean requestTypePopulated = true;

    private String recallNoticeContentConfigName;
    private String requestExpirationNoticeContentConfigName;
    private String OnHoldNoticeContentConfigName;
    private String OnHoldCourtesyNoticeContentConfigName;
    private String OnHoldExpirationNoticeContentConfigName;

    private String requestNote;
    private boolean select=false;
    private String callNumberPrefix;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getItemInstitution() {
        return itemInstitution;
    }

    public void setItemInstitution(String itemInstitution) {
        this.itemInstitution = itemInstitution;
    }

    public String getItemCampus() {
        return itemCampus;
    }

    public void setItemCampus(String itemCampus) {
        this.itemCampus = itemCampus;
    }

    public String getItemLibrary() {
        return itemLibrary;
    }

    public void setItemLibrary(String itemLibrary) {
        this.itemLibrary = itemLibrary;
    }

    public String getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemTypeDesc() {
        return itemTypeDesc;
    }

    public void setItemTypeDesc(String itemTypeDesc) {
        this.itemTypeDesc = itemTypeDesc;
    }

    public boolean isClaimsReturnedFlag() {
        return claimsReturnedFlag;
    }

    public void setClaimsReturnedFlag(boolean claimsReturnedFlag) {
        this.claimsReturnedFlag = claimsReturnedFlag;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getProxyBorrowerFirstName() {
        return proxyBorrowerFirstName;
    }

    public void setProxyBorrowerFirstName(String proxyBorrowerFirstName) {
        this.proxyBorrowerFirstName = proxyBorrowerFirstName;
    }

    private Timestamp recallDueDate;


    public Timestamp getRecallDueDate() {
        return recallDueDate;
    }

    public void setRecallDueDate(Timestamp recallDueDate) {
        this.recallDueDate = recallDueDate;
    }

    public Item getOleItem() {
        return oleItem;
    }

    public String getProxyBorrowerLastName() {
        return proxyBorrowerLastName;
    }

    public void setProxyBorrowerLastName(String proxyBorrowerLastName) {
        this.proxyBorrowerLastName = proxyBorrowerLastName;
    }

    public void setOleItem(Item oleItem) {
        this.oleItem = oleItem;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getCirculationLocationCode() {
        if (circulationLocationId != null && circulationLocationCode == null && oleCirculationLocation.getCirculationDeskId() != null) {
            circulationLocationCode = oleCirculationLocation.getCirculationDeskCode();
        }
        return circulationLocationCode;
    }

    public void setCirculationLocationCode(String circulationLocationCode) {
        this.circulationLocationCode = circulationLocationCode;
    }

    public String getPickUpLocationCode() {
        if (pickUpLocationId != null && pickUpLocationCode == null && olePickUpLocation.getCirculationDeskId() != null) {
            pickUpLocationCode = olePickUpLocation.getCirculationDeskCode();
        }
        return pickUpLocationCode;
    }

    public void setPickUpLocationCode(String pickUpLocationCode) {
        this.pickUpLocationCode = pickUpLocationCode;
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


    public OleItemSearch getOleItemSearch() {
        return oleItemSearch;
    }

    public void setOleItemSearch(OleItemSearch oleItemSearch) {
        this.oleItemSearch = oleItemSearch;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNamespaceCode() {
        return roleNamespaceCode;
    }

    public void setRoleNamespaceCode(String roleNamespaceCode) {
        this.roleNamespaceCode = roleNamespaceCode;
    }

    /**
     * Gets the requestId attribute.
     *
     * @return Returns the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the poLineItemNo attribute.
     *
     * @return Returns the poLineItemNo
     */
    public String getPoLineItemNo() {
        return poLineItemNo;
    }

    public void setPoLineItemNo(String poLineItemNo) {
        this.poLineItemNo = poLineItemNo;
    }

    /**
     * Gets the borrowerId attribute.
     *
     * @return Returns the borrowerId
     */
    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    /**
     * Gets the borrowerQueuePosition attribute.
     *
     * @return Returns the borrowerQueuePosition
     */
    public Integer getBorrowerQueuePosition() {
        return borrowerQueuePosition;
    }

    public void setBorrowerQueuePosition(Integer borrowerQueuePosition) {
        this.borrowerQueuePosition = borrowerQueuePosition;
    }

    /**
     * Gets the proxyBorrowerId attribute.
     *
     * @return Returns the proxyBorrowerId
     */
    public String getProxyBorrowerId() {
        return proxyBorrowerId;
    }

    public void setProxyBorrowerId(String proxyBorrowerId) {
        if (proxyBorrowerId != null && proxyBorrowerId.equals(""))
            this.proxyBorrowerId = null;
        else
            this.proxyBorrowerId = proxyBorrowerId;
    }

    /**
     * Gets the loanTransactionRecordNumber attribute.
     *
     * @return Returns the loanTransactionRecordNumber
     */
    public String getLoanTransactionRecordNumber() {
        return loanTransactionRecordNumber;
    }

    public void setLoanTransactionRecordNumber(String loanTransactionRecordNumber) {
        this.loanTransactionRecordNumber = loanTransactionRecordNumber;
    }

    /**
     * Gets the requestTypeId attribute.
     *
     * @return Returns the requestTypeId
     */
    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {

        this.requestTypeId = requestTypeId;
    }

    /**
     * Gets the contentDescription attribute.
     *
     * @return Returns the contentDescription
     */
    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    /**
     * Gets the copyFormat attribute.
     *
     * @return Returns the copyFormat
     */
    public String getCopyFormat() {
        return copyFormat;
    }

    public void setCopyFormat(String copyFormat) {
        this.copyFormat = copyFormat;
    }

    /**
     * Gets the pickUpLocationId attribute.
     *
     * @return Returns the pickUpLocationId
     */
    public String getPickUpLocationId() {
        return pickUpLocationId;
    }

    public void setPickUpLocationId(String pickUpLocationId) {
        if (pickUpLocationId != null && pickUpLocationId.equals(""))
            this.pickUpLocationId = null;
        else
            this.pickUpLocationId = pickUpLocationId;
    }

    /**
     * Gets the operatorCreateId attribute.
     *
     * @return Returns the operatorCreateId
     */
    public String getOperatorCreateId() {
        return operatorCreateId;
    }

    public void setOperatorCreateId(String operatorCreateId) {
        this.operatorCreateId = operatorCreateId;
    }

    /**
     * Gets the operatorModifiedId attribute.
     *
     * @return Returns the operatorModifiedId
     */
    public String getOperatorModifiedId() {
        return operatorModifiedId;
    }

    public void setOperatorModifiedId(String operatorModifiedId) {
        this.operatorModifiedId = operatorModifiedId;
    }

    /**
     * Gets the machineId attribute.
     *
     * @return Returns the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the circulationLocationId attribute.
     *
     * @return Returns the circulationLocationId
     */
    public String getCirculationLocationId() {
        return circulationLocationId;
    }

    public void setCirculationLocationId(String circulationLocationId) {
        if (circulationLocationId != null && circulationLocationId.equals(""))
            this.circulationLocationId = null;
        else
            this.circulationLocationId = circulationLocationId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getRequestExpiryDate() {
        return requestExpiryDate;
    }

    public void setRequestExpiryDate(Date requestExpiryDate) {
        this.requestExpiryDate = requestExpiryDate;
    }

    public Date getOnHoldNoticeSentDate() {
        return onHoldNoticeSentDate;
    }

    public void setOnHoldNoticeSentDate(Date onHoldNoticeSentDate) {
        this.onHoldNoticeSentDate = onHoldNoticeSentDate;
    }

    public Date getRecallNoticeSentDate() {
        return recallNoticeSentDate;
    }

    public void setRecallNoticeSentDate(Date recallNoticeSentDate) {
        this.recallNoticeSentDate = recallNoticeSentDate;
    }

    /**
     * Gets the itemId attribute.
     *
     * @return Returns the itemId
     */
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the selectRequest attribute.
     *
     * @return Returns the selectRequest
     */
    public String getSelectRequest() {
        return selectRequest;
    }

    public void setSelectRequest(String selectRequest) {
        this.selectRequest = selectRequest;
    }

    /**
     * Gets the requestCreator attribute.
     *
     * @return Returns the requestCreator
     */
    public String getRequestCreator() {
        return requestCreator;
    }

    public void setRequestCreator(String requestCreator) {
        this.requestCreator = requestCreator;
    }

    /**
     * Gets the requestTypeCode attribute.
     *
     * @return Returns the requestTypeCode
     */
    public String getRequestTypeCode() {
        if (requestTypeId != null && oleDeliverRequestType.getRequestTypeId() != null) {
            return oleDeliverRequestType.getRequestTypeCode();
        }
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getFirstName() {
        if (firstName == null && borrowerId != null && olePatron.getBarcode() != null && (borrowerId.equals(olePatron.getOlePatronId()))) {
            firstName = olePatron.getEntity().getNames().get(0).getFirstName();
        } else if(firstName == null && borrowerId != null && (olePatron.getBarcode() == null)) {
            getOlePatron();
            if(borrowerId.equals(olePatron.getOlePatronId())) {
                firstName = olePatron.getEntity().getNames().get(0).getFirstName();
            }
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (lastName == null && borrowerId != null && olePatron.getBarcode() != null && (borrowerId.equals(olePatron.getOlePatronId()))) {
            lastName = olePatron.getEntity().getNames().get(0).getLastName();
        } else if(lastName == null && borrowerId != null && (olePatron.getBarcode() == null)) {
            getOlePatron();
            if(borrowerId.equals(olePatron.getOlePatronId())) {
                lastName = olePatron.getEntity().getNames().get(0).getLastName();
            }
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the borrowerName attribute.
     *
     * @return Returns the borrowerName
     */
    public String getBorrowerName() {
        if (borrowerName == null && borrowerId != null && olePatron.getBarcode() != null && (borrowerId.equals(olePatron.getOlePatronId()))) {
            return olePatron.getEntity().getNames().get(0).getFirstName() + " " + olePatron.getEntity().getNames().get(0).getLastName();
        }
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    /**
     * Gets the proxyBorrowerName attribute.
     *
     * @return Returns the proxyBorrowerName
     */
    public String getProxyBorrowerName() {
        if (proxyBorrowerName == null && proxyBorrowerId != null && oleProxyPatron.getBarcode() != null && (proxyBorrowerId.equals(oleProxyPatron.getOlePatronId()))) {
            return oleProxyPatron.getEntity().getNames().get(0).getFirstName() + " " + oleProxyPatron.getEntity().getNames().get(0).getLastName();
        }
        return proxyBorrowerName;
    }

    public void setProxyBorrowerName(String proxyBorrowerName) {
        this.proxyBorrowerName = proxyBorrowerName;
    }

    /**
     * Gets the operatorCreateName attribute.
     *
     * @return Returns the operatorCreateName
     */
    public String getOperatorCreateName() {
        return operatorCreateName;
    }

    public void setOperatorCreateName(String operatorCreateName) {
        this.operatorCreateName = operatorCreateName;
    }

    /**
     * Gets the courtesyNotice attribute.
     *
     * @return Returns the courtesyNotice
     */
    public String getCourtesyNotice() {
        return courtesyNotice;
    }

    public void setCourtesyNotice(String courtesyNotice) {
        this.courtesyNotice = courtesyNotice;
    }

    /**
     * Gets the title attribute.
     *
     * @return Returns the title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author attribute.
     *
     * @return Returns the author
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the shelvingLocation attribute.
     *
     * @return Returns the shelvingLocation
     */
    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    /**
     * Gets the callNumber attribute.
     *
     * @return Returns the callNumber
     */
    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    /**
     * Gets the copyNumber attribute.
     *
     * @return Returns the copyNumber
     */
    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    /**
     * Gets the patronName attribute.
     *
     * @return Returns the patronName
     */
    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OleCirculationDesk getOleCirculationLocation() {
        return oleCirculationLocation;
    }

    public void setOleCirculationLocation(OleCirculationDesk oleCirculationLocation) {
        this.oleCirculationLocation = oleCirculationLocation;
    }

    public OleCirculationDesk getOlePickUpLocation() {
        return olePickUpLocation;
    }

    public void setOlePickUpLocation(OleCirculationDesk olePickUpLocation) {
        this.olePickUpLocation = olePickUpLocation;
    }

    public OlePatronDocument getOlePatron() {
        if (null == olePatron || StringUtils.isEmpty(olePatron.getOlePatronId())) {
            String patronId = getBorrowerId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                Long startingTime = System.currentTimeMillis();
                olePatron = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
                Long endTimme = System.currentTimeMillis();
                Long timeTaken = endTimme-startingTime;
                System.out.println("The Time Taken for Fetching PatronObject : " + timeTaken);
            }
        }
        return olePatron;
    }

    public void setOlePatron(OlePatronDocument olePatron) {
        this.olePatron = olePatron;
    }

    public OlePatronDocument getOleProxyPatron() {
        if (null == oleProxyPatron) {
            String patronId = getProxyBorrowerId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                oleProxyPatron = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
            }
        }
        return oleProxyPatron;
    }

    public void setOleProxyPatron(OlePatronDocument oleProxyPatron) {
        this.oleProxyPatron = oleProxyPatron;
    }

    public PersonImpl getOperatorCreator() {
        return operatorCreator;
    }

    public void setOperatorCreator(PersonImpl operatorCreator) {
        this.operatorCreator = operatorCreator;
    }

    public PersonImpl getOperatorModifier() {
        return operatorModifier;
    }

    public void setOperatorModifier(PersonImpl operatorModifier) {
        this.operatorModifier = operatorModifier;
    }

    public OleDeliverRequestType getOleDeliverRequestType() {
        return oleDeliverRequestType;
    }

    public void setOleDeliverRequestType(OleDeliverRequestType oleDeliverRequestType) {
        this.oleDeliverRequestType = oleDeliverRequestType;
    }

    public String getOperatorModifierName() {
        return operatorModifierName;
    }

    public void setOperatorModifierName(String operatorModifierName) {
        this.operatorModifierName = operatorModifierName;
    }

    public String getInTransitCheckInNote() {
        return inTransitCheckInNote;
    }

    public void setInTransitCheckInNote(String inTransitCheckInNote) {
        this.inTransitCheckInNote = inTransitCheckInNote;
    }

    public boolean isRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(boolean requestFlag) {
        this.requestFlag = requestFlag;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleDeliverRequestBo into immutable object OleDeliverRequestDefinition
     *
     * @param bo
     * @return OleDeliverRequestDefinition
     */
    public static OleDeliverRequestDefinition to(OleDeliverRequestBo bo) {
        if (bo == null) {
            return null;
        }
        return OleDeliverRequestDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleDeliverRequestDefinition into PersistableBusinessObjectBase OleDeliverRequestBo
     *
     * @param imOleDeliverRequestDefinition
     * @return bo
     */
    public static OleDeliverRequestBo from(OleDeliverRequestDefinition imOleDeliverRequestDefinition) {
        if (imOleDeliverRequestDefinition == null) {
            return null;
        }

        OleDeliverRequestBo bo = new OleDeliverRequestBo();
        bo.itemId = imOleDeliverRequestDefinition.getItemId();
        bo.requestId = imOleDeliverRequestDefinition.getRequestId();
        bo.title = imOleDeliverRequestDefinition.getTitle();
        bo.author = imOleDeliverRequestDefinition.getAuthor();
        bo.callNumber = imOleDeliverRequestDefinition.getCallNumber();
        bo.copyNumber = imOleDeliverRequestDefinition.getCopyNumber();
        bo.itemStatus = imOleDeliverRequestDefinition.getItemStatus();
        bo.itemType = imOleDeliverRequestDefinition.getItemType();
        bo.borrowerQueuePosition = imOleDeliverRequestDefinition.getBorrowerQueuePosition();
        bo.volumeNumber = imOleDeliverRequestDefinition.getVolumeNumber();
        bo.shelvingLocation = imOleDeliverRequestDefinition.getShelvingLocation();
        bo.oleDeliverRequestType = OleDeliverRequestType.from(imOleDeliverRequestDefinition.getOleDeliverRequestType());
        bo.versionNumber = imOleDeliverRequestDefinition.getVersionNumber();

        return bo;
    }

    @Override
    public String getId() {
        return this.requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public String getBorrowerBarcode() {
        return borrowerBarcode;
    }

    public void setBorrowerBarcode(String borrowerBarcode) {
        this.borrowerBarcode = borrowerBarcode;
    }

    public String getProxyBorrowerBarcode() {
        return proxyBorrowerBarcode;
    }

    public void setProxyBorrowerBarcode(String proxyBorrowerBarcode) {
        this.proxyBorrowerBarcode = proxyBorrowerBarcode;
    }

    public Date getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(Date newDueDate) {
        this.newDueDate = newDueDate;
    }

    public Date getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(Date originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isAsrFlag() {
        return asrFlag;
    }

    public void setAsrFlag(boolean asrFlag) {
        this.asrFlag = asrFlag;
    }

    public boolean isASRItem() {
        return isASRItem;
    }

    public void setASRItem(boolean ASRItem) {
        isASRItem = ASRItem;
    }

    public boolean isValidToProcess() {
        return isValidToProcess;
    }

    public void setValidToProcess(boolean isValidToProcess) {
        this.isValidToProcess = isValidToProcess;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getRequestLevel() {
        return requestLevel;
    }

    public void setRequestLevel(String requestLevel) {
        this.requestLevel = requestLevel;
    }

    public Date getHoldExpirationDate() {
        return holdExpirationDate;
    }

    public void setHoldExpirationDate(Date holdExpirationDate) {
        this.holdExpirationDate = holdExpirationDate;
    }

    public int getRecallRequestCount() {
        return recallRequestCount;
    }

    public void setRecallRequestCount(int recallRequestCount) {
        this.recallRequestCount = recallRequestCount;
    }

    public int getHoldRequestCount() {
        return holdRequestCount;
    }

    public void setHoldRequestCount(int holdRequestCount) {
        this.holdRequestCount = holdRequestCount;
    }

    public int getPageRequestCount() {
        return pageRequestCount;
    }

    public void setPageRequestCount(int pageRequestCount) {
        this.pageRequestCount = pageRequestCount;
    }

    public int getCopyRequestCount() {
        return copyRequestCount;
    }

    public void setCopyRequestCount(int copyRequestCount) {
        this.copyRequestCount = copyRequestCount;
    }

    public int getAsrRequestCount() {
        return asrRequestCount;
    }

    public void setAsrRequestCount(int asrRequestCount) {
        this.asrRequestCount = asrRequestCount;
    }

    public OleDroolsHoldResponseBo getOleDroolsHoldResponseBo() {
        return oleDroolsHoldResponseBo;
    }

    public void setOleDroolsHoldResponseBo(OleDroolsHoldResponseBo oleDroolsHoldResponseBo) {
        this.oleDroolsHoldResponseBo = oleDroolsHoldResponseBo;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(int fineAmount) {
        this.fineAmount = fineAmount;
    }

    public List<OLEDeliverNotice> getDeliverNotices() {
        return deliverNotices;
    }

    public void setDeliverNotices(List<OLEDeliverNotice> deliverNotices) {
        this.deliverNotices = deliverNotices;
    }

    public String getOnHoldRequestForPatronMessage() {
        return onHoldRequestForPatronMessage;
    }

    public void setOnHoldRequestForPatronMessage(String onHoldRequestForPatronMessage) {
        this.onHoldRequestForPatronMessage = onHoldRequestForPatronMessage;
    }

    public boolean isRequestTypePopulated() {
        return requestTypePopulated;
    }

    public void setRequestTypePopulated(boolean requestTypePopulated) {
        this.requestTypePopulated = requestTypePopulated;
    }

    public String getRecallNoticeContentConfigName() {
        return recallNoticeContentConfigName;
    }

    public void setRecallNoticeContentConfigName(String recallNoticeContentConfigName) {
        this.recallNoticeContentConfigName = recallNoticeContentConfigName;
    }

    public String getRequestExpirationNoticeContentConfigName() {
        return requestExpirationNoticeContentConfigName;
    }

    public void setRequestExpirationNoticeContentConfigName(String requestExpirationNoticeContentConfigName) {
        this.requestExpirationNoticeContentConfigName = requestExpirationNoticeContentConfigName;
    }

    public String getOnHoldNoticeContentConfigName() {
        return OnHoldNoticeContentConfigName;
    }

    public void setOnHoldNoticeContentConfigName(String onHoldNoticeContentConfigName) {
        OnHoldNoticeContentConfigName = onHoldNoticeContentConfigName;
    }

    public String getOnHoldExpirationNoticeContentConfigName() {
        return OnHoldExpirationNoticeContentConfigName;
    }

    public void setOnHoldExpirationNoticeContentConfigName(String onHoldExpirationNoticeContentConfigName) {
        OnHoldExpirationNoticeContentConfigName = onHoldExpirationNoticeContentConfigName;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public Timestamp getInTransitDate() {
        return inTransitDate;
    }

    public void setInTransitDate(Timestamp inTransitDate) {
        this.inTransitDate = inTransitDate;
    }

    public String getOnHoldCourtesyNoticeContentConfigName() {
        return OnHoldCourtesyNoticeContentConfigName;
    }

    public void setOnHoldCourtesyNoticeContentConfigName(String onHoldCourtesyNoticeContentConfigName) {
        OnHoldCourtesyNoticeContentConfigName = onHoldCourtesyNoticeContentConfigName;
    }
}