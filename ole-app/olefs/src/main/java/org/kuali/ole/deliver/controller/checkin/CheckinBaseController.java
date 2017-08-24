package org.kuali.ole.deliver.controller.checkin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionDate;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionPeriod;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionPeriodWeek;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.notice.executors.HoldExpirationNoticesExecutor;
import org.kuali.ole.deliver.notice.executors.MissingPieceNoticesExecutor;
import org.kuali.ole.deliver.notice.executors.OnHoldNoticesExecutor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.deliver.util.printSlip.*;
import org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pvsubrah on 7/22/15.
 */
public abstract class CheckinBaseController extends CircUtilController {
    private static final Logger LOG = Logger.getLogger(CheckinBaseController.class);

    protected abstract void setDataElements(OLEForm oleForm, ItemRecord itemRecord, OleLoanDocument loanDocument);

    public abstract CheckinForm getCheckinForm(OLEForm oleForm);

    public abstract String getItemBarcode(OLEForm oleForm);

    public abstract String getSelectedCirculationDesk(OLEForm oleForm);

    public abstract Date getCustomDueDateMap(OLEForm oleForm);

    public abstract String getCustomDueDateTime(OLEForm oleForm);

    public abstract void setCheckedInItem(CheckedInItem checkedInItem, OLEForm oleForm);

    public abstract CheckedInItem getCheckedInItem(OLEForm oleForm);

    public abstract void addCheckedInItemToCheckedInItemList(CheckedInItem checkedInItem, OLEForm oleForm);

    public abstract boolean isRecordNoteForDamagedItem(OLEForm oleForm);

    public abstract boolean isRecordNoteForClaimsReturn(OLEForm oleForm);

    public abstract boolean isRecordNoteForMissingPiece(OLEForm oleForm);

    public abstract String getMissingPieceMatchCheck(OLEForm oleForm);

    public abstract void setNoOfPieces(OLEForm oleForm, String numberOfPieces);

    public abstract OleLoanDocument getOleLoanDocument(OLEForm oleForm);

    public abstract DroolsExchange getDroolsExchange(OLEForm oleForm);

    public abstract String getOperatorId(OLEForm oleForm);

    public abstract boolean isItemFoundInLibrary(OLEForm oleForm);

    private OnHoldCourtesyNoticeUtil onHoldCourtesyNoticeUtil;
    private DamagedItemNoteHandler damagedItemNoteHandler;
    private ClaimsReturnedNoteHandler claimsReturnedNoteHandler;

    private MissingPieceNoteHandler missingPieceNoteHandler;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private OleLoanDocumentDaoOjb loanDaoOjb;
    private ParameterValueResolver parameterValueResolver;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    public OnHoldCourtesyNoticeUtil getOnHoldCourtesyNoticeUtil() {
        if (null == onHoldCourtesyNoticeUtil) {
            onHoldCourtesyNoticeUtil = new OnHoldCourtesyNoticeUtil();
        }
        return onHoldCourtesyNoticeUtil;
    }

    public void setOnHoldCourtesyNoticeUtil(OnHoldCourtesyNoticeUtil onHoldCourtesyNoticeUtil) {
        this.onHoldCourtesyNoticeUtil = onHoldCourtesyNoticeUtil;
    }

    public DamagedItemNoteHandler getDamagedItemNoteHandler() {
        if (damagedItemNoteHandler == null) {
            damagedItemNoteHandler = new DamagedItemNoteHandler();
        }
        return damagedItemNoteHandler;
    }

    public void setDamagedItemNoteHandler(DamagedItemNoteHandler damagedItemNoteHandler) {
        this.damagedItemNoteHandler = damagedItemNoteHandler;
    }

    public ClaimsReturnedNoteHandler getClaimsReturnedNoteHandler() {
        if (claimsReturnedNoteHandler == null) {
            claimsReturnedNoteHandler = new ClaimsReturnedNoteHandler();
        }
        return claimsReturnedNoteHandler;
    }

    public void setClaimsReturnedNoteHandler(ClaimsReturnedNoteHandler claimsReturnedNoteHandler) {
        this.claimsReturnedNoteHandler = claimsReturnedNoteHandler;
    }

    public MissingPieceNoteHandler getMissingPieceNoteHandler() {
        if (missingPieceNoteHandler == null) {
            missingPieceNoteHandler = new MissingPieceNoteHandler();
        }
        return missingPieceNoteHandler;
    }

