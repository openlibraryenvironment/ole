package org.kuali.ole.deliver.controller.drools;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by pvsubrah on 7/8/15.
 */
public class DroolsEditorControllerTest {

    @Mock
    MaintenanceDocumentForm mockForm;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRoute() throws Exception {

        DroolsEditorController droolsEditorController = new DroolsEditorController();

        droolsEditorController.route(mockForm, null, null, null);



    }
}