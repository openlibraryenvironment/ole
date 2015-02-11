package org.kuali.ole.deliver.service;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import org.apache.ojb.broker.PersistenceBrokerFactory;
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

    public BusinessObjectService getBusinessObjectService() {
     if(businessObjectService ==null){
         businessObjectService = KRADServiceLocator.getBusinessObjectService();
     }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public Collection<Object> getOverdueLoanDocument(){
        Criteria criteria = new Criteria();
        criteria.addLessThan("loanDueDate",new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        criteria.addColumnIsNull("REPMNT_FEE_PTRN_BILL_ID");
        query.addOrderBy("patronId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;

    }


    public Collection<Object> getCourtesyLoanDocument(Integer interval){
        Criteria criteria = new Criteria();
        criteria.addEqualTo("courtesyNoticeFlag","N");
        Date formDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()), interval-1);
        Date toDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()),interval+1);
        criteria.addBetween("loanDueDate",new Timestamp(formDate.getTime()),new Timestamp(toDate.getTime()));
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        query.addOrderBy("patronId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getLoanDocumentsUsingItemIdsAndPatronId(String patronId,List<String> itemIds){
        Criteria criteria = new Criteria();
        criteria.addEqualTo("patronId",patronId);
        criteria.addIn("itemUuid",itemIds);
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getLoanDocumentsUsingItemBarcodeAndPatronIdForRenewal(String patronId,List<String> itemBarcode){
        Criteria criteria = new Criteria();
        criteria.addEqualTo("patronId",patronId);
        criteria.addIn("itemId",itemBarcode);
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public Collection<Object> getHoldRequests(List<String> requestTypeIds){
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeId",requestTypeIds);
        criteria.addEqualTo("borrowerQueuePosition","1");
        criteria.addColumnIsNull("ONHLD_NTC_SNT_DT");
        String pickupLocation = getPickUpLocation();
        if(pickupLocation!=null && !pickupLocation.trim().isEmpty()){
         criteria.addEqualTo("PCKUP_LOC_ID",pickupLocation);
        }
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public String getPickUpLocation(){
        String pickupLocation = null;
        Map<String,String> oleBatchBoMap = new HashMap<String,String>();
        oleBatchBoMap.put("jobTriggerName","generateOnHoldNoticeJob");
        List<OleBatchJobBo> oleBatchJobBos = (List<OleBatchJobBo>)getBusinessObjectService().findMatching(OleBatchJobBo.class,oleBatchBoMap);
        if(oleBatchJobBos != null && oleBatchJobBos.size()>0){
            pickupLocation = oleBatchJobBos.get(0).getPickupLocation();
        }
        return pickupLocation;
    }

    public Collection<Object> getExpiredRequests(){
        Criteria criteria = new Criteria();
        criteria.addLessOrEqualThan("requestExpiryDate",new Timestamp(System.currentTimeMillis()));
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria);
        query.addOrderBy("borrowerId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }


    public Collection<Object> getDeliverNotices(String noticeType){
        Criteria criteria = new Criteria();
        Date fromDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()),-1);
        Date toDate = DateUtil.addDays(new Timestamp(System.currentTimeMillis()),1);
        if(noticeType.equals(OLEConstants.NOTICE_OVERDUE)){
        List<String> noticeTypes = new ArrayList<>();
            noticeTypes.add(noticeType);
            noticeTypes.add(OLEConstants.NOTICE_LOST);
             criteria.addIn("noticeType",noticeTypes);
            criteria.addLessOrEqualThan("noticeToBeSendDate", new Timestamp(new Date().getTime()));
        }else{
            criteria.addEqualTo("noticeType",noticeType);
            criteria.addBetween("noticeToBeSendDate", fromDate,toDate);
        }
        QueryByCriteria query = QueryFactory.newQuery(OLEDeliverNotice.class, criteria);
        query.addOrderBy("patronId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

    public List<OleLoanDocument> getLoanDocumentsForNoticeGeneration(String noticeType){
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();
        List<OleLoanDocument> oleDeliverLoanDocuments = new ArrayList<OleLoanDocument>();
        Collection<Object> deliverNotices = getDeliverNotices(noticeType);
        List<String> loanIds = getLoanIds(deliverNotices);
        if(loanIds.size()>0){
        Criteria criteria = new Criteria();
        criteria.addColumnIn("LOAN_TRAN_ID", loanIds);
            if(!noticeType.equals(OLEConstants.NOTICE_COURTESY)){
        criteria.addLessOrEqualThan("loanDueDate",new Timestamp(System.currentTimeMillis()));
            } else if(noticeType.equals(OLEConstants.NOTICE_COURTESY)){
                criteria.addGreaterOrEqualThan("loanDueDate", new Timestamp(System.currentTimeMillis()));
            }
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        query.addOrderBy("patronId");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
         if(results.size()>0){
         oleDeliverLoanDocuments = processLoanDocumentWithNoticeData(deliverNotices,results);
         }
        }
        return oleDeliverLoanDocuments;
    }

    /**
     * This method is used to get the loan ids from the notice table
     * @param deliverNotices
     * @return
     */
    private List<String> getLoanIds( Collection<Object> deliverNotices){
        List<String> loanIds = new ArrayList<String>();
        OLEDeliverNotice oleDeliverNotice = null;
        if(deliverNotices!=null && deliverNotices.size()>0){
        for(Object object : deliverNotices){
            oleDeliverNotice = (OLEDeliverNotice)object;
            loanIds.add(oleDeliverNotice.getLoanId());
        }
        }
        return loanIds;
    }

    /**
     *
     * @param deliverNotices
     * @param loanDocuments
     * @return
     */
    private List<OleLoanDocument> processLoanDocumentWithNoticeData(Collection<Object> deliverNotices,Collection<Object> loanDocuments){
        List<OleLoanDocument> oleDeliverLoanDocuments = new ArrayList<OleLoanDocument>();
        OLEDeliverNotice oleDeliverNotice = null;
        OleLoanDocument oleLoanDocument = null;
        List<Object> loanDocumentsList = new ArrayList(loanDocuments);
        List<Object> deliverNoticesList = new ArrayList(deliverNotices);
        Map<String,OleLoanDocument> loanMap = new HashMap<String,OleLoanDocument>();
        for(int i =0;i<loanDocumentsList.size();i++){
            oleDeliverNotice = (OLEDeliverNotice)deliverNoticesList.get(i);
            oleLoanDocument = (OleLoanDocument) loanDocumentsList.get(i);
            if(loanMap.get(oleLoanDocument.getLoanId())==null){
               if(oleDeliverNotice.getLoanId().equals(oleLoanDocument.getLoanId())){
                   oleLoanDocument.setReplacementBill(oleDeliverNotice.getReplacementFeeAmount());
                   oleLoanDocument.setNoticeType(oleDeliverNotice.getNoticeType());
                   oleLoanDocument.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
                   oleLoanDocument.setOleDeliverNotice(oleDeliverNotice);
               } else for(int j=0;j<deliverNoticesList.size();j++){
                    oleDeliverNotice = (OLEDeliverNotice)deliverNoticesList.get(j);
                    oleLoanDocument = (OleLoanDocument) loanDocumentsList.get(i);
                    if(oleDeliverNotice.getLoanId().equals(oleLoanDocument.getLoanId())){
                        oleLoanDocument.setReplacementBill(oleDeliverNotice.getReplacementFeeAmount());
                        oleLoanDocument.setNoticeType(oleDeliverNotice.getNoticeType());
                        oleLoanDocument.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
                        oleLoanDocument.setOleDeliverNotice(oleDeliverNotice);
                        break;
                    }
            }
                loanMap.put(oleLoanDocument.getLoanId(),oleLoanDocument);
                oleDeliverLoanDocuments.add(oleLoanDocument);
            }
            }


    return oleDeliverLoanDocuments;
    }


    /**
     * This method is used to get the loan documents which does not have the over due notice entry
     * @return
     */
    public List<OleLoanDocument> getUnprocessedOverdueLoanDocuments(){
        LOG.info("Inside the getUnprocessedOverdueLoanDocuments");
        Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
        Map<String,OleLoanDocument> loanDocumentMap = new HashMap<String,OleLoanDocument>();
        Map<String,OleCirculationDesk> circulationDeskMap = new HashMap<String,OleCirculationDesk>();

        List<Map<String,OleLoanDocument>> loanDocumentMapList = new ArrayList<Map<String,OleLoanDocument>>();

        Criteria criteria = new Criteria();
        // get the loan documents from the table which does not have the replacement fee
        Long startTimeToGetTheLoanDocument = System.currentTimeMillis();
        QueryByCriteria query = QueryFactory.newQuery(OleLoanDocument.class, criteria);
        criteria.addColumnIsNull("REPMNT_FEE_PTRN_BILL_ID");
        query.addOrderBy("patronId");
        Collection loanDocumentResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        List<OleLoanDocument> loanDocumentsList = new ArrayList(loanDocumentResultSet);
        Long endTimeToGetTheLoanDocument = System.currentTimeMillis();
        Long timeDifferenceToGetTheLoanDocument = endTimeToGetTheLoanDocument-startTimeToGetTheLoanDocument;
        LOG.info("Time taken to get the loan documents in milliseconds  : " + timeDifferenceToGetTheLoanDocument);
        //get existing overdue notice
        criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria,true);
        criteria.addEqualTo("noticeType",OLEConstants.NOTICE_OVERDUE);
        Collection loanOverdueNoticeResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OLEDeliverNotice> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);

        //get existing courtesy notice
        criteria = new Criteria();
        QueryByCriteria noticeCourtesyQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria,true);
        criteria.addEqualTo("noticeType",OLEConstants.NOTICE_COURTESY);
        Collection loanCourtesyNoticeResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeCourtesyQuery);
        List<OLEDeliverNotice> oleCourtesyDeliverNoticeList = new ArrayList(loanCourtesyNoticeResultSet);

        criteria = new Criteria();
        QueryByCriteria noticeLostQuery = QueryFactory.newQuery(OLEDeliverNotice.class, criteria,true);
        criteria.addEqualTo("noticeType",OLEConstants.NOTICE_LOST);
        Collection noticeLostQueryResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeLostQuery);
        List<OLEDeliverNotice> noticeLostList = new ArrayList(noticeLostQueryResultSet);
        //get all the circulation desk from the db
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>)getBusinessObjectService().findAll(OleCirculationDesk.class);
        if(oleCirculationDeskList!=null && oleCirculationDeskList.size()>0){
           for(OleCirculationDesk oleCirculationDesk : oleCirculationDeskList){
               circulationDeskMap.put(oleCirculationDesk.getCirculationDeskId(),oleCirculationDesk);
           }
        }
      LOG.info("Number of loan records before filtering : "  +loanDocumentsList.size());
        OlePatronDocument patronDocument = null;
        Map<String,String> patronMap = new HashMap<String,String>();

       List<OlePatronDocument> olePatronDocuments = null;
        //iterate over all the loan documents to set the circulation desk and borrower type
        if(loanDocumentsList!=null && loanDocumentsList.size()>0){
            for(OleLoanDocument oleLoanDocument : loanDocumentsList){
                try{
                if(oleLoanDocument.getOlePatron()!=null){
                       patronDocument = oleLoanDocument.getOlePatron();
                 }else{
                       patronMap = new HashMap<String,String>();
                    patronMap.put("olePatronId",oleLoanDocument.getPatronId());
                    olePatronDocuments = (List<OlePatronDocument>)getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
                    if(olePatronDocuments!=null && olePatronDocuments.size()>0){
                        patronDocument = olePatronDocuments.get(0);
                    }
                }
                    if(patronDocument == null){
                        LOG.info("Patron not found for the given patron id : "+oleLoanDocument.getPatronId() +"Loan Id " + oleLoanDocument.getLoanId());
                        throw new Exception("Patron Not Found For The Given Patron Id");
                    }else{
                        loanDocumentMap.put(oleLoanDocument.getLoanId(),oleLoanDocument);
                        oleLoanDocument.setBorrowerTypeCode(patronDocument.getOleBorrowerType().getBorrowerTypeCode());
                        oleLoanDocument.setOleCirculationDesk(circulationDeskMap.get(oleLoanDocument.getCirculationLocationId()));
                    }
                }catch(Exception e){
                    LOG.info("Exception occured while setting the borrower type information to the loan document");
                    LOG.error(e, e);
                }
                }
        }
        //remove the loan document for the existing processed loans
        if(oleOverDueDeliverNoticeList!=null && oleOverDueDeliverNoticeList.size()>0){
            for(OLEDeliverNotice oleDeliverNotice : oleOverDueDeliverNoticeList){
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }

        if(oleCourtesyDeliverNoticeList!=null && oleCourtesyDeliverNoticeList.size()>0){
            for(OLEDeliverNotice oleDeliverNotice : oleCourtesyDeliverNoticeList){
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }

        if(noticeLostList!=null && noticeLostList.size()>0){
            for(OLEDeliverNotice oleDeliverNotice : noticeLostList){
                loanDocumentMap.remove(oleDeliverNotice.getLoanId());
            }
        }
        Collection loanList = loanDocumentMap.values();
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList(loanList);
        LOG.info("Number of loan records after filtering : "  +oleLoanDocumentList.size());
        Long endTime = System.currentTimeMillis();
        Long timeDifference = endTime-startTime;
        LOG.info("Time taken for identifying the loan records in milliseconds " + timeDifference);
        return oleLoanDocumentList;
   }

    public List<OleDeliverRequestBo> getDeliverRequests(String patronBarcode){
        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OleDeliverRequestBo.class, criteria,true);
        criteria.addEqualTo("borrowerBarcode",patronBarcode);
        Collection loanOverdueNoticeResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OleDeliverRequestBo> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return oleOverDueDeliverNoticeList;
    }

    public List<OleLoanDocument> getDeliverLoans(String patronId){

        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(OleLoanDocument.class, criteria,true);
        criteria.addEqualTo("patronId",patronId);
        Collection loanOverdueNoticeResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<OleLoanDocument> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return oleOverDueDeliverNoticeList;
    }


    public List<PatronBillPayment> getPatronBillPayments(String patronId){
        Criteria criteria = new Criteria();
        QueryByCriteria noticeOverdueQuery = QueryFactory.newQuery(PatronBillPayment.class, criteria,true);
        criteria.addEqualTo("patronId",patronId);
        Collection loanOverdueNoticeResultSet=  getPersistenceBrokerTemplate().getCollectionByQuery(noticeOverdueQuery);
        List<PatronBillPayment> oleOverDueDeliverNoticeList = new ArrayList(loanOverdueNoticeResultSet);
        return  oleOverDueDeliverNoticeList;
    }


    public Collection<Object> getRequestTypeForHoldNotice(List<String> requestTypeCodes){
        Criteria criteria = new Criteria();
        criteria.addIn("requestTypeCode",requestTypeCodes);
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestType.class, criteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

 public List<String> getRequestTypeIds(Collection<Object> requestTypeObjects){
     List<String> requestTypeIds =  new ArrayList<String>();
     OleDeliverRequestType oleDeliverRequestType = null;
      if(requestTypeObjects != null && requestTypeObjects.size()>0){
         for(Object obj: requestTypeObjects){
             oleDeliverRequestType = (OleDeliverRequestType)obj;
             requestTypeIds.add(oleDeliverRequestType.getRequestTypeId());
         }
     }
    return requestTypeIds;
 }


    public List<String> getRequestTypeIdsForHoldNotice(List<String> requestTypeCodes){
    Collection<Object> requestTypes = getRequestTypeForHoldNotice(requestTypeCodes);
    return getRequestTypeIds(requestTypes);
    }

    public Collection<PatronBillPayment> getPatronBills(String patronId){
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("unPaidBalance",OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        criteria.addEqualTo("patronId",patronId);
        QueryByCriteria query = QueryFactory.newQuery(PatronBillPayment.class, criteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return new ArrayList<PatronBillPayment>(results);
    }

    public Collection<OleDeliverRequestHistoryRecord> getDeliverRequestHistoryRecords(String itemBarcode){
        Criteria criteria = new Criteria();
        criteria.addColumnEqualToField("OLE_ITEM_ID",itemBarcode);
        QueryByCriteria query = QueryFactory.newQuery(OleDeliverRequestHistoryRecord.class, criteria);
        query.addOrderByDescending("archiveDate");
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return results;
    }

}
