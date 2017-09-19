package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.drools.compiler.lang.DRL5Expressions;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.controller.checkin.ClaimsReturnedNoteHandler;
import org.kuali.ole.deliver.controller.checkin.DamagedItemNoteHandler;
import org.kuali.ole.deliver.controller.checkin.MissingPieceNoteHandler;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 8/19/15.
 */
public abstract class CheckoutBaseController extends CircUtilController {

    private static final Logger LOG = Logger.getLogger(CheckoutBaseController.class);

    private OleLoanDocument currentLoanDocument;
    private NoticeInfo noticeInfo;
    private ClaimsReturnedNoteHandler claimsReturnedNoteHandler;
    private DamagedItemNoteHandler damagedItemNoteHandler;
    private MissingPieceNoteHandler missingPieceNoteHandler;
    private DeliverRequestUtil deliverRequestUtil;

    public DeliverRequestUtil getDeliverRequestUtil() {
        if (deliverRequestUtil == null) {
            deliverRequestUtil = new DeliverRequestUtil();
        }
        return deliverRequestUtil;
    }

    public void setDeliverRequestUtil(DeliverRequestUtil deliverRequestUtil) {
        this.deliverRequestUtil = deliverRequestUtil;
    }

    protected abstract void setDataElements(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc);

    public abstract CircForm getCircForm(OLEForm oleForm);

    public abstract ItemRecord getItemRecord(OLEForm oleForm);

    public abstract String getOperatorId(OLEForm oleForm);

    public abstract void setItemRecord(OLEForm oleForm, ItemRecord itemRecord);

    public abstract String getItemBarcode(OLEForm oleForm);

    public abstract void setItemBarcode(OLEForm oleForm, String itemBarcode);

    public abstract OlePatronDocument getCurrentBorrower(OLEForm oleForm);

    public abstract void setItemValidationDone(boolean result, OLEForm oleForm);

    public abstract OleCirculationDesk getSelectedCirculationDesk(OLEForm oleForm);

    public abstract void addLoanDocumentToCurrentSession(OleLoanDocument oleLoanDocument, OLEForm oleForm);

    public abstract boolean processCustomDueDateIfSet(OLEForm oleForm, OleLoanDocument oleLoanDocument);

    public abstract void setDueDateTimeForItemRecord(OLEForm oleForm, Timestamp loanDueDate);

    public abstract void addCurrentLoandDocumentToListofLoandedToPatron(OLEForm oleForm, OleLoanDocument oleLoanDocument);

    public abstract void removeCurrentLoanDocumentFromCurrentSession(OLEForm oleForm, OleLoanDocument oleLoanDocument);

    public abstract void clearCurrentSessionList(OLEForm oleForm);

    public abstract String getCirculationLocationId(OLEForm oleForm);

    public abstract boolean isRecordNoteForDamagedItem(OLEForm oleForm);

    public abstract boolean isRecordNoteForClaimsReturn(OLEForm oleForm);

    public abstract boolean isRecordNoteForMissingPiece(OLEForm oleForm);

    public abstract String getMissingPieceMatchCheck(OLEForm oleForm);

