package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.service.OleExplainOperationService;
import org.kuali.ole.service.OleRequestOperationTypeService;
import org.kuali.ole.service.OleSearchRetrieveOperationService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRequestOperationTypeServiceImpl implements OleRequestOperationTypeService {

    public OleSearchRetrieveOperationService oleSearchRetrieveOperationService;
    public OleExplainOperationService oleExplainOperationService;

    public OleRequestOperationTypeServiceImpl() {
        oleSearchRetrieveOperationService = new OleSearchRetrieveOperationServiceImpl();
        oleExplainOperationService = new OleExplainOperationServiceImpl();
    }

    /**
     * this method will route the operation type whether it is searchRetrieve or explain service
     *
     * @param reqParamMap
     * @param cqlParseBO
     * @return response xml as a string
     */
    public String performOperationTypeService(Map reqParamMap, CQLResponseBO cqlParseBO) {
        String responseXML = null;
        String operationType = (String) reqParamMap.get(OleSRUConstants.OperationType);
        if (OleSRUConstants.SEARCH_RETRIEVE.equals(operationType))
            responseXML = oleSearchRetrieveOperationService.getSearchRetriveResponse(reqParamMap, cqlParseBO);
        else if (OleSRUConstants.EXPLAIN.equals(operationType))
            responseXML = oleExplainOperationService.getExplainResponse(reqParamMap);

        return responseXML;
    }

    public void setOleSearchRetrieveOperationService(OleSearchRetrieveOperationService oleSearchRetrieveOperationService) {
        this.oleSearchRetrieveOperationService = oleSearchRetrieveOperationService;
    }

    public OleSearchRetrieveOperationService getOleSearchRetrieveOperationService() {
        return oleSearchRetrieveOperationService;
    }
}
