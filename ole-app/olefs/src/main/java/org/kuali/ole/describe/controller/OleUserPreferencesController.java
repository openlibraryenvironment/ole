package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.MaintenanceDocumentService;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 27/12/12
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/userPreferences")
public class OleUserPreferencesController
        extends MaintenanceDocumentController {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(OleUserPreferencesController.class);

    /**
     * This method returns the instance of oleUserPreferenceMaintenanceDocumentService
     *
     * @return oleUserPreferenceMaintenanceDocumentService(MaintenanceDocumentService)
     */
    @Override
    protected MaintenanceDocumentService getMaintenanceDocumentService() {
        return GlobalResourceLoader.getService(OLEConstants.OleUserPreferences.USER_PREF_MAINTENANCE_DOC_SERVICE);
    }

}
