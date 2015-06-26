package org.kuali.ole.deliver.notice.executors;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.List;

/**
 * Created by maheswarang on 6/24/15.
 */
public abstract class RequestNoticesExecutor implements Runnable {

    protected List<OleDeliverRequestBo> deliverRequestBos;
    private OleMailer oleMailer;
    private ParameterValueResolver parameterResolverInstance;
    private CircDeskLocationResolver circDeskLocationResolver;



    public RequestNoticesExecutor(List<OleDeliverRequestBo> oleDeliverRequestBos){
        this.deliverRequestBos = oleDeliverRequestBos;
    }

    public abstract RequestEmailContentFormatter getRequestEmailContentFormatter();

    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }



    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (null == circDeskLocationResolver) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    private OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }

    public BusinessObjectService getBusinessObjectService(){
        return KRADServiceLocator.getBusinessObjectService();
    }

    public void setOleMailer(OleMailer oleMailer) {
        this.oleMailer = oleMailer;
    }



    public void setParameterResolverInstance(ParameterValueResolver parameterResolverInstance) {
        this.parameterResolverInstance = parameterResolverInstance;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    @Override
    public void run() {

          String mailContent = generateMailContent(deliverRequestBos);

          sendMail(mailContent);
        
          postProcess(); 



    }

    protected abstract void postProcess();


    public String generateMailContent(List<OleDeliverRequestBo> oleDeliverRequestBos) {

        String mailContent = getRequestEmailContentFormatter().generateRequestMailContentForPatron(oleDeliverRequestBos,getTitle(), getBody());
        return mailContent;

    }


    public abstract String getTitle();

    public abstract String getBody();





    public void sendMail(String mailContent) {
        OlePatronDocument olePatron = deliverRequestBos.get(0).getOlePatron();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                    .getEntityTypeContactInfos().get(0);
            String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                    getPatronHomeEmailId(entityTypeContactInfoBo) : "";

            if (deliverRequestBos.size() == 1) {
                sendMailsToPatron(emailAddress, mailContent, deliverRequestBos.get(0).getItemLocation());
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



}
