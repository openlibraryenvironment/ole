package org.kuali.ole.deliver.controller.renew;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarWeek;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ncip.service.impl.NonSip2RenewItemService;
import org.kuali.ole.ncip.service.impl.RenewItemsService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sheiksalahudeenm on 8/13/15.
 */
public class RenewController extends CircUtilController {

    private static final Logger LOG = Logger.getLogger(RenewController.class);
    private RenewItemControllerUtil renewItemControllerUtil;
    private RenewItemsService renewItemsService;
    private ParameterValueResolver parameterValueResolver;

    public DroolsResponse renewItems(List<OleLoanDocument> selectedLoanDocumentList, OlePatronDocument olePatronDocument) {
        List<Item> itemList = new ArrayList<>();
        DroolsResponse finalDroolResponse = new DroolsResponse();

        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            DroolsResponse droolsResponse = new DroolsResponse();
            OleLoanDocument oleLoanDocument = iterator.next();

            boolean isIndefiniteDueDate = oleLoanDocument.IsIndefiniteDueDate();
            if (!isIndefiniteDueDate) {

                List<Object> facts = new ArrayList<>();

                oleLoanDocument.setPastDueDate(oleLoanDocument.getLoanDueDate());
                ItemRecord itemRecord = getItemRecordByBarcode(oleLoanDocument.getItemId());
                if (itemRecord !=null && !itemRecord.getClaimsReturnedFlag()){
                    OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, null);
                    NoticeInfo noticeInfo = new NoticeInfo();
                    facts.add(oleLoanDocument);
                    facts.add(olePatronDocument);
                    facts.add(oleItemRecordForCirc);
                    facts.add(droolsResponse);
                    facts.add(noticeInfo);
                    fireRules(facts, null, "renewal validation");

                    processDueDateBasedOnExpirationDate(olePatronDocument, oleLoanDocument);

                    Boolean fineCalcWhileRenew = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                            .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FINE_CALC_WHILE_RENEW);
                    if (fineCalcWhileRenew){
                        ItemFineRate itemFineRate = fireFineRules(oleLoanDocument, oleItemRecordForCirc, olePatronDocument);
                        oleLoanDocument.setItemFineRate(itemFineRate);
                    }

                    if (droolsResponse.isRuleMatched()) {
                        if (StringUtils.isBlank(droolsResponse.retrieveErrorMessage())) {
                            boolean pastAndRenewDueDateSame = false;
                            try {
                                pastAndRenewDueDateSame = oleLoanDocument.isPastAndRenewDueDateSame();
                                if (!pastAndRenewDueDateSame) {
                                    Integer numRenewals = getRenewItemControllerUtil().incrementRenewalCount(oleLoanDocument);
                                    oleLoanDocument.setNumberOfRenewals(numRenewals.toString());
                                List<OLEDeliverNotice> oleDeliverNotices = processNotices(oleLoanDocument, oleItemRecordForCirc.getItemRecord(), null);
                                    oleLoanDocument.setDeliverNotices(oleDeliverNotices);
                                    if (null != oleLoanDocument.getLoanId()) {
                                        Item itemForUpdate = getItemForUpdate(oleLoanDocument);
                                        if (null != itemForUpdate) {
                                            itemList.add(itemForUpdate);
                                            droolsResponse.setSucessMessage("Successfully Renewed");
                                            droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                                            finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);

                                            generateBillPayment(oleLoanDocument.getCirculationLocationId(),oleLoanDocument, new Timestamp(new Date().getTime()), new Timestamp(oleLoanDocument.getPastDueDate().getTime()),true);
                                            if (oleItemRecordForCirc.getItemStatusRecord() != null && OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode())){
                                                oleLoanDocument.setItemStatus(OLEConstants.ITEM_STATUS_CHECKEDOUT);
                                                processLostItem(GlobalVariables.getUserSession().getPrincipalId(), oleLoanDocument, true);
                                            }
                                        }
                                    }
                                } else {
                                    droolsResponse.addErrorMessageCode("PastAndNewDueDateSame");
                                    droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                                    finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                                }
                            } catch (Exception e) {
                                if (e.getMessage().equalsIgnoreCase("No Fixed Due Date found for the renewal policy")) {
                                    oleLoanDocument.setLoanDueDate(new Timestamp(oleLoanDocument.getPastDueDate().getTime()));
                                    droolsResponse.addErrorMessageCode("InvalidFixedDueDateMapping");
                                    droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                                    finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                                } else {
                                    oleLoanDocument.setLoanDueDate(new Timestamp(oleLoanDocument.getPastDueDate().getTime()));
                                    droolsResponse.addErrorMessageCode("InternalException");
                                    droolsResponse.addErrorMessage(e.getMessage());
                                    droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                                    finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                                }
                            }
                        } else {
                            droolsResponse.addErrorMessageCode("ErrorMessageFromRule");
                            droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                            finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                        }
                    } else {
                        droolsResponse.addErrorMessageCode("No renewal policy found");
                        droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                        finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                    }
                } else {
                    droolsResponse.addErrorMessageCode("claimsReturned");
                    droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                    finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
                }
            } else {
                droolsResponse.addErrorMessageCode("InDefiniteDueDate");
                droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
            }
        }

        if (CollectionUtils.isNotEmpty(itemList)) {
            getRenewItemsService().processBulkUpdate(itemList, finalDroolResponse);
        }

        return finalDroolResponse;

    }

    public DroolsResponse proceedToSaveLoanDocuments(List<OleLoanDocument> loanDocumentsForRenew) {
        DroolsResponse finalDroolResponse = new DroolsResponse();
        List<Item> updateItemList = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = loanDocumentsForRenew.iterator(); iterator.hasNext(); ) {
            DroolsResponse droolsResponse = new DroolsResponse();
            OleLoanDocument oleLoanDocument = iterator.next();
            Integer numRenewals = getRenewItemControllerUtil().incrementRenewalCount(oleLoanDocument);
            oleLoanDocument.setNumberOfRenewals(numRenewals.toString());
            updateNoticesForLoanDocument(oleLoanDocument);
            Item itemForUpdate = getItemForUpdate(oleLoanDocument);
            if (null != itemForUpdate) {
                updateItemList.add(itemForUpdate);
                droolsResponse.setSucessMessage("Successfully Renewed");
                droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
            }
        }
        getRenewItemsService().processBulkUpdate(updateItemList, finalDroolResponse);
        return finalDroolResponse;

    }


    public Item getItemForUpdate(OleLoanDocument oleLoanDocument) {
        Map updateParameters = getRenewItemControllerUtil().getUpdateParameters(oleLoanDocument);
        return getItemForUpdate(updateParameters);
    }

    public String analyzeRenewedLoanDocuments(CircForm circForm, DroolsResponse droolsResponse) {
        StringBuilder messageContentForRenew = new StringBuilder();
        Map<String, Object> context = droolsResponse.getDroolsExchange().getContext();
        if (context.size() > 0) {
            for (Iterator<String> iterator = context.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                DroolsResponse individualDroolResponse = (DroolsResponse) context.get(key);
                if (StringUtils.isNotBlank(individualDroolResponse.getSucessMessage()) && individualDroolResponse.getSucessMessage().equalsIgnoreCase("Successfully Renewed")) {
                    OleLoanDocument oleLoanDocument = (OleLoanDocument) individualDroolResponse.getDroolsExchange().getContext().get(key);
                    String content = "Successfully renewed for item (" + oleLoanDocument.getItemId() + ")";
                    appendContentToStrinBuilder(messageContentForRenew, content);
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("No renewal policy found")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    if (null != individualLoanDocument) {
                        individualLoanDocument.setNonCirculatingItem(true);
                        if (null == circForm.getLoanDocumentsForRenew()) {
                            circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
                        }
                        individualLoanDocument.setErrorMessage(null);
                        individualLoanDocument.setErrorMessage("No renewal policy found for this item.");
                        circForm.getLoanDocumentsForRenew().add(individualLoanDocument);
                    }
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("ErrorMessageFromRule")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    if (null != individualLoanDocument) {
                        individualLoanDocument.setNonCirculatingItem(true);
                        if (null == circForm.getLoanDocumentsForRenew()) {
                            circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
                        }
                        individualLoanDocument.setErrorMessage(null);
                        individualLoanDocument.setErrorMessage(individualDroolResponse.retrieveErrorMessage());
                        circForm.getLoanDocumentsForRenew().add(individualLoanDocument);
                    }
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("PastAndNewDueDateSame")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    String content = "Item (" + individualLoanDocument.getItemId() + ") was not renewed because the new due date/time would not change.";
                    appendContentToStrinBuilder(messageContentForRenew, content);
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("InDefiniteDueDate")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    String content = "Item (" + individualLoanDocument.getItemId() + ") wasn't renewed. Items on indefinite loan do not need to be renewed.";
                    appendContentToStrinBuilder(messageContentForRenew, content);
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("claimsReturned")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    String content = "Item (" + individualLoanDocument.getItemId() + ") wasn't renewed because the item is claims returned.";
                    appendContentToStrinBuilder(messageContentForRenew, content);
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("InvalidFixedDueDateMapping")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    String content = "Item (" + individualLoanDocument.getItemId() + ") wasn't renewed. Invalid Fixed Due Date Mapping.";
                    appendContentToStrinBuilder(messageContentForRenew, content);
                } else if (individualDroolResponse.retriveErrorCode().equalsIgnoreCase("InternalException")) {
                    Map<String, Object> individualResponseMap = individualDroolResponse.getDroolsExchange().getContext();
                    OleLoanDocument individualLoanDocument = (OleLoanDocument) individualResponseMap.get(key);
                    String content = "Item (" + individualLoanDocument.getItemId() + ") wasn't renewed." + droolsResponse.retrieveErrorMessage();
                    appendContentToStrinBuilder(messageContentForRenew, content);
                }
            }
        }
        return messageContentForRenew.toString();
    }

    private void appendContentToStrinBuilder(StringBuilder stringBuilder, String content) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("<br/>").append(content);
        } else {
            stringBuilder.append(content);
        }
    }

    public void prepareResponeMessage(CircForm circForm, String responseForNonCirculatingItem) {
        ErrorMessage errorMessage = circForm.getErrorMessage();

        if (null == errorMessage) {
            errorMessage = new ErrorMessage();
        }
        String errorMessageContent = errorMessage.getErrorMessage();
        if (StringUtils.isBlank(errorMessageContent)) {
            errorMessage.setErrorMessage(responseForNonCirculatingItem);
        } else {
            errorMessage.setErrorMessage("<br/>" + responseForNonCirculatingItem);
        }
        circForm.setErrorMessage(errorMessage);
    }

    public String prepareResponseForRenewedLoanDocuments(DroolsResponse droolsResponse) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Object> context = droolsResponse.getDroolsExchange().getContext();
        if (context.size() > 0) {
            for (Iterator<String> iterator = context.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                DroolsResponse individualDroolResponse = (DroolsResponse) context.get(key);
                if (StringUtils.isNotBlank(individualDroolResponse.getSucessMessage()) && individualDroolResponse.getSucessMessage().equalsIgnoreCase("Successfully Renewed")) {
                    OleLoanDocument oleLoanDocument = (OleLoanDocument) individualDroolResponse.getDroolsExchange().getContext().get(key);
                    String content = "Successfully renewed for item (" + oleLoanDocument.getItemId() + ")";
                    appendContentToStrinBuilder(stringBuilder, content);

                    generateBillPayment(oleLoanDocument.getCirculationLocationId(), oleLoanDocument, new Timestamp(new Date().getTime()), new Timestamp(oleLoanDocument.getPastDueDate().getTime()),true);
                    if (StringUtils.isNotBlank(oleLoanDocument.getItemStatus()) && oleLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST)) {
                        oleLoanDocument.setItemStatus(OLEConstants.ITEM_STATUS_CHECKEDOUT);
                        processLostItem(GlobalVariables.getUserSession().getPrincipalId(), oleLoanDocument, true);
                    }
                } else {
                    OleLoanDocument oleLoanDocument = (OleLoanDocument) individualDroolResponse.getDroolsExchange().getContext().get(key);
                    String content = "Renewed failed for item (" + oleLoanDocument.getItemId() + ")";
                    appendContentToStrinBuilder(stringBuilder, content);
                }
            }
        }
        return stringBuilder.toString();
    }

    public RenewItemControllerUtil getRenewItemControllerUtil() {
        if (null == renewItemControllerUtil) {
            renewItemControllerUtil = new RenewItemControllerUtil();
        }
        return renewItemControllerUtil;
    }

    public void setRenewItemControllerUtil(RenewItemControllerUtil renewItemControllerUtil) {
        this.renewItemControllerUtil = renewItemControllerUtil;
    }

    public RenewItemsService getRenewItemsService() {
        if (null == renewItemsService) {
            renewItemsService = new NonSip2RenewItemService();
        }
        return renewItemsService;
    }

    public void setRenewItemsService(RenewItemsService renewItemsService) {
        this.renewItemsService = renewItemsService;
    }
    public ParameterValueResolver getParameterValueResolver() {
        if (parameterValueResolver == null) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    private void processDueDateBasedOnExpirationDate(OlePatronDocument olePatronDocument, OleLoanDocument
            oleLoanDocument) {
        if (olePatronDocument.getExpirationDate() != null && oleLoanDocument.getLoanDueDate() != null) {
            Timestamp expirationDate = new Timestamp(olePatronDocument.getExpirationDate().getTime());
            if (isPatronExpiringBeforeLoanDue(oleLoanDocument, expirationDate) && isPatronExpirationGreaterThanToday(expirationDate)) {
                oleLoanDocument.setLoanDueDate(expirationDate);
            }
        }
        if (oleLoanDocument.getLoanDueDate() != null) {
            oleLoanDocument.setRenewalDateMap(oleLoanDocument.getLoanDueDate());
           String dueTime = getParameterValueResolver().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_TIME_FOR_DUE_DATE);
            if(dueTime!=null){
                oleLoanDocument.setRenewalDateTime(dueTime);
            }else{
                oleLoanDocument.setRenewalDateTime(getDefaultClosingTime(oleLoanDocument,oleLoanDocument.getLoanDueDate()));
            }
        }
    }

    private boolean isPatronExpirationGreaterThanToday(Timestamp expirationDate) {
        return expirationDate.compareTo(new Date()) > 0;
    }

    private boolean isPatronExpiringBeforeLoanDue(OleLoanDocument oleLoanDocument, Timestamp expirationDate) {
        return expirationDate.compareTo(oleLoanDocument.getLoanDueDate()) < 0;
    }
}
