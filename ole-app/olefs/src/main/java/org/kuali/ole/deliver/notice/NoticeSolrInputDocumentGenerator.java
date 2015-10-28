package org.kuali.ole.deliver.notice;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 10/16/15.
 */
public class NoticeSolrInputDocumentGenerator {
    public SolrInputDocument getSolrInputDocument(Map parameterMap) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("DocType", parameterMap.get("DocType"));
        solrInputDocument.addField("DocFormat", parameterMap.get("DocFormat"));
        solrInputDocument.addField("noticeType", parameterMap.get("noticeType"));
        solrInputDocument.addField("noticeContent", parameterMap.get("noticeContent"));
        solrInputDocument.addField("patronBarcode", parameterMap.get("patronBarcode"));
        solrInputDocument.addField("dateSent", parameterMap.get("dateSent"));
        solrInputDocument.addField("uniqueId", parameterMap.get("uniqueId"));

        List<String> itemBarcodes = (List<String>) parameterMap.get("itemBarcodes");
        for (Iterator<String> iterator = itemBarcodes.iterator(); iterator.hasNext(); ) {
            String itemBarcode = iterator.next();
            solrInputDocument.addField("itemBarcode", itemBarcode);
        }
        return solrInputDocument;
    }
}
