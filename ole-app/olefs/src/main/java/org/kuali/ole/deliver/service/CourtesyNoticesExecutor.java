package org.kuali.ole.deliver.service;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 4/8/15.
 */
public class CourtesyNoticesExecutor extends LoanNoticesExecutor {
    private static final Logger LOG = Logger.getLogger(CourtesyNoticesExecutor.class);
    private NoticeMailContentFormatter noticeMailContentFormatter;

    public CourtesyNoticesExecutor(Map courtesyMap) {
        super(courtesyMap);
    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.COURTESY_NOTICE;
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
                if (oleDeliverNotice.getNoticeType().equals(OLEConstants.COURTESY_NOTICE) && toBeSendDate.compareTo
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
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.COURTESY_NOTICE);
        noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
       if(oleNoticeContentConfigurationBoList!=null && oleNoticeContentConfigurationBoList.size()>0){
           oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
       }else{
           oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
           oleNoticeContentConfigurationBo.setNoticeTitle(getTitle());
           oleNoticeContentConfigurationBo.setNoticeBody(getBody());
           oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.COURTESY_NOTICE);
           oleNoticeContentConfigurationBo.setNoticeFooterBody("");
       }
    }

    @Override
    public void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        this.oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo;
    }

    public String getTitle() {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                OLEParameterConstants
                        .COURTESY_TITLE);
        return title;
    }


    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT);
        return body;
    }

    @Override
    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments){

        String mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments,oleNoticeContentConfigurationBo);

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
