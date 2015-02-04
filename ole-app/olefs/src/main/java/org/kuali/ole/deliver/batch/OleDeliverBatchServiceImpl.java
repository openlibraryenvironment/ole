package org.kuali.ole.deliver.batch;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
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
    private Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
    private Font ver_15_normal = FontFactory.getFont("VERDANA", 15, 0);
    private DocstoreClientLocator docstoreClientLocator;
    private OutputStream overdueOutPutStream=null;
    private Document overdueDocument=null;

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
        return overdueDocument;
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        if (noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
            if (noticeBoList.get(courtesy).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
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
            if (noticeBo.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_COURTESY)) {
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
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), loanProcessor.getParameter(OLEParameterConstants.OVERDUE_BODY));
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
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE), loanProcessor.getParameter(OLEParameterConstants.EXPIRED_BODY));

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
            getTemplateBody(stringBuffer, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE), loanProcessor.getParameter(OLEParameterConstants.COURTESY_BODY));
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
        Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
        Font ver_15_normal = FontFactory.getFont("VERDANA", 15, 0);
        document.open();
        document.newPage();
        Paragraph paraGraph = new Paragraph();
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(new Chunk(title, boldFont));
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        return outputStream;
    }

    public void createPdfForIntransitRequest(OleDeliverRequestBo oleDeliverRequestBo, HttpServletResponse response) {
        LOG.debug("Creating pdf for intransit request");
        String title = "InTransit Request";
        OutputStream outputStream = null;
        /*String pdfLocationSystemParam = getLoanProcessor().getParameter("PDF_LOCATION");
        if (LOG.isInfoEnabled()) {
            LOG.info("System Parameter for PDF_Location --> " + pdfLocationSystemParam);
        }
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
            if (LOG.isInfoEnabled()) {
                LOG.info("System Parameter for PDF_Location staging dir--> " + pdfLocationSystemParam);
            }
        } else{
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory")+ "/" + pdfLocationSystemParam +"/";
        }
        boolean locationExist = new File(pdfLocationSystemParam).exists();
        boolean fileCreated = false;
        if (LOG.isInfoEnabled()) {
            LOG.info("Is directory Exist ::" + locationExist);
        }
        try {
            if (!locationExist) {
                fileCreated = new File(pdfLocationSystemParam).mkdirs();
                if (!fileCreated) {
                    throw new RuntimeException("Not Able to create directory :" + pdfLocationSystemParam);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occured while creating the directory : " + e.getMessage(),e);

        } */
        String fileName = "IntransitRequestSlip" + "_" + oleDeliverRequestBo.getItemId() + "_" + new Date(System.currentTimeMillis()) + ".pdf";
        if (LOG.isDebugEnabled()) {
            LOG.debug("File Created :" + title + "file name ::" + fileName + "::");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        try {
            String itemTitle = oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "";
            Date dateOfReq = oleDeliverRequestBo.getCreateDate()!=null?oleDeliverRequestBo.getCreateDate():null;
            //String dateOfRequest = dateOfReq != null ? dateOfReq.toString() : "";
            String circulationLocation = oleDeliverRequestBo.getCirculationLocationCode() != null ? oleDeliverRequestBo.getCirculationLocationCode() : "";
            String itemBarcode = oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "";
            String patronBarcode = oleDeliverRequestBo.getBorrowerBarcode() != null ? oleDeliverRequestBo.getBorrowerBarcode() : "";
            String requestType = oleDeliverRequestBo.getRequestTypeId() != null ? oleDeliverRequestBo.getRequestTypeId() : "";
            boolean inTransitSlip = requestType.equalsIgnoreCase("8");

            Document document = this.getDocument(0, 0, 5, 5);
            //outputStream = new FileOutputStream(fileName);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            PdfWriter.getInstance(document, outputStream);
            Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
            document.open();
            document.newPage();
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});
            Paragraph paraGraph = new Paragraph();
            if (inTransitSlip) {
                paraGraph.setAlignment(Element.ALIGN_CENTER);
                paraGraph.add(new Chunk("InTransit Request Slip", boldFont));
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

    private PdfPCell getPdfPCellInJustified(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    public Document getDocument(float f1, float f2, float f3, float f4) {
        Document document = new Document(PageSize.A4);
        document.setMargins(f1, f2, f3, f4);
        return document;
    }

    private void getPdfTemplateHeader(OleNoticeBo noticeBo, Document document) throws Exception {
        if (LOG.isDebugEnabled()){
            LOG.debug("Header for the notice" + noticeBo.getNoticeName() + noticeBo.getItemId());
        }
          //PdfPTable pdfTable = new PdfPTable(3);
//        PdfPCell pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(":"));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Address")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(":"));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Email")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(":"));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Phone #")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(":"));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
//        pdfTable.addCell(pdfPCell);
//        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));
//        pdfPCell.setBorder(pdfPCell.NO_BORDER);
//        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
//        pdfTable.addCell(pdfPCell);
//        document.add(pdfTable);
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Patron information", boldFont));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20,2,30});
        PdfPCell pdfPCell= new PdfPCell(new Paragraph("Patron Name"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronName() != null ? noticeBo.getPatronName() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Address")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Email")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Phone #")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
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
        paraGraph.add(new Chunk(title, boldFont));
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);


        //Notice-specific text
        paraGraph = new Paragraph();
        paraGraph.add(new Chunk(body, boldFont));
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
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(new Chunk("Title/item information", boldFont));
        paraGraph.add(Chunk.NEWLINE);
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);
        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidths(new int[]{20,2,30});
        PdfPCell pdfPCell;
        if (!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {
            pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
        }
        pdfPCell = new PdfPCell(new Paragraph("Title "));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getTitle() == null ? "" : noticeBo.getTitle())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Author ")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getAuthor() == null ? "" : noticeBo.getAuthor())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Volume/Issue/Copy # ")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        if(!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)){
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getVolumeNumber() == null ? "" : noticeBo.getVolumeNumber())));
        }else{
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getVolumeIssueCopyNumber() == null ? "" : noticeBo.getVolumeIssueCopyNumber())));
        }
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Library shelving location ")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemShelvingLocation() == null ? "" : noticeBo.getItemShelvingLocation())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Call # ")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemCallNumber() == null ? "" : noticeBo.getItemCallNumber())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk("Item barcode")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(":"));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);
        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemId() == null ? "" : noticeBo.getItemId())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        pdfTable.addCell(pdfPCell);
        if (noticeBo.getExpiredOnHoldDate() != null) {
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Expiration onHoldDate")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getExpiredOnHoldDate() == null ? "" : dateFormat.format(noticeBo.getExpiredOnHoldDate()))));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
        }
        document.add(pdfTable);
        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        //Information specific text
  /*          paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Information specific text",boldFont));
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);*/
        if (noticeBo.getNoticeName().equals(OLEConstants.NOTICE_RECALL)) {
            if (LOG.isDebugEnabled()){
                LOG.debug("Recall Notice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});
            pdfPCell = new PdfPCell(new Paragraph("Original Due Date"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getOriginalDueDate() == null ? "" : sdf.format(noticeBo.getOriginalDueDate()))));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("New Due Date")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getNewDueDate() == null ? "" : sdf.format(noticeBo.getNewDueDate()))));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
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
            pdfPCell = new PdfPCell(new Paragraph("Pick Up Location"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPickUpLocation() != null ? noticeBo.getPickUpLocation() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Address")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronAddress() != null ? noticeBo.getPatronAddress() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Email")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronEmailAddress() != null ? noticeBo.getPatronEmailAddress() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Phone #")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getPatronPhoneNumber() != null ? noticeBo.getPatronPhoneNumber() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Item Will Be Held until")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getExpiredOnHoldDate() != null ? dateFormat.format(noticeBo.getExpiredOnHoldDate().toString()) : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        } else if (noticeBo.getNoticeName().equals("OverdueNotice")) {
            if (LOG.isDebugEnabled()){
                LOG.debug("OverdueNotice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20,2,30});
            pdfPCell = new PdfPCell(new Paragraph("Item was due"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getDueDate() == null ? "" : (sdf.format(noticeBo.getDueDate())))));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
        }

    }

    public void getPdfFooter(Document document, OutputStream outputStream) throws Exception {
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        String myAccountURL = loanProcessor.getParameter(OLEConstants.MY_ACCOUNT_URL);
        if(myAccountURL!=null && !myAccountURL.trim().isEmpty()){
        ver_15_normal.setColor(Color.blue);
        ver_15_normal.setStyle(Font.UNDERLINE);
        Anchor anchor = new Anchor("MyAccount", ver_15_normal);
        anchor.setName("My Account");
        anchor.setReference(myAccountURL);
        Paragraph paraGraph = new Paragraph();
        paraGraph.add(anchor);
        paraGraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paraGraph);
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public List getPdfNoticeForPatron(List<OleNoticeBo> noticeBoList) throws Exception {
        return getPdfTemplate(noticeBoList);
    }

    private List getPdfTemplate(List<OleNoticeBo> noticeBoList) throws Exception {
        List templateForNoticeList = new ArrayList();
        LOG.debug("In getPdfTemplate()");
        try {
            Document document = new Document(PageSize.A4);
            OutputStream outputStream = null;

            List<OleNoticeBo> overDueNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> onHoldNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> recallNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> expiredNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> expiredRequiredNoticeList = new ArrayList<OleNoticeBo>();
            List<OleNoticeBo> courtesyNoticeList = new ArrayList<OleNoticeBo>();

            for (int temp = 0; temp < noticeBoList.size(); temp++) {
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
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
                if (noticeBoList.get(temp).getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_COURTESY)) {
                    courtesyNoticeList.add(noticeBoList.get(temp));
                }
            }
            LoanProcessor loanProcessor = getLoanProcessor();

            if (overDueNoticeList != null && overDueNoticeList.size() > 0) {
                outputStream = getPdfHeader(document, outputStream, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), overDueNoticeList.get(0).getItemId());
                getPdfTemplateHeader(overDueNoticeList.get(0), document);
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE), loanProcessor.getParameter(OLEParameterConstants.OVERDUE_BODY));
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
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE), loanProcessor.getParameter(OLEParameterConstants.EXPIRED_BODY));
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
                getPdfTemplateBody(document, loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE), loanProcessor.getParameter(OLEParameterConstants.COURTESY_BODY));
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

        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);

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
    public void getPdfPickUpNotice(OleNoticeBo noticeBo) throws Exception{
        if (LOG.isDebugEnabled()){
            LOG.debug("PickUp notice : " + noticeBo.getNoticeName() + noticeBo.getItemId());
        }
        Document document = new Document(PageSize.A4);
        OutputStream outputStream = null;
        outputStream = getPdfHeader(document, outputStream,getLoanProcessor().getParameter(OLEParameterConstants.PICKUP_TITLE), noticeBo.getItemId());
        document.setPageCount(3);
        PdfPTable pdfTable = new PdfPTable(1);

        PdfPCell pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(new Chunk((noticeBo.getPatronName() != null) ? noticeBo.getPatronName().toString() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(new Chunk((noticeBo.getPatronAddress() != null) ? noticeBo.getPatronAddress().toString() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(new Chunk((noticeBo.getPatronEmailAddress() != null) ? noticeBo.getPatronEmailAddress().toString() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(new Chunk((noticeBo.getPatronPhoneNumber() != null) ? noticeBo.getPatronPhoneNumber().toString() : "")));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        document.add(pdfTable);

        pdfTable = new PdfPTable(1);
        pdfTable.setWidths(new int[]{10});

        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getNoticeName(),boldFont)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_CENTER);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getNoticeSpecificContent())));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph(Chunk.NEWLINE));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfTable.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("Thank you."));

        Paragraph paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfTable.addCell(pdfPCell);

        document.add(pdfTable);

        paraGraph = new Paragraph();
        paraGraph.add(Chunk.NEWLINE);
        document.add(paraGraph);

        PdfPTable pdfItemTable = new PdfPTable(3);
        pdfItemTable.setWidths(new int[]{10,2,30});

        String author=((noticeBo.getAuthor() != null) ? noticeBo.getAuthor().toString() : "");
        String title=((noticeBo.getTitle() != null) ? noticeBo.getTitle().toString() : "");
        String callNumber=((noticeBo.getItemCallNumber()!=null)?noticeBo.getItemCallNumber().toString():"");

        PdfPCell pdfItemCell = new PdfPCell(new Paragraph(new Chunk("Title")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(":")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(title)));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk("Author")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(":")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(author)));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk("Call Number")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(":")));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        pdfItemCell = new PdfPCell(new Paragraph(new Chunk(callNumber)));
        pdfItemCell.setBorder(pdfPCell.NO_BORDER);
        pdfItemCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        pdfItemTable.addCell(pdfItemCell);

        document.add(pdfItemTable);

        document.close();
        outputStream.close();

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
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService=new OleDeliverRequestDocumentHelperServiceImpl();
        String patronMail="";
        String patronAddress="";
        String patronPhoneNumber="";
        try{
            patronMail=oleDeliverRequestDocumentHelperService.getPatronHomeEmailId(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
            patronAddress=oleDeliverRequestDocumentHelperService.getPatronPreferredAddress(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
            patronPhoneNumber=oleDeliverRequestDocumentHelperService.getPatronHomePhoneNumber(olePatronDocument.getEntity().getEntityTypeContactInfos().get(0));
        }catch(Exception e){
            LOG.error("Exception while generating mail content from patron bill",e);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        contentForSendMail.append("<TR><TD>Fee Amount</TD><TD>:</TD><TD>" +"$"+ fineAmount + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Item Barcode</TD><TD>:</TD><TD>" + oleLoanDocument.getItemId() + "</TD></TR>");
    /*    try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            if (oleLoanDocument.getItemId() != null) {

                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleLoanDocument.getItemId()), ""));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), Item.CALL_NUMBER));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), Item.COPY_NUMBER));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), Item.ENUMERATION));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.TITLE));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.AUTHOR));
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);


                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                        if (fieldName.equalsIgnoreCase(Bib.TITLE) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {


                            oleLoanDocument.setTitle(fieldValue);
                        } else if (fieldName.equalsIgnoreCase(Bib.AUTHOR) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {


                            oleLoanDocument.setAuthor(fieldValue);

                        } else if (fieldName.equalsIgnoreCase(Item.CALL_NUMBER) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {


                            oleLoanDocument.setItemCallNumber(fieldValue);

                        } else if (fieldName.equalsIgnoreCase(Item.COPY_NUMBER) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {


                            oleLoanDocument.setItemCopyNumber(fieldValue);

                        }  else if (fieldName.equalsIgnoreCase(Item.ENUMERATION) && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {


                            oleLoanDocument.setItemVolumeNumber(fieldValue);

                        }

                    }
                }
            } else {
                item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleLoanDocument.getItemUuid());
                ItemOlemlRecordProcessor itemOlemlRecordProcessor=new ItemOlemlRecordProcessor();
                org.kuali.ole.docstore.common.document.content.instance.Item itemXML=itemOlemlRecordProcessor.fromXML(item.getContent());

                oleLoanDocument.setItemCallNumber(itemXML.getCallNumber().getNumber());
                oleLoanDocument.setAuthor(item.getHolding().getBib().getAuthor());
                oleLoanDocument.setItemVolumeNumber(itemXML.getVolumeNumber());
                oleLoanDocument.setItemCopyNumber(itemXML.getCopyNumber());
                oleLoanDocument.setTitle(item.getHolding().getBib().getTitle());


            }


        } catch (Exception e) {
           LOG.error("Exception while generating mail content from patron bill",e);
        }*/
        String issue = new String(" ");
        contentForSendMail.append("<TR><TD>Title</TD><TD>:</TD><TD>" + (oleLoanDocument.getTitle()!=null ? oleLoanDocument.getTitle() : "") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Author</TD><TD>:</TD><TD>" + (oleLoanDocument.getAuthor()!=null ? oleLoanDocument.getAuthor() : "") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Volume/Issue/Copy # </TD><TD>:</TD><TD>" + (oleLoanDocument.getItemVolumeNumber()!=null? oleLoanDocument.getItemVolumeNumber():"") + "/" + issue + "/" +  (oleLoanDocument.getItemCopyNumber()!=null? oleLoanDocument.getItemCopyNumber():"")+ "</TD></TR>");
        contentForSendMail.append("<TR><TD>Library Shelving Location</TD><TD>:</TD><TD>" + oleLoanDocument.getLocation() + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Call Number</TD><TD>:</TD><TD>" + (oleLoanDocument.getItemCallNumber()!=null? oleLoanDocument.getItemCallNumber():"") + "</TD></TR>");
        contentForSendMail.append("<TR><TD>Item was Due</TD><TD>:</TD><TD>" + (oleLoanDocument.getLoanDueDate()!=null ? sdf.format(oleLoanDocument.getLoanDueDate()) : "") + "</TD></TR>");
        contentForSendMail.append("</TABLE>");
        contentForSendMail.append("</BODY>");
        contentForSendMail.append("</HTML>");
        return contentForSendMail.toString();
    }

    public void getPdfTemplateFooterList(List<OleNoticeBo> oleNoticeBoList, Document document) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        int count = 0;
        for (OleNoticeBo noticeBo : oleNoticeBoList) {
            if (LOG.isDebugEnabled()){
                LOG.debug("Footer for the notice : " + noticeBo.getNoticeName() + noticeBo.getItemId());
            }
            Paragraph paraGraph = new Paragraph();
            if (count == 0) {
                paraGraph.add(new Chunk("Title/item information", boldFont));
            }
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});
            PdfPCell pdfPCell;
            if (!noticeBo.getNoticeName().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)) {
                pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(":"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getCirculationDeskName() != null ? noticeBo.getCirculationDeskName() : "")));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
            }
            pdfPCell = new PdfPCell(new Paragraph("Title "));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getTitle() == null ? "" : noticeBo.getTitle())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Author ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getAuthor() == null ? "" : noticeBo.getAuthor())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Volume/Issue/Copy # ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getVolumeNumber() == null ? "" : noticeBo.getVolumeNumber())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Library shelving location ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemShelvingLocation() == null ? "" : noticeBo.getItemShelvingLocation())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Call # ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemCallNumber() == null ? "" : noticeBo.getItemCallNumber())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Item barcode")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getItemId() == null ? "" : noticeBo.getItemId())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            if (noticeBo.getExpiredOnHoldDate() != null) {
                pdfPCell = new PdfPCell(new Paragraph(new Chunk("Expiration onHoldDate")));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(":"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getExpiredOnHoldDate() == null ? "" : dateFormat.format(noticeBo.getExpiredOnHoldDate()))));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
            }
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            if (noticeBo.getNoticeName().equals("OverdueNotice")) {
                if (LOG.isDebugEnabled()){
                    LOG.debug("OverdueNotice Footer Content : " + noticeBo.getNoticeName() + noticeBo.getItemId());
                }
                pdfTable = new PdfPTable(3);
                pdfTable.setWidths(new int[]{20, 2, 30});

                pdfPCell = new PdfPCell(new Paragraph("Item was due"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(":"));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
                pdfTable.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Paragraph(new Chunk(noticeBo.getDueDate() == null ? "" : (sdf.format(noticeBo.getDueDate()).toString()))));
                pdfPCell.setBorder(pdfPCell.NO_BORDER);
                pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
                pdfTable.addCell(pdfPCell);
                document.add(pdfTable);
                paraGraph = new Paragraph();
                paraGraph.add(Chunk.NEWLINE);
                document.add(paraGraph);
            }
            count++;
        }
    }

    public  String getOverdueNoticeHTMLContent(OleLoanDocument  oleLoanDocument){
        SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
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
        String body = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_BODY);
        if(!isOverdue){
            title=loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE);
            body=loanProcessor.getParameter(OLEParameterConstants.COURTESY_BODY);
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
        stringBuffer.append("<TR><TD>Address :</TD><TD>" + (deliverService.getPatronPreferredAddress(entityTypeContactInfoBo) != null ? deliverService.getPatronPreferredAddress(entityTypeContactInfoBo) : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>EMAIL :</TD><TD>" + (deliverService.getPatronHomeEmailId(entityTypeContactInfoBo) != null ? deliverService.getPatronHomeEmailId(entityTypeContactInfoBo) : "") + "</TD></TR>");

        stringBuffer.append("<TR><TD>Phone Number :</TD><TD>" +(deliverService.getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? deliverService.getPatronHomePhoneNumber(entityTypeContactInfoBo) : "") + "</TD></TR>");
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

    public void getHeaderAndPatronPDFContent(OlePatronDocument olePatronDocument,boolean isOverdue){
        Document document = new Document(PageSize.A4);
        OutputStream outputStream=null;
        try {
            String title = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_TITLE);
            String body = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_BODY);
            if(!isOverdue){
                title=loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE);
                body=loanProcessor.getParameter(OLEParameterConstants.COURTESY_BODY);
            }
            outputStream = getPdfHeader(document, outputStream, title, olePatronDocument.getOlePatronId());
            OleDeliverRequestDocumentHelperServiceImpl deliverService = new OleDeliverRequestDocumentHelperServiceImpl();
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            Paragraph paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Patron information", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});
            PdfPCell pdfPCell = new PdfPCell(new Paragraph("Patron Name"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Address")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(deliverService.getPatronPreferredAddress(entityTypeContactInfoBo) != null ? deliverService.getPatronPreferredAddress(entityTypeContactInfoBo) : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Email")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(deliverService.getPatronHomeEmailId(entityTypeContactInfoBo) != null ? deliverService.getPatronHomeEmailId(entityTypeContactInfoBo) : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Phone #")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(deliverService.getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? deliverService.getPatronHomePhoneNumber(entityTypeContactInfoBo) : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);


            getPdfTemplateBody(document, title, body);
            paraGraph = new Paragraph();
            paraGraph.add(new Chunk("Title/item information", boldFont));
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
            OleDeliverRequestDocumentHelperServiceImpl deliverService = new OleDeliverRequestDocumentHelperServiceImpl();
            SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.TIMESTAMP);
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});
            PdfPCell pdfPCell = new PdfPCell(new Paragraph("Circulation Location / Library Name"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            OleCirculationDesk oleCirculationDesk = deliverService.getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleCirculationDesk.getCirculationDeskPublicName() != null ? oleCirculationDesk.getCirculationDeskPublicName() : "")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Paragraph("Title "));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleLoanDocument.getTitle() == null ? "" : oleLoanDocument.getTitle())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Author ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleLoanDocument.getAuthor() == null ? "" : oleLoanDocument.getAuthor())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Volume/Issue/Copy # ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            String volume = (String) oleLoanDocument.getEnumeration() != null && !oleLoanDocument.getEnumeration().equals("") ? oleLoanDocument.getEnumeration() : "";
            String issue = new String(" ");
            String copyNumber = (String) oleLoanDocument.getItemCopyNumber() != null && !oleLoanDocument.getItemCopyNumber().equals("") ? oleLoanDocument.getItemCopyNumber(): "";
            String volumeNumber = volume + "/" + issue + "/" + copyNumber;
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(volumeNumber == null ? "" : volumeNumber)));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Library shelving location ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleLoanDocument.getItemLocation() == null ? "" : oleLoanDocument.getItemLocation())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Call # ")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            String callNumber = "";
            if (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("")) {
                callNumber = (String) (oleLoanDocument.getItemCallNumber() != null && !oleLoanDocument.getItemCallNumber().equals("") ? oleLoanDocument.getItemCallNumber() : "");
            }
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(callNumber == null ? "" : callNumber)));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk("Item barcode")));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleLoanDocument.getItemId() == null ? "" : oleLoanDocument.getItemId())));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            document.add(pdfTable);
            paraGraph = new Paragraph();
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});

            pdfPCell = new PdfPCell(new Paragraph("Item was due"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(":"));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
            pdfTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Paragraph(new Chunk(oleLoanDocument.getLoanDueDate() == null ? "" : (sdf.format(oleLoanDocument.getLoanDueDate()).toString()))));
            pdfPCell.setBorder(pdfPCell.NO_BORDER);
            pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
            pdfTable.addCell(pdfPCell);
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