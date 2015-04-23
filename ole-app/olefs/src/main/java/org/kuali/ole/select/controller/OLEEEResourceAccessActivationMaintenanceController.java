package org.kuali.ole.select.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLEEResourceAccessActivation;
import org.kuali.ole.select.bo.OLEEResourceNotes;
import org.kuali.ole.select.document.OLEEResourceAccessWorkflow;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
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

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if (oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
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
        MaintenanceDocumentForm forms = (MaintenanceDocumentForm) form;
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = ((MaintenanceDocumentForm) form).getDocument();
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) form).getDocument().getNewMaintainableObject().getDataObject();
        String previousStatus = oleeResourceAccess.getAccessStatus();
        ActionRequestService actionRequestService = KEWServiceLocator.getActionRequestService();
        /*boolean singleApproval = false;
        if (singleApproval) {
            if (actionRequestService.findAllPendingRequests(maintenanceDocument.getDocumentNumber()) != null && actionRequestService.findAllPendingRequests(maintenanceDocument.getDocumentNumber()).size() == 1) {
                Map<String, String> accessConfigMap = new HashMap<String, String>();
                accessConfigMap.put("accessActivationConfigurationId", oleeResourceAccess.getWorkflowId());
                List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = (List<OLEAccessActivationWorkFlow>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEAccessActivationWorkFlow.class, accessConfigMap, "orderNo", true);
                OLEAccessActivationWorkFlow accessActivationWorkFlow = null;
                if (oleAccessActivationWorkFlows != null && oleAccessActivationWorkFlows.size() > 0) {
                    for (int i = 0; i < oleAccessActivationWorkFlows.size(); i++) {
                        if (oleAccessActivationWorkFlows.get(i).getStatus().equals(oleeResourceAccess.getAccessStatus())) {
                            if (i + 1 < oleAccessActivationWorkFlows.size()) {
                                accessActivationWorkFlow = oleAccessActivationWorkFlows.get(i + 1);
                                break;
                            } else {
                                DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(maintenanceDocument.getDocumentNumber());
                                documentBo.setDocRouteStatus("S");
                                KEWServiceLocator.getRouteHeaderService().saveRouteHeader(documentBo);
                                getDocumentService().saveDocument(maintenanceDocument);
                                deleteMaintenanceLock();
                                List<ActionRequestValue> actionRequestValueList = actionRequestService.findAllPendingRequests(maintenanceDocument.getDocumentNumber());
                                KEWServiceLocator.getActionRequestService().deleteByDocumentId(maintenanceDocument.getDocumentNumber());
                                ActionTakenValue actionTakenValue;
                                List<ActionTakenValue> actionTakenValueList = new ArrayList<ActionTakenValue>();
                                for (ActionRequestValue actionRequestValue : actionRequestValueList) {
                                    if (actionRequestValue.getPrincipalId().equalsIgnoreCase(GlobalVariables.getUserSession().getPrincipalId())) {
                                        actionTakenValue = new ActionTakenValue();
                                        actionTakenValue.setAnnotation("Approved status : " + oleeResourceAccess.getAccessStatus());
                                        actionTakenValue.setActionDate(new Timestamp(System.currentTimeMillis()));
                                        actionTakenValue.setActionTaken("A");
                                        actionTakenValue.setDocumentId(actionRequestValue.getDocumentId());
                                        actionTakenValue.setPrincipalId(actionRequestValue.getPrincipalId());
                                        actionTakenValue.setDocVersion(1);
                                        // actionTakenValueList.add(actionTakenValue);
                                        KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                                    }
                                }
                                return getUIFModelAndView(form);
                            }
                        }
                    }
                }
                oleeResourceAccess.setAccessStatus(accessActivationWorkFlow.getStatus());
                List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
                org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
                Role role = roleService.getRole(accessActivationWorkFlow.getRoleId());
                Collection<String> principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
                IdentityService identityService = KimApiServiceLocator.getIdentityService();
                List<String> principalList = new ArrayList<String>();
                principalList.addAll(principalIds);
                List<Principal> principals = identityService.getPrincipals(principalList);
                AdHocRoutePerson adHocRoutePerson;
                if (principals != null && principals.size() > 0) {
                    for (Principal principal : principals) {
                        adHocRoutePerson = new AdHocRoutePerson();
                        adHocRoutePerson.setId(principal.getPrincipalId());
                        adHocRoutePerson.setName(principal.getPrincipalName());
                        adHocRoutePerson.setActionRequested("A");
                        adHocRoutePerson.setdocumentNumber(form.getDocument().getDocumentNumber());
                        adHocRoutePerson.setType(0);
                        adHocRouteRecipients.add(adHocRoutePerson);
                    }
                }
                List<AdHocRouteRecipient> adHocRouteRecipientList = combineAdHocRecipients(form);
                adHocRouteRecipientList.addAll(adHocRouteRecipients);
                getDocumentService().approveDocument(form.getDocument(), "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the  Role :" + role.getName(), adHocRouteRecipientList);
                List<ActionTakenValue> actionTakenList = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(maintenanceDocument.getDocumentNumber());
                ActionTakenValue actionTakenValue = (ActionTakenValue) actionTakenList.get(actionTakenList.size() - 1);
                actionTakenValue.setAnnotation("Approved Status : " + previousStatus);
                KEWServiceLocator.getActionTakenService().saveActionTaken(actionTakenValue);
                deleteMaintenanceLock();

                return super.navigate(form, result, request, response);
            } else {
                getDocumentService().approveDocument(form.getDocument(), "Approved status : " + oleeResourceAccess.getAccessStatus(), combineAdHocRecipients(form));
                deleteMaintenanceLock();
                return super.navigate(form, result, request, response);
            }
        } else {*/
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
                        Role role = roleService.getRole(accessActivationWorkFlow.getRoleId());
                        Collection<String> principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
                        IdentityService identityService = KimApiServiceLocator.getIdentityService();
                        List<String> principalList = new ArrayList<String>();
                        principalList.addAll(principalIds);
                        List<Principal> principals = identityService.getPrincipals(principalList);
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
                            getDocumentService().approveDocument(form.getDocument(), "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the  Role :" + role.getName(), adHocRouteRecipientList);
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
                        return getUIFModelAndView(form);
                    }
                }
            }
        }
        return super.navigate(form, result, request, response);
        /*}*/
    }


    @RequestMapping(params = "methodToCall=startAccessActivation")
    public ModelAndView startAccessActivation(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        org.kuali.rice.krad.maintenance.MaintenanceDocument maintenanceDocument = ((MaintenanceDocumentForm) form).getDocument();
        OLEEResourceAccessActivation oleeResourceAccess = (OLEEResourceAccessActivation) ((MaintenanceDocumentForm) form).getDocument().getNewMaintainableObject().getDataObject();
        if (oleeResourceAccess.getWorkflowId() == null || (oleeResourceAccess.getWorkflowId() != null && oleeResourceAccess.getWorkflowId().trim().isEmpty())) {
            GlobalVariables.getMessageMap().putError("workflowId", OLEConstants.NO_WORKFLOW);
            oleeResourceAccess.setWorkflowId(null);
            return getUIFModelAndView(form);
        }
        if (oleeResourceAccess.getWorkflowDescription() == null || (oleeResourceAccess.getWorkflowDescription() != null && oleeResourceAccess.getWorkflowDescription().trim().isEmpty())) {
            GlobalVariables.getMessageMap().putError("workflowDescription", OLEConstants.NO_DESCRIPTION);
            return getUIFModelAndView(form);
        }
        OLEAccessActivationWorkFlow accessActivationWorkFlow = null;
        getOleeResourceHelperService().deleteMaintenanceLock();
        Map<String, String> accessConfigMap = new HashMap<String, String>();
        accessConfigMap.put("accessActivationConfigurationId", oleeResourceAccess.getWorkflowId());
        List<OLEAccessActivationWorkFlow> oleAccessActivationWorkFlows = (List<OLEAccessActivationWorkFlow>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEAccessActivationWorkFlow.class, accessConfigMap, "orderNo", true);
        if (oleAccessActivationWorkFlows != null && oleAccessActivationWorkFlows.size() > 0) {
            boolean found = false;
            for (int i = 0; i < oleAccessActivationWorkFlows.size(); i++) {
                accessActivationWorkFlow = oleAccessActivationWorkFlows.get(i);
                oleeResourceAccess.setAccessStatus(accessActivationWorkFlow.getStatus());
                List<AdHocRoutePerson> adHocRouteRecipients = new ArrayList<AdHocRoutePerson>();
                org.kuali.rice.kim.api.role.RoleService roleService = (org.kuali.rice.kim.api.role.RoleService) KimApiServiceLocator.getRoleService();
                Role role = roleService.getRole(accessActivationWorkFlow.getRoleId());
                Collection<String> principalIds = (Collection<String>) roleService.getRoleMemberPrincipalIds(role.getNamespaceCode(), role.getName(), new HashMap<String, String>());
                IdentityService identityService = KimApiServiceLocator.getIdentityService();
                List<String> principalList = new ArrayList<String>();
                principalList.addAll(principalIds);
                List<Principal> principals = identityService.getPrincipals(principalList);
                OLEEResourceAccessWorkflow oleeResourceAccessWorkflow = new OLEEResourceAccessWorkflow();
                StringBuffer currentOwnerBuffer = new StringBuffer();
                oleeResourceAccessWorkflow.setDescription(oleeResourceAccess.getWorkflowDescription());
                oleeResourceAccessWorkflow.setLastApproved(new Timestamp(System.currentTimeMillis()));
                oleeResourceAccess.getOleERSAccessWorkflows().add(oleeResourceAccessWorkflow);
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
                        getDocumentService().routeDocument(maintenanceDocument, "Needed Approval for the status : " + accessActivationWorkFlow.getStatus() + " from the members of the Role : " + role.getName(), adHocRouteRecipientList);
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
                return getUIFModelAndView(form);
            }
        }
        return getUIFModelAndView(form);
    }

}

