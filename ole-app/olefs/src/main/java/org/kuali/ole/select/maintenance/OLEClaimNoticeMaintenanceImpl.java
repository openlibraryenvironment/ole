package org.kuali.ole.select.maintenance;

import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.select.bo.OLEClaimNoticeBo;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceLock;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/2/14
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimNoticeMaintenanceImpl extends MaintainableImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEClaimNoticeMaintenanceImpl.class);


    @Override
    public void doRouteStatusChange(DocumentHeader documentHeader) {
         LOG.debug("doRouteStatusChange() starting");
        super.doRouteStatusChange(documentHeader);

        try {
            if (documentHeader.getWorkflowDocument().isFinal()) {
                OLEClaimNoticeBo oleClaimNoticeBo = (OLEClaimNoticeBo) this.getDataObject();
                OleMailer oleMail= GlobalResourceLoader.getService("oleMailer");
                if(oleClaimNoticeBo.getMailAddress()!=null && !oleClaimNoticeBo.getMailAddress().isEmpty()){
                    LoanProcessor loanProcessor = new LoanProcessor();
                    String fromMail = loanProcessor.getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    oleMail.sendEmail(new EmailFrom(fromMail),new EmailTo(oleClaimNoticeBo.getMailAddress()), new EmailSubject("Claim Report"), new EmailBody(claimReportNotice(oleClaimNoticeBo)), true);
                    if (LOG.isInfoEnabled()){
                        LOG.info("Mail send successfully to "+oleClaimNoticeBo.getMailAddress());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception saving routing data while saving document with id " + getDocumentNumber(), e);
        }
        LOG.debug("doRouteStatusChange() ending");
    }

    public String claimReportNotice(OLEClaimNoticeBo oleClaimNoticeBo ) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + oleClaimNoticeBo.getTitle() + "</TITLE>");
        stringBuffer.append("<HEAD><TR><TD><CENTER>" + oleClaimNoticeBo.getTitle() + "</CENTER></TD></TR></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>Name of the Sender :</TD><TD>" + oleClaimNoticeBo.getNameOfTheSender() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Name of the Vendor :</TD><TD>" + oleClaimNoticeBo.getNameOfTheVendor() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Claim Date :</TD><TD>" + oleClaimNoticeBo.getClaimDate() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Claim Count :</TD><TD>" + oleClaimNoticeBo.getClaimCount() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Claim Type :</TD><TD>" + oleClaimNoticeBo.getClaimTypeName() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Title :</TD><TD>" + oleClaimNoticeBo.getTitle() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Place of publication :</TD><TD>" + oleClaimNoticeBo.getPlaceOfPublication() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Publication :</TD><TD>" + oleClaimNoticeBo.getPublication() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Publication Date :</TD><TD>" + oleClaimNoticeBo.getPublicationDate() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Enumeration :</TD><TD>" + oleClaimNoticeBo.getEnumeration() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Chronology :</TD><TD>" + oleClaimNoticeBo.getChronology() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Vendor's Library Account Number :</TD><TD>" + oleClaimNoticeBo.getVendorsLibraryAcctNum() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Vendor Account Number :</TD><TD>" + oleClaimNoticeBo.getVendorOrderNumber() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Vendor Title Number :</TD><TD>" + oleClaimNoticeBo.getVendorTitleNumber() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Unbound Location :</TD><TD>" + oleClaimNoticeBo.getUnboundLocation() + "</TD></TR>");
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</BODY>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();

    }

    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        return Collections.emptyList();
    }

}
