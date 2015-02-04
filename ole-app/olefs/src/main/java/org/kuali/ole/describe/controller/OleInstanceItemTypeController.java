package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleInstanceItemType;
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
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/instanceItemTypeMaintenance")
public class OleInstanceItemTypeController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleInstanceItemType oleInstanceItemType = (OleInstanceItemType) maintenanceDocument.getDocumentDataObject();
        OleInstanceItemType oleInstanceItemTypeOld = (OleInstanceItemType) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String oleInstanceItemTypeOldCode = oleInstanceItemTypeOld.getInstanceItemTypeCode();
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CD, oleInstanceItemType.getInstanceItemTypeCode());
        List<OleInstanceItemType> instanceItemTypeInDatabase = (List<OleInstanceItemType>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);

        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleInstanceItemType.getInstanceItemTypeCode().equals(oleInstanceItemTypeOldCode) || instanceItemTypeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else {
                if (instanceItemTypeInDatabase.size() > 0) {
                    for (OleInstanceItemType instanceItemTypeObj : instanceItemTypeInDatabase) {
                        String instanceItemTypeId = instanceItemTypeObj.getInstanceItemTypeId().toString();
                        if (null == oleInstanceItemType.getInstanceItemTypeId() ||
                                !(oleInstanceItemType.getInstanceItemTypeId().equals(instanceItemTypeId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "error.duplicate.code.instanceitem");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "message.route.approved");
                        return getUIFModelAndView(form);
                    }

                }
            }

        }
        if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (instanceItemTypeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleAccessMethod.ACCESS_METHOD_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((instanceItemTypeInDatabase.size() > 0)) {
                for (OleInstanceItemType instanceItemTypeObj : instanceItemTypeInDatabase) {
                    String instanceItemTypeId = instanceItemTypeObj.getInstanceItemTypeId().toString();
                    if (null == oleInstanceItemType.getInstanceItemTypeId() ||
                            !(oleInstanceItemType.getInstanceItemTypeId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "error.duplicate.code.instanceitem");
                        return getUIFModelAndView(form);
                    }
                }
            }

        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (instanceItemTypeInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((instanceItemTypeInDatabase.size() > 0)) {
                for (OleInstanceItemType instanceItemTypeObj : instanceItemTypeInDatabase) {
                    String instanceItemTypeId = instanceItemTypeObj.getInstanceItemTypeId().toString();
                    if (null == oleInstanceItemType.getInstanceItemTypeId() ||
                            !(oleInstanceItemType.getInstanceItemTypeId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "error.duplicate.code.instanceitem");
                        return getUIFModelAndView(form);
                    }
                }

            }
        }


        /* if((instanceItemTypeInDatabase.size() > 0)) {

            for(OleInstanceItemType instanceItemTypeObj : instanceItemTypeInDatabase){
                String instanceItemTypeId =  instanceItemTypeObj.getInstanceItemTypeId().toString();
                if (null == oleInstanceItemType.getInstanceItemTypeId()  ||
                        !(oleInstanceItemType.getInstanceItemTypeId().equals(instanceItemTypeId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleInstanceItemType.INSTANCE_ITEM_TYPE_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}

