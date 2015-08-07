package org.kuali.ole.deliver.notice.noticeFormatters;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/25/15.
 */
public abstract class RequestEmailContentFormatter {
    private static final Logger LOG = Logger.getLogger(RequestExpirationEmailContentFormatter.class);
    private OlePatronHelperService olePatronHelperService;

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperService olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }



    public String getHeaderContent(String title){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + title + "</TITLE>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");

        return stringBuffer.toString();

    }


    private String getPatronName(OlePatronDocument olePatronDocument){
        String patronName = "";
        if(olePatronDocument!=null && olePatronDocument.getEntity()!=null && olePatronDocument.getEntity().getNames()!=null && olePatronDocument.getEntity().getNames().get(0)!=null){
            patronName = olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName();
        }
        return patronName;
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
                stringBuffer.append((fieldLabelMap.get(OLEConstants.PATRON_NAME)!=null ? fieldLabelMap.get(OLEConstants.PATRON_NAME):OLEConstants.PATRON_NAME) +":</TD><TD>" + getPatronName(olePatronDocument)  + "</TD></TR><TR><TD>");
                stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_ADDRESS)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_ADDRESS) :OLEConstants.NOTICE_ADDRESS)+":</TD><TD>" + (getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "") + "</TD></TR><TR><TD>");
                stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_EMAIL)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_EMAIL) :OLEConstants.NOTICE_EMAIL) +":</TD><TD>" + (getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "") + "</TD></TR><TR><TD>");

                stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_PHONE_NUMBER)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_PHONE_NUMBER) : OLEConstants.NOTICE_PHONE_NUMBER) +":</TD><TD>" + (getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "") + "</TD></TR>");
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

    public String generateRequestMailContentForPatron(List<OleDeliverRequestBo> oleDeliverRequestBos,Map<String,String> fieldLabelMap){
        StringBuffer stringBuffer = new StringBuffer();
        //Add header
        stringBuffer.append(getHeaderContent(fieldLabelMap.get("noticeTitle")));
        //Add patron info
        stringBuffer.append(getPatronInfo(oleDeliverRequestBos.get(0).getOlePatron(),fieldLabelMap));
        for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
            setItemContent(oleDeliverRequestBo,stringBuffer,fieldLabelMap);
        }
        return stringBuffer.toString();

    }


public void setItemContent(OleDeliverRequestBo oleDeliverRequestBo,StringBuffer stringBuffer,Map<String,String> fieldLabelMap){
    stringBuffer.append("<table>");
    if(null != getCustomItemHeaderInfo(oleDeliverRequestBo,fieldLabelMap)){
        stringBuffer.append(getCustomItemHeaderInfo(oleDeliverRequestBo,fieldLabelMap));
    }
    stringBuffer.append(generateItemInfoHTML(oleDeliverRequestBo,fieldLabelMap));
    if(null != getCustomItemFooterInfo(oleDeliverRequestBo,fieldLabelMap)){
        stringBuffer.append(getCustomItemFooterInfo(oleDeliverRequestBo,fieldLabelMap));
    }
    stringBuffer.append("</table>");

}



    public String generateItemInfoHTML(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("<TR><TD>");
            stringBuffer.append((fieldLabelMap.get(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME)!=null ?fieldLabelMap.get(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME):OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME) +":</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append((fieldLabelMap.get(OLEConstants.CIRCULATION_REPLY_TO_EMAIL)!=null ?fieldLabelMap.get(OLEConstants.CIRCULATION_REPLY_TO_EMAIL): OLEConstants.CIRCULATION_REPLY_TO_EMAIL )+":</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()!=null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail() : "" : "") + "</TD></TR><TR><TD>");
            stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_TITLE)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_TITLE):OLEConstants.NOTICE_TITLE) +":</TD><TD>" + (oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_AUTHOR)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_AUTHOR):OLEConstants.NOTICE_AUTHOR)+":</TD><TD>" + (oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : "") + "</TD></TR><TR><TD>");
            String volume = oleDeliverRequestBo.getEnumeration() != null && !oleDeliverRequestBo.getEnumeration().equals("") ? oleDeliverRequestBo.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = oleDeliverRequestBo.getCopyNumber() != null && !oleDeliverRequestBo.getCopyNumber().equals("") ? oleDeliverRequestBo.getCopyNumber() : "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;
            stringBuffer.append((fieldLabelMap.get(OLEConstants.VOLUME_ISSUE_COPY)!=null ?fieldLabelMap.get(OLEConstants.VOLUME_ISSUE_COPY):OLEConstants.VOLUME_ISSUE_COPY)+":</TD><TD>" + (volumeNumber != null ? volumeNumber : "") + "</TD></TR><TR><TD>");
            stringBuffer.append((fieldLabelMap.get(OLEConstants.LIBRARY_SHELVING_LOCATION)!=null ?fieldLabelMap.get(OLEConstants.LIBRARY_SHELVING_LOCATION):OLEConstants.LIBRARY_SHELVING_LOCATION)+":</TD><TD>" + ( oleDeliverRequestBo.getShelvingLocation()!= null ? oleDeliverRequestBo.getShelvingLocation() : "") + "</TD></TR><TR><TD>");
            try {
                String callNumber = "";
                if (oleDeliverRequestBo.getCallNumber() != null && !oleDeliverRequestBo.getCallNumber().equals("")) {
                    callNumber = oleDeliverRequestBo.getCallNumber();
                }
                stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_CALL_NUMBER)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_CALL_NUMBER):OLEConstants.NOTICE_CALL_NUMBER) +":</TD><TD>" + (callNumber != null ? callNumber : "") + "</TD></TR><TR><TD>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append((fieldLabelMap.get(OLEConstants.NOTICE_ITEM_BARCODE)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_ITEM_BARCODE):OLEConstants.NOTICE_ITEM_BARCODE)+":</TD><TD>" + (oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "") + "</TD></TR><TR><TD>");
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

    public EntityTypeContactInfoBo getEntityTypeContactInfo(OlePatronDocument olePatronDocument) {
        if(olePatronDocument!=null && olePatronDocument.getEntity()!=null && olePatronDocument.getEntity().getEntityTypeContactInfos()!=null){
            return olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
        }
        return null;
    }

    public abstract String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap);

    public abstract String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap);

}
