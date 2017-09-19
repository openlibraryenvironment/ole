package org.kuali.ole.deliver.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.EngineResults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by vivekb on 9/16/14.
 */
public class OLEDeliverNoticeHelperServiceImpl implements OLEDeliverNoticeHelperService {
    private static final Logger LOG = Logger.getLogger(OLEDeliverNoticeHelperServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private LoanProcessor loanProcessor;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    private String overdueNoticeType ;
    private String courtesyNoticeType ;
    private String overdueNoticeInterval;
    private String courtesyNoticeInterval;


    /**
     *
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
    /**
     *
     * @param loanProcessor
     */
    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    /**
     * This method initiate LoanProcessor.
     *
     * @return LoanProcessor
     */
    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    /**
     *
     * @return overdueNoticeType
     */
    public String getOverdueNoticeType() {
        if(overdueNoticeType == null){
            overdueNoticeType = loanProcessor.getParameter(OLEParameterConstants.OVERDUE_NOTICE_TYPE);
        }
        return overdueNoticeType;
    }

    /**
     *
     * @param overdueNoticeType
     */
    public void setOverdueNoticeType(String overdueNoticeType) {
        this.overdueNoticeType = overdueNoticeType;
    }

    /**
     *
     * @return courtesyNoticeType
     */
    public String getCourtesyNoticeType() {
        if(courtesyNoticeType == null){
            courtesyNoticeType =loanProcessor.getParameter(OLEParameterConstants.COURTESY_NOTICE_TYPE);
        }
        return courtesyNoticeType;
    }

    /**
     *
     * @param courtesyNoticeType
     */
    public void setCourtesyNoticeType(String courtesyNoticeType) {
        this.courtesyNoticeType = courtesyNoticeType;
    }

    /**
     *
     * @return overdueNoticeInterval
     */
    public String getOverdueNoticeInterval() {
        if(overdueNoticeInterval == null){
            overdueNoticeInterval =loanProcessor.getParameter(OLEConstants.OVERDUE_NOTICE_INTER);
        }
        return overdueNoticeInterval;
    }

    /**
     *
     * @param overdueNoticeInterval
     */
    public void setOverdueNoticeInterval(String overdueNoticeInterval) {
        this.overdueNoticeInterval = overdueNoticeInterval;
    }

    /**
     *
     * @return courtesyNoticeInterval
     */
    public String getCourtesyNoticeInterval() {
        if(courtesyNoticeInterval == null){
            courtesyNoticeInterval =loanProcessor.getParameter(OLEParameterConstants.COURTESY_NOTICE_INTER);
        }
        return courtesyNoticeInterval;
    }

    /**
     *
     * @param courtesyNoticeInterval
     */
    public void setCourtesyNoticeInterval(String courtesyNoticeInterval) {
        this.courtesyNoticeInterval = courtesyNoticeInterval;
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

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = SpringContext.getBean(OleDeliverRequestDocumentHelperServiceImpl.class);
        }
        return oleDeliverRequestDocumentHelperService;
    }


    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    /**
     *
     * @param loanId
     * @return oleDeliverNotices
     */
    @Override
    public List<OLEDeliverNotice> getDeliverNotices(String loanId) {
        Map noticeMap = new HashMap();
        noticeMap.put("loanId",loanId);
        List<OLEDeliverNotice> oleDeliverNotices = (List<OLEDeliverNotice> )getBusinessObjectService().findMatching(OLEDeliverNotice.class,noticeMap);
        return oleDeliverNotices;
    }

    /**
     *
     * @param loanId
     * @return oleDeliverNotices
     */
    @Override
    public List<OLEDeliverNotice> updateDeliverNotices(String loanId) {
        List<OLEDeliverNotice> oleDeliverNotices = getDeliverNotices(loanId);
        for(OLEDeliverNotice deliverNotice : oleDeliverNotices){
            //deliverNotice.setNoticeToBeSendDate();//TODO
        }
        return oleDeliverNotices;
    }

    /**
     *
     * @param loanId
     */
    @Override
    public void deleteDeliverNotices(String loanId) {
        Map noticeMap = new HashMap();
        noticeMap.put("loanId",loanId);
        getBusinessObjectService().deleteMatching(OLEDeliverNotice.class, noticeMap);
    }



