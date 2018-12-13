package org.kuali.ole.oleng.dao.export;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.docstore.common.response.FailureLoanAndNoticeResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;

import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoanNoticeDAO extends NoticeDAO {

    private static final Logger LOG = LoggerFactory.getLogger(LoanNoticeDAO.class);

    @Override
    public Object runNoticeJob(DeliverNoticeHandler deliverNoticeHandler, String noticeType, BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) throws Exception {

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

            ExecutorService loanNoticeExecutorService = Executors.newFixedThreadPool(threadPoolSize);

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
                        futures.add( loanNoticeExecutorService.submit(getNoticeCallable(noticeType, noticeMap)));
                    }catch (Exception e){
                        int failedNoticeCount = deliverNoticeHandler.getTotalNoticesCount(configMap.get(configName), noticeType, noticetoBeSendDate);
                        oleNGBatchNoticeResponse.setNoOfFailureNotice(oleNGBatchNoticeResponse.getNoOfFailureNotice() + failedNoticeCount);
                        oleNGBatchNoticeResponse.getFailureLoanAndNoticeResponses().addAll(deliverNoticeHandler.getFailureLoanAndNoticeResponses(configMap.get(configName), noticeType, noticetoBeSendDate));
                    }

                }
            }
            prepareBatchNoticeResponse(futures,batchJobDetails,oleNGBatchNoticeResponse,deliverNoticeHandler);
            if(!loanNoticeExecutorService.isShutdown()){
                loanNoticeExecutorService.shutdown();
            }
        }

        return  oleNGBatchNoticeResponse;

    }

    @Override
    public NoticeCallable getNoticeCallable(String noticeType, Map loanNoticeMap){

            LoanNoticesCallable loanNoticesCallable = null;

            switch (noticeType){

                case OLEConstants.COURTESY_NOTICE :
                    loanNoticesCallable = new CourtesyNoticeCallable(loanNoticeMap);
                    break;

                case OLEConstants.OVERDUE_NOTICE :
                    loanNoticesCallable = new OverdueNoticeCallable(loanNoticeMap);
                    break;

                case OLEConstants.NOTICE_LOST :
                    loanNoticesCallable = new LostNoticeCallable(loanNoticeMap);
                    break;

            }

            return loanNoticesCallable;
    }

}
