package org.kuali.ole.select.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.form.OLESerialReceivingForm;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.util.KRADConstants;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 6/26/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingDocument extends TransactionalDocumentBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLESerialReceivingDocument.class);
    private String serialReceivingRecordId;
    private Date actionDate;
    private String actionInterval;
    private String bibId;
    private String localId;
    private String boundLocation;
    private String callNumber;
    private String receivingRecordType;
    private String chronologyCaptionLevel1;
    private String chronologyCaptionLevel2;
    private String chronologyCaptionLevel3;
    private String chronologyCaptionLevel4;
    private boolean claim;
    private String claimType;
    private String claimIntervalInformation;
    private String copyNumber;
    private String corporateAuthor;
    private boolean createItem;
    private String enumerationCaptionLevel1;
    private String enumerationCaptionLevel2;
    private String enumerationCaptionLevel3;
    private String enumerationCaptionLevel4;
    private String enumerationCaptionLevel5;
    private String enumerationCaptionLevel6;
    private String generalReceivingNote;
    private String instanceId;
    private String issn;
    private String poId;
    private boolean printLabel;
    private String publisher;
    private String serialReceiptLocation;
    private String serialReceivingRecord;
    private String subscriptionStatus;
    private String title;
    private String treatmentInstructionNote;
    private String unboundLocation;
    private String urgentNote;
    private String vendorId;
    private Timestamp createDate;
    private String operatorId;
    private String machineId;
    private boolean publicDisplay;
    private Timestamp subscriptionStatusDate;
    private boolean specialIssueFlag;
    private String specialIssueNote;
    private boolean treatmentNoteFlag;
    private boolean active = true;
    private String treatmentDialogNote;
    private List<OLESerialReceivingHistory> oleSerialReceivingHistoryList;
    private List<OLESerialReceivingHistory> mainSerialReceivingHistoryList;
    private List<OLESerialReceivingHistory> supplementSerialReceivingHistoryList;
    private List<OLESerialReceivingHistory> indexSerialReceivingHistoryList;
    private String itemUUID;
    private String enumCaption1;
    private String enumCaption2;
    private String enumCaption3;
    private String enumCaption4;
    private String enumCaption5;
    private String enumCaption6;
    private String enumLevel1;
    private String enumLevel2;
    private String enumLevel3;
    private String enumLevel4;
    private String enumLevel5;
    private String enumLevel6;
    private String chronCaption1;
    private String chronCaption2;
    private String chronCaption3;
    private String chronCaption4;
    private String chronLevel1;
    private String chronLevel2;
    private String chronLevel3;
    private String chronLevel4;
    private String serialReceiptHistoryId;
    private boolean returnToSearch;
    private String confirmMessage;
    private String urgentDialogNote;
    private boolean urgentNoteFlag;
    private String vendorAliasName;
    private String vendorName;
    private String searchLimit;
    private String poIdLink;
    private String statusCode;
    private List<OLESerialRelatedPODocument> oleSerialRelatedPODocuments;
    private List<OLESerialReceivingType> oleSerialReceivingTypes;
    private boolean linkPO;

    private String serialPOErrMsg ;

    private String enumCaption1Length;
    private String enumCaption2Length;
    private String enumCaption3Length;
    private String enumCaption4Length;
    private String enumCaption5Length;
    private String enumCaption6Length;
    private String chronCaption1Length;
    private String chronCaption2Length;
    private String chronCaption3Length;
    private String chronCaption4Length;

    private boolean itemCheckFlag=true;

    private String tempInstanceId;

    private boolean claimNoteFlag;
    private boolean claimAgainNoteFlag;
    private String claimDialogNote;

    private boolean validPo = true;
    private boolean availableReceivingRecordType = true;
    private boolean availableSerialReceiptLocation = true;
    private boolean validSubscriptionStatus = true;
    private boolean validBibAndInstance = true;
    private boolean recordAlreadyExist =false;
    private boolean validRecordType = true;
    private boolean validChildRecordType = true;
    private boolean validChildHistoryRecordType = true;

    private String enumerationHistoryCaptionLevel1;
    private String enumerationHistoryCaptionLevel2;
    private String enumerationHistoryCaptionLevel3;
    private String enumerationHistoryCaptionLevel4;
    private String enumerationHistoryCaptionLevel5;
    private String enumerationHistoryCaptionLevel6;

    private String chronologyHistoryCaptionLevel1;
    private String chronologyHistoryCaptionLevel2;
    private String chronologyHistoryCaptionLevel3;
    private String chronologyHistoryCaptionLevel4;

    private boolean displayMainHistory = false;

    private boolean displaySupplementaryHistory = false;

    private boolean displayIndexHistory = false;

    public boolean isDisplayMainHistory() {
        return displayMainHistory;
    }

    public void setDisplayMainHistory(boolean displayMainHistory) {
        this.displayMainHistory = displayMainHistory;
    }

    public boolean isDisplaySupplementaryHistory() {
        return displaySupplementaryHistory;
    }

    public void setDisplaySupplementaryHistory(boolean displaySupplementaryHistory) {
        this.displaySupplementaryHistory = displaySupplementaryHistory;
    }

    public boolean isDisplayIndexHistory() {
        return displayIndexHistory;
    }

    public void setDisplayIndexHistory(boolean displayIndexHistory) {
        this.displayIndexHistory = displayIndexHistory;
    }

    public String getChronologyHistoryCaptionLevel1() {
        return chronologyHistoryCaptionLevel1;
    }

    public void setChronologyHistoryCaptionLevel1(String chronologyHistoryCaptionLevel1) {
        this.chronologyHistoryCaptionLevel1 = chronologyHistoryCaptionLevel1;
    }

    public String getChronologyHistoryCaptionLevel2() {
        return chronologyHistoryCaptionLevel2;
    }

    public void setChronologyHistoryCaptionLevel2(String chronologyHistoryCaptionLevel2) {
        this.chronologyHistoryCaptionLevel2 = chronologyHistoryCaptionLevel2;
    }

    public String getChronologyHistoryCaptionLevel3() {
        return chronologyHistoryCaptionLevel3;
    }

    public void setChronologyHistoryCaptionLevel3(String chronologyHistoryCaptionLevel3) {
        this.chronologyHistoryCaptionLevel3 = chronologyHistoryCaptionLevel3;
    }

    public String getChronologyHistoryCaptionLevel4() {
        return chronologyHistoryCaptionLevel4;
    }

    public void setChronologyHistoryCaptionLevel4(String chronologyHistoryCaptionLevel4) {
        this.chronologyHistoryCaptionLevel4 = chronologyHistoryCaptionLevel4;
    }

    public String getEnumerationHistoryCaptionLevel1() {
        return enumerationHistoryCaptionLevel1;
    }

    public void setEnumerationHistoryCaptionLevel1(String enumerationHistoryCaptionLevel1) {
        this.enumerationHistoryCaptionLevel1 = enumerationHistoryCaptionLevel1;
    }

    public String getEnumerationHistoryCaptionLevel2() {
        return enumerationHistoryCaptionLevel2;
    }

    public void setEnumerationHistoryCaptionLevel2(String enumerationHistoryCaptionLevel2) {
        this.enumerationHistoryCaptionLevel2 = enumerationHistoryCaptionLevel2;
    }

    public String getEnumerationHistoryCaptionLevel3() {
        return enumerationHistoryCaptionLevel3;
    }

    public void setEnumerationHistoryCaptionLevel3(String enumerationHistoryCaptionLevel3) {
        this.enumerationHistoryCaptionLevel3 = enumerationHistoryCaptionLevel3;
    }

    public String getEnumerationHistoryCaptionLevel4() {
        return enumerationHistoryCaptionLevel4;
    }

    public void setEnumerationHistoryCaptionLevel4(String enumerationHistoryCaptionLevel4) {
        this.enumerationHistoryCaptionLevel4 = enumerationHistoryCaptionLevel4;
    }

    public String getEnumerationHistoryCaptionLevel5() {
        return enumerationHistoryCaptionLevel5;
    }

    public void setEnumerationHistoryCaptionLevel5(String enumerationHistoryCaptionLevel5) {
        this.enumerationHistoryCaptionLevel5 = enumerationHistoryCaptionLevel5;
    }

    public String getEnumerationHistoryCaptionLevel6() {
        return enumerationHistoryCaptionLevel6;
    }

    public void setEnumerationHistoryCaptionLevel6(String enumerationHistoryCaptionLevel6) {
        this.enumerationHistoryCaptionLevel6 = enumerationHistoryCaptionLevel6;
    }

    public boolean isRecordAlreadyExist() {
        return recordAlreadyExist;
    }

    public void setRecordAlreadyExist(boolean recordAlreadyExist) {
        this.recordAlreadyExist = recordAlreadyExist;
    }

    public boolean isValidBibAndInstance() {
        return validBibAndInstance;
    }

    public void setValidBibAndInstance(boolean validBibAndInstance) {
        this.validBibAndInstance = validBibAndInstance;
    }

    public boolean isValidPo() {
        return validPo;
    }

    public void setValidPo(boolean validPo) {
        this.validPo = validPo;
    }

    public boolean isAvailableReceivingRecordType() {
        return availableReceivingRecordType;
    }

    public void setAvailableReceivingRecordType(boolean availableReceivingRecordType) {
        this.availableReceivingRecordType = availableReceivingRecordType;
    }

    public boolean isAvailableSerialReceiptLocation() {
        return availableSerialReceiptLocation;
    }

    public void setAvailableSerialReceiptLocation(boolean availableSerialReceiptLocation) {
        this.availableSerialReceiptLocation = availableSerialReceiptLocation;
    }

    public boolean isValidSubscriptionStatus() {
        return validSubscriptionStatus;
    }

    public void setValidSubscriptionStatus(boolean validSubscriptionStatus) {
        this.validSubscriptionStatus = validSubscriptionStatus;
    }

    public boolean isClaimAgainNoteFlag() {
        return claimAgainNoteFlag;
    }

    public void setClaimAgainNoteFlag(boolean claimAgainNoteFlag) {
        this.claimAgainNoteFlag = claimAgainNoteFlag;
    }

    public String getClaimDialogNote() {
        return claimDialogNote;
    }

    public void setClaimDialogNote(String claimDialogNote) {
        this.claimDialogNote = claimDialogNote;
    }

    public boolean isClaimNoteFlag() {
        return claimNoteFlag;
    }

    public void setClaimNoteFlag(boolean claimNoteFlag) {
        this.claimNoteFlag = claimNoteFlag;
    }

    public String getTempInstanceId() {
        return tempInstanceId;
    }

    public void setTempInstanceId(String tempInstanceId) {
        this.tempInstanceId = tempInstanceId;
    }

    public boolean isItemCheckFlag() {
        return itemCheckFlag;
    }

    public void setItemCheckFlag(boolean itemCheckFlag) {
        this.itemCheckFlag = itemCheckFlag;
    }

    public List<OLESerialReceivingType> getOleSerialReceivingTypes() {
        return oleSerialReceivingTypes;
    }

    public void setOleSerialReceivingTypes(List<OLESerialReceivingType> oleSerialReceivingTypes) {
        this.oleSerialReceivingTypes = oleSerialReceivingTypes;
    }

    public String getSerialPOErrMsg() {
        return serialPOErrMsg;
    }

    public void setSerialPOErrMsg(String serialPOErrMsg) {
        this.serialPOErrMsg = serialPOErrMsg;
    }

    public boolean isLinkPO() {
        return linkPO;
    }

    public void setLinkPO(boolean linkPO) {
        this.linkPO = linkPO;
    }

    public List<OLESerialRelatedPODocument> getOleSerialRelatedPODocuments() {
        return oleSerialRelatedPODocuments;
    }

    public void setOleSerialRelatedPODocuments(List<OLESerialRelatedPODocument> oleSerialRelatedPODocuments) {
        this.oleSerialRelatedPODocuments = oleSerialRelatedPODocuments;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPoIdLink() {
        return poIdLink;
    }

    public void setPoIdLink(String poDocNumber) {
        String documentTypeName = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT;
        DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
        String docHandlerUrl = docType.getResolvedDocumentHandlerUrl();
        int endSubString = docHandlerUrl.lastIndexOf("/");
        String serverName = docHandlerUrl.substring(0, endSubString);
        String handler = docHandlerUrl.substring(endSubString + 1, docHandlerUrl.lastIndexOf("?"));
        this.poIdLink = serverName + "/" + KRADConstants.PORTAL_ACTION + "?channelTitle=" + docType.getName() + "&channelUrl=" + handler + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" + KRADConstants.PARAMETER_DOC_ID + "=" + poDocNumber + "&" + KRADConstants.PARAMETER_COMMAND + "=" + KewApiConstants.DOCSEARCH_COMMAND;
    }

    public List<OLESerialReceivingHistory> getMainSerialReceivingHistoryList() {
        return mainSerialReceivingHistoryList;
    }

    public void setMainSerialReceivingHistoryList(List<OLESerialReceivingHistory> mainSerialReceivingHistoryList) {
        this.mainSerialReceivingHistoryList = mainSerialReceivingHistoryList;
    }

    public List<OLESerialReceivingHistory> getSupplementSerialReceivingHistoryList() {
        return supplementSerialReceivingHistoryList;
    }

    public void setSupplementSerialReceivingHistoryList(List<OLESerialReceivingHistory> supplementSerialReceivingHistoryList) {
        this.supplementSerialReceivingHistoryList = supplementSerialReceivingHistoryList;
    }

    public List<OLESerialReceivingHistory> getIndexSerialReceivingHistoryList() {
        return indexSerialReceivingHistoryList;
    }

    public void setIndexSerialReceivingHistoryList(List<OLESerialReceivingHistory> indexSerialReceivingHistoryList) {
        this.indexSerialReceivingHistoryList = indexSerialReceivingHistoryList;
    }

    public String getChronCaption1() {
        return chronCaption1;
    }

    public void setChronCaption1(String chronCaption1) {
        this.chronCaption1 = chronCaption1;
    }

    public String getChronCaption2() {
        return chronCaption2;
    }

    public void setChronCaption2(String chronCaption2) {
        this.chronCaption2 = chronCaption2;
    }

    public String getChronCaption3() {
        return chronCaption3;
    }

    public void setChronCaption3(String chronCaption3) {
        this.chronCaption3 = chronCaption3;
    }

    public String getChronCaption4() {
        return chronCaption4;
    }

    public void setChronCaption4(String chronCaption4) {
        this.chronCaption4 = chronCaption4;
    }

    public String getChronLevel1() {
        return chronLevel1;
    }

    public void setChronLevel1(String chronLevel1) {
        this.chronLevel1 = chronLevel1;
    }

    public String getChronLevel2() {
        return chronLevel2;
    }

    public void setChronLevel2(String chronLevel2) {
        this.chronLevel2 = chronLevel2;
    }

    public String getChronLevel3() {
        return chronLevel3;
    }

    public void setChronLevel3(String chronLevel3) {
        this.chronLevel3 = chronLevel3;
    }

    public String getChronLevel4() {
        return chronLevel4;
    }

    public void setChronLevel4(String chronLevel4) {
        this.chronLevel4 = chronLevel4;
    }

    public String getUrgentDialogNote() {
        return urgentDialogNote;
    }

    public void setUrgentDialogNote(String urgentDialogNote) {
        this.urgentDialogNote = urgentDialogNote;
    }

    public boolean isUrgentNoteFlag() {
        return urgentNoteFlag;
    }

    public void setUrgentNoteFlag(boolean urgentNoteFlag) {
        this.urgentNoteFlag = urgentNoteFlag;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public boolean isReturnToSearch() {
        return returnToSearch;
    }

    public void setReturnToSearch(boolean returnToSearch) {
        this.returnToSearch = returnToSearch;
    }

    public String getSerialReceiptHistoryId() {
        return serialReceiptHistoryId;
    }

    public void setSerialReceiptHistoryId(String serialReceiptHistoryId) {
        this.serialReceiptHistoryId = serialReceiptHistoryId;
    }

    public OLESerialReceivingForm form;

    public OLESerialReceivingForm getForm() {
        return form;
    }

    public void setForm(OLESerialReceivingForm form) {
        this.form = form;
    }

    public String getSerialReceivingRecordId() {
        return serialReceivingRecordId;
    }

    public void setSerialReceivingRecordId(String serialReceivingRecordId) {
        this.serialReceivingRecordId = serialReceivingRecordId;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionInterval() {
        return actionInterval;
    }

    public void setActionInterval(String actionInterval) {
        this.actionInterval = actionInterval;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getBoundLocation() {
        return boundLocation;
    }

    public void setBoundLocation(String boundLocation) {
        this.boundLocation = boundLocation;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getReceivingRecordType() {
        return receivingRecordType;
    }

    public void setReceivingRecordType(String receivingRecordType) {
        this.receivingRecordType = receivingRecordType;
    }

    public String getChronologyCaptionLevel1() {
        return chronologyCaptionLevel1;
    }

    public void setChronologyCaptionLevel1(String chronologyCaptionLevel1) {
        this.chronologyCaptionLevel1 = chronologyCaptionLevel1;
    }

    public String getChronologyCaptionLevel2() {
        return chronologyCaptionLevel2;
    }

    public void setChronologyCaptionLevel2(String chronologyCaptionLevel2) {
        this.chronologyCaptionLevel2 = chronologyCaptionLevel2;
    }

    public String getChronologyCaptionLevel3() {
        return chronologyCaptionLevel3;
    }

    public void setChronologyCaptionLevel3(String chronologyCaptionLevel3) {
        this.chronologyCaptionLevel3 = chronologyCaptionLevel3;
    }

    public String getChronologyCaptionLevel4() {
        return chronologyCaptionLevel4;
    }

    public void setChronologyCaptionLevel4(String chronologyCaptionLevel4) {
        this.chronologyCaptionLevel4 = chronologyCaptionLevel4;
    }

    public boolean isClaim() {
        return claim;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }

    public String getClaimIntervalInformation() {
        return claimIntervalInformation;
    }

    public void setClaimIntervalInformation(String claimIntervalInformation) {
        this.claimIntervalInformation = claimIntervalInformation;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getCorporateAuthor() {
        return corporateAuthor;
    }

    public void setCorporateAuthor(String corporateAuthor) {
        this.corporateAuthor = corporateAuthor;
    }

    public boolean isCreateItem() {
        return createItem;
    }

    public void setCreateItem(boolean createItem) {
        this.createItem = createItem;
    }

    public String getEnumerationCaptionLevel1() {
        return enumerationCaptionLevel1;
    }

    public void setEnumerationCaptionLevel1(String enumerationCaptionLevel1) {
        this.enumerationCaptionLevel1 = enumerationCaptionLevel1;
    }

    public String getEnumerationCaptionLevel2() {
        return enumerationCaptionLevel2;
    }

    public void setEnumerationCaptionLevel2(String enumerationCaptionLevel2) {
        this.enumerationCaptionLevel2 = enumerationCaptionLevel2;
    }

    public String getEnumerationCaptionLevel3() {
        return enumerationCaptionLevel3;
    }

    public void setEnumerationCaptionLevel3(String enumerationCaptionLevel3) {
        this.enumerationCaptionLevel3 = enumerationCaptionLevel3;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public String getEnumerationCaptionLevel4() {
        return enumerationCaptionLevel4;
    }

    public void setEnumerationCaptionLevel4(String enumerationCaptionLevel4) {
        this.enumerationCaptionLevel4 = enumerationCaptionLevel4;
    }

    public String getEnumerationCaptionLevel5() {
        return enumerationCaptionLevel5;
    }

    public void setEnumerationCaptionLevel5(String enumerationCaptionLevel5) {
        this.enumerationCaptionLevel5 = enumerationCaptionLevel5;
    }

    public String getEnumerationCaptionLevel6() {
        return enumerationCaptionLevel6;
    }

    public void setEnumerationCaptionLevel6(String enumerationCaptionLevel6) {
        this.enumerationCaptionLevel6 = enumerationCaptionLevel6;
    }

    public String getGeneralReceivingNote() {
        return generalReceivingNote;
    }

    public void setGeneralReceivingNote(String generalReceivingNote) {
        this.generalReceivingNote = generalReceivingNote;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public boolean isPrintLabel() {
        return printLabel;
    }

    public void setPrintLabel(boolean printLabel) {
        this.printLabel = printLabel;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSerialReceiptLocation() {
        return serialReceiptLocation;
    }

    public void setSerialReceiptLocation(String serialReceiptLocation) {
        this.serialReceiptLocation = serialReceiptLocation;
    }

    public String getSerialReceivingRecord() {
        return serialReceivingRecord;
    }

    public void setSerialReceivingRecord(String serialReceivingRecord) {
        this.serialReceivingRecord = serialReceivingRecord;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTreatmentInstructionNote() {
        return treatmentInstructionNote;
    }

    public void setTreatmentInstructionNote(String treatmentInstructionNote) {
        this.treatmentInstructionNote = treatmentInstructionNote;
    }

    public String getUnboundLocation() {
        return unboundLocation;
    }

    public void setUnboundLocation(String unboundLocation) {
        this.unboundLocation = unboundLocation;
    }

    public String getUrgentNote() {
        return urgentNote;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public void setUrgentNote(String urgentNote) {
        this.urgentNote = urgentNote;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public Timestamp getSubscriptionStatusDate() {
        return subscriptionStatusDate;
    }

    public void setSubscriptionStatusDate(Timestamp subscriptionStatusDate) {
        this.subscriptionStatusDate = subscriptionStatusDate;
    }

    public List<OLESerialReceivingHistory> getOleSerialReceivingHistoryList() {
        return oleSerialReceivingHistoryList;
    }

    public void setOleSerialReceivingHistoryList(List<OLESerialReceivingHistory> oleSerialReceivingHistoryList) {
        this.oleSerialReceivingHistoryList = oleSerialReceivingHistoryList;
    }

    public boolean isSpecialIssueFlag() {
        return specialIssueFlag;
    }

    public void setSpecialIssueFlag(boolean specialIssueFlag) {
        this.specialIssueFlag = specialIssueFlag;
    }

    public String getSpecialIssueNote() {
        return specialIssueNote;
    }

    public void setSpecialIssueNote(String specialIssueNote) {
        this.specialIssueNote = specialIssueNote;
    }

    public boolean isTreatmentNoteFlag() {
        return treatmentNoteFlag;
    }

    public void setTreatmentNoteFlag(boolean treatmentNoteFlag) {
        this.treatmentNoteFlag = treatmentNoteFlag;
    }

    public String getTreatmentDialogNote() {
        return treatmentDialogNote;
    }

    public void setTreatmentDialogNote(String treatmentDialogNote) {
        this.treatmentDialogNote = treatmentDialogNote;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public String getEnumCaption1() {
        return enumCaption1;
    }

    public void setEnumCaption1(String enumCaption1) {
        this.enumCaption1 = enumCaption1;
    }

    public String getEnumCaption2() {
        return enumCaption2;
    }

    public void setEnumCaption2(String enumCaption2) {
        this.enumCaption2 = enumCaption2;
    }

    public String getEnumCaption3() {
        return enumCaption3;
    }

    public void setEnumCaption3(String enumCaption3) {
        this.enumCaption3 = enumCaption3;
    }

    public String getEnumCaption4() {
        return enumCaption4;
    }

    public void setEnumCaption4(String enumCaption4) {
        this.enumCaption4 = enumCaption4;
    }

    public String getEnumCaption5() {
        return enumCaption5;
    }

    public void setEnumCaption5(String enumCaption5) {
        this.enumCaption5 = enumCaption5;
    }

    public String getEnumCaption6() {
        return enumCaption6;
    }

    public void setEnumCaption6(String enumCaption6) {
        this.enumCaption6 = enumCaption6;
    }

    public String getEnumLevel1() {
        return enumLevel1;
    }

    public void setEnumLevel1(String enumLevel1) {
        this.enumLevel1 = enumLevel1;
    }

    public String getEnumLevel2() {
        return enumLevel2;
    }

    public void setEnumLevel2(String enumLevel2) {
        this.enumLevel2 = enumLevel2;
    }

    public String getEnumLevel3() {
        return enumLevel3;
    }

    public void setEnumLevel3(String enumLevel3) {
        this.enumLevel3 = enumLevel3;
    }

    public String getEnumLevel4() {
        return enumLevel4;
    }

    public void setEnumLevel4(String enumLevel4) {
        this.enumLevel4 = enumLevel4;
    }

    public String getEnumLevel5() {
        return enumLevel5;
    }

    public void setEnumLevel5(String enumLevel5) {
        this.enumLevel5 = enumLevel5;
    }

    public String getEnumLevel6() {
        return enumLevel6;
    }

    public void setEnumLevel6(String enumLevel6) {
        this.enumLevel6 = enumLevel6;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(String searchLimit) {
        this.searchLimit = searchLimit;
    }

    public String getEnumCaption1Length() {
        if(StringUtils.isBlank(enumCaption1Length)) {
            if(this.getEnumCaption1() != null) {
                return String.valueOf(this.getEnumCaption1().length());
            }else {
                return "0";
            }
        }
        return enumCaption1Length;
    }

    public void setEnumCaption1Length(String enumCaption1Length) {
        this.enumCaption1Length = enumCaption1Length;
    }

    public String getEnumCaption2Length() {
        if(StringUtils.isBlank(enumCaption2Length)) {
            if(this.getEnumCaption2() != null) {
                return String.valueOf(this.getEnumCaption2().length());
            }else {
                return "0";
            }
        }
        return enumCaption2Length;
    }

    public void setEnumCaption2Length(String enumCaption2Length) {
        this.enumCaption2Length = enumCaption2Length;
    }

    public String getEnumCaption3Length() {
        if(StringUtils.isBlank(enumCaption3Length)) {
            if(this.getEnumCaption3() != null) {
                return String.valueOf(this.getEnumCaption3().length());
            }else {
                return "0";
            }
        }
        return enumCaption3Length;
    }

    public void setEnumCaption3Length(String enumCaption3Length) {
        this.enumCaption3Length = enumCaption3Length;
    }

    public String getEnumCaption4Length() {
        if(StringUtils.isBlank(enumCaption4Length)) {
            if(this.getEnumCaption4() != null) {
                return String.valueOf(this.getEnumCaption4().length());
            }else {
                return "0";
            }
        }
        return enumCaption4Length;
    }

    public void setEnumCaption4Length(String enumCaption4Length) {
        this.enumCaption4Length = enumCaption4Length;
    }

    public String getEnumCaption5Length() {
        if(StringUtils.isBlank(enumCaption5Length)) {
            if(this.getEnumCaption5() != null) {
                return String.valueOf(this.getEnumCaption5().length());
            }else {
                return "0";
            }
        }
        return enumCaption5Length;
    }

    public void setEnumCaption5Length(String enumCaption5Length) {
        this.enumCaption5Length = enumCaption5Length;
    }

    public String getEnumCaption6Length() {
        if(StringUtils.isBlank(enumCaption6Length)) {
            if(this.getEnumCaption6() != null) {
                return String.valueOf(this.getEnumCaption6().length());
            }else {
                return "0";
            }
        }
        return enumCaption6Length;
    }

    public void setEnumCaption6Length(String enumCaption6Length) {
        this.enumCaption6Length = enumCaption6Length;
    }

    public String getChronCaption1Length() {
        if(StringUtils.isBlank(chronCaption1Length)) {
            if(this.getChronCaption1() != null) {
                return String.valueOf(this.getChronCaption1().length());
            }else {
                return "0";
            }
        }
        return chronCaption1Length;
    }

    public void setChronCaption1Length(String chronCaption1Length) {
        this.chronCaption1Length = chronCaption1Length;
    }

    public String getChronCaption2Length() {
        if(StringUtils.isBlank(chronCaption2Length)) {
            if(this.getChronCaption2() != null) {
                return String.valueOf(this.getChronCaption2().length());
            }else {
                return "0";
            }
        }
        return chronCaption2Length;
    }

    public void setChronCaption2Length(String chronCaption2Length) {
        this.chronCaption2Length = chronCaption2Length;
    }

    public String getChronCaption3Length() {
        if(StringUtils.isBlank(chronCaption3Length)) {
            if(this.getChronCaption3() != null) {
                return String.valueOf(this.getChronCaption3().length());
            }else {
                return "0";
            }
        }
        return chronCaption3Length;
    }

    public void setChronCaption3Length(String chronCaption3Length) {
        this.chronCaption3Length = chronCaption3Length;
    }

    public String getChronCaption4Length() {
        if(StringUtils.isBlank(chronCaption4Length)) {
            if(this.getChronCaption4() != null) {
                return String.valueOf(this.getChronCaption4().length());
            }else {
                return "0";
            }
        }
        return chronCaption4Length;
    }

    public void setChronCaption4Length(String chronCaption4Length) {
        this.chronCaption4Length = chronCaption4Length;
    }

    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        LOG.debug("doRouteStatusChange() starting");
        super.doRouteStatusChange(statusChangeEvent);

        try {
            if (this.getDocumentHeader().getWorkflowDocument().isFinal() || this.getDocumentHeader().getWorkflowDocument().isDisapproved()) {
                this.setActive(false);
            }
        } catch (Exception e) {
            LOG.error("Error saving routing data while saving document with id " + getDocumentNumber(), e);
        }
        LOG.debug("doRouteStatusChange() ending");
    }


    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public boolean isValidRecordType() {
        return validRecordType;
    }

    public void setValidRecordType(boolean validRecordType) {
        this.validRecordType = validRecordType;
    }

    public boolean isValidChildRecordType() {
        return validChildRecordType;
    }

    public void setValidChildRecordType(boolean validChildRecordType) {
        this.validChildRecordType = validChildRecordType;
    }

    public boolean isValidChildHistoryRecordType() {
        return validChildHistoryRecordType;
    }

    public void setValidChildHistoryRecordType(boolean validChildHistoryRecordType) {
        this.validChildHistoryRecordType = validChildHistoryRecordType;
    }
}
