package org.kuali.ole.deliver.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.service.ClaimsReturnedNoticeEmailContentFormatter;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.util.RiceConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by govindarajank on 26/4/17.
 */
public class CheckoutReceiptNoticeExecutor extends LoanNoticesExecutor {

    private NoticeMailContentFormatter noticeMailContentFormatter;

    public CheckoutReceiptNoticeExecutor(Map receiptMap) {
        super(receiptMap);
    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.CHECKOUT_RECEIPT_NOTICE;
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {
        if (CollectionUtils.isNotEmpty(loanDocuments)) {
        Set itemBarcodes = new HashSet();
            for(OleLoanDocument oleLoanDocument : loanDocuments){
                itemBarcodes.add(oleLoanDocument.getItemId());
            }
        }
    }

    @Override
    public List<OLEDeliverNotice> buildNoticesForDeletion() {
        List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(loanDocuments)) {
            for (OleLoanDocument oleLoanDocument : loanDocuments) {
                if (CollectionUtils.isNotEmpty(oleLoanDocument.getDeliverNotices())) {
                    for (OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()) {
                        if (oleDeliverNotice != null && oleDeliverNotice.getNoticeType().equalsIgnoreCase(getNoticeType())) {
                            oleDeliverNoticeList.add(oleDeliverNotice);
                            break;
                        }
                    }
                }
            }
        }
        return oleDeliverNoticeList;
    }

    @Override
    public List<OLEDeliverNoticeHistory> saveOLEDeliverNoticeHistory(List<OLEDeliverNotice> oleDeliverNotices, String mailContent) {
        if (isNotifyClaimsReturnedToPatron() && StringUtils.isNotBlank(mailContent)) {
            return super.saveOLEDeliverNoticeHistory(oleDeliverNotices, mailContent);
        }
        return null;
    }

    @Override
    public String generateMailContent(List<OleLoanDocument> oleLoanDocuments) {
        String mailContent = null;
        if (isNotifyClaimsReturnedToPatron()) {
            mailContent = generateMailContentForPatron(oleLoanDocuments, oleNoticeContentConfigurationBo);
        }
        return mailContent;
    }

    @Override
    public void sendMail(String mailContent) {
        if (isNotifyClaimsReturnedToPatron()) {
            super.sendMail(mailContent);
        }
    }
    public void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        this.oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBo;
    }

    @Override
    public void setOleNoticeContentConfigurationBo() {
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String, String> noticeConfigurationMap = new HashMap<String, String>();
        noticeConfigurationMap.put("noticeType", getNoticeType());
        oleNoticeContentConfigurationBoList = (List<OleNoticeContentConfigurationBo>) getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class, noticeConfigurationMap);
        if (oleNoticeContentConfigurationBoList != null && oleNoticeContentConfigurationBoList.size() > 0) {
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        } else {
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeTitle(OLEConstants.CHECKOUT_RECEIPT_NOTICE);
            oleNoticeContentConfigurationBo.setNoticeBody("");
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(getNoticeType()));
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeType(getNoticeType());
        }
    }


    private NoticeMailContentFormatter getNoticeMailContentFormatter() {
        if (null == noticeMailContentFormatter) {
            noticeMailContentFormatter = new ClaimsReturnedNoticeEmailContentFormatter();
        }
        return noticeMailContentFormatter;
    }

    public void setNoticeMailContentFormatter(NoticeMailContentFormatter noticeMailContentFormatter) {
        this.noticeMailContentFormatter = noticeMailContentFormatter;
    }

    public boolean isNotifyClaimsReturnedToPatron() {
        return ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants.APPL_ID_OLE,
                OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTIFY_CLAIMS_RETURNED_TO_PATRON);
    }

    public String generateMailContentForPatron(List<OleLoanDocument> oleLoanDocuments, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        StringBuilder noticeHtmlContent = new StringBuilder();
        int count = 1;
        SimpleDateFormat df = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        SimpleDateFormat df2 = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
        noticeHtmlContent.append("<HTML>\n<TITLE>");
        noticeHtmlContent.append(oleNoticeContentConfigurationBo.getNoticeTitle() + "</TITLE>\n \n <HEAD></HEAD>\n <BODY>");
        noticeHtmlContent.append("<table>");
        noticeHtmlContent.append("<tr><td>" +oleNoticeContentConfigurationBo.getNoticeTitle() + "<tr><td>");
        noticeHtmlContent.append("<tr><td>" +oleLoanDocuments.get(0).getOlePatron().getPatronName() + "<tr><td>");
        noticeHtmlContent.append("<tr><td>" +oleLoanDocuments.get(0).getOlePatron().getBarcode() + "<tr><td>");
        noticeHtmlContent.append("<tr><td><p>" +oleNoticeContentConfigurationBo.getNoticeBody() + "</p><tr><td>");
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments) {
            noticeHtmlContent.append("<tr><td>" + oleLoanDocument.getTitle() + "</td></tr>");
            noticeHtmlContent.append("<tr><td>" + oleLoanDocument.getItemId() + "</td></tr>");
            if(oleLoanDocument.getLoanDueDate()!=null) {
                noticeHtmlContent.append("<tr><td> <b> Due : </b>" + df.format(oleLoanDocument.getLoanDueDate()) + "</td></tr>");
            }
            noticeHtmlContent.append("<tr><td>*************************************************<br/></td></tr>");
        }
        noticeHtmlContent.append("</table><br/>");
        SimpleDateFormat df1 = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        noticeHtmlContent.append("<b>Total Items</b> :" + oleLoanDocuments.size()+ "\n<br/>");
        noticeHtmlContent.append("<b>Date</b> :" + df2.format(System.currentTimeMillis())+ "\n<br/>");
        noticeHtmlContent.append("<b>Time</b> :" + df1.format(System.currentTimeMillis())+ "\n<br/><br/>");
        noticeHtmlContent.append("<p>" +oleNoticeContentConfigurationBo.getNoticeFooterBody() + "</p>");
        noticeHtmlContent.append("</BODY>");
        noticeHtmlContent.append("</HTML>");
        return noticeHtmlContent.toString();
    }
}
