package org.kuali.ole.service;

import org.kuali.ole.bo.cql.CQLResponseBO;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleSearchRetrieveOperationService {

    public String getSearchRetriveResponse(Map reqParamMap, CQLResponseBO cqlParseBO);


}
