package org.kuali.ole.serviceimpl;

import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.cql.CQLSearchClauseTag;
import org.kuali.ole.docstore.discovery.service.SRUCQLQueryService;
import org.kuali.ole.docstore.discovery.service.SRUCQLQueryServiceImpl;
import org.kuali.ole.handler.OleCQLResponseHandler;
import org.kuali.ole.service.OleCQLQueryParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLParser;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCQLQueryParserServiceImpl implements OleCQLQueryParserService {

    private static final Logger LOG = LoggerFactory.getLogger(OleCQLQueryParserServiceImpl.class);
    private StringBuffer query = new StringBuffer("");
    private boolean solrQueryFlag = true;
    private SRUCQLQueryService srucqlQueryService = null;

    public OleCQLQueryParserServiceImpl() {
        srucqlQueryService = SRUCQLQueryServiceImpl.getInstance();
    }

    /**
     * this method is use to parse the query to get the response for the query as an xml cql or proper cql query
     *
     * @param query
     * @return xml as a string
     */
    public String parseCQLQuery(String query) {

        try {
            CQLParser cql = new CQLParser();
            CQLNode node = cql.parse(query);
            LOG.info(node.toCQL());
            LOG.info(node.toXCQL());
            return node.toXCQL();

        } catch (CQLParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * this method is used to call the oleCQLResponseHandler which converts an xml to an object
     *
     * @param cqlParseXml
     * @return cQLResponseBO object
     */
    public CQLResponseBO getCQLResponseObject(String cqlParseXml) {
        CQLResponseBO cQLResponseBO = null;
        OleCQLResponseHandler oleCQLResponseHandler = new OleCQLResponseHandler();
        try {
            try {
                cQLResponseBO = oleCQLResponseHandler.fromXML(cqlParseXml);
            } catch (Exception e) {
            }
            if (cQLResponseBO == null) {
                CQLSearchClauseTag cqlSearchClauseTag = oleCQLResponseHandler.fromCQLXML(cqlParseXml);
                cQLResponseBO = new CQLResponseBO();
                cQLResponseBO.setSearchClauseTag(cqlSearchClauseTag);
            }
        } catch (Exception ex) {
        }
        return cQLResponseBO;
    }

    /**
     * this will generate the proper query
     *
     * @param cqlResponseBO
     * @return query as a string
     */
    public String getSolrQueryFromCQLBO(CQLResponseBO cqlResponseBO) {
        String solrQuery = "";
        try {
            solrQuery = getSolrQueryFromCQLBO(cqlResponseBO, true);
        } catch (Exception e) {
            LOG.error(e.getMessage() , e );
        }
        return solrQuery;
    }

    /**
     * this method will generate a proper query as like solr, from the CQLResponseBO object
     *
     * @param cqlResponseBO object
     * @param flag
     * @return proper query as a string
     * @throws Exception
     */
    public String getSolrQueryFromCQLBO(CQLResponseBO cqlResponseBO, boolean flag) throws Exception {

        String term = "";
        String relation = "";
        String index = "";
        if (cqlResponseBO.getSearchClauseTag() != null) {
            term = cqlResponseBO.getSearchClauseTag().getTerm();
            relation = cqlResponseBO.getSearchClauseTag().getRelationTag().getValue();
            index = cqlResponseBO.getSearchClauseTag().getIndex();
        }
        if (!solrQueryFlag)
            return null;

        if (cqlResponseBO.getLeftOperand() != null) {
            query.append("(");
            query.append(getSolrQueryFromCQLBO(cqlResponseBO.getLeftOperand(), false));
            query.append(")");
            if (cqlResponseBO.getTriple() != null)
                query.append(getSolrQueryFromCQLBO(cqlResponseBO.getLeftOperand(), false));
            else if (cqlResponseBO.getSearchClauseTag() != null)
                query.append(srucqlQueryService.getQuery(term, relation, index, solrQueryFlag));
        }
        if (cqlResponseBO.getTriple() != null) {
            query.append("(");
            query.append(getSolrQueryFromCQLBO(cqlResponseBO.getTriple(), false));
            query.append(")");
            if (cqlResponseBO.getSearchClauseTag() != null)
                query.append(srucqlQueryService.getQuery(term, relation, index, solrQueryFlag));
        }
        if (cqlResponseBO.getRightOperand() != null) {
            query.append(" " + cqlResponseBO.getBooleanTagValue().getValue() + " " );
            query.append("(");
            query.append(getSolrQueryFromCQLBO(cqlResponseBO.getRightOperand(), false));
            query.append(")");
            if (cqlResponseBO.getTriple() != null)
                query.append(getSolrQueryFromCQLBO(cqlResponseBO.getLeftOperand(), false));
            else if (cqlResponseBO.getSearchClauseTag() != null)
                query.append(srucqlQueryService.getQuery(term, relation, index, solrQueryFlag));
        }
        if (cqlResponseBO.getSearchClauseTag() != null) {
            String query = srucqlQueryService.getQuery(term, relation, index, solrQueryFlag);
            if (query != null)
                return query.toString();
            return null;
        }
        if (flag)
            return query.toString();
        return "";
    }


}