    public DroolsResponse lookupItemAndSaveLoan(OLEForm oleForm) {

        DroolsResponse droolsResponse = null;

        String itemBarcode = getItemBarcode(oleForm);

        ItemRecord itemRecord = null;
        if(!itemBarcode.contains("*")){
            itemRecord = getItemRecordByBarcode(itemBarcode);
        }
        if (StringUtils.isNotBlank(itemBarcode) && null != itemRecord) {
            setItemRecord(oleForm, itemRecord);
            OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, getSelectedCirculationDesk(oleForm));
            setDataElements(oleForm, oleItemRecordForCirc);
            droolsResponse = preValidationForCheckout(oleItemRecordForCirc, droolsResponse, oleForm);
            if (droolsResponse != null) {
                return droolsResponse;
            }
            droolsResponse = processCheckoutAfterPreValidations(oleForm, oleItemRecordForCirc);
        } else {
            droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessage("Invalid item barcode : " + itemBarcode);
            droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
        }
        return droolsResponse;
    }
    public OlePatronDocument getPatronDocumentForItemValidation(OLEForm oleForm) {
        Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed = identifyPatron(oleForm);
        OlePatronDocument patronDocument = getPatronDocument(patronForWhomLoanIsBeingProcessed);
        OlePatronDocument scanedPatron = getCircForm(oleForm).getPatronDocument();
        if(null != patronDocument && null != scanedPatron && !StringUtils.equals(scanedPatron.getOlePatronId(),patronDocument.getOlePatronId())){
            patronDocument.setSelectedProxyForPatron(scanedPatron);
        }
        return patronDocument;
    }

    private DroolsResponse preValidationForCheckout(OleItemRecordForCirc oleItemRecordForCirc, DroolsResponse droolsResponse, OLEForm oleForm) {

        if (StringUtils.isBlank(oleItemRecordForCirc.getItemFullPathLocation())) {
            droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC));
            droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_INFO);
            return droolsResponse;
        }
        if (StringUtils.isBlank(oleItemRecordForCirc.getItemType())) {
            droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessage("Invalid item type for this item : " + oleItemRecordForCirc.getItemRecord().getBarCode());
            droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_INFO);
            return droolsResponse;
        }
        if (null == oleItemRecordForCirc.getItemStatusRecord() || StringUtils.isBlank(oleItemRecordForCirc.getItemStatusRecord().getCode())) {
            droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessage("Invalid item status for this item : " + oleItemRecordForCirc.getItemRecord().getBarCode());
            droolsResponse.addErrorMessageCode(DroolsConstants.GENERAL_INFO);
            return droolsResponse;
        }

        droolsResponse = preValidations(oleItemRecordForCirc.getItemRecord(), oleForm);
        if (droolsResponse != null) return droolsResponse;

        return null;
    }

    private DroolsResponse preValidations(ItemRecord itemRecord, OLEForm oleForm) {
        return preValidationForClaimsReturned(itemRecord, oleForm);
    }

    private DroolsResponse preValidationForClaimsReturned(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForClaimsReturnedNote(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForDamagedItem(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForDamagedItem(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForDamagedItemNote(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForMissingPiece(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForMissingPiece(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        DroolsResponse droolsResponse;
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) droolsExchange.getContext().get("oleItemRecordForCirc");
        String operatorId=(String)droolsExchange.getFromContext("operatorId");
        if(operatorId!=null && getParameterValue(OLENCIPConstants.NCIPAPI_PARAMETER_NAME).contains(operatorId)){
            return processCheckoutAfterPreValidations(oleForm, oleItemRecordForCirc);
        }else {
            droolsResponse = checkForMissingPieceNote(itemRecord);
        }
        if (droolsResponse != null) return droolsResponse;
        return processCheckoutAfterPreValidations(oleForm, oleItemRecordForCirc);
    }

    public DroolsResponse processCheckoutAfterPreValidations(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        DroolsResponse droolsResponse;
        setItemRecord(oleForm, oleItemRecordForCirc.getItemRecord());
        droolsResponse = proceedWithExistingRequstAndLoanChecks(oleForm);
        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.retrieveErrorMessage())) {
            droolsResponse = proceedWithItemValidation(oleForm);
        }
        return droolsResponse;
    }

    private DroolsResponse checkForClaimsReturnedNote(ItemRecord itemRecord) {
        if (itemRecord.getClaimsReturnedFlag() != null && itemRecord.getClaimsReturnedFlag()) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_CLAIMS_RETURNED);
            droolsResponse.addErrorMessage("Claims Returned item has been found!" + OLEConstants.BREAK + "Claims Returned Note: " + itemRecord.getClaimsReturnedNote());
            return droolsResponse;
        }
        return null;
    }

    private DroolsResponse checkForDamagedItemNote(ItemRecord itemRecord) {
        if (itemRecord.isItemDamagedStatus()) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_DAMAGED);
            droolsResponse.addErrorMessage("Item is Damaged. Do you want to continue?" + OLEConstants.BREAK + "Damaged Note: " + itemRecord.getDamagedItemNote());
            return droolsResponse;
        }
        return null;
    }

    private DroolsResponse checkForMissingPieceNote(ItemRecord itemRecord) {
        int noOfPieces = StringUtils.isNotBlank(itemRecord.getNumberOfPieces()) ? Integer.parseInt(itemRecord.getNumberOfPieces()) : 0;
        if (noOfPieces > 1) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_MISSING_PIECE);
            droolsResponse.addErrorMessage(OLEConstants.VERIFY_PIECES + itemRecord.getNumberOfPieces() + ")" + OLEConstants.BREAK + "Total No of Pieces :      "
                    + itemRecord.getNumberOfPieces() + OLEConstants.BREAK +"Description Of Pieces : " + (StringUtils.isNotBlank(itemRecord.getDescriptionOfPieces()) ? itemRecord.getDescriptionOfPieces() : "") + OLEConstants.BREAK + "No of missing Pieces : " + (itemRecord.getMissingPiecesCount() != null ? itemRecord.getMissingPiecesCount() : "0"));
            return droolsResponse;
        }
        return null;
    }

    private Map<OlePatronDocument, OlePatronDocument> identifyPatron(OLEForm oleForm) {
        Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed = new HashMap<>();
        OlePatronDocument currentBorrower = getCurrentBorrower(oleForm);
        if (CollectionUtils.isNotEmpty(currentBorrower.getOleProxyPatronDocumentList())) {
            if (currentBorrower.isCheckoutForSelf()) {
                patronForWhomLoanIsBeingProcessed.put(currentBorrower, null);
            } else {
                List<OleProxyPatronDocument> oleProxyPatronDocumentList = currentBorrower.getOleProxyPatronDocumentList();
                for (Iterator<OleProxyPatronDocument> iterator = oleProxyPatronDocumentList.iterator(); iterator.hasNext(); ) {
                    OleProxyPatronDocument proxyForPatron = iterator.next();
                    OlePatronDocument proxyForPatronDocument = proxyForPatron.getOlePatronDocument();
                    if (proxyForPatronDocument.isCheckoutForSelf()) {
                        patronForWhomLoanIsBeingProcessed.put(currentBorrower, proxyForPatronDocument);
                    }
                }
            }
        } else {
            patronForWhomLoanIsBeingProcessed.put(currentBorrower, null);
        }
        return patronForWhomLoanIsBeingProcessed;
    }

    private void handleNoticeTablePopulation(ItemRecord itemRecord) {
        List<OLEDeliverNotice> deliverNotices = processNotices(currentLoanDocument, itemRecord, null);

        //OJB mapping will take care of persisting notice records once the loan document is saved.
        currentLoanDocument.setDeliverNotices(deliverNotices);
    }

    private void setPatronInfoForLoanDocument(OlePatronDocument olePatronDocument,
                                              OleLoanDocument oleLoanDocument) {
        OlePatronDocument proxyPatron = olePatronDocument.getSelectedProxyForPatron();

        if(null != proxyPatron && StringUtils.isNotBlank(proxyPatron.getOlePatronId())){
            oleLoanDocument.setPatronId(olePatronDocument.getOlePatronId());
            oleLoanDocument.setRealPatronBarcode(proxyPatron.getBarcode());
            oleLoanDocument.setProxyPatronId(proxyPatron.getOlePatronId());
            oleLoanDocument.setRealPatronName(proxyPatron.getPatronName());
        }else{
            oleLoanDocument.setPatronId(olePatronDocument.getOlePatronId());
            oleLoanDocument.setRealPatronBarcode(olePatronDocument.getBarcode());
        }
    }

    private boolean isNotDamaged(ItemRecord itemRecord) {
        Boolean damagedFlag = itemRecord.isItemDamagedStatus();
        return !(damagedFlag == null ? false : damagedFlag.booleanValue());
    }

    private boolean isNotClaimsReturned(ItemRecord itemRecord) {
        Boolean claimsReturnedFlag = itemRecord.getClaimsReturnedFlag();
        return !(claimsReturnedFlag == null ? false : claimsReturnedFlag.booleanValue());
    }

    private void processDueDateBasedOnExpirationDate(OlePatronDocument olePatronDocument, OleLoanDocument
            oleLoanDocument) {
        if (olePatronDocument.getExpirationDate() != null && oleLoanDocument.getLoanDueDate() != null) {
            Timestamp expirationDate = new Timestamp(olePatronDocument.getExpirationDate().getTime());
            if (isPatronExpiringBeforeLoanDue(oleLoanDocument, expirationDate) && isPatronExpirationGreaterThanToday(expirationDate)) {
                oleLoanDocument.setLoanDueDate(expirationDate);
            }
        }
    }

    private boolean isPatronExpirationGreaterThanToday(Timestamp expirationDate) {
        return expirationDate.compareTo(new Date()) > 0;
    }

    private boolean isPatronExpiringBeforeLoanDue(OleLoanDocument oleLoanDocument, Timestamp expirationDate) {
        return expirationDate.compareTo(oleLoanDocument.getLoanDueDate()) < 0;
    }


    public DroolsResponse proceedWithExistingRequstAndLoanChecks(OLEForm oleForm) {
        ItemRecord itemRecord = getItemRecord(oleForm);

        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) oleForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");

        currentLoanDocument = getCurrentLoanDocument(itemRecord.getBarCode());
        if (subsequentRequestExistsForItem(oleItemRecordForCirc, getCurrentBorrower(oleForm))) {
            currentLoanDocument.setRequestPatron(true);
        }

        noticeInfo = new NoticeInfo();

        currentLoanDocument.setOleCirculationDesk(getSelectedCirculationDesk(oleForm));

        List<Object> facts = new ArrayList<>();

        facts.add(oleItemRecordForCirc);

        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);

        facts.add(currentLoanDocument);


        OlePatronDocument patronDocument = getCurrentBorrower(oleForm);
        facts.add(patronDocument);

        facts.add(oleItemRecordForCirc.getOleDeliverRequestBo());

        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        fireRules(facts, null, "request-or-existing-loan-checks");
        oleStopWatch.end();
        LOG.info("Time taken to evaluate rules for existing requests and loan: " + (oleStopWatch.getTotalTime()) + " ms");

        return droolsResponse;
    }


    public DroolsResponse proceedWithItemValidation(OLEForm oleForm) {
        ItemRecord itemRecord = getItemRecord(oleForm);

        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) oleForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");

        currentLoanDocument = getCurrentLoanDocument(itemRecord.getBarCode());
        if (subsequentRequestExistsForItem(oleItemRecordForCirc, getCurrentBorrower(oleForm))) {
            currentLoanDocument.setRequestPatron(true);
        }

        noticeInfo = new NoticeInfo();

        currentLoanDocument.setOleCirculationDesk(getSelectedCirculationDesk(oleForm));
        List<Object> facts = new ArrayList<>();

        facts.add(oleItemRecordForCirc);

        DroolsResponse droolsResponse = new DroolsResponse();
        droolsResponse.setDroolsExchange(oleForm.getDroolsExchange());
        facts.add(droolsResponse);

        facts.add(currentLoanDocument);


        OlePatronDocument patronDocument = getCurrentBorrower(oleForm);
        facts.add(patronDocument);

        facts.add(oleItemRecordForCirc.getOleDeliverRequestBo());

        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        fireRules(facts, null, "checkout validation");
        oleStopWatch.end();
        LOG.info("Time taken to evaluate rules for checkout item: " + (oleStopWatch.getTotalTime()) + " ms");

        setItemValidationDone(true, oleForm);
        setPatronInfoForLoanDocument(patronDocument, currentLoanDocument);
        currentLoanDocument.setCirculationLocationId(getCirculationLocationId(oleForm));
        currentLoanDocument.setItemId(itemRecord.getBarCode());
        currentLoanDocument.setItemUuid(itemRecord.getUniqueIdPrefix() + "-" + itemRecord.getItemId());
        currentLoanDocument.setCreateDate(new Timestamp(System.currentTimeMillis()));
        currentLoanDocument.setLoanOperatorId(getOperatorId(oleForm));

        processDueDateBasedOnExpirationDate(getCurrentBorrower(oleForm), currentLoanDocument);

        if (null == currentLoanDocument.getCirculationPolicyId()) {
            droolsResponse.addErrorMessage("No Circulation Policy found that matches the patron/item combination. Please select a due date manually!");
            droolsResponse.addErrorMessageCode(DroolsConstants.CUSTOM_DUE_DATE_REQUIRED_FLAG);
            currentLoanDocument.setCirculationPolicyId(OLEConstants.NO_CIRC_POLICY_FOUND);
            noticeInfo.setNoticeType(DroolsConstants.REGULAR_LOANS_NOTICE_CONFIG);
            return droolsResponse;
        }

        if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            return droolsResponse;
        }

        proceedToSaveLoan(oleForm);

        return droolsResponse;
    }


    public OleLoanDocument getCurrentLoanDocument(String itemBarcode) {
        HashMap<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("itemId", itemBarcode);
        List<OleLoanDocument> matching = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, parameterMap);
        if (CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return new OleLoanDocument();
    }

    private OlePatronDocument getPatronDocument(Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed) {
        OlePatronDocument proxyPatronDocument = null;
        for (Iterator<OlePatronDocument> iterator = patronForWhomLoanIsBeingProcessed.keySet().iterator(); iterator.hasNext(); ) {
            OlePatronDocument olePatronDocument = iterator.next();
            proxyPatronDocument = patronForWhomLoanIsBeingProcessed.get(olePatronDocument);
            if (null == proxyPatronDocument) {
                return olePatronDocument;
            }
        }
        return proxyPatronDocument;
    }

    public DroolsExchange proceedToSaveLoan(OLEForm oleForm) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ItemRecord itemRecord = getItemRecord(oleForm);
        addLoanDocumentToCurrentSession(currentLoanDocument, oleForm);

        if (processCustomDueDateIfSet(oleForm, currentLoanDocument)) return oleForm.getDroolsExchange();

        if (null != currentLoanDocument.getLoanDueDate()) {
            setDueDateTimeForItemRecord(oleForm, currentLoanDocument.getLoanDueDate());
            handleNoticeTablePopulation(itemRecord);
        }

        OleLoanDocument savedLoanDocument = getBusinessObjectService().save(currentLoanDocument);
        if (null != savedLoanDocument.getLoanId()) {
            updateCirculationHistoryRecord(oleForm);
            processAndSavePatronNotes(oleForm, currentLoanDocument);
            Boolean solrUpdateResults = updateItemInfoInSolr(getUpdateParameters(currentLoanDocument, oleForm), itemRecord.getItemId(), true);
            if (solrUpdateResults) {
                updateLoanDocumentWithItemInformation(itemRecord, currentLoanDocument);
                addCurrentLoandDocumentToListofLoandedToPatron(oleForm, currentLoanDocument);
                LOG.info("Saved Loan with ID: " + savedLoanDocument.getLoanId());
                getDeliverRequestUtil().cancelDocument(getItemBarcode(oleForm), getCurrentBorrower(oleForm), currentLoanDocument, getOperatorId(oleForm));
            } else {
                rollBackSavedLoanRecord(itemRecord.getBarCode());
                removeCurrentLoanDocumentFromCurrentSession(oleForm, currentLoanDocument);
            }
        } else {
            clearCurrentSessionList(oleForm);
        }

        oleStopWatch.end();
        LOG.info("Time taken to process the loan: " + (oleStopWatch.getTotalTime()) + " ms");
        return oleForm.getDroolsExchange();
    }

    private void updateCirculationHistoryRecord(OLEForm oleForm) {
        ItemRecord itemRecord = getItemRecord(oleForm);
        Map<String,String> criteriaMap = new HashMap<>();
        criteriaMap.put("loanId",currentLoanDocument.getLoanId());
        List<OleCirculationHistory> circulationHistoryRecords = (List<OleCirculationHistory>) getBusinessObjectService().findMatching(OleCirculationHistory.class,criteriaMap);
        if(circulationHistoryRecords.size()==0){
            OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(currentLoanDocument.getItemUuid());
            OlePatronDocument olePatronDocument = currentLoanDocument.getOlePatron();
            OleCirculationHistory oleCirculationHistory = new OleCirculationHistory();
            oleCirculationHistory.setLoanId(currentLoanDocument.getLoanId());
            oleCirculationHistory.setCirculationPolicyId(currentLoanDocument.getCirculationPolicyId());
            oleCirculationHistory.setBibAuthor(oleItemSearch.getAuthor());
            oleCirculationHistory.setBibTitle(oleItemSearch.getTitle());
           // oleCirculationHistory.setCheckInDate(currentLoanDocument.getCheckInDate() != null ? oleLoanDocument.getCheckInDate() : new Timestamp(System.currentTimeMillis()));
            oleCirculationHistory.setCreateDate(currentLoanDocument.getCreateDate());
            oleCirculationHistory.setCirculationLocationId(currentLoanDocument.getCirculationLocationId());
            oleCirculationHistory.setDueDate(currentLoanDocument.getLoanDueDate());
            oleCirculationHistory.setItemId(currentLoanDocument.getItemId());
            oleCirculationHistory.setStatisticalCategory(olePatronDocument.getStatisticalCategory());
            oleCirculationHistory.setProxyPatronId(olePatronDocument.getProxyPatronId());
            if (olePatronDocument.getOleBorrowerType() != null) {
                oleCirculationHistory.setPatronTypeId(olePatronDocument.getOleBorrowerType().getBorrowerTypeId());
            }
            oleCirculationHistory.setPatronId(currentLoanDocument.getPatronId());
            oleCirculationHistory.setOleRequestId(currentLoanDocument.getOleRequestId());
            oleCirculationHistory.setItemUuid(currentLoanDocument.getItemUuid());
            oleCirculationHistory.setItemLocation(itemRecord.getLocation());
            oleCirculationHistory.setHoldingsLocation(getHoldingsLocation(itemRecord.getHoldingsId()));
            setAffiliationDetails(oleCirculationHistory);
            oleCirculationHistory.setItemTypeId(itemRecord.getItemTypeId());
            oleCirculationHistory.setTemporaryItemTypeId(itemRecord.getTempItemTypeId());
            oleCirculationHistory.setProxyPatronId(currentLoanDocument.getProxyPatronId());
            oleCirculationHistory.setOperatorCreateId(currentLoanDocument.getLoanOperatorId());
            getBusinessObjectService().save(oleCirculationHistory);
        }
    }

    private String getHoldingsLocation(String holdignsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("holdingsId", holdignsId);
        HoldingsRecord byPrimaryKey = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, map);
        if (null != byPrimaryKey) {
            return byPrimaryKey.getLocation();
        }
        return null;
    }

    protected  void  setAffiliationDetails(OleCirculationHistory oleCirculationHistory){
        if(currentLoanDocument.getOlePatron().getEntity().getDefaultAffiliation()!=null){
            oleCirculationHistory.setAffiliationId(currentLoanDocument.getEntity().getDefaultAffiliation().getAffiliationTypeCode());
        if(currentLoanDocument.getOlePatron().getEntity().getPrimaryEmployment()!=null&& currentLoanDocument.getOlePatron().getEntity().getPrimaryEmployment().getPrimaryDepartmentCode()!=null){
            oleCirculationHistory.setDeptId(currentLoanDocument.getOlePatron().getEntity().getPrimaryEmployment().getPrimaryDepartmentCode());
        }
        }

    }


    private void processAndSavePatronNotes(OLEForm oleForm, OleLoanDocument currentLoanDocument) {

        saveClaimsReturnedNote(oleForm, currentLoanDocument);

        saveDamagedItemNote(oleForm, currentLoanDocument);

        saveMissingPieceNote(oleForm, currentLoanDocument);
    }

    private void saveClaimsReturnedNote(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        if (oleLoanDocument != null) {
            if (isRecordNoteForClaimsReturn(oleForm) && oleLoanDocument.getOlePatron() != null) {
                Map<String, Object> claimsRecordInfo = new HashMap<>();
                claimsRecordInfo.put("itemBarcode", getItemBarcode(oleForm));
                claimsRecordInfo.put("selectedCirculationDesk", getCirculationLocationId(oleForm));
                claimsRecordInfo.put("noteParameter", OLEConstants.CLAIMS_CHECKED_OUT_FLAG);
                getClaimsReturnedNoteHandler().savePatronNoteForClaims(claimsRecordInfo, oleLoanDocument.getOlePatron());
            }
        }
    }

    private void saveDamagedItemNote(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        if (oleLoanDocument != null) {
            if (isRecordNoteForDamagedItem(oleForm) && oleLoanDocument.getOlePatron() != null) {
                Map<String, Object> damagedRecordInfo = new HashMap<>();
                damagedRecordInfo.put("itemBarcode", getItemBarcode(oleForm));
                damagedRecordInfo.put("selectedCirculationDesk", getCirculationLocationId(oleForm));
                damagedRecordInfo.put("noteParameter", OLEConstants.DAMAGED_ITEM_CHECKED_OUT_FLAG);
                getDamagedItemNoteHandler().savePatronNoteForDamaged(damagedRecordInfo, oleLoanDocument.getOlePatron());
            }
        }
    }

    private void saveMissingPieceNote(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        if (oleLoanDocument != null) {
            if(StringUtils.isNotBlank(getMissingPieceMatchCheck(oleForm)) && getMissingPieceMatchCheck(oleForm).equalsIgnoreCase("mismatched")) {
                DroolsExchange droolsExchange = oleForm.getDroolsExchange();
                Map<String, Object> missingPieceRecordInfo = new HashMap<>();
                CircForm circForm = getCircForm(oleForm);
                OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) droolsExchange.getFromContext("oleItemRecordForCirc");
                ItemRecord itemRecord = oleItemRecordForCirc.getItemRecord();
                missingPieceRecordInfo.put("itemBarcode", getItemBarcode(oleForm));
                missingPieceRecordInfo.put("selectedCirculationDesk", getCirculationLocationId(oleForm));
                missingPieceRecordInfo.put("missingPieceCount", circForm.getMissingPieces());
                missingPieceRecordInfo.put("missingPieceNote", circForm.getMismatchedMissingPieceNote());
                missingPieceRecordInfo.put("olePatronId", null != oleLoanDocument.getOlePatron() ? oleLoanDocument.getOlePatron().getOlePatronId() : "");
                missingPieceRecordInfo.put("olePatronBarcode", null != oleLoanDocument.getOlePatron() ? oleLoanDocument.getOlePatron().getBarcode() : "");
                missingPieceRecordInfo.put("noteParameter", OLEConstants.MISSING_PIECE_ITEM_CHECKED_OUT_FLAG);
                getMissingPieceNoteHandler().updateMissingPieceRecord(itemRecord, missingPieceRecordInfo, oleLoanDocument);
                if (isRecordNoteForMissingPiece(oleForm) && oleLoanDocument.getOlePatron() != null) {
                    getMissingPieceNoteHandler().savePatronNoteForMissingPiece(missingPieceRecordInfo, oleLoanDocument.getOlePatron(), itemRecord);
                }
            }
        }
    }

    private Map getUpdateParameters(OleLoanDocument currentLoanDocument, OLEForm oleForm) {
        HashMap parameterValues = new HashMap();
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) oleForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");
        ItemRecord itemRecord = oleItemRecordForCirc.getItemRecord();
        parameterValues.put("patronId", currentLoanDocument.getPatronId());
        parameterValues.put("proxyPatronId", currentLoanDocument.getProxyPatronId());
        parameterValues.put("itemCheckoutDateTime", currentLoanDocument.getCreateDate());
        parameterValues.put("loanDueDate", currentLoanDocument.getLoanDueDate());
        parameterValues.put("numRenewals", currentLoanDocument.getNumberOfRenewals());
        parameterValues.put("itemStatus", OLEConstants.ITEM_STATUS_CHECKEDOUT);
        parameterValues.put("missingPieceItemFlag",itemRecord.isMissingPieceFlag());
        parameterValues.put("missingPieceItemNote",itemRecord.getMissingPieceFlagNote());
        parameterValues.put("missingPieceItemCount",itemRecord.getMissingPiecesCount());
        parameterValues.put("noOfmissingPiece", itemRecord.getNumberOfPieces());
        Timestamp missingPieceItemDate = itemRecord.getMissingPieceEffectiveDate();
        parameterValues.put("missingPieceItemDate",missingPieceItemDate == null ? missingPieceItemDate : convertToString(missingPieceItemDate));
        parameterValues.put("itemMissingPieceItemRecords",prepareMissingPieceHistoryRecords(itemRecord));
        return parameterValues;
    }

    private List<MissingPieceItemRecord> prepareMissingPieceHistoryRecords(ItemRecord itemRecord) {
        SimpleDateFormat dateFormatForMissingItem = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList = itemRecord.getMissingPieceItemRecordList();
        List<MissingPieceItemRecord> itemMissingPieceRecordList = new ArrayList<>();
        for (Iterator<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> iterator = missingPieceItemRecordList.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord itemMissingPieceItemRecord = iterator.next();
            MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
            if (itemMissingPieceItemRecord.getMissingPieceDate() != null && !itemMissingPieceItemRecord.getMissingPieceDate().toString().isEmpty()) {
                Timestamp formatedDateStringForDamagedItem = itemMissingPieceItemRecord.getMissingPieceDate();
                missingPieceItemRecord.setMissingPieceDate(dateFormatForMissingItem.format(formatedDateStringForDamagedItem));
            }
            missingPieceItemRecord.setMissingPieceFlagNote(itemMissingPieceItemRecord.getMissingPieceFlagNote());
            missingPieceItemRecord.setMissingPieceCount(itemMissingPieceItemRecord.getMissingPieceCount());
            missingPieceItemRecord.setPatronBarcode(itemMissingPieceItemRecord.getPatronBarcode());
            missingPieceItemRecord.setPatronId(itemMissingPieceItemRecord.getPatronId());
            missingPieceItemRecord.setOperatorId(itemMissingPieceItemRecord.getOperatorId());
            missingPieceItemRecord.setItemId(itemMissingPieceItemRecord.getItemId());
            itemMissingPieceRecordList.add(missingPieceItemRecord);
        }
        return itemMissingPieceRecordList;
    }

    private boolean subsequentRequestExistsForItem(OleItemRecordForCirc oleItemRecordForCirc, OlePatronDocument currentBorrower) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemId", oleItemRecordForCirc.getItemRecord().getBarCode());
        List<OleDeliverRequestBo> matching = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, map);
        if (CollectionUtils.isNotEmpty(matching)) {
            for (Iterator<OleDeliverRequestBo> iterator = matching.iterator(); iterator.hasNext(); ) {
                OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
                OlePatronDocument olePatronDocument = oleDeliverRequestBo.getOlePatron();
                if(null != olePatronDocument && null != currentBorrower && StringUtils.isNotBlank(currentBorrower.getOlePatronId()) &&
                            !(currentBorrower.getOlePatronId().equals(olePatronDocument.getOlePatronId()))) {
                        return true;
                }
            }
        }
        return false;
    }

    public ClaimsReturnedNoteHandler getClaimsReturnedNoteHandler() {
        if (claimsReturnedNoteHandler == null) {
            claimsReturnedNoteHandler = new ClaimsReturnedNoteHandler();
        }
        return claimsReturnedNoteHandler;
    }

    public DamagedItemNoteHandler getDamagedItemNoteHandler() {
        if (damagedItemNoteHandler == null) {
            damagedItemNoteHandler = new DamagedItemNoteHandler();
        }
        return damagedItemNoteHandler;
    }

    public MissingPieceNoteHandler getMissingPieceNoteHandler() {
        if (missingPieceNoteHandler == null) {
            missingPieceNoteHandler = new MissingPieceNoteHandler();
        }
        return missingPieceNoteHandler;
    }

    public String getParameterValue(String key){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, key);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }
}
