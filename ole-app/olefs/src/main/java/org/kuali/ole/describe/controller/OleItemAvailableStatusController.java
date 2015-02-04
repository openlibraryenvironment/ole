package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
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
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/itemAvailableStatusMaintenance")
public class OleItemAvailableStatusController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleItemAvailableStatus oleItemAvailableStatus = (OleItemAvailableStatus) maintenanceDocument.getDocumentDataObject();
        OleItemAvailableStatus oleItemAvailableStatusOld = (OleItemAvailableStatus) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String oleItemAvailableStatusCode = oleItemAvailableStatusOld.getItemAvailableStatusCode();
        Map<String, String> itemAvailableStatusMap = new HashMap<String, String>();
        itemAvailableStatusMap.put(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CD, oleItemAvailableStatus.getItemAvailableStatusCode());
        List<OleItemAvailableStatus> itemAvailableStatusInDatabase = (List<OleItemAvailableStatus>) KRADServiceLocator.getBusinessObjectService().findMatching(OleItemAvailableStatus.class, itemAvailableStatusMap);

        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleItemAvailableStatus.getItemAvailableStatusCode().equals(oleItemAvailableStatusOld) || itemAvailableStatusInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "message.route.approved");
                return getUIFModelAndView(form);

            } else {
                if ((itemAvailableStatusInDatabase.size() > 0)) {

                    for (OleItemAvailableStatus itemAvailableStatusObj : itemAvailableStatusInDatabase) {
                        String instanceItemTypeId = itemAvailableStatusObj.getItemAvailableStatusId().toString();
                        if (null == oleItemAvailableStatus.getItemAvailableStatusId() ||
                                !(oleItemAvailableStatus.getItemAvailableStatusId().equals(instanceItemTypeId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "error.duplicate.code.itemavailable");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "message.route.approved");
                        return getUIFModelAndView(form);
                    }

                }


            }

        } else if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (itemAvailableStatusInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((itemAvailableStatusInDatabase.size() > 0)) {

                for (OleItemAvailableStatus itemAvailableStatusObj : itemAvailableStatusInDatabase) {
                    String instanceItemTypeId = itemAvailableStatusObj.getItemAvailableStatusId().toString();
                    if (null == oleItemAvailableStatus.getItemAvailableStatusId() ||
                            !(oleItemAvailableStatus.getItemAvailableStatusId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "error.duplicate.code.itemavailable");
                        return getUIFModelAndView(form);
                    }
                }

            }
        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (itemAvailableStatusInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((itemAvailableStatusInDatabase.size() > 0)) {

                for (OleItemAvailableStatus itemAvailableStatusObj : itemAvailableStatusInDatabase) {
                    String instanceItemTypeId = itemAvailableStatusObj.getItemAvailableStatusId().toString();
                    if (null == oleItemAvailableStatus.getItemAvailableStatusId() ||
                            !(oleItemAvailableStatus.getItemAvailableStatusId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "error.duplicate.code.itemavailable");
                        return getUIFModelAndView(form);
                    }
                }

            }
        }


        /* if((itemAvailableStatusInDatabase.size() > 0)) {

            for(OleItemAvailableStatus itemAvailableStatusObj : itemAvailableStatusInDatabase){
                String instanceItemTypeId =  itemAvailableStatusObj.getItemAvailableStatusId().toString();
                if (null == oleItemAvailableStatus.getItemAvailableStatusId()  ||
                        !(oleItemAvailableStatus.getItemAvailableStatusId().equals(instanceItemTypeId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}

