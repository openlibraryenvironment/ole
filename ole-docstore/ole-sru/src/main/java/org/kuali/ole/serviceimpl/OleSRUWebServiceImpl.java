package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.OleSRUSearchRetrieveResponse;
import org.kuali.ole.handler.OleSRUOpacXMLResponseHandler;
import org.kuali.ole.service.*;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUWebServiceImpl implements OleSRUWebService {

    public OleDiagnosticsService oleDiagnosticsService;
    public OleCQLQueryParserService oleCQLQueryParserService;
    public OleRequestOperationTypeService oleRequestOperationTypeService;
    public OleValidateInputRequestService oleValidateInputRequestService;
    private Config currentContextConfig;

    public OleSRUWebServiceImpl() {
        oleDiagnosticsService = new OleDiagnosticsServiceImpl();
        oleCQLQueryParserService = new OleCQLQueryParserServiceImpl();
        oleRequestOperationTypeService = new OleRequestOperationTypeServiceImpl();
        oleValidateInputRequestService = new OleValidateInputRequestServiceImpl();
    }

    /**
     * this is to parse the query from the url and get the object and generate the response xml .Diagnostics takes place, if any error
     *
     * @param reqParamMap
     * @return xml as a string
     */
    public String getOleSRUResponse(Map reqParamMap) {

        String respXML = null;
        String operationType = (String) reqParamMap.get(OleSRUConstants.OperationType);
        //String version=(String)reqParamMap.get(OleSRUConstants.VERSION);
        String query = (String) reqParamMap.get(OleSRUConstants.QUERY);
        CQLResponseBO cqlParseBO = null;
        String reqValidation = oleValidateInputRequestService.inputRequestValidation(reqParamMap);
        if (reqValidation != null) {
            OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(getCurrentContextConfig().getProperty(reqValidation));
            OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
            oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
            oleSRUSearchRetrieveResponse.setNumberOfRecords(new Long(0));
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
            return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get("recordSchema"));
        }
        if (OleSRUConstants.SEARCH_RETRIEVE.equalsIgnoreCase(operationType)) {
            String cqlParseXml = oleCQLQueryParserService.parseCQLQuery(query);
            if (cqlParseXml == null) {
                OleSRUDiagnostics oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_QUERY_DIAGNOSTIC_MSG));
                OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
                oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
                oleSRUSearchRetrieveResponse.setNumberOfRecords(new Long(0));
                oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
                OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
                return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get("recordSchema"));

            }
            cqlParseBO = oleCQLQueryParserService.getCQLResponseObject(cqlParseXml);
        }
        respXML = oleRequestOperationTypeService.performOperationTypeService(reqParamMap, cqlParseBO);
        return respXML;
    }

    private Config getCurrentContextConfig() {
        if (null == currentContextConfig) {
            currentContextConfig = ConfigContext.getCurrentContextConfig();
        }
        return currentContextConfig;
    }

    public void setCurrentContextConfig(Config currentContextConfig) {
        this.currentContextConfig = currentContextConfig;
    }

    public void setOleDiagnosticsService(OleDiagnosticsService oleDiagnosticsService) {
        this.oleDiagnosticsService = oleDiagnosticsService;
    }
}
