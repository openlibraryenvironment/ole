package org.kuali.ole.deliver.notice.executors;

import org.apache.log4j.Logger;
import org.kuali.common.util.CollectionUtils;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.service.NoticesExecutor;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.*;

/**
 * Created by maheswarang on 7/6/15.
 */
public abstract class LoanNoticesExecutor extends NoticesExecutor {

    private static final Logger LOG = Logger.getLogger(LoanNoticesExecutor.class);
    protected List<OleLoanDocument> loanDocuments;
    protected Map<String,String> fieldLabelMap = new HashMap<String,String>();
    protected OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;
    private SolrRequestReponseHandler solrRequestReponseHandler;

    public LoanNoticesExecutor(List<OleLoanDocument> loanDocuments) {
        this.loanDocuments = loanDocuments;
    }

    @Override
    public void run() {
        LOG.info("NoticesExecutor thread id---->"+Thread.currentThread().getId()+"current thread---->"+Thread.currentThread());

        //1. Pre process
        preProcess(loanDocuments);
        //2. Determine the correct NoticeConfigurationBo
        setOleNoticeContentConfigurationBo();
        //3. generate email content
        String mailContent = generateMailContent(loanDocuments);
        //4. Generate notices
        List<OLEDeliverNotice> oleDeliverNotices = buildNoticesForDeletion();
        //5. Save loan document
        getBusinessObjectService().save(loanDocuments);
        //6. Delete notices
        deleteNotices(oleDeliverNotices);
        //7. update notice history
        List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories = saveOLEDeliverNoticeHistory(oleDeliverNotices, mailContent);
        //8. send mail
        sendMail(mailContent);
        //9. Index the mail content for solr search
        getSolrRequestReponseHandler().updateSolr(CollectionUtils.singletonList(new NoticeSolrInputDocumentGenerator().getSolrInputDocument(getNoticeType(), mailContent, loanDocuments)));
        //10. Post process
        postProcess(loanDocuments);
    }

    private SolrRequestReponseHandler getSolrRequestReponseHandler() {
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
                sendMailsToPatron(emailAddress, mailContent, loanDocuments.get(0).getItemLocation());
            } else {
                sendMailsToPatron(emailAddress, mailContent, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void postProcess(List<OleLoanDocument> loanDocuments);
    protected abstract void preProcess(List<OleLoanDocument> loanDocuments);
    public abstract List<OLEDeliverNotice> buildNoticesForDeletion();
    public abstract String generateMailContent(List<OleLoanDocument> oleLoanDocuments);
    public abstract void setOleNoticeContentConfigurationBo();

}
