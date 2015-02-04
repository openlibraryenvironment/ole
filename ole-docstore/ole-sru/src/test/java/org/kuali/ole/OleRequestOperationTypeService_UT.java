package org.kuali.ole;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.service.OleCQLQueryParserService;
import org.kuali.ole.service.OleRequestOperationTypeService;
import org.kuali.ole.service.OleSearchRetrieveOperationService;
import org.kuali.ole.serviceimpl.*;
import org.kuali.rice.core.api.config.property.Config;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRequestOperationTypeService_UT extends BaseTestCase{

    private static Logger LOG = LoggerFactory.getLogger(OleRequestOperationTypeService_UT.class);
    private OleRequestOperationTypeService oleRequestOperationTypeService=new OleRequestOperationTypeServiceImpl();
    private OleSearchRetrieveOperationService oleSearchRetrieveOperationService=new OleSearchRetrieveOperationServiceImpl();

    @Mock
    private Config mockConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockConfig.getProperty(OleSRUConstants.INVALID_QUERY_DIAGNOSTIC_MSG)).thenReturn("");
        ((OleSearchRetrieveOperationServiceImpl) oleSearchRetrieveOperationService).setCurrentContextConfig(mockConfig);
        ((OleRequestOperationTypeServiceImpl)oleRequestOperationTypeService).setOleSearchRetrieveOperationService(oleSearchRetrieveOperationService);

    }

    @Ignore
    @Test
    public void testPerformOperationTypeService() throws Exception{

        CQLResponseBO cQLResponseBO=getCQLResponseBOObject();
        String resXML=oleRequestOperationTypeService.performOperationTypeService(getReqParameters(),cQLResponseBO);
        assertNotNull(resXML);

    }

    private CQLResponseBO getCQLResponseBOObject() throws Exception{
        OleCQLQueryParserService oleCQLQueryParserService=new OleCQLQueryParserServiceImpl();
        String parserXml=oleCQLQueryParserService.parseCQLQuery("Title=HTC");
        CQLResponseBO cQLResponseBO=oleCQLQueryParserService.getCQLResponseObject(parserXml);
        return cQLResponseBO;
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
