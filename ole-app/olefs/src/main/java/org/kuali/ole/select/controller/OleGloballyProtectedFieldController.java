package org.kuali.ole.select.controller;

import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.ole.service.OleGloballyProtectedFieldService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
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
 * OleBibProtectedFieldController is the controller class for Bib Protected Field Maintenance Document.
 */
@Controller
@RequestMapping(value = "/oleGloballyProtectedFieldMaintenance")
public class OleGloballyProtectedFieldController extends MaintenanceDocumentController {


    @Override
    protected OleGloballyProtectedFieldService getMaintenanceDocumentService() {
        return GlobalResourceLoader.getService("oleGloballyProtectedFieldService");
    }

    /**
     * This method invokes setupMaintenanceForDelete method to populate the document for deleting.
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
        setupMaintenanceForDelete(form, request, "Delete");
        return getUIFModelAndView(form);
    }

    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        MaintenanceDocument document = form.getDocument();
        OleGloballyProtectedFieldService maintenanceDocumentService = getMaintenanceDocumentService();
        if (document == null) {
            document = maintenanceDocumentService.setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                    maintenanceAction);
            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }

        form.setMaintenanceAction(maintenanceAction);
        maintenanceDocumentService.setupMaintenanceObjectForDelete(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    /**
     * This method invokes deleteAttachment method to delete attachment and set the error accordingly ..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "deleteDocument")
    public ModelAndView deleteDocument(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocument document = form.getDocument();
        if (document.getDocumentDataObject() != null) {
            OleGloballyProtectedField oleGloballyProtectedField = (OleGloballyProtectedField) document.getDocumentDataObject();
            KRADServiceLocator.getBusinessObjectService().delete(oleGloballyProtectedField);
        }
        return back(form, result, request, response);
    }

}
