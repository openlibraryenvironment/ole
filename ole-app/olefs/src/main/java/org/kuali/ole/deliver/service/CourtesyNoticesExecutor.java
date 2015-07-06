package org.kuali.ole.deliver.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maheswarang on 4/8/15.
 */
public class CourtesyNoticesExecutor extends LoanNoticesExecutor {
    private static final Logger LOG = Logger.getLogger(CourtesyNoticesExecutor.class);
    private NoticeMailContentFormatter noticeMailContentFormatter;
    public CourtesyNoticesExecutor(List<OleLoanDocument> loanDocuments) {
        super(loanDocuments);
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    public List<OLEDeliverNotice> buildNoticesForDeletion() {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();
        for (OleLoanDocument loanDocument:loanDocuments) {

            if (loanDocument.getItemTypeName() != null) {
                loanDocument.setItemType(getItemTypeCodeByName(loanDocument.getItemTypeName()));
            }

            for (OLEDeliverNotice oleDeliverNotice : loanDocument.getDeliverNotices()) {
                LOG.info("CourtesyNoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread()+"Loan id-->"+loanDocument.getLoanId()+"notice id--->"+oleDeliverNotice.getId());
                Timestamp toBeSendDate = oleDeliverNotice.getNoticeToBeSendDate();
                if (oleDeliverNotice.getNoticeType().equals(OLEConstants.NOTICE_COURTESY) && toBeSendDate.compareTo
                        (getSendToDate(OLEConstants.COURTESY_NOTICE_TO_DATE)) < 0) {
                    try {
                        oleDeliverNotices.add(oleDeliverNotice);
                    } catch (Exception e) {
                        Log.info(e.getStackTrace());
                    }
                }
            }
        }

        return oleDeliverNotices;
    }

    @Override
    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments) {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                OLEParameterConstants
                        .COURTESY_TITLE);
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT);
        String mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments,title,body);

        return mailContent;
    }

    private NoticeMailContentFormatter getNoticeMailContentFormatter() {
        if (null == noticeMailContentFormatter) {
            noticeMailContentFormatter = new CourtesyNoticeEmailContentFormatter();
        }
        return noticeMailContentFormatter;
    }

    public void setNoticeMailContentFormatter(NoticeMailContentFormatter noticeMailContentFormatter) {
        this.noticeMailContentFormatter = noticeMailContentFormatter;
    }
}
