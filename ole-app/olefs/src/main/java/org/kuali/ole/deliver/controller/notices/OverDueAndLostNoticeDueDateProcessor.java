package org.kuali.ole.deliver.controller.notices;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public class OverDueAndLostNoticeDueDateProcessor extends NoticeDueDateProcessor {
    
    private static final Logger LOG = Logger.getLogger(OverDueAndLostNoticeDueDateProcessor.class);

    @Override
    public boolean isInterested(String noticeType) {
        return noticeType.equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE);
    }

    @Override
    public List<OLEDeliverNotice> generateNotices(NoticeInfo noticeInfo, OleLoanDocument oleLoanDocument) {
        List<OLEDeliverNotice> overdueAndLostNotices = new ArrayList<>();

        try {
            Integer numberOfOverDueNoticeToBeSent = Integer.parseInt(getNumberOfOverDueNoticeToSent(noticeInfo));

            Map<String, Object> overdueMap = getNoticeInfoForTypeMap(noticeInfo);
            Timestamp loanDueDate = oleLoanDocument.getLoanDueDate();
            if (null != loanDueDate) {
                /* Overdue notice process start */
                String overdueIntervals = (String) overdueMap.get(DroolsConstants.INTERVAL_TO_GENERATE_NOTICE_FOR_OVERDUE);
                StringTokenizer intervalTokenizer = new StringTokenizer(overdueIntervals,",");
                int tokenCounts = intervalTokenizer.countTokens();
                if((Integer.valueOf(numberOfOverDueNoticeToBeSent) +1)  == tokenCounts){
                    for(int intervalCount = 1 ; intervalCount <= numberOfOverDueNoticeToBeSent ; intervalCount++){
                        try {
                            Integer interval = Integer.valueOf((String) intervalTokenizer.nextElement());
                            OLEDeliverNotice overdueNotice = new OLEDeliverNotice();
                            Date dateToSent = calculateNoticeDueDate(loanDueDate, interval, noticeInfo.getIntervalType());
                            overdueNotice.setNoticeToBeSendDate(new Timestamp(dateToSent.getTime()));
                            overdueNotice.setNoticeSendType(DroolsConstants.EMAIL);
                            overdueNotice.setNoticeType(OLEConstants.OVERDUE_NOTICE);
                            overdueNotice.setPatronId(oleLoanDocument.getPatronId());
                            overdueNotice.setLoanId(oleLoanDocument.getLoanId());
                            overdueAndLostNotices.add(overdueNotice);
                        } catch (NumberFormatException numberFormatException) {
                            LOG.error("Invalid Interval given for Overdue notice.");
                        } catch (Exception e) {
                            LOG.error("Error while processing Overdue notices");
                        }
                    }
                /* Overdue notice process end */

                /* Lost notice process start */
                    try {
                        Integer interval = Integer.valueOf((String) intervalTokenizer.nextElement());
                        List<OLEDeliverNotice> lostNotices = processLostNotices(noticeInfo, interval, oleLoanDocument);
                        overdueAndLostNotices.addAll(lostNotices);
                    }  catch (NumberFormatException numberFormatException) {
                        LOG.error("Invalid Interval given for Lost notice.");
                    } catch (Exception e) {
                        LOG.error("Error while processing Lost notices");
                    }
                /* Lost notice process end */
                } else{
                    LOG.error("Number of overdue to be sent is not matched with number of intervals");
                }
            }
        } catch (NumberFormatException numberFormatException) {
            LOG.error("Invalid number of overdue notice count given.");
        }
        return overdueAndLostNotices;
    }

    private List<OLEDeliverNotice> processLostNotices(NoticeInfo noticeInfo, int interval, OleLoanDocument loanDocument) {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();

        OLEDeliverNotice lostNotice = new OLEDeliverNotice();
        lostNotice.setNoticeType(OLEConstants.NOTICE_LOST);
        lostNotice.setNoticeSendType(DroolsConstants.EMAIL);
        lostNotice.setPatronId(loanDocument.getPatronId());

        Map<String, Object> overdueMap = new HashMap<String, Object>();
        if (noticeInfo.getNoticeInfoForTypeMap().containsKey(OLEConstants.OVERDUE_NOTICE)) {
            overdueMap = noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.OVERDUE_NOTICE);
        } else if (noticeInfo.getNoticeInfoForTypeMap().containsKey(OLEConstants.RECALL_OVERDUE_NOTICE)) {
            overdueMap = noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.RECALL_OVERDUE_NOTICE);
        }

        Date dateToSentLostNotice = calculateNoticeDueDate(loanDocument.getLoanDueDate(), interval, noticeInfo.getIntervalType());
        lostNotice.setNoticeToBeSendDate(new Timestamp(dateToSentLostNotice.getTime()));
        lostNotice.setReplacementFeeAmount(BigDecimal.valueOf(Double.parseDouble((String) overdueMap.get(DroolsConstants
                .REPLACEMENT_BILL_AMT))));

        oleDeliverNotices.add(lostNotice);
        return oleDeliverNotices;
    }

    @Override
    protected Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo) {
        return noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.OVERDUE_NOTICE);
    }

    protected String getNumberOfOverDueNoticeToSent(NoticeInfo noticeInfo){
        return (String) noticeInfo.getNoticeInfoForTypeMap().get
                (OLEConstants.OVERDUE_NOTICE).get(DroolsConstants.NUMBER_OF_OVERDUE_NOTICES_TO_BE_SENT);
    }
}
