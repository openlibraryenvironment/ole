package org.kuali.ole.deliver.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.service.OlePatronLoanNoticeService;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.sql.Timestamp;

/**
 * Created by maheswarang on 1/18/16.
 */
public class OlePatronLoanNoticeServiceImpl implements OlePatronLoanNoticeService {

    private ParameterValueResolver parameterResolverInstance;
    private OleMailer oleMailer;
    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }

    public OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }
    @Override
    public boolean sendMail(String toMailAddress, String mailContent,String mailSubject) {
           boolean mailSent = false;
           String fromAddress = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                        .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants
                        .NOTICE_FROM_MAIL);
                      try {
                if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                    fromAddress = OLEConstants.KUALI_MAIL;
                }
                if (toMailAddress != null && !toMailAddress.isEmpty()) {
                    mailContent = mailContent.replace('[', ' ');
                    mailContent = mailContent.replace(']', ' ');
                    if (!mailContent.trim().equals("")) {
                        OleMailer oleMailer = getOleMailer();
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(toMailAddress), new EmailSubject(mailSubject), new EmailBody(mailContent), true);
                        mailSent = true;
                    }
                } else {
                }
            } catch (Exception e) {

            }

            return mailSent;
        }



    @Override
    public OLEDeliverNoticeHistory cloneOleDeliverNoticeHistory(OLEDeliverNoticeHistory oleDeliverNoticeHistory) {
        OLEDeliverNoticeHistory clonedOleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
        clonedOleDeliverNoticeHistory.setNoticeType(oleDeliverNoticeHistory.getNoticeType());
        clonedOleDeliverNoticeHistory.setLoanId(oleDeliverNoticeHistory.getLoanId());
        clonedOleDeliverNoticeHistory.setNoticeContent(oleDeliverNoticeHistory.getNoticeContent());
        clonedOleDeliverNoticeHistory.setItemBarcode(oleDeliverNoticeHistory.getItemBarcode());
        clonedOleDeliverNoticeHistory.setNoticeSendType(oleDeliverNoticeHistory.getNoticeSendType());
        clonedOleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(System.currentTimeMillis()));
        clonedOleDeliverNoticeHistory.setPatronId(oleDeliverNoticeHistory.getPatronId());
        clonedOleDeliverNoticeHistory.setRequestId(oleDeliverNoticeHistory.getRequestId());
        return clonedOleDeliverNoticeHistory;
    }
}
