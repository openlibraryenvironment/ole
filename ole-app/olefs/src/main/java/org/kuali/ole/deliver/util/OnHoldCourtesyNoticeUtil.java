package org.kuali.ole.deliver.util;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.service.OleNoticeService;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 7/29/15.
 */
public class OnHoldCourtesyNoticeUtil {
    private static final Logger LOG = Logger.getLogger(OnHoldCourtesyNoticeUtil.class);
    private OlePatronHelperService olePatronHelperService;
    private OleMailer oleMailer;
    private CircDeskLocationResolver circDeskLocationResolver;
    private ParameterValueResolver parameterResolverInstance;

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        if (null == circDeskLocationResolver) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }

    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }

    public void setOlePatronHelperService(OlePatronHelperService olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }

    public void setOleMailer(OleMailer oleMailer) {
        this.oleMailer = oleMailer;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public void setParameterResolverInstance(ParameterValueResolver parameterResolverInstance) {
        this.parameterResolverInstance = parameterResolverInstance;
    }

    public String getHeaderContent(String title){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + title + "</TITLE>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");

        return stringBuffer.toString();

    }

    public String getPatronInfo(OlePatronDocument olePatronDocument,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = getEntityTypeContactInfo(olePatronDocument);
            stringBuffer.append("<HTML>");
            stringBuffer.append("<TITLE>" + fieldLabelMap.get("noticeTitle") + "</TITLE>");
            stringBuffer.append("<HEAD></HEAD>");
            stringBuffer.append("<BODY>");
            try {
                stringBuffer.append("<TABLE></BR></BR><TR><TD>");
                stringBuffer.append("Patron Name </TD><TD>:</TD><TD>" + olePatronDocument.getPatronName()  + "</TD></TR><TR><TD>");
                String patronPreferredAddress = getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo);
                stringBuffer.append("Patron Address </TD><TD>:</TD><TD>" + (patronPreferredAddress != null ? patronPreferredAddress : "") + "</TD></TR><TR><TD>");
                String patronHomeEmailId = getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo);
                stringBuffer.append("Patron Email </TD><TD>:</TD><TD>" + (patronHomeEmailId != null ? patronHomeEmailId : "") + "</TD></TR><TR><TD>");
                String patronHomePhoneNumber = getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo);
                stringBuffer.append("Patron Phone Number </TD><TD>:</TD><TD>" + (patronHomePhoneNumber != null ? patronHomePhoneNumber : "") + "</TD></TR>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append("</TABLE>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            stringBuffer.append("<TABLE width=\"100%\">");
            stringBuffer.append("<TR><TD><CENTER>" + fieldLabelMap.get("noticeTitle") + "</CENTER></TD></TR>");
            stringBuffer.append("<TR><TD><p>" + fieldLabelMap.get("noticeBody") + "</p></TD></TR>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
        } catch (Exception e) {
            LOG.error("Error---->While generating overdue content for email(Patron Information) ");
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public EntityTypeContactInfoBo getEntityTypeContactInfo(OlePatronDocument olePatronDocument) {
        if(olePatronDocument!=null && olePatronDocument.getEntity()!=null && olePatronDocument.getEntity().getEntityTypeContactInfos()!=null){
            return olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
        }
        return null;
    }

    public void setItemContent(OleDeliverRequestBo oleDeliverRequestBo,StringBuffer stringBuffer,Map<String,String> fieldLabelMap){
        stringBuffer.append("<table>");
        stringBuffer.append(generateItemInfoHTML(oleDeliverRequestBo,fieldLabelMap));
        if(null != getCustomItemFooterInfo(oleDeliverRequestBo,fieldLabelMap)){
            stringBuffer.append(getCustomItemFooterInfo(oleDeliverRequestBo,fieldLabelMap));
        }
        stringBuffer.append("</table>");

    }

    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<TR><TD>");
        stringBuffer.append(fieldLabelMap.get("Hold Expiration Date")!=null ? fieldLabelMap.get("Hold Expiration Date") : "Hold Expiration Date"+"</TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getHoldExpirationDate() != null ? oleDeliverRequestBo.getHoldExpirationDate() : "") + "</TD></TR>");
        return stringBuffer.toString();
    }


    public String generateRequestMailContentForPatron(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getHeaderContent(fieldLabelMap.get("noticeTitle")));
        stringBuffer.append(getPatronInfo(oleDeliverRequestBo.getOlePatron(), fieldLabelMap));
        setItemContent(oleDeliverRequestBo, stringBuffer, fieldLabelMap);
        return stringBuffer.toString();

    }

    public String generateItemInfoHTML(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("<TR><TD>");
            stringBuffer.append("Circulation Location/Library Name </TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append("Circulation Reply-To Email </TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()!=null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail() : "" : "") + "</TD></TR><TR><TD>");
            stringBuffer.append("Title </TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append("Author </TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : "") + "</TD></TR><TR><TD>");
            String volume = oleDeliverRequestBo.getEnumeration() != null && !oleDeliverRequestBo.getEnumeration().equals("") ? oleDeliverRequestBo.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = oleDeliverRequestBo.getCopyNumber() != null && !oleDeliverRequestBo.getCopyNumber().equals("") ? oleDeliverRequestBo.getCopyNumber() : "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;
            stringBuffer.append("Volume/Issue/Copy # </TD><TD>:</TD><TD>" + (volumeNumber != null ? volumeNumber : "") + "</TD></TR><TR><TD>");
            stringBuffer.append("Library shelving location </TD><TD>:</TD><TD>" + ( oleDeliverRequestBo.getShelvingLocation()!= null ? oleDeliverRequestBo.getShelvingLocation() : "") + "</TD></TR><TR><TD>");
            try {
                String callNumber = "";
                if (oleDeliverRequestBo.getCallNumber() != null && !oleDeliverRequestBo.getCallNumber().equals("")) {
                    callNumber = oleDeliverRequestBo.getCallNumber();
                }
                stringBuffer.append("Call # </TD><TD>:</TD><TD>" + (callNumber != null ? callNumber : "") + "</TD></TR><TR><TD>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append("Item Barcode </TD><TD>:</TD><TD>" + (oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");

        } catch (Exception e) {
            LOG.error("Error---->While generating HTML overdue content  ");
            if (oleDeliverRequestBo != null) {
                LOG.error("Error---->Item Barcode " + oleDeliverRequestBo.getItemId());
            }
            LOG.error(e.getMessage() + e);
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public void sendMail(OleDeliverRequestBo oleDeliverRequestBo, String mailContent) {
        OlePatronDocument olePatron = oleDeliverRequestBo.getOlePatron();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                    .getEntityTypeContactInfos().get(0);
            String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                    getPatronHomeEmailId(entityTypeContactInfoBo) : "";


            sendMailsToPatron(emailAddress, mailContent, oleDeliverRequestBo.getItemLocation());
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
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(OLEConstants.NOTICE_HOLD_COURTESY)), new EmailBody(noticeContent), true);
                }
            } else {
            }
        } catch (Exception e) {
        }

        return noticeContent;
    }

    public void sendOnHoldNotice(OleDeliverRequestBo oleDeliverRequestBo){
        Map<String,String> fieldLabelMap  = new HashMap<>();
        fieldLabelMap.put("noticeTitle","OnHoldNotice");
        fieldLabelMap.put("noticeBody"," The following requested item(s) is ready for pick-up and will be held until the expiration date at the location shown below.");
        String mailContent = generateRequestMailContentForPatron(oleDeliverRequestBo,fieldLabelMap);
        sendMail(oleDeliverRequestBo,mailContent);
    }
}
