package org.kuali.ole.oleng.dao.export;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.docstore.common.response.FailureLoanAndNoticeResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.kuali.ole.sys.context.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by govindarajank on 4/25/16.
 */

public class NoticeDAO {
    private static final Logger LOG = LoggerFactory.getLogger(NoticeDAO.class);
    private static NoticeDAO noticeDAO;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = (OleDeliverRequestDocumentHelperServiceImpl) SpringContext.getBean("oleDeliverRequestDocumentHelperService");
    public static NoticeDAO getInstance(){
        if(null == noticeDAO){
            noticeDAO = new NoticeDAO();
        }
        return noticeDAO;
    }

    public Object runNoticeJob(DeliverNoticeHandler deliverNoticeHandler, String noticeType, BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) throws Exception{

        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        LoanWithNoticesDAO loanWithNoticesDAO = (LoanWithNoticesDAO) SpringContext.getService(OLEConstants.LOAN_WITH_NOTICES_DAO);

        String noticeTypeToSendDate = deliverNoticeHandler.getNoticeTypeSendDate(noticeType);
        String noticeToSendDate = null;

        oleNGBatchNoticeResponse.setNoticeType(noticeType);

        if(CollectionUtils.isEmpty(oleNGBatchNoticeResponse.getFailureLoanAndNoticeResponses())){
            oleNGBatchNoticeResponse.setFailureLoanAndNoticeResponses(new ArrayList<FailureLoanAndNoticeResponse>());
        }

        List<Future> futures = new ArrayList<>();

        if(StringUtils.isNotBlank(noticeTypeToSendDate)){
            noticeToSendDate = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,noticeTypeToSendDate);
        }

        List<String> loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(noticeToSendDate, noticeType);

        if(loanIds.size() > 0){
            List<OleLoanDocument> loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
            List<OleLoanDocument> loanDocumentsWithItemInfo = oleDeliverRequestDocumentHelperService.getLoanDocumentWithItemInfo(loanDocuments,Boolean.FALSE.toString());
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
            if (org.apache.commons.lang.StringUtils.isNotBlank(threadPoolSizeValue)) {
                try {
                    threadPoolSize = Integer.parseInt(threadPoolSizeValue);
                } catch (Exception e) {
                    LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                    threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
                }
            }

            Timestamp noticetoBeSendDate = new Timestamp(System.currentTimeMillis());

            if(StringUtils.isNotBlank(noticeToSendDate)){
                noticetoBeSendDate = new Timestamp(new Date(noticeToSendDate).getTime());
            }

            int totalNoticeCount = deliverNoticeHandler.getTotalNoticesCount(loanDocumentsWithItemInfo,noticeType, noticetoBeSendDate);

            Map<String, Map<String, List<OleLoanDocument>>> mapofNoticesForEachPatronAndConfigName = oleDeliverRequestDocumentHelperService.buildMapofNoticesForEachPatronAndConfigName(loanDocumentsWithItemInfo, noticeToSendDate, noticeType);

            oleNGBatchNoticeResponse.setTotalNoticeCount(totalNoticeCount);
            batchJobDetails.setTotalRecords(Integer.toString(totalNoticeCount));
            deliverNoticeHandler.updateBatchJob(batchJobDetails);

            ExecutorService noticeExecutorService = Executors.newFixedThreadPool(threadPoolSize);

            for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
                String patronId = iterator.next();
                Map<String, List<OleLoanDocument>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
                for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                    String configName = configIterator.next();
                    try {
                        Map noticeMap = new HashMap();
                        noticeMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                        noticeMap.put(OLEConstants.LOAN_DOCUMENTS, configMap.get(configName));
                        noticeMap.put("DeliverNoticeHandler",deliverNoticeHandler);
                        futures.add( noticeExecutorService.submit(deliverNoticeHandler.getNoticeCallable(noticeType, noticeMap)));

                    }catch (Exception e){
                        int failedNoticeCount = deliverNoticeHandler.getTotalNoticesCount(configMap.get(configName), noticeType, noticetoBeSendDate);
                        oleNGBatchNoticeResponse.setNoOfFailureNotice(oleNGBatchNoticeResponse.getNoOfFailureNotice() + failedNoticeCount);
                        oleNGBatchNoticeResponse.getFailureLoanAndNoticeResponses().addAll(deliverNoticeHandler.getFailureLoanAndNoticeResponses(configMap.get(configName), noticeType, noticetoBeSendDate));
                    }

                }
            }
            prepareBatchNoticeResponse(futures,batchJobDetails,oleNGBatchNoticeResponse,deliverNoticeHandler);
            if(!noticeExecutorService.isShutdown()){
                noticeExecutorService.shutdown();
            }
        }

        return  oleNGBatchNoticeResponse;
    }


    private void prepareBatchNoticeResponse(List<Future> futures, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse originalNoticeResponse,DeliverNoticeHandler deliverNoticeHandler) {
        for(Future future:futures) {
            if (future != null) {
                try {
                    Object response = future.get();
                    if (null != response) {
                        OleNGBatchNoticeResponse noticeResponse = (OleNGBatchNoticeResponse) response;
                        mergeResponses(originalNoticeResponse, noticeResponse);
                        batchJobDetails.setTotalRecordsProcessed(Integer.toString(originalNoticeResponse.getNoOfSuccessNotice() + originalNoticeResponse.getNoOfFailureNotice()));
                        batchJobDetails.setTotalFailureRecords(Integer.toString(originalNoticeResponse.getNoOfFailureNotice()));
                        deliverNoticeHandler.updateBatchJob(batchJobDetails);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void mergeResponses(OleNGBatchNoticeResponse originalNoticeResponse, OleNGBatchNoticeResponse noticeResponse) {
        originalNoticeResponse.setNoOfSuccessNotice(originalNoticeResponse.getNoOfSuccessNotice() + noticeResponse.getNoOfSuccessNotice());
        originalNoticeResponse.setNoOfFailureNotice(originalNoticeResponse.getNoOfFailureNotice() + noticeResponse.getNoOfFailureNotice());
        if(CollectionUtils.isNotEmpty(noticeResponse.getFailureLoanAndNoticeResponses())) {
            originalNoticeResponse.getFailureLoanAndNoticeResponses().addAll(noticeResponse.getFailureLoanAndNoticeResponses());
        }
    }
}
