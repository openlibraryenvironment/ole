package org.kuali.ole.deliver.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronDefintionHelper;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.service.OlePatronService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasan.e
 * Date: 5/31/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronMaintenanceDocumentController_IT extends SpringBaseTestCase {

    @Mock
    private MaintenanceDocumentForm mockUifFormBase;

    @Mock
    private MaintenanceDocument mockUifDocumentBase;

    @Mock
    private Maintainable mockUifMaintainable;

    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView mockModelView;

    public  MockOlePatronMaintenanceDocumentController mockOlePatronMaintenanceDocumentController;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockOlePatronMaintenanceDocumentController = new MockOlePatronMaintenanceDocumentController();

    }

    @Test
    @Transactional
    public void testPatronRouteDocument() throws Exception {
        OlePatronService olePatronService = GlobalResourceLoader.getService(OLEConstants.OlePatron.OLE_PATRON_SERVICE);
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDefinition patron = olePatronService.createPatron(olePatronDefinition);
        assertNotNull(patron);
        assertNotNull(patron.getOlePatronId());

        OlePatronDocument updatedPatron = OlePatronDocument.from(patron);
        Mockito.when(mockUifFormBase.getDocument()).thenReturn((mockUifDocumentBase));
        Mockito.when(mockUifFormBase.getDocument().getNewMaintainableObject()).thenReturn((mockUifMaintainable));
        Mockito.when(mockUifFormBase.getDocument().getNewMaintainableObject().getDataObject()).thenReturn((updatedPatron));
        ModelAndView modelAndView =
                mockOlePatronMaintenanceDocumentController.route(mockUifFormBase, mockBindingResult, mockRequest, mockResponse);
        assertNotNull(modelAndView);
    }

    private class MockOlePatronMaintenanceDocumentController extends OlePatronMaintenanceDocumentController {

    }
}
