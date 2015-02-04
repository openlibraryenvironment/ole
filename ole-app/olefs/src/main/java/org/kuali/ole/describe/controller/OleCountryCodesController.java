package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleCountryCodes;
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
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/countryCodesMaintenance")
public class OleCountryCodesController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleCountryCodes oleCountryCodes = (OleCountryCodes) maintenanceDocument.getDocumentDataObject();
        OleCountryCodes oleCountryCodesOld = (OleCountryCodes) maintenanceDocument.getOldMaintainableObject().getDataObject();
        String oleCountryCodeOldCode = oleCountryCodesOld.getCountryCode();
        Map<String, String> countryCodesMap = new HashMap<String, String>();
        countryCodesMap.put(OLEConstants.OleCountryCodes.COUNTRY_CD, oleCountryCodes.getCountryCode());
        List<OleCountryCodes> countryCodesInDatabase = (List<OleCountryCodes>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCountryCodes.class, countryCodesMap);
        if ("Edit".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            Document document = form.getDocument();
            String successMessageKey = null;
            if (oleCountryCodes.getCountryCode().equals(oleCountryCodeOldCode) || countryCodesInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleCountryCodes.COUNTRY_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else {
                if (countryCodesInDatabase.size() > 0) {
                    for (OleCountryCodes countryCodesObj : countryCodesInDatabase) {
                        String countryCodeTypeId = countryCodesObj.getCountryCodeId().toString();
                        String countryCodeCode = countryCodesObj.getCountryCode();
                        if (null == oleCountryCodes.getCountryCodeId() ||
                                !(oleCountryCodes.getCountryCodeId().equals(countryCodeTypeId))) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OleCountryCodes.COUNTRY_CODE, "error.duplicate.code.country");
                            return getUIFModelAndView(form);
                        }
                        getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                                form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OleCountryCodes.COUNTRY_CODE, "message.route.approved");
                        return getUIFModelAndView(form);


                    }
                }
            }
        } else if ("Copy".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if (countryCodesInDatabase.size() == 0) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleCountryCodes.COUNTRY_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((countryCodesInDatabase.size() > 0)) {
                for (OleCountryCodes countryCodesObj : countryCodesInDatabase) {
                    String instanceItemTypeId = countryCodesObj.getCountryCodeId().toString();
                    if (null == oleCountryCodes.getCountryCodeId() ||
                            !(oleCountryCodes.getCountryCodeId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleCountryCodes.COUNTRY_CODE, "error.duplicate.code.country");
                        return getUIFModelAndView(form);
                    }
                }
            }

        } else if ("New".equals(maintenanceDocument.getNewMaintainableObject().getMaintenanceAction())) {
            String successMessageKey = null;
            Document document = form.getDocument();
            if ((countryCodesInDatabase.size() == 0)) {
                getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                        form));
                successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OleCountryCodes.COUNTRY_CODE, "message.route.approved");
                return getUIFModelAndView(form);
            } else if ((countryCodesInDatabase.size() > 0)) {
                for (OleCountryCodes countryCodesObj : countryCodesInDatabase) {
                    String instanceItemTypeId = countryCodesObj.getCountryCodeId().toString();
                    if (null == oleCountryCodes.getCountryCodeId() ||
                            !(oleCountryCodes.getCountryCodeId().equals(instanceItemTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleCountryCodes.COUNTRY_CODE, "error.duplicate.code.country");
                        return getUIFModelAndView(form);
                    }
                }
            }
        }

        /*else if((countryCodesInDatabase.size() > 0)) {

            for(OleCountryCodes countryCodesObj : countryCodesInDatabase){
                String instanceItemTypeId =  countryCodesObj.getCountryCodeId().toString();
                if (null == oleCountryCodes.getCountryCodeId()  ||
                        !(oleCountryCodes.getCountryCodeId().equals(instanceItemTypeId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleCountryCodes.COUNTRY_CODE, "error.duplicate.code");
                    return getUIFModelAndView(form);
                }
            }

        }*/
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return returnToPrevious(form);
    }
}

