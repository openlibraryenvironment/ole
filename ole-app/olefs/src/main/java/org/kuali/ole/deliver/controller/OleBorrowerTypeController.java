package org.kuali.ole.deliver.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.deliver.bo.OlePatronDocument;
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
 * Date: 12/27/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/borrowerTypeMaintenance")
public class OleBorrowerTypeController extends MaintenanceDocumentController {
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleBorrowerType oleBorrowerType = (OleBorrowerType) maintenanceDocument.getDocumentDataObject();
        Map<String, String> borrowerTypeMap = new HashMap<String, String>();
        borrowerTypeMap.put(OLEConstants.OleBorrowerType.BORROWER_TYPE_CD, oleBorrowerType.getBorrowerTypeCode());
        List<OleBorrowerType> borrowerTypeInDatabase = (List<OleBorrowerType>) KRADServiceLocator.getBusinessObjectService().findMatching(OleBorrowerType.class, borrowerTypeMap);
        if (!oleBorrowerType.isActive()) {
            Map<String, String> borrowerTypeMap1 = new HashMap<String, String>();
            borrowerTypeMap1.put("borrowerType", oleBorrowerType.getBorrowerTypeId());
            List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, borrowerTypeMap1);
            if (patronDocumentList != null && patronDocumentList.size() > 0) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OleBorrowerType.BORROWER_TYPE_ACTIVE, OLEConstants.OleBorrowerType.BORROWER_TYPE_ACTIVE_ERROR);
                return getUIFModelAndView(form);
            }
        }
        if ((borrowerTypeInDatabase.size() > 0)) {

            for (OleBorrowerType borrowerObj : borrowerTypeInDatabase) {
                String borrowerTypeId = borrowerObj.getBorrowerTypeId();
                if (null == oleBorrowerType.getBorrowerTypeId() ||
                        !(oleBorrowerType.getBorrowerTypeId().equalsIgnoreCase(borrowerTypeId))) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleBorrowerType.BORROWER_TYPE_CODE, "error.duplicate.borrowerCode");
                    return getUIFModelAndView(form);
                }
            }

        }
        performWorkflowAction(form, UifConstants.WorkflowAction.BLANKETAPPROVE, true);
        return getUIFModelAndView(form);
    }
}
