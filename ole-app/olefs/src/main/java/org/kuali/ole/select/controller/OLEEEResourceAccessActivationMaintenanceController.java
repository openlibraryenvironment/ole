package org.kuali.ole.select.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLEEResourceAccessActivation;
import org.kuali.ole.select.bo.OLEEResourceNotes;
import org.kuali.ole.select.document.OLEEResourceAccessWorkflow;
import org.kuali.ole.select.document.OLEEResourceEventLog;
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
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
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
import java.sql.Timestamp;
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
            KRADServiceLocator.getBusinessObjectService().save(oleeResourceEventLog);
        }
        String previousStatus = oleeResourceAccess.getAccessStatus();
        ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
        Map<String, String> accessConfigMap = new HashMap<String, String>();
        accessConfigMap.put("accessActivationConfigurationId", oleeResourceAccess.getWorkflowId());
        List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = (List<OLEAccessActivationWorkFlow>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEAccessActivationWorkFlow.class, accessConfigMap, "orderNo", true);
        OLEAccessActivationWorkFlow accessActivationWorkFlow = null;
        if (oleAccessActivationWorkFlows != null && oleAccessActivationWorkFlows.size() > 0) {
            for (int i = 0; i < oleAccessActivationWorkFlows.size(); i++) {
                if (oleAccessActivationWorkFlows.get(i).getStatus().equals(oleeResourceAccess.getAccessStatus())) {
                    if (i + 1 < oleAccessActivationWorkFlows.size()) {
                        accessActivationWorkFlow = oleAccessActivationWorkFlows.get(i + 1);
                        oleeResourceAccess.setAccessStatus(accessActivationWorkFlow.getStatus());
                        List<OLEEResourceAccessWorkflow> accessWorkflowList = oleeResourceAccess.getOleERSAccessWorkflows();
                        List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
                        org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
                        List<Principal> principals = getOleAccessActivationService().getPrincipals(accessActivationWorkFlow);
                        OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = accessWorkflowList.get(accessWorkflowList.size() - 1);
                        StringBuffer currentOwnerBuffer = new StringBuffer();
                        AdHocRoutePerson adHocRoutePerson;
                        if (principals != null && principals.size() > 0) {
                            oleeResourceAccessWorkflow.setStatus(accessActivationWorkFlow.getStatus());
                            for (Principal principal : principals) {
                                currentOwnerBuffer.append(principal.getPrincipalName() + ",");
                                adHocRoutePerson = new AdHocRoutePerson();
                                adHocRoutePerson.setId(principal.getPrincipalId());
                                adHocRoutePerson.setName(principal.getPrincipalName());
                                adHocRoutePerson.setActionRequested("A");
                                adHocRoutePerson.setdocumentNumber(form.getDocument().getDocumentNumber());
                                adHocRoutePerson.setType(0);
                                adHocRouteRecipients.add(adHocRoutePerson);
                            }
                            if (currentOwnerBuffer.length() > 0) {
                                oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));
                            }
                            List<AdHocRouteRecipient> adHocRouteRecipientList = combineAdHocRecipients(form);
                            adHocRouteRecipientList.addAll(adHocRouteRecipients);
                            actionRequestService.deleteByDocumentId(maintenanceDocument.getDocumentNumber());
                            if(StringUtils.isNotEmpty(accessActivationWorkFlow.getRoleId())) {
                                Role role = roleService.getRole(accessActivationWorkFlow.getRoleId());
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
                        getOleeResourceHelperService().setWorkflowCompletedStatusAfterApproval(oleeResourceAccess, maintenanceDocument);
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_APPROVED);
                        return getUIFModelAndView(form);
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
            KRADServiceLocator.getBusinessObjectService().save(oleeResourceEventLog);
        }
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
                List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
                org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
                List<Principal> principals = getOleAccessActivationService().getPrincipals(accessActivationWorkFlow);
                StringBuffer currentOwnerBuffer = new StringBuffer();
                AdHocRoutePerson adHocRoutePerson;
                if (principals != null && principals.size() > 0) {
                    oleeResourceAccessWorkflow.setStatus(accessActivationWorkFlow.getStatus());
                    found = true;
                    for (Principal principal : principals) {
                        currentOwnerBuffer.append(principal.getPrincipalName() + ",");
                        adHocRoutePerson = new AdHocRoutePerson();
                        adHocRoutePerson.setId(principal.getPrincipalId());
                        adHocRoutePerson.setName(principal.getPrincipalName());
                        adHocRoutePerson.setActionRequested("A");
                        adHocRoutePerson.setdocumentNumber(maintenanceDocument.getDocumentNumber());
                        adHocRoutePerson.setType(0);
                        adHocRouteRecipients.add(adHocRoutePerson);
                    }
                    if (currentOwnerBuffer.length() > 0) {
                        oleeResourceAccessWorkflow.setCurrentOwner(currentOwnerBuffer.substring(0, currentOwnerBuffer.length() - 1));
                    }
                    List<AdHocRouteRecipient> adHocRouteRecipientList = new ArrayList<AdHocRouteRecipient>();
                    adHocRouteRecipientList.addAll(adHocRouteRecipients);
                    try {
                        if(StringUtils.isNotEmpty(accessActivationWorkFlow.getRoleId())) {
                            Role role = roleService.getRole(accessActivationWorkFlow.getRoleId());
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
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL);
        return getUIFModelAndView(form);
    }

}

