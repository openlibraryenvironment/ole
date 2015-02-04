package org.kuali.ole;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.service.OleExplainOperationService;
import org.kuali.ole.serviceimpl.OleDiagnosticsServiceImpl;
import org.kuali.ole.serviceimpl.OleExplainOperationServiceImpl;
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
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class OleExplainOperationService_UT extends BaseTestCase{

    private static Logger LOG = LoggerFactory.getLogger(OleExplainOperationService_UT.class);
    private OleExplainOperationService oleExplainOperationServiceService = new OleExplainOperationServiceImpl();

    @Mock
    private Config mockConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockConfig.getProperty(OleSRUConstants.STARTRECORD)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.MAXRECORD)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_DATABASE_INFO)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_HOST)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_METHOD)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_PORT)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_PROTOCOL)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_TRANSPORT)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_SERVER_VERSION)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_DATABASE_LANG)).thenReturn("5");
        Mockito.when(mockConfig.getProperty(OleSRUConstants.EXPLAIN_DATABASE_PRIMARY)).thenReturn("5");
        ((OleExplainOperationServiceImpl) oleExplainOperationServiceService).setCurrentContextConfig(mockConfig);

    }

    @Test
    public void testExplainResponse() throws Exception{

        String explainXML = null;
        explainXML = oleExplainOperationServiceService.getExplainResponse(getReqParameters());
        assertNotNull(explainXML);
    }
    public Map getReqParameters() {

        HashMap reqParamMap=new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType,OleSRUConstants.SEARCH_RETRIEVE);
        reqParamMap.put(OleSRUConstants.VERSION,"1.1");
        reqParamMap.put(OleSRUConstants.RECORD_PACKING,"xml");
        return reqParamMap;

    }
}
