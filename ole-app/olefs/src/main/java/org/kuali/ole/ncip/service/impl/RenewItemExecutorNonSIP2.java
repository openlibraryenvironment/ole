package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ncip.bo.OLERenewItem;
import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.ole.ncip.converter.OLERenewItemConverter;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by pvsubrah on 6/23/15.
 */
public class RenewItemExecutorNonSIP2 implements Callable {

    private static final Logger LOG = Logger.getLogger(RenewItemExecutorNonSIP2.class);

    private final String operatorId;
    private final String itemBarcode;
    private final OlePatronDocument olePatronDocument;

    private BusinessObjectService businessObjectService;
    private CircUtilController circUtilController;
    private OleItemRecordForCirc oleItemRecordForCirc;


    public RenewItemExecutorNonSIP2(OlePatronDocument olePatronDocument, String operatorId, String itemBarcode) {
        this.olePatronDocument = olePatronDocument;
        this.operatorId = operatorId;
        this.itemBarcode = itemBarcode;
    }

    @Override
    public Object call() throws Exception {
        long startTime = System.currentTimeMillis();
        OLERenewItem renewedItem = renew();
        long endTime = System.currentTimeMillis();
        LOG.info("Time taken to process renew item for barcode: " + itemBarcode + " is: " + (endTime-startTime) + "ms");
        return renewedItem;
    }

    public OLERenewItem renew() {
        OLERenewItem oleRenewItem = new OLERenewItem();

        List<OleLoanDocument> loanDocuments = getLoanDocuments(itemBarcode, olePatronDocument);

        if (loanDocuments.size() > 0) {
            OleLoanDocument oleLoanDocument = loanDocuments.get(0);
            oleLoanDocument.setPastDueDate(oleLoanDocument.getLoanDueDate());

            ItemRecord itemRecord = getCircUtilController().getItemRecordByBarcode(itemBarcode);
            OleItemRecordForCirc oleItemRecordForCirc = getOleItemRecordForCirc(itemRecord);
            ErrorMessage itemValidationErrors = new ErrorMessage();
            NoticeInfo noticeInfo = new NoticeInfo();

            fireRules(olePatronDocument, oleLoanDocument, oleItemRecordForCirc, itemValidationErrors, noticeInfo);

            if (StringUtils.isBlank(itemValidationErrors.getErrorMessage())) {
                if (!isPastAndRenewDueDateSame(oleLoanDocument)) {
                    try {
                        Integer numRenewals = IncrementRenewalCount(oleLoanDocument);
                        oleLoanDocument.setNumberOfRenewals(numRenewals.toString());

                        deleteExistingNotices(oleLoanDocument);

                        List<OLEDeliverNotice> oleDeliverNotices = getCircUtilController().processNotices(noticeInfo, oleLoanDocument);
                        oleLoanDocument.setDeliverNotices(oleDeliverNotices);

                        if (null != oleLoanDocument.getLoanId()) {
                            Long solrStartingTime = System.currentTimeMillis();
                            Boolean solrUpdateResults = getCircUtilController().updateItemInfoInSolr(oleLoanDocument, itemRecord.getItemId());
                            Long solrEndTimme = System.currentTimeMillis();
                            Long timeTakenForSolrUpdate = solrEndTimme-solrStartingTime;
                            LOG.info("The Time Taken for Solr Update : "+timeTakenForSolrUpdate);
                            if (solrUpdateResults) {
                                Long dbStartingTime = System.currentTimeMillis();
                                try {
                                    getBusinessObjectService().save(oleLoanDocument);
                                } catch (Exception e) {
                                    LOG.error(e.getMessage());
                                    Timestamp pastDueDate = new Timestamp(oleLoanDocument.getPastDueDate().getTime());
                                    oleLoanDocument.setLoanDueDate(pastDueDate);
                                    int previousNumRenewals = Integer.parseInt(oleLoanDocument.getNumberOfRenewals());
                                    oleLoanDocument.setNumberOfRenewals(new Integer(previousNumRenewals -1).toString());
                                    getCircUtilController().updateItemInfoInSolr(oleLoanDocument, itemRecord.getItemId());
                                    oleRenewItem.setMessage("Item wasn't renewed.");
                                    oleRenewItem.setItemBarcode(itemBarcode);
                                }
                                Long dbEndTimme = System.currentTimeMillis();
                                Long timeTakenForDbUpdate = dbEndTimme-dbStartingTime;
                                LOG.info("The Time Taken for database update : " + timeTakenForDbUpdate);
                                oleRenewItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RENEW_SUCCESS)+" - Item Barcode("+itemBarcode+")");
                                oleRenewItem.setItemBarcode(itemBarcode);
                                oleRenewItem.setPastDueDate(oleLoanDocument.getPastDueDate().toString());
                                oleRenewItem.setNewDueDate(oleLoanDocument.getLoanDueDate().toString());
                                oleRenewItem.setRenewalCount(oleLoanDocument.getNumberOfRenewals());
                            }
                        }
                    } catch (Exception e) {
                        oleRenewItem.setMessage(e.getMessage());
                        oleRenewItem.setItemBarcode(itemBarcode);
                    }
                } else {
                    oleRenewItem.setMessage("Item wasn't renewed.");
                    oleRenewItem.setItemBarcode(itemBarcode);
                }

            } else {
                oleRenewItem.setMessage(itemValidationErrors.getErrorMessage());
                oleRenewItem.setItemBarcode(itemBarcode);
            }

        } else {
            oleRenewItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_NT_LOAN) +" - Item Barcode("+itemBarcode+")");
            oleRenewItem.setItemBarcode(itemBarcode);
        }

        return oleRenewItem;
    }

    private boolean isPastAndRenewDueDateSame(OleLoanDocument oleLoanDocument) {
        Timestamp pastLoanDueDate = oleLoanDocument.getLoanDueDate();
        Date newLoanDueDate = oleLoanDocument.getPastDueDate();
        return (new Date(pastLoanDueDate.getTime()).compareTo(newLoanDueDate) == 0 ? true : false);
    }

    private void deleteExistingNotices(OleLoanDocument oleLoanDocument) {
        getBusinessObjectService().delete(oleLoanDocument.getDeliverNotices());
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

    private void fireRules(OlePatronDocument olePatronDocument, OleLoanDocument oleLoanDocument, OleItemRecordForCirc oleItemRecordForCirc, ErrorMessage itemValidationErrors, NoticeInfo noticeInfo) {
        List<Object> facts = new ArrayList<>();
        facts.add(oleLoanDocument);
        facts.add(olePatronDocument);
        facts.add(oleItemRecordForCirc);
        facts.add(itemValidationErrors);
        facts.add(noticeInfo);
        getCircUtilController().fireRulesForPatron(facts, null, "renewal validation");
    }

    private OleItemRecordForCirc getOleItemRecordForCirc(ItemRecord itemRecord) {
        if (null == oleItemRecordForCirc) {
            oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord);
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

    private String handleXMLResponse(OLERenewItemList oleRenewItemList) {
        String response;
        response = new OLERenewItemConverter().generateRenewItemListXml(oleRenewItemList);
        return response;
    }
}
