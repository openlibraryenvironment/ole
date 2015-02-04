package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.api.OlePatronDefintionHelper;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.OleMyAccountController;
import org.kuali.ole.deliver.form.OleMyAccountForm;
import org.kuali.ole.service.OlePatronService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
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
@Transactional
public class OleMyAccountController_IT extends SpringBaseTestCase{

    @Mock
    private OleMyAccountForm mockUifFormBase;

    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView mockModelView;

    public  MockOleMyAccountController mockOleMyAccountController;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockOleMyAccountController = new MockOleMyAccountController();

    }

    @Test
    @Transactional
    public void testMyAccountPatronSearch() throws Exception {
        OlePatronService olePatronService = GlobalResourceLoader.getService(OLEConstants.OlePatron.OLE_PATRON_SERVICE);
        /*OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDefinition patron = olePatronService.createPatron(olePatronDefinition);*/
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDocument updatedPatron = OlePatronDocument.from(olePatronDefinition);

        assertNotNull(updatedPatron);
        assertNotNull(updatedPatron.getOlePatronId());

        /*OlePatronDocument updatedPatron = OlePatronDocument.from(patron);*/
        updatedPatron=new OLEPatronHelper().changeId(updatedPatron);
        Mockito.when(mockUifFormBase.getPatronBarcode()).thenReturn((updatedPatron.getOlePatronId()));
        Mockito.when(mockUifFormBase.getOlePatronDocument()).thenReturn((updatedPatron));
        ModelAndView modelAndView =
                mockOleMyAccountController.myAccountPatronSearch(mockUifFormBase, mockBindingResult, mockRequest, mockResponse);
        assertNotNull(modelAndView);
    }

    @Test
    @Transactional
    public void testSavePatron() throws Exception {
        /*OlePatronService olePatronService = GlobalResourceLoader.getService(OLEConstants.OlePatron.OLE_PATRON_SERVICE);
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDefinition patron = olePatronService.createPatron(olePatronDefinition);
        assertNotNull(patron);
        assertNotNull(patron.getOlePatronId());

        OlePatronDocument updatedPatron = OlePatronDocument.from(patron);*/
        OlePatronDefinition olePatronDefinition = OlePatronDefintionHelper.create();
        OlePatronDocument updatedPatron = OlePatronDocument.from(olePatronDefinition);
        assertNotNull(updatedPatron);
        assertNotNull(updatedPatron.getOlePatronId());
        updatedPatron=new OLEPatronHelper().changeId(updatedPatron);
        Mockito.when(mockUifFormBase.getBarcode()).thenReturn((updatedPatron.getBarcode()));
        Mockito.when(mockUifFormBase.getOlePatronDocument()).thenReturn((updatedPatron));
        ModelAndView modelAndView =
                mockOleMyAccountController.savePatron(mockUifFormBase, mockBindingResult, mockRequest, mockResponse);
        assertNotNull(modelAndView);
    }


    private class MockOleMyAccountController extends OleMyAccountController {

    }
}
