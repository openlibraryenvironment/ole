package org.kuali.ole.indexer;

import org.apache.solr.common.SolrInputDocument;

import java.util.Map;

/**
 * Created by sheiks on 28/10/16.
 */
public class SolrInputDocumentAndDocumentMap {
    private SolrInputDocument solrInputDocument;
    private Map map;

    public SolrInputDocumentAndDocumentMap(SolrInputDocument solrInputDocument, Map map) {
        this.solrInputDocument = solrInputDocument;
        this.map = map;
    }

    public SolrInputDocument getSolrInputDocument() {
        return solrInputDocument;
    }

    public void setSolrInputDocument(SolrInputDocument solrInputDocument) {
        this.solrInputDocument = solrInputDocument;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
