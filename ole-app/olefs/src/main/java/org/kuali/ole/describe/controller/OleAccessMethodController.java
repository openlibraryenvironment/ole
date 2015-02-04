package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleAccessMethod;
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
 * Date: 1/23/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/accessMethodMaintenance")
public class OleAccessMethodController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleAccessMethod oleAccessMethod = (OleAccessMethod) maintenanceDocument.getDocumentDataObject();
        String accessMethodName = oleAccessMethod.getAccessMethodName();
        OleAccessMethod oleAccessMethodOld = (OleAccessMethod) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String newAccessMethodCode = oleAccessMethodOld.getAccessMethodCode();
        Map<String, String> accessMethodMap = new HashMap<String, String>();
        accessMethodMap.put(OLEConstants.OleAccessMethod.ACCESS_METHOD_CD, oleAccessMethod.getAccessMethodCode());
        List<OleAccessMethod> accessMethodInDatabase = (List<OleAccessMethod>) KRADServiceLocator.getBusinessObjectService().findMatching(OleAccessMethod.class, accessMethodMap);
        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleAccessMethod.getAccessMethodCode().equals(newAccessMethodCode) || accessMethodInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                return getUIFModelAndView(form);

            } else {
                if (accessMethodInDatabase.size() > 0) {
                    for (OleAccessMethod accessMehtodObj : accessMethodInDatabase) {
                        String accessMethodId = accessMehtodObj.getAccessMethodId().toString();
                        //String accessMethodCode=accessMehtodObj.getAccessMethodCode();
                        if (null == oleAccessMethod.getAccessMethodId() ||
                                !(oleAccessMethod.getAccessMethodId().equals(accessMethodId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "error.duplicate.code.access");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                        return getUIFModelAndView(form);

                    }
                }

            }

        } else if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (accessMethodInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((accessMethodInDatabase.size() > 0)) {

                for (OleAccessMethod accessMehtodObj : accessMethodInDatabase) {
                    String accessMethodId = accessMehtodObj.getAccessMethodId().toString();
                    if (null == oleAccessMethod.getAccessMethodId() ||
                            !(oleAccessMethod.getAccessMethodId().equals(accessMethodId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "error.duplicate.code.access");
                        return getUIFModelAndView(form);
                    }
                }

            }
        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (accessMethodInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((accessMethodInDatabase.size() > 0)) {

                for (OleAccessMethod accessMehtodObj : accessMethodInDatabase) {
                    String accessMethodId = accessMehtodObj.getAccessMethodId().toString();
                    if (null == oleAccessMethod.getAccessMethodId() ||
                            !(oleAccessMethod.getAccessMethodId().equals(accessMethodId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "error.duplicate.code.access");
                        return getUIFModelAndView(form);
                    }
                }

            }
        }

        /*else if((accessMethodInDatabase.size() > 0)) {

            for(OleAccessMethod accessMehtodObj : accessMethodInDatabase){
                String accessMethodId =  accessMehtodObj.getAccessMethodId().toString();
                if (null == oleAccessMethod.getAccessMethodId()  ||
                        !(oleAccessMethod.getAccessMethodId().equals(accessMethodId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}
