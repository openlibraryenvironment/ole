package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
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
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/statisticalSearchingCodesMaintenance")
public class OleStatisticalSearchingCodesController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleStatisticalSearchingCodes oleStatisticalSearchingCodes = (OleStatisticalSearchingCodes) maintenanceDocument.getDocumentDataObject();
        OleStatisticalSearchingCodes oleStatisticalSearchingOld = (OleStatisticalSearchingCodes) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String statisticalSearchingCodeOld = oleStatisticalSearchingOld.getStatisticalSearchingCode();
        Map<String, String> statisticalSearchingCodesMap = new HashMap<String, String>();
        statisticalSearchingCodesMap.put(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CD, oleStatisticalSearchingCodes.getStatisticalSearchingCode());
        List<OleStatisticalSearchingCodes> statisticalSearchingCodesInDatabase = (List<OleStatisticalSearchingCodes>) KRADServiceLocator.getBusinessObjectService().findMatching(OleStatisticalSearchingCodes.class, statisticalSearchingCodesMap);
        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleStatisticalSearchingCodes.getStatisticalSearchingCode().equals(statisticalSearchingCodeOld) || statisticalSearchingCodesInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "message.route.approved");
                return getUIFModelAndView(form);

            } else {
                if ((statisticalSearchingCodesInDatabase.size() > 0)) {
                    for (OleStatisticalSearchingCodes statisticalSearchingCodesOjb : statisticalSearchingCodesInDatabase) {
                        String statisticalSearchingCodesId = statisticalSearchingCodesOjb.getStatisticalSearchingCodeId().toString();
                        if (null == oleStatisticalSearchingCodes.getStatisticalSearchingCodeId() ||
                                !(oleStatisticalSearchingCodes.getStatisticalSearchingCodeId().equals(statisticalSearchingCodesId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "error.duplicate.code.statistical");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "message.route.approved");
                        return getUIFModelAndView(form);
                    }
                }
            }
        } else if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (statisticalSearchingCodesInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((statisticalSearchingCodesInDatabase.size() > 0)) {

                for (OleStatisticalSearchingCodes statisticalSearchingCodesOjb : statisticalSearchingCodesInDatabase) {
                    String statisticalSearchingCodesId = statisticalSearchingCodesOjb.getStatisticalSearchingCodeId().toString();
                    if (null == oleStatisticalSearchingCodes.getStatisticalSearchingCodeId() ||
                            !(oleStatisticalSearchingCodes.getStatisticalSearchingCodeId().equals(statisticalSearchingCodesId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "error.duplicate.code.statistical");
                        return getUIFModelAndView(form);
                    }
                }

            }
        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (statisticalSearchingCodesInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((statisticalSearchingCodesInDatabase.size() > 0)) {

                for (OleStatisticalSearchingCodes statisticalSearchingCodesOjb : statisticalSearchingCodesInDatabase) {
                    String statisticalSearchingCodesId = statisticalSearchingCodesOjb.getStatisticalSearchingCodeId().toString();
                    if (null == oleStatisticalSearchingCodes.getStatisticalSearchingCodeId() ||
                            !(oleStatisticalSearchingCodes.getStatisticalSearchingCodeId().equals(statisticalSearchingCodesId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "error.duplicate.code.statistical");
                        return getUIFModelAndView(form);
                    }
                }

            }
        }


        /* if((statisticalSearchingCodesInDatabase.size() > 0)) {

            for(OleStatisticalSearchingCodes statisticalSearchingCodesOjb : statisticalSearchingCodesInDatabase){
                String statisticalSearchingCodesId =  statisticalSearchingCodesOjb.getStatisticalSearchingCodeId().toString();
                if (null == oleStatisticalSearchingCodes.getStatisticalSearchingCodeId()  ||
                        !(oleStatisticalSearchingCodes.getStatisticalSearchingCodeId().equals(statisticalSearchingCodesId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleStatisticalSearchingCodes.STATISTICAL_SEARCHING_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}

