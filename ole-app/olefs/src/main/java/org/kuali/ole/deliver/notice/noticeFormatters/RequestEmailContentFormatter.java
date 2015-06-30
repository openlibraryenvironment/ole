package org.kuali.ole.deliver.notice.noticeFormatters;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.List;

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

    public String getPatronInfo(OlePatronDocument olePatronDocument,String title,String body) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = getEntityTypeContactInfo(olePatronDocument);

            stringBuffer.append("<HTML>");
            stringBuffer.append("<TITLE>" + title + "</TITLE>");
            stringBuffer.append("<HEAD></HEAD>");
            stringBuffer.append("<BODY>");

            try {
                stringBuffer.append("<TABLE></BR></BR>");
                stringBuffer.append("<TR><TD>Patron Name :</TD><TD>" + getPatronName(olePatronDocument)  + "</TD></TR>");
                stringBuffer.append("<TR><TD>Address :</TD><TD>" + (getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "") + "</TD></TR>");
                stringBuffer.append("<TR><TD>EMAIL :</TD><TD>" + (getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "") + "</TD></TR>");

                stringBuffer.append("<TR><TD>Phone Number :</TD><TD>" + (getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "") + "</TD></TR>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append("</TABLE>");

            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            stringBuffer.append("<TABLE width=\"100%\">");
            stringBuffer.append("<TR><TD><CENTER>" + title + "</CENTER></TD></TR>");
            stringBuffer.append("<TR><TD><p>" + body + "</p></TD></TR>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
        } catch (Exception e) {
            LOG.error("Error---->While generating overdue content for email(Patron Information) ");
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public String generateRequestMailContentForPatron(List<OleDeliverRequestBo> oleDeliverRequestBos,String title,String body){
        StringBuffer stringBuffer = new StringBuffer();
        //Add header
        stringBuffer.append(getHeaderContent(title));
        //Add patron info
        stringBuffer.append(getPatronInfo(oleDeliverRequestBos.get(0).getOlePatron(), title, body));
        for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
            setItemContent(oleDeliverRequestBo,stringBuffer);
        }
        return stringBuffer.toString();

    }


public void setItemContent(OleDeliverRequestBo oleDeliverRequestBo,StringBuffer stringBuffer){
    stringBuffer.append("<table>");
    if(null != getCustomItemHeaderInfo(oleDeliverRequestBo)){
        stringBuffer.append(getCustomItemHeaderInfo(oleDeliverRequestBo));
    }
    stringBuffer.append(generateItemInfoHTML(oleDeliverRequestBo));
    if(null != getCustomItemFooterInfo(oleDeliverRequestBo)){
        stringBuffer.append(getCustomItemFooterInfo(oleDeliverRequestBo));
    }
    stringBuffer.append("</table>");

}



    public String generateItemInfoHTML(OleDeliverRequestBo oleDeliverRequestBo) {
        StringBuffer stringBuffer = new StringBuffer();
        try {

            stringBuffer.append("<TR><TD>Circulation Location / Library Name :</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Circulation Reply-To Email :</TD><TD>" + (oleDeliverRequestBo.getOlePickUpLocation() != null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()!=null ? oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail() : "" : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Title :</TD><TD>" + (oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Author :</TD><TD>" + (oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : "") + "</TD></TR>");
            String volume = oleDeliverRequestBo.getEnumeration() != null && !oleDeliverRequestBo.getEnumeration().equals("") ? oleDeliverRequestBo.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = oleDeliverRequestBo.getCopyNumber() != null && !oleDeliverRequestBo.getCopyNumber().equals("") ? oleDeliverRequestBo.getCopyNumber() : "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;
            stringBuffer.append("<TR><TD>Volume/Issue/Copy Number :</TD><TD>" + (volumeNumber != null ? volumeNumber : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Library shelving location :</TD><TD>" + ( oleDeliverRequestBo.getShelvingLocation()!= null ? oleDeliverRequestBo.getShelvingLocation() : "") + "</TD></TR>");
            try {
                String callNumber = "";
                if (oleDeliverRequestBo.getCallNumber() != null && !oleDeliverRequestBo.getCallNumber().equals("")) {
                    callNumber = oleDeliverRequestBo.getCallNumber();
                }
                stringBuffer.append("<TR><TD>Call Number :</TD><TD>" + (callNumber != null ? callNumber : "") + "</TD></TR>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append("<TR><TD>Item Barcode :</TD><TD>" + (oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "") + "</TD></TR>");
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

    public abstract String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo);

    public abstract String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo);

}
