package org.kuali.ole;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.ole.serviceimpl.OleDiagnosticsServiceImpl;
import org.kuali.ole.serviceimpl.OleSearchRetrieveOperationServiceImpl;
import org.kuali.rice.core.api.config.property.Config;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDiagnosticsService_UT extends BaseTestCase{

    private static Logger LOG = LoggerFactory.getLogger(OleDiagnosticsService_UT.class);

    @Mock
    private Config mockConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockConfig.getProperty(OleSRUConstants.INVALID_OperationType)).thenReturn("");
        ((OleDiagnosticsServiceImpl) oleDiagnosticsService).setCurrentContextConfig(mockConfig);

    }

    private OleDiagnosticsService oleDiagnosticsService=new OleDiagnosticsServiceImpl();

    @Test
    public void testCQLQueryParser() throws Exception{

        OleSRUDiagnostics diagnosticXMl=null;
        diagnosticXMl=oleDiagnosticsService.getDiagnosticResponse("Unsupported Request");
        assertNotNull(diagnosticXMl);
    }

}
