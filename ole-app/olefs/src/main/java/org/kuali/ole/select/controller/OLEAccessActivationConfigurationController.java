package org.kuali.ole.select.controller;

import org.kuali.ole.OLEConstants;

import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLERoleBo;
import org.kuali.ole.select.rule.OLEAccessActivationConfigurationRule;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
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

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hemalathas on 12/22/14.
 */
@Controller
@RequestMapping(value = "/oleAccessActivationConfiguration")
public class OLEAccessActivationConfigurationController extends MaintenanceDocumentController {

    OLEAccessActivationConfiguration accessConfiguration = new OLEAccessActivationConfiguration();
    boolean isValid = true;

    @Override
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);

        OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) form.getDocument().getNewMaintainableObject().getDataObject();
        List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList = oleAccessActivationConfiguration.getAccessActivationWorkflowList();
        for(OLEAccessActivationWorkFlow oleAccessActivationWorkFlow : accessActivationWorkFlowList){
            Map<String,Object> oleBoMap = new HashMap<>();
            oleBoMap.put("id",oleAccessActivationWorkFlow.getRoleId());
            OLERoleBo oleRoleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLERoleBo.class,oleBoMap);
            oleAccessActivationWorkFlow.setRoleName(oleRoleBo.getName());
        }
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
        //accessWorkflow.setActive(true);
        if(!validateEmptyFieldActivationWorkFlow(accessWorkflow)){
            return getUIFModelAndView(form);
        }

        if(!validateDuplicateValuesForAccessActivationWorkFlow(accessActivation.getAccessActivationWorkflowList(), accessWorkflow) ){
            return getUIFModelAndView(form);
        }

        View view = form.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        //  accessActivation.getAccessActivationWorkflowList().add(accessWorkflow);


        return getUIFModelAndView(form);
    }




    private boolean validateDuplicateValuesForAccessActivationWorkFlow(List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList,OLEAccessActivationWorkFlow accessActivationWorkFlow){
        //OLEAccessActivationConfigurationRule accessActivationRule = new OLEAccessActivationConfigurationRule();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> dataSourceNameInDatabaseroleName ;
        RoleBo roleBo;
        boolean duplicateOrderNumber = false;
        boolean duplicateStatus = false;
        boolean validRole = false;
        boolean validOrderNo = false;
        for(OLEAccessActivationWorkFlow activationWorkFlow : accessActivationWorkFlowList){
            if(accessActivationWorkFlow.getOrderNo() != null){
                if(activationWorkFlow.getOrderNo() == accessActivationWorkFlow.getOrderNo()){
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                    //GlobalVariables.getMessageMap().putErrorForSectionId("create_orderNo", OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                    duplicateOrderNumber = true;
                }
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                duplicateOrderNumber = true;

            }
            if(accessActivationWorkFlow.getStatus() != null && !accessActivationWorkFlow.getStatus().isEmpty()){
                if(activationWorkFlow.getStatus().equalsIgnoreCase(accessActivationWorkFlow.getStatus())){
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_STATUS);
                    //GlobalVariables.getMessageMap().putErrorForSectionId("create_status", OLEConstants.ERROR_DUPLICATE_STATUS);
                    duplicateStatus = true;
                }
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                duplicateOrderNumber = true;
            }
        }

        if(accessActivationWorkFlow.getRoleId()!=null && accessActivationWorkFlow.getRoleName()!=null){
            criteria.put(OLEConstants.ACCESS_ROLE_ID,accessActivationWorkFlow.getRoleId());
            criteria.put(OLEConstants.ACCESS_ROLE_NAME, accessActivationWorkFlow.getRoleName());
            dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBusinessObjectService().findMatching(RoleBo.class, criteria);

            if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_ID_NAME);
                validRole= true;
            }
        }else if(accessActivationWorkFlow.getRoleId()==null && accessActivationWorkFlow.getRoleName()!=null){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_NAME,accessActivationWorkFlow.getRoleName());
            dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                roleBo = dataSourceNameInDatabaseroleName.get(0);
                accessActivationWorkFlow.setRoleId(roleBo.getId());
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_NAME);
                validRole= true;
            }
        }else if(accessActivationWorkFlow.getRoleId()!=null && accessActivationWorkFlow.getRoleName()==null){
            criteria = new HashMap<String,String>();
            criteria.put(OLEConstants.ACCESS_ROLE_ID,accessActivationWorkFlow.getRoleId());
            dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBusinessObjectService()
                    .findMatching(RoleBo.class, criteria);
            if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                roleBo = dataSourceNameInDatabaseroleName.get(0);
                accessActivationWorkFlow.setRoleName(roleBo.getName());
                validRole= false;
            }else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_INVALID_ID);
                validRole= true;
            }
        }

        if(duplicateStatus || duplicateOrderNumber || validRole)
            return false;

        return true;
    }



    private boolean validateEmptyFieldActivationWorkFlow(OLEAccessActivationWorkFlow accessActivationWorkFlow){
        boolean emptyOrderNumber = false;
        boolean emptyStatus = false;
        boolean emptyRole = false;
        if(accessActivationWorkFlow.getOrderNo() == null){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_ORDER_NO);
            emptyOrderNumber = true;
        }
        if(accessActivationWorkFlow.getStatus() == null || accessActivationWorkFlow.getStatus().isEmpty()){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_STATUS);
            emptyStatus = true;
        }
        if(accessActivationWorkFlow.getRoleName() == null || accessActivationWorkFlow.getRoleName().isEmpty()){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ACCESS_ACTIVATION, OLEConstants.ERROR_EMPTY_ROLE_NAME);
            emptyRole = true;
        }


        if(emptyOrderNumber || emptyStatus || emptyRole)
            return false;

        return true;
    }
}
