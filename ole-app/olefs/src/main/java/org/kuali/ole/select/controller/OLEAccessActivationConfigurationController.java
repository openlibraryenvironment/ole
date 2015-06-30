package org.kuali.ole.select.controller;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;

import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.service.OLEAccessActivationService;
import org.kuali.ole.select.service.impl.OLEAccessActivationServiceImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hemalathas on 12/22/14.
 */
@Controller
@RequestMapping(value = "/oleAccessActivationConfiguration")
public class OLEAccessActivationConfigurationController extends MaintenanceDocumentController {


    private OLEAccessActivationService oleAccessActivationService;

    public OLEAccessActivationService getOleAccessActivationService() {
        if(oleAccessActivationService == null){
            oleAccessActivationService = new OLEAccessActivationServiceImpl();
        }
        return oleAccessActivationService;
    }

    public void setOleAccessActivationService(OLEAccessActivationService oleAccessActivationService) {
        this.oleAccessActivationService = oleAccessActivationService;
    }

    @Override
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);
        OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) form.getDocument().getNewMaintainableObject().getDataObject();
        oleAccessActivationConfiguration = getOleAccessActivationService().setRoleAndPersonName(oleAccessActivationConfiguration);
        return getUIFModelAndView(form);
    }

    @Override
    public ModelAndView maintenanceCopy(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_COPY_ACTION);
        OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) form.getDocument().getNewMaintainableObject().getDataObject();
        oleAccessActivationConfiguration = getOleAccessActivationService().setRoleAndPersonName(oleAccessActivationConfiguration);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addWorkflow")
    public ModelAndView addWorkflow(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized addLine method");
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OLEAccessActivationConfiguration accessActivation = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        OLEAccessActivationWorkFlow accessWorkflow = (OLEAccessActivationWorkFlow) eventObject;
        if(StringUtils.isNotBlank(accessActivation.getSelector()) && accessActivation.getSelector().equals(OLEConstants.SELECTOR_ROLE)) {
            accessWorkflow.setPersonName(null);
            accessWorkflow.setPersonId(null);
        } else if(StringUtils.isNotBlank(accessActivation.getSelector()) && accessActivation.getSelector().equals(OLEConstants.SELECTOR_PERSON)) {
            accessWorkflow.setRoleName(null);
            accessWorkflow.setRoleId(null);
        }
        if (!validateEmptyFieldActivationWorkFlow(accessWorkflow, accessActivation.getSelector())) {
            return getUIFModelAndView(form);
        }
        if (!getOleAccessActivationService().validateAccessActivationWorkFlow(accessActivation.getAccessActivationWorkflowList(), accessWorkflow, accessActivation.getSelector())) {
            return getUIFModelAndView(form);
        }
        View view = form.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        //  accessActivation.getAccessActivationWorkflowList().add(accessWorkflow);
        return getUIFModelAndView(form);
    }



    private boolean validateEmptyFieldActivationWorkFlow(OLEAccessActivationWorkFlow accessActivationWorkFlow, String selector) {
        boolean emptyOrderNumber = false;
        boolean emptyStatus = false;
        boolean emptyRoleOrPerson = false;
        if (accessActivationWorkFlow.getOrderNo() == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_ORDER_NO);
            emptyOrderNumber = true;
        }
        if (StringUtils.isBlank(accessActivationWorkFlow.getStatus())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_STATUS);
            emptyStatus = true;
        }
        if (StringUtils.isNotBlank(selector) && selector.equals(OLEConstants.SELECTOR_ROLE) && StringUtils.isBlank(accessActivationWorkFlow.getRoleName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_ROLE_NAME);
            emptyRoleOrPerson = true;
        }
        if(StringUtils.isNotBlank(selector) && selector.equals(OLEConstants.SELECTOR_PERSON) && StringUtils.isBlank(accessActivationWorkFlow.getPersonName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_PERSON_NAME);
            emptyRoleOrPerson = true;
        }
        if ((emptyOrderNumber || emptyStatus) || emptyRoleOrPerson) {
            return false;
        }
        return true;
    }

}