    public void generateDeliverNotices(OleLoanDocument oleLoanDocument) throws Exception{
       generateDeliverNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemUuid(),
               oleLoanDocument.getOleCirculationDesk()!=null ? oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode() : null,
               oleLoanDocument.getBorrowerTypeCode(),oleLoanDocument.getItemTypeName(), oleLoanDocument.getItemStatus(),
               oleLoanDocument.isClaimsReturnedIndicator() ? OLEConstants.TRUE : OLEConstants.FALSE,
               oleLoanDocument.getRepaymentFeePatronBillId() != null ? OLEConstants.TRUE : OLEConstants.FALSE,
               oleLoanDocument.getItemLocation(), oleLoanDocument.getItemCollection(), oleLoanDocument.getItemLibrary(),
               oleLoanDocument.getItemCampus(), oleLoanDocument.getItemInstitution(), oleLoanDocument.getLoanDueDate(),oleLoanDocument.getLoanId());
    }
    public void generateDeliverNotices(String patronId,String itemId, String deskLocation,String borrowerType,
                                        String itemType,String itemStatus,String claimsReturned,String replacementBill,
                                        String itemShelving,String itemCollection,String itemLibrary,String itemCampus,
                                        String itemInstitution,Date itemDueDate,String loanId) throws Exception{
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

        String agendaName = OLEConstants.NOTICE_AGENDA_NM;

        dataCarrierService.removeData(patronId+itemId);
        HashMap<String, Object> termValues = new HashMap<String, Object>();
        termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
        termValues.put(OLEConstants.ITEM_TYPE, itemType);

        termValues.put(OLEConstants.ITEM_STATUS, itemStatus);

        termValues.put(OLEConstants.OleDeliverRequest.CLAIM_RETURNED, claimsReturned);
        termValues.put(OLEConstants.OleDeliverRequest.REPLACEMENT_FEE_PATRON_BILL, replacementBill);

        termValues.put(OLEConstants.ITEM_SHELVING, itemShelving);
        termValues.put(OLEConstants.ITEM_COLLECTION, itemCollection);
        termValues.put(OLEConstants.ITEM_LIBRARY,itemLibrary);
        termValues.put(OLEConstants.ITEM_CAMPUS, itemCampus);
        termValues.put(OLEConstants.ITEM_INSTITUTION, itemInstitution);

        termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
        termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);

        termValues.put(OLEConstants.ITEMS_DUE_DATE, itemDueDate);

        termValues.put(OLEConstants.DESK_LOCATION, deskLocation);

        if (LOG.isDebugEnabled()) {
            LOG.debug("termValues.toString()" + termValues.toString());
        }
        EngineResults engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
        dataCarrierService.removeData(patronId+itemId);

        List<String> errorMessage = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
        if (errorMessage != null && LOG.isDebugEnabled()) {
            LOG.debug("errorMessage" + errorMessage.toString());
        }

        List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResults.getAttribute("deliverNotices");
        if(deliverNotices!=null){
            for(OLEDeliverNotice deliverNotice : deliverNotices){
                deliverNotice.setLoanId(loanId);
                deliverNotice.setPatronId(patronId);
            }
          getBusinessObjectService().save(deliverNotices);
        }
    }


    public void generateDeliverNoticesUsingKRMSValues(List<OLEDeliverNotice> deliverNotices,Timestamp dueDate,
                                                       String noticeType ,String noticeFormat,
                                                       String numberOfOverdueToBeSent, String intervalToGenerateNotice,
                                                       String replacementBill){

        if(deliverNotices==null){
            deliverNotices = new ArrayList<>();
        }

        Integer lostCount = 0;

        if(noticeType!= null && noticeType.equalsIgnoreCase(OLEConstants.COURTESY_NOTICE)){
            OLEDeliverNotice courtesyNotice = new OLEDeliverNotice();
            courtesyNotice.setNoticeType(noticeType);
            if(noticeFormat!=null && !noticeFormat.trim().isEmpty()){
                courtesyNotice.setNoticeSendType(noticeFormat);
            }else{
                courtesyNotice.setNoticeSendType(getCourtesyNoticeType());
            }
            Timestamp noticeToBeSentDate=null;
            Integer interval;
            if(intervalToGenerateNotice!=null && !intervalToGenerateNotice.trim().isEmpty()){
                interval = intervalToGenerateNotice!=null ? -Integer.parseInt(intervalToGenerateNotice) : null;
            }else{
                interval = getCourtesyNoticeInterval()!=null ? -Integer.parseInt(getCourtesyNoticeInterval()) : null;
            }
            noticeToBeSentDate=calculateNoticeToBeSentDate(interval,dueDate,1);
            courtesyNotice.setNoticeToBeSendDate(noticeToBeSentDate);
            deliverNotices.add(courtesyNotice);
        }
        else if(noticeType!= null && noticeType.equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)){
            if(numberOfOverdueToBeSent!=null)
                for(int i = 1; i<= Integer.parseInt(numberOfOverdueToBeSent);i++ ){
                    OLEDeliverNotice overdueNotice = new OLEDeliverNotice();
                    overdueNotice.setNoticeType(noticeType);
                    if(noticeFormat!=null && !noticeFormat.trim().isEmpty()){
                        overdueNotice.setNoticeSendType(noticeFormat);
                    }else{
                        overdueNotice.setNoticeSendType(getOverdueNoticeType());
                    }
                    Timestamp noticeToBeSentDate=null;
                    Integer interval;
                    if(intervalToGenerateNotice!=null && !intervalToGenerateNotice.trim().isEmpty()){
                        interval = intervalToGenerateNotice!=null ? Integer.parseInt(intervalToGenerateNotice) : null;
                    }else{
                        interval = getOverdueNoticeInterval()!=null ? Integer.parseInt(getOverdueNoticeInterval()) : null;
                    }
                    noticeToBeSentDate=calculateNoticeToBeSentDate(interval,dueDate,i);
                    lostCount = i;
                    overdueNotice.setNoticeToBeSendDate(noticeToBeSentDate);
                    deliverNotices.add(overdueNotice);
                }
        }
        if(replacementBill!=null && !replacementBill.trim().isEmpty()){
            OLEDeliverNotice lostNotice = new OLEDeliverNotice();
            lostNotice.setNoticeType(OLEConstants.NOTICE_LOST);
            lostNotice.setReplacementFeeAmount(new BigDecimal(replacementBill));
            Timestamp noticeToBeSentDate=null;
            Integer interval;
            if(intervalToGenerateNotice!=null && !intervalToGenerateNotice.trim().isEmpty()){
                interval = intervalToGenerateNotice!=null ? Integer.parseInt(intervalToGenerateNotice) : null;
            }else{
                interval = getOverdueNoticeInterval()!=null ? Integer.parseInt(getOverdueNoticeInterval()) : null;
            }
            noticeToBeSentDate=calculateNoticeToBeSentDate(interval,dueDate,lostCount+1);
            lostNotice.setNoticeToBeSendDate(noticeToBeSentDate);
            deliverNotices.add(lostNotice);
        }

    }

    private Timestamp calculateNoticeToBeSentDate(Integer interval,Timestamp dueDate,Integer count){
        Timestamp noticeToBeSentDate;

        noticeToBeSentDate=interval!=null && dueDate !=null ?
                DateUtil.addDays(dueDate,interval * count) : null;
/*        noticeToBeSentDate.setHours(0);
        noticeToBeSentDate.setMinutes(0);
        noticeToBeSentDate.setSeconds(0);
        noticeToBeSentDate.setNanos(0);*/

        return noticeToBeSentDate;
    }

    @Override
    public void updateDeliverNoticeForUnprocessedLoans()throws Exception{
        Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> oleLoanDocuments = getOleLoanDocumentDaoOjb().getUnprocessedOverdueLoanDocuments();
        try {
            oleLoanDocuments = getOleDeliverRequestDocumentHelperService().getLoanDocumentWithItemInfo(oleLoanDocuments,Boolean.FALSE.toString());
        } catch (Exception e) {
          LOG.info("Exception occured while setting the item info " + e.getMessage());
            LOG.error(e,e);
            throw new Exception();
        }
        if(oleLoanDocuments!=null && oleLoanDocuments.size()>0){
            for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
                oleLoanDocument.setItemTypeName(oleLoanDocument.getItemType());
                generateDeliverNotices(oleLoanDocument);
            }
        }
        Long endTime = System.currentTimeMillis();
        Long differenceInTime = endTime-startTime;
        LOG.info("Time taken for updating the notice table in milliseconds " +differenceInTime);
    }

}
