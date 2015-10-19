package org.kuali.ole.deliver.notice;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 10/16/15.
 */
public class NoticeSolrInputDocumentGenerator {
    public SolrInputDocument getSolrInputDocument(String noticeType, List<OleLoanDocument> loanDocuments) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("DocType", noticeType);
        solrInputDocument.addField("DocFormat", "Email");
        solrInputDocument.addField("noticeType", OLEConstants.NOTICE_OVERDUE);
        String patronBarcode = loanDocuments.get(0).getPatronBarcode();
        String patronId = loanDocuments.get(0).getPatronId();
        solrInputDocument.addField("patronBarcode", patronBarcode);
        solrInputDocument.addField("patronId", patronId);
        solrInputDocument.addField("dateSent", new Date());
        solrInputDocument.addField("deskLocation", "TODO");
        solrInputDocument.addField("uniqueId", patronId);

        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            String itemBarcode = oleLoanDocument.getItemId();
            solrInputDocument.addField("itemBarcode", itemBarcode);
        }
        return solrInputDocument;
    }
}
