package org.kuali.ole.deliver.notice.executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.common.util.CollectionUtils;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.service.ClaimsReturnedNoticesExecutor;
import org.kuali.ole.deliver.service.NoticesExecutor;
import org.kuali.ole.deliver.service.impl.CheckoutReceiptNoticeExecutor;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.*;

/**
 * Created by maheswarang on 7/6/15.
 */
public abstract class LoanNoticesExecutor extends NoticesExecutor {

    private static final Logger LOG = Logger.getLogger(LoanNoticesExecutor.class);
    protected List<OleLoanDocument> loanDocuments;
    protected String noticeContentConfigName;
    protected Map<String,String> fieldLabelMap = new HashMap<String,String>();
    protected OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private NoticeSolrInputDocumentGenerator noticeSolrInputDocumentGenerator;

    public LoanNoticesExecutor(Map loanNoticeMap) {
        this.loanDocuments = (List<OleLoanDocument>) loanNoticeMap.get(OLEConstants.LOAN_DOCUMENTS);
        this.noticeContentConfigName = (String) loanNoticeMap.get(OLEConstants.NOTICE_CONTENT_CONFIG_NAME);
    }

    @Override
    public void run() {
        LOG.info("NoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread());

        //1. Pre process
        if((this instanceof ClaimsReturnedNoticesExecutor)){
            preProcess(loanDocuments);
        }
        //2. Determine the correct NoticeConfigurationBo
        setOleNoticeContentConfigurationBo();
        //3. generate email content
        String mailContent = generateMailContent(loanDocuments);
        if(StringUtils.isNotBlank(mailContent) && !mailContent.contains("FreeMarker template error")) {
            if(!(this instanceof ClaimsReturnedNoticesExecutor)){
               preProcess(loanDocuments);
               mailContent = generateMailContent(loanDocuments);
            }
            //4. Generate notices
            List<OLEDeliverNotice> oleDeliverNotices = buildNoticesForDeletion();
            //5. Save loan document
            if(!(this instanceof ClaimsReturnedNoticesExecutor) && !(this instanceof CheckoutReceiptNoticeExecutor)){
                saveLoanDocument();
            }
            //6. Delete notices
            if(!(this instanceof CheckoutReceiptNoticeExecutor)){
                deleteNotices(oleDeliverNotices);
            }
            //7. update notice history
            saveOLEDeliverNoticeHistory(oleDeliverNotices, mailContent);
            //8. send mail
            sendMail(mailContent);
            //9. Index the mail content for solr search
            getSolrRequestReponseHandler().updateSolr(CollectionUtils.singletonList(
                    getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                            buildMapForIndexToSolr(getNoticeType(), mailContent, loanDocuments))));
            //10. Post process
            postProcess(loanDocuments);
        }
    }

    public void saveLoanDocument() {
        getBusinessObjectService().save(loanDocuments);
    }

    public NoticeSolrInputDocumentGenerator getNoticeSolrInputDocumentGenerator() {
        if (null == noticeSolrInputDocumentGenerator) {
            noticeSolrInputDocumentGenerator = new NoticeSolrInputDocumentGenerator();
        }
        return noticeSolrInputDocumentGenerator;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    protected abstract String getNoticeType();

    public void sendMail(String mailContent) {
        OlePatronDocument olePatron = loanDocuments.get(0).getOlePatron();
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                    .getEntityTypeContactInfos().get(0);
            String emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                    getPatronHomeEmailId(entityTypeContactInfoBo) : "";

            if (loanDocuments.size() == 1) {
                sendMailsToPatron(emailAddress, mailContent, loanDocuments.get(0).getItemLocation(),oleNoticeContentConfigurationBo.getNoticeSubjectLine());
            } else {
                sendMailsToPatron(emailAddress, mailContent, null,oleNoticeContentConfigurationBo.getNoticeSubjectLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map buildMapForIndexToSolr(String noticeType, String noticeContent, List<OleLoanDocument> oleLoanDocuments) {
        Map parameterMap = new HashMap();
        parameterMap.put("DocType", noticeType);
        parameterMap.put("DocFormat", "Email");
        parameterMap.put("noticeType", noticeType);
        parameterMap.put("noticeContent", noticeContent);
        String patronBarcode = oleLoanDocuments.get(0).getOlePatron().getBarcode();
        String patronId = oleLoanDocuments.get(0).getOlePatron().getOlePatronId();
        parameterMap.put("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        parameterMap.put("dateSent", dateSent);
        parameterMap.put("uniqueId", patronId + dateSent.getTime());
        List<String> itemBarcodes = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            String itemBarcode = oleLoanDocument.getItemId();
            itemBarcodes.add(itemBarcode);
        }
        parameterMap.put("itemBarcodes",itemBarcodes);
        return parameterMap;
    }

    protected abstract void postProcess(List<OleLoanDocument> loanDocuments);
    protected abstract void preProcess(List<OleLoanDocument> loanDocuments);
    public abstract List<OLEDeliverNotice> buildNoticesForDeletion();
    public abstract String generateMailContent(List<OleLoanDocument> oleLoanDocuments);
    public abstract void setOleNoticeContentConfigurationBo();
    public abstract void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo);

}
