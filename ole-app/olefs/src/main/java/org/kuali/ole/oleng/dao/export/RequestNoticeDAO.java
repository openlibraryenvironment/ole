package org.kuali.ole.oleng.dao.export;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.response.FailureRequestAndNoticeResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RequestNoticeDAO extends NoticeDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RequestNoticeDAO.class);

    @Override
    public Object runNoticeJob(DeliverNoticeHandler deliverNoticeHandler, String noticeType, BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) throws Exception {

        List<Future> futures = new ArrayList<>();

        oleNGBatchNoticeResponse.setNoticeType(noticeType);
        if(CollectionUtils.isEmpty(oleNGBatchNoticeResponse.getFailureRequestAndNoticeResponses())){
            oleNGBatchNoticeResponse.setFailureRequestAndNoticeResponses(new ArrayList<FailureRequestAndNoticeResponse>());
        }

        Collection deliverRequestNotices = getRequestNotices(noticeType);

        if(CollectionUtils.isNotEmpty(deliverRequestNotices)) {
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTICE_THREAD_POOL_SIZE);
            if (StringUtils.isNotBlank(threadPoolSizeValue)) {
                try {
                    threadPoolSize = Integer.parseInt(threadPoolSizeValue);
                } catch (Exception e) {
                    LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                    threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
                }
            }

            oleNGBatchNoticeResponse.setTotalNoticeCount(deliverRequestNotices.size());
            batchJobDetails.setTotalRecords(Integer.toString(oleNGBatchNoticeResponse.getTotalNoticeCount()));
            deliverNoticeHandler.updateBatchJob(batchJobDetails);

            Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = oleDeliverRequestDocumentHelperService.buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) deliverRequestNotices);

            ExecutorService requestNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

            for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
                String patronId = iterator.next();
                Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
                for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                    String configName = configIterator.next();
                    try {
                        Map requestMap = new HashMap();
                        requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                        requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                        requestMap.put("DeliverNoticeHandler",deliverNoticeHandler);
                        futures.add(requestNoticesExecutorService.submit(getNoticeCallable(noticeType, requestMap)));
                    }catch (Exception e){
                        List<OLEDeliverNotice> failedDeliverNotices = configMap.get(configName);
                        int failedNoticeCount = failedDeliverNotices.size();
                        oleNGBatchNoticeResponse.setNoOfFailureNotice(oleNGBatchNoticeResponse.getNoOfFailureNotice() + failedNoticeCount);
                        oleNGBatchNoticeResponse.getFailureRequestAndNoticeResponses().addAll(deliverNoticeHandler.getFailureRequestAndNoticeResponse(failedDeliverNotices));
                    }
                }
            }
            prepareBatchNoticeResponse(futures,batchJobDetails,oleNGBatchNoticeResponse,deliverNoticeHandler);
            if(!requestNoticesExecutorService.isShutdown()) {
                requestNoticesExecutorService.shutdown();
            }
        }
        return oleNGBatchNoticeResponse;
    }



    private Collection getRequestNotices(String noticeType){

        Collection deliverRequestNotices = null;

        switch (noticeType){
            case OLEConstants.REQUEST_EXPIRATION_NOTICE :
                  deliverRequestNotices = oleLoanDocumentDaoOjb.getRequestExpiredNotice();
                  break;

            case OLEConstants.ONHOLD_NOTICE :
                deliverRequestNotices = oleLoanDocumentDaoOjb.getOnHoldNotice();
                break;

            case OLEConstants.ONHOLD_EXPIRATION_NOTICE :
                deliverRequestNotices = oleLoanDocumentDaoOjb.getOnHoldExpiredNotice();
                break;

            case OLEConstants.ONHOLD_COURTESY_NOTICE :
                deliverRequestNotices = oleLoanDocumentDaoOjb.getOnHoldCourtesyNotice();
                break;

        }

        return deliverRequestNotices;
    }

    @Override
    public NoticeCallable getNoticeCallable(String noticeType, Map requestNoticeMap){

        RequestNoticesCallable requestNoticesCallable = null;

        switch (noticeType){
            case OLEConstants.REQUEST_EXPIRATION_NOTICE :
                requestNoticesCallable = new RequestExpirationNoticesCallable(requestNoticeMap);
                break;

            case OLEConstants.ONHOLD_NOTICE :
                requestNoticesCallable = new OnHoldNoticesCallable(requestNoticeMap);
                break;

            case OLEConstants.ONHOLD_EXPIRATION_NOTICE :
                requestNoticesCallable = new HoldExpirationNoticesCallable(requestNoticeMap);
                break;

            case OLEConstants.ONHOLD_COURTESY_NOTICE :
                requestNoticesCallable = new HoldCourtesyNoticeCallable(requestNoticeMap);
                break;
        }

        return requestNoticesCallable;
    }


}
