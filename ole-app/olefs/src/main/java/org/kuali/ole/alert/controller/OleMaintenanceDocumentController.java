package org.kuali.ole.alert.controller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.ole.alert.document.OleMaintenanceDocumentBase;
import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.alert.service.impl.AlertHelperServiceImpl;
import org.kuali.ole.alert.service.impl.AlertServiceImpl;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.role.service.RoleService;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.bo.*;
import org.kuali.rice.krad.exception.UnknownDocumentIdException;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;

/**
 * Created by maheswarang on 12/18/14.
 */
@Controller
@RequestMapping(value = "/oleMaintenance")
public class OleMaintenanceDocumentController extends MaintenanceDocumentController {
    private AlertServiceImpl alertService = new AlertServiceImpl();
    private GroupService groupService = KimApiServiceLocator.getGroupService();
    private AlertHelperServiceImpl alertHelperService = new AlertHelperServiceImpl();
   private org.kuali.rice.kim.api.role.RoleService roleService = KimApiServiceLocator.getRoleService();
    @Override
    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

        // TODO getting double view if we call base, not sure how to handle
        // so pasting in superclass code
        // super.docHandler(formBase, request, response);
        // * begin copy/paste from the base
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) formBase;

        // in all of the following cases we want to load the document
        if (ArrayUtils.contains(DOCUMENT_LOAD_COMMANDS, form.getCommand()) && form.getDocId() != null) {
            try {
                loadDocument(form);
                OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)form.getDocument().getNewMaintainableObject().getDataObject();
                olePersistableBusinessObjectBase.setAlertBoList(alertService.retrieveAlertList(form.getDocument().getDocumentNumber()));
            } catch (UnknownDocumentIdException udie) {
                ConfigurationService kualiConfigurationService = CoreApiServiceLocator.getKualiConfigurationService();
                StringBuffer sb = new StringBuffer();
                sb.append(kualiConfigurationService.getPropertyValueAsString(KRADConstants.KRAD_URL_KEY));
                sb.append(kualiConfigurationService.getPropertyValueAsString(KRADConstants.KRAD_INITIATED_DOCUMENT_URL_KEY));
                Properties props = new Properties();
                props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.START);
                GlobalVariables.getUifFormManager().removeSessionForm(form); // removeForm(form);
                return performRedirect(new InitiatedDocumentInfoForm(), sb.toString(), props);
            }
        } else if (KewApiConstants.INITIATE_COMMAND.equals(form.getCommand())) {
            createDocument(form);
        } else {
            LOG.error("docHandler called with invalid parameters");
            throw new IllegalArgumentException("docHandler called with invalid parameters");
        }
        // * end copy/paste from the base

        if (KewApiConstants.ACTIONLIST_COMMAND.equals(form.getCommand()) ||
                KewApiConstants.DOCSEARCH_COMMAND.equals(form.getCommand()) ||
                KewApiConstants.SUPERUSER_COMMAND.equals(form.getCommand()) ||
                KewApiConstants.HELPDESK_ACTIONLIST_COMMAND.equals(form.getCommand()) && form.getDocId() != null) {
            // TODO: set state in view
            // form.setReadOnly(true);
            form.setMaintenanceAction((form.getDocument()).getNewMaintainableObject().getMaintenanceAction());

            // Retrieving the FileName from BO table
            Maintainable tmpMaintainable = form.getDocument().getNewMaintainableObject();
            if (tmpMaintainable.getDataObject() instanceof PersistableAttachment) {
                PersistableAttachment bo = (PersistableAttachment) getBusinessObjectService()
                        .retrieve((PersistableBusinessObject) tmpMaintainable.getDataObject());
                if (bo != null) {
                    request.setAttribute("fileName", bo.getFileName());
                }
            }
        } else if (KewApiConstants.INITIATE_COMMAND.equals(form.getCommand())) {
            // form.setReadOnly(false);
            setupMaintenance(form, request, KRADConstants.MAINTENANCE_NEW_ACTION);
        } else {
            LOG.error("We should never have gotten to here");
            throw new IllegalArgumentException("docHandler called with invalid parameters");
        }

        return getUIFModelAndView(form);
    }


    /**
     * This method is to save alert
     *
     * @param formBase
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveAlert")
    public ModelAndView saveAlert(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)formBase;
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(maintenanceDocumentForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        olePersistableBusinessObjectBase.getAlertBoList().get(index).setEditFlag(false);
        AlertBo alertBo = olePersistableBusinessObjectBase.getAlertBoList().get(index);
        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            if(alertDate.toString().equals(new Date(System.currentTimeMillis()).toString())){
                status = "Active";
            }else{
                int dateCompare= alertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()));
                if(dateCompare>0){
                    status = "Future";
                }else if(dateCompare<0){
                    status="Complete";
                }
            }
        }
        alertBo.setStatus(status);
        return super.navigate(maintenanceDocumentForm, result, request, response);
    }

    /**
     * This method is to edit alert
     *
     * @param formBase
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=editAlert")
    public ModelAndView editAlert(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)formBase;
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(maintenanceDocumentForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        AlertBo alertBo = olePersistableBusinessObjectBase.getAlertBoList().get(index);
        alertBo.setEditFlag(true);
        alertBo.setAlertModifierId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertModifiedDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertModifierName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        return super.navigate(maintenanceDocumentForm, result, request, response);
    }


    /**
     * This method is to delete alert
     *
     * @param formBase
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteAlert")
    public ModelAndView deleteAlert(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)formBase;
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(maintenanceDocumentForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (olePersistableBusinessObjectBase.getAlertBoList().size() > index) {
            olePersistableBusinessObjectBase.getAlertBoList().remove(index);
        }
        return super.navigate(formBase, result, request, response);
    }


    /**
     * This method is to delete alert
     *
     * @param formBase
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=approveAlert")
    public ModelAndView approveAlert(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        AlertBo alertBo = null;
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)formBase;
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(maintenanceDocumentForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        olePersistableBusinessObjectBase.getAlertBoList().get(index).setAlertStatus(false);
        olePersistableBusinessObjectBase.getAlertBoList().get(index).setAlertModifierId(GlobalVariables.getUserSession().getPrincipalId());
        if(olePersistableBusinessObjectBase.getAlertBoList().get(index).isRepeatable()){
            alertBo = alertHelperService.createNewAlertBo(olePersistableBusinessObjectBase.getAlertBoList().get(index));
            olePersistableBusinessObjectBase.getAlertBoList().add(alertBo);
        }
        return super.navigate(formBase, result, request, response);
    }


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAlertLine")
    public ModelAndView addAlertLine(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        String selectedCollectionPath = formBase.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        CollectionGroup collectionGroup = formBase.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object addLine = ObjectPropertyUtils.getPropertyValue(formBase, addLinePath);
        ModelAndView modelAndView =  super.addLine(formBase,result,request,response);
        List<String> principalIds = new ArrayList<String>();
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)modelAndView.getModel().get("KualiForm");
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)maintenanceDocumentForm.getDocument().getNewMaintainableObject().getDataObject();
        AlertBo alertBo = olePersistableBusinessObjectBase.getAlertBoList().get(0);
        olePersistableBusinessObjectBase.getAlertBoList().remove(0);
        if(alertBo.getReceivingGroupId()==null && alertBo.getReceivingUserId()==null && alertBo.getReceivingRoleId()==null && StringUtils.isEmpty(alertBo.getReceivingGroupId()) && StringUtils.isEmpty(alertBo.getReceivingUserName()) && StringUtils.isEmpty(alertBo.getReceivingRoleName()) && StringUtils.isEmpty(alertBo.getReceivingGroupName())){
            GlobalVariables.getMessageMap().putErrorForSectionId("OLE-AlertSection", OLEConstants.SELECT_USER);
            return modelAndView ;
        }
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));

        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            if(alertDate.toString().equals(new Date(System.currentTimeMillis()).toString())){
                status = "Active";
            }else{
                int dateCompare= alertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()));
                if(dateCompare>0){
                    status = "Future";
                }else if(dateCompare<0){
                    status="Complete";
                }
            }
        }
        alertBo.setStatus(status);
        alertBo.setAlertStatus(true);
        List<AlertBo> alerts = new ArrayList<AlertBo>();
        if(alertBo.getReceivingUserId()!=null && (alertBo.getReceivingUserName() == null || (alertBo.getReceivingUserName()!=null && alertBo.getReceivingUserName().trim().isEmpty()))){
            alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }
        if(alertBo.getReceivingUserId() == null && (alertBo.getReceivingUserName() != null && !alertBo.getReceivingUserName().trim().isEmpty())){
            alertBo.setReceivingUserId(alertService.getPersonId(alertBo.getReceivingUserName()));
        }
        if(alertBo.getReceivingUserId()!=null){
            principalIds.add(alertBo.getReceivingUserId());
        }
        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,false));
        principalIds = new ArrayList<String>();



        if(alertBo.getReceivingGroupId()!=null && (alertBo.getReceivingGroupName() == null || (alertBo.getReceivingGroupName()!=null && alertBo.getReceivingGroupName().trim().isEmpty()))){
            alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }
        if(alertBo.getReceivingGroupId() == null && (alertBo.getReceivingGroupName() != null && !alertBo.getReceivingGroupName().trim().isEmpty())){
            alertBo.setReceivingGroupId(alertService.getGroupId((alertBo.getReceivingUserName())));
        }


        if(alertBo.getReceivingGroupId()!=null){
            List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
            principalIds.addAll(memberIds);
        }
        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,true));


        principalIds = new ArrayList<String>();

        if(alertBo.getReceivingRoleId()!=null && (alertBo.getReceivingRoleName() == null || (alertBo.getReceivingRoleName()!=null && alertBo.getReceivingRoleName().trim().isEmpty()))){
            alertBo.setReceivingRoleName(alertService.getRoleName(alertBo.getReceivingRoleId()));
        }
        if(alertBo.getReceivingRoleId() == null && (alertBo.getReceivingRoleName() != null && !alertBo.getReceivingRoleName().trim().isEmpty())){
            alertBo.setReceivingRoleId(alertService.getRoleId((alertBo.getReceivingRoleName())));
        }


        if(alertBo.getReceivingRoleId()!=null){
            List<String> roleIds = new ArrayList<String>();
            roleIds.add(alertBo.getReceivingRoleId());
            Role role =  roleService.getRole(alertBo.getReceivingRoleId());
            Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String,String>());
            List<String> memberIds = new ArrayList<String>();
            memberIds.addAll(collection);
            principalIds.addAll(memberIds);
        }


        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,true,false));


        olePersistableBusinessObjectBase.getAlertBoList().addAll(alerts);
        if(alertBo.getReceivingUserId()==null && alertBo.getReceivingGroupId()!=null){
            olePersistableBusinessObjectBase.getAlertBoList().remove(0);
        }
        if(alertBo.getReceivingUserId()!=null && alertBo.getReceivingGroupId()!=null){
            alertBo.setReceivingGroupName(null);
            alertBo.setReceivingGroupId(null);
        }

        return modelAndView ;

}



/*    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAlertLine")
    public ModelAndView addAlertLine(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        String selectedCollectionPath = formBase.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = formBase.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object addLine = ObjectPropertyUtils.getPropertyValue(formBase, addLinePath);
        AlertBo alertBo = (AlertBo)addLine;
        List<String> principalIds = new ArrayList<String>();
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        if(alertBo.getReceivingUserId()!=null){
//            principalIds.add(alertBo.getReceivingUserId());
            alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }
        if(alertBo.getReceivingGroupId()!=null && !alertBo.getReceivingGroupId().trim().isEmpty()){
            alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }

        if(alertBo.getReceivingGroupId()!=null){
            List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
            principalIds.addAll(memberIds);
        }

        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            if(alertDate.toString().equals(new Date(System.currentTimeMillis()).toString())){
                status = "Active";
            }else{
                int dateCompare= alertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()));
                if(dateCompare>0){
                    status = "Future";
                }else if(dateCompare<0){
                    status="Complete";
                }
            }
        }
        alertBo.setStatus(status);
        alertBo.setAlertStatus(true);

        List<AlertBo> alerts = alertService.getAlertBo(alertBo,principalIds);
       MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm)formBase;
        OleMaintenanceDocumentBase oleMaintenanceDocumentBase = (OleMaintenanceDocumentBase) maintenanceDocumentForm.getDocument();
        oleMaintenanceDocumentBase.setAlertBoList(maintenanceDocumentForm.getAddedCollectionItems());
        oleMaintenanceDocumentBase.getAlertBoList().addAll(alerts);
        if(alertBo.getReceivingUserId()==null && alertBo.getReceivingGroupId()!=null){
            oleMaintenanceDocumentBase.getAlertBoList().remove(0);
        }
        if(alertBo.getReceivingUserId()!=null && alertBo.getReceivingGroupId()!=null){
            alertBo.setReceivingGroupName(null);
            alertBo.setReceivingGroupId(null);
        }

        return getUIFModelAndView(formBase);

      *//*  MaintenanceDocumentForm maintenanceDocumentFormBase = (MaintenanceDocumentForm)formBase;
        OleMaintenanceDocumentBase oleMaintenanceDocumentBase = (OleMaintenanceDocumentBase) maintenanceDocumentFormBase.getDocument();
        oleMaintenanceDocumentBase.getDocumentHeader().getWorkflowDocument().getDocument().getStatus().getCode();
*//**//*        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(true);*//**//*
        ModelAndView modelAndView =  super.addLine(formBase,result,request,response);
        maintenanceDocumentFormBase = (MaintenanceDocumentForm)modelAndView.getModel().get("KualiForm");
        List<String> principalIds = new ArrayList<String>();
        AlertBo alertBo = oleMaintenanceDocumentBase.getAlertBoList().get(0);
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        if(alertBo.getReceivingUserId()!=null){
//            principalIds.add(alertBo.getReceivingUserId());
            alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }
        if(alertBo.getReceivingGroupId()!=null && !alertBo.getReceivingGroupId().trim().isEmpty()){
            alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }

        if(alertBo.getReceivingGroupId()!=null){
            List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
            principalIds.addAll(memberIds);
        }

        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            if(alertDate.toString().equals(new Date(System.currentTimeMillis()).toString())){
                status = "Active";
            }else{
                int dateCompare= alertBo.getAlertDate().compareTo(new Date(System.currentTimeMillis()));
                if(dateCompare>0){
                    status = "Future";
                }else if(dateCompare<0){
                    status="Complete";
                }
            }
        }
        alertBo.setStatus(status);
        alertBo.setAlertStatus(true);

        List<AlertBo> alerts = alertService.getAlertBo(alertBo,principalIds);

        oleMaintenanceDocumentBase.getAlertBoList().addAll(alerts);
        if(alertBo.getReceivingUserId()==null && alertBo.getReceivingGroupId()!=null){
            oleMaintenanceDocumentBase.getAlertBoList().remove(0);
        }
        if(alertBo.getReceivingUserId()!=null && alertBo.getReceivingGroupId()!=null){
            alertBo.setReceivingGroupName(null);
            alertBo.setReceivingGroupId(null);
        }

        return modelAndView ;*//*
    }*/

