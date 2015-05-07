package org.kuali.ole.deliver.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 4/8/15.
 */
public abstract class NoticesExecutor implements Runnable {

    private static final Logger LOG = Logger.getLogger(NoticesExecutor.class);
    private OleMailer oleMailer;
    protected List<OleLoanDocument> loanDocuments;
    private BusinessObjectService businessObjectService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private ParameterValueResolver parameterResolverInstance;

    public NoticesExecutor(List<OleLoanDocument> loanDocuments) {
        this.loanDocuments = loanDocuments;
    }

    @Override
    public void run() {
        LOG.info("NoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread());

        //1. Pre process
        preProcess(loanDocuments);
        //2. generate email content
        String mailContent = generateMailContent(loanDocuments);
        //3. Generate notices
        List<OLEDeliverNotice> oleDeliverNotices = buildNoticesForDeletion();
        //4. Save loan document
        getBusinessObjectService().save(loanDocuments);
        //5. Delete notices
        deleteNotices(oleDeliverNotices);
        //6. update notice history
        saveOLEDeliverNoticeHistory(oleDeliverNotices, mailContent);
        //7. send mail
        sendMail(mailContent);
        //8 Post process
        postProcess(loanDocuments);
    }

    private void sendMail(String mailContent) {
        OlePatronDocument olePatron = loanDocuments.get(0).getOlePatron();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                    .getEntityTypeContactInfos().get(0);
            String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                    getPatronHomeEmailId(entityTypeContactInfoBo) : "";

            if (loanDocuments.size() == 1) {
                sendMailsToPatron(emailAddress, mailContent, loanDocuments.get(0).getItemLocation());
            } else {
                sendMailsToPatron(emailAddress, mailContent, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPatronHomeEmailId(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        String emailId = "";
        if (entityTypeContactInfoBo.getEmailAddresses() != null) {
            for (int j = 0; j < entityTypeContactInfoBo.getEmailAddresses().size(); j++) {
                if (entityTypeContactInfoBo.getEmailAddresses().get(j).getDefaultValue()) {
                    emailId = (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailAddress());
                    break;
                }
            }
        }
        return emailId;
    }

    public String sendMailsToPatron(String emailAddress, String noticeContent, String itemLocation) {
        String fromAddress = getCircDeskLocationResolver().getReplyToEmail(itemLocation);

        if (fromAddress == null) {
            fromAddress = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants
                    .NOTICE_FROM_MAIL);
        }
        try {
            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                fromAddress = OLEConstants.KUALI_MAIL;
            }
            if (emailAddress != null && !emailAddress.isEmpty()) {
                noticeContent = noticeContent.replace('[', ' ');
                noticeContent = noticeContent.replace(']', ' ');
                if (!noticeContent.trim().equals("")) {
                    OleMailer oleMailer = getOleMailer();
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(noticeContent), true);
                }
            } else {
            }
        } catch (Exception e) {
        }

        return noticeContent;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (null == circDeskLocationResolver) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    private OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }

    private void deleteNotices(List<OLEDeliverNotice> oleDeliverNotices) {
            getBusinessObjectService().delete(oleDeliverNotices);
    }

    private void saveOLEDeliverNoticeHistory(List<OLEDeliverNotice> oleDeliverNotices, String mailContent) {
        for (OLEDeliverNotice oleDeliverNotice : oleDeliverNotices) {
                OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
                oleDeliverNoticeHistory.setLoanId(oleDeliverNotice.getLoanId());
                oleDeliverNoticeHistory.setNoticeType(oleDeliverNotice.getNoticeType());
                oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
                oleDeliverNoticeHistory.setPatronId(oleDeliverNotice.getPatronId());
                oleDeliverNoticeHistory.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
                oleDeliverNoticeHistory.setNoticeContent(mailContent.getBytes());
                getBusinessObjectService().save(oleDeliverNoticeHistory);
        }
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }



    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    protected abstract void postProcess(List<OleLoanDocument> loanDocuments);
    protected abstract void preProcess(List<OleLoanDocument> loanDocuments);
    public abstract List<OLEDeliverNotice> buildNoticesForDeletion();
    public abstract String generateMailContent(List<OleLoanDocument> oleLoanDocuments);

    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }

    public String getItemTypeCodeByName(String itemTypeName) {
        String itemTypeCode = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put("instanceItemTypeName", itemTypeName);
        instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            itemTypeCode = instanceItemTypeList.get(0).getInstanceItemTypeCode();
        }
        return itemTypeCode;
    }

    protected Timestamp getSendToDate(String noticeToDate) {
        String lostNoticeToDate;
        lostNoticeToDate = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, noticeToDate);
        Timestamp lostNoticetoSendDate = new Timestamp(System.currentTimeMillis());
        if (!StringUtils.isEmpty(lostNoticeToDate)) {
            lostNoticetoSendDate = new Timestamp(new Date(lostNoticeToDate).getTime());
        }
        return lostNoticetoSendDate;
    }

}
