package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/24/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/shelvingSchemeMaintenance")
public class OleShelvingSchemeController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleShelvingScheme oleShelvingScheme = (OleShelvingScheme) maintenanceDocument.getDocumentDataObject();
        OleShelvingScheme oleShelvingSchemeOld = (OleShelvingScheme) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String shelvingSchemeCodeOld = oleShelvingSchemeOld.getShelvingSchemeCode();
        Map<String, String> shelvingSchemeMap = new HashMap<String, String>();
        shelvingSchemeMap.put(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CD, oleShelvingScheme.getShelvingSchemeCode());
        List<OleShelvingScheme> shelvingSchemeInDatabase = (List<OleShelvingScheme>) KRADServiceLocator.getBusinessObjectService().findMatching(OleShelvingScheme.class, shelvingSchemeMap);

        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleShelvingScheme.getShelvingSchemeCode().equals(shelvingSchemeCodeOld) || shelvingSchemeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                return getUIFModelAndView(form);

            } else {
                if (shelvingSchemeInDatabase.size() > 0) {
                    for (OleShelvingScheme shelvingSchemeObj : shelvingSchemeInDatabase) {
                        String shelvingSchemeId = shelvingSchemeObj.getShelvingSchemeId().toString();
                        //String accessMethodCode=accessMehtodObj.getAccessMethodCode();
                        if (null == oleShelvingScheme.getShelvingSchemeId() ||
                                !(oleShelvingScheme.getShelvingSchemeId().equals(shelvingSchemeId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "error.duplicate.code.shelving");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "message.route.approved");
                        return getUIFModelAndView(form);

                    }
                }

            }

        } else if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (shelvingSchemeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((shelvingSchemeInDatabase.size() > 0)) {

                for (OleShelvingScheme shelvingSchemeObj : shelvingSchemeInDatabase) {
                    String shelvingSchemeId = shelvingSchemeObj.getShelvingSchemeId().toString();
                    if (null == oleShelvingScheme.getShelvingSchemeId() ||
                            !(oleShelvingScheme.getShelvingSchemeId().equals(shelvingSchemeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "error.duplicate.code.shelving");
                        return getUIFModelAndView(form);
                    }
                }

            }
        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (shelvingSchemeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((shelvingSchemeInDatabase.size() > 0)) {

                for (OleShelvingScheme shelvingSchemeObj : shelvingSchemeInDatabase) {
                    String shelvingSchemeId = shelvingSchemeObj.getShelvingSchemeId().toString();
                    if (null == oleShelvingScheme.getShelvingSchemeId() ||
                            !(oleShelvingScheme.getShelvingSchemeId().equals(shelvingSchemeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "error.duplicate.code.shelving");
                        return getUIFModelAndView(form);
                    }
                }

            }
        }


        /* if((shelvingSchemeInDatabase.size() > 0)) {

            for(OleShelvingScheme shelvingSchemeObj : shelvingSchemeInDatabase){
                String shelvingSchemeId =  shelvingSchemeObj.getShelvingSchemeId().toString();
                if (null == oleShelvingScheme.getShelvingSchemeId()  ||
                        !(oleShelvingScheme.getShelvingSchemeId().equals(shelvingSchemeId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleShelvingScheme.SHELVING_SCHEME_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}

