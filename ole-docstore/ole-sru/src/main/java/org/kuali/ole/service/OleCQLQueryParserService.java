package org.kuali.ole.service;

import org.kuali.ole.bo.cql.CQLResponseBO;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleCQLQueryParserService {

    public String parseCQLQuery(String query);

    public CQLResponseBO getCQLResponseObject(String cqlParseXml);

    public String getSolrQueryFromCQLBO(CQLResponseBO cqlResponseBO);
}
