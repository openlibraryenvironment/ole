package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by pvsubrah on 6/23/15.
 */
public class RenewItemExecutor implements Callable {

    private static final Logger LOG = Logger.getLogger(RenewItemExecutor.class);

    private ParameterValueResolver parameterValueResolver;

    private final String itemBarcode;
    private final OlePatronDocument olePatronDocument;

    private BusinessObjectService businessObjectService;
    private CircUtilController circUtilController;
    private OleItemRecordForCirc oleItemRecordForCirc;


    public RenewItemExecutor(OlePatronDocument olePatronDocument, String itemBarcode) {
        this.olePatronDocument = olePatronDocument;
        this.itemBarcode = itemBarcode;
    }

    @Override
    public Object call() throws Exception {
        long startTime = System.currentTimeMillis();
        DroolsResponse droolsResponse = renew();
        long endTime = System.currentTimeMillis();
        LOG.info("Time taken to process renew item for barcode: " + itemBarcode + " is: " + (endTime - startTime) + "ms");
        return droolsResponse;
    }

    public DroolsResponse renew() {
        DroolsResponse finalDroolResponse = null;
        List<OleLoanDocument> loanDocuments = getLoanDocuments(itemBarcode, olePatronDocument);

        if (loanDocuments.size() > 0) {
            OleLoanDocument oleLoanDocument = loanDocuments.get(0);
            boolean isIndefiniteDueDate = IsIndefiniteDueDate(oleLoanDocument);
            if (!isIndefiniteDueDate) {
                Timestamp loanDueDate = oleLoanDocument.getLoanDueDate();
                Date pastDueDate = oleLoanDocument.getPastDueDate();

                oleLoanDocument.setPastDueDate(oleLoanDocument.getLoanDueDate());

                ItemRecord itemRecord = getCircUtilController().getItemRecordByBarcode(itemBarcode);
                if (itemRecord !=null && !itemRecord.getClaimsReturnedFlag()){
                    OleItemRecordForCirc oleItemRecordForCirc = getOleItemRecordForCirc(itemRecord);
                    if (oleItemRecordForCirc != null && oleItemRecordForCirc.getItemStatusRecord() != null
                            && OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode())){
                        finalDroolResponse = new DroolsResponse();
                        oleLoanDocument.setErrorMessage("Item  wasn't renewed because the item is 'lost'.");
                        finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                    } else {
                        NoticeInfo noticeInfo = new NoticeInfo();

                        finalDroolResponse = fireRules(olePatronDocument, oleLoanDocument, oleItemRecordForCirc, noticeInfo);

                        processDueDateBasedOnExpirationDate(olePatronDocument, oleLoanDocument);

                        Boolean fineCalcWhileRenew = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FINE_CALC_WHILE_RENEW);
                        if (fineCalcWhileRenew) {
                            ItemFineRate itemFineRate = getCircUtilController().fireFineRules(oleLoanDocument, oleItemRecordForCirc, olePatronDocument);
                            oleLoanDocument.setItemFineRate(itemFineRate);
                        }

                        if (finalDroolResponse.isRuleMatched()) {
                            if (StringUtils.isBlank(finalDroolResponse.retrieveErrorMessage())) {
                                boolean pastAndRenewDueDateSame = false;
                                try {
                                    pastAndRenewDueDateSame = isPastAndRenewDueDateSame(oleLoanDocument);
                                    if (!pastAndRenewDueDateSame) {
                                        Integer numRenewals = IncrementRenewalCount(oleLoanDocument);
                                        oleLoanDocument.setNumberOfRenewals(numRenewals.toString());
                                        oleLoanDocument.getDeliverNotices().clear();
                                List<OLEDeliverNotice> oleDeliverNotices = getCircUtilController().processNotices(oleLoanDocument, oleItemRecordForCirc.getItemRecord(), null);
                                        oleLoanDocument.setDeliverNotices(oleDeliverNotices);
                                        if (null != oleLoanDocument.getLoanId()) {
                                            finalDroolResponse.setSucessMessage("Successfully Renewed");
                                            finalDroolResponse.getDroolsExchange().addToContext(oleLoanDocument.getItemUuid(), oleLoanDocument);

                                            getCircUtilController().generateBillPayment(oleLoanDocument.getCirculationLocationId(), oleLoanDocument, new Timestamp(new Date().getTime()), new Timestamp(oleLoanDocument.getPastDueDate().getTime()),true);

                                        }
                                    } else {
                                        oleLoanDocument.setLoanDueDate(loanDueDate);
                                        oleLoanDocument.setPastDueDate(pastDueDate);
                                        oleLoanDocument.setErrorMessage("The past and the current due data are same and hence item wasn't renewed.");
                                        finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                                    }
                                } catch (Exception e) {
                                    oleLoanDocument.setLoanDueDate(loanDueDate);
                                    oleLoanDocument.setPastDueDate(pastDueDate);
                                    if (e.getMessage().equalsIgnoreCase("No Fixed Due Date found for the renewal policy")) {
                                        oleLoanDocument.setErrorMessage("Item wasn't renewed. Invalid Fixed Due Date Mapping.");
                                        finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                                    } else {
                                        oleLoanDocument.setErrorMessage("Item wasn't renewed. " + e.getMessage());
                                        finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                                    }
                                }
                            } else {
                                oleLoanDocument.setLoanDueDate(loanDueDate);
                                oleLoanDocument.setPastDueDate(pastDueDate);
                                oleLoanDocument.setErrorMessage(finalDroolResponse.retrieveErrorMessage());
                                finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                            }
                        } else {
                            oleLoanDocument.setLoanDueDate(loanDueDate);
                            oleLoanDocument.setPastDueDate(pastDueDate);
                            oleLoanDocument.setErrorMessage("Item wasn't renewed as no circulation policy was found!");
                            finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                        }
                    }
                }else {
                    finalDroolResponse = new DroolsResponse();
                    oleLoanDocument.setErrorMessage("Item  wasn't renewed because the item is claims returned.");
                    finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
                }
            }else{
                finalDroolResponse = new DroolsResponse();
                oleLoanDocument.setErrorMessage("Item wasn't renewed. Items on indefinite loan do not need to be renewed.");
                finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
            }

        } else {
            finalDroolResponse = new DroolsResponse();
            OleLoanDocument oleLoanDocument = new OleLoanDocument();
            oleLoanDocument.setErrorMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_NT_LOAN) +" - Item Barcode("+itemBarcode+")");
            finalDroolResponse.getDroolsExchange().addToContext(itemBarcode, oleLoanDocument);
        }

        return finalDroolResponse;
    }

    private boolean IsIndefiniteDueDate(OleLoanDocument oleLoanDocument) {
        return oleLoanDocument.getLoanDueDate() == null ? true : false ;
    }

    private boolean isPastAndRenewDueDateSame(OleLoanDocument oleLoanDocument) throws Exception {
        Timestamp pastLoanDueDate = oleLoanDocument.getLoanDueDate();
        Date newLoanDueDate = oleLoanDocument.getPastDueDate();
        if (pastLoanDueDate != null) {
            return (new Date(pastLoanDueDate.getTime()).compareTo(newLoanDueDate) == 0 ? true : false);
        } else {
            throw new Exception("No Fixed Due Date found for the renewal policy");
        }
    }

    private List<OleLoanDocument> getLoanDocuments(String itemBarcode, OlePatronDocument olePatronDocument) {
        HashMap<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put("patronId", olePatronDocument.getOlePatronId());
        criteriaMap.put("itemId", itemBarcode);
        List<OleLoanDocument> loanDocuments =
                (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, criteriaMap);
        return loanDocuments;
    }

    private Integer IncrementRenewalCount(OleLoanDocument oleLoanDocument) {
        Integer numRenewals = 0;
        String numberOfRenewalsPreviously = oleLoanDocument.getNumberOfRenewals();
        if (StringUtils.isNotBlank(numberOfRenewalsPreviously)) {
            numRenewals = new Integer(numberOfRenewalsPreviously) + 1;
        }
        return numRenewals;
    }

    private DroolsResponse fireRules(OlePatronDocument olePatronDocument, OleLoanDocument oleLoanDocument, OleItemRecordForCirc oleItemRecordForCirc, NoticeInfo noticeInfo) {
        DroolsResponse droolsResponse = new DroolsResponse();
        List<Object> facts = new ArrayList<>();
        facts.add(oleLoanDocument);
        facts.add(olePatronDocument);
        facts.add(oleItemRecordForCirc);
        facts.add(droolsResponse);
        facts.add(noticeInfo);
        getCircUtilController().fireRules(facts, null, "renewal validation");
        return droolsResponse;
    }

    private OleItemRecordForCirc getOleItemRecordForCirc(ItemRecord itemRecord) {
        if (null == oleItemRecordForCirc) {
            oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, null);
        }
        return oleItemRecordForCirc;
    }

    private CircUtilController getCircUtilController() {
        if (null == circUtilController) {
            circUtilController = new CircUtilController();
        }
        return circUtilController;
    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public void setCircUtilController(CircUtilController circUtilController) {
        this.circUtilController = circUtilController;
    }

    public void setOleItemRecordForCirc(OleItemRecordForCirc oleItemRecordForCirc) {
        this.oleItemRecordForCirc = oleItemRecordForCirc;
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
    }

    private boolean isPatronExpirationGreaterThanToday(Timestamp expirationDate) {
        return expirationDate.compareTo(new Date()) > 0;
    }

    private boolean isPatronExpiringBeforeLoanDue(OleLoanDocument oleLoanDocument, Timestamp expirationDate) {
        return expirationDate.compareTo(oleLoanDocument.getLoanDueDate()) < 0;
    }
}
