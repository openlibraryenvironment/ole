package org.kuali.ole.alert.controller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.alert.service.impl.AlertHelperServiceImpl;
import org.kuali.ole.alert.service.impl.AlertServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maheswarang on 11/5/14.
 */
public abstract class OleTransactionalDocumentControllerBase extends TransactionalDocumentControllerBase {
private AlertServiceImpl alertService = new AlertServiceImpl();
    private GroupService groupService = KimApiServiceLocator.getGroupService();
    private AlertHelperServiceImpl alertHelperService = new AlertHelperServiceImpl();
    private org.kuali.rice.kim.api.role.RoleService roleService = KimApiServiceLocator.getRoleService();

/*
    @Override
    @Autowired
    @Qualifier("oleDocumentService")
    public void setControllerService(ControllerService controllerService) {
        super.setControllerService(controllerService);
    }
*/


    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        String command = form.getCommand();

        // in all of the following cases we want to load the document
        if (ArrayUtils.contains(DOCUMENT_LOAD_COMMANDS, command) && form.getDocId() != null) {
            loadDocument(form);
            OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase)form.getDocument();
            oleTransactionalDocumentBase.setAlertBoList(alertService.retrieveAlertList(form.getDocument().getDocumentNumber()));

        } else if (KewApiConstants.INITIATE_COMMAND.equals(command)) {
            createDocument(form);
        } else {
            LOG.error("docHandler called with invalid parameters");
            throw new IllegalArgumentException("docHandler called with invalid parameters");
        }

        // TODO: authorization on document actions
        // if (KEWConstants.SUPERUSER_COMMAND.equalsIgnoreCase(command)) {
        // form.setSuppressAllButtons(true);
        // }

        return getUIFModelAndView(form);
    }


    /**
     * This method is to save alert
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveAlert")
    public ModelAndView saveAlert(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(false);
        AlertBo alertBo = oleTransactionalDocumentBase.getAlertBoList().get(index);
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
        return super.navigate(transactionalDocumentFormBase, result, request, response);
    }

   /**
     * This method is to edit alert
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=editAlert")
    public ModelAndView editAlert(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        AlertBo alertBo = oleTransactionalDocumentBase.getAlertBoList().get(index);
        alertBo.setEditFlag(true);
        alertBo.setAlertModifierId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertModifiedDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertModifierName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        return super.navigate(transactionalDocumentFormBase, result, request, response);
    }


    /**
     * This method is to delete alert
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteAlert")
    public ModelAndView deleteAlert(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (oleTransactionalDocumentBase.getAlertBoList().size() > index) {
            oleTransactionalDocumentBase.getAlertBoList().remove(index);
        }
        return super.navigate(uifForm, result, request, response);
    }


    /**
     * This method is to delete alert
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=approveAlert")
    public ModelAndView approveAlert(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        AlertBo alertBo = null;
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertStatus(false);
        oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertModifierId(GlobalVariables.getUserSession().getPrincipalId());
        if(oleTransactionalDocumentBase.getAlertBoList().get(index).isRepeatable()){
        alertBo = alertHelperService.createNewAlertBo(oleTransactionalDocumentBase.getAlertBoList().get(index));
            oleTransactionalDocumentBase.getAlertBoList().add(alertBo);
        }
        return super.navigate(uifForm, result, request, response);
    }



    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAlertLine")
    public ModelAndView addAlertLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocument().getStatus().getCode();
/*        int index = Integer.parseInt(transactionalDocumentFormBase.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(true);*/
        ModelAndView modelAndView =  super.addLine(uifForm,result,request,response);
        transactionalDocumentFormBase = (TransactionalDocumentFormBase)modelAndView.getModel().get("KualiForm");
        List<String> principalIds = new ArrayList<String>();
        AlertBo alertBo = oleTransactionalDocumentBase.getAlertBoList().get(0);
        oleTransactionalDocumentBase.getAlertBoList().remove(0);
        if(StringUtils.isBlank(alertBo.getReceivingGroupId()) && StringUtils.isBlank(alertBo.getReceivingUserId()) && StringUtils.isBlank(alertBo.getReceivingRoleId()) && StringUtils.isBlank(alertBo.getReceivingGroupId()) && StringUtils.isEmpty(alertBo.getReceivingUserName()) && StringUtils.isEmpty(alertBo.getReceivingRoleName()) && StringUtils.isEmpty(alertBo.getReceivingGroupName())){
            GlobalVariables.getMessageMap().putErrorForSectionId("OLE-AlertSection", OLEConstants.SELECT_USER);
            return modelAndView ;
        } 
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
       /* if(alertBo.getReceivingUserId()!=null){
//            principalIds.add(alertBo.getReceivingUserId());
        alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }
        if(alertBo.getReceivingGroupId()!=null && !alertBo.getReceivingGroupId().trim().isEmpty()){
        alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }

        if(alertBo.getReceivingGroupId()!=null){
            List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
            principalIds.addAll(memberIds);
        }*/
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
        if(StringUtils.isNotBlank(alertBo.getReceivingUserId()) && (alertBo.getReceivingUserName() == null || (alertBo.getReceivingUserName()!=null && alertBo.getReceivingUserName().trim().isEmpty()))){
            alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }
        if(StringUtils.isBlank(alertBo.getReceivingUserId()) && (alertBo.getReceivingUserName() != null && !alertBo.getReceivingUserName().trim().isEmpty())){
            alertBo.setReceivingUserId(alertService.getPersonId(alertBo.getReceivingUserName()));
        }
       if(StringUtils.isNotBlank(alertBo.getReceivingUserId())){
        principalIds.add(alertBo.getReceivingUserId());
       }
        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,false));
        principalIds = new ArrayList<String>();


