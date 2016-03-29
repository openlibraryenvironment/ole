package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleTitleLevelRequestItem;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Date;
import java.util.List;

/**
 * Created by arunag on 12/16/14.
 */
public class OLEPlaceRequestForm extends UifFormBase {

    private String bibId;
    private String itemId;
    private String title;
    private String author;
    private String holdingId;
    private String patronBarcode;
    private String patronId;
    private String patronType;
    private String patronName;
    private String PatronFirstName;
    private EntityAddressBo oleEntityAddressBo;
    private boolean processRequest=false;
    private String requestLevel;
    private String requestType;
    private Date requestExpiryDate;
    private String pickUpLocationId;
    private boolean address;
    private String createDate;
    private String recallRequestType;
    private String holdRequestType;
    private String pageRequestType;
    private String copyRequestType;
    private String asrRequestType;
    private boolean blockOverride=false;

    private boolean recallRequest;
    private boolean holdRequest;
    private boolean pageRequest;
    private boolean copyRequest;
    private boolean asrRequest;
    private String itemBarcode;

    private String itemLocation;
    private String pickUpLocationCode;

    private OlePatronDocument olePatronDocument;

    private String errorMessage;
    private String itemStatus;
    private String successMessage;
    private boolean titleLevelRequest;
    private boolean itemLevelRequest = true;
    private boolean itemEligible=true;
    private List<OleTitleLevelRequestItem> titleLevelRequestItems;
    private boolean patronFound;
    private String itemType;
    private String newPrincipalId;
    private String loanLoginName;
    private boolean overrideFlag;
    private String overrideLoginMessage;
    private String overrideErrorMessage;
    private boolean blockSubmit=false;
    private String existingAddressType;
    private boolean displayRequestType=false;
    private boolean addressUpdated;
    private boolean showOk=false;
    private String requestNote;

    private String pickUpLocation;


    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public boolean isAddressUpdated() {
        return addressUpdated;
    }

    public void setAddressUpdated(boolean addressUpdated) {
        this.addressUpdated = addressUpdated;
    }

    public String getPatronFirstName() {
        return PatronFirstName;
    }

    public void setPatronFirstName(String patronFirstName) {
        PatronFirstName = patronFirstName;
    }

    public EntityAddressBo getOleEntityAddressBo() {
        return oleEntityAddressBo;
    }

    public void setOleEntityAddressBo(EntityAddressBo oleEntityAddressBo) {
        this.oleEntityAddressBo = oleEntityAddressBo;
    }

    public boolean isBlockSubmit() {
        return blockSubmit;
    }

    public void setBlockSubmit(boolean blockSubmit) {
        this.blockSubmit = blockSubmit;
    }

    public String getOverrideErrorMessage() {
        return overrideErrorMessage;
    }

    public void setOverrideErrorMessage(String overrideErrorMessage) {
        this.overrideErrorMessage = overrideErrorMessage;
    }

    public String getOverrideLoginMessage() {
        return overrideLoginMessage;
    }

    public void setOverrideLoginMessage(String overrideLoginMessage) {
        this.overrideLoginMessage = overrideLoginMessage;
    }

    public boolean isOverrideFlag() {
        return overrideFlag;
    }

    public void setOverrideFlag(boolean overrideFlag) {
        this.overrideFlag = overrideFlag;
    }

    public String getLoanLoginName() {
        return loanLoginName;
    }

    public void setLoanLoginName(String loanLoginName) {
        this.loanLoginName = loanLoginName;
    }

    public String getNewPrincipalId() {
        return newPrincipalId;
    }

    public void setNewPrincipalId(String newPrincipalId) {
        this.newPrincipalId = newPrincipalId;
    }

    public boolean isBlockOverride() {
        return blockOverride;
    }

