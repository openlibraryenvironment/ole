package org.kuali.ole.deliver.batch;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/31/12
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverBatchServiceImpl {

    private LoanProcessor loanProcessor;
    private DocstoreClientLocator docstoreClientLocator;
    private OutputStream overdueOutPutStream=null;
    private Document overdueDocument=null;
    private OlePatronHelperServiceImpl olePatronHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }


    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    private static final Logger LOG = Logger.getLogger(OleDeliverBatchServiceImpl.class);

    /**
     * This method initiate LoanProcessor.
     *
     * @return LoanProcessor
     */
    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public OutputStream getOverdueOutPutStream() {
        return overdueOutPutStream;
    }

    public void setOverdueOutPutStream(OutputStream overdueOutPutStream) {
        this.overdueOutPutStream = overdueOutPutStream;
    }

    public Document getOverdueDocument() {
        if(this.overdueDocument==null){
            this.overdueDocument=new Document(PageSize.A4);
        }
        return overdueDocument;
    }

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperServiceImpl olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public void setOverdueDocument(Document overdueDocument) {
        this.overdueDocument = overdueDocument;
    }

    private void getHTMLHeader(StringBuffer stringBuffer, String title) throws Exception {
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + title + "</TITLE>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");
    }

    private void getTemplateHeader(OleNoticeBo noticeBo, StringBuffer stringBuffer) throws Exception {

        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>Patron Name :</TD><TD>" + noticeBo.getPatronName() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Address :</TD><TD>" + noticeBo.getPatronAddress() + "</TD></TR>");
        stringBuffer.append("<TR><TD>EMAIL :</TD><TD>" + noticeBo.getPatronEmailAddress() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Phone Number :</TD><TD>" + ( noticeBo.getPatronPhoneNumber()!=null ? noticeBo.getPatronPhoneNumber() : "") + "</TD></TR>");
        stringBuffer.append("</TABLE>");
    }

    private void getTemplateBody(StringBuffer stringBuffer, String title, String body) throws Exception {
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        stringBuffer.append("<TABLE width=\"100%\">");
        stringBuffer.append("<TR><TD><CENTER>" + title + "</CENTER></TD></TR>");
        stringBuffer.append("<TR><TD><p>" + body + "</p></TD></TR>");
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
    }

    public BusinessObjectService getBusinessObjectService() {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    private String itemShelvingLocationName(String code) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("locationCode", code);
        List<OleLocation> oleLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, criteria);

        return oleLocation.size() == 1 ? oleLocation.get(0).getLocationName() : "";
    }

    private void getTemplateFooter(OleNoticeBo noticeBo, StringBuffer stringBuffer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        stringBuffer.append("<table>");
        if (!(noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL) || noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.CANCELLATION_NOTICE))) {
            stringBuffer.append("<TR><TD>Circulation Location/Library Name :</TD><TD>" + (noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "") + "</TD></TR>");
        }
        stringBuffer.append("<TR><TD>Title :</TD><TD>" + (noticeBo.getTitle()!=null ? noticeBo.getTitle() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Author :</TD><TD>" + (noticeBo.getAuthor()!=null ? noticeBo.getAuthor() : "") + "</TD></TR>");
        if (!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.CANCELLATION_NOTICE)){
            stringBuffer.append("<TR><TD>Volume/Issue/Copy Number :</TD><TD>" + (noticeBo.getVolumeIssueCopyNumber()!=null ? noticeBo.getVolumeIssueCopyNumber() : "") + "</TD></TR>");
        }
        stringBuffer.append("<TR><TD>Library shelving location :</TD><TD>" + (itemShelvingLocationName(noticeBo.getItemShelvingLocation())!=null ? itemShelvingLocationName(noticeBo.getItemShelvingLocation()) : "" )+ "</TD></TR>");

        stringBuffer.append("<TR><TD>Call Number :</TD><TD>" +(noticeBo.getItemCallNumber()!=null ? noticeBo.getItemCallNumber() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Item Barcode :</TD><TD>" +(noticeBo.getItemId()!=null ? noticeBo.getItemId() : "") + "</TD></TR>");
        if (noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
            stringBuffer.append("<TR><TD>Item was due :</TD><TD>" +( noticeBo.getDueDate()!=null ? sdf.format(noticeBo.getDueDate()).toString() : "" )+ "</TD></TR>");
        }
        if (noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {
            stringBuffer.append("<TR><TD>Original due date:</TD><TD>" + (noticeBo.getOriginalDueDate()!=null ? sdf.format(noticeBo.getOriginalDueDate()) : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>New due date:</TD><TD>" + (noticeBo.getNewDueDate()!=null ? sdf.format(noticeBo.getNewDueDate()) : "") + "</TD></TR>");
        }
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        stringBuffer.append("</table>");
    }

    private void getTemplateFooterList(List<OleNoticeBo> noticeBoList, StringBuffer stringBuffer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int courtesy = 0; courtesy < noticeBoList.size(); courtesy++) {
            stringBuffer.append("<table>");
            if (courtesy == 0) {
                stringBuffer.append("<TR><TD>Title/item information</TD></TR>");
                stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            }
            stringBuffer.append("<TR><TD>Title :</TD><TD>" + (noticeBoList.get(courtesy).getTitle() != null ? noticeBoList.get(courtesy).getTitle() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Author :</TD><TD>" + (noticeBoList.get(courtesy).getAuthor() != null ? noticeBoList.get(courtesy).getAuthor() : "") + "</TD></TR>");
            if (!noticeBoList.get(courtesy).getNoticeName().equalsIgnoreCase(OLEConstants.CANCELLATION_NOTICE)) {
                stringBuffer.append("<TR><TD>Volume/Issue/Copy Number :</TD><TD>" + (noticeBoList.get(courtesy).getVolumeIssueCopyNumber() != null ? noticeBoList.get(courtesy).getVolumeIssueCopyNumber() : "") + "</TD></TR>");
            }
            if (noticeBoList.get(courtesy).getNoticeName().equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                stringBuffer.append("<TR><TD>Item was due :</TD><TD>" + (noticeBoList.get(courtesy).getDueDate() != null ? sdf.format(noticeBoList.get(courtesy).getDueDate()).toString() : "") + "</TD></TR>");
            }
            stringBuffer.append("<TR><TD>Library shelving location :</TD><TD>" + (itemShelvingLocationName(noticeBoList.get(courtesy).getItemShelvingLocation()) != null ? itemShelvingLocationName(noticeBoList.get(courtesy).getItemShelvingLocation()) : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Call Number :</TD><TD>" + (noticeBoList.get(courtesy).getItemCallNumber() != null ? noticeBoList.get(courtesy).getItemCallNumber() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>Item Barcode :</TD><TD>" + (noticeBoList.get(courtesy).getItemId() != null ? noticeBoList.get(courtesy).getItemId() : "") + "</TD></TR>");
            stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
            stringBuffer.append("</table>");
        }
    }


    private void getPickUpLocationTemplate(OleNoticeBo noticeBo, StringBuffer stringBuffer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        stringBuffer.append("<table>");
        stringBuffer.append("<TR><TD>Pick up location :</TD><TD></TD></TR>");
        if (!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {
            stringBuffer.append("<TR><TD>Circulation Location/Library Name :</TD><TD>" + (noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "") + "</TD></TR>");
        }
        stringBuffer.append("<TR><TD>Address :</TD><TD>" + (noticeBo.getCirculationDeskAddress()!=null ? noticeBo.getCirculationDeskAddress() : "" )+ "</TD></TR>");
        stringBuffer.append("<TR><TD>Email :</TD><TD>" + (noticeBo.getCirculationDeskEmailAddress()!=null ? noticeBo.getCirculationDeskEmailAddress() : "" )+ "</TD></TR>");
        stringBuffer.append("<TR><TD>Phone Number :</TD><TD>" +( noticeBo.getCirculationDeskPhoneNumber()!=null ? noticeBo.getCirculationDeskPhoneNumber() : "") + "</TD></TR>");
        if (noticeBo.getNoticeName() != null && noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_ONHOLD)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
            if(noticeBo.getExpiredOnHoldDate() != null){
                String dateAfterFormat=dateFormat.format(noticeBo.getExpiredOnHoldDate());
                stringBuffer.append("<TR><TD>Item will be held until</TD><TD>" + dateAfterFormat + "</TD></TR>");
            }

        } else {
            stringBuffer.append("<TR><TD>Item will be held until</TD><TD>" +( noticeBo.getDueDate()!=null ?sdf.format( noticeBo.getDueDate()).toString() : "") + "</TD></TR>");
        }
        stringBuffer.append("</table>");
    }

    private void getHTMLFooter(StringBuffer stringBuffer) throws Exception {
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
       String myAccountURL = loanProcessor.getParameter(OLEConstants.MY_ACCOUNT_URL);
        if(myAccountURL!=null && !myAccountURL.trim().isEmpty()){
        stringBuffer.append("<TABLE width=\"100%\"><TR><TD><CENTER><a href = "+myAccountURL+">"+"My Account"+"</a></CENTER></TD></TR></TABLE>");
        }
        stringBuffer.append("</TABLE></BODY></HTML>");
        if (LOG.isDebugEnabled()){
            LOG.debug("MAIL HTML CONTENT : "+stringBuffer.toString());
        }
    }

    private Map getSMSTemplate(List<OleNoticeBo> noticeBo) throws Exception {
        List<OleNoticeBo> overDueNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> onHoldNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> recallNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> expiredNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> expiredRequiredNoticeList = new ArrayList<OleNoticeBo>();
        Map smsMap = new HashMap();
        for (int temp = 0; temp < noticeBo.size(); temp++) {
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                overDueNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_ONHOLD)) {
                onHoldNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OleDeliverRequest.RECALL)) {
                recallNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
                expiredNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST)) {
                expiredRequiredNoticeList.add(noticeBo.get(temp));
            }
        }
        Map overdue = new HashMap();
        if (overDueNoticeList != null && overDueNoticeList.size() > 0) {
            overdue = getOverDueList(overdue, overDueNoticeList);
        }
        Map hod = new HashMap();
        if (onHoldNoticeList != null && onHoldNoticeList.size() > 0) {
            hod = getHoldList(hod, onHoldNoticeList);
        }
        Map recl = new HashMap();
        if (recallNoticeList != null && recallNoticeList.size() > 0) {
            recl = getRecallList(recl, recallNoticeList);
        }
        Map exp = new HashMap();
        if (expiredNoticeList != null && expiredNoticeList.size() > 0) {
            exp = getExpiredList(exp, expiredNoticeList);
        }
        Map expReq = new HashMap();
        if (expiredRequiredNoticeList != null && expiredRequiredNoticeList.size() > 0) {
            expReq = getExpiredRequiredList(expReq, expiredRequiredNoticeList);
        }

        smsMap.put("OVERDUE", overdue);
        smsMap.put("HOLD", hod);
        smsMap.put("RECALL", recl);
        smsMap.put("EXPIRED", exp);
        smsMap.put("EXPIREDREQ", expReq);
        return smsMap;
    }

    private Map getExpiredRequiredList(Map expReq, List expiredRequiredNoticeList) throws Exception {
        String smsExpiredReq = "";
        LoanProcessor loanProcessor = getLoanProcessor();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int exReq = 0; exReq < expiredRequiredNoticeList.size(); exReq++) {
            OleNoticeBo oleNoticeBo = (OleNoticeBo) expiredRequiredNoticeList.get(exReq);
            smsExpiredReq = loanProcessor.getParameter("EXP_REQ_TITLE") + "    " + "Circulation Location/Library Name :" + oleNoticeBo.getCirculationDeskName() + ",Address :" + oleNoticeBo.getCirculationDeskAddress() + ",Title :" + oleNoticeBo.getTitle() + ",Call Number :" + oleNoticeBo.getItemCallNumber() + ",Item was due :" + sdf.format(oleNoticeBo.getDueDate()).toString();
            expReq.put(exReq, smsExpiredReq);
        }
        return expReq;
    }

    private Map getExpiredList(Map expire, List expiredNoticeList) throws Exception {
        String smsExpire = "";
        LoanProcessor loanProcessor = getLoanProcessor();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int exp = 0; exp < expiredNoticeList.size(); exp++) {
            OleNoticeBo oleNoticeBo = (OleNoticeBo) expiredNoticeList.get(exp);
            smsExpire = loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE) + "    " + "Circulation Location/Library Name :" + oleNoticeBo.getCirculationDeskName() + ",Address :" + oleNoticeBo.getCirculationDeskAddress() + ",Title :" + oleNoticeBo.getTitle() + ",Call Number :" + oleNoticeBo.getItemCallNumber() + ",Item was due :" + sdf.format(oleNoticeBo.getDueDate()).toString();
            expire.put(exp, smsExpire);
        }
        return expire;
    }

    private Map getRecallList(Map rcall, List recallNoticeList) throws Exception {
        String smsRecall = "";
        LoanProcessor loanProcessor = getLoanProcessor();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int recall = 0; recall < recallNoticeList.size(); recall++) {
            OleNoticeBo oleNoticeBo = (OleNoticeBo) recallNoticeList.get(recall);
            smsRecall = loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE) + "    " + "Circulation Location/Library Name :" + oleNoticeBo.getCirculationDeskName() + ",Address :" + oleNoticeBo.getCirculationDeskAddress() + ",Title :" + oleNoticeBo.getTitle() + ",Call Number :" + oleNoticeBo.getItemCallNumber() + ",Original due date :" + sdf.format(oleNoticeBo.getDueDate()).toString() + ",New due date: " + sdf.format(oleNoticeBo.getNewDueDate());
            rcall.put(recall, smsRecall);
        }
        return rcall;
    }

    private Map getHoldList(Map hod, List onHoldNoticeList) throws Exception {
        String smsHod = "";
        LoanProcessor loanProcessor = getLoanProcessor();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int hold = 0; hold < onHoldNoticeList.size(); hold++) {
            OleNoticeBo oleNoticeBo = (OleNoticeBo) onHoldNoticeList.get(hold);
            smsHod = loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE) + "    " + "Circulation Location/Library Name :" + oleNoticeBo.getCirculationDeskName() + ",Address :" + oleNoticeBo.getCirculationDeskAddress() + ",Title :" + oleNoticeBo.getTitle() + ",Call Number :" + oleNoticeBo.getItemCallNumber() + ",Item will be held until :" + sdf.format(oleNoticeBo.getDueDate()).toString() + ",Pick up location:" + oleNoticeBo.getCirculationDeskName() + ",Pick up location Address :" + oleNoticeBo.getCirculationDeskAddress();
            hod.put(hold, smsHod);
        }
        return hod;
    }


    private Map getOverDueList(Map overdue, List overDueNoticeList) throws Exception {
        String smsDue = "";
        LoanProcessor loanProcessor = getLoanProcessor();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        for (int due = 0; due < overDueNoticeList.size(); due++) {
            OleNoticeBo oleNoticeBo = (OleNoticeBo) overDueNoticeList.get(due);
            smsDue = loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE) + "    " + "Circulation Location/Library Name :" + oleNoticeBo.getCirculationDeskName() + ",Address :" + oleNoticeBo.getCirculationDeskAddress() + ",Title :" + oleNoticeBo.getTitle() + ",Call Number :" + oleNoticeBo.getItemCallNumber() + ",Item was due :" + sdf.format(oleNoticeBo.getDueDate()).toString();
            overdue.put(due, smsDue);
        }

        return overdue;
    }

    private List getTemplate(List<OleNoticeBo> noticeBo) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();

        List templateForNoticeList = new ArrayList();
        List<OleNoticeBo> overDueNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> onHoldNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> recallNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> expiredNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> expiredRequiredNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> courtesyNoticeList = new ArrayList<OleNoticeBo>();
        List<OleNoticeBo> cancellationNoticeList = new ArrayList<OleNoticeBo>();

        for (int temp = 0; temp < noticeBo.size(); temp++) {
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                overDueNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_ONHOLD)) {
                onHoldNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase("RecallNotice")) {
                recallNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
                expiredNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST)) {
                expiredRequiredNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.COURTESY_NOTICE)) {
                courtesyNoticeList.add(noticeBo.get(temp));
            }
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.CANCELLATION_NOTICE)) {
                cancellationNoticeList.add(noticeBo.get(temp));
            }
        }

        LoanProcessor loanProcessor = getLoanProcessor();

        if (overDueNoticeList != null && overDueNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE));
            getTemplateHeader(overDueNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT));
            getTemplateFooterList(overDueNoticeList, stringBuffer);
            getHTMLFooter(stringBuffer);
        }

        if (onHoldNoticeList != null && onHoldNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE));
            getTemplateHeader(onHoldNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE), loanProcessor.getParameter(OLEParameterConstants.ONHOLD_BODY));
            for (int onHold = 0; onHold < onHoldNoticeList.size(); onHold++) {
                getTemplateFooter(noticeBo.get(onHold), stringBuffer);
                getPickUpLocationTemplate(noticeBo.get(onHold), stringBuffer);
            }
            getHTMLFooter(stringBuffer);
        }

        if (recallNoticeList != null && recallNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE));
            getTemplateHeader(recallNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE), loanProcessor.getParameter(OLEParameterConstants.RECALL_BODY));
            for (int recall = 0; recall < recallNoticeList.size(); recall++) {
                getTemplateFooter(noticeBo.get(recall), stringBuffer);
            }
            getHTMLFooter(stringBuffer);
        }

        if (expiredNoticeList != null && expiredNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE));
            getTemplateHeader(expiredNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT));

            for (int exp = 0; exp < expiredNoticeList.size(); exp++) {
                getTemplateFooter(noticeBo.get(exp), stringBuffer);
            }
            getHTMLFooter(stringBuffer);
        }

        if (expiredRequiredNoticeList != null && expiredRequiredNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE));
            getTemplateHeader(expiredRequiredNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE), loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_BODY));
            for (int exReq = 0; exReq < expiredRequiredNoticeList.size(); exReq++) {
                getTemplateFooter(noticeBo.get(exReq), stringBuffer);
            }
            getHTMLFooter(stringBuffer);
        }
        if (courtesyNoticeList != null && courtesyNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE));
            getTemplateHeader(courtesyNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT));
            getTemplateFooterList(courtesyNoticeList, stringBuffer);
            getHTMLFooter(stringBuffer);
        }
        if (cancellationNoticeList != null && cancellationNoticeList.size() > 0) {
            getHTMLHeader(stringBuffer, loanProcessor.getParameter("CANCELLATION_NOTICE"));
            getTemplateHeader(cancellationNoticeList.get(0), stringBuffer);
            getTemplateBody(stringBuffer, loanProcessor.getParameter("CANCELLATION_NOTICE"), loanProcessor.getParameter(OLEParameterConstants.CANCELLATION_BODY));
            for (int exReq = 0; exReq < cancellationNoticeList.size(); exReq++) {
                getTemplateFooter(noticeBo.get(exReq), stringBuffer);
            }
            getHTMLFooter(stringBuffer);
        }
        templateForNoticeList.add(stringBuffer);

        return templateForNoticeList;
    }


    public List getPdfNoticeForPatron(List<OleNoticeBo> noticeBoList) throws Exception {
        return getPdfTemplate(noticeBoList);
    }

    private List getPdfTemplate(List<OleNoticeBo> noticeBoList) throws Exception {
        List templateForNoticeList = new ArrayList();
        LOG.debug("In getPdfTemplate()");
        try {
            Document document = getOverdueDocument();
            OutputStream outputStream = null;

            List<OleNoticeBo> overDueNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> onHoldNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> recallNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> expiredNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> expiredRequiredNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> courtesyNoticeList = new ArrayList<OleNoticeBo>();

            for (int temp = 0; temp < noticeBoList.size(); temp++) {
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                    overDueNoticeList.add(noticeBoList.get(temp));
                }
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_ONHOLD)) {
                    onHoldNoticeList.add(noticeBoList.get(temp));
                }
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {
                    recallNoticeList.add(noticeBoList.get(temp));
                }
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
                    expiredNoticeList.add(noticeBoList.get(temp));
                }
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST)) {
                    expiredRequiredNoticeList.add(noticeBoList.get(temp));
                }
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.COURTESY_NOTICE)) {
                    courtesyNoticeList.add(noticeBoList.get(temp));
                }
            }
            LoanProcessor loanProcessor = getLoanProcessor();

            if (overDueNoticeList != null && overDueNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), overDueNoticeList.get(0).getItemId());
                getPdfTemplateHeader(overDueNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT));
                getPdfTemplateFooterList(overDueNoticeList, document);
                getPdfFooter(document, outputStream);
            }

            if (onHoldNoticeList != null && onHoldNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE), onHoldNoticeList.get(0).getItemId());
                getPdfTemplateHeader(onHoldNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE), loanProcessor.getParameter(OLEParameterConstants.ONHOLD_BODY));
                for (int onHold = 0; onHold < onHoldNoticeList.size(); onHold++) {
                    getPdfTemplateFooter(noticeBoList.get(onHold), document);
                    //getPickUpLocationTemplate(noticeBoList.get(onHold),stringBuffer);
                }
                getPdfFooter(document, outputStream);
            }

            if (recallNoticeList != null && recallNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE), recallNoticeList.get(0).getItemId());
                getPdfTemplateHeader(recallNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE), loanProcessor.getParameter(OLEParameterConstants.RECALL_BODY));
                for (int recall = 0; recall < recallNoticeList.size(); recall++) {
                    getPdfTemplateFooter(noticeBoList.get(recall), document);
                }
                getPdfFooter(document, outputStream);
            }

            if (expiredNoticeList != null && expiredNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE), expiredNoticeList.get(0).getItemId());
                getPdfTemplateHeader(expiredNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT));
                for (int exp = 0; exp < expiredNoticeList.size(); exp++) {
                    getPdfTemplateFooter(noticeBoList.get(exp), document);
                }
                getPdfFooter(document, outputStream);
            }

            if (expiredRequiredNoticeList != null && expiredRequiredNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE), expiredRequiredNoticeList.get(0).getItemId());
                getPdfTemplateHeader(expiredRequiredNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE), loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_BODY));
                for (int exReq = 0; exReq < expiredRequiredNoticeList.size(); exReq++) {
                    getPdfTemplateFooter(noticeBoList.get(exReq), document);
                }
                getPdfFooter(document, outputStream);
            }
            if (courtesyNoticeList != null && courtesyNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE), courtesyNoticeList.get(0).getItemId());
                getPdfTemplateHeader(courtesyNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE), loanProcessor.getParameter(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT));
                getPdfTemplateFooterList(courtesyNoticeList, document);
                getPdfFooter(document, outputStream);
            }

        } catch (Exception e) {
            LOG.error("Exception in getPdfTemplate", e);
        }
        //     templateForNoticeList.add(stringBuffer);

        return templateForNoticeList;
    }


    public List getNoticeForPatron(List<OleNoticeBo> noticeBo) throws Exception {
        return getTemplate(noticeBo);

    }

    public Map getSMSForPatron(List<OleNoticeBo> noticeBo) throws Exception {
        return getSMSTemplate(noticeBo);
    }


    public String sendMissingNotice(OleNoticeBo oleNoticeBo) {

        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + oleNoticeBo.getNoticeName() + "</TITLE>");
        stringBuffer.append("<HEAD><TR><TD><CENTER>" + oleNoticeBo.getNoticeName() + "</CENTER></TD></TR></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>Circulation Location / Library Name :</TD><TD>" + oleNoticeBo.getCirculationDeskName() + "</TD></TR>");
        stringBuffer.append("<TR><TD>PatronName :</TD><TD>" + oleNoticeBo.getPatronName() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Address :</TD><TD>" + oleNoticeBo.getPatronAddress() + "</TD></TR>");
        stringBuffer.append("<TR><TD>EMAIL :</TD><TD>" + oleNoticeBo.getPatronEmailAddress() + "</TD></TR>");
        stringBuffer.append("<TR><TD>PhoneNumber :</TD><TD>" + oleNoticeBo.getPatronPhoneNumber() + "</TD></TR>");
        stringBuffer.append("</TABLE>");
        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        stringBuffer.append("<TABLE width=\"100%\">");
        stringBuffer.append("<TR><TD><CENTER>" + oleNoticeBo.getNoticeName() + "</CENTER></TD></TR>");
        stringBuffer.append("<TR><TD><p>" + oleNoticeBo.getNoticeSpecificContent() + "</p></TD></TR>");
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>Title :</TD><TD>" + oleNoticeBo.getTitle() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Author :</TD><TD>" + oleNoticeBo.getAuthor() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Volume/Issue/Copy # :</TD><TD>" + oleNoticeBo.getVolumeNumber() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Library shelvinglocation  :</TD><TD>" + oleNoticeBo.getItemShelvingLocation() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Call #:</TD><TD>" + oleNoticeBo.getItemCallNumber() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Item barcode  :</TD><TD>" + oleNoticeBo.getItemId() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Check In Date  :</TD><TD>" + (oleNoticeBo.getCheckInDate()!=null ? oleNoticeBo.getCheckInDate() : "") + "</TD></TR>");
        String missingPieceNote="";
        if(oleNoticeBo.getOleItem()!=null&& oleNoticeBo.getOleItem().getMissingPieceFlagNote()!=null){
            missingPieceNote=oleNoticeBo.getOleItem().getMissingPieceFlagNote();
        }
        stringBuffer.append("<TR><TD>Missing Piece Note  :</TD><TD>" + missingPieceNote + "</TD></TR>");
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</BODY>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();
    }

    public String getEmailPickUpNotice(OleNoticeBo noticeBo) throws Exception {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<TABLE>");
        stringBuffer.append("<TR><TD>");
        stringBuffer.append("<TABLE>");
        stringBuffer.append("<TR><TD COLSPAN='3'>" + ((noticeBo.getPatronName() != null) ? noticeBo.getPatronName().toString() : "") + " </TD></TR>");
        stringBuffer.append("<TR><TD COLSPAN='3'>" + ((noticeBo.getPatronAddress() != null) ? noticeBo.getPatronAddress().toString() : "") + " </TD></TR>");
        stringBuffer.append("<TR><TD COLSPAN='3'>" + ((noticeBo.getPatronEmailAddress() != null) ? noticeBo.getPatronEmailAddress().toString() : "") + " </TD></TR>");
        stringBuffer.append("<TR><TD COLSPAN='3'>" + ((noticeBo.getPatronPhoneNumber() != null) ? noticeBo.getPatronPhoneNumber().toString() : "") + " </TD></TR>");
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</TD></TR>");

        stringBuffer.append("<TR><TD align='center'>");
        stringBuffer.append("<B>"+noticeBo.getNoticeName()+"</B>");
        stringBuffer.append("</TD></TR>");

        stringBuffer.append("<TR><TD align='left'>");
        stringBuffer.append("<P>"+noticeBo.getNoticeSpecificContent()+"</P>");
        stringBuffer.append("</TD></TR>");

        stringBuffer.append("<TR><TD align='left'>");
        stringBuffer.append("<P>Thank you .</P>");
        stringBuffer.append("</TD></TR>");

        stringBuffer.append("<TR><TD>");
        String author = ((noticeBo.getAuthor() != null) ? noticeBo.getAuthor().toString() : "");
        String title = ((noticeBo.getTitle() != null) ? noticeBo.getTitle().toString() : "");
        String callNumber = ((noticeBo.getItemCallNumber() != null) ? noticeBo.getItemCallNumber().toString() : "");
        stringBuffer.append("<TR><TD>");
        stringBuffer.append("<TABLE>");
        stringBuffer.append("<TR><TD COLSPAN='3'> Title : " + title + " </TD></TR>");
        stringBuffer.append("<TR><TD COLSPAN='3'> Author : " + author + " </TD></TR>");
        stringBuffer.append("<TR><TD COLSPAN='3'> Call Number :" + callNumber + " </TD></TR>");
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</TD></TR>");
        stringBuffer.append("</TABLE>");
        return stringBuffer.toString();
    }

    public String generateMailContentFromPatronBill(OleLoanDocument oleLoanDocument,OlePatronDocument olePatronDocument,String feeTypeName,String fineAmount,PatronBillPayment patronBillPayment ){
        StringBuffer contentForSendMail=new StringBuffer();
        String patronMail="";
        String patronAddress="";
        String patronPhoneNumber="";
        try{
            patronMail=getOlePatronHelperService().getPatronHomeEmailId(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
            patronAddress=getOlePatronHelperService().getPatronPreferredAddress(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
            patronPhoneNumber=getOlePatronHelperService().getPatronHomePhoneNumber(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
        }catch(Exception e){
            LOG.error("Exception while generating mail content from patron bill",e);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        contentForSendMail.append("<HTML>");
        contentForSendMail.append("<TITLE><CENTER><h2>"+ feeTypeName + " Bill </h2></CENTER></TITLE>");
        contentForSendMail.append("<HEAD><TR><TD><CENTER><h2>" + feeTypeName + "<h2></CENTER></TD></TR></HEAD>");
        contentForSendMail.append("<BODY>");
        contentForSendMail.append("<TABLE width=\"100%\">");
        contentForSendMail.append("<TR><TD><h3>Patron Details</h3></TD></TR>");
        contentForSendMail.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");;
        contentForSendMail.append("<TABLE></BR></BR>");
        contentForSendMail.append("<TR><TD>PatronName</TD><TD>:</TD><TD>" + olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName() + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Address</TD><TD>:</TD><TD>" + patronAddress + "</TD></TR>");
        contentForSendMail.append("<TR><TD>EMAIL</TD><TD>:</TD><TD>" + patronMail + "</TD></TR>");
        contentForSendMail.append("<TR><TD>PhoneNumber</TD><TD>:</TD><TD>" + ( patronPhoneNumber!=null ? patronPhoneNumber : "") + "</TD></TR>");
        contentForSendMail.append("</TABLE>");
        contentForSendMail.append("<TABLE></BR></BR>");
        contentForSendMail.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        contentForSendMail.append("<TABLE width=\"100%\">");
        contentForSendMail.append("<TR><TD><h3>Fine Details</h3></TD></TR>");
        contentForSendMail.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
        contentForSendMail.append("<TABLE></BR></BR>");
        contentForSendMail.append("<TR><TD>Bill Number</TD><TD>:</TD><TD>" + patronBillPayment.getBillNumber() + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Fee Type</TD><TD>:</TD><TD>" + feeTypeName + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Fee Amount</TD><TD>:</TD><TD>" + CurrencyFormatter.getSymbolForCurrencyPattern() + fineAmount + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Item Barcode</TD><TD>:</TD><TD>" + oleLoanDocument.getItemId() + "</TD></TR>");

        String issue = new String(" ");
        contentForSendMail.append("<TR><TD>Title</TD><TD>:</TD><TD>" + (oleLoanDocument.getTitle()!=null ? oleLoanDocument.getTitle() : "") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Author</TD><TD>:</TD><TD>" + (oleLoanDocument.getAuthor()!=null ? oleLoanDocument.getAuthor() : "") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Volume/Issue/Copy # </TD><TD>:</TD><TD>" + (oleLoanDocument.getItemVolumeNumber()!=null? oleLoanDocument.getItemVolumeNumber():"") + "/" + issue + "/" +  (oleLoanDocument.getItemCopyNumber()!=null? oleLoanDocument.getItemCopyNumber():"")+ "</TD></TR>");
        contentForSendMail.append("<TR><TD>Library Shelving Location</TD><TD>:</TD><TD>" + oleLoanDocument.getLocation() + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Call Number</TD><TD>:</TD><TD>" + (oleLoanDocument.getItemCallNumber()!=null? oleLoanDocument.getItemCallNumber():"") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Item was Due</TD><TD>:</TD><TD>" + ((CollectionUtils.isNotEmpty(patronBillPayment.getFeeType()) && patronBillPayment.getFeeType().get(0).getDueDate()!=null) ? sdf.format(patronBillPayment.getFeeType().get(0).getDueDate()) : "") + "</TD></TR>");
        contentForSendMail.append("</TABLE>");
        contentForSendMail.append("</BODY>");
        contentForSendMail.append("</HTML>");
        return contentForSendMail.toString();
    }



    public  String getOverdueNoticeHTMLContent(OleLoanDocument  oleLoanDocument){
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        StringBuffer stringBuffer = new StringBuffer();
        try {
        stringBuffer.append("<table>");
        stringBuffer.append("<TR><TD>Title :</TD><TD>" + (oleLoanDocument.getTitle() != null ? oleLoanDocument.getTitle() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Author :</TD><TD>" + (oleLoanDocument.getAuthor() != null ? oleLoanDocument.getAuthor() : "") + "</TD></TR>");
        String volume = (String) oleLoanDocument.getEnumeration()!= null && !oleLoanDocument.getEnumeration().equals("") ? oleLoanDocument.getEnumeration() : "";
        String issue = new String(" ");
        String copyNumber = (String) oleLoanDocument.getItemCopyNumber() != null && !oleLoanDocument.getItemCopyNumber().equals("") ? oleLoanDocument.getItemCopyNumber() : "";
        String volumeNumber=volume + "/" + issue + "/" + copyNumber;
        stringBuffer.append("<TR><TD>Volume/Issue/Copy Number :</TD><TD>" + (volumeNumber != null ? volumeNumber : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Item was due :</TD><TD>" + (oleLoanDocument.getLoanDueDate() != null ? sdf.format(oleLoanDocument.getLoanDueDate()).toString() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Library shelving location :</TD><TD>" + (itemShelvingLocationName(oleLoanDocument.getItemLocation()) != null ? itemShelvingLocationName(oleLoanDocument.getItemLocation()) : "") + "</TD></TR>");
        try {
            String callNumber = "";
            if (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("")) {
                callNumber = oleLoanDocument.getItemCallNumber();
            }
            stringBuffer.append("<TR><TD>Call Number :</TD><TD>" + (callNumber != null ? callNumber : "") + "</TD></TR>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuffer.append("<TR><TD>Item Barcode :</TD><TD>" + (oleLoanDocument.getItemId() != null ? oleLoanDocument.getItemId() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        stringBuffer.append("</table>");
        }catch (Exception e){
            LOG.error("Error---->While generating verdue content for email pdf  ");
            if(oleLoanDocument!=null){
                LOG.error("Error---->Item Barcode "+oleLoanDocument.getItemId());
            }
            LOG.error(e.getMessage()+e);
        }
        return stringBuffer.toString();
    }
    public String getHeaderAndPatronContent(OlePatronDocument olePatronDocument,boolean isOverdue){

        StringBuffer stringBuffer = new StringBuffer();
        try{
        String title = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_TITLE);
        String body = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT);
        if(!isOverdue){
            title=loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE);
            body=loanProcessor.getParameter(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT);
        }
        OleDeliverRequestDocumentHelperServiceImpl deliverService = new OleDeliverRequestDocumentHelperServiceImpl();
        EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);

        stringBuffer.append("<HTML>");
        stringBuffer.append("<TITLE>" + title + "</TITLE>");
        stringBuffer.append("<HEAD></HEAD>");
        stringBuffer.append("<BODY>");

        try{
        stringBuffer.append("<TABLE></BR></BR>");
        stringBuffer.append("<TR><TD>Patron Name :</TD><TD>" + olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName() + "</TD></TR>");
        stringBuffer.append("<TR><TD>Address :</TD><TD>" + (getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>EMAIL :</TD><TD>" + (getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "") + "</TD></TR>");

        stringBuffer.append("<TR><TD>Phone Number :</TD><TD>" +(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "") + "</TD></TR>");
        } catch (Exception e){
            e.printStackTrace();
        }
        stringBuffer.append("</TABLE>");

        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
        stringBuffer.append("<TABLE width=\"100%\">");
        stringBuffer.append("<TR><TD><CENTER>" + title + "</CENTER></TD></TR>");
        stringBuffer.append("<TR><TD><p>" + body + "</p></TD></TR>");
        stringBuffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
        }catch (Exception e){
            LOG.error("Error---->While generating overdue content for email(Patron Information) ");
        }
        return stringBuffer.toString();
    }


    /* This section is related  to generation of pdf notices */

    public Font getDefaultFont(){
        com.itextpdf.text.Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/ARIALUNI.TTF"), BaseFont.IDENTITY_H,10);
        return font;
    }

    public Font getFont(String data) {
        String myData = new String(data);
        List<Character.UnicodeBlock> unicodeBlocks = new ArrayList<>();
        if (StringUtils.isNotBlank(myData)) {
            unicodeBlocks = getListOfLanguage(myData);
        }
        if (unicodeBlocks.contains(Character.UnicodeBlock.ARABIC)) {
            com.itextpdf.text.Font font = FontFactory.getFont(getFontFilePath("org/kuali/ole/deliver/batch/fonts/arial.ttf"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            return font;
        } else {
            return getDefaultFont();
        }
    }

    public Font getBoldFont(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD);
    }

    private PdfPCell getPdfPCellInJustified(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getFont(chunk))));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private List<Character.UnicodeBlock> getListOfLanguage(String data){
        Set<Character.UnicodeBlock> languageSet=new HashSet<Character.UnicodeBlock>();
        char[] valueArray = data.toCharArray();
        for(int counter=0;counter<valueArray.length;counter++){
            languageSet.add(Character.UnicodeBlock.of(valueArray[counter]));
        }
        return (new ArrayList<Character.UnicodeBlock>(languageSet));
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInCenter(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk, getDefaultFont())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInCenter(String chunk,Font font) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk,font)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellNewLine() {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        return pdfPCell;
    }

    private Paragraph getPdfParagraphNewLine() {
        Paragraph paragraph = new Paragraph(new Paragraph(Chunk.NEWLINE));
        return paragraph;
    }

    public String getFontFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            LOG.error("Error : while accessing font file "+e);
        }
        return null;
    }

    private OutputStream getPdfHeader(Document document, OutputStream outputStream, String title, String itemId) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        String pdfLocationSystemParam = getLoanProcessor().getParameter(OLEParameterConstants.PDF_LOCATION);
        if (LOG.isDebugEnabled()) {
            LOG.debug("System Parameter for PDF_Location --> " + pdfLocationSystemParam);
        }
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
            if (LOG.isDebugEnabled()) {
                LOG.debug("System Parameter for PDF_Location staging dir--> " + pdfLocationSystemParam);
            }
        } else {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory") + "/" + pdfLocationSystemParam + "/";
        }
        boolean locationExist = new File(pdfLocationSystemParam).exists();
        boolean fileCreated = false;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Is directory Exist ::" + locationExist);
        }
        try {
            if (!locationExist) {
                fileCreated = new File(pdfLocationSystemParam).mkdirs();
                if (!fileCreated) {
                    throw new RuntimeException("Not Able to create directory :" + pdfLocationSystemParam);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occured while creating the directory : " + e.getMessage(), e);
            throw e;
        }

        if (title == null || title.trim().isEmpty()) {
            title = OLEConstants.ITEM_TITLE;
        }
        title = title.replaceAll(" ", "_");

        if (itemId == null || itemId.trim().isEmpty()) {
            itemId = OLEConstants.ITEM_ID;
        }
        String fileName = pdfLocationSystemParam + title + "_" + itemId + "_" + (new Date(System.currentTimeMillis())).toString().replaceAll(":","_") + ".pdf";
        fileName = fileName.replace(" ","_");
        if (LOG.isDebugEnabled()) {
            LOG.debug("File Created :" + title + "file name ::" + fileName + "::");
        }
        outputStream = new FileOutputStream(fileName);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        document.newPage();
        Paragraph paraGraph = new Paragraph();
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(new Chunk(title, getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        return outputStream;
    }

    public void createPdfForIntransitRequest(OleDeliverRequestBo oleDeliverRequestBo, HttpServletResponse response) {
        LOG.debug("Creating pdf for intransit request");
        String title = "InTransit Request";
        OutputStream outputStream = null;
        String fileName = "IntransitRequestSlip" + "_" + oleDeliverRequestBo.getItemId() + "_" + new Date(System.currentTimeMillis()) + ".pdf";
        if (LOG.isDebugEnabled()) {
            LOG.debug("File Created :" + title + "file name ::" + fileName + "::");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        try {
            String itemTitle = oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "";
            Date dateOfReq = oleDeliverRequestBo.getCreateDate()!=null?oleDeliverRequestBo.getCreateDate():null;
            //String dateOfRequest = dateOfReq != null ? dateOfReq.toString() : "";
            String circulationLocation = oleDeliverRequestBo.getCirculationLocationCode() != null ? oleDeliverRequestBo.getCirculationLocationCode() : "";
            String itemBarcode = oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "";
            String patronBarcode = oleDeliverRequestBo.getBorrowerBarcode() != null ? oleDeliverRequestBo.getBorrowerBarcode() : "";
            String requestType = oleDeliverRequestBo.getRequestTypeId() != null ? oleDeliverRequestBo.getRequestTypeId() : "";
            boolean inTransitSlip = requestType.equalsIgnoreCase("8");
            // Document document = this.getDocument(0, 0, 5, 5);
            Document document = getOverdueDocument();
            //outputStream = new FileOutputStream(fileName);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.newPage();
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});
            Paragraph paraGraph = new Paragraph();
            if (inTransitSlip) {
                paraGraph.setAlignment(Element.ALIGN_CENTER);
                paraGraph.add(new Chunk("InTransit Request Slip", getBoldFont()));
            }
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            if (inTransitSlip) {
                pdfTable.addCell(getPdfPCellInJustified("Date/Time "));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(sdf.format(dateOfReq)));

                pdfTable.addCell(getPdfPCellInJustified("Circulation Location"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(circulationLocation));

                pdfTable.addCell(getPdfPCellInJustified("Item Barcode"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(itemBarcode));

                pdfTable.addCell(getPdfPCellInJustified("Title"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(itemTitle));

                pdfTable.addCell(getPdfPCellInJustified("Patron Barcode"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified(patronBarcode));
                document.add(pdfTable);
            }
            document.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOG.error("Exception while creating Pdf For IntransitRequest", e);
        }
    }



    private void getPdfTemplateHeader(OleNoticeBo noticeBo, Document document) throws Exception {
        if (LOG.isDebugEnabled()){
            LOG.debug("Header for the notice" + noticeBo.getNoticeName() + noticeBo.getItemId());
        }
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Patron information", getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20,2,30});


        pdfTable.addCell(getPdfPCellInJustified("Patron Name"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified(noticeBo.getPatronName() != null ? noticeBo.getPatronName() : ""));

        pdfTable.addCell(getPdfPCellInJustified("Address"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));

        pdfTable.addCell(getPdfPCellInJustified("Email"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));

        pdfTable.addCell(getPdfPCellInJustified("Phone #"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));

        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
    }

    private void getPdfTemplateBody(Document document, String title, String body) throws Exception {
        //Notice Type
        if (LOG.isDebugEnabled()){
            LOG.debug("Body Content of the notice :" + title);
        }
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(new Chunk(title, getBoldFont()));
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);


        //Notice-specific text
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk(body, getBoldFont()));
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
    }

    private void getPdfTemplateFooter(OleNoticeBo noticeBo, Document document) throws Exception {
        if (LOG.isDebugEnabled()){
            LOG.debug("Footer for the notice : " + noticeBo.getNoticeName() + noticeBo.getItemId());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Title/item information", getBoldFont()));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20,2,30});
        if (noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL) || noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST)) {
            String circulationLocation = null;
            String circulationReplyToEmail = null;
            OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDesk(noticeBo.getItemShelvingLocation());
            if (oleCirculationDesk != null) {
                circulationLocation = oleCirculationDesk.getCirculationDeskPublicName();
                if (StringUtils.isNotBlank(oleCirculationDesk.getReplyToEmail())) {
                    circulationReplyToEmail = oleCirculationDesk.getReplyToEmail();
                } else {
                    circulationReplyToEmail = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                }
            } else {
                circulationLocation = getLoanProcessor().getParameter(OLEParameterConstants.DEFAULT_CIRCULATION_DESK);
                circulationReplyToEmail = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
            }
            pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(circulationLocation));

            pdfTable.addCell(getPdfPCellInJustified("Circulation Reply-To Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(circulationReplyToEmail));

        } else if (noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_ONHOLD) || noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
            pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));

            pdfTable.addCell(getPdfPCellInJustified("Circulation Reply-To Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((StringUtils.isNotBlank(noticeBo.getCirculationDeskReplyToEmail()) ? noticeBo.getCirculationDeskReplyToEmail() : getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL))));
        }
        pdfTable.addCell(getPdfPCellInJustified("Title"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getTitle() == null ? "" : noticeBo.getTitle())));

        pdfTable.addCell(getPdfPCellInJustified("Author"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getAuthor() == null ? "" : noticeBo.getAuthor())));


        pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy # "));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        if(!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)){
            pdfTable.addCell(getPdfPCellInJustified(noticeBo.getVolumeNumber() == null ? "" : noticeBo.getVolumeNumber()));
        }else{
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getVolumeIssueCopyNumber() == null ? "" : noticeBo.getVolumeIssueCopyNumber())));
        }


        pdfTable.addCell(getPdfPCellInJustified("Library shelving location "));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemShelvingLocation() == null ? "" : noticeBo.getItemShelvingLocation())));


        pdfTable.addCell(getPdfPCellInJustified("Call #"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemCallNumber() == null ? "" : noticeBo.getItemCallNumber())));


        pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
        pdfTable.addCell(getPdfPCellInLeft(":"));
        pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemId() == null ? "" : noticeBo.getItemId())));

        if (noticeBo.getExpiredOnHoldDate() != null) {
            pdfTable.addCell(getPdfPCellInJustified("Hold Expiration Date"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getExpiredOnHoldDate() == null ? "" : dateFormat.format(noticeBo.getExpiredOnHoldDate()))));

        }
        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        if (noticeBo.getNoticeName().equals(OLEConstants.NOTICE_RECALL)) {
            if (LOG.isDebugEnabled()){
                LOG.debug("Recall Notice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});

            pdfTable.addCell(getPdfPCellInJustified("Original Due Date"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getOriginalDueDate() == null ? "" : sdf.format(noticeBo.getOriginalDueDate()))));


            pdfTable.addCell(getPdfPCellInJustified("New Due Date"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getNewDueDate() == null ? "" : sdf.format(noticeBo.getNewDueDate()))));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        } else if (noticeBo.getNoticeName().equals("OnHold")) {
            if (LOG.isDebugEnabled()){
                LOG.debug("OnHold Notice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});

            pdfTable.addCell(getPdfPCellInJustified("Pick Up Location"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPickUpLocation() != null ? noticeBo.getPickUpLocation() : "")));

            pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));


            pdfTable.addCell(getPdfPCellInJustified("Address"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));


            pdfTable.addCell(getPdfPCellInJustified("Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));


            pdfTable.addCell(getPdfPCellInJustified("Phone #"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});

            pdfTable.addCell(getPdfPCellInJustified("Item Will Be Held until"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getExpiredOnHoldDate() != null ? dateFormat.format(noticeBo.getExpiredOnHoldDate().toString()) : "")));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        } else if (noticeBo.getNoticeName().equals("Overdue Notice")) {
            if (LOG.isDebugEnabled()){
                LOG.debug("OverdueNotice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});

            pdfTable.addCell(getPdfPCellInJustified("Item was due"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getDueDate() == null ? "" : (sdf.format(noticeBo.getDueDate())))));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        }

    }

    public void getPdfFooter(Document document, OutputStream outputStream) throws Exception {
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
       // String myAccountURL = loanProcessor.getParameter(OLEConstants.MY_ACCOUNT_URL);
        String myAccountURL = url+OLEConstants.OLE_MY_ACCOUNT_URL_CHANNEL+url+OLEConstants.OLE_MY_ACCOUNT_URL;
        if(myAccountURL!=null && !myAccountURL.trim().isEmpty()){
            Font ver_15_normal = FontFactory.getFont("Times-Roman", 15, Font.BOLD,BaseColor.BLUE);
            ver_15_normal.setColor(BaseColor.BLUE);
            ver_15_normal.setStyle(Font.UNDERLINE);
            Anchor anchor = new Anchor("MyAccount", ver_15_normal);
            anchor.setReference(myAccountURL);
            Paragraph paraGraph = new Paragraph();
            paraGraph.add(anchor);
            paraGraph.setFont(ver_15_normal);
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paraGraph);
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public void getPdfPickUpNotice(OleNoticeBo noticeBo) throws Exception{
        if (LOG.isDebugEnabled()){
            LOG.debug("PickUp notice : " + noticeBo.getNoticeName() + noticeBo.getItemId());
        }
        Document document = new Document(PageSize.A4);
        OutputStream outputStream = null;
        outputStream = getPdfHeader(document, outputStream,getLoanProcessor().getParameter(OLEParameterConstants.PICKUP_TITLE), noticeBo.getItemId());
        document.setPageCount(3);
        PdfPTable pdfTable = new PdfPTable(1);

        pdfTable.addCell(getPdfPCellNewLine());
        pdfTable.addCell(getPdfPCellInLeft((noticeBo.getPatronName() != null) ? noticeBo.getPatronName().toString() : ""));
        pdfTable.addCell(getPdfPCellInLeft((noticeBo.getPatronAddress() != null) ? noticeBo.getPatronAddress().toString() : ""));
        pdfTable.addCell(getPdfPCellInLeft((noticeBo.getPatronEmailAddress() != null) ? noticeBo.getPatronEmailAddress().toString() : ""));
        pdfTable.addCell(getPdfPCellInLeft((noticeBo.getPatronPhoneNumber() != null) ? noticeBo.getPatronPhoneNumber().toString() : ""));

        document.add(pdfTable);

        pdfTable = new PdfPTable(1);
        pdfTable.setWidths(new int[]{10});

        pdfTable.addCell(getPdfPCellInCenter(noticeBo.getNoticeName(),getBoldFont()));
        pdfTable.addCell(getPdfPCellNewLine());
        pdfTable.addCell(getPdfPCellInLeft(noticeBo.getNoticeSpecificContent()));
        pdfTable.addCell(getPdfPCellNewLine());
        pdfTable.addCell(getPdfPCellInLeft("Thank you."));

        document.add(pdfTable);


        document.add(getPdfParagraphNewLine());

        PdfPTable pdfItemTable = new PdfPTable(3);
        pdfItemTable.setWidths(new int[]{10,2,30});

        String author=((noticeBo.getAuthor() != null) ? noticeBo.getAuthor().toString() : "");
        String title=((noticeBo.getTitle() != null) ? noticeBo.getTitle().toString() : "");
        String callNumber=((noticeBo.getItemCallNumber()!=null)?noticeBo.getItemCallNumber().toString():"");

        pdfItemTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
        pdfItemTable.addCell(getPdfPCellInLeft(":"));
        pdfItemTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));

        pdfItemTable.addCell(getPdfPCellInJustified("Circulation Reply-To Email"));
        pdfItemTable.addCell(getPdfPCellInLeft(":"));
        pdfItemTable.addCell(getPdfPCellInJustified((StringUtils.isNotBlank(noticeBo.getCirculationDeskReplyToEmail()) ? noticeBo.getCirculationDeskReplyToEmail() : getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL))));

        pdfItemTable.addCell(getPdfPCellInLeft("Title"));
        pdfItemTable.addCell(getPdfPCellInLeft(":"));
        pdfItemTable.addCell(getPdfPCellInJustified(title));

        pdfItemTable.addCell(getPdfPCellInLeft("Author"));
        pdfItemTable.addCell(getPdfPCellInLeft(":"));
        pdfItemTable.addCell(getPdfPCellInJustified(author));

        pdfItemTable.addCell(getPdfPCellInLeft("Call Number"));
        pdfItemTable.addCell(getPdfPCellInLeft(":"));
        pdfItemTable.addCell(getPdfPCellInJustified(callNumber));

        document.add(pdfItemTable);

        document.close();
        outputStream.close();

    }

    public void getPdfTemplateFooterList(java.util.List<OleNoticeBo> oleNoticeBoList, Document document) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        int count = 0;
        for (OleNoticeBo noticeBo : oleNoticeBoList) {
            if (LOG.isDebugEnabled()){
                LOG.debug("Footer for the notice : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            Paragraph paraGraph = new Paragraph();
            if (count == 0) {
                paraGraph.add(new Chunk("Title/item information", getBoldFont()));
            }
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});
            PdfPCell pdfPCell;
            if (!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {

                pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));

            }

            pdfTable.addCell(getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getTitle() == null ? "" : noticeBo.getTitle())));


            pdfTable.addCell(getPdfPCellInJustified("Author"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getAuthor() == null ? "" : noticeBo.getAuthor())));


            pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy # "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getVolumeNumber() == null ? "" : noticeBo.getVolumeNumber())));


            pdfTable.addCell(getPdfPCellInJustified("Library shelving location"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemShelvingLocation() == null ? "" : noticeBo.getItemShelvingLocation())));



            pdfTable.addCell(getPdfPCellInJustified("Call # "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemCallNumber() == null ? "" : noticeBo.getItemCallNumber())));



            pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((noticeBo.getItemId() == null ? "" : noticeBo.getItemId())));

            if (noticeBo.getExpiredOnHoldDate() != null) {

                pdfTable.addCell(getPdfPCellInJustified("Expiration onHoldDate"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getExpiredOnHoldDate() == null ? "" : dateFormat.format(noticeBo.getExpiredOnHoldDate()))));

            }
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            if (noticeBo.getNoticeName().equals("Overdue Notice")) {
                if (LOG.isDebugEnabled()){
                    LOG.debug("OverdueNotice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
                }
                pdfTable = new PdfPTable(3);
                pdfTable.setWidths(new int[]{20, 2, 30});

                pdfTable.addCell(getPdfPCellInJustified("Item was due"));
                pdfTable.addCell(getPdfPCellInLeft(":"));
                pdfTable.addCell(getPdfPCellInJustified((noticeBo.getDueDate() == null ? "" : (sdf.format(noticeBo.getDueDate()).toString()))));

                document.add(pdfTable);
                paraGraph = new Paragraph();
                paraGraph.add(Chunk.NEWLINE);
                document.add(paraGraph);
            }
            count++;
        }
    }

    public void getHeaderAndPatronPDFContent(OlePatronDocument olePatronDocument,boolean isOverdue){
        Document document = new Document(PageSize.A4);
        OutputStream outputStream=null;
        try {
            String title = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_TITLE);
            String body = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT);
            if(!isOverdue){
                title=loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE);
                body=loanProcessor.getParameter(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT);
            }
            outputStream = getPdfHeader(document, outputStream, title, olePatronDocument.getOlePatronId());
            OleDeliverRequestDocumentHelperServiceImpl deliverService = new OleDeliverRequestDocumentHelperServiceImpl();
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            Paragraph paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Patron information", getBoldFont()));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});

            pdfTable.addCell(getPdfPCellInJustified("Patron Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName())));


            pdfTable.addCell(getPdfPCellInJustified("Address"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "")));



            pdfTable.addCell(getPdfPCellInJustified("Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "")));

            pdfTable.addCell(getPdfPCellInJustified("Phone #"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "")));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);


            getPdfTemplateBody(document, title, body);
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Title/item information", getBoldFont()));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            this.setOverdueDocument(document);
            this.setOverdueOutPutStream(outputStream);
        } catch (Exception e) {
            LOG.error("Error---->While generating overdue notice pdf (Patron Information) "+e.getMessage()+e);
        }

    }

    public Document getOverdueNoticePDFContent(OleLoanDocument oleLoanDocument, boolean isOverdue, Document document) {
        try {
            Paragraph paraGraph = new Paragraph();
            SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" "+RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});

            String circulationLocation = null;
            String circulationReplyToEmail = null;
            OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDesk(oleLoanDocument.getItemLocation());
            if (oleCirculationDesk != null) {
                circulationLocation = oleCirculationDesk.getCirculationDeskPublicName();
                if (StringUtils.isNotBlank(oleCirculationDesk.getReplyToEmail())) {
                    circulationReplyToEmail = oleCirculationDesk.getReplyToEmail();
                } else {
                    circulationReplyToEmail = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                }
            } else {
                circulationLocation = getLoanProcessor().getParameter(OLEParameterConstants.DEFAULT_CIRCULATION_DESK);
                circulationReplyToEmail = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
            }

            pdfTable.addCell(getPdfPCellInJustified("Circulation Location / Library Name"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(circulationLocation));

            pdfTable.addCell(getPdfPCellInJustified("Circulation Reply-To Email"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(circulationReplyToEmail));

            pdfTable.addCell(getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getTitle() == null ? "" : oleLoanDocument.getTitle())));

            pdfTable.addCell(getPdfPCellInJustified("Author"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getAuthor() == null ? "" : oleLoanDocument.getAuthor())));

            String volume = (String) oleLoanDocument.getEnumeration() != null && !oleLoanDocument.getEnumeration().equals("") ? oleLoanDocument.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = (String) oleLoanDocument.getItemCopyNumber() != null && !oleLoanDocument.getItemCopyNumber().equals("") ? oleLoanDocument.getItemCopyNumber(): "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;

            pdfTable.addCell(getPdfPCellInJustified("Volume/Issue/Copy #"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((volumeNumber == null ? "" : volumeNumber)));

            pdfTable.addCell(getPdfPCellInJustified("Library shelving location "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getItemLocation() == null ? "" : oleLoanDocument.getItemLocation())));

            String callNumber = "";
            if (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("")) {
                callNumber = (String) (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("") ? oleLoanDocument.getItemCallNumber() : "");
            }
            pdfTable.addCell(getPdfPCellInJustified("Call # "));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((callNumber == null ? "" : callNumber)));

            pdfTable.addCell(getPdfPCellInJustified("Item barcode"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getItemId() == null ? "" : oleLoanDocument.getItemId())));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});

            pdfTable.addCell(getPdfPCellInJustified("Item was due"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((oleLoanDocument.getLoanDueDate() == null ? "" : (sdf.format(oleLoanDocument.getLoanDueDate()).toString()))));

            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

        } catch (Exception e) {
            LOG.error("Error---->While generating overdue notice pdf  ");
            if(oleLoanDocument!=null){
                LOG.error("Error---->Item Barcode "+oleLoanDocument.getItemId());
            }
            LOG.error(e.getMessage()+e);
        }
        return document;
    }

    public boolean cleanZeroByteFiles() {
        boolean isFileDeleted=false;
        String pdfLocationSystemParam = getLoanProcessor().getParameter(OLEParameterConstants.PDF_LOCATION);
        if (LOG.isDebugEnabled()) {
            LOG.debug("System Parameter for PDF_Location --> " + pdfLocationSystemParam);
        }
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
            if (LOG.isDebugEnabled()) {
                LOG.debug("System Parameter for PDF_Location staging dir--> " + pdfLocationSystemParam);
            }
        } else {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory") + "/" + pdfLocationSystemParam + "/";
        }
        File file=new File(pdfLocationSystemParam);
        boolean locationExist = file.exists();
        if(locationExist){
            try {
                File folder = new File(pdfLocationSystemParam);
                File[] listOfFiles = folder.listFiles();
                for (File file1 : listOfFiles) {
                    if (file1.isFile() && file1.length()==0) {
                        file1.delete();
                    }
                }
            }  catch (Exception e) {

               LOG.error("Error in deleting the file"+e.getMessage());
            }
        }
       return isFileDeleted;
    }

}