package org.kuali.ole.deliver.notice.solr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/20/15.
 */
public class OleNoticeContentReIndexer {

    private SolrRequestReponseHandler solrRequestReponseHandler;

    public UpdateResponse reindexNoticeContent(){
        UpdateResponse updateResponse = null;
        List<OLEDeliverNoticeHistory> deliverNoticeHistories = (List<OLEDeliverNoticeHistory>) KRADServiceLocator.getBusinessObjectService().findAll(OLEDeliverNoticeHistory.class);
        if(CollectionUtils.isNotEmpty(deliverNoticeHistories)){
            List<SolrInputDocument> solrInputDocuments = buildSolrInputDocuments(deliverNoticeHistories);
            if(CollectionUtils.isNotEmpty(solrInputDocuments)){
                updateResponse = getSolrRequestReponseHandler().updateSolr(solrInputDocuments);
            }
        }
        return updateResponse;
    }

    private List<SolrInputDocument> buildSolrInputDocuments(List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(oleDeliverNoticeHistories)){
            for (Iterator<OLEDeliverNoticeHistory> iterator = oleDeliverNoticeHistories.iterator(); iterator.hasNext(); ) {
                OLEDeliverNoticeHistory oleDeliverNoticeHistory = iterator.next();
                byte[] noticeContent = oleDeliverNoticeHistory.getNoticeContent();
                String patronId = oleDeliverNoticeHistory.getPatronId();
                OlePatronDocument patronDocument = getPatronById(patronId);
                if (null != noticeContent && null != patronDocument) {
                    SolrInputDocument solrInputDocument = new SolrInputDocument();
                    solrInputDocument.addField("DocType", oleDeliverNoticeHistory.getNoticeType());
                    solrInputDocument.addField("DocFormat", "Email");
                    solrInputDocument.addField("noticeType", OLEConstants.NOTICE_OVERDUE);
                    solrInputDocument.addField("noticeContent", new String(noticeContent));
                    solrInputDocument.addField("patronBarcode", patronDocument.getBarcode());
                    Date dateSent = oleDeliverNoticeHistory.getNoticeSentDate();
                    solrInputDocument.addField("dateSent", dateSent);
                    solrInputDocument.addField("deskLocation", "TODO");
                    solrInputDocument.addField("uniqueId", patronId+ dateSent.getTime());
                    solrInputDocuments.add(solrInputDocument);
                }
            }
        }
        return solrInputDocuments;
    }

    public OlePatronDocument getPatronById(String patronId) {
        Map patronMap = new HashMap();
        patronMap.put("olePatronId", patronId);
        return KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
    }

    private SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }
}
