package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.service.impl.ExternalDSConfigMaintenanceDocumentServiceImpl;
import org.kuali.ole.service.OlePatronMaintenanceDocumentServiceImpl;
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
 * Date: 10/12/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/externalDataSourceMaintenance")
public class OleExternalDSConfigMaintenanceDocumentController
        extends MaintenanceDocumentController {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            OleExternalDSConfigMaintenanceDocumentController.class);

    /**
     * Use to create a link delete in the lookup result action field which will use to delete patron record..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "maintenanceDelete")
    public ModelAndView maintenanceDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug(" Inside maintenanceDelete ");
        setupMaintenanceForDelete(form, request, OLEConstants.OleExternalDataSourceConfig.DATA_SOURCE_DELETE);
        return getUIFModelAndView(form);
    }

    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request,
                                             String maintenanceAction) {
        LOG.debug(" Inside setupMaintenanceForDelete ");
        MaintenanceDocument document = form.getDocument();
        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }

        form.setMaintenanceAction(maintenanceAction);
        ExternalDSConfigMaintenanceDocumentServiceImpl dataSourceService
                = (ExternalDSConfigMaintenanceDocumentServiceImpl) getMaintenanceDocumentService();
        dataSourceService.setupMaintenanceObjectForDelete(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    /**
     * This method returns the instance of OleExternalDataSourceConfigMaintenanceDocumentService
     *
     * @return OleExternalDataSourceConfigMaintenanceDocumentService(MaintenanceDocumentService)
     */
    @Override
    protected MaintenanceDocumentService getMaintenanceDocumentService() {
        return GlobalResourceLoader
                .getService(OLEConstants.OleExternalDataSourceConfig.DATA_SOURCE_MAINTENANCE_DOC_SERVICE);
    }

}
