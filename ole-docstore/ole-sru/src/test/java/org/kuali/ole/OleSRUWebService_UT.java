package org.kuali.ole;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.ole.service.OleSRUWebService;
import org.kuali.ole.serviceimpl.OleDiagnosticsServiceImpl;
import org.kuali.ole.serviceimpl.OleSRUWebServiceImpl;
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
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleSRUWebService_UT extends BaseTestCase {

    private static Logger LOG = LoggerFactory.getLogger(OleValidateInputRequestService_UT.class);
    private OleSRUWebService oleSRUWebService=new OleSRUWebServiceImpl();
    private OleDiagnosticsService OleDiagnosticsServiceImpl=new OleDiagnosticsServiceImpl();

    @Mock
    private Config mockConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockConfig.getProperty("invalid.record.schema")).thenReturn("");
        ((OleDiagnosticsServiceImpl) OleDiagnosticsServiceImpl).setCurrentContextConfig(mockConfig);
        ((OleSRUWebServiceImpl)oleSRUWebService).setOleDiagnosticsService(OleDiagnosticsServiceImpl);
        ((OleSRUWebServiceImpl) oleSRUWebService).setCurrentContextConfig(mockConfig);

    }

    @Test
    public void testInputRequestValidation() throws Exception{

        Map reqParamMap=getReqParameters();
        String respXML=oleSRUWebService.getOleSRUResponse(reqParamMap);
        assertNotNull(respXML);
    }

    public Map getReqParameters() {

        HashMap reqParamMap=new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType,OleSRUConstants.SEARCH_RETRIEVE);
        reqParamMap.put(OleSRUConstants.VERSION,"1.1");
        reqParamMap.put(OleSRUConstants.QUERY,"title=HTC");
        reqParamMap.put(OleSRUConstants.START_RECORD,"1");
        reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS,"10");
        reqParamMap.put(OleSRUConstants.RECORD_PACKING,"xml");
        reqParamMap.put(OleSRUConstants.RECORD_SCHEMA,"recordSchema");

        return reqParamMap;

    }

  }
