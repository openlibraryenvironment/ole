package org.kuali.ole.deliver.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 4/13/16.
 */
public class RequestNoticeUtil {
    private static final Logger LOG = Logger.getLogger(RequestNoticeUtil.class);
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if (oleLoanDocumentDaoOjb == null) {
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
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

    public void populateNoticeTableForExistingRequests(){
        Long startTime = System.currentTimeMillis();
        RequestNoticeDAO requestNoticeDAO = (RequestNoticeDAO) SpringContext.getService(OLEConstants.REQUEST_NOTICE_DAO);
        List<String> requestIds = requestNoticeDAO.getRequestIds();
        if(requestIds!=null && requestIds.size()>0) {
            List<OleDeliverRequestBo> oleDeliverRequestList = getOleLoanDocumentDaoOjb().getAllRequests(requestIds);
            if (oleDeliverRequestList != null && oleDeliverRequestList.size() > 0) {
                try {
                    //getOleDeliverRequestDocumentHelperService().getLoanDocumentWithItemInfo(oleLoanDocumentList);
                    Map<String, List<OleDeliverRequestBo>> patronMapWithRequestDetails = buildMapOfRequestForEachPatron(oleDeliverRequestList);
                    for(String patronId : patronMapWithRequestDetails.keySet()){
                        RequestNoticeExecutor requestNoticeExecutor = new RequestNoticeExecutor(patronMapWithRequestDetails.get(patronId));
                        requestNoticeExecutor.generateNotice();
                    }
                    Long endTime = System.currentTimeMillis();
                    Long timeDifference = endTime - startTime;
                    LOG.info("Time Taken to set the item information in the loan records in milliseconds : " + timeDifference);
                    LOG.info("Time taken in minutes " + timeDifference / (1000 * 60));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            LOG.info("No records found for processing");
        }
    }


    private Map<String,List<OleDeliverRequestBo>> buildMapOfRequestForEachPatron(List<OleDeliverRequestBo> oleDeliverRequestBos) {
        Map<String, List<OleDeliverRequestBo>> map = new HashMap<>();
        String patronId;
        Timestamp noticetoSendDate = new Timestamp(System.currentTimeMillis());
        //iterating over the loan documents for grouping of loan documents for each patron
        for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            //iterating over deliver notices for identifying the notice to which we need to send mail that falls on the noticeToDate and put in a map with patron id as key
            patronId = oleDeliverRequestBo.getBorrowerId();
            //if the map already contains an entry for that patron id then get the map for that patron id which has configurationName as key and list of loan documents as value
            if (map.containsKey(patronId)) {
                List<OleDeliverRequestBo> oleDeliverRequestBoListList= map.get(patronId);
                //if the configMap has an entry already for the configuration name then add the current loan document to that list .if there is no entry for that configuration name add a new entry to that configMap with this configuration name along with the loan document
                oleDeliverRequestBoListList.add(oleDeliverRequestBo);
            }
            //if the map does not have an entry for the patron id add a new entry to the map
            else {
                List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<>();
                oleDeliverRequestBoList.add(oleDeliverRequestBo);
                map.put(patronId, oleDeliverRequestBoList);
            }
        }

        return map;
    }

}
