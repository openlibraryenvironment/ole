package org.kuali.ole.deliver.batch;

import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/19/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleNoticeBo extends PersistableBusinessObjectBase  implements Cloneable{

    private OleCirculationDesk oleCirculationDesk;
    private String circulationDeskAddress;
    private String circulationDeskEmailAddress;
    private String circulationDeskPhoneNumber;
    private String circulationDeskName;
    private String circulationDeskReplyToEmail;

    private String patronName;
    private String patronAddress;
    private String patronEmailAddress;
    private String patronPhoneNumber;
    private OlePatronDocument olePatron;

    private String noticeTitle;
    private String title;
    private String author;
    private String volumeNumber;
    private String itemCallNumber;
    private String itemId;
    private String itemShelvingLocation;
    private Item oleItem;

    private Date onHoldDueDate;
    private Date originalDueDate;
    private Date newDueDate;
    private String noticeName;
    private String noticeSpecificContent;
    private String noticeSpecificFooterContent;
    private String pickUpLocation;
    private Date dueDate;
    private Date expiredOnHoldDate;

    private Date overDueNoticeDate;
    private OleLoanDocument oleLoanDocument;

    private String volumeIssueCopyNumber;

    private String checkInDate;

    private String noticeType;

    private String chronology;
    private String enumeration;

    private String dueDateString;
    private String copyNumber;
    private String billNumber;
    private String feeType;
    private String feeAmount;
    private String itemInstitution;
    private String itemCampus;
    private String itemCollection;
    private String itemLibrary;
    private String itemLocation;
    private String itemTypeDesc;
    private String missingPieceNote;
    private String itemCallNumberPrefix;
    private int claimsSearchCount;
    private String fineItemDueDate;

    public String getFineItemDueDate() {
        return fineItemDueDate;
    }

    public void setFineItemDueDate(String fineItemDueDate) {
        this.fineItemDueDate = fineItemDueDate;
    }

    public int getClaimsSearchCount() {
        return claimsSearchCount;
    }

    public void setClaimsSearchCount(int claimsSearchCount) {
        this.claimsSearchCount = claimsSearchCount;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getVolumeIssueCopyNumber() {
        return volumeIssueCopyNumber;
    }

    public void setVolumeIssueCopyNumber(String volumeIssueCopyNumber) {
        this.volumeIssueCopyNumber = volumeIssueCopyNumber;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public String getCirculationDeskAddress() {
        return circulationDeskAddress;
    }

    public void setCirculationDeskAddress(String circulationDeskAddress) {
        this.circulationDeskAddress = circulationDeskAddress;
    }

    public String getCirculationDeskEmailAddress() {
        return circulationDeskEmailAddress;
    }

    public void setCirculationDeskEmailAddress(String circulationDeskEmailAddress) {
        this.circulationDeskEmailAddress = circulationDeskEmailAddress;
    }

    public String getCirculationDeskPhoneNumber() {
        return circulationDeskPhoneNumber;
    }

    public void setCirculationDeskPhoneNumber(String circulationDeskPhoneNumber) {
        this.circulationDeskPhoneNumber = circulationDeskPhoneNumber;
    }

    public String getCirculationDeskName() {
        return circulationDeskName;
    }

    public void setCirculationDeskName(String circulationDeskName) {
        this.circulationDeskName = circulationDeskName;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getPatronAddress() {
        return patronAddress;
    }

    public void setPatronAddress(String patronAddress) {
        this.patronAddress = patronAddress;
    }

    public String getPatronEmailAddress() {
        return patronEmailAddress;
    }

    public void setPatronEmailAddress(String patronEmailAddress) {
        this.patronEmailAddress = patronEmailAddress;
    }

    public String getPatronPhoneNumber() {
        return patronPhoneNumber;
    }

    public void setPatronPhoneNumber(String patronPhoneNumber) {
        this.patronPhoneNumber = patronPhoneNumber;
    }

    public OlePatronDocument getOlePatron() {
        return olePatron;
    }

    public void setOlePatron(OlePatronDocument olePatron) {
        this.olePatron = olePatron;
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

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getItemCallNumber() {
        return itemCallNumber;
    }

    public void setItemCallNumber(String itemCallNumber) {
        this.itemCallNumber = itemCallNumber;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemShelvingLocation() {
        return itemShelvingLocation;
    }

    public void setItemShelvingLocation(String itemShelvingLocation) {
        this.itemShelvingLocation = itemShelvingLocation;
    }

    public Item getOleItem() {
        return oleItem;
    }

    public void setOleItem(Item oleItem) {
        this.oleItem = oleItem;
    }

    public Date getOnHoldDueDate() {
        return onHoldDueDate;
    }

    public void setOnHoldDueDate(Date onHoldDueDate) {
        this.onHoldDueDate = onHoldDueDate;
    }

    public Date getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(Date originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public Date getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(Date newDueDate) {
        this.newDueDate = newDueDate;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeSpecificContent() {
        return noticeSpecificContent;
    }

    public void setNoticeSpecificContent(String noticeSpecificContent) {
        this.noticeSpecificContent = noticeSpecificContent;
    }

    public Date getOverDueNoticeDate() {
        return overDueNoticeDate;
    }

    public void setOverDueNoticeDate(Date overDueNoticeDate) {
        this.overDueNoticeDate = overDueNoticeDate;
    }

    public OleLoanDocument getOleLoanDocument() {
        return oleLoanDocument;
    }

    public void setOleLoanDocument(OleLoanDocument oleLoanDocument) {
        this.oleLoanDocument = oleLoanDocument;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Date getExpiredOnHoldDate() {
        return expiredOnHoldDate;
    }

    public void setExpiredOnHoldDate(Date expiredOnHoldDate) {
        this.expiredOnHoldDate = expiredOnHoldDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
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

    public String getCirculationDeskReplyToEmail() {
        return circulationDeskReplyToEmail;
    }

    public void setCirculationDeskReplyToEmail(String circulationDeskReplyToEmail) {
        this.circulationDeskReplyToEmail = circulationDeskReplyToEmail;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getDueDateString() {
        return dueDateString;
    }

    public void setDueDateString(String dueDateString) {
        this.dueDateString = dueDateString;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getFeeType() {
        return feeType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public String getNoticeSpecificFooterContent() {
        return noticeSpecificFooterContent;
    }

    public void setNoticeSpecificFooterContent(String noticeSpecificFooterContent) {
        this.noticeSpecificFooterContent = noticeSpecificFooterContent;
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

    public String getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }

    public String getItemLibrary() {
        return itemLibrary;
    }

    public void setItemLibrary(String itemLibrary) {
        this.itemLibrary = itemLibrary;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemTypeDesc() {
        return itemTypeDesc;
    }

    public void setItemTypeDesc(String itemTypeDesc) {
        this.itemTypeDesc = itemTypeDesc;
    }

    public String getMissingPieceNote() {
        return missingPieceNote;
    }

    public void setMissingPieceNote(String missingPieceNote) {
        this.missingPieceNote = missingPieceNote;
    }
    public String getItemCallNumberPrefix() {
        return itemCallNumberPrefix;
    }

    public void setItemCallNumberPrefix(String itemCallNumberPrefix) {
        this.itemCallNumberPrefix = itemCallNumberPrefix;
    }
}