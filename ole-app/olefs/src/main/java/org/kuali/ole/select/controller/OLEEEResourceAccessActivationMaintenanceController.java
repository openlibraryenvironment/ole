package org.kuali.ole.select.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLEAdHocRoutingForAccessActivation;
import org.kuali.ole.select.bo.OLEEResourceAccessActivation;
import org.kuali.ole.select.bo.OLEEResourceNotes;
import org.kuali.ole.select.document.OLEEResourceAccessWorkflow;
import org.kuali.ole.select.document.OLEEResourceEventLog;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.service.OLEAccessActivationService;
import org.kuali.ole.select.service.impl.OLEAccessActivationServiceImpl;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.rule.event.SaveDocumentEvent;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/6/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleEEResourceAccessMaintenance")
public class OLEEEResourceAccessActivationMaintenanceController extends MaintenanceDocumentController {

    private OLEEResourceHelperService oleeResourceHelperService;
    private OLEAccessActivationService oleAccessActivationService;
    private PersonService personService;

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if (oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }

    public OLEAccessActivationService getOleAccessActivationService() {
        if(oleAccessActivationService == null){
            oleAccessActivationService = new OLEAccessActivationServiceImpl();
        }
        return oleAccessActivationService;
    }

    public PersonService getPersonService() {
        if(personService == null){
            personService = KimApiServiceLocator.getPersonService();
        }
        return personService;
    }

