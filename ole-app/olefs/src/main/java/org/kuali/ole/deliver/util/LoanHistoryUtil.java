package org.kuali.ole.deliver.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.docstore.common.document.content.instance.ItemType;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.model.rdbms.bo.ItemTypeRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.omg.CosNotification.StopTime;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maheswarang on 3/1/16.
 */
public class LoanHistoryUtil {
    private static final Logger LOG = Logger.getLogger(LoanHistoryUtil.class);
    private BusinessObjectService businessObjectService;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if (oleLoanDocumentDaoOjb == null) {
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }


    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public int getThreadPoolSize(){
        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
    }
        return threadPoolSize;
    }



    public void populateCirculationHistoryTable(){
        System.out.println("Record Size before processing "+getBusinessObjectService().findAll(OleCirculationHistory.class).size());
        Long startTime = System.currentTimeMillis();

        LoanHistoryDAO loanHistoryDAO = (LoanHistoryDAO) SpringContext.getService(OLEConstants.LOAN_HISTORY_DAO);
        List<OleLoanDocument> oleLoanDocumentList = getOleLoanDocumentDaoOjb().getAllLoans(loanHistoryDAO.getLoanIds());
        try{
        //getOleDeliverRequestDocumentHelperService().getLoanDocumentWithItemInfo(oleLoanDocumentList);
        Map<String,List<OleLoanDocument>> patronMapWithLoanDetails = buildMapOfLoanForEachPatron(oleLoanDocumentList);
        int threadPoolSize = getThreadPoolSize();
       EntityEmploymentBo entityEmploymentBo = null;
       EntityAffiliationBo entityAffiliationBo = null;
        for(String patronId : patronMapWithLoanDetails.keySet()){
            Map<String,String> patronMap = new HashMap<String,String>();
            patronMap.put("olePatronId",patronId);
            List<OlePatronDocument> patronDocuments = (List<OlePatronDocument>)getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
            OlePatronDocument olePatronDocument = null;
            if(patronDocuments!=null && patronDocuments.size()>0){
                olePatronDocument = patronDocuments.get(0);
            }
            ExecutorService oleCirculationHistoryExecutor = Executors.newFixedThreadPool(5);
            List<EntityAffiliationBo> entityAffiliationBos = getOleLoanDocumentDaoOjb().getEntityAffiliationBos(oleLoanDocumentList.get(0).getPatronId());
            if(entityAffiliationBos!=null && entityAffiliationBos.size()>0){
                entityAffiliationBo = entityAffiliationBos.get(0);
            }
            List<EntityEmploymentBo> entityEmploymentBos = getOleLoanDocumentDaoOjb().getEntityEmploymentBos(oleLoanDocumentList.get(0).getPatronId());
            if(entityEmploymentBos!=null && entityEmploymentBos.size()>0){
                entityEmploymentBo = entityEmploymentBos.get(0);
            }
            if(patronMapWithLoanDetails.get(patronId)!=null && patronMapWithLoanDetails.get(patronId).size()>0){
            Runnable oleCirculationHistory = new OleCirculationHistoryExecutor(patronMapWithLoanDetails.get(patronId),entityAffiliationBo,entityEmploymentBo,olePatronDocument);
            oleCirculationHistoryExecutor.execute(oleCirculationHistory);
            }
        }
        Long endTime = System.currentTimeMillis();
        Long timeDifference = endTime - startTime;
        LOG.info("Time Taken to set the item information in the loan records in milliseconds : " + timeDifference);
        LOG.info("Time taken in minutes "+timeDifference/(1000*60));
        System.out.println("Record Size after processing " + getBusinessObjectService().findAll(OleCirculationHistory.class).size());
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private Map<String,List<OleLoanDocument>> buildMapOfLoanForEachPatron(List<OleLoanDocument> loanDocuments) {
        Map<String, List<OleLoanDocument>> map = new HashMap<>();
        String patronId;
        Timestamp noticetoSendDate = new Timestamp(System.currentTimeMillis());
        //iterating over the loan documents for grouping of loan documents for each patron
        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            //iterating over deliver notices for identifying the notice to which we need to send mail that falls on the noticeToDate and put in a map with patron id as key
                    patronId = oleLoanDocument.getPatronId();
                    //if the map already contains an entry for that patron id then get the map for that patron id which has configurationName as key and list of loan documents as value
                    if (map.containsKey(patronId)) {
                        List<OleLoanDocument> loanDocumentList= map.get(patronId);
                        //if the configMap has an entry already for the configuration name then add the current loan document to that list .if there is no entry for that configuration name add a new entry to that configMap with this configuration name along with the loan document
                        loanDocumentList.add(oleLoanDocument);
                    }
                    //if the map does not have an entry for the patron id add a new entry to the map
                    else {
                        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
                        oleLoanDocumentList.add(oleLoanDocument);
                        map.put(patronId, oleLoanDocumentList);
                    }
            }

        return map;
    }





}
