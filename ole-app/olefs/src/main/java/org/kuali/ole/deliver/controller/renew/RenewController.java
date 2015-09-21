package org.kuali.ole.deliver.controller.renew;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ncip.service.impl.NonSip2RenewItemService;
import org.kuali.ole.ncip.service.impl.RenewItemsService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 8/13/15.
 */
public class RenewController extends CircUtilController {

    private static final Logger LOG = Logger.getLogger(RenewController.class);
    private RenewItemControllerUtil renewItemControllerUtil;
    private RenewItemsService renewItemsService;

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
                OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, null);

                NoticeInfo noticeInfo = new NoticeInfo();
                facts.add(oleLoanDocument);
                facts.add(olePatronDocument);
                facts.add(oleItemRecordForCirc);
                facts.add(droolsResponse);
                facts.add(noticeInfo);
                fireRules(facts, null, "renewal validation");

                if (droolsResponse.isRuleMatched()) {
                    if (StringUtils.isBlank(droolsResponse.retrieveErrorMessage())) {
                        boolean pastAndRenewDueDateSame = false;
                        try {
                            pastAndRenewDueDateSame = oleLoanDocument.isPastAndRenewDueDateSame();
                            if (!pastAndRenewDueDateSame) {
                                Integer numRenewals = getRenewItemControllerUtil().incrementRenewalCount(oleLoanDocument);
                                oleLoanDocument.setNumberOfRenewals(numRenewals.toString());
                                List<OLEDeliverNotice> oleDeliverNotices = processNotices(oleLoanDocument, oleItemRecordForCirc.getItemRecord());
                                oleLoanDocument.setDeliverNotices(oleDeliverNotices);
                                if (null != oleLoanDocument.getLoanId()) {
                                    Item itemForUpdate = getItemForUpdate(oleLoanDocument);
                                    if (null != itemForUpdate) {
                                        itemList.add(itemForUpdate);
                                        droolsResponse.setSucessMessage("Successfully Renewed");
                                        droolsResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);
                                        finalDroolResponse.getDroolsExchange().getContext().put(oleLoanDocument.getItemUuid(), droolsResponse);
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
                        individualLoanDocument.setNonCirculatingItem(false);
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
}
