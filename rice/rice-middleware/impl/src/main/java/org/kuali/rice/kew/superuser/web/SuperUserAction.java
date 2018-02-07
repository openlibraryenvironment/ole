/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.superuser.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.DocumentActionParameters;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.web.AppSpecificRouteRecipient;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;

/**
 * A Struts Action which provides super user functionality.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserAction extends KewKualiAction {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SuperUserAction.class);
    public static final String UNAUTHORIZED = "authorizationFailure";

    //public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //	defaultDispatch(mapping, form, request, response);
    //}

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        initForm(request, form);
        return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ((SuperUserForm) form).getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward displaySuperUserDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SuperUserForm superUserForm = (SuperUserForm) form;
        superUserForm.setDocHandlerUrl(KewApiConstants.DOC_HANDLER_REDIRECT_PAGE + "?docId="
                + superUserForm.getDocumentId() + "&" + KewApiConstants.COMMAND_PARAMETER + "="
                + KewApiConstants.SUPERUSER_COMMAND);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward routeLevelApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering routeLevelApprove()...");
        SuperUserForm superUserForm = (SuperUserForm) form;
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());

        documentActions.superUserNodeApprove(parameters, superUserForm.isRunPostProcessorLogic(),
                superUserForm.getDestNodeName());
        saveDocumentMessage("general.routing.superuser.routeLevelApproved", request, superUserForm.getDocumentId(),
                null);
        LOG.info("exiting routeLevelApprove()...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering approve() ...");
        SuperUserForm superUserForm = (SuperUserForm) form;
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());
        documentActions.superUserBlanketApprove(parameters, superUserForm.isRunPostProcessorLogic());
        saveDocumentMessage("general.routing.superuser.approved", request, superUserForm.getDocumentId(), null);
        LOG.info("exiting approve() ...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward disapprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering disapprove() ...");
        SuperUserForm superUserForm = (SuperUserForm) form;
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());
        documentActions.superUserDisapprove(parameters, superUserForm.isRunPostProcessorLogic());
        saveDocumentMessage("general.routing.superuser.disapproved", request, superUserForm.getDocumentId(), null);
        LOG.info("exiting disapprove() ...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering cancel() ...");
        SuperUserForm superUserForm = (SuperUserForm) form;
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());
        documentActions.superUserCancel(parameters, superUserForm.isRunPostProcessorLogic());
        saveDocumentMessage("general.routing.superuser.canceled", request, superUserForm.getDocumentId(), null);
        LOG.info("exiting cancel() ...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward returnToPreviousNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering returnToPreviousNode() ...");
        SuperUserForm superUserForm = (SuperUserForm) form;
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());
        documentActions.superUserReturnToPreviousNode(parameters, superUserForm.isRunPostProcessorLogic(),
                ReturnPoint.create(superUserForm.getReturnDestNodeName()));
        saveDocumentMessage("general.routing.returnedToPreviousNode", request, "document", superUserForm
                .getReturnDestNodeName().toString());
        LOG.info("exiting returnToPreviousRouteLevel() ...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return defaultDispatch(mapping, form, request, response);
    }

    public ActionForward actionRequestApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOG.info("entering actionRequestApprove() ...");
        SuperUserForm superUserForm = (SuperUserForm) form;

        // Retrieve the relevant arguments from the "methodToCall" parameter.
        String methodToCallAttr = (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        superUserForm.setActionTakenRecipientCode(StringUtils.substringBetween(methodToCallAttr,
                KRADConstants.METHOD_TO_CALL_PARM1_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM1_RIGHT_DEL));
        superUserForm.setActionTakenNetworkId(StringUtils.substringBetween(methodToCallAttr,
                KRADConstants.METHOD_TO_CALL_PARM2_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM2_RIGHT_DEL));
        superUserForm.setActionTakenWorkGroupId(StringUtils.substringBetween(methodToCallAttr,
                KRADConstants.METHOD_TO_CALL_PARM4_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM4_RIGHT_DEL));
        superUserForm.setActionTakenActionRequestId(StringUtils.substringBetween(methodToCallAttr,
                KRADConstants.METHOD_TO_CALL_PARM5_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM5_RIGHT_DEL));

        LOG.debug("Routing super user action request approve action");
        boolean runPostProcessorLogic = ArrayUtils.contains(superUserForm.getActionRequestRunPostProcessorCheck(),
                superUserForm.getActionTakenActionRequestId());
        String documentId = superUserForm.getRouteHeader().getDocumentId();
        WorkflowDocumentActionsService documentActions = getWorkflowDocumentActionsService(documentId);
        DocumentActionParameters parameters = DocumentActionParameters.create(documentId, getUserSession(request)
                .getPrincipalId(), superUserForm.getAnnotation());
        documentActions.superUserTakeRequestedAction(parameters, runPostProcessorLogic,
                superUserForm.getActionTakenActionRequestId());
        String messageString;
        String actionReqest = StringUtils.substringBetween(methodToCallAttr,
                KRADConstants.METHOD_TO_CALL_PARM6_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM6_RIGHT_DEL);
        if (actionReqest.equalsIgnoreCase("acknowledge")) {
            messageString = "general.routing.superuser.actionRequestAcknowledged";
        } else if (actionReqest.equalsIgnoreCase("FYI")) {
            messageString = "general.routing.superuser.actionRequestFYI";
        } else if (actionReqest.equalsIgnoreCase("complete")) {
            messageString = "general.routing.superuser.actionRequestCompleted";
        } else if (actionReqest.equalsIgnoreCase("approved")) {
            messageString = "general.routing.superuser.actionRequestApproved";
        } else {
            messageString = "general.routing.superuser.actionRequestApproved";
        }
        saveDocumentMessage(messageString, request, superUserForm.getDocumentId(),
                superUserForm.getActionTakenActionRequestId());
        LOG.info("exiting actionRequestApprove() ...");
        superUserForm.getActionRequests().clear();
        initForm(request, form);

        // If the action request was also an app specific request, remove it from the app specific route recipient list.
        int removalIndex = findAppSpecificRecipientIndex(superUserForm, superUserForm.getActionTakenActionRequestId());

        if (removalIndex >= 0) {
            superUserForm.getAppSpecificRouteList().remove(removalIndex);
        }

        return defaultDispatch(mapping, form, request, response);
    }

    /**
     * Finds the index in the app specific route recipient list of the recipient whose routing was
     * handled by the given action request.
     * 
     * @param superUserForm The SuperUserForm currently being processed.
     * @param actionRequestId The ID of the action request that handled the routing of the app
     *        specific recipient that is being removed.
     * @return The index of the app specific route recipient that was handled by the given action
     *         request, or -1 if no such recipient was found.
     */
    private int findAppSpecificRecipientIndex(SuperUserForm superUserForm, String actionRequestId) {
    	int tempIndex = 0;
    	for (Iterator<?> appRouteIter = superUserForm.getAppSpecificRouteList().iterator(); appRouteIter.hasNext();) {
    		String tempActnReqId = ((AppSpecificRouteRecipient) appRouteIter.next()).getActionRequestId();
    		if (StringUtils.equals(tempActnReqId, actionRequestId)) {
    			return tempIndex;
    		}
    		tempIndex++;
    	}
    	return -1;
    }

    public ActionForward initForm(HttpServletRequest request, ActionForm form) throws Exception {
        request.setAttribute("Constants", getServlet().getServletContext().getAttribute("KewApiConstants"));
        SuperUserForm superUserForm = (SuperUserForm) form;
        DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(
                superUserForm.getDocumentId());
        if(ObjectUtils.isNull(routeHeader)) {
            throw new ValidationException("No route header ID found.  Try searching for the document again using the super user document search.");
        }
        superUserForm.setRouteHeader(routeHeader);
        String principalId = getUserSession(request).getPrincipalId();
        boolean isAuthorized = KEWServiceLocator.getDocumentTypePermissionService().canAdministerRouting(principalId,
                routeHeader.getDocumentType());
        superUserForm.setAuthorized(isAuthorized);
        if (!isAuthorized) {
            saveDocumentMessage("general.routing.superuser.notAuthorized", request, superUserForm.getDocumentId(), null);
            return null;
        }

        superUserForm.setFutureNodeNames(KEWServiceLocator.getRouteNodeService().findFutureNodeNames(
                routeHeader.getDocumentId()));

        Collection actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(
                routeHeader.getDocumentId());
        Iterator requestIterator = actionRequests.iterator();
        while (requestIterator.hasNext()) {
            ActionRequestValue req = (ActionRequestValue) requestIterator.next();
            // if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equalsIgnoreCase(req.getActionRequested())) {
            superUserForm.getActionRequests().add(req);
            // }
        }

        superUserForm.setDocId(superUserForm.getDocumentId());
        if (superUserForm.getDocId() != null) {
            superUserForm.setWorkflowDocument(WorkflowDocumentFactory.loadDocument(getUserSession(request)
                    .getPrincipalId(), superUserForm.getDocId()));
            superUserForm.establishVisibleActionRequestCds();
        }

        return null;
    }

    private void saveDocumentMessage(String messageKey, HttpServletRequest request, String subVariable1,
            String subVariable2) {
        if (subVariable2 == null) {
            GlobalVariables.getMessageMap().putInfo("document", messageKey, subVariable1);
        } else {
            GlobalVariables.getMessageMap().putInfo("document", messageKey, subVariable1, subVariable2);
        }
    }

    public ActionForward routeToAppSpecificRecipient(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SuperUserForm superUserForm = (SuperUserForm) form;

        //super.routeToAppSpecificRecipient(mapping, form, request, response);
        //WorkflowRoutingForm routingForm = (WorkflowRoutingForm) form;
        String routeType = StringUtils.substringBetween(
                (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE),
                KRADConstants.METHOD_TO_CALL_PARM1_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM1_RIGHT_DEL);
        AppSpecificRouteRecipient recipient = null;
        if (KewApiConstants.PERSON.equals(routeType)) {
            recipient = superUserForm.getAppSpecificRouteRecipient();
            recipient.setActionRequested(superUserForm.getAppSpecificRouteActionRequestCd());
            superUserForm.setAppSpecificPersonId(recipient.getId());
        } else {
            recipient = superUserForm.getAppSpecificRouteRecipient2();
            recipient.setActionRequested(superUserForm.getAppSpecificRouteActionRequestCd2());
            superUserForm.setAppSpecificWorkgroupId(recipient.getId());
        }

        validateAppSpecificRoute(recipient);

        // Make sure that the requested action is still available.
        superUserForm.establishVisibleActionRequestCds();
        if (superUserForm.getAppSpecificRouteActionRequestCds().get(recipient.getActionRequested()) == null) {
            GlobalVariables.getMessageMap().putError("appSpecificRouteRecipient" +
                    ((KewApiConstants.WORKGROUP.equals(recipient.getType())) ? "2" : "") + ".id",
                    "appspecificroute.actionrequested.invalid");

            throw new ValidationException("The requested action of '" + recipient.getActionRequested()
                    + "' is no longer available for this document");
        }

        try {
            String routeNodeName = getAdHocRouteNodeName(superUserForm.getWorkflowDocument().getDocumentId());
            //if (KewApiConstants.PERSON.equals(recipient.getType())) {
            if (KewApiConstants.PERSON.equals(routeType)) {
                String recipientPrincipalId = KEWServiceLocator.getIdentityHelperService().getIdForPrincipalName(
                        recipient.getId());
                superUserForm.getWorkflowDocument().adHocToPrincipal(
                        ActionRequestType.fromCode(recipient.getActionRequested()), routeNodeName,
                        superUserForm.getAnnotation(), recipientPrincipalId, "", true);
            } else {
                String recipientGroupId = KEWServiceLocator.getIdentityHelperService().getIdForGroupName(
                        recipient.getNamespaceCode(), recipient.getId());
                superUserForm.getWorkflowDocument().adHocToGroup(
                        ActionRequestType.fromCode(recipient.getActionRequested()), routeNodeName,
                        superUserForm.getAnnotation(), recipientGroupId, "", true);
            }
        } catch (Exception e) {
            LOG.error("Error generating app specific route request", e);
            throw new WorkflowServiceErrorException("AppSpecific Route Error", new WorkflowServiceErrorImpl(
                    "AppSpecific Route Error", "appspecificroute.systemerror"));
        }

        superUserForm.getActionRequests().clear();
        initForm(request, form);

        // Retrieve the ID of the latest action request and store it with the app specific route recipient.
        ActionRequestValue latestActnReq = getLatestActionRequest(superUserForm);
        if (latestActnReq != null) {
            recipient.setActionRequestId(latestActnReq.getActionRequestId());
        }
        // Add the recipient to the list.
        superUserForm.getAppSpecificRouteList().add(recipient);
        superUserForm.resetAppSpecificRoute();

        return start(mapping, form, request, response);
    }

    /**
     * Searches the current action requests list for the most recent request, which is the one with
     * the highest ID.
     * @param superUserForm The SuperUserForm currently being processed.
     * @return The action request on the form with the highest ID, or null if no action requests
     *         exist in the list.
     */
    private ActionRequestValue getLatestActionRequest(SuperUserForm superUserForm) {
    	ActionRequestValue latestActnReq = null;
//    	long latestId = -1;
    	
    	// FIXME: KULRICE-5201 required the following refactor since action request ids are no longer numeric (and in any case the assumption that
    	// they are strictly ordinal by time of creation may be false)
    	List<ActionRequestValue> actionRequests = superUserForm.getActionRequests();
    	
    	if (actionRequests != null && actionRequests.size() > 0) {
	    	Collections.sort(actionRequests, new Comparator<ActionRequestValue>() {
	
				@Override
				// Should should by date in descending order
				public int compare(ActionRequestValue o1, ActionRequestValue o2) {
					if (o1 == null && o2 == null)
						return 0;
					if (o1 == null)
						return -1;
					if (o2 == null)
						return 1;
					
					if (o1.getCreateDate() == null && o2.getCreateDate() == null)
						return 0;
					if (o1.getCreateDate() == null)
						return -1;
					if (o2.getCreateDate() == null)
						return 1;
	
					return o2.getCreateDate().compareTo(o1.getCreateDate());
				}
	    	
	    	});

	    	// If the list above is sorted in descending order then the first item should be the most recent
	    	latestActnReq = actionRequests.get(0);
    	}
    	
    	// TODO: As part of KULRICE-5329 this change above needs to be verified and compared with code below
//    	// Search the list for the action request with the highest action request value.
//    	for (Iterator<?> actnReqIter = superUserForm.getActionRequests().iterator(); actnReqIter.hasNext();) {
//    		ActionRequestValue tmpActnReq = (ActionRequestValue) actnReqIter.next();
//    		if (tmpActnReq.getActionRequestId().longValue() > latestId) {
//    			latestActnReq = tmpActnReq;
//    			latestId = tmpActnReq.getActionRequestId().longValue();
//    		}
//    	}
    	return latestActnReq;
    }

    /**
     * Removes an existing AppSpecificRouteRecipient from the list.
     */
    public ActionForward removeAppSpecificRecipient(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SuperUserForm superUserForm = (SuperUserForm) form;
        // Make sure a valid route recipient index was specified in the "methodToCall" attribute.
        String strIndex = StringUtils.substringBetween(
                (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE),
                KRADConstants.METHOD_TO_CALL_PARM1_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM1_RIGHT_DEL);
        if (StringUtils.isBlank(strIndex)) {
            throw new WorkflowException("No adhoc route recipient index specified");
        }
        int removeIndex = Integer.parseInt(strIndex);
        if (removeIndex < 0 || removeIndex >= superUserForm.getAppSpecificRouteList().size()) {
            throw new WorkflowException("Invalid adhoc route recipient index specified");
        }
        // Remove the specified recipient from the routing, based on the recipient's ID and the ID of the action request that handled the recipient.
        AppSpecificRouteRecipient removedRec = (AppSpecificRouteRecipient) superUserForm.getAppSpecificRouteList().get(
                removeIndex);
        if (removedRec.getActionRequestId() != null) {
            superUserForm.getWorkflowDocument().revokeAdHocRequestById(removedRec.getActionRequestId().toString(), "");
        } else {
            AdHocRevoke adHocRevoke = null;
            // Set the ID according to whether the recipient is a person or a group.
            if (KewApiConstants.PERSON.equals(removedRec.getType())) {
                adHocRevoke = AdHocRevoke.createRevokeFromPrincipal(KEWServiceLocator.getIdentityHelperService()
                        .getIdForPrincipalName(removedRec.getId()));
            } else {
                adHocRevoke = AdHocRevoke.createRevokeFromGroup(KEWServiceLocator.getIdentityHelperService()
                        .getIdForGroupName(removedRec.getNamespaceCode(), removedRec.getId()));
            }
            superUserForm.getWorkflowDocument().revokeAdHocRequests(adHocRevoke, "");
        }
        superUserForm.getAppSpecificRouteList().remove(removeIndex);

        superUserForm.getActionRequests().clear();
        initForm(request, form);
        return start(mapping, form, request, response);
    }

    private WorkflowDocumentActionsService getWorkflowDocumentActionsService(String documentId) {
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByDocumentId(documentId);
        String applicationId = documentType.getApplicationId();
        QName serviceName = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0,
                KewApiConstants.ServiceNames.WORKFLOW_DOCUMENT_ACTIONS_SERVICE_SOAP);
        WorkflowDocumentActionsService service = (WorkflowDocumentActionsService) KsbApiServiceLocator.getServiceBus()
                .getService(serviceName, applicationId);
        if (service == null) {
            service = KewApiServiceLocator.getWorkflowDocumentActionsService();
        }
        return service;
    }

    protected void validateAppSpecificRoute(AppSpecificRouteRecipient recipient) {
        if (recipient.getId() == null || recipient.getId().trim().equals("")) {
            GlobalVariables.getMessageMap().putError("appSpecificRouteRecipient" +
                    ((KewApiConstants.WORKGROUP.equals(recipient.getType())) ? "2" : "") + ".id",
                    "appspecificroute.recipient.required");
        } else {
            if (KewApiConstants.PERSON.equals(recipient.getType())) {
                Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(
                        recipient.getId());
                if (principal == null) {
                    LOG.error("App Specific user recipient not found");
                    GlobalVariables.getMessageMap().putError("appSpecificRouteRecipient.id",
                            "appspecificroute.user.invalid");
                }
            } else if (KewApiConstants.WORKGROUP.equals(recipient.getType())) {
                //if (getIdentityManagementService().getGroup(recipient.getId()) == null) {
                if (getGroupService().getGroupByNamespaceCodeAndName(recipient.getNamespaceCode(), recipient.getId()) == null) {
                    GlobalVariables.getMessageMap().putError("appSpecificRouteRecipient2.id",
                            "appspecificroute.workgroup.invalid");
                }
            }
        }
        if (GlobalVariables.getMessageMap().hasErrors()) {
            throw new ValidationException("AppSpecific Route validation Errors");
        }

    }

    protected String getAdHocRouteNodeName(String documentId) throws WorkflowException {
        WorkflowDocumentService workflowDocumentService = KewApiServiceLocator.getWorkflowDocumentService();
        List<RouteNodeInstance> nodeInstances = workflowDocumentService.getActiveRouteNodeInstances(documentId);
        if (nodeInstances == null || nodeInstances.isEmpty()) {
            nodeInstances = workflowDocumentService.getTerminalRouteNodeInstances(documentId);
        }
        if (nodeInstances == null || nodeInstances.isEmpty()) {
            throw new WorkflowException("Could not locate a node on the document to send the ad hoc request to.");
        }
        return nodeInstances.get(0).getName();
    }

    private GroupService getGroupService() {
        return KimApiServiceLocator.getGroupService();
    }

    public static UserSession getUserSession(HttpServletRequest request) {
        return GlobalVariables.getUserSession();
    }

}