/*        if(alertBo.getReceivingUserId()!=null && !alertBo.getReceivingUserId().trim().isEmpty()){
//            principalIds.add(alertBo.getReceivingUserId());
            alertBo.setReceivingUserName(alertService.getName(alertBo.getReceivingUserId()));
        }*/
        if(StringUtils.isNotBlank(alertBo.getReceivingGroupId()) && (alertBo.getReceivingGroupName() == null || (alertBo.getReceivingGroupName()!=null && alertBo.getReceivingGroupName().trim().isEmpty()))){
            alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }
        if(StringUtils.isBlank(alertBo.getReceivingGroupId()) && (alertBo.getReceivingGroupName() != null && !alertBo.getReceivingGroupName().trim().isEmpty())){
            alertBo.setReceivingGroupId(alertService.getGroupId((alertBo.getReceivingGroupName())));
        }

/*        if(alertBo.getReceivingGroupId()!=null && !alertBo.getReceivingGroupId().trim().isEmpty()){
            alertBo.setReceivingGroupName(alertService.getGroupName(alertBo.getReceivingGroupId()));
        }*/

        if(StringUtils.isNotBlank(alertBo.getReceivingGroupId())){
            List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
            principalIds.addAll(memberIds);
        }
        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,true));


        principalIds = new ArrayList<String>();

        if(StringUtils.isNotBlank(alertBo.getReceivingRoleId()) && (alertBo.getReceivingRoleName() == null || (alertBo.getReceivingRoleName()!=null && alertBo.getReceivingRoleName().trim().isEmpty()))){
            alertBo.setReceivingRoleName(alertService.getRoleName(alertBo.getReceivingRoleId()));
        }
        if(StringUtils.isBlank(alertBo.getReceivingRoleId()) && (alertBo.getReceivingRoleName() != null && !alertBo.getReceivingRoleName().trim().isEmpty())){
            alertBo.setReceivingRoleId(alertService.getRoleId((alertBo.getReceivingRoleName())));
        }


        if(StringUtils.isNotBlank(alertBo.getReceivingRoleId())){
            List<String> roleIds = new ArrayList<String>();
            roleIds.add(alertBo.getReceivingRoleId());
            Role role =  roleService.getRole(alertBo.getReceivingRoleId());
            Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
            List<String> memberIds = new ArrayList<String>();
            memberIds.addAll(collection);
            principalIds.addAll(memberIds);
        }


        alerts.addAll(alertService.getAlertBo(alertBo,principalIds,true,false));


        oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
        if(StringUtils.isBlank(alertBo.getReceivingUserId()) && StringUtils.isNotBlank(alertBo.getReceivingGroupId())){
            oleTransactionalDocumentBase.getAlertBoList().remove(0);
        }
        if(StringUtils.isNotBlank(alertBo.getReceivingUserId()) && StringUtils.isNotBlank(alertBo.getReceivingGroupId())){
            alertBo.setReceivingGroupName(null);
            alertBo.setReceivingGroupId(null);
        }

        return modelAndView ;
    }



    @Override
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.save(form,result,request,response);
    }


}