/*    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {

      //   getDocumentService().saveDocument(form.getDocument());
        List<ActionRequest> actionRequests = form.getDocument().getDocumentHeader().getWorkflowDocument().getDocumentDetail().getActionRequests();


        List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
        org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService)KimApiServiceLocator.getRoleService();
        Role role = roleService.getRole("123456");
        Collection<String> principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        List<String> principalList = new ArrayList<String>();
        principalList.addAll(principalIds);
        List<Principal> principals = identityService.getPrincipals(principalList);
        AdHocRoutePerson adHocRoutePerson;
        if(principals!=null && principals.size()>0){
            for(Principal principal : principals){
                adHocRoutePerson = new AdHocRoutePerson();
                adHocRoutePerson.setId(principal.getPrincipalId());
                adHocRoutePerson.setName(principal.getPrincipalName());
                adHocRoutePerson.setActionRequested("A");
                adHocRoutePerson.setdocumentNumber(form.getDocument().getDocumentNumber());
                adHocRoutePerson.setType(0);
                adHocRouteRecipients.add(adHocRoutePerson);
            }
        }
        List<AdHocRouteRecipient>  adHocRouteRecipientList = combineAdHocRecipients(form);
        adHocRouteRecipientList.addAll(adHocRouteRecipients);

        getDocumentService().approveDocument(form.getDocument(), "Approved by user " + role.getName(), adHocRouteRecipientList);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
        org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService)KimApiServiceLocator.getRoleService();
        Role role = roleService.getRole("12345");
          Collection<String> principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        List<String> principalList = new ArrayList<String>();
        principalList.addAll(principalIds);
        List<Principal> principals = identityService.getPrincipals(principalList);
*//*        AdHocRouteWorkgroup adHocRouteWorkgroup = new AdHocRouteWorkgroup();
        adHocRouteWorkgroup.setRecipientName(role.getName());
        adHocRouteWorkgroup.setRecipientNamespaceCode(role.getNamespaceCode());
        adHocRouteWorkgroup.setType(1);

        List<AdHocRouteWorkgroup> adHocRouteGroupRecipientList  = new ArrayList<AdHocRouteWorkgroup> ();
        adHocRouteGroupRecipientList.add(adHocRouteWorkgroup);*//*


        AdHocRoutePerson adHocRoutePerson;
        if(principals!=null && principals.size()>0){
            for(Principal principal : principals){
                adHocRoutePerson = new AdHocRoutePerson();
                adHocRoutePerson.setId(principal.getPrincipalId());
                adHocRoutePerson.setName(principal.getPrincipalName());
                adHocRoutePerson.setActionRequested("A");
                adHocRoutePerson.setdocumentNumber(form.getDocument().getDocumentNumber());
                adHocRoutePerson.setType(0);
             adHocRouteRecipients.add(adHocRoutePerson);
            }
        }
       List<AdHocRouteRecipient>  adHocRouteRecipientList = combineAdHocRecipients(form);
        adHocRouteRecipientList.addAll(adHocRouteRecipients);

        getDocumentService().routeDocument(form.getDocument(), form.getAnnotation(), adHocRouteRecipientList);

        return getUIFModelAndView(form);
    }*/

    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_EDIT)
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);
        OlePersistableBusinessObjectBase olePersistableBusinessObjectBase = (OlePersistableBusinessObjectBase)form.getDocument().getNewMaintainableObject().getDataObject();
        olePersistableBusinessObjectBase.setAlertBoList(alertService.retrieveAlertList(form.getDocument().getDocumentNumber()));
        return getUIFModelAndView(form);
    }

}
