package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrDocument;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/3/2015.
 */
@Service("oleDsNGSearchUtil")
public class OleDsNGSearchUtil implements DocstoreConstants{

    @Autowired
    BibDAO bibDAO;

    private SolrRequestReponseHandler solrRequestReponseHandler;

    public BibRecord retrieveBibBasedOnMatchPoints(String query) {
        SolrDocument solrDocument = getSolrRequestReponseHandler().retriveResultFromSolr(query);
        if(null != solrDocument) {
            String bibId = (String) solrDocument.get(LOCALID_DISPLAY);
            BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
            return bibRecord;
        }
        return null;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }
}
;