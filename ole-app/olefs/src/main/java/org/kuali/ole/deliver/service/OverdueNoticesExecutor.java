package org.kuali.ole.deliver.service;

import org.apache.commons.lang3.StringUtils;
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
 * Created by premkb on 3/30/15.
 */
public class OverdueNoticesExecutor extends LoanNoticesExecutor {
    private static final Logger LOG = Logger.getLogger(OverdueNoticesExecutor.class);
    private NoticeMailContentFormatter noticeMailContentFormatter;

    public OverdueNoticesExecutor(Map overdueMap) {
        super(overdueMap);
    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.OVERDUE_NOTICE;
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {

    }

    public List<OLEDeliverNotice> buildNoticesForDeletion() {
        List<OLEDeliverNotice> oleDeliverNotices = new ArrayList<>();
        for (OleLoanDocument loanDocument:loanDocuments) {
            if (loanDocument.getItemTypeName() != null) {
                loanDocument.setItemType(getItemTypeCodeByName(loanDocument.getItemTypeName()));
            }
            String overdueNoticeToDate = getParameterResolverInstance().getParameter(OLEConstants
                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OVERDUE_NOTICE_TO_DATE);
            Timestamp overdueNoticetoSendDate = new Timestamp(System.currentTimeMillis());
            if (!StringUtils.isEmpty(overdueNoticeToDate)) {
                overdueNoticetoSendDate = new Timestamp(new Date(overdueNoticeToDate).getTime());
            }
            for (OLEDeliverNotice oleDeliverNotice : loanDocument.getDeliverNotices()) {
                LOG.info("OverdueNoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread()+"Loan id-->"+loanDocument.getLoanId()+"notice id--->"+oleDeliverNotice.getId());
                Timestamp toBeSendDate = oleDeliverNotice.getNoticeToBeSendDate();
                if (oleDeliverNotice.getNoticeType().equals(OLEConstants.OVERDUE_NOTICE) && toBeSendDate.compareTo(overdueNoticetoSendDate) < 0) {
                    try {
                        int noOfOverdueNoticeSent = Integer.parseInt(loanDocument.getNumberOfOverdueNoticesSent() != null ? loanDocument.getNumberOfOverdueNoticesSent() : "0");
                        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                        loanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                        loanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                        //getBusinessObjectService().save(loanDocument);
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
        noticeConfigurationMap.put("noticeType",OLEConstants.OVERDUE_NOTICE);
        noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(oleNoticeContentConfigurationBoList!=null && oleNoticeContentConfigurationBoList.size()>0){
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        }else{
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeTitle(getTitle());
            oleNoticeContentConfigurationBo.setNoticeBody(getBody());
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.OVERDUE_NOTICE);
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
                        .OVERDUE_TITLE);
        return title;
    }


    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT);
        return body;
    }


    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments) {
        String mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments,oleNoticeContentConfigurationBo);
        return mailContent;
    }

    private NoticeMailContentFormatter getNoticeMailContentFormatter() {
        if (null == noticeMailContentFormatter) {
            noticeMailContentFormatter = new OverdueNoticeEmailContentFormatter();
        }
        return noticeMailContentFormatter;
    }

    public void setNoticeMailContentFormatter(NoticeMailContentFormatter noticeMailContentFormatter) {
        this.noticeMailContentFormatter = noticeMailContentFormatter;
    }

}
