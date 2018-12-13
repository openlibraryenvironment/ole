package org.kuali.ole.oleng.dao.export;


import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.*;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.kuali.ole.sys.context.SpringContext;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by govindarajank on 4/25/16.
 */

public abstract class NoticeDAO {
    protected OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = (OleDeliverRequestDocumentHelperServiceImpl) SpringContext.getBean("oleDeliverRequestDocumentHelperService");
    protected OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
    protected LoanWithNoticesDAO loanWithNoticesDAO = (LoanWithNoticesDAO) SpringContext.getService(OLEConstants.LOAN_WITH_NOTICES_DAO);

    public void prepareBatchNoticeResponse(List<Future> futures, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse originalNoticeResponse,DeliverNoticeHandler deliverNoticeHandler) {
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


    public void mergeResponses(OleNGBatchNoticeResponse originalNoticeResponse, OleNGBatchNoticeResponse noticeResponse) {
        originalNoticeResponse.setNoOfSuccessNotice(originalNoticeResponse.getNoOfSuccessNotice() + noticeResponse.getNoOfSuccessNotice());
        originalNoticeResponse.setNoOfFailureNotice(originalNoticeResponse.getNoOfFailureNotice() + noticeResponse.getNoOfFailureNotice());
        if(CollectionUtils.isNotEmpty(noticeResponse.getFailureLoanAndNoticeResponses())) {
            originalNoticeResponse.getFailureLoanAndNoticeResponses().addAll(noticeResponse.getFailureLoanAndNoticeResponses());
        }else if(CollectionUtils.isNotEmpty(noticeResponse.getFailureRequestAndNoticeResponses())){
            originalNoticeResponse.getFailureRequestAndNoticeResponses().addAll(noticeResponse.getFailureRequestAndNoticeResponses());
        }
    }

    public abstract Object runNoticeJob(DeliverNoticeHandler deliverNoticeHandler, String noticeType, BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) throws Exception;
    protected abstract NoticeCallable getNoticeCallable(String noticetype , Map noticeMap);

}