    @Override
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase formBase, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) formBase;
        ModelAndView modelAndView = super.docHandler(formBase, result, request, response);
        MaintenanceDocument maintenanceDocument = form.getDocument();
        OLEEResourceAccessActivation oleEResourceAccessActivation = (OLEEResourceAccessActivation) maintenanceDocument.getNewMaintainableObject().getDataObject();
        if (oleEResourceAccessActivation!=null && StringUtils.isNotBlank(oleEResourceAccessActivation.getOleERSIdentifier())){
            OLEEResourceRecordDocument oleEResourceRecordDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class,oleEResourceAccessActivation.getOleERSIdentifier());
            if (oleEResourceRecordDocument!=null){
                oleEResourceAccessActivation.seteResourceTitle(oleEResourceRecordDocument.getTitle());
                oleEResourceAccessActivation.seteResourceDocumentNumber(oleEResourceRecordDocument.getDocumentNumber());
            }
        }
        return modelAndView;
    }

    public void setOleAccessActivationService(OLEAccessActivationService oleAccessActivationService) {
        this.oleAccessActivationService = oleAccessActivationService;
    }

    @RequestMapping(params = "methodToCall=addNoteTextSection")
    public ModelAndView addNoteTextSection(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) uifForm).getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        index++;
        List<OLEEResourceNotes> oleeResourceNoteses = oleeResourceAccess.getEresNotes();
        oleeResourceAccess.getEresNotes().add(index, new OLEEResourceNotes());
        oleeResourceAccess.setEresNotes(oleeResourceNoteses);
        return super.navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeNoteTextSection")
    public ModelAndView removeNoteTextSection(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) uifForm).getDocument().getNewMaintainableObject().getDataObject();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<OLEEResourceNotes> oleeResourceNoteses = oleeResourceAccess.getEresNotes();
        if (oleeResourceNoteses.size() > 1) {
            oleeResourceNoteses.remove(index);
        }
        return super.navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addAdHocRouteLine")
    public ModelAndView addAdHocRouteLine(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = maintenanceDocumentForm.getDocument();
        OLEEResourceAccessActivation oleeResourceAccessActivation = (OLEEResourceAccessActivation) maintenanceDocument.getNewMaintainableObject().getDataObject();
        ModelAndView modelAndView = super.addLine(form, result, request, response);
        OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation = oleeResourceAccessActivation.getOleAdHocRoutingForAccessActivations().get(0);
        oleeResourceAccessActivation.getOleAdHocRoutingForAccessActivations().remove(0);
        if(OLEConstants.SELECTOR_ROLE.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
            oleAdHocRoutingForAccessActivation.setAdHocGroupId(null);
            oleAdHocRoutingForAccessActivation.setAdHocGroupName(null);
            oleAdHocRoutingForAccessActivation.setAdHocPrincipalId(null);
            oleAdHocRoutingForAccessActivation.setAdHocPrincipalName(null);
            if(!checkAndAddRole(oleAdHocRoutingForAccessActivation)) {
                return getUIFModelAndView(form);
            }
        } else if(OLEConstants.SELECTOR_GROUP.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
            oleAdHocRoutingForAccessActivation.setAdHocRoleId(null);
            oleAdHocRoutingForAccessActivation.setAdHocRoleName(null);
            oleAdHocRoutingForAccessActivation.setAdHocPrincipalId(null);
            oleAdHocRoutingForAccessActivation.setAdHocPrincipalName(null);
            if(!checkAndAddGroup(oleAdHocRoutingForAccessActivation)) {
                return getUIFModelAndView(form);
            }
        } else if(OLEConstants.SELECTOR_PERSON.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
            oleAdHocRoutingForAccessActivation.setAdHocRoleId(null);
            oleAdHocRoutingForAccessActivation.setAdHocRoleName(null);
            oleAdHocRoutingForAccessActivation.setAdHocGroupId(null);
            oleAdHocRoutingForAccessActivation.setAdHocGroupName(null);
            if(!checkAndAddPerson(oleAdHocRoutingForAccessActivation)) {
                return getUIFModelAndView(form);
            }
        }
        oleeResourceAccessActivation.getOleAdHocRoutingForAccessActivations().add(oleAdHocRoutingForAccessActivation);
        return getUIFModelAndView(form);
    }

    private boolean checkAndAddRole(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        if(StringUtils.isBlank(oleAdHocRoutingForAccessActivation.getAdHocRoleName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS, OLEConstants.ERROR_EMPTY_ROLE);
            return false;
        }
        RoleBo roleBo = getRoleBo(oleAdHocRoutingForAccessActivation);
        if(null == roleBo) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS,OLEConstants.ERROR_INVALID_NAME);
            return false;
        }
        oleAdHocRoutingForAccessActivation.setAdHocRoleId(roleBo.getId());
        return true;
    }

    private boolean checkAndAddGroup(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        if(StringUtils.isBlank(oleAdHocRoutingForAccessActivation.getAdHocGroupName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS, OLEConstants.ERROR_EMPTY_GROUP);
            return false;
        }
        GroupBo groupBo = getGroupBo(oleAdHocRoutingForAccessActivation);
        if(null == groupBo) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS,OLEConstants.ERROR_INVALID_GROUP_NAME);
            return false;
        }
        oleAdHocRoutingForAccessActivation.setAdHocGroupId(groupBo.getId());
        return true;
    }

    private boolean checkAndAddPerson(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        if(StringUtils.isBlank(oleAdHocRoutingForAccessActivation.getAdHocPrincipalName())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS, OLEConstants.ERROR_EMPTY_PERSON);
            return false;
        }
        Person person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(oleAdHocRoutingForAccessActivation.getAdHocPrincipalName());
        if(null == person) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS, OLEConstants.ERROR_INVALID_PERSON_NAME);
            return false;
        }
        oleAdHocRoutingForAccessActivation.setAdHocPrincipalId(person.getPrincipalId());
        return true;
    }

    @Override
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = ((MaintenanceDocumentForm) form).getDocument();
        MaintenanceDocumentForm forms = (MaintenanceDocumentForm) form;
        org.kuali.rice.krad.maintenance.MaintenanceDocument oldMaintenanceDocument =  (MaintenanceDocument)documentService.getByDocumentHeaderId(maintenanceDocument.getDocumentNumber());
        OLEEResourceAccessActivation oldOleeResourceAccess  = new OLEEResourceAccessActivation();
        if(oldMaintenanceDocument != null){
            oldOleeResourceAccess = (OLEEResourceAccessActivation)oldMaintenanceDocument.getNewMaintainableObject().getDataObject();
        }
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) form).getDocument().getNewMaintainableObject().getDataObject();
        if(oleeResourceAccess != null && oleeResourceAccess.getLastRecordLoadDate() != null && oldOleeResourceAccess != null && !oleeResourceAccess.getLastRecordLoadDate().equals(oldOleeResourceAccess.getLastRecordLoadDate())){
            OLEEResourceEventLog oleeResourceEventLog = new OLEEResourceEventLog();
            Timestamp timestamp = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
            oleeResourceEventLog.setEventDate(timestamp);
            oleeResourceEventLog.setEventTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.getEventTypeName();
            oleeResourceEventLog.setEventUser(GlobalVariables.getUserSession().getPrincipalName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
            oleeResourceEventLog.setEventNote("Last Record Load Date has been updated : " +simpleDateFormat.format(oleeResourceAccess.getLastRecordLoadDate()));
            oleeResourceEventLog.setOleERSIdentifier(oleeResourceAccess.getOleERSIdentifier());
            oleeResourceEventLog.setLogTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.setSaveFlag(true);
            getBusinessObjectService().save(oleeResourceEventLog);
        }
        getDocumentService().validateAndPersistDocument(maintenanceDocument,new SaveDocumentEvent(maintenanceDocument));
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_SAVED);
        return super.navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {

        DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
        MaintenanceDocumentForm forms = (MaintenanceDocumentForm) form;
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = ((MaintenanceDocumentForm) form).getDocument();
        org.kuali.rice.krad.maintenance.MaintenanceDocument oldMaintenanceDocument =  (MaintenanceDocument)documentService.getByDocumentHeaderId(maintenanceDocument.getDocumentNumber());
        OLEEResourceAccessActivation oldOleeResourceAccess  = new OLEEResourceAccessActivation();
        if(oldMaintenanceDocument != null){
            oldOleeResourceAccess = (OLEEResourceAccessActivation)oldMaintenanceDocument.getNewMaintainableObject().getDataObject();
        }
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) form).getDocument().getNewMaintainableObject().getDataObject();
        if(oleeResourceAccess != null && oleeResourceAccess.getLastRecordLoadDate() != null && oldOleeResourceAccess != null && !oleeResourceAccess.getLastRecordLoadDate().equals(oldOleeResourceAccess.getLastRecordLoadDate())){
            OLEEResourceEventLog oleeResourceEventLog = new OLEEResourceEventLog();
            Timestamp timestamp = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
            oleeResourceEventLog.setEventDate(timestamp);
            oleeResourceEventLog.setEventTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.getEventTypeName();
            oleeResourceEventLog.setEventUser(GlobalVariables.getUserSession().getPrincipalName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
            oleeResourceEventLog.setEventNote("Last Record Load Date has been updated : " +simpleDateFormat.format(oleeResourceAccess.getLastRecordLoadDate()));
            oleeResourceEventLog.setOleERSIdentifier(oleeResourceAccess.getOleERSIdentifier());
            oleeResourceEventLog.setLogTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.setSaveFlag(true);
            getBusinessObjectService().save(oleeResourceEventLog);
        }
        if (!processAdHocRecipients(form, false, oleeResourceAccess)){
            String previousStatus = oleeResourceAccess.getAccessStatus();
            ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
            Map<String, String> accessConfigMap = new HashMap<String, String>();
            accessConfigMap.put("accessActivationConfigurationId", oleeResourceAccess.getWorkflowId());
            List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = (List<OLEAccessActivationWorkFlow>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEAccessActivationWorkFlow.class, accessConfigMap, "orderNo", true);
            OLEAccessActivationWorkFlow accessActivationWorkFlow = null;
            oleeResourceAccess.setAdHocAccessStatus("");
            oleeResourceAccess.setAdHocAccessStatusDummy("");
            if (oleAccessActivationWorkFlows != null && oleAccessActivationWorkFlows.size() > 0) {
                for (int i = 0; i <= oleAccessActivationWorkFlows.size(); i++) {
                    if (oleeResourceAccess.isAdHocUserExists() || oleAccessActivationWorkFlows.get(i).getStatus().equals(oleeResourceAccess.getAccessStatus())) {
                        int desiredPosition;
                        if (oleeResourceAccess.isAdHocUserExists()) {
                            desiredPosition = i;
                        } else {
                            desiredPosition = i + 1;
                        }
                        if (desiredPosition < oleAccessActivationWorkFlows.size()) {
                            accessActivationWorkFlow = oleAccessActivationWorkFlows.get(desiredPosition);
                            List<OLEEResourceAccessWorkflow> accessWorkflowList = oleeResourceAccess.getOleERSAccessWorkflows();
                            List<Principal> principals = getOleAccessActivationService().getPrincipals(accessActivationWorkFlow);
                            OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = accessWorkflowList.get(accessWorkflowList.size() - 1);
                            StringBuffer currentOwnerBuffer = new StringBuffer();
                            if (principals != null && principals.size() > 0) {
                                oleeResourceAccess.setAccessStatus(accessActivationWorkFlow.getStatus());
                                oleeResourceAccess.setAdHocUserExists(false);
                                oleeResourceAccessWorkflow.setStatus(accessActivationWorkFlow.getStatus());
                                List<AdHocRouteRecipient> adHocRouteRecipientList = combineAdHocRecipients(form);
                                populateAdHocRouteRecipients(maintenanceDocument, currentOwnerBuffer, adHocRouteRecipientList, principals);
                                if (currentOwnerBuffer.length() > 0) {
                                    oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));
                                }
                                actionRequestService.deleteByDocumentId(maintenanceDocument.getDocumentNumber());
                                if(StringUtils.isNotEmpty(accessActivationWorkFlow.getRoleId())) {
                                    Role role = KimApiServiceLocator.getRoleService().getRole(accessActivationWorkFlow.getRoleId());
                                    getDocumentService().approveDocument(form.getDocument(), "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the  Role :" + role.getName(), adHocRouteRecipientList);
                                } else {
                                    getDocumentService().approveDocument(form.getDocument(), "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the  Role :" + "", adHocRouteRecipientList);
                                }
                                List<ActionTakenValue> actionTakenList = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(maintenanceDocument.getDocumentNumber());
                                ActionTakenValue actionTakenValue = (ActionTakenValue) actionTakenList.get(actionTakenList.size() - 1);
                                actionTakenValue.setAnnotation("Approved Status : " + previousStatus);
                                KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                                KEWServiceLocator.getActionListService().deleteByDocumentId(maintenanceDocument.getDocumentNumber());
                                getOleeResourceHelperService().deleteMaintenanceLock();
                                break;
                            }
                        } else {
                            oleeResourceAccess.setAdHocUserExists(false);
                            getOleeResourceHelperService().setWorkflowCompletedStatusAfterApproval(oleeResourceAccess, maintenanceDocument);
                            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_APPROVED);
                            return getUIFModelAndView(form);
                        }
                    }
                }
            }
        }
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_APPROVED);
        return super.navigate(form, result, request, response);
        /*}*/
    }


    @RequestMapping(params = "methodToCall=startAccessActivation")
    public ModelAndView startAccessActivation(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {

        DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = ((MaintenanceDocumentForm) form).getDocument();
        org.kuali.rice.krad.maintenance.MaintenanceDocument oldMaintenanceDocument =  (MaintenanceDocument)documentService.getByDocumentHeaderId(maintenanceDocument.getDocumentNumber());
        OLEEResourceAccessActivation oldOleeResourceAccess = new OLEEResourceAccessActivation();
        if(oldMaintenanceDocument != null){
            oldOleeResourceAccess  = (OLEEResourceAccessActivation)oldMaintenanceDocument.getNewMaintainableObject().getDataObject();
        }
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) form).getDocument().getNewMaintainableObject().getDataObject();
        boolean flag = false;
        if (oleeResourceAccess.getWorkflowId() == null || (oleeResourceAccess.getWorkflowId() != null && oleeResourceAccess.getWorkflowId().trim().isEmpty())) {
            GlobalVariables.getMessageMap().putError("document.newMaintainableObject.dataObject.workflowId", OLEConstants.NO_WORKFLOW);
            oleeResourceAccess.setWorkflowId(null);
            flag = true;
        }
        if (oleeResourceAccess.getWorkflowDescription() == null || (oleeResourceAccess.getWorkflowDescription() != null && oleeResourceAccess.getWorkflowDescription().trim().isEmpty())) {
            GlobalVariables.getMessageMap().putError("document.newMaintainableObject.dataObject.workflowDescription", OLEConstants.NO_DESCRIPTION);
            flag = true;
        }
        if (flag) {
            return getUIFModelAndView(form);
        }
        if(oleeResourceAccess != null && oleeResourceAccess.getLastRecordLoadDate() != null && oldOleeResourceAccess != null && !oleeResourceAccess.getLastRecordLoadDate().equals(oldOleeResourceAccess.getLastRecordLoadDate())){
            OLEEResourceEventLog oleeResourceEventLog = new OLEEResourceEventLog();
            Timestamp timestamp = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
            oleeResourceEventLog.setEventDate(timestamp);
            oleeResourceEventLog.setEventUser(GlobalVariables.getUserSession().getPrincipalName());
            oleeResourceEventLog.setEventTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.getEventTypeName();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
            oleeResourceEventLog.setEventNote("Last Record Load Date has been updated : " +simpleDateFormat.format(oleeResourceAccess.getLastRecordLoadDate()));
            oleeResourceEventLog.setOleERSIdentifier(oleeResourceAccess.getOleERSIdentifier());
            oleeResourceEventLog.setLogTypeId(OLEConstants.OLEEResourceRecord.ID_FOR_LOG_TYPE_SYSTEM);
            oleeResourceEventLog.setSaveFlag(true);
            getBusinessObjectService().save(oleeResourceEventLog);
        }
        if (!processAdHocRecipients(form, true, oleeResourceAccess)){
            oleeResourceAccess.setAdHocUserExists(false);
            OLEAccessActivationWorkFlow accessActivationWorkFlow = null;
            getOleeResourceHelperService().deleteMaintenanceLock();
            Map<String, String> accessConfigMap = new HashMap<String, String>();
            accessConfigMap.put("accessActivationConfigurationId", oleeResourceAccess.getWorkflowId());
            List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = (List<OLEAccessActivationWorkFlow>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEAccessActivationWorkFlow.class, accessConfigMap, "orderNo", true);
            if (oleAccessActivationWorkFlows != null && oleAccessActivationWorkFlows.size() > 0) {
                boolean found = false;
                OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = new OLEEResourceAccessWorkflow();
                oleeResourceAccessWorkflow.setDescription(oleeResourceAccess.getWorkflowDescription());
                oleeResourceAccessWorkflow.setLastApproved(new Timestamp(System.currentTimeMillis()));
                oleeResourceAccess.getOleERSAccessWorkflows().add(oleeResourceAccessWorkflow);
                for (int i = 0; i < oleAccessActivationWorkFlows.size(); i++) {
                    accessActivationWorkFlow = oleAccessActivationWorkFlows.get(i);
                    oleeResourceAccess.setAccessStatus(accessActivationWorkFlow.getStatus());
                    List<Principal> principals = getOleAccessActivationService().getPrincipals(accessActivationWorkFlow);
                    StringBuffer currentOwnerBuffer = new StringBuffer();
                    if (principals != null && principals.size() > 0) {
                        oleeResourceAccessWorkflow.setStatus(accessActivationWorkFlow.getStatus());
                        found = true;
                        List<AdHocRouteRecipient> adHocRouteRecipientList = new ArrayList<AdHocRouteRecipient>();
                        populateAdHocRouteRecipients(maintenanceDocument, currentOwnerBuffer, adHocRouteRecipientList, principals);
                        if (currentOwnerBuffer.length() > 0) {
                            oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));
                        }
                        try {
                            if(StringUtils.isNotEmpty(accessActivationWorkFlow.getRoleId())) {
                                Role role = KimApiServiceLocator.getRoleService().getRole(accessActivationWorkFlow.getRoleId());
                                getDocumentService().routeDocument(maintenanceDocument, "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the Role : " + role.getName(), adHocRouteRecipientList);
                            } else {
                                getDocumentService().routeDocument(maintenanceDocument, "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the Role : " + "", adHocRouteRecipientList);
                            }
                            List<ActionTakenValue> actionTakenList = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(maintenanceDocument.getDocumentNumber());
                            ActionTakenValue actionTakenValue = (ActionTakenValue) actionTakenList.get(actionTakenList.size() - 1);
                            actionTakenValue.setAnnotation("Initiated the access activation workflow");
                            KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                            getOleeResourceHelperService().deleteMaintenanceLock();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if (!found) {
                    getOleeResourceHelperService().setWorkflowCompletedStatus(oleeResourceAccess, maintenanceDocument, false);
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL);
                    return getUIFModelAndView(form);
                }
            }
        }
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL);
        return getUIFModelAndView(form);
    }

    private boolean processAdHocRecipients(DocumentFormBase form, boolean startAccessActivation, OLEEResourceAccessActivation oleEResourceAccess) {
        boolean processedAdHocRecipients = false;
        MaintenanceDocument document = ((MaintenanceDocumentForm) form).getDocument();
        StringBuffer currentOwnerBuffer = new StringBuffer();
        List<AdHocRouteRecipient> adHocRouteRecipientList = new ArrayList<>();
        List<OLEAdHocRoutingForAccessActivation> oleAdHocRoutingForAccessActivations = oleEResourceAccess.getOleAdHocRoutingForAccessActivations();
        document.setAdHocRoutePersons(new ArrayList<AdHocRoutePerson>());
        if (CollectionUtils.isNotEmpty(oleAdHocRoutingForAccessActivations)) {
            getAdHocRouteRecipientList(document, currentOwnerBuffer, adHocRouteRecipientList, oleAdHocRoutingForAccessActivations);
            oleEResourceAccess.setAdHocAccessStatus(oleEResourceAccess.getAdHocAccessStatusDummy());
            oleEResourceAccess.setAdHocAccessStatusDummy("");
            oleEResourceAccess.setOleAdHocRoutingForAccessActivations(new ArrayList<OLEAdHocRoutingForAccessActivation>());
            if (adHocRouteRecipientList.size() > 0) {
                processedAdHocRecipients = true;
                try {
                    if (startAccessActivation) {
                        oleEResourceAccess.setAdHocUserExists(true);
                        OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = new OLEEResourceAccessWorkflow();
                        oleeResourceAccessWorkflow.setDescription(oleEResourceAccess.getWorkflowDescription());
                        oleeResourceAccessWorkflow.setLastApproved(new Timestamp(System.currentTimeMillis()));
                        oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));
                        oleEResourceAccess.getOleERSAccessWorkflows().add(oleeResourceAccessWorkflow);
                        oleEResourceAccess.setAccessStatus("");
                        getDocumentService().routeDocument(document, "Needed Approval from the members : " + "", adHocRouteRecipientList);
                        List<ActionTakenValue> actionTakenList = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(document.getDocumentNumber());
                        ActionTakenValue actionTakenValue = (ActionTakenValue) actionTakenList.get(actionTakenList.size() - 1);
                        actionTakenValue.setAnnotation("Initiated the access activation workflow");
                        KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                    } else {
                        if (StringUtils.isBlank(oleEResourceAccess.getAccessStatus())){
                            oleEResourceAccess.setAdHocUserExists(true);
                        }else{
                            oleEResourceAccess.setAdHocUserExists(false);
                        }
                        List<OLEEResourceAccessWorkflow> accessWorkflowList = oleEResourceAccess.getOleERSAccessWorkflows();
                        OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = accessWorkflowList.get(accessWorkflowList.size() - 1);
                        oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));

                        ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
                        actionRequestService.deleteByDocumentId(document.getDocumentNumber());
                        getOleeResourceHelperService().deleteMaintenanceLock();
                        getDocumentService().approveDocument(document, "Needed Approval from the members : " + "", adHocRouteRecipientList);
                        List<ActionTakenValue> actionTakenList = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(document.getDocumentNumber());
                        ActionTakenValue actionTakenValue = (ActionTakenValue) actionTakenList.get(actionTakenList.size() - 1);
                        actionTakenValue.setAnnotation("Approved AdHoc Status : " + oleEResourceAccess.getAdHocAccessStatus());
                        KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                        KEWServiceLocator.getActionListService().deleteByDocumentId(document.getDocumentNumber());
                    }
                    getOleeResourceHelperService().deleteMaintenanceLock();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return processedAdHocRecipients;
    }

    private void getAdHocRouteRecipientList(MaintenanceDocument document, StringBuffer currentOwnerBuffer, List<AdHocRouteRecipient> adHocRouteRecipientList, List<OLEAdHocRoutingForAccessActivation> oleAdHocRoutingForAccessActivations) {
        for (OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation : oleAdHocRoutingForAccessActivations) {
            if (OLEConstants.SELECTOR_ROLE.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
                List<Principal> principals = new ArrayList<>();
                Collection collection = getRoleMemberIds(oleAdHocRoutingForAccessActivation);
                if(CollectionUtils.isNotEmpty(collection)) {
                    List<String> memberIds = new ArrayList<String>();
                    memberIds.addAll(collection);
                    principals = KimApiServiceLocator.getIdentityService().getPrincipals(memberIds);
                }
                if(CollectionUtils.isNotEmpty(principals)) {
                    populateAdHocRouteRecipients(document, currentOwnerBuffer, adHocRouteRecipientList, principals);
                }
            } else if (OLEConstants.SELECTOR_GROUP.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
                List<Principal> principals = new ArrayList<>();
                List<String> memberIds = getGroupMemberIds(oleAdHocRoutingForAccessActivation);
                if(CollectionUtils.isNotEmpty(memberIds)) {
                    principals = KimApiServiceLocator.getIdentityService().getPrincipals(memberIds);
                }
                if(CollectionUtils.isNotEmpty(principals)) {
                    populateAdHocRouteRecipients(document, currentOwnerBuffer, adHocRouteRecipientList, principals);
                }
            } else if (OLEConstants.SELECTOR_PERSON.equalsIgnoreCase(oleAdHocRoutingForAccessActivation.getAdHocRecipientSelector())) {
                Person person = null;
                if (StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocPrincipalId())) {
                    person = getPersonService().getPerson(oleAdHocRoutingForAccessActivation.getAdHocPrincipalId());
                } else if (StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocPrincipalName())) {
                    person = getPersonService().getPersonByPrincipalName(oleAdHocRoutingForAccessActivation.getAdHocPrincipalName());
                }
                if (person != null) {
                    currentOwnerBuffer.append(person.getPrincipalName() + ",");
                    AdHocRoutePerson adHocRoutePerson = new AdHocRoutePerson();
                    adHocRoutePerson.setId(person.getPrincipalId());
                    adHocRoutePerson.setName(person.getPrincipalName());
                    adHocRoutePerson.setActionRequested("A");
                    adHocRoutePerson.setdocumentNumber(document.getDocumentNumber());
                    adHocRoutePerson.setType(0);
                    adHocRouteRecipientList.add(adHocRoutePerson);
                }
            }
        }
    }

    private Collection getRoleMemberIds(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        Collection collection = null;
        if (StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocRoleId())) {
            Role role = KimApiServiceLocator.getRoleService().getRole(oleAdHocRoutingForAccessActivation.getAdHocRoleId());
            if(null != role) {
                collection = KimApiServiceLocator.getRoleService().getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
            }
        } else if (StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocRoleName())) {
            RoleBo roleBo = getRoleBo(oleAdHocRoutingForAccessActivation);
            if(null != roleBo) {
                collection = KimApiServiceLocator.getRoleService().getRoleMemberPrincipalIds(roleBo.getNamespaceCode(), roleBo.getName(), new HashMap<String, String>());
            }
        }
        return collection;
    }

    private RoleBo getRoleBo(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        RoleBo roleBo = null;
        Map<String, String> map = new HashMap<>();
        map.put(OLEConstants.ALERT_ROLE_NAME, oleAdHocRoutingForAccessActivation.getAdHocRoleName());
        List<RoleBo> roleBoList = (List<RoleBo>) KRADServiceLocator.getBusinessObjectService()
                .findMatching(RoleBo.class, map);
        if (CollectionUtils.isNotEmpty(roleBoList)) {
            roleBo = roleBoList.get(0);
        }
        return roleBo;
    }

    private List<String> getGroupMemberIds(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        List<String> memberIds = new ArrayList<>();
        if(StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocGroupId() )) {
            memberIds = KimApiServiceLocator.getGroupService().getMemberPrincipalIds(oleAdHocRoutingForAccessActivation.getAdHocGroupId());
        } else if(StringUtils.isNotBlank(oleAdHocRoutingForAccessActivation.getAdHocGroupName())) {
            GroupBo groupBo = getGroupBo(oleAdHocRoutingForAccessActivation);
            if(null != groupBo) {
                memberIds = KimApiServiceLocator.getGroupService().getMemberPrincipalIds(groupBo.getId());
            }
        }
        return memberIds;
    }

    private GroupBo getGroupBo(OLEAdHocRoutingForAccessActivation oleAdHocRoutingForAccessActivation) {
        GroupBo groupBo = null;
        Map<String,String> map = new HashMap<>();
        map.put(OLEConstants.ALERT_GROUP_NAME, oleAdHocRoutingForAccessActivation.getAdHocGroupName());
        List<GroupBo> groupBoList = (List<GroupBo>) KRADServiceLocator.getBusinessObjectService().findMatching(GroupBo.class, map);
        if(CollectionUtils.isNotEmpty(groupBoList)) {
            groupBo = groupBoList.get(0);
        }
        return groupBo;
    }

    private void populateAdHocRouteRecipients(MaintenanceDocument document, StringBuffer currentOwnerBuffer, List<AdHocRouteRecipient> adHocRouteRecipientList, List<Principal> principals) {
        for (Principal principal : principals) {
            currentOwnerBuffer.append(principal.getPrincipalName() + ",");
            AdHocRoutePerson adHocRoutePerson = new AdHocRoutePerson();
            adHocRoutePerson.setId(principal.getPrincipalId());
            adHocRoutePerson.setName(principal.getPrincipalName());
            adHocRoutePerson.setActionRequested("A");
            adHocRoutePerson.setdocumentNumber(document.getDocumentNumber());
            adHocRoutePerson.setType(0);
            adHocRouteRecipientList.add(adHocRoutePerson);
        }
    }

}

