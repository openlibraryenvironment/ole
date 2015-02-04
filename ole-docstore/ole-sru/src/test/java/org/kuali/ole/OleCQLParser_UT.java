package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.cql.CQLSearchClauseTag;
import org.kuali.ole.service.OleCQLQueryParserService;
import org.kuali.ole.serviceimpl.OleCQLQueryParserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/10/12
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCQLParser_UT extends BaseTestCase{
    private static Logger LOG = LoggerFactory.getLogger(OleCQLParser_UT.class);
    private OleCQLQueryParserService oleCQLQueryParserService=new OleCQLQueryParserServiceImpl();
    private boolean solrQueryFlag=true;

    @Test
    public void testCQLQueryParser() throws Exception{

        String parserXml=null;
        parserXml=oleCQLQueryParserService.parseCQLQuery("subject any \"fish frog\"");
        assertNotNull(parserXml);

    }
    @Test
    public void testCQLResponseObject() throws Exception{


        String parserXml=oleCQLQueryParserService.parseCQLQuery("(Title=(jon and roy) or Title=james)");
        CQLResponseBO cQLResponseBO=oleCQLQueryParserService.getCQLResponseObject(parserXml);
        String solrQuery=oleCQLQueryParserService.getSolrQueryFromCQLBO(cQLResponseBO);
        assertNotNull(solrQuery);

    }



}
