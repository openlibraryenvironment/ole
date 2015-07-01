package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pvsubrah on 6/4/15.
 */
public class CheckoutController extends CircUtilController {
    private static final Logger LOG = Logger.getLogger(CheckoutController.class);
    private OleLoanDocument currentLoanDocument;
    private NoticeInfo noticeInfo;


    public ErrorMessage lookupItemAndSaveLoan(UifFormBase form) {
        CircForm circForm = (CircForm) form;
        ErrorMessage errorMessage = null;

        String itemBarcode = circForm.getItemBarcode();

        ItemRecord itemRecord = getItemRecordByBarcode(itemBarcode);

        if (null != itemRecord) {
            circForm.setItemRecord(itemRecord);

            if (isNotClaimsReturned(itemRecord) && (isAvailable(itemRecord) || isRecentlyReturned(itemRecord    ))) {
                currentLoanDocument = new OleLoanDocument();
                noticeInfo = new NoticeInfo();

                OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId
                        (GlobalVariables.getUserSession().getPrincipalId());
                currentLoanDocument.setOleCirculationDesk(oleCirculationDesk);

                List<Object> facts = new ArrayList<>();
                OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord);
                facts.add(oleItemRecordForCirc);

                errorMessage = new ErrorMessage();
                facts.add(errorMessage);

                facts.add(currentLoanDocument);

                Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed = identifyPatron(circForm);

                facts.add(circForm.getPatronDocument().getSelectedProxyForPatron() == null ? circForm.getPatronDocument() : circForm.getPatronDocument().getSelectedProxyForPatron());

                facts.add(noticeInfo);

                OleStopWatch oleStopWatch = new OleStopWatch();
                oleStopWatch.start();
                fireRulesForPatron(facts, null, "checkout validation");
                oleStopWatch.end();
                LOG.info("Time taken to evaluvate rules for checkout item: " + (oleStopWatch.getTotalTime()) +  " ms");

                circForm.setItemValidationDone(true);
                setPatronInfForLoanDocument(patronForWhomLoanIsBeingProcessed, currentLoanDocument);
                currentLoanDocument.setCirculationLocationId(circForm.getSelectedCirculationDesk());
                currentLoanDocument.setItemId(itemRecord.getBarCode());
                currentLoanDocument.setItemUuid(itemRecord.getUniqueIdPrefix() + "-" + itemRecord.getItemId());
                currentLoanDocument.setCreateDate(new Date(System.currentTimeMillis()));
                currentLoanDocument.setLoanOperatorId(getLoginUserId());

                processDueDateBasedOnExpirationDate(circForm.getPatronDocument(), currentLoanDocument);

                if (null == currentLoanDocument.getCirculationPolicyId()) {
                    errorMessage.setErrorMessage("No Circulation Policy found that matches the patron/item combination. Please select a due date manually!");
                    errorMessage.setErrorCode(DroolsConstants.CUSTOM_DUE_DATE_REQUIRED_FLAG);
                    currentLoanDocument.setCirculationPolicyId("No Circ Policy Found");
                    noticeInfo.setLoanType(DroolsConstants.REGULAR_LOANS_NOTICE_CONFIG);
                    return errorMessage;
                }

                if(null!= errorMessage && StringUtils.isNotBlank(errorMessage.getErrorMessage())){
                    return errorMessage;
                }

                proceedToSaveLoan(circForm);

            } else {
                errorMessage = new ErrorMessage();
                errorMessage.setErrorMessage("Only Item(s) with \"Available\" status can be checked out using this screen at this time!");
                errorMessage.setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
            }
        } else {
            errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Invalid item barcode : " + itemBarcode);
            errorMessage.setErrorCode(DroolsConstants.GENERAL_MESSAGE_FLAG);
        }
        return errorMessage;
    }

    private boolean isRecentlyReturned(ItemRecord itemRecord) {
        return itemRecord.getItemStatusRecord().getCode().equalsIgnoreCase("RECENTLY-RETURNED");
    }

    private boolean isAvailable(ItemRecord itemRecord) {
        return itemRecord.getItemStatusRecord().getCode().equalsIgnoreCase(OLEConstants.AVAILABLE);
    }

    private Map<OlePatronDocument, OlePatronDocument> identifyPatron(CircForm circForm) {
        Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed = new HashMap<>();
        OlePatronDocument currentBorrower = circForm.getPatronDocument();
        if (CollectionUtils.isNotEmpty(currentBorrower.getOleProxyPatronDocumentList())) {
            if (currentBorrower.isCheckoutForSelf()) {
                patronForWhomLoanIsBeingProcessed.put(currentBorrower, null);
            } else {
                List<OleProxyPatronDocument> oleProxyPatronDocumentList = currentBorrower.getOleProxyPatronDocumentList();
                for (Iterator<OleProxyPatronDocument> iterator = oleProxyPatronDocumentList.iterator(); iterator.hasNext(); ) {
                    OleProxyPatronDocument proxyForPatron = iterator.next();
                    OlePatronDocument proxyForPatronDocument = proxyForPatron.getOlePatronDocument();
                    if (proxyForPatronDocument.isCheckoutForSelf()) {
                        patronForWhomLoanIsBeingProcessed.put(proxyForPatronDocument, currentBorrower);
                    }
                }
            }
        } else {
            patronForWhomLoanIsBeingProcessed.put(currentBorrower, null);
        }
        return patronForWhomLoanIsBeingProcessed;
    }

    private void handleNoticeTablePopulation(ItemRecord itemRecord) {
        List<Object> facts = new ArrayList<Object>();
        facts.add(noticeInfo);
        facts.add(itemRecord);

        fireRulesForPatron(facts, null, "notice generation");

        List<OLEDeliverNotice> deliverNotices = processNotices(noticeInfo, currentLoanDocument);

        //OJB mapping will take care of persisting notice records once the loan document is saved.
        currentLoanDocument.setDeliverNotices(deliverNotices);
    }


    private void setPatronInfForLoanDocument(Map<OlePatronDocument, OlePatronDocument> patronForWhomLoanIsBeingProcessed,
                                             OleLoanDocument oleLoanDocument) {
        OlePatronDocument olePatronDocument = patronForWhomLoanIsBeingProcessed.keySet().iterator().next();
        OlePatronDocument proxyPatron = patronForWhomLoanIsBeingProcessed.get(olePatronDocument);

        oleLoanDocument.setPatronId(olePatronDocument.getOlePatronId());
        oleLoanDocument.setRealPatronBarcode(olePatronDocument.getBarcode());
        if (null != proxyPatron) {
            oleLoanDocument.setProxyPatronId(proxyPatron.getOlePatronId());
            oleLoanDocument.setRealPatronName(proxyPatron.getPatronName());
        }
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

    public UifFormBase proceedToSaveLoan(UifFormBase form) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        CircForm circForm = (CircForm) form;
        ItemRecord itemRecord = circForm.getItemRecord();
        circForm.getLoanDocumentListForCurrentSession().add(currentLoanDocument);

        if (processCustomDueDateIfSet(circForm)) return circForm;

        if (null != currentLoanDocument.getLoanDueDate()) {
            circForm.getItemRecord().setDueDateTime(currentLoanDocument.getLoanDueDate());
            handleNoticeTablePopulation(itemRecord);
        }

        OleLoanDocument savedLoanDocument = getBusinessObjectService().save(currentLoanDocument);
        if (null != savedLoanDocument.getLoanId()) {
            Boolean solrUpdateResults = updateItemInfoInSolr(currentLoanDocument, itemRecord.getItemId());
            if (solrUpdateResults) {
                updateLoanDocumentWithItemInformation(itemRecord, currentLoanDocument);
                circForm.getPatronDocument().getOleLoanDocuments().add(currentLoanDocument);
                LOG.info("Saved Loan with ID: " + savedLoanDocument.getLoanId());
            } else {
                rollBackSavedLoanRecord(itemRecord.getBarCode());
                circForm.getLoanDocumentListForCurrentSession().remove(currentLoanDocument);
            }
        } else {
            circForm.getLoanDocumentListForCurrentSession().clear();
        }
        oleStopWatch.end();
        LOG.info("Time taken to process the loan: " + (oleStopWatch.getTotalTime()) + " ms" );
        return circForm;
    }

    private boolean processCustomDueDateIfSet(CircForm circForm) {
        if (null != circForm.getCustomDueDateMap()) {
            try {
                processCustomDueDate(circForm);
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    private void processCustomDueDate(CircForm circForm) throws Exception {
        boolean timeFlag = false;
        Timestamp timestamp;
        Pattern pattern;
        Matcher matcher;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);

        String customDueDateTime = circForm.getCustomDueDateTime();
        Date customDueDateMap = circForm.getCustomDueDateMap();

        if (StringUtils.isNotBlank(customDueDateTime)) {
            String[] str = customDueDateTime.split(":");
            pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
            matcher = pattern.matcher(customDueDateTime);
            timeFlag = matcher.matches();
            if (timeFlag) {
                if (str != null && str.length <= 2) {
                    circForm.setCustomDueDateTime(customDueDateTime + OLEConstants.CHECK_IN_TIME_MS);
                }
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(customDueDateMap).concat(" ").concat(customDueDateTime));
            } else {
                circForm.setCustomDueDateTimeMessage(OLEConstants.DUE_DATE_TIME_FORMAT_MESSAGE);
                throw new Exception();
            }
        } else if (fmt.format(customDueDateMap).compareTo(fmt.format(new Date())) == 0) {
            timestamp = new Timestamp(new Date().getTime());
        } else {
            timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(customDueDateMap).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
        }
        currentLoanDocument.setLoanDueDate(timestamp);
        currentLoanDocument.setCirculationPolicyId("No Circulation Policy Matched");
    }
}
