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
    public SolrInputDocument getSolrInputDocument(String noticeType, String noticeContent, List<OleLoanDocument> loanDocuments) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("DocType", noticeType);
        solrInputDocument.addField("DocFormat", "Email");
        solrInputDocument.addField("noticeType", OLEConstants.OVERDUE_NOTICE);
        solrInputDocument.addField("noticeContent", noticeContent);
        String patronBarcode = loanDocuments.get(0).getOlePatron().getBarcode();
        String patronId = loanDocuments.get(0).getPatronId();
        solrInputDocument.addField("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        solrInputDocument.addField("dateSent", dateSent);
        solrInputDocument.addField("deskLocation", "TODO");
        solrInputDocument.addField("uniqueId", patronId+ dateSent.getTime());

        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            String itemBarcode = oleLoanDocument.getItemId();
            solrInputDocument.addField("itemBarcode", itemBarcode);
        }
        return solrInputDocument;
    }
}