    public void setBlockOverride(boolean blockOverride) {
        this.blockOverride = blockOverride;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isAddress() {
        return address;
    }

    public void setAddress(boolean address) {
        this.address = address;
    }

    public String getPickUpLocationId() {
        return pickUpLocationId;
    }

    public void setPickUpLocationId(String pickUpLocationId) {
        this.pickUpLocationId = pickUpLocationId;
    }

    public Date getRequestExpiryDate() {
        return requestExpiryDate;
    }

    public void setRequestExpiryDate(Date requestExpiryDate) {
        this.requestExpiryDate = requestExpiryDate;
    }

    public String getRequestLevel() {
        return requestLevel;
    }

    public void setRequestLevel(String requestLevel) {
        this.requestLevel = requestLevel;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isProcessRequest() {
        return processRequest;
    }

    public void setProcessRequest(boolean processRequest) {
        this.processRequest = processRequest;
    }

    public String getPatronType() {
        return patronType;
    }

    public void setPatronType(String patronType) {
        this.patronType = patronType;
    }

    public String getPatronName() {
        if(olePatronDocument!=null ){
            if( olePatronDocument.getRealPatronFirstName()!=null ){
            patronName = olePatronDocument.getRealPatronFirstName();}
            if(olePatronDocument.getRealPatronLastName()!=null){
                patronName = patronName + " " + olePatronDocument.getRealPatronLastName();
            }
        }
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(String holdingId) {
        this.holdingId = holdingId;
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

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRecallRequestType() {
        return recallRequestType;
    }

    public void setRecallRequestType(String recallRequestType) {
        this.recallRequestType = recallRequestType;
    }

    public String getHoldRequestType() {
        return holdRequestType;
    }

    public void setHoldRequestType(String holdRequestType) {
        this.holdRequestType = holdRequestType;
    }

    public String getPageRequestType() {
        return pageRequestType;
    }

    public void setPageRequestType(String pageRequestType) {
        this.pageRequestType = pageRequestType;
    }

    public String getCopyRequestType() {
        return copyRequestType;
    }

    public void setCopyRequestType(String copyRequestType) {
        this.copyRequestType = copyRequestType;
    }

    public boolean isRecallRequest() {
        return recallRequest;
    }

    public void setRecallRequest(boolean recallRequest) {
        this.recallRequest = recallRequest;
    }

    public boolean isHoldRequest() {
        return holdRequest;
    }

    public void setHoldRequest(boolean holdRequest) {
        this.holdRequest = holdRequest;
    }

    public boolean isPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(boolean pageRequest) {
        this.pageRequest = pageRequest;
    }

    public boolean isCopyRequest() {
        return copyRequest;
    }

    public void setCopyRequest(boolean copyRequest) {
        this.copyRequest = copyRequest;
    }


    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getPickUpLocationCode() {
        return pickUpLocationCode;
    }

    public void setPickUpLocationCode(String pickUpLocationCode) {
        this.pickUpLocationCode = pickUpLocationCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public List<OleTitleLevelRequestItem> getTitleLevelRequestItems() {
        return titleLevelRequestItems;
    }

    public void setTitleLevelRequestItems(List<OleTitleLevelRequestItem> titleLevelRequestItems) {
        this.titleLevelRequestItems = titleLevelRequestItems;
    }

    public boolean isTitleLevelRequest() {
        return titleLevelRequest;
    }

    public void setTitleLevelRequest(boolean titleLevelRequest) {
        this.titleLevelRequest = titleLevelRequest;
    }

    public boolean isItemLevelRequest() {
        return itemLevelRequest;
    }

    public void setItemLevelRequest(boolean itemLevelRequest) {
        this.itemLevelRequest = itemLevelRequest;
    }

    public boolean isItemEligible() {
        return itemEligible;
    }

    public void setItemEligible(boolean itemEligible) {
        this.itemEligible = itemEligible;
    }

    public boolean isPatronFound() {
        return patronFound;
    }

    public void setPatronFound(boolean patronFound) {
        this.patronFound = patronFound;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getExistingAddressType() {
        return existingAddressType;
    }

    public void setExistingAddressType(String existingAddressType) {
        this.existingAddressType = existingAddressType;
    }

    public boolean isDisplayRequestType() {
        return displayRequestType;
    }

    public void setDisplayRequestType(boolean displayRequestType) {
        this.displayRequestType = displayRequestType;
    }

    public String getAsrRequestType() {
        return asrRequestType;
    }

    public void setAsrRequestType(String asrRequestType) {
        this.asrRequestType = asrRequestType;
    }

    public boolean isAsrRequest() {
        return asrRequest;
    }

    public void setAsrRequest(boolean asrRequest) {
        this.asrRequest = asrRequest;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public boolean isShowOk() {
        return showOk;
    }

    public void setShowOk(boolean showOk) {
        this.showOk = showOk;
    }
}
