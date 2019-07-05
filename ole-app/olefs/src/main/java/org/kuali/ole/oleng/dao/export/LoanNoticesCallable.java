package org.kuali.ole.oleng.dao.export;

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
import org.kuali.ole.docstore.common.response.OleNGBatchNoticeResponse;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by maheswarang on 7/6/15.
 */
public abstract class LoanNoticesCallable extends NoticeCallable {

    private static final Logger LOG = Logger.getLogger(LoanNoticesCallable.class);
    protected List<OleLoanDocument> loanDocuments;
    protected String noticeContentConfigName;
    protected OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private NoticeSolrInputDocumentGenerator noticeSolrInputDocumentGenerator;
    protected OleNGBatchNoticeResponse oleNGBatchNoticeResponse;
    private DeliverNoticeHandler deliverNoticeHandler;

    public LoanNoticesCallable(Map loanNoticeMap) {
        this.loanDocuments = (List<OleLoanDocument>) loanNoticeMap.get(OLEConstants.LOAN_DOCUMENTS);
        this.noticeContentConfigName = (String) loanNoticeMap.get(OLEConstants.NOTICE_CONTENT_CONFIG_NAME);
        this.deliverNoticeHandler = (DeliverNoticeHandler) loanNoticeMap.get("DeliverNoticeHandler");
    }

    @Override
    public Object call() throws Exception {
        LOG.info("NoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread());

        try{
            //1. Pre process
           // preProcess(loanDocuments);
            //2. Determine the correct NoticeConfigurationBo
            setOleNoticeContentConfigurationBo();
            //3. generate email content
            String mailContent = generateMailContent(loanDocuments);
            if (StringUtils.isNotBlank(mailContent) && !mailContent.contains("FreeMarker template error")) {
                preProcess(loanDocuments);
                //4. Generate notices
                List<OLEDeliverNotice> oleDeliverNotices = buildNoticesForDeletion();
                //5. Save loan document
                saveLoanDocument();

                if(this instanceof LostNoticeCallable) {
                    mailContent = generateMailContent(loanDocuments);
                }

                //6. Delete notices
                deleteNotices(oleDeliverNotices);

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

                //11. Prepare Success Response
                prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), true);

            } else {
                // Prepare Failure Response
                prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), false);
            }
        }catch (Exception e){
            e.printStackTrace();
            prepareNoticeResponses(loanDocuments,getNoticeType(),getSendToDate(deliverNoticeHandler.getNoticeTypeSendDate(getNoticeType())), false);
        }
        return oleNGBatchNoticeResponse;
    }

    public abstract void saveLoanDocument();

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

    private void prepareNoticeResponses(List<OleLoanDocument> oleLoanDocuments, String noticeType , Timestamp noticeToBeSendDate, boolean isNoticeSent){
        oleNGBatchNoticeResponse = new OleNGBatchNoticeResponse();
        int noticeCount = deliverNoticeHandler.getTotalNoticesCount(oleLoanDocuments,noticeType,noticeToBeSendDate);
        if(isNoticeSent){
            oleNGBatchNoticeResponse.setNoOfSuccessNotice(noticeCount);
        }else{
            oleNGBatchNoticeResponse.setNoOfFailureNotice(noticeCount);
            oleNGBatchNoticeResponse.setFailureLoanAndNoticeResponses(deliverNoticeHandler.getFailureLoanAndNoticeResponses(oleLoanDocuments,noticeType,noticeToBeSendDate));
        }
    }

    protected abstract void postProcess(List<OleLoanDocument> loanDocuments);
    protected abstract void preProcess(List<OleLoanDocument> loanDocuments);
    public abstract List<OLEDeliverNotice> buildNoticesForDeletion();
    public abstract String generateMailContent(List<OleLoanDocument> oleLoanDocuments);
    public abstract void setOleNoticeContentConfigurationBo();
    public abstract void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo);

}
