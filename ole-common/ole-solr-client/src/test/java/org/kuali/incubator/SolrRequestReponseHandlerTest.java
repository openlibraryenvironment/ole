package org.kuali.incubator;

import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 20/09/16.
 */
public class SolrRequestReponseHandlerTest {

    @Test
    public void testTotalRecordCount() {
        String query = "(dateUpdated:[2016-06-16T15:06:26Z TO NOW])AND(staffOnlyFlag:false)";
        SolrRequestReponseHandler solrRequestReponseHandler = new MockSolrRequestReponseHandler();
        SolrDocumentList solrDocumentList = solrRequestReponseHandler.getSolrDocumentList(query,null, null, OleNGConstants.BIB_IDENTIFIER);
        assertNotNull(solrDocumentList);
    }

    class MockSolrRequestReponseHandler extends SolrRequestReponseHandler {
        @Override
        public String getSolrUrl() {
            return "http://localhost:8080/oledocstore/bib";
        }
    }
}