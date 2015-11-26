package org.kuali.ole.dsng.indexer;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;

import java.util.List;

/**
 * Created by SheikS on 11/26/2015.
 */
public abstract class OleDsNgIndexer  implements DocstoreConstants {

    private SolrRequestReponseHandler solrRequestReponseHandler;

    public abstract void indexDocument(Object object);

    public abstract List<SolrInputDocument> buildSolrInputDocument(Object object);

    public void commitDocumentToSolr(List<SolrInputDocument> solrInputDocuments){
        getSolrRequestReponseHandler().updateSolr(solrInputDocuments);
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }
}
