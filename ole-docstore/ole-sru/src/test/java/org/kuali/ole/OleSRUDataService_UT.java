package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.serachRetrieve.OleSRUBibDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkHoldingsDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.service.OleCQLQueryParserService;
import org.kuali.ole.service.OleSRUDataService;
import org.kuali.ole.serviceimpl.OleCQLQueryParserServiceImpl;
import org.kuali.ole.serviceimpl.OleSRUDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDataService_UT extends BaseTestCase {

    private static Logger LOG = LoggerFactory.getLogger(OleValidateInputRequestService_UT.class);
    private OleSRUDataService oleSRUDataService=new OleSRUDataServiceImpl();
    private OleCQLQueryParserService oleCQLQueryParserService=new OleCQLQueryParserServiceImpl();
    @Test
    public void testGetBibliographicRecordsObject() throws Exception{
        CQLResponseBO cQLResponseBO=getCQLResponseBOObject();
        String solrQuery=getSolrQueryFromCQLParseBO(cQLResponseBO);
        List<OleSRUBibDocument> oleSRUBibDocumentList=oleSRUDataService.getBibRecordsIdList(getReqParameters(),solrQuery);
        if(oleSRUBibDocumentList!=null)
         assertNotNull(oleSRUBibDocumentList);
        else
         assertNull(oleSRUBibDocumentList);
    }

    private CQLResponseBO getCQLResponseBOObject() throws Exception{
        OleCQLQueryParserService oleCQLQueryParserService=new OleCQLQueryParserServiceImpl();
        String parserXml=oleCQLQueryParserService.parseCQLQuery("Title=HTC");
        CQLResponseBO cQLResponseBO=oleCQLQueryParserService.getCQLResponseObject(parserXml);
        return cQLResponseBO;
    }


    public String getSolrQueryFromCQLParseBO(CQLResponseBO cqlResponseBO){
        String solrQuery=null;
        solrQuery=oleCQLQueryParserService.getSolrQueryFromCQLBO(cqlResponseBO);
        return solrQuery;
    }

    private WorkBibDocument generateWorkBibDocumentObject(CQLResponseBO cqlResponseBO) {

        String term=cqlResponseBO.getSearchClauseTag().getTerm();
        String relation=cqlResponseBO.getSearchClauseTag().getRelationTag().getValue();
        String index=cqlResponseBO.getSearchClauseTag().getIndex();
        WorkBibDocument workBibDocument = new WorkBibDocument();
        WorkInstanceDocument workInstanceDocument = new WorkInstanceDocument();
        WorkHoldingsDocument workHoldingsDocument=new WorkHoldingsDocument();
        if("title".equalsIgnoreCase(index))
            workBibDocument.setTitle(term+"*");
        workBibDocument.setInstanceDocument(workInstanceDocument);
        return workBibDocument;
    }
    public Map getReqParameters() {

        HashMap reqParamMap=new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType,OleSRUConstants.SEARCH_RETRIEVE);
        reqParamMap.put(OleSRUConstants.VERSION,"1.1");
        reqParamMap.put(OleSRUConstants.QUERY,"title=HTC");
        reqParamMap.put(OleSRUConstants.START_RECORD,"1");
        reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS,"10");
        reqParamMap.put(OleSRUConstants.RECORD_PACKING,"xml");

        return reqParamMap;

    }
}