    public void setMissingPieceNoteHandler(MissingPieceNoteHandler missingPieceNoteHandler) {
        this.missingPieceNoteHandler = missingPieceNoteHandler;
    }

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    public DroolsResponse checkin(OLEForm oleForm) {
        String itemBarcode = getItemBarcode(oleForm);

        ItemRecord itemRecord = null;
        if(!itemBarcode.contains("*")){
            itemRecord = getItemRecordByBarcode(itemBarcode);
        }

        DroolsResponse droolsResponse = null;
        if (null != itemRecord) {
            OleLoanDocument loanDocument = getLoanDocument(itemBarcode);
            try {
                loanDocument = getOleDeliverRequestDocumentHelperService().getLoanDocumentWithItemInfo(loanDocument, false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            setDataElements(oleForm, itemRecord, loanDocument);
            droolsResponse = preValidations(itemRecord, oleForm);
            if (null != droolsResponse) {
                return droolsResponse;
            }
            droolsResponse = processCheckinAfterPreValidation(itemRecord, oleForm, loanDocument);

        } else {
            droolsResponse = new DroolsResponse();
            droolsResponse.getErrorMessage().setErrorMessage("Invalid item barcode : " + itemBarcode);
            droolsResponse.getErrorMessage().setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
        }

        transferExchangeInfoFromDroolsResponseToCheckinForm(oleForm, droolsResponse);

        return droolsResponse;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    private void transferExchangeInfoFromDroolsResponseToCheckinForm(OLEForm oleForm, DroolsResponse droolsResponse) {
        for (Iterator<String> iterator = droolsResponse.getDroolsExchange().getContext().keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            getDroolsExchange(oleForm).addToContext(key, droolsResponse.getDroolsExchange().getContext().get(key));
        }
    }

    public DroolsResponse preValidations(ItemRecord itemRecord, OLEForm oleForm) {
        return preValidationForLostItemWithReplacementBill(itemRecord, oleForm);
    }

   /* public DroolsResponse preValidationForLost(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForLostItem(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForLostItemWithReplacementBill(itemRecord, oleForm);
    }*/

    public DroolsResponse preValidationForLostItemWithReplacementBill(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForLostItemWithReplacemntBill(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForDamaged(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForDamaged(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForDamagedItem(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForClaimsReturned(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForClaimsReturned(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForClaimsReturnedNote(itemRecord);
        if (droolsResponse != null) return droolsResponse;
        return preValidationForMissingPiece(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForMissingPiece(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        droolsResponse = checkForMissingPieceNote(itemRecord);
        setNoOfPieces(oleForm, itemRecord.getNumberOfPieces());
        if (droolsResponse != null) return droolsResponse;
        return preValidationForCheckinRequestExists(itemRecord, oleForm);
    }

    public DroolsResponse preValidationForCheckinRequestExists(ItemRecord itemRecord, OLEForm oleForm) {
        DroolsResponse droolsResponse;
        handleHoldExpiredRequest(itemRecord, oleForm);
        droolsResponse = processIfCheckinRequestExist(itemRecord, oleForm);
        if (droolsResponse != null) return droolsResponse;
        OleLoanDocument oleLoanDocument = getOleLoanDocument(oleForm);
        return processCheckinAfterPreValidation(itemRecord, oleForm, oleLoanDocument);
    }


    public DroolsResponse processCheckinAfterPreValidation(ItemRecord itemRecord, OLEForm oleForm, OleLoanDocument loanDocument) {
        DroolsResponse droolsResponse;

        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(getSelectedCirculationDesk(oleForm));
        OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, oleCirculationDesk);
        oleItemRecordForCirc.setOperatorCircLocation(oleCirculationDesk);
        oleItemRecordForCirc.setCheckinLocation(oleCirculationDesk);


        oleItemRecordForCirc.setItemRecord(itemRecord);
        /*
            oleItemSearch record is fetched to get all the bib/holdings/item information
         */
        OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(oleItemRecordForCirc.getItemUUID());

        boolean updateRecentlyReturnedTable = false;
        droolsResponse = new DroolsResponse();
        droolsResponse.setDroolsExchange(oleForm.getDroolsExchange());

        OlePatronDocument olePatronDocument = null;
        if (null != loanDocument) {
            loanDocument.setItemFullLocation(oleItemSearch.getShelvingLocation());
            Boolean claimsReturnedFlag = itemRecord.getClaimsReturnedFlag();
            olePatronDocument = loanDocument.getOlePatron();
            loanDocument.setOleDeliverRequestBo(oleItemRecordForCirc.getOleDeliverRequestBo());
            ArrayList<Object> facts = new ArrayList<>();
            facts.add(droolsResponse);
            facts.add(oleItemRecordForCirc);
            facts.add(oleCirculationDesk);

            fireRules(facts, null, "checkin-validation-for-loan");

            if (!ruleMatched(droolsResponse)) {
                droolsResponse.getErrorMessage().setErrorMessage("No checkin rule found!");
                droolsResponse.getErrorMessage().setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
                return droolsResponse;
            }

            facts.clear();

            ItemFineRate itemFineRate = fireFineRules(loanDocument, oleItemRecordForCirc, olePatronDocument);
            loanDocument.setItemFineRate(itemFineRate);

            String lostProcessingFeeParameter = getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.LOST_PROCESSING_FEE);
            Double lostProcessingFee = new Double(0) ;
            if (StringUtils.isNotBlank(lostProcessingFeeParameter)) {
                lostProcessingFee = Double.valueOf(lostProcessingFeeParameter);
            }
            try {
                Timestamp checkinDate = processDateAndTimeForAlterDueDate(getCustomDueDateMap(oleForm), getCustomDueDateTime(oleForm));
                loanDocument.setCheckInDate(checkinDate);
                long currentTime = System.currentTimeMillis();
                    if(checkinDate.getTime()+(60*1000)<currentTime){
                    loanDocument.setOverrideCheckInTime(true);
                }
                if(loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID)){
                    oleItemRecordForCirc.setItemStatusToBeUpdatedTo(OLEConstants.ITEM_STATUS_LOST_AND_PAID);
                }
                updateLoanDocument(loanDocument, oleItemSearch, itemRecord);
                saveMissingPieceNote(oleForm);
                saveClaimsReturnedNote(oleForm);
                saveDamagedItemNote(oleForm);
                oleItemRecordForCirc.setItemRecord((ItemRecord)getDroolsExchange(oleForm).getContext().get("itemRecord"));
                updateItemStatusAndCircCount(oleItemRecordForCirc);
                emailToPatronForOnHoldStatus();
                String billNumber = null;
                if (!(claimsReturnedFlag && isItemFoundInLibrary(oleForm)) && !(loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID))){
                    billNumber = generateBillPayment(getSelectedCirculationDesk(oleForm), loanDocument, checkinDate, loanDocument.getLoanDueDate(),false);
                } else if(loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST_AND_PAID)){
                    if(lostProcessingFee.equals(new Double(0))){
                        billNumber = generateBillPayment(getSelectedCirculationDesk(oleForm), loanDocument, checkinDate, loanDocument.getLoanDueDate(),false);
                    }else{
                        generateProcessingBill(loanDocument, lostProcessingFee, loanDocument.getLoanDueDate(),false);
                    }
                }
                if (claimsReturnedFlag && oleItemRecordForCirc.getItemStatusRecord() != null && OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode())) {
                    processClaimsReturnedAndLostItem(oleForm, loanDocument, checkinDate);
                } else if (claimsReturnedFlag) {
                    processClaimsReturnedItem(oleForm, loanDocument, billNumber);
                } else if (oleItemRecordForCirc.getItemStatusRecord() != null && (OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode()) || (OLEConstants.ITEM_STATUS_LOST_AND_PAID.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode())))) {
                    processLostItem(getOperatorId(oleForm), loanDocument, false);
                }
            } catch (Exception e) {
                LOG.error(e.getStackTrace());
            }

            if (null != oleItemRecordForCirc.getItemStatusToBeUpdatedTo() && oleItemRecordForCirc.getItemStatusToBeUpdatedTo().equals(OLEConstants.RECENTLY_RETURNED)) {
                updateRecentlyReturnedTable = true;
            }

        } else {
            ArrayList<Object> facts = new ArrayList<>();
            facts.add(oleCirculationDesk);
            facts.add(oleItemRecordForCirc);
            facts.add(droolsResponse);

            fireRules(facts, null, "checkin-validation-for-no-loan");

            if (null != oleItemRecordForCirc.getItemStatusToBeUpdatedTo()) {
                saveMissingPieceNote(oleForm);
                updateItemStatusAndCircCount(oleItemRecordForCirc);
                if (oleItemRecordForCirc.getItemStatusToBeUpdatedTo().equals(OLEConstants.RECENTLY_RETURNED)) {
                    updateRecentlyReturnedTable = true;
                }
            } else {
                if (!ruleMatched(droolsResponse)) {
                    droolsResponse.getErrorMessage().setErrorMessage("No checkin rule found!");
                    droolsResponse.getErrorMessage().setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
                    return droolsResponse;
                }
            }
        }

        transferExchangeInfoFromDroolsResponseToCheckinForm(oleForm, droolsResponse);

        handleRecentlyReturnedRecord(oleItemRecordForCirc, updateRecentlyReturnedTable);

        updateCheckedInItemList(oleForm, itemRecord.getCheckInNote(), oleItemRecordForCirc, oleItemSearch, olePatronDocument);

        handleOnHoldRequestIfExists(oleItemRecordForCirc);

        handleMissingPieceIfExists(oleForm, loanDocument, oleItemSearch);

        updateReturnHistory(oleItemRecordForCirc, oleForm);

        handleCheckinNote(oleForm, oleItemRecordForCirc);

        handleAutoCheckout(oleForm, oleItemRecordForCirc);

        return droolsResponse;
    }

    private void processClaimsReturnedAndLostItem(OLEForm oleForm, OleLoanDocument loanDocument, Timestamp checkinDate) {
        if (isItemFoundInLibrary(oleForm)) {
            updateFees(loanDocument, getOperatorId(oleForm), Arrays.asList(OLEConstants.FEE_TYPE_CODE_REPL_FEE, OLEConstants.LOST_ITEM_PRCS_FEE), "Claimed item was found by staff on ", false);
            boolean isNotifyClaimsReturnedToPatron = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE,
                    OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTIFY_CLAIMS_RETURNED_TO_PATRON);
            if (isNotifyClaimsReturnedToPatron) {
                sendClaimReturnedNotice(loanDocument, OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_TITLE, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_CONTENT,false);
            }
        } else {
            processLostItem(getOperatorId(oleForm), loanDocument, false);
        }
    }

    public void claimsReturnedCheckInProcess(OleLoanDocument loanDocument, OLEForm oleForm, String itemStatus){
        if (StringUtils.isNotBlank(loanDocument.getItemId())) {
            ItemRecord itemRecord = getItemRecordByBarcode(loanDocument.getItemId());
            if (itemRecord != null) {
                itemRecord.setClaimsReturnedFlag(false);
                itemRecord.setClaimsReturnedNote(null);
                itemRecord.setClaimsReturnedFlagCreateDate(null);
                OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(getSelectedCirculationDesk(oleForm));
                OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, oleCirculationDesk);
                oleItemRecordForCirc.setOperatorCircLocation(oleCirculationDesk);
                oleItemRecordForCirc.setCheckinLocation(oleCirculationDesk);

                OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(oleItemRecordForCirc.getItemUUID());
                ItemNoteRecord itemNoteRecord = new ItemNoteRecord();
                itemNoteRecord.setNote("Claimed Item was forgiven by staff on " + new Date());
                itemNoteRecord.setType("nonPublic");
                if (CollectionUtils.isEmpty(itemRecord.getItemNoteRecords())) {
                    itemRecord.setItemNoteRecords(Arrays.asList(itemNoteRecord));
                } else {
                    itemRecord.getItemNoteRecords().add(itemNoteRecord);
                }
                try {
                    updateLoanDocument(loanDocument, oleItemSearch, itemRecord);
                    oleItemRecordForCirc.setItemRecord(itemRecord);
                    oleItemRecordForCirc.setItemStatusToBeUpdatedTo(itemStatus);
                    updateItemStatusAndCircCount(oleItemRecordForCirc);
                    updateReturnHistory(oleItemRecordForCirc, oleForm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processClaimsReturnedItem(OLEForm oleForm, OleLoanDocument loanDocument, String billNumber) {
        boolean isNotifyClaimsReturnedToPatron = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE,
                OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTIFY_CLAIMS_RETURNED_TO_PATRON);
        if (isItemFoundInLibrary(oleForm)) {
            updateFees(loanDocument, getOperatorId(oleForm), Arrays.asList(OLEConstants.FEE_TYPE_CODE_REPL_FEE, OLEConstants.LOST_ITEM_PRCS_FEE), "Claimed item was found by staff on ", false);
            if (isNotifyClaimsReturnedToPatron) {
                sendClaimReturnedNotice(loanDocument, OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_TITLE, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_CONTENT,false);
            }
        } else {
            updatePaymentStatusToOutstanding(loanDocument);
            if (isNotifyClaimsReturnedToPatron) {
                if (StringUtils.isBlank(billNumber)) {
                    sendClaimReturnedNotice(loanDocument, OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_TITLE,
                            OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_CONTENT,false);
                } else {
                    sendClaimReturnedNotice(loanDocument, OLEConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE_TITLE,
                            OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE_CONTENT,false);
                }
            }
        }
    }

    public DroolsResponse locationPopupMessageCheck(OLEForm oleForm) {
        DroolsResponse droolsResponse = null;
        CheckinForm checkinForm = getCheckinForm(oleForm);
        if(null != checkinForm) {
            OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(getSelectedCirculationDesk(oleForm));
            OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) checkinForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");
            String itemFullLocationPath = null != oleItemRecordForCirc ? oleItemRecordForCirc.getItemFullPathLocation() : "";
            if(null != oleCirculationDesk && StringUtils.isNotBlank(itemFullLocationPath)) {
                List<OleCirculationDeskLocation> oleCirculationDeskLocationList = oleCirculationDesk.getOleCirculationDeskLocations();
                for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocationList) {
                    String circDeskFullLocationPath = null != oleCirculationDeskLocation.getLocation() ? oleCirculationDeskLocation.getLocation().getFullLocationPath() : "";
                    if(StringUtils.isBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()) &&
                            itemFullLocationPath.equals(circDeskFullLocationPath)) {
                        if(oleCirculationDeskLocation.isLocationPopup() &&
                                StringUtils.isNotBlank(oleCirculationDeskLocation.getLocationPopupMsg())) {
                            droolsResponse = new DroolsResponse();
                            checkinForm.setLocationPopupMsg(oleCirculationDeskLocation.getLocationPopupMsg());
                        }
                        break;
                    }
                }
            }
        }
        return droolsResponse;
    }

    private DroolsResponse processIfCheckinRequestExist(ItemRecord itemRecord, OLEForm oleForm) {
        OleDeliverRequestBo prioritizedRequest = getLoanDaoOjb().getPrioritizedRequest(itemRecord.getBarCode());
        if (null != prioritizedRequest) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM);
            droolsResponse.addErrorMessage("Request already exist for this item. <br/><br/> Do you want to checkin this item?.");
            return droolsResponse;
        }
        return null;
    }

