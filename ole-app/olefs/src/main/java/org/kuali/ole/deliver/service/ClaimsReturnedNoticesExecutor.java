package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.LoanNoticesExecutor;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;

import java.util.*;

/**
 * Created by chenchulakshmig on 3/15/16.
 */
public class ClaimsReturnedNoticesExecutor extends LoanNoticesExecutor {

    private NoticeMailContentFormatter noticeMailContentFormatter;

    public ClaimsReturnedNoticesExecutor(Map claimMap) {
        super(claimMap);
    }

    @Override
    protected String getNoticeType() {
        return OLEConstants.CLAIMS_RETURNED_NOTICE;
    }

    @Override
    protected void postProcess(List<OleLoanDocument> loanDocuments) {

    }

    @Override
    protected void preProcess(List<OleLoanDocument> loanDocuments) {
        if (CollectionUtils.isNotEmpty(loanDocuments)) {
            Set itemBarcodes = new HashSet();
            for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
                OleLoanDocument loanDocument = iterator.next();
                if (StringUtils.isNotBlank(loanDocument.getItemStatus()) && loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST)) {
                    itemBarcodes.add(loanDocument.getItemId());
                }
                if (isNotifyClaimsReturnedToPatron()) {
                    loanDocument.setNoOfClaimsReturnedNoticesSent(loanDocument.getNoOfClaimsReturnedNoticesSent() + 1);
                }
            }
            updatePaymentStatus(itemBarcodes, loanDocuments.get(0).getOlePatron().getOlePatronId());
        }
    }

    private void updatePaymentStatus(Set itemBarcodes, String olePatronId) {
        if (CollectionUtils.isNotEmpty(itemBarcodes) && StringUtils.isNotBlank(olePatronId)) {
            Map criteria = new HashMap<String, String>();
            criteria.put("patronBillPayment.patronId", olePatronId);
            criteria.put("itemBarcode", itemBarcodes);
            criteria.put("oleFeeType.feeTypeCode", Arrays.asList(OLEConstants.FEE_TYPE_CODE_REPL_FEE, OLEConstants.LOST_ITEM_PRCS_FEE));
            List<FeeType> feeTypes = (List<FeeType>) getBusinessObjectService().findMatching(FeeType.class, criteria);
            if (CollectionUtils.isNotEmpty(feeTypes)) {
                OlePaymentStatus paymentStatus = new PatronBillHelperService().getPaymentStatus(OLEConstants.SUSPENDED);
                if (paymentStatus != null) {
                    for (FeeType feeType : feeTypes) {
                        feeType.setPaymentStatus(paymentStatus.getPaymentStatusId());
                    }
                    getBusinessObjectService().save(feeTypes);
                }
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
            mailContent = getNoticeMailContentFormatter().generateMailContentForPatron(oleLoanDocuments, oleNoticeContentConfigurationBo);
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
        if(noticeContentConfigName != null){
            noticeConfigurationMap.put("noticeName", noticeContentConfigName);
        }
        oleNoticeContentConfigurationBoList = (List<OleNoticeContentConfigurationBo>) getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class, noticeConfigurationMap);
        if (oleNoticeContentConfigurationBoList != null && oleNoticeContentConfigurationBoList.size() > 0) {
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        } else {
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeTitle(getTitle());
            oleNoticeContentConfigurationBo.setNoticeBody(getBody());
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(getNoticeType()));
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeType(getNoticeType());
        }
    }

    public String getTitle() {
        String title = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.CLAIMS_RETURNED_NOTICE_TITLE);
        return title;
    }

    public String getBody() {
        String body = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_NOTICE_CONTENT);
        return body;
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

}
