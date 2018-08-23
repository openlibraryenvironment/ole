package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.docstore.common.response.FailureLoanAndNoticeResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchDeliverNotices;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.export.*;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by govindarajank on 4/20/16.
 */
@Service("deliverNoticeHandler")
public class DeliverNoticeHandler extends BatchUtil {

    private NoticeDAO noticeDao = NoticeDAO.getInstance();

    public OleNGBatchNoticeResponse processExport(BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) {
        try {
            List<BatchDeliverNotices> batchDeliverNotices = (batchProcessProfile != null ? batchProcessProfile.getBatchDeliverNotices() : null);
            String noticeType = (CollectionUtils.isNotEmpty(batchDeliverNotices) ?  (batchDeliverNotices.get(1) != null ? batchDeliverNotices.get(1).getDeliverNoticeName()  : null ) : null);
            if (StringUtils.isNotBlank(noticeType)) {
                oleNGBatchNoticeResponse = (OleNGBatchNoticeResponse) noticeDao.runNoticeJob(this,noticeType,batchProcessProfile,batchJobDetails,oleNGBatchNoticeResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return oleNGBatchNoticeResponse;
    }

    public String getNoticeTypeSendDate(String noticeType){

        String noticeTypeToSendDate = null;
        switch(noticeType){
            case OLEConstants.COURTESY_NOTICE :
                noticeTypeToSendDate = OLEConstants.COURTESY_NOTICE_TO_DATE;
                break;

            case OLEConstants.OVERDUE_NOTICE :
                noticeTypeToSendDate = OLEConstants.OVERDUE_NOTICE_TO_DATE;
                break;

            case OLEConstants.NOTICE_LOST :
                noticeTypeToSendDate = OLEConstants.LOST_NOTICE_TO_DATE;
                break;

        }

        return  noticeTypeToSendDate;
    }

    public NoticeCallable getNoticeCallable(String noticeType, Map noticeMap){

        NoticeCallable noticeCallable = null;

        switch (noticeType){

            case OLEConstants.COURTESY_NOTICE :
                noticeCallable = new CourtesyNoticeCallable(noticeMap);
                break;

            case OLEConstants.OVERDUE_NOTICE :
                noticeCallable = new OverdueNoticeCallable(noticeMap);
                break;

            case OLEConstants.NOTICE_LOST :
                noticeCallable = new LostNoticeCallable(noticeMap);
                break;

        }

        return noticeCallable;
    }

    public int getTotalNoticesCount(List<OleLoanDocument> oleLoanDocuments, String noticeType, Timestamp noticeToBeSendDate){

        int noticeCount = 0;
        if(CollectionUtils.isNotEmpty(oleLoanDocuments)){
            for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
                for(OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()){
                    if(oleDeliverNotice != null && oleDeliverNotice.getNoticeType().equals(noticeType) &&
                            noticeToBeSendDate != null && oleDeliverNotice.getNoticeToBeSendDate().compareTo(noticeToBeSendDate) < 0){
                        noticeCount++;
                    }
                }
            }
        }
        return noticeCount;
    }


    public List<FailureLoanAndNoticeResponse> getFailureLoanAndNoticeResponses(List<OleLoanDocument> oleLoanDocuments, String noticeType, Timestamp noticeToBeSendDate){

        List<FailureLoanAndNoticeResponse> failureLoanAndNoticeResponses = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(oleLoanDocuments)){
            for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
                for(OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()){
                    if(oleDeliverNotice != null && oleDeliverNotice.getNoticeType().equals(noticeType) &&
                            noticeToBeSendDate != null && oleDeliverNotice.getNoticeToBeSendDate().compareTo(noticeToBeSendDate) < 0){
                        FailureLoanAndNoticeResponse failureLoanAndNoticeResponse = new FailureLoanAndNoticeResponse();
                        failureLoanAndNoticeResponse.setFailedLoanId(oleDeliverNotice.getLoanId());
                        failureLoanAndNoticeResponse.setFailedNoticeId(oleDeliverNotice.getId());
                        failureLoanAndNoticeResponses.add(failureLoanAndNoticeResponse);
                    }
                }
            }
        }

        return  failureLoanAndNoticeResponses;
    }

}

