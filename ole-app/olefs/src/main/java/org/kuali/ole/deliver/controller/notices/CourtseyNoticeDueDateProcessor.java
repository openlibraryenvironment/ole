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

        if (null != loanDueDate) {
            String loanId = oleLoanDocument.getLoanId();
            Map<String, Object> courtesyMap = getNoticeInfoForTypeMap(noticeInfo);

            try {
                int intervalToGenerateCourtseyNotice = Integer.parseInt((String) courtesyMap.get(DroolsConstants.INTERVAL_TO_GENERATE_NOTICE_FOR_COURTESY));
                OLEDeliverNotice courtseyNotice = new OLEDeliverNotice();
                courtseyNotice.setNoticeType(OLEConstants.COURTESY_NOTICE);
                courtseyNotice.setNoticeSendType(DroolsConstants.EMAIL);
                Date dateToSent = calculateNoticeDueDate(loanDueDate, -intervalToGenerateCourtseyNotice, noticeInfo.getIntervalType());
                courtseyNotice.setNoticeToBeSendDate(new Timestamp(dateToSent.getTime()));
                courtseyNotice.setLoanId(loanId);
                courtseyNotice.setPatronId(oleLoanDocument.getPatronId());
                courtseyNotices.add(courtseyNotice);
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
}
