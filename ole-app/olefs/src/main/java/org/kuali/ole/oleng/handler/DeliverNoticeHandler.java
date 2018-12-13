package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.docstore.common.response.FailureLoanAndNoticeResponse;
import org.kuali.ole.docstore.common.response.FailureRequestAndNoticeResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchDeliverNotices;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.export.*;
import org.kuali.ole.spring.batch.BatchUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by govindarajank on 4/20/16.
 *
 */
@Service("deliverNoticeHandler")
public class DeliverNoticeHandler extends BatchUtil {

    private NoticeDAO noticeDao;

    private static Map<String,List<String>> noticeCallableMap = new HashMap<>();

    static {
        noticeCallableMap.put(OLEConstants.LOAN, new ArrayList<String>(Arrays.asList(OLEConstants.COURTESY_NOTICE,OLEConstants.OVERDUE_NOTICE,OLEConstants.NOTICE_LOST)));
        noticeCallableMap.put(OLEConstants.REQUEST, new ArrayList<String>(Arrays.asList(OLEConstants.REQUEST_EXPIRATION_NOTICE,OLEConstants.ONHOLD_NOTICE,OLEConstants.ONHOLD_EXPIRATION_NOTICE,OLEConstants.ONHOLD_COURTESY_NOTICE)));
    }

    public OleNGBatchNoticeResponse processExport(BatchProcessProfile batchProcessProfile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) {
        try {
            List<BatchDeliverNotices> batchDeliverNotices = (batchProcessProfile != null ? batchProcessProfile.getBatchDeliverNotices() : null);
            String noticeType = (CollectionUtils.isNotEmpty(batchDeliverNotices) ?  (batchDeliverNotices.get(1) != null ? batchDeliverNotices.get(1).getDeliverNoticeName()  : null ) : null);
            if (StringUtils.isNotBlank(noticeType)) {
                if(getKeyFromNoticeMap(noticeType).equalsIgnoreCase(OLEConstants.LOAN)){
                    noticeDao = new LoanNoticeDAO();
                }else{
                    noticeDao = new RequestNoticeDAO();
                }
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

    public List<FailureRequestAndNoticeResponse> getFailureRequestAndNoticeResponse(List<OLEDeliverNotice> oleDeliverNotices){

        List<FailureRequestAndNoticeResponse> failureRequestAndNoticeResponses = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(oleDeliverNotices)){
            for(OLEDeliverNotice oleDeliverNotice : oleDeliverNotices) {
                FailureRequestAndNoticeResponse failureRequestAndNoticeResponse = new FailureRequestAndNoticeResponse();
                failureRequestAndNoticeResponse.setFailedNoticeId(oleDeliverNotice.getId());
                failureRequestAndNoticeResponse.setFailedRequestId(oleDeliverNotice.getRequestId());
                failureRequestAndNoticeResponses.add(failureRequestAndNoticeResponse);
            }
        }
        return failureRequestAndNoticeResponses;
    }

    private String getKeyFromNoticeMap(String noticeType){

        for(Map.Entry<String,List<String>> entry : noticeCallableMap.entrySet()){
            List<String> noticeTypeList = entry.getValue();
            if(noticeTypeList.contains(noticeType)){
                return entry.getKey();
            }
        }
        return "";
    }

}