    private void handleHoldExpiredRequest(ItemRecord itemRecord, OLEForm oleForm) {
        if (itemRecord != null && itemRecord.getItemStatusRecord() != null && StringUtils.isNotBlank(itemRecord.getItemStatusRecord().getCode())
                && itemRecord.getItemStatusRecord().getCode().equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD)) {
            OleDeliverRequestBo holdExpiredRequest = getLoanDaoOjb().getHoldExpiredRequests(itemRecord.getBarCode());
            if (holdExpiredRequest != null) {
                OLEDeliverNotice onHoldExpiredNotice = getLoanDaoOjb().getOnHoldExpiredNotice(holdExpiredRequest.getRequestId());
                if (onHoldExpiredNotice != null) {
                    Map requestMap = new HashMap();
                    requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, onHoldExpiredNotice.getNoticeContentConfigName());
                    requestMap.put(OLEConstants.DELIVER_NOTICES, Arrays.asList(onHoldExpiredNotice));
                    requestMap.put(OLEConstants.OPTR_ID, getOperatorId(oleForm));
                    HoldExpirationNoticesExecutor onHoldNoticesExecutor = new HoldExpirationNoticesExecutor(requestMap);
                    onHoldNoticesExecutor.run();
                }
            }
        }
    }

    private void saveMissingPieceNote(OLEForm oleForm) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        ItemRecord itemRecord = (ItemRecord) droolsExchange.getContext().get("itemRecord");
        OlePatronDocument olePatronDocument = (OlePatronDocument) droolsExchange.getContext().get("olePatronDocument");
        if (StringUtils.isNotBlank(getMissingPieceMatchCheck(oleForm)) && getMissingPieceMatchCheck(oleForm).equalsIgnoreCase("mismatched")) {
            Map<String, Object> missingPieceRecordInfo = new HashMap<>();
            CheckinForm checkinForm = getCheckinForm(oleForm);
            missingPieceRecordInfo.put("itemBarcode", checkinForm.getItemBarcode());
            missingPieceRecordInfo.put("customDate", checkinForm.getCustomDueDateMap());
            missingPieceRecordInfo.put("customTime", checkinForm.getCustomDueDateTime());
            missingPieceRecordInfo.put("selectedCirculationDesk", checkinForm.getSelectedCirculationDesk());
            missingPieceRecordInfo.put("missingPieceCount", checkinForm.getMissingPieceCount());
            missingPieceRecordInfo.put("missingPieceNote", checkinForm.getMissingPieceNote());
            missingPieceRecordInfo.put("olePatronId", null != olePatronDocument ? olePatronDocument.getOlePatronId() : "");
            missingPieceRecordInfo.put("olePatronBarcode", null != olePatronDocument ? olePatronDocument.getBarcode() : "");
            missingPieceRecordInfo.put("noteParameter", OLEConstants.MISSING_PIECE_ITEM_CHECKED_IN_FLAG);
            getMissingPieceNoteHandler().updateMissingPieceRecord(itemRecord, missingPieceRecordInfo, null);
            if (isRecordNoteForMissingPiece(oleForm)) {
                if (olePatronDocument != null) {
                    getMissingPieceNoteHandler().savePatronNoteForMissingPiece(missingPieceRecordInfo, olePatronDocument, itemRecord);
                }
            }
        }
    }

    private DroolsResponse checkForMissingPieceNote(ItemRecord itemRecord) {
        int noOfPieces = StringUtils.isNotBlank(itemRecord.getNumberOfPieces()) ? Integer.parseInt(itemRecord.getNumberOfPieces()) : 0;
        if (noOfPieces > 1) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_MISSING_PIECE);
            droolsResponse.addErrorMessage(OLEConstants.VERIFY_PIECES + itemRecord.getNumberOfPieces() + OLEConstants.PIECES_RETURNED + OLEConstants.BREAK + "Total No of Pieces :      "
                    + itemRecord.getNumberOfPieces() + OLEConstants.BREAK + "Description Of Pieces : " + (StringUtils.isNotBlank(itemRecord.getDescriptionOfPieces()) ? itemRecord.getDescriptionOfPieces() : "") + OLEConstants.BREAK + "No of missing Pieces : " + (itemRecord.getMissingPiecesCount() != null ? itemRecord.getMissingPiecesCount() : "0"));
            return droolsResponse;
        }
        return null;
    }

    private void saveClaimsReturnedNote(OLEForm oleForm) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        ItemRecord itemRecord = (ItemRecord) droolsExchange.getContext().get("itemRecord");
        OlePatronDocument olePatronDocument = (OlePatronDocument) droolsExchange.getContext().get("olePatronDocument");
        if (isRecordNoteForClaimsReturn(oleForm) && olePatronDocument != null) {
            Map<String, Object> claimsRecordInfo = new HashMap<>();
            CheckinForm checkinForm = getCheckinForm(oleForm);
            claimsRecordInfo.put("itemBarcode", checkinForm.getItemBarcode());
            claimsRecordInfo.put("customDate", checkinForm.getCustomDueDateMap());
            claimsRecordInfo.put("customTime", checkinForm.getCustomDueDateTime());
            claimsRecordInfo.put("selectedCirculationDesk", checkinForm.getSelectedCirculationDesk());
            claimsRecordInfo.put("noteParameter", OLEConstants.CLAIMS_CHECKED_IN_FLAG);
            if(isItemFoundInLibrary(oleForm)) {
                claimsRecordInfo.put("customNote",OLEConstants.CLAIMS_RETURN_FOUND_IN_LIB);
            } else {
                claimsRecordInfo.put("customNote",OLEConstants.CLAIMS_RETURN_NOT_FOUND_IN_LIB);
            }
            getClaimsReturnedNoteHandler().savePatronNoteForClaims(claimsRecordInfo, olePatronDocument);
        }

        itemRecord.setClaimsReturnedFlag(false);
        itemRecord.setClaimsReturnedNote(null);
        itemRecord.setClaimsReturnedFlagCreateDate(null);
    }

    private DroolsResponse checkForClaimsReturnedNote(ItemRecord itemRecord) {
        if (itemRecord.getClaimsReturnedFlag()!=null && itemRecord.getClaimsReturnedFlag()) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_CLAIMS_RETURNED);
            droolsResponse.addErrorMessage("Claims Returned item has been found!" + OLEConstants.BREAK + "Claims Returned Note: " + itemRecord.getClaimsReturnedNote());
            return droolsResponse;
        }
        return null;
    }

    private void saveDamagedItemNote(OLEForm oleForm) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        OlePatronDocument olePatronDocument = (OlePatronDocument) droolsExchange.getContext().get("olePatronDocument");
        if (isRecordNoteForDamagedItem(oleForm) && olePatronDocument != null) {
            Map<String, Object> damagedRecordInfo = new HashMap<>();
            CheckinForm checkinForm = getCheckinForm(oleForm);
            damagedRecordInfo.put("itemBarcode", checkinForm.getItemBarcode());
            damagedRecordInfo.put("customDate", checkinForm.getCustomDueDateMap());
            damagedRecordInfo.put("customTime", checkinForm.getCustomDueDateTime());
            damagedRecordInfo.put("selectedCirculationDesk", checkinForm.getSelectedCirculationDesk());
            damagedRecordInfo.put("noteParameter", OLEConstants.DAMAGED_ITEM_CHECKED_IN_FLAG);
            getDamagedItemNoteHandler().savePatronNoteForDamaged(damagedRecordInfo, olePatronDocument);
        }
    }

    private DroolsResponse checkForDamagedItem(ItemRecord itemRecord) {
        if (itemRecord.isItemDamagedStatus()) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_DAMAGED);
            droolsResponse.addErrorMessage("Item is Damaged. Do you want to continue?" + OLEConstants.BREAK + "Damaged Note: " + itemRecord.getDamagedItemNote());
            return droolsResponse;
        }
        return null;
    }

   /* private DroolsResponse checkForLostItem(ItemRecord itemRecord) {
        String itemStatus = itemRecord.getItemStatusRecord().getCode();
        if (StringUtils.isNotBlank(itemStatus) && itemStatus.equals(OLEConstants.ITEM_STATUS_LOST)) {
            DroolsResponse droolsResponse = new DroolsResponse();
            droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_LOST);
            droolsResponse.addErrorMessage("Item status is 'lost', return item?");
            return droolsResponse;
        }
        return null;
    }
*/
    private DroolsResponse checkForLostItemWithReplacemntBill(ItemRecord itemRecord) {

        String itemStatus = itemRecord.getItemStatusRecord().getCode();
        if (StringUtils.isNotBlank(itemStatus) && itemStatus.equals(OLEConstants.ITEM_STATUS_LOST)) {
            Map feeTypeMap = new HashMap();
            feeTypeMap.put("itemBarcode",itemRecord.getBarCode());
            feeTypeMap.put("oleFeeType.feeTypeCode",OLEConstants.REPLACMENT_FEE);
            List<FeeType> feeTypes = (List<FeeType>)getBusinessObjectService().findMatching(FeeType.class,feeTypeMap);
            if(feeTypes.size() > 0) {
                DroolsResponse droolsResponse = new DroolsResponse();
                droolsResponse.addErrorMessageCode(DroolsConstants.ITEM_LOST_REPLACEMENT_BILL);
                droolsResponse.addErrorMessage("The selected item is already billed as Lost");
                return droolsResponse;
            }
        }
        return null;
    }


    public void updateReturnHistory(OleItemRecordForCirc oleItemRecordForCirc, OLEForm oleForm) {
        if(oleItemRecordForCirc != null) {
            getDroolsExchange(oleForm).addToContext("oleItemRecordForCirc", oleItemRecordForCirc);
            String itemStatusToBeUpdatedTo = oleItemRecordForCirc.getItemStatusToBeUpdatedTo();
            if((itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT) || itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD) || itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN))
                    && StringUtils.isNotBlank(oleItemRecordForCirc.getRouteToLocation())) {
                saveReturnHistoryRecord(oleForm, oleItemRecordForCirc);
            } else if(!(itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT)) && !(itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD)) && !(itemStatusToBeUpdatedTo.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN))) {
                oleItemRecordForCirc.setRouteToLocation("N/A");
                saveReturnHistoryRecord(oleForm, oleItemRecordForCirc);
            }
        }
    }

    private void saveReturnHistoryRecord(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        ItemRecord itemRecord = null != oleItemRecordForCirc ? oleItemRecordForCirc.getItemRecord() : null;
        OLEReturnHistoryRecord oleReturnHistoryRecord = new OLEReturnHistoryRecord();
        if(null != itemRecord) {
            oleReturnHistoryRecord.setItemBarcode(itemRecord.getBarCode());
            oleReturnHistoryRecord.setItemUUID(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
        }
        oleReturnHistoryRecord.setHomeCirculationDesk(null != oleItemRecordForCirc.getCheckinLocation() ? oleItemRecordForCirc.getCheckinLocation().getCirculationDeskCode() : getSelectedCirculationDesk(oleForm));
        oleReturnHistoryRecord.setRouteCirculationDesk(oleItemRecordForCirc.getRouteToLocation());
        oleReturnHistoryRecord.setOperator(getOperatorId(oleForm));
        oleReturnHistoryRecord.setReturnedItemStatus(oleItemRecordForCirc.getItemStatusToBeUpdatedTo());
        try {
            oleReturnHistoryRecord.setReturnedDateTime(new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getBusinessObjectService().save(oleReturnHistoryRecord);
    }

    private void handleAutoCheckout(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        OleDeliverRequestBo oleDeliverRequestBo = oleItemRecordForCirc.getOleDeliverRequestBo();
        getDroolsExchange(oleForm).addToContext("requestBo", oleDeliverRequestBo);
    }

    private void handleCheckinNote(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        if (StringUtils.isNotBlank(oleItemRecordForCirc.getItemRecord().getCheckInNote())) {
            getDroolsExchange(oleForm).addToContext("checkinNote", true);
        }
    }

    private void handleOnHoldRequestIfExists(OleItemRecordForCirc oleItemRecordForCirc) {
        OleDeliverRequestBo oleDeliverRequestBo = oleItemRecordForCirc.getOleDeliverRequestBo();
        List<String> requestTypes = getRequestTypes();
       boolean expirationDateToBeModified = false;
        if (null != oleItemRecordForCirc.getItemStatusToBeUpdatedTo() && oleItemRecordForCirc.getItemStatusToBeUpdatedTo().equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD) &&
                null != oleDeliverRequestBo && requestTypes.contains(oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode())) {
            Boolean sendOnHoldNoticeWhileCheckinItem = ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.SEND_ONHOLD_NOTICE_WHILE_CHECKIN);
            if(null == oleDeliverRequestBo.getHoldExpirationDate()) {
                Date holdExpiryDate = generateHoldExpirationDate(oleDeliverRequestBo);
                oleDeliverRequestBo.setHoldExpirationDate(new java.sql.Date(holdExpiryDate.getTime()));
                if(oleDeliverRequestBo.getRequestExpiryDate().compareTo(oleDeliverRequestBo.getHoldExpirationDate())<0){
                    expirationDateToBeModified = true;
                }
                List<OLEDeliverNotice> oleDeliverNotices = oleDeliverRequestBo.getDeliverNotices();
                for(OLEDeliverNotice oleDeliverNotice : oleDeliverNotices) {
                    if(oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.ONHOLD_EXPIRATION_NOTICE) || (expirationDateToBeModified && oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.REQUEST_EXPIRATION_NOTICE))) {
                        oleDeliverNotice.setNoticeToBeSendDate(new Timestamp(holdExpiryDate.getTime()));
                    }if(!sendOnHoldNoticeWhileCheckinItem && oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.ONHOLD_NOTICE)){
                        oleDeliverNotice.setNoticeToBeSendDate(new Timestamp(new Date().getTime()));
                    }if(oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.ONHOLD_COURTESY_NOTICE)){
                        int noOfDaysBefore  = Integer.parseInt(getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.ONHOLD_COURTESY_NOTICE_INTERVAL));
                        oleDeliverNotice.setNoticeToBeSendDate(new Timestamp((holdExpiryDate.getTime() - (noOfDaysBefore* 24 * 3600 * 1000l))));
                    }
                }
                getBusinessObjectService().save(oleDeliverRequestBo);
            }
            if (sendOnHoldNoticeWhileCheckinItem && oleDeliverRequestBo.getOnHoldNoticeSentDate() == null) {

                OLEDeliverNotice  deliverNoticeToSentMail = getOnHoldNoticeToSendMail(oleDeliverRequestBo);

                if (null != deliverNoticeToSentMail) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    Map requestMap = new HashMap();
                    requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, deliverNoticeToSentMail.getNoticeContentConfigName());
                    requestMap.put(OLEConstants.DELIVER_NOTICES, Collections.singletonList(deliverNoticeToSentMail));
                    OnHoldNoticesExecutor runnable = new OnHoldNoticesExecutor(requestMap);
                    executorService.execute(runnable);
                    executorService.shutdown();
                }
            }
        }
    }

    private List<String> getRequestTypes() {
        List<String> requestTypes = new ArrayList<>();
        String requestTypeParameter = getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.ON_HOLD_NOTICE_REQUEST_TYPE);
        if (StringUtils.isNotBlank(requestTypeParameter)) {
            StringTokenizer stringTokenizer = new StringTokenizer(requestTypeParameter,";");
            while(stringTokenizer.hasMoreTokens()){
                requestTypes.add(stringTokenizer.nextToken());
            }
        }
        return requestTypes;
    }

    private Date generateHoldExpirationDate(OleDeliverRequestBo oleDeliverRequestBo) {
        Date holdExpiryDate = new Date();
        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
        OleCirculationDesk oleCirculationDesk = oleDeliverRequestBo.getOlePickUpLocation();
        if(org.apache.commons.lang.StringUtils.isEmpty(oleCirculationDesk.getOnHoldDays())){
            holdExpiryDate = new java.sql.Date(System.currentTimeMillis());
        } else if(null != oleCirculationDesk) {
            holdExpiryDate = DateUtils.addDays(new Date(), Integer.parseInt(oleCirculationDesk.getOnHoldDays()));
            holdExpiryDate = calculateHoldExpirationDate(holdExpiryDate, loanDateTimeUtil, oleCirculationDesk);
        }
        return holdExpiryDate;
    }

    private Date calculateHoldExpirationDate(Date holdExpiryDate, LoanDateTimeUtil loanDateTimeUtil, OleCirculationDesk oleCirculationDesk) {
        OleCalendar activeCalendar = loanDateTimeUtil.getActiveCalendar(holdExpiryDate, oleCirculationDesk.getCalendarGroupId());
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = loanDateTimeUtil.doesDateFallInExceptionPeriod(activeCalendar, holdExpiryDate);
        if (null == oleCalendarExceptionPeriod) {
            OleCalendarExceptionDate exceptionDate = loanDateTimeUtil.isDateAnExceptionDate(activeCalendar, holdExpiryDate);
            if (null != exceptionDate) {
                if (StringUtils.isEmpty(exceptionDate.getOpenTime()) && StringUtils.isEmpty(exceptionDate.getCloseTime())) {
                    Date followingDay = DateUtils.addDays(holdExpiryDate, 1);
                    holdExpiryDate = calculateHoldExpirationDate(followingDay, loanDateTimeUtil, oleCirculationDesk);
                }
            }
        } else {
            List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList();
            //If the week list is empty i.e its a holiday period;
            if (CollectionUtils.isEmpty(oleCalendarExceptionPeriodWeekList)) {
                Timestamp endDate = oleCalendarExceptionPeriod.getEndDate();
                Date followingDay = DateUtils.addDays(endDate, 1);
                holdExpiryDate = calculateHoldExpirationDate(followingDay, loanDateTimeUtil, oleCirculationDesk);
            }
        }
        return holdExpiryDate;
    }

    private void handleMissingPieceIfExists(OLEForm oleForm, OleLoanDocument loanDocument, OleItemSearch oleItemSearch) {
        if(StringUtils.isNotBlank(getMissingPieceMatchCheck(oleForm)) && getMissingPieceMatchCheck(oleForm).equalsIgnoreCase("mismatched") && loanDocument!=null){
            CheckinForm checkinForm = (CheckinForm)oleForm;
            populateLoanDocumentForMissingPiece(oleForm, loanDocument, oleItemSearch, checkinForm);
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Map requestMap = new HashMap();
            requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, OLEConstants.MISSING_PIECE_NOTICE_CONFIG_NAME);
            requestMap.put(OLEConstants.LOAN_DOCUMENTS, Collections.singletonList(loanDocument));
            MissingPieceNoticesExecutor runnable = new MissingPieceNoticesExecutor(requestMap);
            executorService.execute(runnable);
            executorService.shutdown();

        }
    }

    private void populateLoanDocumentForMissingPiece(OLEForm oleForm, OleLoanDocument loanDocument, OleItemSearch oleItemSearch, CheckinForm checkinForm) {
        loanDocument.setItemFullLocation(oleItemSearch.getShelvingLocation());
        loanDocument.setTitle(oleItemSearch.getTitle());
        loanDocument.setAuthor(oleItemSearch.getAuthor());
        loanDocument.setItemCallNumber(oleItemSearch.getCallNumber());
        loanDocument.setItemCopyNumber(oleItemSearch.getCopyNumber());
        loanDocument.setEnumeration(oleItemSearch.getEnumeration());
        loanDocument.setChronology(oleItemSearch.getChronology());
        loanDocument.setCheckInDate(Timestamp.valueOf(getCustomDueDateMap(oleForm) + " " + getCustomDueDateTime(oleForm)));
        loanDocument.setMissingPieceNote(checkinForm.getMissingPieceNote());
    }

    private OLEDeliverNotice getOnHoldNoticeToSendMail(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OLEDeliverNotice> deliverNotices = oleDeliverRequestBo.getDeliverNotices();
        for (Iterator<OLEDeliverNotice> iterator = deliverNotices.iterator(); iterator.hasNext(); ) {
            OLEDeliverNotice oleDeliverNotice = iterator.next();
            if(oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.ONHOLD_NOTICE)){
                return oleDeliverNotice;
            }
        }
        return null;
    }

    public ItemInfoUtil getItemInfoUtil() {
        return ItemInfoUtil.getInstance();
    }

    public OlePrintSlipUtil getOlePrintSlipUtil(OleItemRecordForCirc oleItemRecordForCirc) {
        String itemStatusToBeUpdatedTo = oleItemRecordForCirc.getItemStatusToBeUpdatedTo();
        OleCirculationDesk oleCirculationDesk = null != oleItemRecordForCirc ? oleItemRecordForCirc.getCheckinLocation() : null;

        if (itemStatusToBeUpdatedTo.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT)) {
            return new InTransitRegularPrintSlipUtil();
        } else if (itemStatusToBeUpdatedTo.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD)) {
            return new InTransitForHoldRegularPrintSlipUtil();
        } else if (itemStatusToBeUpdatedTo.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT_STAFF)) {
            return new InTransitForStaffRegularPrintSlipUtil();
        } else if (itemStatusToBeUpdatedTo.equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD)) {
            if (oleCirculationDesk != null && oleCirculationDesk.getHoldFormat() != null && oleCirculationDesk.getHoldFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
                return new OnHoldRecieptPrintSlipUtil();
            } else {
                return new OnHoldRegularPrintSlipUtil();
            }
        } else if (itemStatusToBeUpdatedTo.equalsIgnoreCase(OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN)) {
            return new InTransitForLoanRegularPrintSlipUtil();
        }
        return null;
    }

    private void handleRecentlyReturnedRecord(OleItemRecordForCirc oleItemRecordForCirc, boolean updateRecentlyReturnedTable) {
        if (updateRecentlyReturnedTable) {
            createOrUpdateRecentlyReturnedRecord(oleItemRecordForCirc.getItemUUID(), oleItemRecordForCirc.getOperatorCircLocation().getCirculationDeskId());
        }
    }

    private void emailToPatronForOnHoldStatus() {

    }

    public boolean ruleMatched(DroolsResponse droolsResponse) {
        if (droolsResponse.isRuleMatched()) {
            return true;
        }
        return false;
    }

    private void updateCheckedInItemList(OLEForm oleForm, String checkinNote, OleItemRecordForCirc oleItemRecordForCirc, OleItemSearch oleItemSearch, OlePatronDocument olePatronDocument) {
        CheckedInItem checkedInItem = new CheckedInItem();
        SimpleDateFormat dateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        checkedInItem.setCheckinNote(checkinNote);
        checkedInItem.setTitle(oleItemSearch.getTitle());
        checkedInItem.setItemId(oleItemSearch.getItemBarCode());
        checkedInItem.setAuthor(oleItemSearch.getAuthor());
        checkedInItem.setBibUuid(oleItemSearch.getBibUUID());
        checkedInItem.setInstanceUuid(oleItemSearch.getInstanceUUID());
        checkedInItem.setItemUuid(oleItemSearch.getItemUUID());
        String location = null != oleItemSearch.getShelvingLocation() ? oleItemSearch.getShelvingLocation().split("/")[oleItemSearch.getShelvingLocation().split("/").length - 1] : oleItemSearch.getShelvingLocation();
        try {
            checkedInItem.setLocation(getOleLoanDocumentsFromSolrBuilder().getLocationFullName(location));
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkedInItem.setLocationCode(oleItemSearch.getShelvingLocation());
        checkedInItem.setCallNumber(oleItemSearch.getCallNumber());
        checkedInItem.setCopyNumber(oleItemSearch.getCopyNumber());
        checkedInItem.setEnumeration(oleItemSearch.getEnumeration());
        checkedInItem.setChronology(oleItemSearch.getChronology());
        try {
            checkedInItem.setCheckInDate(dateFormat.format(processDateAndTimeForAlterDueDate(getCustomDueDateMap(oleForm),getCustomDueDateTime(oleForm))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkedInItem.setItemStatus(oleItemRecordForCirc.getItemStatusToBeUpdatedTo());
        checkedInItem.setPatronId((olePatronDocument != null) ? olePatronDocument.getOlePatronId() : "");
        checkedInItem.setPatronBarcode((olePatronDocument != null) ? olePatronDocument.getBarcode() : "");
        checkedInItem.setBorrowerType((olePatronDocument != null) ? olePatronDocument.getBorrowerTypeName() : "");
        checkedInItem.setBillName(getBillName(olePatronDocument, oleItemSearch.getItemBarCode()));
        checkedInItem.setItemType(oleItemSearch.getItemType());
        checkedInItem.setItemForCircRecord(oleItemRecordForCirc);

        Map<String, Object> context = oleForm.getDroolsExchange().getContext();
        if(context.containsKey(DroolsConstants.PRINT_SLIP_FLAG) && (Boolean)context.get(DroolsConstants.PRINT_SLIP_FLAG)) {
            checkedInItem.setPrintSlipForEndSession(true);
        }
        if(oleItemRecordForCirc.getItemStatusToBeUpdatedTo().equals(OLEConstants.ITEM_STATUS_IN_TRANSIT )|| oleItemRecordForCirc.getItemStatusToBeUpdatedTo().equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD)) {
            checkedInItem.setRouteToLocation(oleItemRecordForCirc.getRouteToLocation());
        }
        addCheckedInItemToCheckedInItemList(checkedInItem, oleForm);
        setCheckedInItem(checkedInItem, oleForm);
    }

    private String getBillName(OlePatronDocument olePatronDocument, String itemBarcode) {
        if (null != olePatronDocument) {
            List<PatronBillPayment> patronBillPayments = olePatronDocument.getPatronBillPayments();
            for (Iterator<PatronBillPayment> iterator = patronBillPayments.iterator(); iterator.hasNext(); ) {
                PatronBillPayment patronBillPayment = iterator.next();
                for (Iterator<FeeType> patronBillPaymentIterator = patronBillPayment.getFeeType().iterator(); patronBillPaymentIterator.hasNext(); ) {
                    FeeType feeType = patronBillPaymentIterator.next();
                    if (null != feeType.getItemBarcode() && feeType.getItemBarcode().equalsIgnoreCase(itemBarcode)) {
                        return feeType.getOleFeeType().getFeeTypeCode();
                    }
                }
            }
        }
        return null;
    }

    public void updateLoanDocument(OleLoanDocument loanDocument, OleItemSearch oleItemSearch, ItemRecord itemRecord) throws Exception {
        createCirculationHistoryAndTemporaryHistoryRecords(loanDocument, oleItemSearch, itemRecord);
        getBusinessObjectService().delete(loanDocument);
    }

    public void updateItemStatusAndCircCount(OleItemRecordForCirc oleItemRecordForCirc) {
        HashMap parameterValues = new HashMap();
        parameterValues.put("patronId", null);
        parameterValues.put("proxyPatronId", null);
        parameterValues.put("itemCheckoutDateTime", null);
        parameterValues.put("loanDueDate", null);
        parameterValues.put("numRenewals", null);
        parameterValues.put("itemStatus", oleItemRecordForCirc.getItemStatusToBeUpdatedTo());
        parameterValues.put("locationsCheckinCountRecordToBeUpdated", oleItemRecordForCirc.getLocationsCheckinCountRecordToBeUpdated());
        parameterValues.put("claimsReturnNote", oleItemRecordForCirc.getItemRecord().getClaimsReturnedNote());
        Timestamp claimsReturnedFlagCreateDate = oleItemRecordForCirc.getItemRecord().getClaimsReturnedFlagCreateDate();
        parameterValues.put("ClaimsReturnedDate", claimsReturnedFlagCreateDate == null ? claimsReturnedFlagCreateDate : convertToString(claimsReturnedFlagCreateDate));
        parameterValues.put("itemClaimsReturnedRecords", prepareClaimsReturnHistoryRecords(oleItemRecordForCirc.getItemRecord()));
        parameterValues.put("itemClaimsReturnedFlag", oleItemRecordForCirc.getItemRecord().getClaimsReturnedFlag());
        parameterValues.put("missingPieceItemFlag", oleItemRecordForCirc.getItemRecord().isMissingPieceFlag());
        parameterValues.put("missingPieceItemNote", oleItemRecordForCirc.getItemRecord().getMissingPieceFlagNote());
        parameterValues.put("missingPieceItemCount", oleItemRecordForCirc.getItemRecord().getMissingPiecesCount());
        parameterValues.put("noOfmissingPiece", oleItemRecordForCirc.getItemRecord().getNumberOfPieces());
        Timestamp missingPieceItemDate = oleItemRecordForCirc.getItemRecord().getMissingPieceEffectiveDate();
        parameterValues.put("missingPieceItemDate", missingPieceItemDate == null ? missingPieceItemDate : convertToString(missingPieceItemDate));
        parameterValues.put("itemMissingPieceItemRecords", prepareMissingPieceHistoryRecords(oleItemRecordForCirc.getItemRecord()));
        updateItemInfoInSolr(parameterValues, oleItemRecordForCirc.getItemUUID(), true);
    }

    private List<ItemClaimsReturnedRecord> prepareClaimsReturnHistoryRecords(ItemRecord itemRecord) {
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> claimsReturnedRecordList = itemRecord.getItemClaimsReturnedRecords();
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = new ArrayList<>();
        for (Iterator<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> iterator = claimsReturnedRecordList.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord itemClaimsReturnedRecord = iterator.next();
            ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
            if (itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate() != null && !itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate().toString().isEmpty()) {
                String formatedDateStringForClaimsReturn = convertToString(itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate());
                claimsReturnedRecord.setClaimsReturnedFlagCreateDate(formatedDateStringForClaimsReturn);
            }
            claimsReturnedRecord.setClaimsReturnedNote(itemClaimsReturnedRecord.getClaimsReturnedNote());
            claimsReturnedRecord.setClaimsReturnedPatronBarcode(itemClaimsReturnedRecord.getClaimsReturnedPatronBarcode());
            claimsReturnedRecord.setClaimsReturnedOperatorId(itemClaimsReturnedRecord.getClaimsReturnedOperatorId());
            claimsReturnedRecord.setItemId(itemClaimsReturnedRecord.getItemId());
            itemClaimsReturnedRecordList.add(claimsReturnedRecord);
        }
        return itemClaimsReturnedRecordList;
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


    private void createCirculationHistoryAndTemporaryHistoryRecords(OleLoanDocument oleLoanDocument, OleItemSearch oleItemSearch, ItemRecord itemRecord) throws Exception {
        try {
            OlePatronDocument olePatronDocument = oleLoanDocument.getOlePatron();
            Map<String,String> criteriaMap = new HashMap<>();
            criteriaMap.put("loanId",oleLoanDocument.getLoanId());
            List<OleCirculationHistory> circulationHistoryRecords = (List<OleCirculationHistory>) getBusinessObjectService().findMatching(OleCirculationHistory.class,criteriaMap);
            if(circulationHistoryRecords.size()>0){
            OleCirculationHistory oleCirculationHistory =  circulationHistoryRecords.get(0);
            if(GlobalVariables.getUserSession()!=null && GlobalVariables.getUserSession().getPrincipalId()!=null){
                oleCirculationHistory.setCheckInOperatorId(GlobalVariables.getUserSession().getPrincipalId());
            }
            if(oleLoanDocument.isOverrideCheckInTime()){
              oleCirculationHistory.setCheckInDate(new Timestamp(System.currentTimeMillis()));
              oleCirculationHistory.setOverrideCheckInDateTime(oleLoanDocument.getCheckInDate());
             }else{
            oleCirculationHistory.setCheckInDate(oleLoanDocument.getCheckInDate() != null ? oleLoanDocument.getCheckInDate() : new Timestamp(System.currentTimeMillis()));
            }
                oleCirculationHistory.setDueDate(oleLoanDocument.getLoanDueDate());
            oleCirculationHistory.setNumberOfOverdueNoticesSent(oleLoanDocument.getNumberOfOverdueNoticesSent());
            oleCirculationHistory.setNumberOfRenewals(oleLoanDocument.getNumberOfRenewals());
            oleCirculationHistory.setRepaymentFeePatronBillId(oleLoanDocument.getRepaymentFeePatronBillId());
            oleCirculationHistory.setPastDueDate(oleLoanDocument.getPastDueDate());
            oleCirculationHistory.setOverdueNoticeDate(oleLoanDocument.getOverDueNoticeDate());
            OleCirculationHistory savedCircHistoryRecord = getBusinessObjectService().save(oleCirculationHistory);
            }
            OleTemporaryCirculationHistory oleTemporaryCirculationHistory = new OleTemporaryCirculationHistory();
            oleTemporaryCirculationHistory.setCirculationLocationId(oleLoanDocument.getCirculationLocationId());
            oleTemporaryCirculationHistory.setOlePatronId(oleLoanDocument.getPatronId());
            oleTemporaryCirculationHistory.setItemId(oleLoanDocument.getItemId());
            oleTemporaryCirculationHistory.setCheckInDate(oleLoanDocument.getCheckInDate() != null ? oleLoanDocument.getCheckInDate() : new Timestamp(System.currentTimeMillis()));
            oleTemporaryCirculationHistory.setItemUuid(oleLoanDocument.getItemUuid());
            oleTemporaryCirculationHistory.setDueDate(oleLoanDocument.getLoanDueDate());
            oleTemporaryCirculationHistory.setCheckOutDate(oleLoanDocument.getCreateDate());
            OleTemporaryCirculationHistory savedTempCircHistoryRecord = getBusinessObjectService().save(oleTemporaryCirculationHistory);

        } catch (Exception tempHistoryException) {
            throw new Exception("Unable to record the transaction in temporary circulation history.");
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

    private void createOrUpdateRecentlyReturnedRecord(String itemUUID, String circulationDeskId) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("itemUuid", itemUUID);
        OleRecentlyReturned oleRecentlyReturned = getBusinessObjectService().findByPrimaryKey(OleRecentlyReturned.class, map);
        if (oleRecentlyReturned != null) {
            oleRecentlyReturned.setCirculationDeskId(circulationDeskId);
        } else {
            oleRecentlyReturned = new OleRecentlyReturned();
            oleRecentlyReturned.setCirculationDeskId(circulationDeskId);
            oleRecentlyReturned.setItemUuid(itemUUID);
        }
        getBusinessObjectService().save(oleRecentlyReturned);
    }

    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public OleLoanDocumentDaoOjb getLoanDaoOjb() {
        if (null == loanDaoOjb) {
            loanDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return loanDaoOjb;
    }

    public void setLoanDaoOjb(OleLoanDocumentDaoOjb loanDaoOjb) {
        this.loanDaoOjb = loanDaoOjb;
    }
}
