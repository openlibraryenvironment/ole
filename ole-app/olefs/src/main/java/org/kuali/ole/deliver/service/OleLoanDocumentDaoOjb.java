package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.DeliverConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 6/11/14
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLoanDocumentDaoOjb extends PlatformAwareDaoBaseOjb {
    private static final Logger LOG = Logger.getLogger(OleLoanDocumentDaoOjb.class);

    public BusinessObjectService businessObjectService;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if(oleLoanDocumentDaoOjb == null){
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }

    public void setOleLoanDocumentDaoOjb(OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb) {
        this.oleLoanDocumentDaoOjb = oleLoanDocumentDaoOjb;
    }

    public Collection<Object> getOverdueLoanDocument() {
        Criteria criteria = new Criteria();
        criteria.addLessThan("loanDueDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        criteria.addColumnIsNull("REPMNT_FEE_PTRN_BILL_ID");
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;

    }


    public Collection<Object> getCourtesyLoanDocument(Integer interval) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("courtesyNoticeFlag", "N");
        Date formDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()), interval - 1);
        Date toDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()), interval + 1);
        criteria.addBetween("loanDueDate", new Timestamp(formDate.getTime()), new Timestamp(toDate.getTime()));
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getLoanDocumentsUsingItemIdsAndPatronId(String patronId, List<String> itemIds) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("patronId", patronId);
        criteria.addIn("itemUuid", itemIds);
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getPickUpLocationForCirculationDesk(OleCirculationDesk oleCirculationDesk){
        Criteria criteria = new Criteria();
        criteria.addEqualTo("circulationDeskId", oleCirculationDesk.getCirculationDeskId());
        criteria.addNotNull("circulationPickUpDeskLocation");
        QueryByCriteria query = QueryFactory.newQuery(OleCirculationDeskLocation.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getLoanDocumentsUsingItemBarcodeAndPatronIdForRenewal(String patronId, List<String> itemBarcode) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("patronId", patronId);
        criteria.addIn("itemId", itemBarcode);
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getHoldRequests(List<String> requestTypeIds) {
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeId", requestTypeIds);
        criteria.addEqualTo("borrowerQueuePosition", "1");
        criteria.addColumnIsNull("ONHLD_NTC_SNT_DT");
        String pickupLocation = getPickUpLocation();
        if (pickupLocation != null && !pickupLocation.trim().isEmpty()) {
            criteria.addEqualTo("PCKUP_LOC_ID", pickupLocation);
        }
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getOnHoldNotice(){
        Criteria criteria = new Criteria();
      /*  criteria.addColumnIsNull("ONHLD_NTC_SNT_DT");*/
        criteria.addEqualTo("oleDeliverRequestBo.borrowerQueuePosition", "1");
        criteria.addIsNull("oleDeliverRequestBo.onHoldNoticeSentDate");
        criteria.addEqualTo("noticeType", OLEConstants.ONHOLD_NOTICE);
        String pickupLocation = getPickUpLocation();
        if (pickupLocation != null && !pickupLocation.trim().isEmpty()) {
            criteria.addEqualTo("oleDeliverRequestBo.pickUpLocationId", pickupLocation);
        }
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getOnHoldNoticeByPickUpLocation(String pickupLocation){
        Criteria criteria = new Criteria();
       /* criteria.addColumnIsNull("ONHLD_NTC_SNT_DT");*/
        criteria.addEqualTo("oleDeliverRequestBo.borrowerQueuePosition", "1");
        criteria.addIsNull("oleDeliverRequestBo.onHoldNoticeSentDate");
        criteria.addEqualTo("noticeType",OLEConstants.ONHOLD_NOTICE);
        if (pickupLocation != null && !pickupLocation.trim().isEmpty()) {
            criteria.addEqualTo("oleDeliverRequestBo.pickUpLocationId", pickupLocation);
        }
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    /*public Collection<Object> getOnHoldExpirationNotices(){
        Criteria criteria = new Criteria();


    }*/



    public Collection<Object> getHoldRequestsByPickupLocation(List<String> requestTypeIds, String pickupLocationId) {
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeId", requestTypeIds);
        criteria.addEqualTo("borrowerQueuePosition", "1");
        criteria.addColumnIsNull("ONHLD_NTC_SNT_DT");
        if (pickupLocationId != null && !pickupLocationId.trim().isEmpty()) {
            criteria.addEqualTo("PCKUP_LOC_ID", pickupLocationId);
        }
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }



    public String getPickUpLocation() {
        String pickupLocation = null;
        Map<String, String> oleBatchBoMap = new HashMap<String, String>();
        oleBatchBoMap.put("jobTriggerName", "generateOnHoldNoticeJob");
        List<OleBatchJobBo> oleBatchJobBos = (List<OleBatchJobBo>) getBusinessObjectService().findMatching(OleBatchJobBo.class, oleBatchBoMap);
        if (oleBatchJobBos != null && oleBatchJobBos.size() > 0) {
            pickupLocation = oleBatchJobBos.get(0).getPickupLocation();
        }
        return pickupLocation;
    }

    public Collection<Object> getExpiredRequests() {
        Criteria criteria = new Criteria();
        criteria.addLessOrEqualThan("requestExpiryDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getRequestExpiredNotice() {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("noticeType",OLEConstants.REQUEST_EXPIRATION_NOTICE);
        criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getOnHoldExpiredNotice() {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("noticeType",OLEConstants.ONHOLD_EXPIRATION_NOTICE);
        criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public OLEDeliverNotice getOnHoldExpiredNotice(String requestId) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("noticeType", OLEConstants.ONHOLD_EXPIRATION_NOTICE);
        criteria.addEqualTo("requestId", requestId);
        criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        List<OLEDeliverNotice> oleDeliverNoticeList = (List<OLEDeliverNotice>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        if (CollectionUtils.isNotEmpty(oleDeliverNoticeList)){
            return oleDeliverNoticeList.get(0);
        }
        return null;
    }

    public Collection<Object> getOnHoldCourtesyNotice() {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("noticeType",OLEConstants.ONHOLD_COURTESY_NOTICE);
        criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public OleDeliverRequestBo getHoldExpiredRequests(String itemId) {
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeId", Arrays.asList("2", "4", "6"));
        criteria.addEqualTo("borrowerQueuePosition", "1");
        criteria.addEqualTo("itemId", itemId);
        criteria.addColumnNotNull("ONHLD_NTC_SNT_DT");
        criteria.addLessOrEqualThan("holdExpirationDate", new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        if (CollectionUtils.isNotEmpty(oleDeliverRequestBoList)){
            return oleDeliverRequestBoList.get(0);
        }
        return null;
    }




    public Collection<Object> getDeliverNotices(String noticeType) {
        Criteria criteria = new Criteria();
        Date fromDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()), -1);
        Date toDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()), 1);
        if (noticeType.equals(OLEConstants.OVERDUE_NOTICE)) {
            List<String> noticeTypes = new ArrayList<>();
            noticeTypes.add(noticeType);
            noticeTypes.add(OLEConstants.NOTICE_LOST);
            criteria.addIn("noticeType", noticeTypes);
            criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(new Date().getTime()));
        } else {
            criteria.addEqualTo("noticeType", noticeType);
            criteria.addBetween("noticeToBeSendDate", fromDate, toDate);
        }
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<OleLoanDocument> getLoanDocumentsForNoticeGeneration(String noticeType, Collection<Object> deliverNotices) {
        List<OleLoanDocument> oleDeliverLoanDocuments = new ArrayList<>();
        List<String> loanIds = getLoanIds(deliverNotices);
        if (loanIds.size() > 0) {
            Criteria criteria = new Criteria();
            criteria.addColumnIn("LOAN_TRAN_ID", loanIds);
            if (noticeType.equals(OLEConstants.OVERDUE_NOTICE) || noticeType.equals(OLEConstants.NOTICE_LOST)) {
                criteria.addLessOrEqualThan("loanDueDate", new Timestamp(System.currentTimeMillis()));
            } else if (noticeType.equals(OLEConstants.COURTESY_NOTICE)) {
                criteria.addGreaterOrEqualThan("loanDueDate", new Timestamp(System.currentTimeMillis()));
            }
            QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
            query.addOrderBy("patronId");
            Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
            for (Object oleLoanDocument : results) {
                oleDeliverLoanDocuments.add((OleLoanDocument) oleLoanDocument);
            }
        }
        return oleDeliverLoanDocuments;
    }

    /**
     * This method is used to get the loan ids from the notice table
     *
     * @param deliverNotices
     * @return
     */
    private List<String> getLoanIds(Collection<Object> deliverNotices) {
        List<String> loanIds = new ArrayList<String>();
        OLEDeliverNotice oleDeliverNotice = null;
        if (deliverNotices != null && deliverNotices.size() > 0) {
            for (Object object : deliverNotices) {
                oleDeliverNotice = (OLEDeliverNotice) object;
                loanIds.add(oleDeliverNotice.getLoanId());
            }
        }
        return loanIds;
    }

    /**
     * @param deliverNotices
     * @param loanDocuments
     * @return
     */
    public List<OleLoanDocument> processLoanDocumentWithNoticeData(Collection<Object> deliverNotices, List<OleLoanDocument> loanDocuments) {
        List<Object> deliverNoticesList = new ArrayList(deliverNotices);
        for (OleLoanDocument loanDocument : loanDocuments) {
            for (Object deliverNotice : deliverNoticesList) {
                OLEDeliverNotice oleDeliverNotice = (OLEDeliverNotice) deliverNotice;
                if (oleDeliverNotice.getLoanId().equals(loanDocument.getLoanId())) {
                    if (oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.NOTICE_LOST)) {
                        loanDocument.setReplacementBill(oleDeliverNotice.getReplacementFeeAmount());
                    }
                    loanDocument.getDeliverNotices().add(oleDeliverNotice);
                }
            }
        }
        return loanDocuments;
    }


    /**
     * This method is used to get the loan documents which does not have the over due notice entry
     *
     * @return
     */
    public List<OleLoanDocument> getUnprocessedOverdueLoanDocuments() {
        LOG.info("Inside the getUnprocessedOverdueLoanDocuments");
        Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
        Map<String, OleLoanDocument> loanDocumentMap = new HashMap<String, OleLoanDocument>();
        Map<String, OleCirculationDesk> circulationDeskMap = new HashMap<String, OleCirculationDesk>();

        List<Map<String, OleLoanDocument>> loanDocumentMapList = new ArrayList<Map<String, OleLoanDocument>>();

        Criteria criteria = new Criteria();
        // get the loan documents from the table which does not have the replacement fee
        Long startTimeToGetTheLoanDocument = System.currentTimeMillis();
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        criteria.addColumnIsNull("REPMNT_FEE_PTRN_BILL_ID");
        query.addOrderBy("patronId");
        Collection loanDocumentResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        List<OleLoanDocument> loanDocumentsList = new ArrayList(loanDocumentResultSet);
        Long endTimeToGetTheLoanDocument = System.currentTimeMillis();
        Long timeDifferenceToGetTheLoanDocument = endTimeToGetTheLoanDocument - startTimeToGetTheLoanDocument;
        LOG.info("Time taken to get the loan documents in milliseconds  : " + timeDifferenceToGetTheLoanDocument);
        //get existing overdue notice
        criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria, true);
        criteria.addEqualTo("noticeType", OLEConstants.OVERDUE_NOTICE);
        Collection loanOverdueNoticeResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OLEDeliverNotice> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);

        //get existing courtesy notice
        criteria = new Criteria();
        QueryByCriteria noticeCourtesyQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria, true);
        criteria.addEqualTo("noticeType", OLEConstants.COURTESY_NOTICE);
        Collection loanCourtesyNoticeResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeCourtesyQuery);
        List<OLEDeliverNotice> oleCourtesyDeliverNoticeList = new ArrayList(loanCourtesyNoticeResultSet);

        criteria = new Criteria();
        QueryByCriteria noticeLostQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria, true);
        criteria.addEqualTo("noticeType", OLEConstants.NOTICE_LOST);
        Collection noticeLostQueryResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeLostQuery);
        List<OLEDeliverNotice> noticeLostList = new ArrayList(noticeLostQueryResultSet);
        //get all the circulation desk from the db
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findAll(OleCirculationDesk.class);
        if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
            for (OleCirculationDesk oleCirculationDesk : oleCirculationDeskList) {
                circulationDeskMap.put(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk);
            }
        }
        LOG.info("Number of loan records before filtering : " + loanDocumentsList.size());
        OlePatronDocument patronDocument = null;
        Map<String, String> patronMap = new HashMap<String, String>();

        List<OlePatronDocument> olePatronDocuments = null;
        //iterate over all the loan documents to set the circulation desk and borrower type
        if (loanDocumentsList != null && loanDocumentsList.size() > 0) {
            for (OleLoanDocument oleLoanDocument : loanDocumentsList) {
                try {
                    if (oleLoanDocument.getOlePatron() != null) {
                        patronDocument = oleLoanDocument.getOlePatron();
                    } else {
                        patronMap = new HashMap<String, String>();
                        patronMap.put("olePatronId", oleLoanDocument.getPatronId());
                        olePatronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
                        if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
                            patronDocument = olePatronDocuments.get(0);
                        }
                    }
                    if (patronDocument == null) {
                        LOG.info("Patron not found for the given patron id : " + oleLoanDocument.getPatronId() + "Loan Id " + oleLoanDocument.getLoanId());
                        throw new Exception("Patron Not Found For The Given Patron Id");
                    } else {
                        loanDocumentMap.put(oleLoanDocument.getLoanId(), oleLoanDocument);
                        oleLoanDocument.setBorrowerTypeCode(patronDocument.getOleBorrowerType().getBorrowerTypeCode());
                        oleLoanDocument.setOleCirculationDesk(circulationDeskMap.get(oleLoanDocument.getCirculationLocationId()));
                    }
                } catch (Exception e) {
                    LOG.info("Exception occured while setting the borrower type information to the loan document");
                    LOG.error(e, e);
                }
            }
        }
        //remove the loan document for the existing processed loans
        if (oleOverDueDeliverNoticeList != null && oleOverDueDeliverNoticeList.size() > 0) {
            for (OLEDeliverNotice oleDeliverNotice : oleOverDueDeliverNoticeList) {
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }

        if (oleCourtesyDeliverNoticeList != null && oleCourtesyDeliverNoticeList.size() > 0) {
            for (OLEDeliverNotice oleDeliverNotice : oleCourtesyDeliverNoticeList) {
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }

        if (noticeLostList != null && noticeLostList.size() > 0) {
            for (OLEDeliverNotice oleDeliverNotice : noticeLostList) {
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }
        Collection loanList = loanDocumentMap.values();
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList(loanList);
        LOG.info("Number of loan records after filtering : " + oleLoanDocumentList.size());
        Long endTime = System.currentTimeMillis();
        Long timeDifference = endTime - startTime;
        LOG.info("Time taken for identifying the loan records in milliseconds " + timeDifference);
        return oleLoanDocumentList;
    }

    public List<OleDeliverRequestBo> getDeliverRequests(String patronBarcode) {
        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria, true);
        criteria.addEqualTo("borrowerBarcode", patronBarcode);
        Collection loanOverdueNoticeResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OleDeliverRequestBo> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return oleOverDueDeliverNoticeList;
    }

    public List<OleLoanDocument> getDeliverLoans(String patronId) {

        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OleLoanDocument.class, criteria, true);
        criteria.addEqualTo("patronId", patronId);
        Collection loanOverdueNoticeResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OleLoanDocument> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return oleOverDueDeliverNoticeList;
    }


    public List<PatronBillPayment> getPatronBillPayments(String patronId) {
        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(PatronBillPayment.class, criteria, true);
        criteria.addEqualTo("patronId", patronId);
        Collection loanOverdueNoticeResultSet = getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<PatronBillPayment> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return oleOverDueDeliverNoticeList;
    }


    public Collection<Object> getRequestTypeForHoldNotice(List<String> requestTypeCodes) {
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeCode", requestTypeCodes);
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestType.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<String> getRequestTypeIds(Collection<Object> requestTypeObjects) {
        List<String> requestTypeIds = new ArrayList<String>();
        OleDeliverRequestType oleDeliverRequestType = null;
        if (requestTypeObjects != null && requestTypeObjects.size() > 0) {
            for (Object obj : requestTypeObjects) {
                oleDeliverRequestType = (OleDeliverRequestType) obj;
                requestTypeIds.add(oleDeliverRequestType.getRequestTypeId());
            }
        }
        return requestTypeIds;
    }


    public List<String> getRequestTypeIdsForHoldNotice(List<String> requestTypeCodes) {
        Collection<Object> requestTypes = getRequestTypeForHoldNotice(requestTypeCodes);
        return getRequestTypeIds(requestTypes);
    }

    public Collection<PatronBillPayment> getPatronBills(String patronId) {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("unPaidBalance", OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        criteria.addEqualTo("patronId", patronId);
        QueryByCriteria query = QueryFactory.newQuery(PatronBillPayment.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return new ArrayList<PatronBillPayment>(results);
    }

    public Collection<OleDeliverRequestHistoryRecord> getDeliverRequestHistoryRecords(String itemBarcode) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(OLEConstants.ITEM_BARCODE, itemBarcode);
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestHistoryRecord.class, criteria);
        query.addOrderByDescending(OLEConstants.ARCHIVE_DATE);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<OleLoanDocument> getLaonDocumentsFromLaondId(List<String> loanIds) {
        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OleLoanDocument.class, criteria, true);
        criteria.addColumnIn("LOAN_TRAN_ID", loanIds);
        criteria.addOrderBy("patronId");
        return (List<OleLoanDocument>)getPersistenceBrokerTemplate().getCollectionByQuery
                (noticeOverdueQuery);
    }

    public List<OleDeliverRequestBo> getDeliverRequestBos(List<String> itemIds) {
        if (!itemIds.isEmpty()) {
            Criteria criteria = new Criteria();
            QueryByCriteria oleDeliverRequestBoQuery = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria, true);
            criteria.addColumnIn("ITM_ID", itemIds);
            return (List<OleDeliverRequestBo>)getPersistenceBrokerTemplate().getCollectionByQuery
                    (oleDeliverRequestBoQuery);
        }
        return new ArrayList<>();
    }

    public Collection<Object> getDeliverNoticeHistory(Criteria criteria) {
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNoticeHistory.class, criteria);
        Collection results = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<FeeType> getFeeTypes(Criteria criteria) {
        QueryByCriteria query = QueryFactory.newQuery(FeeType.class, criteria);
        List<FeeType> results = (List<FeeType>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<FeeType> getFeeTypeDocument(Criteria criteria) {
        QueryByCriteria query = QueryFactory.newQuery(FeeType.class, criteria);
        List<FeeType> results = (List<FeeType>)getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;

    }

    public OleDeliverRequestBo getPrioritizedRequest(String itemId) {
        Criteria criteria = new Criteria();
        criteria.addGreaterThan("requestExpiryDate", new Timestamp(System.currentTimeMillis()));
        criteria.addEqualTo("itemId", itemId);
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerQueuePosition");
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return CollectionUtils.isNotEmpty(oleDeliverRequestBoList) ? oleDeliverRequestBoList.get(0) : null;
    }

    public List<OleDeliverRequestHistoryRecord> getExpiredRequest(String itemBarcode, Date loanCreatedDate) {
        List<OleDeliverRequestHistoryRecord> oleDeliverRequestHistoryRecords = new ArrayList<>();
        if(loanCreatedDate != null) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo(OLEConstants.ITEM_BARCODE, itemBarcode);
            criteria.addEqualTo(OLEConstants.OleDeliverRequest.REQUEST_OUTCOME_STATUS, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
            java.sql.Date loanCreateDate = new java.sql.Date(loanCreatedDate.getTime());
            criteria.addBetween(OLEConstants.ARCHIVE_DATE, loanCreateDate, new java.sql.Date(System.currentTimeMillis()));
            QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestHistoryRecord.class, criteria);
            oleDeliverRequestHistoryRecords = (List<OleDeliverRequestHistoryRecord>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        }
        return oleDeliverRequestHistoryRecords;
    }

    public List<OleCirculationHistory> getReturnedItem(Criteria criteria) {
        QueryByCriteria query = QueryFactory.newQuery(OleCirculationHistory.class, criteria);
        List<OleCirculationHistory>  results = ( List<OleCirculationHistory> )getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public OleCirculationHistory retrieveCircHistoryRecord(String itemUUID ) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("itemUuid", itemUUID);
        criteria.addNotNull("checkInDate");
        criteria.addOrderByDescending("checkInDate");
        QueryByCriteria query = QueryFactory.newQuery(OleCirculationHistory.class, criteria);
        List<OleCirculationHistory> results = (List<OleCirculationHistory>) getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return CollectionUtils.isNotEmpty(results) ? results.get(0) : null;
    }

    public List<OLEDeliverNoticeHistory> getLaonHistoryRecords(java.sql.Date loanHistoryToDate) {
        LOG.info("getLaonHistoryRecords() started");
        Criteria criteria = new Criteria();

        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            if (loanHistoryToDate != null) {
                criteria.addLessOrEqualThan(DeliverConstants.NOTICE_SENT_DATE, loanHistoryToDate);
            }
        }
        else {
            if (loanHistoryToDate != null) {
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(loanHistoryToDate);
                criteria.addLessOrEqualThan(DeliverConstants.NOTICE_SENT_DATE ,formatDateForOracle(date));
            }
        }
        QueryByCriteria qbc = new QueryByCriteria(OLEDeliverNoticeHistory.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getLaonHistoryRecords() Query criteria is " + criteria.toString());
        }
        List<OLEDeliverNoticeHistory>  oleDeliverNoticeHistoryList = (List<OLEDeliverNoticeHistory>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.info("getLaonHistoryRecords() ended.");
        return oleDeliverNoticeHistoryList;
    }

    public List<OleRenewalHistory> getRenewalHistoryRecords(java.sql.Date renewalHistoryToDate) {
        LOG.info("getRenewalHistoryRecords() started");
        Criteria criteria = new Criteria();

        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            if (renewalHistoryToDate != null) {
                criteria.addLessOrEqualThan(DeliverConstants.ITEM_RENEWED_DATE, renewalHistoryToDate);
            }
        }
        else {
            if (renewalHistoryToDate != null) {
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(renewalHistoryToDate);
                criteria.addLessOrEqualThan(DeliverConstants.ITEM_RENEWED_DATE ,formatDateForOracle(date));
            }
        }
        QueryByCriteria qbc = new QueryByCriteria(OleRenewalHistory.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getRenewalHistoryRecords() Query criteria is " + criteria.toString());
        }
        List<OleRenewalHistory>  renewalHistoryList = (List<OleRenewalHistory>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.info("getRenewalHistoryRecords() ended.");
        return renewalHistoryList;
    }

    public List<OLEReturnHistoryRecord> getReturnHistoryRecords(java.sql.Date returnHistoryToDate) {
        LOG.info("getReturnHistoryRecords() started");
        Criteria criteria = new Criteria();

        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            if (returnHistoryToDate != null) {
                criteria.addLessOrEqualThan(DeliverConstants.ITEM_RETURNED_DATE, returnHistoryToDate);
            }
        }
        else {
            if (returnHistoryToDate != null) {
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(returnHistoryToDate);
                criteria.addLessOrEqualThan(DeliverConstants.ITEM_RETURNED_DATE ,formatDateForOracle(date));
            }
        }
        QueryByCriteria qbc = new QueryByCriteria(OLEReturnHistoryRecord.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getReturnHistoryRecords() Query criteria is " + criteria.toString());
        }
        List<OLEReturnHistoryRecord>  returnHistoryList = (List<OLEReturnHistoryRecord>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.info("getReturnHistoryRecords() ended.");
        return returnHistoryList;
    }

    public List<OleDeliverRequestHistoryRecord> getRequestHistoryRecords(java.sql.Date requestHistoryToDate) {
        LOG.info("getRequuestHistoryRecords() started");
        Criteria criteria = new Criteria();

        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            if (requestHistoryToDate != null) {
                criteria.addLessOrEqualThan(DeliverConstants.REQUEST_CREATED_DATE, requestHistoryToDate);
            }
        }
        else {
            if (requestHistoryToDate != null) {
                SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(requestHistoryToDate);
                criteria.addLessOrEqualThan(DeliverConstants.REQUEST_CREATED_DATE ,formatDateForOracle(date));
            }
        }
        QueryByCriteria qbc = new QueryByCriteria(OleDeliverRequestHistoryRecord.class, criteria);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getRequuestHistoryRecords() Query criteria is " + criteria.toString());
        }
        List<OleDeliverRequestHistoryRecord>  returnHistoryList = (List<OleDeliverRequestHistoryRecord>) getPersistenceBrokerTemplate().getCollectionByQuery(qbc);
        LOG.info("getRequuestHistoryRecords() ended.");
        return returnHistoryList;
    }

    private String formatDateForOracle(String historyDate) {
        String forOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(historyDate);
        return forOracle;
    }

    protected String getProperty(String property) {
        return ConfigContext.getCurrentContextConfig().getProperty(property);
    }

}
