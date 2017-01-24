package org.kuali.ole.deliver.controller.notices;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public class CourtseyNoticeDueDateProcessor extends NoticeDueDateProcessor {

    private static final Logger LOG = Logger.getLogger(CourtseyNoticeDueDateProcessor.class);

    @Override
    public boolean isInterested(String noticeType) {
        return noticeType.equalsIgnoreCase(OLEConstants.COURTESY_NOTICE);
    }

    @Override
    public List<OLEDeliverNotice> generateNotices(NoticeInfo noticeInfo, OleLoanDocument oleLoanDocument) {
        List<OLEDeliverNotice> courtseyNotices = new ArrayList<>();

        Timestamp loanDueDate = oleLoanDocument.getLoanDueDate();
        Integer numberOfCourtesyNoticeToBeSent = Integer.parseInt(getNumberOfCourtesyNoticeToSent(noticeInfo));
        if (null != loanDueDate) {
            String loanId = oleLoanDocument.getLoanId();
            Map<String, Object> courtesyMap = getNoticeInfoForTypeMap(noticeInfo);

            try {
                String intervalToGenerateCourtseyNotice = (String) courtesyMap.get(DroolsConstants.INTERVAL_TO_GENERATE_NOTICE_FOR_COURTESY);
                StringTokenizer intervalTokenizer = new StringTokenizer(intervalToGenerateCourtseyNotice,",");
                int tokenCounts = intervalTokenizer.countTokens();
                String noticeContentConfigName = (String) courtesyMap.get(OLEConstants.COURTESY_NOTICE_CONTENT_CONFIG_NAME);
                if((Integer.valueOf(numberOfCourtesyNoticeToBeSent))  == tokenCounts){
                    for(int intervalCount = 1 ; intervalCount <= numberOfCourtesyNoticeToBeSent ; intervalCount++){
                        Integer interval = Integer.valueOf((String) intervalTokenizer.nextElement());
                        OLEDeliverNotice courtesyNotice = new OLEDeliverNotice();
                        courtesyNotice.setNoticeType(OLEConstants.COURTESY_NOTICE);
                        courtesyNotice.setNoticeSendType(DroolsConstants.EMAIL);
                        Date dateToSent = calculateNoticeDueDate(loanDueDate, -interval, noticeInfo.getIntervalType());
                        courtesyNotice.setNoticeToBeSendDate(new Timestamp(dateToSent.getTime()));
                        courtesyNotice.setLoanId(loanId);
                        courtesyNotice.setPatronId(oleLoanDocument.getPatronId());
                        courtesyNotice.setNoticeContentConfigName(noticeContentConfigName);
                        courtseyNotices.add(courtesyNotice);
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                LOG.error("Invalid Interval given for courtsey notice.");
            } catch (Exception e) {
                LOG.error("Error while processing courtsey notices");
            }
        }

        return  courtseyNotices;
    }

    @Override
    protected Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo) {
        return noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.COURTESY_NOTICE);
    }

    protected String getNumberOfCourtesyNoticeToSent(NoticeInfo noticeInfo){
         return (String) noticeInfo.getNoticeInfoForTypeMap().get
                        (OLEConstants.COURTESY_NOTICE).get(DroolsConstants.NUMBER_OF_COURTESY_NOTICES_TO_BE_SENT);
    }
}
