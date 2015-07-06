package org.kuali.ole.alert.controller;

import org.apache.commons.collections.CollectionUtils;
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
import org.kuali.rice.krad.service.KRADServiceLocator;
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
            oleTransactionalDocumentBase.setTempAlertBoList(alertService.retrieveApprovedAlertList(form.getDocument().getDocumentNumber()));

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
        List<String> principalIds = new ArrayList<String>();
        List<AlertBo> alerts = new ArrayList<AlertBo>();
        String oldSelector = null;
        String oldRecipientName = null;
        String oldRecipientId = null;
        AlertBo alertBo = oleTransactionalDocumentBase.getAlertBoList().get(index);
        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            int dateCompare= alertDate.compareTo(new Date(System.currentTimeMillis()));
            if(dateCompare <= 0){
                status = "Active";
            }else{
                if(dateCompare>0){
                    status = "Future";
                }
            }
        }
        alertBo.setStatus(status);
        alertBo.setAlertStatus(true);
        AlertBo oldAlertBo = null;
        if(StringUtils.isNotBlank(alertBo.getAlertId())) {
            oldAlertBo = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(AlertBo.class,alertBo.getAlertId());
        }
        if(oldAlertBo != null) {
            oldAlertBo.getAlertSelector();
            if(StringUtils.isNotBlank(alertBo.getAlertSelector())) {
                if(alertBo.getAlertSelector().equals(oldAlertBo.getAlertSelector())) {
                    if(!validateAndSaveAlertForSameSelector(alertBo, oldRecipientName, oldRecipientId, oleTransactionalDocumentBase, principalIds, index, alerts)) {
                        return getUIFModelAndView(uifForm);
                    }
                } else {
                    if(!validateAndSaveAlertForDiffSelector(alertBo, oleTransactionalDocumentBase, principalIds, index, alerts)) {
                        return getUIFModelAndView(uifForm);
                    }
                }
            }
        } else {
            if(StringUtils.isNotBlank(alertBo.getAlertDetails())) {
                String[] alertDetails = alertBo.getAlertDetails().split("/");
                if(alertDetails != null && alertDetails.length > 0) {
                    for(int i=0 ; i < alertDetails.length ; i++) {
                        if(i == 0) {
                            oldSelector = alertDetails[i];
                        } else if(i == 1) {
                            oldRecipientName = alertDetails[i];
                        } else if(i == 2) {
                            oldRecipientId = alertDetails[i];
                        }
                    }
                }
                if(StringUtils.isNotBlank(alertBo.getAlertSelector())) {
                    if(alertBo.getAlertSelector().equals(oldSelector)) {
                        if(!validateAndSaveAlertForSameSelector(alertBo, oldRecipientName, oldRecipientId, oleTransactionalDocumentBase, principalIds, index, alerts)) {
                            return getUIFModelAndView(uifForm);
                        }
                    } else {
                        if(!validateAndSaveAlertForDiffSelector(alertBo, oleTransactionalDocumentBase, principalIds, index, alerts)) {
                            return getUIFModelAndView(uifForm);
                        }
                    }
                }
            }
        }
        return super.navigate(transactionalDocumentFormBase, result, request, response);
    }

    private boolean validateAndSaveAlertForDiffSelector(AlertBo alertBo, OleTransactionalDocumentBase oleTransactionalDocumentBase, List<String> principalIds, int index, List<AlertBo> alerts) {
        if (alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_ROLE)) {
            if (!checkAndAddRole(principalIds, alertBo)) {
                return false;
            }
            alerts.addAll(alertService.getAlertBo(alertBo, principalIds, true, false));
            oleTransactionalDocumentBase.getAlertBoList().remove(index);
        } else if (alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_GROUP)) {
            if (!checkAndAddGroup(principalIds, alertBo)) {
                return false;
            }
            alerts.addAll(alertService.getAlertBo(alertBo, principalIds, false, true));
            oleTransactionalDocumentBase.getAlertBoList().remove(index);
        } else if (alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_PERSON)) {
            if (!checkAndAddPerson(principalIds, alertBo)) {
                return false;
            }
            alerts.addAll(alertService.getAlertBo(alertBo, principalIds, false, false));
            oleTransactionalDocumentBase.getAlertBoList().remove(index);
        }
        oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
        return true;
    }

    private boolean validateAndSaveAlertForSameSelector(AlertBo alertBo, String oldRecipientName, String oldRecipientId, OleTransactionalDocumentBase oleTransactionalDocumentBase, List<String> principalIds, int index, List<AlertBo> alerts) {
        if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_ROLE)) {
            if(StringUtils.isBlank(oldRecipientName) && StringUtils.isNotBlank(oldRecipientId)) {
                oldRecipientName = alertService.getRoleName(oldRecipientId);
            }
            if(StringUtils.isNotBlank(oldRecipientName) && !oldRecipientName.equals(alertBo.getReceivingRoleName())) {
                if(!checkAndAddRole(principalIds, alertBo)) {
                    return false;
                }
                alerts.addAll(alertService.getAlertBo(alertBo, principalIds, true, false));
                oleTransactionalDocumentBase.getAlertBoList().remove(index);
                oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
            } else {
                oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(false);
            }
        } else if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_GROUP)) {
            if(StringUtils.isBlank(oldRecipientName) && StringUtils.isNotBlank(oldRecipientId)) {
                oldRecipientName = alertService.getGroupName(oldRecipientId);
            }
            if(StringUtils.isNotBlank(oldRecipientName) && !oldRecipientName.equals(alertBo.getReceivingGroupName())) {
                if(!checkAndAddGroup(principalIds, alertBo)) {
                    return false;
                }
                alerts.addAll(alertService.getAlertBo(alertBo, principalIds, false, true));
                oleTransactionalDocumentBase.getAlertBoList().remove(index);
                oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
            } else {
                oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(false);
            }
        } else if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_PERSON)) {
            if(StringUtils.isBlank(oldRecipientName) && StringUtils.isNotBlank(oldRecipientId)) {
                oldRecipientName = alertService.getName(oldRecipientId);
            }
            if(StringUtils.isNotBlank(oldRecipientName) && !oldRecipientName.equals(alertBo.getReceivingUserName())) {
                if(!checkAndAddPerson(principalIds, alertBo)) {
                    return false;
                }
                alerts.addAll(alertService.getAlertBo(alertBo, principalIds, false, false));
                oleTransactionalDocumentBase.getAlertBoList().remove(index);
                oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
            } else {
                oleTransactionalDocumentBase.getAlertBoList().get(index).setEditFlag(false);
            }
        }
        return true;
    }

    private boolean checkAndAddPerson(List<String> principalIds, AlertBo alertBo) {
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        if(StringUtils.isBlank(alertBo.getReceivingUserName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.ERROR_EMPTY_PERSON);
            return false;
        }
        alertBo.setReceivingUserId(alertService.getPersonId(alertBo.getReceivingUserName()));
        if(StringUtils.isBlank(alertBo.getReceivingUserId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.ERROR_INVALID_PERSON_NAME);
            return false;
        }
        principalIds.add(alertBo.getReceivingUserId());
        alertBo.setAlertDetails(alertBo.getAlertSelector()+"/"+alertBo.getReceivingUserName()+"/"+alertBo.getReceivingUserId());
        return true;
    }

    private boolean checkAndAddGroup(List<String> principalIds, AlertBo alertBo) {
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        if(StringUtils.isBlank(alertBo.getReceivingGroupName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.ERROR_EMPTY_GROUP);
            return false;
        }
        alertBo.setReceivingGroupId(alertService.getGroupId(alertBo.getReceivingGroupName()));
        if(StringUtils.isBlank(alertBo.getReceivingGroupId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.ERROR_INVALID_GROUP_NAME);
            return false;
        }
        List<String> memberIds = groupService.getMemberPrincipalIds(alertBo.getReceivingGroupId());
        principalIds.addAll(memberIds);
        if(CollectionUtils.isEmpty(principalIds)) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.NO_USER_FOR_GROUP);
            return false;
        }
        alertBo.setAlertDetails(alertBo.getAlertSelector() + "/" + alertBo.getReceivingGroupName() + "/" + alertBo.getReceivingGroupId());
        return true;
    }

    private boolean checkAndAddRole(List<String> principalIds, AlertBo alertBo) {
        alertBo.setAlertCreateDate(new Date(System.currentTimeMillis()));
        alertBo.setAlertInitiatorId(GlobalVariables.getUserSession().getPrincipalId());
        alertBo.setAlertInitiatorName(alertService.getName(GlobalVariables.getUserSession().getPrincipalId()));
        if(StringUtils.isBlank(alertBo.getReceivingRoleName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.ERROR_EMPTY_ROLE);
            return false;
        }
        alertBo.setReceivingRoleId(alertService.getRoleId((alertBo.getReceivingRoleName())));
        if(StringUtils.isBlank(alertBo.getReceivingRoleId())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION,OLEConstants.ERROR_INVALID_NAME);
            return false;
        }
        List<String> roleIds = new ArrayList<String>();
        roleIds.add(alertBo.getReceivingRoleId());
        Role role =  roleService.getRole(alertBo.getReceivingRoleId());
        Collection collection  =  (Collection)roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(),role.getName(),new HashMap<String, String>());
        List<String> memberIds = new ArrayList<String>();
        memberIds.addAll(collection);
        principalIds.addAll(memberIds);
        if(CollectionUtils.isEmpty(principalIds)) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_ALERT_SECTION, OLEConstants.NO_USER_FOR_ROLE);
            return false;
        }
        alertBo.setAlertDetails(alertBo.getAlertSelector()+"/"+alertBo.getReceivingRoleName()+"/"+alertBo.getReceivingRoleId());
        return true;
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
        List<AlertBo> tempAlertBoList = new ArrayList<>();
        if(StringUtils.isNotEmpty((oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingRoleName()))) {
            if(StringUtils.isNotEmpty(oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingRoleId())) {
                tempAlertBoList.addAll(approveAlertBaseOnRole(oleTransactionalDocumentBase, index));
            }
        } else if(StringUtils.isNotEmpty(oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingGroupName())) {
            if(StringUtils.isNotEmpty(oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingGroupId())) {
                tempAlertBoList.addAll(approveAlertBaseOnGroup(oleTransactionalDocumentBase, index));
            }
        } else {
            oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertStatus(false);
            oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApproverId(GlobalVariables.getUserSession().getPrincipalId());
            oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApprovedDate(new Date(System.currentTimeMillis()));
            if(oleTransactionalDocumentBase.getAlertBoList().get(index).isRepeatable()){
                alertBo = alertHelperService.createNewAlertBo(oleTransactionalDocumentBase.getAlertBoList().get(index));
                oleTransactionalDocumentBase.getAlertBoList().add(alertBo);
            }
            tempAlertBoList.add(oleTransactionalDocumentBase.getAlertBoList().get(index));
            oleTransactionalDocumentBase.getAlertBoList().remove(index);
        }
        oleTransactionalDocumentBase.getTempAlertBoList().addAll(tempAlertBoList);
        return super.navigate(uifForm, result, request, response);
    }

    private List<AlertBo> approveAlertBaseOnRole(OleTransactionalDocumentBase oleTransactionalDocumentBase, int actualIndex) {
        List<AlertBo> tempAlertBoList = new ArrayList<>();
        AlertBo alertBo = null;
        for(int index=0 ; index < oleTransactionalDocumentBase.getAlertBoList().size() ; index++) {
            if(oleTransactionalDocumentBase.getAlertBoList().get(actualIndex).getReceivingRoleId().equals(oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingRoleId())) {
                if(oleTransactionalDocumentBase.getAlertBoList().get(actualIndex).getAlertCreateDate().equals(oleTransactionalDocumentBase.getAlertBoList().get(index).getAlertCreateDate())) {
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertStatus(false);
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApproverId(GlobalVariables.getUserSession().getPrincipalId());
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApprovedDate(new Date(System.currentTimeMillis()));
                    if(oleTransactionalDocumentBase.getAlertBoList().get(index).isRepeatable()){
                        alertBo = alertHelperService.createNewAlertBo(oleTransactionalDocumentBase.getAlertBoList().get(index));
                        oleTransactionalDocumentBase.getAlertBoList().add(alertBo);
                    }
                    tempAlertBoList.add(oleTransactionalDocumentBase.getAlertBoList().get(index));
                }
            }
        }
        oleTransactionalDocumentBase.getAlertBoList().removeAll(tempAlertBoList);
        return tempAlertBoList;
    }

    private List<AlertBo> approveAlertBaseOnGroup(OleTransactionalDocumentBase oleTransactionalDocumentBase, int actualIndex) {
        List<AlertBo> tempAlertBoList = new ArrayList<>();
        AlertBo alertBo = null;
        for(int index=0 ; index < oleTransactionalDocumentBase.getAlertBoList().size() ; index++) {
            if(oleTransactionalDocumentBase.getAlertBoList().get(actualIndex).getReceivingGroupId().equals(oleTransactionalDocumentBase.getAlertBoList().get(index).getReceivingGroupId())) {
                if(oleTransactionalDocumentBase.getAlertBoList().get(actualIndex).getAlertCreateDate().equals(oleTransactionalDocumentBase.getAlertBoList().get(index).getAlertCreateDate())) {
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertStatus(false);
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApproverId(GlobalVariables.getUserSession().getPrincipalId());
                    oleTransactionalDocumentBase.getAlertBoList().get(index).setAlertApprovedDate(new Date(System.currentTimeMillis()));
                    if(oleTransactionalDocumentBase.getAlertBoList().get(index).isRepeatable()){
                        alertBo = alertHelperService.createNewAlertBo(oleTransactionalDocumentBase.getAlertBoList().get(index));
                        oleTransactionalDocumentBase.getAlertBoList().add(alertBo);
                    }
                    tempAlertBoList.add(oleTransactionalDocumentBase.getAlertBoList().get(index));
                }
            }
        }
        oleTransactionalDocumentBase.getAlertBoList().removeAll(tempAlertBoList);
        return tempAlertBoList;
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAlertLine")
    public ModelAndView addAlertLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        TransactionalDocumentFormBase transactionalDocumentFormBase = (TransactionalDocumentFormBase)uifForm;
        OleTransactionalDocumentBase oleTransactionalDocumentBase = (OleTransactionalDocumentBase) transactionalDocumentFormBase.getDocument();
        oleTransactionalDocumentBase.getDocumentHeader().getWorkflowDocument().getDocument().getStatus().getCode();
        ModelAndView modelAndView =  super.addLine(uifForm,result,request,response);
        transactionalDocumentFormBase = (TransactionalDocumentFormBase)modelAndView.getModel().get("KualiForm");
        List<String> principalIds = new ArrayList<String>();
        List<AlertBo> alerts = new ArrayList<AlertBo>();
        AlertBo alertBo = oleTransactionalDocumentBase.getAlertBoList().get(0);
        oleTransactionalDocumentBase.getAlertBoList().remove(0);
        String status = null;
        if(alertBo.getAlertDate()!=null){
            Date alertDate = alertBo.getAlertDate();
            int dateCompare= alertDate.compareTo(new Date(System.currentTimeMillis()));
            if(dateCompare <= 0){
                status = "Active";
            }else{
                if(dateCompare>0){
                    status = "Future";
                }
            }
        }
        alertBo.setStatus(status);
        alertBo.setAlertStatus(true);
        if(StringUtils.isNotBlank(alertBo.getAlertSelector())) {
            if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_ROLE)) {
                alertBo.setReceivingGroupName(null);
                alertBo.setReceivingGroupId(null);
                alertBo.setReceivingUserName(null);
                alertBo.setReceivingUserId(null);
                if(!checkAndAddRole(principalIds, alertBo)) {
                    return getUIFModelAndView(uifForm);
                }
                alerts.addAll(alertService.getAlertBo(alertBo,principalIds,true,false));
            } else if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_GROUP)) {
                alertBo.setReceivingRoleName(null);
                alertBo.setReceivingRoleId(null);
                alertBo.setReceivingUserName(null);
                alertBo.setReceivingUserId(null);
                if(!checkAndAddGroup(principalIds,alertBo)) {
                    return getUIFModelAndView(uifForm);
                }
                alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,true));
            } else if(alertBo.getAlertSelector().equals(OLEConstants.SELECTOR_PERSON)) {
                alertBo.setReceivingRoleName(null);
                alertBo.setReceivingRoleId(null);
                alertBo.setReceivingGroupName(null);
                alertBo.setReceivingGroupId(null);
                if(!checkAndAddPerson(principalIds,alertBo)) {
                    return getUIFModelAndView(uifForm);
                }
                alerts.addAll(alertService.getAlertBo(alertBo,principalIds,false,false));
            }
        }
        oleTransactionalDocumentBase.getAlertBoList().addAll(alerts);
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
