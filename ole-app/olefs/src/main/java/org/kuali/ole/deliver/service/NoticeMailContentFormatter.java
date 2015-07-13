package org.kuali.ole.deliver.service;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 4/6/15.
 */
public abstract class NoticeMailContentFormatter {
    private static final Logger LOG = Logger.getLogger(NoticeMailContentFormatter.class);
    private ParameterValueResolver parameterValueResolver;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private BusinessObjectService businessObjectService;
    private OlePatronHelperServiceImpl olePatronHelperService;

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperServiceImpl olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }


    public String generateMailContentForPatron(List<OleLoanDocument> oleLoanDocuments,Map<String,String> fieldLabelMap){
        StringBuffer stringBuffer = new StringBuffer();
        //Add header
        stringBuffer.append(getHeaderContent(fieldLabelMap.get("noticeTitle")));
        //Add patron info
        stringBuffer.append(getPatronInfo(oleLoanDocuments.get(0).getOlePatron(),fieldLabelMap));
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            //add custom HTML (Overdue or Replacement or Courtsey)
            String generateCustomHTML = generateCustomHTML(oleLoanDocument);
            if (null != generateCustomHTML) {
                stringBuffer.append(generateCustomHTML);
            }
            //add item info
            stringBuffer.append(generateItemInfoHTML(oleLoanDocument,fieldLabelMap));
        }
        return stringBuffer.toString();

    }

    protected abstract String generateCustomHTML(OleLoanDocument oleLoanDocument);

    public String getHeaderContent(String title){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + title + "</TITLE>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");

        return stringBuffer.toString();

    }

    private ParameterValueResolver getParameterInstance() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    public String getPatronInfo(OlePatronDocument olePatronDocument,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            OleDeliverRequestDocumentHelperServiceImpl deliverService = getOleDeliverRequestDocumentHelperService();
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);

            stringBuffer.append("<HTML>");
            stringBuffer.append("<TITLE>" + fieldLabelMap.get("noticeTitle") + "</TITLE>");
            stringBuffer.append("<HEAD></HEAD>");
            stringBuffer.append("<BODY>");

            try {
                stringBuffer.append("<TABLE></BR></BR><TR><TD>");
                stringBuffer.append(fieldLabelMap.get(OLEConstants.PATRON_NAME)!=null ? fieldLabelMap.get(OLEConstants.PATRON_NAME):OLEConstants.PATRON_NAME +":</TD><TD>"  + olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName() + "</TD></TR><TR><TD>");
                stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_ADDRESS)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_ADDRESS) :OLEConstants.NOTICE_ADDRESS+":</TD><TD>" + (getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "") + "</TD></TR><TR><TD>");
                stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_EMAIL)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_EMAIL) :OLEConstants.NOTICE_EMAIL +":</TD><TD>" + (getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "") + "</TD></TR><TR><TD>");

                stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_PHONE_NUMBER)!=null ? fieldLabelMap.get(OLEConstants.NOTICE_PHONE_NUMBER) : OLEConstants.NOTICE_PHONE_NUMBER +":</TD><TD>" + (getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "") + "</TD></TR><TR><TD>");
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
        return stringBuffer.toString();
    }

    private OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (null == oleDeliverRequestDocumentHelperService) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public String generateItemInfoHTML(OleLoanDocument oleLoanDocument,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stringBuffer.append("<table><TR><TD>");
            stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_TITLE)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_TITLE):OLEConstants.NOTICE_TITLE +":</TD><TD>" + (oleLoanDocument.getTitle() != null ? oleLoanDocument.getTitle() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_AUTHOR)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_AUTHOR):OLEConstants.NOTICE_AUTHOR+":</TD><TD>" + (oleLoanDocument.getAuthor() != null ? oleLoanDocument.getAuthor() : "") + "</TD></TR><TR><TD>");
            String volume = oleLoanDocument.getEnumeration() != null && !oleLoanDocument.getEnumeration().equals("") ? oleLoanDocument.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = oleLoanDocument.getItemCopyNumber() != null && !oleLoanDocument.getItemCopyNumber().equals("") ? oleLoanDocument.getItemCopyNumber() : "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;
            stringBuffer.append(fieldLabelMap.get(OLEConstants.VOLUME_ISSUE_COPY)!=null ?fieldLabelMap.get(OLEConstants.VOLUME_ISSUE_COPY):OLEConstants.VOLUME_ISSUE_COPY+":</TD><TD>"  + (volumeNumber != null ? volumeNumber : "") + "</TD></TR><TR><TD>");
            stringBuffer.append(fieldLabelMap.get(OLEConstants.ITEM_WAS_DUE)!=null ?fieldLabelMap.get(OLEConstants.ITEM_WAS_DUE):OLEConstants.ITEM_WAS_DUE+":</TD><TD>" + (oleLoanDocument.getLoanDueDate() != null ? sdf.format(oleLoanDocument.getLoanDueDate()).toString() : "") + "</TD></TR><TR><TD>");
            stringBuffer.append(fieldLabelMap.get(OLEConstants.LIBRARY_SHELVING_LOCATION)!=null ?fieldLabelMap.get(OLEConstants.LIBRARY_SHELVING_LOCATION):OLEConstants.LIBRARY_SHELVING_LOCATION+":</TD><TD>" + (itemShelvingLocationName(oleLoanDocument.getItemLocation()) != null ? itemShelvingLocationName(oleLoanDocument.getItemLocation()) : "") + "</TD></TR><TR><TD>");
            try {
                String callNumber = "";
                if (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("")) {
                    callNumber = oleLoanDocument.getItemCallNumber();
                }
                stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_CALL_NUMBER)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_CALL_NUMBER):OLEConstants.NOTICE_CALL_NUMBER +":</TD><TD>"  + (callNumber != null ? callNumber : "") + "</TD></TR><TR><TD>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append(fieldLabelMap.get(OLEConstants.NOTICE_ITEM_BARCODE)!=null ?fieldLabelMap.get(OLEConstants.NOTICE_ITEM_BARCODE):OLEConstants.NOTICE_ITEM_BARCODE+":</TD><TD>" + (oleLoanDocument.getItemId() != null ? oleLoanDocument.getItemId() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            stringBuffer.append("</table>");
        } catch (Exception e) {
            LOG.error("Error---->While generating HTML overdue content  ");
            if (oleLoanDocument != null) {
                LOG.error("Error---->Item Barcode " + oleLoanDocument.getItemId());
            }
            LOG.error(e.getMessage() + e);
        }
        return stringBuffer.toString();
    }

    protected SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
    }

    private String itemShelvingLocationName(String code) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("locationCode", code);
        List<OleLocation> oleLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, criteria);

        return oleLocation.size() == 1 ? oleLocation.get(0).getLocationName() : "";
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
